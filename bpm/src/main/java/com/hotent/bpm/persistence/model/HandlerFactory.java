package com.hotent.bpm.persistence.model;

import com.hotent.bpm.api.model.process.nodedef.ext.BaseBpmNodeDef;
import com.hotent.bpm.persistence.model.nodehandler.BaseNodeHandler;
import com.hotent.bpm.persistence.model.nodehandler.SignTaskHandler;
import com.hotent.bpm.persistence.model.nodehandler.UserTaskHandler;

/**
 * 处理器工厂。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-29-下午7:27:39
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class HandlerFactory {
	
	public static NodeHandler createHandler(BaseBpmNodeDef nodeDef){
		NodeHandler nodeHandler=null;
		switch (nodeDef.getType()) {
			case USERTASK:
				nodeHandler=new UserTaskHandler();
				break;
			case SIGNTASK:
				nodeHandler=new SignTaskHandler();
				break;
			case CUSTOMSIGNTASK:
				// 不需要特殊处理会签规则 特权
				nodeHandler=new UserTaskHandler();
				break;
			case START:
			case END:
			case SERVICETASK:
				nodeHandler=new BaseNodeHandler();
			default:
				break;
		}
		
		
		return nodeHandler;
	}

}
