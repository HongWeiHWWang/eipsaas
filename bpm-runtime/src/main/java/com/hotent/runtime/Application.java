package com.hotent.runtime;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@SpringCloudApplication
@Configuration
@MapperScan(basePackages={"com.hotent.**.dao"})
@ComponentScan({"com.hotent.*","org.activiti.engine.*"})
@EnableFeignClients(basePackages = {"com.hotent.*"})
@EnableHystrixDashboard
public class Application 
{
	public static void main( String[] args )
	{
		SpringApplication.run(Application.class, args);
	}
	
//	@Bean
//	public ServletRegistrationBean getServlet(){
//	   HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
//	   ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
//	   registrationBean.setLoadOnStartup(1);
//	   registrationBean.addUrlMappings("/actuator/hystrix.stream");
//	   registrationBean.setName("HystrixMetricsStreamServlet");
//	   return registrationBean;
//	}
}