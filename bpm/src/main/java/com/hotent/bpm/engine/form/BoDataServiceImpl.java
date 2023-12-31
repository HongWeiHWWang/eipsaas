package com.hotent.bpm.engine.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.bpm.api.context.ContextThreadUtil;
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.engine.def.BpmDefUtil;
import com.hotent.bpm.persistence.manager.BpmBusLinkManager;
import com.hotent.bpm.persistence.manager.BpmCheckOpinionManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.BpmBusLink;
import com.hotent.bpm.persistence.model.DefaultBpmCheckOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;

@Service
public class BoDataServiceImpl implements BoDataService {
	
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BpmBusLinkManager bpmBusLinkManager;
	@Resource
	BpmCheckOpinionManager bpmCheckOpinionManager;
	@Resource
	FormFeignService formRestfulService;
	
	@Override
	public List<ObjectNode> getDataByInst(BpmProcessInstance instance) throws Exception{
		
		ObjectNode formRestParams=JsonUtil.getMapper().createObjectNode();
        formRestParams.put("flowDefId", instance.getProcDefId());
        formRestParams.put("flowKey", instance.getProcDefKey());
        
        //查子流程定义配置，根据上一级的父流程key来查
		if (BeanUtils.isNotEmpty(instance) && StringUtil.isNotZeroEmpty(instance.getParentInstId())) {
			DefaultBpmProcessInstance pInstance = bpmProcessInstanceManager.get(instance.getParentInstId());
			if (BeanUtils.isNotEmpty(instance)) {
				formRestParams.put("parentFlowKey", pInstance.getProcDefKey());
			}
		}
		//查子流程数据，根据顶级的流程实例来查
		instance = bpmProcessInstanceManager.getTopBpmProcessInstance(instance);
		
		DefaultBpmProcessDefExt defExt=BpmDefUtil.getProcessExt(instance);
		List<ProcBoDef> boList= defExt.getBoDefList();

		if (BeanUtils.isEmpty(boList)) return Collections.emptyList();
		//根据实例ID获取关联数据。
		Map<String, BpmBusLink> keyValueMap =bpmBusLinkManager.getMapByInstId(instance.getId());
		String saveType=defExt.isBoSaveToDb() ? "database" : "boObject";

		List<ObjectNode> dataObjects = new ArrayList<ObjectNode>();
		for (String key : keyValueMap.keySet()){
			BpmBusLink link = keyValueMap.get(key);
			//本来应该是根据boent的主键类型来判断拿str还是key的，但为了方便其实都拿一次也行
			String id=StringUtil.isNotEmpty(link.getBusinesskeyStr())?link.getBusinesskeyStr():link.getBusinesskey().toString();
			
			formRestParams.put("saveType", saveType);
			formRestParams.put("boid", id);
			formRestParams.put("code", link.getBoDefCode());

            formRestParams.put("nodeId", ContextThreadUtil.getCommuVar("nodeId", "").toString());
            ObjectNode boData= formRestfulService.getBodataById(formRestParams);
			dataObjects.add(boData);
		}
		return dataObjects;
	}
	
	@Override
	public List<ObjectNode> getDataByBizKey(String  businessKey) throws Exception{
		//获取最外层的流程实例数据。
		
		BpmProcessInstance instance=bpmProcessInstanceManager.getTopBpmProcessInstance(bpmProcessInstanceManager.getByBusinessKey(businessKey));
		
		DefaultBpmProcessDefExt defExt=BpmDefUtil.getProcessExt(instance);
		List<ProcBoDef> boList= defExt.getBoDefList();

		if (BeanUtils.isEmpty(boList)) return Collections.emptyList();
		//根据实例ID获取关联数据。
		Map<String, BpmBusLink> keyValueMap =bpmBusLinkManager.getMapByInstId(instance.getId());

		String  saveType=defExt.isBoSaveToDb() ? "database" : "boObject";

		List<ObjectNode> dataObjects = new ArrayList<ObjectNode>();
		for (String key : keyValueMap.keySet()){
			BpmBusLink link = keyValueMap.get(key);
			//本来应该是根据boent的主键类型来判断拿str还是key的，但为了方便其实都拿一次也行
			String id=StringUtil.isNotEmpty(link.getBusinesskeyStr())?link.getBusinesskeyStr():link.getBusinesskey().toString();
			ObjectNode formRestParams=JsonUtil.getMapper().createObjectNode();
			formRestParams.put("saveType", saveType);
			formRestParams.put("boid", id);
			formRestParams.put("code", link.getBoDefCode());
            formRestParams.put("flowDefId", instance.getProcDefId());
			
			ObjectNode boData= formRestfulService.getBodataById(formRestParams);
			dataObjects.add(boData);
		}
		return dataObjects;
	}
	
	
	/**
	 * 1.根据流程定义ID获取流程定义定义的BO列表。
	 * 2.根据bocode 获取 bodata数据。
	 * @throws Exception 
	 */
	@Override
	public List<ObjectNode> getDataByDefId(String defId) throws Exception{
		BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
		List<ProcBoDef> boList = defExt.getBoDefList();
		if (BeanUtils.isEmpty(boList)) return null;
		 
		 
		String  saveType=defExt.isBoSaveToDb() ? "database" : "boObject";
		
		List<ObjectNode> dataObjects = new ArrayList<ObjectNode>();
		
		for (ProcBoDef procBoDef : boList){
			String boKey = procBoDef.getKey();
			ObjectNode boData= formRestfulService.getBodataByDefCode(saveType, boKey);
			dataObjects.add(boData);
		}
		return dataObjects;
	}
	
	@Override
	public List<ObjectNode> getDataByBoKeys(List<String> boKeyList) throws ClientProtocolException, IOException{
		List<ObjectNode> dataObjects = new ArrayList<ObjectNode>();

		for (String key : boKeyList){
			ObjectNode boData= formRestfulService.getBodataByDefCode("database", key);
			dataObjects.add(boData);
		}
		return dataObjects;
	}
	
	
	/**
	 * 表单意见转换。
	 * {
		"caiwuOpinion":[{auditor:"",opinion:"",createTime:"",auditorName:"",status:""},{auditor:"",opinion:"",auditorName:"",userName:"",status:""}],
		"juzhangyOpinion":[{auditor:"",opinion:"",createTime:"",auditorName:"",status:""},{auditor:"",opinion:"",auditorName:"",userName:"",status:""}]
		}
	 * @throws Exception 
	 */
	@Override
	public ObjectNode getFormOpinionJson(String proInstId) throws Exception {
		ObjectNode json = JsonUtil.getMapper().createObjectNode();
		
		List<DefaultBpmCheckOpinion> opinionList = bpmCheckOpinionManager.getFormOpinionByInstId(proInstId);
		//节点和类型的映射
		Map<String,Boolean> nodeTypeMap=convertNodeDef( proInstId);
		
		Map<String,String> identityMap=new HashMap<String, String>();
	
		for(DefaultBpmCheckOpinion opinion:opinionList){
			String formIdentity=opinion.getFormName();
			ObjectNode opinionJson= getJsonByOpinion(opinion);
			identityMap.put(formIdentity, opinion.getTaskKey());
			
			if(JsonUtil.isContainsKey(json, formIdentity)){
				ArrayNode ary = (ArrayNode) JsonUtil.toJsonNode(json.get(formIdentity).asText());
				ary.add(opinionJson);
			}
			else{
				ArrayNode ary = JsonUtil.getMapper().createArrayNode();
				ary.add(opinionJson);
				json.set(formIdentity, ary);
			}
		}
		ObjectNode rtnJson = JsonUtil.getMapper().createObjectNode();
		
		for(Iterator<String> it= json.fieldNames();it.hasNext();){
			String key=it.next();
			ArrayNode ary= (ArrayNode) JsonUtil.toJsonNode(json.get(key).asText());
			String nodeId=identityMap.get(key);
			boolean isSignTask = false;
			if(nodeTypeMap.containsKey(nodeId)){
				isSignTask=nodeTypeMap.get(nodeId);
			}
			if(isSignTask){
				rtnJson.set(key, ary);
			}
			else{
				ArrayNode tmpAry = JsonUtil.getMapper().createArrayNode();
				tmpAry.add(ary.get(ary.size()-1));
				rtnJson.set(key, tmpAry);
			}
		}
		
		
		return rtnJson;
	}
	
	/**
	 * 返回节点和节点类型的map。
	 * 类型：true : 会签 ,false :任务节点
	 * @param proInstId
	 * @return
	 * @throws Exception 
	 */
	private Map<String,Boolean> convertNodeDef(String proInstId) throws Exception{
		BpmProcessInstance instance= bpmProcessInstanceManager.get(proInstId);
		List<BpmNodeDef> nodeList=bpmDefinitionAccessor.getAllNodeDef(instance.getProcDefId());
		Map<String,Boolean> map=new HashMap<String, Boolean>();
		for(BpmNodeDef def:nodeList){
			map.put(def.getNodeId(),NodeType.SIGNTASK.equals( def.getType()));
		}
		return map;
		
	}
	
	private ObjectNode getJsonByOpinion(DefaultBpmCheckOpinion opinion){
		ObjectNode json=JsonUtil.getMapper().createObjectNode();
		json.put("nodeId", opinion.getTaskKey());
		json.put("opinion", opinion.getOpinion());
		json.put("createTime", opinion.getCompleteTime().toString());
		json.put("status", opinion.getStatus());
		json.put("auditorName", opinion.getAuditorName());
		json.put("auditor", opinion.getAuditor());
		return json;
	}

	

}
