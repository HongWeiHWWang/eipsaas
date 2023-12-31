package com.hotent.base.jms.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hotent.base.jms.JmsProducer;

/**
 * Jms生产者的空实现类
 * <pre>
 *	空实现不提供任何实现代码，仅仅保证其他类注入JmsProducer接口的不报错
 * </pre>
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年6月29日
 */
@Service
public class JmsProducerEmptyImpl implements JmsProducer{
	private static final Log logger= LogFactory.getLog(JmsProducerEmptyImpl.class);
	private final String WARN_MESSAGE = "[JMS]: There is no implements of JmsProducer, so we can not send the message out.";
	
	@Override
	public void sendToQueue(Object object) {
		logger.warn(WARN_MESSAGE);
	}
	
	@Override
	public void sendToQueue(Object object, String queueName) {
		logger.warn(WARN_MESSAGE);
	}

	@Override
	public void sendToTopic(Object object) {
		logger.warn(WARN_MESSAGE);
	}

	@Override
	public void sendToTopic(Object object, String topicName) {
		logger.warn(WARN_MESSAGE);
	}
}