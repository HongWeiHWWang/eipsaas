package com.hotent.runtime.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.groovy.IUserScript;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.cmd.ActionCmd;
import com.hotent.bpm.api.cmd.BaseActionCmd;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.context.ContextThreadUtil;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.runtime.manager.BpmSelectorDefManager;
import com.hotent.runtime.manager.ConditionScriptManager;
import com.hotent.runtime.model.BpmSelectorDef;
import com.hotent.runtime.model.ConditionScript;
import com.hotent.runtime.model.IConditionScript;
import com.hotent.uc.api.impl.service.UserServiceImpl;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre>
 * 描述：sys_multi_script管理
 * 构建组：x5-bpmx-platform
 * 作者:helh
 * 邮箱:helh@jee-soft.cn
 * 日期:2014-05-08 15:33:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/runtime/conditionScript/v1/")
@Api(tags = "条件脚本管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class ConditionScriptController extends BaseController<ConditionScriptManager,ConditionScript> {

	@Resource
	private FormFeignService formFeignService;

	@Resource
	private ConditionScriptManager conditionScriptManager;

	@RequestMapping(value = "list", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "条件脚本列表(分页条件查询)", httpMethod = "POST", notes = "条件脚本列表(分页条件查询)")
	public PageList<ConditionScript> listJson(
			@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(name = "type", value = "脚本类型") @RequestParam Optional<Integer> type) throws Exception {
		Integer itype = type.orElse(null);
		if (BeanUtils.isNotEmpty(itype)) {
			queryFilter.addFilter("type", itype, QueryOP.EQUAL, FieldRelation.AND, "2");
		}
		PageList<ConditionScript> fileList = (PageList<ConditionScript>) conditionScriptManager.query(queryFilter);
		return fileList;
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取脚本设置明细", httpMethod = "GET", notes = "获取脚本设置明细")
	public ObjectNode edit(@ApiParam(name = "id", value = "脚本id") @RequestParam String id) throws Exception {
		// type=1条件脚本，type=2人员脚本
		ConditionScript conditionScript = null;
		if (StringUtil.isNotEmpty(id)) {
			conditionScript = conditionScriptManager.get(id);
		} else {
			conditionScript = new ConditionScript();
			conditionScript.setType(1);
		}
		BpmSelectorDefManager bpmSelectorDefManager = AppUtil.getBean(BpmSelectorDefManager.class);
		QueryFilter queryFilter = QueryFilter.<BpmSelectorDef>build().withPage(new PageBean(1, Integer.MAX_VALUE));
		PageList<BpmSelectorDef> pageList = bpmSelectorDefManager.query(queryFilter);// 控件
		List<BpmSelectorDef> bpmSelectorDefs = pageList.getRows();
		List<Object> customDialogs = formFeignService.getCustomDialogs();
		List<ParaCtOtion> controlBindList = new ArrayList<ParaCtOtion>();
		ArrayNode optionJson = JsonUtil.getMapper().createArrayNode();
		if (bpmSelectorDefs.size() > 0) {
			ObjectNode optgroup = JsonUtil.getMapper().createObjectNode();
			ArrayNode selectors = JsonUtil.getMapper().createArrayNode();
			for (BpmSelectorDef selector : bpmSelectorDefs) {
				ObjectNode select = JsonUtil.getMapper().createObjectNode();
				select.put("value", "selector:" + selector.getAlias());
				select.put("name", selector.getName());
				selectors.add(select);
				ParaCtOtion option = new ParaCtOtion();
				option.id = "selector:" + selector.getAlias();
				option.option = selector.getGroupField();
				controlBindList.add(option);
			}
			optgroup.put("lable", "选择器组合控件");
			optgroup.set("option", selectors);
			optionJson.add(optgroup);
		}

		if (customDialogs.size() > 0) {
			ObjectNode optgroup = JsonUtil.getMapper().createObjectNode();
			ArrayNode cusdgs = JsonUtil.getMapper().createArrayNode();
			for (Object obj : customDialogs) {
				ObjectNode cus = (ObjectNode) JsonUtil.toJsonNode(obj);
				cus.put("value", "cusdg:" + cus.get("alias").asText());
				cus.put("name", cus.get("name").asText());
				cusdgs.add(cus);
				ParaCtOtion option = new ParaCtOtion();
				option.id = "cusdg:" + cus.get("alias").asText();
				if (BeanUtils.isNotEmpty(cus.get("resultfield"))
						&& StringUtil.isNotEmpty(cus.get("resultfield").toString())) {
					option.option = cus.get("resultfield").toString().replace("\"field\":", "\"name\":")
							.replace("\"comment\":", "\"key\":");
				}
				controlBindList.add(option);
			}
			optgroup.put("lable", "选择自定义对话框");
			optgroup.set("option", cusdgs);
			optionJson.add(optgroup);
		}
		ObjectNode result = JsonUtil.getMapper().createObjectNode();
		if (BeanUtils.isNotEmpty(controlBindList)) {
			// 选择器的Json
			ArrayNode controlBindSourceJson = (ArrayNode) JsonUtil.toJsonNode(controlBindList);
			String jsonStr = controlBindSourceJson.toString();
			result.put("controlBindSourceJson", jsonStr);
		}
		result.set("conditionScript", JsonUtil.toJsonNode(conditionScript));
		result.set("optionJson", optionJson);
		result.set("bpmSelectorDefs", JsonUtil.toJsonNode(bpmSelectorDefs));
		return result;
	}

	@RequestMapping(value = "getImplClasses", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据指定的接口或基类获取实现类列表", httpMethod = "GET", notes = "根据指定的接口或基类获取实现类列表")
	public List<Class> getImplClasses(
			@ApiParam(name = "type", value = "脚本类型（1：条件脚本，2：人员脚本 ）", required = true) @RequestParam Integer type)
			throws Exception {
		List<Class> implClasses = null;
		if (type == 1) {
			implClasses = AppUtil.getImplClass(IConditionScript.class);
		} else if (type == 2) {
			implClasses = AppUtil.getImplClass(IUserScript.class);
		}
		return implClasses;
	}

	public class ParaCtOtion {
		protected String id; // 控件类型ID
		protected String option; // 控件类型可以绑定的字段列表

		public void setId(String id) {
			this.id = id;
		}

		public String getId() {
			return this.id;
		}

		public void setOption(String option) {
			this.option = option;
		}

		public String getOption() {
			return this.option;
		}
	}

	@RequestMapping(value = "get", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取脚本明细", httpMethod = "GET", notes = "获取脚本明细")
	public ConditionScript get(@ApiParam(name = "id", value = "脚本id", required = true) @RequestParam String id)
			throws Exception {
		ConditionScript conditionScript = null;
		if (StringUtil.isNotEmpty(id)) {
			conditionScript = conditionScriptManager.get(id);
		}
		return conditionScript;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存脚本信息", httpMethod = "POST", notes = "保存脚本信息")
	public CommonResult<String> save(
			@ApiParam(name = "conditionScript", value = "脚本设置", required = true) @RequestBody ConditionScript conditionScript)
			throws Exception {
		String resultMsg = null;
		String id = conditionScript.getId();
		try {
			if (StringUtil.isEmpty(id)) {
				conditionScript.setId(UniqueIdUtil.getSuid());
				conditionScriptManager.create(conditionScript);
				resultMsg = "添加脚本成功";
			} else {
				conditionScriptManager.update(conditionScript);
				resultMsg = "更新脚本成功";
			}
			return new CommonResult<String>(true, resultMsg);
		} catch (Exception e) {
			return new CommonResult<String>(false, resultMsg + "," + e.getMessage());
		}
	}

	@RequestMapping(value = "remove", method = RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除脚本设置", httpMethod = "DELETE", notes = "删除脚本设置")
	public CommonResult<String> remove(
			@ApiParam(name = "ids", value = "流程的测试用例设置id，多个用“,”号分隔", required = true) @RequestParam String ids)
			throws Exception {
		try {
			String[] aryIds = null;
			if (!StringUtil.isEmpty(ids)) {
				aryIds = ids.split(",");
			}
			conditionScriptManager.removeByIds(aryIds);
			return new CommonResult<String>(true, "删除成功");
		} catch (Exception e) {
			return new CommonResult<String>(false, "删除失败");
		}
	}

	@RequestMapping(value = "getMethodsByName", method = RequestMethod.GET, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "通过类名获取类的所有方法", httpMethod = "GET", notes = "通过类名获取类的所有方法")
	public CommonResult<String> getMethodsByName(
			@ApiParam(name = "className", value = "类名", required = true) @RequestParam String className,
			@ApiParam(name = "id", value = "id") @RequestParam String id,
			@ApiParam(name = "type", value = "脚本类型") @RequestParam Integer type) throws Exception {
		try {
			ConditionScript conditionScript = null;
			if (StringUtil.isNotEmpty(id)) {
				conditionScript = conditionScriptManager.get(id);
				type = conditionScript.getType();
			}
			ArrayNode jarray = conditionScriptManager.getMethodsByClassName(className, conditionScript, type);
			return new CommonResult<String>(true, "获取成功！", jarray.toString());
		} catch (Exception ex) {
			return new CommonResult<String>(false, ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "testScript", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "测试脚本", httpMethod = "POST", notes = "测试脚本")
	public CommonResult<Object> testScript(@ApiParam(name = "param", value = "参数") @RequestBody ObjectNode param)
			throws Exception {
		try {
			GroovyScriptEngine groovyScriptEngine = AppUtil.getBean(GroovyScriptEngine.class);
			String script = "";
			if (param.hasNonNull("script")) {
				script = param.get("script").asText();
			}
			if (StringUtil.isEmpty(script)) {
				return new CommonResult<>(false, "脚本不能为空");
			}
			
			Map<String, Object> variables = new HashMap<>();
			String startId  = "";
			if (param.hasNonNull("startId") && StringUtil.isNotEmpty(param.get("startId").asText())) {
				startId = param.get("startId").asText();
			}else{
				startId = ContextUtil.getCurrentUserId();
			}
			variables.put(BpmConstants.START_USER, startId);
			
			if (param.hasNonNull("userId")  && StringUtil.isNotEmpty(param.get("userId").asText())) {
				String userId = param.get("userId").asText();
				UserServiceImpl userServiceImpl = AppUtil.getBean(UserServiceImpl.class);
				IUser userById = userServiceImpl.getUserById(userId);
				ContextUtil.setCurrentUser(userById);
			}
			
			ActionCmd actionCmd = new BaseActionCmd();
			actionCmd.addTransitVars(BpmConstants.START_USER, startId);
			actionCmd.setVariables(variables);
			ContextThreadUtil.setActionCmd(actionCmd);
			Set<BpmIdentity> set = (Set<BpmIdentity>) groovyScriptEngine.executeObject(script, variables);

			return new CommonResult<Object>(true, "获取成功！", set);
		} catch (Exception ex) {
			return new CommonResult<Object>(false, ex.getMessage());
		}
	}

}
