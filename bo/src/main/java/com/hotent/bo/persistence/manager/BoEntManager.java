package com.hotent.bo.persistence.manager;

import java.sql.SQLException;
import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.bo.model.BoEnt;

/**
 * 业务对象定义 处理接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoEntManager extends BaseManager<BoEnt> {

	/**
	 * 根据实体类创建物理表。
	 * 
	 * @param boEnt
	 * @throws SQLException
	 */
	void createTable(BoEnt boEnt) throws SQLException;
	
	/**
	 * 保存BoEnt
	 * <pre>
	 * 1.保存或者更新实体信息；
	 * 2.同步保存、更新BoAttribute信息；
	 * 3.发布实体时生成对应的物理表。
	 * </pre>
	 * @param boEnt
	 * @return
	 */
	String saveEnt(BoEnt boEnt) throws Exception;
	
	/**
	 * 删除BoEnt
	 * @param entityId
	 */
	void remove(String entityId);
	
	/**
	 * 根据实体ID获取实体类和属性列表。
	 * 
	 * @param entId
	 * @return BoEnt
	 */
	BoEnt getById(String entId);

	/**
	 * 根据名字返回bo实体对象，不包括属性等信息。
	 * @param name
	 * @return
	 */
	BoEnt getByName(String name);
	
	/**
	 * 获取实体是否可以被修改类型。
	 * 1: 绑定表单后可以增加列
	 * 0: 没有绑定表单可以任意修改。
	 * @param name	实体名称
	 * @return
	 */
	int getCanEditByName(String name);
	
	/**
	 * 根据boDef定义id获取关联boEnt
	 * @param defId
	 * @return
	 */
	List<BoEnt> getByDefId(String defId);
	
	/**
	 * 根据defId删除所有的BoEnt数据
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
	 * 通过子实体ID查询孙实体关系
	 * @param entId	实体ID
	 * @return
	 */
	List<BoEnt> getBySubEntId(String entId);
}
