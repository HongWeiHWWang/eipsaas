package com.hotent.runtime.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.runtime.manager.BpmTransRecordManager;
import com.hotent.runtime.model.BpmTransRecord;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre> 
 * 描述：移交记录 控制器类
 * 构建组：x7
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2019-02-18 09:46
 * </pre>
 */
@RestController
@RequestMapping(value="/runtime/bpmTransRecord/v1")
@Api(tags="移交记录")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmTransRecordController extends BaseController<BpmTransRecordManager,BpmTransRecord> {
	@Resource
	BpmTransRecordManager bpmTransRecordManager;
	@Resource
    BpmProcessInstanceManager bpmProcessInstanceManager;
	/**
	 * 移交记录列表(分页条件查询)数据
	 * @param queryFilter
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="移交记录数据列表", httpMethod = "POST", notes = "获取移交记录列表")
	public PageList<BpmTransRecord> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return bpmTransRecordManager.query(queryFilter);
	}
	
	/**
	 * 移交记录明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="移交记录数据详情",httpMethod = "GET",notes = "移交记录数据详情")
	public BpmTransRecord get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return bpmTransRecordManager.get(id);
	}
	
    /**
	 * 新增移交记录
	 * @param bpmTransRecord
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新移交记录数据", httpMethod = "POST", notes = "新增,更新移交记录数据")
	public CommonResult<String> save(@ApiParam(name="bpmTransRecord",value="移交记录业务对象", required = true)@RequestBody BpmTransRecord bpmTransRecord) throws Exception{
		String msg = "添加移交记录成功";
		if(StringUtil.isEmpty(bpmTransRecord.getTransfer())){
			return new CommonResult<>(false, "移交人不能为空！");
		}
		if(StringUtil.isEmpty(bpmTransRecord.getTransfered())){
			return new CommonResult<>(false, "被移交人不能为空！");
		}
		if(bpmTransRecord.getTransfered().equals(bpmTransRecord.getTransfer())){
			return new CommonResult<>(false, "移交人和被移交人不能为同一人！");
		}
		if(StringUtil.isEmpty(bpmTransRecord.getId())){
			bpmTransRecordManager.create(bpmTransRecord);
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除移交记录记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除移交记录记录", httpMethod = "DELETE", notes = "删除移交记录记录")
	public CommonResult<String> remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		bpmTransRecordManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除移交记录记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除移交记录记录", httpMethod = "DELETE", notes = "批量删除移交记录记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		bpmTransRecordManager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	/**
	 * 新增移交记录
	 * @param bpmTransRecord
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="turnOver")
	@ApiOperation(value = "前台批量移交流程", httpMethod = "POST", notes = "前台批量移交流程")
	public CommonResult<String> turnOver(@ApiParam(name="bpmTransRecord",value="移交记录业务对象", required = true)@RequestBody BpmTransRecord bpmTransRecord) throws Exception{
		String msg = "移交流程成功";
		if(StringUtil.isEmpty(bpmTransRecord.getTransfer())){
			IUser user= ContextUtil.getCurrentUser();
			bpmTransRecord.setTransfer(user.getUserId());
			bpmTransRecord.setTransferName(user.getFullname());
		}
		if(StringUtil.isEmpty(bpmTransRecord.getInsts())){
			return new CommonResult<>(false, "移交流程不能为空！");
		}
		if(StringUtil.isEmpty(bpmTransRecord.getTransfered())){
			return new CommonResult<>(false, "被移交人不能为空！");
		}
		if(bpmTransRecord.getTransfered().equals(bpmTransRecord.getTransfer())){
			return new CommonResult<>(false, "当前登陆用户和被移交人不能为同一人！");
		}
		return bpmTransRecordManager.turnOver(bpmTransRecord);
	}
	
	/**
	 * 移交流程列表
	 * @param tranId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="tran/tranResult",method=RequestMethod.POST, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "获取移交流程列表（带分页信息，DefaultBpmProcessInstance对象）", httpMethod = "POST", notes = "获取移交流程列表（带分页信息，DefaultBpmProcessInstance对象）")
	public PageList<DefaultBpmProcessInstance> tranResult(@ApiParam(name="queryFilter",value="通用查询对象")
	 @RequestBody QueryFilter queryFilter, @ApiParam(name="tranId",value="移交记录id", required = true) @RequestParam String tranId) throws Exception{
		BpmTransRecord record = bpmTransRecordManager.get(tranId);
		String recordId = record.getProcinstIds();
		if(StringUtil.isEmpty(recordId))return null;
		queryFilter.addFilter("id", recordId.substring(0,recordId.length()-1), QueryOP.IN);
		PageList<DefaultBpmProcessInstance> list=bpmProcessInstanceManager.query(queryFilter);
		return list;
	}
}
