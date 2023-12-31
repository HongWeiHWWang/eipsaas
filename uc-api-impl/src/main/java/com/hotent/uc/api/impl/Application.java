package com.hotent.uc.api.impl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 类 {@code Application} 在SpringBoot创建时会自动创建启动类,本工程项目和服务器的启动加载类
 */
@SpringBootApplication
@Configuration
@MapperScan(basePackages={"com.hotent.base.dao","com.hotent.**.dao"})
@ComponentScan({"com.hotent.*"})
public class Application 
{
	public static void main( String[] args )
	{
		SpringApplication.run(Application.class, args);
	}
}