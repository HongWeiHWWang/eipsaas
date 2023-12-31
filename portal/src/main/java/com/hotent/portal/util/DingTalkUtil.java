package com.hotent.portal.util;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.util.Base64;
import com.hotent.base.util.JsonUtil;
import com.hotent.system.consts.DingTalkConsts;

/**
 * 阿里钉钉工具类
 * @author pangq
 *
 */
public class DingTalkUtil {

	public static String renderMobileUrl(JmsMessage jmsMessage) {
		String newUrl = "";
		try {
			Document doc= Jsoup.parseBodyFragment(jmsMessage.getContent());
			Elements elms = doc.getElementsByTag("a");
			Map<String,Object> vars = jmsMessage.getExtendVars();
//			String str = "{\"taskId\":\""+vars.getOrDefault("taskId", "")+"\" ,\"instId\":\""+vars.getOrDefault("instId", "")+
//					"\",\"templateAlias\":\""+jmsMessage.getTemplateAlias()
//					+"\",\"templateType\":\""+vars.getOrDefault("templateType","")+"\"";
			Map<String,Object> params = new HashMap<>();
			params.put("taskId", vars.getOrDefault("taskId", ""));
			params.put("instId", vars.getOrDefault("instId", ""));
			params.put("templateType", vars.getOrDefault("templateType",""));
			params.put("templateAlias", jmsMessage.getTemplateAlias());
			for(Element e : elms) {
				String oldUrl = e.attr("href");
//				str+=",\"originUrl\":\""+oldUrl+"\"}";
				params.put("originUrl", oldUrl);
				String paramStr = Base64.getBase64(JsonUtil.toJson(params));
				newUrl = DingTalkConsts.getAuthorize(paramStr);
				jmsMessage.setContent(jmsMessage.getContent().replace(oldUrl, newUrl));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newUrl;
	}
	
}
