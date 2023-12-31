package com.hotent.bpm.plugin.usercalc.org.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

/**
 * 部门作为审批人的定义
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年11月18日
 */
public class OrgPluginDef extends AbstractUserCalcPluginDef {
	private static final long serialVersionUID = -4275133246687703477L;
	/**
	 * 部门代码
	 */
	private String orgCode;
	/**
	 * 部门名称
	 */
	private String orgName;
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}
