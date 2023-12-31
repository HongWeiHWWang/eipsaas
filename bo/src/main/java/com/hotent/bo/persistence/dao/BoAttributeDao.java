package com.hotent.bo.persistence.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bo.model.BoAttribute;

/**
 * bo定义属性DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoAttributeDao extends BaseMapper<BoAttribute> {
	boolean removeByEntId(String entId);
	/**
	 * 通过entId获取属性列表
	 * @param entId
	 * @return
	 */
	List<BoAttribute> getByEntId(String entId);
}
