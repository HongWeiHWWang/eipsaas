package com.hotent.base.feign.impl;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hotent.base.feign.BpmModelFeignService;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.model.CommonResult;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class BpmModelFeignServiceImpl implements BpmModelFeignService {
	private static Logger logger = LoggerFactory.getLogger(FormFeignService.class);
	
	@Override
	public List<String> getApprovalByDefKeyAndTypeId(String defKey,
			String typeId, String userId) {
		logger.error("bpm-model服务开小差了，请稍后重试");
		return new ArrayList<String>();
	}

	@Override
	public CommonResult<String> executeTaskReminderJob() {
		logger.error("bpm-model服务开小差了，请稍后重试");
		return new CommonResult<String>(false, "服务开小差了，执行催办失败");
	}

	@Override
	public CommonResult<Boolean> isBoBindFlowCheck(String boCode, String formKey) {
		logger.error("bpm-model服务开小差了，请稍后重试");
		return new CommonResult<Boolean>(false, "服务开小差了，删除表单失败",true);
	}

	@Override
	public Set<String> getMyOftenFlowKey() {
		logger.error("bpm-model服务开小差了，请稍后重试");
		return new HashSet<>();
	}

	@Override
	public List<Map<String, String>> bpmDefinitionData(String alias) {
		logger.error("bpm-model服务开小差了，请稍后重试");
		return new ArrayList<>();
	}
}
