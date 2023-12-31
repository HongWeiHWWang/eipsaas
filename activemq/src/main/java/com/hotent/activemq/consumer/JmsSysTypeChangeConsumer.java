package com.hotent.activemq.consumer;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.hotent.activemq.model.JmsSysTypeChangeMessage;
import com.hotent.activemq.model.JmsTableTypeConf;
import com.hotent.activemq.model.JmsTableTypeFiledDetail;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.ExceptionUtil;
import com.hotent.base.util.StringUtil;

@Service
@ConditionalOnProperty(value="jms.enable", matchIfMissing = true)
public class JmsSysTypeChangeConsumer {
	private static final Logger logger = LoggerFactory.getLogger(JmsSysTypeChangeConsumer.class);
	@Resource
	CommonManager commonManager;

	@JmsListener(destination = "${jms.topic.name:eipTopic}", containerFactory="jmsListenerContainerTopic")
	public void receiveQueue(Object model) throws JMSException {
		logger.debug("[JMS]: Topic message is :" + model.getClass().getName() + "---" + model);
		handlerSysTypeChange(model);
	}

	private void handlerSysTypeChange(Object source) throws JMSException {
		if (BeanUtils.isEmpty(source) || !(source instanceof ObjectMessage))
			return;

		ObjectMessage message = (ObjectMessage) source;
		Serializable object = message.getObject();
		if (!(object instanceof JmsSysTypeChangeMessage)) {
			return;
		}
		JmsSysTypeChangeMessage type = (JmsSysTypeChangeMessage) object;
		try {
			if (StringUtil.isNotEmpty(type.getTypeGroupKey()) && JmsTableTypeConf.getTypeConf().containsKey(type.getTypeGroupKey().toUpperCase())) {
				JmsTableTypeFiledDetail detail = JmsTableTypeConf.getTypeConf().get(type.getTypeGroupKey());
				StringBuilder sqlSb = new StringBuilder();
				sqlSb.append("UPDATE ").append(detail.getTableName()).append(" set ");
				//如果是删除则将该数据的分类id也置空
				if (2==type.getOpType()) {
					if (StringUtil.isNotEmpty(detail.getTypeIdFiledName())) {
						sqlSb.append(detail.getTypeIdFiledName()).append(" = ''");
					}
					type.setTypeName("");
				}else if (3==type.getOpType()) {
					if (StringUtil.isNotEmpty(detail.getTypeIdFiledName())) {
						sqlSb.append(detail.getTypeIdFiledName()).append(" = '");
						sqlSb.append(type.getTypeId()).append("'");
					}
				}
				//有分类名称字段则更新分类名称
				if (StringUtil.isNotEmpty(detail.getTypeNameFiledName())) {
					if (StringUtil.isNotEmpty(detail.getTypeIdFiledName()) && 1!=type.getOpType()) {
						sqlSb.append(",");
					}
					sqlSb.append(detail.getTypeNameFiledName()).append(" = '").append(type.getTypeName());
				}
				sqlSb.append("' where ");
				if (3==type.getOpType() && StringUtil.isNotEmpty(detail.getPkFiledName())) {
					sqlSb.append(detail.getPkFiledName()).append(" in ('").append(StringUtil.join(type.getEntityIds().split(","), "','")).append("')");
					commonManager.execute(sqlSb.toString());
				}else if (StringUtil.isNotEmpty(detail.getTypeIdFiledName())) {
					sqlSb.append(detail.getTypeIdFiledName()).append("='").append(type.getTypeId()).append("'");
					commonManager.execute(sqlSb.toString());
				}else if (StringUtil.isNotEmpty(detail.getTypeNameFiledName())) {
					sqlSb.append(detail.getTypeNameFiledName()).append("='").append(type.getOldTypeName()).append("'");
					commonManager.execute(sqlSb.toString());
				}
				
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
	}
}
