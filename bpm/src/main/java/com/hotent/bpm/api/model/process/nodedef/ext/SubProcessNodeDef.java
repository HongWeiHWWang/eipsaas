package com.hotent.bpm.api.model.process.nodedef.ext;

import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.nodedef.MultiInstanceDef;

/**
 * 内部子流程节点定义扩展。
 * @author ray
 *
 */
public class SubProcessNodeDef extends BaseBpmNodeDef implements MultiInstanceDef {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1165886168391484970L;

	public SubProcessNodeDef(){
		setType(NodeType.SUBPROCESS);
	}
	
	private BpmProcessDef<BpmProcessDefExt> bpmChildProcessDef;
	
	/**
	 * 是否串行会签。
	 */
	private boolean isParallel=false;
	
	private boolean setSupportMuliInstance=false;

	@Override
	public boolean supportMuliInstance() {
		
		return this.setSupportMuliInstance;
	}

	@Override
	public boolean isParallel() {
		return isParallel;
	}
	
	public void setParallel(boolean isParallel){
		this.isParallel=isParallel;
	}
	
	
	/**
	 * 获得内部子流程的流程定义。
	 * @return 
	 * Map<String,BpmNodeDef> key：nodeId，value：BpmNodeDef
	 * @exception 
	 * @since  1.0.0
	 */
	public BpmProcessDef<? extends BpmProcessDefExt> getChildBpmProcessDef(){
		return bpmChildProcessDef;
	}
	
	public void setChildBpmProcessDef(BpmProcessDef<BpmProcessDefExt> bpmChildProcessDef ){
		this.bpmChildProcessDef=bpmChildProcessDef;
	}

	@Override
	public void setSupportMuliInstance(boolean support) {
		this.setSupportMuliInstance=support;
		
	}

}
