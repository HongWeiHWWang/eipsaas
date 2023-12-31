package com.hotent.runtime.params;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import com.hotent.bpm.api.model.process.def.BpmDefLayout;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;

/**
 * 测试用例设置基本信息vo
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="测试用例设置基本信息")
public class FlowImageVo {

	@ApiModelProperty(name="defId",notes="流程定义id")
	private String defId;
	
	@ApiModelProperty(name="bpmProcessInstance",notes="流程实例")
	private BpmProcessInstance bpmProcessInstance;
	
	@ApiModelProperty(name="instanceId",notes="流程实例id")
	private String instanceId;
	
	@ApiModelProperty(name="bpmProcessInstanceList",notes="子流程列表")
	private List<BpmProcessInstance> bpmProcessInstanceList;
	
	@ApiModelProperty(name="parentInstId",notes="父流程实例id")
	private String parentInstId;
	
	@ApiModelProperty(name="bpmDefLayout",notes="流程定义坐标")
	private BpmDefLayout bpmDefLayout;
	
	@ApiModelProperty(name="from",notes="")
	private String from;

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public BpmProcessInstance getBpmProcessInstance() {
		return bpmProcessInstance;
	}

	public void setBpmProcessInstance(BpmProcessInstance bpmProcessInstance) {
		this.bpmProcessInstance = bpmProcessInstance;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public List<BpmProcessInstance> getBpmProcessInstanceList() {
		return bpmProcessInstanceList;
	}

	public void setBpmProcessInstanceList(
			List<BpmProcessInstance> bpmProcessInstanceList) {
		this.bpmProcessInstanceList = bpmProcessInstanceList;
	}

	public String getParentInstId() {
		return parentInstId;
	}

	public void setParentInstId(String parentInstId) {
		this.parentInstId = parentInstId;
	}

	public BpmDefLayout getBpmDefLayout() {
		return bpmDefLayout;
	}

	public void setBpmDefLayout(BpmDefLayout bpmDefLayout) {
		this.bpmDefLayout = bpmDefLayout;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
}
