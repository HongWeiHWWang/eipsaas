package com.hotent.portal.controller;

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
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.portal.model.MsgTemplate;
import com.hotent.portal.persistence.manager.MsgTemplateManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IGroup;
import com.hotent.uc.api.service.IUserGroupService;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 描述：消息模版 控制器类
 * @company 广州宏天软件有限公司
 * @author wanghb
 * @email wanghb@jee-soft.cn
 * @date 2018年7月19日
 */
@RestController
@RequestMapping("/flow/MsgTemplate/v1")
@Api(tags="消息模板管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class MsgTemplateController extends BaseController<MsgTemplateManager, MsgTemplate>{
	@Resource
	MsgTemplateManager MsgTemplateManager;
	@Resource
	IUserService userServiceImpl;
	@Resource
	IUserGroupService userGroupService;
	
	/**
	 * 消息模版列表(分页条件查询)数据
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "消息模版列表(分页条件查询)数据", httpMethod = "POST", notes = "消息模版列表(分页条件查询)数据")
	public PageList<MsgTemplate> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<MsgTemplate> queryFilter) throws Exception {
		PageList<MsgTemplate> sysMsgTemplateList=MsgTemplateManager.query(queryFilter);
		return sysMsgTemplateList;
	}

	/**
	 * 保存消息模版信息
	 * @param request
	 * @param response
	 * @param MsgTemplate
	 * @throws Exception 
	 * void
	 * @exception 
	 */
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存消息模版信息", httpMethod = "POST", notes = "保存消息模版信息")
	public CommonResult<String> save(
			@ApiParam(name="template",value="代理对象", required = true) @RequestBody MsgTemplate msgTemplate)throws Exception {
		String resultMsg=null;
		String currentUserId = ContextUtil.getCurrentUserId();
		IGroup group =ContextUtil.getCurrentGroup();
		String currentGroupId = "0";
		if(group!= null)currentGroupId = group.getGroupId();
		try {
			boolean isExist=false;
			if(StringUtil.isEmpty(msgTemplate.getId())){
				QueryFilter queryFilter=QueryFilter.build().withDefaultPage();
				queryFilter.addFilter("KEY_", msgTemplate.getKey(), QueryOP.EQUAL);
				queryFilter.addFilter("TYPE_KEY_", msgTemplate.getTypeKey(), QueryOP.EQUAL);
				PageList<MsgTemplate> query = MsgTemplateManager.query(queryFilter);
				isExist=query.getRows().size()>0;
				if (isExist) {
					resultMsg="消息模版业务键已经存在,添加失败!";
				}else {
					msgTemplate.setId(UniqueIdUtil.getSuid());
					msgTemplate.setCreateBy(currentUserId);
					msgTemplate.setCreateOrgId(currentGroupId);
					MsgTemplateManager.create(msgTemplate);
					resultMsg="添加消息模版成功";
				}
			}else{
				MsgTemplate sysMsgTemplateTemp = MsgTemplateManager.get(msgTemplate.getId());
				if (BeanUtils.isNotEmpty(sysMsgTemplateTemp)) {
					if(!sysMsgTemplateTemp.getKey().equals(msgTemplate.getKey())){
						isExist = MsgTemplateManager.isExistByKeyAndTypeKey(msgTemplate.getKey(),msgTemplate.getTypeKey());
					}
				}else {
					isExist = MsgTemplateManager.isExistByKeyAndTypeKey(msgTemplate.getKey(),msgTemplate.getTypeKey());
				}
				if (isExist) {
					resultMsg="消息模版业务键已经存在,更新失败!";
				}else {
					msgTemplate.setUpdateBy(currentUserId);
					MsgTemplateManager.update(msgTemplate);
					resultMsg="更新消息模版成功";
				}
			}
			return new CommonResult<String>(resultMsg);
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,resultMsg+e.getMessage());
		}
	}
	
	/**
	 * 批量删除消息模版记录
	 * @param request
	 * @param response
	 * @throws Exception 
	 * void
	 * @exception 
	 */
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除消息模版记录", httpMethod = "DELETE", notes = "批量删除消息模版记录)")
	public CommonResult<String> remove(
			@ApiParam(name="ids",value="模板id", required = true) @RequestParam String ids) throws Exception{
		try {
			String[] aryIds=ids.split(",");
			MsgTemplateManager.removeByIds(aryIds);
			return new CommonResult<String>("删除消息模版成功");
		} catch (Exception e) {
			return new CommonResult<String>(false,"删除消息模版失败:"+e.getMessage());
		}
	}
	
	/**
	 * 根据ID获取内容
	 * @param request
	 * @param reponse
	 * @return
	 */
	@RequestMapping(value="getById",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据ID获取内容", httpMethod = "GET", notes = "根据ID获取内容")
	public MsgTemplate getById(
			@ApiParam(name="id",value="模板id", required = true) @RequestParam String id){
		MsgTemplate MsgTemplate = MsgTemplateManager.get(id);
		return MsgTemplate;
	}
	
	/**
	 * 设置消息模板未默认
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="setDefault",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "设置消息模板未默认", httpMethod = "GET", notes = "设置消息模板未默认")
	public CommonResult<String> setDefault(
			@ApiParam(name="id",value="模板id", required = true) @RequestParam String id) throws Exception{
		try {
			MsgTemplateManager.setDefault(id);
			return new CommonResult<String>("设置默认成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"设置默认失败:"+e.getMessage());
		}
	}

	/**
	 * 设置消息模板未默认
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="setNotDefault",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取消默认消息模板", httpMethod = "GET", notes = "取消默认消息模板")
	public CommonResult<String> setNotDefault(
			@ApiParam(name="id",value="模板id", required = true) @RequestParam String id) throws Exception{
		try {
			MsgTemplateManager.setNotDefault(id);
			return new CommonResult<String>("取消默认成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"取消默认失败:"+e.getMessage());
		}
	}
}
