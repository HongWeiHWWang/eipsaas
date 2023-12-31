package com.hotent.assembly.feign.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.BpmModelFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.bpmModel.controller.ApprovalItemController;
import com.hotent.bpmModel.controller.BpmOftenFlowController;
import com.hotent.bpmModel.controller.BpmTaskReminderController;
import com.hotent.bpmModel.controller.DefController;
import com.hotent.bpmModel.controller.NodeController;

@Service
@Primary
public class BpmModelFeignServiceAssemblyImpl implements BpmModelFeignService{
	@Resource
	DefController defController;
	@Resource
	BpmTaskReminderController bpmTaskReminderController;
	@Resource
	ApprovalItemController approvalItemController;
	@Resource
	BpmOftenFlowController bpmOftenFlowController;
    @Resource
    NodeController nodeController;

	@Override
	public List<String> getApprovalByDefKeyAndTypeId(String defKey, String typeId, String userId) {
		try {
			return approvalItemController.getApprovalByDefKeyAndTypeId(defKey, Optional.ofNullable(typeId), Optional.ofNullable(userId));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<String> executeTaskReminderJob() {
		try {
			return bpmTaskReminderController.executeTaskReminderJob();
		} catch (Exception e) {
			return new CommonResult<String>(false,"执行催办任务失败",ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<Boolean> isBoBindFlowCheck(String boCode, String formKey) {
		try {
			return defController.isBoBindFlowCheck(boCode, formKey);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Set<String> getMyOftenFlowKey() {
		try {
			return bpmOftenFlowController.getMyOftenFlowKey();
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<Map<String, String>> bpmDefinitionData(String alias) {
		try {
			return defController.bpmDefinitionData(alias);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
}
