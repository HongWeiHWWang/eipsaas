package com.hotent.base.manager.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.manager.QueryFilterHelper;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;

public class BaseManagerImpl<M extends BaseMapper<T>, T extends Model<T>> extends ServiceImpl<M, T> implements BaseManager<T>, QueryFilterHelper<T> {
	@Override
	public T get(Serializable id) {
		return this.getById(id);
	}

	@Override
	public void create(T t) {
		this.save(t);
	}

	@Override
	public void update(T t) {
		this.updateById(t);
	}

	@Override
	public void remove(Serializable id) {
		this.removeById(id);
	}
	
	@Override
	public void removeByIds(String ...ids) {
		this.removeByIds(Arrays.asList(ids));
	}
	
	@Override
	@Transactional(readOnly=true)
	public PageList<T> query(QueryFilter<T> queryFilter) {
		M m = super.getBaseMapper();
		PageBean pageBean = queryFilter.getPageBean();
		Class<T> currentModelClass = currentModelClass();
		IPage<T> result = m.selectPage(convert2IPage(pageBean), convert2Wrapper(queryFilter, currentModelClass));
		return new PageList<T>(result);
	}
	
	
	@Override
	public PageList<T> page(PageBean pageBean) {
		IPage<T> result = this.page(convert2IPage(pageBean));
		return new PageList<T>(result);
	}

	@Override
	public List<T> getAll() {
		return this.list();
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> queryNoPage(QueryFilter<T> queryFilter) {
		M m = super.getBaseMapper();
		Class<T> currentModelClass = currentModelClass();
		return m.selectList(convert2Wrapper(queryFilter, currentModelClass));
	}
}
