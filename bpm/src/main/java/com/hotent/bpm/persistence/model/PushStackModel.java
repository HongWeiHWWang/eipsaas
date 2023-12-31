package com.hotent.bpm.persistence.model;

import java.io.Serializable;

import com.hotent.bpm.api.model.process.task.BpmTask;


/**
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2020-03-w3-下午3:57:56
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class PushStackModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ActTask actTask=null;
	
	private BpmTask bpmTask=null;

	public PushStackModel(ActTask actTask,BpmTask bpmTask) {
		this.actTask = actTask;
		this.bpmTask = bpmTask;
	}
	
	public ActTask getActTask() {
		return actTask;
	}
	
	public BpmTask getBpmTask() {
		return bpmTask;
	}
}
