package com.hotent.service.ws.model;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.BeanUtils;

public class SoapServiceInfo extends AbstractSoapModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 所有参数不要添加名称空间前缀
	 */
	public static final String ELEMENTFORMDEFAULT_UNQUALIFIED = "unqualified";
	/**
	 * 所有参数都需要添加名称空间前缀
	 */
	public static final String ELEMENTFORMDEFAULT_QUALIFIED = "qualified";

	private List<SoapBindingInfo> soapBindingInfos;
	//是否添加名称空间前缀
	private String elementFormDefault = SoapServiceInfo.ELEMENTFORMDEFAULT_QUALIFIED;

	public List<SoapBindingInfo> getSoapBindingInfos() {
		return soapBindingInfos;
	}

	public void setSoapBindingInfos(List<SoapBindingInfo> soapBindingInfos) {
		this.soapBindingInfos = soapBindingInfos;
	}

	public void putSoapBindingInfo(SoapBindingInfo soapBindingInfo) {
		if (BeanUtils.isEmpty(soapBindingInfos)) {
			soapBindingInfos = new ArrayList<SoapBindingInfo>();
		}
		this.soapBindingInfos.add(soapBindingInfo);
	}

	public String getElementFormDefault() {
		return elementFormDefault;
	}

	public void setElementFormDefault(String elementFormDefault) {
		this.elementFormDefault = elementFormDefault;
	}
	
	/**
	 * 获取是否需要添加名称空间前缀
	 * @return
	 */
	public Boolean needPrefix(){
		return SoapServiceInfo.ELEMENTFORMDEFAULT_QUALIFIED.equals(this.elementFormDefault);
	}
}
