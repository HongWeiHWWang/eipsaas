package com.hotent.service.ws;

import java.io.File;
import com.hotent.base.service.InvokeCmd;
import com.hotent.base.service.InvokeResult;
import com.hotent.service.ws.model.SoapService;

/**
 * WebService调用客户端
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public interface WebServiceClient {
	/**
	 * 解析WebService
	 * @param wsdlPath
	 * @return
	 */
	SoapService parse(String wsdlPath);
	/**
	 * 解析WebService(带账号密码)
	 * @param wsdlPath
	 * @param username
	 * @param password
	 * @return
	 */
	SoapService parse(String wsdlPath,String username,String password);
	/**
	 * 解析WebService
	 * @param wsdlFile
	 * @return
	 */
	SoapService parse(File wsdlFile);
	/**
	 * 解析WebService
	 * @param wsdlFile
	 * @param username
	 * @param password
	 * @return
	 */
	SoapService parse(File wsdlFile,String username,String password);
	/**
	 * 调用WebService
	 * @param invokeCmd
	 * @return
	 */
	InvokeResult invoke(InvokeCmd invokeCmd);
}