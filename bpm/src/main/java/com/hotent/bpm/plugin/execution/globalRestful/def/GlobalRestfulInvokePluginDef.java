package com.hotent.bpm.plugin.execution.globalRestful.def;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.model.process.def.IGlobalRestfulPluginDef;
import com.hotent.bpm.api.model.process.def.Restful;
import com.hotent.bpm.plugin.core.plugindef.AbstractBpmExecutionPluginDef;
import com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFuls;

/**
 * Restful接口调用插件定义
 * @author heyifan
 */
public class GlobalRestfulInvokePluginDef extends AbstractBpmExecutionPluginDef implements IGlobalRestfulPluginDef{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1395834407487366236L;
	private List<Restful> restfulList;

	public List<Restful> getRestfulList() {
		return restfulList;
	}

	public void setRestfulList(List<Restful> restfulList) {
		this.restfulList = restfulList;
	}
	
	public static GlobalRestfulInvokePluginDef getRestfuls(GlobalRestFuls restfulsExt){
		List<com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFul> restfulExtList = restfulsExt.getGlobalRestFul();
		if(BeanUtils.isEmpty(restfulExtList))return null;
		
		List<Restful> restFulList = new ArrayList<Restful>();
		for (com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFul r : restfulExtList) {
			Restful restful = convertExt2Restful(r);
			restFulList.add(restful);
		}
		GlobalRestfulInvokePluginDef def = new GlobalRestfulInvokePluginDef();
		def.setRestfulList(restFulList);
		return def;
	}
	
	public static com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFuls getRestfulExt(GlobalRestfulInvokePluginDef restfuls){
		List<Restful> restFulList = restfuls.getRestfulList();
		if(BeanUtils.isEmpty(restFulList))return null;
		
		List<com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFul> restfulExtList = new ArrayList<com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFul>();
		for (Restful r : restFulList) {
			com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFul restful = convertRestful2Ext(r);
			restfulExtList.add(restful);
		}
		com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFuls restfulsExt = new com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFuls();
		restfulsExt.setGlobalRestFul(restfulExtList);
		return restfulsExt;
	}
	
	
	public static Restful convertExt2Restful(com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFul restfulExt){
		Restful  restful = new Restful();
		restful.setUrl(restfulExt.getUrl());
		restful.setDesc(restfulExt.getDesc());
		restful.setHeader(restfulExt.getHeader());
		restful.setInvokeMode(restfulExt.getInvokeMode());
		restful.setCallTime(restfulExt.getCallTime());
		restful.setCallNodes(restfulExt.getCallNodes());
		restful.setParams(restfulExt.getParams());
		restful.setOutPutScript(restfulExt.getOutPutScript());
		restful.setParentDefKey(restfulExt.getParentDefKey());
		return restful;
	}
	
	
	public static com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFul convertRestful2Ext(Restful restful){
		com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFul restfulExt = new com.hotent.bpm.plugin.execution.globalRestful.entity.GlobalRestFul();
		restfulExt.setUrl(restful.getUrl());
		restfulExt.setDesc(restful.getDesc());;
		restfulExt.setHeader(restful.getHeader());
		restfulExt.setCallTime(restful.getCallTime());
		restfulExt.setCallNodes(restful.getCallNodes());
		restfulExt.setInvokeMode(restful.getInvokeMode());
		restfulExt.setParams(restful.getParams());
		restfulExt.setOutPutScript(restful.getOutPutScript());
		restfulExt.setParentDefKey(restful.getParentDefKey());
		return restfulExt;
	}
}
