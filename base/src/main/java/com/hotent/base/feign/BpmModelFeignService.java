package com.hotent.base.feign;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotent.base.conf.FeignConfig;
import com.hotent.base.feign.impl.BpmModelFeignServiceImpl;
import com.hotent.base.model.CommonResult;

/**
 * 
 * @author liyg
 *
 */
@FeignClient(name="bpm-model-eureka",fallback=BpmModelFeignServiceImpl.class, configuration=FeignConfig.class)
public interface BpmModelFeignService {

	/**
	 * 获取常用语
	 * @param defKey
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/flow/approvalItem/v1/getApprovalByDefKeyAndTypeId",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	public List<String> getApprovalByDefKeyAndTypeId(
			@RequestParam(value="defKey", required = true) String defKey,
			@RequestParam(value="typeId",required = true) String typeId,
			@RequestParam(name="userId",required=true) String userId);
	
	/**
	 * 执行催办任务
	 */
	@RequestMapping(value="/flow/bpmTaskReminder/v1/executeTaskReminderJob",method=RequestMethod.GET)
	public CommonResult<String>  executeTaskReminderJob();
	
	/**
	 * 检测bo是否已绑定流程
	 */
	@RequestMapping(value="/flow/def/v1/isBoBindFlowCheck",method=RequestMethod.GET)
	public CommonResult<Boolean> isBoBindFlowCheck(@RequestParam(value="boCode", required=true) String boCode,@RequestParam(value="formKey", required=true) String formKey);
	
	/**
	 * 获取我的常用流程key
	 */
	@RequestMapping(value="/bpmModel/BpmOftenFlow/v1/getMyOftenFlowKey",method=RequestMethod.GET)
	public Set<String> getMyOftenFlowKey();

	@RequestMapping(value = "/flow/def/v1/bpmDefinitionData",method=RequestMethod.GET)
	public List<Map<String, String>> bpmDefinitionData(@RequestParam(value="alias", required=true)String alias);
}
