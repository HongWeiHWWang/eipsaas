package com.hotent.mail.controller;


import java.util.Arrays;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.mail.model.MailAttachment;
import com.hotent.mail.persistence.manager.MailAttachmentManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 外部邮件附件表 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月8日
 */
@RestController
@RequestMapping("/mail/mail/mailAttachment/v1/")
@Api(tags="外部邮件附件")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
@SuppressWarnings("rawtypes")
public class MailAttachmentController extends BaseController<MailAttachmentManager, MailAttachment>{
	@Resource
	MailAttachmentManager mailAttachmentManager;
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="listJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取外部邮件附件表列表(分页条件查询)数据任务", httpMethod = "GET", notes = "获取外部邮件附件表列表(分页条件查询)数据")
	public PageList<MailAttachment> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		return mailAttachmentManager.query(queryFilter);
	}

	@RequestMapping(value="getJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取外部邮件附件表明细页面", httpMethod = "POST", notes = "获取外部邮件附件表明细页面")
	public @ResponseBody MailAttachment getJson(@ApiParam(name="FILEID", value="附件id", required = true)@RequestParam String fileId) throws Exception {
		if(StringUtil.isEmpty(fileId)){
			return new MailAttachment();
		}
		MailAttachment mailAttachment=mailAttachmentManager.get(fileId);
		return mailAttachment;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存外部邮件附件表信息", httpMethod = "POST", notes = "保存外部邮件附件表信息")
	public CommonResult<String> save(@ApiParam(name="mailAttachment",value="邮件附件")@RequestBody MailAttachment mailAttachment) throws Exception {
		String resultMsg=null;
		String FILEID=mailAttachment.getId();
		try {
			if(StringUtil.isEmpty(FILEID)){
				mailAttachment.setId(UniqueIdUtil.getSuid());
				mailAttachmentManager.create(mailAttachment);
				resultMsg="添加外部邮件附件表成功";
			}else{
				mailAttachmentManager.update(mailAttachment);
				resultMsg="更新外部邮件附件表成功";
			}
			return new CommonResult<>(true, resultMsg, null);
		} catch (Exception e) {
			return new CommonResult<>(false, "对外部邮件附件表操作失败", null);
		}
	}
	
	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除外部邮件附件表记录", httpMethod = "DELETE", notes = "执行任务")
	public CommonResult<String> remove(@ApiParam(name="ids", value="文件ids", required = true)@RequestParam String ids) throws Exception {
		try {
			if(ids==null){
				return null;
			}
			String[] aryId = StringUtil.getStringAryByStr(ids);
			mailAttachmentManager.removeByIds(Arrays.asList(aryId));
			return new CommonResult<>(true, "删除外部邮件附件表成功", null);
		} catch (Exception e) {
		return new CommonResult<>(false, "删除外部邮件附件表失败", null);
	  }
	}
}
