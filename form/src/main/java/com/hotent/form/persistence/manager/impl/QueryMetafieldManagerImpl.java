package com.hotent.form.persistence.manager.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.form.model.QueryMetafield;
import com.hotent.form.persistence.dao.QueryMetafieldDao;
import com.hotent.form.persistence.manager.QueryMetafieldManager;
import org.springframework.transaction.annotation.Transactional;

@Service("queryMetafieldManager")
public class QueryMetafieldManagerImpl extends BaseManagerImpl<QueryMetafieldDao, QueryMetafield> implements QueryMetafieldManager{
	@Override
	public List<QueryMetafield> getBySqlId(String sqlId){
		return baseMapper.getBySqlId(sqlId);
	}
	
	@Override
	@Transactional
	public void removeBySqlId(String sqlId){
		baseMapper.removeBySqlId(sqlId);
	}
}
