package com.hotent.base.manager;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;

/**
 * 基础服务层
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月9日
 */
public interface BaseManager<T extends Model<T>> extends IService<T>{
	/**
	 * 通过主键查询实体
	 * @param id
	 * @return
	 */
	T get(Serializable id);
	/**
	 * 添加实体
	 * @param t
	 */
	void create(T t);
	/**
	 * 更新实体
	 * @param t
	 */
	void update(T t);
	/**
	 * 通过主键删除实体
	 * @param id
	 */
	void remove(Serializable id);
	/**
	 * 通过主键集合批量删除实体
	 * @param ids
	 */
	void removeByIds(String ...ids);
	/**
	 * 通用查询
	 * @param queryFilter
	 * @return
	 */
	PageList<T> query(QueryFilter<T> queryFilter);
	/**
	 * 无分页通用查询
	 * @param queryFilter
	 * @return
	 */
	List<T> queryNoPage(QueryFilter<T> queryFilter);
	/**
	 * 无查询条件分页查询
	 * @param pageBean
	 * @return
	 */
	PageList<T> page(PageBean pageBean);
	/**
	 * 查询所有数据
	 * @return
	 */
	List<T> getAll();
}
