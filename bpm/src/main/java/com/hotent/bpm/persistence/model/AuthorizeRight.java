package com.hotent.bpm.persistence.model;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;





/**
 * 对象功能:流程授权
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 09:00:53
 */
public class AuthorizeRight {
	
	/**
	 * 获取创建人的权限。
	 * @return
	 * @throws IOException 
	 */
	public static ObjectNode getCreateRight() throws IOException{
		String json="{\"m_edit\":true,\"m_del\":true,\"m_start\":false,\"m_set\":true, \"m_clean\":true, \"i_del\":false,\"i_log\":false}";
		JsonNode oldObj= JsonUtil.toJsonNode(json);
		return (ObjectNode) oldObj;
	}
	
	/**
	 * 获取超管的默认权限。
	 * @return
	 * @throws IOException 
	 */
	public static ObjectNode getAdminRight() throws IOException{
		String json="{\"m_edit\":true,\"m_del\":true,\"m_start\":true,\"m_set\":true, \"m_clean\":true, \"i_del\":true,\"i_log\":true}";
		JsonNode oldObj= JsonUtil.toJsonNode(json);
		return (ObjectNode) oldObj;
	}
	
	/**
	 * 权限进行合并。
	 * @param oldJson
	 * @param newJson
	 * @return
	 * @throws IOException 
	 */
	public static ObjectNode mergeJson(ObjectNode oldObj,String newJson) throws IOException{
		JsonNode newObj= JsonUtil.toJsonNode(newJson);
		Iterator<Entry<String, JsonNode>> newSet= newObj.fields();
        while (newSet.hasNext())  
        {  
            Entry<String, JsonNode> ent = newSet.next();  
            String key=ent.getKey();
			boolean val= (Boolean) ent.getValue().asBoolean();
			if(val){
				oldObj.put(key, val);
			}
        }  
		return oldObj;
	}

}