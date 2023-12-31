package com.hotent.im.config;

import org.springframework.beans.factory.annotation.Value;

public class MqttProperty {
	@Value("${mqtt.host}")
	public String host;
	
	@Value("${mqtt.port}")
	public String port;
	
	public String getServerUrl(){
		return "ws://"+host+":"+port;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
}
