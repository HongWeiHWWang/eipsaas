package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * 流程自定义异常
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月9日
 */
public class WorkFlowException extends BaseException {
	private static final long serialVersionUID = 1L;

	public WorkFlowException(){
		super(ResponseErrorEnums.WORKFLOW_ERROR);
	}
	
	public WorkFlowException(String detailMessage) {
		super(ResponseErrorEnums.WORKFLOW_ERROR, detailMessage);
	}
}
