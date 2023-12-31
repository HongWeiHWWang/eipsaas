package com.hotent.bpm.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;

public class BpmIdentityUtil
{
	/**
			 * 将数据 [{nodeId:"userTask1",executors:[{id:"",name:""},{id:"",name:""}]}],返回为
			 * 对象执行人。
			 * @param executors
			 * @return runtime/task/v1/canLock
			 * Map<String,List<BpmIdentity>>
	 * @throws IOException 
			 */
			public static Map<String,List<BpmIdentity>> getBpmIdentity(String executors) throws IOException{
				Map<String,List<BpmIdentity>> map=new HashMap<String, List<BpmIdentity>>();
				if(StringUtil.isEmpty(executors)) return map;
				ArrayNode ArrayNode = (ArrayNode) JsonUtil.toJsonNode(executors);
				for(Object obj:ArrayNode){
					ObjectNode jsonNode = (ObjectNode) JsonUtil.toJsonNode(obj);
					if (!jsonNode.hasNonNull("nodeId") || StringUtil.isEmpty(jsonNode.get("nodeId").asText())) {
						continue;
					}
					String nodeId=jsonNode.get("nodeId").asText();
					ArrayNode users= (ArrayNode) JsonUtil.toJsonNode(jsonNode.get("executors"));
					List<BpmIdentity> userList=new ArrayList<BpmIdentity>();
					for(Object userObj:users){
						ObjectNode user = (ObjectNode) JsonUtil.toJsonNode(userObj);
						BpmIdentity bpmInentity=(BpmIdentity) DefaultBpmIdentity.getIdentityByUserId(user.get("id").asText()
								, user.get("name").asText());
						
						userList.add(bpmInentity);
					}
					map.put(nodeId, userList);
					//for()
				}
				return map;
			}
			

			/**
			 * 将数据 [{executors:[{id:"",name:""},{id:"",name:""}]}],返回为
			 * 对象执行人。
			 * @param executors
			 * @return 
			 * List<BpmIdentity>
			 * @throws IOException 
			 */
			public static List<BpmIdentity> getNextNodeBpmIdentity(String executors) throws IOException{
				List<BpmIdentity> userList=new ArrayList<BpmIdentity>();
				if(StringUtil.isEmpty(executors)) return userList;
				ArrayNode ArrayNode = (ArrayNode) JsonUtil.toJsonNode(executors);
				for(Object obj:ArrayNode){
					ObjectNode jsonNode = (ObjectNode) JsonUtil.toJsonNode(obj);
					JsonNode executorsOjb = jsonNode.get("executors");
					if (BeanUtils.isEmpty(executorsOjb)) {
						continue;
					}
					ArrayNode users=JsonUtil.getMapper().createArrayNode();
					if (executorsOjb instanceof ArrayNode) {
						users = (ArrayNode) executorsOjb;
					}else if (executorsOjb instanceof TextNode) {
						users =(ArrayNode) JsonUtil.toJsonNode(executorsOjb.asText());
					}
					for(Object userObj:users){
						ObjectNode user = (ObjectNode) JsonUtil.toJsonNode(userObj);
						BpmIdentity bpmInentity=(BpmIdentity) DefaultBpmIdentity.getIdentityByUserId(user.get("id").asText()
								, user.get("name").asText());
						
						userList.add(bpmInentity);
					}
				}
				return userList;
			}
			
			public static List<BpmIdentity> qualfields2BpmIdentity(String qualfieds) throws Exception{
				List<BpmIdentity> result = new ArrayList<BpmIdentity>();
				if(StringUtil.isEmpty(qualfieds)){
					return result;
				}
				JsonNode parse = JsonUtil.toJsonNode(qualfieds);
				if(BeanUtils.isNotEmpty(parse) && parse.isArray()){
					ArrayNode ArrayNode = (ArrayNode) parse;
					for (JsonNode jsonNode : ArrayNode) {
						if(BeanUtils.isEmpty(jsonNode) || !jsonNode.isObject()){
							continue;
						}
						ObjectNode jobject = (ObjectNode) jsonNode;
						if(BeanUtils.isNotEmpty(jobject.get("type")) && jobject.get("type").isTextual()){
							result.add(new DefaultBpmIdentity(jobject.get("id").asText(), jobject.get("name").asText(), jobject.get("type").asText()));
						}
					}
				}
				return result;
			}

}

