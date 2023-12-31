package com.hotent.uc.exception;

import java.util.HashMap;
import java.util.Map;


public enum HotentHttpStatus {
	
	REUIRED(1000,"必填");
	
	/**
	 * 错误代码
	 */
	private Integer code;
	
	/**
	 * 错误描述
	 */
	private String description;
	
	private HotentHttpStatus(Integer code , String description){
		this.code = code;
		this.description = description;
	}
	
	public Integer code(){
		return code;
	}
	public String description(){
		return description;
	}
	
	public static Map<Integer, String> getHotentHttpStatusTypes()
	{
		Map<Integer, String> map=new HashMap<Integer, String>();
		for (HotentHttpStatus e : HotentHttpStatus.values())
		{
			map.put(e.code(), e.description());
		}
		return map;
	}
	
	

}
