package com.hotent.uc.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import com.hotent.base.model.BaseModel;


/**
 * 基础实体类
 * @author Administrator
 *
 */
public abstract class UcBaseModel  extends BaseModel<String> implements Serializable{
	private static final long serialVersionUID = 3796984803158565007L;
	
	/**
	 * 是否已删除 0：未删除 1：已删除
	 */
	@ApiModelProperty(name="isDelete",notes="是否已删除 0：未删除 1：已删除（新增、更新数据时不需要传入）")
	protected String isDelete = "0";
	
	/**
	 * 版本号
	 */
	@ApiModelProperty(name="version",notes="版本号（新增、更新数据时不需要传入）")
	protected Integer version;

	

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
