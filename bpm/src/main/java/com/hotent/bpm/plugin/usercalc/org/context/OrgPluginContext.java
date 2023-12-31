package com.hotent.bpm.plugin.usercalc.org.context;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.org.def.OrgPluginDef;
import com.hotent.bpm.plugin.usercalc.org.runtime.OrgPlugin;

/**
 * 部门作为流程审批人的上下文对象
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年11月18日
 */
public class OrgPluginContext extends AbstractUserCalcPluginContext {
	private static final long serialVersionUID = -5261531157333798291L;

	@Override
	public String getDescription() {
		OrgPluginDef def = (OrgPluginDef)this.getBpmPluginDef();
		if(def==null) return "";
		return def.getOrgName();
	}

	@Override
	public String getTitle() {
		return "部门";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return OrgPlugin.class;
	}
	
	/**
	 * <Org orgCode="" orgName="" />
	 */
	@Override
	public String getPluginXml() {
		BpmPluginDef bpmPluginDef = getBpmPluginDef();
		OrgPluginDef def = (OrgPluginDef)bpmPluginDef;
		if(def==null) return "";
		String xml = String.format("<org xmlns=\"http://www.jee-soft.cn/bpm/plugins/userCalc/org\" logicCal=\"%s\"  extract=\"%s\" orgCode=\"%s\" orgName=\"%s\" />", 
								   def.getLogicCal(), def.getExtract(), def.getOrgCode(), def.getOrgName());
		return xml;
	}
	
	@Override
	protected BpmPluginDef parseElement(Element element) {
		OrgPluginDef def = new OrgPluginDef();
		
		String orgCode = element.getAttribute("orgCode");
		String orgName = element.getAttribute("orgName");
		def.setOrgCode(orgCode);
		def.setOrgName(orgName);
		return def;
	}

	@Override
	protected BpmPluginDef parseJson(ObjectNode pluginJson) throws Exception {
		OrgPluginDef def = new OrgPluginDef();
		String orgCode = JsonUtil.getString(pluginJson, "orgCode");
		String orgName = JsonUtil.getString(pluginJson, "orgName");
		def.setOrgCode(orgCode);
		def.setOrgName(orgName);
		return def;
	}
}
