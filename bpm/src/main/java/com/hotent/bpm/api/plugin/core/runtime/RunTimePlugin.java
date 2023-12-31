package com.hotent.bpm.api.plugin.core.runtime;


/**
 * 插件接口定义。
 * @author yancm
 *
 * @param <T>
 * @param <M>
 * @param <R>
 */
public interface RunTimePlugin<T,M,R> {
	
	R execute(T pluginSession,M pluginDef) throws Exception;
}
