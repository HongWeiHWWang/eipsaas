package com.hotent.mail.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Arrays;
import java.util.Optional;

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
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.mail.model.MailSetting;
import com.hotent.mail.persistence.manager.MailSettingManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.service.IUserService;

/**
 * 外部邮件用户设置 控制器类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@RestController
@RequestMapping("/mail/mail/mailSetting/v1/")
@Api(tags="外部邮件设置")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
@SuppressWarnings("rawtypes")
public class MailSettingController extends BaseController<MailSettingManager, MailSetting>{
	@Resource
	MailSettingManager mailSettingManager;
	@Resource
	IUserService ius;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取外部邮件用户设置列表(分页条件查询)数据", httpMethod = "POST", notes = "获取外部邮件用户设置列表(分页条件查询)数据")
	public PageList<MailSetting> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		queryFilter.addFilter("userId", ContextUtil.getCurrentUserId(), QueryOP.EQUAL,FieldRelation.AND,"userId");
		return mailSettingManager.query(queryFilter);
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取外部邮件用户设置明细页面", httpMethod = "GET", notes = "获取外部邮件用户设置明细页面")
	public @ResponseBody MailSetting getJson(@ApiParam(name="id", value="邮箱设置id", required = true)@RequestParam String id) throws Exception {
		return mailSettingManager.get(id);
	}

	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存外部邮件用户设置信息", httpMethod = "POST", notes = "保存外部邮件用户设置信息")
	public CommonResult<String> save(@ApiParam(name="mailSetting", value="邮箱设置")@RequestBody MailSetting mailSetting, 
									 @ApiParam(name="isOriginPwd", value="是否原始密码", required = true)@RequestParam Optional<Boolean> isOriginPwd) throws Exception {
		try {
			mailSettingManager.saveSetting(mailSetting, isOriginPwd.orElse(false), ContextUtil.getCurrentUserId());
			return new CommonResult<String>(true,"保存成功");
		}
		catch(Exception ex) {
			String message = ex.getMessage();
			return new CommonResult<String>(false, message);
		}
	}
	
	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除外部邮件用户设置记录", httpMethod = "DELETE", notes = "批量删除外部邮件用户设置记录")
	public CommonResult<String> remove(@ApiParam(name="ids", value="外部邮件用户id", required = true)@RequestParam String ids) throws Exception {
		try {
			String[] aryIds = StringUtil.getStringAryByStr(ids);
			mailSettingManager.removeByIds(Arrays.asList(aryIds));
			return new CommonResult<>(true, "删除外部邮件用户设置成功", null);
		} catch (Exception e) {
			return new CommonResult<>(false, "删除外部邮件用户设置失败", null);
		}
	}
	
	@RequestMapping(value="test", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "测试接收/发送 服务器连接情况", httpMethod = "POST", notes = "测试接收/发送 服务器连接情况")
	public CommonResult<String> test(@ApiParam(name="id", value="计划名称", required = true)@RequestParam Optional<String> id,
			@ApiParam(name="isOriginPwd", value="是否原始密码", required = true)@RequestParam Optional<Boolean> isOriginPwd,
			@ApiParam(name="mailSetting", value="邮箱设置")@RequestBody Optional<MailSetting> mailSetting) throws Exception {
		try {
			MailSetting setting = mailSetting.orElse(null);
			if(BeanUtils.isNotEmpty(setting) && setting.getMailType()!=null) {
				mailSettingManager.testConnection(setting, isOriginPwd.orElse(false));
			}
			else {
				mailSettingManager.testConnection(id.orElse(null));
			}
			return new CommonResult<String>();
		}
		catch(Exception ex) {
			String message = "测试连接失败,请检查邮箱配置";
			return new CommonResult<String>(false, message);
		}
	}

	@RequestMapping(value="setDefault", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "更改默认邮箱设置", httpMethod = "POST", notes = "更改默认邮箱设置")
	public CommonResult<String> setupDefault(@ApiParam(name="id", value="邮箱设置id", required = true)@RequestParam String id) throws Exception {
		String userId = ContextUtil.getCurrentUserId();
		MailSetting mailSetting=mailSettingManager.get(id);
		mailSetting.setIsDefault((short)1);
		try{
			mailSettingManager.setDefault(mailSetting, userId);
			return new CommonResult<>(true, "设置成功!");
		}catch(Exception ex){
			return new CommonResult<>(false, "设置失败!");
		}
	}
}

