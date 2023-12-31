package com.hotent.portal.jms.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
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
 * 短信消息发送处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
@Service
public class SmsHandler implements JmsHandler {
	private static final Logger logger = LoggerFactory.getLogger(SmsHandler.class);
	@Resource
	MessageLogManager messageLogManager;
	@Autowired
	AlidayuSetting alidayuSetting;
    @Resource
    MsgTemplateManager msgTemplateManager;
	
	@Override
	public String getType() {
		return NoticeMessageType.SMS.key();
	}
	
	@Override
	public boolean send(JmsMessage jmsMessage) {
		try {
			// 调用阿里大于
			List<JmsActor> recievers = jmsMessage.getReceivers();
			String content = jmsMessage.getContent();
			String templateCode = jmsMessage.getSmsTemplateNo();
            if(StringUtil.isEmpty(templateCode) && StringUtil.isNotEmpty(jmsMessage.getTemplateAlias())){
                MsgTemplate msgTemplate = msgTemplateManager.getByKey(jmsMessage.getTemplateAlias());
                templateCode = msgTemplate.getSmsTemplateNo();
            }
			if (StringUtil.isEmpty(content) || BeanUtils.isEmpty(recievers)) return false;
			//设置超时时间-可自行调整
			System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
			System.setProperty("sun.net.client.defaultReadTimeout", "10000");
			//初始化ascClient,暂时不支持多region（请勿修改）
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", alidayuSetting.getAppkey(), alidayuSetting.getSecret());
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", alidayuSetting.getUrl());
			IAcsClient acsClient = new DefaultAcsClient(profile);
			for (JmsActor user : recievers) {
				if (StringUtil.isEmpty(user.getMobile())) continue;
				//组装请求对象
				SendSmsRequest req = new SendSmsRequest();
				//使用post提交
				req.setMethod(MethodType.POST);
				req.setSmsUpExtendCode(alidayuSetting.getExtend());//上行短信扩展码
				req.setSignName(alidayuSetting.getFreeSignName());//短信签名
                jmsMessage.getExtendVars().remove("baseUrl");//因baseUrl参数值超过阿里大于参数值长度，所以去除；阿里大于参数值不能超过20个字符
                List<String> parms = new ArrayList<String>(jmsMessage.getExtendVars().keySet());  //短信模板中${xxx}中的参数
                jmsMessage.setParms(parms);
				String parmString = TaoBaoUtil.buildParams(jmsMessage);
				req.setTemplateParam(parmString);//模板中的变量
				req.setPhoneNumbers(user.getMobile());//待发送手机号
                if(StringUtil.isEmpty(templateCode)){
                    templateCode = "SMS_0000";
                }
				req.setTemplateCode(templateCode);//短信模板
				//请求失败这里会抛ClientException异常
				SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(req);
				logger.debug("发送短信：" + sendSmsResponse.getMessage());
			}
			messageLogManager.handLogByMsgHander(jmsMessage, true, "");
			return true;
		}catch(ClientException e){
			logger.error(e.getMessage());
			messageLogManager.handLogByMsgHander(jmsMessage, false, e.getMessage());
			return false;
		}
	}
	@Override
	public String getTitle() {
		return "短信";
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
