package com.hotent.bpm.exception;

/**
 * 审批任务异常。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-11-下午5:33:18
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ApproveTaskException extends RuntimeException {

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -6237354795951526417L;

	public ApproveTaskException(String msg){
		
		super(msg);
	}
	
	public ApproveTaskException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}

}
