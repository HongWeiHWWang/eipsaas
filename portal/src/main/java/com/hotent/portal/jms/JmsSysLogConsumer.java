package com.hotent.portal.jms;

import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.constants.JmsConstant;
import com.hotent.base.context.BaseContext;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.ExceptionUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.sys.util.SysLogsUtil;

@Service
@ConditionalOnProperty(value="jms.enable", matchIfMissing = true)
public class JmsSysLogConsumer {
	private static final Logger logger = LoggerFactory.getLogger(JmsSysLogConsumer.class);

	@JmsListener(destination = JmsConstant.SYS_LOG_QUEUE, containerFactory="jmsListenerContainerQueue")
	public void receiveQueue(Object model) {
		logger.debug("[JMS]: queue message is :"+model.getClass().getName()+"---"+model);
		handlerSysLog(model);
	}

	private void handlerSysLog(Object source) {
		if(BeanUtils.isEmpty(source) || !(source instanceof TextMessage)) return;

		TextMessage textMsg = (TextMessage)source;
		try {
			String text = textMsg.getText();
			JsonNode jsonNode = JsonUtil.toJsonNode(text);
			if(BeanUtils.isNotEmpty(jsonNode) && jsonNode.isObject()) {
				ObjectNode objectNode = (ObjectNode)jsonNode;
				String type = JsonUtil.getString(objectNode, "type");
				String logType = JsonUtil.getString(objectNode, "logType");
				String opName = JsonUtil.getString(objectNode, "opeName");
				String executor = JsonUtil.getString(objectNode, "executor");
				String ip = JsonUtil.getString(objectNode, "ip");
				String reqUrl = JsonUtil.getString(objectNode, "reqUrl");
				
				if("sysLog".equals(type)) {
					String tenantId = JsonUtil.getString(objectNode, "tenantId");
					if(StringUtil.isNotEmpty(tenantId)) {
						BaseContext baseContext = AppUtil.getBean(BaseContext.class);
						baseContext.setTempTenantId(tenantId);
						SysLogsUtil.addSysLogs(JsonUtil.getString(objectNode, "id"),
											   opName,
											   executor,
											   ip,
											   logType,
											   JsonUtil.getString(objectNode, "moduleType"), 
											   reqUrl, 
											   JsonUtil.getString(objectNode, "content"));
					}
					else {
						logger.debug(String.format("记录日志时未获取到tenantId，操作名称：%s，操作人：%s，ip：%s，请求地址：%s", opName, executor, ip, reqUrl));
					}
				}
			}
		} catch (Exception e) {
			logger.error( ExceptionUtil.getExceptionMessage(e) );
		}
	}
}
