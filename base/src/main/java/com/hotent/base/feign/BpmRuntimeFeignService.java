package com.hotent.base.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.conf.FeignConfig;
import com.hotent.base.feign.impl.BpmRuntimeFeignServiceImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.QueryFilter;

/**
 * 流程运行Restful接口访问的代理类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年8月21日
 */
@FeignClient(name="bpm-runtime-eureka",fallback=BpmRuntimeFeignServiceImpl.class, configuration=FeignConfig.class)
public interface BpmRuntimeFeignService {

	@RequestMapping(value = "/runtime/instance/v1/start", method = RequestMethod.POST)
	public ObjectNode start(@RequestBody ObjectNode startFlowParamObject) throws Exception;

	@RequestMapping(value = "/runtime/instance/v1/getBusLink", method = RequestMethod.POST)
	public List<String> getBusLink(@RequestBody ObjectNode startFlowParamObject) throws Exception;

	@RequestMapping(value = "/runtime/instance/v1/getByBusinesKey", method = RequestMethod.GET)
	public ObjectNode getByBusinesKey(@RequestParam(value="businessKey", required=true)String businessKey,@RequestParam(value="formIdentity")String formIdentity,@RequestParam(value="isNumber", required=true)Boolean isNumber) throws Exception;

	@RequestMapping(value = "/runtime/instance/v1/getDataByDefId", method = RequestMethod.GET)
	public List<ObjectNode> getDataByDefId(@RequestParam(value="defId", required=true) String defId) throws Exception;

	@RequestMapping(value = "/runtime/instance/v1/getDataByInst", method = RequestMethod.GET)
	public List<ObjectNode> getDataByInst(@RequestParam(value="instId", required=true) String instId) throws Exception;

    @RequestMapping(value = "/runtime/instance/v1/isSynchronize", method = RequestMethod.GET)
    public Boolean isSynchronize(@RequestParam(value="instId", required=true) String instId,@RequestParam(value="nodeIds", required=true) String nodeIds,
                                 @RequestParam(value="status", required=true) String status,@RequestParam(value="lastStatus", required=true) String lastStatus,
                                 @RequestParam(value="lastNodeIds", required=true) String lastNodeIds) throws Exception;

    @RequestMapping(value = "/runtime/instance/v1/getFlowFieldList", method = RequestMethod.POST)
	public List<Map<String,Object>> getFlowFieldList(@RequestBody QueryFilter queryFilter) throws Exception;

    @RequestMapping(value="/runtime/instance/v1/getSubDataSqlByFk",method=RequestMethod.POST)
    public CommonResult<String> getSubDataSqlByFk(@RequestBody(required=true) ObjectNode boEnt, 
    											@RequestParam(value="fkValue", required=true) Object fkValue, 
    											@RequestParam(value="defId", required=false) String defId, 
    											@RequestParam(value="nodeId", required=false) String nodeId, 
    											@RequestParam(value="parentDefKey", required=false) String parentDefKey) ;

	@RequestMapping(value = "/bpm/bpmAutoStartConf/v1/defAutoStart", method = RequestMethod.GET)
	public ObjectNode defAutoStart() throws Exception;

	//获取节点表单
	@RequestMapping(value="/runtime/instance/v1/printBoAndFormKey",method=RequestMethod.GET)
	public ObjectNode printBoAndFormKey(@RequestParam(value="defId", required=false) String defId,
								 @RequestParam(value="nodeId", required=false) String nodeId,
								 @RequestParam(value="procInstId", required=false) String procInstId) throws Exception;
	
	@RequestMapping(value="/bpm/boDataModifyRecord/v1/handleBoDateModify",method=RequestMethod.POST)
	public CommonResult<String> handleBoDateModify(@RequestBody Map<String, Object> params) throws Exception;
	
	@RequestMapping(value = "/bpm/boDataModifyRecord/v1/getJson", method = RequestMethod.GET)
	public ObjectNode getModifyById(@RequestParam(value="id", required=true) String id) throws Exception;
	
	@RequestMapping(value = "/runtime/task/v1/getTaskListByTenantId", method = RequestMethod.GET)
	public List<ObjectNode> getTaskListByTenantId(@RequestParam(value="tenantId", required=true) String tenantId);
}
