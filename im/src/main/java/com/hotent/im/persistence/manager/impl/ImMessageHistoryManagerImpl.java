package com.hotent.im.persistence.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.hotent.base.dao.MyBatisDao;
import com.hotent.base.manager.impl.AbstractManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.im.persistence.dao.ImMessageHistoryDao;
import com.hotent.im.persistence.manager.ImMessageHistoryManager;
import com.hotent.im.persistence.model.ImMessageHistory;

/**
 * 
 * <pre> 
 * 描述：聊天消息历史表 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:52
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("imMessageHistoryManager")
public class ImMessageHistoryManagerImpl extends AbstractManagerImpl<String, ImMessageHistory> implements ImMessageHistoryManager{
	@Resource
	ImMessageHistoryDao imMessageHistoryDao;
	@Override
	protected MyBatisDao<String, ImMessageHistory> getDao() {
		return imMessageHistoryDao;
	}
	
	@Override
	public List<ImMessageHistory> sessionMessage(QueryFilter queryFilter) {
		return imMessageHistoryDao.sessionMessage(queryFilter.getParams());
	}
	@Override
	public List<ImMessageHistory> getMsgHistory(QueryFilter filter) {
		return imMessageHistoryDao.getMsgHistory(filter.getParams());
	}

	@Override
	public PageList<ImMessageHistory> queryHistory(QueryFilter queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
    	if(BeanUtils.isEmpty(pageBean)){
    		PageHelper.startPage(1, Integer.MAX_VALUE, false);
    	}
    	else{
    		PageHelper.startPage(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
    	}
    	List<ImMessageHistory> query = imMessageHistoryDao.queryHistory(queryFilter.getParams());
		return new PageList<ImMessageHistory>(query);
	}
	
}
