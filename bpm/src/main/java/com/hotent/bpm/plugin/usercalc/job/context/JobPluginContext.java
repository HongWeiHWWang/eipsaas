package com.hotent.bpm.plugin.usercalc.job.context;

import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.plugin.core.context.AbstractUserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.RunTimePlugin;
import com.hotent.bpm.plugin.usercalc.job.def.JobPluginDef;
import com.hotent.bpm.plugin.usercalc.job.runtime.JobPlugin;

/**
 * 岗位作为流程审批人的上下文对象
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年11月18日
 */
public class JobPluginContext extends AbstractUserCalcPluginContext {
	private static final long serialVersionUID = -5261531157333798291L;

	@Override
	public String getDescription() {
		JobPluginDef def = (JobPluginDef)this.getBpmPluginDef();
		if(def==null) return "";
		return def.getJobName();
	}

	@Override
	public String getTitle() {
		return "职务";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends RunTimePlugin> getPluginClass() {
		return JobPlugin.class;
	}
	
	/**
	 * <Job jobCode="" jobName="" />
	 */
	@Override
	public String getPluginXml() {
		BpmPluginDef bpmPluginDef = getBpmPluginDef();
		JobPluginDef def = (JobPluginDef)bpmPluginDef;
		if(def==null) return "";
		String xml = String.format("<job xmlns=\"http://www.jee-soft.cn/bpm/plugins/userCalc/job\" logicCal=\"%s\"  extract=\"%s\" jobCode=\"%s\" jobName=\"%s\" />", 
								   def.getLogicCal(), def.getExtract(), def.getJobCode(), def.getJobName());
		return xml;
	}
	
	@Override
	protected BpmPluginDef parseElement(Element element) {
		JobPluginDef def = new JobPluginDef();
		
		String jobCode = element.getAttribute("jobCode");
		String jobName = element.getAttribute("jobName");
		def.setJobCode(jobCode);
		def.setJobName(jobName);
		return def;
	}

	@Override
	protected BpmPluginDef parseJson(ObjectNode pluginJson) throws Exception {
		JobPluginDef def = new JobPluginDef();
		String jobCode = JsonUtil.getString(pluginJson, "jobCode");
		String jobName = JsonUtil.getString(pluginJson, "jobName");
		def.setJobCode(jobCode);
		def.setJobName(jobName);
		return def;
	}
}
