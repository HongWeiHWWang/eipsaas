package com.hotent.bpm.plugin.core.plugindef;

import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.constant.LogicType;
import com.hotent.bpm.api.plugin.core.def.BpmUserCalcPluginDef;


/**
 * 抽象用户策略定义类。
 * @author ray
 *
 */
public abstract class AbstractUserCalcPluginDef extends AbstractBpmPluginDef implements BpmUserCalcPluginDef{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6996733477569532383L;
	private ExtractType extractType=ExtractType.EXACT_NOEXACT;
	private LogicType logicType=LogicType.OR;

	/**
	 * 获取抽取类型。
	 * @return 
	 * ExtractType
	 */
	@Override
	public ExtractType getExtract(){
		return this.extractType;
	}
	
	
	/**
	 * 设置抽取类型。
	 * @param type 
	 * void
	 */
	@Override
	public void setExtract(ExtractType type){
		this.extractType=type;
	}
	
	/**
	 * 逻辑类型。
	 * @return 
	 * LogicType
	 */
	@Override
	public LogicType getLogicCal(){
		return this.logicType;
	}

	/**
	 * 设置逻辑类型
	 * @param logicType 
	 * void
	 */
	@Override
	public void setLogicCal(LogicType logicType){
		this.logicType=logicType;
	}
	

}
