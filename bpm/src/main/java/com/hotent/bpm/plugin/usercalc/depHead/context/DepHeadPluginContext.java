package com.hotent.bpm.plugin.usercalc.depHead.context;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.depHead.def.DepHeadPluginDef;
import com.hotent.bpm.plugin.usercalc.depHead.runtime.DepHeadPlugin;

/**
 * 发起人的部门负责人
 * @author Administrator
 *
 */
public class DepHeadPluginContext extends AbstractUserCalcPluginContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6561914713551147197L;

	@Override
	public String getDescription() {
		
		return "发起人的部门负责人";
	}

	@Override
	public String getTitle() {
		return "发起人的部门负责人";
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return DepHeadPlugin.class;
	}

	@Override
	public String getPluginXml() {
		DepHeadPluginDef def=(DepHeadPluginDef)getBpmPluginDef();
		if(def==null) return "";
		return "<depHead xmlns=\"http://www.jee-soft.cn/bpm/plugins/userCalc/depHead\"" + 
			  "	logicCal=\""+ def.getLogicCal().getKey() +"\" extract=\""+def.getExtract().getKey() +"\" mainLeader=\""+def.isMainLeader() +"\"/>";
	}

	@Override
	protected BpmPluginDef parseElement(Element element) {
		DepHeadPluginDef def=new DepHeadPluginDef();
		if (StringUtil.isNotEmpty(element.getAttribute("mainLeader"))) {
			def.setMainLeader(Boolean.valueOf(element.getAttribute("mainLeader")));
		}
		return def;
	}

	@Override
	protected BpmPluginDef parseJson(ObjectNode pluginJson) {
		DepHeadPluginDef def=new DepHeadPluginDef();
		def.setMainLeader(JsonUtil.getBoolean(pluginJson, "mainLeader", false));
		return def;
	}

}
