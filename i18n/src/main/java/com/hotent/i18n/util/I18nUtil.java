package com.hotent.i18n.util;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.i18n.support.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * I18n工具类
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 */
public class I18nUtil {

	private static MessageService messageService;

	private static void initService(){
		if(BeanUtils.isEmpty(messageService)){
			messageService = AppUtil.getBean(MessageService.class);
		}
	}
	
	public static void initMessage(){
		initService();
		messageService.initMessage();
	}

	/**
	 * 获取国际化资源
	 * @param code
	 * @return
	 */
	public static String getMessage(String code, Locale locale){
		initService();
		String message = messageService.getMessage(code, getLocaleTag(locale));
		return message;
	}

	// 获取当前语言
	private static String getLocaleTag(Locale locale) {
		return locale.toLanguageTag();
	}

	/**
	 *
	 * @param temp temp单个的占位符${key}或"${key}"，如：
	 * ${test}，其中test为国际化资源的key值
	 * @param rep
	 * @return
	 */
	public static String getCode(String temp,String rep){
		String rtn = "";
		Pattern regex = Pattern.compile(rep);
		Matcher regexMatcher = regex.matcher(temp);
		while (regexMatcher.find()) {
			rtn = regexMatcher.group(1);
		}
		return rtn;
	}

	/**
	 * 通过多个code获取国际化资源集合
	 * @param codes
	 * @return
	 */
	public static Map<String, String> getMessages(List<String> codes, Locale locale){
		initService();
		return messageService.getMessages(codes, getLocaleTag(locale));
	}

	/**
	 * 获取codes
	 * @param temp 模板
	 * @param rep 正则表达式
	 * @return
	 */
	public static List<String> getCodes(String temp,String rep){
		List<String> result = new ArrayList<String>();
		Pattern regex = Pattern.compile(rep);
		Matcher regexMatcher = regex.matcher(temp);
		while (regexMatcher.find()) {
			result.add(regexMatcher.group(1));
		}
		return result;
	}

	/**
	 * 替换字符串中的国际化资源占位符${key}
	 * @param temp 模板内容
	 * @param rep 正则表达式
	 * @return
	 */
	public static String replaceTemp(String temp, String rep, Locale locale){
		if (StringUtil.isEmpty(temp)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		List<String> codes = getCodes(temp,rep);
		Map<String,String> messages = getMessages(codes, locale);
		Pattern regex = Pattern.compile(rep);
		Matcher regexMatcher = regex.matcher(temp);
		//再将temp中匹配的数据逐个替换掉  返回到页面上
		while (regexMatcher.find()) {
			regexMatcher.appendReplacement(sb, messages.get(regexMatcher.group(1)));
		}
		regexMatcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 为framemaker提供，因为在ftl中${}会解析掉，此处用￥{}暂时替换掉，在展示到页面上时再替换出对应的国际化资源
	 * @param temp
	 * @param rep
	 * @return
	 */
	public static String replaceTempForFramemaker(String temp,String rep){
		StringBuffer sb = new StringBuffer();
		Pattern regex = Pattern.compile(rep);
		Matcher regexMatcher = regex.matcher(temp);
		//再将temp中匹配的数据逐个替换掉  返回到页面上
		while (regexMatcher.find()) {
			regexMatcher.appendReplacement(sb, "￥{"+regexMatcher.group(1)+"}");
		}
		regexMatcher.appendTail(sb);
		return sb.toString();
	}

}
