package com.hotent.bpm.plugin.usercalc.job.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.usercalc.job.def.JobPluginDef;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.model.OrgJob;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.service.IOrgService;

/**
 * 岗位作为审批人的插件
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年11月18日
 */
public class JobPlugin extends AbstractUserCalcPlugin{

	@Override
	public List<BpmIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) {
		List<BpmIdentity> identityList=new ArrayList<BpmIdentity>();
		JobPluginDef def = (JobPluginDef)pluginDef;
		String jobCode = def.getJobCode();
		String jobName = def.getJobName();
		if(StringUtil.isEmpty(jobCode)) return identityList;
		IOrgService orgEngine= pluginSession.getOrgEngine();
		Map<String, Object> vars= pluginSession.getVariables();
		boolean isReqUc = StringUtil.isEmpty((String)vars.get(BpmConstants.NOT_REQUEST_UC));
		IGroup group = null;
		if (isReqUc) {
			group = orgEngine.getUserGroupService().getGroupByIdOrCode(GroupTypeConstant.JOB.key(), jobCode);
		}else {
			group = buildJobGroup(jobCode, jobName);
		}
		BpmIdentity bpmIdentity = getBpmIdentityConverter().convertGroup(group);
		identityList.add(bpmIdentity);
		return identityList;
	}
	
	private IGroup buildJobGroup(String jobCode, String jobName) {
		OrgJob job = new OrgJob();
		job.setCode(jobCode);
		job.setName(jobName);
		return job;
	}
}
