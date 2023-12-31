package com.hotent.service.client;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.service.InvokeCmd;
import com.hotent.base.service.InvokeResult;
import com.hotent.base.service.ServiceClient;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.service.dao.ServiceSetDao;
import com.hotent.service.model.DefaultInvokeCmd;
import com.hotent.service.model.ServiceSet;
import com.hotent.service.util.ParamHandlerUtil;
import com.hotent.service.ws.WebServiceClient;

/**
 * 提供基于WebService的服务调用实现类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
@Primary
@Service
public class ServiceClientImpl implements ServiceClient{
	@Resource
	ServiceSetDao sysServiceSetDao;
	
	public InvokeResult invoke(InvokeCmd invokeCmd) {
		WebServiceClient webServiceClient = AppUtil.getBean(WebServiceClient.class);
		if("webservice".equals(invokeCmd.getType())){
			return webServiceClient.invoke(invokeCmd);
		}
		return null;
	}

	@Override
	public InvokeResult invoke(String alias, Map<String, Object> map) {
		ServiceSet serviceSet = sysServiceSetDao.getByAlias(alias);

		InvokeCmd invokeCmd = new DefaultInvokeCmd();
		invokeCmd.setAddress(serviceSet.getAddress());
		invokeCmd.setOperatorName(serviceSet.getMethodName());
		invokeCmd.setOperatorNamespace(serviceSet.getNamespace());
		invokeCmd.setNeedPrefix(serviceSet.getSoapAction()=='Y');

		String params;
		InvokeResult result = null;
		try {
			params = getJsonParam(map, serviceSet.getInputSet());
			invokeCmd.setJsonParam(params);
			result = this.invoke(invokeCmd);
		} catch (IOException e) {
			
		}
		return result;
	}

	/**
	 * 构建查询参数
	 * @param variables
	 * @param inputSet
	 * @return
	 * @throws IOException
	 */
	private String getJsonParam(Map<String, Object> variables,String inputSet) throws IOException{
		ArrayNode jsonElement = (ArrayNode) JsonUtil.toJsonNode(inputSet);
		ObjectNode jsonParamObj = JsonUtil.getMapper().createObjectNode();

		if(jsonElement.isArray()){
			ParamHandlerUtil.buildJsonParam(jsonElement,jsonParamObj,variables,ParamHandlerUtil.INPUT);
		}
		String ss = jsonParamObj.toString();
		return ss;
	}
}