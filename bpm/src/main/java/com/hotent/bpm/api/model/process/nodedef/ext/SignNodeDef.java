package com.hotent.bpm.api.model.process.nodedef.ext;

import java.util.List;

import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.PrivilegeItem;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.SignRule;



/**
 * 会签定义，这里定义会签的规则和特权。
 
 * <pre> 
 * 1.配置了会签规则。
 * 2.配置特权模式。
 * 3.是否并发执行。
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-14-上午9:56:25
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class SignNodeDef  extends UserTaskNodeDef {
	/**
	 * 
	 */
	private static final long serialVersionUID = -12965499812901552L;

	/**
	 * 是否串行会签。
	 */
	private boolean isParallel=false;
	
	/**
	 * 会签规则
	 */
	private SignRule signRule;
	
	/**
	 * 特权模式。
	 */
	private List<PrivilegeItem> privilegeList;

	
	@Override
	public void setParallel(boolean isParallel) {
		this.isParallel = isParallel;
	}
	
	@Override
	public boolean isParallel() {
	
		return this.isParallel;
	}

	/**
	 * 会签规则。
	 * @return  SignRule
	 */
	public SignRule getSignRule() {
		return signRule;
	}

	public void setSignRule(SignRule signRule) {
		this.signRule = signRule;
	}

	/**
	 * 特权列表。
	 * @return 
	 * List&lt;PrivilegeItem>
	 */
	public List<PrivilegeItem> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<PrivilegeItem> privilegeList) {
		this.privilegeList = privilegeList;
	}
	
	
	@Override
	public boolean supportMuliInstance() {
		return true;
	}

}
