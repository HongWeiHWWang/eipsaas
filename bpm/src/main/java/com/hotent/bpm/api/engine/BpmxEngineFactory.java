package com.hotent.bpm.api.engine;

/**
 * 
 * <pre> 
 * 描述: BPM引擎工厂
 * 构建组：x5-bpmx-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014-3-3-下午5:02:33
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmxEngineFactory {
	/**
	 * 获取流程引擎
	 * @return 
	 * BpmxEngine
	 * @exception 
	 * @since  1.0.0
	 */
	public BpmxEngine getEngine();
	/**
	 * 按名称获取流程引擎
	 * @param engineName
	 * @return 
	 * BpmxEngine
	 * @exception 
	 * @since  1.0.0
	 */
	public BpmxEngine getEngine(String engineName);
}
