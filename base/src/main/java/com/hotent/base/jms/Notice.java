package com.hotent.base.jms;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 通知(通过模板来发送的消息)
 * <pre>
 * 在流程处理过程中，需要发送的通知类消息。 <br/>
 * 通知需要通过jms队列来发送，所以定义在base中依赖于jms接口。
 * </pre>
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年6月29日
 */
public class Notice implements Serializable{
	private static final long serialVersionUID = 1L;
	// 消息类型
	NoticeMessageType[] messageTypes;
	// 消息标题
	String subject;
	// 消息内容
	String content;
	// 发送者账号（默认为系统）
	String sender;
	// 收件人账号
	List<JmsActor> receiver;
	// 收件人账号
	String[] receivers;
	// 使用模板
	boolean useTemplate = false;
	// 模板类型 (当useTemplate为true时，通过解析模板来获得标题和内容)
	String templateType;
	// 模板key（设置了模板key时优先查找key对应的模板，只有未设置模板key时才通过模板类型查找默认模板）
	String templateKey;
	// 模板变量值
	Map<String, Object> vars;
	
	/**
	 * 获取消息类型
	 * @return	消息类型
	 */
	public NoticeMessageType[] getMessageTypes() {
		return messageTypes;
	}
	
	/**
	 * 设置消息类型
	 * @param messageTypes 消息类型
	 */
	public void setMessageTypes(NoticeMessageType[] messageTypes) {
		this.messageTypes = messageTypes;
	}
	
	/**
	 * 获取模板类型
	 * @return	模板类型
	 */
	public String getTemplateType() {
		return templateType;
	}
	
	/**
	 * 设置模板类型
	 * <pre>
	 * 设置了模板key时优先查找key对应的模板，只有未设置模板key时才通过模板类型查找默认模板
	 * </pre>
	 * @param templateType	模板类型
	 */
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	/**
	 * 获取模板key
	 * @return	模板key
	 */
	public String getTemplateKey() {
		return templateKey;
	}
	
	/**
	 * 设置模板key
	 * <pre>
	 * 设置了模板key时优先查找key对应的模板，只有未设置模板key时才通过模板类型查找默认模板
	 * </pre>
	 * @param templateKey	模板key
	 */
	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}
	/**
	 * 获取标题
	 * @return	标题
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * 设置标题
	 * @param subject	标题
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * 获取内容
	 * @return	内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置内容
	 * @param content	内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取发送者账号
	 * @return	发送者账号
	 */
	public String getSender() {
		return sender;
	}
	
	/**
	 * 设置发送者账号
	 * @param sender	发送者账号
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	/**
	 * 获取收件人账号
	 * @return	收件人账号
	 */
	public List<JmsActor> getReceiver() {
		return receiver;
	}
	
	/**
	 * 设置收件人账号
	 * @param receivers	收件人账号
	 */
	public void setReceiver(List<JmsActor> receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * 是否使用模板
	 * @return
	 */
	public boolean isUseTemplate() {
		return useTemplate;
	}
	
	/**
	 * 设置是否使用模板
	 * @param useTemplate
	 */
	public void setUseTemplate(boolean useTemplate) {
		this.useTemplate = useTemplate;
	}

	/**
	 * 获取模板中的变量值
	 * @return	模板中的变量值
	 */
	public Map<String, Object> getVars() {
		return vars;
	}
	
	/**
	 * 设置模板中的变量值
	 * @param map
	 */
	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}

	public String[] getReceivers() {
		return receivers;
	}

	public void setReceivers(String[] receivers) {
		this.receivers = receivers;
	}
}
