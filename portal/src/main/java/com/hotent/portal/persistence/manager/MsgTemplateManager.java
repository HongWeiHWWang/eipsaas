package com.hotent.portal.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.portal.model.MsgTemplate;

/**
 * 对象功能:消息模版 Manager
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 */
public interface MsgTemplateManager extends BaseManager<MsgTemplate> {
	/**
	 * 通过模板key获取模板
	 * @param templateKey
	 * @return
	 */
	public MsgTemplate getByKey(String templateKey);
	/**
	 * 获取指定类型的默认模板
	 * @param typeKey
	 * @return
	 */
	public MsgTemplate getDefault(String typeKey);
	/**
	 * 设置模板为该类型的默认模板
	 * @param id
	 */
	void setDefault(String id);
	/**
	 * 判断指定类型、指定key的模板是否已经存在
	 * @param key
	 * @param typeKey
	 * @return
	 */
	boolean isExistByKeyAndTypeKey(String key, String typeKey);

	/**
	 * 将模板设置为非默认
	 * @param id
	 */
	void setNotDefault(String id);
}
