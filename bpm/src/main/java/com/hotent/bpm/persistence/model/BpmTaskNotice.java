package com.hotent.bpm.persistence.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 知会任务表
 * <pre> 
 * 描述：知会任务表 实体对象
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-04-09 15:35:22
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@TableName("bpm_task_notice")
 @ApiModel(value = "BpmTaskNotice",description = "知会任务表") 
public class BpmTaskNotice extends BaseModel<BpmTaskNotice>{

	private static final long serialVersionUID = 1L;
	
	@TableId("id_")
	@ApiModelProperty(value="任务ID")
	protected String id; 
	
	@TableField("name_")
	@ApiModelProperty(value="任务名称")
	protected String name; 
	
	@TableField("subject_")
	@ApiModelProperty(value="待办事项标题")
	protected String subject; 
	
	@TableField("proc_inst_id_")
	@ApiModelProperty(value="关联 - 流程实例ID")
	protected String procInstId; 
	
	@TableField("task_id_")
	@ApiModelProperty(value="关联的任务ID")
	protected String taskId; 
	
	@TableField("exec_id_")
	@ApiModelProperty(value="关联 - 节点执行ID")
	protected String execId; 
	
	@TableField("node_id_")
	@ApiModelProperty(value="关联 - 任务节点ID")
	protected String nodeId; 
	
	@TableField("proc_def_id_")
	@ApiModelProperty(value="关联 - 流程定义ID")
	protected String procDefId; 
	
	@TableField("proc_def_key_")
	@ApiModelProperty(value="关联 - 流程业务主键")
	protected String procDefKey; 
	
	@TableField("proc_def_name_")
	@ApiModelProperty(value="关联 - 流程名称")
	protected String procDefName; 
	
	@TableField("owner_id_")
	@ApiModelProperty(value="任务所属人ID")
	protected String ownerId; 
	
	@TableField("owner_name_")
	@ApiModelProperty(value="OWNER_NAME_")
	protected String ownerName; 
	
	@TableField("assignee_id_")
	@ApiModelProperty(value="任务执行人ID")
	protected String assigneeId; 
	
	@TableField("assignee_name_")
	@ApiModelProperty(value="ASSIGNEE_NAME_")
	protected String assigneeName; 
	
	@TableField("status_")
	@ApiModelProperty(value="任务状态")
	protected String status; 
	
	@TableField("priority_")
	@ApiModelProperty(value="任务优先级")
	protected Integer priority; 
	
	@TableField("create_time_")
	@ApiModelProperty(value = "创建时间")
	protected LocalDateTime createTime = LocalDateTime.now();

	
	@TableField("due_time_")
	@ApiModelProperty(value="任务到期时间")
	protected java.util.Date dueTime; 
	
	@TableField("suspend_state_")
	@ApiModelProperty(value="是否挂起(0正常,1挂起)")
	protected Short suspendState; 
	
	@TableField("parent_id_")
	@ApiModelProperty(value="父任务ID")
	protected String parentId; 
	
	@TableField("bpmn_inst_id_")
	@ApiModelProperty(value="bpmn实例ID")
	protected String bpmnInstId; 
	
	@TableField("bpmn_def_id_")
	@ApiModelProperty(value="BPMN定义ID")
	protected String bpmnDefId; 
	
	@TableField("type_id_")
	@ApiModelProperty(value="分类ID")
	protected String typeId; 
	
	@TableField("support_mobile_")
	@ApiModelProperty(value="支持手机")
	protected Integer supportMobile;
	
	@TableField("is_read_")
    @ApiModelProperty(value="传阅的任务是否已阅 0:待阅，1：已阅")
    protected Integer isRead = 0;
	
	@TableField("is_revoke_")
    @ApiModelProperty(value="传阅的任务是否撤回 0:否，1：是")
    protected Integer isRevoke = 0;

    public BpmTaskNotice(){};

     public LocalDateTime getCreateTime() {
         return createTime;
     }

     public void setCreateTime(LocalDateTime createTime) {
         this.createTime = createTime;
     }

     public BpmTaskNotice(String name, String subject, String procInstId, String procDefId, String procDefName,
                          String assigneeId, String assigneeName, String status, Integer supportMobile, String ownerName,
                          String ownerId, Integer isRead, String taskId, String nodeId) {
		super();
		this.taskId = taskId;
		this.name = name;
		this.subject = subject;
		this.procInstId = procInstId;
		this.procDefId = procDefId;
		this.procDefName = procDefName;
		this.ownerName = ownerName;
		this.assigneeId = assigneeId;
		this.assigneeName = assigneeName;
		this.status = status;
		this.supportMobile = supportMobile;
		this.ownerId = ownerId;
        this.isRead = isRead;
        this.nodeId = nodeId;
	}


     public Integer getIsRead() {
         return isRead;
     }

     public void setIsRead(Integer isRead) {
         this.isRead = isRead;
     }

     public Integer getIsRevoke() {
         return isRevoke;
     }

     public void setIsRevoke(Integer isRevoke) {
         this.isRevoke = isRevoke;
     }

     public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 任务ID
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
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * 返回 待办事项标题
	 * @return
	 */
	public String getSubject() {
		return this.subject;
	}
	
	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}
	
	/**
	 * 返回 关联 - 流程实例ID
	 * @return
	 */
	public String getProcInstId() {
		return this.procInstId;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	/**
	 * 返回 关联的任务ID
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
	 * @return
	 */
	public String getNodeId() {
		return this.nodeId;
	}
	
	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}
	
	/**
	 * 返回 关联 - 流程定义ID
	 * @return
	 */
	public String getProcDefId() {
		return this.procDefId;
	}
	
	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}
	
	/**
	 * 返回 关联 - 流程业务主键
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
	 * @return
	 */
	public String getOwnerId() {
		return this.ownerId;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	/**
	 * 返回 OWNER_NAME_
	 * @return
	 */
	public String getOwnerName() {
		return this.ownerName;
	}
	
	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}
	
	/**
	 * 返回 任务执行人ID
	 * @return
	 */
	public String getAssigneeId() {
		return this.assigneeId;
	}
	
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	
	/**
	 * 返回 ASSIGNEE_NAME_
	 * @return
	 */
	public String getAssigneeName() {
		return this.assigneeName;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 返回 任务状态
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	/**
	 * 返回 任务优先级
	 * @return
	 */
	public Integer getPriority() {
		return this.priority;
	}
	

	
	
	public void setDueTime(java.util.Date dueTime) {
		this.dueTime = dueTime;
	}
	
	/**
	 * 返回 任务到期时间
	 * @return
	 */
	public java.util.Date getDueTime() {
		return this.dueTime;
	}
	
	public void setSuspendState(Short suspendState) {
		this.suspendState = suspendState;
	}
	
	/**
	 * 返回 是否挂起(0正常,1挂起)
	 * @return
	 */
	public Short getSuspendState() {
		return this.suspendState;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 返回 父任务ID
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
	}
	
	public void setBpmnInstId(String bpmnInstId) {
		this.bpmnInstId = bpmnInstId;
	}
	
	/**
	 * 返回 bpmn实例ID
	 * @return
	 */
	public String getBpmnInstId() {
		return this.bpmnInstId;
	}
	
	public void setBpmnDefId(String bpmnDefId) {
		this.bpmnDefId = bpmnDefId;
	}
	
	/**
	 * 返回 BPMN定义ID
	 * @return
	 */
	public String getBpmnDefId() {
		return this.bpmnDefId;
	}
	
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	/**
	 * 返回 分类ID
	 * @return
	 */
	public String getTypeId() {
		return this.typeId;
	}
	
	public void setSupportMobile(Integer supportMobile) {
		this.supportMobile = supportMobile;
	}
	
	public DefaultBpmTask  convertToBpmTask(){
		DefaultBpmTask bpmTask =new DefaultBpmTask();
		bpmTask.setName(this.name);
		bpmTask.setSubject(this.subject);
		bpmTask.setProcInstId(this.procInstId);
		bpmTask.setNodeId(this.nodeId);
		bpmTask.setProcDefId(this.procDefId);
		bpmTask.setProcDefKey(this.procDefKey);
		bpmTask.setProcDefName(this.procDefName);
		bpmTask.setOwnerId(this.ownerId);
		bpmTask.setOwnerName(this.ownerName);
		bpmTask.setAssigneeId(this.assigneeId);
		bpmTask.setAssigneeName(this.assigneeName);
		bpmTask.setStatus(this.status);
		bpmTask.setCreateTime(this.createTime);
        bpmTask.setSupportMobile(this.supportMobile);
		return bpmTask;
	}
	
	/**
	 * 返回 支持手机
	 * @return
	 */
	public Integer getSupportMobile() {
		return this.supportMobile;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("subject", this.subject) 
		.append("procInstId", this.procInstId) 
		.append("taskId", this.taskId) 
		.append("execId", this.execId) 
		.append("nodeId", this.nodeId) 
		.append("procDefId", this.procDefId) 
		.append("procDefKey", this.procDefKey) 
		.append("procDefName", this.procDefName) 
		.append("ownerId", this.ownerId) 
		.append("ownerName", this.ownerName) 
		.append("assigneeId", this.assigneeId) 
		.append("assigneeName", this.assigneeName) 
		.append("status", this.status) 
		.append("priority", this.priority) 
		.append("createTime", this.createTime) 
		.append("dueTime", this.dueTime) 
		.append("suspendState", this.suspendState) 
		.append("parentId", this.parentId) 
		.append("bpmnInstId", this.bpmnInstId) 
		.append("bpmnDefId", this.bpmnDefId) 
		.append("typeId", this.typeId) 
		.append("supportMobile", this.supportMobile) 
		.toString();
	}
}