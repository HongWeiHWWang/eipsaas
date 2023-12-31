package com.hotent.bpm.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

/**
 * 带自动填充字段的基础实体类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月8日
 */
public abstract class BpmAutoFillModel<T extends BpmAutoFillModel<?>> extends BaseModel<T>{
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@ApiModelProperty(value = "创建人ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_by_", fill=FieldFill.INSERT, select=false)
	private String createBy;

	@JsonIgnore
	@ApiModelProperty(value = "创建人组织ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_org_id_", fill=FieldFill.INSERT, select=false)
	private String createOrgId;

	@ApiModelProperty(value = "创建时间", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="create_time_", fill=FieldFill.INSERT, select=true)
	private Date createTime;
	
	@JsonIgnore
	@ApiModelProperty(value = "更新人ID", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="update_by_", fill=FieldFill.UPDATE, select=false)
	private String updateBy;

	@ApiModelProperty(value = "更新时间", hidden=true, accessMode=AccessMode.READ_ONLY)
	@TableField(value="update_time_", fill=FieldFill.UPDATE, select=true)
	private Date updateTime;

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return "BaseModel{" +
				", createBy=" + createBy +
				", createOrgId=" + createOrgId +
				", createTime=" + createTime +
				", updateBy=" + updateBy +
				", updateTime=" + updateTime +
				"}";
	}
}
