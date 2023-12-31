package com.hotent.bpm.plugin.usercalc.position.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

/**
 * 岗位作为审批人的定义
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年11月18日
 */
public class PositionPluginDef extends AbstractUserCalcPluginDef {
	private static final long serialVersionUID = -4275133246687703477L;
	/**
	 * 岗位代码
	 */
	private String posCode;
	/**
	 * 岗位名称
	 */
	private String posName;
	public String getPosCode() {
		return posCode;
	}
	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}
	public String getPosName() {
		return posName;
	}
	public void setPosName(String posName) {
		this.posName = posName;
	}
}
