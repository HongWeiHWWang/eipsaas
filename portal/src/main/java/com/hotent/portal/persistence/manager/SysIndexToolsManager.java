package com.hotent.portal.persistence.manager;

import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.portal.model.SysIndexTools;


/**
 * 首页工具 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
public interface SysIndexToolsManager extends BaseManager<SysIndexTools>{

	
	/**
	 * 根据ids获取列表
	 * @param toolsIds 工具id
	 * @return
	 */
	List<SysIndexTools> getToolsByIds(List<String> toolsIds);
	
	/**
	 * 获取实体通过处理机制
	 * @param handler 处理机制
	 * @param args 	      对象参数
	 * @param parameterTypes 参数类型
	 * @return
	 * @throws Exception
	 */
	Object getModelByHandler(String handler, Object[] args,Class<?>[] parameterTypes) throws Exception;
	
	/**
	 * 通过参数获取数据
	 * @param dataParam 数据参数
	 * @param params    参数
	 * @return
	 * @throws Exception 
	 */
	Object[] getDataParam(String dataParam, Map<String, Object> params) throws Exception;
	
	/**
	 * 通过类型获取参数
	 * @param dataParam 数据参数
	 * @param params    map参数
	 * @return
	 * @throws Exception 
	 */
	Class<?>[] getParameterTypes(String dataParam,Map<String, Object> params) throws Exception;
	
}


