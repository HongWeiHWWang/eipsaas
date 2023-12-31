package com.hotent.file.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.file.model.Catalog;

/**
 * 附件分类接口
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月15日
 */
public interface CatalogDao extends BaseMapper<Catalog> {
	/**
	 * 根据父级ID找到子级对象
	 * @param parentId
	 * @return
	 */
	List<Catalog> getCatalogBypParentId(String parentId);


	List<Catalog> getCatalogByCreateBy(String userId);

	List<Catalog> getListByParentId(@Param("parentId")String parentId, @Param("name")String name);

}
