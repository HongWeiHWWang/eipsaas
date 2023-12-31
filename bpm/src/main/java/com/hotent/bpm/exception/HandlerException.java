package com.hotent.bpm.exception;

/**
 * 处理器执行出错。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-9-4-下午3:37:49
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class HandlerException  extends RuntimeException {
	
	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = -8550692135347253469L;
	

	public HandlerException(String msg){
		
		super(msg);
	}
	
	public HandlerException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}

}