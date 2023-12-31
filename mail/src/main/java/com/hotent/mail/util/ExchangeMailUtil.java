package com.hotent.mail.util;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.attachment.Attachment;
import com.hotent.base.attachment.AttachmentService;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.file.model.DefaultFile;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.file.util.AppFileUtil;
import com.hotent.mail.api.AttacheHandler;
import com.hotent.mail.model.Mail;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.SortDirection;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;



/**
 * Exchange邮件服务工具类
 *
 * @author Yang Cheng
 * @date 2017-02-13
 */
public class ExchangeMailUtil {

	private AttachmentService attachmentService;
	private String mailServer;
	private String user;
	private String password;
	private String domain;
	/**
	 * 附件处理接口
	 */
	private AttacheHandler handler;

	public ExchangeMailUtil(String mailServer, String user, String password) {
		this.mailServer = "https://"+mailServer+"/EWS/exchange.asmx";
		this.user = user;
		this.password = password;
	}

	public ExchangeMailUtil(String mailServer, String user, String password, String domain) {
		this.mailServer = "https://"+mailServer+"/EWS/exchange.asmx";
		this.user = user;
		this.password = password;
		this.domain = domain;
	}

	/**
	 * 创建邮件服务
	 *
	 * @return 邮件服务
	 */
	private ExchangeService getExchangeService() {
		try (ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);){
			//用户认证信息
			ExchangeCredentials credentials;
			if (domain == null) {
				credentials = new WebCredentials(user, password);
			} else {
				credentials = new WebCredentials(user, password, domain);
			}
			service.setCredentials(credentials);
			service.setUrl(new URI(mailServer));
			return service;
		} catch (Exception e) {}
		return null;
	}

	/**
	 * 收取邮件
	 *
	 * @param max          最大收取邮件数
	 * @param searchFilter 收取邮件过滤规则
	 * @return
	 * @throws Exception
	 */
	public List<Mail> receive(int max, SearchFilter searchFilter) throws Exception {
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials(user, password);
		service.setCredentials(credentials);
		service.setUrl(new URI(mailServer));
		//绑定收件箱,同样可以绑定发件箱
		Folder inbox = Folder.bind(service, WellKnownFolderName.Inbox);
		//获取文件总数量
		int count = inbox.getTotalCount();
		if (max > 0) {
			count = count > max ? max : count;
		}
		//循环获取邮箱邮件
		ItemView view = new ItemView(count);
		
		//按照时间顺序收取
		view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
		FindItemsResults<Item> findResults;
		if (searchFilter == null) {
			findResults = service.findItems(inbox.getId(), view);
		} else {
			findResults = service.findItems(inbox.getId(), searchFilter, view);
		}
		int size = findResults.getItems().size();
		List<Mail> result = new ArrayList<>();
		if(size == 0) {
			return result;
		}
		service.loadPropertiesForItems(findResults, PropertySet.FirstClassProperties);
		for (Item item : findResults.getItems()) {
			EmailMessage message = (EmailMessage) item;
			Mail mail = this.getMail(message);
			result.add(mail);
		}
		return result;
	}

	/**
	 * 收取所有邮件
	 *
	 * @throws Exception
	 */
	public List<Mail> receive(int max) throws Exception {
		return receive(max, null);
	}

	/**
	 * 收取邮件
	 *
	 * @throws Exception
	 */
	public List<Mail> receive() throws Exception {
		return receive(0, null);
	}
	
	/**
	 * 收取指定时间以后的邮件
	 * @param date	指定时间
	 * @return
	 * @throws Exception
	 */
	public List<Mail> receive(AttacheHandler handler, LocalDateTime date) throws Exception{
		this.handler = handler;
		SearchFilter searchFilter = null;
		if(date!=null) {
			Date t = DateFormatUtil.parse(date);
			searchFilter = new SearchFilter.IsGreaterThan(EmailMessageSchema.DateTimeReceived, t);
		}
		return receive(Integer.MAX_VALUE, searchFilter);
	}

	/**
	 * 发送带附件的mail
	 *
	 * @param subject         邮件标题
	 * @param to              收件人列表
	 * @param cc              抄送人列表
	 * @param bodyText        邮件内容
	 * @param attachmentPaths 附件地址列表
	 * @throws Exception
	 */
	public void send(String subject, String[] to, String[] cc, String[] as,String bodyText, String[] attachmentPaths)throws Exception {
		ExchangeService service = getExchangeService();
		EmailMessage msg = new EmailMessage(service);
		msg.setSubject(subject);
		MessageBody body = MessageBody.getMessageBodyFromText(bodyText);
		body.setBodyType(BodyType.HTML);
		msg.setBody(body);
		for (String toPerson : to) {
			msg.getToRecipients().add(toPerson);
		}
		if (BeanUtils.isNotEmpty(cc)) {
			for (String ccPerson : cc) {
				if(StringUtil.isNotEmpty(ccPerson)) {
					msg.getCcRecipients().add(ccPerson);
				}
			}
		}
		if (BeanUtils.isNotEmpty(as)) {
			for (String asPerson : as) {
				if(StringUtil.isNotEmpty(asPerson)) {
					msg.getCcRecipients().add(asPerson);
				}
			}
		}
		if (BeanUtils.isNotEmpty(attachmentPaths)) {
			FileManager fileManager = AppUtil.getBean(FileManager.class);
			for (String attachmentPath : attachmentPaths) {
				if(StringUtil.isNotEmpty(attachmentPath)) {
					ArrayNode attachmentObjs = (ArrayNode) JsonUtil.toJsonNode(attachmentPath);
					for (JsonNode jsonNode : attachmentObjs) {
						DefaultFile sysFile = fileManager.get(jsonNode.get("id").asText());
						if(BeanUtils.isNotEmpty(sysFile)){
							String fileName = sysFile.getFileName()+"."+sysFile.getExtensionName();
							if(Attachment.SAVE_TYPE_FOLDER.equals(sysFile.getStoreType())){
								String filePath = AppFileUtil.getAttachPath()+File.separator+sysFile.getFilePath();
								File file = new File(filePath);
								if(file.exists()){
									InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
									byte[] fileBlob = FileUtil.readByte(inputStream);
									msg.getAttachments().addFileAttachment(fileName,fileBlob);
								}
							}else if(Attachment.SAVE_TYPE_DTABASE.equals(sysFile.getStoreType())){
								byte[] fileBlob = sysFile.getBytes();
								msg.getAttachments().addFileAttachment(fileName,fileBlob);
							}else if(Attachment.SAVE_TYPE_FTP.equals(sysFile.getStoreType())){
								 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
								 attachmentService.download(sysFile, outStream);
								 ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
								 msg.getAttachments().addFileAttachment(fileName,inStream);
							}
						}
					}
				}
			}
		}
		msg.send();
	}

	/**
	 * 发送不带附件的mail
	 *
	 * @param subject  邮件标题
	 * @param to       收件人列表
	 * @param cc       抄送人列表
	 * @param bodyText 邮件内容
	 * @throws Exception
	 */
	public void send(String subject, String[] to, String[] cc, String[] as,String bodyText) throws Exception {
		send(subject, to, cc,as, bodyText, null);
	}

	/**
	 * 根据EmailMessage获得Mail实体
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 * @see	MimeMessage
	 */
	public Mail getMail(EmailMessage message) throws Exception {
		Mail mail = new Mail();
		LocalDateTime sentDate = null;
		if (message.getDateTimeSent() != null) {
			Instant instant = message.getDateTimeSent().toInstant();
			ZoneId zone = ZoneId.systemDefault();
			sentDate = LocalDateTime.ofInstant(instant, zone);
		} else {
			sentDate = LocalDateTime.now();
		}
		// 邮件发送时间
		mail.setSendDate(sentDate);
		String subject = message.getSubject();
		if(subject!=null){
			mail.setSubject(subject);
		}else {
			mail.setSubject("无主题");
		}
		
		if(message.getHasAttachments()) {
			AttachmentCollection attachments = message.getAttachments();
			if(handler!=null) {
				handler.handle(attachments, mail);
			}
		}
		
		// 取得邮件内容
		String content = message.getBody().toString();
		
		mail.setContent(Base64.getBase64(content));
		// 发件人
		mail.setSenderAddress(message.getFrom().getAddress());
		mail.setSenderName(message.getFrom().getName());
		// 接受者
		mail.setReceiverAddresses(message.getReceivedBy().getAddress());
		mail.setReceiverName(message.getReceivedBy().getName());
		// 暗送者
		mail = getBccAddressesAndNames(message, mail);
		// 抄送者
		mail = getCcAddressesAndNames(message, mail);
		return mail;
	}

	/**
	 * 获取密送人员存储到mail实体
	 * @param to
	 * @param message
	 * @return
	 * @throws Exception 
	 */
	public Mail getBccAddressesAndNames(EmailMessage message,Mail mail) throws Exception {
		List<EmailAddress> items = message.getBccRecipients().getItems();
		StringBuffer bccAddresses = new StringBuffer();
		StringBuffer bccNames = new StringBuffer();
		if(items.size()>0){
			for (int i = 0; i < items.size(); i++) {
				if(i==items.size()-1){
					bccAddresses.append(items.get(i).getAddress());
					bccNames.append(items.get(i).getName());
				}else{
					bccAddresses.append(items.get(i).getAddress()).append(",");
					bccNames.append(items.get(i).getName()).append(",");
				}
			}	
		}
		mail.setBcCAddresses(bccAddresses.toString());
		mail.setBccName(bccNames.toString());
		return mail;
	}

	/**
	 * 获取抄送人员存储到mail实体
	 * @param to
	 * @param message
	 * @return
	 * @throws Exception 
	 */
	public Mail getCcAddressesAndNames(EmailMessage message,Mail mail) throws Exception {
		List<EmailAddress> items = message.getCcRecipients().getItems();
		StringBuffer ccAddresses = new StringBuffer();
		StringBuffer ccNames = new StringBuffer();
		if(items.size()>0){
			for (int i = 0; i < items.size(); i++) {
				if(i==items.size()-1){
					ccAddresses.append(items.get(i).getAddress());
					ccNames.append(items.get(i).getName());
				}else{
					ccAddresses.append(items.get(i).getAddress()).append(",");
					ccNames.append(items.get(i).getName()).append(",");
				}
			}	
		}
		mail.setCopyToAddresses(ccAddresses.toString());
		mail.setCopyToName(ccNames.toString());
		return mail;
	}
}