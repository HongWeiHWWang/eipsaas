package com.hotent.system.persistence.manager;


import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IWXOrgService {
	/**
	 * 新增组织
	 * @param org
	 */
	public void create(ObjectNode org);
	/**
	 * 更新组织
	 * @param org
	 */
	public void update(ObjectNode org);
	
	/**
	 * 删除组织
	 * @param orgId 用户账户
	 */
	public void delete(String orgId);
	/**
	 * 批量删除
	 * @param orgIds
	 */
	public void deleteAll(String orgIds);
	/**
	 * 批量添加组织
	 * @param orgList
	 */
	void addAll(List<ObjectNode> orgList);
	
	public String getDepartmentUser(String orgCode);

    /**
     * 微信组织机构同步
     */
    void syncAllOrg();
}
