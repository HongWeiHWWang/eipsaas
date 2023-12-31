package com.hotent.bpm.api.model.process.task;

/**
 * 跳过任务结果对象。
 * <pre>
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-19-下午3:03:10
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class SkipResult {

	public static String SKIP_FIRST="first";
	public static String SKIP_SAME_USER="sameUser";
	public static String SKIP_EMPTY_USER="emptyUser";
	public static String SKIP_APPROVER = "approver";
	public static String SKIP_ALL = "all";
	public static final String SKIP_APPROVER_AUDITOR = "Auditor";
	public static final String SKIP_APPROVER_AUDITORNAME = "auditorName";

	/**
	 * 已经计算跳过任务。
	 */
	private boolean hasGetSkip=false;
	/**
	 * 是否跳过。
	 */
	private boolean skipTask=false;
	/**
	 * 跳过类型。
	 */
	private String skipType=SKIP_SAME_USER;

	public SkipResult(){}


	public SkipResult(boolean hasGetSkip, boolean skipTask, String skipType) {
		this.hasGetSkip = hasGetSkip;
		this.skipTask = skipTask;
		this.skipType = skipType;
	}


	public boolean isHasGetSkip() {
		return hasGetSkip;
	}
	public void setHasGetSkip(boolean hasGetSkip) {
		this.hasGetSkip = hasGetSkip;
	}
	public boolean isSkipTask() {
		return skipTask;
	}
	public void setSkipTask(boolean skipTask) {
		this.skipTask = skipTask;
	}
	public String getSkipType() {
		return skipType;
	}
	public void setSkipType(String skipType) {
		this.skipType = skipType;
	}







}
