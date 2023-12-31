package com.hotent.runtime.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.AutoFillModel;


 /**
 * 分类管理
 * <pre> 
 * 描述：分类管理 实体对象
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-15 18:35:27
 * 版权：广州宏天软件有限公司
 * </pre>
 */
 @TableName("bpm_message_type")
 @ApiModel(value = "MessageType",description = "分类管理") 
public class MessageType extends AutoFillModel<MessageType>{

	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@TableId("id_")
	@ApiModelProperty(value="ID_")
	protected String id; 
	
	@XmlAttribute(name = "classificationCode")
	@TableField("CLASSIFICATION_CODE_")
	@ApiModelProperty(value="分类名称")
	protected String classificationCode; 
	
	@XmlAttribute(name = "isPending")
	@TableField("IS_PENDING_")
	@ApiModelProperty(value="是否需要审批")
	protected Short isPending; 
	
	@XmlAttribute(name = "pendingUserId")
	@TableField("PENDING_USER_ID_")
	@ApiModelProperty(value="审批人id")
	protected String pendingUserId; 
	
	@XmlAttribute(name = "pendingUserName")
	@TableField("PENDING_USER_NAME_")
	@ApiModelProperty(value="审批人姓名")
	protected String pendingUserName; 
	
	
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
	
	public void setClassificationCode(String classificationCode) {
		this.classificationCode = classificationCode;
	}
	
	/**
	 * 返回 分类名称
	 * @return
	 */
	public String getClassificationCode() {
		return this.classificationCode;
	}
	
	

	
	public void setIsPending(Short isPending) {
		this.isPending = isPending;
	}
	
	/**
	 * 返回 是否需要审批
	 * @return
	 */
	public Short getIsPending() {
		return this.isPending;
	}
	
	public void setPendingUserId(String pendingUserId) {
		this.pendingUserId = pendingUserId;
	}
	
	/**
	 * 返回 审批人id
	 * @return
	 */
	public String getPendingUserId() {
		return this.pendingUserId;
	}
	
	public void setPendingUserName(String pendingUserName) {
		this.pendingUserName = pendingUserName;
	}
	
	/**
	 * 返回 审批人姓名
	 * @return
	 */
	public String getPendingUserName() {
		return this.pendingUserName;
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("classificationCode", this.classificationCode) 
		.append("isPending", this.isPending) 
		.append("pendingUserId", this.pendingUserId) 
		.append("pendingUserName", this.pendingUserName) 
		.toString();
	}
}