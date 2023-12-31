package com.hotent.file.persistence.manager;


import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.file.model.Catalog;


/**
 * 附件分类处理接口
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月15日
 */
public interface CatalogManager extends BaseManager<Catalog> {

	/**
	 * 根据用户ID得到附件分类
	 * @param userId
	 * @return
	 */
	List<Catalog> getCatalogByCreateBy(String userId);

	/**
	 * 查询目录下面所有的子目录ID
	 * @param id id
	 * @param listId
	 * @return
	 */
	List<String> getDepartmentList(String id,List<String> listId);
	
	/**
	 * 根据父节点id和名称查询列表
	 * @param parentId
	 * @param name
	 * @return
	 */
	List<Catalog> getListByParentId(String parentId, String name);
}
