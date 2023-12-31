package com.hotent.assembly.feign.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.portal.controller.SysTypeController;
import com.hotent.sys.persistence.model.SysType;
import com.hotent.uc.api.model.Group;
import com.hotent.uc.controller.DemensionController;
import com.hotent.uc.controller.JobController;
import com.hotent.uc.controller.OrgAuthController;
import com.hotent.uc.controller.OrgController;
import com.hotent.uc.controller.ParamsController;
import com.hotent.uc.controller.RoleController;
import com.hotent.uc.controller.TenantIgnoreMenuController;
import com.hotent.uc.controller.TenantManageController;
import com.hotent.uc.controller.UserController;
import com.hotent.uc.controller.UserRelController;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.Org;
import com.hotent.uc.model.OrgAuth;
import com.hotent.uc.model.OrgJob;
import com.hotent.uc.model.OrgParams;
import com.hotent.uc.model.OrgPost;
import com.hotent.uc.model.OrgUser;
import com.hotent.uc.model.Role;
import com.hotent.uc.model.TenantManage;
import com.hotent.uc.model.User;
import com.hotent.uc.model.UserParams;
import com.hotent.uc.params.group.GroupIdentity;
import com.hotent.uc.params.user.UserRelFilterObject;
import com.hotent.uc.params.user.UserVo;

@Service
@Primary
public class UCFeignServiceAssemblyImpl implements UCFeignService {
	@Resource
	UserController userController;
	@Resource
	RoleController roleController;
	@Resource
	JobController jobController;
	@Resource
	OrgController orgController;
	@Resource
	SysTypeController sysTypeController;
	@Resource
	OrgAuthController orgAuthController;
	@Resource
	UserRelController userRelController;
	@Resource
	ParamsController paramsController;
	@Resource
	DemensionController demensionController;
	@Resource
	TenantIgnoreMenuController tenantIgnoreMenuController;
	@Resource
	TenantManageController tenantManageController;

	@Override
	public JsonNode loadUserByUsername(String account) {
		try {
			UserDetails userDetails = userController.loadUserByUsername(account);
			return JsonUtil.toJsonNode(userDetails);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public JsonNode getAllUser() {

		try {
			List<User> list = userController.getAllUser();;
			return JsonUtil.toJsonNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}


	}

	@Override
	public CommonResult<JsonNode> getUserById(String userId) {
		try {
			CommonResult<UserVo> userVoCR = userController.getUserById(userId);
			UserVo userVo = userVoCR.getValue();
			JsonNode jsonNode = JsonUtil.toJsonNode(userVo);
			return new CommonResult<JsonNode>(userVoCR.getState(), userVoCR.getMessage(), jsonNode);
		}
		catch(Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public JsonNode getUser(String account, String userNumber) {
		try {
			UserVo userVo = userController.getUser(account,userNumber);
			return JsonUtil.toJsonNode(userVo);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getUserByAccounts(String accounts) {
		try {
			List<UserVo> users = userController.getUserByAccounts(accounts);
			return JsonUtil.listToArrayNode(users);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}


	@Override
	public ObjectNode getAllOrgUsers(QueryFilter queryFilter) {
		try {
			PageList<UserVo> query = userController.getAllOrgUsers(queryFilter);
			JsonNode jsonNode = JsonUtil.toJsonNode(query);
			return (ObjectNode)jsonNode;
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getUsersByRoleCode(String codes) {
		try {
			List<UserVo> usersByRoleCode = roleController.getUsersByRoleCode(codes);
			return JsonUtil.listToArrayNode(usersByRoleCode);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getUserByPost(String postCode) {
		try {
			List<UserVo> usersByRoleCode = userController.getUserByPost(postCode);
			return JsonUtil.listToArrayNode(usersByRoleCode);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getUsersByJob(String codes) {
		try {
			List<UserVo> usersByRoleCode = jobController.getUsersByJob(codes);
			return JsonUtil.listToArrayNode(usersByRoleCode);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getUserByEmail(String email) {
		try {
			List<User> usersByRoleCode =userController.getUserByEmail(email);
			return JsonUtil.listToArrayNode(usersByRoleCode);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getChargesByOrgId(String orgId, boolean isMain) {
		try {
			List<User> users = orgController.getChargesByOrgId(orgId,isMain,false);
			return JsonUtil.listToListNode(users);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getSysTypeByIdOrKey(String id) {
		try {
			SysType sysType = sysTypeController.getJson(id);
			return (ObjectNode) JsonUtil.toJsonNode(sysType);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getAllRole() {
		try {
			List<Role> all = roleController.getAll();
			return JsonUtil.listToListNode(all);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getMainGroup(String userId) {
		try {
			Org entity = orgController.getMainGroup(userId, Optional.ofNullable(""));
			return BeanUtils.isEmpty(entity)?null:(ObjectNode) JsonUtil.toJsonNode(entity);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getCurrentUserAuthOrgLayout(String userId) {
		try {
			List<OrgAuth> all= orgAuthController.getCurrentUserAuthOrgLayout( Optional.ofNullable(userId));
			return JsonUtil.listToArrayNode(all);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getOrgListByUserId(String userId) {
		try {
			List<Org> all= orgController.getOrgListByUserId(userId);
			return JsonUtil.listToArrayNode(all);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getOrgByIdOrCode(String id) {
		try {
			Org entity = orgController.get(id);
			return BeanUtils.isEmpty(entity)?null:(ObjectNode) JsonUtil.toJsonNode(entity);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}


	@Override
	public ArrayNode getJobsByUserId(String userId) {
		try {
			List<OrgJob> all= orgController.getJobsByUserId(userId);
			return JsonUtil.listToArrayNode(all);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getByOrgRelDefCode(String redDefCode, String orgCode) {
		try {
			Set<GroupIdentity> all= userController.getByJobCodeAndOrgCode(redDefCode,orgCode);
			return JsonUtil.listToListNode(new ArrayList<>(all));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getByOrgRelCode(String orgCode, String relCode) {
		try {
			Set<GroupIdentity> all= userController.getByPostCodeAndOrgCode(orgCode,orgCode);
			return JsonUtil.listToListNode(new ArrayList<>(all));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}


	@Override
	public ObjectNode getOrgUserMaster(String userId, String demId) {
    	try {
    		OrgUser supOrgByCurrMain = orgController.getSupOrgByCurrMain(userId, demId);
    		if(BeanUtils.isEmpty(supOrgByCurrMain)) {
    			return null;
    		}
    		return (ObjectNode)JsonUtil.toJsonNode(supOrgByCurrMain);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getSuperUser(ObjectNode obj) {
		try {
			UserRelFilterObject userRelFilterObject = JsonUtil.toBean(obj, UserRelFilterObject.class);
			List<UserVo> users = userRelController.getSuperUser(userRelFilterObject);
			return JsonUtil.listToListNode(users);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getRoleListByAccount(String account) {
		try {
			List<Role> users = roleController.getRolesByUser(account);
			return JsonUtil.listToListNode(users);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getPosListByAccount(String account) {
		try {
			List<OrgPost> entList = orgController.getOrgPostByUserAccount(account);
			return JsonUtil.listToListNode(entList);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}



	@Override
	public ObjectNode getMainGroup(String userId, String demId) {
		try {
			Org entity = orgController.getMainGroup(userId, Optional.ofNullable(demId));
			return BeanUtils.isEmpty(entity)?null:(ObjectNode) JsonUtil.toJsonNode(entity);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public JsonNode getCharges(String userId, Boolean isMain, Boolean isP) {
		try {
			List<User> list = userController.getCharges(userId, isMain, isP);
			return JsonUtil.listToArrayNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}



	@Override
	public boolean isSupOrgByCurrMain(String userId, String demId, Integer level) {
		try {
			return orgController.isSupOrgByCurrMain(userId,demId,level);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<ObjectNode> getRoleByIdOrCode(String code) {
		try {
			CommonResult<Role> result = roleController.getRole(code);
			return new CommonResult<ObjectNode>(result.getState(), result.getMessage(),(ObjectNode)JsonUtil.toJsonNode(result.getValue()));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<ObjectNode> getPostByIdOrCode(String code) {
		try {
			CommonResult<OrgPost> result = orgController.getOrgPost(code);
			return new CommonResult<ObjectNode>(result.getState(), result.getMessage(),(ObjectNode)JsonUtil.toJsonNode(result.getValue()));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<ObjectNode> getJobByOrgCode(String code) {
		try {
			CommonResult<OrgJob> result = jobController.getOrgJob(code);
			return new CommonResult<ObjectNode>(result.getState(), result.getMessage(),(ObjectNode)JsonUtil.toJsonNode(result.getValue()));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getJobByIdOrCode(String code) {
		try {
			OrgJob result = jobController.getJob(code);
			return (ObjectNode)JsonUtil.toJsonNode(result);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getSuperFromUnder(String userId, String orgId, String demId) {
		try {
			List<User> list = userController.getSuperFromUnder(userId, orgId, demId);
			return JsonUtil.listToArrayNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getCustomLevelCharge(String userId, String level, boolean isMainCharge) {
		try {
			List<User> list = orgController.getCustomLevelCharge(userId, level, isMainCharge);
			return JsonUtil.listToArrayNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Set<ObjectNode> getCustomLevelPost(String userId, String level, String postCode) {
		try {
			Set<GroupIdentity> list = orgController.getCustomLevelPost(userId, level, postCode);
			Set<ObjectNode> nodeSet = new HashSet<>();
			for (GroupIdentity groupIdentity : list) {
				nodeSet.add((ObjectNode)JsonUtil.toJsonNode(groupIdentity));
			}
			return nodeSet;
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}


	@Override
	public Set<ObjectNode> getCustomLevelJob(String userId, String level, String jobCode) {
		try {
			Set<GroupIdentity> list = orgController.getCustomLevelJob(userId, level, jobCode);
			Set<ObjectNode> nodeSet = new HashSet<>();
			for (GroupIdentity groupIdentity : list) {
				nodeSet.add((ObjectNode)JsonUtil.toJsonNode(groupIdentity));
			}
			return nodeSet;
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getStartOrgParam(String userId, String param) {
		try {
			return orgController.getStartOrgParam(userId, param);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getOrgParamsById(String orgId, String code) { 
		try {
			 OrgParams orgParamsById = paramsController.getOrgParamsById(orgId, code);
			return (ObjectNode)JsonUtil.toJsonNode(orgParamsById);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getUserParamsById(String userId, String code) {
		try {
			 UserParams orgParamsById = paramsController.getUserParamsById(userId, code);
			return (ObjectNode)JsonUtil.toJsonNode(orgParamsById);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getUserByIdsOrAccounts(String ids) {
		try {
			List<UserVo> list = userController.postUserByIds(ids);
			return JsonUtil.listToArrayNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getChildOrg(String parentId) {
		try {
			List<Org> list = orgController.getByParentId(parentId);
			return JsonUtil.listToListNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
	@Override
	public List<ObjectNode> getOrgsByparentId(String parentId) {
		try {
			List<Org> list = orgController.getOrgsByparentId(parentId);
			return JsonUtil.listToListNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<Map<String, String>> getPathNames(List<String> userIds) {
		try {
			List<Map<String, String>> list = orgController.getPathNames(userIds);
			return list;
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getDepHeader(String userId ,Boolean isMain) {
		try {
			List<UserVo> list = userController.getDepHeader(Optional.ofNullable(userId),Optional.ofNullable(isMain));
			return JsonUtil.listToArrayNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ArrayNode getDepHeaderByOrg(String orgId ,Boolean isMain) {
		try {
			List<UserVo> list = userController.getDepHeaderByOrg(orgId,Optional.ofNullable(isMain));
			return JsonUtil.listToArrayNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<Group> getGroupsByUidAndType(String userId ,String type) {
		try {
			List<Group> list = userController.getGroupsByUidAndType(userId,type);
			return list;
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Map<String, Set<String>> getChildrenIds(Map<String, String> ids) {
		try {
			Map<String, Set<String>> list = orgController.getChildrenIds(ids);
			return list;
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List queryOrgUserRel(QueryFilter queryFilter) {
		try {
			 List queryOrgUserRel = userController.queryOrgUserRel(queryFilter);
			return queryOrgUserRel;
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}


	@Override
	public List<ObjectNode> getUserByNameaAndEmal(String query) {
		try {
			List<User> list = userController.getUserByNameaAndEmal(query);
			return JsonUtil.listToListNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Map<String, Object> calculateNodeUser(Map<String, Object> nodeMap) {
		try {
			return userController.calculateNodeUser(nodeMap);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getOrgListByDemId(String demId) {
		try {
			List<Org> list = orgController.getOrgListByDemId(demId);
			return JsonUtil.listToListNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

    @Override
    public CommonResult<JsonNode> getUserByOpenId(String openId) {
        try {
            CommonResult<JsonNode> jsonNodeCommonResult =  new CommonResult<>();
            CommonResult<UserVo> userVo = userController.getUserByOpenId(openId);
            jsonNodeCommonResult.setValue(JsonUtil.toJsonNode(userVo.getValue()));
            return jsonNodeCommonResult;
        } catch (Exception e) {
            throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
        }
    }

	@Override
	public Map<String, Map<String, String>> getUserRightMapByIds(Set<String> ids) {
		try {
			return userController.getUserRightMapByIds(ids);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getDefaultDem() {
		try {
			Demension dem = demensionController.getDefaultDem();
			return (ObjectNode) JsonUtil.toJsonNode(dem);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getSupOrgByCurrMain(String userId, String demId) {
		try {
			OrgUser orguser = orgController.getSupOrgByCurrMain(userId,demId);
			return (ObjectNode) JsonUtil.toJsonNode(orguser);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getAllDems() {
		try {
			List<Demension>  dems = demensionController.getAll();
			return JsonUtil.listToListNode(dems);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Map<String, Object> getDetailByAccountOrId(String account) {
		try {
			return userController.getDetailByAccountOrId(account);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}

	}

	@Override
	public CommonResult<String> postUserByAccount(String accounts, String openid) {
		try {
			return userController.postUserByAccount(accounts,openid);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

    @Override
    public ArrayNode getUserByIds(String ids) {
        try {
            List<UserVo> users = userController.getUserByIds(ids);
            return JsonUtil.listToArrayNode(users);
        } catch (Exception e) {
            throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
        }
    }

	@Override
	public CommonResult<String> addOrgFromExterUni(ObjectNode orgVo) {
		try {
			Org org = new Org();
			org.setId(orgVo.get("id")==null?"":orgVo.get("id").asText());
			org.setName(orgVo.get("name")==null?"":orgVo.get("name").asText());
			org.setCode(orgVo.get("code")==null?"":orgVo.get("code").asText());
			org.setDemId(orgVo.get("demId")==null?"":orgVo.get("demId").asText());
			org.setParentId(orgVo.get("parentId")==null?"":orgVo.get("parentId").asText());
			org.setOrderNo(orgVo.get("orderNo")==null?null:orgVo.get("orderNo").asLong());
			return orgController.addOrgFromExterUni(org);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
	
	@Override
	public CommonResult<JsonNode> getUserByMobile(String mobile) {
		CommonResult<UserVo> userVoCR = userController.getUserByMobile(mobile);
		try {
			UserVo userVo = userVoCR.getValue();
			JsonNode jsonNode = JsonUtil.toJsonNode(userVo);
			return new CommonResult<JsonNode>(userVoCR.getState(), userVoCR.getMessage(), jsonNode);
		}
		catch(Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<String> addUserFromExterUni(ObjectNode newUser) {
		try {
			UserVo user = new UserVo();
			user.setAccount(newUser.get("account").asText());
			user.setMobile(newUser.get("mobile").asText());
			user.setFullname(newUser.get("fullname").asText());
			user.setPassword(newUser.get("password").asText());
			return userController.addUserFromExterUni(user);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<String> addUsersForOrg(String orgCode, String accounts) {
		try {
			return orgController.addUsersForOrg(orgCode, accounts);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
	@Override
	public ArrayNode getUserInfoBySignData(ArrayNode customSignDatas) throws Exception {
		return userController.getUserInfoBySignData(customSignDatas);
	}
	@Override
	public List<String> getIgnoreMenuCodes(String tenantId) throws Exception {
		return tenantIgnoreMenuController.getIgnoreMenuCodes(tenantId);
	}
	@Override
	public JsonNode getTenantById(String id) {
		try {
			TenantManage tenantManage = tenantManageController.get(id);
			return JsonUtil.toJsonNode(tenantManage);
		} catch(Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getMainPostOrOrgByUserId(String userId) {
		try {
    		OrgUser mainPostByUserId = orgController.getMainPostOrOrgByUserId(userId);
    		if(BeanUtils.isEmpty(mainPostByUserId)) {
    			return null;
    		}
    		return (ObjectNode)JsonUtil.toJsonNode(mainPostByUserId);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
	
	@Override
	public CommonResult<Object> getFillOrg(String demId) throws Exception {
		CommonResult<Org> result = orgController.getFillOrg(Optional.ofNullable(""),null);
		if(result.getState() && BeanUtils.isNotEmpty(result.getValue())){
			return new CommonResult<Object>(true, result.getMessage(), JsonUtil.toJsonNode(result.getValue()));
		}
		return new CommonResult<Object>(false, result.getMessage());
	}
}
