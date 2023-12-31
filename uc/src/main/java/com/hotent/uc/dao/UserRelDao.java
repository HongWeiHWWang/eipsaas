package com.hotent.uc.dao;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.uc.model.UserRel;
import com.baomidou.mybatisplus.core.toolkit.Constants;
/**
 * 
 * <pre> 
 * 描述：用户关系 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2017-06-12 09:21:48
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface UserRelDao extends BaseMapper<UserRel>{

	/**
	 *删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	/**
	 * 通过类型id获取用户关系定义
	 * @param typeId
	 * @param authSql
	 * @return
	 */
	List<UserRel> getByTypeId(@Param("typeId") String typeId,@Param("authSql") String authSql);
	
	/**
	 * 根据关系节点编码获取
	 * @param code
	 * @return
	 */
	UserRel getByAlias(@Param("alias") String alias);

	/**
	 * 获取父关系定义
	 * @param typeId
	 * @param value
	 * @param parentId
	 * @return
	 */
	UserRel getByUserIdAndParentId(@Param("typeId") String typeId,@Param("parentId") String parentId,
			@Param("value") String value);
	
	/**
	 * 根据自定义sql查询
	 * @param whereSql
	 * @return
	 */
	List<UserRel> getSuperUserRelBySql(@Param("whereSql") String whereSql);
	

	/**
	 * 根据自定义sql查询
	 * @param whereSql
	 * @return
	 */
	List<UserRel> getByWhereSql(@Param("whereSql") String whereSql);
	
	/**
	 * 通过汇报关系分类删除汇报线
	 * @param typeId
	 */
	void removeByTypeId(@Param("typeId") String typeId,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 根据路径删除数据
	 * @param path
	 */
	void removeByPath(@Param("path") String path,@Param("updateTime")LocalDateTime updateTime);
	
	/**
	 * 根据父id获取子节点列表
	 * @param whereSql
	 * @return
	 */
	List<UserRel> getByParentId(@Param("parentId") String parentId);

	IPage<UserRel> query(IPage<UserRel> convert2iPage,@Param(Constants.WRAPPER) Wrapper<UserRel> wrapper);
	
}
