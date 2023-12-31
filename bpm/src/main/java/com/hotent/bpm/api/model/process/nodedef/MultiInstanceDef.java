package com.hotent.bpm.api.model.process.nodedef;

public interface MultiInstanceDef {
	
	/**
	 * 是否支持多实例
	 * @return 
	 * boolean
	 * @exception 
	 * @since  1.0.0
	 */
	boolean supportMuliInstance();

	/**
	 * 是否串行
	 * @return 
	 * boolean
	 * @exception 
	 * @since  1.0.0
	 */
	boolean isParallel();
	
	void setSupportMuliInstance(boolean support);
	
	void setParallel(boolean isParallel);

}
