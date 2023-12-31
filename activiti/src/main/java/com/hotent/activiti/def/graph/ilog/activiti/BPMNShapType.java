package com.hotent.activiti.def.graph.ilog.activiti;

public enum BPMNShapType {
	StartEvent,
	Task,
	ScriptTask,
	ServiceTask,
	BusinessRuleTask,
	ManualTask,
	UserTask,
	SendTask,
	ReceiveTask,
	SubProcess,
	CallActivity,
	IntermediateCatchEvent,
	EventBasedGateway,
	HPool,
	VPool,
	HLane,
	VLane,
	EndEvent,
	CancelEvent,
	ErrorEvent,
	ParallelGateway,
	ExclusiveGateway,
	InclusiveGateway,
	AdHocSubProcess,
	ComplexGateway,
	Transaction,
	UnknowType
}