package com.hotent.service.model;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotent.base.entity.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 服务调用设置
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
@ApiModel(description="服务调用设置")
@TableName("portal_service_set")
public class ServiceSet extends BaseModel<ServiceSet>{
	private static final long serialVersionUID = 441926964799734935L;
	
	@ApiModelProperty(name="id", notes="主键")
	@TableId("id_")
	protected String id;
	
	@ApiModelProperty(name="alias", notes="别名", required=true)
	@TableField("alias_")
	protected String alias;
	
	@ApiModelProperty(name="name", notes="名称", required=true)
	@TableField("name_")
	protected String name;
	
	@ApiModelProperty(name="address", notes="服务调用地址", required=true)
	@TableField("address_")
	protected String address;
	
	@ApiModelProperty(name="wsdlUrl", notes="WebService的WSDL地址")
	@TableField("url_")
	protected String wsdlUrl;
	
	@ApiModelProperty(name="methodName", notes="WebService的调用方法")
	@TableField("method_name_")
	protected String methodName;
	
	@ApiModelProperty(name="namespace", notes="WebService的名称空间")
	@TableField("namespace_")
	protected String namespace;
	
	@ApiModelProperty(name="soapAction", notes="WebService的请求模式")
	@TableField("soap_action_")
	protected char soapAction;
	
	@ApiModelProperty(name="inputSet", notes="入参设定")
	@TableField("input_set_")
	protected String inputSet;
	
	@ApiModelProperty(name="serviceParamList", notes="参数列表", hidden = true)
	@TableField(exist=false)
	protected List<ServiceParam> serviceParamList=new ArrayList<ServiceParam>();
	
	public void setId(String id) 
	{
		this.id = id;
	}
	/**
	 * 返回 主键
	 * @return
	 */
	public String getId() 
	{
		return this.id;
	}
	public void setAlias(String alias) 
	{
		this.alias = alias;
	}
	/**
	 * 返回 别名
	 * @return
	 */
	public String getAlias() 
	{
		return this.alias;
	}
	public void setAddress(String address) 
	{
		this.address = address;
	}
	/**
	 * 返回 接口调用地址
	 * @return
	 */
	public String getAddress() 
	{
		return this.address;
	}
	public void setMethodName(String methodName) 
	{
		this.methodName = methodName;
	}
	/**
	 * 返回 调用的方法名称
	 * @return
	 */
	public String getMethodName() 
	{
		return this.methodName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWsdlUrl() {
		return wsdlUrl;
	}
	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}
	public void setNamespace(String namespace) 
	{
		this.namespace = namespace;
	}
	/**
	 * 返回 名称空间
	 * @return
	 */
	public String getNamespace() 
	{
		return this.namespace;
	}
	public void setSoapAction(char soapAction) 
	{
		this.soapAction = soapAction;
	}
	/**
	 * 返回 构建soap的模式
	 * @return
	 */
	public char getSoapAction() 
	{
		return this.soapAction;
	}
	public void setInputSet(String inputSet) 
	{
		this.inputSet = inputSet;
	}
	/**
	 * 返回 输入参数设定
	 * @return
	 */
	public String getInputSet() 
	{
		return this.inputSet;
	}
	public void setServiceParamList(List<ServiceParam> sysServiceParamList) 
	{
		this.serviceParamList = sysServiceParamList;
	}
	/**
	 * 返回 通用服务调用参数表列表
	 * @return
	 */
	public List<ServiceParam> getServiceParamList() 
	{
		return this.serviceParamList;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("alias", this.alias) 
		.append("wsdlUrl", this.wsdlUrl) 
		.append("address", this.address) 
		.append("methodName", this.methodName) 
		.append("namespace", this.namespace) 
		.append("soapAction", this.soapAction) 
		.append("inputSet", this.inputSet) 
		.toString();
	}
}