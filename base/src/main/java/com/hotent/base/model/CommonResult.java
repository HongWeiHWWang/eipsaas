package com.hotent.base.model;

import com.hotent.base.enums.ResponseErrorEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("通用返回结果")
public class CommonResult<E> {
	@ApiModelProperty(name="state",notes="状态 true：操作成功  false：操作失败")
	Boolean state = true;

	@ApiModelProperty(name="message",notes="提示信息")
	String message;
	
	@ApiModelProperty(name="value",notes="返回的数据")
	E value;
	
	@ApiModelProperty(value = "错误代码")
	private String errorCode;
	
	/**
	 * 返回成功及成功的提示信息
	 * @param message
	 */
	public CommonResult(String message) {
		this(true, message, null);
	}
	
	/**
	 * 返回成功/失败，及对应的成功/失败提示信息
	 * @param state
	 * @param message
	 */
	public CommonResult(boolean state, String message) {
		this(state, message, null);
	}
	
	/**
	 * 返回成功/失败，及对应的成功/失败提示信息，还有返回对应的数据
	 * @param state
	 * @param message
	 * @param value
	 */
	public CommonResult(boolean state,String message,E value){
		this.state = state;
		this.message = message;
		this.value = value;
	}
	
	/**
	 * 返回错误，及错误编码，对应的错误信息
	 * @param error
	 */
	public CommonResult(ResponseErrorEnums error) {
		this.state = false;
		this.errorCode = error.getCode();
		this.message = error.getMessage();
	}
	
	/**
	 * 返回错误，及错误编码，对应的错误信息，还有返回对应的数据
	 * @param error
	 * @param value
	 */
	public CommonResult(ResponseErrorEnums error, E value) {
		this.state = false;
		this.errorCode = error.getCode();
		this.message = error.getMessage();
		this.value = value;
	}
	
	/**
	 * 返回错误，错误编码，对应的错误信息
	 * @param errorCode
	 * @param message
	 */
	public CommonResult(String errorCode, String message) {
		this.state = false;
		this.errorCode = errorCode;
		this.message = message;
	}
	
	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public CommonResult() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public E getValue() {
		return value;
	}

	public void setValue(E value) {
		this.value = value;
	}
}
