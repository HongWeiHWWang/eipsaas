package com.hotent.bpm.api.exception;

/**
 * 流程定义异常。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-17-下午2:27:42
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ProcessDefException extends RuntimeException {
	
	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -677697411072424596L;

	public ProcessDefException(String msg){
		
		super(msg);
	}
	
	public ProcessDefException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}

}
