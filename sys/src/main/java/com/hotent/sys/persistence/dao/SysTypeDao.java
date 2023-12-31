package com.hotent.sys.persistence.dao;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.sys.persistence.model.SysType;

/**
 * 系统类型 DAO接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月21日
 */
public interface SysTypeDao extends BaseMapper<SysType> {
	
	/**
	 * 通过父节点获取系统类型
	 * @param parentId 父节点
	 * @return
	 */
	List<SysType> getByParentId(String parentId);
	
	/**
	 * 判断是否存在这个key
	 * @param params 参数
	 */
	int isKeyExist(Map params);

	/**
	 * 通过分组key获取系统类型
	 * @param params 参数
	 */
	List<SysType> getByGroupKey(String groupKey);

	/**
	 * 根据path获取其子节点
	 * 不包含本身！！
	 * @param path 路径
	 * @return
	 */
	List<SysType> getByPath(Map params);

	/**
	 * 
	 * @param params 参数
	 * @return
	 */
	List<SysType> getPrivByPartId(Map params);

	/**
	 * 更新排序  sn
	 * @param params 参数
	 */
	void updSn(Map params);
	
	/**
	 * 通过所属分组key 以及parentId获取分组  ：<br>eg: 通过 分组key，分组id 获取 所有分类
	 * @param groupKey
	 * @param id
	 * @return
	 */
	List<SysType> getTypesByParentId(String groupKey, String parentId);
	
	/**
	 * 通过Key 获取 type
	 * @param typeKey
	 * @return
	 */
	SysType getByTypeKey(String typeKey);
	
	/**
	 * 获取系统类型通过类型key和分组key
	 * @param params
	 * @return
	 */
	SysType getByTypeKeyAndGroupKey(Map params);

}
