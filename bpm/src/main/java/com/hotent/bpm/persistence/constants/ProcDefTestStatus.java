package com.hotent.bpm.persistence.constants;

public enum ProcDefTestStatus {
	TEST("test","测试状态"),RUN("run","正式");
	private String key;
	private String name;
	private ProcDefTestStatus(String _key,String _name){
		key = _key;
		name = _name;
	}
	public String getKey(){
		return key;
	}
	public String getName(){
		return name;
	}
}
