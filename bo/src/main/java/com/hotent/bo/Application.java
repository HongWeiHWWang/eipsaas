package com.hotent.bo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@Configuration
@MapperScan(basePackages={"com.hotent.**.dao"})
@ComponentScan({"com.hotent.*"})
public class Application 
{
	public static void main( String[] args )
	{
		SpringApplication.run(Application.class, args);
	}
}