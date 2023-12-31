package com.hotent.base.jms;

/**
 * jms生产者
 * <pre>
 * 队列消息的发送者
 * </pre>
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年6月29日
 */
public interface JmsProducer {
	/**
	 * 发送消息到队列中
	 * @param object	消息
	 */
	void sendToQueue(Object object);
	
	/**
	 * 发送消息到指定队列中
	 * @param object 消息
	 * @param queueName 队列名
	 */
	void sendToQueue(Object object,String queueName);
	/**
	 * 发送消息到广播中
	 * <pre>
	 * 注意广播的消息在本系统中不一定会订阅，可能由其他消费者来订阅。
	 * </pre>
	 * @param object	消息
	 */
	void sendToTopic(Object object);
	
	/**
	 * 发送消息到指定广播中
	 * @param object
	 * @param topicName
	 */
	void sendToTopic(Object object, String topicName);
}