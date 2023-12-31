package com.hotent.bpmModel.params;

import java.util.List;

import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 流程定义按钮保存对象
 * 
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(description = "流程定义按钮保存对象")
public class DefBtnsSaveVo {

	@ApiModelProperty(name = "nodeId", notes = "节点id", required = true)
	protected String nodeId;

	@ApiModelProperty(name = "btns", notes = "按钮列表", required = true)
	protected List<Button> btns;

	@ApiModelProperty(name = "defId", notes = "流程定义id", required = true)
	protected String defId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public List<Button> getBtns() {
		return btns;
	}

	public void setBtns(List<Button> btns) {
		this.btns = btns;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

}