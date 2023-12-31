package com.hotent.form.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.form.model.FieldAuth;
import com.hotent.form.persistence.manager.FieldAuthManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre> 
 * 描述：字段权限设置 控制器类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-10-27 14:37:11
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping("/form/fieldAuth/v1")
@Api(tags="字段权限设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class FieldAuthController extends BaseController<FieldAuthManager, FieldAuth>{
	/**
	 * 字段权限设置列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="字段权限设置数据列表", httpMethod = "POST", notes = "获取字段权限设置列表")
	public PageList<FieldAuth> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<FieldAuth> queryFilter) throws Exception{
		return baseService.query(queryFilter);
	}
	
	/**
	 * 根据类名获取字段权限设置
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getByClassName")
	@ApiOperation(value="根据类名获取字段权限设置",httpMethod = "GET",notes = "根据类名获取字段权限设置")
	public FieldAuth getByClassName(@ApiParam(name="className",value="类名", required = true)@RequestParam String className) throws Exception{
		return baseService.getByClassName(className);
	}
	
	/**
	 * 根据表名获取字段权限设置
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getByTableName")
	@ApiOperation(value="根据表名获取字段权限设置",httpMethod = "GET",notes = "根据表名获取字段权限设置")
	public FieldAuth getByTableName(@ApiParam(name="tableName",value="表名", required = true)@RequestParam String tableName) throws Exception{
		return baseService.getByTableName(tableName);
	}
	
	/**
	 * 根据实体别名获取字段权限设置
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getByEntName")
	@ApiOperation(value="根据实体别名获取字段权限设置",httpMethod = "GET",notes = "根据实体别名获取字段权限设置")
	public FieldAuth getByEntName(@ApiParam(name="entName",value="表名", required = true)@RequestParam String entName) throws Exception{
		return baseService.getByEntName(entName);
	}
	
	/**
	 * 字段权限设置明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="字段权限设置数据详情",httpMethod = "GET",notes = "字段权限设置数据详情")
	public FieldAuth get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return baseService.get(id);
	}
	
    /**
	 * 新增字段权限设置
	 * @param fieldAuth
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新字段权限设置数据", httpMethod = "POST", notes = "新增,更新字段权限设置数据")
	public CommonResult<String> save(@ApiParam(name="fieldAuth",value="字段权限设置业务对象", required = true)@RequestBody FieldAuth fieldAuth) throws Exception{
		String msg = "添加字段权限设置成功";
		if(StringUtil.isEmpty(fieldAuth.getId())){
			baseService.create(fieldAuth);
		}else{
			baseService.update(fieldAuth);
			 msg = "更新字段权限设置成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除字段权限设置记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除字段权限设置记录", httpMethod = "DELETE", notes = "删除字段权限设置记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		baseService.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除字段权限设置记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除字段权限设置记录", httpMethod = "DELETE", notes = "批量删除字段权限设置记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		baseService.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
}
