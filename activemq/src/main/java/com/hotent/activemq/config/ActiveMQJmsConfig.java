package com.hotent.activemq.config;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

/**
 * 提供Jms的ActiveMQ实现的配置类
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-14 15:30
 */
@EnableJms
@Configuration
@ConditionalOnProperty(value="jms.enable", matchIfMissing = true)
public class ActiveMQJmsConfig{
	@Value("${jms.queue.name:eipQueue}")
	private String queueName;
	@Value("${jms.topic.name:eipTopic}")
	private String topicName;

	@Resource
	JmsProperties jmsProperties;

	//消息对象队列
	@Bean("queue")
	public Queue getQueue() {
		return new ActiveMQQueue(queueName);
	}

	//消息广播
	@Bean("topic")
	public Topic getTopic() {
		return new ActiveMQTopic(topicName);
	}


	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ActiveMQConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
		bean.setPubSubDomain(jmsProperties.isPubSubDomain());
		bean.setConnectionFactory(connectionFactory);
		return bean;
	}

	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ActiveMQConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
		bean.setConnectionFactory(connectionFactory);
		return bean;
	}

}
