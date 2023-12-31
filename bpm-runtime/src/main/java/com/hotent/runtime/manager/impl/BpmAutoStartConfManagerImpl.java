package com.hotent.runtime.manager.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.context.BaseContext;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.time.DateUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.service.BoDataService;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.util.BoDataUtil;
import com.hotent.runtime.dao.BpmAutoStartConfDao;
import com.hotent.runtime.manager.BpmAutoStartConfManager;
import com.hotent.runtime.manager.IProcessManager;
import com.hotent.runtime.model.BpmAutoStartConf;
import com.hotent.runtime.params.StartFlowParamObject;
import com.hotent.runtime.params.StartResult;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre>
 *  
 * 描述：流程自动发起配置表 处理实现类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-04-07 10:51:28
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("boAutoStartConfManager")
public class BpmAutoStartConfManagerImpl extends BaseManagerImpl<BpmAutoStartConfDao, BpmAutoStartConf>
implements BpmAutoStartConfManager {
	@Resource
	BpmAutoStartConfDao boAutoStartConfDao;
	@Resource
	BpmIdentityExtractService bpmIdentityExtractService;
	@Resource
	BoDataService boDataService;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	IProcessManager iProcessService;
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	BaseContext baseContext;

	@Override
	public BpmAutoStartConf getByDefKey(String defKey) {
		return this.getOne(Wrappers.<BpmAutoStartConf> lambdaQuery().eq(BpmAutoStartConf::getDefKey, defKey));
	}

	@Override
	public ObjectNode defAutoStart() throws Exception {
		List<BpmAutoStartConf> all = null;
		try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
			all = super.getAll();
		}

		if (BeanUtils.isNotEmpty(all)) {
			for (BpmAutoStartConf conf : all) {
				String trigger = conf.getTrigger();	
				String startUser = conf.getStartUser();
				// 1,未配置启动人，2，未配置触发时机，3如果未到启动时间或者已过。 则跳过
				if (StringUtil.isEmpty(startUser) || StringUtil.isEmpty(trigger) || !checkTrigger(trigger)) {
					continue;
				}
				// 将流程的租户id设置为当前线程的租户id
				baseContext.setTempTenantId(conf.getTenantId());

				List<DefaultBpmIdentity> list = JsonUtil.toBean(startUser,
						new TypeReference<List<DefaultBpmIdentity>>() {
				});
				if (BeanUtils.isEmpty(list)) {
					continue;
				}
				List<BpmIdentity> bpmIdentities = new ArrayList<>();
				for (DefaultBpmIdentity identity : list) {
					if (StringUtil.isNotEmpty(identity.getId()) || StringUtil.isNotEmpty(identity.getCode())) {
						bpmIdentities.add(identity);
					}
				}

				List<IUser> extractUser = bpmIdentityExtractService.extractUser(bpmIdentities);
				if (BeanUtils.isEmpty(extractUser)) {
					continue;
				}
				DefaultBpmDefinition mainDef = bpmDefinitionManager.getMainByDefKey(conf.getDefKey(), false);
				List<ObjectNode> boDatas = boDataService.getDataByDefId(mainDef.getId());
				ObjectNode jsondata = BoDataUtil.hanlerData(mainDef.getId(), boDatas);
				ExecutorService executorService = Executors.newCachedThreadPool();
				for (IUser iUser : extractUser) {
					executorService.execute(() -> {
						try {
							startByConf(iUser, conf, jsondata, mainDef);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}
				baseContext.clearTempTenantId();
			}
		}
		return (ObjectNode) JsonUtil.toJsonNode(new CommonResult<>("执行成功"));
	}

	/**
	 * 根据配置自动启动流程
	 * 
	 * @param iUser
	 * @param conf
	 * @param jsondata
	 * @return
	 * @throws Exception
	 */
	private StartResult startByConf(IUser iUser, BpmAutoStartConf conf, ObjectNode jsondata,
			DefaultBpmDefinition mainDef) throws Exception {
		ContextUtil.setCurrentUser(iUser);
		baseContext.setTempTenantId(conf.getTenantId());
		StartFlowParamObject sObject = new StartFlowParamObject();
		sObject.setAccount(iUser.getAccount());
		sObject.setDefId(mainDef.getDefId());
		sObject.setFlowKey(mainDef.getDefKey());
		sObject.setExpression(BpmAutoStartConf.START_OPINION);
		String formData = conf.getFormData();
		if (StringUtil.isNotEmpty(formData)) {
			ObjectNode dataConf = (ObjectNode) JsonUtil.toJsonNode(formData);
			Map<String, Object> varMap = new HashMap<>();
			varMap.put("startUser", iUser.getUserId());
			varMap.put("flowKey_", mainDef.getDefKey());
			for (Iterator<Entry<String, JsonNode>> iterator = jsondata.fields(); iterator.hasNext();) {
				Entry<String, JsonNode> ent = iterator.next();
				String entKey = ent.getKey();
				if (ent.getValue() instanceof ObjectNode) {
					ObjectNode entity = (ObjectNode) ent.getValue();
					for (Iterator<Entry<String, JsonNode>> iterator2 = entity.fields(); iterator2.hasNext();) {
						Entry<String, JsonNode> filed = iterator2.next();
						String filePath = entKey + "." + filed.getKey();
						if (BeanUtils.isNotEmpty(dataConf.get(filePath))) {
							Object executeObject = groovyScriptEngine.executeObject(dataConf.get(filePath).asText(),
									varMap);
							JsonUtil.putObjectToJson(entity, filed.getKey(), executeObject);
						}
					}
				}

			}
		}
		sObject.setData(Base64.getBase64(JsonUtil.toJson(jsondata)));
		return iProcessService.start(sObject);
	}

	/**
	 * 检查流程是否到了配置的启动时间
	 * 
	 * @param trigger
	 * @return
	 * @throws IOException
	 */
	private boolean checkTrigger(String trigger) throws IOException {
		ObjectNode triggerObj = (ObjectNode) JsonUtil.toJsonNode(trigger);
		if (!triggerObj.hasNonNull("rdoTimeType")) {
			return false;
		}
		String triggerType = triggerObj.get("rdoTimeType").asText();
		String curDateStr = DateUtil.getCurrentTime("HH:mm");
		String triggerDate = "";
		switch (triggerType) {
		// 只触发一次
		case "1":
			curDateStr = DateUtil.getCurrentTime("yyyy-MM-dd HH:mm");
			triggerDate = JsonUtil.getString(triggerObj, "sampleDate");
			if (StringUtil.isNotEmpty(triggerDate)) {
				triggerDate = TimeUtil.getDateString(TimeUtil.convertString(triggerDate), "yyyy-MM-dd HH:mm");
			}
			// 如果到了触发时间，则返回可以执行的结果
			if (curDateStr.equals(triggerDate)) {
				return true;
			}
			break;
			// 每天定时触发
		case "2":
			triggerDate = JsonUtil.getString(triggerObj, "txtDay");
			// 如果到了触发时间，则返回可以执行的结果
			if (curDateStr.equals(triggerDate)) {
				return true;
			}
			break;
			// 每周定时触发
		case "4":
			triggerDate = JsonUtil.getString(triggerObj, "txtWeek");
			String chkWeek = JsonUtil.getString(triggerObj, "chkWeek");
			// 如果到了触发时间，则返回可以执行的结果
			if (curDateStr.equals(triggerDate)
					&& chkWeek.indexOf(String.valueOf(LocalDateTime.now().getDayOfWeek().getValue())) > -1) {
				return true;
			}
			break;
		case "5":
			triggerDate = triggerObj.get("txtMon").asText();
			String chkMons = JsonUtil.getString(triggerObj, "chkMons");
			// 如果到了触发时间，则返回可以执行的结果
			if (curDateStr.equals(triggerDate)
					&& chkMons.indexOf(String.valueOf(LocalDateTime.now().getDayOfMonth())) > -1) {
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}

}
