package com.hotent.runtime.params;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.persistence.model.BpmIdentityResult;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.bpm.persistence.util.ServiceUtil;
import com.hotent.uc.api.model.IUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程任务
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程任务")
public class BpmTaskResult {
	@ApiModelProperty(name="taskId",notes="任务ID",example="10000000000001",required=true)
	protected String taskId;
	@ApiModelProperty(name="taskName",notes="任务名称",example="经理审批",required=true)
	protected String taskName; 
	@ApiModelProperty(name="subject",notes="流程实例标题",example="经理审批",required=true)
	protected String subject;
	@ApiModelProperty(name="nodeId",notes="任务所属节点ID",example="managerApproval",required=true)
	protected String nodeId;
	@ApiModelProperty(name="flowKey",notes="流程定义KEY",example="qjlc",required=true)
	protected String flowKey;
	@ApiModelProperty(name="flowName",notes="流程名称",example="请假流程",required=true)
	protected String flowName;
	@ApiModelProperty(name="status",notes="任务状态",
					  allowableValues="NORMAL(普通任务),AGENT(代理任务),DELIVERTO(转办任务),TRANSFORMING(流转源任务),TRANSFORMED(接收流转任务),COMMU(通知任务),BACK(被驳回任务),ADDSIGN(加签)",
					  example="NORMAL",required=true)
	protected String status;
	@ApiModelProperty(name="dueTime",notes="任务到期时间",example="2017-10-17 12:18:00",required=true)
	protected LocalDateTime dueTime;
	@ApiModelProperty(name="suspendState",notes="是否挂起",
			  allowableValues="0(正常),1(挂起)",
			  example="0",required=true)
	protected Short suspendState;
	@ApiModelProperty(name="typeId",notes="任务类型ID",required=true)
	protected String typeId = "";
	@ApiModelProperty(name="creator",notes="任务创建人",required=true)
	protected BpmIdentityResult creator = null;
	@ApiModelProperty(name="assignee",notes="任务执行人",required=true)
	protected BpmIdentityResult assignee = null;
	@ApiModelProperty(name="candidate",notes="任务候选人",required=true)
	protected List<BpmIdentityResult> candidate = null;
	@ApiModelProperty(name="createDate",notes="任务创建时间",required=true)
	protected LocalDateTime createDate;
	
	public BpmTaskResult(DefaultBpmTask defaultBpmTask) throws Exception{
		if(BeanUtils.isEmpty(defaultBpmTask)){
			throw new RuntimeException("流程任务为空");
		}
		this.taskId = defaultBpmTask.getId();
		this.taskName = defaultBpmTask.getName();
		this.subject = defaultBpmTask.getSubject();
		this.nodeId = defaultBpmTask.getNodeId();
		this.flowKey = defaultBpmTask.getProcDefKey();
		this.flowName = defaultBpmTask.getProcDefName();
		this.status = defaultBpmTask.getStatus();
		this.dueTime = defaultBpmTask.getDueTime();
		this.suspendState = defaultBpmTask.getSuspendState();
		this.typeId = defaultBpmTask.getTypeId();
		this.createDate = defaultBpmTask.getCreateDate();
		String creatorId = defaultBpmTask.getCreatorId();
		if(StringUtil.isNotEmpty(creatorId)){
			IUser creatorUser = ServiceUtil.getUserById(creatorId);
			this.creator = new BpmIdentityResult(creatorUser);
		}
		String assigneeId = defaultBpmTask.getAssigneeId();
		if(StringUtil.isNotZeroEmpty(assigneeId)){
			IUser assigneeUser = ServiceUtil.getUserById(assigneeId);
			this.assignee = new BpmIdentityResult(assigneeUser);
		}
		List<BpmIdentity> identityList = defaultBpmTask.getIdentityList();
		if(BeanUtils.isNotEmpty(identityList)){
			List<IUser> extractUser = ServiceUtil.extractUser(identityList);
			if(BeanUtils.isNotEmpty(extractUser)){
				this.candidate = new ArrayList<BpmIdentityResult>();
				for (IUser iUser : extractUser) {
					this.candidate.add(new BpmIdentityResult(iUser));
				}
			}
		}
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getFlowKey() {
		return flowKey;
	}
	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getDueTime() {
		return dueTime;
	}
	public void setDueTime(LocalDateTime dueTime) {
		this.dueTime = dueTime;
	}
	public Short getSuspendState() {
		return suspendState;
	}
	public void setSuspendState(Short suspendState) {
		this.suspendState = suspendState;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public BpmIdentityResult getCreator() {
		return creator;
	}
	public void setCreator(BpmIdentityResult creator) {
		this.creator = creator;
	}
	public BpmIdentityResult getAssignee() {
		return assignee;
	}
	public void setAssignee(BpmIdentityResult assignee) {
		this.assignee = assignee;
	}
	public List<BpmIdentityResult> getCandidate() {
		return candidate;
	}
	public void setCandidate(List<BpmIdentityResult> candidate) {
		this.candidate = candidate;
	}
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
}
