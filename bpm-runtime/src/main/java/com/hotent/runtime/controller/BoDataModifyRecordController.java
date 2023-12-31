package com.hotent.runtime.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.model.BoDataModifyRecord;
import com.hotent.bpm.persistence.manager.BoDataModifyRecordManager;

/**
 * 
 * <pre> 
 * 描述：流程表单数据修改记录 控制器类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-23 11:45:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/bpm/boDataModifyRecord/v1")
@Api(tags="表单数据处理")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BoDataModifyRecordController extends BaseController<BoDataModifyRecordManager,BoDataModifyRecord>{
	@Resource
	BoDataModifyRecordManager boDataModifyRecordManager;
	
	/**
	 * 流程表单数据修改记录列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="流程表单数据修改记录数据列表", httpMethod = "POST", notes = "获取流程表单数据修改记录列表")
	public PageList<BoDataModifyRecord> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return super.query(queryFilter);
	}
	
	/**
	 * 流程表单数据修改记录明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="流程表单数据修改记录数据详情",httpMethod = "GET",notes = "流程表单数据修改记录数据详情")
	public BoDataModifyRecord get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return boDataModifyRecordManager.get(id);
	}
	
    /**
	 * 新增流程表单数据修改记录
	 * @param boDataModifyRecord
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新流程表单数据修改记录数据", httpMethod = "POST", notes = "新增,更新流程表单数据修改记录数据")
	public CommonResult<String> save(@ApiParam(name="boDataModifyRecord",value="流程表单数据修改记录业务对象", required = true)@RequestBody BoDataModifyRecord boDataModifyRecord) throws Exception{
		String msg = "添加流程表单数据修改记录成功";
		if(StringUtil.isEmpty(boDataModifyRecord.getId())){
			this.create(boDataModifyRecord);
		}else{
			boDataModifyRecordManager.update(boDataModifyRecord);
			 msg = "更新流程表单数据修改记录成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 批量删除流程表单数据修改记录记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除流程表单数据修改记录记录", httpMethod = "DELETE", notes = "批量删除流程表单数据修改记录记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		boDataModifyRecordManager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	@PostMapping(value="/handleBoDateModify")
	@ApiOperation(value = "添加修改记录", httpMethod = "POST", notes = "添加修改记录")
	public CommonResult<String> handleBoDateModify(@ApiParam(name="params",value="修改记录数据", required = true)@RequestBody Map<String, Object> params) throws Exception{
		boDataModifyRecordManager.handleBoDateModify(params);
		return new CommonResult<String>(true, "添加修改记录成功");
	}
	
	@GetMapping("/getListByRefId")
	@ApiOperation(value="通过外键查询列表", httpMethod = "POST", notes = "通过外键查询列表")
	public List<BoDataModifyRecord> getListByRefId(@ApiParam(name="refId",value="外键", required = true)@RequestParam String refId){
		return boDataModifyRecordManager.getListByRefId(refId);
	}
}
