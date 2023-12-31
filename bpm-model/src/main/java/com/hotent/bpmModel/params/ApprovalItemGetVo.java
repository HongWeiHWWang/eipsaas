package com.hotent.bpmModel.params;

import com.hotent.base.query.QueryFilter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程常用语请求对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "流程常用语请求对象")
public class ApprovalItemGetVo {

	@ApiModelProperty(name = "isPersonal", notes = "是否个人", required = true)
	protected Boolean isPersonal;

	@ApiModelProperty(name = "queryFilter", notes = "通用查询对象", required = true)
	protected QueryFilter queryFilter;

	public Boolean getIsPersonal() {
		return isPersonal;
	}

	public void setIsPersonal(Boolean isPersonal) {
		this.isPersonal = isPersonal;
	}

	public QueryFilter getQueryFilter() {
		return queryFilter;
	}

	public void setQueryFilter(QueryFilter queryFilter) {
		this.queryFilter = queryFilter;
	}

}