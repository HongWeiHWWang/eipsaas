package com.hotent.file.persistence.manager.impl;

import java.util.List;
import javax.annotation.Resource;

import com.hotent.base.manager.impl.BaseManagerImpl;
import org.springframework.stereotype.Service;
import com.hotent.file.model.Catalog;
import com.hotent.file.persistence.dao.CatalogDao;
import com.hotent.file.persistence.manager.CatalogManager;

/**
 *
 * <pre>
 * 描述：w_fjml 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:maoww
 * 邮箱:maoww@jee-soft.cn
 * 日期:2018-05-15 11:45:41
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("catalogManager")
public class CatalogManagerImpl extends BaseManagerImpl<CatalogDao,Catalog> implements CatalogManager{


	@Override
	public List<Catalog> getCatalogByCreateBy(String userId) {
		return baseMapper.getCatalogByCreateBy(userId);
	}
	
	@Override
	public List<String> getDepartmentList(String id,List<String> listId) {
		try {
			List<Catalog> list = baseMapper.getCatalogBypParentId(id);
			if (null != list && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					Catalog c = list.get(i);
					listId.add(c.getId());
					getDepartmentList(c.getId(),listId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listId;
	}

	@Override
	public List<Catalog> getListByParentId(String parentId, String name) {
		return baseMapper.getListByParentId(parentId, name);
	}
}
