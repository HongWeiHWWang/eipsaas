package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 并行审批任务撤销参数对象
 * 
 * @company 广州宏天软件股份有限公司
 * @author jason
 * @email liygui@jee-soft.cn
 * @date 2019年10月9日
 */
@ApiModel(value="撤销流程参数对象")
public class RevokeSignLineParamObject extends RevokeTransParamObject {
	
	@ApiModelProperty(name="inApprovalTaskIds",notes="撤回审批中的待办id",required=false)
	private String inApprovalTaskIds;

	public String getInApprovalTaskIds() {
		return inApprovalTaskIds;
	}

	public void setInApprovalTaskIds(String inApprovalTaskIds) {
		this.inApprovalTaskIds = inApprovalTaskIds;
	}
	
}
