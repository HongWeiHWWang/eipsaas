package com.hotent.service.ws.cxf.parse;

import java.io.File;

import org.apache.cxf.endpoint.Client;

import com.hotent.service.ws.model.SoapService;

/**
 * cxf框架的服务解析器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public interface CxfParseService {
	/**
	 * 解析soap服务
	 * @param wsdlPath
	 * @return
	 */
	SoapService parse(String wsdlPath);
	/**
	 * 解析soap服务
	 * @param client
	 * @return
	 */
	SoapService parse(Client client);
	/**
	 * 解析soap服务
	 * @param wsdlFile
	 * @return
	 */
	SoapService parse(File wsdlFile);
	/**
	 * 创建调用的客户端
	 * @param wsdlPath
	 * @return
	 */
	Client createClient(String wsdlPath);
	/**
	 * 创建调用的客户端
	 * @param wsdlFile
	 * @return
	 */
	Client createClient(File wsdlFile);
}
