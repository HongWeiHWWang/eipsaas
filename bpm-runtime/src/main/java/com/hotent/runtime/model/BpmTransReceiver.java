package com.hotent.runtime.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


/**
 * 流转任务接收人 实体对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */

@TableName("bpm_trans_receiver")
@ApiModel(value="流转任务接收人 实体对象")
public class BpmTransReceiver extends BaseModel<BpmTransReceiver>{
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(name="id",notes="主键")
	protected String id; 
	
	@XmlAttribute(name = "transRecordid")
	@TableField("TRANS_RECORDID_")
	@ApiModelProperty(name="transRecordid",notes="流转任务记录ID")
	protected String transRecordid; 
	
	@XmlAttribute(name = "receiver")
	@TableField("RECEIVER_")
	@ApiModelProperty(name="receiver",notes="流转接受人员")
	protected String receiver; 
	
	@XmlAttribute(name = "receiverId")
	@TableField("RECEIVER_ID_")
	@ApiModelProperty(name="receiverId",notes="流转接收人id")
	protected String receiverId; 
	
	@XmlAttribute(name = "status")
	@TableField("STATUS_")
	@ApiModelProperty(name="status",notes="状态:0尚未处理1已处理")
	protected Short status; 
	
	@XmlAttribute(name = "opinion")
	@TableField("OPINION_")
	@ApiModelProperty(name="opinion",notes="审核意见")
	protected String opinion; 
	
	@XmlAttribute(name = "receiverTime")
	@TableField("RECEIVER_TIME_")
	@ApiModelProperty(name="receiverTime",notes="接收时间")
	protected LocalDateTime receiverTime; 
	
	@XmlAttribute(name = "checkTime")
	@TableField("CHECK_TIME_")
	@ApiModelProperty(name="checkTime",notes="审核时间")
	protected LocalDateTime checkTime; 
	
	@XmlAttribute(name = "checkType")
	@TableField("CHECK_TYPE_")
	@ApiModelProperty(name="checkType",notes="审核状态")
	protected Short checkType; 
	
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	public void setTransRecordid(String transRecordid) {
		this.transRecordid = transRecordid;
	}
	
	/**
	 * 返回 流转任务记录ID
	 * @return
	 */
	public String getTransRecordid() {
		return this.transRecordid;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * 返回 流转接受人员
	 * @return
	 */
	public String getReceiver() {
		return this.receiver;
	}
	
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	
	/**
	 * 返回 流转接收人id
	 * @return
	 */
	public String getReceiverId() {
		return this.receiverId;
	}
	
	public void setStatus(Short status) {
		this.status = status;
	}
	
	/**
	 * 返回 状态:0尚未处理1已处理
	 * @return
	 */
	public Short getStatus() {
		return this.status;
	}
	
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	/**
	 * 返回 审核意见
	 * @return
	 */
	public String getOpinion() {
		return this.opinion;
	}
	
	public void setReceiverTime(LocalDateTime receiverTime) {
		this.receiverTime = receiverTime;
	}
	
	/**
	 * 返回 接收时间
	 * @return
	 */
	public LocalDateTime getReceiverTime() {
		return this.receiverTime;
	}
	
	public void setCheckTime(LocalDateTime checkTime) {
		this.checkTime = checkTime;
	}
	
	/**
	 * 返回 审核时间
	 * @return
	 */
	public LocalDateTime getCheckTime() {
		return this.checkTime;
	}
	
	public void setCheckType(Short checkType) {
		this.checkType = checkType;
	}
	
	/**
	 * 返回 审核状态
	 * @return
	 */
	public Short getCheckType() {
		return this.checkType;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("transRecordid", this.transRecordid) 
		.append("receiver", this.receiver) 
		.append("receiverId", this.receiverId) 
		.append("status", this.status) 
		.append("opinion", this.opinion) 
		.append("receiverTime", this.receiverTime) 
		.append("checkTime", this.checkTime) 
		.append("checkType", this.checkType) 
		.toString();
	}
}