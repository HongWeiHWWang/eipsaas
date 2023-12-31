package com.hotent.base.service;

import java.util.Map;

/**
 * 服务调用接口
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public interface ServiceClient {
	/**
	 * 执行调用接口
	 * @param invokeCmd	调用的命令封装
	 * @return			调用结果
	 */
	InvokeResult invoke(InvokeCmd invokeCmd);
	/**
	 * 执行调用接口
	 * @param alias	服务设置的别名
	 * @param map	调用的参数
	 * @return		调用结果
	 */
	InvokeResult invoke(String alias, Map<String, Object> map);
}
