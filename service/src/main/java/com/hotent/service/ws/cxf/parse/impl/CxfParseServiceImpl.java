package com.hotent.service.ws.cxf.parse.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.SchemaInfo;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.ws.commons.schema.XmlSchemaForm;
import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;
import com.hotent.service.exception.WSDLParseException;
import com.hotent.service.ws.cxf.parse.CxfParseService;
import com.hotent.service.ws.model.SoapBindingInfo;
import com.hotent.service.ws.model.SoapBindingOperationInfo;
import com.hotent.service.ws.model.SoapParamInfo;
import com.hotent.service.ws.model.SoapService;
import com.hotent.service.ws.model.SoapServiceInfo;
import com.hotent.service.ws.security.AuthInfoContext;

@Service
public class CxfParseServiceImpl implements CxfParseService {

	private static final Log logger = LogFactory.getLog(CxfParseServiceImpl.class);
	//java类型字典
	private String[] javaDataType = new String[]{"java.lang.String","java.lang.Integer","java.lang.Boolean",
										"java.lang.Byte","java.lang.Double","java.lang.Short",
										"java.lang.Long","java.lang.Float","java.lang.Number",
										"java.util.Date","java.math.BigInteger","java.math.BigDecimal",
										"javax.xml.datatype.XMLGregorianCalendar","short","int"};
	//soap协议字典
	private String[] soapVersion = new String[]{"http://schemas.xmlsoap.org/wsdl/soap/",
												"http://schemas.xmlsoap.org/wsdl/soap12/"};

	@Override
	public SoapService parse(Client client) {
		try {
			return this.parseWSDL(client);
		} catch (Exception e) {
			throw new WSDLParseException(e.getMessage());
		}
	}

	@Override
	public SoapService parse(File wsdlFile) {
		URL wsdlURL;
		try {
			wsdlURL = wsdlFile.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new WSDLParseException(e.getMessage());
		}
		Client client = createClient(wsdlURL);
		try {
			return this.parseWSDL(client);
		} catch (Exception e) {
			throw new WSDLParseException(e.getMessage());
		}
	}

	@Override
	public Client createClient(String wsdlPath) {
		logger.debug("开始解析wsdl:" + wsdlPath);
		URL wsdlURL;
		try {
			wsdlURL = new URL(wsdlPath);
		} catch (MalformedURLException e) {
			throw new WSDLParseException(e.getMessage());
		}

		return createClient(wsdlURL);
	}

	@Override
	public Client createClient(File wsdlFile) {
		try {
			return createClient(wsdlFile.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new WSDLParseException(e.getMessage());
		}
	}

	private Client createClient(URL wsdlURL) {
		JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
		factory.setJaxbContextProperties(null);
		logger.debug(wsdlURL.toExternalForm());
		Client client = factory.createClient(wsdlURL.toExternalForm());
		if (AuthInfoContext.authed()) {//HTTP Basic authentication
			AuthorizationPolicy authorization = ((HTTPConduit) client.getConduit()).getAuthorization();  
			authorization.setUserName(AuthInfoContext.getProperty(AuthInfoContext.AUTH_USERNAME).toString());  
			authorization.setPassword(AuthInfoContext.getProperty(AuthInfoContext.AUTH_PASSWORD).toString()); 
			AuthInfoContext.clear();
		}
		return client;
	}

	@Override
	public SoapService parse(String wsdlPath) {
		try {
			return this.parseWSDL(this.createClient(wsdlPath));
		}catch (Exception e) {
			throw new WSDLParseException(e.getMessage());
		}
	}
	
	//解析服务并构建SoapService对象
	private SoapService parseWSDL(Client client) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		//wsdl文档中的service元素(每个wsdl文档有且仅有一个)
		org.apache.cxf.service.Service service = client.getEndpoint().getService();
		logger.debug("服务名称：" + service.getName());
		SoapService soapService = new SoapService();
		soapService.setName(service.getName().getLocalPart());
		soapService.setNamespace(service.getName().getNamespaceURI());
		this.builderServiceInfo(service, soapService);
		return soapService;
	}
	
	private void builderServiceInfo(org.apache.cxf.service.Service service, SoapService soapService) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		logger.debug("开始构建服务信息。");
		//按照portType分类的service列表
		List<ServiceInfo> serviceInfos = service.getServiceInfos();
		if (BeanUtils.isEmpty(serviceInfos)) {
			logger.debug("服务信息内容为空。");
			return;
		}
		for (ServiceInfo serviceInfo : serviceInfos) {
			logger.debug("正在处理服务信息：" + serviceInfo.getName());
			SoapServiceInfo soapServiceInfo = new SoapServiceInfo();
			soapServiceInfo.setName(serviceInfo.getName().getLocalPart());
			soapServiceInfo.setNamespace(serviceInfo.getName().getNamespaceURI());
			List<SchemaInfo> schemas = serviceInfo.getSchemas();
			if(BeanUtils.isNotEmpty(schemas)){
				SchemaInfo schemaInfo = schemas.get(0);
				XmlSchemaForm elementFormDefault = schemaInfo.getSchema().getElementFormDefault();
				boolean equals = elementFormDefault.equals(XmlSchemaForm.UNQUALIFIED);
				if(equals){
					soapServiceInfo.setElementFormDefault(SoapServiceInfo.ELEMENTFORMDEFAULT_UNQUALIFIED);
				}
				else{
					soapServiceInfo.setElementFormDefault(SoapServiceInfo.ELEMENTFORMDEFAULT_QUALIFIED);
				}
			}
			// 构建服务绑定
			this.builderBindingInfo(soapService, serviceInfo, soapServiceInfo);
		}
	}
	
	//获取服务对应的binding信息
	private void builderBindingInfo(SoapService soapService, ServiceInfo serviceInfo, SoapServiceInfo soapServiceInfo) throws IllegalAccessException, InvocationTargetException,NoSuchMethodException {
		Collection<EndpointInfo> points = serviceInfo.getEndpoints();
		Boolean tag = true;
		if (BeanUtils.isEmpty(points)) {
			logger.debug("服务的绑定信息为空。");
			return;
		}
		for(EndpointInfo endpointInfo : points){
			String address = endpointInfo.getAddress();
			BindingInfo bindingInfo = endpointInfo.getBinding();
			String bingdingId = bindingInfo.getBindingId();
			//非soap服务
			if(!Arrays.asList(soapVersion).contains(bingdingId)){
				tag = false;
				continue;
			}
			logger.debug("正在处理服务绑定信息：" + bindingInfo.getName());
			SoapBindingInfo soapBindingInfo = new SoapBindingInfo();
			soapBindingInfo.setName(bindingInfo.getName().getLocalPart());
			soapBindingInfo.setNamespace(bindingInfo.getName().getNamespaceURI());
			soapBindingInfo.setAddress(address);
			this.builderBindingOperationInfo(soapService, bindingInfo,soapBindingInfo);
			soapServiceInfo.putSoapBindingInfo(soapBindingInfo);
		}
		if(tag){
			soapService.putSoapServiceInfo(soapServiceInfo);
		}
	}

	private void builderBindingOperationInfo(SoapService soapService, BindingInfo bindingInfo, SoapBindingInfo soapBindingInfo) throws IllegalAccessException, InvocationTargetException,NoSuchMethodException {
		Collection<BindingOperationInfo> bindingOperationInfos = bindingInfo.getOperations(); 
		if (BeanUtils.isEmpty(bindingOperationInfos)) {
			logger.debug("服务的绑定的方法信息为空。");
			return;
		}

		for (BindingOperationInfo bindingOperationInfo : bindingOperationInfos) {
			logger.debug("正在处理服务绑定方法信息：" + bindingOperationInfo.getName());
			SoapBindingOperationInfo soapBindingOperationInfo = new SoapBindingOperationInfo();
			soapBindingOperationInfo.setName(bindingOperationInfo.getName().getLocalPart());
			soapBindingOperationInfo.setNamespace(bindingOperationInfo.getName().getNamespaceURI());

			this.builderParams(bindingOperationInfo, soapBindingOperationInfo);

			soapBindingInfo.putSoapBindingOperationInfo(soapBindingOperationInfo);
		}
	}

	private void builderParams(BindingOperationInfo bindingOperationInfo,SoapBindingOperationInfo soapBindingOperationInfo)throws IllegalAccessException, InvocationTargetException,NoSuchMethodException {
		List<MessagePartInfo> inputMessageParts = bindingOperationInfo.getInput().getMessageParts();
		if (BeanUtils.isNotEmpty(inputMessageParts)) {
			for (MessagePartInfo messagePartInfo : inputMessageParts) {
				logger.debug("正在处理输入参数:" + messagePartInfo.getName() + ",Index:" + messagePartInfo.getIndex() + ",QName:" + messagePartInfo.getTypeQName());
				Map<String, Object> structureInfos = parseFieldType(messagePartInfo.getTypeClass());
				SoapParamInfo soapParamInfo = new SoapParamInfo();
				soapParamInfo.setName(messagePartInfo.getName().getLocalPart());
				soapParamInfo.setNamespace(messagePartInfo.getName().getNamespaceURI());
				soapParamInfo.setIndex(messagePartInfo.getIndex());
				soapParamInfo.setStructureInfos(structureInfos);
				soapParamInfo.setTypeClass(messagePartInfo.getTypeClass());
				soapBindingOperationInfo.putInputParam(soapParamInfo);
			}
		}
		
		List<MessagePartInfo> outputMessageParts = bindingOperationInfo.getOutput().getMessageParts();

		if (BeanUtils.isNotEmpty(outputMessageParts)) {
			for (MessagePartInfo messagePartInfo : outputMessageParts) {
				logger.debug("正在处理输出参数:" + messagePartInfo.getName() + ",Index:" + messagePartInfo.getIndex() + ",QName:" + messagePartInfo.getTypeQName());
				Map<String, Object> structureInfos = parseFieldType(messagePartInfo.getTypeClass());
				SoapParamInfo soapParamInfo = new SoapParamInfo();
				soapParamInfo.setName(messagePartInfo.getName().getLocalPart());
				soapParamInfo.setNamespace(messagePartInfo.getName().getNamespaceURI());
				soapParamInfo.setIndex(messagePartInfo.getIndex());
				soapParamInfo.setStructureInfos(structureInfos);
				soapParamInfo.setTypeClass(messagePartInfo.getTypeClass());
				soapBindingOperationInfo.putOutputParam(soapParamInfo);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Object> parseFieldType(Object bean) throws IllegalAccessException, InvocationTargetException,NoSuchMethodException {
		Map<String, Object> beanFields = new TreeMap<String, Object>();
		if(BeanUtils.isEmpty(bean)) return beanFields;
		
		//泛型
		if(bean instanceof ParameterizedType){
			ParameterizedType parameterizedType = (ParameterizedType)bean;
			Type[] actuals = parameterizedType.getActualTypeArguments();
			Type raw = parameterizedType.getRawType();
			Map<String,Object> actualsMap = new HashMap<String,Object>();
			for(Type actual : actuals){
				if(actual instanceof Class){
					Class clazz = (Class)actual;
					String name = clazz.getSimpleName();
					String canonicalName = clazz.getCanonicalName();
					if (Arrays.asList(javaDataType).contains(canonicalName)) {
						actualsMap.put(name, canonicalName);
					} else {
						actualsMap.put(name,this.parseFieldType(actual));
					}
				}
			}
			beanFields.put("_actual", actualsMap);
			if(raw instanceof Class){
				beanFields.put("_raw",((Class)raw).getCanonicalName());
			}
		}
		else{
			if(bean instanceof Class){
				Class cls = (Class)bean;
				if (Arrays.asList(javaDataType).contains(cls.getCanonicalName())) {
					return beanFields;
				}
			}
			
			Map<?, ?> paramsStat = BeanUtilsBean.getInstance().getPropertyUtils().describe(bean);
			Field[] fields = (Field[]) paramsStat.get("declaredFields");
			
			if(BeanUtils.isEmpty(fields)){
				return beanFields;
			}
			//保证参数的顺序是正确的
			for(int i=fields.length-1; i>-1; i--){
				Field field = fields[i];
				String name = field.getName();
				Class clazz = field.getType();
				Type type = field.getGenericType();
				String canonicalName = clazz.getCanonicalName();
				logger.debug("类属性,Name:" + name + ",Type:" + canonicalName + ",GenericType:" + field.getGenericType());
				// 检查是否为java一般类型
				if (Arrays.asList(javaDataType).contains(canonicalName)) {
					logger.debug(canonicalName + "，为一般类型");
					beanFields.put(name, canonicalName);
				} else {
					beanFields.put(name,this.parseFieldType(type));
				}
			}
		}
		return beanFields;
	}
}
