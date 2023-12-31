package com.hotent.portal.jms.impl;

import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.jms.JmsActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.jms.NoticeMessageType;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.mail.model.MailLing;
import com.hotent.mail.model.MailSetting;
import com.hotent.mail.util.MailUtil;
import com.hotent.portal.jms.JmsHandler;
import com.hotent.portal.persistence.manager.MessageLogManager;

/**
 * 邮件消息处理器
 *
 * <pre>
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:23
 * </pre>
 */
@Service
public class MailHandler implements JmsHandler {
	private static final Logger logger = LoggerFactory.getLogger(MailHandler.class);
	@Resource
	MessageLogManager messageLogManager;
	
	@Value("${mail.username}")
	private String username;
	@Value("${mail.host}")
	private String sendHost;
	@Value("${mail.port}")
	private String sendPort;
	@Value("${mail.password}")
	private String password;

	@Override
	public String getType() {
		return NoticeMessageType.MAIL.key();
	}

	@Override
	public boolean send(JmsMessage jmsMessage) {
		try {
			List<JmsActor> recievers = jmsMessage.getReceivers();

			String strReceiver = getMailAddress(recievers);
			if(StringUtil.isEmpty(strReceiver)) return false;
			MailSetting mailSetting = new MailSetting();
			mailSetting.setSendHost(sendHost);
			mailSetting.setSendPort(sendPort);
			mailSetting.setMailAddress(username);
			mailSetting.setPassword(password);

			MailLing mail = new MailLing();
			mail.setTo(strReceiver);
			mail.setFrom(username);
			mail.setSubject(jmsMessage.getSubject());
			mail.setContent(jmsMessage.getContent());
			MailUtil mailUtil = new MailUtil(mailSetting);
			mailUtil.sendEmail(mail);
			messageLogManager.handLogByMsgHander(jmsMessage, true, "");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			messageLogManager.handLogByMsgHander(jmsMessage, false, e.getMessage());
			return false;
		}
	}

	private String getMailAddress(List<JmsActor> recievers){
		if(BeanUtils.isEmpty(recievers)) return "";
		StringBuilder toUserEmail = new StringBuilder();
		for(JmsActor iUser : recievers) {
			String email = iUser.getEmail();
			if (StringUtil.isEmpty(email))continue;
			toUserEmail.append(",");
			toUserEmail.append(email);
		}
		String result = toUserEmail.toString();
		result = result.replaceFirst(",", "");
		return result;
	}

	@Override
	public String getTitle() {
		return "邮件";
	}
	@Override
	public boolean getIsDefault() {
		return false;
	}
	@Override
	public boolean getSupportHtml() {
		return true;
	}
}
