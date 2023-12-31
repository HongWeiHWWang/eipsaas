package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import com.hotent.bpm.api.model.process.def.BpmDefLayout;

/**
 * 查看任务流程图页面参数类
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(value="查看任务流程图页面参数类")
public class TaskjImageVo {
	
	@ApiModelProperty(name="taskId",notes="任务id")
	private String taskId;
	
	@ApiModelProperty(name="bpmDefLayout",notes="流程定义的坐标")
	private BpmDefLayout bpmDefLayout;
	
	@ApiModelProperty(name="instId",notes="流程实例id")
	private String instId;
	
	@ApiModelProperty(name="parentInstId",notes="父流程实例id")
	private String parentInstId;
	
	public TaskjImageVo(){}
	
	public TaskjImageVo(String taskId, BpmDefLayout bpmDefLayout, String instId, String parentInstId){
		this.taskId = taskId;
		this.bpmDefLayout = bpmDefLayout;
		this.instId = instId;
		this.parentInstId = parentInstId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public BpmDefLayout getBpmDefLayout() {
		return bpmDefLayout;
	}

	public void setBpmDefLayout(BpmDefLayout bpmDefLayout) {
		this.bpmDefLayout = bpmDefLayout;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getParentInstId() {
		return parentInstId;
	}

	public void setParentInstId(String parentInstId) {
		this.parentInstId = parentInstId;
	}
}
