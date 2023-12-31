package com.hotent.bpmModel.manager;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.model.CommonResult;

/**
 * 流程导入导出接口
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月15日
 */
public interface BpmDefTransform {
	
	/**
	 * 流程导出接口。
	 * @param defList		流程定义ID列表。
	 * @return  String		导出流程成字符串。
	 * @throws Exception 
	 */
	Map<String,String> exportDef(List<String> defList) throws Exception;
	
	/**
	 * 导入流程。 
	 * @param 解压后文件的位置。
	 * void
	 * @return 
	 */
	CommonResult<String> importDef(ObjectNode objectNode,String typeId);
}
