package com.hotent.service.parse.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.util.StringUtil;
import com.hotent.service.parse.ServiceBean;
import com.hotent.service.parse.ServiceParse;
import com.hotent.service.ws.WebServiceClient;
import com.hotent.service.ws.model.SoapService;

@Service
public class ServiceParseImpl implements ServiceParse{
	@Resource
	private WebServiceClient webServiceClient;
	
	public ServiceBean parse(String url) {
		if(StringUtil.isEmpty(url))return null;
		if(url.matches(".*\\?wsdl$")){
			ServiceBean serviceBean = new ServiceBean();
			SoapService soapService = webServiceClient.parse(url);
			serviceBean.setUrl(url);
			serviceBean.setName(soapService.getName());
			serviceBean.setNamespace(soapService.getNamespace());
			serviceBean.setSoapService(soapService);
			return serviceBean;
		}
		return null;
	}
}