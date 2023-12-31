package com.hotent.bpm.api.model.process.nodedef.ext.extmodel;

import java.io.Serializable;
import java.util.List;

import com.hotent.bpm.api.constant.PrivilegeMode;

/**
 * 特权对象定义。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-14-上午9:51:24
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class PrivilegeItem implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PrivilegeItem(){
		
	}
	
	
	public PrivilegeItem(PrivilegeMode privilegeMode,List<UserAssignRule> userRuleList){
		this.privilegeMode=privilegeMode;
		this.userRuleList=userRuleList;
	}
	
	/**
	 * 特权模式
	 */
	private PrivilegeMode privilegeMode;
	/**
	 * 对应的成员插件。
	 */
	private List<UserAssignRule> userRuleList;
	
	public PrivilegeMode getPrivilegeMode() {
		return privilegeMode;
	}

	public void setPrivilegeMode(PrivilegeMode privilegeMode) {
		this.privilegeMode = privilegeMode;
	}


	public List<UserAssignRule> getUserRuleList() {
		return userRuleList;
	}


	public void setUserRuleList(List<UserAssignRule> userRuleList) {
		this.userRuleList = userRuleList;
	}

	

	
}
