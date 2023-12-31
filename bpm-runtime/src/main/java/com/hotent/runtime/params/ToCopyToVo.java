package com.hotent.runtime.params;



import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程转发页面参数vo
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程转发页面参数")
public class ToCopyToVo {

	@ApiModelProperty(name="handlerType",notes="支持的消息类型集合")
	private Map<String, String> handlerTypes;
	
	@ApiModelProperty(name="proInstId",notes="流程实例id")
	private String proInstId;
	
	@ApiModelProperty(name="nodeId",notes="任务节点id")
	private String nodeId;
	
	@ApiModelProperty(name="copyToType",notes="类型：0 抄送  1转发")
	private String copyToType;

	public Map<String, String> getHandlerTypes() {
		return handlerTypes;
	}

	public void setHandlerTypes(Map<String, String> handlerTypes) {
		this.handlerTypes = handlerTypes;
	}

	public String getProInstId() {
		return proInstId;
	}

	public void setProInstId(String proInstId) {
		this.proInstId = proInstId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getCopyToType() {
		return copyToType;
	}

	public void setCopyToType(String copyToType) {
		this.copyToType = copyToType;
	}
	
}
