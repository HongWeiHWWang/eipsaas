package com.hotent.uc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringCloudApplication
@Configuration
@MapperScan(basePackages={"com.hotent.**.dao"})
@ComponentScan({"com.hotent.*"})
@EnableFeignClients(basePackages = {"com.hotent.*"})
public class Application 
{
	public static void main( String[] args )
	{
		SpringApplication.run(Application.class, args);
	}
}