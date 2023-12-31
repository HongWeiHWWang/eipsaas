package com.hotent.bpm.persistence.model;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


 /**
 * 秘书管理表
 * <pre> 
 * 描述：秘书管理表 实体对象
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2019-09-16 10:07:13
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@TableName("bpm_secretary_manage")
 @ApiModel(value = "BpmSecretaryManage",description = "秘书管理表") 
public class BpmSecretaryManage extends BaseModel<BpmSecretaryManage>{

	private static final long serialVersionUID = 1L;
	
	public static final String RIGHT_START = "1";
	
	public static final String RIGHT_TASK = "2";
	
	@TableId("id_")
	@ApiModelProperty(value="ID_")
	protected String id; 

    @TableField("leader_id_")
	@ApiModelProperty(value="领导id")
	protected String leaderId; 

    @TableField("leader_name_")
	@ApiModelProperty(value="领导姓名")
	protected String leaderName; 

    @TableField("secretary_id_")
	@ApiModelProperty(value="秘书id")
	protected String secretaryId; 

    @TableField("secretary_name_")
	@ApiModelProperty(value="秘书姓名")
	protected String secretaryName; 

    @TableField("share_type_")
	@ApiModelProperty(value="共享类型，1流程分类。2，流程定义")
	protected String shareType; 

    @TableField("share_right_")
	@ApiModelProperty(value="共享权限，1流程发起。2，任务审批")
	protected String shareRight; 

    @TableField("share_key_")
	@ApiModelProperty(value="共享的key")
	protected String shareKey; 

    @TableField("share_name_")
	@ApiModelProperty(value="共享对象的名称")
	protected String shareName; 

    @TableField("enabled_")
	@ApiModelProperty(value="是否有效1.有效。0无效")
	protected String enabled; 
	
	
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
	
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	
	/**
	 * 返回 领导id
	 * @return
	 */
	public String getLeaderId() {
		return this.leaderId;
	}
	
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	
	/**
	 * 返回 领导姓名
	 * @return
	 */
	public String getLeaderName() {
		return this.leaderName;
	}
	
	public void setSecretaryId(String secretaryId) {
		this.secretaryId = secretaryId;
	}
	
	/**
	 * 返回 秘书id
	 * @return
	 */
	public String getSecretaryId() {
		return this.secretaryId;
	}
	
	public void setSecretaryName(String secretaryName) {
		this.secretaryName = secretaryName;
	}
	
	/**
	 * 返回 秘书姓名
	 * @return
	 */
	public String getSecretaryName() {
		return this.secretaryName;
	}
	
	public void setShareType(String shareType) {
		this.shareType = shareType;
	}
	
	/**
	 * 返回 共享类型，1流程分类。2，流程定义
	 * @return
	 */
	public String getShareType() {
		return this.shareType;
	}
	
	public void setShareKey(String shareKey) {
		this.shareKey = shareKey;
	}
	
	/**
	 * 返回 共享的key
	 * @return
	 */
	public String getShareKey() {
		return this.shareKey;
	}
	
	public void setShareName(String shareName) {
		this.shareName = shareName;
	}
	
	/**
	 * 返回 共享对象的名称
	 * @return
	 */
	public String getShareName() {
		return this.shareName;
	}
	
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * 返回 是否有效
	 * @return
	 */
	public String getEnabled() {
		return this.enabled;
	}
	
	
	public String getShareRight() {
		return shareRight;
	}

	public void setShareRight(String shareRight) {
		this.shareRight = shareRight;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("leaderId", this.leaderId) 
		.append("leaderName", this.leaderName) 
		.append("secretaryId", this.secretaryId) 
		.append("secretaryName", this.secretaryName) 
		.append("shareType", this.shareType) 
		.append("shareKey", this.shareKey) 
		.append("shareName", this.shareName) 
		.append("enabled", this.enabled) 
		.toString();
	}
}