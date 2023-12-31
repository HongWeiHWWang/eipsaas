package com.hotent.runtime.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.entity.AutoFillModel;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.util.BpmUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 流程流转 entity对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@TableName("bpm_task_trans")
@ApiModel(value="流程流转entity对象")
public class BpmTaskTrans extends AutoFillModel<BpmTaskTrans> implements TaskTrans{
	
	public static final String SIGN_TYPE_PARALLEL="parallel";
	public static final String SIGN_TYPE_SEQ="seq";
	
	public static final String SIGN_ACTION_SUBMIT="submit";
	public static final String SIGN_ACTION_BACK="back";
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键")
	protected String  id;
	
	@XmlAttribute(name = "instanceId")
	@TableField("INSTANCE_ID_")
	@ApiModelProperty(name="instanceId",notes="流程实例")
	protected String  instanceId;
	
	@XmlAttribute(name = "taskId")
	@TableField("TASK_ID_")
	@ApiModelProperty(name="taskId",notes="任务ID")
	protected String  taskId;
	
	@XmlAttribute(name = "action")
	@TableField("ACTION_")
	@ApiModelProperty(name="action",notes="完成后的操作(submit：提交,back：返回)")
	protected String  action=SIGN_ACTION_BACK;
	
	@XmlAttribute(name = "creator")
	@TableField("CREATOR_")
	@ApiModelProperty(name="creator",notes="创建人")
	protected String  creator;
	
	@XmlAttribute(name = "decideType")
	@TableField("DECIDE_TYPE_")
	@ApiModelProperty(name="decideType",notes="决策类型（agree：同意,refuse：反对）")
	protected String  decideType;
	
	@XmlAttribute(name = "voteType")
	@TableField("VOTE_TYPE_")
	@ApiModelProperty(name="voteType",notes="投票类型（amount：票数,percent：百分比）")
	protected String  voteType;
	
	@XmlAttribute(name = "voteAmount")
	@TableField("VOTE_AMOUNT_")
	@ApiModelProperty(name="voteAmount",notes="票数")
	protected Short  voteAmount=0;
	
	@XmlAttribute(name = "signType")
	@TableField("SIGN_TYPE_")
	@ApiModelProperty(name="signType",notes="会签类型：parallel，seq")
	protected String  signType=SIGN_TYPE_PARALLEL;
	
	@XmlAttribute(name = "totalAmount")
	@TableField("TOTAL_AMOUNT_")
	@ApiModelProperty(name="totalAmount",notes="总票数")
	protected Short  totalAmount=0;
	
	@XmlAttribute(name = "agreeAmount")
	@TableField("AGREE_AMOUNT_")
	@ApiModelProperty(name="agreeAmount",notes="通过票数")
	protected Short  agreeAmount=0;
	
	@XmlAttribute(name = "opposeAmount")
	@TableField("OPPOSE_AMOUNT_")
	@ApiModelProperty(name="opposeAmount",notes="反对票数")
	protected Short  opposeAmount=0;
	
	@XmlAttribute(name = "seq")
	@TableField("SEQ_")
	@ApiModelProperty(name="seq",notes="投票次序")
	protected Short  seq=0;
	
	@XmlAttribute(name = "userJson")
	@TableField("USER_JSON_")
	@ApiModelProperty(name="userJson",notes="用户数据")
	protected String  userJson="";
	
	@XmlAttribute(name = "allowFormEdit")
	@TableField("ALLOW_FORM_EDIT_")
	@ApiModelProperty(name="allowFormEdit",notes="允许表单编辑")
	protected Short allowFormEdit=0;
	
	public void setId(String id) 
	{
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getId()
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setInstanceId(String instanceId) 
	{
		this.instanceId = instanceId;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getInstanceId()
	 */
	public String getInstanceId() 
	{
		return this.instanceId;
	}
	public void setTaskId(String taskId) 
	{
		this.taskId = taskId;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getTaskId()
	 */
	public String getTaskId() 
	{
		return this.taskId;
	}
	public void setAction(String action) 
	{
		this.action = action;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getAction()
	 */
	public String getAction() 
	{
		return this.action;
	}
	
	public void setCreator(String creator) 
	{
		this.creator = creator;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getCreator()
	 */
	public String getCreator() 
	{
		return this.creator;
	}
	
	public void setDecideType(String decideType) 
	{
		this.decideType = decideType;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getDecideType()
	 */
	public String getDecideType() 
	{
		return this.decideType;
	}
	public void setVoteType(String voteType) 
	{
		this.voteType = voteType;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getVoteType()
	 */
	public String getVoteType() 
	{
		return this.voteType;
	}
	public void setVoteAmount(Short voteAmount) 
	{
		this.voteAmount = voteAmount;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getVoteAmount()
	 */
	public Short getVoteAmount() 
	{
		return this.voteAmount;
	}
	public void setSignType(String signType) 
	{
		this.signType = signType;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getSignType()
	 */
	public String getSignType() 
	{
		return this.signType;
	}
	public void setTotalAmount(Short totalAmount) 
	{
		this.totalAmount = totalAmount;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getTotalAmount()
	 */
	public Short getTotalAmount() 
	{
		return this.totalAmount;
	}
	public void setAgreeAmount(Short aggreeAmount) 
	{
		this.agreeAmount = aggreeAmount;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getAgreeAmount()
	 */
	public Short getAgreeAmount() 
	{
		return this.agreeAmount;
	}
	public void setOpposeAmount(Short opposeAmount) 
	{
		this.opposeAmount = opposeAmount;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getOpposeAmount()
	 */
	public Short getOpposeAmount() 
	{
		return this.opposeAmount;
	}
	public void setSeq(Short seq) 
	{
		this.seq = seq;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getSeq()
	 */
	public Short getSeq() 
	{
		return this.seq;
	}
	public void setUserJson(String userJson) 
	{
		this.userJson = userJson;
	}
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getUserJson()
	 */
	public String getUserJson() 
	{
		return this.userJson;
	}
	
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getUserByIndex(int)
	 */
	public IUser getUserByIndex(int index) throws IOException{
		JsonNode jsonAry = JsonUtil.toJsonNode(this.userJson);
		ObjectNode jsonObj= (ObjectNode) jsonAry.get(index);
		String userId=jsonObj.get("userId").asText();
		String fullname=jsonObj.get("fullname").asText();
		IUser user=BpmUtil. getUser(userId, fullname);
		return user;
	}
	
	
	/* (non-Javadoc)
	 * @see com.hotent.runtime.persistence.model.TaskTrans#getAllowFormEdit()
	 */
	public Short getAllowFormEdit() {
		return allowFormEdit;
	}
	public void setAllowFormEdit(Short allowFormEdit) {
		this.allowFormEdit = allowFormEdit;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("instanceId", this.instanceId) 
		.append("taskId", this.taskId) 
		.append("action", this.action) 
		.append("creator", this.creator) 
		.append("decideType", this.decideType) 
		.append("voteType", this.voteType) 
		.append("voteAmount", this.voteAmount) 
		.append("signType", this.signType) 
		.append("totalAmount", this.totalAmount) 
		.append("agreeAmount", this.agreeAmount) 
		.append("opposeAmount", this.opposeAmount) 
		.append("seq", this.seq) 
		.append("userJson", this.userJson) 
		.toString();
	}
	@Override
	public LocalDateTime getCreateTime() {
		return null;
	}
}