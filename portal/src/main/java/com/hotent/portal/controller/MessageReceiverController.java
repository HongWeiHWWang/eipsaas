package com.hotent.portal.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.model.MessageRead;
import com.hotent.portal.model.SysMessage;
import com.hotent.portal.params.MessaboxVo;
import com.hotent.portal.persistence.manager.MessageReadManager;
import com.hotent.portal.persistence.manager.SysMessageManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 内部消息
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年7月17日
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequestMapping("/innermsg/messageReceiver/v1")
@Api(tags="内部消息接收")
public class MessageReceiverController extends BaseController{
	@Resource
	MessageReadManager messageReadManager;
	@Resource
	SysMessageManager sysMessageManager;
	@Resource
	IUserService iUserService;

	@RequestMapping(value="list", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "消息列表", httpMethod = "POST", notes = "消息列表")
	public PageList<SysMessage> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception {
		queryFilter.addFilter("receiverId", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
		PageList<SysMessage> sysMessageList = sysMessageManager.getMsgByUserId(queryFilter);
		List<SysMessage> list = sysMessageList.getRows();
		Collections.sort(list, new ReceiveTimeComparator());
		PageList<SysMessage> newSysMessageList = new PageList<SysMessage>();
		//消息类型值得转换
//		for (int i = 0; i < list.size(); i++) {
//			SysMessage sysMessage = list.get(i);
//			String messageType= MessageTypeUtil.getValue(sysMessage.getMessageType());
//			sysMessage.setMessageType(messageType);
//			newSysMessageList.getRows().add(sysMessage);
//		}
		newSysMessageList.setRows(list);
		newSysMessageList.setPage(sysMessageList.getPage());
		newSysMessageList.setPageSize(sysMessageList.getPageSize());
		newSysMessageList.setTotal(sysMessageList.getTotal());
		return newSysMessageList;
	}
	
	//（1）未读的消息排在前面，未读的消息按照“发信时间”从后往前排序   （2）已读的消息只按照“发信时间”从后往前排序；
	static class ReceiveTimeComparator implements Comparator {  
		@Override
		public int compare(Object o1, Object o2) {
			SysMessage p1 = (SysMessage) o1; // 强制转换  
			SysMessage p2 = (SysMessage) o2;  
			LocalDateTime date1 = p1.getReceiveTime();
			LocalDateTime date2 = p2.getReceiveTime();
			int flag = 1;
			if((date1!=null&&date2!=null)||(date1==null&&date2==null)){
				flag = -p1.getCreateTime().compareTo(p2.getCreateTime());
			}else if(date2!=null){
				flag = -1;
			}
            return flag;  
		}  
    } 

	@RequestMapping(value="edit", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "消息编辑页面信息", httpMethod = "GET", notes = "消息编辑页面信息")
	public MessageRead edit(@ApiParam(name="id", value="主键", required = false)@RequestParam String id) throws Exception {
		MessageRead messageRead = null;
		if(StringUtil.isNotEmpty(id)){
			messageRead = messageReadManager.get(id);
		}
		 return messageRead;
	}

	@RequestMapping(value="get", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "消息明细页面", httpMethod = "GET", notes = "消息明细页面")
	public SysMessage get(@ApiParam(name="id", value="主键", required = false)@RequestParam String id) throws Exception {
		SysMessage sysMessage = sysMessageManager.get(id);
		IUser user = ContextUtil.getCurrentUser();
		//保存到已读表
		messageReadManager.addMessageRead(id, user);
		return sysMessage;
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "删除消息记录", httpMethod = "DELETE", notes = "删除消息记录")
	public CommonResult<String> remove(@ApiParam(name="ids",value="任务记录ID，多个用“,”号分隔", required = true) @RequestParam String ids) throws Exception{
		String[] aryIds = null;
		if(!StringUtil.isEmpty(ids)){
			aryIds = ids.split(",");
		}
		sysMessageManager.removeByIds(Arrays.asList(aryIds));
		return new CommonResult<String>(true,"删除消息成功","");
	}
	
	@RequestMapping(value="getMessBoxInfo", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取用户已读未读消息", httpMethod = "GET", notes = "获取用户已读未读消息")
	public MessaboxVo getMessBoxInfo(@ApiParam(name="account", value="用户账号", required = true)@RequestParam String account) throws Exception {
		IUser user = iUserService.getUserByAccount(account);
		int messCount = 0;
		int noReadMessCount = 0;
		if(BeanUtils.isNotEmpty(user)){
			QueryFilter queryFilter = QueryFilter.build().withPage(new PageBean(1,Integer.MAX_VALUE));
			queryFilter.addFilter("receiverId", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
			PageList<SysMessage> sysMessageList = sysMessageManager.getMsgByUserId(queryFilter);
			messCount = (int)sysMessageList.getTotal();
			if(messCount>0){
				for (SysMessage message : sysMessageList.getRows()) {
					if(BeanUtils.isEmpty(message.getReceiveTime())){
						++noReadMessCount;
					}
				}
			}
		}
		return new MessaboxVo(messCount, noReadMessCount);
	}
}
