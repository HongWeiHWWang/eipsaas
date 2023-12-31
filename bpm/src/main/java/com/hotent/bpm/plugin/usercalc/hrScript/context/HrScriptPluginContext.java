package com.hotent.bpm.plugin.usercalc.hrScript.context;

import java.io.IOException;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.hrScript.def.HrScriptPluginDef;
import com.hotent.bpm.plugin.usercalc.hrScript.runtime.HrScriptPlugin;

public class HrScriptPluginContext extends AbstractUserCalcPluginContext {

	private static final long serialVersionUID = -2353875054502587417L;

	@Override
	public String getDescription() {
		HrScriptPluginDef def=(HrScriptPluginDef) this.getBpmPluginDef();
		if(def==null) return "";
		return def.getDescription();
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return HrScriptPlugin.class;
	}
	@Override
	public String getTitle() {
		return "人员脚本";
	}
	@Override
	protected BpmPluginDef parseElement(Element element) {
		HrScriptPluginDef hrScriptPluginDef = new HrScriptPluginDef();
		Element el = XmlUtil.getChildNodeByName(element, "content");
		Element descEL = XmlUtil.getChildNodeByName(element, "description");
		Element idEl= XmlUtil.getChildNodeByName(element, "scriptId");
		Element paramsEl= XmlUtil.getChildNodeByName(element, "params");
		hrScriptPluginDef.setScript(el.getTextContent());
		hrScriptPluginDef.setDescription(descEL==null?"人员脚本":descEL.getTextContent());
		hrScriptPluginDef.setScriptId(idEl==null?"":idEl.getTextContent());
		hrScriptPluginDef.setParams(paramsEl==null?"":paramsEl.getTextContent());
		return hrScriptPluginDef;
	}
	// <hrScript xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/hrScript" logicCal=""
	// extract="">
	// <content>
	// <![CDATA[
	//
	// ]]>
	// </content>
	// </hrScript>
	@Override
	public String getPluginXml() {
		HrScriptPluginDef def = (HrScriptPluginDef) this.getBpmPluginDef();
		if (def == null)
			return "";
		StringBuffer sb = new StringBuffer();
		sb.append("<hrScript xmlns=\"http://www.jee-soft.cn/bpm/plugins/userCalc/hrScript\" ");
		sb.append("  logicCal=\"" + def.getLogicCal().getKey() + "\" extract=\"" + def.getExtract().getKey() + "\">");
		sb.append("<content>");
		sb.append("<![CDATA[");
		sb.append(def.getScript());
		sb.append("]]>");
		sb.append("</content>");
		
		sb.append("<description>");
		sb.append("<![CDATA[");
		sb.append(def.getDescription());
		sb.append("]]>");
		sb.append("</description>");
		
		sb.append("<scriptId>");
		sb.append("<![CDATA[");
		sb.append(def.getScriptId());
		sb.append("]]>");
		sb.append("</scriptId>");
		
		sb.append("<params>");
		sb.append("<![CDATA[");
		sb.append(def.getParams());
		sb.append("]]>");
		sb.append("</params>");
		
		sb.append("</hrScript>");
		return sb.toString();
	}
	@Override
	protected BpmPluginDef parseJson(ObjectNode pluginJson) {
		HrScriptPluginDef def = new HrScriptPluginDef();
		String script = pluginJson.get("script").asText();
		String description=JsonUtil.getString(pluginJson, "description","人员脚本"); 
		def.setScript(script);
		def.setDescription(description);
		if(JsonUtil.isContainsKey(pluginJson,"scriptId")){
			String scriptId = pluginJson.get("scriptId").asText();
			def.setScriptId(scriptId);
		}
		if(JsonUtil.isContainsKey(pluginJson,"params")){
			String params = null;
			try {
				params = JsonUtil.toJson(pluginJson.get("params"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			def.setParams(params);
		}
		return def;
	}
}
