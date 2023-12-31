/**
 * 描述：TODO
 * 包名：com.hotent.platform.util
 * 文件名：CustomUtil.java
 * 作者：User-mailto:chensx@jee-soft.cn
 * 日期2014-8-14-上午10:04:35
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.form.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.base.util.time.DateFormatUtil;


/**
 * <pre> 
 * 描述：自定义查询和对话框的相关工具类
 * 构建组：x5-bpmx-platform
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-8-14-上午10:04:35
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class CustomUtil {
	public static final String DEFAULTTYPE_INPUT_PARAM = "1";
	public static final String DEFAULTTYPE_FIX_VALUE = "2";
	public static final String DEFAULTTYPE_SCRIPT = "3";
	public static final String DEFAULTTYPE_INPUT_TRENDS = "4";
	
	/**
	 * 构建value
	 * 
	 * @param field
	 *            字段名
	 * @param defaultType
	 *            默认类型
	 * @param defaultValue
	 *            默认值
	 * @param params
	 *            页面传来的参数
	 * @return Object
	 * @exception
	 * @since 1.0.0
	 */
	public static Object buildValue(String field, String defaultType, String defaultValue, Map<String, Object> params) {
		Object value = null;
		if (defaultType.equals(DEFAULTTYPE_INPUT_PARAM)||defaultType.equals(DEFAULTTYPE_INPUT_TRENDS)) {// 参数输入
			if (params.containsKey(field) && StringUtil.isNotEmpty(params.get(field).toString())) {
				value = params.get(field).toString();
			}
		} else if (defaultType.equals(DEFAULTTYPE_FIX_VALUE)) {// 固定值
			value = defaultValue;
		} else if (defaultType.equals(DEFAULTTYPE_SCRIPT)) {// 脚本
			String script = defaultValue;
			if (StringUtil.isNotEmpty(script)) {
				GroovyScriptEngine groovyScriptEngine = (GroovyScriptEngine) AppUtil.getBean(GroovyScriptEngine.class);
				value = groovyScriptEngine.executeObject(script, null);
			}
		}
		return value;
	}
	
	/**
	 * 处理between的value，把它按"|"分割成startDate,endDate的json
	 * @param value
	 * @return 
	 * Object
	 * @exception 
	 * @since  1.0.0
	 */
	public static Object handleDateBetweenValue(Object value){
		String[] aryDate = value.toString().split("\\|");
		String startDate = aryDate[0];
		String endDate = aryDate[1];
		LocalDateTime d = DateFormatUtil.parse(endDate, StringPool.DATE_FORMAT_DATETIME, StringPool.DATE_FORMAT_DATE, StringPool.DATE_FORMAT_TIMESTAMP);
		endDate = d.plusDays(1).format(DateTimeFormatter.ofPattern(StringPool.DATE_FORMAT_DATETIME));
		ObjectNode tjo = JsonUtil.getMapper().createObjectNode();
		tjo.put("startDate", startDate);
		tjo.put("endDate", endDate);
		return tjo;
	}
}
