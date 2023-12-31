package com.hotent.mail.persistenc.util;


import org.junit.Test;

import com.hotent.base.util.UniqueIdUtil;
import com.hotent.mail.model.Mail;
import com.hotent.mail.model.MailSetting;
import com.hotent.mail.util.MailUtil;

/**
 * 邮件处理类测试
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月12日
 */
public class MailUtilTest{
	
	@Test
	public void connectTest() throws Exception{
		MailSetting ms = new MailSetting();
		String popHost = "pop.qq.com";
		String popPort = "995";
		String mailAddress = "1686469292@qq.com";
		String smtpHost = "smtp.qq.com";
		String smtpPort = "465";
		String password = "ezgyzvokgflnebhe";
		String uid = "3";
		String userId = "4";
		String protocal = MailSetting.POP3_PROTOCAL;
		ms.setSSL(true);
		ms.setPassword(password);
		ms.setSmtpHost(smtpHost);
		ms.setSmtpPort(smtpPort);
		ms.setPopPort(popPort);
		ms.setPopHost(popHost);
		ms.setSendHost(smtpHost);
		ms.setSendPort(smtpPort);
		ms.setReceiveHost(popHost);
		ms.setReceivePort(popPort);
		ms.setMailAddress(mailAddress);
		ms.setUserId(userId);
		ms.setId(uid);
		ms.setProtocal(protocal);
		ms.setMailType("pop3");
		MailUtil m = new MailUtil(ms);
		m.connectSmtpAndReceiver();
	}
	
	//@Test
	public void testMailUtil() throws Exception{
		MailSetting ms = new MailSetting();
		String popHost = "pop.163.com";
		String popPort = "110";
		String mailAddress = "mww_a_yjj@163.com";
		String smtpHost = "smtp.163.com";
		String smtpPort = "25";
		String password = "mww123456";
		String uid = "3";
		String userId = "4";
		String protocal = MailSetting.POP3_PROTOCAL;
		ms.setPassword(password);
		ms.setSmtpHost(smtpHost);
		ms.setSmtpPort(smtpPort);
		ms.setPopPort(popPort);
		ms.setPopHost(popHost);
		ms.setSendHost(smtpHost);
		ms.setSendPort(smtpPort);
		ms.setReceiveHost(popHost);
		ms.setReceivePort(popPort);
		ms.setMailAddress(mailAddress);
		ms.setUserId(userId);
		ms.setId(uid);
		ms.setProtocal(protocal);
		
		Mail mail = new Mail();
		String subject = "我的朋友";
		String content = "韩梅梅生日快乐";
		String receiveAddress = "1353669924@qq.com";
		String messageID = UniqueIdUtil.getSuid();
		mail.setId(messageID);
		mail.setReceiverAddresses(receiveAddress);
		mail.setSenderAddress(mailAddress);
		mail.setSubject(subject);
		mail.setContent(content);
		
		MailUtil m = new MailUtil(ms);
		//发送邮件
		m.send(mail);
		
		//测试发送邮件服务器连接情况
		m.connectSmtp();
		
		//接收邮件
//		AttacheHandler handler = AttacheHandler();
//		m.receive(handler);
		
		//测试发送邮件服务器和接收邮件服务器连接情况
		m.connectSmtpAndReceiver();
		
//		//通过messageID，下载邮件
//		m.getByMessageID(handler, messageID);
	}
}
