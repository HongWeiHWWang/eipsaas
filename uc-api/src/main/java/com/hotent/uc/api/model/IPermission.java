package com.hotent.uc.api.model;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 接口 {@code IPermission} 权限
 * @author ray
 */
public interface IPermission {
	
	/**
	 * 获取权限规则名称
	 * @return 权限规则名称
	 */
	String getTitle();
	
	/**
	 * 获取权限类型
	 * @return 权限类型
	 */
	String getType();
	
	/**
	 * 根据规则判断是否有权限
	 *
	 * @param json
	 *  格式如下：
	 *  {"type":"user","id":"1,2","name":"ray,tom"}
     * @param currentMap
	 * 键代表算法,值表示当前算法的配置数据
	 * @return 是否有权限
	 * @throws IOException
	 */
	boolean hasRight(String json,Map<String, Set<String>> currentMap) throws IOException;
	
	/**
	 * 获取当前人的配置
	 * @return 当前人的配置
	 */
	Set<String> getCurrentProfile();
}
