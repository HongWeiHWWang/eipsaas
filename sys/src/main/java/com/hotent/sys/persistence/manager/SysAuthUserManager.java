package com.hotent.sys.persistence.manager;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.sys.persistence.model.SysAuthUser;


/**
 * 对象功能:流程定义权限明细 Manager类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 14:10:50
 */
public interface SysAuthUserManager extends  BaseManager<SysAuthUser>{
	/**
	 * 获取首页栏目权限
	 * @param id
	 * @param objType
	 * @return
	 * @throws IOException 
	 */
	public ArrayNode getRights(String id,String objType) throws IOException;
	/**
	 * 保存首页栏目权限
	 * @param id
	 * @param objType
	 * @param ownerNameJson
	 * @throws IOException 
	 */
	public void saveRights(String id,String objType,String ownerNameJson) throws IOException;
	/**
	 * 通过objType获取当前用户权限
	 * @param objType
	 * @return
	 */
	public List<String> getAuthorizeIdsByUserMap(String objType);
	/**
	 * 判断用户对某个模块数据是否有权限
	 * @param userID
	 * @param authorizeId
	 */
	public boolean hasRights(String authorizeId);
	/**
	 * 删除指定用户的权限缓存
	 * @param userId
	 */
	void delUserMenuCache(String userId);
}
