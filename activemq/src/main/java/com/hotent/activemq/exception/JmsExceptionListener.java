package com.hotent.activemq.exception;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hotent.base.util.ExceptionUtil;

/**
 * JMS发送异常监控
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 *
 */
public class JmsExceptionListener implements ExceptionListener {
	protected Logger logger = LoggerFactory.getLogger(JmsExceptionListener.class);

	public void onException(JMSException ex) {
		ex.printStackTrace();
		String message= ExceptionUtil.getExceptionMessage(ex);
		logger.error(message);
	}
}
