package com.hotent.bpm.listener;

import java.io.IOException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.event.AutoTestEvent;
import com.hotent.base.constants.JmsConstant;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.bpm.persistence.model.AutoTestModel;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.jms.JmsProducer;

/**
 * 流程仿真测试
 * 
 * <pre>
 * 构建组：x5-bpmx-core
 * 作者：liygui
 * 邮箱:liygui@jee-soft.cn
 * 日期:2018-01-03-上午10:02:23
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@Service
@Transactional
public class AutoTestEventListener implements ApplicationListener<AutoTestEvent>, Ordered
{
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void onApplicationEvent(AutoTestEvent event) {
		AutoTestModel model = (AutoTestModel) event.getSource();
		try {
			doNext(model);
		} catch (Exception e) {
			throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
		}
	}
	
	// 任务审批
	private void doNext(AutoTestModel model) throws ClientProtocolException, IOException{
		ObjectNode jsonObject = JsonUtil.getMapper().createObjectNode();
		jsonObject.put("account",model.getRandomAccount());
		jsonObject.put("actionName", "agree");
		jsonObject.put("taskId", model.getTaskId());
		JmsProducer jmsProducer = AppUtil.getBean(JmsProducer.class);
		jmsProducer.sendToQueue(model, JmsConstant.BPM_TEST_CASE);
	}
}