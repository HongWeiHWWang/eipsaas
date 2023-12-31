package com.hotent.mail.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.file.persistence.manager.FileManager;
import com.hotent.file.util.AppFileUtil;
import com.hotent.mail.model.Mail;
import com.hotent.mail.model.MailAttachment;
import com.hotent.mail.model.MailLinkman;
import com.hotent.mail.model.MailSetting;
import com.hotent.mail.persistence.manager.MailAttachmentManager;
import com.hotent.mail.persistence.manager.MailLinkmanManager;
import com.hotent.mail.persistence.manager.MailManager;
import com.hotent.mail.persistence.manager.MailSettingManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



/**
 * 外部邮件 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月8日
 */

@RestController
@RequestMapping("/mail/mail/mail/v1/")
@Api(tags="外部邮件")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
@SuppressWarnings("rawtypes")
public class MailController extends BaseController<MailManager, Mail>{
	@Resource
	MailManager mailManager;
	@Resource
	MailSettingManager mailSettingManager;
	@Resource
	MailAttachmentManager mailAttachmentManager;
	@Resource
	MailLinkmanManager mailLinkmanManager;
	@Resource
	FileManager fileManager;
	Properties properties;
	@Resource
	IUserService is;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取外部邮件列表(分页条件查询)数据", httpMethod = "POST", notes = "获取外部邮件列表(分页条件查询)数据")
	public PageList<Mail> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		String userId= ContextUtil.getCurrentUserId();
		List<QueryField> query=queryFilter.getQuerys();
		for (QueryField queryField : query) {
			System.out.println(queryField.getProperty());
			if("subject".equals(queryField.getProperty())){
				queryField.setGroup("mail");
			}else if("senderAddress".equals(queryField.getProperty())){
				queryField.setGroup("mail");
			}
		}
		QueryField qf=new QueryField();
		qf.setGroup("mainplus");
		qf.setOperation(QueryOP.EQUAL);
		qf.setProperty("userId");
		qf.setRelation(FieldRelation.AND);
		qf.setValue(userId);
		query.add(qf);
		PageList<Mail> pageList=mailManager.query(queryFilter);
		List<Mail> list=pageList.getRows();
		for(Mail m:list) {
			MailLinkman mailman=mailLinkmanManager.findLinkMan(m.getSenderAddress(), userId);
			if(mailman!=null) {
				m.setIsExitedMan("0");
			}else {
				m.setIsExitedMan("1");
			}
		}
		return pageList;
	}
	
	
	@RequestMapping(value="mailList", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得邮件分页列表", httpMethod = "GET", notes = "取得邮件分页列表")
	public CommonResult<List<Mail>> mailList(@ApiParam(name="id",value="默认邮箱id", required = true)@RequestParam String mailSetId,
								 @ApiParam(name="types",value="类型", required = true)@RequestParam String type,
								 @ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter
	, HttpServletResponse response) throws Exception {
		IUser user = ContextUtil.getCurrentUser();
		
		String userId = user.getUserId();
		MailSetting defaultMail= mailSettingManager.getByIsDefault(userId);
		if(BeanUtils.isEmpty(defaultMail)){
			return new CommonResult<>(false, "无默认邮箱！");
		}

		queryFilter.addFilter("userId", userId, QueryOP.EQUAL);
		queryFilter.addFilter("setId", mailSetId, QueryOP.EQUAL);
		queryFilter.addFilter("type", type, QueryOP.EQUAL);
		List<Mail> list=mailManager.getFolderList(queryFilter);
		return new CommonResult<>(true, "", list);
	}
	
	
	@RequestMapping(value="mailSettingList", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得邮件配置列表", httpMethod = "GET", notes = "取得邮件配置列表")
	public MailSetting mailList(@ApiParam(name="mailSetId",value="默认邮箱id", required = true)@RequestParam String mailSetId) throws Exception {
		MailSetting setting = mailSettingManager.get(mailSetId);
		return setting;
	}
	
	
	@RequestMapping(value="sync", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "邮箱同步处理", httpMethod = "POST", notes = "邮箱同步处理")
	public CommonResult<String> executeJob(@ApiParam(name="id", value="邮箱id", required = true)@RequestParam String id) throws Exception {
		MailSetting mailSetting= mailSettingManager.get(id);
		String userId = ContextUtil.getCurrentUserId();
		List<Mail> mailList = new ArrayList<>();
		try {
			//读取邮件
			mailList = mailManager.getMailListBySetting(mailSetting);
			//保存邮件
			mailManager.saveMail(mailList, id, userId);
			return new CommonResult<>(true, "同步邮件成功", null);
		} catch (Exception e) {
			String message = null;
			String str = ThreadMsgUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				message = "同步邮件失败:" + str;			
			} else {
				message = "同步邮件失败，请检查邮箱设置是否正确！";
				e.printStackTrace();
			}
			return new CommonResult<>(false, message, null);
		}
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取外部邮件明细页面", httpMethod = "GET", notes = "获取外部邮件明细页面")
	public @ResponseBody Mail getJson(@ApiParam(name="id", value="邮箱id", required = true)@RequestParam String id) throws Exception {
		if(StringUtil.isEmpty(id)){
			return new Mail();
		}
		Mail mail = mailManager.get(id);
		int type=mail.getType();
		if(type== Mail.Mail_InBox){
			mailManager.emailRead(mail);
		}
		List<MailAttachment> attachments = mailAttachmentManager.getByMailId(mail.getId());
		if(type==Mail.Mail_OutBox || type==Mail.Mail_DraftBox){
			attachments = mailAttachmentManager.getByOutMailFileIds(mail.getFileIds());
		}else {
			attachments = mailAttachmentManager.getByMailId(mail.getId());
		}
		mail.setMailAttachments(attachments);
		return mail;
	}
	
	@RequestMapping(value="remove", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除外部邮件记录", httpMethod = "POST", notes = "批量删除外部邮件记录")
	public CommonResult<String> remove(@ApiParam(name="id", value="邮箱id", required = true)@RequestParam String ids,
							   		   @ApiParam(name="types", value="邮箱类型", required = true)@RequestParam String type
	) throws Exception {
		String[] lAryId = StringUtil.getStringAryByStr(ids);
		String message = null;
		try{
			if(Integer.parseInt(type)==4){
				mailManager.removeByIds(Arrays.asList(lAryId));
				message="成功删除本地上邮件!";
			}else if(Integer.parseInt(type)==3||Integer.parseInt(type)==2){//直接删除本地草稿箱/发件箱中的邮件
				mailManager.removeByIds(Arrays.asList(lAryId));
				message="成功删除本地上邮件!";
			}else{//将收件箱与发件箱邮件移至垃圾箱
				mailManager.addDump(lAryId);
				message="成功将邮件移至垃圾箱";
			}
			return new CommonResult<>(true, message, null);
		}catch(Exception ex){
			return new CommonResult<>(false, "删除失败:" + ex.getMessage(), null);
	  }
	}
	
	@RequestMapping(value="warn", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "无邮件时的提示信息", httpMethod = "POST", notes = "无邮件时的提示信息")
	public int warn() throws Exception {
		String userId = ContextUtil.getCurrentUserId();
		int count=mailSettingManager.getCountByUserId(userId);
		return count;
	}
	
	@RequestMapping(value="reply", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "回复邮件", httpMethod = "POST", notes = "回复邮件")
	public @ResponseBody Mail reply(@ApiParam(name="mailId", value="邮箱id", required = true)@RequestParam String mailId						
	) throws Exception {
		Mail mail=mailManager.getMailReply(mailId);
		return mail;
		
	}
	
	@RequestMapping(value="getMailSetting", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取邮箱配置", httpMethod = "POST", notes = "获取邮箱配置")
	public MailSetting getMailSetting(   @ApiParam(name="mailSetId", value="邮箱配置id", required = true)@RequestParam String mailSetId){
		MailSetting mailUserSeting= mailSettingManager.get(mailSetId);
		return mailUserSeting;
	}
	
	
	@RequestMapping(value="get", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得邮件明细", httpMethod = "GET", notes = "取得邮件明细")
	public List<MailAttachment> get(@ApiParam(name="mailId", value="邮箱id", required = true)@RequestParam String mailId,
							  		@ApiParam(name="mailSetId", value="邮箱配置id", required = true)@RequestParam String mailSetId
	) throws Exception {
		Mail mail = mailManager.get(mailId);
		int type=mail.getType();
		List<MailAttachment> attachments = mailAttachmentManager.getByMailId(mailId);
		if(type== Mail.Mail_InBox){
			mailManager.emailRead(mail);
		}
		
		if(type==Mail.Mail_OutBox || type==Mail.Mail_DraftBox){
			attachments = mailAttachmentManager.getByOutMailFileIds(mail.getFileIds());
		}else {
			attachments = mailAttachmentManager.getByMailId(mailId);
		}
		return attachments;
	}
	
	@RequestMapping(value="getMail", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得邮件", httpMethod = "GET", notes = "取得邮件")
	public Mail getMailList(@ApiParam(name="mailId", value="邮箱id", required = true)@RequestParam String mailId){
		Mail mail=null;
		if(StringUtil.isNotZeroEmpty(mailId)){
			mail=new Mail();
		}else{
			mail= mailManager.get(mailId);
		}
		return mail;
	}
	
	@RequestMapping(value="mailEdit", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "邮件编辑", httpMethod = "GET", notes = "邮件编辑")
	public List<MailSetting> edit(@ApiParam(name="mailId", value="邮箱id", required = true)@RequestParam String mailId,
								  @ApiParam(name="returnUrl", value="返回地址")@RequestParam String returnUrl
	) throws Exception {
		String userId = ContextUtil.getCurrentUserId();
		List<MailSetting> list=mailSettingManager.getMailByUserId(userId);
		return list;
	}
	
	@RequestMapping(value="getMailTreeData", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获得邮箱树形列表的json数据", httpMethod = "GET", notes = "获得邮箱树形列表的json数据")
	public List<MailSetting> getMailTreeData() throws Exception {
		String userId= ContextUtil.getCurrentUserId();
		List<MailSetting> list= mailManager.getMailTreeData(userId);
		List<MailSetting> json = BeanUtils.listToTree(list);
		return json;
	}

	
	@RequestMapping(value="getRecieveServerTypeData", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取邮件接收服务器类型的json数据", httpMethod = "POST", notes = "获取邮件接收服务器类型的json数据")
	public String getRecieveServerType(@ApiParam(name="mailSetId", value="邮箱配置id", required = true)@RequestParam String mailSetId) throws Exception {
		MailSetting mailSetting= mailSettingManager.get(mailSetId);
		String type= mailSetting.getMailType();
		return type;
	}
		
	@RequestMapping(value="send", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "添加或更新邮件", httpMethod = "POST", notes = "添加或更新邮件")
	public CommonResult<Set<String>> save(@ApiParam(name="mail",value="邮件")@RequestBody Mail mail,HttpServletRequest request) throws Exception {
		int type= mail.getType();
		String userId = ContextUtil.getCurrentUserId();
		MailSetting mailSetting=mailSettingManager.getMailByAddress(mail.getSenderAddress());
		mail.setSendDate(LocalDateTime.now());
		mail.setIsReply(mail.getIsReply());
		mail.setUserId(userId);
		mail.setSenderName(mailSetting.getNickName());
		mail.setSetId(mailSetting.getId());
		String context=request.getContextPath();
		String basePath=AppFileUtil.getBasePath();
		String msg = "邮件发送";
		try{
			//获取邮件地址
			Set<String> list= getMailAddress(mail);
			//发送邮件
			if(type==2){
				if(MailSetting.EXCHANGE_MAIL_TYPE.equals(mailSetting.getMailType())){
					mailManager.sendExchangeMail(mailSetting,mail);
					mail.setIsRead((short)0);
					mail.setContent(com.hotent.base.util.Base64.getBase64(mail.getContent()));
					Mail notnull= mailManager.get(mail.getId());
					if(notnull!=null){
						mailManager.update(mail);
					}else{
						mailManager.create(mail);
					}
					//handLinkMan(userId, list ,mail.getId());
				}else{
					mailManager.sendMail(mail,userId,mail.getId(),mail.getIsReply(),context,basePath);
					mail.setContent(com.hotent.base.util.Base64.getBase64(mail.getContent()));
					mail.setIsRead((short)0);
					Mail notnull= mailManager.get(mail.getId());
					if(notnull!=null){
						mailManager.update(mail);
					}else{
						mailManager.create(mail);
					}
					//handLinkMan(userId, list,mail.getId());
				}
				return new CommonResult<>(true, "邮件发送成功");
			}
			//草稿
			else{
				if(StringUtil.isZeroEmpty(mail.getId())){
					mail.setId(UniqueIdUtil.getSuid());
					//添加发出邮件
					mail.setContent(com.hotent.base.util.Base64.getBase64(mail.getContent()));
					mail.setIsRead((short)0);
					mailManager.create(mail);
					msg = "邮件保存";
				}else{
					mailManager.update(mail);
					msg = "邮件更新";
				}
				Set<String> set = checkAddress(list);
				return new CommonResult<>(true, msg+"成功", set);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<>(false, msg+"失败!", null);
		}
	}
	
	
	/**
	 * 处理联系人
	 * @param userId
	 * @param list
	 * @throws Exception 
	 * void
	 */
	@SuppressWarnings("unused")
	private void handLinkMan(String userId, Set<String> list,String mailId) throws Exception{
		MailLinkman man =null;
		//向最近联系人中增加记录或更新记录
		for(String address:list){
			man = mailLinkmanManager.findLinkMan(address, userId);
			String linkName = mailManager.getNameByEmail(address);
			if(man!=null){//更新
				man.setSendTimes(man.getSendTimes()+1);
				man.setMailId(mailId);
				man.setLinkName(linkName);
				man.setLinkAddress(address);
				LocalDateTime date = LocalDateTime.now();
				man.setSendTime(date);
				mailLinkmanManager.update(man);
			}else{//添加
				man=new MailLinkman();
				man.setSendTimes((long)1);
				man.setUserId(userId);
				man.setMailId(mailId);
				man.setLinkName(linkName);
				man.setSendTime(LocalDateTime.now());
				man.setLinkAddress(address);
				man.setId(UniqueIdUtil.getSuid());
				mailLinkmanManager.create(man);
			}
		}
	}
	
	
	private Set<String> getMailAddress(Mail mail){
		String toAddess=mail.getReceiverAddresses();
		String ccAddress=mail.getCopyToAddresses();
		String bccAddress=mail.getBcCAddresses();
		List<String> list=new ArrayList<String>();
		addAddress(toAddess,list);
		addAddress(ccAddress,list);
		addAddress(bccAddress,list);
		Set<String> set = new HashSet<>(list);
		return set;
	}
	
	
	private void addAddress(String address,List<String> list){
		if(StringUtil.isEmpty(address)) return;
		address=StringUtil.trim(address, ",");
		String[] aryAddress=address.split(",");
		for(String addr:aryAddress){
			list.add(addr);
		}
	}
	
	/**
	 * 检查地址是否存在于联系人列表
	 * @param set
	 * @return 
	 * Set&lt;String>
	 */
	private Set<String> checkAddress(Set<String> set){
		Set<String> rtnset = new HashSet<String>();
		for(String addr:set){
			//判断地址是否存在于联系人列表当中
			List<IUser> list = is.getByEmail(addr);
			if(BeanUtils.isNotEmpty(list)) continue;
			rtnset.add(addr);
		}
		return rtnset;
	}
			
	@SuppressWarnings("unchecked")
	@RequestMapping(value="getToReadMailList", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取未读邮件列表", httpMethod = "GET", notes = "获取未读邮件列表")
	public PageList<Mail> getToReadMailList() throws Exception {
		String userId= ContextUtil.getCurrentUserId();
		QueryFilter queryFilter = QueryFilter.build().withPage(new PageBean(1, 15));
		queryFilter.addFilter("USER_ID_", userId, QueryOP.EQUAL);
		queryFilter.addFilter("TYPE_", 1, QueryOP.EQUAL);
		queryFilter.addFilter("IS_READ_", 0, QueryOP.EQUAL);
		PageList<Mail> mailList= mailManager.query(queryFilter);
		return mailList;
	}
	
	@RequestMapping(value="isRead", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "邮件变成已读", httpMethod = "GET", notes = "邮件变成已读")
	public void isRead(@ApiParam(name="id", value="id", required = true)@RequestParam String id) {
		mailManager.isRead(id);
	}
	
}
	
