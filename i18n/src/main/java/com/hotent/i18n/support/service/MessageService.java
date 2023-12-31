package com.hotent.i18n.support.service;

import java.util.List;
import java.util.Map;

/**
 * 资源接口
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 */
public interface MessageService {
	/**
	 * 通过资源KEY和国际化类型获取资源值
	 * @param code 资源KEY
	 * @param type 国际化类型
	 * @return
	 */
	String getMessage(String code, String type);
	
	/**
	 * 批量获取资源值
	 * @param codes
	 * @param type
	 * @return
	 */
	Map<String, String> getMessages(List<String> codes, String type);
	
	/**
	 * 初始化国际化资源到Cache中
	 */
	void initMessage();
	
	/**
	 * 清空Cache中的所有国际化资源
	 */
	void clearAllMessage();
	
	/**
	 * 根据键删除缓存
	 * @param key
	 */
	void delByKey(String key);
	
	/**
	 * 清除对应缓存
	 * @param key
	 * @param field
	 */
	void hdel(String key, String field);
}
