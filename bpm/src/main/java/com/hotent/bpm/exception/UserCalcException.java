package com.hotent.bpm.exception;

/**
 * 人员计算异常。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：miao
 * 邮箱:miaojf@jee-soft.cn
 * 日期:2014年10月23日
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class UserCalcException extends RuntimeException {

	/**
	 * serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -6237354795951526417L;

	public UserCalcException(String msg){
		
		super(msg);
	}
	
	public UserCalcException(String msg, Throwable throwable)
	{
		super(msg,throwable);
	}

}
