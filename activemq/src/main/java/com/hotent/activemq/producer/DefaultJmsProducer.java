package com.hotent.activemq.producer;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import com.hotent.base.jms.JmsProducer;


/**
 * jms生产者的activeMQ实现
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 */
@Primary
@Service
@ConditionalOnProperty(value="jms.enable", matchIfMissing = true)
public class DefaultJmsProducer implements JmsProducer{
    private static final Log logger= LogFactory.getLog(DefaultJmsProducer.class);
	@Resource
	private Queue queue;
	@Resource
	private Topic topic;
	@Resource
    JmsMessagingTemplate jmsMessagingTemplate;
	
	QueueSession session = null;
	
	public void sendToQueue(Object object) {
		jmsMessagingTemplate.convertAndSend(queue, object);
		logger.debug("[JMS]: Send to queue.");
	}

	@Override
	public void sendToTopic(Object object) {
		jmsMessagingTemplate.convertAndSend(topic, object);
		logger.debug("[JMS]: send to topic.");
	}

	@Override
	public void sendToQueue(Object object, String queueName) {
        Queue bnsQueue = getQueue(queueName);
        jmsMessagingTemplate.convertAndSend(bnsQueue, object);
		logger.debug("[JMS]: Send to queue："+queueName);
	}

	@Override
	public void sendToTopic(Object object, String topicName) {
		Topic bnsTopic = getTopic(topicName);
		jmsMessagingTemplate.convertAndSend(bnsTopic, object);
		logger.debug("[JMS]: send to topic："+topicName);
	}
	
	private Queue getQueue(String queueName){
		Queue bnsQueue = new ActiveMQQueue(queueName);
		return bnsQueue;
	}
	
	private Topic getTopic(String topicName){
		Topic bnsTopic = new ActiveMQTopic(topicName);
		return bnsTopic;
	}
}
