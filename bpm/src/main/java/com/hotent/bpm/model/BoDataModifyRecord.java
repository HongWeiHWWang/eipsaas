package com.hotent.bpm.model;
import java.time.LocalDateTime;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 流程表单数据修改记录
 * <pre> 
 * 描述：流程表单数据修改记录 实体对象
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-23 11:45:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("bpm_form_modify_record")
@ApiModel(value = "BoDataModifyRecord",description = "流程表单数据修改记录") 
public class BoDataModifyRecord extends BaseModel<BoDataModifyRecord>{

	private static final long serialVersionUID = 1L;
	
	@TableId("id_")
	@ApiModelProperty(value="ID_")
	protected String id;
	
	@TableField("ref_id_")
	@ApiModelProperty(value="外键（表单记录主键）")
	protected String refId;
	
	@TableField("user_id_")
	@ApiModelProperty(value="修改人id")
	protected String userId; 
	
	@TableField("user_name_")
	@ApiModelProperty(value="修改人姓名")
	protected String userName; 
	
	@TableField("inst_id_")
	@ApiModelProperty(value="流程实例id")
	protected String instId; 
	
	@TableField("task_id_")
	@ApiModelProperty(value="任务id")
	protected String taskId; 
	
	@TableField("task_name_")
	@ApiModelProperty(value="任务名称")
	protected String taskName; 
	
	@TableField("node_id_")
	@ApiModelProperty(value="节点id")
	protected String nodeId; 
	
	@TableField("modify_time_")
	@ApiModelProperty(value="修改时间")
	protected LocalDateTime modifyTime; 
	
	@TableField("ip_")
	@ApiModelProperty(value="修改人ip")
	protected String ip; 
	
	@TableField("detail_")
	@ApiModelProperty(value="修改详情")
	protected String detail; 
	
	@TableField("reason_")
	@ApiModelProperty(value="修改原因")
	protected String reason;
	
	@TableField("data_")
	@ApiModelProperty(value="表单数据")
	protected String data;
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 ID_
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 返回 修改人id
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * 返回 修改人姓名
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}
	
	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	/**
	 * 返回 流程实例id
	 * @return
	 */
	public String getInstId() {
		return this.instId;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	/**
	 * 返回 任务id
	 * @return
	 */
	public String getTaskId() {
		return this.taskId;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	/**
	 * 返回 任务名称
	 * @return
	 */
	public String getTaskName() {
		return this.taskName;
	}
	
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	/**
	 * 返回 节点id
	 * @return
	 */
	public String getNodeId() {
		return this.nodeId;
	}
	
	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	/**
	 * 返回 修改时间
	 * @return
	 */
	public LocalDateTime getModifyTime() {
		return this.modifyTime;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * 返回 修改人ip
	 * @return
	 */
	public String getIp() {
		return this.ip;
	}
	
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	/**
	 * 返回 修改详情
	 * @return
	 */
	public String getDetail() {
		return this.detail;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	/**
	 * 返回 修改原因
	 * @return
	 */
	public String getReason() {
		return this.reason;
	}
	
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("userId", this.userId) 
		.append("userName", this.userName) 
		.append("instId", this.instId) 
		.append("taskId", this.taskId) 
		.append("taskName", this.taskName) 
		.append("nodeId", this.nodeId) 
		.append("modifyTime", this.modifyTime) 
		.append("ip", this.ip) 
		.append("detail", this.detail) 
		.append("reason", this.reason) 
		.toString();
	}
}