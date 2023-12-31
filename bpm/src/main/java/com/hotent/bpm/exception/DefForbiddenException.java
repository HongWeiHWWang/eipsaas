package com.hotent.bpm.exception;

/**
 * 流程定义或流程实例被禁止时抛出异常。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-9-4-下午2:55:41
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefForbiddenException  extends RuntimeException {
	
	
	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -3911980847054828710L;

	public DefForbiddenException(String msg){
		
		super(msg);
	}
	
	public DefForbiddenException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}

}
