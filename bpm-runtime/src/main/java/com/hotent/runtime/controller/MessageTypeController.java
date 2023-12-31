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
import com.hotent.runtime.manager.MessageTypeManager;
import com.hotent.runtime.model.MessageType;

/**
 * 
 * <pre> 
 * 描述：分类管理 控制器类
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-15 18:35:27
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/portal/messageType/v1")
@Api(tags="分类管理")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM})
public class MessageTypeController extends BaseController<MessageTypeManager,MessageType>{
	@Resource
	MessageTypeManager messageTypeManager;
	
	/**
	 * 分类管理列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="分类管理数据列表", httpMethod = "POST", notes = "获取分类管理列表")
	public PageList<MessageType> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return messageTypeManager.query(queryFilter);
	}
	
	/**
	 * 分类管理明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="分类管理数据详情",httpMethod = "GET",notes = "分类管理数据详情")
	public MessageType get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return messageTypeManager.get(id);
	}
	
    /**
	 * 新增分类管理
	 * @param messageType
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="save")
	@ApiOperation(value = "新增,更新分类管理数据", httpMethod = "POST", notes = "新增,更新分类管理数据")
	public CommonResult<String> save(@ApiParam(name="messageType",value="分类管理业务对象", required = true)@RequestBody MessageType messageType) throws Exception{
		String msg = "添加分类管理成功";
		if(StringUtil.isEmpty(messageType.getId())){
			messageTypeManager.create(messageType);
		}else{
			messageTypeManager.update(messageType);
			 msg = "更新分类管理成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除分类管理记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="remove/{id}")
	@ApiOperation(value = "删除分类管理记录", httpMethod = "DELETE", notes = "删除分类管理记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		messageTypeManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除分类管理记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除分类管理记录", httpMethod = "DELETE", notes = "批量删除分类管理记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="业务主键数组,多个业务主键之间用逗号分隔", required = true)@RequestParam String ids) throws Exception{
		String[] aryIds=StringUtil.getStringAryByStr(ids);
		messageTypeManager.removeByIds(aryIds);
		return new CommonResult<String>(true, "批量删除成功");
	}
	
	/**
	 * 获取当前用户有权限的分类
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@GetMapping("/getHasRightMessageType")
	@ApiOperation(value="获取当前用户有权限的分类", httpMethod = "GET", notes = "获取当前用户有权限的分类")
	public List<MessageType> getHasRightMeetingRoom() throws Exception{
		QueryFilter queryFilter=QueryFilter.build();
		return messageTypeManager.query(queryFilter).getRows();
	}
}
