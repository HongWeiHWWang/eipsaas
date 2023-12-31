package com.hotent.bpm.persistence.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.helper.identity.IConditionCheck;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.context.UserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.persistence.dao.BpmAgentConditionDao;
import com.hotent.bpm.persistence.manager.BpmAgentConditionManager;
import com.hotent.bpm.persistence.model.BpmAgentCondition;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmAgentConditionManager")
public class BpmAgentConditionManagerImpl extends BaseManagerImpl<BpmAgentConditionDao, BpmAgentCondition> implements BpmAgentConditionManager{
	@Resource
	BpmPluginSessionFactory bpmPluginSessionFactory;
	@Resource
	BpmAgentConditionDao bpmAgentConditionDao;

	
	/**
	 * 检查条件代理设置的条件，如果通过返回true，不通过返回false
	 * @param con
	 * @param busData
	 * @param vars
	 * @return
	 */
	@Override
	public boolean checkCondition(BpmDelegateTask delegateTask,BpmAgentCondition con) {
		if(BeanUtils.isEmpty(con.getCondition()))return true;
		BpmUserCalcPluginSession bpmUserCalcPluginSession= AppUtil.getBean(BpmPluginSessionFactory.class).buildBpmUserCalcPluginSession(delegateTask);
		UserAssignRule userAgentRule  = new UserAssignRule();
		List<UserCalcPluginContext> calcPluginContextList = new ArrayList<UserCalcPluginContext>();
		userAgentRule.setCalcPluginContextList(calcPluginContextList);
		userAgentRule.setCondition(con.getCondition().toString());
		return isRuleValid(userAgentRule,bpmUserCalcPluginSession);
	}
	/**
	 * 调用接口进行条件检查。
	 * @param rule
	 * @param pluginSession
	 * @return 
	 * boolean
	 */
	private static boolean isRuleValid(UserAssignRule rule,BpmUserCalcPluginSession pluginSession){
		
		IConditionCheck conditionCheck=(IConditionCheck) AppUtil.getBean("userConditionCheck");
		
		return conditionCheck.check(rule.getCondition(), rule.getConditionMode(), pluginSession);
	}
	@Override
    @Transactional
	public void removeBySettingId(String pk) {
        baseMapper.removeBySettingId(pk);
	}
	@Override
	public List<BpmAgentCondition> getBySettingId(String id) {
		return baseMapper.getBySettingId(id);
	}
}
