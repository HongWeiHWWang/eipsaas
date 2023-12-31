package com.hotent.portal.controller;


import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.hotent.base.util.EncryptUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.sys.persistence.manager.SysPropertiesManager;
import com.hotent.sys.persistence.model.SysProperties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 
 * <pre> 
 * 描述：系统属性 控制器类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-07-2 09:29:59
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping("/sys/sysProperties/v1")
@Api(tags="系统属性")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysPropertiesController extends BaseController<SysPropertiesManager, SysProperties> {
	@Resource
	SysPropertiesManager sysPropertiesManager;
	

	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "系统属性列表", httpMethod = "POST", notes = "系统属性列表")
	public PageList<SysProperties> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysProperties> queryFilter) throws Exception {
		return sysPropertiesManager.query(queryFilter);
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "系统属性明细页面", httpMethod = "GET", notes = "系统属性明细页面")
	public @ResponseBody SysProperties getJson(@ApiParam(name="id", value="主键", required = true)@RequestParam String id) throws Exception {
		SysProperties sysProperties =new SysProperties();
		List<String> groups= sysPropertiesManager.getGroups();
		if(StringUtil.isEmpty(id)){
			sysProperties.setCategorys(groups);
			return sysProperties;
		}
		sysProperties=sysPropertiesManager.get(id);
		if (BeanUtils.isEmpty(sysProperties)) {
			sysProperties =sysPropertiesManager.getByAliasFromDb(id);
		}
		if (BeanUtils.isNotEmpty(sysProperties)) {
			String val=sysProperties.getRealVal();
			sysProperties.setValue(val);
			sysProperties.setCategorys(groups);
		}
		return sysProperties;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存系统属性信息", httpMethod = "POST", notes = "保存系统属性信息")
	@CacheEvict(value = "syspropertys", key = "#sysProperties.alias")
	public CommonResult<String> save(@ApiParam(name="sysProperties", value="系统属性", required = true)@RequestBody @Valid SysProperties sysProperties) throws Exception {
		String resultMsg=null;
		String id=sysProperties.getId();
		//判断是否需要进行加密
		sysProperties.setValByEncrypt();
		if(StringUtil.isEmpty(id)){
			sysProperties.setId(UniqueIdUtil.getSuid());
			sysPropertiesManager.create(sysProperties);
			resultMsg="添加系统属性成功";
		}else{
			sysPropertiesManager.update(sysProperties);
			resultMsg="更新系统属性成功";
		}
		return new CommonResult<String>(true, resultMsg);
	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除系统属性记录", httpMethod = "DELETE", notes = "批量删除系统属性记录1,2,3")
	@CacheEvict(value = "syspropertys", allEntries = true)
	public CommonResult<String> remove(@ApiParam(name="id", value="主键", required = true)@RequestParam String ids) throws Exception {
		String[] aryIds=StringUtil.getStringAryByStr(ids);
		sysPropertiesManager.removeByIds(aryIds);
		return new CommonResult<String>(true, "删除系统属性成功");
	}
	
	@RequestMapping(value="getByAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取系统属性参数值。", httpMethod = "GET", notes = "获取系统属性参数值")
	public String getByAlias(@ApiParam(name="alias", value="属性别名", required = true)@RequestParam String alias, 
			@ApiParam(name="defaultValue", value="默认值")@RequestParam  Optional<String> defaultValue) throws Exception {
		String value = sysPropertiesManager.getByAlias(alias,defaultValue.orElse(""));
		return value;
	}

    @RequestMapping(value="getDecryptByAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
    @ApiOperation(value = "获取系统属性解密后参数值。", httpMethod = "GET", notes = "获取系统属性解密后参数值")
    public SysProperties getDecryptByAlias(@ApiParam(name="alias", value="属性别名", required = true)@RequestParam String alias) throws Exception {
        SysProperties sysProperties = sysPropertiesManager.getByAliasFromDb(alias);
        if (BeanUtils.isNotEmpty(sysProperties)){
            sysProperties.setValue(EncryptUtil.decrypt(sysProperties.getValue()));
            return sysProperties;
        }

        return null;
    }
}
