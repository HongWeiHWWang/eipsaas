package com.hotent.service.ws.model;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.BeanUtils;

public class SoapBindingInfo extends AbstractSoapModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private String address;

	private List<SoapBindingOperationInfo> soapBindingOperationInfos;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<SoapBindingOperationInfo> getSoapBindingOperationInfos() {
		return soapBindingOperationInfos;
	}

	public void setSoapBindingOperationInfos(List<SoapBindingOperationInfo> soapBindingOperationInfos) {
		this.soapBindingOperationInfos = soapBindingOperationInfos;
	}

	public void putSoapBindingOperationInfo(SoapBindingOperationInfo soapBindingOperationInfo) {
		if (BeanUtils.isEmpty(soapBindingOperationInfos)) {
			this.soapBindingOperationInfos = new ArrayList<SoapBindingOperationInfo>();
		}
		this.soapBindingOperationInfos.add(soapBindingOperationInfo);
	}
}
