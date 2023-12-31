package com.hotent.service.dao;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.service.model.ServiceParam;

/**
 * 服务调用设置dao
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月4日
 */
public interface ServiceParamDao extends BaseMapper<ServiceParam> {
	/**
	 * 通过设置ID删除所有记录
	 * @param setId 设置ID
	 */
	void removeBySetId(String setId);
	
	/**
	 * 通过设置ID查询所有记录
	 * @param setId	设置ID
	 * @return		参数记录
	 */
	List<ServiceParam> getBySetId(String setId);
}
