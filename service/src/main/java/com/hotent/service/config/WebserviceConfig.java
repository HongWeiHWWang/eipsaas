package com.hotent.service.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 注册Webservice的servlet
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2019年4月29日
 */
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
@Configuration
public class WebserviceConfig {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		org.apache.cxf.transport.servlet.CXFServlet cxfServlet = new org.apache.cxf.transport.servlet.CXFServlet();
		ServletRegistrationBean servletDef = new ServletRegistrationBean(cxfServlet, "/service/*");
		servletDef.setLoadOnStartup(1);
		return servletDef;
	}
}
