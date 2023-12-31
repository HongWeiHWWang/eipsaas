package com.hotent.bpm.plugin.task.userassign.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.def.BpmTaskPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmTaskPluginSession;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.bpm.plugin.core.runtime.BaseUserAssignPlugin;
import com.hotent.bpm.plugin.task.userassign.def.UserAssignPluginDef;

/**
 * 用户分配插件，继承自{@linkplain com.hotent.bpm.plugin.task.userassign.plugin.UserAssignPlugin 基础用户插件}。
 * <pre>
 * 	调用节点配置插件计算用户并添加到当前任务执行人中。
 * </pre>
 * @author ray
 * 
 */
public class UserAssignPlugin extends BaseUserAssignPlugin{	
	
	
	@Override
	public void executeExt(BpmTaskPluginSession pluginSession,BpmTaskPluginDef pluginDef) throws Exception {
		BpmDelegateTask bpmDelegateTask=pluginSession.getBpmDelegateTask();
		//获取顶级流程的父类key。
		String parentFlowKey = (String) bpmDelegateTask.getSupperVariable(BpmConstants.BPM_FLOW_KEY);
		UserAssignPluginDef assignPluginDef = new UserAssignPluginDef();
		try {
			BeanUtils.copyNotNullProperties(assignPluginDef, (UserAssignPluginDef)pluginDef);
		} catch (Exception e) {
			throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
		} 
		//过滤人员规则。
		handelRules(assignPluginDef,parentFlowKey);
		
		BpmUserCalcPluginSession bpmUserCalcPluginSession = getBpmPluginSessionFactory().buildBpmUserCalcPluginSession(bpmDelegateTask);		
		//调用人员计算插件对人员进行计算。 
		UserQueryPlugin userQueryPlugin = AppUtil.getBean(UserQueryPlugin.class);		
		List<BpmIdentity> bpmIdentities = userQueryPlugin.execute(bpmUserCalcPluginSession, assignPluginDef);
		
		//将人员添加到执行人中。
		if(BeanUtils.isNotEmpty(bpmIdentities)){
			bpmDelegateTask.addExecutors(bpmIdentities);
		}		
	}

	
	/**
	 * 如果是子流程，处理userRules , local_类型的 groupNo加 100 使子流程独自配置排在后面。
	 */
	private void handelRules(UserAssignPluginDef assignPluginDef,String parentFlowKey) {
		//过滤人员规则。
		if(StringUtil.isEmpty(parentFlowKey)) {
			parentFlowKey=BpmConstants.LOCAL;
		}
		List<UserAssignRule> rules = assignPluginDef.getRuleList();
		if(BeanUtils.isEmpty(rules)) return ;
		//如果没有找父流程相关配置，则查找本地的配置。
		List<UserAssignRule> assignRules = getAssignRules(rules,parentFlowKey);
		if(!BpmConstants.LOCAL.equals(parentFlowKey) && BeanUtils.isEmpty(assignRules)){
			assignRules= getAssignRules(rules,BpmConstants.LOCAL);
		}
		assignPluginDef.setRuleList(assignRules); 
	}
	
	private List<UserAssignRule> getAssignRules(List<UserAssignRule> rules ,String key){
		List<UserAssignRule> assignRules = new ArrayList<UserAssignRule>();
		for(UserAssignRule rule : rules){
			String parentKey=rule.getParentFlowKey();
			if(parentKey.equals(BpmConstants.LOCAL)){
				rule.setGroupNo(rule.getGroupNo()+100);
				assignRules.add(rule);
			}
			else if(parentKey.equals(key)){
				assignRules.add(rule);
			}
		}
		return assignRules;
	}
	
	
}
