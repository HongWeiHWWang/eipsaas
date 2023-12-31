package com.hotent.runtime.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;


 /**
 * 会议室
 * <pre> 
 * 描述：会议室 实体对象
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-10 15:01:37
 * 版权：广州宏天软件有限公司
 * </pre>
 */
 @TableName("bpm_meetingroom")
 @ApiModel(value = "MeetingRoom",description = "会议室") 
public class MeetingRoom extends BaseModel<MeetingRoom>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="ID_")
	protected String id; 
	
	@XmlAttribute(name = "name")
	@TableField("NAME_")
	@ApiModelProperty(value="会议室名称")
	protected String name; 
	
	@XmlAttribute(name = "needPending")
	@TableField("NEED_PENDING_")
	@ApiModelProperty(value="是否需要审批")
	protected Short needPending; 
	
	@XmlAttribute(name = "pendingUserId")
	@TableField("PENDING_USER_ID_")
	@ApiModelProperty(value="审批人ID")
	protected String pendingUserId; 
	
	@XmlAttribute(name = "pendingUserName")
	@TableField("PENDING_USER_NAME_")
	@ApiModelProperty(value="审批人")
	protected String pendingUserName; 
	
	@XmlAttribute(name = "supportService")
	@TableField("SUPPORT_SERVICE_")
	@ApiModelProperty(value="支持的服务")
	protected String supportService; 
	
	@XmlAttribute(name = "location")
	@TableField("LOCATION_")
	@ApiModelProperty(value="会议室地址")
	protected String location; 
	
	@XmlAttribute(name = "capacity")
	@TableField("CAPACITY_")
	@ApiModelProperty(value="会议室容量")
	protected Integer capacity; 
	
	@XmlAttribute(name = "memo")
	@TableField("MEMO_")
	@ApiModelProperty(value="会议室备注")
	protected String memo; 
	
	
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
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 返回 会议室名称
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	public void setNeedPending(Short needPending) {
		this.needPending = needPending;
	}
	
	/**
	 * 返回 是否需要审批
	 * @return
	 */
	public Short getNeedPending() {
		return this.needPending;
	}
	
	public void setPendingUserId(String pendingUserId) {
		this.pendingUserId = pendingUserId;
	}
	
	/**
	 * 返回 审批人ID
	 * @return
	 */
	public String getPendingUserId() {
		return this.pendingUserId;
	}
	
	public void setPendingUserName(String pendingUserName) {
		this.pendingUserName = pendingUserName;
	}
	
	/**
	 * 返回 审批人
	 * @return
	 */
	public String getPendingUserName() {
		return this.pendingUserName;
	}
	
	public void setSupportService(String supportService) {
		this.supportService = supportService;
	}
	
	/**
	 * 返回 支持的服务
	 * @return
	 */
	public String getSupportService() {
		return this.supportService;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * 返回 会议室地址
	 * @return
	 */
	public String getLocation() {
		return this.location;
	}
	
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * 返回 会议室容量
	 * @return
	 */
	public Integer getCapacity() {
		return this.capacity;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	/**
	 * 返回 会议室备注
	 * @return
	 */
	public String getMemo() {
		return this.memo;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("needPending", this.needPending) 
		.append("pendingUserId", this.pendingUserId) 
		.append("pendingUserName", this.pendingUserName) 
		.append("supportService", this.supportService) 
		.append("location", this.location) 
		.append("capacity", this.capacity) 
		.append("memo", this.memo) 
		.toString();
	}
}