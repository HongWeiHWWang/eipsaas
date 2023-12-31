package com.hotent.base.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;

public interface CommonDao{
	@Update("${sql}")
	int update(Map<String,Object> map);
	
	List<Map<String,Object>> query(Map<String,Object> map);
	
	IPage<Map<String,Object>> queryByPage(IPage<?> page,@Param("sql") String sql);
	
	IPage<Map<String,Object>> queryByQueryFilter(IPage<?> page, @Param("sql") String sql, @Param(Constants.WRAPPER) Wrapper<Map<String,Object>> wrapper);

	IPage<Map<String,Object>> queryByCustomSql(IPage<?> page,@Param("sql") String sql,@Param(Constants.WRAPPER) Wrapper<Map<String,Object>> wrapper);
}
