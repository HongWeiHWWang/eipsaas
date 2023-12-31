package com.hotent.bpm.api.event;

import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.AopType;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;

/**
 * 流程启动是的监听器事件。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-6-6-下午1:38:10
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmStartModel {

	private BpmProcessInstance bpmProcessInstance;
	
	
	private ActionCmd cmd;
	
	
	private AopType aopType=AopType.PREVIOUS;
	
	


	public BpmStartModel(BpmProcessInstance bpmProcessInstance, ActionCmd cmd,
			AopType aopType) {
		super();
		this.bpmProcessInstance = bpmProcessInstance;
		this.cmd = cmd;
		this.aopType = aopType;
	}


	public BpmProcessInstance getBpmProcessInstance() {
		return bpmProcessInstance;
	}


	public void setBpmProcessInstance(BpmProcessInstance bpmProcessInstance) {
		this.bpmProcessInstance = bpmProcessInstance;
	}


	public ActionCmd getCmd() {
		return cmd;
	}


	public void setCmd(ActionCmd cmd) {
		this.cmd = cmd;
	}


	public AopType getAopType() {
		return aopType;
	}


	public void setAopType(AopType aopType) {
		this.aopType = aopType;
	}
	
	

}
