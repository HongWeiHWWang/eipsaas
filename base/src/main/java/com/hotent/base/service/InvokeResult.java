package com.hotent.base.service;

import java.util.List;

/**
 * 服务调用结果
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public interface InvokeResult {
	/**
	 * 获取返回值
	 * @return	返回值
	 */
	Object getObject();
	/**
	 * 获取返回值列表
	 * @return	返回值列表
	 */
	List<Object> getList();
	/**
	 * 获取返回的json
	 * @return	返回的json
	 */
	String getJson();
	/**
	 * 结果是否为列表
	 * @return	是否为列表
	 */
	Boolean isList();
	/**
	 * 是否无返回值
	 * @return	是否无返回值
	 */
	Boolean isVoid();
	/**
	 * 是否出现异常
	 * @return	是否出现异常
	 */
	Boolean isFault();
	/**
	 * 获取异常信息
	 * @return	异常信息
	 */
	Exception getException();
}
