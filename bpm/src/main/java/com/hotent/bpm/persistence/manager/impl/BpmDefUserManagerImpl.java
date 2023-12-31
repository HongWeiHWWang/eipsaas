package com.hotent.bpm.persistence.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.persistence.dao.BpmDefUserDao;
import com.hotent.bpm.persistence.manager.BpmDefUserManager;
import com.hotent.bpm.persistence.manager.CurrentUserService;
import com.hotent.bpm.persistence.model.BpmDefUser;
import org.springframework.transaction.annotation.Transactional;


/**
 * 对象功能:流程定义权限明细 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 14:10:50
 */
@Service
public class BpmDefUserManagerImpl extends BaseManagerImpl<BpmDefUserDao,  BpmDefUser> implements  BpmDefUserManager{

	@Resource(name="bpmCurrentUserService")
	private CurrentUserService currentUserService;


	@Override
	public ArrayNode getRights(String authorizeId,String objType) throws IOException {
		String ownerNameJson = "[]";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("authorizeId", authorizeId);
		params.put("objType", objType);
		List<BpmDefUser> bpmDefUsers = baseMapper.getAll(params);
		ownerNameJson = toOwnerNameJson(bpmDefUsers);
		return (ArrayNode) JsonUtil.toJsonNode(ownerNameJson);
	}

	@Override
    @Transactional
	public void saveRights(String authorizeId,String objType,String ownerNameJson) throws IOException {
		// TODO Auto-generated method stub
		if(StringUtil.isNotEmpty(ownerNameJson)){
			baseMapper.delByAuthorizeId(authorizeId,objType);
			List<BpmDefUser> bpmDefUserList = toBpmDefUserList(ownerNameJson, authorizeId);
			for (BpmDefUser bpmDefUser : bpmDefUserList){
				bpmDefUser.setObjType(objType);
				super.create(bpmDefUser);
			}
		}
	}
	
	/**
	 * 授权人员JSON转成授权人员列表
	 * @param ownNameJson
	 * @param authorizeId
	 * @return 
	 * List<BpmDefUser>
	 * 以下为JSON格式：
	 * [{type:"everyone"},{type:"user",id:"",name:""}]
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	private List<BpmDefUser> toBpmDefUserList(String ownNameJson,String authorizeId) throws IOException{
		List<BpmDefUser> userList = new ArrayList<BpmDefUser>();
		if(StringUtil.isEmpty(ownNameJson)){
			return userList;
		}
		
		ArrayNode aryJson=(ArrayNode) JsonUtil.toJsonNode(ownNameJson);
		for(Object obj:aryJson){
			ObjectNode jsonObject=(ObjectNode)obj;
			List<BpmDefUser>  list= getList(jsonObject, authorizeId);
			userList.addAll(list);
		}
		return userList;
	}
	
	private List<BpmDefUser> getList(ObjectNode json,String authorizeId){
		List<BpmDefUser> bpmDefUsers = new ArrayList<BpmDefUser>();
		String type=json.get("type").asText();
		
		if("everyone".equals(type)){
			BpmDefUser defUser = new BpmDefUser();
	        defUser.setId(UniqueIdUtil.getSuid());
	        defUser.setAuthorizeId(authorizeId);
	        defUser.setRightType(type);
	        bpmDefUsers.add(defUser);
		}
		else{
			String ids=json.get("id").asText();
			String names=json.get("name").asText();
			
			String[] aryId=ids.split(",");
			String[] aryName=names.split(",");
			for(int i=0;i<aryId.length;i++){
				BpmDefUser defUser = new BpmDefUser();
		        defUser.setId(UniqueIdUtil.getSuid());
		        defUser.setAuthorizeId(authorizeId);
		        defUser.setRightType(type);
		        defUser.setOwnerId(aryId[i]);
		        defUser.setOwnerName(aryName[i]);
		        
		        bpmDefUsers.add(defUser);
			}
		}
		return bpmDefUsers;
	}
	
	
	/**
	 * 授权人员列表转成按RightType分组授权人员JSON (单个authorize_id_的人员列表)
	 * [{type:"everyone"},{type:"user",id:"",name:""}]
	 * @param myBpmDefUserList
	 * @return 
	 * String
	 */
	private String toOwnerNameJson(List<BpmDefUser> bpmDefUsers){
		if(BeanUtils.isEmpty(bpmDefUsers)) return "[]";
		Map<String,List<BpmDefUser>> map = new HashMap<String, List<BpmDefUser>>();
		
		Map<String,String> userTypeMap= currentUserService.getUserTypeMap(CurrentUserService.DEFAULT_OBJECT_RIGHTTYPE_BEAN);
		
		
		for(BpmDefUser user:bpmDefUsers){
			String rightType=user.getRightType();
			if(map.containsKey(rightType)){
				List<BpmDefUser> list=map.get(rightType);
				list.add(user);
			}
			else{
				List<BpmDefUser> list =new ArrayList<BpmDefUser>();
				list.add(user);
				map.put(rightType, list);
			}
		}
		ArrayNode ArrayNode=JsonUtil.getMapper().createArrayNode();
		
		for (Map.Entry<String, List<BpmDefUser>> entry : map.entrySet()) {
			ObjectNode json= userEntToJson(entry,userTypeMap);
			ArrayNode.add(json);
		}
		return ArrayNode.toString();
	}
	
	
	private ObjectNode userEntToJson(Map.Entry<String, List<BpmDefUser>> entry,Map<String,String> userTypeMap){
		ObjectNode jsonObj=JsonUtil.getMapper().createObjectNode();
		String type=entry.getKey();
		String title=userTypeMap.get(type);
		jsonObj.put("type", type);
		jsonObj.put("title", title);
		if(type.equals("everyone")) {
			return jsonObj;
		}
		List<BpmDefUser> list=entry.getValue(); 
		String ids="";
		String names="";
		
		for(int i=0;i<list.size();i++){
			BpmDefUser user=list.get(i);
			if(i==0){
				ids+=user.getOwnerId();
				names+=user.getOwnerName();
			}
			else{
				ids+="," +user.getOwnerId();
				names+="," + user.getOwnerName();
			}
		}
		jsonObj.put("id", ids);
		jsonObj.put("name", names);
		
		return jsonObj;
	}


	@Override
	public List<String> getAuthorizeIdsByUserMap(String objType) {
		// 获得流程分管授权与用户相关的信息集合的流程权限内容
		Map<String,Set<String>> userRightMap=currentUserService.getUserRightMap();
		//用户权限列表
		Map<String, String> userRightMapStr=currentUserService.getMapStringByMayList(userRightMap);
		List<String> list = baseMapper.getAuthorizeIdsByUserMap(userRightMapStr,objType);
		return list;
	}
	

	@Override
	public boolean hasRights(String authorizeId) {
		// 获得流程分管授权与用户相关的信息集合的流程权限内容
		Map<String,Set<String>> userRightMap=currentUserService.getUserRightMap();
		//用户权限列表
		Map<String, String> userRightMapStr=currentUserService.getMapStringByMayList(userRightMap);
		List<String> list = baseMapper.getAuthByAuthorizeId(userRightMapStr,authorizeId);
		if(BeanUtils.isNotEmpty(list)){
			return true;
		}
		return false;
	}
	
	@Override
	public List<BpmDefUser> getByUserMap(String objType) {
		// 获得流程分管授权与用户相关的信息集合的流程权限内容
		Map<String,Set<String>> userRightMap =currentUserService.getUserRightMap();
		//用户权限列表
		Map<String, String> userRightMapStr=currentUserService.getMapStringByMayList(userRightMap);
		return baseMapper.getByUserMap(userRightMapStr , objType);
	}

	@Override
    @Transactional
	public void delByAuthorizeId(String authorizeId, String bpmDef) {
		baseMapper.delByAuthorizeId(authorizeId, bpmDef);
	}

	@Override
	public List<BpmDefUser> getAll(Map<String, Object> params) {
		return baseMapper.getAll(params);
	}
}
