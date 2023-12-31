package com.hotent.bpm.api.plugin.core.runtime;

import java.util.List;

import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;

/**
 * 用户计算接口。
 * <pre> 
 * 描述：TODO
 * 构建组：x5-bpmx-api
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-2-28-下午1:43:09
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmUserCalcPlugin extends RunTimePlugin<BpmUserCalcPluginSession, BpmPluginDef, List<BpmIdentity>> {

}
