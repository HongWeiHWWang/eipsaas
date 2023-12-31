package com.hotent.bpm.plugin.usercalc.job.def;

import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

/**
 * 岗位作为审批人的定义
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年11月18日
 */
public class JobPluginDef extends AbstractUserCalcPluginDef {
	private static final long serialVersionUID = -4275133246687703477L;
	/**
	 * 岗位代码
	 */
	private String jobCode;
	/**
	 * 岗位名称
	 */
	private String jobName;
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
}
