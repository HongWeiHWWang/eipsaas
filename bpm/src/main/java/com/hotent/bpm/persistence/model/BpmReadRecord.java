package com.hotent.bpm.persistence.model;

import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import com.hotent.base.util.StringUtil;

/**
 * 流程实例、任务阅读记录 entity对象
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2019-03-20 09:20
 */
@TableName("bpm_read_record")
public class BpmReadRecord extends BaseModel<BpmReadRecord> {
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
	protected String procInstId; /* 流程实例ID */
	
	@TableField("task_id_")
	protected String taskId; /* 任务ID */
	
	@TableField("task_key_")
	protected String taskKey; /* 任务定义Key */
	
	@TableField("task_name_")
	protected String taskName; /* 任务名称 */
	
	@TableField("reader_")
	protected String reader; /* 阅读人ID */
	
	@TableField("reader_name_")
	protected String readerName; /* 阅读人名 */
	
	@TableField("read_time_")
	protected LocalDateTime readTime; /* 阅读时间 */
	
	@TableField("org_id_")
	protected String orgId; /* 组织id*/
	
	@TableField("org_path_")
	protected String orgPath; /* 组织名称*/
	
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

	
	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getReaderName() {
		return readerName;
	}

	public void setReaderName(String readerName) {
		this.readerName = readerName;
	}

	public LocalDateTime getReadTime() {
		return readTime;
	}

	public void setReadTime(LocalDateTime readTime) {
		this.readTime = readTime;
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

	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("procDefId", this.procDefId)
				.append("supInstId", this.supInstId)
				.append("procInstId", this.procInstId)
				.append("taskId", this.taskId).append("taskKey", this.taskKey)
				.append("taskName", this.taskName)
				.append("reader", this.reader)
				.append("readerName", this.readerName)
				.append("readTime", this.readTime)
				.append("orgId", this.orgId)
				.append("orgPath", this.orgPath).toString();
	}
}