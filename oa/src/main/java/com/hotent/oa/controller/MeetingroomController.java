package com.hotent.oa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hotent.oa.persistence.manager.MeetingroomManager;
import com.hotent.oa.model.Meetingroom;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;

/**
 * 
 * <pre> 
 * 描述：会议室 控制器类
 * 构建组：x7
 * 作者:David
 * 邮箱:376514860@qq.com
 * 日期:2023-12-24 01:13:25
 * 版权：wijo
 * </pre>
 */
@RestController
@RequestMapping(value="/oa/meetingroom/v1")
@Api(tags="meetingroomController")
public class MeetingroomController extends BaseController<MeetingroomManager,Meetingroom>{
	@Resource
	MeetingroomManager meetingroomManager;
	
	/**
	 * 会议室列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/listJson")
	@ApiOperation(value="会议室数据列表", httpMethod = "POST", notes = "获取会议室列表")
	public PageList<Meetingroom> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return meetingroomManager.query(queryFilter);
	}
	
	/**
	 * 会议室明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/getJson")
	@ApiOperation(value="会议室数据详情",httpMethod = "GET",notes = "会议室数据详情")
	public Meetingroom get(@ApiParam(name="id",value="业务对象主键", required = true)@RequestParam String id) throws Exception{
		return meetingroomManager.get(id);
	}
	
    /**
	 * 新增会议室
	 * @param meetingroom
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新会议室数据", httpMethod = "POST", notes = "新增,更新会议室数据")
	public CommonResult<String> save(@ApiParam(name="meetingroom",value="会议室业务对象", required = true)@RequestBody Meetingroom meetingroom) throws Exception{
		String msg = "添加会议室成功";
		if(StringUtil.isEmpty(meetingroom.getId())){
			meetingroomManager.create(meetingroom);
		}else{
			meetingroomManager.update(meetingroom);
			 msg = "更新会议室成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 批量删除会议室记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove")
	@ApiOperation(value = "批量删除会议室记录", httpMethod = "DELETE", notes = "批量删除会议室记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		meetingroomManager.removeByIds(ids);
		return new CommonResult<String>(true, "批量删除成功");
	}
}
