package com.hotent.bpm.persistence.model;

import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.OpinionStatus;
import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;

/**
 * <pre>
 * 	对象功能:流程审批意见 entity对象
 *  开发公司:广州宏天软件有限公司 
 *  开发人员:zyp
 *  创建时间:2014-03-21 17:07:23
 *  </pre>	
 */
@TableName("bpm_check_opinion")
public class DefaultBpmCheckOpinion extends BaseModel<DefaultBpmCheckOpinion> implements BpmTaskOpinion {
	/***  */
	private static final long serialVersionUID = 1L;

	public static final String OPINION_FLAG = "__form_opinion";
	
	@TableId("id_")
	protected String id; /* 意见ID */

    @TableField("proc_def_id_")
	protected String procDefId; /* 流程定义ID */

    @TableField("sup_inst_id_")
	protected String supInstId="0"; /* 父流程实例ID */

    @TableField("proc_inst_id_")
	protected String procInstId; /* proc_inst_id_ */

    @TableField("task_id_")
	protected String taskId; /* 任务ID */

    @TableField("task_key_")
	protected String taskKey; /* 任务定义Key */

    @TableField("task_name_")
	protected String taskName; /* 任务名称 */

    @TableField("token_")
	protected String token; /* 任务令牌 */

    @TableField("qualfieds_")
	protected String qualfieds; /* 有审批资格用户ID串 */

    @TableField("qualfied_names_")
	protected String qualfiedNames; /* 有审批资格用户名称串 */

    @TableField("auditor_")
	protected String auditor; /* 执行人ID */

    @TableField("auditor_name_")
	protected String auditorName; /* 执行人名 */

    @TableField("opinion_")
	protected String opinion; /* 审批意见 */

	/**
	 *  审批状态(@see {@link OpinionStatus})。
	 * <pre>
	 * start=发起流程；
	 * end=结束流程；
	 * awaiting_check=待审批；
	 * agree=同意；
	 * against=反对；
	 * return=驳回；
	 * abandon=弃权；
	 * retrieve=追回
	 * </pre>
	 **/
    @TableField("status_")
	protected String status; 

    @TableField("is_read_")
    protected Integer isRead = 0;//0：待阅，1：已阅

    @TableField("form_def_id_")
	protected String formDefId; /* 表单定义ID */

    @TableField("agent_leader_id_")
	protected String agentLeaderId;/* 被代理的领导id */

    @TableField("sign_type_")
	protected String signType;/* 签署任务类型  查看  CustomSignNodeDef   */

    @TableField("skip_type_")
	protected String skipType;/* 审批意见审批跳过类型  */
    
    @TableField("form_name_")
	protected String formName; /* 表单名 */

    @TableField("create_time_")
	protected LocalDateTime createTime; /* 执行开始时间 */

    @TableField("assign_time_")
	protected LocalDateTime assignTime; /* 任务分配用户时间 */

    @TableField("complete_time_")
	protected LocalDateTime completeTime; /* 结束时间 */

    @TableField("dur_ms_")
	protected Long durMs; /* 持续时间(ms) */
	/**
	 * 可以使用JSON存储。 
	 * 附件存放格式。 [{fileId:"",name:""},{fileId:"",name:""}]
	 */
    @TableField("files_")
	protected String files = ""; /* 附件 */
    /**
     * 可以使用JSON存储。
     * 正文存放格式。 [{fileId:"",name:""},{fileId:"",name:""}]
     */

    @TableField("z_files_")
    protected String zfiles = ""; /* 正文 */

    @TableField("interpose_")
	protected Integer interpose = 0; /* 是否干预 0,不干预,1干预 */

    @TableField("org_id_")
	protected String orgId; /* 组织id*/

    @TableField("org_path_")
	protected String orgPath; /* 组织名称*/

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

    @TableField("form_data_")
	protected String formData;

    @TableField("parent_task_id_")
	protected String parentTaskId;/*父任务id，主要用于并行审批任务撤回*/

    @TableField(exist=false)
	protected String statusVal; 

    public String getZfiles() {
        return zfiles;
    }

    public void setZfiles(String zfiles) {
        this.zfiles = zfiles;
    }

    public DefaultBpmCheckOpinion() {}
	
	public DefaultBpmCheckOpinion(String id ,String opinion,String files) {
		this.id = id;
		this.opinion = opinion;
		this.files = files;
	}

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 意见ID
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	/**
	 * 返回 流程定义ID
	 * 
	 * @return
	 */
	public String getProcDefId() {
		return this.procDefId;
	}

	public void setSupInstId(String supInstId_) {
		if(StringUtil.isEmpty(supInstId_)){
			supInstId_="0";
		}
		this.supInstId = supInstId_;
	}

	/**
	 * 返回 父流程实例ID
	 * 
	 * @return
	 */
	public String getSupInstId() {
		return this.supInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	/**
	 * 返回 proc_inst_id_
	 * 
	 * @return
	 */
	public String getProcInstId() {
		return this.procInstId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 返回 任务ID
	 * 
	 * @return
	 */
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	/**
	 * 返回 任务定义Key
	 * 
	 * @return
	 */
	public String getTaskKey() {
		return this.taskKey;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * 返回 任务名称
	 * 
	 * @return
	 */
	public String getTaskName() {
		return this.taskName;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * 返回 任务令牌
	 * 
	 * @return
	 */
	public String getToken() {
		return this.token;
	}

	public void setQualfieds(String qualfieds) {
		this.qualfieds = qualfieds;
	}

	/**
	 * 返回 有审批资格用户ID串
	 * 
	 * @return
	 */
	public String getQualfieds() {
		return this.qualfieds;
	}

	public void setQualfiedNames(String qualfiedNames) {
		this.qualfiedNames = qualfiedNames;
	}

	/**
	 * 返回 有审批资格用户名称串
	 * 
	 * @return
	 */
	public String getQualfiedNames() {
		return this.qualfiedNames;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	/**
	 * 返回 执行人ID
	 * 
	 * @return
	 */
	public String getAuditor() {
		return this.auditor;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	/**
	 * 返回 执行人名
	 * 
	 * @return
	 */
	public String getAuditorName() {
		return this.auditorName;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	/**
	 * 返回 审批意见
	 * 
	 * @return
	 */
	public String getOpinion() {
		return this.opinion;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 返回 审批状态。start=发起流程；awaiting_check=待审批；agree=同意；against=反对；return=驳回；
	 * abandon=弃权；retrieve=追回
	 * 
	 * @return
	 */
	public String getStatus() {
		return this.status;
	}

	public void setFormDefId(String formDefId) {
		this.formDefId = formDefId;
	}

	/**
	 * 返回 表单定义ID
	 * 
	 * @return
	 */
	public String getFormDefId() {
		return this.formDefId;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * 返回 表单名
	 * 
	 * @return
	 */
	public String getFormName() {
		return this.formName;
	}

	public void setAssignTime(LocalDateTime assignTime) {
		this.assignTime = assignTime;
	}

	/**
	 * 返回 任务分配用户时间
	 * 
	 * @return
	 */
	public LocalDateTime getAssignTime() {
		return this.assignTime;
	}

	public void setCompleteTime(LocalDateTime completeTime) {
		this.completeTime = completeTime;
	}

	/**
	 * 返回 结束时间
	 * 
	 * @return
	 */
	public LocalDateTime getCompleteTime() {
		return this.completeTime;
	}

	public void setDurMs(Long durMs) {
		this.durMs = durMs;
	}

	/**
	 * 返回 持续时间(ms)
	 * 
	 * @return
	 */
	public Long getDurMs() {
		return this.durMs;
	}

	@Override
	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	public Integer getInterpose() {
		return interpose;
	}

	public void setInterpose(Integer interpose) {
		this.interpose = interpose;
	}
	
	public String getStatusVal() {
		if(this.status != null)
			statusVal = OpinionStatus.fromKey(this.status).getValue();
		return statusVal;
	}

	public void setStatusVal(String statusVal) {
		this.statusVal = statusVal;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}
	
	public String getAgentLeaderId() {
		return agentLeaderId;
	}

	public void setAgentLeaderId(String agentLeaderId) {
		this.agentLeaderId = agentLeaderId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("procDefId", this.procDefId)
				.append("supInstId", this.supInstId)
				.append("procInstId", this.procInstId)
				.append("taskId", this.taskId).append("taskKey", this.taskKey)
				.append("taskName", this.taskName).append("token", this.token)
				.append("qualfieds", this.qualfieds)
				.append("qualfiedNames", this.qualfiedNames)
				.append("auditor", this.auditor)
				.append("auditorName", this.auditorName)
				.append("opinion", this.opinion).append("status", this.status)
				.append("formDefId", this.formDefId)
				.append("formName", this.formName)
				.append("createTime", this.createTime)
				.append("assignTime", this.assignTime)
				.append("completeTime", this.completeTime)
				.append("orgId", this.orgId)
				.append("orgPath", this.orgPath)
				.append("durMs", this.durMs).toString();
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

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	@Override
	public String getSkipType() {
		return this.skipType;
	}
	
	public void setSkipType(String skipType) {
		this.skipType = skipType;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	
}