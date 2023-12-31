package com.hotent.bo.constant;

public class BoConstants {
	/*---bo数据格式 ---*/
	/**
	 * 以xml格式处理bo数据
	 */
	public final static String XML="xml";
	/**
	 * 以json格式处理bo数据
	 */
	public final static String JSON="json";
	
	/*---bo数据保存格式 ---*/
	/**
	 * 保存bo数据到通用实例表
	 */
	public final static String SAVE_MODE_BOOBJECT="boObject";
	/**
	 * 保存bo数据到物理表
	 */
	public final static String SAVE_MODE_DB="database";
	
	/*---必填、非必填 ---*/
	/**
	 * 必填
	 */
	public final static int REQUIRED_YES = 1;
	/**
	 * 非必填
	 */
	public final static int REQUIRED_NO = 0;
	
	/*---bo实体间对应关系 ---*/
	/**
	 * 主实体
	 */
	public final static String RELATION_MAIN = "main";
	/**
	 * 一对一
	 */
	public final static String RELATION_ONE_TO_ONE = "onetoone";
	/**
	 * 一对多
	 */
	public final static String RELATION_ONE_TO_MANY = "onetomany";
	/**
	 * 多对多
	 */
	public final static String RELATION_MANY_TO_MANY = "manytomany";
	
	/*--- bo数据处理动作 ---*/
	/**
	 * 添加
	 */
	public final static String HANDLE_ADD = "add";
	/**
	 * 更新
	 */
	public final static String HANDLE_UPDATE = "upd";
	/**
	 * 删除
	 */
	public final static String HANDLE_DELETE = "del";
	
	/*--- Short与Boolean类型映射 ---*/
	/**
	 * true对应的Short值
	 */
	public final static Short BOOLEAN_YES_SHORT = 1;
	/**
	 * false对应的Short值
	 */
	public final static Short BOOLEAN_NO_SHORT = 0;
}
