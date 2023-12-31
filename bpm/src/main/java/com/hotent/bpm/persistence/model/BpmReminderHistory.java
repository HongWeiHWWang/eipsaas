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
 * 描述：催办历史 实体对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "催办历史 实体对象")
@TableName("bpm_reminder_history")
public class BpmReminderHistory extends BaseModel<BpmReminderHistory> {

	private static final long serialVersionUID = 1096776600805470960L;

	@TableId("id_")
	@ApiModelProperty(name = "id", notes = "主键")
	protected String id;

	@TableField("inst_id_")
	@ApiModelProperty(name = "instId", notes = "流程实例ID")
	protected String instId;

	@TableField("isnt_name_")
	@ApiModelProperty(name = "isntName", notes = "流程实例标题")
	protected String isntName;

	@TableField("node_name_")
	@ApiModelProperty(name = "nodeName", notes = "节点名称")
	protected String nodeName;

	@TableField("node_id_")
	@ApiModelProperty(name = "nodeId", notes = "节点ID")
	protected String nodeId;

	@TableField("execute_date_")
	@ApiModelProperty(name = "executeDate", notes = "执行时间")
	protected LocalDateTime executeDate;

	@TableField("remind_type_")
	@ApiModelProperty(name = "remindType", notes = "执行类型")
	protected String remindType;

	@TableField("user_id_")
	@ApiModelProperty(name = "userId", notes = "用户id")
	protected String userId;

	@TableField("note_")
	@ApiModelProperty(name = "note", notes = "说明")
	protected String note;
	
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回 id_
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	/**
	 * 返回 流程实例ID
	 * 
	 * @return
	 */
	public String getInstId() {
		return this.instId;
	}

	public void setIsntName(String isntName) {
		this.isntName = isntName;
	}

	/**
	 * 返回 流程实例标题
	 * 
	 * @return
	 */
	public String getIsntName() {
		return this.isntName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 返回 节点名称
	 * 
	 * @return
	 */
	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 返回 节点ID
	 * 
	 * @return
	 */
	public String getNodeId() {
		return this.nodeId;
	}

	public void setExecuteDate(LocalDateTime executeDate) {
		this.executeDate = executeDate;
	}

	/**
	 * 返回 执行时间
	 * 
	 * @return
	 */
	public LocalDateTime getExecuteDate() {
		return this.executeDate;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	/**
	 * 返回 执行类型
	 * 
	 * @return
	 */
	public String getRemindType() {
		return this.remindType;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 返回 user_id_
	 * 
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * 返回 说明
	 * 
	 * @return
	 */
	public String getNote() {
		return this.note;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("instId", this.instId)
				.append("isntName", this.isntName).append("nodeName", this.nodeName).append("nodeId", this.nodeId)
				.append("executeDate", this.executeDate).append("remindType", this.remindType)
				.append("userId", this.userId).append("note", this.note).toString();
	}
}