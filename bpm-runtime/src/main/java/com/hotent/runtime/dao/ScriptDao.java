package com.hotent.runtime.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.runtime.model.Script;


public interface ScriptDao extends BaseMapper<Script> {

	/**
	 * 返回所有脚本的分类
	 * @return
	 */
	public List<String> getDistinctCategory();
	
	/**
	 * 判断脚本名称是否存在
	 * @param name
	 * @return
	 */
	public Integer isNameExists(String name);
}
