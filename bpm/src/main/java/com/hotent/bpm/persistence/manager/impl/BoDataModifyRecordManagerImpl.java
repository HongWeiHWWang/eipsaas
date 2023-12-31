package com.hotent.bpm.persistence.manager.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.model.BoDataModifyRecord;
import com.hotent.bpm.persistence.dao.BoDataModifyRecordDao;
import com.hotent.bpm.persistence.manager.BoDataModifyRecordManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre> 
 * 描述：流程表单数据修改记录 处理实现类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-03-23 11:45:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("boDataModifyRecordManager")
public class BoDataModifyRecordManagerImpl extends BaseManagerImpl<BoDataModifyRecordDao, BoDataModifyRecord> implements BoDataModifyRecordManager{
	@Resource
	BoDataModifyRecordDao boDataModifyRecordDao;

	@Override
	public void handleBoDateModify(Map<String, Object> params) throws Exception {
		IUser currentUser = ContextUtil.getCurrentUser();
		String modifyRes = "";
		String refId = "";
		
		//获取数据
		JsonNode boResult = JsonUtil.toJsonNode(params.get("boResult"));
		
		for(JsonNode objectNode:boResult) {
			if(objectNode.hasNonNull("modifyDetail") && StringUtil.isNotEmpty(objectNode.get("modifyDetail").asText())) {
				modifyRes = objectNode.get("modifyDetail").asText();
				refId = objectNode.get("pk").asText();
				break;
			}
		}
		
		if (StringUtil.isEmpty(modifyRes)) {
			return;
		}
		BoDataModifyRecord record = new BoDataModifyRecord();
		
		//添加外键
		if(StringUtil.isNotEmpty(refId)) {
			record.setRefId(refId);
		}
		if(BeanUtils.isNotEmpty(params.get("data"))) {
			record.setData(JsonUtil.toJson(params.get("data")));
		}
		
		record.setUserId(currentUser.getUserId());
		record.setUserName(currentUser.getFullname());
		record.setDetail(modifyRes);
		record.setModifyTime(LocalDateTime.now());
		this.create(record);
	}

	@Override
	public List<BoDataModifyRecord> getListByRefId(String refId) {
		return boDataModifyRecordDao.getListByRefId(refId);
	}

}
