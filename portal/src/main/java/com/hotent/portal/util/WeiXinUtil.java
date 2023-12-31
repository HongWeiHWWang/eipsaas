package com.hotent.portal.util;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hotent.activemq.model.JmsMessage;
import com.hotent.base.util.Base64;
import com.hotent.base.util.HttpUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.system.consts.WeChatOffAccConsts;
import com.hotent.system.consts.WeChatWorkConsts;
import com.hotent.system.model.msg.WxBaseMessage;
import com.hotent.system.model.msg.impl.WxNewsMessage;
import com.hotent.system.util.WechatOffAccTokenUtil;

/**
 * 微信工具类。
 * @author ray
 *
 */
public class WeiXinUtil {
	
	/**
	 * 发送模板消息给企业成员。（企业微信）
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	public static String sendTextMessage(WxBaseMessage msg) throws Exception{
		String resultJson = HttpUtil.sendHttpsRequest(WeChatWorkConsts.getSendMsgUrl(),JsonUtil.toJson(msg),WeChatWorkConsts.METHOD_POST);
		return resultJson;
	}
	
	/**
	 * 发送新闻消息给指定用户。
	 * @param msg
	 * @return
	 * @throws Exception 
	 */
	public static String sendNewsMessage(WxNewsMessage msg) throws Exception{
		String resultJson = HttpUtil.sendHttpsRequest(WeChatWorkConsts.getSendMsgUrl(),msg.toString(),WeChatWorkConsts.METHOD_POST);
		return resultJson;
	}

    /**
     * 发送新闻消息给指定用户。（微信公众号）
     * @param msg
     * @return
     * @throws Exception
     */
    public static String sendWeiXinMessage(String msg) throws Exception{
        String resultJson = HttpUtil.sendHttpsRequest(WeChatOffAccConsts.send(WechatOffAccTokenUtil.getToken()),msg,WeChatOffAccConsts.METHOD_POST);
        return resultJson;
    }
	
	/**
	 * 	企业微信：转换pc端的url
	 * @return
	 * @throws Exception 
	 */
	public static String renderMobileUrl(JmsMessage jmsMessage) throws Exception {
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
				newUrl = WeChatWorkConsts.getWxAuthorize(paramStr);
				jmsMessage.setContent(jmsMessage.getContent().replace(oldUrl, newUrl));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newUrl;
	}

	/**
	 * 微信公众号 转换pc端的url
	 * @param jmsMessage
	 * @return
	 */
	public static String renderOffAccUrl(JmsMessage jmsMessage) {
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
				newUrl = WeChatOffAccConsts.getWxAuthorize(paramStr);
				jmsMessage.setContent(jmsMessage.getContent().replace(oldUrl, newUrl));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newUrl;
	}
}
