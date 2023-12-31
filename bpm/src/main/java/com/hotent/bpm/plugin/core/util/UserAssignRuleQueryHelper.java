/**
 * 描述：TODO
 * 包名：com.hotent.bpm.plugin.task.userassign.plugin
 * 文件名：UserQueryHelper.java
 * 作者：win-mailto:chensx@jee-soft.cn
 * 日期2014-4-4-上午11:34:56
 *  2014广州宏天软件有限公司版权所有
 * 
 */
package com.hotent.bpm.plugin.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.constant.LogicType;
import com.hotent.bpm.api.helper.identity.IConditionCheck;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.context.UserCalcPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmUserCalcPluginDef;
import com.hotent.bpm.api.plugin.core.factory.BpmPluginSessionFactory;
import com.hotent.bpm.api.plugin.core.runtime.BpmUserCalcPlugin;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.uc.api.model.IUser;

/**
 * <pre> 
 * 构建组：x5-bpmx-plugin
 * 作者：Winston Yan
 * 邮箱：yancm@jee-soft.cn
 * 日期：2014-4-4-上午11:34:56
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class UserAssignRuleQueryHelper {
	/**
	 * 根据用户规则列表和BpmUserCalcPluginSession返回BpmIdentity列表。
	 * @param userAssignRules
	 * @param pluginSession
	 * @return List&lt;BpmIdentity>
	 * @throws Exception 
	 */
	public static List<BpmIdentity> query(List<UserAssignRule> userAssignRules,BpmUserCalcPluginSession pluginSession) throws Exception{
		//首先对规则进行排序。
		Collections.sort(userAssignRules, new UserAssignRuleComparator());
		List<BpmIdentity> bpmIdentities = calcByRule(userAssignRules, pluginSession,false);
		return bpmIdentities;
	}
	
	/**
	 * 根据流程规则获取用户列表，这个对用户进行抽取。
	 * @param userAssignRules
	 * @param pluginSession
	 * @return 
	 * List&lt;BpmIdentity>
	 * @throws Exception 
	 */
	public static List<BpmIdentity> queryExtract(List<UserAssignRule> userAssignRules,BpmUserCalcPluginSession pluginSession) throws Exception{
		Collections.sort(userAssignRules, new UserAssignRuleComparator());
		List<BpmIdentity> bpmIdentities = calcByRule(userAssignRules, pluginSession,true);
		return bpmIdentities;
	}
	
	/**
	 * 根据流程规则和流程变量获取任务执行人列表。
	 * @param userAssignRules
	 * @param variables
	 * @return  List&lt;User>
	 * @throws Exception 
	 */
	public static List<IUser> queryUsersWithExtract(List<UserAssignRule> userAssignRules,Map<String,Object> variables) throws Exception{
		BpmPluginSessionFactory sessionFactory = AppUtil.getBean(BpmPluginSessionFactory.class);
		BpmUserCalcPluginSession bpmUserCalcPluginSession = sessionFactory.buildBpmUserCalcPluginSession(variables);
		//查询插件定义的接收者（强制抽取）
		List<BpmIdentity> pluginBpmIdentities =UserAssignRuleQueryHelper.queryExtract(userAssignRules,bpmUserCalcPluginSession);
		//查询插件定义的用户				 				
		List<IUser> users = UserConverter.queryAndConvert(pluginBpmIdentities);		
		return users;
	}
	
	
	/**
	 * 根据规则列表和BpmUserCalcPluginSession和是否抽取人员获取BpmIdentity列表。
	 * @param ruleList
	 * @param pluginSession
	 * @param forceExtract
	 * @return 
	 * List&lt;BpmIdentity>
	 * @throws Exception 
	 */
	private static List<BpmIdentity> calcByRule(List<UserAssignRule> ruleList,BpmUserCalcPluginSession pluginSession,boolean forceExtract) throws Exception{
		List<BpmIdentity> bpmIdentities = new ArrayList<BpmIdentity>();
		int groupNo=-1;
		for(UserAssignRule userRule:ruleList){			
			int tmpGroupNo=userRule.getGroupNo();
			
			//批次号GroupNo，由低到高开始计算，如果计算出用户，则会返回不再计算。
			if(groupNo!=tmpGroupNo && bpmIdentities.size()>0) break;
			boolean isValid=isRuleValid(userRule, pluginSession);
			//规则无效不进行循环
			if(!isValid) continue;
			//对用户组进行赋值。
			groupNo=tmpGroupNo;
			
			List<UserCalcPluginContext> calcList= userRule.getCalcPluginContextList();
			
			for(UserCalcPluginContext context:calcList){
				//获得计算运行时
				BpmUserCalcPlugin plugin=(BpmUserCalcPlugin) AppUtil.getBean(context.getPluginClass());
				BpmUserCalcPluginDef pluginDef =(BpmUserCalcPluginDef) context.getBpmPluginDef();
				if(forceExtract){
					pluginDef.setExtract(ExtractType.EXACT_EXACT_USER);
				}
				//查询
				List<BpmIdentity> biList= plugin.execute(pluginSession,context.getBpmPluginDef());				
				
				if(biList==null) continue;
				
				if(bpmIdentities.size()==0){
					bpmIdentities.addAll(biList);
				}else{
					//集合运算
					calc(bpmIdentities, biList,pluginDef.getLogicCal());
				}
			}
		}
		return bpmIdentities;
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
	
	/**
	 *  人员进行逻辑运算。
	 * @param existBpmIdentities
	 * @param newBpmIdentities
	 * @param logic 
	 * void
	 */
	private static void calc(List<BpmIdentity> existBpmIdentities,List<BpmIdentity> newBpmIdentities,LogicType logic){
		switch (logic) {
		
			case OR:
				Set<BpmIdentity> set=new LinkedHashSet<BpmIdentity>();
				set.addAll(existBpmIdentities);
				set.addAll(newBpmIdentities);
				existBpmIdentities.clear();
				existBpmIdentities.addAll(set);
				break;
			case AND:
				List<BpmIdentity> rtnList=new ArrayList<BpmIdentity>();
				for(BpmIdentity identity:existBpmIdentities){
					for(BpmIdentity tmp:newBpmIdentities){
						if(identity.equals(tmp)){
							rtnList.add(identity);
						}
					}
				}
				existBpmIdentities.clear();
				existBpmIdentities.addAll(rtnList);
				break;
			
			default:
				for(BpmIdentity tmp:newBpmIdentities){
					existBpmIdentities.remove(tmp);
				}
				break;
		}
			
		
	}
}
