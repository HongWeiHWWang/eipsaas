package com.hotent.bpm.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.transaction.TransactionException;

import com.hotent.base.util.Dom4jUtil;

/**
 * 
 * @author heyifan
 */
public class BpmTransUtil {
	/**
	 * 将通过设计器设计的流程定义xml添加监听器设置
	 * @param id 流程定义ID
	 * @param name 流程定义名称
	 * @param xml 流程定义xml
	 * @return
	 * @throws Exception 
	 */
	public static String transform(String id, String name, String xml) throws Exception{
		try{
			ClassLoader loader  =  Thread.currentThread().getContextClassLoader();
			InputStream  is=loader.getResourceAsStream("com/hotent/bpmx/activiti/xml/transform.xsl");
			if(is==null){
				is=BpmTransUtil.class.getResourceAsStream("com/hotent/bpmx/activiti/xml/transform.xsl");   
			}
			
			Map<String, String> map =new HashMap<String, String>();
			map.put("id", id);
			map.put("name", name);
			String result= Dom4jUtil.transXmlByXslt(xml, is, map);
			result = result.replace("&lt;", "<").replace("&gt;", ">")
					.replace("xmlns=\"\"", "").replace("&amp;", "&");

			Pattern regex = Pattern.compile("name=\".*?\"");
			Matcher regexMatcher = regex.matcher(result);
			while (regexMatcher.find()) {
				String strReplace = regexMatcher.group(0);
				String strReplaceWith = strReplace.replace("&", "&amp;")
						.replace("<", "&lt;").replace(">", "&gt;");
				result = result.replace(strReplace, strReplaceWith);
			}
			return result;
		}
		catch(Exception ex){
			throw new TransactionException("转换流程定义出错", ex);
		}
	}
}
