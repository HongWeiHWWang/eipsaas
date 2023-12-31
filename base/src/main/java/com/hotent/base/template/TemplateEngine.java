package com.hotent.base.template;

import java.io.IOException;

import freemarker.template.TemplateException;

/**
 * 模板解析引擎
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年6月4日
 */
public interface TemplateEngine {
	/**
	 * 通过模板名称解析
	 * @param templateName	模板名称(指定系统中的一个目录为根目录，传入该目录下的模板名称来解析，名称需包含后缀，如：test.ftl)
	 * @param model			数据对象
	 * @return				解析结果
	 * @throws IOException
	 * @throws TemplateException
	 */
	String parseByTempName(String templateName, Object model) throws Exception;
	
	/**
	 * 通过模板解析
	 * <pre>
	 * 注意该方法可能不能使用include或者import引入相对目录下的模板
	 * </pre>
	 * @param template		模板内容
	 * @param model			数据对象
	 * @return				解析结果
	 * @throws IOException
	 * @throws TemplateException
	 */
	String parseByTemplate(String template, Object model) throws Exception;
	
	/**
	 * 根据字符串模版解析出内容
	 * 
	 * @param templateSource
	 *            字符串模版。
	 * @param model
	 *            需要解析的对象。
	 * @return 解析后的模板
	 * @throws TemplateException
	 * @throws IOException
	 */
	String parseByStringTemplate(String templateSource, Object model)  throws Exception;
}
