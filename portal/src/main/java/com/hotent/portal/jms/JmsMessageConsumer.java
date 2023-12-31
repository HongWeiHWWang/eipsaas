package com.hotent.portal.jms;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.jms.Notice;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.ExceptionUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.portal.persistence.manager.SysMessageManager;
import com.hotent.portal.service.TemplateService;
import com.hotent.sys.util.SysLogsUtil;

/**
 * <pre> 
 * 描述：消息消费者
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:23
 * </pre>
 */
public class JmsMessageConsumer {
	private static final Logger logger = LoggerFactory.getLogger(JmsMessageConsumer.class);

	private Map<String, JmsHandler> jmsHandlerMap = new HashMap<String, JmsHandler>();
	@Resource
	SysMessageManager sysMessageManager;
	@Resource
	TemplateService templateService;

	public void setJmsHandList(List<JmsHandler> jmsHandList){
		for(JmsHandler handler:jmsHandList){
			jmsHandlerMap.put(handler.getType(), handler);
		}
	}

	@JmsListener(destination = "${jms.queue.name:eipQueue}", containerFactory="jmsListenerContainerQueue")
	public void receiveQueue(Object model) {
		logger.debug("[JMS]: Queue message is :"+model.getClass().getName()+"---"+model);
		handlerJmsMessage(model);
	}

	private void handlerJmsMessage(Object source) {
		if(BeanUtils.isEmpty(source)) return;

		if(source instanceof ObjectMessage){
			ObjectMessage objectMessage = (ObjectMessage)source;
			try {
				Serializable object = objectMessage.getObject();
				if(BeanUtils.isEmpty(object)) return;
				if(object instanceof JmsMessage) {
					JmsMessage jmsMessage = (JmsMessage)object;
					JmsHandler jmsHandler = jmsHandlerMap.get(jmsMessage.getType());
					jmsHandler.send(jmsMessage);
				}
				if(object instanceof Notice) {
					templateService.sendNotice((Notice)object);
				}
			} catch (JMSException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
