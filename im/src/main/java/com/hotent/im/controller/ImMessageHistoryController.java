package com.hotent.im.controller;


import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.im.persistence.manager.ImMessageHistoryManager;
import com.hotent.im.persistence.model.ImMessageHistory;

/**
 * 
 * <pre> 
 * 描述：聊天消息历史表 控制器类
 * 构建组：x5-bpmx-platform
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2017-11-17 14:45:52
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@RequestMapping("/im/imMessageHistory/v1")
@RestController
@Api(tags="即时通讯历史消息")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class ImMessageHistoryController extends BaseController{
	
	@Resource
	ImMessageHistoryManager imMessageHistoryManager;
	
	@RequestMapping(value="listJson",method=RequestMethod.POST, produces = {"application/json; charset=utf-8" })
	@ApiOperation(value = "获取历史记录", httpMethod = "POST", notes = "获取历史记录")
	public PageList<ImMessageHistory> getUserPage(@ApiParam(name="queryFilter",value="通用查询对象")
	 @RequestBody QueryFilter queryFilter,@ApiParam(name="queryFilter",value="通用查询对象")
	 @RequestParam(required=true) String sessionCode) throws Exception{
		queryFilter.addParams("userAccount", current());
		queryFilter.addFilter("imh.session_code_", sessionCode, QueryOP.EQUAL);
		PageList<ImMessageHistory> query = imMessageHistoryManager.queryHistory(queryFilter);
    	return query;
	}
}
