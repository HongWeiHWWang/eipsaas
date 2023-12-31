package com.hotent.bpm.persistence.constants;

public enum NodeDefType {
	UserTask,//人工任务
	ServiceTask,//服务任务
	ScriptTask,//脚本任务
	SendTask,//发送消息任务
	ManualTask,//手动任务
	ReceiveTask,//接收消息任务
	SignTask,//会签任务
	StartEvent,//启动事件
	EndEvent,//结束事件
	SubProcess,//子流程	
	CallActivity,//外部子流程
	
	Unknown//未知节点
}
