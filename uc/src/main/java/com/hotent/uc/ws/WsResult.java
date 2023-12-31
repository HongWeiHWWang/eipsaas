package com.hotent.uc.ws;

import java.io.Serializable;

/**
 * Webservice通用返回结果
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2019年4月29日
 */
public class WsResult implements Serializable{
	private static final long serialVersionUID = 7664683108328979719L;
	
	private Boolean result = true;	/*执行结果*/
	private String message;			/*错误原因*/
	
	public static WsResult build() {
		return new WsResult();
	}
	
	public static WsResult build(Boolean result, String message) {
		return new WsResult(result, message);
	}
	
	private WsResult() {
	}
	
	private WsResult(Boolean result, String message) {
		this.result = result;
		this.message = message;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		if(message==null) {
			return String.format("{\"result\":\"%s\"}", this.result);
		}
		else {
			return String.format("{\"result\":\"%s\", \"message\":\"%s\"}", this.result, this.message);
		}
	}
}
