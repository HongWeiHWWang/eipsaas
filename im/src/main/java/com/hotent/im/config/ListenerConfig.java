package com.hotent.im.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotent.im.service.MqttInitListener;

@Configuration
public class ListenerConfig {
	@Bean
    public MqttInitListener applicationStartListener(){
        return new MqttInitListener();
    }
	
	@Bean
	public MqttProperty mqttProperty() {
		return new MqttProperty();
	}
}
