package com.hotent.bo.persistence.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.bo.model.BoEntRel;

/**
 * 实体关系
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月13日
 */
public interface BoEntRelDao extends BaseMapper<BoEntRel> {
	/**
	 * 通过defId和entId查询数据
	 * @param wrapper
	 * @return
	 */
	List<BoEntRel> getByDefIdAndEntId(@Param("defId") String defId,@Param("entId") String entId);
	/**
	 * 根据BO定义ID获取BO实体列表。
	 * @param defId
	 * @return
	 */
	List<BoEntRel> getByDefId(String defId);
	/**
	 * 通过bo定义ID删除实体关系
	 * @param defId	bo定义ID
	 */
	void removeByDefId(String defId);
	/**
	 * 通过实体ID查询实体关系
	 * @param entId	实体ID
	 * @return
	 */
	List<BoEntRel> getByEntId(String entId);
}