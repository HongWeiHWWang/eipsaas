package com.hotent.bpm.persistence.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;

/**
 * 操作时的返回结果。
 * 
 * <pre>
 * 在一般操作时我们一般都需要返回一个结果。
 * 构建组：x5-base-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2013-12-24-下午3:33:13
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ResultMessage implements Serializable {

	/** */
	private static final long serialVersionUID = -7102370526170507252L;

	/** 成功 */
	public static final int SUCCESS = 1;

	/** 失败 */
	public static final int FAIL = 0;

	/** 错误 */
	public static final int ERROR = -1;
	
	/** 登陆超时 */
	public static final int TIMEOUT = 2;

	// 返回结果(成功或失败)
	private int result = SUCCESS;
	// 返回消息
	private String message = "";
	// 引起原因
	private String cause = "";
	
	private Map<String,Object> vars=new HashMap<String, Object>();
	

	public ResultMessage() {
	}

	public ResultMessage(int result, String message) {
		this.result = result;
		this.message = message;
	}

	public ResultMessage(int result, String message, String cause) {
		this.result = result;
		this.message = message;
		this.cause = cause;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	/**
	 * 获取成功消息。
	 * 
	 * @param message
	 * @return ResultMessage
	 */
	public static ResultMessage getSuccess(String message) {
		return new ResultMessage(SUCCESS, message);
	}

	/**
	 * 获取成功的消息。
	 * 
	 * @return ResultMessage
	 */
	public static ResultMessage getSuccess() {
		return new ResultMessage(SUCCESS, "");
	}

	/**
	 * 
	 * 获取失败的消息。
	 * 
	 * @param message
	 *            消息。
	 * @return ResultMessage
	 */
	public static ResultMessage getFail(String message) {
		return new ResultMessage(FAIL, message);
	}
	
	public void addVariable(String key,Object value){
		this.vars.put(key, value);
	}

	public Map<String, Object> getVars() {
		return vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}
	
	public String toString() {
		ObjectNode stringer = JsonUtil.getMapper().createObjectNode();
		stringer.put("result", result);
		stringer.put("message", message);
		stringer.put("cause", cause);
		return stringer.asText();
	}


}
