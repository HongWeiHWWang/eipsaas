package com.hotent.base.feign.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.EmptyFeignException;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.uc.api.model.Group;

@Component
public class UCFeignServiceImpl implements UCFeignService {
	@Override
	public JsonNode loadUserByUsername(String account) {
		throw new EmptyFeignException();
	}

	@Override
	public JsonNode getAllUser() {
		throw new EmptyFeignException();
	}


	@Override
	public CommonResult<JsonNode> getUserById(String userId) {
		throw new EmptyFeignException();
	}

	@Override
	public JsonNode getUser(String account, String userNumber) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getUserByAccounts(String accounts) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getAllOrgUsers(QueryFilter queryFilter) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getUsersByRoleCode(String codes) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getUserByPost(String postCode) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getUsersByJob(String codes) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getUserByEmail(String email) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getChargesByOrgId(String email, boolean isMain) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getSysTypeByIdOrKey(String userId) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getAllRole() {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getMainGroup(String userId) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getCurrentUserAuthOrgLayout(String userId) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getOrgListByUserId(String userId) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getOrgByIdOrCode(String email) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getJobsByUserId(String userId) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getByOrgRelDefCode(String redDefCode, String orgCode) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getByOrgRelCode(String orgCode, String relCode) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getOrgUserMaster(String userId, String demId) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getSuperUser(ObjectNode obj) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getRoleListByAccount(String account) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getPosListByAccount(String account) {
		throw new EmptyFeignException();
	}


	@Override
	public ObjectNode getMainGroup(String userId, String demId) {
		throw new EmptyFeignException();
	}

	@Override
	public JsonNode getCharges(String userId, Boolean isMain, Boolean isP) {
		throw new EmptyFeignException();
	}

	@Override
	public boolean isSupOrgByCurrMain(String userId, String demId, Integer level) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<ObjectNode> getRoleByIdOrCode(String code) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<ObjectNode> getPostByIdOrCode(String code) {
		throw new EmptyFeignException();
	}
	@Override
	public CommonResult<ObjectNode> getJobByOrgCode(String code) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getJobByIdOrCode(String code) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getSuperFromUnder(String userId, String orgId, String demId) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getCustomLevelCharge(String userId, String level, boolean isMainCharge) {
		throw new EmptyFeignException();
	}

	@Override
	public Set<ObjectNode> getCustomLevelPost(String userId, String level, String postCode) {
		throw new EmptyFeignException();
	}

	@Override
	public Set<ObjectNode> getCustomLevelJob(String userId, String level, String jobCode) {
		throw new EmptyFeignException();
	}

	@Override
	public String getStartOrgParam(String userId, String param) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getOrgParamsById(String orgId, String code) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getUserParamsById(String userId, String code) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getUserByIdsOrAccounts(String ids) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getChildOrg(String parentId) {
		throw new EmptyFeignException();
	}
	@Override
	public List<ObjectNode> getOrgsByparentId(String parentId) {
		throw new EmptyFeignException();
	}

	@Override
	public List<Map<String, String>> getPathNames(List<String> userIds) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getDepHeader(String userId ,Boolean isMain) {
		throw new EmptyFeignException();
	}

	@Override
	public ArrayNode getDepHeaderByOrg(String orgId ,Boolean isMain) {
		throw new EmptyFeignException();
	}

	@Override
	public List<Group> getGroupsByUidAndType(String userId ,String type) {
		throw new EmptyFeignException();
	}

	@Override
	public Map<String, Set<String>> getChildrenIds(Map<String, String> ids) {
		throw new EmptyFeignException();
	}

	@Override
	public List queryOrgUserRel(QueryFilter queryFilter) {
		throw new EmptyFeignException();
	}


	@Override
	public List<ObjectNode> getUserByNameaAndEmal(String query) {
		throw new EmptyFeignException();
	}

	@Override
	public Map<String, Object> calculateNodeUser(Map<String, Object> nodeMap) {
		throw new EmptyFeignException();
	}


	@Override
	public List<ObjectNode> getOrgListByDemId(String demId) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<JsonNode> getUserByOpenId(String openId) {
		throw new EmptyFeignException();
	}

	@Override
	public Map<String, Map<String, String>> getUserRightMapByIds(Set<String> ids) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getDefaultDem() {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getSupOrgByCurrMain(String userId, String demId) {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getAllDems() {
		throw new EmptyFeignException();
	}

	@Override
	public Map<String, Object> getDetailByAccountOrId(String account) {
		throw new EmptyFeignException();
	}


	@Override
	public CommonResult<String> postUserByAccount(String accounts, String openid) {
		throw new EmptyFeignException();
	}

    @Override
    public ArrayNode getUserByIds(String ids) {
        throw new EmptyFeignException();
    }

	@Override
	public CommonResult<String> addOrgFromExterUni(ObjectNode org) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<JsonNode> getUserByMobile(String mobile) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<String> addUserFromExterUni(ObjectNode newUser) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<String> addUsersForOrg(String orgCode, String accounts) {
		throw new EmptyFeignException();
	}
	@Override
	public ArrayNode getUserInfoBySignData(ArrayNode customSignDatas) throws Exception {
		throw new EmptyFeignException();
	}

	@Override
	public List<String> getIgnoreMenuCodes(String tenantId) throws Exception {
		throw new EmptyFeignException();
	}

	@Override
	public JsonNode getTenantById(String id) {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getMainPostOrOrgByUserId(String userId) {
		throw new EmptyFeignException();
	}
	
	@Override
	public CommonResult<Object> getFillOrg(String demId) throws Exception {
		throw new EmptyFeignException();
	}
}
