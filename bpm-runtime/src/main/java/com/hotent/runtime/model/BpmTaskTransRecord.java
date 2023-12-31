package com.hotent.runtime.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;


 /**
  * 流程流转记录
  * 
  * @company 广州宏天软件股份有限公司
  * @author zhangxianwen
  * @email zhangxw@jee-soft.cn
  * @date 2018年6月28日
  */
@TableName("bpm_task_trans_record")
@ApiModel(value="流程流转记录")
public class BpmTaskTransRecord extends AutoFillModel<BpmTaskTransRecord>{
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键id")
	protected String id; 
	
	@XmlAttribute(name = "taskId")
	@TableField("TASK_ID_")
	@ApiModelProperty(name="taskId",notes="关联的任务ID")
	protected String taskId; 
	
	@XmlAttribute(name = "taskName")
	@TableField("TASK_NAME_")
	@ApiModelProperty(name="taskName",notes="任务名称")
	protected String taskName; 
	
	@XmlAttribute(name = "taskSubject")
	@TableField("TASK_SUBJECT_")
	@ApiModelProperty(name="taskSubject",notes="流转任务标题")
	protected String taskSubject; 
	
	@XmlAttribute(name = "status")
	@TableField("STATUS_")
	@ApiModelProperty(name="status",notes="状态：0流转中 1流转结束 2已撤销")
	protected Short status; 
	
	@XmlAttribute(name = "transUsers")
	@TableField("TRANS_USERS_")
	@ApiModelProperty(name="transUsers",notes="流转人员")
	protected String transUsers; 
	
	@XmlAttribute(name = "transUserIds")
	@TableField("TRANS_USER_IDS_")
	@ApiModelProperty(name="transUserIds",notes="流转人员ID")
	protected String transUserIds; 
	
	@XmlAttribute(name = "transOpinion")
	@TableField("TRANS_OPINION_")
	@ApiModelProperty(name="transOpinion",notes="流转意见（原因）")
	protected String transOpinion; 
	
	@XmlAttribute(name = "decideType")
	@TableField("DECIDE_TYPE")
	@ApiModelProperty(name="decideType",notes="决策类型 同意：agree   反对：oppose")
	protected String decideType; 
	
	@XmlAttribute(name = "action")
	@TableField("ACTION_")
	@ApiModelProperty(name="action",notes="完成后的操作动作")
	protected String action; 
	
	@XmlAttribute(name = "voteType")
	@TableField("VOTE_TYPE_")
	@ApiModelProperty(name="voteType",notes="投票类型（amount：票数,percent：百分比）")
	protected String voteType; 
	
	@XmlAttribute(name = "voteAmount")
	@TableField("VOTE_AMOUNT_")
	@ApiModelProperty(name="voteAmount",notes="票数")
	protected Short voteAmount; 
	
	@XmlAttribute(name = "signType")
	@TableField("SIGN_TYPE_")
	@ApiModelProperty(name="signType",notes="会签类型：parallel，seq")
	protected String signType; 
	
	@XmlAttribute(name = "totalAmount")
	@TableField("TOTAL_AMOUNT_")
	@ApiModelProperty(name="totalAmount",notes="总票数")
	protected Short totalAmount; 
	
	@XmlAttribute(name = "agreeAmount")
	@TableField("AGREE_AMOUNT_")
	@ApiModelProperty(name="agreeAmount",notes="通过票数")
	protected Short agreeAmount; 
	
	@XmlAttribute(name = "opposeAmount")
	@TableField("OPPOSE_AMOUNT_")
	@ApiModelProperty(name="opposeAmount",notes="反对票数")
	protected Short opposeAmount; 
	
	@XmlAttribute(name = "transOwner")
	@TableField("TRANS_OWNER_")
	@ApiModelProperty(name="transOwner",notes="流转任务所属人")
	protected String transOwner; 
	
	@XmlAttribute(name = "creator")
	@TableField("CREATOR_")
	@ApiModelProperty(name="creator",notes="创建人")
	protected String creator; 
	
	@XmlAttribute(name = "transTime")
	@TableField("TRANS_TIME_")
	@ApiModelProperty(name="transTime",notes="流转时间")
	protected LocalDateTime transTime; 
	
	@XmlAttribute(name = "defName")
	@TableField("DEF_NAME_")
	@ApiModelProperty(name="defName",notes="流程名称")
	protected String defName; 
	
	@XmlAttribute(name = "procInstId")
	@TableField("PROC_INST_ID_")
	@ApiModelProperty(name="procInstId",notes="流程实例ID")
	protected String procInstId; 
	
	@TableField(exist=false)
	@ApiModelProperty(name="receiverList",notes="接收人员列表")
	protected List<BpmTransReceiver> receiverList = new ArrayList<BpmTransReceiver>();
	
	
	public List<BpmTransReceiver> getReceiverList() {
		return receiverList;
	}

	public void setReceiverList(List<BpmTransReceiver> receiverList) {
		this.receiverList = receiverList;
	}

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
	
	public void setTaskSubject(String taskSubject) {
		this.taskSubject = taskSubject;
	}
	
	/**
	 * 返回 流转任务标题
	 * @return
	 */
	public String getTaskSubject() {
		return this.taskSubject;
	}
	
	public void setStatus(Short status) {
		this.status = status;
	}
	
	/**
	 * 返回 状态：0流转中 1流转结束 2已撤销
	 * @return
	 */
	public Short getStatus() {
		return this.status;
	}
	
	public void setTransUsers(String transUsers) {
		this.transUsers = transUsers;
	}
	
	/**
	 * 返回 流转人员
	 * @return
	 */
	public String getTransUsers() {
		return this.transUsers;
	}
	
	public void setTransUserIds(String transUserIds) {
		this.transUserIds = transUserIds;
	}
	
	/**
	 * 返回 流转人员ID
	 * @return
	 */
	public String getTransUserIds() {
		return this.transUserIds;
	}
	
	public void setTransOpinion(String transOpinion) {
		this.transOpinion = transOpinion;
	}
	
	/**
	 * 返回 流转意见（原因）
	 * @return
	 */
	public String getTransOpinion() {
		return this.transOpinion;
	}
	
	public void setDecideType(String decideType) {
		this.decideType = decideType;
	}
	
	/**
	 * 返回 决策类型 同意：agree   反对：oppose
	 * @return
	 */
	public String getDecideType() {
		return this.decideType;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	/**
	 * 返回 完成后的操作动作
	 * @return
	 */
	public String getAction() {
		return this.action;
	}
	
	public void setVoteType(String voteType) {
		this.voteType = voteType;
	}
	
	/**
	 * 返回 投票类型
	 * @return
	 */
	public String getVoteType() {
		return this.voteType;
	}
	
	public void setVoteAmount(Short voteAmount) {
		this.voteAmount = voteAmount;
	}
	
	/**
	 * 返回 票数
	 * @return
	 */
	public Short getVoteAmount() {
		return this.voteAmount;
	}
	
	public void setSignType(String signType) {
		this.signType = signType;
	}
	
	/**
	 * 返回 会签类型
	 * @return
	 */
	public String getSignType() {
		return this.signType;
	}
	
	public void setTotalAmount(Short totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	/**
	 * 返回 总票数
	 * @return
	 */
	public Short getTotalAmount() {
		return this.totalAmount;
	}
	
	public void setAgreeAmount(Short agreeAmount) {
		this.agreeAmount = agreeAmount;
	}
	
	/**
	 * 返回 通过票数
	 * @return
	 */
	public Short getAgreeAmount() {
		return this.agreeAmount;
	}
	
	public void setOpposeAmount(Short opposeAmount) {
		this.opposeAmount = opposeAmount;
	}
	
	/**
	 * 返回 反对票数
	 * @return
	 */
	public Short getOpposeAmount() {
		return this.opposeAmount;
	}
	
	public void setTransOwner(String transOwner) {
		this.transOwner = transOwner;
	}
	
	/**
	 * 返回 流转任务所属人
	 * @return
	 */
	public String getTransOwner() {
		return this.transOwner;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
	 * 返回 创建人
	 * @return
	 */
	public String getCreator() {
		return this.creator;
	}
	
	public LocalDateTime getTransTime() {
		return transTime;
	}

	public void setTransTime(LocalDateTime transTime) {
		this.transTime = transTime;
	}

	public String getDefName() {
		return defName;
	}

	public void setDefName(String defName) {
		this.defName = defName;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("taskId", this.taskId) 
		.append("taskName", this.taskName) 
		.append("taskSubject", this.taskSubject) 
		.append("status", this.status) 
		.append("transUsers", this.transUsers) 
		.append("transUserIds", this.transUserIds) 
		.append("transOpinion", this.transOpinion) 
		.append("decideType", this.decideType) 
		.append("action", this.action) 
		.append("voteType", this.voteType) 
		.append("voteAmount", this.voteAmount) 
		.append("signType", this.signType) 
		.append("totalAmount", this.totalAmount) 
		.append("agreeAmount", this.agreeAmount) 
		.append("opposeAmount", this.opposeAmount) 
		.append("transOwner", this.transOwner) 
		.append("creator", this.creator) 
		.append("transTime",this.transTime)
		.append("defName",this.defName)
		.append("procInstId",this.procInstId)
		.toString();
	}
}