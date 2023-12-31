package com.hotent.form.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.form.model.FormRight;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.persistence.manager.FormRightManager;
import com.hotent.form.vo.FormRigthParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 表单权限管理
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月8日
 */
@RestController
@RequestMapping("/form/rights/v1")
@Api(tags="表单权限")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class FormRightsController extends BaseController<FormRightManager, FormRight>{
	@Resource
	private FormRightManager bpmFormRightManager;
	@Resource
	private FormMetaManager bpmFormDefManager;


	@RequestMapping(value="getPermission", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单权限", httpMethod = "POST", notes = "获取表单权限")
	public Map<String,Object>   getPermission(@ApiParam(name="param",value="表单权限参数对象")@RequestBody FormRigthParam param) throws IOException {
		if(param.getType()==1){
			bpmFormRightManager.remove(param.getFormKey(), param.getFlowKey(), param.getNodeId(), param.getParentflowKey());
		}

		JsonNode json = bpmFormRightManager.getPermissionSetting(param.getFormKey(), param.getFlowKey(), param.getParentflowKey(), param.getNodeId(),  param.getType(),true);
		List<Map<String,String>> tableSn =  bpmFormRightManager.getTableOrderBySn(param.getFormKey());
		int maxSn = tableSn.size();
		for (Iterator<Entry<String, JsonNode>> iterator = json.get("table").fields(); iterator.hasNext();) {
			Entry<String, JsonNode> next =  iterator.next();
			JsonNode tab = next.getValue();
			if (tab.hasNonNull("ctrlType") && "dataView".equals(tab.get("ctrlType").asText())) {
				Map<String,String> snMap =new HashMap<>();
				snMap.put("desc_", tab.get("description").asText());
				snMap.put("name_", next.getKey());
				snMap.put("type_", "onetoone");
				snMap.put("ctrlType", "dataView");
				snMap.put("sn_", String.valueOf(maxSn));
				maxSn++;
				tableSn.add(snMap);
			}
		}
		Map<String,Object>  map=new HashMap<>();
		map.put("json", json);
		map.put("permissionList", AppUtil.getBean("defaultObjectRightType"));
		map.put("tableSn", tableSn);
		return map;
	}

	/**
	 * 保存表单权限配置。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="save", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存表单权限配置", httpMethod = "POST", notes = "获取表单权限")
	public CommonResult<String> save(@ApiParam(name="param",value="表单权限参数对象")@RequestBody FormRigthParam param) {
		//权限表中存入表单元数据key。
		bpmFormRightManager.save(param.getFormKey(), param.getFlowKey(), param.getParentflowKey(), param.getNodeId(), param.getPermission(), param.getType(),param.getIsCheckOpinion());
		return new CommonResult<String>(true,"保存表单权限配置成功",null);
	}

	/**
	 * 获取默认表单权限。
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="getDefaultByFormKey", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存表单权限配置", httpMethod = "POST", notes = "获取表单权限")
	public Map<String,Object> getDefaultByFormKey(@ApiParam(name="formKey",value="表单KEY")@RequestParam String formKey,
			@ApiParam(name="type",value="权限类型 ")@RequestParam String type) throws IOException {
		// 1.为流程权限，2，为实例权限。
		boolean isInstance = !"1".equals(type);
		String formMetaKey=bpmFormDefManager.getMetaKeyByFormKey(formKey);
		JsonNode json = bpmFormRightManager.getDefaultByFormDefKey(formMetaKey, isInstance);
		Map<String,Object> jo = new  HashMap<>();
		jo.put("json", json);
		jo.put("tableSn", bpmFormRightManager.getTableOrderBySn(formKey));
		return jo;
	}
	
	/**
	 * 清空流程绑定的所有表单权限
	 * @param flowKey
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="emptyAll", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "清空流程绑定的所有表单权限", httpMethod = "GET", notes = "清空流程绑定的所有表单权限")
	public CommonResult<String> emptyAll(@ApiParam(name="flowKey",value="流程KEY")@RequestParam String flowKey) throws Exception {
		bpmFormRightManager.emptyAll(flowKey);
		return new CommonResult<String>(true,"清空表单权限成功！",null);
	}


	@RequestMapping(value="saveSub", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存表单权限配置", httpMethod = "POST", notes = "获取表单权限")
	public void saveSub(@ApiParam(name="nodeId",value="节点ID")@RequestBody String nodeId,
			@ApiParam(name="defId",value="权限类型 ")@RequestBody String defId,
			@ApiParam(name="parentDefKey",value="父流程定义")@RequestBody String parentDefKey) throws Exception {
		//TODO
		/*try {
			Map<String, Object> param = new HashMap<String, Object>();
			List<BpmSubTableRight> rights = new ArrayList<BpmSubTableRight>();
			String json = FileUtil.inputStream2String(request.getInputStream());
			JsonNode jobj = JsonUtil.toJsonNode(json);
			for (JsonNode jsonNode : jobj) {
				BpmSubTableRight right=JsonUtil.toBean(jsonNode, BpmSubTableRight.class);
			}
			param.put("list", rights);
			param.put("parentDefKey", parentDefKey);
			BpmDefXmlHandler handler = AppUtil.getBean(SubRightBpmDefXmlHandler.class);
			handler.saveNodeXml(defId, nodeId, param);
			writeResultMessage(response.getWriter(), "保存子表权限成功", ResultMessage.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), "保存子表权限失败", e.getMessage(), ResultMessage.FAIL);
		}*/
	}


	
	@RequestMapping(value="remove", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "删除表单权限配置", httpMethod = "GET", notes = "删除表单权限")
	public CommonResult<String> remove(@ApiParam(name="flowKey",value="流程key")@RequestParam String flowKey,
			@ApiParam(name="nodeId",value="节点ID")@RequestParam String nodeId,
			@ApiParam(name="parentFlowKey",value="父流程定义KEY")@RequestParam String parentFlowKey) throws Exception {
		bpmFormRightManager.remove(flowKey, nodeId, parentFlowKey);
		return new CommonResult<String>(true,"表单设置已清除！",null);
	}

    @RequestMapping(value="getByTeam", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
    @ApiOperation(value = "根据流程定义KEY、节点ID判断当前节点审批记录是否显示", httpMethod = "GET", notes = "根据流程定义KEY、节点ID判断当前节点审批记录是否显示")
    public String getByTeam(@ApiParam(name="flowKey",value="流程key")@RequestParam String flowKey,
                                       @ApiParam(name="nodeId",value="节点ID")@RequestParam String nodeId) throws Exception {

	    String isCheckOpinion = "";
        isCheckOpinion = bpmFormRightManager.getByTeam(flowKey,nodeId);
        if(StringUtil.isEmpty(isCheckOpinion)){
            isCheckOpinion="false";
        }
	    return isCheckOpinion;

    }

}
