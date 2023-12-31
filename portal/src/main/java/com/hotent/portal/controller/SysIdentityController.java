package com.hotent.portal.controller;


import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.id.IdGenerator;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.sys.persistence.manager.SysIdentityManager;
import com.hotent.sys.persistence.model.SysIdentity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** <pre>
* 描述：流水号 控制器类
* @author dengyg
* @company 广州宏天软件股份有限公司
* @email dengyg@jee-soft.cn
* @date 2018-06-06 14:20
* </pre>
*/
@RestController
@RequestMapping("/sys/identity/v1")
@Api(tags="流水号")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class SysIdentityController extends BaseController<SysIdentityManager, SysIdentity> {
	@Resource
	SysIdentityManager identityManager;
	@Resource
	protected IdGenerator idGenerator;
	
	@RequestMapping(value="listJson", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "流水号生成列表(分页条件查询)数据", httpMethod = "POST", notes = "流水号生成列表(分页条件查询)数据")
	public PageList<SysIdentity> listJson(HttpServletRequest request,HttpServletResponse reponse,@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<SysIdentity> queryFilter) throws Exception{
		return identityManager.query(queryFilter);
	}
	
	@RequestMapping(value="getAll", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取所有流水号", httpMethod = "POST", notes = "流水号生成列表(分页条件查询)数据")
	public List<SysIdentity> getAll() throws Exception{
		return identityManager.list();
	}

	@RequestMapping(value="getById",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据id获取流水号", httpMethod = "GET", notes = "根据id获取流水号")
	public Object getById(HttpServletRequest request,HttpServletResponse response,@ApiParam(name="id",value="流水号定义Id", required = true) @RequestParam String id) throws Exception{
		SysIdentity identity = identityManager.get(id);
		return identity;
	}
	
	@RequestMapping(value="getJson",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流水号明细页面", httpMethod = "GET", notes = "流水号明细页面")
	public Object get(HttpServletRequest request,HttpServletResponse response,@ApiParam(name="id",value="流水号定义Id", required = true) @RequestParam String id) throws Exception{
		SysIdentity identity=null;
		if(StringUtil.isNotEmpty(id)){
			identity=identityManager.get(id);
		}
		return identity;
	}
	
	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存流水号", httpMethod = "POST", notes = "保存流水号")
	public Object save(@ApiParam(name="identity",value="流水号定义", required = true) @RequestBody SysIdentity identity) throws  Exception{
		String resultMsg=null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", identity.getId());
		params.put("alias", identity.getAlias());
		boolean rtn=identityManager.isAliasExisted(params);
		if (rtn) {
			return new CommonResult<String>(false,"流水号别名已经存在",null);
		}
		try {
			if(StringUtil.isEmpty(identity.getId())){
				identity.setId(idGenerator.getSuid());
				identityManager.create(identity);
				resultMsg="添加流水号生成成功";
			}else{
				identityManager.update(identity);
				resultMsg="更新流水号生成成功";
			}
			return new CommonResult<String>(true,resultMsg,null);
		} catch (Exception e) { 
			return new CommonResult<String>(false,resultMsg,null);
		}
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除流水号", httpMethod = "DELETE", notes = "批量删除流水号")
	public Object remove(@ApiParam(name="ids",value="流水号定义Ids", required = true) @RequestParam String ids) throws Exception{
		try {
			String[] aryIds=StringUtil.getStringAryByStr(ids);
			identityManager.removeByIds(Arrays.asList(aryIds));
			return new CommonResult<String>(true,"删除成功",null);
		} catch (Exception e) {
			return new CommonResult<String>(false,"删除失败",null);
		}
	}
	
	@RequestMapping(value="preview",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流水号预览", httpMethod = "GET", notes = "流水号预览")
	public PageList<SysIdentity> preview(@ApiParam(name="alias",value="流水号别名", required = true) @RequestParam String alias) throws Exception{
		List<SysIdentity> identities = identityManager.getPreviewIden(alias);
		return new PageList<SysIdentity>(identities);
	}
	
	@RequestMapping(value="getNextIdByAlias",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取下一个流水号", httpMethod = "GET", notes = "获取下一个流水号")
	public  CommonResult<String> getNextIdByAlias(@ApiParam(name="alias",value="流水号别名", required = true) @RequestParam String alias) throws Exception{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", null);
		params.put("alias", alias);
		if(identityManager.isAliasExisted(params)){
			String nextId = identityManager.nextId(alias);
			return new CommonResult<String>(true, "获取流水号成功！", nextId);
		}
		return new CommonResult<String>(false, "获取流水号失败！");
	}
}
