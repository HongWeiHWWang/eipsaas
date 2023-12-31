package com.hotent.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.service.model.ServiceSet;

/**
 * 服务调用设置dao
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月4日
 */
public interface ServiceSetDao extends BaseMapper<ServiceSet> {
	/**
	 * 通过别名获取服务调用设置
	 * @param alias	别名
	 * @return		服务调用设置
	 */
	ServiceSet getByAlias(String alias);
}
