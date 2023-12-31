package com.hotent.bpm.engine.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.model.HtJsonNodeFactory;
import com.hotent.base.model.HtObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.process.def.FieldInitSetting;
import com.hotent.bpm.api.model.process.def.FormInitItem;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.DataObjectHandler;
import com.hotent.bpm.engine.def.BpmDefUtil;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.util.BoDataUtil;


/**
 * 负责根据配置修改表单数据。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014年8月24日-下午4:01:32
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Component
public class DefaultDataObjectHandler implements DataObjectHandler {
	
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BoDataService boDataService;
	@Resource
	FormFeignService formRestfulService;
	@Resource
	GroovyScriptEngine groovyScriptEngine;

	@Override
	public void handShowData(String defId,  List<ObjectNode> boDatas) throws Exception {
		FormInitItem formInitItem= getFormInitItem( defId);
		List<FieldInitSetting> fieldInitSettings= getFieldSetting(formInitItem,true);
		if(fieldInitSettings==null) return;
		
		setDataObject(fieldInitSettings,boDatas);
	}

	@Override
	public void handSaveData(BpmProcessInstance instance, List<ObjectNode> boDatas) throws Exception {
		FormInitItem formInitItem= getFormInitItem( instance.getProcDefId());
		List<FieldInitSetting> fieldInitSettings= getFieldSetting(formInitItem,false);
		if(fieldInitSettings==null) return;
		
		addUnUseBoData(instance,boDatas);
		setDataObject(fieldInitSettings,boDatas);
	}

	@Override
	public void handSaveData(BpmProcessInstance instance, String nodeId,List<ObjectNode> boDatas) throws Exception {
		//获取节点修改配置。
		FormInitItem formInitItem=getFormInitItem(instance, nodeId);
		List<FieldInitSetting> fieldInitSettings= getFieldSetting(formInitItem,false);
		if(fieldInitSettings==null) return;
		
		addUnUseBoData(instance,boDatas);
		setDataObject(fieldInitSettings,boDatas);
	}
	
	/**
	 * 添加未使用的BO对象
	 * @param instance
	 * @param boDatas
	 * @throws Exception 
	 */
	private void addUnUseBoData(BpmProcessInstance instance,List<ObjectNode> boDatas) throws Exception {
		DefaultBpmProcessDefExt defExt=BpmDefUtil.getProcessExt(instance);
		String saveMode =  defExt.isBoSaveToDb() ? "database" : "boObject";
		List<ProcBoDef> boList= defExt.getBoDefList();
		List<String> boDefCodes = new ArrayList<String>();
		for (ProcBoDef procBoDef : boList) {
			boDefCodes.add(procBoDef.getKey());
		}
		ObjectNode jsonObject = (ObjectNode) BoDataUtil.hanlerData(boDatas);
		for (String boDefCode : boDefCodes) {
			if(jsonObject.findValue(boDefCode) == null){
				ObjectNode boData= formRestfulService.getBodataByDefCode(saveMode, boDefCode);
				ObjectNode boDef = formRestfulService.getBodefByAlias(boDefCode);
				ObjectNode boEnt = (ObjectNode) boDef.get("boEnt");
				boData.set("boEnt",boEnt);
				boDatas.add(boData);
			}
		}
	}
	
	/**
	 * 修改BO数据。
	 * 
	 * @param fieldInitSettings
	 * @param dataObject 
	 * void
	 * @throws IOException 
	 */
	private void setDataObject(List<FieldInitSetting> fieldInitSettings, List<ObjectNode> boDatas) throws IOException{
		Map<String, Object> vars=new HashMap<String, Object>();
		
		for(ObjectNode boData:boDatas){
			String boDefCode = "";
			if(boData.has("boDef")){
				boDefCode = boData.get("boDef").get("alias").asText();
			}else{
				boDefCode = boData.get("boDefAlias").asText();
			}
			HtJsonNodeFactory factory = new HtJsonNodeFactory();
			HtObjectNode htBodata =factory.htObjectNode(boData);
			vars.put(boDefCode, htBodata);
		}
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		if(BeanUtils.isNotEmpty(cmd)){
			vars.putAll(cmd.getVariables());
		}
		
		for(FieldInitSetting setting:fieldInitSettings){
			String script = setting.getSetting();
			if(StringUtil.isEmpty(script)) continue;
			groovyScriptEngine.execute(script, vars);
		}
		if(BeanUtils.isNotEmpty(cmd)){
			cmd.setBusData(BoDataUtil.hanlerData(boDatas).toString());
		}
	}
	
	

	@Override
	public void handShowData(BpmProcessInstance instance, String nodeId,List<ObjectNode> boDatas) throws Exception {
		FormInitItem formInitItem=getFormInitItem(instance, nodeId);
		List<FieldInitSetting> fieldInitSettings= getFieldSetting(formInitItem,true);
		if(fieldInitSettings==null) return;
		
		setDataObject(fieldInitSettings,boDatas);
		
	}
	
	private FormInitItem getFormInitItem(BpmProcessInstance instance, String nodeId) throws Exception{
		FormInitItem formInitItem=null;
		BpmNodeDef bpmNodeDef= bpmDefinitionAccessor.getBpmNodeDef(instance.getProcDefId(), nodeId);
		if(StringUtil.isNotZeroEmpty(instance.getParentInstId())){
			BpmProcessInstance topInstance= bpmProcessInstanceManager.getTopBpmProcessInstance(instance);
			String defKey=topInstance.getProcDefKey();
			formInitItem=bpmNodeDef.getFormInitItemByParentKey(defKey);
			if(formInitItem!=null) return formInitItem;
		}
		return bpmNodeDef.getFormInitItem();
	}
	
	private FormInitItem getFormInitItem(String defId) throws Exception{
		BpmNodeDef bpmNodeDef= bpmDefinitionAccessor.getStartEvent(defId);
		return bpmNodeDef.getFormInitItem();
	}

	
	private List<FieldInitSetting> getFieldSetting(FormInitItem formInitItem,boolean isShow){
		if(formInitItem==null) return null;
		if(isShow){
			return formInitItem.getShowFieldsSetting();
		}
		else{
			return formInitItem.getSaveFieldsSetting();
		}
	}

	
}
