package com.hotent.tools.sqlgen.util;

import java.io.IOException;
import java.io.StringWriter;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Freemark帮助类
 * 
 * <pre>
 * 构建组：x6-tools
 * 作者：hugh
 * 邮箱:zhuangxh@jee-soft.cn
 * 日期:2014-9-26-上午10：09:54
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class FreemarkUtil {

	/**
	 * 根据字符串模版解析出内容
	 * 
	 * @param obj
	 *            需要解析的对象。
	 * @param templateSource
	 *            字符串模版。
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static String parseByStringTemplate(Object obj, String templateSource)
			throws TemplateException, IOException {
		Configuration cfg = new Configuration();
		StringTemplateLoader loader = new StringTemplateLoader();
		cfg.setTemplateLoader(loader);
		cfg.setClassicCompatible(true);
		loader.putTemplate("freemaker", templateSource);
		Template template = cfg.getTemplate("freemaker");
		StringWriter writer = new StringWriter();
		template.process(obj, writer);
		return writer.toString();

	}

}
