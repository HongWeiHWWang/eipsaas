package com.hotent.uc.api.impl.util;


import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.uc.api.model.IPermission;

/**
 * 类 {@code PermissionUtil} 权限工具类
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月9日
 */
public class PermissionUtil {
	
	/**
	 * 根据beanId 实例键 x5-sys-core.xml 的formPermissionCalcList。
	 * @param beanId beanId
	 * @return
     * 返回格式：
     * {"everyone":"所有人"},{"none":"无"},{type:"user",id:"",name:""}
	 */
	@SuppressWarnings("unchecked")
	public static JsonNode getPermissionList(String beanId){
		List<IPermission> list=(List<IPermission>) AppUtil.getBean(beanId);
		ObjectNode rtnJson=JsonUtil.getMapper().createObjectNode();
		for(IPermission permission:list){
			rtnJson.put(permission.getType(), permission.getTitle());
		}
		return rtnJson;
	}

}
