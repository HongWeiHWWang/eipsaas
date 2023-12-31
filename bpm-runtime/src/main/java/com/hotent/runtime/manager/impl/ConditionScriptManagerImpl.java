package com.hotent.runtime.manager.impl;

import java.lang.reflect.Method;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.runtime.dao.ConditionScriptDao;
import com.hotent.runtime.manager.ConditionScriptManager;
import com.hotent.runtime.model.ConditionScript;


@Service("conditionScriptManager")
public class ConditionScriptManagerImpl extends BaseManagerImpl<ConditionScriptDao, ConditionScript> implements ConditionScriptManager {

	@Override
	public ArrayNode getMethodsByClassName(String className, ConditionScript conditionScript,Integer type) throws Exception {
		ArrayNode jarray = JsonUtil.getMapper().createArrayNode();
		Class<?> t = Class.forName(className);
		Method[] methods = t.getDeclaredMethods();
		for (Method method : methods) {
			String returnType = method.getReturnType().getCanonicalName();
			// 只要返回值为boolean的方法
			//if (type==1&& (!"boolean".equals(returnType) && !"java.lang.Boolean".equals(returnType) )) continue;
			
			if(type==2&&!"java.util.Set".equals(returnType)) continue; 
			
			Integer modifirer = method.getModifiers();
			// 只要public方法
			if (modifirer != 1) continue;
			ObjectNode jobMethod = JsonUtil.getMapper().createObjectNode();
			ArrayNode jaryPara = JsonUtil.getMapper().createArrayNode();
			Class<?>[] paraArr = method.getParameterTypes();
			for (int i = 0; i < paraArr.length; i++) {
				Class<?> para = paraArr[i];
				String paraName = "arg" + i;
				String paraType = para.getCanonicalName();
				ObjectNode jsonObject = JsonUtil.getMapper().createObjectNode().put("paraName", paraName).put("paraType", paraType).put("paraDesc", "");

				//初始化之前写下来的备注和控件类型
				if(conditionScript!=null&&conditionScript.getMethodName().equals(method.getName())&&StringUtil.isNotEmpty(conditionScript.getArgument())){
					ArrayNode ja = (ArrayNode) JsonUtil.toJsonNode(conditionScript.getArgument());
					for(int j=0;j<ja.size();j++){
						ObjectNode jo = (ObjectNode) JsonUtil.toJsonNode(ja.get(j));
						if(jo.get("paraName").asText().equals(paraName)){
							jsonObject.remove("paraDesc");
							jsonObject.put("paraDesc", jo.get("paraDesc").asText());
							jsonObject.remove("paraCt");
							jsonObject.put("paraCt", jo.get("paraCt").asText());
						}
					}
				}
				jaryPara.add(jsonObject);
			}
			jobMethod.put("returnType", returnType).put("methodName", method.getName()).set("para", jaryPara);
			jarray.add(jobMethod);
		}
		return jarray;
	}

}
