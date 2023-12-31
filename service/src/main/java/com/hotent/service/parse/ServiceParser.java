package com.hotent.service.parse;

/**
 * 通过服务地址解析出服务对象
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public interface ServiceParser {
	/**
	 * 解析服务对象
	 * @param url
	 * @return
	 */
	ServiceBean parse(String url);
}