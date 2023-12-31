package com.hotent.runtime.params;



import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.model.form.FormModel;

/**
 * 任务的详情表单数据
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="任务的详情表单数据")
public class TaskDetailVo {

	@ApiModelProperty(name="result",notes="获取结果")
	private Object result;
	
	@ApiModelProperty(name="form",notes="表单")
	private FormModel form;
	
	@ApiModelProperty(name="data",notes="bo数据")
	private ObjectNode data;
	
	@ApiModelProperty(name="opinionList",notes="表单中的意见数据")
	private ObjectNode opinionList;
	
	@ApiModelProperty(name="permission",notes="表单权限")
	private String permission;
	
	@ApiModelProperty(name="buttons",notes="处理按钮")
	private List<Button> buttons;
	

	public Object getResult() {
		return result;
	}



	public void setResult(Object result) {
		this.result = result;
	}



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



	public List<Button> getButtons() {
		return buttons;
	}



	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}
	

}
