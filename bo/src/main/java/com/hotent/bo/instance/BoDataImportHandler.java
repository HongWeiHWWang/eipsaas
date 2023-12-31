package com.hotent.bo.instance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.ValidateResult;

/**
 * 数据导入接口类
 * @author co
 *
 */
public interface BoDataImportHandler {
	

	/**
	 * 提供给第三方校验数据的接口
	 * @param data 当前需要校验的数据
	 * @param boEnt 实体对象
	 * @param threadVarMap 变量map
	 * @return
	 * @throws IOException
	 */
	List<ValidateResult> validateData(Map<String, Object> data,BoEnt boEnt,Map<String, Object> threadVarMap) throws Exception;
	

	/**
	 * 提供给第三方转换数据的接口
	 * @param data 当前需要转换的数据
	 * @param boEnt 实体对象
	 * @param threadVarMap 变量map
	 * @return
	 * @throws IOException
	 */
	Map<String, Object> transData(Map<String, Object> data,BoEnt boEnt,Map<String, Object> threadVarMap) throws Exception;
	
}
