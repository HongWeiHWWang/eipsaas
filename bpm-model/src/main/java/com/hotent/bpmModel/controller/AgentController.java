package com.hotent.bpmModel.controller;

import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.manager.BpmAgentSettingManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.model.BpmAgentSetting;
import com.hotent.bpm.persistence.model.ResultMessage;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 描述：流程委托设置管理
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年6月26日
 */
@RestController
@RequestMapping("/flow/agent/v1/")
@Api(tags="流程委托设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class AgentController extends BaseController<BpmAgentSettingManager, BpmAgentSetting>{
	@Resource
	BpmDefinitionManager bpmDefinitionManager;

	/**
	 * 流程委托设置列表(分页条件查询)数据
	 * @return
	 * @throws Exception
	 *             PageJson
	 */
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流程委托设置列表(分页条件查询)数据", httpMethod = "POST", notes = "流程委托设置列表(分页条件查询)数据")
	public PageList<BpmAgentSetting> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<BpmAgentSetting> queryFilter,
			@ApiParam(name="isMgr",value="通用查询对象")@RequestParam Optional<Boolean> isMgr) throws Exception {
		if(!isMgr.orElse(false)){
			queryFilter.addFilter("auth_id_",ContextUtil.getCurrentUserId(),QueryOP.EQUAL, FieldRelation.AND,"a");
		}
		PageList<BpmAgentSetting> bpmAgentSettingList = baseService.query(queryFilter);
		return bpmAgentSettingList;
	}

	/**
	 * 获取流程委托设置页面
	 *
	 * @return
	 * @throws Exception
	 *             ModelAndView
	 * @exception
	 * @since 1.0.0
	 */
	@RequestMapping(value="agentGet",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据委托id获取委托信息", httpMethod = "GET", notes = "获取bo定义详情")
	public Object get(
			@ApiParam(name="id",value="，流程委托id", required = true) @RequestParam String id) throws Exception {
		BpmAgentSetting bpmAgentSetting = null;
		if (StringUtil.isNotEmpty(id)) {
			bpmAgentSetting = baseService.getById(id);
		}
		return bpmAgentSetting;
	}

	/**
	 * 保存委托信息
	 *
	 * @throws Exception
	 *             void
	 * @exception
	 * @since 1.0.0
	 */
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存委托信息", httpMethod = "POST", notes = "保存委托信息")
	public CommonResult<String> save(
			@ApiParam(name="bpmAgentSet",value="委托对象", required = true) @RequestBody BpmAgentSetting bpmAgentSet)throws Exception {
		String resultMsg = "";
		try {
			BpmAgentSetting bpmAgentSetting = getAgentSetting(bpmAgentSet);

			ResultMessage result = baseService.checkConflict(bpmAgentSetting);
			if (ResultMessage.FAIL == result.getResult()) {
				return new CommonResult<String>(false,result.getMessage(),"");
			}
			String id = bpmAgentSetting.getId();
			if (StringUtil.isEmpty(id)) {
				baseService.create(bpmAgentSetting);
				resultMsg = "添加流程委托设置成功";
			} else {
				baseService.update(bpmAgentSetting);
				resultMsg = "更新流程委托设置成功";
			}
			return new CommonResult<String>(true,resultMsg,"");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,e.getMessage(),"");
		}
	}

	/**
	 * 批量删除系统用户记录(逻辑删除)
	 *
	 * @throws Exception
	 *             void
	 * @exception
	 * @since 1.0.0
	 */
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除系统用户记录(逻辑删除)", httpMethod = "DELETE", notes = "批量删除系统用户记录(逻辑删除)")
	public CommonResult<String> remove(@ApiParam(name="ids",value="用户id字符串", required = true) @RequestParam String ids)throws Exception {
		try {
			String[] aryIds = ids.split(",");
			baseService.removeByIds(aryIds);
			return new CommonResult<String>(true,"删除流程委托设置成功","");
		} catch (Exception e) {
			return new CommonResult<String>(false,"删除流程委托设置失败","");
		}
	}

	private BpmAgentSetting getAgentSetting(BpmAgentSetting agentSetting) throws Exception {
        IUser user = ContextUtil.getCurrentUser();
	    if(StringUtil.isEmpty(agentSetting.getAuthId())){
            agentSetting.setAuthId(user.getUserId());
            agentSetting.setAuthName(user.getFullname());
        }

	    if(StringUtil.isEmpty(agentSetting.getAgentId())){
	    	
            agentSetting.setAgentId(user.getUserId());
            agentSetting.setAgent(user.getFullname());
        }
		if (BpmAgentSetting.TYPE_GLOBAL.equals(agentSetting.getType())) {
			agentSetting.getDefList().clear();
			agentSetting.getConditionList().clear();
			agentSetting.setFlowKey(null);
			agentSetting.setFlowName(null);
		} else if (BpmAgentSetting.TYPE_PART.equals(agentSetting.getType())) {
			agentSetting.getConditionList().clear();
			agentSetting.setFlowKey(null);
			agentSetting.setFlowName(null);
		} else if (BpmAgentSetting.TYPE_CONDITION
				.equals(agentSetting.getType())) {
			agentSetting.getDefList().clear();
			agentSetting.setAgentId(null);
			agentSetting.setAgent(null);
		} else {
			throw new Exception("无效委托设定");
		}
		//添加用户组织信息
		String id = agentSetting.getId();
		String iGroup = ContextUtil.getCurrentGroupId();
		if (StringUtil.isEmpty(id)) {
			agentSetting.setCreateBy(user.getUserId());
			agentSetting.setCreateOrgId(iGroup);
		} else {
			agentSetting.setUpdateBy(user.getUserId());
		}
		return agentSetting;
	}
}
