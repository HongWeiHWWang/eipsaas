package com.hotent.form.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hotent.base.util.time.DateUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.model.CustomDialog;
import com.hotent.form.persistence.manager.CombinateDialogManager;
import com.hotent.form.persistence.manager.CustomDialogManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * <pre>
 * 描述：自定义对话框管理
 * 构建组：x5-bpmx-platform
 * 作者:liyj_aschs
 * 邮箱:liyj_aschs@jee-soft.cn
 * 日期:2014-1-10-下午3:29:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/form/customDialog/v1")
@Api(tags="自定义对话框")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CustomDialogController extends BaseController<CustomDialogManager, CustomDialog> {
	@Resource
	CombinateDialogManager combinateDialogManager;
    @Resource
    DatabaseContext databaseContext;

	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "自定义对话框列表(分页条件查询)数据", httpMethod = "POST", notes = "自定义对话框列表(分页条件查询)数据")
	public PageList listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		return baseService.query(queryFilter);
	}


	@RequestMapping(value="getAll", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "查询所有的自定义对话框", httpMethod = "POST", notes = "查询所有的自定义对话框")
	public List<CustomDialog> getAll() throws Exception {
		List<CustomDialog> customDialogs = baseService.list();
		return customDialogs;
	}


	@RequestMapping(value="getAllDialogs", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "返回组合对话框和自定义对话框", httpMethod = "POST", notes = "返回组合对话框和自定义对话框")
	public List<Object> getAllDialogs() throws Exception {
		List<Object> customDialogs = (List)baseService.list();
		customDialogs.addAll(combinateDialogManager.list());
		return customDialogs;
	}

	@RequestMapping(value="getByAlias", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "根据别名获取自定义对话框", httpMethod = "GET", notes = "根据别名获取自定义对话框")
	public CustomDialog getByAlias(@ApiParam(name="alias",value="别名")@RequestParam  String alias) throws Exception {
		CustomDialog customDialog = null;
		if (StringUtil.isNotEmpty(alias)) {
			customDialog = baseService.getByAlias(alias);
		}
		if(customDialog==null){
			customDialog=new CustomDialog();
		}
		return customDialog;
	}


	@RequestMapping(value="save", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "保存自定义对话框信息", httpMethod = "POST", notes = "保存自定义对话框信息")
	public CommonResult save(@ApiParam(name="json",value="自定义对话框JSON对象")@RequestBody   String json) throws Exception {
		CustomDialog customDialog =null;
		String resultMsg;
		if(!StringUtil.isEmpty(json)){
			ObjectNode node=(ObjectNode) JsonUtil.toJsonNode(json);
			String displayfield=node.get("displayfield")+"";
			String conditionfield=node.get("conditionfield")+"";
			String resultfield=node.get("resultfield")+"";
			String sortfield=node.get("sortfield")+"";
			node.remove("displayfield");
			node.remove("conditionfield");
			node.remove("resultfield");
			node.remove("sortfield");
			customDialog=JsonUtil.toBean(node, CustomDialog.class);
			customDialog.setDisplayfield(displayfield);
			customDialog.setConditionfield(conditionfield);
			customDialog.setResultfield(resultfield);
			customDialog.setSortfield(sortfield);
		}
		if (StringUtil.isEmpty(customDialog.getId())) {
			if (baseService.getByAlias(customDialog.getAlias()) != null) {
				return new CommonResult(false, "别名"+customDialog.getAlias() + "，已存在", null);
			}
			customDialog.setId(UniqueIdUtil.getSuid());
			customDialog.setUpdateTime(DateUtil.getCurrentDate());
			baseService.create(customDialog);
			resultMsg = "添加成功";
		} else {
			baseService.update(customDialog);
			resultMsg = "更新成功";
		}
		return new CommonResult(true, resultMsg, null);
	}


	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "删除自定义对话框记录", httpMethod = "DELETE", notes = "删除自定义对话框记录")
	public CommonResult remove(@ApiParam(name="id",value="主键")@RequestParam  String id) throws Exception {
		String msg = "删除成功";
		boolean index=true;
		CustomDialog customDialog = baseService.get(id);
		if (customDialog.getSystem() == null || customDialog.getSystem() == false) {
			baseService.remove(id);
		} else {
			index=false;
			msg = "id:" + id + "是系统默认不能删除";
		}
		return new CommonResult(index, msg);
	}

	@RequestMapping(value="removes",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除bo定义", httpMethod = "DELETE", notes = "批量删除bo定义")
	public CommonResult<String> batchRemove(@ApiParam(name="ids",value="bo主键集合", required = true) @RequestParam String...ids) throws Exception{
		baseService.removeByIds(ids);
		return new CommonResult<String>(true, "删除成功");
	}

	@RequestMapping(value="getTreeData", method=RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "取得树形数据。", httpMethod = "GET", notes = "取得树形数据。")
	public List getTreeData(@ApiParam(name="alias",value="别名")@RequestParam String alias,
							@ApiParam(name="mapParam",value="动态传入的字段")@RequestParam String mapParam) throws Exception {
		CustomDialog customDialog = null;
		Map<String, Object> param=new HashMap<String, Object>();
		if(StringUtil.isNotEmpty(mapParam)){
			mapParam="{"+mapParam+"}";
			Map map=JsonUtil.toMap(mapParam);
			for (Object key : map.keySet()) {
				param.put(key+"",map.get(key));
			}
		}
		if (StringUtil.isNotEmpty(alias));
			customDialog = baseService.getByAlias(alias);
		if (customDialog == null) {
			return null;
		}
		// 改变当前数据源
		try (DatabaseSwitchResult dResult = databaseContext.setDataSource(customDialog.getDsalias())){
			return baseService.geTreetData(customDialog, param, dResult.getDbType());
		} catch(Exception e) {
			throw new BaseException(e.getMessage());
		}
	}

	@RequestMapping(value="getListData", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "自定义对话框查询", httpMethod = "POST", notes = "自定义对话框查询")
	public Object getListData(@ApiParam(name="alias",value="别名")@RequestParam  String alias,
			@ApiParam(name="filter",value="通用查询对象")@RequestBody  QueryFilter filter,
			@ApiParam(name="mapParam",value="")@RequestParam  String mapParam) throws Exception {
		return baseService.getCustomDialogData(alias, filter, mapParam);
	}




	@RequestMapping(value="mobileCustomDialog", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "手机端自定义对话框", httpMethod = "POST", notes = "手机端自定义对话框")
	public CommonResult mobileDialog(@ApiParam(name="isCombine",value="")@RequestBody  Boolean isCombine,
			@ApiParam(name="alias",value="别名")@RequestBody String alias) throws Exception {
		Map mv = baseService.getMobileCustomDialogData(isCombine, alias);
		return new CommonResult(true, null, mv);
	}

}
