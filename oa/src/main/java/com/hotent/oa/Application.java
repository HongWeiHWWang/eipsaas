package com.hotent.oa;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
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