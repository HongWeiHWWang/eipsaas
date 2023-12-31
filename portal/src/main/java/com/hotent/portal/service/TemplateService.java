package com.hotent.portal.service;

import java.util.Map;

import com.hotent.base.jms.Notice;
import com.hotent.portal.model.MsgTemplate;

/**
 * 模板解析
 * 
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 */
public interface TemplateService {
	/**
	 * 通过模板key查找模板
	 * @param templateKey	模板key
	 * @return	模板
	 */
	MsgTemplate getTemplate(String templateKey);
	/**
	 * 通过类型key查找默认模板
	 * @param typeKey	类型key
	 * @return	模板
	 */
	MsgTemplate getDefaultTemplate(String typeKey);
	/**
	 * 解析模板标题
	 * @param templateVo	模板
	 * @param vars	变量
	 * @return	标题
	 */
	String parseSubject(MsgTemplate templateVo, Map<String, Object> vars);
	/**
	 * 解析纯文本内容
	 * @param templateVo	模板
	 * @param vars	变量
	 * @return	纯文本内容
	 */
	String parsePlainContent(MsgTemplate templateVo, Map<String, Object> vars);
	/**
	 * 解析html内容
	 * @param templateVo	模板
	 * @param vars	变量
	 * @return	html内容
	 */
	String parseHtmlContent(MsgTemplate templateVo, Map<String, Object> vars);
	
	/**
	 * 将Notic放到队列中，再进行处理（解决流程通过feign调用portal来发送通知时耗时太长的问题）
	 * @param notice
	 */
	void sendNotice2Jms(Notice notice);
	
	/**
	 * 通过模板发送消息
	 * @param notice
	 */
	void sendNotice(Notice notice);
}
