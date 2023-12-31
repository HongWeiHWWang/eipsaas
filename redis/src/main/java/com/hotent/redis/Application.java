package com.hotent.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

/**
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 */
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