package com.hotent.assembly.feign.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.BpmRuntimeFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.model.BoDataModifyRecord;
import com.hotent.bpm.persistence.model.BpmBusLink;
import com.hotent.runtime.controller.BoDataModifyRecordController;
import com.hotent.runtime.controller.BpmAutoStartConfController;
import com.hotent.runtime.controller.InstanceController;
import com.hotent.runtime.controller.TaskController;
import com.hotent.runtime.params.StartFlowParamObject;
import com.hotent.runtime.params.StartResult;

@Service
@Primary
public class BpmRuntimeFeignServiceAssemblyImpl implements BpmRuntimeFeignService{
	private static String errorMsg = "聚合微服务中的实现类尚未提供该接口的实现";
	@Resource
	InstanceController instanceController;
	@Resource
	TaskController taskController;
	@Resource
    BpmAutoStartConfController bpmAutoStartConfController;
	@Resource
	BoDataModifyRecordController boDataModifyRecordController;
	@Override
	public ObjectNode start(ObjectNode startFlowParamObject) throws Exception {
		try {
			StartFlowParamObject sfpo = JsonUtil.toBean(startFlowParamObject, StartFlowParamObject.class);
			StartResult startResult = instanceController.start(sfpo);
			return (ObjectNode)JsonUtil.toJsonNode(startResult);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getByBusinesKey(String businessKey, String formIdentity, Boolean isNumber) throws Exception {
		try {
			BpmBusLink byBusinesKey = instanceController.getByBusinesKey(businessKey, formIdentity, isNumber);
			return (ObjectNode)JsonUtil.toJsonNode(byBusinesKey);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getDataByDefId(String defId) throws Exception {
		try {
			return instanceController.getDataByDefId(defId);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getDataByInst(String instId) throws Exception {
		throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, errorMsg);
	}

    @Override
    public Boolean isSynchronize(String instId, String nodeIds, String status,String lastStatus,String lastNodeIds) throws Exception {
        try {
            return instanceController.isSynchronize(instId,nodeIds,status,lastStatus,lastNodeIds);
        } catch (Exception e) {
            throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
        }
    }

	@Override
	public List<Map<String,Object>> getFlowFieldList(QueryFilter queryFilter) throws Exception {
		try {
            return instanceController.getFlowFieldList(queryFilter);
        } catch (Exception e) {
            throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
        }
	}

    @Override
    public CommonResult<String> getSubDataSqlByFk(ObjectNode boEnt, Object fkValue, String defId, String nodeId, String parentDefKey) {
        try {
            return instanceController.getSubDataSqlByFk(boEnt, fkValue, defId, nodeId, parentDefKey);
        } catch (Exception e) {
            throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
        }
    }

	@Override
	public List<String> getBusLink(ObjectNode startFlowParamObject) throws Exception {
		try {
			return instanceController.getBusLink(startFlowParamObject);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode defAutoStart() throws Exception {
		try {
			return bpmAutoStartConfController.defAutoStart();
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode printBoAndFormKey(String defId, String nodeId, String procInstId) throws Exception {
		try {
			return instanceController.printBoAndFormKey(defId,nodeId,procInstId);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}

	}

	@Override
	public CommonResult<String> handleBoDateModify(Map<String, Object> params) throws Exception {
		try {
			return boDataModifyRecordController.handleBoDateModify(params);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getModifyById(String id) throws Exception {
		try {
			BoDataModifyRecord record = boDataModifyRecordController.get(id);
			return (ObjectNode)JsonUtil.toJsonNode(record);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<ObjectNode> getTaskListByTenantId(String tenantId) {
		try {
			return taskController.getTaskListByTenantId(tenantId);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
}
