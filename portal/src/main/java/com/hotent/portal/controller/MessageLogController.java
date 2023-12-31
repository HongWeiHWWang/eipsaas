package com.hotent.portal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Arrays;

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
import com.hotent.portal.model.MessageLog;
import com.hotent.portal.persistence.manager.MessageLogManager;

/**
 * 
 * <pre> 
 * 描述：portal_message_log 控制器类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2019-05-30 21:34:36
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RestController
@RequestMapping(value="/portal/messageLog/v1")
@Api(tags="消息发送日志")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class MessageLogController extends BaseController<MessageLogManager, MessageLog>{
	@Resource
	MessageLogManager messageLogManager;
	
	/**
	 * 消息发送日志列表(分页条件查询)数据
	 * @param request
	 * @return
	 * @throws Exception 
	 * PageJson
	 * @exception 
	 */
	@PostMapping("/list")
	@ApiOperation(value="消息发送日志数据列表", httpMethod = "POST", notes = "获取消息发送日志列表")
	public PageList<MessageLog> list(@ApiParam(name="queryFilter",value="查询对象")@RequestBody QueryFilter<MessageLog> queryFilter) throws Exception{
		return messageLogManager.query(queryFilter);
	}
	
	/**
	 * 消息发送日志明细页面
	 * @param id
	 * @return
	 * @throws Exception 
	 * ModelAndView
	 */
	@GetMapping(value="/get/{id}")
	@ApiOperation(value="消息发送日志数据详情",httpMethod = "GET",notes = "消息发送日志数据详情")
	public MessageLog get(@ApiParam(name="id",value="业务对象主键", required = true)@PathVariable String id) throws Exception{
		return messageLogManager.get(id);
	}
	
    /**
	 * 新增消息发送日志
	 * @param MessageLog
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@PostMapping(value="/save")
	@ApiOperation(value = "新增,更新消息发送日志数据", httpMethod = "POST", notes = "新增,更新消息发送日志数据")
	public CommonResult<String> save(@ApiParam(name="MessageLog",value="消息发送日志业务对象", required = true)@RequestBody MessageLog MessageLog) throws Exception{
		String msg = "添加消息发送日志成功";
		if(StringUtil.isEmpty(MessageLog.getId())){
			messageLogManager.create(MessageLog);
		}else{
			messageLogManager.update(MessageLog);
			 msg = "更新消息发送日志成功";
		}
		return new CommonResult<String>(msg);
	}
	
	/**
	 * 删除消息发送日志记录
	 * @param id
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/remove/{id}")
	@ApiOperation(value = "删除消息发送日志记录", httpMethod = "DELETE", notes = "删除消息发送日志记录")
	public  CommonResult<String>  remove(@ApiParam(name="id",value="业务主键", required = true)@PathVariable String id) throws Exception{
		messageLogManager.remove(id);
		return new CommonResult<String>(true, "删除成功");
	}
	
	/**
	 * 批量删除消息发送日志记录
	 * @param ids
	 * @throws Exception 
	 * @return
	 * @exception 
	 */
	@DeleteMapping(value="/removes")
	@ApiOperation(value = "批量删除消息发送日志记录", httpMethod = "DELETE", notes = "批量删除消息发送日志记录")
	public CommonResult<String> removes(@ApiParam(name="ids",value="多个主键之间用逗号分隔", required = true)@RequestParam String...ids) throws Exception{
		messageLogManager.removeByIds(Arrays.asList(ids));
		return new CommonResult<String>(true, "删除成功");
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
		messageLogManager.reinvoke(id);
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
		messageLogManager.signSuccess(id);
		return new CommonResult<String>("标记成功");
	}
}
