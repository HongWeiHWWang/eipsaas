package com.hotent.bpm.persistence.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.string.StringPool;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.api.model.process.task.SkipResult;
import com.hotent.i18n.util.I18nUtil;


/**
 * <pre>
 * 对象功能:流程任务 entity对象 
 * 开发公司:广州宏天软件有限公司 
 * 开发人员:zyp 
 * 创建时间:2014-02-12 18:04:14
 * </pre>
 */
@TableName("bpm_task")
public class DefaultBpmTask extends BaseModel<DefaultBpmTask> implements BpmTask,
		Cloneable {
	/***/
	private static final long serialVersionUID = 2240144872175803135L;

	@TableId("id_")
	protected String id; /* 任务ID */
	
	@TableField("name_")
	protected String name; /* 任务名称 */
	
	@TableField("subject_")
	protected String subject; /* 待办事项标题 */
	
	@TableField("task_id_")
	protected String taskId = ""; /* 关联的任务ID */
	
	@TableField("exec_id_")
	protected String execId; /* 关联 - 节点执行ID */
	
	@TableField("node_id_")
	protected String nodeId; /* 关联 - 任务节点ID */
	
	@TableField("proc_inst_id_")
	protected String procInstId; /* 关联 - 流程实例ID */
	
	@TableField("proc_def_id_")
	protected String procDefId; /* 关联 - 流程定义ID */
	
	@TableField(exist=false)
	protected String instIsForbidden; /* 关联 - 流程实例是否禁止 */
	
	@TableField("proc_def_key_")
	protected String procDefKey; /* 关联 - 流程定义KEY */
	
	@TableField("proc_def_name_")
	protected String procDefName; /* 关联 - 流程名称 */
	
	@TableField("owner_id_")
	protected String ownerId = "0"; /* 任务所属人ID */
	
	@TableField("assignee_id_")
	protected String assigneeId = "0"; /* 任务执行人ID */
	
	// 任务执行人人
	@TableField("assignee_name_")
	protected  String assigneeName = "";
	
	// 任务所属人
	@TableField("owner_name_")
	protected  String ownerName = "";
		
	@TableField("status_")
	protected String status; /* 任务状态 */
	
	@TableField("priority_")
	protected Long priority; /* 任务优先级 */
	
	@TableField("due_time_")
	protected LocalDateTime dueTime; /* 任务到期时间 */
	
	@TableField("suspend_state_")
	protected Short suspendState; /* 是否挂起(0正常,1挂起) */
	
	@TableField("parent_id_")
	protected String parentId = "0";
	
	// 流程实例ID
	@TableField("bpmn_inst_id_")
	protected String bpmnInstId = "";
	
	@TableField("bpmn_def_id_")
	protected String bpmnDefId = "";
	
	//类型ID
	@TableField("type_id_")
	protected String typeId = "";
	
	@TableField("is_read_")
	protected  Integer isRead = null;
	
	@TableField("is_revoke_")
    protected  Integer isRevoke = null;
	
	@TableField("prop1_")
	protected String prop1;
	
	@TableField("prop2_")
	protected String prop2;
	
	@TableField("prop3_")
	protected String prop3;
	
	@TableField("prop4_")
	protected String prop4;
	
	@TableField("prop5_")
	protected String prop5;
	
	@TableField("prop6_")
	protected String prop6;
	
	//支持手机表单
	@TableField("support_mobile_")
	protected int supportMobile=0;
	
	@TableField("create_time_")
	protected LocalDateTime createTime = LocalDateTime.now();
	
	//流转日期
	@TableField(exist=false)
	protected LocalDate transDate;
	
	// 流程分管授权权限对象
	@TableField(exist=false)
	protected AuthorizeRight authorizeRight; 

	//任务执行人是否为空，这个不保存到数据库。
	@TableField(exist=false)
	protected transient boolean isIdentityEmpty = false;
	
	// 任务候选人
	@TableField(exist=false)
	protected transient List<BpmIdentity> identityList = null;

	@TableField(exist=false)
	protected transient SkipResult skipResult = new SkipResult();
	
	/* 流程创建时间 */
	@TableField(exist=false)
	protected  transient LocalDateTime createDate; 
	
	/* 流程创建人ID */
	@TableField(exist=false)
	protected  String creatorId; 
	
	/* 流程创建人名称 */
	@TableField(exist=false)
	protected  String creator; 
	
	/* 流程状态 */
	@TableField(exist=false)
	protected  String instStatus;
	
	@TableField(exist=false)
    protected String leaderIds = ""; /* 共享的领导id集合 */

	
	@TableField(exist=false)
	protected boolean isGateway_=false;
	
	/* 计算时间的方式  worktime 工作日  caltime日历日 */
	@TableField(exist=false)
	protected String dueDateType;
	
	/* 任务到期时间 */
	@TableField(exist=false)
	protected LocalDateTime dueExpDate;
	
	/* 任务用时 */
	@TableField(exist=false)
	protected int dueTaskTime = 0;
	
	/* 任务已用时间 */
	@TableField(exist=false)
	protected int dueUseTaskTime = 0;
	
	/* dueStatus 是否进行过延迟申请  */
	@TableField(exist=false)
	protected int dueStatus;
	
	@TableField(exist=false)
	protected String urgentStateValue;
    
	// 是否bpm_task表的任务
	@TableField(exist=false)
	protected String isBpmTask;
	
	public void setId(String id) {
		this.id = id;
	}
    
	
	/**
	 * 返回 任务ID
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回 任务名称
	 * 
	 * @return
	 */	
	public String getName() {
		return I18nUtil.replaceTemp(this.name, StringPool.FLOW_REG, LocaleContextHolder.getLocale());
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 返回 待办事项标题
	 * 
	 * @return
	 */
	public String getSubject() {
		return this.subject;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 返回 关联的任务ID
	 * 
	 * @return
	 */
	public String getTaskId() {
		return this.taskId;
	}

	public void setExecId(String execId) {
		this.execId = execId;
	}

	/**
	 * 返回 关联 - 节点执行ID
	 * 
	 * @return
	 */
	public String getExecId() {
		return this.execId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 返回 关联 - 任务节点ID
	 * 
	 * @return
	 */
	public String getNodeId() {
		return this.nodeId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	/**
	 * 返回 关联 - 流程实例ID
	 * 
	 * @return
	 */
	public String getProcInstId() {
		return this.procInstId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	/**
	 * 返回 关联 - 流程定义ID
	 * 
	 * @return
	 */
	public String getProcDefId() {
		return this.procDefId;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

	/**
	 * 返回  流程定义的Key
	 * 
	 * @return
	 */
	public String getProcDefKey() {
		return this.procDefKey;
	}

	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}

	/**
	 * 返回 关联 - 流程名称
	 * 
	 * @return
	 */
	public String getProcDefName() {
		return this.procDefName;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * 返回 任务所属人ID
	 * 
	 * @return
	 */
	public String getOwnerId() {
		return this.ownerId;
	}

	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}

	/**
	 * 返回 任务执行人ID
	 * 
	 * @return
	 */
	public String getAssigneeId() {
		return this.assigneeId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 返回 任务状态
	 * 
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	/**
	 * 返回 任务优先级
	 * 
	 * @return
	 */
	public Long getPriority() {
		return this.priority;
	}

	public void setDueTime(LocalDateTime dueTime) {
		this.dueTime = dueTime;
	}

	/**
	 * 返回 任务到期时间
	 * 
	 * @return
	 */
	public LocalDateTime getDueTime() {
		return this.dueTime;
	}

	public void setSuspendState(Short suspendState) {
		this.suspendState = suspendState;
	}

	/**
	 * 返回 是否挂起(0正常,1挂起)
	 * 
	 * @return
	 */
	@Override
	public Short getSuspendState() {
		return this.suspendState;
	}

	/**
	 * 返回父ID
	 */
	@Override
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getBpmnInstId() {
		return bpmnInstId;
	}

	public void setBpmnInstId(String bpmnInstId) {
		this.bpmnInstId = bpmnInstId;
	}

	public String getBpmnDefId() {
		return bpmnDefId;
	}

	public void setBpmnDefId(String bpmnDefId) {
		this.bpmnDefId = bpmnDefId;
	}

	/**
	 * 判断任务是否为流程引擎产生的任务。
	 * 
	 * @return boolean
	 */
	public boolean isBpmnTask() {
		String execId = this.execId;
		return StringUtil.isNotEmpty(execId);
	}

	
	public String getDueDateType() {
		return dueDateType;
	}

	public void setDueDateType(String dueDateType) {
		this.dueDateType = dueDateType;
	}

	public LocalDateTime getDueExpDate() {
		return dueExpDate;
	}

	public void setDueExpDate(LocalDateTime dueExpDate) {
		this.dueExpDate = dueExpDate;
	}

	public int getDueTaskTime() {
		return dueTaskTime;
	}

	public void setDueTaskTime(int dueTaskTime) {
		this.dueTaskTime = dueTaskTime;
	}

	public int getDueUseTaskTime() {
		return dueUseTaskTime;
	}

	public void setDueUseTaskTime(int dueUseTaskTime) {
		this.dueUseTaskTime = dueUseTaskTime;
	}

	public int getDueStatus() {
		return dueStatus;
	}

	public void setDueStatus(int dueStatus) {
		this.dueStatus = dueStatus;
	}

	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("name", this.name).append("subject", this.subject)
				.append("taskId", this.taskId).append("execId", this.execId)
				.append("nodeId", this.nodeId)
				.append("procInstId", this.procInstId)
				.append("procDefId", this.procDefId)
				.append("procDefKey", this.procDefKey)
				.append("procDefName", this.procDefName)
				.append("ownerId", this.ownerId)
				.append("assigneeId", this.assigneeId)
				.append("status", this.status)
				.append("priority", this.priority)
				.append("createTime", this.createTime)
				.append("createDate", this.createDate)
				.append("creator", this.creator)
				.append("createTime", this.createTime)
				.append("dueTime", this.dueTime)
				.append("suspendState", this.suspendState).toString();
	}

	@Override
	public boolean equals(Object obj) {
		DefaultBpmTask task = (DefaultBpmTask) obj;
		if (this.id.equals(task.getId())) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	@Override
	public Object clone() {
		DefaultBpmTask o = null;
		try {
			o = (DefaultBpmTask) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	@Override
	public List<BpmIdentity> getIdentityList() {
		return this.identityList;
	}

	public void setIdentityList(List<BpmIdentity> identityList) {
		this.identityList = identityList;
	}

	@Override
	public void setSkipResult(SkipResult skipResult) {
		this.skipResult = skipResult;
	}

	@Override
	public SkipResult getSkipResult() {
		return this.skipResult;
	}

	public AuthorizeRight getAuthorizeRight() {
		return authorizeRight;
	}

	public void setAuthorizeRight(AuthorizeRight authorizeRight) {
		this.authorizeRight = authorizeRight;
	}

	public boolean isIdentityEmpty() {
		return isIdentityEmpty;
	}

	public void setIdentityEmpty(boolean isIdentityEmpty) {
		this.isIdentityEmpty = isIdentityEmpty;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getInstStatus() {
		return instStatus;
	}

	public void setInstStatus(String instStatus) {
		this.instStatus = instStatus;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public LocalDate getTransDate() {
		return transDate;
	}

	public void setTransDate(LocalDate transDate) {
		this.transDate = transDate;
	}

	public int getSupportMobile() {
		return supportMobile;
	}

	public void setSupportMobile(int supportMobile) {
		this.supportMobile = supportMobile;
	}

	@Override
	public void setIsGateWay(boolean isGateway) {
		this.isGateway_=isGateway;
	}

	@Override
	public boolean isGateWay() {
		return isGateway_;
	}

	public String getInstIsForbidden() {
		return instIsForbidden;
	}

	public void setInstIsForbidden(String instIsForbidden) {
		this.instIsForbidden = instIsForbidden;
	}

	public String getUrgentStateValue() {
		return urgentStateValue;
	}



	public void setUrgentStateValue(String urgentStateValue) {
		this.urgentStateValue = urgentStateValue;
	}



	public String getLeaderIds() {
		return leaderIds;
	}



	public void setLeaderIds(String leaderIds) {
		this.leaderIds = leaderIds;
	}



	public String getProp1() {
		return prop1;
	}



	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}



	public String getProp2() {
		return prop2;
	}



	public void setProp2(String prop2) {
		this.prop2 = prop2;
	}



	public String getProp3() {
		return prop3;
	}



	public void setProp3(String prop3) {
		this.prop3 = prop3;
	}



	public String getProp4() {
		return prop4;
	}



	public void setProp4(String prop4) {
		this.prop4 = prop4;
	}



	public String getProp5() {
		return prop5;
	}



	public void setProp5(String prop5) {
		this.prop5 = prop5;
	}



	public String getProp6() {
		return prop6;
	}



	public void setProp6(String prop6) {
		this.prop6 = prop6;
	}



	public String getIsBpmTask() {
		return isBpmTask;
	}



	public void setIsBpmTask(String isBpmTask) {
		this.isBpmTask = isBpmTask;
	}



	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

    
}