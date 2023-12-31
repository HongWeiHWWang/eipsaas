package com.hotent.portal.controller;


import javax.annotation.Resource;

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
import com.hotent.i18n.persistence.manager.I18nMessageErrorManager;
import com.hotent.i18n.persistence.model.I18nMessageError;
import com.hotent.i18n.util.I18nUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 *
 * <pre>
 * 描述：国际化资源异常日志 控制器类
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@RestController
@RequestMapping("/i18n/custom/i18nMessageError/v1/")
@Api(tags="国际化资源异常日志")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class I18nMessageErrorController extends BaseController<I18nMessageErrorManager, I18nMessageError>{
	@Resource
	I18nMessageErrorManager i18nMessageErrorManager;

	@RequestMapping(value="listJson", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "国际化资源异常日志列表(分页条件查询)数据", httpMethod = "POST", notes = "国际化资源异常日志列表(分页条件查询)数据")
	public PageList<I18nMessageError> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<I18nMessageError> queryFilter) throws Exception {
		return i18nMessageErrorManager.query(queryFilter);
	}

	@RequestMapping(value="getJson",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "国际化资源异常日志明细页面", httpMethod = "GET", notes = "国际化资源异常日志明细页面")
	public Object getJson(@ApiParam(name="id",value="error定义Id", required = true) @RequestParam String id) throws Exception{
		if(StringUtil.isEmpty(id)){
			return new I18nMessageError();
		}
		I18nMessageError i18nMessageError=i18nMessageErrorManager.get(id);
		return i18nMessageError;
	}

	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存国际化资源异常日志信息", httpMethod = "POST", notes = "保存国际化资源异常日志信息")
	public Object save(@ApiParam(name="i18nMessageError",value="error的实体定义", required = true) @RequestBody I18nMessageError i18nMessageError) throws Exception{
		String resultMsg=null;
		String id=i18nMessageError.getId();
		try {
			if(StringUtil.isEmpty(id)){
				i18nMessageError.setId(UniqueIdUtil.getSuid());
				i18nMessageErrorManager.create(i18nMessageError);
				resultMsg=I18nUtil.getMessage("i18nMessageError.addSuccess",LocaleContextHolder.getLocale());
			}else{
				i18nMessageErrorManager.update(i18nMessageError);
				resultMsg= I18nUtil.getMessage("i18nMessageError.updateSuccess",LocaleContextHolder.getLocale());
			}
			return new CommonResult<String>(true,resultMsg,null);
		} catch (Exception e) {
			resultMsg=I18nUtil.getMessage("i18nMessageError.operationFail",LocaleContextHolder.getLocale());
			return new CommonResult<String>(false,resultMsg,null);
		}
	}

	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除国际化资源异常日志记录", httpMethod = "DELETE", notes = "批量删除国际化资源异常日志记录")
	public Object remove(@ApiParam(name="ids",value="error定义Ids", required = true) @RequestParam String ids) throws Exception{
		try {
			String[] aryIds=StringUtil.getStringAryByStr(ids);
			i18nMessageErrorManager.removeByIds(aryIds);
			return new CommonResult<String>(true,I18nUtil.getMessage("i18nMessageError.deleteSuccess",LocaleContextHolder.getLocale()),null);
		} catch (Exception e) {
			return new CommonResult<String>(false,I18nUtil.getMessage("i18nMessageError.deleteFail",LocaleContextHolder.getLocale()),null);
		}
	}
}
