package com.hotent.uc.api.service;

/**
 * 接口 {@code IParamService} 参数服务
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public interface IParamService {
	
	/**
	 * 根据用户ID和指定的KEY获取配置的参数
	 * @param userId 用户ID
	 * @param key 指定的KEY
	 * @return 配置的参数
	 */
	Object getParamsByKey(String userId,String key);
	
	/**
	 * 根据用户组ID和指定的KEY获取配置的参数
	 * @param groupId 配置参数
	 * @param key 指定的KEY
	 * @return 配置的参数
	 */
	Object getParamByGroup(String groupId,String key);
}
