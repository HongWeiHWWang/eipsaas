package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 管理员处理任务页面参数类
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(value="处理任务页面参数类")
public class TaskDoNextVo {
	
	@ApiModelProperty(name="taskId",notes="任务id")
	private String taskId;
	
	@ApiModelProperty(name="nodeId",notes="节点ID")
	private String nodeId;
	
	@ApiModelProperty(name="isPopWin",notes="是否弹窗（点击同意处理任务时）")
	private boolean isPopWin;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public boolean isPopWin() {
		return isPopWin;
	}

	public void setPopWin(boolean isPopWin) {
		this.isPopWin = isPopWin;
	}
	
}
