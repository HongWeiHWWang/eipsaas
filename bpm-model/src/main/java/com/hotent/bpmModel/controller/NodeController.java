package com.hotent.bpmModel.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.DecideType;
import com.hotent.bpm.api.constant.FollowMode;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.constant.PrivilegeMode;
import com.hotent.bpm.api.constant.ScriptType;
import com.hotent.bpm.api.constant.VoteType;
import com.hotent.bpm.api.def.BpmDefXmlHandler;
import com.hotent.bpm.api.helper.identity.UserQueryPluginHelper;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.model.process.def.BpmBoDef;
import com.hotent.bpm.api.model.process.def.BpmDefLayout;
import com.hotent.bpm.api.model.process.def.BpmDefSetting;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmSubTableRight;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.def.EventScript;
import com.hotent.bpm.api.model.process.def.NodeProperties;
import com.hotent.bpm.api.model.process.def.Restful;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.JumpRule;
import com.hotent.bpm.api.model.process.nodedef.ext.AutoTaskDef;
import com.hotent.bpm.api.model.process.nodedef.ext.CallActivityNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SignNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SubProcessNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.Button;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.DefaultJumpRule;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.PrivilegeItem;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.ProcBoDef;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.SignRule;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.UserAssignRule;
import com.hotent.bpm.api.plugin.core.context.AbstractBpmPluginContext;
import com.hotent.bpm.api.plugin.core.context.BpmPluginContext;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.api.service.DiagramService;
import com.hotent.bpm.engine.def.impl.DefaultBpmDefConditionService;
import com.hotent.bpm.engine.def.impl.handler.BoBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.BpmDefSettingBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.EventScriptBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.PluginsBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.PrivilegeBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.ServiceNodeBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.SignRulesBpmDefXmlHandler;
import com.hotent.bpm.engine.def.impl.handler.SubRightBpmDefXmlHandler;
import com.hotent.bpm.model.form.DefaultForm;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.form.FormCategory;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmProBoManager;
import com.hotent.bpm.persistence.model.BpmProBo;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.plugin.core.util.UserAssignRuleParser;
import com.hotent.bpm.plugin.execution.globalRestful.context.GlobalRestFulsPluginContext;
import com.hotent.bpm.plugin.task.restful.context.RestFulsPluginContext;
import com.hotent.bpm.plugin.task.userassign.UserCopyToDefBpmDefXmlHandler;
import com.hotent.bpm.plugin.task.userassign.UserDefBpmDefXmlHandler;
import com.hotent.bpm.plugin.task.userassign.context.UserAssignPluginContext;
import com.hotent.bpm.plugin.task.userassign.context.UserCopyToPluginContext;
import com.hotent.bpm.util.HandlerUtil;
import com.hotent.bpm.util.MessageUtil;
import com.hotent.bpmModel.params.DefConfSaveVo;
import com.hotent.bpmModel.params.NodeConfSaveVo;
import com.hotent.bpmModel.params.VarTreeGetVo;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserGroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 *  描述：流程节点设置管理
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/flow/node/v1/")
@Api(tags="流程节点设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class NodeController {
	@Resource
	BpmDefinitionService bpmDefinitionService;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;
	@Resource
	private UserQueryPluginHelper userQueryPluginHelper;
	@Resource
	private IUserGroupService userGroupService;
	@Resource
	private FormFeignService formRestfulService;
	@Autowired
	FormFeignService  formFeignService;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Resource
	DiagramService diagramService;
	@Resource
	BpmProBoManager bpmProBoManager;
	@Resource
	BoBpmDefXmlHandler boBpmDefXmlHandler;

    @RequestMapping(value="saveSub", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "保存表单权限配置", httpMethod = "POST", notes = "获取表单权限")
    public CommonResult<String> saveSub(@ApiParam(name="nodeId",value="节点ID")@RequestParam String nodeId,
                                        @ApiParam(name="defId",value="权限类型 ")@RequestParam String defId,
                                        @ApiParam(name="parentDefKey",value="父流程定义")@RequestParam String parentDefKey,HttpServletRequest request) throws Exception {
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            List<BpmSubTableRight> rights = new ArrayList<BpmSubTableRight>();
            String json = FileUtil.inputStream2String(request.getInputStream());
            JsonNode jobj = JsonUtil.toJsonNode(json);
            for (JsonNode jsonNode : jobj) {
                BpmSubTableRight right=JsonUtil.toBean(jsonNode, BpmSubTableRight.class);
                rights.add(right);
            }
            param.put("list", rights);
            param.put("parentDefKey", parentDefKey);
            BpmDefXmlHandler<Map<String, Object>> handler = AppUtil.getBean(SubRightBpmDefXmlHandler.class);
            handler.saveNodeXml(defId, nodeId, param);
            return new CommonResult<String>(true,"保存子表权限成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult<String>(false,"保存子表权限失败："+e.getMessage());
        }
    }

    @RequestMapping(value="initSub", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "显示表单权限配置", httpMethod = "GET", notes = "显示表单权限配置")
    public Object initSub(@ApiParam(name="nodeId",value="节点ID")@RequestParam String nodeId,
                          @ApiParam(name="defId",value="权限类型 ")@RequestParam String defId,
                          @ApiParam(name="parentDefKey",value="父流程定义")@RequestParam String parentDefKey) throws Exception {
        Map<String,JsonNode> map = new HashMap<>();
        BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
        UserTaskNodeDef utnd = (UserTaskNodeDef) nodeDef;
        for (BpmSubTableRight bsr : utnd.getBpmSubTableRightByParentDefKey(parentDefKey)) {
            map.put(bsr.getTableName(),JsonUtil.toJsonNode(bsr));
        }
        return map;
    }

	/**
	 * 编辑节点规则页面初始化数据
	 */
	@RequestMapping(value="ruleEdit", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "编辑节点规则页面初始化数据", httpMethod = "GET", notes = "编辑节点规则页面初始化数据")
	public Map<String,Object> ruleEdit(
			@ApiParam(name="definitionId",value="流程定义id")@RequestParam  String definitionId,
			@ApiParam(name="nodeId",value="节点id")@RequestParam  String nodeId) throws Exception {
		Map <String,Object> resMap=new HashMap<>();
		List<BpmNodeDef> nodeDefList = bpmDefinitionAccessor.getAllNodeDef(definitionId);
		List<ObjectNode> list=new ArrayList<>();
		for (BpmNodeDef bpmNodeDef : nodeDefList) {
			ObjectNode node =JsonUtil.getMapper().createObjectNode();
			node.put("nodeId", bpmNodeDef.getNodeId());
			node.put("name", bpmNodeDef.getName());
			list.add(node);
		}
		UserTaskNodeDef nodeDef = (UserTaskNodeDef) bpmDefinitionService.getBpmNodeDefByDefIdNodeId(definitionId, nodeId);
		resMap.put("nodeDef", nodeDef);
		resMap.put("nodeDefList", list);
		return resMap;
	}

	/**
	 * 节点规则列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="ruleListJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "节点规则列表", httpMethod = "POST", notes = "节点规则列表")
	public PageList<DefaultJumpRule> ruleListJson(
			@ApiParam(name="definitionId",value="流程定义id")@RequestParam  String definitionId,
			@ApiParam(name="nodeId",value="节点id")@RequestParam  String nodeId) throws Exception {
		UserTaskNodeDef nodeDef = (UserTaskNodeDef) bpmDefinitionService.getBpmNodeDefByDefIdNodeId(definitionId, nodeId);
		List<DefaultJumpRule> rules = nodeDef.getJumpRuleList();
		if(rules == null)rules = Collections.EMPTY_LIST;
		return new  PageList<DefaultJumpRule>(rules);
	}

	/**
	 * 保存节点的跳转规则
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="ruleSave",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存节点的跳转规则", httpMethod = "POST", notes = "保存节点的跳转规则")
	public CommonResult<String> ruleSave(
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId,
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="nodeList",value="节点规则json", required = true) @RequestBody List<JsonNode> nodeList) throws Exception {

		List<JumpRule> jumpRuleList= new ArrayList<JumpRule>();
		for (JsonNode jsonNode : nodeList) {
			jumpRuleList.add(JsonUtil.toBean(jsonNode, DefaultJumpRule.class));
		}
		try {
			if (StringUtil.isNotEmpty(nodeId)) {
				BpmDefXmlHandler<List<JumpRule>> bpmDefXmlHandler = (BpmDefXmlHandler<List<JumpRule>>) AppUtil.getBean("transRulesBpmDefXmlHandler");
				bpmDefXmlHandler.saveNodeXml(defId, nodeId, jumpRuleList);
			}
			return new CommonResult<String>("更新节点跳转规则成功");
		} catch (Exception e) {
			return new CommonResult<String>(false,"更新节点跳转规则失败："+e.getMessage());
		}
	}



	/**
	 * 保存节点json 配置
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	@RequestMapping(value="nodeUserConditionSave", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存节点json 配置", httpMethod = "POST", notes = "保存节点json 配置")
	public CommonResult<String> nodeUserConditionSave(@ApiParam(name="vo",value="节点保存对象")@RequestBody NodeConfSaveVo vo) throws Exception {
		String nodeId = vo.getNodeId();
		String defId = vo.getDefId();
		String nodeJson = vo.getNodeJson();
		String parentFlowKey = vo.getParentFlowKey();
		try {
			if (StringUtil.isNotEmpty(nodeId)) {
				UserDefBpmDefXmlHandler userDefBpmDefXmlHandler = (UserDefBpmDefXmlHandler) AppUtil.getBean(UserDefBpmDefXmlHandler.class);
				userDefBpmDefXmlHandler.saveNodeXml(defId, nodeId, nodeJson, parentFlowKey);
			}
			return new CommonResult<String>("更新节点人员配置成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"更新节点人员配置失败"+e.getMessage());
		}
	}

	

	/**
	 * 节点规则脚本设置
	 */
	@RequestMapping(value="eventScriptEdit", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "节点规则脚本设置", httpMethod = "GET", notes = "节点规则脚本设置")
	public  Map<String, Object> eventScriptEdit(
			@ApiParam(name="defId",value="流程定义id")@RequestParam  String defId,
			@ApiParam(name="nodeId",value="节点id")@RequestParam  String nodeId) throws Exception {
		BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		Map<ScriptType, String> scriptMap = bpmNodeDef.getScripts();
		Map<String, Object> resMap=new HashMap<>();
		resMap.put("bpmNodeDef", bpmNodeDef);
		resMap.put("eventScriptMap", scriptMap);
		return resMap;
	}

	/**
	 * 节点事件脚本保存
	 */
	@RequestMapping(value="eventScriptSave", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "节点事件脚本保存", httpMethod = "POST", notes = "节点事件脚本保存")
	public CommonResult<String> eventScriptSave(@ApiParam(name="vo",value="节点配置保存对象")@RequestBody NodeConfSaveVo vo) throws Exception {
		String defId = vo.getDefId();
		String nodeId = vo.getNodeId();
		String eventScriptArray = vo.getEventScriptArray();
		try {
			EventScriptBpmDefXmlHandler eventScriptHandler = AppUtil.getBean(EventScriptBpmDefXmlHandler.class);
			ArrayNode eventScript =  (ArrayNode) JsonUtil.toJsonNode(eventScriptArray);

			for (int i = 0; i < eventScript.size(); i++) {
				ObjectNode objct = (ObjectNode) eventScript.get(i);
				ScriptType scriptType = ScriptType.fromKey(objct.get("scriptType").asText());
				EventScript es = new EventScript(scriptType, objct.get("content").asText());

				eventScriptHandler.saveNodeXml(defId, nodeId, es);
			}
			return new CommonResult<String>("脚本保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false, "脚本保存失败："+e.getMessage());
		}

	}

	/**
	 * 获取分支设置信息
	 */
	@RequestMapping(value="branchConditionEdit", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取分支设置信息", httpMethod = "GET", notes = "获取分支设置信息")
	public Map<String,Object> branchConditionEdit(
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId,
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId) throws Exception {

		BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		Map<String,Object> data=new HashMap<>();
		List<ObjectNode> incomeNodes=new ArrayList<>();
		for(BpmNodeDef def:bpmNodeDef.getIncomeNodes()){
			ObjectNode obj=JsonUtil.getMapper().createObjectNode();
			obj.put("nodeId", def.getNodeId());
			obj.put("name", def.getName());
			incomeNodes.add(obj);
		}
		
		List<ObjectNode> outcomeNodes=new ArrayList<>();
		for(BpmNodeDef def:bpmNodeDef.getOutcomeNodes()){
			ObjectNode obj=JsonUtil.getMapper().createObjectNode();
			obj.put("nodeId", def.getNodeId());
			obj.put("name", def.getName());
			outcomeNodes.add(obj);
		}
		data.put("incomeNodes", incomeNodes);
		data.put("outcomeNodes", outcomeNodes);
		data.put("conditions",bpmNodeDef.getConditions());
		return data;
	}

	/**
	 * 分支节点规则脚本保存
	 */
	@RequestMapping(value="branchConditionSave", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "分支节点规则脚本保存", httpMethod = "POST", notes = "分支节点规则脚本保存")
	public CommonResult<String> branchConditionSave(@ApiParam(name="vo",value="节点配置保存对象")@RequestBody NodeConfSaveVo vo) throws Exception {
		String defId = vo.getDefId();
		String nodeId = vo.getNodeId();
		String condition = vo.getCondition();
		try {
			DefaultBpmDefConditionService bpmDefHandler = AppUtil.getBean(DefaultBpmDefConditionService.class);
			Map<String, String> map = JsonUtil.toMap(condition);
			bpmDefHandler.saveCondition(defId, nodeId, map);
			return new CommonResult<String>("分支网关设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"分支网关设置失败:"+e.getMessage());
		}

	}

	/**
	 * 自动任务管理
	 */
	@RequestMapping(value="autoTaskManager", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "自动任务管理", httpMethod = "GET", notes = "自动任务管理")
	public Map<String,Object> autoTaskManager(
			@ApiParam(name="defId",value="流程定义id")@RequestParam  String defId,
			@ApiParam(name="nodeId",value="节点id")@RequestParam  String nodeId) throws Exception {
		
		Map<String,Object> data= new HashMap<>();
		 
		AutoTaskDef autoTaskDef = (AutoTaskDef) bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		
		BpmPluginContext bpmPluginContext = autoTaskDef.getAutoTaskBpmPluginContext();
		data.put("bpmPluginContext", bpmPluginContext);
		return data;
	}

	/**
	 * 自动节点，获取插件数据
	 */
	@RequestMapping("autoTaskPluginGet")
	public Map<String,Object> autoTaskPluginGet(
			@ApiParam(name="defId",value="流程定义id")@RequestParam  String defId,
			@ApiParam(name="nodeId",value="节点id")@RequestParam  String nodeId,
			@ApiParam(name="pluginType",value="插件类型")@RequestParam  String pluginType) throws Exception {
		List<BpmVariableDef> bpmVariableList = getAllBpmVariableDef(defId, nodeId);

		AutoTaskDef autoTaskDef = (AutoTaskDef) bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);

		AbstractBpmPluginContext bpmPluginContext = (AbstractBpmPluginContext) autoTaskDef.getAutoTaskBpmPluginContext();
		Map<String,Object> data=new HashMap<>();
		// 已经选择并保存该插件。
		if (bpmPluginContext != null && bpmPluginContext.getType().equals(pluginType)) {
			BpmPluginDef bpmPluginDef = bpmPluginContext.getBpmPluginDef();
			data.put("bpmPluginDef", bpmPluginDef);
			String json = bpmPluginContext.getJson();
			json = Base64.getBase64(json);
			data.put("bpmPluginDefJson", json);
		}
		data.put("bpmPluginContext", bpmPluginContext);
		data.put("bpmVariableList", bpmVariableList);

		return data;
	}

	/**
	 * 自动节点保存json 配置
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="autoTaskPluginSave", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "自动节点保存json 配置", httpMethod = "POST", notes = "自动节点保存json 配置")
	public CommonResult<String> autoTaskPluginSave(@ApiParam(name="vo",value="节点配置保存对象")@RequestBody NodeConfSaveVo vo)  throws Exception {
		String defId = vo.getDefId();
		String nodeId = vo.getNodeId();
		String jsonStr = vo.getJsonStr();
		try {
			if (StringUtil.isNotEmpty(nodeId)) {
				ServiceNodeBpmDefXmlHandler serviceNodeDefXmlHandler = AppUtil.getBean(ServiceNodeBpmDefXmlHandler.class);
				serviceNodeDefXmlHandler.saveNodeXml(defId, nodeId, jsonStr);
			}
			return new CommonResult<String>("更新节点配置成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"更新节点配置失败："+e.getMessage());
		}
	}


	
	/**
	 * 获取会签规则特权配置
	 */
	@RequestMapping(value="getSignConfig", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取会签规则特权配置", httpMethod = "GET", notes = "获取会签规则特权配置")
	public Object getSignConfig(
			@ApiParam(name="defId",value="流程定义id")@RequestParam  String defId,
			@ApiParam(name="nodeId",value="节点id")@RequestParam  String nodeId)  throws Exception {

		SignNodeDef signNodeDef = (SignNodeDef) bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		List<PrivilegeItem> privilegeList = signNodeDef.getPrivilegeList();
		SignRule signRule = signNodeDef.getSignRule();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("privilegeList", getPrivilegeListJson(privilegeList));
		map.put("signRule", SignRule.toJson(signRule));
		return map;
	}
	

	/** 将PrivilegeList 转化成json 
	 * @throws IOException */
	@SuppressWarnings({ })
	private ObjectNode getPrivilegeListJson(List<PrivilegeItem> privilegeList) throws IOException {
		ObjectNode ObjectNode =  JsonUtil.getMapper().createObjectNode();
		if (BeanUtils.isEmpty(privilegeList)) return ObjectNode;

		for (PrivilegeItem privilege : privilegeList) {
			ArrayNode config=JsonUtil.getMapper().createArrayNode();
			UserAssignRuleParser.handJsonConfig(config, privilege.getUserRuleList());
			ObjectNode.set(privilege.getPrivilegeMode().getKey(), config);
		}
		String json =  ObjectNode.toString().replaceAll("null,", "\"\",");
		return (ObjectNode) JsonUtil.toJsonNode(json);
	}

	/**
	 * 会签规则特权配置
	 */
	@RequestMapping(value="signConfigSave", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "会签规则特权配置", httpMethod = "POST", notes = "获取会签规则特权配置")
	public Object signConfigSave(@ApiParam(name="vo",value="节点配置保存对象")@RequestBody NodeConfSaveVo vo) throws Exception {
		String defId = vo.getDefId();
		String nodeId = vo.getNodeId();
		String signRuleJson = vo.getSignRule();
		String privilegeListJson = vo.getPrivilegeList();
		String  resultMsg="";
		try {
			if (StringUtil.isNotEmpty(nodeId)) {
				List<PrivilegeItem> privilegeList = getPrivilegeList(privilegeListJson);
				SignRule signRule = getSignRule(signRuleJson);

				SignRulesBpmDefXmlHandler signRulesBpmDefXmlHandler = AppUtil.getBean(SignRulesBpmDefXmlHandler.class);
				signRulesBpmDefXmlHandler.saveNodeXml(defId, nodeId, signRule);
				resultMsg = "会签规则更新成功，会签权限人员配置更新失败!";

				PrivilegeBpmDefXmlHandler privilegeBpmDefXmlHandler = AppUtil.getBean(PrivilegeBpmDefXmlHandler.class);
				privilegeBpmDefXmlHandler.saveNodeXml(defId, nodeId, privilegeList);
				resultMsg = "会签节点配置成功";
			}
			return new CommonResult<String>(resultMsg);
		} catch (Exception e) {
			resultMsg = resultMsg == null ? "会签规则配置失败" : resultMsg;
			e.printStackTrace();
			return new CommonResult<String>(false, resultMsg);
		}
	}

	// /json to SingRule
	private SignRule getSignRule(String json) throws IOException {
		ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(json);
		DecideType decideType = DecideType.fromKey(obj.get("decideType").asText());
		VoteType voteType = VoteType.fromKey(obj.get("voteType").asText());
		FollowMode followMode = FollowMode.fromKey(obj.get("followMode").asText());
		int voteAmount = obj.get("voteAmount").asInt();
		return new SignRule(decideType, voteType, followMode, voteAmount);
	}

	/** PrivilegeList转化成 对象 
	 * @throws Exception */
	private List<PrivilegeItem> getPrivilegeList(String json) throws Exception {
		List<PrivilegeItem> privilegeList = new ArrayList<PrivilegeItem>();
		if(StringUtil.isEmpty(json)) return privilegeList;
		ObjectNode privilegeListJson = (ObjectNode) JsonUtil.toJsonNode(json);
		Iterator<Entry<String, JsonNode>> newSet= privilegeListJson.fields();
        while (newSet.hasNext())  
        {  
            Entry<String, JsonNode> ent = newSet.next();  
			PrivilegeItem privilege = new PrivilegeItem();
			PrivilegeMode privilegeMode = PrivilegeMode.fromKey(ent.getKey());
			privilege.setPrivilegeMode(privilegeMode);

			ArrayNode ruleArray = (ArrayNode) privilegeListJson.get(ent.getKey());
			if (ruleArray.size() == 0)
				continue;
			// /用户规则
			List<UserAssignRule> rules = new ArrayList<UserAssignRule>();
			for (int i = 0; i < ruleArray.size(); i++) {
				ObjectNode ruleObjectNode = (ObjectNode) ruleArray.get(i);
				UserAssignRule rule = UserAssignRuleParser.getUserAssignRule(ruleObjectNode);
				rules.add(rule);
			}
			privilege.setUserRuleList(rules);
			privilegeList.add(privilege);
		}

		return privilegeList;
	}

	/**
	 * 预览人员条件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="previewCondition",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "预览人员条件", httpMethod = "GET", notes = "添加bo定义")
	public PageList<IUser> previewCondition(
			@ApiParam(name="conditionArray",value="人员条件数组", required = true)@RequestParam String conditionArray,
			@ApiParam(name="variables",value="变量", required = true)@RequestParam String variables)  throws Exception {
		ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(variables);
		Map<String, String> map = (Map<String, String>) obj;

		List<IUser> users = userQueryPluginHelper.queryUsersByConditions(conditionArray, map);
		return (PageList<IUser>) users;
	}

	/**
	 * 获取流程节点的流程变量 bo变量，流程变量
	 */
	/*@RequestMapping("flowVarDialog")
	public ModelAndView flowVarDialog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String defId = RequestUtil.getString(request, "defId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		List<GroupType> dimensionList = userGroupService.getGroupTypes();
		return getAutoView().addObject("defId", defId).addObject("nodeId", nodeId).addObject("dimensionList", dimensionList);
	}*/

	/**
	 * 该节点能用的所有变量
	 * @throws Exception 
	 */
	private List<BpmVariableDef> getAllBpmVariableDef(String defId, String nodeId) throws Exception {
		List<BpmVariableDef> bpmVariableList = new ArrayList<BpmVariableDef>();
		// 全局变量
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		if (defExt.getVariableList() != null) {
			bpmVariableList.addAll(defExt.getVariableList());
		}

		List<BpmNodeDef> list = bpmProcessDefExt.getBpmnNodeDefs();
		for (BpmNodeDef node : list) {
			if (!node.getNodeId().equals(nodeId))
				continue;

			if (!node.getType().toString().equalsIgnoreCase("usertask"))
				continue;

			UserTaskNodeDef targetNodeDef = (UserTaskNodeDef) node;
			if (targetNodeDef.getVariableList() != null) {
				bpmVariableList.addAll(targetNodeDef.getVariableList());
			}

		}
		return bpmVariableList;
	}

	/**
	 * 规则选择框
	 */
	@RequestMapping("userAssignConditionDialog")
	public ModelAndView userAssignConditionDialog(HttpServletRequest request, HttpServletResponse response, String defId, String nodeId) throws Exception {
		return new ModelAndView("/flow/def/userAssignConditionDialog.jsp").addObject("defId", defId).addObject("nodeId", nodeId);
	}


	
	/**
	 * 判断手机表单是否存在。
	 * @param defId
	 * @param bpmDefSetting
	 * @return
	 * @throws Exception 
	 */
	private boolean isMobileSet(String defId, BpmDefSetting bpmDefSetting) throws Exception{
		BpmProcessDef<BpmProcessDefExt> bpmProcessDef = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDef.getProcessDefExt();
		
		BpmNodeDef nodeDef= bpmDefinitionAccessor.getStartEvent(defId);
		
		List<BpmNodeDef> startNodes= bpmDefinitionAccessor.getStartNodes(defId);
		Map<String,Form> nodeMap= bpmDefSetting.getFormMap(false);
		
		Form startForm=nodeMap.get(nodeDef.getNodeId());
		boolean isSet=isFormEmpty(startForm);
		if(isSet)  return true;
		
		for(BpmNodeDef def:startNodes){
			Form frm=nodeMap.get(def.getNodeId());
			isSet=isFormEmpty(frm);
			if(isSet) return true;
		}
		Form globalForm=defExt.getGlobalMobileForm();
		isSet=isFormEmpty(globalForm);
		return isSet;
	}
	
	private boolean isFormEmpty(Form startForm){
		
		if(startForm!=null && StringUtil.isNotEmpty(startForm.getFormValue())){
			return true;
		}
		return false;
	}

	

	/**
	 * 验证handler。 输入格式为 serviceId +"." + 方法名。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="validHandler",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "验证handler。 输入格式为 serviceId +.法名", httpMethod = "GET", notes = "验证handler。 输入格式为 serviceId +.法名")
	public Object validHandler(@ApiParam(name="handler",value="handler字符串", required = true) @RequestParam String handler) throws IOException {
		int rtn = HandlerUtil.isHandlerValid(handler);
		String template = "{\"result\":\"%s\",\"msg\":\"%s\"}";
		String msg = "";
		switch (rtn) {
		case 0:
			msg = "输入有效";
			break;
		case -1:
			msg = "输入格式无效";
			break;
		case -2:
			msg = "没有service类";
			break;
		case -3:
			msg = "没有对应的方法";
			break;
		default:
			msg = "其他错误";
			break;
		}
		String str = String.format(template, rtn, msg);
		return str;
	}

	/**
	 * 流程变量对话框的树 其中包含：bodef的字段，流程变量，流程常量（发起人,当前用户,...）
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             Object
	 * @exception
	 * @since 1.0.0
	 */
	@RequestMapping(value="varTree",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程变量对话框的树 其中包含：bodef的字段，流程变量，流程常量（发起人,当前用户,...）", httpMethod = "POST", notes = "流程变量对话框的树 其中包含：bodef的字段，流程变量，流程常量（发起人,当前用户,...）")
	public Object varTree(@ApiParam(name="vo",value="获取流程变量参数", required = true) @RequestBody VarTreeGetVo vo)throws Exception {
		String defId = vo.getDefId();
		boolean removeSub = vo.getRemoveSub();
		boolean removeMain = vo.getRemoveMain();
		String flowKey = vo.getFlowKey();
		String nodeId = vo.getNodeId();
		String parentFlowKey = vo.getParentFlowKey();
		
		if(StringUtil.isNotEmpty(parentFlowKey)){
			BpmDefinition parentDefinition = bpmDefinitionService.getBpmDefinitionByDefKey(parentFlowKey, true);
			if(BeanUtils.isNotEmpty(parentDefinition)){
				defId = parentDefinition.getDefId();
			}
		}

		if (StringUtil.isEmpty(defId) && StringUtil.isNotEmpty(flowKey)) {
			BpmDefinition definition = bpmDefinitionService.getBpmDefinitionByDefKey(flowKey, false);
			defId = definition.getDefId();
		}
		
		ArrayNode treeJA = JsonUtil.getMapper().createArrayNode();
		
		if(StringUtil.isEmpty(defId)) return treeJA;

		// 获取流程定义
		BpmProcessDef<BpmProcessDefExt> procDef = bpmDefinitionAccessor.getBpmProcessDef(defId);


		// 获取表单BO树
		List<ProcBoDef> boDefs = procDef.getProcessDefExt().getBoDefList();
		ArrayNode boList = JsonUtil.getMapper().createArrayNode();

		if (BeanUtils.isNotEmpty(boDefs) && vo.isBpmForm()) {
			for (ProcBoDef boDef : boDefs) {
				ObjectNode def = formFeignService.getBodefByAlias(boDef.getKey());
				ObjectNode objec = formRestfulService.getBoJosn(def.get("id").asText());

				if (removeSub || removeMain) {// 去掉子表数据
					JsonNode children = objec.get("children");
					ArrayNode temp = JsonUtil.getMapper().createArrayNode();
					for (Object obj : children) {
						JsonNode json = (JsonNode) obj;
						if (json.get("children") == null && removeSub) {
							temp.add(json);
						}
						if (json.get("children") != null && removeMain) {
							temp.add(json);
						}
					}
					((ObjectNode) objec).set("children", temp);
				}
				boList.add(objec);
			}

			JsonNode bos = JsonUtil.toJsonNode("{\"id\":\"0\",\"parentId\":\"-1\",\"desc\":\"表单变量\",\"name\":\"表单变量\",\"con\":\"fa fa-bold dark\"}");
			((ObjectNode) bos).set("children", boList);
			((ArrayNode) treeJA).add(bos);
		}

		// 获取流程变量
		boolean includeBpmConstants=vo.getIncludeBpmConstants();
		if (!includeBpmConstants) {
			return treeJA;
		}
		ObjectNode flowVarJson= getFlowVarJson(procDef, includeBpmConstants,nodeId,vo.isUrgent());
		if(flowVarJson!=null){
			treeJA.add(flowVarJson);
		}
		return treeJA;
	}

	private ObjectNode getFlowVarJson(BpmProcessDef<BpmProcessDefExt> procDef, boolean includeBpmConstants, String nodeId,boolean isUrgent) throws Exception {
		List<BpmVariableDef> variables = new ArrayList<BpmVariableDef>();
		boolean isBmpnInstId = true;//流程变量中是否加入流程实例ID（开始节点不加入）
		// 全局变量
		if(procDef.getProcessDefExt().getVariableList()!=null){
			variables.addAll(procDef.getProcessDefExt().getVariableList());
		}
		ObjectNode flowVariable = (ObjectNode) JsonUtil.toJsonNode("{\"id\":\"-99\",\"desc\":\"流程变量\",\"name\":\"流程变量\",\"icon\":\"fa fa-bold dark\",\"nodeType\":\"root\"}");
		
		// 节点变量
		if(StringUtil.isNotEmpty(nodeId)){
			BpmNodeDef bpmNodeDef = bpmDefinitionAccessor.getBpmNodeDef(procDef.getProcessDefinitionId(), nodeId);
			if(bpmNodeDef !=null && bpmNodeDef instanceof UserTaskNodeDef){
				UserTaskNodeDef taskNodeDef = (UserTaskNodeDef) bpmNodeDef;
				if(taskNodeDef.getVariableList()!=null){
					variables.addAll(taskNodeDef.getVariableList());
				}
			}else{
				//开始节点流程变量中不加入流程实例ID
				if(bpmNodeDef.getType().equals(NodeType.START)){
					isBmpnInstId = false;
				}
			}
			
		}
		
		ArrayNode varList =JsonUtil.getMapper().createArrayNode();
		if (BeanUtils.isNotEmpty(variables)){
			for (BpmVariableDef variable : variables) {
				String name = variable.getName();
				variable.setName(variable.getVarKey()); // @ 前端流程变量都是 取name，
														// 而名字为desc
				ObjectNode obj = (ObjectNode) JsonUtil.toJsonNode(variable);
				obj.put("nodeType", "var");
				obj.put("desc", name);
				varList.add(obj);
			}
		}
		// 如果表单变量需要包含流程常量
		if (includeBpmConstants) {
			ObjectNode bussinessKey = (ObjectNode) JsonUtil.toJsonNode("{\"name\":\"" + BpmConstants.BPM_FLOW_KEY + "\",\"desc\":\"流程定义Key\",\"nodeType\":\"var\"}");
			ObjectNode startUser =(ObjectNode) JsonUtil.toJsonNode("{\"name\":\"" + BpmConstants.START_USER + "\",\"desc\":\"发起人\",\"nodeType\":\"var\"}");

			if(isBmpnInstId){
				ObjectNode bmpnInstId =(ObjectNode) JsonUtil.toJsonNode("{\"name\":\"" + BpmConstants.PROCESS_INST_ID + "\",\"desc\":\"流程实例ID\",\"nodeType\":\"var\"}");
				varList.add(bmpnInstId);
			}
			varList.add(bussinessKey);
			varList.add(startUser);

            //是否包含催办的催办人和被催办人
            if(isUrgent){
                ObjectNode promoter =(ObjectNode) JsonUtil.toJsonNode("{\"name\":\"" + BpmConstants.PROMOTER + "\",\"desc\":\"催办人\",\"nodeType\":\"var\"}");
                varList.add(promoter);
                ObjectNode appointee =(ObjectNode) JsonUtil.toJsonNode("{\"name\":\"" + BpmConstants.APPOINTEE + "\",\"desc\":\"被催办人\",\"nodeType\":\"var\"}");
                varList.add(appointee);
            }
		}
		if(varList.size()<=0){
			return null;
		}
		for (JsonNode jsonNode : varList) {
			ObjectNode objectNode = (ObjectNode) jsonNode;
			if (!objectNode.hasNonNull("id")) {
				objectNode.put("id",UniqueIdUtil.getSuid());
			}
		}
		flowVariable.set("children", varList);
		return flowVariable;
	}

	/**
	 * 获取流程节点的列表 一些基本信息而已
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *             Object
	 * @exception
	 * @since 1.0.0
	 */
	@RequestMapping(value="getNodes",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取流程节点的列表 一些基本信息而已", httpMethod = "GET", notes = "获取流程节点的列表 一些基本信息而已")
	public Object getNodes(@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId) throws Exception {
		
		List<BpmNodeDef> nodeDefs = bpmDefinitionAccessor.getAllNodeDef(defId);

		ArrayNode list = JsonUtil.getMapper().createArrayNode();
		for (BpmNodeDef node : nodeDefs) {
			ObjectNode jo = JsonUtil.getMapper().createObjectNode();
			jo.put("name", node.getName());
			jo.put("nodeId", node.getNodeId());
			jo.put("type", node.getType().toString());
			list.add(jo);
		}
		return list;
	}
	
	/**
	 * 流程定义节点配置页面json数据
	 * @return json 
	 * @throws Exception
	 */
	@RequestMapping(value="getDefSetting",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程定义节点配置页面json数据", httpMethod = "GET", notes = "流程定义节点配置页面json数据")
	public Map<String, Object> nodeDefSetting(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="topDefKey",value="父流程定义key", required = true) @RequestParam String topDefKey) throws Exception {
		DefaultBpmDefinition bpmDefinition = null;
		BpmDefLayout bpmDefLayout = null;
		if (StringUtil.isNotEmpty(defId)) {
			bpmDefinition = bpmDefinitionManager.getById(defId);
			bpmDefLayout = diagramService.getLayoutByDefId(defId);
		}
		Map<String, String> messageTypelist = MessageUtil.getHandlerTypes();
		Map<String, Object> initData=new HashMap<>();
		initData.put("bpmDefinition", bpmDefinition);
		initData.put("bpmDefLayout", bpmDefLayout);
		initData.put("messageTypelist", messageTypelist);
		
		Map<String, Object> nodeSetData=new HashMap<>();
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		
		BpmDefSetting bpmDefSetting = new BpmDefSetting();
		bpmDefSetting.setParentDefKey(topDefKey);
		//全局表单
		bpmDefSetting.setGlobalForm(StringUtil.isEmpty(topDefKey)?defExt.getGlobalForm():defExt.getGlobalFormByDefKey(topDefKey, false));
		bpmDefSetting.setGlobalMobileForm(StringUtil.isEmpty(topDefKey)? defExt.getGlobalMobileForm():defExt.getGlobalFormByDefKey(topDefKey, true));
		bpmDefSetting.setInstForm(StringUtil.isEmpty(topDefKey)?defExt.getInstForm():defExt.getInstFormByDefKey(topDefKey, false));
		bpmDefSetting.setInstMobileForm(StringUtil.isEmpty(topDefKey)? defExt.getInstMobileForm():defExt.getInstFormByDefKey(topDefKey, true));
		//全局restful事件
		bpmDefSetting.setGlobalRestfuls(getGlobalRestFulList(bpmProcessDefExt,defId, topDefKey));
		//节点设置，节点表单，节点信息，节点脚本
		ArrayNode nodes = JsonUtil.getMapper().createArrayNode();
		List<Form> formList = new ArrayList<Form>();
		List<NodeProperties> properties = new ArrayList<NodeProperties>();
		Map<String,List<Button>> btnMap = new HashMap<String, List<Button>>();
		Map<String, ObjectNode> nodeScriptMap = new HashMap<String, ObjectNode>();
		
		handNodeDefSetting(topDefKey,bpmProcessDefExt.getBpmnNodeDefs(),properties,formList,nodes,btnMap,nodeScriptMap);
  		bpmDefSetting.setNodeProperties(properties);
		bpmDefSetting.setNodeForms(formList);
		
		List<BpmNodeDef> nodeDefList =bpmDefinitionAccessor.getAllNodeDef(defId);
		String userJson =JsonUtil.toJson(getNodesUserJson(nodeDefList,defId, topDefKey)) ;//节点审批人员
        String userReadJson =JsonUtil.toJson(getNodesReadUserJson(nodeDefList,defId, topDefKey)) ;//节点传阅人员
		String restfulJson =JsonUtil.toJson(getNodesRestFulJson(nodeDefList,defId, topDefKey));
		
		nodeSetData.put("bpmDefSetting",JsonUtil.toJsonNode(bpmDefSetting));
		nodeSetData.put("nodes", nodes);
		nodeSetData.put("nodeUserMap",JsonUtil.toJsonNode(userJson));
        nodeSetData.put("nodeReadUserMap",JsonUtil.toJsonNode(userReadJson));
		nodeSetData.put("nodeBtnMap", btnMap);
		nodeSetData.put("nodeScriptMap", nodeScriptMap);
		nodeSetData.put("nodeRestfulMap",JsonUtil.toJsonNode(restfulJson));
		
		Map<String, Object> resultMap=new HashMap<>();
		resultMap.put("initData", initData);
		resultMap.put("nodeSetData", nodeSetData);
		return resultMap;
	}
	
	/**流程定义节点配置页面json数据
	 * @return json 
	 * @throws IOException 
	 * @throws Exception
	 *//*
	@RequestMapping(value="getDefSetting",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程定义设置json数据", httpMethod = "GET", notes = "流程定义设置json数据")
	public Map<String, Object> getDefSetting(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="topDefKey",value="父流程定义key", required = true) @RequestParam String topDefKey) throws Exception {
		

		return returnData;
	}*/
	
	//整理用户节点  节点信息，节点属性，节点表单，手机表单,节点按钮
	private void handNodeDefSetting(String parentDefKey,List<BpmNodeDef> nodeDefList,
			List<NodeProperties> properties,List<Form> formList, ArrayNode nodes, 
			Map<String, List<Button>> btnMap,Map<String, ObjectNode> nodeScriptMap) throws IOException{
		for (BpmNodeDef nodeDef : nodeDefList) {
			String nodeId = nodeDef.getNodeId();
			NodeType type = nodeDef.getType();
			if (NodeType.START.equals(type)  || NodeType.CUSTOMSIGNTASK.equals(type)  || NodeType.USERTASK.equals(type) || NodeType.SIGNTASK.equals(type)) {
				ObjectNode node = JsonUtil.getMapper().createObjectNode();
				//节点信息
				node.put("name", nodeDef.getName());
				node.put("nodeId", nodeId);
				node.put("type", nodeDef.getType().toString());
				nodes.add(node);
				
				//节点表单，节点按钮，节点手机表单
				Form form = null;
				Form mobileForm = null;
				NodeProperties propertie = null;
				//本地节点。
				if (StringUtil.isEmpty(parentDefKey) || BpmConstants.LOCAL.equals(parentDefKey)){
					propertie =nodeDef.getLocalProperties();
					form = nodeDef.getForm();
					mobileForm = nodeDef.getMobileForm();
				}else{
					propertie = nodeDef.getPropertiesByParentDefKey(parentDefKey);
					form = nodeDef.getSubForm(parentDefKey, FormType.PC);
					mobileForm = nodeDef.getSubForm(parentDefKey, FormType.MOBILE);
				}
				//开始节点 添加属性配置
				if(propertie != null){
					propertie.setNodeId(nodeId);
					properties.add(propertie);
				}
				
				//设置form默认值
				if(form== null) {
					form = new DefaultForm();
					form.setType(FormCategory.INNER);
				}
				if(mobileForm== null){
					mobileForm = new DefaultForm();
					mobileForm.setFormType(FormType.MOBILE.value());
				}
				
				form.setNodeId(nodeId);
				mobileForm.setNodeId(nodeId);
				formList.add(mobileForm);
				formList.add(form);

				List<Button> buttons = nodeDef.getButtons();
				btnMap.put(nodeDef.getNodeId(), buttons);
			} else if (NodeType.SUBPROCESS.equals(type)) {
				SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef) nodeDef;
				BpmProcessDef<? extends BpmProcessDefExt> processDef = subProcessNodeDef.getChildBpmProcessDef();
				List<BpmNodeDef> bpmNodeDefs = processDef.getBpmnNodeDefs();
				handNodeDefSetting(parentDefKey, bpmNodeDefs, properties, formList,nodes,btnMap, nodeScriptMap);
			}
			//节点脚本
			Map<ScriptType, String> scriptMap = nodeDef.getScripts();
			if(!scriptMap.isEmpty()){
				nodeScriptMap.put(nodeId,(ObjectNode) JsonUtil.toJsonNode(scriptMap));
			}
			
		}
	}
	//获取所有节点人员的JSON (节点审批人员)
	private Object getNodesUserJson(List<BpmNodeDef> nodeDefList, String defId, String parentFlowKey) throws Exception {

		ObjectNode jobject = JsonUtil.getMapper().createObjectNode();
		List<BpmNodeDef> userNodes = new ArrayList<BpmNodeDef>();
		for (BpmNodeDef nodeDef : nodeDefList) {
			if (nodeDef.getType() == NodeType.USERTASK || nodeDef.getType() == NodeType.SIGNTASK  || nodeDef.getType() == NodeType.CUSTOMSIGNTASK)
				userNodes.add(nodeDef);
		}

		for (int i = 0; i < userNodes.size(); i++) {
			BpmNodeDef node = userNodes.get(i);
			UserAssignPluginContext userPluginContext = (UserAssignPluginContext) node.getPluginContext(UserAssignPluginContext.class);
			if (userPluginContext == null) {
				jobject.set(node.getNodeId(), JsonUtil.getMapper().createArrayNode());
			} 
			else {
				//String nodeConditionJson = userPluginContext.getJsonByParentFlowKey(parentFlowKey);
				String nodeConditionJson = userPluginContext.getJsonByParentFlowKey(parentFlowKey);
				ArrayNode nodeConditionJsonAry = (ArrayNode) JsonUtil.toJsonNode(nodeConditionJson);
				jobject.set(node.getNodeId(), nodeConditionJsonAry);
			}
		}
		return jobject;
	}

    //获取所有节点人员的JSON (节点传阅人员)
    private Object getNodesReadUserJson(List<BpmNodeDef> nodeDefList, String defId, String parentFlowKey) throws Exception {

        ObjectNode jobject = JsonUtil.getMapper().createObjectNode();
        List<BpmNodeDef> userNodes = new ArrayList<BpmNodeDef>();
        for (BpmNodeDef nodeDef : nodeDefList) {
            if (nodeDef.getType() == NodeType.USERTASK || nodeDef.getType() == NodeType.SIGNTASK)
                userNodes.add(nodeDef);
        }

        for (int i = 0; i < userNodes.size(); i++) {
            BpmNodeDef node = userNodes.get(i);
            UserCopyToPluginContext userPluginContext = (UserCopyToPluginContext) node.getPluginContext(UserCopyToPluginContext.class);
            if (userPluginContext == null) {
                jobject.set(node.getNodeId(), JsonUtil.getMapper().createArrayNode());
            }
            else {
                //String nodeConditionJson = userPluginContext.getJsonByParentFlowKey(parentFlowKey);
                String nodeConditionJson = userPluginContext.getJsonByParentFlowKey(parentFlowKey);
                ArrayNode nodeConditionJsonAry = (ArrayNode) JsonUtil.toJsonNode(nodeConditionJson);
                jobject.set(node.getNodeId(), nodeConditionJsonAry);
            }
        }
        return jobject;
    }
	
	//获取全局restful事件
	private List<Restful> getGlobalRestFulList(BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt, String defId,String parentFlowKey ) throws Exception {
		List<Restful> globalRests = new ArrayList<Restful>();
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		List<BpmPluginContext> plugins = defExt.getPluginContextList();
		for (BpmPluginContext bpmPluginContext : plugins) {
			if(bpmPluginContext instanceof GlobalRestFulsPluginContext){
				GlobalRestFulsPluginContext context = (GlobalRestFulsPluginContext) bpmPluginContext;
				List<Restful> restFuls = context.getByParentFlowKey(parentFlowKey);
				if(BeanUtils.isNotEmpty(restFuls)){
					globalRests.addAll(restFuls);
				}
			}
		}
		return globalRests;
	}
	
	//获取所有节点事件的JSON
	private Object getNodesRestFulJson(List<BpmNodeDef> nodeDefList, String defId, String parentFlowKey) throws Exception {

		ObjectNode jobject = JsonUtil.getMapper().createObjectNode();
		List<BpmNodeDef> restfulNodes = new ArrayList<BpmNodeDef>();
		for (BpmNodeDef nodeDef : nodeDefList) {
			if (NodeType.USERTASK.equals(nodeDef.getType())  || NodeType.SIGNTASK.equals(nodeDef.getType())
					||NodeType.START.equals(nodeDef.getType())||NodeType.END.equals(nodeDef.getType()))
				restfulNodes.add(nodeDef);
		}

		for (int i = 0; i < restfulNodes.size(); i++) {
			BpmNodeDef node = restfulNodes.get(i);
			RestFulsPluginContext context = (RestFulsPluginContext)node.getPluginContext(RestFulsPluginContext.class);
			if (context == null) {
				jobject.set(node.getNodeId(), JsonUtil.getMapper().createArrayNode());
			} 
			else {
				String nodeRestfulJson = context.getJsonByParentFlowKey(parentFlowKey);
				JsonNode nodeRestfulJsonAry = JsonUtil.toJsonNode(nodeRestfulJson);
				jobject.set(node.getNodeId(), nodeRestfulJsonAry);
			}
		}
		return jobject;
	}
	
	private String getInnerFormKey (FormExt form){
		String formkey = "";
		if (BeanUtils.isNotEmpty(form) && BeanUtils.isNotEmpty(form.getType()) && 
		    form.getType().value().equals(FormCategory.INNER.value()) && StringUtil.isNotEmpty(form.getFormValue())) {
			return form.getFormValue();
		}
		return formkey;
	}
	
	/**
	 * 保存流程配置
	 * 
	 * @param request
	 * @param reponse
	 * @throws Exception
	 */
	@SuppressWarnings({ })
	@RequestMapping(value="saveDefConf", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存流程配置", httpMethod = "POST", notes = "保存流程配置")
	public CommonResult<String> saveDefConf(@ApiParam(name="vo",value="流程设置保存对象")@RequestBody DefConfSaveVo vo) throws Exception {
		String defId = vo.getDefId();
		String topDefKey = vo.getTopDefKey();
		String defSettingJson = vo.getDefSettingJson();
		String userJson = vo.getUserJson();
        String userReadJson = vo.getUserReadJson();
		String restfulJson = vo.getRestfulJson();
		//保存表单
		BpmDefSettingBpmDefXmlHandler bpmDefSettingBpmDefXmlHandler = AppUtil.getBean(BpmDefSettingBpmDefXmlHandler.class);
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		try {
            Map<String,Object> map = new HashMap<>();
            map.put("id", defId);
            map.put("rev", vo.getRev());
            DefaultBpmDefinition defaultBpmDefinition1 = bpmDefinitionManager.getBpmDefinitionByRev(map);
		    if(BeanUtils.isNotEmpty(defaultBpmDefinition1)) {
                defSettingJson = dealRestFulHeader(defSettingJson);
                BpmDefSetting bpmDefSetting = JsonUtil.toBean(defSettingJson, BpmDefSetting.class);

                Set<String> formKeys = new HashSet<>();

                if (BeanUtils.isNotEmpty(bpmDefSetting)) {
                    if (BeanUtils.isNotEmpty(getInnerFormKey(bpmDefSetting.getGlobalForm())))
                        formKeys.add(bpmDefSetting.getGlobalForm().getFormValue());
                    if (BeanUtils.isNotEmpty(getInnerFormKey(bpmDefSetting.getGlobalMobileForm())))
                        formKeys.add(bpmDefSetting.getGlobalMobileForm().getFormValue());
                    if (BeanUtils.isNotEmpty(getInnerFormKey(bpmDefSetting.getInstForm())))
                        formKeys.add(bpmDefSetting.getInstForm().getFormValue());
                    if (BeanUtils.isNotEmpty(getInnerFormKey(bpmDefSetting.getInstMobileForm())))
                        formKeys.add(bpmDefSetting.getInstMobileForm().getFormValue());
                    if (BeanUtils.isNotEmpty(bpmDefSetting.getNodeForms())) {
                        List<Form> nodeForms = bpmDefSetting.getNodeForms();
                        for (Form form : nodeForms) {
                            if (BeanUtils.isNotEmpty(getInnerFormKey((FormExt)form))) formKeys.add(form.getFormValue());
                        }
                    }
                }
                Map<String, ObjectNode> boMap = new HashMap<>();
                FormFeignService formRestfulService = AppUtil.getBean(FormFeignService.class);
                for (Iterator<String> iterator = formKeys.iterator(); iterator.hasNext(); ) {
                    String formkey = iterator.next();
                    if (StringUtil.isNotEmpty(formkey)) {
                        List<ObjectNode> list = formRestfulService.getFormBoLists(formkey);
                        for (ObjectNode objectNode : list) {
                            boMap.put(objectNode.get("alias").asText(), objectNode);
                        }
                    }
                }
                //如果保存的不是子流程。找到子流程节点，把子流程的业务对象也添加到主流程中
                if (StringUtil.isEmpty(topDefKey)) {
                	List<BpmNodeDef> bpmnNodeDefs = bpmProcessDefExt.getBpmnNodeDefs();
            		BpmDefinitionManager bpmDefinitionManager = AppUtil.getBean(BpmDefinitionManager.class);
            		for (BpmNodeDef bpmNodeDef : bpmnNodeDefs) {
            			if (bpmNodeDef instanceof CallActivityNodeDef) {
            				String subFlowKey = ((CallActivityNodeDef)bpmNodeDef).getFlowKey();
            				DefaultBpmDefinition mainByDefKey = bpmDefinitionManager.getMainByDefKey(subFlowKey, false);
            				if (BeanUtils.isEmpty(mainByDefKey)) {
            					continue;
            				}
            				BpmProcessDef<BpmProcessDefExt> subProcessDef = bpmDefinitionAccessor.getBpmProcessDef(mainByDefKey.getDefId());
            				List<ProcBoDef> boDefList = subProcessDef.getProcessDefExt().getBoDefList();
            				for (ProcBoDef procBoDef : boDefList) {
            					if (!boMap.containsKey(procBoDef.getKey()) && procBoDef.getParentDefKey().equals(bpmProcessDefExt.getDefKey())) {
            						ObjectNode objectNode = JsonUtil.getMapper().createObjectNode();
            						objectNode.put("alias", procBoDef.getKey());
            						boMap.put(procBoDef.getKey(), objectNode);
            					}
            				}
            			}
            		}
				}
                BpmBoDef bpmBoDef = new BpmBoDef();
                Iterator<Entry<String, ObjectNode>> it = boMap.entrySet().iterator();
                List<ProcBoDef> boDefs = new ArrayList<ProcBoDef>();
                while (it.hasNext()) {
                    Entry<String, ObjectNode> entry = it.next();
                    ObjectNode value = entry.getValue();
                    ProcBoDef procBoDef = new ProcBoDef();
                    procBoDef.setKey(value.get("alias").asText());
                    procBoDef.setName(value.get("alias").asText());
                    if (StringUtil.isNotEmpty(topDefKey)) {
                    	procBoDef.setParentDefKey(topDefKey);
					}
                    boDefs.add(procBoDef);
                }
                bpmBoDef.setBoDefs(boDefs);
                bpmBoDef.setBoSaveMode("database");
                saveBpmProBoList("", defId, "", topDefKey, bpmBoDef);
                //保存bo
                boBpmDefXmlHandler.saveNodeXml(defId, "", bpmBoDef);
                bpmDefSettingBpmDefXmlHandler.saveNodeXml(defId, null, bpmDefSetting);

                //设置是否支持手机表单。
                int supportMobile = isMobileSet(defId, bpmDefSetting) ? 1 : 0;
                DefaultBpmDefinition def = (DefaultBpmDefinition) bpmDefinitionService.getBpmDefinitionByDefId(defId);
                def.setSupportMobile(supportMobile);
                def.setUpdateTime(LocalDateTime.now());
                bpmDefinitionManager.update(def);
            }else{
                return  new CommonResult<String>(false,"此流程定义不是最新版本，请重新获取再修改");
            }
		} catch (Exception e) {
			e.printStackTrace();
			return  new CommonResult<String>(false,"保存表单参数失败："+e.getMessage());
		}
		
		//保存全局restful插件
		try {
			ObjectNode settingJson =(ObjectNode) JsonUtil.toJsonNode(defSettingJson);
			String globalRestfulStr = settingJson.get("globalRestfuls").toString();
			if(StringUtil.isNotEmpty(globalRestfulStr)||settingJson.get("globalRestfuls").size()>0){
				GlobalRestFulsPluginContext globalContext = new GlobalRestFulsPluginContext();
				globalContext.parse(globalRestfulStr);
				DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
				List<BpmPluginContext> plugins = changeOnePluginContextForSave(defExt.getPluginContextList(),globalContext);
				PluginsBpmDefXmlHandler bpmDefXmlHandler = (PluginsBpmDefXmlHandler) AppUtil.getBean(PluginsBpmDefXmlHandler.class);
				bpmDefXmlHandler.saveNodeXml(defId, null, plugins);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		String theNode = "";
		try {
			//保存审批人员
			ObjectNode objectNode =(ObjectNode) JsonUtil.toJsonNode(userJson);
			Iterator<Entry<String, JsonNode>> fields = objectNode.fields();
			while (fields.hasNext()) {
                Entry<String, JsonNode> ent = fields.next();
                theNode = ent.getKey();
                UserDefBpmDefXmlHandler userDefBpmDefXmlHandler = (UserDefBpmDefXmlHandler) AppUtil.getBean(UserDefBpmDefXmlHandler.class);
                userDefBpmDefXmlHandler.saveNodeXml(defId, ent.getKey(), JsonUtil.toJson(objectNode.get(ent.getKey())), topDefKey);
			}
            //保存传阅人员
            ObjectNode objectNodeRead =(ObjectNode) JsonUtil.toJsonNode(userReadJson);
            Iterator<Entry<String, JsonNode>> fieldsRead = objectNodeRead.fields();
            while (fieldsRead.hasNext()) {
                Entry<String, JsonNode> ent = fieldsRead.next();
                theNode = ent.getKey();
                UserCopyToDefBpmDefXmlHandler userCopyToDefBpmDefXmlHandler = (UserCopyToDefBpmDefXmlHandler) AppUtil.getBean(UserCopyToDefBpmDefXmlHandler.class);
                userCopyToDefBpmDefXmlHandler.saveNodeXml(defId, ent.getKey(), JsonUtil.toJson(objectNodeRead.get(ent.getKey())), topDefKey);
            }
        }catch (Exception e) {
			e.printStackTrace();
			return  new CommonResult<String>(false,theNode+"节点人员失败："+e.getMessage());
		}
		
		try {
			//保存节点restful事件
			ObjectNode restObjectNode =(ObjectNode) JsonUtil.toJsonNode(restfulJson);
			Iterator<Entry<String, JsonNode>> fields = restObjectNode.fields();
			 while (fields.hasNext())  {
				 Entry<String, JsonNode> ent = fields.next();
				BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, ent.getKey());
				RestFulsPluginContext context = new RestFulsPluginContext();
				if(restObjectNode.get(ent.getKey()).size()==0) continue;
				context.parse(JsonUtil.toJson(restObjectNode.get(ent.getKey())));
				List<BpmPluginContext> plugins = changeOnePluginContextForSave(nodeDef.getBpmPluginContexts(),context);
				PluginsBpmDefXmlHandler bpmDefXmlHandler = (PluginsBpmDefXmlHandler) AppUtil.getBean(PluginsBpmDefXmlHandler.class);
				bpmDefXmlHandler.saveNodeXml(defId, ent.getKey(), plugins);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  new CommonResult<String>(false,theNode+"节点Restful事件保存失败："+e.getMessage());
		}
		
		return  new CommonResult<String>("流程配置保存成功");
	}
	
	//处理header json解析失败问题
	private String dealRestFulHeader(String defSettingJson) throws IOException{
		JsonNode node = JsonUtil.toJsonNode(defSettingJson);
		ArrayNode globalRestArray = (ArrayNode) node.get("globalRestfuls");
		if(BeanUtils.isNotEmpty(globalRestArray)){
			for (JsonNode jsonNode : globalRestArray) {
				ObjectNode restJson = (ObjectNode) jsonNode;
				if(BeanUtils.isNotEmpty(restJson.get("header")) && StringUtil.isEmpty(restJson.get("header").asText())){
					restJson.put("header", restJson.get("header").toString());
				}
			}
			
		}
		return node.toString();
	}
	
	
	private void saveBpmProBoList(String userId, String flowId, String flowKey,String parentFlowKey, BpmBoDef bpmBoDef) throws Exception {

		// 清除之前的流程和BO的绑定数据
		boolean mark = true;
		if (StringUtil.isNotEmpty(flowId)) {
			bpmProBoManager.removeByProcessId(flowId);
			mark = false;
		}
		if (mark && StringUtil.isNotEmpty(flowKey)) {
			bpmProBoManager.removeByProcessKey(flowKey);
		}
		
		// 封装数据到绑定表
		List<BpmProBo> bpmProBoList = new ArrayList<BpmProBo>();
		List<ProcBoDef> boDefs = bpmBoDef.getBoDefs();

		for (ProcBoDef procBoDef : boDefs) {
			BpmProBo bpmProBo = new BpmProBo();
			bpmProBo.setProcessId(flowId);
			bpmProBo.setProcessKey(flowKey);
			bpmProBo.setBoCode(procBoDef.getKey());
			bpmProBo.setBoName(procBoDef.getName());
			bpmProBo.setCreatorId(userId);
			bpmProBoList.add(bpmProBo);
		}
		bpmProBoManager.createByBpmProBoList(bpmProBoList);
		
	}
	
	
	/**
	 * 替换要保存的插件，
	 * @return List<BpmPluginContext>
	 */
	private List<BpmPluginContext> changeOnePluginContextForSave(List<BpmPluginContext> contexts,BpmPluginContext pluginContext){
		List<BpmPluginContext> bpmPluginContexts = new ArrayList<BpmPluginContext>();
		bpmPluginContexts.add(pluginContext);
		
		if(BeanUtils.isEmpty(contexts)) return bpmPluginContexts;
		
		for(BpmPluginContext context : contexts){
			if(!context.getClass().isAssignableFrom(pluginContext.getClass())){
				bpmPluginContexts.add(context);
			}
		}
		return bpmPluginContexts;
	}
	
	
	/**
	 * 取得当前节点的所有出口节点集合
	 * @param defId
	 * @param nodeId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="getNodeOutcomes",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得当前节点的所有出口节点集合", httpMethod = "GET", notes = "取得当前节点的所有出口节点集合")
	public Map<String,Object> getNodeOutcomes(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId) throws Exception{
		BpmNodeDef nodeDef = bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		if(nodeDef == null) return null;
		Map<String,Object> data = new HashMap<>();
		ArrayNode outComes = JsonUtil.getMapper().createArrayNode();
		for(BpmNodeDef n :nodeDef.getOutcomeNodes()){
			ObjectNode nodeJson = JsonUtil.getMapper().createObjectNode();
			nodeJson.put("nodeName", n.getName());
			nodeJson.put("nodeId", n.getNodeId());
			outComes.add(nodeJson);
		}
		data.put("scriptMap",nodeDef.getConditions());
		data.put("outComes", outComes);
		return data;
	}
	
	/**
	 * 自动任务信息明细
	 */
	@RequestMapping(value="getNodeAutoTask",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "自动任务信息明细", httpMethod = "GET", notes = "自动任务信息明细")
	public Object getNodeAutoTask(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id", required = true) @RequestParam String nodeId) throws Exception {
		AutoTaskDef autoTaskDef = (AutoTaskDef) bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
		AbstractBpmPluginContext bpmPluginContext = (AbstractBpmPluginContext)autoTaskDef.getAutoTaskBpmPluginContext();
		if(bpmPluginContext == null)return null;
		
		ObjectNode object =(ObjectNode) JsonUtil.toJsonNode( bpmPluginContext.getJson());
		object.put("title", bpmPluginContext.getTitle());
		return object;
	}
}
