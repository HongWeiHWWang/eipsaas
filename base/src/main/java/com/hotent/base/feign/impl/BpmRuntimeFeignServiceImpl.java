package com.hotent.base.feign.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.EmptyFeignException;
import com.hotent.base.feign.BpmRuntimeFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;

@Component
public class BpmRuntimeFeignServiceImpl implements BpmRuntimeFeignService {
	@Override
	public ObjectNode start(ObjectNode startFlowParamObject) throws Exception {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getByBusinesKey(String businessKey, String formIdentity,
			Boolean isNumber) throws Exception {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getDataByDefId(String defId) throws Exception {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getDataByInst(String instId) throws Exception {
		throw new EmptyFeignException();
	}

    @Override
    public Boolean isSynchronize(String instId, String nodeIds, String status,String lastStatus,String lastNodeIds) throws Exception {
        throw new EmptyFeignException();
    }

	@Override
	public List<Map<String,Object>> getFlowFieldList(QueryFilter queryFilter) throws Exception {
		throw new EmptyFeignException();
	}

    @Override
    public CommonResult<String> getSubDataSqlByFk(ObjectNode boEnt, Object fkValue, String defId, String nodeId,
                                                  String parentDefKey) {
    	throw new EmptyFeignException();
    }

	@Override
	public List<String> getBusLink(ObjectNode startFlowParamObject) throws Exception {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode defAutoStart() throws Exception {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode printBoAndFormKey(String defId, String nodeId, String procInstId) {
		throw new EmptyFeignException();
	}

	@Override
	public CommonResult<String> handleBoDateModify(Map<String, Object> params) throws Exception {
		throw new EmptyFeignException();
	}

	@Override
	public ObjectNode getModifyById(String id) throws Exception {
		throw new EmptyFeignException();
	}

	@Override
	public List<ObjectNode> getTaskListByTenantId(String tenantId) {
		throw new EmptyFeignException();
	}
}
