package com.hotent.bpm.api.exception;

/**
 * 未找到TaskActionHandler异常。
 * <pre> 
 * 这个异常是在审批任务时根据ActionName查询TaskActionHandler未查询到时抛出的异常。
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-24-上午10:31:30
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class NoTaskActionHandlerException extends RuntimeException {

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -6237354795951526417L;

	public NoTaskActionHandlerException(String msg){
		
		super(msg);
	}
	
	public NoTaskActionHandlerException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}

}
