package com.hotent.bpm.persistence.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.persistence.dao.BpmDefAuthorizeDao;
import com.hotent.bpm.persistence.manager.BpmDefActManager;
import com.hotent.bpm.persistence.manager.BpmDefAuthorizeManager;
import com.hotent.bpm.persistence.manager.BpmDefAuthorizeTypeManager;
import com.hotent.bpm.persistence.manager.BpmDefUserManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.CurrentUserService;
import com.hotent.bpm.persistence.model.AuthorizeRight;
import com.hotent.bpm.persistence.model.BpmDefAct;
import com.hotent.bpm.persistence.model.BpmDefAuthorize;
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType;
import com.hotent.bpm.persistence.model.BpmDefUser;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;


/**
 * 对象功能:流程定义权限明细 Service类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 10:10:50
 */
@Service
public class BpmDefAuthorizeManagerImpl extends BaseManagerImpl<BpmDefAuthorizeDao, BpmDefAuthorize> implements BpmDefAuthorizeManager{

	@Resource
	private BpmDefUserManager bpmDefUserManager;
	@Resource
	private BpmDefActManager bpmDefActManager;
	@Resource
	private BpmDefAuthorizeTypeManager bpmDefAuthorizeTypeManager;
	@Resource
	private BpmDefAuthorizeManager bpmDefAuthorizeManager;
	@Resource
	private BpmDefinitionManager defaultBpmDefinitionManager;
	@Resource(name="bpmCurrentUserService")
	private CurrentUserService currentUserService;
	
	
	
	/**
	 * 获取流程分管授权列表信息
	 * @param queryFilter
	 * @return 
	 * List<BpmDefAuthorize>
	 */
	public PageList<BpmDefAuthorize> getAuthorizeListByFilter(QueryFilter queryFilter){
		PageList<BpmDefAuthorize> list= (PageList<BpmDefAuthorize>) this.query(queryFilter);
		return  list;
	}
	
	
	
	/**
	 * 获取流程分管授权所有信息
	 * @param id
	 * @return 
	 * BpmDefAuthorize
	 */
	public BpmDefAuthorize getAuthorizeById(String id){
		BpmDefAuthorize bpmDefAuthorize = getAuthorizeById(id,true);
		return bpmDefAuthorize;
	}
	

	/**
	 * 根据参数内容获取流程分管授权所有信息(即bpm_def_authorize及其相关表的所有授权信息)
	 * @param id
	 * @param isNeedjson
	 * @return 
	 * BpmDefAuthorize
	 */
	public BpmDefAuthorize getAuthorizeById(String id,boolean isNeedjson){
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("authorizeId", id);
		params.put("objType", BpmDefUser.BPMDEFUSER_OBJ_TYPE.BPM_DEF);
		
		BpmDefAuthorize	bpmDefAuthorize = bpmDefAuthorizeManager.get(id);
		
		//获取授权类型信息
		List<BpmDefAuthorizeType> bpmDefAuthorizeTypeList = bpmDefAuthorizeTypeManager.getAll(params);
		bpmDefAuthorize.setBpmDefAuthorizeTypeList(bpmDefAuthorizeTypeList);
		
		//获取子表授权用户信息
		List<BpmDefUser> bpmDefUserList = bpmDefUserManager.getAll(params);
		bpmDefAuthorize.setBpmDefUserList(bpmDefUserList);

		//获取子表授权流程信息
		List<BpmDefAct> bpmDefActList = bpmDefActManager.getAll(params);
		bpmDefAuthorize.setBpmDefActList(bpmDefActList);
		
		//子表信息需要转JSON数据时
		if(isNeedjson){
			String ownerNameJson = toOwnerNameJson(bpmDefUserList);
			bpmDefAuthorize.setOwnerNameJson(ownerNameJson);
		
			String multipe=bpmDefAuthorize.getMultiple();
			String  defNameJson = toDefNameJson(bpmDefActList);
			
			if("1".equals(multipe)) {
				bpmDefAuthorize.setDefNameJson(defNameJson);
			}
			if("2".equals(multipe)) {
				bpmDefAuthorize.setDefAllNameJson(defNameJson);
			}
		}
		return bpmDefAuthorize;
	}
	
	
	
	/**
	 * 按ID数据删除流程分管授权所有信息
	 * @param bpmDefAuthorize
	 * @return 
	 */
	@Override
    @Transactional
	public void deleteAuthorizeByIds(String[] lAryId){
		for (String id : lAryId){
			//删除授权主表信息
			bpmDefAuthorizeManager.remove(id);
			//删除原来的从表信息，包括授权类型表、被授权用户子表及流程权限子表
			bpmDefAuthorizeTypeManager.delByAuthorizeId(id);
			bpmDefUserManager.delByAuthorizeId(id,BpmDefUser.BPMDEFUSER_OBJ_TYPE.BPM_DEF);
			bpmDefActManager.delByAuthorizeId(id);
		}
	}
	
	
	/**
	 * 保存或修改流程分管授权所有信息
	 * @param bpmDefAuthorize
	 * @return 
	 * Long
	 * @throws IOException 
	 */
	@Override
    @Transactional
	public String saveOrUpdateAuthorize(BpmDefAuthorize bpmDefAuthorize) throws IOException{
		//保存或修改流程分管授权主表信息（如果是修改的话先删除原来的从表信息，包括被授权用户子表及流程权限子表）
		String authorizeId = bpmDefAuthorize.getId();
		if(StringUtil.isNotEmpty(authorizeId)){
			bpmDefAuthorizeManager.update(bpmDefAuthorize);
			//删除原来的从表信息，包括授权类型表、被授权用户子表及流程权限子表
			bpmDefAuthorizeTypeManager.delByAuthorizeId(authorizeId);
			bpmDefUserManager.delByAuthorizeId(authorizeId,BpmDefUser.BPMDEFUSER_OBJ_TYPE.BPM_DEF);
			bpmDefActManager.delByAuthorizeId(authorizeId);
			
		}else{
			authorizeId = UniqueIdUtil.getSuid();
			bpmDefAuthorize.setId(authorizeId);
			bpmDefAuthorizeManager.create(bpmDefAuthorize);
			
		}

		//保存分管授权类型表信息
		String authorizeTypes = bpmDefAuthorize.getAuthorizeTypes();
		List<BpmDefAuthorizeType> bpmDefAuthorizeTypeList = toBpmDefAuthorizeTypeList(authorizeTypes, authorizeId);
		for (BpmDefAuthorizeType bpmDefAuthorizeType : bpmDefAuthorizeTypeList){
			bpmDefAuthorizeTypeManager.create(bpmDefAuthorizeType);
		}
		
		//保存流程分管授权用户子表信息
		String myOwnerNameJson = bpmDefAuthorize.getOwnerNameJson();
		List<BpmDefUser> bpmDefUserList = toBpmDefUserList(myOwnerNameJson, authorizeId);
		for (BpmDefUser bpmDefUser : bpmDefUserList){
			bpmDefUser.setObjType(BpmDefUser.BPMDEFUSER_OBJ_TYPE.BPM_DEF);
			bpmDefUserManager.create(bpmDefUser);
		}
		
		//保存流程分管授权流程子表信息
		String myDefNameJson ="";
		if(bpmDefAuthorize.getMultiple().equals("1")) {//流程
			myDefNameJson=bpmDefAuthorize.getDefNameJson();
		}
		if(bpmDefAuthorize.getMultiple().equals("2")) {//流程分类
			myDefNameJson=bpmDefAuthorize.getDefAllNameJson();
		}
		
		List<BpmDefAct> bpmDefActList = toBpmDefActList(myDefNameJson, authorizeId);
		for (BpmDefAct bpmDefAct : bpmDefActList){
			bpmDefAct.setType(bpmDefAuthorize.getMultiple());
			bpmDefActManager.create(bpmDefAct);
		}
		
		return authorizeId;
	}
	
	/**
	 * 以逗号隔开授权类型字符串转成授权类型列表
	 * @param authorizeTypes
	 * 格式 {"start":false,"management":true,"task":false,"instance":false}
	 * @param authorizeId 授权主表ID
	 * @return 
	 * List<BpmDefAuthorizeType>
	 * @throws IOException 
	 */
	private List<BpmDefAuthorizeType> toBpmDefAuthorizeTypeList(String authorizeTypes,String authorizeId) throws IOException{
		List<BpmDefAuthorizeType> authTypeList = new ArrayList<BpmDefAuthorizeType>();
		if(StringUtil.isEmpty(authorizeTypes)) return authTypeList;
		//{"start":false,"management":true,"task":false,"instance":false}
		ObjectNode jsonObject=(ObjectNode) JsonUtil.toJsonNode(authorizeTypes);
		Iterator<Entry<String, JsonNode>> fields = jsonObject.fields();
		 while (fields.hasNext())    {  
	            Entry<String, JsonNode> ent = fields.next();  
	            boolean blnSet=ent.getValue().asBoolean();
				if(!blnSet) continue;
				BpmDefAuthorizeType authType = new BpmDefAuthorizeType();
				authType.setId(UniqueIdUtil.getSuid());
				authType.setAuthorizeId(authorizeId);
				authType.setAuthorizeType(ent.getKey());
				authTypeList.add(authType);
	        }  
		return authTypeList;
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
		ArrayNode jsonArray=JsonUtil.getMapper().createArrayNode();
		
		for (Map.Entry<String, List<BpmDefUser>> entry : map.entrySet()) {
			ObjectNode json= userEntToJson(entry,userTypeMap);
			jsonArray.add(json);
		}
		return jsonArray.toString();
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
		String authOrg = "";
		String authOrgName = "";
		
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
			authOrg = user.getAuthOrg();
			authOrgName = user.getAuthOrgName();
		}
		jsonObj.put("id", ids);
		jsonObj.put("name", names);
		jsonObj.put("authOrg", authOrg);
		jsonObj.put("authOrgName", authOrgName);
		
		return jsonObj;
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
		String authOrg = "";
		String authOrgName = "";
		if(BeanUtils.isNotEmpty(json.get("authOrg"))){
			authOrg = json.get("authOrg").asText();
        }
        if(BeanUtils.isNotEmpty(json.get("authOrgName"))){
        	authOrgName = json.get("authOrgName").asText();
        }
		if("everyone".equals(type)){
			BpmDefUser defUser = new BpmDefUser();
	        defUser.setId(UniqueIdUtil.getSuid());
	        defUser.setAuthorizeId(authorizeId);
	        defUser.setRightType(type);
	        defUser.setAuthOrg(authOrg);
	        defUser.setAuthOrgName(authOrgName);
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
		        defUser.setAuthOrg(authOrg);
		        defUser.setAuthOrgName(authOrgName);
		        bpmDefUsers.add(defUser);
			}
		}
		return bpmDefUsers;
	}
	
	
	/**
	 * 授权流程列表转成授权流程JSON（仅一个authorize_id_时的流程列表）
	 * @param defActList
	 * @return 
	 * String
	 */
	public String toDefNameJson(List<BpmDefAct> defActList){
		if(BeanUtils.isEmpty(defActList) ){
			return "[]";
		}
		ArrayNode jsonArray = JsonUtil.getMapper().createArrayNode(); 
		
		Map<String,String> sysTypeMap = null;
		for(BpmDefAct act:defActList){
			ObjectNode jsonObj=JsonUtil.getMapper().createObjectNode();
			jsonObj.put("defKey", act.getDefKey());
			jsonObj.put("defName", act.getDefName());
			if ("1".equals(act.getType())) {
				DefaultBpmDefinition definition =defaultBpmDefinitionManager.getMainByDefKey(act.getDefKey(), false);
				if (BeanUtils.isNotEmpty(definition)) {
					jsonObj.put("defName", definition.getName());
				}
			}else{
				if (sysTypeMap == null) {
					PortalFeignService portalFeignService = AppUtil.getBean(PortalFeignService.class);
					QueryFilter queryFilter = QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
					queryFilter.addFilter("type_group_key_", "FLOW_TYPE", QueryOP.EQUAL);
					ObjectNode allFlowType = portalFeignService.getAllSysType(queryFilter);
					sysTypeMap = new HashMap<>();
					if (BeanUtils.isNotEmpty(allFlowType) && BeanUtils.isNotEmpty(allFlowType.get("rows"))) {
						for (JsonNode jsonNode : allFlowType.get("rows")) {
							sysTypeMap.put(jsonNode.get("id").asText(), jsonNode.get("name").asText());
						}
					}
				}
				if (BeanUtils.isNotEmpty(sysTypeMap.get(act.getDefKey()))) {
					jsonObj.put("defName", sysTypeMap.get(act.getDefKey()));
				}
			}
			jsonObj.put("right", act.getRightContent());
			jsonArray.add(jsonObj);
		}
		return jsonArray.toString();
	}
	
	
	
	/**
	 * 授权流程JSON转成授权流程列表
	 * @param defNameJson
	 * @param authorizeId
	 * @return 
	 * List<BpmDefAct>
	 * JSON格式：
	 * 	  [
	 * 	   { 
	 *         "defKey":"zchz",
	 *         "defName":"周程汇总",
	 *         "right":{"m_edit":"Y","m_del":"N","m_start":"Y","m_set":"N"}
	 *       },
	 *       {
	 *        "defKey":"csjdsz",
	 *        "defName":"测试节点设置",
	 *        "right":{"m_edit":"Y","m_del":"N","m_start":"Y","m_set":"N"}
	 *       },
	 *       {
	 *        "defKey":"gxzlc",
	 *        "defName":"共享子流程",
	 *        "right":{"m_edit":"Y","m_del":"N","m_start":"Y","m_set":"N"}
	 *        }
	 *      ]
	 * @throws IOException 
	 */
	private List<BpmDefAct> toBpmDefActList(String defNameJson, String authorizeId) throws IOException{
		List<BpmDefAct> myBpmDefActList = new ArrayList<BpmDefAct>();
		if(StringUtil.isEmpty(defNameJson)){
			return myBpmDefActList;
		}
		
		ArrayNode myJsonArray = (ArrayNode) JsonUtil.toJsonNode(defNameJson);
		//分析JSON,生成对应的BpmDefAct对象
		for(int i = 0; i < myJsonArray.size(); i++){     
            ObjectNode jsonObject = (ObjectNode) myJsonArray.get(i);     
            BpmDefAct bpmDefAct = new BpmDefAct();
           
        	String defKey = jsonObject.get("defKey").asText();
        	bpmDefAct.setDefKey(defKey);
        	String defName = jsonObject.get("defName").asText();
            bpmDefAct.setDefName(defName);
        	String rightContent = JsonUtil.toJson(jsonObject.get("right"));
        	bpmDefAct.setRightContent(rightContent);
        	
            bpmDefAct.setId(UniqueIdUtil.getSuid());
            bpmDefAct.setAuthorizeId(authorizeId);
            myBpmDefActList.add(bpmDefAct);
        } 
		
		return myBpmDefActList;
	}

	

	/**
	 * 查询自己相关的分管授权的流程权限
	 * @param userId 指定用户ID
	 * @param authorizeType 授权类型(management,task,start,instance)
	 * @param isRight（是否包括流程操作细化的权限）
	 * @param isMyDef（是否包括自己创建的流程的所有权限，即自己创建的流程就拥有所有权限）
	 * @return 
	 * <pre>
	 * Map<String,Object> :包括：  defKeys授权的流程定义key 和authorizeRightMap对象
	 * defKeys:授权定义的KEY，以逗号隔开
	 * authorizeRightMap：流程授权的对象，即Map<String,ObjectNode> 
	 * 	键：流程定义KEY
	 *  值：authorizeRight 流程明细权限 {"m_edit":true,"m_edit":true}
	 * </pre>
	 * @throws IOException 
	 */	
	@Override
	public Map<String, Object> getActRightByUserId(String userId,String authorizeType, boolean isRight, boolean isMyDef) throws IOException {
		Map<String,Set<String>> userRightMap=currentUserService.getUserRightMap();
		//用户权限列表
		Map<String, String> userRightMapStr=currentUserService.getMapStringByMayList(userRightMap);
		
		return getActRightByRightMapAndUserId(userId,authorizeType,isRight,isMyDef,userRightMapStr);
	}

	/**
	 * 根据传入的用户权限map获取分管授权的流程
	 */
	@Override
	public Map<String, Object> getActRightByRightMapAndUserId(String userId,String authorizeType, boolean isRight, boolean isMyDef ,Map<String, String> userRightMapStr) throws IOException {
		String defKeys = "";
		//转换流程授权的内容
		Map<String,ObjectNode> authorizeRightMap = new HashMap<String, ObjectNode>();
		
		//查询自己创建的流程
		if(isMyDef){
			List<String> myDefKeys = defaultBpmDefinitionManager.queryByCreateBy(userId);
			if(myDefKeys!=null && myDefKeys.size()>0){
				//如果需要所有权限的就直接虚拟一个有处理权限的对象
				for (String defKey : myDefKeys){
					defKeys += "'"+defKey+"',";
					if(!isRight) continue;
					authorizeRightMap.put(defKey, AuthorizeRight.getCreateRight());
				}
			}
		}
		
		//获取流程授权的列表内容
		List<BpmDefAct> list = bpmDefActManager.getActRightByUserMap(userRightMapStr,authorizeType,null);
		if(list.size()>0){
			for (BpmDefAct bpmDefAct : list){
				String type=bpmDefAct.getType();
				String defKey = "";
				String rightContent="";
				ObjectNode authorizeRight=null;
				if(type.equals("1")) {
					defKey=bpmDefAct.getDefKey();
					defKeys += "'"+defKey+"',";
					if(!isRight) continue;
					
					rightContent = bpmDefAct.getRightContent();
					authorizeRight = authorizeRightMap.get(defKey);
					if(authorizeRight!=null){
						AuthorizeRight.mergeJson(authorizeRight, rightContent);
					}else{
						authorizeRight=(ObjectNode) JsonUtil.toJsonNode(rightContent);
						authorizeRightMap.put(defKey, authorizeRight);
					}
				}
				if(type.equals("2")) {
					List<String> typeIds = Arrays.asList(new String[]{bpmDefAct.getDefKey()});
					List<String> typeDefKeys = defaultBpmDefinitionManager.queryByTypeId(typeIds);
					if(typeDefKeys!=null && typeDefKeys.size()>0) {
						for(String def : typeDefKeys) {
							if(!isRight) continue;
							
							rightContent = bpmDefAct.getRightContent();
							authorizeRight = authorizeRightMap.get(def);
							if(authorizeRight!=null){
								AuthorizeRight.mergeJson(authorizeRight, rightContent);
							}else{
								authorizeRight=(ObjectNode) JsonUtil.toJsonNode(rightContent);
								authorizeRightMap.put(def, authorizeRight);
							}
							defKey+="'"+def+"',";
						}
					}
					if(StringUtil.isNotEmpty(defKey)) {
						defKeys += defKey;
					}
				}
			}
		}
		if(StringUtil.isNotEmpty(defKeys)){
			defKeys = defKeys.substring(0, defKeys.length()-1);
		}
		
		Map<String,Object>  resultMap=new HashMap<String,Object>();
		resultMap.put("defKeys", defKeys);
		resultMap.put("authorizeRightMap", authorizeRightMap);
		return resultMap;
	}


	@Override
	public ObjectNode getRight(String flowKey, String authorizeType) throws IOException {
		Map<String,Set<String>> userRightMap=currentUserService.getUserRightMap();
		//用户权限列表
		Map<String, String> userRightMapStr=currentUserService.getMapStringByMayList(userRightMap);
		ObjectNode resultJson=null;
		//获取流程授权的列表内容
		List<BpmDefAct> list =bpmDefActManager.getActRightByUserMap(userRightMapStr,authorizeType,flowKey);
		if(BeanUtils.isEmpty(list)) return null;
		
		for (BpmDefAct bpmDefAct : list){
			String rightContent = bpmDefAct.getRightContent();
			if(resultJson!=null){
				AuthorizeRight.mergeJson(resultJson, rightContent);
			}
			else{
				resultJson=(ObjectNode) JsonUtil.toJsonNode(rightContent);
			}
		}
		return resultJson;
	}
}
