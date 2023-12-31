package com.hotent.portal.controller;

import org.springframework.context.i18n.LocaleContextHolder;
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
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.i18n.persistence.manager.I18nMessageTypeManager;
import com.hotent.i18n.persistence.model.I18nMessageType;
import com.hotent.i18n.util.I18nUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <pre>
 * 描述：国际化资源支持的语言类型 控制器类
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@RestController
@RequestMapping("/i18n/custom/i18nMessageType/v1/")
@Api(tags="国际化语种")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class I18nMessageTypeController extends BaseController<I18nMessageTypeManager, I18nMessageType>{
	@RequestMapping(value="list", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "国际化资源支持的语言类型列表(分页条件查询)数据", httpMethod = "POST", notes = "国际化资源支持的语言类型列表(分页条件查询)数据")
	public PageList<I18nMessageType> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<I18nMessageType> queryFilter) throws Exception{
		return baseService.query(queryFilter);
	}

	@RequestMapping(value="all",method= RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "国际化资源支持的所有语言类型", httpMethod = "POST", notes = "国际化资源支持的所有语言类型")
	public Object getAll() throws Exception{
		return baseService.getAllType();
	}

	@RequestMapping(value="getJson",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "国际化资源支持的语言类型明细页面", httpMethod = "GET", notes = "国际化资源支持的语言类型明细页面")
	public Object getJson(@ApiParam(name="id",value="type定义Id", required = true) @RequestParam String id) throws Exception{
		if(StringUtil.isEmpty(id)){
			return new I18nMessageType();
		}
		I18nMessageType i18nMessageType=baseService.get(id);
		return i18nMessageType;
	}

	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存国际化资源支持的语言类型信息", httpMethod = "POST", notes = "保存国际化资源支持的语言类型信息")
	public Object save(@ApiParam(name="i18nMessageType",value="type的实体定义", required = true) @RequestBody I18nMessageType i18nMessageType) throws Exception{
		String resultMsg=null;
		String id=i18nMessageType.getId();
		try {
			if(StringUtil.isEmpty(id)){
				i18nMessageType.setId(UniqueIdUtil.getSuid());
				baseService.create(i18nMessageType);
				resultMsg= I18nUtil.getMessage("i18nMessageType.addSuccess",LocaleContextHolder.getLocale());
			}else{
				baseService.update(i18nMessageType);
				resultMsg=I18nUtil.getMessage("i18nMessageType.updateSuccess",LocaleContextHolder.getLocale());
			}
			baseService.delMessageTypeCache();
			return new CommonResult<String>(true,resultMsg,null);
		} catch (Exception e) {
			resultMsg=I18nUtil.getMessage("i18nMessageType.operationFail",LocaleContextHolder.getLocale());
			return new CommonResult<String>(false,resultMsg,null);
		}
	}


	@RequestMapping(value="getByType",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "判断类型是否存在", httpMethod = "POST", notes = "判断类型是否存在")
	public Object getByType(@ApiParam(name="type",value="语种类型", required = true) @RequestParam String type) throws Exception{
		I18nMessageType i18nMessageType = baseService.getByType(type);
		return i18nMessageType;
	}

	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除国际化资源支持的语言类型记录", httpMethod = "DELETE", notes = "批量删除国际化资源支持的语言类型记录")
	public Object remove(@ApiParam(name="ids",value="type定义ids", required = true) @RequestParam String ids) throws Exception{
		try {
			String[] aryIds=StringUtil.getStringAryByStr(ids);
			baseService.removeByIds(aryIds);
			baseService.delMessageTypeCache();
			return new CommonResult<String>(true,I18nUtil.getMessage("i18nMessageType.deleteSuccess",LocaleContextHolder.getLocale()),null);
		} catch (Exception e) {
			return new CommonResult<String>(false,I18nUtil.getMessage("i18nMessageType.deleteFail",LocaleContextHolder.getLocale()),null);
		}
	}
}
