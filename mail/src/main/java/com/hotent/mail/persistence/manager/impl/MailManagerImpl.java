package com.hotent.mail.persistence.manager.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.file.util.AppFileUtil;
import com.hotent.mail.api.AttacheHandler;
import com.hotent.mail.model.Mail;
import com.hotent.mail.model.MailAttachment;
import com.hotent.mail.model.MailSetting;
import com.hotent.mail.persistence.dao.MailDao;
import com.hotent.mail.persistence.manager.MailAttachmentManager;
import com.hotent.mail.persistence.manager.MailManager;
import com.hotent.mail.persistence.manager.MailSettingManager;
import com.hotent.mail.util.ExchangeMailUtil;
import com.hotent.mail.util.MailUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.FileAttachment;

/**
 * 外部邮件 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月6日
 */
@Service("mailManager")
public class MailManagerImpl extends BaseManagerImpl<MailDao, Mail> implements MailManager{
	
	protected static Logger logger = LoggerFactory.getLogger(MailUtil.class);
	static short MAIL_NO_READ=0;//未读
	static short MAIL_IS_READ=1;//已读
	static Integer MAIL_IS_RECEIVE = 1;// 收件箱
	static Integer MAIL_IS_SEND = 2;// 发件箱
	static Integer MAIL_IS_DRAFT = 3;// 草稿箱
	static Integer MAIL_IS_DELETE = 4;// 垃圾箱
	
	@Resource
	MailSettingManager mailSettingManager;
	@Resource
	MailSettingManagerImpl mailSettingService;
	@Resource
	MailAttachmentManager mailAttachmentManager;
	@Resource
	FileManager fileManager;
	@Resource
	IUserService ius;

	@Override
	public void addDump(String[] lAryId) {
		for(String l:lAryId){
			Mail mail = this.get(l);
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("type", MAIL_IS_DELETE);
			params.put("id", mail.getId());
			baseMapper.updateTypes(params);
		}
	}

	@Override
	public void emailRead(Mail mail) throws NoSuchProviderException, MessagingException {
		if(Mail.Mail_IsNotRead.shortValue() == mail.getIsRead().shortValue()
				&& Mail.Mail_InBox.shortValue() != mail.getType().shortValue()) return;
		mail.setIsRead(Mail.Mail_IsRead);
		baseMapper.updateById(mail);
	}

	@Override
	public List<Mail> getMailListBySetting(MailSetting mailSetting) throws Exception {
		// 邮箱类型
		String mailType = mailSetting.getMailType();
		// 上一封邮件ID
		String lastMessageId = mailSetting.getLastMessageId();
		// 上一封邮件收件时间
		LocalDateTime lastReceiveTime = mailSetting.getLastReceiveTime();
		
		List<Mail> list = null;
		String userId = mailSetting.getUserId();
		String userFullname = ius.getUserById(userId).getFullname();
		
		AttacheHandler handler = new AttacheHandler() {
			@Override
			public Boolean isDownlad(String UID) {
				return true;
			}
			
			@Override
			public void handle(Part part, Mail mail) {
				try {
					String content = mail.getContent();
					if(StringUtil.isNotEmpty(content)) {
						mail.setContent(Base64.getBase64(content));
					}
					saveAttach(part, mail, mailSetting, userId, userFullname);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void handle(AttachmentCollection attachments, Mail mail) {
				try {
					saveExchangeAttach(attachments, mail, mailSetting, userId, userFullname);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		if(MailSetting.EXCHANGE_MAIL_TYPE.equals(mailType)) {
			String password = mailSetting.getPassword();
			ExchangeMailUtil mailUtil = new ExchangeMailUtil(mailSetting.getSmtpPort(), mailSetting.getMailAddress(), EncryptUtil.decrypt(password));
			list = mailUtil.receive(handler, lastReceiveTime);
		}
		else {
			String password = mailSetting.getPassword();
			mailSetting.setPassword(EncryptUtil.decrypt(password));
			MailUtil mailUtil = new MailUtil(mailSetting);
			list = mailUtil.receive(handler, lastMessageId);
		}
		if(list.size() > 0) {
			Mail mail = list.get(0);
			String messageId = mail.getMessageId();
			LocalDateTime sendDate = mail.getSendDate();
			// 更新上一封邮件标记
			mailSettingManager.updateLastEnvelop(mailSetting.getId(), messageId, sendDate);
		}
		return list;
	}
	
	private DefaultFile getDefaultFile(Long byteCount, String fileName, String mailAccount, String userId, String userFullname) {
		Calendar cal=Calendar.getInstance();//使用日历类
    	int year=cal.get(Calendar.YEAR);//得到年
    	int month=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
    	String fileExt = FileUtil.getFileExt(fileName);
		String filePath = "emailAttachs" + File.separator + mailAccount + File.separator + year + File.separator + month + File.separator
						  + UniqueIdUtil.getUId() + "." + fileExt;
		
		DefaultFile file = new DefaultFile();
		file.setByteCount(byteCount);
		file.setFileType("mail");
		file.setFileName(fileName.replace("." + fileExt, ""));
		file.setExtensionName(fileExt);
		file.setFilePath(filePath);
		file.setCreateTime(LocalDateTime.now());
		if(StringUtil.isNotEmpty(userId)) {
			file.setCreateBy(userId);
		}
		if(StringUtil.isNotEmpty(userFullname)) {
			file.setCreatorName(userFullname);
		}
		return file;
	}
	
	private void saveExchangeAttach(AttachmentCollection attachments, Mail mail, MailSetting mailSetting, String userId, String userFullname) throws Exception{
		List<microsoft.exchange.webservices.data.property.complex.Attachment> items = attachments.getItems();
		String mailAccount = mailSetting.getMailAddress();
		Boolean isHandleAttach = mailSetting.getIsHandleAttach();
		for(microsoft.exchange.webservices.data.property.complex.Attachment attach : items) {
			if(attach instanceof FileAttachment) {
				FileAttachment fileAttach = (FileAttachment)attach;
				String filename = fileAttach.getName();
				String fileId = "";
				
				if(isHandleAttach) {
			    	fileAttach.load();
			    	int size = fileAttach.getSize();
			    	byte[] content = fileAttach.getContent();
			    	InputStream sbs = new ByteArrayInputStream(content); 
			    	DefaultFile file = getDefaultFile((long)size, filename, mailAccount, userId, userFullname);
			    	fileManager.uploadFile(file, sbs);
			    	fileId = file.getId();
				}
				mail.getMailAttachments().add(new MailAttachment(filename, fileId));
			}
		}
	}
	
	/**
     * 将邮件中的附件保存在本地指定目录下
     * @param message
     * @param mail
     * @return
     */
    private void saveAttach(Part message, Mail mail, MailSetting mailSetting, String userId, String userFullname)throws Exception{
    	String mailAccount = mailSetting.getMailAddress();
//    	String filename=MimeUtility.decodeText(message.getFileName());
    	String filename = message.getFileName();
    	String fileId = "";
    	int size = message.getSize();
		DefaultFile file = getDefaultFile((long)size, filename, mailAccount, userId, userFullname);
		fileManager.uploadFile(file, message.getInputStream());
		fileId = file.getId();
		mail.getMailAttachments().add(new MailAttachment(filename, fileId));
    }
	
	@Override
	public void saveMail(List<Mail> list, String setId, String currentUserId) throws Exception {
		List<Mail> existMails = baseMapper.getAll(Wrappers.<Mail>lambdaQuery().in(Mail::getSetId,Arrays.asList(setId.split(","))));
		Map<String,Boolean> mailIDMap = new HashMap<String, Boolean>(); 
		for (Mail mail : existMails) {
			mailIDMap.put(mail.getMessageId(), true);
		}
		
		for(Mail mail:list){
			
			// 解决邮件重复问题
			if( !BeanUtils.isEmpty(mailIDMap.get(mail.getMessageId())) ) continue;
			
			Mail bean = getOutMail(mail, setId, currentUserId);
			// 主键
			String mailId = UniqueIdUtil.getSuid();
			bean.setId(mailId);
			// 邮件标识
			bean.setMessageId(mail.getUID());
			baseMapper.insert(bean);
			logger.info("已下载邮件"+bean.getSubject());
			List<MailAttachment> attachments = mail.getMailAttachments();
			if(BeanUtils.isEmpty(attachments)) continue ;
			MailAttachment mailAttachment ;
			for(MailAttachment attachment:attachments){
				String fileName = attachment.getFileName();
				String filePath = attachment.getFilePath();
				String ext = FileUtil.getFileExt(fileName);
				String fileId = StringUtil.isNotEmpty(filePath)?new String(new File(filePath).getName().replace("."+ext, "")):UniqueIdUtil.getSuid();
				mailAttachment = new MailAttachment();
				mailAttachment.setId(fileId);
				mailAttachment.setFileName(attachment.getFileName());
				mailAttachment.setFilePath(filePath);
				mailAttachment.setMailId(mailId);
				mailAttachmentManager.create(mailAttachment);
			}
		}
	}

	/**
	 * 获得Mail实体
	 * @param mail  邮件
	 * @param setId setId
	 * @return		返回邮箱
	 */
	private Mail getOutMail(Mail mail, String setId, String userId) {
		Mail bean =new Mail();
		LocalDateTime sentDate = null;
		if (mail.getSendDate() != null) {
			sentDate = mail.getSendDate();
		} else {
			sentDate = LocalDateTime.now();
		}
		//邮件发送时间
		bean.setSendDate(sentDate);
		bean.setSetId(setId);
		bean.setSubject(mail.getSubject());
		bean.setContent(mail.getContent());
		//发件人
		bean.setSenderAddress(mail.getSenderAddress());
		bean.setSenderName(mail.getSenderName());
		//接受者
		bean.setReceiverAddresses(mail.getReceiverAddresses());
		bean.setReceiverName(mail.getReceiverName());
		//暗送者
		bean.setBcCAddresses(mail.getBcCAddresses());
		bean.setBccName(mail.getBccName());
		//抄送者
		bean.setCopyToAddresses(mail.getCopyToAddresses());
		bean.setCopyToName(mail.getCopyToName());
		bean.setType(Mail.Mail_InBox);
		bean.setIsRead(Mail.Mail_IsNotRead);
		bean.setUserId(userId);
		return bean;
	}

	@Override
	public List<MailSetting> getMailTreeData(String userId) throws Exception {
		List<MailSetting> list= mailSettingManager.getMailByUserId(userId);
		List<MailSetting> temp=new ArrayList<MailSetting>();
		MailSetting omus=null;
		for(MailSetting beanTemp:list){
			beanTemp.setParentId("0");
			String id=beanTemp.getId();
			temp.add(beanTemp);
		    for(int i=0;i<4;i++){
		    	omus=new MailSetting();
		    	if(i==0){ 
		    		omus.setNickName("收件箱("+getCount(id,MAIL_IS_RECEIVE)+")");
			    	omus.setTypes(MAIL_IS_RECEIVE);
		    	}else if(i==1){
		    		omus.setNickName("发件箱("+getCount(id,MAIL_IS_SEND)+")");
			    	omus.setTypes(MAIL_IS_SEND);
		    	}else if(i==2){
		    		omus.setNickName("草稿箱("+getCount(id,MAIL_IS_DRAFT)+")");
			    	omus.setTypes(MAIL_IS_DRAFT);
		    	}else {
		    		omus.setNickName("垃圾箱("+getCount(id,MAIL_IS_DELETE)+")");
			    	omus.setTypes(MAIL_IS_DELETE);
			    }
				omus.setId(UniqueIdUtil.getSuid());
				omus.setParentId(beanTemp.getId());
			    temp.add(omus);
		    }
		}
		return temp;
	}

	/**
	 * 获取邮箱的分类邮件数
	 * @param id
	 * @param type
	 * @return
	 */
	private int getCount(String id, int type) {
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("type", type);
		params.put("setId", id);
		return baseMapper.getFolderCount(params);
	}

	@Override
	public List<Mail> getFolderList(QueryFilter queryFilter) {
		return baseMapper.getFolderList(queryFilter.getParams());
	}

	@Override
	public List<Mail> getDefaultMailList(QueryFilter queryFilter) {
		return baseMapper.getFolderList(queryFilter.getParams());
	}

	@Override
	public String sendMail(Mail outMail, String userId, String mailId, int isReply, String context, String basePath)throws Exception {
		String content=outMail.getContent();
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("type", 2);
		params.put("id", mailId);		
		if( "0".equals(mailId)||isReply==1){
			outMail.setId(UniqueIdUtil.getSuid());
			create(outMail);
		}else{
			baseMapper.updateTypes(params);
		}
		outMail.setContent(content);
		getMailAttachments(outMail, basePath);
		String mailsetId = outMail.getSetId();
		MailSetting mailSetting = mailSettingManager.get(mailsetId);
		MailUtil m = new MailUtil(mailSetting);
		m.send(outMail);
		return outMail.getId();
	}

	/**
	 * 获取邮件附件
	 * @param outMail
	 * @param basePath
	 * @throws Exception 
	 */
	private Mail getMailAttachments(Mail outMail, String basePath) throws Exception {
		if(BeanUtils.isNotEmpty(outMail)&&StringUtil.isNotEmpty(outMail.getFileIds())){
			String fileIds=outMail.getFileIds().replaceAll("quot;", "\"");
			JsonNode jsonArray = JsonUtil.toJsonNode(fileIds);
			if(jsonArray.size()>0){
				DefaultFile sysFile = null ;
				List<MailAttachment> attachments = outMail.getMailAttachments();
				for(Object obj:jsonArray){
					ObjectNode json = (ObjectNode)obj;
					sysFile= fileManager.get(json.get("id").textValue());
					String filePath = sysFile.getFilePath();
					String fileName = sysFile.getFileName()+"."+sysFile.getExtensionName();
					if(StringUtil.isEmpty(filePath)){
						MailAttachment mailAttachment = new MailAttachment(fileName,sysFile.getBytes());
						mailAttachment.setId(json.get("id").textValue());
						attachments.add(mailAttachment);
						continue;
					}
					if(StringUtil.isEmpty(basePath)){
						//路径从配置文件中获取
						basePath= AppFileUtil.getBasePath();
					}
					filePath = basePath+File.separator+filePath;
					MailAttachment mailAttachment = new MailAttachment(fileName,filePath);
					mailAttachment.setId(json.get("id").textValue());
					attachments.add(mailAttachment);
				}
			}
		}
		return outMail;
	}

	@Override
	public Mail getMailReply(String mailId) {
		Mail outMail= get(mailId);
		outMail.setIsReply(Mail.Mail_IsReplay);
		outMail.setSubject("回复:" + outMail.getSubject());
		return outMail;
	}

	@Override
	public void delBySetId(String setId) {
		baseMapper.delBySetId(setId);
	}

	@Override
	public String mailAttachementFilePath(MailAttachment entity) throws Exception {
		Mail outMail = get(entity.getId());
		String setId = outMail.getSetId(); 
		final String emailId = outMail.getMessageId();
		MailSetting outMailSetting= mailSettingManager.get(setId);
		outMailSetting.setIsHandleAttach(true);
		MailUtil mailUtil = new MailUtil(outMailSetting);
		String userId = outMailSetting.getUserId();
		String userFullname = ius.getUserById(userId).getFullname();
		List<Mail> list = mailUtil.receive(new AttacheHandler() {
			@Override
			public Boolean isDownlad(String UID) {
				if(StringUtil.isEmpty(UID)) return false;
				return UID.equals(emailId);
			}
			
			@Override
			public void handle(Part part, Mail mail) {
				try {
					saveAttach(part, mail, outMailSetting, userId, userFullname);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void handle(AttachmentCollection attachments, Mail mail) {
			}
		}, emailId);
		if(BeanUtils.isEmpty(list)) throw new Exception("找不到该邮件，可能邮件已被删除！");
		String mailId = outMail.getId();
		String attachFileName = entity.getFileName();
		String resultPath = "";
		Mail mail = list.get(0);
		List<MailAttachment> attachments = mail.getMailAttachments();
		for(MailAttachment attachment:attachments){
			String fileName = attachment.getFileName();
			String filePath = attachment.getFilePath();
			if(fileName.equals(attachFileName)) resultPath = filePath;
			mailAttachmentManager.updateFilePath(fileName, mailId, filePath);
		}
		return resultPath;
}

	@Override
	public String getNameByEmail(String email) {
		String linkName = "陌生人";
		try {
			List<IUser> users = ius.getByEmail(email);
	    	if(BeanUtils.isNotEmpty(users)){
	    		linkName = users.get(0).getFullname();
	    	}
		} catch (Exception e) {}
    	return linkName; 
	}

	@Override
	public void sendExchangeMail(MailSetting mailSetting, Mail mail) throws Exception {
		ExchangeMailUtil eu = new ExchangeMailUtil(mailSetting.getSmtpPort(), mailSetting.getMailAddress(), EncryptUtil.decrypt(mailSetting.getPassword()));
		if( "0".equals(mail.getId())||mail.getIsReply()==1){
			mail.setId(UniqueIdUtil.getSuid());
			create(mail);
		}else{
			Map<String, Object> params = new HashMap<>();
			params.put("mailId", mail.getId());
			params.put("types", 2);
			baseMapper.updateTypes(params);
		}
		if(mail.getFileIds()!=null){
			String basePath = AppFileUtil.getBasePath();
			getMailAttachments(mail, basePath);
			eu.send(mail.getSubject(), new String[]{mail.getReceiverAddresses()}, mail.getCopyToAddresses()==null?null:new String[]{mail.getCopyToAddresses()},mail.getBcCAddresses()==null?null:new String[]{mail.getBcCAddresses()}, mail.getContent(), new String[]{mail.getFileIds()});
		}else{
			eu.send(mail.getSubject(), new String[]{mail.getReceiverAddresses()}, mail.getCopyToAddresses()==null?null:new String[]{mail.getCopyToAddresses()},mail.getBcCAddresses()==null?null:new String[]{mail.getBcCAddresses()}, mail.getContent());
		}
	}

	@Override
	public void isRead(String id) {
		baseMapper.isRead(id);
	}

	
}