package com.hotent.service.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.Test;
import com.hotent.base.service.InvokeResult;
import com.hotent.base.service.ServiceClient;
import com.hotent.service.ServiceBaseTest;
import com.hotent.service.model.DefaultInvokeCmd;
import com.hotent.service.parse.ServiceBean;
import com.hotent.service.parse.ServiceParser;
import com.hotent.service.ws.model.SoapBindingInfo;
import com.hotent.service.ws.model.SoapBindingOperationInfo;
import com.hotent.service.ws.model.SoapParamInfo;
import com.hotent.service.ws.model.SoapService;
import com.hotent.service.ws.model.SoapServiceInfo;

/**
 * 测试服务的解析和调用
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月4日
 */
public class ServiceParseTest extends ServiceBaseTest{
	@Resource
	ServiceParser serviceParser;
	@Resource
	ServiceClient serviceClient;
	
	@Test
	public void parserAndInvoke() {
		String wsdlUrl = "http://www.webxml.com.cn/WebServices/TrainTimeWebService.asmx?wsdl";
		ServiceBean parse = serviceParser.parse(wsdlUrl);
		assertNotNull(parse);
		
		SoapService soapService = parse.getSoapService();
		String address = soapService.getAddress();
		List<SoapServiceInfo> soapServiceInfos = soapService.getSoapServiceInfos();
		// 服务信息
		SoapServiceInfo soapServiceInfo = soapServiceInfos.get(0);
		String elementFormDefault = soapServiceInfo.getElementFormDefault();
		// 参数是否需要添加前缀
		boolean needPrefix = "qualified".equals(elementFormDefault);
		List<SoapBindingInfo> soapBindingInfos = soapServiceInfo.getSoapBindingInfos();
		
		// 协议信息，例如:soap1.1 或 soap1.2
		SoapBindingInfo soapBindingInfo = soapBindingInfos.get(0);
		// 方法列表
		List<SoapBindingOperationInfo> soapBindingOperationInfos = soapBindingInfo.getSoapBindingOperationInfos();
		
		String methodName = "";
		String namespace = "";
		for(SoapBindingOperationInfo info : soapBindingOperationInfos) {
			String name = info.getName();
			if("getDetailInfoByTrainCode".equals(name)) {
				namespace = info.getNamespace();
				methodName = name;
				List<SoapParamInfo> inputParams = info.getInputParams();
				for(SoapParamInfo paramInfo : inputParams) {
					@SuppressWarnings("unused")
					Map<String, Object> structureInfos = paramInfo.getStructureInfos();
				}
			}
		}
		
		DefaultInvokeCmd cmd = new DefaultInvokeCmd();
		cmd.setAddress(address);
		cmd.setOperatorNamespace(namespace);
		cmd.setOperatorName(methodName);
		cmd.setNeedPrefix(needPrefix);
		cmd.setJsonParam("{\"TrainCode\":\"k9063\", \"UserID\":\"\"}");
		InvokeResult result = serviceClient.invoke(cmd);
		assertFalse(result.isFault());
		String json = result.getJson();
		assertTrue(json.contains("张家界"));
	}
}
