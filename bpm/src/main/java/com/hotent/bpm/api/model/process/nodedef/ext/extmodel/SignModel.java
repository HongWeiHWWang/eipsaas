package com.hotent.bpm.api.model.process.nodedef.ext.extmodel;

import java.io.Serializable;
import java.util.List;

/**
 * 会签规则对象。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-16-下午1:41:28
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class SignModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private SignRule signRule;
	
	private List<PrivilegeItem> privilegeList;
	
	

	public SignRule getSignRule() {
		return signRule;
	}

	public void setSignRule(SignRule signRule) {
		this.signRule = signRule;
	}

	public List<PrivilegeItem> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<PrivilegeItem> privilegeList) {
		this.privilegeList = privilegeList;
	}
	
	

}
