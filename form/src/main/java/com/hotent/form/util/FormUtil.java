package com.hotent.form.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JsonUtil;





/**
 * 表单实用函数。
 * @author ray。
 *
 */
public class FormUtil {
	/**
	 * 返回模版物理的路径。
	 * @return
	 * @throws Exception 
	 */
	public static String getDesignTemplatePath() throws Exception {
		return FileUtil. getClassesPath() + "template" + File.separator +"design" + File.separator;
	}
	/**
	 * 获取意见表单字段。
	 * <pre>
	 * 通过解析表单，返回表单中的意见字段数据。
	 * </pre>
	 * @param html
	 * @return
	 */
	public static Map<String,String> parseOpinion(String html){
		Map<String,String> map=new HashMap<String, String>();
		Document doc= Jsoup.parseBodyFragment(html);
		Elements list=doc.select("[opinion]");
		for(Iterator<Element> it= list.iterator();it.hasNext();){
			Element el=it.next();
			String opinion=el.attr("opinion");
			try {
				if(opinion!=null){
					JsonNode jsonObject = JsonUtil.toJsonNode(opinion);
					String name=jsonObject.get("name")+"";//;el.attr("name");
					String memo=jsonObject.get("desc")+"";//el.attr("opinionname");
					map.put(name, memo);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
