package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;

/**
 * 驳回任务页面参数
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月12日
 */
@ApiModel(value="驳回任务页面参数")
public class TaskToRejectVo {
	
	@ApiModelProperty(name="taskId",notes="任务id",required=true)
	protected String taskId;
	
	@ApiModelProperty(name="backNode",notes="")
	protected String backNode;
	
	@ApiModelProperty(name="backMode",notes="驳回模式：reject、backToStart")
	protected String backMode;
	
	@ApiModelProperty(name="canRejectToStart",notes="允许驳回到发起人")
	private boolean canRejectToStart;
	
	@ApiModelProperty(name="canRejectToAnyNode",notes="允许驳回指定节点")
	private boolean canRejectToAnyNode;
	
	@ApiModelProperty(name="canRejectPreAct",notes="允许驳回到上一步")
	private boolean canRejectPreAct;
	
	@ApiModelProperty(name="canReject",notes="允许驳回")
	private boolean canReject;
	
	@ApiModelProperty(name="allowDirectNode",notes="允许直来直往的节点")
	protected List<BpmNodeDef> allowDirectNode;
	
	@ApiModelProperty(name="allowNormalNode",notes="允许按流程图执行的节点")
	protected List<BpmNodeDef> allowNormalNode;

	@ApiModelProperty(name="isInGateway",notes="是否同步网关内的任务")
	protected boolean isInGateway = false;
	
	@ApiModelProperty(name="isAfterGateway",notes="是否在同步网关后面")
	protected boolean isAfterGateway = false;

	@ApiModelProperty(name="isInSubProcess",notes="是在内部子流程")
	protected boolean isInSubProcess = false;
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getBackNode() {
		return backNode;
	}

	public void setBackNode(String backNode) {
		this.backNode = backNode;
	}

	public String getBackMode() {
		return backMode;
	}

	public void setBackMode(String backMode) {
		this.backMode = backMode;
	}

	public boolean isCanRejectToStart() {
		return canRejectToStart;
	}

	public void setCanRejectToStart(boolean canRejectToStart) {
		this.canRejectToStart = canRejectToStart;
	}

	public boolean isCanRejectToAnyNode() {
		return canRejectToAnyNode;
	}

	public void setCanRejectToAnyNode(boolean canRejectToAnyNode) {
		this.canRejectToAnyNode = canRejectToAnyNode;
	}

	public boolean isCanRejectPreAct() {
		return canRejectPreAct;
	}

	public void setCanRejectPreAct(boolean canRejectPreAct) {
		this.canRejectPreAct = canRejectPreAct;
	}

	public boolean isCanReject() {
		return canReject;
	}

	public void setCanReject(boolean canReject) {
		this.canReject = canReject;
	}


	public List<BpmNodeDef> getAllowDirectNode() {
		return allowDirectNode;
	}

	public void setAllowDirectNode(List<BpmNodeDef> allowDirectNode) {
		this.allowDirectNode = allowDirectNode;
	}

	public List<BpmNodeDef> getAllowNormalNode() {
		return allowNormalNode;
	}

	public void setAllowNormalNode(List<BpmNodeDef> allowNormalNode) {
		this.allowNormalNode = allowNormalNode;
	}

	public boolean isInGateway() {
		return isInGateway;
	}

	public void setInGateway(boolean isInGateway) {
		this.isInGateway = isInGateway;
	}

	public boolean isAfterGateway() {
		return isAfterGateway;
	}

	public void setAfterGateway(boolean isAfterGateway) {
		this.isAfterGateway = isAfterGateway;
	}

	public boolean isInSubProcess() {
		return isInSubProcess;
	}

	public void setInSubProcess(boolean isInSubProcess) {
		this.isInSubProcess = isInSubProcess;
	}
	
}
