package com.hotent.form;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

/**
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月7日
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@Configuration
@MapperScan(basePackages={"com.hotent.**.dao"})
@ComponentScan(basePackages={"com.hotent.*"},
			   excludeFilters= {@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=DruidDataSourceAutoConfigure.class)})
@EnableFeignClients(basePackages = {"com.hotent.*"})
public class Application {
	public static void main( String[] args )
	{
		SpringApplication.run(Application.class, args);
	}
}
