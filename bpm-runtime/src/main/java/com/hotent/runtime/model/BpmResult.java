package com.hotent.runtime.model;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

import com.hotent.bpm.api.constant.DataType;

/**
 * 流程执行结果
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */

@ApiModel(value="流程执行结果")
public class BpmResult {

	@ApiModelProperty(name="businessKey",notes="业务主键")
	private String businessKey="";
	
	@ApiModelProperty(name="dataType",notes="业务主键类型")
	private DataType dataType=DataType.STRING;

	@ApiModelProperty(name="vars",notes="流程变量数据")
	private Map<String,Object> vars=new HashMap<String, Object>();

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public Map<String, Object> getVars() {
		return vars;
	}

	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}
	
	public void addVariable(String name,Object value){
		this.vars.put(name, value);
	}
	
	
	
}
