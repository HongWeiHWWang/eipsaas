package com.hotent.portal.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程定义权限明细保存对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "流程定义权限明细保存对象")
public class SaveRightsVo {

	@ApiModelProperty(name = "id", notes = "id", required = true)
	protected String id;

	@ApiModelProperty(name = "objType", notes = "objType", required = true)
	protected String objType;

	@ApiModelProperty(name = "ownerNameJson", notes = "ownerNameJson")
	protected String ownerNameJson;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getOwnerNameJson() {
		return ownerNameJson;
	}

	public void setOwnerNameJson(String ownerNameJson) {
		this.ownerNameJson = ownerNameJson;
	}

}