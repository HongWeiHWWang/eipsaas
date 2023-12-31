package com.hotent.bpm.plugin.usercalc.org.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.usercalc.org.def.OrgPluginDef;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.model.Org;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.service.IOrgService;

/**
 * 部门作为审批人的插件
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年11月18日
 */
public class OrgPlugin extends AbstractUserCalcPlugin{

	@Override
	public List<BpmIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) {
		List<BpmIdentity> identityList=new ArrayList<BpmIdentity>();
		OrgPluginDef def = (OrgPluginDef)pluginDef;
		String orgCode = def.getOrgCode();
		String orgName = def.getOrgName();
		if(StringUtil.isEmpty(orgCode)) return identityList;
		IOrgService orgEngine= pluginSession.getOrgEngine();
		Map<String, Object> vars= pluginSession.getVariables();
		boolean isReqUc = StringUtil.isEmpty((String)vars.get(BpmConstants.NOT_REQUEST_UC));
		IGroup group = null;
		if (isReqUc) {
			group = orgEngine.getUserGroupService().getGroupByIdOrCode(GroupTypeConstant.ORG.key(), orgCode);
		}else {
			group = buildOrgGroup(orgCode, orgName);
		}
		BpmIdentity bpmIdentity = getBpmIdentityConverter().convertGroup(group);
		identityList.add(bpmIdentity);
		return identityList;
	}
	
	private IGroup buildOrgGroup(String orgCode, String orgName) {
		Org org = new Org();
		org.setCode(orgCode);
		org.setName(orgName);
		return org;
	}
}
