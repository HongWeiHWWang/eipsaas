package com.hotent.bpm.plugin.usercalc.approver.context;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.approver.def.ApproverPluginDef;
import com.hotent.bpm.plugin.usercalc.approver.runtime.ApproverPlugin;

/**
 * 流程实例审批人
 * @author Administrator
 *
 */
public class ApproverPluginContext extends AbstractUserCalcPluginContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6561914713551147197L;

	@Override
	public String getDescription() {
		
		return "流程实例审批人";
	}

	@Override
	public String getTitle() {
		return "流程实例审批人";
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return ApproverPlugin.class;
	}

	@Override
	public String getPluginXml() {
		ApproverPluginDef def=(ApproverPluginDef)getBpmPluginDef();
		if(def==null) return "";
		return "<approver xmlns=\"http://www.jee-soft.cn/bpm/plugins/userCalc/approver\"" + 
			  "	logicCal=\""+ def.getLogicCal().getKey() +"\" extract=\""+def.getExtract().getKey() +"\"/>";
	}

	@Override
	protected BpmPluginDef parseElement(Element element) {
		ApproverPluginDef def=new ApproverPluginDef();
		return def;
	}

	@Override
	protected BpmPluginDef parseJson(ObjectNode pluginJson) {
		ApproverPluginDef def=new ApproverPluginDef();
		return def;
	}

}
