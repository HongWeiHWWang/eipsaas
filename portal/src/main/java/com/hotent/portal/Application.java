package com.hotent.portal;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.hotent.base.util.StringUtil;
import com.hotent.i18n.util.I18nUtil;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@Configuration
@MapperScan(basePackages={"com.hotent.**.dao"})
@ComponentScan({"com.hotent.*"})
@EnableFeignClients(basePackages = {"com.hotent.*"})
public class Application 
{
	@Value("${spring.servlet.multipart.maxFileSize:'1024MB'}")
	private String maxFileSize;
	@Value("${spring.servlet.multipart.maxRequestSize:'1024MB'}")
	private String maxRequestSize;
	
	public static void main( String[] args )
	{
		SpringApplication.run(Application.class, args);
		// 启动后初始化国际化资源到redis 缓存中
		I18nUtil.initMessage();
	}
	
	/**  
     * 文件上传配置  
     * @return  
     */  
    @Bean  
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        // 文件最大,DataUnit提供5中类型B,KB,MB,GB,TB
        factory.setMaxFileSize(StringUtil.isEmpty(maxFileSize)?DataSize.of(10, DataUnit.MEGABYTES):DataSize.parse(maxFileSize));
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(StringUtil.isEmpty(maxRequestSize)?DataSize.of(100, DataUnit.MEGABYTES):DataSize.parse(maxRequestSize));
        return factory.createMultipartConfig();  
    }  
}