package com.hotent.runtime.params;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.bpm.model.form.FormModel;

/**
 * 流程实例表单数据vo
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程实例表单数据")
public class InstFormAndBoVo {

	@ApiModelProperty(name="result",notes="获取实例表单结果")
	private String result;
	
	@ApiModelProperty(name="form",notes="表单")
	private FormModel form;
	
	@ApiModelProperty(name="data",notes="bo数据")
	private ObjectNode data;
	
	@ApiModelProperty(name="opinionList",notes="表单中的意见数据")
	private ObjectNode opinionList;
	
	@ApiModelProperty(name="permission",notes="表单权限")
	private String permission;
	
	@ApiModelProperty(name="doneDataVersion",notes="查看已办数据版本（latest：最新版本，history：历史版本）")
	private String doneDataVersion;

	public FormModel getForm() {
		return form;
	}



	public void setForm(FormModel form) {
		this.form = form;
	}



	public ObjectNode getData() {
		return data;
	}



	public void setData(ObjectNode data) {
		this.data = data;
	}



	public ObjectNode getOpinionList() {
		return opinionList;
	}



	public void setOpinionList(ObjectNode opinionList) {
		this.opinionList = opinionList;
	}



	public String getPermission() {
		return permission;
	}



	public void setPermission(String permission) {
		this.permission = permission;
	}


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}



	public String getDoneDataVersion() {
		return doneDataVersion;
	}



	public void setDoneDataVersion(String doneDataVersion) {
		this.doneDataVersion = doneDataVersion;
	}

}
