package com.hotent.runtime.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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
import com.hotent.base.util.AuthenticationUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.persistence.manager.TaskFollowManager;
import com.hotent.bpm.persistence.model.TaskFollow;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.service.IUserService;

/**
 * 
 * <pre> 
 * 描述：任务跟踪表 控制器类
 * 构建组：x7
 * 作者:maoww
 * 邮箱:maoww@jee-soft.cn
 * 日期:2018-11-13 19:04:41
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/runtime/taskFollow/v1")
@Api(tags="跟踪任务")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class TaskFollowController extends BaseController<TaskFollowManager,TaskFollow>{
	@Resource
	TaskFollowManager taskFollowManager;
	@Resource
	IUserService ius;
	
	/**
	 * 任务跟踪表列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value="任务跟踪表数据列表", httpMethod = "POST", notes = "获取任务跟踪表列表")
	public PageList<TaskFollow> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return taskFollowManager.query(queryFilter);
	}
	
	/**
	 * 任务跟踪表明细页面
	 * @param ID
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@RequestMapping(value = "get", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value="任务跟踪表数据详情",httpMethod = "GET",notes = "任务跟踪表数据详情")
	public TaskFollow get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return taskFollowManager.get(id);
	}
	
    /**
	 * 新增任务跟踪表
	 * @param taskFollow
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "新增,更新任务跟踪表数据", httpMethod = "POST", notes = "新增,更新任务跟踪表数据")
	public CommonResult<String> save(@ApiParam(name="taskFollow",value="任务跟踪表业务对象", required = true)@RequestBody TaskFollow taskFollow) throws Exception{
		String msg = "";
		QueryFilter queryFilter = QueryFilter.<TaskFollow>build();
		queryFilter.addFilter("PRO_INST_", taskFollow.getProInst(), QueryOP.EQUAL);
		queryFilter.addFilter("CREATOR_ID_", AuthenticationUtil.getCurrentUserId(), QueryOP.EQUAL);
		PageList<TaskFollow> list = taskFollowManager.query(queryFilter);
		TaskFollow oldFollow = null;
		if(list.getRows().size()>0){
			oldFollow = list.getRows().get(0);
		}
		if(oldFollow == null){
			msg = "添加跟踪任务成功";
			taskFollow.setCreatorId(AuthenticationUtil.getCurrentUserId());
			taskFollow.setId(UniqueIdUtil.getSuid());
			taskFollowManager.create(taskFollow);
		}else{
			oldFollow.setTaskId(taskFollow.getTaskId());
			taskFollowManager.update(oldFollow);
			msg = "更新任务跟踪成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除任务跟踪表记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "remove", method = RequestMethod.DELETE, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "删除任务跟踪表记录", httpMethod = "DELETE", notes = "删除任务跟踪表记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@RequestParam String id) throws Exception{
		taskFollowManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除任务跟踪表记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "removes", method = RequestMethod.DELETE, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "批量删除任务跟踪表记录", httpMethod = "DELETE", notes = "批量删除任务跟踪表记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		taskFollowManager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	/**
	 * 根据流程实例id获取已跟踪的节点
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@RequestMapping(value = "getFollowedNode", method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "根据流程实例id获取已跟踪的节点", httpMethod = "GET", notes = "根据流程实例id获取已跟踪的节点")
	public CommonResult<String> getFollowedNode(@ApiParam(name="instId",value="流程实例id", required = true)@RequestParam String instId) throws Exception{
		QueryFilter queryFilter = QueryFilter.<TaskFollow>build();
		queryFilter.addFilter("PRO_INST_", instId, QueryOP.EQUAL);
		queryFilter.addFilter("CREATOR_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
		PageList<TaskFollow> list = taskFollowManager.query(queryFilter);
		List<String> ids =  new ArrayList<String>();
		for (TaskFollow follow : list.getRows()) {
			ids.add(follow.getTaskId());
		}
		return new CommonResult<String>(true, "获取成功成功",StringUtil.join(ids, ","));
	}
}
