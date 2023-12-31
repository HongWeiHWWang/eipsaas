package com.hotent.bo.persistence.dao;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bo.model.BoEnt;

/**
 * 业务对象定义 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoEntDao extends BaseMapper<BoEnt> {
	/**
	 * 根据名称获取实体对象。
	 * @param name
	 * @return
	 */
	BoEnt getByName(String name);
	
	/**
	 * 根据boDef定义id获取关联boEnt
	 * @param defId
	 * @return
	 */
	List<BoEnt> getByDefId(String defId);
	
	/**
	 * 根据defId删除所有数据
	 * @param defId
	 */
	void deleteByDefId(String defId);

	/**
	 * 通过表名获取信息
	 * @param tableName
	 * @return
	 */
	List<BoEnt> getByTableName(String tableName);
	
	/**
	 * 通过子实体ID查询孙实体
	 * @param entId	实体ID
	 * @return
	 */
	List<BoEnt> getBySubEntId(String entId);
}