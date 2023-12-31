package com.hotent.activemq.jms.produce;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.annotation.Resource;
import javax.jms.JMSException;

import com.hotent.base.jms.JmsActor;
import org.junit.Test;
import org.springframework.jms.annotation.JmsListener;

import com.hotent.activemq.jms.JmsTestCase;
import com.hotent.base.jms.JmsProducer;
/**
 * @author jason
 * @company 广州宏天软件股份有限公司
 * @email liygui@jee-soft.cn
 * @date 2020-04-13 09:05
 */
public class JmsProducerTest extends JmsTestCase {
    @Resource
    JmsProducer jmsProducer;
    
    @Test
    public void sendQueue() {
    	JmsActor jmsActor = new JmsActor(idGenerator.getSuid(), "jason", "黎扬贵", "", "", "");
    	jmsProducer.sendToQueue(jmsActor,"testSendQueue");
    }
    
    @JmsListener(destination = "testSendQueue", containerFactory="jmsListenerContainerQueue")
    public void receiveQueue(JmsActor model) throws JMSException {
		assertEquals("jason", model.getAccount());
	}
    
    @Test
    public void sendTopic() {

    	JmsActor jmsActor = new JmsActor(idGenerator.getSuid(), "jason", "黎扬贵", "", "", "");
    	jmsProducer.sendToTopic(jmsActor,"testSendTopic");
    
    }
    
    /**
     * topic 有重复消费问题 
     * 可以通过唯一标识判断是否重复消息，已经消费过的不需要再进行业务处理
     * @param model
     * @throws JMSException
     */
    @JmsListener(destination = "testSendTopic", containerFactory="jmsListenerContainerTopic")
    public void receiveTopic(JmsActor model) throws JMSException {
    	System.out.println(model.toString());
		assertEquals("jason", model.getAccount());
	}
    
    @JmsListener(destination = "testSendTopic", containerFactory="jmsListenerContainerTopic")
    public void receiveTopic2(JmsActor model) throws JMSException {
    	System.out.println(model.toString());
		assertEquals("jason", model.getAccount());
	}
  
    
  
    
}
