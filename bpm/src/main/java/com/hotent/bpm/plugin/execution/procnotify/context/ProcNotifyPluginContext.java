package com.hotent.bpm.plugin.execution.procnotify.context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.constant.EventType;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmExecutionPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.core.util.UserAssignRuleParser;
import com.hotent.bpm.plugin.execution.procnotify.def.ProcNotifyPluginDef;
import com.hotent.bpm.plugin.execution.procnotify.entity.OnEnd;
import com.hotent.bpm.plugin.execution.procnotify.entity.ProcNotify;
import com.hotent.bpm.plugin.execution.procnotify.plugin.ProcNotifyPlugin;
import com.hotent.bpm.plugin.task.tasknotify.def.model.NotifyItem;
import com.hotent.bpm.plugin.task.tasknotify.def.model.NotifyVo;
import com.hotent.bpm.plugin.task.tasknotify.util.NotifyUtil;
import com.jamesmurty.utils.XMLBuilder;

public class ProcNotifyPluginContext extends AbstractBpmExecutionPluginContext{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -480039987476499232L;

	public List<EventType> getEventTypes() {
		List<EventType> eventTypes = new ArrayList<EventType>();
		eventTypes.add(EventType.END_EVENT);
		return eventTypes;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends RunTimePlugin> getPluginClass() {
		return ProcNotifyPlugin.class;
	}

	private NotifyVo convert(OnEnd onEnd,Element pluginEl){
		NotifyVo notifyVo = new NotifyVo();
		notifyVo.setEventType(EventType.END_EVENT);
		Element onEndEl = XmlUtil.getChildNodeByName(pluginEl, "onEnd");		
		List<NotifyItem> notifyItems = NotifyUtil.parseNotifyItems(onEndEl);
		notifyVo.setNotifyItemList(notifyItems);		
		return notifyVo;
	}

	/**
	 * 构建插件XML。
	 * xml格式如下。
	 * <pre>
	 * &lt;procNotify xmlns="http://www.jee-soft.cn/bpm/plugins/execution/procNotify" >
	 * &lt;onEnd >
     * &lt;notify xmlns="http://www.jee-soft.cn/bpm/plugins/task/baseNotify" msgTypes="sms">
     *   &lt;userRule xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/base" groupNo="1">
     *        &lt;calcs>&lt;/calcs>
     *   &lt;/userRule>
     *&lt;/notify>
     *&lt;notify xmlns="http://www.jee-soft.cn/bpm/plugins/task/baseNotify" msgTypes="mail">&lt;/notify>
	 *&lt;/onEnd>
	 *&lt;/procNotify>
	 *</pre>
	 */
	@Override
	public String getPluginXml() {
		ProcNotifyPluginDef def=(ProcNotifyPluginDef)this.getBpmPluginDef();
		try {
			XMLBuilder xmlBuilder = XMLBuilder.create("procNotify")
					.a("xmlns", "http://www.jee-soft.cn/bpm/plugins/execution/procNotify");
			
			xmlBuilder= xmlBuilder.e("onEnd");
			NotifyVo createVo=def.getNotifyVoMap().get(EventType.END_EVENT);
			if(createVo!=null){
				NotifyUtil.handXmlBuilder(createVo,xmlBuilder);
			}
			return xmlBuilder.asString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String getJson() throws IOException {
		ProcNotifyPluginDef def=(ProcNotifyPluginDef) this.getBpmPluginDef();
		Collection<NotifyVo> listVo=def.getNotifyVoMap().values();
		ArrayNode notifyItemArray = JsonUtil.getMapper().createArrayNode() ;
		for(NotifyVo vo:listVo){
			List<NotifyItem> notifyItems = vo.getNotifyItemList();
			for(NotifyItem item:notifyItems){
				ObjectNode node =  JsonUtil.getMapper().createObjectNode();
				ArrayNode array = JsonUtil.getMapper().createArrayNode() ;
				UserAssignRuleParser.handJsonConfig(array, item.getUserAssignRules());
				node.set("userAssignRules", array);
				List<String> msgTypes = item.getMsgTypes();
				node.put("msgTypes", BeanUtils.isNotEmpty(msgTypes)?StringUtils.join(msgTypes,","):"");
				notifyItemArray.add(node);
			}
		}
		return JsonUtil.toJson(notifyItemArray);
	}

	@Override
	protected BpmPluginDef parseJson(String pluginJson) throws Exception {
		ProcNotifyPluginDef procNotifyPluginDef = new ProcNotifyPluginDef();
		
		ArrayNode endJson = (ArrayNode) JsonUtil.toJsonNode(pluginJson);
		//ArrayNode endJson=jsonObject.getJSONArray(EventType.END_EVENT.getEventName());
		NotifyVo createVo= NotifyUtil.getNotifyVo(endJson);
		
		
		procNotifyPluginDef.addNotifyVo(EventType.END_EVENT, createVo);
		
		return procNotifyPluginDef;
	}

	@Override
	protected BpmPluginDef parseElement(Element element) {
		String xml = XmlUtil.getXML(element);
		ProcNotifyPluginDef procNotifyPluginDef = new ProcNotifyPluginDef();
		try {
			ProcNotify procNotify = (ProcNotify)JAXBUtil.unmarshall(xml,com.hotent.bpm.plugin.execution.procnotify.entity.ObjectFactory.class);
			List<NotifyVo> notifyVoList = new ArrayList<NotifyVo>();
			OnEnd onEnd=procNotify.getOnEnd();
			if(onEnd!=null){
				NotifyVo notifyVoOnEnd = convert(onEnd,element);									
				notifyVoList.add(notifyVoOnEnd);
				procNotifyPluginDef.getNotifyVoMap().put(EventType.END_EVENT, notifyVoOnEnd);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}		
		return procNotifyPluginDef;
	}

	@Override
	public String getTitle() {
		
		return "抄送通知";
	}
}
