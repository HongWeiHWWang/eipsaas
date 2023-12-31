package com.hotent.bo.persistence.manager;

import java.util.List;
import com.hotent.base.manager.BaseManager;
import com.hotent.bo.model.BoEntRel;

/**
 * bo应用定义处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoEntRelManager extends BaseManager<BoEntRel>{
	/**
	 * 根据BO定义ID获取BO实体列表。
	 * @param defId
	 * @return
	 */
	List<BoEntRel> getByDefId(String defId);
	
	/**
	 * 根据实体定义ID删除bo实体关系。
	 * @param defId
	 */
	void removeByDefId(String defId);
	
	List<BoEntRel> getByEntId(String entId);

	BoEntRel getByDefIdAndEntId(String defId, String entId);
}
