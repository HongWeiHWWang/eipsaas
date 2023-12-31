package com.hotent.sys.persistence.dao;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.sys.persistence.model.DataDict;


/**
 * 数据字典 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月21日
 */
public interface DataDictDao extends BaseMapper<DataDict> {

	/**
	 * 通过字典类别查询所有的字典项
	 * @param typeId 类型id
	 * @return
	 */
	List<DataDict> getByTypeId(String typeId);
	/**
	 * 通过字典key 类型获取字典项
	 * @param params 参数
	 * @return
	 */
	DataDict getByDictKey(Map<String, Object> params);
	/**
	 * 根据parentId 获取下一级节点
	 * @param id 父节点
	 * @return
	 */
	List<DataDict> getByParentId(String parentId);
	/**
	 * 根据类型删除所有字典
	 * @param dictTypeId 字典类型
	 */
	void delByDictTypeId(String dictTypeId);
	/**
	 * 更新排序  sn
	 * @param params 参数集合
	 */
	void updSn(Map params);
}
