package com.hotent.form.persistence.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.form.model.FormField;
import com.hotent.form.model.FormMeta;
import com.hotent.form.model.FormRight;
import com.hotent.form.model.FormRightXml;
import com.hotent.form.persistence.dao.FormRightDao;
import com.hotent.form.persistence.manager.FormFieldManager;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.persistence.manager.FormRightManager;
import com.hotent.uc.api.impl.util.PermissionCalc;

@Service("bpmFormRightManager")
public class FormRightManagerImpl extends BaseManagerImpl<FormRightDao, FormRight> implements FormRightManager{
	@Resource
	FormMetaManager formMetaManager;
	@Resource
	FormManager bpmFormManager;
	@Resource
	FormFieldManager formFieldManager;
	@Resource
	PermissionCalc permssionCalc;
	@Resource
	BoDefManager boDefManager;
	
	/**
	 * 根据表单key获取默认的权限设置json。
	 * @param formKey	这个字段对应BPM_FROM_DEF的key字段。
	 * @param isInstance	是否实例表单
	 * @return
	 * @throws IOException 
	 */
	@Override
	public JsonNode getDefaultByFormDefKey(String formKey,boolean isInstance) throws IOException {
		FormMeta formDef = formMetaManager.getByKey(formKey);
		ObjectNode jsonObj=JsonUtil.getMapper().createObjectNode();
		if(formDef!=null){
			String formDefId=formDef.getId();
			//实体map列表。
			List<Map<String,String>>  lowList=convertMapLower(formDef.getExpand());
			
			//只获取字段名称描述和实体名称。
            List<FormField> fieldList= formFieldManager.getExtByFormId(formDefId);
			//字段按照表进行分组。
			Map<String,List<FormField>> fieldMap=convertFormGroup(fieldList);
			
			
			
			//构建表的JSON
			ObjectNode tableJson=JsonUtil.getMapper().createObjectNode();
			for(Map<String,String>  entMap:lowList ){
				
				JsonNode json= buildTable(entMap,fieldMap,isInstance);
				if(json==null) continue;
				String entName=entMap.get("name_");
				tableJson.set(entName, json);
			}
			QueryFilter<FormField> filter = QueryFilter.build();
			filter.addFilter("form_id_", formDefId, QueryOP.EQUAL);
			filter.addFilter("ctrl_type_", "dataView", QueryOP.EQUAL);
			PageList<FormField> query = formFieldManager.query(filter);
			if (BeanUtils.isNotEmpty(query)) {
				for (FormField field : query.getRows()) {
					BoDef byDefId = boDefManager.getByDefId(field.getBoDefId());
					if (BeanUtils.isEmpty(byDefId) || BeanUtils.isEmpty(byDefId.getBoEnt())) {
						continue;
					}
					ObjectNode rightJsonObj=JsonUtil.getMapper().createObjectNode();
					ObjectNode rightJson=JsonUtil.getMapper().createObjectNode();
					rightJson.put("hidden", false);
					rightJson.put("add", !isInstance);
					rightJson.put("del", !isInstance);
					rightJson.put("edit", true);
					rightJson.put("export", true);
					
					rightJsonObj.put("main", false);
					rightJsonObj.set("rights", rightJson);
					rightJsonObj.put("description", byDefId.getBoEnt().getDesc());
					rightJsonObj.put("ctrlType", "dataView");
					tableJson.set(byDefId.getAlias(), rightJsonObj);
				}
			}
			jsonObj.set("table", tableJson);
			
			//构建意见的JSON。
			String opinionJson=formDef.getOpinionConf();
			JsonNode opinionJsonObj=buildOpinion(opinionJson,isInstance);
			if(opinionJsonObj!=null){
				jsonObj.set("opinion", opinionJsonObj);
			}
		}else{
			jsonObj=null;
		}
		return jsonObj;
	}
	
	private JsonNode buildOpinion(String json,boolean isInstance) throws IOException{
		if(StringUtil.isEmpty(json)) return null;
		ObjectNode rtnJson=JsonUtil.getMapper().createObjectNode();
		JsonNode ary=JsonUtil.toJsonNode(json);
		for(Object obj:ary){
			JsonNode jsonObj=(JsonNode)obj;
			String name=jsonObj.get("name")+"";
			String desc=jsonObj.get("desc")+"";
			JsonNode permissionJson=getPermissionJson(desc);
			
			if(isInstance){
				permissionJson=getInstPermissionJson(desc);
			}
			else{
				permissionJson=getPermissionJson(desc);
			}
			rtnJson.set(name, permissionJson);
		}
		return rtnJson;
	}
	
	private JsonNode buildTable(Map<String,String>  entMap,Map<String,List<FormField>> fieldMap,boolean isInstance) throws IOException{
		String entName=entMap.get("name_");
		String entDesc=entMap.get("desc_");
		String type=entMap.get("type_");
		List<FormField> list=fieldMap.get(entName);
		if(BeanUtils.isEmpty(list)) return null;
		ObjectNode jsonObj=JsonUtil.getMapper().createObjectNode();
		jsonObj.put("description", entDesc);
		//主表类型。
		if("main".equalsIgnoreCase(type)){
			jsonObj.put("main", true);
		}
		else{
			ObjectNode rightJson=JsonUtil.getMapper().createObjectNode();
			rightJson.put("hidden", false);
			rightJson.put("add", !isInstance);
			rightJson.put("del", !isInstance);
			rightJson.put("required", false);
			
			jsonObj.put("main", false);
			jsonObj.set("rights", rightJson);
		}
		
		ObjectNode fieldsJson=JsonUtil.getMapper().createObjectNode();
		//构建字段。
		for(FormField field:list){
			ObjectNode permissonJson=null;
			if(!isInstance){
				permissonJson=getPermissionJson(field.getDesc(),field.getSn());
			}
			else{
				permissonJson=getInstPermissionJson(field.getDesc(),field.getSn());
			}
			permissonJson.put("fieldName", field.getName());
			
			fieldsJson.set(field.getName(), permissonJson);
		} 
		jsonObj.set("fields", fieldsJson);
		return jsonObj;
	}
	
	/**
	 * 获取默认的权限。
	 * @param desc
	 * @return
	 * @throws IOException 
	 */
	private ObjectNode getPermissionJson(String desc) throws IOException{
	    desc = desc.replaceAll("\"","\'" );
		String json="{\"description\": \""+desc+"\",\"read\": [{\"type\": \"everyone\"}],"
				+ "\"write\": [{\"type\": \"everyone\"}],\"required\": [{\"type\": \"none\"}]}";
		
		return (ObjectNode) JsonUtil.toJsonNode(json);
	}
	
	private ObjectNode getPermissionJson(String desc,Integer sn) throws IOException{
		ObjectNode json = getPermissionJson(desc);
		json.put("sn", sn);
		return json;
	}
	
	/**
	 * 获取只读权限。
	 * @param desc
	 * @return
	 * @throws IOException 
	 */
	private ObjectNode getInstPermissionJson(String desc) throws IOException{
        desc = desc.replaceAll("\"","\'" );
		String json="{\"description\": \""+desc+"\",\"read\": [{\"type\": \"everyone\"}]}";
		
		return (ObjectNode) JsonUtil.toJsonNode(json);
	}
	
	private ObjectNode getInstPermissionJson(String desc, Integer sn) throws IOException{
		ObjectNode json = getInstPermissionJson(desc);
		json.put("sn", sn);
		return json;
	}
	
	
	/**
	 * 将表单字段按照表进行分组。
	 * @param fieldList
	 * @return
	 */
	private Map<String,List<FormField>> convertFormGroup(List<FormField> fieldList){
		Map<String,List<FormField>> map=new HashMap<String, List<FormField>>();
		
		for(FormField field:fieldList){
			String entName=field.getEntName();
			if(map.containsKey(entName)){
				List<FormField> list=map.get(entName);
				list.add(field);
			}
			else{
				List<FormField> list=new ArrayList<FormField>();
				list.add(field);
				map.put(entName, list);
			}
		}
		return map;
	}
	
	/**
	 * map 转换 ，将map的键值转换成小写。
	 * 2020-01-08 获取实体信息  修改为从数据库中获取， 兼容angular 版本的代码 
	 * @param entList
	 * @return
	 * @throws IOException 
	 */
	private List<Map<String,String>> convertMapLower(String expand) throws IOException {
		JsonNode expandJson =JsonUtil.toJsonNode(expand);
		ArrayNode boDefList = (ArrayNode) expandJson.findValue("boDefList");
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		for (JsonNode jsonNode : boDefList) {
			BoDef boDef = boDefManager.getByDefId(jsonNode.get("id").asText());
			BoEnt boEnt = boDef.getBoEnt();
			Map<String,String> map = new LinkedHashMap<String, String>();
			map.put("name_", boEnt.getName());
			map.put("desc_", boEnt.getDesc());
			map.put("type_", boEnt.getType());
			map.put("sn_", "0");
			list.add(map);
			List<BoEnt> childEntList = boEnt.getChildEntList();
			for (int i = 0; i<childEntList.size();) {
				BoEnt childEnt = childEntList.get(i);
				map = new LinkedHashMap<String, String>();
				map.put("name_", childEnt.getName());
				map.put("desc_", childEnt.getDesc());
				map.put("type_", childEnt.getType());
				map.put("sn_", (i++) + "");
			    list.add(map);
			    List<BoEnt> sunEntList = childEnt.getChildEntList();
			    if(BeanUtils.isNotEmpty(sunEntList)){
			    	for (int j = 0; j<sunEntList.size();) {
						map = new LinkedHashMap<String, String>();
						map.put("name_", sunEntList.get(j).getName());
						map.put("desc_", sunEntList.get(j).getDesc());
						map.put("type_", sunEntList.get(j).getType());
						map.put("sn_", (j++) + "");
					    list.add(map);
			    	}
			    }
			}
			
		}
		
		
		return list;
	}

	/**
	 * 根据流程获取表单权限。
	 * @param flowKey
	 * @param parentFlowKey
	 * @return
	 * @throws IOException 
	 */
	private JsonNode getByFlowKey(String flowKey, String parentFlowKey) throws IOException {
		
		FormRight right= baseMapper.getByFlowKey(flowKey, parentFlowKey,1);
		if(right!=null){
			JsonNode rtnJson=JsonUtil.toJsonNode(right.getPermission());
			return rtnJson;
		}
		return null;
	}

	/**
	 * 根据节点获取表单权限。
	 * @param flowKey
	 * @param nodeId
	 * @param parentFlowKey
	 * @return
	 * @throws IOException 
	 */
	private JsonNode getByFlowNodeId(String flowKey, String nodeId, String parentFlowKey) throws IOException {
		FormRight right= baseMapper.getByFlowNodeId(flowKey, nodeId, parentFlowKey,1);
		if(right!=null){
			JsonNode rtnJson=JsonUtil.toJsonNode(right.getPermission());
			return rtnJson;
		}
		return null;
	}
	
	@Override
	public JsonNode getByFormKey(String formKey, boolean isReadOnly) throws IOException {
		
		FormRight right= baseMapper.getByFormKey(formKey,isReadOnly);
		JsonNode rtnJson = null;
		if(right!=null){
			try {
				rtnJson=JsonUtil.toJsonNode(right.getPermission());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rtnJson;
		}else{
			String formMetaKey=formMetaManager.getMetaKeyByFormKey(formKey);
			rtnJson = getDefaultByFormDefKey(formMetaKey,isReadOnly);
			return rtnJson;
		}
	}
	

	/**
	 * 根据流程定义获取实例权限配置。
	 * @param flowKey
	 * @return
	 * @throws IOException 
	 */
	private JsonNode getByInst(String flowKey) throws IOException {
		FormRight right= baseMapper.getByFlowKey(flowKey, "",2);
		if(right!=null){
			JsonNode rtnJson=JsonUtil.toJsonNode(right.getPermission());
			return rtnJson;
		}
		return null;
	}

	@Override
	@Transactional
	public void removeInst(String flowKey) {
		baseMapper.removeByFlowKey(flowKey, "", 2);
	}
	
	@Override
	@Transactional
	public void remove(String formKey, String flowKey, String nodeId, String parentFlowKey) {
		FormRight right = null;
		if(StringUtil.isNotEmpty(flowKey)){
			if(StringUtil.isNotEmpty(nodeId)){
				right = baseMapper.getByFlowNodeId(flowKey, nodeId, parentFlowKey,1);
			}else{
				right= baseMapper.getByFlowKey(flowKey, parentFlowKey,1);
			}
		}
		if( right!=null && !formKey.equals(right.getFormKey())){
			remove(flowKey,nodeId,parentFlowKey);
		}
	}

	@Override
	@Transactional
	public void remove(String flowKey, String nodeId, String parentFlowKey) {
		baseMapper.removeByFlowNode(flowKey, nodeId, parentFlowKey);
	}

	@Override
	@Transactional
	public void remove(String flowKey, String parentFlowKey) {
		baseMapper.removeByFlowKey(flowKey,parentFlowKey, 1);
	}

	@Override
	@Transactional
	public void save(String formKey, String flowKey, String parentFlowKey, String nodeId, String permission, int type,String isCheckOpinion) {
		//清除之前的流程设置。
		if(StringUtil.isNotEmpty(flowKey)){//表单已绑定流程
			if(type==1){
				remove(flowKey, nodeId, parentFlowKey);
			}
			else{
				removeInst(flowKey);
			}
		}else{//表单还没绑定流程
			removeByFormKey(formKey);
		}
		//添加表单权限
		String id=UniqueIdUtil.getSuid();
		FormRight right=new FormRight();
		right.setId(id);
		right.setFormKey(formKey);
		right.setFlowKey(flowKey);
		right.setNodeId(nodeId);
		right.setParentFlowKey(parentFlowKey);
		right.setPermission(permission);
		right.setPermissionType(type);
        right.setIsCheckOpinion(isCheckOpinion);
		this.create(right);
	}

	/**
	 * formKey form_definition的key字段。
	 * @throws IOException 
	 */
	@Override
	public JsonNode getPermissionSetting(String formKey, String flowKey, String parentFlowKey, String nodeId, int type, boolean isGlobalPermission) throws IOException {
		JsonNode json=null;
		//流程权限
		if(StringUtil.isNotEmpty(flowKey)){
			if(type==1){
				if(StringUtil.isEmpty(nodeId)){
					json=getByFlowKey(flowKey, parentFlowKey);
				}
				else{
					json=getByFlowNodeId(flowKey, nodeId, parentFlowKey) ;
					//获取下全局的权限配置。
					if(json==null && isGlobalPermission){
						json=getByFlowKey(flowKey, parentFlowKey);
					}
					//没有获取到全局配置，但是父流程key不为空（说明是子流程），则查询父流程的权限
					if(json==null && StringUtil.isNotEmpty(parentFlowKey)){
						json=getByFlowKey(parentFlowKey, "");
					}
				}
			}
			//实例
			else{
				json=getByInst(flowKey);
			}
		}
		//如果未配置节点全局权限，获取表单配置权限,或者表单基础权限
		boolean isReadOnly=type!=1;
		if(json == null){
			json=getByFormKey(formKey,isReadOnly);
		}
		return json;
	}

	@Override
	public JsonNode getPermission(String formKey, String flowKey, String parentFlowKey, String nodeId, int type, boolean isGlobalPermission) throws IOException {
		JsonNode json= getPermissionSetting(formKey,flowKey,parentFlowKey,nodeId,type,isGlobalPermission);
		return calcFormPermission(json);
	}
	/**
	 *  通过获取的permissionJson 获取表单权限
	 * @param permissionConf
	 * @return
	 * @throws IOException 
	 */
	@Override
	public JsonNode calcFormPermission(JsonNode permissionConf) throws IOException{
		Map<String,Set<String>> profilesMap= permssionCalc.getCurrentProfiles();
		
		ObjectNode rtnJson=JsonUtil.getMapper().createObjectNode();
		//获取表单权限设定。
		//获取表
		ObjectNode tableJsons= (ObjectNode) permissionConf.get("table");
		if(BeanUtils.isNotEmpty(tableJsons)){
			//1.构建字段权限JSON，构建子表权限。
			ObjectNode rtnTableFieldJson=JsonUtil.getMapper().createObjectNode();
			ObjectNode rtnTableJson=JsonUtil.getMapper().createObjectNode();
			 
			for(Iterator<Entry<String, JsonNode>> tableIt = tableJsons.fields();tableIt.hasNext();){
				Entry<String, JsonNode> table = tableIt.next();
				JsonNode tableJson= tableJsons.get(table.getKey());
				JsonNode tableFieldJson= buildTablePermission(tableJson,profilesMap);
				rtnTableFieldJson.set(table.getKey(), tableFieldJson);
				boolean isMain= tableJson.get("main").asBoolean();
				if(!isMain){
					JsonNode tableRights=tableJson.get("rights") ;
					rtnTableJson.set(table.getKey(), tableRights);
				}
			} 
			//字段权限。
			rtnJson.set("fields", rtnTableFieldJson);
			//2.构建子表权限JSON。
			if(rtnTableJson.size()>0){
				rtnJson.set("table", rtnTableJson);
			}
			if(permissionConf.get("opinion")!=null || StringUtil.isEmpty(permissionConf.get("opinion")+""))return rtnJson;
			
			//3.构建意见权限JSON。
			ObjectNode rtnOpinionJson=JsonUtil.getMapper().createObjectNode();
			JsonNode opinionJson=permissionConf.get("opinion");
			
			if(BeanUtils.isNotEmpty(opinionJson)){
				for(Iterator<Entry<String, JsonNode>> opinionIt= opinionJson.fields();opinionIt.hasNext();){
					Entry<String, JsonNode> opinion = opinionIt.next();
					JsonNode perJson= opinionJson.get(opinion.getKey());
					//进行权限计算
					String permission=calcPermission(perJson,profilesMap);
					rtnOpinionJson.put(opinion.getKey(), permission);
				} 
			}
			rtnJson.set("opinion", rtnOpinionJson);
			
			return rtnJson;
		}else{
			return JsonUtil.getMapper().createObjectNode();
		}
		
	}
	
	/**
	 * 字段1:权限，权限的值(n:没有权限,r:只读,w:编辑,b:必填)
	 * 如果有必填就不再继续判断，没有就判断判断编辑权限，再判断只读权限，没有就没有权限。
	 * 所有人权限和无权限只能有一项。
	 * @param tableJson
	 * @return
	 * @throws IOException 
	 */
	private JsonNode buildTablePermission(JsonNode tableJson,Map<String,Set<String>> profilesMap) throws IOException{
		ObjectNode rtnJson=JsonUtil.getMapper().createObjectNode();
		JsonNode fieldJsons=tableJson.get("fields");
		if (BeanUtils.isEmpty(fieldJsons)) {
			return rtnJson;
		}
		//对每一个字段计算权限。
		for(Iterator<Entry<String, JsonNode>> fieldIt = fieldJsons.fields();fieldIt.hasNext();){
			Entry<String, JsonNode> field = fieldIt.next();
			JsonNode perJson=fieldJsons.get(field.getKey());
			String permission=calcPermission(perJson,profilesMap);
			rtnJson.put(field.getKey(), permission);
		}
		return rtnJson;
	}
	
	/**
	 * 权限计算。
	 * <pre>
	 * 1.先判断是否有必填权限。
	 * 2.再判断是否有编辑权限。
	 * 3.再判断是否有读的权限。
	 * 4.无权限。
	 * 
	 * 权限的值：
	 * n: 没有权限
	 * b: 必填
	 * w: 编辑
	 * r: 只读
	 * </pre>
	 * @param perJson
	 * @return
	 * @throws IOException 
	 */
	private String calcPermission(JsonNode perJson,Map<String,Set<String>> profilesMap) throws IOException{
		//判断必填权限
		boolean hasRequired=hasRight(perJson, "required",profilesMap);
		
		String permission="n";
		
		if(hasRequired){
			permission="b";
		}
		else{
			//判断编辑
			boolean hasWrite=hasRight(perJson, "write",profilesMap);
			if(hasWrite){
				permission="w";
			}
			else{
				//判断只读
				boolean hasRead=hasRight(perJson, "read",profilesMap);
				if(hasRead){
					permission="r";
				}
			}
		}
		return permission;
	}

	/**
	 * 判断是否有权限。
	 * 
	 * 参数：
	 * jsonObj: 格式如下
	 * {
                "description": "姓名",
                "read": [
                    {
                        "type": "everyone"
                    }
                ],
                "write": [
                    {
                        "type": "none"
                    }
                ],
                "required": [
                    {
                        "type": "none"
                    }
                ]
            }
	 * @param jsonObj
	 * @param type 可能的值required,write,read
	 * @return
	 * @throws IOException 
	 */
	private boolean hasRight(JsonNode jsonObj, String type,Map<String,Set<String>> profilesMap) throws IOException {
		if(!jsonObj.has(type)) return false;
		
		JsonNode jsonArray = jsonObj.get(type);
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonNode json = jsonArray.get(i);
			boolean hasRight=permssionCalc.hasRight(json.toString(), profilesMap);
			if(hasRight) return true;
		} 
		return false;
	}
	
	@Override
	@Transactional
	public void removeByFormKey(String formKey) {
		baseMapper.removeByFormKey(formKey);
	}
	
	/**
	 * 
	 * 获取流程启动时的流程权限
	 * 1. 获取开始节点
	 * 2. 获取第一个任务节点
	 * 3. 获取全局
	 * 4. 在form_rights中根据formKey获取
	 * 5. 获取表单元数据的授权
	 * @param formKey
	 * @param flowKey
	 * @param nodeId
	 * @param nextNodeId
	 * @return 
	 * JsonNode
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	@Override
	public JsonNode getStartPermission(String formKey, String flowKey, String nodeId, String nextNodeId) throws IOException {
		JsonNode JsonNode = getByFlowNodeId(flowKey, nodeId, "");
		if(BeanUtils.isEmpty(JsonNode)){
			JsonNode = getByFlowNodeId(flowKey, nextNodeId, "");
		}
		if(BeanUtils.isEmpty(JsonNode)){
			JsonNode = getByFlowKey(flowKey, "");
		}
		if(BeanUtils.isEmpty(JsonNode)){
			JsonNode = getByFormKey(formKey, false);
		}
		return calcFormPermission(JsonNode);
	}

	@Override
	public List<FormRight> getByFlowKey(String flowKey) {
		return baseMapper.getAllByFlowKey(flowKey);
	}

	/**
	 * 获取表单排序
	 * @throws IOException 
	 */
	@Override
	public List<Map<String, String>> getTableOrderBySn(String formKey) throws IOException {
		String formMetaKey=formMetaManager.getMetaKeyByFormKey(formKey);
		FormMeta formDef = formMetaManager.getByKey(formMetaKey);
		if(formDef!=null){
			List<Map<String,String>>  retList=convertMapLower(formDef.getExpand());
			return retList;
		}
		return new ArrayList<Map<String,String>>();
	}

	@Override
	@Transactional
	public void importFormRights(String formRightsXml) {
		try {
			FormRightXml formRightList = (FormRightXml) JAXBUtil.unmarshall(formRightsXml, FormRightXml.class);
			List<FormRight> list = formRightList.getRightList();
			for (FormRight bpmFormRight : list) {
				FormRight right = baseMapper.getByFlowNodeId(bpmFormRight.getFlowKey(), bpmFormRight.getNodeId(), bpmFormRight.getParentFlowKey(),bpmFormRight.getPermissionType());
				if(BeanUtils.isNotEmpty(right)){
					bpmFormRight.setId(right.getId());
					this.update(bpmFormRight);
					ThreadMsgUtil.addMsg("流程["+bpmFormRight.getFlowKey()+"]中节点[ "+bpmFormRight+" ]的表单授权已经存在,更新成功");
				}else{
					this.create(bpmFormRight);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("导入表单权限失败"+e.getMessage(),e);
		} 
	}

	@Override
	@Transactional
	public void removeByFlowKey(String flowKey, String parentFlowKey,
			int permissionType) {
		baseMapper.removeByFlowKey(flowKey, parentFlowKey, permissionType);
	}

	@Override
	public JsonNode getDefaultByDesign(String formDefId,String expand, boolean isInstance) throws Exception {
		JsonNode expandJson = JsonUtil.toJsonNode(expand);
		if(BeanUtils.isEmpty(expandJson)){
			return JsonUtil.getMapper().createObjectNode();
		}
		
		//实体map列表。
		List<Map<String,String>>  lowList=convertMapLower(expand);
		
		//只获取字段名称描述和实体名称。
		//A.name_,B.NAME_ ENT_NAME,A.desc_, A.sn_
		List<FormField> fieldList= formFieldManager.getExtByFormId(formDefId) ;
		
		//字段按照表进行分组。
		Map<String,List<FormField>> fieldMap = convertFormGroup(fieldList);
		
		ObjectNode jsonObj = JsonUtil.getMapper().createObjectNode();
		
		//构建表的JSON
		ObjectNode tableJson = JsonUtil.getMapper().createObjectNode();
		for(Map<String,String>  entMap:lowList ){
			JsonNode json= buildTable(entMap,fieldMap,isInstance);
			if(json==null) continue;
			String entName=entMap.get("name_");
			tableJson.set(entName, json);
		}
		jsonObj.set("table", tableJson);
		
		//构建意见的JSON。
		String opinionJson= JsonUtil.toJson(expandJson.get("opinion"));
		JsonNode opinionJsonObj=buildOpinion(opinionJson,isInstance);
		if(opinionJsonObj!=null){
			jsonObj.set("opinion", opinionJsonObj);
		}
		
		return jsonObj;
	}

    @Override
    public String getByTeam(String flowKey,String nodeId){
        return baseMapper.getByTeam(flowKey,nodeId);
    }

	@Override
	@Transactional
	public void emptyAll(String flowKey) {
		 baseMapper.emptyAll(flowKey);
	}
}
