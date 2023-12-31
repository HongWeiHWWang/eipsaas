package com.hotent.portal.controller;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.model.CommonResult;
import com.hotent.portal.service.TenantService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：系统操作日志 控制器类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-08-31 10:59:25
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/portal/tenantInitData/v1",produces=MediaType.APPLICATION_JSON_VALUE)
@Api(tags="租户数据初始化")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class TenantController{
	
	@Resource
	TenantService tenantService;
	
	/**
	 * 给租户初始化一些数据
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/initData")
	public CommonResult<String> initData(@ApiParam(name="tenantId",value="租户id", required = true)@RequestParam String tenantId) throws Exception{
		tenantService.initData(tenantId);
		return new CommonResult<String>("初始化数据成功");
	}
}
