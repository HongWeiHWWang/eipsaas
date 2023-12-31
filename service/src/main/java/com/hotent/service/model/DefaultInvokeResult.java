package com.hotent.service.model;

import java.io.Serializable;
import java.util.List;

import com.hotent.base.service.InvokeResult;
import com.hotent.base.util.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 调用服务的返回值对象
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
@ApiModel(description="调用服务的返回值对象")
public class DefaultInvokeResult implements InvokeResult, Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name="obj", notes="单个对象的返回值")
	private Object obj;
	@ApiModelProperty(name="list", notes="列表类型的返回值")
	private List<Object> list;
	@ApiModelProperty(name="ex", notes="调用服务时出现异常")
	private Exception ex;
	@ApiModelProperty(name="json", notes="以json格式返回结果")
	private String json;

	public Object getObject() {
		return obj;
	}

	public void setObject(Object obj) {
		this.obj = obj;
	}
	
	public void setList(List<Object> list) {
		this.list = list;
	}
	
	public List<Object> getList(){
		return list;
	}

	public Exception getException() {
		return ex;
	}

	public void setException(Exception ex) {
		this.ex = ex;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Boolean isVoid() {
		return BeanUtils.isEmpty(obj)&&BeanUtils.isEmpty(list);
	}
	
	public Boolean isList(){
		return BeanUtils.isNotEmpty(list);
	}
	
	public Boolean isFault(){
		return BeanUtils.isNotEmpty(ex);
	}
}
