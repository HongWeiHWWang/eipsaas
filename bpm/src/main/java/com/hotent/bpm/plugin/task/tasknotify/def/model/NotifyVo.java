package com.hotent.bpm.plugin.task.tasknotify.def.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.hotent.bpm.api.constant.EventType;

public class NotifyVo implements Serializable{
	private static final long serialVersionUID = 1L;

	private EventType eventType;

	private List<NotifyItem> notifyItemList = new ArrayList<NotifyItem>(); 


	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public List<NotifyItem> getNotifyItemList() {
		return notifyItemList;
	}
	
	public void setNotifyItemList(List<NotifyItem> notifyItemList) {
		this.notifyItemList = notifyItemList;
	}
	public String toString(){
    	return new ToStringBuilder(this).append("eventType")
    			.toString();
    }
    
}
