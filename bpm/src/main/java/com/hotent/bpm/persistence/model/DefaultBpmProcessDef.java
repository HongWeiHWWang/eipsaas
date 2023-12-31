package com.hotent.bpm.persistence.model;

import java.util.ArrayList;
import java.util.List;

import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;

/**
 * 流程定义。
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-2-26-下午6:05:58
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class DefaultBpmProcessDef implements BpmProcessDef<DefaultBpmProcessDefExt>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4893982810814218180L;
	private String defKey="";
	private String name="";
	private String processDefinitionId="";
	private List<BpmNodeDef> bpmnNodeDefs;
	private DefaultBpmProcessDefExt processDefExt;
	private BpmProcessDef<DefaultBpmProcessDefExt> parentProcessDef=null;

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getDefKey() {
		return this.defKey;
	}
	
	public void setDefKey(String defKey){
		this.defKey=defKey;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	@Override
	public String getProcessDefinitionId() {
		return this.processDefinitionId;
	}

	public void setBpmnNodeDefs(List<BpmNodeDef> bpmnNodeDefs) {
		this.bpmnNodeDefs = bpmnNodeDefs;
	}

		

	@Override
	public List<BpmNodeDef> getBpmnNodeDefs() {
		return this.bpmnNodeDefs;
	}
	

	
	public void setProcessDefExt(DefaultBpmProcessDefExt def){
		this.processDefExt=def;
	}


	@Override
	public DefaultBpmProcessDefExt getProcessDefExt() {
		
		return this.processDefExt;
	}
	
	public void setParentProcessDef(BpmProcessDef<DefaultBpmProcessDefExt> parentProcessDef ){
		this.parentProcessDef=parentProcessDef;
	}

	@Override
	public BpmProcessDef<DefaultBpmProcessDefExt> getParentProcessDef() {
		
		return this.parentProcessDef;
	}


	@Override
	public BpmNodeDef getStartEvent() {
		for(BpmNodeDef nodeDef:bpmnNodeDefs){
			if(nodeDef.getType().equals(NodeType.START)){
				return nodeDef;
			}
		}
		return null;
	}

	@Override
	public List<BpmNodeDef> getStartNodes() {
		BpmNodeDef startNode= getStartEvent();
		if(startNode==null) return null;
		return startNode.getOutcomeNodes();
	}

	@Override
	public List<BpmNodeDef> getEndEvents() {
		List<BpmNodeDef> rtnList=new ArrayList<BpmNodeDef>();
		for(BpmNodeDef nodeDef:bpmnNodeDefs){
			if(nodeDef.getType().equals(NodeType.END)){
				rtnList.add(nodeDef);
			}
		}
		return rtnList;
	}


}
