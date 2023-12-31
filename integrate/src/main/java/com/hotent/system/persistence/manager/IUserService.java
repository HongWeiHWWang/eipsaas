package com.hotent.system.persistence.manager;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface IUserService {
	/**
	 * 新增用户
	 * @param org
	 * @throws Exception
	 */
	public void create(ObjectNode sysUser) throws IOException;
	/**
	 * 更新用户
	 * @param org
	 * @throws Exception
	 */
	public void update(ObjectNode sysUser) throws IOException;

	/**
	 * 删除用户
	 * @param orgId
	 */
	public void delete(String userId) throws IOException;
	/**
	 * 批量删除用户
	 * @param userIds
	 * @throws Exception
	 */
	void deleteAll(String userIds) throws IOException;
	/**
	 * 批量同步
	 * 已经存在、尚未绑定微信号的忽略
	 * @param sysUserList
	 * @throws Exception
	 */
	void addAll(JsonNode sysUserList) throws IOException;

	/**
	 * 通讯录同步
	 * @param lAryId
	 */
	void syncUser(String[] lAryId) throws IOException;
}

