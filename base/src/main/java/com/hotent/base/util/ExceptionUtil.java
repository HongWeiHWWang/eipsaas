package com.hotent.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 错误信息处理类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月11日
 */
public class ExceptionUtil {

	/**
	 * 获取exception的详细错误信息。
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionMessage(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		String str = sw.toString();
		return str;
	}
}
