package com.hotent.portal.jms.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.jms.JmsActor;
import com.hotent.base.jms.NoticeMessageType;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.jms.JmsHandler;
import com.hotent.portal.model.AlidayuSetting;
import com.hotent.portal.model.MsgTemplate;
import com.hotent.portal.persistence.manager.MessageLogManager;
import com.hotent.portal.persistence.manager.MsgTemplateManager;
import com.hotent.portal.util.TaoBaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 语音消息发送处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
@Service
public class VoiceHandler implements JmsHandler {
	private static final Logger logger = LoggerFactory.getLogger(VoiceHandler.class);
	@Autowired
	AlidayuSetting alidayuSetting;
	@Resource
	MessageLogManager messageLogManager;
    @Resource
    MsgTemplateManager msgTemplateManager;
	
	@Override
	public String getType() {
		return NoticeMessageType.VOICE.key();
	}
	@Override
	public boolean send(JmsMessage jmsMessage) {
		try {
			// 调用阿里大于
			List<JmsActor> recievers = jmsMessage.getReceivers();
			String content = jmsMessage.getContent();
			String templateCode = jmsMessage.getVoiceTemplateNo();
            if(StringUtil.isEmpty(templateCode) && StringUtil.isNotEmpty(jmsMessage.getTemplateAlias())){
                MsgTemplate msgTemplate = msgTemplateManager.getByKey(jmsMessage.getTemplateAlias());
                templateCode = msgTemplate.getSmsTemplateNo();
            }
			String calledShowNum = alidayuSetting.getCalledShowNum();
			if (StringUtil.isEmpty(content) ||  BeanUtils.isEmpty(recievers)) return false;
			//设置超时时间
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
			//初始化ascClient,暂时不支持多region（请勿修改）
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", alidayuSetting.getAppkey(), alidayuSetting.getSecret());
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", alidayuSetting.getUrl());
			IAcsClient acsClient = new DefaultAcsClient(profile);
			for (JmsActor user : recievers) {
                if(StringUtil.isEmpty(user.getMobile()))continue;
				SingleCallByTtsRequest request = new SingleCallByTtsRequest();
				request.setCalledShowNumber(calledShowNum);//被叫显号
				request.setCalledNumber(user.getMobile());//被叫号码
				request.setTtsCode(templateCode);//Tts模板ID
                jmsMessage.getExtendVars().remove("baseUrl");//因baseUrl参数值超过阿里大于参数值长度，所以去除；阿里大于参数值不能超过20个字符
                List<String> parms = new ArrayList<String>(jmsMessage.getExtendVars().keySet());  //短信模板中${xxx}中的参数
                jmsMessage.setParms(parms);
				String parmString = TaoBaoUtil.buildParams(jmsMessage);
				request.setTtsParam(parmString);//模板中存在变量时需要设置此值
				request.setVolume(100);//音量 取值范围 0--200
				request.setPlayTimes(3);//播放次数
				//请求失败这里会抛ClientException异常
				SingleCallByTtsResponse singleCallByTtsResponse = acsClient.getAcsResponse(request);
				if(singleCallByTtsResponse.getCode() != null && singleCallByTtsResponse.getCode().equals("OK")) {
					//请求成功
					logger.debug(String.format("语音文本外呼: RequestId=%s, Code=%s, Message=%s, CallId=%s", 
												singleCallByTtsResponse.getRequestId(),
												singleCallByTtsResponse.getCode(),
												singleCallByTtsResponse.getMessage(),
												singleCallByTtsResponse.getCallId()));
				}
            }
			messageLogManager.handLogByMsgHander(jmsMessage, true, "");
			return true;
		} catch (ClientException e) {
			logger.error(e.getMessage());
			messageLogManager.handLogByMsgHander(jmsMessage, false, e.getMessage());
			return false;
		}
	}
	
	@Override
	public String getTitle() {
		return "电话语音";
	}
	
	@Override
	public boolean getIsDefault() {
		return false;
	}
	
	@Override
	public boolean getSupportHtml() {
		return false;
	}
}
