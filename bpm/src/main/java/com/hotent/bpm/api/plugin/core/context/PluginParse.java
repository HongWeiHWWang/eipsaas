package com.hotent.bpm.api.plugin.core.context;

import java.io.IOException;


public interface PluginParse {
	/**
	 * 根据插件定义构造插件xml
	 * @param bpmPluginDef
	 * @return String
	 */
	String getPluginXml();
	
	
	/**
	 * 解析插件定义。
	 * @param pluginDefJson
	 * @return 
	 * BpmPluginDef
	 * @throws IOException 
	 * @throws Exception 
	 */
	void parse(String pluginDefJson) throws IOException, Exception;
	
	/**
	 * 返回JSON 
	 * @return 
	 * String
	 * @throws IOException 
	 * @throws Exception 
	 */
	String getJson() throws IOException, Exception;
	
	
	/**
	 * 插件类型。
	 * @return 
	 * String
	 */
	String getType();
	
	
	
}
