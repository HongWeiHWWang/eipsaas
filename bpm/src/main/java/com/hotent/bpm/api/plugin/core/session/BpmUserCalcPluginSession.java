package com.hotent.bpm.api.plugin.core.session;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 用户计算会话对象包括流程变量数据和bo数据。
 * @author Administrator
 *
 */
public interface BpmUserCalcPluginSession extends BpmPluginSession {
	
	/**
	 * 流程变量
	 * @return
	 */
	Map<String, Object> getVariables();
	
	/**
	 * BO对象map。
	 * @return
	 */
	Map<String,ObjectNode>  getBoMap();
}
