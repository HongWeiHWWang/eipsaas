package com.hotent.bpm.plugin.usercalc.position.context;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.position.def.PositionPluginDef;
import com.hotent.bpm.plugin.usercalc.position.runtime.PositionPlugin;

/**
 * 岗位作为流程审批人的上下文对象
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年11月18日
 */
public class PositionPluginContext extends AbstractUserCalcPluginContext {
	private static final long serialVersionUID = -5261531157333798291L;

	@Override
	public String getDescription() {
		PositionPluginDef def = (PositionPluginDef)this.getBpmPluginDef();
		if(def==null) return "";
		return def.getPosName();
	}

	@Override
	public String getTitle() {
		return "岗位";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return PositionPlugin.class;
	}
	
	/**
	 * <position posCode="" posName="" />
	 */
	@Override
	public String getPluginXml() {
		BpmPluginDef bpmPluginDef = getBpmPluginDef();
		PositionPluginDef def = (PositionPluginDef)bpmPluginDef;
		if(def==null) return "";
		String xml = String.format("<position xmlns=\"http://www.jee-soft.cn/bpm/plugins/userCalc/position\" logicCal=\"%s\"  extract=\"%s\" posCode=\"%s\" posName=\"%s\" />", 
								   def.getLogicCal(), def.getExtract(), def.getPosCode(), def.getPosName());
		return xml;
	}
	
	@Override
	protected BpmPluginDef parseElement(Element element) {
		PositionPluginDef def = new PositionPluginDef();
		
		String posCode = element.getAttribute("posCode");
		String posName = element.getAttribute("posName");
		def.setPosCode(posCode);
		def.setPosName(posName);
		return def;
	}

	@Override
	protected BpmPluginDef parseJson(ObjectNode pluginJson) throws Exception {
		PositionPluginDef def = new PositionPluginDef();
		String posCode = JsonUtil.getString(pluginJson, "posCode");
		String posName = JsonUtil.getString(pluginJson, "posName");
		def.setPosCode(posCode);
		def.setPosName(posName);
		return def;
	}
}
