package com.hotent.bpm.plugin.task.userassign.def;



import java.util.ArrayList;
import java.util.List;

import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.plugin.core.plugindef.AbstractBpmTaskPluginDef;

/**
 * <pre> 
 * 描述：用户授权插件
 * 构建组：x5-bpmx-core
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-2-14-上午10:16:03
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class UserAssignPluginDef extends AbstractBpmTaskPluginDef {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4087177558628186926L;
	List<UserAssignRule> ruleList=new ArrayList<UserAssignRule>();

	public List<UserAssignRule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<UserAssignRule> ruleList) {
		this.ruleList = ruleList;
	}
	

}
