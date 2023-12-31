package com.hotent.form.persistence.manager.impl;

import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.form.model.FieldAuth;
import com.hotent.form.persistence.dao.FieldAuthDao;
import com.hotent.form.persistence.manager.FieldAuthManager;

/**
 * 表单字段授权
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@Service("fieldAuthManager")
public class FieldAuthManagerImpl extends BaseManagerImpl<FieldAuthDao, FieldAuth> implements FieldAuthManager{
	@Override
	public FieldAuth getByTableName(String tableName) {
		return baseMapper.getByTableName(tableName);
	}
	@Override
	public FieldAuth getByClassName(String className) {
		return baseMapper.getByClassName(className);
	}
	@Override
	public FieldAuth getByEntName(String entName) {
		return baseMapper.getByEntName(entName);
	}
}
