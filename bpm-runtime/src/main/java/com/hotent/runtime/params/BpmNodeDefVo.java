package com.hotent.runtime.params;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;

/**
 * 流程任务节点vo
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="流程任务节点vo")
public class BpmNodeDefVo {

	@ApiModelProperty(name="nodeId",notes="节点的ID",required=true)
	private String nodeId;
	
	@ApiModelProperty(name="nodeName",notes="节点的名称",required=true)
	private String nodeName;
	
	@ApiModelProperty(name="type",notes="节点的类型：start(开始节点),end(结束节点),userTask(用户任务节点),signTask(会签任务节点),subProcess(子流程),callActivity(外部子流程),exclusiveGateway(分支网关),parallelGateway(同步网关),inclusiveGateway(条件网关),subStartGateway(内嵌子流程开始网关),subEndGateway(内嵌子流程结束网关),subMultiStartGateway(多实例内嵌子流程开始网关),serviceTask(服务任务节点)。")
	private NodeType type;
	
	@ApiModelProperty(name="typeDescription",notes="节点类型描述")
	private String typeDescription;

	/**
	 * 取得节点的ID
	 * @return
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * @param nodeId
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 
	 * @return
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * 取得
	 * @param nodeName
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 
	 * @return
	 */
	public NodeType getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(NodeType type) {
		this.type = type;
	}


	public String getTypeDescription() {
		return typeDescription;
	}


	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}



	public static BpmNodeDefVo parseVo(BpmNodeDef nodeDef) {
		BpmNodeDefVo nodeDefVo = new BpmNodeDefVo();
		nodeDefVo.setNodeId(nodeDef.getNodeId());
		nodeDefVo.setNodeName(nodeDef.getName());
		nodeDefVo.setType(nodeDef.getType());
		nodeDefVo.setTypeDescription(nodeDef.getType().getValue());
		return nodeDefVo;
	}
	
}
