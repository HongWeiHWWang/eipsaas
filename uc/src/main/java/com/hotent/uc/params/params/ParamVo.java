package com.hotent.uc.params.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.uc.manager.ParamsManager;
import com.hotent.uc.model.Params;
import com.hotent.uc.util.OperateLogUtil;
import com.hotent.uc.util.UpdateCompare;

/**
 * 用户或组织参数
 * @author zhangxw
 *
 */
@ApiModel
public class ParamVo  implements UpdateCompare{

	@ApiModelProperty(name="name",notes="参数名称",required=true)
	private String name;
	
	@ApiModelProperty(name="code",notes="参数别名",required=true)
	private String code;
	
	@ApiModelProperty(name="type",notes="参数类型 1：用户参数 2：组织参数 3：租户分类参数",required=true)
	private String type;
	
	@ApiModelProperty(name="ctrType",notes="参数控件类型：input：手动输入； select：下拉框； checkbox：复选框；  radio：单选按钮；  date：日期；  number：数字；",required=true)
	private String ctrType;
	
	@ApiModelProperty(name="json",notes="参数数据（为ArrayNode格式）",required=true)
	private JsonNode json;
	
	@ApiModelProperty(name="tenantTypeId",notes="租户类型id（当type为3时需传入值）",required=true)
	private String tenantTypeId;


	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getCtrType() {
		return ctrType;
	}



	public void setCtrType(String ctrType) {
		this.ctrType = ctrType;
	}

	public JsonNode getJson() {
		return json;
	}

	public void setJson(JsonNode json) {
		this.json = json;
	}

	public String getTenantTypeId() {
		return tenantTypeId;
	}



	public void setTenantTypeId(String tenantTypeId) {
		this.tenantTypeId = tenantTypeId;
	}



	public static Params parse(ParamVo paramVo) throws IOException{
		Params param = new Params();
		param.setCode(paramVo.getCode());
		param.setName(paramVo.getName());
		param.setType(paramVo.getType());
		param.setCtlType(paramVo.getCtrType());
		param.setJson(BeanUtils.isEmpty(paramVo.getJson())?"":JsonUtil.toJson(paramVo.getJson()));
		param.setTenantTypeId(paramVo.getTenantTypeId());
		return param;
	}

	@Override
	public String toString() {
		return "{"
				+ "\""+"name"+"\""+":"+"\""+this.name+"\","
				+"\""+"code"+"\""+":"+"\""+this.code+"\""
				+"\""+"type"+"\""+":"+"\""+this.type+"\""
				+"\""+"ctrType"+"\""+":"+"\""+this.ctrType+"\""
				+"\""+"json"+"\""+":"+"\""+this.json+"\""
				+"\""+"tenantTypeId"+"\""+":"+"\""+this.tenantTypeId+"\""
				+ "}";
	}
	
	@Override
	public String compare() throws Exception {
	    ParamsManager service =	AppUtil.getBean(ParamsManager.class);
	    Params oldVo=service.getByAlias(this.code);
		return OperateLogUtil.compare(this,changeVo(oldVo));
	}


	public ParamVo changeVo(Params oldVo) throws IOException {
		ParamVo newVo=new ParamVo();
		if (BeanUtils.isEmpty(newVo)) return newVo;
		newVo.setCode(oldVo.getCode());
		newVo.setCtrType(oldVo.getCtlType());
		newVo.setJson(JsonUtil.toJsonNode(oldVo.getJson()));
		newVo.setName(oldVo.getName());
		newVo.setType(oldVo.getType());
		newVo.setTenantTypeId(oldVo.getTenantTypeId());
		return newVo;
	}
	
}
