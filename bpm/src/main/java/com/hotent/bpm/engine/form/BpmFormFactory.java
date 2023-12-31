package com.hotent.bpm.engine.form;

import com.hotent.base.util.AppUtil;
import com.hotent.bpm.api.model.form.FormType;
import com.hotent.bpm.api.service.BpmFormService;

/**
 * 返回表单服务。
 * @author ray
 *
 */
public class BpmFormFactory {

	/**
	 * 根据formtype 获取对应的service。
	 * @param formType
	 * @return
	 */
	public static BpmFormService getFormService(FormType formType){
		BpmFormService service=null;
		if(FormType.PC.equals(formType)){
			service=  (BpmFormService) AppUtil.getBean("defaultBpmFormService");
		}
		else if(FormType.MOBILE.equals(formType)){
			service=  (BpmFormService) AppUtil.getBean("mobileFormService");
		}
		
		return service;
		
	}
}
