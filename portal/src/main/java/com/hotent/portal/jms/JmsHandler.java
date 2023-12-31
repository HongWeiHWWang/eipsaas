package com.hotent.portal.jms;

import com.hotent.activemq.model.JmsMessage;

/**
 * 发送消息处理接口
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
public interface JmsHandler{
	/**
	 * 消息类型
	 * @return
	 */
	String getType();
	/**
	 * 标题
	 * @return String
	 */
	String getTitle();
	/**
	 * 是否默认处理器
	 * @return   boolean
	 */
	boolean getIsDefault();
	/**
	 * 是否支持html
	 * @return   boolean
	 */
	boolean getSupportHtml();
	/**
	 * 处理jms消息，如发送、持久化等操作。
	 * @param 	jms消息
	 * @return  boolean
	 */
	boolean send(JmsMessage jmsMessage);
}
