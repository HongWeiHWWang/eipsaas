package com.hotent.bpm.plugin.usercalc.script.context;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.XmlUtil;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.script.def.ScriptPluginDef;
import com.hotent.bpm.plugin.usercalc.script.runtime.ScriptPlugin;

public class ScriptPluginContext extends AbstractUserCalcPluginContext{

	private static final long serialVersionUID = -2353875054502587417L;

	@Override
	public String getDescription() {
		ScriptPluginDef def=(ScriptPluginDef)this.getBpmPluginDef();
		if(def==null) return "";
		return def.getDescription();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return ScriptPlugin.class;
	}
	@Override
	public String getTitle() {
		return "脚本";
	}
	@Override
	protected BpmPluginDef parseElement(Element element) {
		ScriptPluginDef hrScriptPluginDef=new ScriptPluginDef();
		Element el= XmlUtil.getChildNodeByName(element, "content");
		Element descEL = XmlUtil.getChildNodeByName(element, "description");
		Element idEl= XmlUtil.getChildNodeByName(element, "scriptId");
		Element paramsEl= XmlUtil.getChildNodeByName(element, "params");
		hrScriptPluginDef.setDescription(descEL==null?"脚本":descEL.getTextContent());
		hrScriptPluginDef.setScript(el.getTextContent());
		hrScriptPluginDef.setScriptId(idEl==null?"":idEl.getTextContent());
		hrScriptPluginDef.setParams(paramsEl==null?"":paramsEl.getTextContent());
		return hrScriptPluginDef;
	}

	//<hrScript xmlns="http://www.jee-soft.cn/bpm/plugins/userCalc/hrScript"   logicCal="" extract="">
	//    <content>
	//        <![CDATA[
	//        
	//        ]]>
	//    </content>
	//</hrScript>
	@Override
	public String getPluginXml() {
		ScriptPluginDef def=(ScriptPluginDef) this.getBpmPluginDef();
		if(def==null) return "";
		StringBuffer sb=new StringBuffer();
		sb.append("<hrScript xmlns=\"http://www.jee-soft.cn/bpm/plugins/userCalc/hrScript\" ");
		sb.append("  logicCal=\""+ def.getLogicCal().getKey() +"\" extract=\""+ def.getExtract().getKey() +"\">");
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
		ScriptPluginDef def=new ScriptPluginDef();
		String script=pluginJson.get("script").asText();
		String description=JsonUtil.getString(pluginJson, "description","脚本"); 
		def.setScript(script);
		def.setDescription(description);
		if(JsonUtil.isContainsKey(pluginJson,"scriptId")){
			String scriptId = pluginJson.get("scriptId").asText();
			def.setScriptId(scriptId);
		}
		if(JsonUtil.isContainsKey(pluginJson,"params")){
			String params = pluginJson.get("params").asText();
			def.setParams(params);
		}
		return def;
	}



	

}
