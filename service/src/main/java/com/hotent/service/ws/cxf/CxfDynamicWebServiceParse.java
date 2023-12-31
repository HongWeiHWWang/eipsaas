package com.hotent.service.ws.cxf;

import java.io.File;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hotent.base.service.InvokeCmd;
import com.hotent.base.service.InvokeResult;
import com.hotent.service.model.DefaultInvokeResult;
import com.hotent.service.ws.WebServiceClient;
import com.hotent.service.ws.cxf.invok.CxfInvokService;
import com.hotent.service.ws.cxf.parse.CxfParseService;
import com.hotent.service.ws.model.SoapService;
import com.hotent.service.ws.security.AuthInfoContext;

@Service
public class CxfDynamicWebServiceParse implements WebServiceClient {
	@Resource
	private CxfParseService parseService;
	@Resource
	private CxfInvokService invokeService;
	private static final Log logger = LogFactory.getLog(CxfDynamicWebServiceParse.class);
	
	@Override
	public SoapService parse(String wsdlPath) {
		return parseService.parse(wsdlPath);
	}

	@Override
	public SoapService parse(String wsdlPath, String username, String password) {
		setAuthInfo(username, password);
		return parseService.parse(wsdlPath);
	}

	@Override
	public SoapService parse(File wsdlFile) {
		return parseService.parse(wsdlFile);
	}

	@Override
	public SoapService parse(File wsdlFile, String username, String password) {
		setAuthInfo(username, password);
		return parseService.parse(wsdlFile);
	}

	private void setAuthInfo(String username, String password) {
		if (username != null) {
			AuthInfoContext.setProperty(AuthInfoContext.AUTH_USERNAME, username);
			AuthInfoContext.setProperty(AuthInfoContext.AUTH_PASSWORD, password);
		}
	}

	@Override
	public InvokeResult invoke(InvokeCmd invokeCmd){
		try {
			return invokeService.invoke(invokeCmd);
		} catch (Exception e) {
			logger.error(e);
			DefaultInvokeResult result = new DefaultInvokeResult();
			//捕获到异常信息，将异常信息放入返回对象中
			result.setException(e);
			return result;
		}
	}
}
