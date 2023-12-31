package com.hotent.bo.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.bo.model.BoData;

/**
 * BO工具类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public class BoUtil {
	/**
	 * 将JSON转成BoData
	 * @param json	json格式数据
	 * @return		bo数据
	 */
	public static BoData transJSON(JsonNode jsonNode) {
		BoData data = new BoData();
		Iterator<Entry<String, JsonNode>> fields = jsonNode.fields();
		while(fields.hasNext()){
			Entry<String, JsonNode> next = fields.next();
			String key = next.getKey();
			JsonNode jNode = next.getValue();
			if(jNode.isArray() && key.startsWith("sub_")){
				ArrayNode arys = (ArrayNode)jNode;
				arys.forEach(new Consumer<JsonNode>() {
					public void accept(JsonNode t) {
						//递归解析数组
						BoData transResult = transJSON(t);
						data.addSubRow(key.replaceFirst("sub_", ""), transResult);
					}
				});
			}
			else{
				if(jNode.isTextual()){
					data.set(key, jNode.asText());
				}else if(jNode.isInt()){
					data.set(key, jNode.asInt());
				}else if(jNode.isLong()){
					data.set(key, jNode.asLong());
				}else if(jNode.isArray()) {
                    ArrayNode arys = (ArrayNode)jNode;
                    data.set(key, arys.toString().replace("\"","" ));
				}else{
					data.set(key, jNode);
				}
				
			}
		}
		return data;
	}
	
	/**
	 * 将BoData对象转换成JSON对象
	 * @param boData	bo数据
	 * @return			json格式数据
	 * @throws IOException 
	 */
	public static ObjectNode toJSON(BoData boData,boolean needInitData) throws IOException {
		Map<String,Map<String,Object>> initMap  =new HashMap<String, Map<String,Object>>();
		ObjectNode json= toJSON(boData,initMap);
		
		if(needInitData){
			ObjectNode objectNode = JsonUtil.getMapper().createObjectNode();
			
			for (Map.Entry<String,Map<String,Object>> entry : initMap.entrySet()) {
				String key=entry.getKey();
				ObjectNode rowJson = JsonUtil.getMapper().createObjectNode();
				
				for (Map.Entry<String, Object> row : entry.getValue().entrySet()){
					Object value = row.getValue();
					JsonNode oNode = JsonUtil.toJsonNode(value);
					rowJson.set(row.getKey(), oNode);
				}
				objectNode.set(key, rowJson);
			}
			json.set("initData", objectNode);
		}
		return json;
	} 
	
	/**
	 * 将bo数据转换成json数据格式对象
	 * @param boData	bo数据
	 * @param initMap	初始化数据
	 * @return			json格式数据
	 * @throws IOException 
	 */
	private static ObjectNode toJSON(BoData boData, Map<String,Map<String,Object>> initMap) throws IOException {
		ObjectNode objectNode = JsonUtil.getMapper().createObjectNode();
		for (Map.Entry<String, Object> entry : boData.getData().entrySet()) {
			Object value = entry.getValue();
			if(BeanUtils.isNotEmpty(value)){
				objectNode.set(entry.getKey(), BeanUtils.isNotEmpty(value)?JsonUtil.toJsonNode(value):null);
			}else{
				objectNode.put(entry.getKey(), "");
			}
			
		}
		Map<String, List<BoData>> subMap= boData.getSubMap();

		for (Map.Entry<String, List<BoData>> ent : subMap.entrySet()) {
			ArrayNode aNode = JsonUtil.getMapper().createArrayNode();
			for (BoData obj : ent.getValue()) {
				aNode.add(toJSON(obj,initMap));
			}
			objectNode.set("sub_" +ent.getKey(), aNode);
		}
		initMap.putAll(boData.getInitDataMap());
		return objectNode;
	}
	
	/**
	 * 处理表单json数据。
	 * @param boDatas
	 * @return
	 * @throws Exception 
	 */
	public static ObjectNode hanlerData(List<BoData> boJsons) throws Exception{
		ObjectNode jsondata = JsonUtil.getMapper().createObjectNode();
		for (BoData boJson : boJsons) {
			ObjectNode json= toJSON(boJson,true);
			jsondata.set(boJson.getBoDefAlias(), json);
		}
		return jsondata;
	}
}
