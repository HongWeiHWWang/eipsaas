package com.hotent.uc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.context.BaseContext;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.uc.manager.TenantMailServerManager;
import com.hotent.uc.manager.TenantManageManager;
import com.hotent.uc.model.TenantMailServer;
import com.hotent.uc.model.TenantManage;

/**
 * 
 * <pre> 
 * 描述：租户邮件服务器信息 控制器类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 11:01:30
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/uc/tenantMailServer/v1")
@Api(tags="租户邮件服务器信息")
@ApiGroup(group= {ApiGroupConsts.GROUP_UC})
public class TenantMailServerController extends BaseController<TenantMailServerManager,TenantMailServer>{
	@Resource
	TenantMailServerManager tenantMailServerManager;
	@Resource
	BaseContext baseContext;
	@Resource
	TenantManageManager tenantManageManager;
	
	/**
	 * 租户邮件服务器信息列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="租户邮件服务器信息数据列表", httpMethod = "POST", notes = "获取租户邮件服务器信息列表")
	public PageList<TenantMailServer> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return tenantMailServerManager.query(queryFilter);
	}
	
	/**
	 * 租户邮件服务器信息明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="租户邮件服务器信息数据详情",httpMethod = "GET",notes = "租户邮件服务器信息数据详情")
	public TenantMailServer get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return tenantMailServerManager.get(id);
	}
	
	@GetMapping(value="/getByCurrent")
	@ApiOperation(value="租户邮件服务器信息数据详情",httpMethod = "GET",notes = "租户邮件服务器信息数据详情")
	public TenantMailServer getByCurrent(@ApiParam(name="tenantId",value="租户id", required = true)@RequestParam Optional<String> tenantId) throws Exception{
		String currentTenantId = StringUtil.isEmpty(tenantId.orElse(null))?baseContext.getCurrentTenantId():tenantId.get();
		TenantMailServer mailServer = tenantMailServerManager.getByTenantId(currentTenantId);
		if(BeanUtils.isEmpty(mailServer)){
			mailServer = new TenantMailServer();
			mailServer.setTenantId(currentTenantId);
		}
		return mailServer;
	}
	
    /**
	 * 新增租户邮件服务器信息
	 * @param tenantMailServer
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新租户邮件服务器信息数据", httpMethod = "POST", notes = "新增,更新租户邮件服务器信息数据")
	public CommonResult<String> save(@ApiParam(name="tenantMailServer",value="租户邮件服务器信息业务对象", required = true)@RequestBody TenantMailServer tenantMailServer) throws Exception{
		String msg = "添加租户邮件服务器信息成功";
		if(StringUtil.isEmpty(tenantMailServer.getTenantId())){
			return new CommonResult<String>(false,"租户id【tenantId】必填！");
		}
		TenantMailServer mailServer = tenantMailServerManager.getByTenantId(tenantMailServer.getTenantId());
		if(StringUtil.isEmpty(tenantMailServer.getId()) && BeanUtils.isEmpty(mailServer)){
			tenantMailServerManager.create(tenantMailServer);
		}else if(StringUtil.isEmpty(tenantMailServer.getId()) && BeanUtils.isNotEmpty(mailServer)){
			TenantManage tenant = tenantManageManager.get(mailServer.getTenantId());
			return new CommonResult<String>(false,"租户【"+tenant.getName()+"】已存在邮件服务器信息，不需要多次添加！");
		}else{
			tenantMailServerManager.update(tenantMailServer);
			 msg = "更新租户邮件服务器信息成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 批量删除租户邮件服务器信息记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除租户邮件服务器信息记录", httpMethod = "DELETE", notes = "批量删除租户邮件服务器信息记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		tenantMailServerManager.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}
}
