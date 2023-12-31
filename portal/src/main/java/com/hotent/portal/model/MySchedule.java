package com.hotent.portal.model;
import java.time.LocalDate;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 行程管理
 * <pre> 
 * 描述：行程管理 实体对象
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2018-09-10 09:51:04
 * 版权：广州宏天软件有限公司
 * </pre>
 */
 @ApiModel(value = "MySchedule",description = "行程管理") 
 @TableName("portal_my_schedule")
public class MySchedule extends BaseModel<MySchedule>{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="主键")
	@TableId("ID_")
	protected String id; 
	
	@ApiModelProperty(value="用户id")
	@TableField("USER_ID_")
	protected String userId; 
	
	@ApiModelProperty(value="日程日期")
	@TableField("DATE_")
	protected LocalDate date; 
	
	@ApiModelProperty(value="日程开始时间")
	@TableField("START_TIEM_")
	protected String startTiem; 
	
	@ApiModelProperty(value="日程结束时间")
	@TableField("END_TIME_")
	protected String endTime; 
	
	@ApiModelProperty(value="日程名称")
	@TableField("NAME_")
	protected String name; 
	
	@ApiModelProperty(value="日程说明")
	@TableField("NOTE_")
	protected String note; 
	
	
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
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 返回 用户id
	 * @return
	 */
	public String getUserId() {
		return this.userId;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	/**
	 * 返回 日程日期
	 * @return
	 */
	public LocalDate getDate() {
		return this.date;
	}
	
	public void setStartTiem(String startTiem) {
		this.startTiem = startTiem;
	}
	
	/**
	 * 返回 日程开始时间
	 * @return
	 */
	public String getStartTiem() {
		return this.startTiem;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * 返回 日程结束时间
	 * @return
	 */
	public String getEndTime() {
		return this.endTime;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 日程名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	/**
	 * 返回 日程说明
	 * @return
	 */
	public String getNote() {
		return this.note;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("userId", this.userId) 
		.append("date", this.date) 
		.append("startTiem", this.startTiem) 
		.append("endTime", this.endTime) 
		.append("name", this.name) 
		.append("note", this.note) 
		.toString();
	}
}