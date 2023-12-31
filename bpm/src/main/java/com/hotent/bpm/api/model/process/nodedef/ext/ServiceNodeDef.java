package com.hotent.bpm.api.model.process.nodedef.ext;

import com.hotent.bpm.api.constant.NodeType;

/**
 * 自动任务。
 * <pre> 
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-4-24-下午1:43:33
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class ServiceNodeDef extends AutoTaskDef {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6842324902293539258L;

	public ServiceNodeDef(){
		this.setType(NodeType.SERVICETASK);
	}
}
