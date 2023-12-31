package com.hotent.bpm.api.model.process.nodedef.ext;

import java.util.List;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;


/**
 * 自动节点。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-3-17-下午6:01:13
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class AutoTaskDef extends BaseBpmNodeDef {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8313548750880126105L;

	public BpmPluginContext getAutoTaskBpmPluginContext(){
		List<BpmPluginContext> list= getBpmPluginContexts();
		if(BeanUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	public BpmPluginDef getAutoTaskBpmPluginDef(){
		BpmPluginContext autoTaskContext = getAutoTaskBpmPluginContext();
		if(autoTaskContext == null) return null;
		BpmPluginDef pluginDef = autoTaskContext.getBpmPluginDef();
		return pluginDef;
	}
}
