package com.hotent.uc.api.impl.model.permission;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IPermission;

/**
 * 类 {@code ScriptPermission} 脚本权限计算
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class ScriptPermission implements IPermission {
	
	@Resource
	GroovyScriptEngine groovyScriptEngine;

	@Override
	public String getTitle() {
		return "脚本";
	}

	@Override
	public String getType() {
		return "script";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasRight(String json, Map<String, Set<String>> currentMap) throws IOException {
		ObjectNode jsonObj=(ObjectNode) JsonUtil.toJsonNode(json);
		String script=jsonObj.get("id").asText();
		Set<String> set= (Set<String>) groovyScriptEngine.executeObject(script, null);
		if(BeanUtils.isEmpty(set)) return false;
		
		String userId=ContextUtil.getCurrentUserId();
		if(set.contains(userId)) return true;
	
		return false;
	}

	@Override
	public Set<String> getCurrentProfile() {
		return null;
	}

}
