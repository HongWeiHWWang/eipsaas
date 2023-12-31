package com.hotent.portal.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.jms.JmsProducer;
import com.hotent.base.jms.Notice;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.AppUtil;
import com.hotent.portal.service.TemplateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * jms相关操作
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年8月2日
 */
@RestController
@RequestMapping("/portal/jms/v1")
@Api(tags="队列消息发送")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class JmsProducerController {
	@Resource
	TemplateService templateService;
	
	@RequestMapping(value="sendNoticeToQueue", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "发送消息", httpMethod = "POST", notes = "发送消息")
	public CommonResult<String> sendNoticeToQueue(@ApiParam(name="notice",value="通知(通过模板来发送的消息)")@RequestBody Notice notice) throws Exception {
		templateService.sendNotice2Jms(notice);
		return new CommonResult<String>(true, "发送消息成功！");
	}
	
	@RequestMapping(value="sendToQueue", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "发送消息到队列中", httpMethod = "POST", notes = "发送消息到队列中")
	public CommonResult<String> sendToQueue(@ApiParam(name="notice",value="通知(通过模板来发送的消息)")@RequestBody ObjectNode model) throws Exception {
		JmsProducer jmsProducer = AppUtil.getBean(JmsProducer.class);
		jmsProducer.sendToQueue(model);
		return new CommonResult<String>(true, "成功发送消息到队列！");
	}
}
