package com.hotent.bo.persistence.manager;

import java.io.IOException;
import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoEnt;

/**
 * 业务实体定义属性 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoAttributeManager extends BaseManager<BoAttribute> {
	
	/**
	 * 根据实体ID获取BO属性
	 * @param entId	实体ID
	 * @return		返回bo属性列表
	 */
	List<BoAttribute> getByEntId(String entId);
	
	/**
	 * 根据BoEnt获取属性列表
	 * @param boEnt
	 * @return
	 */
	List<BoAttribute> getByBoEnt(BoEnt boEnt);

	/**
	 * 根据实体ID删除属性。
	 * @param entId	实体ID
	 */
	void removeByEntId(String entId);

	/**
	 * 修改字段状态
	 * @param json
	 */
	void updateAttrStatus(String json) throws IOException;

	void recovery(String json) throws Exception;

}