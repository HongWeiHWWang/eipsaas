package com.hotent.bpm.persistence.manager;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.model.BoDataModifyRecord;

/**
 * 
 * <pre> 
 * 描述：流程表单数据修改记录 处理接口
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-23 11:45:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
public interface BoDataModifyRecordManager extends BaseManager<BoDataModifyRecord>{
	
	/**
	 * 根据数据表单修改添加记录
	 * @param resultList
	 */
	void handleBoDateModify(Map<String, Object> params) throws Exception;
	
	/**
	 * 通过外键查询列表
	 * @param refId
	 * @return
	 */
	List<BoDataModifyRecord> getListByRefId(String refId);
	
	
}
