package com.hotent.runtime.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

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
import com.hotent.runtime.manager.MeetingRoomManager;
import com.hotent.runtime.model.MeetingRoom;

/**
 * 
 * <pre> 
 * 描述：会议室 控制器类
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-10 15:01:37
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/portal/meetingRoom/v1")
@Api(tags="会议室管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class MeetingRoomController extends BaseController<MeetingRoomManager,MeetingRoom>{
	@Resource
	MeetingRoomManager meetingRoomManager;
	
	/**
	 * 会议室列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="会议室数据列表", httpMethod = "POST", notes = "获取会议室列表")
	public PageList<MeetingRoom> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return meetingRoomManager.query(queryFilter);
	}
	
	/**
	 * 会议室明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="会议室数据详情",httpMethod = "GET",notes = "会议室数据详情")
	public MeetingRoom get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return meetingRoomManager.get(id);
	}
	
    /**
	 * 新增会议室
	 * @param meetingRoom
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新会议室数据", httpMethod = "POST", notes = "新增,更新会议室数据")
	public CommonResult<String> save(@ApiParam(name="meetingRoom",value="会议室业务对象", required = true)@RequestBody MeetingRoom meetingRoom) throws Exception{
		String msg = "添加会议室成功";
		if(StringUtil.isEmpty(meetingRoom.getId())){
			meetingRoomManager.create(meetingRoom);
		}else{
			meetingRoomManager.update(meetingRoom);
			 msg = "更新会议室成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除会议室记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除会议室记录", httpMethod = "DELETE", notes = "删除会议室记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		meetingRoomManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除会议室记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除会议室记录", httpMethod = "DELETE", notes = "批量删除会议室记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String ids) throws Exception{
		String[] aryIds=StringUtil.getStringAryByStr(ids);
		meetingRoomManager.removeByIds(aryIds);
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	/**
	 * 获取当前用户有权限的会议室
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@GetMapping("/getHasRightMeetingRoom")
	@ApiOperation(value="获取当前用户有权限的会议室", httpMethod = "GET", notes = "获取当前用户有权限的会议室")
	public List<MeetingRoom> getHasRightMeetingRoom() throws Exception{
		QueryFilter queryFilter=QueryFilter.build();
		return meetingRoomManager.query(queryFilter).getRows();
	}
}
