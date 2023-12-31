
package com.hotent.bpm.plugin.task.userassign.plugin;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.runtime.AbstractUserCalcPlugin;
import com.hotent.bpm.plugin.core.session.DefaultBpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.util.UserAssignRuleQueryHelper;
import com.hotent.bpm.plugin.task.userassign.def.UserAssignPluginDef;

/**
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-plugin
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-3-30-下午6:26:11
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class UserQueryPlugin extends AbstractUserCalcPlugin{


	@Override
	public List<BpmIdentity> execute(BpmUserCalcPluginSession pluginSession,BpmPluginDef pluginDef) throws Exception {
		UserAssignPluginDef assignPluginDef=(UserAssignPluginDef)pluginDef;
		List<UserAssignRule> ruleList = assignPluginDef.getRuleList();
		ruleList = filterUserRule(ruleList,pluginSession);
		List<BpmIdentity> bpmIdentities = UserAssignRuleQueryHelper.query(ruleList, pluginSession);
		
		/**排除掉通过配置查出为null的数据  很有用**/
		List<BpmIdentity> identitieList = new ArrayList<BpmIdentity>();
		if(BeanUtils.isNotEmpty(bpmIdentities))
		for (BpmIdentity identity:bpmIdentities) {
			if(identity == null) continue;
			identitieList.add(identity);
		}
		
		return identitieList;
	}
	
	//过滤非本流程人员
	private List<UserAssignRule> filterUserRule(List<UserAssignRule> ruleList,BpmUserCalcPluginSession pluginSession){
		List<UserAssignRule> ruleLists = new ArrayList<UserAssignRule>();
		/***************当flowKey不一致时不加入***************/
		for(UserAssignRule userRule:ruleList){	
			Object flowKey_ = pluginSession.getVariables().get(BpmConstants.BPM_FLOW_KEY); 
			String parentFlowKey = userRule.getParentFlowKey();
			if(flowKey_!=null&&!flowKey_.toString().equals(parentFlowKey)){
				continue;
			}else{
				ruleLists.add(userRule);
			}
		}
		/***********当ruleLists为空时，加入local_的************/
		if(ruleLists==null || ruleLists.size()==0){
			for(UserAssignRule userRule:ruleList){	
				String parentFlowKey = userRule.getParentFlowKey();
				if(parentFlowKey!=null&&parentFlowKey.equals(BpmConstants.LOCAL)){
					ruleLists.add(userRule);
				}
			}
		}
		return ruleLists;
	}
	
	@SuppressWarnings("unused")
	private List<UserAssignRule> getRuleList(DefaultBpmUserCalcPluginSession pluginSession){
		
		return null;
	}
	
	
	@Override
	public List<BpmIdentity> queryByPluginDef(
			BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) {
		return null;
	}


}
