package com.hotent.runtime.script;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.context.BaseContext;
import com.hotent.base.feign.BpmRuntimeFeignService;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.groovy.IScript;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.EncryptUtil;
import com.hotent.base.util.FluentUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.delegate.BpmDelegateTask;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.api.service.BpmTaskActionService;
import com.hotent.bpm.engine.def.BpmDefUtil;
import com.hotent.bpm.engine.task.cmd.DefaultTaskFinishCmd;
import com.hotent.bpm.listener.BusDataUtil;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.persistence.manager.BpmBusLinkManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.BpmBusLink;
import com.hotent.bpm.persistence.model.BpmExeStack;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.util.BoDataUtil;
import com.hotent.runtime.manager.IProcessManager;
import com.hotent.runtime.params.StartFlowParamObject;
import com.hotent.runtime.params.StartResult;
import com.hotent.uc.api.constant.GroupTypeConstant;
import com.hotent.uc.api.impl.service.UserServiceImpl;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.model.IUser;

/**
 * 系统配置中的常用脚本。
 * 
 * @author ray
 *
 */
@Component
public class ScriptImpl implements IScript {

	@Resource
	PortalFeignService portalFeignService;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	BoDataService boDataService;
	@Resource
	UCFeignService uCFeignService;
	@Resource
	IProcessManager iProcessService;
	@Resource
	UserServiceImpl userServiceImpl;
	@Resource
	BaseContext baseContext;

	private final String FIELD_PREFIX = "F_";

	/**
	 * 获取当前用户ID。
	 * 
	 * @return
	 */
	public Set<String> getCurrentUserIdSet() {
		Set<String> set = new HashSet<String>();
		set.add(ContextUtil.getCurrentUser().getUserId());
		return set;
	}

	/**
	 * 获取当前用户对象。
	 * 
	 * @return
	 */
	public IUser getCurrentUser() {
		return ContextUtil.getCurrentUser();
	}

	/**
	 * 获取当前用户ID。
	 * 
	 * @return
	 */
	public String getCurrentUserId() {
		return ContextUtil.getCurrentUser().getUserId();
	}

	/**
	 * 获取当前用户帐号。
	 * 
	 * @return
	 */
	public String getCurrentAccount() {
		return ContextUtil.getCurrentUser().getAccount();
	}

	/**
	 * 获取当前用户的当前组织id
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCurrentGroupId() throws Exception {
		return ContextUtil.getCurrentGroupId();
	}

	/**
	 * 获取当前用户的当前组织名称。
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getCurrentGroupName() throws IOException {
		IGroup currentGroup = ContextUtil.getCurrentGroup();
		if (currentGroup == null) {
			return null;
		}
		return currentGroup.getName();
	}

	/**
	 * 根据别名生成流水号。
	 * 
	 * <pre>
	 * 使用方法：
	 * scriptImpl.getNextNo("globalNo");
	 * </pre>
	 * 
	 * @param alias
	 *            流水号配置别名。
	 * @return
	 */
	public String getNextNo(String alias) {
		return portalFeignService.getNextIdByAlias(alias);
	}

	/**
	 * 判断命令是否是 DefaultTaskFinishCmd
	 * 
	 * @param processCmd
	 * @return
	 */
	public boolean isDefaultTaskFinishCmd(ActionCmd processCmd) {
		return DefaultTaskFinishCmd.class.getName().equals(processCmd.getClass().getName());
	}

	/**
	 * 判断数据是否在系统中存在。
	 * 
	 * @param boData
	 * @param fieldName
	 *            字段名
	 * @messages 消息
	 * @return
	 */
	public void validBoDataExist(ObjectNode boData, String fieldName, String messages) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");

		ObjectNode boEnt = (ObjectNode) boData.get("boEnt");

		boolean isAdd = true;

		String pk = boEnt.get("pkKey").asText().toLowerCase();

		if (boEnt.findValues("pk") != null && StringUtil.isNotEmpty(String.valueOf(boEnt.get(pk).asText()))) {
			isAdd = false;
		}
		String sql = "";
		Object[] aryObj = null;
		Object obj = boData.get(fieldName);
		String val = "";
		if (BeanUtils.isNotEmpty(obj)) {
			val = obj.toString().trim();
		}

		// 添加
		if (isAdd) {
			aryObj = new Object[1];
			aryObj[0] = val;
			sql = "select count(*) from " + boEnt.get("tableName").asText() + " where " + FIELD_PREFIX + fieldName
					+ "=?";
		}
		// 更新数据
		else {
			aryObj = new Object[2];
			aryObj[0] = val;
			aryObj[1] = boData.get(boEnt.get("pkKey").asText());
			sql = "select  count(*) from " + boEnt.get("tableName").asText() + " where " + FIELD_PREFIX + fieldName
					+ "=? and " + boEnt.get("pkKey").asText() + "!=?";
		}
		Integer rtn = jdbcTemplate.queryForObject(sql, aryObj, Integer.class);
		if (rtn > 0) {
			if (StringUtil.isNotEmpty(messages)) {
				throw new RuntimeException(messages);
			} else {
				throw new RuntimeException(val + "数据已经存在,请检查表单数据!");
			}
		}
	}

	/**
	 * 全部角色
	 * 
	 * @return
	 */
	public Set<BpmIdentity> getRoles() {
		Set<BpmIdentity> set = new LinkedHashSet<BpmIdentity>();
		List<ObjectNode> listRole = uCFeignService.getAllRole();
		for (ObjectNode role : listRole) {
			BpmIdentity identity = new DefaultBpmIdentity(role.get("id").asText(), role.get("name").asText(),
					BpmIdentity.TYPE_GROUP);
			identity.setType(BpmIdentity.TYPE_GROUP);
			identity.setExtractType(ExtractType.EXACT_NOEXACT);
			identity.setGroupType(GroupTypeConstant.ROLE.key());
			set.add(identity);
		}
		return set;
	}

	/**
	 * 判断某个用户是否属于某个角色
	 * 
	 * @param account
	 *            用户賬號
	 * @param roleAlias
	 *            角色别名
	 * @return
	 */
	public boolean isUserInRole(String account, String roleAlias) {
		if (StringUtil.isEmpty(account) || StringUtil.isEmpty(roleAlias)) {
			return false;
		}
		List<ObjectNode> listRole = uCFeignService.getRoleListByAccount(account);
		if (BeanUtils.isNotEmpty(listRole)) {
			for (ObjectNode role : listRole) {
				if (roleAlias.equals(role.get("alias").asText())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取cmd对象
	 * 
	 * @return
	 */
	public ActionCmd getActionCmd() {
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		return cmd;
	}

	/**
	 * 获取当前用户的主组织名称
	 * 
	 * @return
	 */
	public String getIUserMainOrgName() {
		ObjectNode org = uCFeignService.getMainGroup(ContextUtil.getCurrentUser().getUserId());
		if (BeanUtils.isNotEmpty(org)) {
			return org.get("name").asText();
		}
		return null;
	}

	/**
	 * 获取当前用户的主组织ID
	 * 
	 * @return
	 */
	public String getIUserMainOrgID() {
		ObjectNode org = uCFeignService.getMainGroup(ContextUtil.getCurrentUser().getUserId());
		if (BeanUtils.isNotEmpty(org)) {
			return org.get("id").asText();
		}
		return null;
	}

	/**
	 * 获取当前用户的岗位名称
	 * 
	 * @return
	 */
	public Set<String> getIUserPostName() {
		Set<String> set = new HashSet<String>();
		List<ObjectNode> orgRels = uCFeignService.getPosListByAccount(ContextUtil.getCurrentUser().getAccount());
		if (BeanUtils.isNotEmpty(orgRels)) {
			for (ObjectNode orgRel : orgRels) {
				set.add(orgRel.get("name").asText());
			}
		}
		return set;
	}

	/**
	 * 获取当前用户的岗位ID
	 * 
	 * @return
	 */
	public Set<String> getIUserPostID() {
		Set<String> set = new HashSet<String>();
		List<ObjectNode> orgRels = uCFeignService.getPosListByAccount(ContextUtil.getCurrentUser().getAccount());
		if (BeanUtils.isNotEmpty(orgRels)) {
			for (ObjectNode orgRel : orgRels) {
				set.add(orgRel.get("id").asText());
			}
		}
		return set;
	}

	/**
	 * 获取当前用户的角色名称
	 * 
	 * @return
	 */
	public Set<String> getIUserRoleName() {
		Set<String> set = new HashSet<String>();
		List<ObjectNode> roles = uCFeignService.getRoleListByAccount(ContextUtil.getCurrentUser().getUserId());
		if (BeanUtils.isNotEmpty(roles)) {
			for (ObjectNode role : roles) {
				set.add(role.get("name").asText());
			}
		}
		return set;
	}

	/**
	 * 获取当前用户的角色ID
	 * 
	 * @return
	 */
	public Set<String> getIUserRoleID() {
		Set<String> set = new HashSet<String>();
		List<ObjectNode> roles = uCFeignService.getRoleListByAccount(ContextUtil.getCurrentUser().getUserId());
		if (BeanUtils.isNotEmpty(roles)) {
			for (ObjectNode role : roles) {
				set.add(role.get("id").asText());
			}
		}
		return set;
	}

	/**
	 * 获取当前用户名称
	 * 
	 * @return
	 */
	public String getCurrentUserName() {
		return ContextUtil.getCurrentUser().getFullname();
	}

	/**
	 * 根据新闻公告id修改其数据审批状态为审批通过 1。审批中。2.审批通过
	 */
	public void getNewsStatusById() throws Exception {
		ActionCmd actionCmd = getActionCmd();
		BpmBusLinkManager bpmBusLinkManager = AppUtil.getBean(BpmBusLinkManager.class);
		if (BeanUtils.isNotEmpty(actionCmd)) {
			Map<String, BpmBusLink> linkMap = bpmBusLinkManager.getMapByInstId(actionCmd.getInstId());
			ObjectNode jsonObject = (ObjectNode) JsonUtil.toJsonNode(actionCmd.getBusData());
			// BO Map数据。
			Map<String, ObjectNode> jsonMap = BoDataUtil.getMap(jsonObject);
			FormFeignService formFeignService = AppUtil.getBean(FormFeignService.class);
			for (String key : jsonMap.keySet()) {
				ObjectNode def = formFeignService.getBodefByAlias(key);
				BpmBusLink busLink = linkMap.get(def.get("alias").asText());
				PortalFeignService portalFeignService = AppUtil.getBean(PortalFeignService.class);
				portalFeignService.publicMsgNews(busLink.getBusinesskeyStr());
			}
		}
	}

	public Boolean isSynchronize(String nodeIds, String actionName) throws Exception {
		Boolean flag = true;
		ActionCmd actionCmd = getActionCmd();
		if (BeanUtils.isNotEmpty(actionCmd)) {
			BpmRuntimeFeignService bean = AppUtil.getBean(BpmRuntimeFeignService.class);
			String lastNodeIds = "";// 最后一个任务节点ID
			if (BeanUtils.isNotEmpty(actionCmd.getTransitVars("parentStack"))) {
				BpmExeStack bpmExeStack = (BpmExeStack) actionCmd.getTransitVars("parentStack");
				lastNodeIds = bpmExeStack.getNodeId();
			}
			String lastActionName = actionCmd.getActionName();// 最后一个任务节点审批处理的状态
			flag = bean.isSynchronize(actionCmd.getInstId(), nodeIds, actionName, lastActionName, lastNodeIds);
		}
		return flag;
	}

	/**
	 * 获取当前日期，例如"2002-11-06"
	 * 
	 * @return
	 */
	public String getCurrentDate() {
		return TimeUtil.getCurrentDate();
	}

	/**
	 * 获取当前日期，按指定格式输出
	 * 
	 * @return
	 */
	public String getCurrentDateByStyle(String style) {
		return DateUtil.getCurrentTime(style);
	}

	/**
	 * 更新主表字段值
	 * 
	 * @param defCode
	 *            业务对象编码
	 * @param field
	 *            字段名称
	 * @param value
	 *            字段值
	 * @throws Exception
	 */
	public void updateMainField(String defCode, String field, Object value) throws Exception {
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		String instId = cmd.getInstId();
		DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(instId);
		if (StringUtil.isNotEmpty(defCode) && StringUtil.isNotEmpty(field) && BeanUtils.isNotEmpty(instance)) {
			String busData = cmd.getBusData();
			ObjectNode jsonObject = null;
			if (StringUtil.isEmpty(busData)) {
				List<ObjectNode> boDatas = boDataService.getDataByInst(instance);
				jsonObject = BoDataUtil.hanlerData(instance, "", boDatas);
			} else {
				jsonObject = (ObjectNode) JsonUtil.toJsonNode(busData);
			}
			Iterator<Entry<String, JsonNode>> iterator = jsonObject.fields();
			boolean isUpdate = false;
			while (iterator.hasNext()) {
				Entry<String, JsonNode> next = iterator.next();
				String key = next.getKey();
				if (key.equals(defCode)) {
					ObjectNode ywdxObj = (ObjectNode) next.getValue();
					if (BeanUtils.isNotEmpty(ywdxObj)) {
						Iterator<Entry<String, JsonNode>> fieldIterator = ywdxObj.fields();
						while (fieldIterator.hasNext()) {
							String fieldKey = fieldIterator.next().getKey();
							if (fieldKey.equals(field)) {
								if (value instanceof Integer) {
									ywdxObj.put(field, (int) value);
								} else {
									ywdxObj.put(field, (String) value);
								}
								isUpdate = true;
								break;
							}
						}
					}
				}
			}
			if (isUpdate) {

				DefaultBpmProcessDefExt bpmProcessDefExt = BpmDefUtil.getProcessExt(instance);
				// 验证BO数据。
				BoDataUtil.validBo(bpmProcessDefExt, jsonObject);
				// BO Map数据。
				Map<String, ObjectNode> jsonMap = BoDataUtil.getMap(jsonObject);
				List<ProcBoDef> list = bpmProcessDefExt.getBoDefList();
				List<ObjectNode> boDatas = new ArrayList<ObjectNode>();

				for (ProcBoDef boDef : list) {
					String key = boDef.getKey();
					if (!jsonMap.containsKey(key))
						continue;
					ObjectNode json = jsonMap.get(key);
					json = (ObjectNode) BoDataUtil.transJSON(json);
					String dataStr = JsonUtil.toJson(json);
					FormFeignService formFeignService = AppUtil.getBean(FormFeignService.class);
					ObjectNode def = formFeignService.getBodefByAlias(key);
					ObjectNode boEnt = formFeignService.getMainBOEntByDefAliasOrId("", def.get("id").asText());
					// BO数据。
					json.set("boDef", def);
					json.set("boEnt", boEnt);
					json.set("data", JsonUtil.toJsonNode(dataStr));
					boDatas.add(json);
				}
				for (ObjectNode boData : boDatas) {
					BusDataUtil.updateBoData(instId, boData);
				}
				cmd.setBusData(JsonUtil.toJson(jsonObject));
			}
		}
	}

	/**
	 * 更新子表字段值
	 * 
	 * @param mainDefCode
	 *            主表bo定义编码
	 * @param subDefCode
	 *            子表bo定义编码
	 * @param index
	 *            更新子表数据行序号
	 * @param field
	 *            更新子表字段
	 * @param value
	 *            更新子表字段值
	 * @throws Exception
	 */
	public void updateSubField(String mainDefCode, String subDefCode, int index, String field, Object value)
			throws Exception {
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		if (StringUtil.isNotEmpty(mainDefCode) && StringUtil.isNotEmpty(field)) {
			String busData = cmd.getBusData();
			ObjectNode jsonObject = (ObjectNode) JsonUtil.toJsonNode(busData);
			Iterator<Entry<String, JsonNode>> iterator = jsonObject.fields();
			boolean isUpdate = false;
			while (iterator.hasNext()) {
				Entry<String, JsonNode> next = iterator.next();
				String key = next.getKey();
				if (key.equals(mainDefCode)) {
					ObjectNode ywdxObj = (ObjectNode) next.getValue();
					if (BeanUtils.isNotEmpty(ywdxObj)) {
						Iterator<Entry<String, JsonNode>> fieldIterator = ywdxObj.fields();
						while (fieldIterator.hasNext()) {
							Entry<String, JsonNode> mainNext = fieldIterator.next();
							if (mainNext.getKey().contains("sub_")) {
								String fieldKey = mainNext.getKey().replace("sub_", "");
								if (fieldKey.equals(subDefCode)) {
									ArrayNode subArrayObj = (ArrayNode) mainNext.getValue();
									if (BeanUtils.isNotEmpty(subArrayObj) && subArrayObj.size() >= (index + 1)) {
										ObjectNode subObj = (ObjectNode) subArrayObj.get(index);
										Iterator<Entry<String, JsonNode>> subIterator = subObj.fields();
										while (subIterator.hasNext()) {
											Entry<String, JsonNode> subNext = subIterator.next();
											String subFieldKey = subNext.getKey();
											if (subFieldKey.equals(field)) {
												if (value instanceof Integer) {
													subObj.put(field, (int) value);
												} else {
													subObj.put(field, (String) value);
												}
												isUpdate = true;
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (isUpdate && BeanUtils.isNotEmpty(bpmProcessInstanceManager.get(cmd.getInstId()))) {

				DefaultBpmProcessInstance instance = bpmProcessInstanceManager.get(cmd.getInstId());
				DefaultBpmProcessDefExt bpmProcessDefExt = BpmDefUtil.getProcessExt(instance);
				// 验证BO数据。
				BoDataUtil.validBo(bpmProcessDefExt, jsonObject);
				// BO Map数据。
				Map<String, ObjectNode> jsonMap = BoDataUtil.getMap(jsonObject);
				List<ProcBoDef> list = bpmProcessDefExt.getBoDefList();
				List<ObjectNode> boDatas = new ArrayList<ObjectNode>();

				for (ProcBoDef boDef : list) {
					String key = boDef.getKey();
					if (!jsonMap.containsKey(key))
						continue;
					ObjectNode json = jsonMap.get(key);
					json = (ObjectNode) BoDataUtil.transJSON(json);
					String dataStr = JsonUtil.toJson(json);
					FormFeignService formFeignService = AppUtil.getBean(FormFeignService.class);
					ObjectNode def = formFeignService.getBodefByAlias(key);
					ObjectNode boEnt = formFeignService.getMainBOEntByDefAliasOrId("", def.get("id").asText());
					// BO数据。
					json.set("boDef", def);
					json.set("boEnt", boEnt);
					json.set("data", JsonUtil.toJsonNode(dataStr));
					boDatas.add(json);
				}
				for (ObjectNode boData : boDatas) {
					BusDataUtil.updateBoData(cmd.getInstId(), boData);
				}
				cmd.setBusData(JsonUtil.toJson(jsonObject));
			}
		}
	}

	/**
	 * 获取子表字段值
	 * 
	 * @param ywdxCode
	 *            业务对象编码
	 * @param subTableName
	 *            子表表名
	 * @param field
	 *            字段名称
	 * @return
	 * @throws IOException
	 */
	public Object getSubFiledValue(String defCode, String subTableName, String field) throws IOException {
		ActionCmd cmd = ContextThreadUtil.getActionCmd();
		if (StringUtil.isNotEmpty(defCode) && StringUtil.isNotEmpty(subTableName) && StringUtil.isNotEmpty(field)) {
			String busData = cmd.getBusData();
			ObjectNode jsonObject = (ObjectNode) JsonUtil.toJsonNode(busData);
			Iterator<Entry<String, JsonNode>> iterator = jsonObject.fields();
			while (iterator.hasNext()) {
				String key = iterator.next().getKey();
				if (key.equals(defCode)) {
					ObjectNode ywdxObj = (ObjectNode) jsonObject.get(key);
					if (BeanUtils.isNotEmpty(ywdxObj)) {
						Iterator<Entry<String, JsonNode>> fieldIterator = ywdxObj.fields();
						while (fieldIterator.hasNext()) {
							String fieldKey = fieldIterator.next().getKey();
							String subName = "sub_" + subTableName;
							if (fieldKey.equals(subName)) {
								ObjectNode subObj = (ObjectNode) jsonObject.get(subName);
								if (BeanUtils.isNotEmpty(subObj)) {
									Iterator<Entry<String, JsonNode>> subIterator = ywdxObj.fields();
									while (subIterator.hasNext()) {
										String subFiledKey = fieldIterator.next().getKey();
										if (subFiledKey.equals(field)) {
											return ywdxObj.get(field);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static boolean littleThen(String st1, String st2) {
		if (BeanUtils.isNotEmpty(st1) && BeanUtils.isNotEmpty(st1)) {
			return st1.compareTo(st2) < 0;
		}
		return false;
	}

	public static boolean LargeThen(String st1, String st2) {
		if (BeanUtils.isNotEmpty(st1) && BeanUtils.isNotEmpty(st1)) {
			return st1.compareTo(st2) > 0;
		}
		return false;
	}

	public static boolean equals(String st1, String st2) {
		if (BeanUtils.isEmpty(st1) && BeanUtils.isEmpty(st2)) {
			return true;
		}
		if (BeanUtils.isNotEmpty(st1) && BeanUtils.isNotEmpty(st1)) {
			return st1.equals(st2);
		}
		return false;
	}

	public static boolean contains(String st1, String st2) {
		if (BeanUtils.isNotEmpty(st1) && BeanUtils.isNotEmpty(st1)) {
			return st1.contains(st2);
		}
		return false;
	}

	/**
	 * 开始时间是否大于结束时间
	 *
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static boolean isDateLarge(LocalDateTime startTime, LocalDateTime endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return false;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return true;
		}
		return startTime.toEpochSecond(ZoneOffset.of("+8")) - endTime.toEpochSecond(ZoneOffset.of("+8")) > 0;
	}

	public static boolean isDateLarge(LocalDateTime startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return false;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return true;
		}
		try {
			LocalDateTime date = DateFormatUtil.parse(endTime);
			return startTime.toEpochSecond(ZoneOffset.of("+8")) - date.toEpochSecond(ZoneOffset.of("+8")) > 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isDateLarge(String startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return false;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return true;
		}
		try {
			LocalDateTime sdate = DateFormatUtil.parse(startTime);
			LocalDateTime date = DateFormatUtil.parse(endTime);
			return sdate.toEpochSecond(ZoneOffset.of("+8")) - date.toEpochSecond(ZoneOffset.of("+8")) > 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 开始时间是否大于等于结束时间
	 *
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static boolean isDateLargeEquals(LocalDateTime startTime, LocalDateTime endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return false;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return true;
		}
		return startTime.toEpochSecond(ZoneOffset.of("+8")) - endTime.toEpochSecond(ZoneOffset.of("+8")) >= 0;
	}

	public static boolean isDateLargeEquals(LocalDateTime startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return false;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return true;
		}
		try {
			LocalDateTime date = DateFormatUtil.parse(endTime);
			return startTime.toEpochSecond(ZoneOffset.of("+8")) - date.toEpochSecond(ZoneOffset.of("+8")) >= 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isDateLargeEquals(String startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return false;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return true;
		}
		try {
			LocalDateTime sdate = DateFormatUtil.parse(startTime);
			LocalDateTime date = DateFormatUtil.parse(endTime);
			return sdate.toEpochSecond(ZoneOffset.of("+8")) - date.toEpochSecond(ZoneOffset.of("+8")) >= 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 开始时间是否小于结束时间
	 *
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static boolean isDateLittle(LocalDateTime startTime, LocalDateTime endTime) {
		if (BeanUtils.isEmpty(startTime) && BeanUtils.isNotEmpty(endTime)) {
			return true;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return false;
		}
		return startTime.toEpochSecond(ZoneOffset.of("+8")) - endTime.toEpochSecond(ZoneOffset.of("+8")) < 0;
	}

	public static boolean isDateLittle(LocalDateTime startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return true;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return false;
		}
		try {
			LocalDateTime date = DateFormatUtil.parse(endTime);
			return startTime.toEpochSecond(ZoneOffset.of("+8")) - date.toEpochSecond(ZoneOffset.of("+8")) < 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}

	public static boolean isDateLittle(String startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return true;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return false;
		}
		try {
			LocalDateTime sdate = DateFormatUtil.parse(startTime);
			LocalDateTime edate = DateFormatUtil.parse(endTime);
			return sdate.toEpochSecond(ZoneOffset.of("+8")) - edate.toEpochSecond(ZoneOffset.of("+8")) < 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 开始时间是否小于等于结束时间
	 *
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static boolean isDateLittleEquals(LocalDateTime startTime, LocalDateTime endTime) {
		if (BeanUtils.isEmpty(startTime) && BeanUtils.isNotEmpty(endTime)) {
			return true;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return false;
		}
		return startTime.toEpochSecond(ZoneOffset.of("+8")) - endTime.toEpochSecond(ZoneOffset.of("+8")) <= 0;
	}

	public static boolean isDateLittleEquals(LocalDateTime startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return true;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return false;
		}
		try {
			LocalDateTime date = DateFormatUtil.parse(endTime);
			return startTime.toEpochSecond(ZoneOffset.of("+8")) - date.toEpochSecond(ZoneOffset.of("+8")) <= 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}

	public static boolean isDateLittleEquals(String startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime)) {
			return true;
		}
		if (BeanUtils.isEmpty(endTime)) {
			return false;
		}
		try {
			LocalDateTime sdate = DateFormatUtil.parse(startTime);
			LocalDateTime edate = DateFormatUtil.parse(endTime);
			return sdate.toEpochSecond(ZoneOffset.of("+8")) - edate.toEpochSecond(ZoneOffset.of("+8")) <= 0;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 开始时间是否等于结束时间
	 *
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static boolean isDateEquals(LocalDateTime startTime, LocalDateTime endTime) {
		if (BeanUtils.isEmpty(startTime) && BeanUtils.isEmpty(endTime)) {
			return true;
		}
		if (BeanUtils.isNotEmpty(startTime) && BeanUtils.isNotEmpty(endTime)) {
			return startTime.toEpochSecond(ZoneOffset.of("+8")) - endTime.toEpochSecond(ZoneOffset.of("+8")) == 0;
		}
		return false;

	}

	public static boolean isDateEquals(LocalDateTime startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime) && BeanUtils.isEmpty(endTime)) {
			return true;
		}
		if (BeanUtils.isNotEmpty(startTime) && BeanUtils.isNotEmpty(endTime)) {
			try {
				LocalDateTime date = DateFormatUtil.parse(endTime);
				return startTime.toEpochSecond(ZoneOffset.of("+8")) - date.toEpochSecond(ZoneOffset.of("+8")) < 0;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean isDateEquals(String startTime, String endTime) {
		if (BeanUtils.isEmpty(startTime) && BeanUtils.isEmpty(endTime)) {
			return true;
		}
		if (BeanUtils.isNotEmpty(startTime) && BeanUtils.isNotEmpty(endTime)) {
			try {
				LocalDateTime sdate = DateFormatUtil.parse(startTime);
				LocalDateTime date = DateFormatUtil.parse(endTime);
				return sdate.toEpochSecond(ZoneOffset.of("+8")) - date.toEpochSecond(ZoneOffset.of("+8")) < 0;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 日期时间是否属于范围内
	 * 
	 * @param time
	 *            需要比较的时间
	 * @param boundary
	 *            范围，两个时间逗号隔开：如 2020-05-06 00:00:00,2020-06-25 01:01:01
	 * @return
	 */
	public static boolean isDateBelongTo(String time, String boundary) {
		if (BeanUtils.isEmpty(time) || BeanUtils.isEmpty(boundary)) {
			return false;
		}
		try {
			LocalDateTime date = DateFormatUtil.parse(time);
			String[] boundaryArr = boundary.split(",");
			if (boundaryArr.length != 2) {
				return false;
			}
			LocalDateTime sdate = DateFormatUtil.parse(boundaryArr[0]);
			LocalDateTime edate = DateFormatUtil.parse(boundaryArr[1]);
			return date.toEpochSecond(ZoneOffset.of("+8")) >= sdate.toEpochSecond(ZoneOffset.of("+8"))
					&& date.toEpochSecond(ZoneOffset.of("+8")) <= edate.toEpochSecond(ZoneOffset.of("+8"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void testStart(String instid) throws Exception {
		// InstFormAndBoVo instFormAndBO = iFlowService.getInstFormAndBO(instid,
		// null,FormType.PC);
		// ObjectNode datas = instFormAndBO.getData();
		//
		//
		// BpmRuntimeFeignService service =
		// AppUtil.getBean(BpmRuntimeFeignService.class);
		// JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
		// ObjectNode resNode = new ObjectNode(nodeFactory);
		//
		// //
		// String json = JsonUtil.toJson(datas);
		// String basedata = Base64.getBase64(json);
		//
		StartFlowParamObject startFlowParamObject = new StartFlowParamObject();
		startFlowParamObject.setFlowKey("csjdlc");
		startFlowParamObject.setAccount("admin");
		ContextThreadUtil.clearTaskMap();
		ContextThreadUtil.cleanCommuVars();
		ContextUtil.clearAll();
		StartResult start = iProcessService.start(startFlowParamObject);
		System.out.println(start.getState());

	}

	public void startNewWithCurFlowData(String startUsers, String newFlowKey) throws Exception {

		if (StringUtil.isEmpty(startUsers)) {
			return;
		}
		List<IUser> users = userServiceImpl.getUserByAccounts(startUsers);
		if (BeanUtils.isEmpty(users)) {
			return;
		}

		ActionCmd actionCmd = getActionCmd();

		StartFlowParamObject startFlowParamObject = new StartFlowParamObject();
		startFlowParamObject.setFlowKey(newFlowKey);
		startFlowParamObject.setData(Base64.getBase64(actionCmd.getBusData()));

		ExecutorService executorService = Executors.newCachedThreadPool();

		for (IUser iUser : users) {
			executorService.execute(() -> {
				try {
					ContextUtil.setCurrentUser(iUser);
					baseContext.setTempTenantId(iUser.getTenantId());
					startFlowParamObject.setAccount(iUser.getAccount());
					iProcessService.start(startFlowParamObject);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	/**
	 * 数字是否属于范围内
	 * 
	 * @param num
	 *            需要比较的数字
	 * @param boundary
	 *            范围，两个时间逗号隔开：如 0,100
	 * @return
	 */
	public static boolean isNumberBelongTo(Integer num, String boundary) {
		if (BeanUtils.isEmpty(num) || BeanUtils.isEmpty(boundary)) {
			return false;
		}
		String[] boundaryArr = boundary.split(",");
		if (boundaryArr.length != 2) {
			return false;
		}
		Integer start = Integer.valueOf(boundaryArr[0]);
		Integer end = Integer.valueOf(boundaryArr[1]);
		return start <= num && num <= end;
	}

	/**
	 * 字符串是否属于数组集合内
	 * 
	 * @param target
	 *            需要比较的字符串
	 * @param boundary
	 *            英文逗号隔开的字符串数组：如 aaa,bbb,ccc
	 * @return
	 */
	public static boolean isStringBelongTo(String target, String boundary) {
		if (BeanUtils.isEmpty(target) || BeanUtils.isEmpty(boundary)) {
			return false;
		}
		String[] boundaryArr = boundary.split(",");
		if (boundaryArr.length <= 0) {
			return false;
		}
		List<String> list = Arrays.asList(boundaryArr);
		return list.contains(target);
	}

	/**
	 * demo脚本，根据所传账号查询用户，使用用户名更新表单字段
	 * 
	 * @param account
	 * @param attrName
	 * @param formPath
	 * @throws Exception
	 */
	public void updateFormData(String account, String bodefName, String fileName) throws Exception {
		if (StringUtil.isNotEmpty(account) && StringUtil.isNotEmpty(bodefName) && StringUtil.isNotEmpty(fileName)) {
			ObjectNode header = JsonUtil.getMapper().createObjectNode();
			String token = "admin:" + EncryptUtil.encrypt("feignCallEncry");
			header.put("Authorization", "Basic " + Base64.getBase64(token));
			String string = FluentUtil.get(
					"http://www.hotent.xyz:8088/api/user/v1/user/getUser?userNumber" + "" + "&account=" + account,
					Base64.getBase64(JsonUtil.toJson(header)));
			if (BeanUtils.isNotEmpty(string)) {
				ObjectNode jsonNode = (ObjectNode) JsonUtil.toJsonNode(string);
				String userName = jsonNode.get("fullname").asText();
				updateMainField(bodefName, fileName, userName);
			}
		}
	}

	/**
	 * 
	 * @param account
	 * @param checkValue
	 * @param destination
	 * @throws Exception
	 */
	public void getDataAndCheckBack(BpmDelegateTask delegateTask, String account, String checkValue, String skipNode)
			throws Exception {
		if (StringUtil.isNotEmpty(account) && StringUtil.isNotEmpty(checkValue)) {
			ObjectNode header = JsonUtil.getMapper().createObjectNode();
			String token = "admin:" + EncryptUtil.encrypt("feignCallEncry");
			header.put("Authorization", "Basic " + Base64.getBase64(token));
			String string = FluentUtil.get(
					"http://www.hotent.xyz:8088/api/user/v1/user/getUser?userNumber" + "" + "&account=" + account,
					Base64.getBase64(JsonUtil.toJson(header)));
			if (BeanUtils.isNotEmpty(string)) {
				ObjectNode jsonNode = (ObjectNode) JsonUtil.toJsonNode(string);
				String userName = jsonNode.get("fullname").asText();
				if (userName.equals(checkValue)) {

					ExecutorService executorService = Executors.newCachedThreadPool();
					executorService.execute(() -> {
						
						try {
						    IUser userByAccount = userServiceImpl.getUserByAccount("admin");
						    Thread.sleep(2000);
							ContextUtil.setCurrentUser(userByAccount);
							baseContext.setTempTenantId(userByAccount.getTenantId());
							DefaultTaskFinishCmd cmd = new DefaultTaskFinishCmd();
							ContextThreadUtil.setActionCmd(cmd);
							cmd.setTaskId(delegateTask.getId());
							if (StringUtil.isEmpty(skipNode)) {
								cmd.addTransitVars(BpmConstants.SKIP_NODE,"UserTask4" );
							} else {
								cmd.addTransitVars(BpmConstants.SKIP_NODE,skipNode);
							}
							cmd.setActionName("agree");
							BpmTaskActionService bpmTaskActionService = AppUtil.getBean(BpmTaskActionService.class);
							bpmTaskActionService.finishTask(cmd);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					});
				}
			}
		}
	}

	/**
	 *  获取当前年份
	 * @return 年份
	 */
	public String getCurrentYear(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
		return LocalDateTime.now().format(formatter);
	}

	/**
	 *  获取当前月份
	 * @return 月份
	 */
	public String getCurrentMonth(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
		return LocalDateTime.now().format(formatter);
	}
	
	/**
	 * 获取当前用户填制单位id
	 * @return
	 * @throws Exception
	 */
	public String getFillOrgId(String demId) throws Exception {
		String orgId = "";
		CommonResult<Object> result =  uCFeignService.getFillOrg(demId);
		if(result.getState()){
			JsonNode orgNode = (JsonNode) result.getValue();
			if(orgNode.has("id")){
				orgId = orgNode.get("id").asText();
			}
		}
		if(StringUtil.isEmpty(orgId)){
			throw new RuntimeException("获取当前填制单位失败！");
		}
		return orgId;
	}
	
	/**
	 * 获取当前用户填制单位
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getFillOrg(String demId) throws Exception {
		CommonResult<Object> result =  uCFeignService.getFillOrg(demId);
		Map<String,String> org = null;
		if(result.getState()){
			JsonNode orgNode = (JsonNode) result.getValue();
			if(orgNode.has("id")){
				org = new HashMap<String, String>();
				org.put("id", orgNode.get("id").asText());
				org.put("code", orgNode.get("code").asText());
				org.put("name", orgNode.get("name").asText());
			}
		}
		if(BeanUtils.isEmpty(org)){
			throw new RuntimeException("获取当前填制单位失败！");
		}
		return org;
	}

}
