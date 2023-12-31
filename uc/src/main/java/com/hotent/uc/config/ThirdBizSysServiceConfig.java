package com.hotent.uc.config;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.hotent.platform.webservice.impl.ThirdBizSysService;
import com.hotent.uc.ws.impl.ThirdBizSysServiceImpl;

@Configuration
public class ThirdBizSysServiceConfig {
	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public ThirdBizSysService thirdBizSysService() {
		return new ThirdBizSysServiceImpl();
	}
	
	@DependsOn("servletRegistrationBean")
	@Bean
	public Endpoint endpoint() {
		Bus bus = (Bus) applicationContext.getBean(Bus.DEFAULT_BUS_ID);
		EndpointImpl endpoint = new EndpointImpl(bus, thirdBizSysService());
		endpoint.publish("/ThirdBizSysService");
		endpoint.getServer().getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
		endpoint.getServer().getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
		return endpoint;
	}
}
