package com.hotent.portal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.base.jms.JmsActor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.exception.NotFoundException;
import com.hotent.base.exception.RequiredException;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.jms.JmsProducer;
import com.hotent.base.jms.Notice;
import com.hotent.base.jms.NoticeMessageType;
import com.hotent.base.model.CommonResult;
import com.hotent.base.template.impl.FreeMarkerEngine;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.ArrayUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.model.MsgTemplate;
import com.hotent.portal.persistence.manager.MsgTemplateManager;
import com.hotent.portal.service.TemplateService;

/**
 * <pre> 
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@Service
public class TemplateServiceImpl implements TemplateService {
	private Log logger = LogFactory.getLog(TemplateServiceImpl.class);
	@Resource
	FreeMarkerEngine freeMarkerEngine;
	@Resource
	MsgTemplateManager msgTemplateManager;
	@Resource
	JmsProducer jmsProducer;
	@Resource
	UCFeignService ucFeignService;

	@Override
	public void sendNotice2Jms(Notice notice) {
		jmsProducer.sendToQueue(notice);
	}

	@Override
	public void sendNotice(Notice notice) {
		if(BeanUtils.isEmpty(notice)) {
			throw new RequiredException("The args 'Notice' is required."); 
		}
		NoticeMessageType[] messageTypes = notice.getMessageTypes();
		if(messageTypes.length==0) {
			throw new RequiredException("The 'messageTypes' in 'Notice' is required."); 
		}
		List<JmsActor> receiver = notice.getReceiver();
		if(receiver.size()==0) {
			throw new RequiredException("The 'receivers' in 'Notice' is required."); 
		}
		JmsProducer jmsProducer = AppUtil.getBean(JmsProducer.class);
		String templateKey = notice.getTemplateKey();
		String templateType = notice.getTemplateType();
		if(notice.isUseTemplate() && StringUtil.isEmpty(templateKey) && StringUtil.isEmpty(templateType)) {
			throw new RequiredException("Nor the 'templateKey' and the 'templateType' is empty, so we can not find out the template to send with.");
		}
		
		JsonNode sender = null;
		String senderAccount = notice.getSender();
		if(StringUtil.isNotEmpty(senderAccount)) {
			sender = ucFeignService.loadUserByUsername(senderAccount);
		}
//		List<JsonNode> receiveUsers = new ArrayList<>();
//		JmsActor[] receiverAccounts = ArrayUtil.unique(receivers);
//		for(String account : receiverAccounts) {
//			JsonNode receiver = ucFeignService.loadUserByUsername(account);
//			if(BeanUtils.isEmpty(receiver)){
//				CommonResult<JsonNode> userById = ucFeignService.getUserById(account);
//				if(userById.getState()) {
//					receiver = userById.getValue();
//				}
//			}
//			if(BeanUtils.isNotEmpty(receiver)) {
//				receiveUsers.add(receiver);
//			}
//		}
//		if(receiveUsers.size()==0) {
//			throw new RequiredException("The accounts of 'receivers' in 'Notice' can not find the match user, so we did not send the message.");
//		}
		String subject = notice.getSubject();
		String content = notice.getContent();
		String plainContent = "";
		String templateAlias = "";
		
		if(notice.isUseTemplate()) {
			MsgTemplate templateVo = null;
			if(StringUtil.isNotEmpty(templateKey)) {
				templateVo = this.getTemplate(templateKey);
				if(BeanUtils.isEmpty(templateVo)) {
					throw new NotFoundException(String.format("The is no template key was '%s'.", templateKey));
				}
			}
			else {
				templateVo = this.getDefaultTemplate(templateType);
				if(BeanUtils.isEmpty(templateVo)) {
					throw new NotFoundException(String.format("The is no default template for type: '%s'.", templateType));
				}
			}
			Map<String, Object> vars = notice.getVars();
			subject = this.parseSubject(templateVo, vars);
			content = this.parseHtmlContent(templateVo, vars);
			plainContent = this.parsePlainContent(templateVo, vars);
			templateAlias = templateVo.getKey();
		}
		
//		List<JmsActor> receives = new ArrayList<>();
//		for(JsonNode user : receiveUsers) {
//			receives.add(convertUserObject2JmsActor(user));
//		}
		
		for(NoticeMessageType type : messageTypes) {
			JmsMessage jmsMessage = null;
			if(type.isPlain() && notice.isUseTemplate()) {
				jmsMessage = new JmsMessage(templateAlias,subject, plainContent, convertUserObject2JmsActor(sender), receiver, type.key());
			}
			else {
				jmsMessage = new JmsMessage(templateAlias,subject, content, convertUserObject2JmsActor(sender), receiver, type.key());
			}
			jmsMessage.setExtendVars(notice.getVars());
			jmsProducer.sendToQueue(jmsMessage);
		}
	}
	
	public MsgTemplate getTemplate(String templateKey) {
		MsgTemplate msgTemplate = msgTemplateManager.getByKey(templateKey);
		return msgTemplate;
	}

	public MsgTemplate getDefaultTemplate(String typeKey) {
		MsgTemplate msgTemplate = msgTemplateManager.getDefault(typeKey);
		if(msgTemplate==null)
			throw new RuntimeException("There is not a default msgTemplate in table.");
		return msgTemplate;
	}

	public String parseSubject(MsgTemplate templateVo,Map<String,Object> vars) {
		String subject = "";
		try{
			subject = freeMarkerEngine.parseByTemplate(templateVo.getSubject(),vars);
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return subject;
	}

	public String parsePlainContent(MsgTemplate templateVo,Map<String,Object> vars) {
		String content = "";
		try{
			content = freeMarkerEngine.parseByTemplate(templateVo.getPlain(),vars);
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return content;
	}
	
	/**
	 * 转换IUser对象为JmsActor
	 * @param user
	 * @return
	 */
	public JmsActor convertUserObject2JmsActor(JsonNode jsonNode) {
		if(BeanUtils.isEmpty(jsonNode) || !jsonNode.isObject()) return null;
		ObjectNode userNode = (ObjectNode)jsonNode;
		String userId = JsonUtil.getString(userNode, "userId");
		String account = JsonUtil.getString(userNode, "account");
		String fullname = JsonUtil.getString(userNode, "fullname");
		String email = JsonUtil.getString(userNode, "email");
		String mobile = JsonUtil.getString(userNode, "mobile");
        String weixin = JsonUtil.getString(userNode, "weixin");
		return new JmsActor(userId, account, fullname, email, mobile,weixin);
	}

	public String parseHtmlContent(MsgTemplate templateVo,Map<String,Object> vars) {
		String content = "";
		try{
			content = freeMarkerEngine.parseByTemplate(templateVo.getHtml(),vars);
		}catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		return content;
	}

}
