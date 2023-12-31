package com.hotent.runtime.core.persistence.manager;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.persistence.model.DefaultBpmTask;
import com.hotent.runtime.core.RunTimeTestCase;
import com.hotent.runtime.manager.TaskTransService;
import com.hotent.runtime.model.TaskTrans;
import com.hotent.runtime.params.TaskTransParamObject;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;

/**
 * TaskTransService测试
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月27日
 */
public class TaskTransServiceTest extends RunTimeTestCase{
	@Resource
	TaskTransService taskTransService;
	@Resource
	IUserService userService;
	
	@Test
	public void completeTask() throws IOException, Exception{
		taskTransService.completeTask("10000000510027", "agree", "inner", "同意","");
	}
	
	@Test
	public void getMyTransTask() throws IOException, Exception{
		QueryFilter filter = QueryFilter.build()
				                        .withDefaultPage()
				                        .withQuery(new QueryField("taskId","10000000510027"));
		PageList<DefaultBpmTask> page = taskTransService.getMyTransTask("1",filter);
		System.out.println(page.getTotal());
	}
	
	@Test
	public void withDraw() throws IOException, Exception{
		taskTransService.withDraw("10000000510027", "inner", "同意");
	}
	
	@Test
	public void addTransTask() throws IOException, Exception{
		TaskTransParamObject taskTransParamObject = new TaskTransParamObject();
		taskTransParamObject.setTaskId("10000000510027");
		taskTransParamObject.setUserIds("1");
		taskTransParamObject.setOpinion("测试");
		taskTransParamObject.setAction("back");
		taskTransParamObject.setDecideType("agree");
		taskTransParamObject.setNotifyType("inner");
		taskTransParamObject.setVoteAmount(Short.valueOf("1"));
		TaskTrans taskTrans = JsonUtil.toBean(JsonUtil.toJson(taskTransParamObject), TaskTrans.class);
		List<IUser> users = new ArrayList<IUser>();
		IUser user = userService.getUserByAccount("admin");
		users.add(user);
		taskTransService.addTransTask(taskTrans, users, "inner", "测试","","",false);
	}
	
	@Test
	public void getTransTaskByTaskId() throws IOException, Exception{
		TaskTrans taskTrans = taskTransService.getTransTaskByTaskId("10000000510027");
		assertEquals(null, taskTrans);
	}
	
}
