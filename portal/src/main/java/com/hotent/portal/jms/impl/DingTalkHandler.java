package com.hotent.portal.jms.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.jms.JmsActor;
import com.hotent.base.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.ActionCard;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.Msg;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.jms.NoticeMessageType;
import com.hotent.base.util.BeanUtils;
import com.hotent.portal.jms.JmsHandler;
import com.hotent.portal.model.MsgTemplate;
import com.hotent.portal.persistence.manager.MessageLogManager;
import com.hotent.portal.persistence.manager.MsgTemplateManager;
import com.hotent.portal.service.impl.TemplateServiceImpl;
import com.hotent.portal.util.DingTalkUtil;
import com.hotent.system.consts.DingTalkConsts;
import com.hotent.system.model.SysExternalUnite;
import com.hotent.system.persistence.manager.SysExternalUniteManager;
import com.hotent.system.util.DingTalkTokenUtil;
import org.springframework.stereotype.Service;

/**
 * 阿里钉钉消息处理类
 * 
 * @author pangq
 *
 */
@Service
public class DingTalkHandler implements JmsHandler {
	private static final Logger logger = LoggerFactory.getLogger(DingTalkHandler.class);
	@Resource
	MessageLogManager messageLogManager;
	@Resource
	SysExternalUniteManager sysExternalUniteManager;
	@Resource
	MsgTemplateManager msgTemplateManager;
	@Resource
	TemplateServiceImpl templateServiceImpl;

	@Override
	public String getType() {
		return NoticeMessageType.DINGTALK.key();
	}

	@Override
	public String getTitle() {
		return "钉钉消息";
	}

	@Override
	public boolean getIsDefault() {
		return false;
	}

	@Override
	public boolean getSupportHtml() {
		return true;
	}

	@Override
	public boolean send(JmsMessage jmsMessage) {
		try {
			List<JmsActor> receivers = jmsMessage.getReceivers();
			String content = jmsMessage.getContent();
			content = content.replace("<p>", "");
			content = content.replace("</p>", "");
			jmsMessage.setContent(content);
			// 根据消息模板的key获取模板对象
			MsgTemplate msgTemplate = msgTemplateManager.getByKey(jmsMessage.getTemplateAlias());
			if (BeanUtils.isNotEmpty(msgTemplate)) {
				jmsMessage.getExtendVars().put("templateType", msgTemplate.getTypeKey());
			}
			// 发送钉钉信息
			SysExternalUnite dingtalkUnite = sysExternalUniteManager.getDingtalk();
			if (BeanUtils.isNotEmpty(dingtalkUnite)) {
				List<String> users = new ArrayList<String>();
				for (JmsActor receiver : receivers) {
					users.add(receiver.getAccount());
				}
				DingTalkClient client = new DefaultDingTalkClient(DingTalkConsts.getMsgSendUrl());
				OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
				req.setAgentId(Long.valueOf(dingtalkUnite.getAgentId()));
				req.setUseridList(String.join(",", users));
				
				Msg msg = new Msg();
				msg.setMsgtype("action_card");
				ActionCard actionCard = new ActionCard();
				actionCard.setTitle("eip审批");
				String cardContent = "# {0} \n {1}";
				cardContent = MessageFormat.format(cardContent,msgTemplate.getSubject(),
						templateServiceImpl.parsePlainContent(msgTemplate, jmsMessage.getExtendVars()));
				actionCard.setMarkdown(cardContent);
				actionCard.setSingleTitle("查看详情");
				
				String url = DingTalkUtil.renderMobileUrl(jmsMessage);
				actionCard.setSingleUrl(url);
				msg.setActionCard(actionCard);
				
				req.setMsg(msg);
				OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, DingTalkTokenUtil.getToken());
				//rsp: {"errcode":0,"task_id":211776512894,"request_id":"6ov1uwi1x0lg"}
				if(rsp.isSuccess()){
					messageLogManager.handLogByMsgHander(jmsMessage, true, JsonUtil.toJson(rsp));
				}else{
					messageLogManager.handLogByMsgHander(jmsMessage, false, JsonUtil.toJson(rsp));
					logger.error(JsonUtil.toJson(rsp));
					return false;
				}
			}
			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			messageLogManager.handLogByMsgHander(jmsMessage, false, ex.getMessage());
			return false;
		}
	}

}
