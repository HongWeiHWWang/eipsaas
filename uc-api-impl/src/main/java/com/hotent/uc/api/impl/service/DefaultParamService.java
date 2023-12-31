package com.hotent.uc.api.impl.service;

import javax.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.uc.api.service.IParamService;

/**
 * 类 {@code DefaultParamService} 参数服务的实现
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2019年1月4日
 */
@Primary
@Service
public class DefaultParamService implements IParamService{
	@Resource
	UCFeignService ucfeignService;
	
	@Override
	public Object getParamsByKey(String userId, String key) {
		ObjectNode userParamsById = ucfeignService.getUserParamsById(userId, key);
		Assert.notNull(userParamsById, String.format("通过用户ID：%s和参数key：%s未找到对应的用户参数", userId, key));
		JsonNode valueNode = userParamsById.get("value");
		Assert.notNull(valueNode, "获取到的用户参数中没有对应的参数值");
		return valueNode.asText();
	}

	@Override
	public Object getParamByGroup(String groupId, String key) {
		ObjectNode orgParamsById = ucfeignService.getOrgParamsById(groupId, key);
		Assert.notNull(orgParamsById, String.format("通过组织ID：%s和参数key：%s未找到对应的组织参数", groupId, key));
		JsonNode valueNode = orgParamsById.get("value");
		Assert.notNull(valueNode, "获取到的组织参数中没有对应的参数值");
		return valueNode.asText();
	}
}
