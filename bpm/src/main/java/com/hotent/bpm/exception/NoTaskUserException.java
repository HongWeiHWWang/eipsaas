package com.hotent.bpm.exception;

/**
 * 任务没有分配用户。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-9-下午9:26:09
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class NoTaskUserException extends RuntimeException {

	
	
	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = 8370606920599997592L;

	public NoTaskUserException(String msg){
		
		super(msg);
	}
	
	public NoTaskUserException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}

}
