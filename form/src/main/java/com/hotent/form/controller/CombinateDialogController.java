package com.hotent.form.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.CombinateDialog;
import com.hotent.form.persistence.manager.CombinateDialogManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 组合对话框 控制器类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月14日
 */
@RestController
@RequestMapping("/form/combinateDialog/v1")
@Api(tags="组合对话框")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class CombinateDialogController extends BaseController<CombinateDialogManager, CombinateDialog> {
	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "组合对话框列表(分页条件查询)数据", httpMethod = "POST", notes = "组合对话框列表(分页条件查询)数据")
	public PageList<CombinateDialog> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<CombinateDialog> queryFilter) throws Exception {
		return super.query(queryFilter);
	}

	
	@RequestMapping(value="combinateDialogEdit", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "编辑组合对话框信息页面", httpMethod = "POST", notes = "编辑组合对话框信息页面")
	public CombinateDialog edit(@ApiParam(name="id",value="主键")@RequestBody  String id) throws Exception {
		return super.getById(id);
	}
	
	@RequestMapping(value="getObject", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据主键或别名获取一个组合对象", httpMethod = "POST", notes = "获取一个组合对象")
	public CombinateDialog getObject(@ApiParam(name="id",value="主键")@RequestBody String id, @ApiParam(name="alias",value="别名")@RequestBody String alias) throws Exception {
		if (StringUtil.isNotEmpty(id)) {
			return baseService.get(id);
		}
		if (StringUtil.isNotEmpty(alias)) {
			return baseService.getByAlias(alias);
		}
		return null;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存组合对话框信息", httpMethod = "POST", notes = "保存组合对话框信息")
	public CommonResult<String> save(@ApiParam(name="combinateDialog",value="组合对话框JSON对象")@RequestBody String json) throws Exception {
		String resultMsg = null;
		CombinateDialog combinateDialog=null;
		if(StringUtil.isEmpty(json)){
			combinateDialog=JsonUtil.toBean(json, CombinateDialog.class);
		}
		String id = combinateDialog.getId();
		if (StringUtil.isEmpty(id)) {
			combinateDialog.setId(UniqueIdUtil.getSuid());
			baseService.create(combinateDialog);
			resultMsg = "添加组合对话框成功";
		} else {
			baseService.update(combinateDialog);
			resultMsg = "更新组合对话框成功";
		}
		return new CommonResult<>(true, resultMsg, null);
	}

	
	@RequestMapping(value="remove", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除组合对话框记录", httpMethod = "POST", notes = "批量删除组合对话框记录")
	public CommonResult<String> remove(@ApiParam(name="id",value="主键,多个用,分割")@RequestBody String id) throws Exception {
		String[] aryIds = null;
		if(!StringUtil.isEmpty(id)){
			aryIds=id.split(",");
		}
		baseService.removeByIds(aryIds);
		return new CommonResult<>(true, "删除组合对话框成功", null);
	}
}
