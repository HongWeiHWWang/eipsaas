package com.hotent.bpm.api.plugin.core.def;

import com.hotent.bpm.api.constant.ActionType;


public interface TaskActionHandlerDef extends BpmPluginDef {
	
	public static final String SUPPORT_TYPE_ALL="all";
	
	public static final String SUPPORT_TYPE_START="start";
	
	public static final String SUPPORT_TYPE_SIGNTASK="signtask";
	
	public static final String SUPPORT_TYPE_CUSTOMSIGNTASK="customsigntask";
	
	public static final String SUPPORT_TYPE_USERTASK="usertask";
	
	public static final String SUPPORT_TYPE_BOTH="both";
	
	
	
	/**
	 * 获得描述
	 * @return 
	 * String
	 */
	String getDescription();
	
	/**
	 * 
	 * 插件支持的任务节点类型。
	 * 任务和会签节点。
	 * @return  String
	 */
	String getSupportType();
	
	/**
	 * 获得操作类的类名
	 * @return 
	 * String
	 */
	String getHandlerClass();
	
	/**
	 * 获得操作名称，如agree、end、back等
	 * @return 
	 * String
	 */
	String getName();
	
	
	/**
	 * 支持的动作类型。
	 * @return 
	 * ActionType
	 */
	ActionType getActionType();
	
	/**
	 * 是否支持脚本
	 * @return
	 */
	boolean isSupportScript();
}
