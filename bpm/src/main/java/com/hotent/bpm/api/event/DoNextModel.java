package com.hotent.bpm.api.event;

import com.hotent.bpm.api.cmd.TaskFinishCmd;
import com.hotent.bpm.api.constant.AopType;

public class DoNextModel {

	/**
	 * 命令CMD下一步CMD对象。
	 */
	private TaskFinishCmd taskFinishCmd;
	
	/**
	 * 前置和后置处理类型。
	 */
	private AopType aopType=AopType.PREVIOUS;
	
	
	
	

	public DoNextModel(TaskFinishCmd taskFinishCmd, AopType aopType) {
		this.taskFinishCmd = taskFinishCmd;
		this.aopType = aopType;
	}

	public TaskFinishCmd getTaskFinishCmd() {
		return taskFinishCmd;
	}

	public void setTaskFinishCmd(TaskFinishCmd taskFinishCmd) {
		this.taskFinishCmd = taskFinishCmd;
	}

	public AopType getAopType() {
		return aopType;
	}

	public void setAopType(AopType aopType) {
		this.aopType = aopType;
	}
	
	
	
}
