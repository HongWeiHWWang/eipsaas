package com.hotent.portal.persistence.manager;

import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.manager.BaseManager;
import com.hotent.portal.model.MessageLog;

/**
 * 
 * <pre> 
 * 描述：portal_message_log 处理接口
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2019-05-30 21:34:36
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface MessageLogManager extends BaseManager<MessageLog>{
	/**
	 * 重新调用接口
	 * @param id
	 */
	void reinvoke(String id) throws Exception;
	
	/**
	 * 标记为成功
	 * @param id
	 */
	void signSuccess(String id);
	
	/**
	 * 处理消息日志
	 * @param jmsMessage
	 * @param state
	 * @param errorMsg
	 */
	void handLogByMsgHander(JmsMessage jmsMessage,boolean state,String errorMsg);
}
