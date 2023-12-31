package com.hotent.im.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hotent.base.util.BeanUtils;
import com.hotent.im.config.MqttProperty;
import com.hotent.im.util.ImConstant;

@Service
public class MqttSendService {
	@Resource
	MqttProperty mqttProperty;
	
	private MqttClient mqttClient = null;
	
	public MqttClient connect() {
		if(BeanUtils.isNotEmpty(mqttClient) && mqttClient.isConnected()){
			return mqttClient;
		}
		MqttConnectOptions options = new MqttConnectOptions();
		options.setConnectionTimeout(10);
		options.setKeepAliveInterval(20);
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		try {
			Assert.notNull(mqttProperty, "未获取到MqttProperty的实现类");
			mqttClient = new MqttClient(mqttProperty.getServerUrl(), "defaultSendClient"+System.currentTimeMillis(), new MemoryPersistence());  
			mqttClient.setCallback(new MqttCallback() {        
                @Override
                public void messageArrived(String topicName, MqttMessage message) throws Exception {
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                	 // publish后会执行到这里  
                    System.out.println("deliveryComplete---------"+ token.isComplete());  
                }
                @Override
                public void connectionLost(Throwable cause) {
                	System.out.println(cause.getMessage()+",connect lost");
                }
            });
			mqttClient.connect(options);
			mqttClient.subscribe(ImConstant.MQTT_SEND_DESTINATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mqttClient;
	}
	
	/**
	 * 发送消息
	 * @param topic 
	 * @return
	 * @throws MqttException 
	 * @throws MqttPersistenceException 
	 * @throws UnsupportedEncodingException 
	 */
	public Map<String,Object> send(String str, String topic) throws MqttPersistenceException, MqttException, UnsupportedEncodingException{
		Map<String,Object> map = new HashMap<String,Object>();
		MqttMessage message = new MqttMessage();
		message.setPayload(str.getBytes("utf-8"));
		this.mqttClient.publish(topic, message);
		return map;
	}
}
