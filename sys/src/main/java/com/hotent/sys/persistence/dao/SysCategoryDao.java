package com.hotent.sys.persistence.dao;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.sys.persistence.model.SysCategory;


public interface SysCategoryDao extends BaseMapper<SysCategory> {
	int isKeyExist(Map<String,Object> params);
	/**
	 * 通过分类key 获取改分类
	 * @param key
	 * @return
	 */
	SysCategory getByKey(String key);
}
