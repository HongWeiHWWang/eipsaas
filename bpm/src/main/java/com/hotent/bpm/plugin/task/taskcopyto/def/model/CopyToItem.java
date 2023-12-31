package com.hotent.bpm.plugin.task.taskcopyto.def.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringCollections;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;

public class CopyToItem implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
     * 用户分配规则（用于查询用户）
     */
    private List<UserAssignRule> userAssignRules;
    /**
     * 消息类型（mail=邮件；inner=内部消息；sms=短信）
     */
	private List<String> msgTypes = new ArrayList<String>();
    
	public List<UserAssignRule> getUserAssignRules() {
		return userAssignRules;
	}
	public void setUserAssignRules(List<UserAssignRule> userAssignRules) {
		this.userAssignRules = userAssignRules;
	}
	public List<String> getMsgTypes() {
		return msgTypes;
	}
	public void setMsgTypes(String msgTypes){
		if(StringUtil.isNotEmpty(msgTypes)){
			this.msgTypes = StringCollections.toList(msgTypes, ",");
		}
	}
}
