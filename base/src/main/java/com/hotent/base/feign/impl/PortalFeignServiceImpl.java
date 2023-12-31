package com.hotent.base.feign.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.EmptyFeignException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.jms.Notice;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;

@Component
public class PortalFeignServiceImpl implements PortalFeignService {
	@Override
	public List<HashMap<String, String>> getMethodRoleAuth() {
		throw new EmptyFeignException();
	}

	@Override
	public String getPropertyByAlias(String alias) {
		throw new EmptyFeignException();
	}

    @Override
    public String getByAlias(String alias,String defaultValue) {
        throw new EmptyFeignException();
    }

	@Override
	public ObjectNode getSysTypeById(String id) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getAllSysType(QueryFilter queryFilter) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<String> sendNoticeToQueue(Notice notice) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<String> sendToQueue(ObjectNode model) {
		throw new EmptyFeignException();
	}

	@Override
	public String getEndTimeByUser(ObjectNode param) {
		throw new EmptyFeignException();
	}

	@Override
	public Long getWorkTimeByUser(ObjectNode param) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getMessBoxInfo(String account) {
		throw new EmptyFeignException();
	}

	@Override
	public JsonNode getBeanByAlias(String alias) {
		throw new EmptyFeignException();
	}

	@Override
	public String getNextIdByAlias(String alias) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getMessageNews(JsonNode queryFilter) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode publicMsgNews(String ids) {
		throw new EmptyFeignException();
	}

	@Override
	public boolean calcPermssion(String permssionJson) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode calcAllPermssion(String permssionJson) {
		throw new EmptyFeignException();
	}

	@Override
	public List<String> getAuthorizeIdsByUserMap(String objType) {
		throw new EmptyFeignException();
	}

	@Override
	public String getToken(String type) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<String> initData(String tenantId) {
		throw new EmptyFeignException();
	}

	@Override
	public String getUserInfoUrl(String type, String code) {
		throw new EmptyFeignException();
	}

	@Override
	public String wordPrint(ObjectNode objectNode) {
		throw new EmptyFeignException();
	}

	@Override
	public Map<String, String> getSysLogsSettingStatusMap() {
		throw new EmptyFeignException();
	}
}
