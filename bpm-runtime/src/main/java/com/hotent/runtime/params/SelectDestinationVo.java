package com.hotent.runtime.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;

/**
 * 可跳转路径参数
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月11日
 */
@ApiModel(value="可跳转路径参数")
public class SelectDestinationVo {
	
	@ApiModelProperty(name="jumpType",notes="跳转类型")
	protected String jumpType;
	
	@ApiModelProperty(name="outcomeUserMap",notes="")
	protected Map outcomeUserMap;
	
	@ApiModelProperty(name="outcomeNodes",notes="")
	protected List<BpmNodeDef> outcomeNodes;
	
	@ApiModelProperty(name="allNodeDef",notes="")
	protected List<BpmNodeDef> allNodeDef;
	
	@ApiModelProperty(name="allNodeUserMap",notes="")
	protected Map allNodeUserMap;

	public String getJumpType() {
		return jumpType;
	}

	public void setJumpType(String jumpType) {
		this.jumpType = jumpType;
	}

	public Map getOutcomeUserMap() {
		return outcomeUserMap;
	}

	public void setOutcomeUserMap(Map outcomeUserMap) {
		this.outcomeUserMap = outcomeUserMap;
	}

	public List<BpmNodeDef> getOutcomeNodes() {
		return outcomeNodes;
	}

	public void setOutcomeNodes(List<BpmNodeDef> outcomeNodes) {
		this.outcomeNodes = outcomeNodes;
	}

	public List<BpmNodeDef> getAllNodeDef() {
		return allNodeDef;
	}

	public void setAllNodeDef(List<BpmNodeDef> allNodeDef) {
		this.allNodeDef = allNodeDef;
	}

	public Map getAllNodeUserMap() {
		return allNodeUserMap;
	}

	public void setAllNodeUserMap(Map allNodeUserMap) {
		this.allNodeUserMap = allNodeUserMap;
	}
	
}
