package com.hotent.runtime.listener;

import java.util.Map;

import javax.annotation.Resource;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.hotent.base.exception.BaseException;
import com.hotent.bpm.api.event.DoTaskEvent;
import com.hotent.runtime.manager.TaskTransService;

@Service
public class DoTaskEventListener implements ApplicationListener<DoTaskEvent>{
	@Resource
	TaskTransService taskTransService;

	@Override
	public void onApplicationEvent(DoTaskEvent arg0) {
		try {
			Map<String, Object> map = (Map<String, Object>) arg0.getSource();
			String taskId = map.get("taskId").toString();
			String actionName = map.get("actionName").toString();
			String notifyType = map.get("notifyType").toString();
			String opinion = map.get("opinion").toString();
            String addSignAction = "";
			if(BeanUtils.isNotEmpty(map.get("addSignAction"))){
                addSignAction = map.get("addSignAction").toString();
            }
			taskTransService.completeTask(taskId, actionName, notifyType, opinion,addSignAction);
		} catch (Exception e) {
			System.out.println("流程任务处理失败："+e.getMessage());
			throw new BaseException("流程任务处理失败");
		}
	}
}
