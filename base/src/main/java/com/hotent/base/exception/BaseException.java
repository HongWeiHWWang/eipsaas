package com.hotent.base.exception;

import com.hotent.base.enums.ResponseErrorEnums;

/**
 * 通用异常
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月4日
 */
public class BaseException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	/**
	 * 错误代码
	 */
	protected String code;
	/**
	 * 返回的异常信息
	 * <pre>
	 * 通过restful接口返回到前端的信息
	 * </pre>
	 */
	protected String message;
	/**
	 * 打印的异常信息
	 * <pre>
	 * 通过控制台或者输出到统一日志平台的信息
	 * </pre>
	 */
	protected String detailMessage;
	
	public BaseException() {
		super();
	}
	
	public BaseException(String message) {
		super(message);
		this.message = message;
	}
	
	public BaseException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}
	
	public BaseException(ResponseErrorEnums enums) {
		super();
		this.code = enums.getCode();
		this.message = enums.getMessage();
	}

	public BaseException(ResponseErrorEnums enums, String detailMessage) {
		super();
		this.code = enums.getCode();
		this.message = enums.getMessage();
		this.detailMessage = detailMessage;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetailMessage() {
		return detailMessage;
	}

	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}
}
