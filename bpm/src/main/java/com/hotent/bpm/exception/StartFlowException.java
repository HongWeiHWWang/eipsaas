package com.hotent.bpm.exception;

/**
 * 启动流程的异常。
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-11-下午5:31:58
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class StartFlowException extends RuntimeException {
	

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -7289238698048230824L;

	public StartFlowException(String msg){
		
		super(msg);
	}
	
	public StartFlowException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}

}
