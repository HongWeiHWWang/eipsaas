package com.hotent.bpm.persistence.model.nodehandler;

import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.nodedef.ext.BaseBpmNodeDef;
import com.hotent.bpm.persistence.model.NodeHandler;

/**
 * 基础节点处理器。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-29-下午7:16:23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BaseNodeHandler implements NodeHandler{

	@Override
	public void handNode(BaseBpmNodeDef bpmNodeDef, Object baseNode) throws Exception {
		
		PluginContextUtil.handBaseNode(bpmNodeDef, baseNode);
		if(NodeType.START.equals(bpmNodeDef.getType())){
			//处理子流程表单
			PluginContextUtil.handSubForm(bpmNodeDef, baseNode);
		}
		
	}
	
	

}
