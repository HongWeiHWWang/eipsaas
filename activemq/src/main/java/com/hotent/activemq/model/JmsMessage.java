package com.hotent.activemq.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hotent.base.jms.JmsActor;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JMS消息
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
public class JmsMessage implements Serializable{
	private static final long serialVersionUID = 2196432461921825855L;
	private String templateId;
	private String type;						/*消息类型*/
	private String templateAlias;				/*模板别名*/
	private String subject;						/*主题*/
	private String smsTemplateNo; 				/*短信模板*/
	private String voiceTemplateNo; 			/*语音模板*/
	private String content;						/*内容*/
	private JmsActor sender;					/*发送人*/
	private List<JmsActor> receivers;			/*收信人*/
	private List<String> parms;					/*模板中${xxx}中的参数*/
	private Map<String, Object> extendVars = new HashMap<String, Object>();/*模板中${xxx}中的参数的值*/
	private String logId;						/*消息日志ID（方便重调）*/

	public JmsMessage() {
	}

	public JmsMessage(String content, JmsActor sender, List<JmsActor> receivers, String type){
		this.subject = "";
		this.content = content;
		this.sender = sender;
		this.receivers = receivers;
		this.type = type;
	}
	
	public JmsMessage(String subject, String content, JmsActor sender, List<JmsActor> receivers, String type){
		this.subject = subject;
		this.content = content;
		this.sender = sender;
		this.receivers = receivers;
		this.type = type;
	}
	
	public JmsMessage(String templateAlias,String subject, String content, JmsActor sender, List<JmsActor> receivers, String type){
		this.templateAlias = templateAlias;
		this.subject = subject;
		this.content = content;
		this.sender = sender;
		this.receivers = receivers;
		this.type = type;
	}
	

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTemplateAlias() {
		return templateAlias;
	}

	public void setTemplateAlias(String templateAlias) {
		this.templateAlias = templateAlias;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSmsTemplateNo() {
		return smsTemplateNo;
	}

	public void setSmsTemplateNo(String smsTemplateNo) {
		this.smsTemplateNo = smsTemplateNo;
	}

	public String getVoiceTemplateNo() {
		return voiceTemplateNo;
	}

	public void setVoiceTemplateNo(String voiceTemplateNo) {
		this.voiceTemplateNo = voiceTemplateNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public JmsActor getSender() {
		return sender;
	}

	public void setSender(JmsActor sender) {
		this.sender = sender;
	}

	public List<JmsActor> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<JmsActor> receivers) {
		this.receivers = receivers;
	}

	public List<String> getParms() {
		return parms;
	}

	public void setParms(List<String> parms) {
		this.parms = parms;
	}

	public Map<String, Object> getExtendVars() {
		return extendVars;
	}

	public void setExtendVars(Map<String, Object> extendVars) {
		this.extendVars = extendVars;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	@Override
	public String toString() {		
		return new ToStringBuilder(this)
				.append("type",getType())
				.append("templateAlias",templateAlias)
				.append("subject",subject)
				.append("content",content)
				.append("sender",sender)
				.append("receivers",receivers)
				.append("extendVars",extendVars)
				.toString();
	}
}
