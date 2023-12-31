package com.hotent.bpm.api.model.process.nodedef.ext;

import java.util.List;

import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;

/**
 * 网关节点定义。
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-14-上午10:12:16
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class GateWayBpmNodeDef extends BaseBpmNodeDef {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -226028912593132253L;

	@Override
	public List<BpmPluginContext> getBpmPluginContexts(){
		throw new RuntimeException("GateWayBpmNodeDef not support getBpmPluginContexts method");
	}

	/**
	 * 获得内部子流程的流程定义。
	 * @return 
	 * Map<String,BpmNodeDef> key：nodeId，value：BpmNodeDef
	 * @exception 
	 * @since  1.0.0
	 */
	public BpmProcessDef<BpmProcessDefExt>  getChildBpmProcessDef(){
		throw new RuntimeException("GateWayBpmNodeDef not support getChildBpmProcessDef method");
	}

	
}
