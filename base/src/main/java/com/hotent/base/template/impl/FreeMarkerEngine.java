package com.hotent.base.template.impl;

import java.io.StringWriter;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import com.hotent.base.exception.BaseException;
import com.hotent.base.template.TemplateEngine;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

/**
 * FreemarkEngine解析引擎
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年6月4日
 */
@Service
@Transactional
public class FreeMarkerEngine implements InitializingBean, TemplateEngine{
	private Version version = Configuration.VERSION_2_3_28;
	private Configuration configuration;
	@Resource
	ResourceLoader resourceLoader;
	@Value("${spring.freemarker.template-loader-path}")
	private String templateLoaderPath = "classpath:/templates/";
	@Value("${spring.freemarker.charset}")
	private String defaultEncoding = "UTF-8";
	// 当直接传入模板内容进行解析时，需要指定模板名称，这里使用该名称
	private String commonTemplateName = "common_template_freemark";
	
	@Override
	public void afterPropertiesSet() throws Exception {
		configuration = new Configuration(version);
		TemplateLoader templateLoader = new SpringTemplateLoader(resourceLoader, templateLoaderPath);
		configuration.setTemplateLoader(templateLoader);
		configuration.setDefaultEncoding(defaultEncoding);
	}

	@Override
	public String parseByTempName(String templateName, Object model) throws Exception {
		try {
			Template template = configuration.getTemplate(templateName);
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		}
		catch(Exception ex) {
			throw new BaseException(String.format("模板解析异常，异常信息：%s", ExceptionUtils.getRootCauseMessage(ex)));
		}
	}

	@Override
	public String parseByTemplate(String template, Object model) throws Exception {
		try {
			// 避免污染全局配置，临时性的创建新的配置来装在模板
			Configuration cfg = new Configuration(version);
			StringTemplateLoader loader = new StringTemplateLoader();
			cfg.setDefaultEncoding(defaultEncoding);
			cfg.setTemplateLoader(loader);
			cfg.setClassicCompatible(true);
			loader.putTemplate(commonTemplateName, template);
			Template templateObj = cfg.getTemplate(commonTemplateName);
			StringWriter writer = new StringWriter();
			templateObj.process(model, writer);
			// 用完移除该模板
			loader.removeTemplate(commonTemplateName);
			return writer.toString();
		}
		catch(Exception ex) {
			throw new BaseException(String.format("模板解析异常，异常信息：%s", ExceptionUtils.getRootCauseMessage(ex)));
		}
	}
	
	
	@Override
	public String parseByStringTemplate(String templateSource, Object model) throws Exception {
		try {
			Configuration cfg = new Configuration(version);
			StringTemplateLoader loader = new StringTemplateLoader();
			cfg.setTemplateLoader(loader);
			cfg.setClassicCompatible(true);
			loader.putTemplate("freemaker", templateSource);
			Template template = cfg.getTemplate("freemaker");
			StringWriter writer = new StringWriter();
			template.process(model, writer);
			return writer.toString();
		}
		catch(Exception ex) {
			throw new BaseException(String.format("模板解析异常，异常信息：%s", ExceptionUtils.getRootCauseMessage(ex)));
		}
	}
}
