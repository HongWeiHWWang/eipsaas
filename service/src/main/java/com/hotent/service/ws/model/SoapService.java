package com.hotent.service.ws.model;

import java.util.ArrayList;
import java.util.List;

import com.hotent.base.util.BeanUtils;

/**
 * soap服务信息
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月3日
 */
public class SoapService extends AbstractSoapModel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private List<SoapServiceInfo> soapServiceInfos;
	
	/**
	 * 获取绑定的soap方法信息
	 * @return
	 */
	public SoapBindingOperationInfo getSoapBindingOperationInfo(String name){
		if(!BeanUtils.isEmpty(soapServiceInfos)){
			SoapServiceInfo soapServiceInfo = soapServiceInfos.get(0);
			List<SoapBindingInfo> soapBindingInfos = soapServiceInfo.getSoapBindingInfos();
			if(!BeanUtils.isEmpty(soapBindingInfos)){
				SoapBindingInfo soapBindingInfo = soapBindingInfos.get(0);
				List<SoapBindingOperationInfo> soapBindingOperationInfos = soapBindingInfo.getSoapBindingOperationInfos();
				
				for (SoapBindingOperationInfo soapBindingOperationInfo : soapBindingOperationInfos) {
					if(name.equals(soapBindingOperationInfo.getName())){
						return soapBindingOperationInfo;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取服务调用的地址
	 * @return
	 */
	public String getAddress(){
		if(!BeanUtils.isEmpty(soapServiceInfos)){
			SoapServiceInfo soapServiceInfo = soapServiceInfos.get(0);
			List<SoapBindingInfo> soapBindingInfos = soapServiceInfo.getSoapBindingInfos();
			if(!BeanUtils.isEmpty(soapBindingInfos)){
				SoapBindingInfo soapBindingInfo = soapBindingInfos.get(0);
				return soapBindingInfo.getAddress();
			}
		}
		return "";
	}

	public List<SoapServiceInfo> getSoapServiceInfos() {
		return soapServiceInfos;
	}

	public void setSoapServiceInfos(List<SoapServiceInfo> soapServiceInfos) {
		this.soapServiceInfos = soapServiceInfos;
	}

	public void putSoapServiceInfo(SoapServiceInfo soapServiceInfo) {
		if (BeanUtils.isEmpty(soapServiceInfos)) {
			this.soapServiceInfos = new ArrayList<SoapServiceInfo>();
		}
		this.soapServiceInfos.add(soapServiceInfo);
	}
}
