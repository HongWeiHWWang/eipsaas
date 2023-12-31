package com.hotent.im.service;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.im.config.MqttProperty;
import com.hotent.im.persistence.manager.ImMessageHistoryManager;
import com.hotent.im.persistence.manager.ImMessageSessionManager;
import com.hotent.im.persistence.manager.ImSessionUserManager;
import com.hotent.im.persistence.model.ImMessageHistory;
import com.hotent.im.util.ImConstant;
import com.hotent.im.util.ImMqttUtil;

@Service
public class MqttReceiveService {
	private MqttClient mqttClient = null;
	
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10); 
	
	@Resource
	ImMessageHistoryManager imMessageHistoryManager;
	@Resource
	ImMessageSessionManager imMessageSessionManager;
	@Resource
	ImSessionUserManager imSessionUserManager;
	@Resource
	MqttProperty mqttProperty;
	
	public  MqttClient connect() {
		if(BeanUtils.isNotEmpty(mqttClient) && mqttClient.isConnected()){
			return mqttClient;
		}
		//MqttClient mqttClient = null  ;
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		options.setKeepAliveInterval(20);
		options.setAutomaticReconnect(true);
		try {
			Assert.notNull(mqttProperty, "未获取到MqttProperty的实现类");
			mqttClient = new MqttClient(mqttProperty.getServerUrl(), "defaultReceiveClient"+System.currentTimeMillis(), new MemoryPersistence());  
			mqttClient.setCallback(new MqttCallback() {        
                @Override
                public void messageArrived(String topicName, MqttMessage message) throws Exception {
                	System.out.println(topicName);
                	fixedThreadPool.execute(new Runnable() {
						@Override
						public void run() {
							// subscribe后得到的消息会执行到这里面  
		                	try {
		                		String str = new String(message.getPayload(),"UTF-8");
		                		System.out.println("接收消息内容:"+str);
		                		if(!StringUtils.isEmpty(str)){
		                			JsonNode node = JsonUtil.toJsonNode(str);
		                			if("offline".equals(node.get("type").asText())){
		                				return;
		                			}
		                			ImMessageHistory immsg = new ImMessageHistory();
		                			immsg.setSessionCode(node.get("sessionCode").asText());
		                			immsg.setContent(node.get("content").toString());
		                			immsg.setFrom(node.get("from").asText());
		                			immsg.setType(node.get("type").asText());
		                			immsg.setMessageId(node.get("messageId").asText());
			                    	immsg.setSendTime(System.currentTimeMillis());
			                    	immsg.setId(UniqueIdUtil.getSuid());
			                    	if(immsg.getMessageId() == null){
			                    		immsg.setMessageId(ImMqttUtil.generateUUID());
			                    	}
			                    	imMessageHistoryManager.create(immsg);
			                    	Map<String,Object> param = new HashMap<String,Object>();
			                    	param.put("lastText", str);
			                    	param.put("sessionCode", immsg.getSessionCode());
			                    	param.put("lastTextTime", new Date());
			                    	imMessageSessionManager.updateMessageArrived(param);
			                    	//imSessionUserManager.updateLastReadTime(immsg.getFrom(), immsg.getSessionCode(), new Date());
		                		}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                	 // publish后会执行到这里  
                    System.out.println("deliveryComplete---------"+ token.isComplete());  
                }
                @Override
                public void connectionLost(Throwable cause) {
                	System.out.println(cause.getMessage()+",connect lost");
//                	long i = 0;
//                	while (mqttClient == null || !mqttClient.isConnected()) {
//						try {
//							i++;
//							System.out.println("重连次数："+i);
//							Thread.sleep(5000);
//							mqttClient = connect();
//						} catch (Exception e) {
//							System.out.println("连接错误："+e.getMessage());
//						}
//					}
                }
            });
			//mqttClient.connect(options);
			mqttClient.connect(options);
			mqttClient.subscribe(ImConstant.MQTT_RECEIVE_DESTINATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mqttClient;
	}
}
