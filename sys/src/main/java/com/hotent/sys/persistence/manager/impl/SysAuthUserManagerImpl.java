package com.hotent.sys.persistence.manager.impl;

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
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.CacheKeyConst;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.sys.persistence.dao.SysAuthUserDao;
import com.hotent.sys.persistence.manager.CurrentUserService;
import com.hotent.sys.persistence.manager.SysAuthUserManager;
import com.hotent.sys.persistence.model.SysAuthUser;
import com.hotent.uc.api.service.IUserService;

/**
 * 对象功能:流程定义权限明细 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 14:10:50
 */
@Service
public class SysAuthUserManagerImpl extends BaseManagerImpl<SysAuthUserDao,  SysAuthUser> implements  SysAuthUserManager{

	@Resource
	private CurrentUserService currentUserService;
	@Resource
	IUserService iUserService;



	@Override
	public ArrayNode getRights(String authorizeId,String objType) throws IOException {
		String ownerNameJson = "[]";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("authorizeId", authorizeId);
		params.put("objType", objType);
		List<SysAuthUser> bpmDefUsers = baseMapper.getAll(params);
		ownerNameJson = toOwnerNameJson(bpmDefUsers);
		return (ArrayNode) JsonUtil.toJsonNode(ownerNameJson);
	}

	@Override
	public void saveRights(String authorizeId,String objType,String ownerNameJson) throws IOException {
		if(StringUtil.isNotEmpty(ownerNameJson)){
			baseMapper.delByAuthorizeId(authorizeId,objType);
			List<SysAuthUser> bpmDefUserList = toBpmDefUserList(ownerNameJson, authorizeId);
			for (SysAuthUser bpmDefUser : bpmDefUserList){
				bpmDefUser.setObjType(objType);
				this.create(bpmDefUser);
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
	private List<SysAuthUser> toBpmDefUserList(String ownNameJson,String authorizeId) throws IOException{
		List<SysAuthUser> userList = new ArrayList<SysAuthUser>();
		if(StringUtil.isEmpty(ownNameJson)){
			return userList;
		}
		
		ArrayNode aryJson=(ArrayNode) JsonUtil.toJsonNode(ownNameJson);
		for(Object obj:aryJson){
			ObjectNode jsonObject=(ObjectNode)obj;
			List<SysAuthUser>  list= getList(jsonObject, authorizeId);
			userList.addAll(list);
		}
		return userList;
	}
	
	private List<SysAuthUser> getList(ObjectNode json,String authorizeId){
		List<SysAuthUser> bpmDefUsers = new ArrayList<SysAuthUser>();
		String type=json.get("type").asText();
		
		if("everyone".equals(type)){
			SysAuthUser defUser = new SysAuthUser();
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
				SysAuthUser defUser = new SysAuthUser();
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
	private String toOwnerNameJson(List<SysAuthUser> bpmDefUsers){
		if(BeanUtils.isEmpty(bpmDefUsers)) return "[]";
		Map<String,List<SysAuthUser>> map = new HashMap<String, List<SysAuthUser>>();
		
		Map<String,String> userTypeMap= currentUserService.getUserTypeMap(CurrentUserService.DEFAULT_OBJECT_RIGHTTYPE_BEAN);
		
		
		for(SysAuthUser user:bpmDefUsers){
			String rightType=user.getRightType();
			if(map.containsKey(rightType)){
				List<SysAuthUser> list=map.get(rightType);
				list.add(user);
			}
			else{
				List<SysAuthUser> list =new ArrayList<SysAuthUser>();
				list.add(user);
				map.put(rightType, list);
			}
		}
		ArrayNode ArrayNode=JsonUtil.getMapper().createArrayNode();
		
		for (Map.Entry<String, List<SysAuthUser>> entry : map.entrySet()) {
			ObjectNode json= userEntToJson(entry,userTypeMap);
			ArrayNode.add(json);
		}
		return ArrayNode.toString();
	}
	
	
	private ObjectNode userEntToJson(Map.Entry<String, List<SysAuthUser>> entry,Map<String,String> userTypeMap){
		ObjectNode jsonObj=JsonUtil.getMapper().createObjectNode();
		String type=entry.getKey();
		String title=userTypeMap.get(type);
		jsonObj.put("type", type);
		jsonObj.put("title", title);
		if(type.equals("everyone")) {
			return jsonObj;
		}
		List<SysAuthUser> list=entry.getValue(); 
		String ids="";
		String names="";
		
		for(int i=0;i<list.size();i++){
			SysAuthUser user=list.get(i);
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
	@CacheEvict(value = CacheKeyConst.USER_MENU_CACHENAME, key = "#userId")
	public void delUserMenuCache(String userId) {}
}
