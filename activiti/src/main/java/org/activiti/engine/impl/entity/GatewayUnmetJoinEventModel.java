package org.activiti.engine.impl.entity;

import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class GatewayUnmetJoinEventModel
{
	private  PvmActivity activity=null;
	private ActivityExecution activityExecution=null;
	private String noteType;
	//标识,标识是指哪一种类型的聚合:并行，条件，并行多实例，多实例
	private String flag=null;
	
	
	public GatewayUnmetJoinEventModel(){}
	
	
	public GatewayUnmetJoinEventModel(PvmActivity activity, ActivityExecution activityExecution, String noteType) {
		this.activity = activity;
		this.activityExecution = activityExecution;
		this.noteType = noteType;
		
	}
	
	public GatewayUnmetJoinEventModel(PvmActivity activity, ActivityExecution activityExecution, String noteType,
			String flag) {
		this(activity,activityExecution,noteType);
		this.flag = flag;
	}
	public PvmActivity getActivity() {
		return activity;
	}
	public void setActivity(PvmActivity activity) {
		this.activity = activity;
	}
	public ActivityExecution getActivityExecution() {
		return activityExecution;
	}
	public void setActivityExecution(ActivityExecution activityExecution) {
		this.activityExecution = activityExecution;
	}
	public String getNoteType() {
		return noteType;
	}
	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
 
	
}
