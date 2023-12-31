package com.hotent.runtime.params;



import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.model.form.FormModel;

/**
 * 测试用例设置基本信息vo
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="表单数据")
public class FormAndBoVo {

	@ApiModelProperty(name="resultMsg",notes="获取结果")
	private String resultMsg;
	
	@ApiModelProperty(name="form",notes="表单")
	private FormModel form;
	
	@ApiModelProperty(name="data",notes="bo数据")
	private ObjectNode data;
	
	@ApiModelProperty(name="opinionList",notes="表单中的意见数据")
	private List<Button> buttons;
	
	@ApiModelProperty(name="permission",notes="表单权限")
	private String permission;
	
	@ApiModelProperty(name="jumpType",notes="跳转类型(common,free,select)")
	private String jumpType;
	
	@ApiModelProperty(name="urgentStateRight",notes="紧急状态权限(w:编辑,r:只读,n:隐藏)")
	private String urgentStateRight;
	
	
	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
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



	public String getJumpType() {
		return jumpType;
	}



	public void setJumpType(String jumpType) {
		this.jumpType = jumpType;
	}

	public String getUrgentStateRight() {
		return urgentStateRight;
	}

	public void setUrgentStateRight(String urgentStateRight) {
		this.urgentStateRight = urgentStateRight;
	}
	
	

}
