package com.hotent.im.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * MQTT初始化监听器
 * <pre>
 * 作者：dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class MqttInitListener implements ApplicationListener<ContextRefreshedEvent> {


	@Override
	public void onApplicationEvent(ContextRefreshedEvent ev) {
		ApplicationContext context = ev.getApplicationContext();
		
		MqttReceiveService mqttReceiveService = (MqttReceiveService) context.getBean("mqttReceiveService");
		MqttSendService mqttSendService = (MqttSendService) context.getBean("mqttSendService");

		mqttReceiveService.connect();
		mqttSendService.connect();
	}
}
