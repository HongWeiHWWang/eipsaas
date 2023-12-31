package com.hotent.uc.api.impl.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.uc.api.model.IPermission;

/**
 * 
 * 类 {@code PermissionCalc} 权限计算
 * <pre>
 * 为了性能考虑，比如在计算字段权限的时候，我们在计算权限之前，
 * 先获取当前人的身份信息，比如：
 * 这个人属于哪些组织，哪些角色
 * 然后把这个放到一个map当中，在运算的时候，直接获取对应的配置，然后进行权限运算
 * </pre>
 * @author ray
 *
 */
public class PermissionCalc {
	
	private Map<String,IPermission> permissionMap=new HashMap<String, IPermission>();
	
	private List<IPermission> permissionList=new ArrayList<IPermission>();
	
	/**
	 * 设置权限列表
	 * @param iPermissions 权限列表
	 */
	public void setPermissionList(List<IPermission> iPermissions) {
		for(IPermission permission:iPermissions){
			permissionMap.put(permission.getType(), permission);
		}
		this.permissionList=iPermissions;
	}


    /**
     * 获取权限
     * @param type 权限类型
     * @return 权限
     */
	public IPermission getPermission(String type){
		return permissionMap.get(type);
	}
	
	
	/**
	 * 获取权限规则的当前人配置数据
	 * 比如当前算法为用户角色：那么获取当前人的角色
	 * @return 当前人配置数据
	 */
	public Map<String,Set<String>> getCurrentProfiles(){
		Map<String,Set<String>> map=new HashMap<String, Set<String>>();
		for(IPermission permission:permissionList){
			Set<String> set=permission.getCurrentProfile();
			if(BeanUtils.isEmpty(set)) continue;
			map.put(permission.getType(), set) ;
		}
		return map;
	}
	
	/**
	 * 权限计算
	 * @param json
     * 参数说明：
     * json:传入的配置
     * 数据格式如下：
     * {"type":"user","id":"1,2","name":"ray,tom"}
	 * @param currentMap
     * currentMap:
     * 当前算法计算出来的当前人的配置，比如某个人属于某几个角色
	 * @return 是否有权限
	 * @throws IOException 
	 */
	public boolean hasRight(String json, Map<String, Set<String>> currentMap) throws IOException {
		JsonNode jsonObj = JsonUtil.toJsonNode(json);
		String type=jsonObj.get("type").asText();
		IPermission permission= getPermission(type);
		return permission.hasRight(json, currentMap);
	}
}
