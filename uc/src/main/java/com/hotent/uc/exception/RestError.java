package com.hotent.uc.exception;



/**
 * restful接口异常信息封装类
 * @author heyifan
 * @date 2017年6月30日
 */
public class RestError {
	private Boolean result = false;			/*返回*/
	private int status;						/*HttpStatus错误代码*/
	private int code;						/*自编错误代码*/
	private String message;					/*异常信息*/
	private String shortMessage;			/*简短的异常信息，可直接显示给终端用户*/
	private String moreInfoUrl = "";		/*错误详情地址*/

	public RestError(BaseException baseException){
		this.status = baseException.getStatus().value();
		this.code = baseException.getCode();
		this.shortMessage = baseException.getShortMessage();
		this.message = baseException.getMessage();
	}
	
	public RestError(int status, int code, String message, String shortMessage){
		this.status = status;
		this.code = code;
		this.message = message;
		this.shortMessage = shortMessage;
	}
	
	public RestError(int status, int code, String message, String shortMessage, String moreInfoUrl) {
		this.status = status;
		this.code = code;
		this.message = message;
		this.shortMessage = shortMessage;
		this.moreInfoUrl = moreInfoUrl;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getMoreInfoUrl() {
		return moreInfoUrl;
	}

	public void setMoreInfoUrl(String moreInfoUrl) {
		this.moreInfoUrl = moreInfoUrl;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}
}