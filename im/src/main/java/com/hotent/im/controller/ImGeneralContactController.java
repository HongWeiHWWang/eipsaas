package com.hotent.im.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
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
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.im.persistence.manager.ImGeneralContactManager;
import com.hotent.im.persistence.model.ImGeneralContact;
import com.hotent.uc.api.impl.model.UserFacade;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;


/**
 * 
 * <pre> 
 * 描述：im_general_contact 控制器类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2018-03-23 10:00:00
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@RequestMapping("/im/imGeneralContact/v1/")
@RestController
@Api(tags="即时通讯联系人")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class ImGeneralContactController extends BaseController{
	@Resource
	ImGeneralContactManager imGeneralContactManager;
	@Resource
	IUserService userManager;
	
	@RequestMapping(value="listJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取联系人列表", httpMethod = "GET", notes = "获取联系人列表")
	public @ResponseBody PageList<ImGeneralContact> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		PageList<ImGeneralContact> imGeneralContactList=(PageList<ImGeneralContact>)imGeneralContactManager.query(queryFilter);
		return imGeneralContactList;
	}
	
	@RequestMapping(value="getJson", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取联系人", httpMethod = "GET", notes = "获取联系人")
	public @ResponseBody ImGeneralContact getJson(@ApiParam(name="id",value="主键")@RequestParam String id) throws Exception{
		if(StringUtil.isEmpty(id)){
			return new ImGeneralContact();
		}
		ImGeneralContact imGeneralContact=imGeneralContactManager.get(id);
		return imGeneralContact;
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存联系人", httpMethod = "POST", notes = "保存联系人")
	public @ResponseBody CommonResult<String> save(@ApiParam(name="imGeneralContact",value="保存的对象")@RequestBody ImGeneralContact imGeneralContact) throws Exception{
		CommonResult<String> commonResult= null;
		String id=imGeneralContact.getId();
		try {
			if(StringUtil.isEmpty(id)){
				imGeneralContact.setId(UniqueIdUtil.getSuid());
				imGeneralContactManager.create(imGeneralContact);
			}else{
				imGeneralContactManager.update(imGeneralContact);
			}
			commonResult = new CommonResult<String>(true,"操作成功","");
		} catch (Exception e) {
			commonResult = new CommonResult<String>(false,"操作失败","");
		}
		return commonResult;
	}
	
	@RequestMapping(value="remove", method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除联系人", httpMethod = "DELETE", notes = "删除联系人")
	public @ResponseBody CommonResult<String> remove(@ApiParam(name="id",value="主键,多个用,隔开")@RequestParam String id) throws Exception{
		CommonResult<String> commonResult= null;
		try {
			String[] aryIds=id.split(",");
			imGeneralContactManager.removeByIds(aryIds);
			commonResult = new CommonResult<String>(true,"操作成功","");
		} catch (Exception e) {
			commonResult = new CommonResult<String>(false,"操作失败","");
		}
		return commonResult;
	}
	
	@RequestMapping(value = "getMyGeneralContact",method = RequestMethod.GET, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "获取我的联系人", httpMethod = "GET", notes = "获取我的联系人")
	@ResponseBody
	public Map<String,Object> getMyGeneralContact() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			QueryFilter queryFilter= QueryFilter.build();
			queryFilter.setPageBean(new PageBean(1,1000));
			queryFilter.addFilter("igc.owner_", ContextUtil.getCurrentUser().getAccount(), QueryOP.EQUAL);
			List<ImGeneralContact> imGeneralContacts=imGeneralContactManager.getGeneralContactInfo(queryFilter);
			List<IUser> user = new ArrayList<IUser>();
			if(BeanUtils.isNotEmpty(imGeneralContacts)) {
				List<String> accounts = new ArrayList<String>();
				for(ImGeneralContact imGeneralContact : imGeneralContacts){
					accounts.add(imGeneralContact.getAccount());
				}
				if(accounts.size() > 0) {
					user = userManager.getUserByAccounts(String.join(",", accounts));
				}
			}
			map.put("success", true);
			map.put("users", user);
			map.put("imGeneralContacts", imGeneralContacts);
		} catch (Exception e) {
			map.put("success", false);
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "addContact",method = RequestMethod.POST, produces = {"application/json; charset=utf-8"})
	@ApiOperation(value = "添加联系人", httpMethod = "POST", notes = "添加联系人")
	public void addContact(@ApiParam(name="account",value="用户帐号")@RequestParam String account) throws Exception{
		try {
			UserFacade target = new UserFacade();
			target.setAccount(account);
			imGeneralContactManager.createContact((UserFacade)ContextUtil.getCurrentUser(),target);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
