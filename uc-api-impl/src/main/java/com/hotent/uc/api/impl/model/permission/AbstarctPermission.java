package com.hotent.uc.api.impl.model.permission;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.api.model.IPermission;




/**
 * 类 {@code AbstarctPermission} 权限计算抽象类，默认提供一个权限计算方法。
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public abstract class AbstarctPermission implements IPermission {

	
	/**
	 * 默认计算下面的配置：
	 * json结构：
	 * {type:"user",id:"1,2",name:"ray,tom"}
	 * 或:
	 * {type:"group",id:"1,2",name:"hotent,google"}
	 * currentMap:当前人对算法的配置。
     * @return 是否有权限
	 * @throws IOException 
	 */
	@Override
	public boolean hasRight(String json, Map<String, Set<String>> currentMap) throws IOException {
		ObjectNode jsonObj = (ObjectNode) JsonUtil.toJsonNode(json);
		String id=jsonObj.get("id").asText();
		if(StringUtil.isEmpty(id)) return false;
		String [] ids = jsonObj.get("id").asText().split(",");
		Set<String> set=currentMap.get(getType());
		for(String tmp:ids){
			if(set!=null && set.contains(tmp)){
				return true;
			}
		}
		return false;
	}
}
