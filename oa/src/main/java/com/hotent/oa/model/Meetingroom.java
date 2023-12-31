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



 /**
 * 会议室
 * <pre> 
 * 描述：会议室 实体对象
 * 构建组：x7
 * 作者:David
 * 邮箱:376514860@qq.com
 * 日期:2023-12-24 01:13:24
 * 版权：wijo
 * </pre>
 */
 @TableName("w_meetingroom")
 @ApiModel(value = "Meetingroom",description = "会议室") 
public class Meetingroom extends BaseModel<Meetingroom>{

	private static final long serialVersionUID = 1L;
	@XmlTransient
	@TableId("ID_")
	@ApiModelProperty(value="主键")
	protected String id; 
	
	@XmlAttribute(name = "refId")
	@TableField("REF_ID_")
	@ApiModelProperty(value="外键")
	protected String refId; 
	
	@XmlAttribute(name = "FName")
	@TableField("F_NAME_")
	@ApiModelProperty(value="会议名称")
	protected String FName; 
	
	@XmlAttribute(name = "FNeedPending")
	@TableField("F_NEED_PENDING_")
	@ApiModelProperty(value="是否需要审批")
	protected Integer FNeedPending; 
	
	@XmlAttribute(name = "FPendingUserId")
	@TableField("F_PENDING_USER_ID_")
	@ApiModelProperty(value="会议审批人ID")
	protected String FPendingUserId; 
	
	@XmlAttribute(name = "FPendingUserName")
	@TableField("F_PENDING_USER_NAME_")
	@ApiModelProperty(value="会议审批人Name")
	protected String FPendingUserName; 
	
	@XmlAttribute(name = "FLocation")
	@TableField("F_LOCATION_")
	@ApiModelProperty(value="会议地址")
	protected String FLocation; 
	
	@XmlAttribute(name = "FCapacity")
	@TableField("F_CAPACITY_")
	@ApiModelProperty(value="会议室容量")
	protected Integer FCapacity; 
	
	@XmlAttribute(name = "FMemo")
	@TableField("F_MEMO_")
	@ApiModelProperty(value="备忘录")
	protected String FMemo; 
	
	@XmlAttribute(name = "FContact")
	@TableField("F_CONTACT")
	@ApiModelProperty(value="联系人")
	protected String FContact; 
	
	@XmlAttribute(name = "FContactName")
	@TableField("F_CONTACT_NAME_")
	@ApiModelProperty(value="联系人姓名")
	protected String FContactName; 
	
	@XmlAttribute(name = "FContactPhoto")
	@TableField("F_CONTACT_PHOTO_")
	@ApiModelProperty(value="联系人电话")
	protected Long FContactPhoto; 
	
	@XmlAttribute(name = "FArea")
	@TableField("F_AREA_")
	@ApiModelProperty(value="面积")
	protected Double FArea; 
	
	@XmlAttribute(name = "FProjector")
	@TableField("F_PROJECTOR_")
	@ApiModelProperty(value="是否有投影仪")
	protected String FProjector; 
	
	@XmlAttribute(name = "FComputer")
	@TableField("F_COMPUTER_")
	@ApiModelProperty(value="是否有电脑")
	protected String FComputer; 
	
	@XmlAttribute(name = "FStatus")
	@TableField("F_STATUS_")
	@ApiModelProperty(value="状态")
	protected Integer FStatus; 
	
	@XmlAttribute(name = "FPrice")
	@TableField("F_PRICE")
	@ApiModelProperty(value="单价")
	protected Double FPrice; 
	
	@XmlAttribute(name = "FFormDataRev")
	@TableField("F_form_data_rev_")
	@ApiModelProperty(value="表单数据版本")
	protected Long FFormDataRev; 
	
	
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
	public void setFName(String FName) {
		this.FName = FName;
	}
	
	/**
	 * 返回 会议名称
	 * @return
	 */
	public String getFName() {
		return this.FName;
	}
	public void setFNeedPending(Integer FNeedPending) {
		this.FNeedPending = FNeedPending;
	}
	
	/**
	 * 返回 是否需要审批
	 * @return
	 */
	public Integer getFNeedPending() {
		return this.FNeedPending;
	}
	public void setFPendingUserId(String FPendingUserId) {
		this.FPendingUserId = FPendingUserId;
	}
	
	/**
	 * 返回 会议审批人ID
	 * @return
	 */
	public String getFPendingUserId() {
		return this.FPendingUserId;
	}
	public void setFPendingUserName(String FPendingUserName) {
		this.FPendingUserName = FPendingUserName;
	}
	
	/**
	 * 返回 会议审批人Name
	 * @return
	 */
	public String getFPendingUserName() {
		return this.FPendingUserName;
	}
	public void setFLocation(String FLocation) {
		this.FLocation = FLocation;
	}
	
	/**
	 * 返回 会议地址
	 * @return
	 */
	public String getFLocation() {
		return this.FLocation;
	}
	public void setFCapacity(Integer FCapacity) {
		this.FCapacity = FCapacity;
	}
	
	/**
	 * 返回 会议室容量
	 * @return
	 */
	public Integer getFCapacity() {
		return this.FCapacity;
	}
	public void setFMemo(String FMemo) {
		this.FMemo = FMemo;
	}
	
	/**
	 * 返回 备忘录
	 * @return
	 */
	public String getFMemo() {
		return this.FMemo;
	}
	public void setFContact(String FContact) {
		this.FContact = FContact;
	}
	
	/**
	 * 返回 联系人
	 * @return
	 */
	public String getFContact() {
		return this.FContact;
	}
	public void setFContactName(String FContactName) {
		this.FContactName = FContactName;
	}
	
	/**
	 * 返回 联系人姓名
	 * @return
	 */
	public String getFContactName() {
		return this.FContactName;
	}
	public void setFContactPhoto(Long FContactPhoto) {
		this.FContactPhoto = FContactPhoto;
	}
	
	/**
	 * 返回 联系人电话
	 * @return
	 */
	public Long getFContactPhoto() {
		return this.FContactPhoto;
	}
	public void setFArea(Double FArea) {
		this.FArea = FArea;
	}
	
	/**
	 * 返回 面积
	 * @return
	 */
	public Double getFArea() {
		return this.FArea;
	}
	public void setFProjector(String FProjector) {
		this.FProjector = FProjector;
	}
	
	/**
	 * 返回 是否有投影仪
	 * @return
	 */
	public String getFProjector() {
		return this.FProjector;
	}
	public void setFComputer(String FComputer) {
		this.FComputer = FComputer;
	}
	
	/**
	 * 返回 是否有电脑
	 * @return
	 */
	public String getFComputer() {
		return this.FComputer;
	}
	public void setFStatus(Integer FStatus) {
		this.FStatus = FStatus;
	}
	
	/**
	 * 返回 状态
	 * @return
	 */
	public Integer getFStatus() {
		return this.FStatus;
	}
	public void setFPrice(Double FPrice) {
		this.FPrice = FPrice;
	}
	
	/**
	 * 返回 单价
	 * @return
	 */
	public Double getFPrice() {
		return this.FPrice;
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
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("refId", this.refId) 
		.append("FName", this.FName) 
		.append("FNeedPending", this.FNeedPending) 
		.append("FPendingUserId", this.FPendingUserId) 
		.append("FPendingUserName", this.FPendingUserName) 
		.append("FLocation", this.FLocation) 
		.append("FCapacity", this.FCapacity) 
		.append("FMemo", this.FMemo) 
		.append("FContact", this.FContact) 
		.append("FContactName", this.FContactName) 
		.append("FContactPhoto", this.FContactPhoto) 
		.append("FArea", this.FArea) 
		.append("FProjector", this.FProjector) 
		.append("FComputer", this.FComputer) 
		.append("FStatus", this.FStatus) 
		.append("FPrice", this.FPrice) 
		.append("FFormDataRev", this.FFormDataRev) 
		.toString();
	}
}