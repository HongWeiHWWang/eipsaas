package com.hotent.mail.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.mail.model.MailLinkman;
import com.hotent.mail.persistence.manager.MailLinkmanManager;
import com.hotent.mail.persistence.manager.MailManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * 外部邮件最近联系 控制器类 
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@Controller
@RestController
@RequestMapping("/mail/mail/mailLinkman/v1/")
@Api(tags="外部邮件联系人")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
@SuppressWarnings("rawtypes")
public class MailLinkmanController extends BaseController<MailLinkmanManager, MailLinkman>{
	@Resource
	MailLinkmanManager mailLinkmanManager;
	@Resource
	MailManager mailManager;
	@Resource
	IUserService ius;
	@Resource
	UCFeignService ucService;
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="listJson", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取外部邮件最近联系列表(分页条件查询)数据", httpMethod = "POST", notes = "获取外部邮件最近联系列表(分页条件查询)数据")
	public PageList<MailLinkman> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		queryFilter.addFilter("userId", ContextUtil.getCurrentUserId(), QueryOP.EQUAL,FieldRelation.AND,"userId");
		return mailLinkmanManager.query(queryFilter);
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "外部邮件最近联系明细页面", httpMethod = "GET", notes = "外部邮件最近联系明细页面")
	public @ResponseBody MailLinkman getJson(@ApiParam(name="id", value="联系id", required = true)@RequestParam String id) throws Exception {
		if(StringUtil.isEmpty(id)){
			return new MailLinkman();
		}
		MailLinkman mailLinkman=mailLinkmanManager.get(id);
		return mailLinkman;
	}
	
	@RequestMapping(value="getLinkMan", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "通过邮箱地址查询联系人信息", httpMethod = "GET", notes = "通过邮箱地址查询联系人信息")
	public @ResponseBody CommonResult<String> getLinkMan(@ApiParam(name="mailId", value="邮箱地址", required = true)@RequestParam String mailId) throws Exception {
		String userId = ContextUtil.getCurrentUserId();
		MailLinkman mailLinkman=mailLinkmanManager.findLinkMan(mailId,userId);
		if(mailLinkman!=null) {
			return new CommonResult<>(true,"1");
		}
		return new CommonResult<>(true,"2");
	}
	
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存外部邮件最近联系信息", httpMethod = "POST", notes = "保存外部邮件最近联系信息")
	public CommonResult<String> save(@ApiParam(name="mailLinkman", value="计划名称")@RequestBody MailLinkman mailLinkman) throws Exception {
		String resultMsg=null;
		String LINKID=mailLinkman.getId();
		String mailId=mailLinkman.getMailId();
		String userId = ContextUtil.getCurrentUserId();
		mailLinkman.setUserId(userId);
		try {
			if(StringUtil.isEmpty(LINKID)){
				MailLinkman mailman=mailLinkmanManager.findLinkMan(mailId,userId);
				if(mailman!=null) {
					resultMsg="该邮箱地址已存在，无须重复添加";
					return new CommonResult<>(false, resultMsg);
				}else {
					mailLinkman.setId(UniqueIdUtil.getSuid());
					mailLinkmanManager.create(mailLinkman);
					resultMsg="添加邮箱联系人成功";
				}
			}else{
				mailLinkmanManager.update(mailLinkman);
				resultMsg="更新邮箱联系人成功";
			}
			return new CommonResult<>(true, resultMsg);
		} catch (Exception e) {
			resultMsg="对外部邮件最近联系操作失败";
			return new CommonResult<>(false, resultMsg);
		}
	}

	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除外部邮件最近联系记录", httpMethod = "DELETE", notes = "批量删除外部邮件最近联系记录")
	public CommonResult<String> remove(@ApiParam(name="ids", value="联系人ids", required = true)@RequestParam String ids) throws Exception {
		try {
			String[] aryIds=StringUtil.getStringAryByStr(ids);
			mailLinkmanManager.removeByIds(Arrays.asList(aryIds));
			return new CommonResult<>(true, "删除外部邮件最近联系成功", null);
		} catch (Exception e) {
			return new CommonResult<>(false, "删除外部邮件最近联系失败", null);
		}
	}
	
	@RequestMapping(value="getMailLinkmanData", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "最近联系人树形列表的json数据", httpMethod = "GET", notes = "最近联系人树形列表的json数据")
	public List<MailLinkman> getOutMailLinkmanData(@ApiParam(name="condition" , value="最近联系", required = true)@RequestParam String condition) throws Exception {
		String userId = ContextUtil.getCurrentUserId();
		List<MailLinkman> mailLinkmans=mailLinkmanManager.getAllByUserId(userId,condition);
		List<MailLinkman> mailLinkmanList = new ArrayList<MailLinkman>();
		for(MailLinkman man :mailLinkmans){
			String linkName = mailManager.getNameByEmail(man.getLinkAddress());//查看联系人
			man.setLinkAddress(linkName+"("+man.getLinkAddress()+")");
			man.setLinkName(linkName);
			mailLinkmanList.add(man);
		}
		return mailLinkmanList;
	}
	
	@RequestMapping(value="getMailAndUserData", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "模糊查询邮箱联系人和用户", httpMethod = "GET", notes = "模糊查询邮箱联系人和用户")
	public @ResponseBody List<MailLinkman> getMailAndUserData(@ApiParam(name="query",value="查询条件",required=true)@RequestParam String query) throws Exception{
		List<MailLinkman> mailLinkmanList = new ArrayList<MailLinkman>();
		if(StringUtil.isEmpty(query)){
			return new ArrayList<MailLinkman>(); 
		}
		query="%"+query+"%";
		mailLinkmanList =mailLinkmanManager.queryByAll(query);
		List<ObjectNode> list=ucService.getUserByNameaAndEmal(query);
		if(BeanUtils.isNotEmpty(list)) {
			for(ObjectNode o:list) {
				MailLinkman mail=new MailLinkman();
				String userId=o.get("id").asText();
				String name=o.get("fullname").asText();
				if(o.get("email")!=null) {
					String email=o.get("email").asText();
					mail.setMailId(email);
				}
				ObjectNode org=ucService.getMainGroup(userId);
				if(org!=null) {
					String orgName=org.get("name").asText();
					mail.setOrgName(orgName);
				}
				mail.setId(userId);
				mail.setLinkName(name);
				mailLinkmanList.add(mail);
			}
		}
		
		return mailLinkmanList;
	}
}