package com.hotent.bpm.api.model.process.nodedef.ext;

import com.hotent.bpm.api.model.process.nodedef.MultiInstanceDef;

/**
 * 外部子流程扩展。
 * 扩展了流程定义KEY。
 * @author ray
 *
 */
public class CallActivityNodeDef extends BaseBpmNodeDef implements MultiInstanceDef {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3745232151785369093L;

	/**
	 * 子流程流程定义KEY。
	 */
	private String flowKey="";
	
	/**
	 * 是否串行会签。
	 */
	private boolean isParallel=false;
	
	private boolean supportMuliInstance=false;
	
	public void setSupportMuliInstance(boolean supportMuliInstance){
		this.supportMuliInstance=supportMuliInstance;
	}

	@Override
	public boolean supportMuliInstance() {
		return this.supportMuliInstance;
	}

	@Override
	public boolean isParallel() {
		return isParallel;
	}
	
	public void setParallel(boolean isParallel){
		this.isParallel=isParallel;
	}

	public String getFlowKey() {
		return flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

}
