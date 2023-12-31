package com.hotent.bpm.plugin.task.restful.def;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.model.process.def.IGlobalRestfulPluginDef;
import com.hotent.bpm.api.model.process.def.Restful;
import com.hotent.bpm.plugin.core.plugindef.AbstractBpmTaskPluginDef;
import com.hotent.bpm.plugin.task.restful.entity.RestFuls;

/**
 * Restful接口调用插件定义
 * @author heyifan
 */
public class RestfulInvokePluginDef extends AbstractBpmTaskPluginDef implements IGlobalRestfulPluginDef{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8839583743906697495L;
	private List<Restful> restfulList;

	public List<Restful> getRestfulList() {
		return restfulList;
	}

	public void setRestfulList(List<Restful> restfulList) {
		this.restfulList = restfulList;
	}
	
	public static RestfulInvokePluginDef getRestfuls(RestFuls restfulsExt){
		List<com.hotent.bpm.plugin.task.restful.entity.RestFul> restfulExtList = restfulsExt.getRestFul();
		if(BeanUtils.isEmpty(restfulExtList))return null;
		
		List<Restful> restFulList = new ArrayList<Restful>();
		for (com.hotent.bpm.plugin.task.restful.entity.RestFul r : restfulExtList) {
			Restful restful = convertExt2Restful(r);
			restFulList.add(restful);
		}
		RestfulInvokePluginDef def = new RestfulInvokePluginDef();
		def.setRestfulList(restFulList);
		return def;
	}
	
	public static com.hotent.bpm.plugin.task.restful.entity.RestFuls getRestfulExt(RestfulInvokePluginDef restfuls){
		List<Restful> restFulList = restfuls.getRestfulList();
		if(BeanUtils.isEmpty(restFulList))return null;
		
		List<com.hotent.bpm.plugin.task.restful.entity.RestFul> restfulExtList = new ArrayList<com.hotent.bpm.plugin.task.restful.entity.RestFul>();
		for (Restful r : restFulList) {
			com.hotent.bpm.plugin.task.restful.entity.RestFul restful = convertRestful2Ext(r);
			restfulExtList.add(restful);
		}
		com.hotent.bpm.plugin.task.restful.entity.RestFuls restfulsExt = new com.hotent.bpm.plugin.task.restful.entity.RestFuls();
		restfulsExt.setRestFul(restfulExtList);
		return restfulsExt;
	}
	
	
	public static Restful convertExt2Restful(com.hotent.bpm.plugin.task.restful.entity.RestFul restfulExt){
		Restful  restful = new Restful();
		restful.setUrl(restfulExt.getUrl());
		restful.setDesc(restfulExt.getDesc());
		restful.setHeader(restfulExt.getHeader());
		restful.setInvokeMode(restfulExt.getInvokeMode());
		restful.setCallTime(restfulExt.getCallTime());
		restful.setParams(restfulExt.getParams());
		restful.setOutPutScript(restfulExt.getOutPutScript());
		restful.setParentDefKey(restfulExt.getParentDefKey());
		return restful;
	}
	
	
	public static com.hotent.bpm.plugin.task.restful.entity.RestFul convertRestful2Ext(Restful restful){
		com.hotent.bpm.plugin.task.restful.entity.RestFul restfulExt = new com.hotent.bpm.plugin.task.restful.entity.RestFul();
		restfulExt.setUrl(restful.getUrl());
		restfulExt.setDesc(restful.getDesc());;
		restfulExt.setHeader(restful.getHeader());
		restfulExt.setCallTime(restful.getCallTime());
		restfulExt.setInvokeMode(restful.getInvokeMode());
		restfulExt.setParams(restful.getParams());
		restfulExt.setOutPutScript(restful.getOutPutScript());
		restfulExt.setParentDefKey(restful.getParentDefKey());
		return restfulExt;
	}
}
