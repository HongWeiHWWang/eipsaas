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
 * 会议室预约
 * <pre> 
 * 描述：会议室预约 实体对象
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-10 15:11:20
 * 版权：广州宏天软件有限公司
 * </pre>
 */
 @TableName("bpm_meetingroom_appointment")
 @ApiModel(value = "MeetingRoomAppointment",description = "会议室预约") 
public class MeetingRoomAppointment extends BaseModel<MeetingRoomAppointment>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="ID_")
	protected String id; 
	
	@XmlAttribute(name = "meetingroomId")
	@TableField("MEETINGROOM_ID_")
	@ApiModelProperty(value="会议室id")
	protected String meetingroomId; 
	
	@XmlAttribute(name = "meetingId")
	@TableField("MEETING_ID_")
	@ApiModelProperty(value="会议id")
	protected String meetingId; 
	
	@XmlAttribute(name = "meetingName")
	@TableField("MEETING_NAME_")
	@ApiModelProperty(value="会议名称")
	protected String meetingName; 
	
	@XmlAttribute(name = "hostessName")
	@TableField("HOSTESS_NAME_")
	@ApiModelProperty(value="主持人姓名")
	protected String hostessName; 
	
	@XmlAttribute(name = "appointmentBegTime")
	@TableField("APPOINTMENT_BEG_TIME_")
	@ApiModelProperty(value="预约开始时间")
	protected LocalDateTime appointmentBegTime; 
	
	@XmlAttribute(name = "appointmentEndTime")
	@TableField("APPOINTMENT_END_TIME_")
	@ApiModelProperty(value="预约结束时间")
	protected LocalDateTime appointmentEndTime; 
	
	@XmlAttribute(name = "appointmentStatus")
	@TableField("APPOINTMENT_STATUS_")
	@ApiModelProperty(value="预约状态")
	protected Integer appointmentStatus; 
	
	
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
	
	public void setMeetingroomId(String meetingroomId) {
		this.meetingroomId = meetingroomId;
	}
	
	/**
	 * 返回 会议室id
	 * @return
	 */
	public String getMeetingroomId() {
		return this.meetingroomId;
	}
	
	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}
	
	/**
	 * 返回 会议id
	 * @return
	 */
	public String getMeetingId() {
		return this.meetingId;
	}
	
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	
	/**
	 * 返回 会议名称
	 * @return
	 */
	public String getMeetingName() {
		return this.meetingName;
	}
	
	public void setHostessName(String hostessName) {
		this.hostessName = hostessName;
	}
	
	/**
	 * 返回 主持人姓名
	 * @return
	 */
	public String getHostessName() {
		return this.hostessName;
	}
	
	public void setAppointmentBegTime(LocalDateTime appointmentBegTime) {
		this.appointmentBegTime = appointmentBegTime;
	}
	
	/**
	 * 返回 预约开始时间
	 * @return
	 */
	public LocalDateTime getAppointmentBegTime() {
		return this.appointmentBegTime;
	}
	
	public void setAppointmentEndTime(LocalDateTime appointmentEndTime) {
		this.appointmentEndTime = appointmentEndTime;
	}
	
	/**
	 * 返回 预约结束时间
	 * @return
	 */
	public LocalDateTime getAppointmentEndTime() {
		return this.appointmentEndTime;
	}
	
	public void setAppointmentStatus(Integer appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}
	
	/**
	 * 返回 预约状态
	 * @return
	 */
	public Integer getAppointmentStatus() {
		return this.appointmentStatus;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("meetingroomId", this.meetingroomId) 
		.append("meetingId", this.meetingId) 
		.append("meetingName", this.meetingName) 
		.append("hostessName", this.hostessName) 
		.append("appointmentBegTime", this.appointmentBegTime) 
		.append("appointmentEndTime", this.appointmentEndTime) 
		.append("appointmentStatus", this.appointmentStatus) 
		.toString();
	}
}