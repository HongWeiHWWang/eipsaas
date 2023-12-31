package com.hotent.bpm.persistence.model;

import com.hotent.bpm.api.model.process.nodedef.ext.BaseBpmNodeDef;

/**
 * 节点配置处理器。
 * 用户读取节点的插件，用户任务节点等配置。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-29-下午7:28:07
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface NodeHandler {

	void handNode(BaseBpmNodeDef bpmNodeDef,Object node) throws Exception; 
}
