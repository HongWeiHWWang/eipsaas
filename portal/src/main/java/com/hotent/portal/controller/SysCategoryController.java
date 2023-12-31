package com.hotent.portal.controller;


import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.id.IdGenerator;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.sys.persistence.manager.SysCategoryManager;
import com.hotent.sys.persistence.model.SysCategory;
import com.hotent.uc.api.impl.util.ContextUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 
 * <pre> 
 * 描述：系统分类组值表管理
 * 构建组：x5-bpmx-platform
 * 作者:zyp
 * 邮箱:zyp@jee-soft.cn
 * 日期:2014-1-10-下午3:29:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/sys/category/v1/")
@Api(tags="系统分类管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysCategoryController extends BaseController<SysCategoryManager, SysCategory> {
	@Resource
	SysCategoryManager sysCategoryManager;
	@Resource
	IdGenerator idGenerator;
	
	@RequestMapping(value="list", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "分类标识列表", httpMethod = "POST")
	public PageList<SysCategory> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysCategory> queryFilter) throws Exception{
		return sysCategoryManager.query(queryFilter);
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取标识json", httpMethod = "GET")
	public SysCategory edit(@ApiParam(name="id",value="主键")@RequestParam String id) throws Exception{
		SysCategory sysCategory=null;
		if(StringUtil.isNotEmpty(id)){
			sysCategory=sysCategoryManager.get(id);
		}
		return sysCategory;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存分类标识", httpMethod = "POST")
	public CommonResult<String> save(@ApiParam(name="sysCategory",value="标识对象")@RequestBody SysCategory sysCategory) throws Exception{
		String id=sysCategory.getId();
		String groupKey=sysCategory.getGroupKey();
		if (StringUtil.isEmpty(id)) 
			id=null;
		boolean isKeyExist=sysCategoryManager.isKeyExist(id,groupKey);
		if (isKeyExist) {
			return new CommonResult<String>(false, "输入的分类组主键系统中已存在!");
		}
		try {
			if(StringUtil.isEmpty(id)){
				sysCategory.setId(idGenerator.getSuid());
				sysCategory.setFlag(0);
				sysCategory.setSn(0);
				//当前用户的组织ID 临时
				sysCategory.setCreateOrgId(ContextUtil.getCurrentGroupId());
				sysCategoryManager.create(sysCategory);
				return new CommonResult<String>(true, "添加系统分类组值表成功");
			}else{
				sysCategory.setGroupKey(groupKey);
				sysCategoryManager.update(sysCategory);
				return new CommonResult<String>(true, "更新系统分类组值表成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,"操作失败");
		}
	}
	
	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除标识", httpMethod = "DELETE", notes = "分类标识")
	public CommonResult<String> remove(@ApiParam(name="ids",value="多个用“,”号分隔")@RequestParam String ids) throws Exception{
			String[] aryIds = StringUtil.getStringAryByStr(ids);
			sysCategoryManager.removeByIds(aryIds);
			return new CommonResult<String>(true,"删除成功");
	}
}
