package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * Feign未找到对应微服务异常
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月9日
 */
public class EmptyFeignException extends BaseException{
	private static final long serialVersionUID = 1L;

	public EmptyFeignException() {
		super(ResponseErrorEnums.FEIGN_EMPTY_ERROR);
	}
	
	public EmptyFeignException(String detailMessage) {
		super(ResponseErrorEnums.FEIGN_EMPTY_ERROR, detailMessage);
	}
}
