<#import "function.ftl" as func>
<#assign package=model.variables.package>
<#assign class=model.variables.class>
<#assign system=vars.system>
<#assign companyEn=vars.companyEn>
<#assign comment=model.tabComment>
<#assign subtables=model.subTableList>
<#assign classVar=model.variables.classVar>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign pkType=func.getPkType(model)>
package com.${companyEn}.${system}.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.${companyEn}.${system}.persistence.manager.${class}Manager;
import com.${companyEn}.${system}.persistence.model.${class};
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;

/**
 * 
 * <pre> 
 * 描述：${comment} 控制器类
 * 构建组：x7
 <#if vars.developer?exists>
 * 作者:${vars.developer}
 * 邮箱:${vars.email}
 </#if>
 * 日期:${date?string("yyyy-MM-dd HH:mm:ss")}
 * 版权：${vars.company}
 * </pre>
 */
@RestController
@RequestMapping(value="/${system}/${classVar}/v1",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags="${classVar}Controller")
public class ${class}Controller extends BaseController{
	@Resource
	${class}Manager ${classVar}Manager;
	
	/**
	 * ${comment}列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="${comment}数据列表", httpMethod = "POST", notes = "获取${comment}列表")
	public PageList<${class}> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return ${classVar}Manager.query(queryFilter);
	}
	
	/**
	 * ${comment}明细页面
	 * @param ${pkVar}
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{${pkVar}}")
	@ApiOperation(value="${comment}数据详情",httpMethod = "GET",notes = "${comment}数据详情")
	public ${class} get(@ApiParam(name="${pkVar}",value="业务对象主键", required = true)@PathVariable ${pkType} ${pkVar}) throws Exception{
		return ${classVar}Manager.get(${pkVar});
	}
	
    /**
	 * 新增${comment}
	 * @param ${classVar}
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新${comment}数据", httpMethod = "POST", notes = "新增,更新${comment}数据")
	public CommonResult<String> save(@ApiParam(name="${classVar}",value="${comment}业务对象", required = true)@RequestBody ${class} ${classVar}) throws Exception{
		String msg = "添加${comment}成功";
		if(StringUtil.isEmpty(${classVar}.getId())){
			${classVar}Manager.create(${classVar});
		}else{
			${classVar}Manager.update(${classVar});
			 msg = "更新${comment}成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除${comment}记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{${pkVar}}")
	@ApiOperation(value = "删除${comment}记录", httpMethod = "DELETE", notes = "删除${comment}记录")
	public  CommonResult<String>  remove(@ApiParam(name="${pkVar}",value="业务主键", required = true)@PathVariable ${pkType} ${pkVar}) throws Exception{
		${classVar}Manager.remove(${pkVar});
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除${comment}记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除${comment}记录", httpMethod = "DELETE", notes = "批量删除${comment}记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam ${pkType} ids) throws Exception{
		${classVar}Manager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
}
