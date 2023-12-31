package com.hotent.oa.model;
import org.apache.commons.lang.builder.ToStringBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;
import java.time.LocalDateTime;


 /**
 * 会议室预约
 * <pre> 
 * 描述：会议室预约 实体对象
 * 构建组：x7
 * 作者:David
 * 邮箱:376514860@qq.com
 * 日期:2023-12-24 01:13:25
 * 版权：wijo
 * </pre>
 */
 @TableName("w_meetingroom_book")
 @ApiModel(value = "MeetingroomBook",description = "会议室预约") 
public class MeetingroomBook extends BaseModel<MeetingroomBook>{

	private static final long serialVersionUID = 1L;
	@XmlTransient
	@TableId("ID_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@XmlAttribute(name = "refId")
	@TableField("REF_ID_")
	@ApiModelProperty(value="外键")
	protected String refId; 
	
	@XmlAttribute(name = "FMeetingroomId")
	@TableField("F_MEETINGROOM_ID")
	@ApiModelProperty(value="会议室ID")
	protected String FMeetingroomId; 
	
	@XmlAttribute(name = "FMeetingId")
	@TableField("F_MEETING_ID_")
	@ApiModelProperty(value="会议ID")
	protected String FMeetingId; 
	
	@XmlAttribute(name = "FMeetingName")
	@TableField("F_MEETING_NAME_")
	@ApiModelProperty(value="会议名称")
	protected String FMeetingName; 
	
	@XmlAttribute(name = "FHostessId")
	@TableField("F_HOSTESS_ID_")
	@ApiModelProperty(value="组织者ID")
	protected String FHostessId; 
	
	@XmlAttribute(name = "FHosstessName")
	@TableField("F_HOSSTESS_NAME_")
	@ApiModelProperty(value="组织者姓名")
	protected String FHosstessName; 
	
	@XmlAttribute(name = "FHosstessPhoto")
	@TableField("F_HOSSTESS_PHOTO_")
	@ApiModelProperty(value="组织者电话")
	protected String FHosstessPhoto; 
	
	@XmlAttribute(name = "FMeetingroomName")
	@TableField("F_MEETINGROOM_NAME_")
	@ApiModelProperty(value="会议室名称")
	protected String FMeetingroomName; 
	
	@XmlAttribute(name = "FAppointmentBegTime")
	@TableField("F_APPOINTMENT_BEG_TIME_")
	@ApiModelProperty(value="预定开始时间")
	protected LocalDateTime FAppointmentBegTime; 
	
	@XmlAttribute(name = "FAppointmentEndTime")
	@TableField("F_APPOINTMENT_END_TIME_")
	@ApiModelProperty(value="预定结束时间")
	protected LocalDateTime FAppointmentEndTime; 
	
	@XmlAttribute(name = "FAppointmentStatus")
	@TableField("F_APPOINTMENT_STATUS_")
	@ApiModelProperty(value="预定状态")
	protected String FAppointmentStatus; 
	
	@XmlAttribute(name = "FMeetingserviceId")
	@TableField("F_MEETINGSERVICE_ID_")
	@ApiModelProperty(value="会议服务ID")
	protected String FMeetingserviceId; 
	
	@XmlAttribute(name = "FMeetingserviceFee")
	@TableField("F_MEETINGSERVICE_FEE_")
	@ApiModelProperty(value="会议服务费")
	protected Double FMeetingserviceFee; 
	
	@XmlAttribute(name = "FMeetingroomFee")
	@TableField("F_MEETINGROOM_FEE_")
	@ApiModelProperty(value="会议室费用")
	protected Double FMeetingroomFee; 
	
	@XmlAttribute(name = "FMeetingTotoalFee")
	@TableField("F_MEETING_TOTOAL_FEE")
	@ApiModelProperty(value="会议总费用")
	protected Double FMeetingTotoalFee; 
	
	@XmlAttribute(name = "FFormDataRev")
	@TableField("F_form_data_rev_")
	@ApiModelProperty(value="表单数据版本")
	protected Long FFormDataRev; 
	
	@XmlAttribute(name = "FMeetingMinute")
	@TableField("F_MEETING_MINUTE")
	@ApiModelProperty(value="会议时长")
	protected Double FMeetingMinute; 
	
	@XmlAttribute(name = "FMeetingRoomPrice")
	@TableField("F_MEETING_ROOM_PRICE")
	@ApiModelProperty(value="会议室单价")
	protected Double FMeetingRoomPrice; 
	
	
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
	public void setRefId(String refId) {
		this.refId = refId;
	}
	
	/**
	 * 返回 外键
	 * @return
	 */
	public String getRefId() {
		return this.refId;
	}
	public void setFMeetingroomId(String FMeetingroomId) {
		this.FMeetingroomId = FMeetingroomId;
	}
	
	/**
	 * 返回 会议室ID
	 * @return
	 */
	public String getFMeetingroomId() {
		return this.FMeetingroomId;
	}
	public void setFMeetingId(String FMeetingId) {
		this.FMeetingId = FMeetingId;
	}
	
	/**
	 * 返回 会议ID
	 * @return
	 */
	public String getFMeetingId() {
		return this.FMeetingId;
	}
	public void setFMeetingName(String FMeetingName) {
		this.FMeetingName = FMeetingName;
	}
	
	/**
	 * 返回 会议名称
	 * @return
	 */
	public String getFMeetingName() {
		return this.FMeetingName;
	}
	public void setFHostessId(String FHostessId) {
		this.FHostessId = FHostessId;
	}
	
	/**
	 * 返回 组织者ID
	 * @return
	 */
	public String getFHostessId() {
		return this.FHostessId;
	}
	public void setFHosstessName(String FHosstessName) {
		this.FHosstessName = FHosstessName;
	}
	
	/**
	 * 返回 组织者姓名
	 * @return
	 */
	public String getFHosstessName() {
		return this.FHosstessName;
	}
	public void setFHosstessPhoto(String FHosstessPhoto) {
		this.FHosstessPhoto = FHosstessPhoto;
	}
	
	/**
	 * 返回 组织者电话
	 * @return
	 */
	public String getFHosstessPhoto() {
		return this.FHosstessPhoto;
	}
	public void setFMeetingroomName(String FMeetingroomName) {
		this.FMeetingroomName = FMeetingroomName;
	}
	
	/**
	 * 返回 会议室名称
	 * @return
	 */
	public String getFMeetingroomName() {
		return this.FMeetingroomName;
	}
	public void setFAppointmentBegTime(LocalDateTime FAppointmentBegTime) {
		this.FAppointmentBegTime = FAppointmentBegTime;
	}
	
	/**
	 * 返回 预定开始时间
	 * @return
	 */
	public LocalDateTime getFAppointmentBegTime() {
		return this.FAppointmentBegTime;
	}
	public void setFAppointmentEndTime(LocalDateTime FAppointmentEndTime) {
		this.FAppointmentEndTime = FAppointmentEndTime;
	}
	
	/**
	 * 返回 预定结束时间
	 * @return
	 */
	public LocalDateTime getFAppointmentEndTime() {
		return this.FAppointmentEndTime;
	}
	public void setFAppointmentStatus(String FAppointmentStatus) {
		this.FAppointmentStatus = FAppointmentStatus;
	}
	
	/**
	 * 返回 预定状态
	 * @return
	 */
	public String getFAppointmentStatus() {
		return this.FAppointmentStatus;
	}
	public void setFMeetingserviceId(String FMeetingserviceId) {
		this.FMeetingserviceId = FMeetingserviceId;
	}
	
	/**
	 * 返回 会议服务ID
	 * @return
	 */
	public String getFMeetingserviceId() {
		return this.FMeetingserviceId;
	}
	public void setFMeetingserviceFee(Double FMeetingserviceFee) {
		this.FMeetingserviceFee = FMeetingserviceFee;
	}
	
	/**
	 * 返回 会议服务费
	 * @return
	 */
	public Double getFMeetingserviceFee() {
		return this.FMeetingserviceFee;
	}
	public void setFMeetingroomFee(Double FMeetingroomFee) {
		this.FMeetingroomFee = FMeetingroomFee;
	}
	
	/**
	 * 返回 会议室费用
	 * @return
	 */
	public Double getFMeetingroomFee() {
		return this.FMeetingroomFee;
	}
	public void setFMeetingTotoalFee(Double FMeetingTotoalFee) {
		this.FMeetingTotoalFee = FMeetingTotoalFee;
	}
	
	/**
	 * 返回 会议总费用
	 * @return
	 */
	public Double getFMeetingTotoalFee() {
		return this.FMeetingTotoalFee;
	}
	public void setFFormDataRev(Long FFormDataRev) {
		this.FFormDataRev = FFormDataRev;
	}
	
	/**
	 * 返回 表单数据版本
	 * @return
	 */
	public Long getFFormDataRev() {
		return this.FFormDataRev;
	}
	public void setFMeetingMinute(Double FMeetingMinute) {
		this.FMeetingMinute = FMeetingMinute;
	}
	
	/**
	 * 返回 会议时长
	 * @return
	 */
	public Double getFMeetingMinute() {
		return this.FMeetingMinute;
	}
	public void setFMeetingRoomPrice(Double FMeetingRoomPrice) {
		this.FMeetingRoomPrice = FMeetingRoomPrice;
	}
	
	/**
	 * 返回 会议室单价
	 * @return
	 */
	public Double getFMeetingRoomPrice() {
		return this.FMeetingRoomPrice;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("refId", this.refId) 
		.append("FMeetingroomId", this.FMeetingroomId) 
		.append("FMeetingId", this.FMeetingId) 
		.append("FMeetingName", this.FMeetingName) 
		.append("FHostessId", this.FHostessId) 
		.append("FHosstessName", this.FHosstessName) 
		.append("FHosstessPhoto", this.FHosstessPhoto) 
		.append("FMeetingroomName", this.FMeetingroomName) 
		.append("FAppointmentBegTime", this.FAppointmentBegTime) 
		.append("FAppointmentEndTime", this.FAppointmentEndTime) 
		.append("FAppointmentStatus", this.FAppointmentStatus) 
		.append("FMeetingserviceId", this.FMeetingserviceId) 
		.append("FMeetingserviceFee", this.FMeetingserviceFee) 
		.append("FMeetingroomFee", this.FMeetingroomFee) 
		.append("FMeetingTotoalFee", this.FMeetingTotoalFee) 
		.append("FFormDataRev", this.FFormDataRev) 
		.append("FMeetingMinute", this.FMeetingMinute) 
		.append("FMeetingRoomPrice", this.FMeetingRoomPrice) 
		.toString();
	}
}