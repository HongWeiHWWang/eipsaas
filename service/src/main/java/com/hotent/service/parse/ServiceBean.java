package com.hotent.service.parse;

import java.io.Serializable;

import com.hotent.service.ws.model.SoapService;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 服务信息
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
@ApiModel(description="服务信息")
public class ServiceBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(name="url", notes="服务地址")
	private String url;
	@ApiModelProperty(name="type", notes="服务类型")
	private final String type = "webservice";
	@ApiModelProperty(name="name", notes="服务名称")
	private String name;
	@ApiModelProperty(name="description", notes="服务描述")
	private String description;
	@ApiModelProperty(name="namespace", notes="名称空间")
	private String namespace;
	@ApiModelProperty(name="soapService", notes="soap服务的详情")
	private SoapService soapService;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public SoapService getSoapService() {
		return soapService;
	}
	public void setSoapService(SoapService soapService) {
		this.soapService = soapService;
	}
}