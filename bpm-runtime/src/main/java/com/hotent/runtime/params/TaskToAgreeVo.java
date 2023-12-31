package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;

/**
 * 任务办理(同意、反对、弃权)页面参数
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月12日
 */
@ApiModel(value="任务办理(同意、反对、弃权)页面参数")
public class TaskToAgreeVo {
	
	@ApiModelProperty(name="taskId",notes="任务id",required=true)
	protected String taskId;
	
	@ApiModelProperty(name="actionName",required=true,
			notes="审批动作,agree（审批）abandon（弃权）oppose（反对）agreeTrans（同意流转）opposeTrans（反对流转）commu（沟通反馈）reject（驳回）backToStart（驳回指定节点）"
			,allowableValues="agree,abandon,oppose,agreeTrans,opposeTrans,commu,reject,backToStart")
	private String actionName;
	
	@ApiModelProperty(name="isGoNextJustEndEvent",notes="下一个节点是否为结束节点")
	private boolean isGoNextJustEndEvent;
	
	@ApiModelProperty(name="outcomeUserMap",notes="")
	protected Map outcomeUserMap;
	
	@ApiModelProperty(name="outcomeNodes",notes="")
	protected List<BpmNodeDef> outcomeNodes;
	
	@ApiModelProperty(name="allNodeDef",notes="")
	protected List<BpmNodeDef> allNodeDef;
	
	@ApiModelProperty(name="allNodeUserMap",notes="")
	protected Map allNodeUserMap;
	
	@ApiModelProperty(name="directHandlerSign",notes="会签时是否直接审批通过")
	private boolean directHandlerSign;
	
	@ApiModelProperty(name="jumpType",notes="跳转类型")
	protected String jumpType;
	
	@ApiModelProperty(name="approvalItem",notes="常用语")
	private List<String> approvalItem;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public boolean isGoNextJustEndEvent() {
		return isGoNextJustEndEvent;
	}

	public void setGoNextJustEndEvent(boolean isGoNextJustEndEvent) {
		this.isGoNextJustEndEvent = isGoNextJustEndEvent;
	}

	public Map getOutcomeUserMap() {
		return outcomeUserMap;
	}

	public void setOutcomeUserMap(Map outcomeUserMap) {
		this.outcomeUserMap = outcomeUserMap;
	}

	public List<BpmNodeDef> getOutcomeNodes() {
		return outcomeNodes;
	}

	public void setOutcomeNodes(List<BpmNodeDef> outcomeNodes) {
		this.outcomeNodes = outcomeNodes;
	}

	public List<BpmNodeDef> getAllNodeDef() {
		return allNodeDef;
	}

	public void setAllNodeDef(List<BpmNodeDef> allNodeDef) {
		this.allNodeDef = allNodeDef;
	}

	public Map getAllNodeUserMap() {
		return allNodeUserMap;
	}

	public void setAllNodeUserMap(Map allNodeUserMap) {
		this.allNodeUserMap = allNodeUserMap;
	}

	public boolean isDirectHandlerSign() {
		return directHandlerSign;
	}

	public void setDirectHandlerSign(boolean directHandlerSign) {
		this.directHandlerSign = directHandlerSign;
	}

	public String getJumpType() {
		return jumpType;
	}

	public void setJumpType(String jumpType) {
		this.jumpType = jumpType;
	}

	public List<String> getApprovalItem() {
		return approvalItem;
	}

	public void setApprovalItem(List<String> approvalItem) {
		this.approvalItem = approvalItem;
	}
}
