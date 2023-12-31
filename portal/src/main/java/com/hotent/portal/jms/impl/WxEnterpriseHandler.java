package com.hotent.portal.jms.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.base.jms.JmsActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.jms.NoticeMessageType;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.portal.jms.JmsHandler;
import com.hotent.portal.model.MsgTemplate;
import com.hotent.portal.persistence.manager.MessageLogManager;
import com.hotent.portal.persistence.manager.MsgTemplateManager;
import com.hotent.portal.persistence.manager.SysMessageManager;
import com.hotent.portal.service.impl.TemplateServiceImpl;
import com.hotent.portal.util.WeiXinUtil;
import com.hotent.system.model.SysExternalUnite;
import com.hotent.system.model.msg.WXTemplateParam;
import com.hotent.system.model.msg.impl.WxTextCardMessage;
import com.hotent.system.persistence.manager.SysExternalUniteManager;

/**
 * 内部消息处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
@Service
public class WxEnterpriseHandler implements JmsHandler {
	private static final Logger logger = LoggerFactory.getLogger(WxEnterpriseHandler.class);
	@Resource
	SysMessageManager sysMessageManager;
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
		return NoticeMessageType.WXENTERPRISE.key();
	}

	@Override
	public boolean send(JmsMessage jmsMessage) {
		try {
			List<JmsActor> receivers = jmsMessage.getReceivers();
            String content = jmsMessage.getContent();
            content = content.replace("<p>","" );
            content = content.replace("</p>","" );
            jmsMessage.setContent(content);
            //根据消息模板的key获取模板对象
            MsgTemplate msgTemplate = msgTemplateManager.getByKey(jmsMessage.getTemplateAlias());
            if(BeanUtils.isNotEmpty(msgTemplate)){
            	jmsMessage.getExtendVars().put("templateType", msgTemplate.getTypeKey());
            }
            
            //发送微信公众号信息
            SysExternalUnite wechatOffAcc = sysExternalUniteManager.getWeChatOfficialAccounts();
            if(BeanUtils.isNotEmpty(wechatOffAcc)){
            	for(JmsActor receiver : receivers) {
            		if(StringUtil.isNotEmpty(receiver.getWeixin())) {
            			Map<String,Object> map = new HashMap<>();
            			String url = WeiXinUtil.renderOffAccUrl(jmsMessage);
            			
            			map.put("touser", receiver.getWeixin());
            			map.put("template_id", wechatOffAcc.getTempMsgId());
            			map.put("url", url);
            			Map<String, WXTemplateParam> params = new HashMap<>();
            			params.put("first", new WXTemplateParam(jmsMessage.getSubject(),"#173177"));
            			params.put("keyword1", new WXTemplateParam(jmsMessage.getExtendVars().get("bpmName").toString()));
            			params.put("keyword2", new WXTemplateParam(jmsMessage.getExtendVars().get("creator").toString()));
            			params.put("keyword3", new WXTemplateParam(jmsMessage.getExtendVars().get("date").toString()));
            			params.put("keyword4", new WXTemplateParam(jmsMessage.getExtendVars().get("instSubject").toString()));
            			params.put("remark", new WXTemplateParam("点击详情跳转。","#173177"));
            			map.put("data", params);
            			String msg = JsonUtil.toJson(map);
            			WeiXinUtil.sendWeiXinMessage(msg);
            		}
            	}
            }
            //发送企业微信信息
            SysExternalUnite wechatWork = sysExternalUniteManager.getWechatWork();
            if(BeanUtils.isNotEmpty(wechatWork)){
            	List<String> users = new ArrayList<String>();
	        	 for(JmsActor receiver : receivers) {
	         		users.add(receiver.getAccount());
	             }
//            	WxTextMessage wxMsg = new WxTextMessage(String.join("|", users),"@all",jmsMessage.getContent(),wechatWork.getAgentId());
            	String url = WeiXinUtil.renderMobileUrl(jmsMessage);
            	String description2 = templateServiceImpl.parsePlainContent(msgTemplate, jmsMessage.getExtendVars());
            	String description3 = "";
            	if(BeanUtils.isNotEmpty(jmsMessage.getExtendVars().getOrDefault("nodeName", null))){
            		description3 = "当前流程节点为："+jmsMessage.getExtendVars().getOrDefault("nodeName", null);
            	}
            	WxTextCardMessage wxMsg = new WxTextCardMessage(String.join("|", users),"@all", wechatWork.getAgentId(),
            				msgTemplate.getSubject(),DateUtil.getCurrentTime("yyyy年MM月dd日"),description2,description3,url,"详情");
            	WeiXinUtil.sendTextMessage(wxMsg);
            }
			messageLogManager.handLogByMsgHander(jmsMessage, true, "");
			return true;
		}
		catch(Exception ex) {
			logger.error(ex.getMessage());
			messageLogManager.handLogByMsgHander(jmsMessage, false, ex.getMessage());
			return false;
		}
	}
	
	@Override
	public String getTitle() {
		return "微信消息";
	}

	@Override
	public boolean getIsDefault() {
		return true;
	}

	@Override
	public boolean getSupportHtml() {
		return false;
	}
}