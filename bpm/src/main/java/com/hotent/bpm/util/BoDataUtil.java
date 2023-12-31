package com.hotent.bpm.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.context.BpmContextUtil;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.service.DataObjectHandler;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;



public class BoDataUtil {
	
	
	
	
	/**
	 * 验证BO必填。
	 * @param bpmProcessDefExt
	 * @param jsonObj 
	 * void
	 */
	public static void validBo(DefaultBpmProcessDefExt bpmProcessDefExt,ObjectNode jsonObj){
		
		List<ProcBoDef> list= bpmProcessDefExt.getBoDefList();
		if(BeanUtils.isEmpty(list)){
			throw new RuntimeException("流程没有定义Bo列表");
		}
		for(ProcBoDef boDef :list){
			String name = boDef.getName();
			if(!boDef.isRequired()) continue;

			if(!JsonUtil.isContainsKey(jsonObj, name)){
				throw new RuntimeException("提交数据不包含:"+ boDef.getName());
			}
		}
	}
	
	
	/**
	 * 将bo数据按照bo定义组装成map。
	 * @param jsonObj
	 * @return  Map<String,String>
	 */
	public static  Map<String, ObjectNode> getMap(ObjectNode jsonObj){
		Map<String, ObjectNode> map=new HashMap<String, ObjectNode>();
		Iterator<Entry<String, JsonNode>> it= jsonObj.fields();
		
		while(it.hasNext()){
			Entry<String, JsonNode> ent=it.next();
			String key=ent.getKey();
			try {
				ObjectNode val= (ObjectNode) JsonUtil.toJsonNode(ent.getValue());
				map.put(key, val);
			} catch (Exception e) {}
		}
		return map;
	}
	
	
	/**
	 * 处理表单json数据。
	 * @param defId //只通过表单获取初始化数据 可以为空
	 * @param boDatas
	 * @return
	 * @throws Exception 
	 */
	public static ObjectNode hanlerData(String defId, List<ObjectNode> boDatas) throws Exception{
		ObjectNode jsondata = JsonUtil.getMapper().createObjectNode();
		
		DataObjectHandler dataObjectHandler=AppUtil.getBean(DataObjectHandler.class);
		if(StringUtil.isNotEmpty(defId)){
			dataObjectHandler.handShowData(defId, boDatas);
		}
		if(BeanUtils.isNotEmpty(boDatas)){
			for(ObjectNode data:boDatas){
				ObjectNode json= toJSON(data,true);
				String boDefCode = "";
				if(data.has("boDef")){
					boDefCode = data.get("boDef").get("alias").asText();
				}else{
					boDefCode = data.get("boDefAlias").asText();
				} 
				jsondata.set(boDefCode, json);
			}
		}
		return jsondata;
	}
	
	/**
	 * 处理表单数据。
	 * @param instance
	 * @param nodeId
	 * @param boDatas
	 * @return
	 */
	public static ObjectNode hanlerData(BpmProcessInstance instance,String nodeId, List<ObjectNode> boDatas) throws Exception {
		ObjectNode jsondata = JsonUtil.getMapper().createObjectNode();
		DataObjectHandler dataObjectHandler=(DataObjectHandler) AppUtil.getBean(DataObjectHandler.class);
		if (StringUtil.isNotEmpty(nodeId)) {
			dataObjectHandler.handShowData(instance, nodeId, boDatas);
		}
		//设置bo数据到上下文。
		BpmContextUtil.setBoToContext(boDatas);
		
		for(ObjectNode data:boDatas){
			ObjectNode json=toJSON(data,true);
			String boDefCode = "";
			if(data.has("boDef")){
				boDefCode = data.get("boDef").get("alias").asText();
			}else{
				boDefCode = data.get("boDefAlias").asText();
			} 
			jsondata.set(boDefCode, json);
		}
		return jsondata;
	}

	/**
	 * 转成JSON数据。
	 * @param boDatas
	 * @return
	 * @throws IOException 
	 */
	public static Object hanlerData( List<ObjectNode> boDatas) throws IOException{
		ObjectNode jsondata = JsonUtil.getMapper().createObjectNode();
		for(ObjectNode data:boDatas){
			ObjectNode json= toJSON(data,true);
			String boDefCode = "";
			if(data.has("boDef")){
				boDefCode = data.get("boDef").get("alias").asText();
			}else{
				boDefCode = data.get("boDefAlias").asText();
			} 
			jsondata.set(boDefCode, json);
		}
		return jsondata;
	}
	
	/**
	 * 将JSON转成BoData
	 * @param json	json格式数据
	 * @return		bo数据
	 */
	public static JsonNode transJSON(JsonNode jsonNode) {
		ObjectNode data =JsonUtil.getMapper().createObjectNode();
		Iterator<Entry<String, JsonNode>> fields = jsonNode.fields();
		while(fields.hasNext()){
			Entry<String, JsonNode> next = fields.next();
			String key = next.getKey();
			JsonNode jNode = next.getValue();
			if(jNode.isArray()){
				boolean isCheck=false;
				if(BeanUtils.isNotEmpty(jNode)) {
					for (JsonNode fxk : jNode) {
						//为复选框  复选框从前端传过来的是数组 不添加到子表中 过滤掉
						if(!fxk.isObject()) {
							isCheck =true;
							break;
						}
					}
				}
				if(!isCheck && key.toString().indexOf("sub_") >-1) {
					String tmp=key.toString().replaceFirst("sub_", "");
					ObjectNode subNode = (ObjectNode) data.get("subMap");
					if(BeanUtils.isEmpty(subNode)){
						subNode = JsonUtil.getMapper().createObjectNode();
					}
					subNode.set(tmp, jNode);
					data.set("subMap", subNode);
				}else {
					((ObjectNode) data).set(key, jNode);
				}
			}
			else{
				((ObjectNode) data).set(key, jNode);
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
	public static ObjectNode toJSON(ObjectNode boData,boolean needInitData) throws IOException {
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
	private static ObjectNode toJSON(ObjectNode boData, Map<String,Map<String,Object>> initMap) throws IOException {
		ObjectNode objectNode = (ObjectNode) boData.get("data");
		ObjectNode subMap= (ObjectNode) boData.get("subMap");
		if(BeanUtils.isNotEmpty(subMap)){
			Iterator<Entry<String, JsonNode>> fields = subMap.fields();
			while(fields.hasNext()){
				Entry<String, JsonNode> next = fields.next();
				String key = next.getKey();
				JsonNode jNode = next.getValue();
				ArrayNode aNode = JsonUtil.getMapper().createArrayNode();
				for (JsonNode obj : jNode) {
					ObjectNode objNode = null;
					if (obj.hasNonNull("data") && (obj.get("data") instanceof  ObjectNode)) {
						objNode = (ObjectNode)obj.get("data");
					}else{
						objNode = (ObjectNode)obj;
					}
					//处理孙表
					if(BeanUtils.isNotEmpty(obj.get("subMap"))){
						JsonNode sunNodes = obj.get("subMap");
						Iterator<Entry<String, JsonNode>> sunTabFields = sunNodes.fields();
						while(sunTabFields.hasNext()){
							Entry<String, JsonNode> nextSun = sunTabFields.next();
							JsonNode sunValueNode = nextSun.getValue();
							if(BeanUtils.isNotEmpty(sunValueNode)){
								String sunKey = nextSun.getKey();
								ArrayNode sunNode = JsonUtil.getMapper().createArrayNode();
								for (JsonNode sunObj : sunValueNode) {
									if (sunObj.hasNonNull("data")) {
										sunNode.add(sunObj.get("data"));
									}else{
										sunNode.add(sunObj);
									}
								}
								objNode.set("sub_" +sunKey, sunNode);
							}
							
						}
					}
					aNode.add(objNode);
				}
				objectNode.set("sub_" +key, aNode);
			}
		}
		Map<String, Map<String, Object>> initDataMap=JsonUtil.toMap(JsonUtil.toJson(boData.get("initDataMap")));
		if(BeanUtils.isNotEmpty(initDataMap)){
			initMap.putAll(initDataMap);
		}
		return objectNode;
	}
			

}
