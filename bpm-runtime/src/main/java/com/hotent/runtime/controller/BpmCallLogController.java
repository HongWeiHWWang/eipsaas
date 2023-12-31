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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.manager.BpmCallLogManager;
import com.hotent.bpm.persistence.model.BpmCallLog;

/**
 * 
 * <pre> 
 * 描述：restful接口事件日志 控制器类
 * 构建组：x7
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2018-11-22 19:16:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/runtime/bpmCallLog/v1")
@Api(tags="流程调用Restful配置")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class BpmCallLogController extends BaseController<BpmCallLogManager,BpmCallLog>{
	@Resource
	BpmCallLogManager bpmCallLogManager;
	
	/**
	 * restful接口事件日志列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="restful接口事件日志数据列表", httpMethod = "POST", notes = "获取restful接口事件日志列表")
	public PageList<BpmCallLog> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return bpmCallLogManager.query(queryFilter);
	}
	
	/**
	 * restful接口事件日志明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="restful接口事件日志数据详情",httpMethod = "GET",notes = "restful接口事件日志数据详情")
	public BpmCallLog get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return bpmCallLogManager.get(id);
	}
	
    /**
	 * 新增restful接口事件日志
	 * @param bpmCallLog
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新restful接口事件日志数据", httpMethod = "POST", notes = "新增,更新restful接口事件日志数据")
	public CommonResult<String> save(@ApiParam(name="bpmCallLog",value="restful接口事件日志业务对象", required = true)@RequestBody BpmCallLog bpmCallLog) throws Exception{
		String msg = "添加restful接口事件日志成功";
		if(StringUtil.isEmpty(bpmCallLog.getId())){
			bpmCallLogManager.create(bpmCallLog);
		}else{
			bpmCallLogManager.update(bpmCallLog);
			 msg = "更新restful接口事件日志成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除restful接口事件日志记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove/{id}")
	@ApiOperation(value = "删除restful接口事件日志记录", httpMethod = "DELETE", notes = "删除restful接口事件日志记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		bpmCallLogManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除restful接口事件日志记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除restful接口事件日志记录", httpMethod = "DELETE", notes = "批量删除restful接口事件日志记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		bpmCallLogManager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	/**
	 * 重新调用接口
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/reinvoke/{id}")
	@ApiOperation(value = "接口重调", httpMethod = "POST", notes = "接口重调")
	public CommonResult<String> reinvoke(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		bpmCallLogManager.reinvoke(id);
		return new CommonResult<String>("调用成功");
	}
	
	/**
	 * 设置为调用成功
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/signSuccess/{id}")
	@ApiOperation(value = "标记为成功", httpMethod = "POST", notes = "标记为成功")
	public CommonResult<String> signSuccess(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		bpmCallLogManager.signSuccess(id);
		return new CommonResult<String>("标记成功");
	}
}
