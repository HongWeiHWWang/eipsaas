package com.hotent.bpm.plugin.task.restful.context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.model.process.def.Restful;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmTaskPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.task.restful.entity.ObjectFactory;
import com.hotent.bpm.plugin.task.restful.def.RestfulInvokePluginDef;
import com.hotent.bpm.plugin.task.restful.entity.RestFuls;
import com.hotent.bpm.plugin.task.restful.plugin.RestfulInvokePlugin;
             
public class RestFulsPluginContext extends AbstractBpmTaskPluginContext{
	private static final long serialVersionUID = -5879623294061566500L;

	@Override
	public List<EventType> getEventTypes() {
		List<EventType> list=new ArrayList<EventType>();
		list.add(EventType.TASK_CREATE_EVENT);
		list.add(EventType.TASK_COMPLETE_EVENT);
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return (Class<? extends RunTimePlugin>) RestfulInvokePlugin.class;
	}

	@Override
	public String getPluginXml() {
		RestfulInvokePluginDef pluginDef = (RestfulInvokePluginDef) this.getBpmPluginDef();
		String xml = "";
		try {
			if(BeanUtils.isEmpty(pluginDef.getRestfulList()))return xml;
			xml = JAXBUtil.marshall(RestfulInvokePluginDef.getRestfulExt(pluginDef), ObjectFactory.class);
			xml = xml.replace("encoding=\"utf-8\"", "encoding=\"UTF-8\"");
			xml = xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n", "");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xml;
	}
	
	@Override
	public String getJson() throws IOException {
		RestfulInvokePluginDef pluginDef=(RestfulInvokePluginDef) this.getBpmPluginDef();
		return JsonUtil.toJson(pluginDef);
	}
	
	

	@Override
	protected BpmPluginDef parseJson(String pluginJson) throws IOException {
		ArrayNode array = (ArrayNode) JsonUtil.toJsonNode(pluginJson) ;
		List<Restful> restFuls = new ArrayList<Restful>();
		for (Object object : array) {
			ObjectNode obj = (ObjectNode) object;
			if(BeanUtils.isNotEmpty(obj.get("header"))&&StringUtil.isEmpty(obj.get("header").asText())){
				obj.put("header", obj.get("header").toString());
			}
			String objStr = JsonUtil.toJson(obj);
			Restful restFul = JsonUtil.toBean(objStr, Restful.class);
			if(StringUtil.isNotEmpty(restFul.getHeader())){
				try {
					restFul.setHeader(Base64.getBase64(restFul.getHeader()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			restFuls.add(restFul);
		}
		RestfulInvokePluginDef def = new RestfulInvokePluginDef();
		def.setRestfulList(restFuls);
		return def;
	}
	@Override
	protected BpmPluginDef parseElement(Element element) {
		String xml = XmlUtil.getXML(element);
		RestfulInvokePluginDef def ;
		 try {
			 RestFuls restfuls = (RestFuls) JAXBUtil.unmarshall(xml,ObjectFactory.class);
			def =RestfulInvokePluginDef.getRestfuls(restfuls);
			return def;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@Override
	public String getTitle() {
		return "restFul接口事件";
	}
	
	
	public String getJsonByParentFlowKey(String flowKey) throws IOException {
		if(StringUtil.isEmpty(flowKey))  flowKey = BpmConstants.LOCAL;
		RestfulInvokePluginDef pluginDef=(RestfulInvokePluginDef) this.getBpmPluginDef();
		List<Restful> restFuls = pluginDef.getRestfulList();
		if(BeanUtils.isEmpty(restFuls)) return "[]";
		List<Restful> restFulList = new ArrayList<Restful>();
		for (Restful restful : restFuls) {
			if(StringUtil.isNotEmpty(restful.getHeader())){
				try {
					restful.setHeader(Base64.getFromBase64(restful.getHeader()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if(StringUtil.isEmpty(restful.getParentDefKey()))  restful.setParentDefKey(BpmConstants.LOCAL); //如果为空，改为local_
			if(restful.getParentDefKey().equals(flowKey))  restFulList.add(restful);
		}
		if(restFulList.size() ==0) return "[]";
		return JsonUtil.toJson(restFulList);
	}
	

}
