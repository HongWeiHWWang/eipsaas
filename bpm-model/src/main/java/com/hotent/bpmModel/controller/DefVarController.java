package com.hotent.bpmModel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.def.BpmVariableDef;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.UserTaskNodeDef;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.engine.def.impl.handler.VarDefBpmDefXmlHandler;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpmModel.params.BpmVariableDefVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 *  描述：流程变量管理
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/flow/var/v1/")
@Api(tags="流程变量")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class DefVarController {
	@Resource
	VarDefBpmDefXmlHandler varDefBpmDefXmlHandler;
	@Resource
	BpmDefinitionService bpmDefinitionService;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;

	/**
	 * 流程变量列表数据
	 */
	@RequestMapping(value="listJson", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程变量列表数据", httpMethod = "GET", notes = "流程变量列表数据")
	public List<BpmVariableDef> listJson(
			@ApiParam(name="defId",value="常用语id")@RequestParam String defId,
			@ApiParam(name="nodeId",value="节点id")@RequestParam String nodeId) throws Exception {
		List<BpmVariableDef> bpmVariableList = new ArrayList<BpmVariableDef>();

		if (StringUtil.isNotEmpty(nodeId) && StringUtil.isNotEmpty(defId)) {
			UserTaskNodeDef taskNodeDef = (UserTaskNodeDef) bpmDefinitionAccessor.getBpmNodeDef(defId, nodeId);
			bpmVariableList = taskNodeDef.getVariableList();
		} else {
			if (StringUtil.isNotEmpty(defId))
				bpmVariableList = getAllBpmVariableDef(defId);
		}
		return bpmVariableList;
	}

	/**
	 * 所有的变量
	 * @throws Exception 
	 */
	private List<BpmVariableDef> getAllBpmVariableDef(String defId) throws Exception {
		List<BpmVariableDef> bpmVariableList = new ArrayList<BpmVariableDef>();
		// 全局变量
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		if (defExt.getVariableList() != null)
			bpmVariableList.addAll(defExt.getVariableList());

		// 节点变量
		List<BpmNodeDef> bpmNodeDefList = bpmDefinitionAccessor.getNodesByType(defId, NodeType.USERTASK);
		bpmNodeDefList.addAll(bpmDefinitionAccessor.getNodesByType(defId, NodeType.SIGNTASK));

		for (BpmNodeDef bpmNodeDef : bpmNodeDefList) {
			UserTaskNodeDef taskNodeDef = (UserTaskNodeDef) bpmNodeDef;
			List<BpmVariableDef> nodeVarList = taskNodeDef.getVariableList();
			if (nodeVarList != null)
				bpmVariableList.addAll(nodeVarList);
		}

		return bpmVariableList;
	}

	/**
	 * 编辑节点变量
	 */
	@RequestMapping(value="defVarEdit", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "编辑节点变量", httpMethod = "GET", notes = "编辑节点变量")
	public Map<String,Object> defVarEdit(
			@ApiParam(name="defId",value="常用语id")@RequestParam String defId,
			@ApiParam(name="varKey",value="变量key")@RequestParam String varKey) throws Exception {

		List<BpmNodeDef> nodeDefList = bpmDefinitionAccessor.getNodeDefs(defId);
		List<BpmVariableDef> list = getAllBpmVariableDef(defId);
		BpmVariableDef bpmVariableDef = null;
		List<ObjectNode> nodeList=new ArrayList<>();
		for (BpmNodeDef bpmNodeDef : nodeDefList) {
			if(bpmNodeDef.getType()==NodeType.USERTASK) {//过滤其他节点，只取用户任务节点
				ObjectNode node =JsonUtil.getMapper().createObjectNode();
				node.put("nodeId", bpmNodeDef.getNodeId());
				node.put("name", bpmNodeDef.getName());
				nodeList.add(node);
			}
		}
		for (BpmVariableDef varDef : list) {
			if (varKey.equals(varDef.getVarKey())) {
				bpmVariableDef = varDef;
			}
		}
		Map<String,Object> obj=new HashMap<>();
		obj.put("bpmVariableDef",BeanUtils.isEmpty(bpmVariableDef)?null:JsonUtil.toJsonNode( bpmVariableDef));
		obj.put("nodeDefList", nodeList);
		return obj;
	}

	/**
	 * 删除节点变量
	 */
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除节点变量", httpMethod = "DELETE", notes = "删除节点变量")
	public CommonResult<String> remove(
			@ApiParam(name="defId",value="流程定义id", required = true) @RequestParam String defId,
			@ApiParam(name="varKey",value="变量key", required = true) @RequestParam String varKey) throws Exception {
		try {
			List<BpmVariableDef> list = getAllBpmVariableDef(defId);
			List<BpmVariableDef> bpmVariableDefs = new ArrayList<BpmVariableDef>();

			for (BpmVariableDef varDef : list) {
				if (!varKey.equals(varDef.getVarKey())) {
					bpmVariableDefs.add(varDef);
				}
			}
			varDefBpmDefXmlHandler.saveNodeXml(defId, null, bpmVariableDefs);
			return new CommonResult<String>(true,"删除成功","");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"删除失败:"+e.getMessage(),"");
		}
	}

	/**
	 * 保存节点规则
	 */
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存节点规则", httpMethod = "POST", notes = "保存节点规则")
	public CommonResult<String> save(
			@ApiParam(name="variableDefVo",value="保存节点规则", required = true) @RequestBody BpmVariableDefVo variableDefVo) throws Exception {
		String defId = variableDefVo.getDefId();
		boolean isAdd = variableDefVo.getIsAdd();
		BpmVariableDef  variableDef=(BpmVariableDef) variableDefVo.getVariableDef();
		String varKey = variableDef.getVarKey();
		try {
			List<BpmVariableDef> list = getAllBpmVariableDef(defId);
			List<BpmVariableDef> bpmVariableDefs = new ArrayList<BpmVariableDef>();
			// 修改，过滤掉旧数据。 新增情况存在相同key 抛出异常。
			for (BpmVariableDef varDef : list) {
				if (varKey.equals(varDef.getVarKey())) {
					if (isAdd)
						throw new Exception("变量Key必须唯一！");
				} else {
					bpmVariableDefs.add(varDef);
				}
			}
			bpmVariableDefs.add(variableDef);

			varDefBpmDefXmlHandler.saveNodeXml(defId, null, bpmVariableDefs);
			return new CommonResult<String>(true,"操作成功！","");
		} catch (Exception e) {
			return new CommonResult<String>(false,"操作失败:"+e.getMessage(),"");
		}
	}
}
