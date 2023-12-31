package com.hotent.runtime.params;



import java.util.List;

import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmDefLayout;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.task.BpmTaskOpinion;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmTask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 任务详细页面数据
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
@ApiModel(value="任务详细页面数据")
public class TaskGetVo {

	@ApiModelProperty(name="bpmTask",notes="任务类")
	private DefaultBpmTask bpmTask;
	
	@ApiModelProperty(name="bpmDefLayout",notes="流程定义坐标")
	private BpmDefLayout bpmDefLayout;
	
	@ApiModelProperty(name="opinionList",notes="审批意见列表")
	private List<List<BpmTaskOpinion>> opinionList;
	
	@ApiModelProperty(name="bpmIdentities",notes="任务的候选人")
	private List<BpmIdentity> bpmIdentities;
	
	@ApiModelProperty(name="curNodeDef",notes="当前节点对象")
	private BpmNodeDef curNodeDef;
	
	@ApiModelProperty(name="showModifyRecord",notes="是否显示表单修改记录")
	private boolean showModifyRecord = false;
	
	@ApiModelProperty(name="formKey",notes="表单key")
	private String formKey;
	
	@ApiModelProperty(name="initFillData",notes="是否初始化填报数据")
	private boolean initFillData;
	
	public TaskGetVo(){}
	
	public TaskGetVo(DefaultBpmTask bpmTask,BpmDefLayout BpmDefLayout,List<List<BpmTaskOpinion>> opinionList,
			List<BpmIdentity> bpmIdentities){
		this.bpmTask = bpmTask;
		this.bpmDefLayout = BpmDefLayout;
		this.opinionList = opinionList;
		this.bpmIdentities = bpmIdentities;
	}

	public TaskGetVo(DefaultBpmTask bpmTask,BpmDefLayout BpmDefLayout,List<List<BpmTaskOpinion>> opinionList,
			List<BpmIdentity> bpmIdentities ,BpmNodeDef nodeDef){
		this.bpmTask = bpmTask;
		this.bpmDefLayout = BpmDefLayout;
		this.opinionList = opinionList;
		this.bpmIdentities = bpmIdentities;
		this.curNodeDef = nodeDef;
	}

	public TaskGetVo(DefaultBpmTask bpmTask, BpmDefLayout bpmDefLayout, List<List<BpmTaskOpinion>> opinionList,
			List<BpmIdentity> bpmIdentities, BpmNodeDef curNodeDef, boolean showModifyRecord) {
		this.bpmTask = bpmTask;
		this.bpmDefLayout = bpmDefLayout;
		this.opinionList = opinionList;
		this.bpmIdentities = bpmIdentities;
		this.curNodeDef = curNodeDef;
		this.showModifyRecord = showModifyRecord;
	}

	public TaskGetVo(DefaultBpmTask bpmTask, BpmDefLayout bpmDefLayout, List<List<BpmTaskOpinion>> opinionList,
			List<BpmIdentity> bpmIdentities, BpmNodeDef curNodeDef, boolean showModifyRecord, String formKey) {
		this.bpmTask = bpmTask;
		this.bpmDefLayout = bpmDefLayout;
		this.opinionList = opinionList;
		this.bpmIdentities = bpmIdentities;
		this.curNodeDef = curNodeDef;
		this.showModifyRecord = showModifyRecord;
		this.formKey = formKey;
	}

	public DefaultBpmTask getBpmTask() {
		return bpmTask;
	}

	public void setBpmTask(DefaultBpmTask bpmTask) {
		this.bpmTask = bpmTask;
	}

	public BpmDefLayout getBpmDefLayout() {
		return bpmDefLayout;
	}

	public void setBpmDefLayout(BpmDefLayout bpmDefLayout) {
		this.bpmDefLayout = bpmDefLayout;
	}

	public List<List<BpmTaskOpinion>> getOpinionList() {
		return opinionList;
	}

	public void setOpinionList(List<List<BpmTaskOpinion>> opinionList) {
		this.opinionList = opinionList;
	}

	public List<BpmIdentity> getBpmIdentities() {
		return bpmIdentities;
	}

	public void setBpmIdentities(List<BpmIdentity> bpmIdentities) {
		this.bpmIdentities = bpmIdentities;
	}

	public BpmNodeDef getCurNodeDef() {
		return curNodeDef;
	}

	public void setCurNodeDef(BpmNodeDef curNodeDef) {
		this.curNodeDef = curNodeDef;
	}

	public boolean isShowModifyRecord() {
		return showModifyRecord;
	}

	public void setShowModifyRecord(boolean showModifyRecord) {
		this.showModifyRecord = showModifyRecord;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	
	public boolean isInitFillData() {
		return initFillData;
	}

	public void setInitFillData(boolean initFillData) {
		this.initFillData = initFillData;
	}
}
