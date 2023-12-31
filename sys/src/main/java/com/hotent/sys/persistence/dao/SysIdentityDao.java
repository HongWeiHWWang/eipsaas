package com.hotent.sys.persistence.dao;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.sys.persistence.model.SysIdentity;


public interface SysIdentityDao extends BaseMapper<SysIdentity> {


	/**
	 * 判读流水号别名是否已经存在
	 * @param id  id为null 表明是新增的流水号，否则为更新流水号
	 * @param alias
	 * @return
	 */
	Integer isAliasExisted(Map<String,Object> params);

	/**
	 * 根据别名获取流水号数据（数据库锁定了对应的行数据）
	 * @param alias
	 * @return
	 */
	SysIdentity getByAlias(String alias);
	
	
	/**
	 * 根据流程别名 。
	 * @param identity 
	 * void
	 */
	int updByAlias(SysIdentity identity);
}
