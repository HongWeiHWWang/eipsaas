package com.hotent.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.hotent.base.annotation.IgnoreOnAssembly;



/**
 * @company 广州宏天软件股份有限公司
 * @author:liyg
 * @date:2018年6月7日
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@Configuration
@MapperScan(basePackages={"com.hotent.**.dao"})
@ComponentScan(basePackages={"com.hotent.*","org.activiti.engine.*"}, 
excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {IgnoreOnAssembly.class})})
public class Application {
	public static void main( String[] args )
	{
		SpringApplication.run(Application.class, args);
	}
}
