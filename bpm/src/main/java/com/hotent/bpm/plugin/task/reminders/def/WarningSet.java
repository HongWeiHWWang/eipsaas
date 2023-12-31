package com.hotent.bpm.plugin.task.reminders.def;

import java.io.Serializable;

public class WarningSet implements Serializable{
	private static final long serialVersionUID = 1L;
	//预警名称
    protected String warnName;
    //预警期限
    protected Integer warnTime;
    //预警级别
    protected Integer level;

    
	public String getWarnName() {
		return warnName;
	}
	public void setWarnName(String warnName) {
		this.warnName = warnName;
	}
	public Integer getWarnTime() {
		return warnTime;
	}
	public void setWarnTime(Integer warnTime) {
		this.warnTime = warnTime;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
}
