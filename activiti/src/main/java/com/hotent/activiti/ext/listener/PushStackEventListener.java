package com.hotent.activiti.ext.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.exception.WorkFlowException;
import com.hotent.bpm.api.constant.BpmConstants;
import com.hotent.bpm.api.constant.MultiInstanceType;
import com.hotent.bpm.api.event.PushStackEvent;
import com.hotent.bpm.api.model.process.task.BpmTask;
import com.hotent.bpm.natapi.inst.NatProInstanceService;
import com.hotent.bpm.persistence.manager.BpmExeStackManager;
import com.hotent.bpm.persistence.model.ActTask;
import com.hotent.bpm.persistence.model.PushStackModel;

/**
 * 加入堆栈监听事件
 * 
 * <pre>
 * 构建组：x5-bpmx-core
 * 作者：liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2020-03-23-上午10:02:23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
@Transactional
public class PushStackEventListener implements ApplicationListener<PushStackEvent>, Ordered {
	
	protected static final Logger logger = LoggerFactory.getLogger(PushStackEventListener.class);
	@Resource
	BpmExeStackManager bpmExeStackManager;
	@Resource
	NatProInstanceService natProInstanceService;
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void onApplicationEvent(PushStackEvent event) {
		try {
			PushStackModel model = (PushStackModel) event.getSource();
			ActTask actTask = model.getActTask();
			BpmTask bpmTask = model.getBpmTask();
			if (actTask == null) {
				return;
			}
			String token = String.valueOf(natProInstanceService.getVariable(actTask.getExecutionId(), BpmConstants.TOKEN_NAME));
			bpmExeStackManager.pushStack(bpmTask.getProcDefId(), token, bpmTask.getProcInstId(), bpmTask.getNodeId(), MultiInstanceType.PARALLEL , bpmTask);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加堆栈数据失败", e);
			throw new WorkFlowException("添加堆栈数据失败");
		}

	}

}