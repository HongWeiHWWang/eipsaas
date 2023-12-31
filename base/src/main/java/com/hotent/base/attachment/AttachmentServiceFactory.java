package com.hotent.base.attachment;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.hotent.base.util.BeanUtils;


/**
 * 附件处理工厂类
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月13日
 */
@Service
public class AttachmentServiceFactory {

	// 当前附件处理器
	private AttachmentService currentServices;

	private String saveType = "database";

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	/**
	 * @param attachment the attachment to set
	 */

	@Autowired
	ApplicationContext context;

	public AttachmentService getCurrentServices(String saveType) throws Exception{
		if(BeanUtils.isEmpty(currentServices)||!currentServices.getStoreType().equals(saveType)){
			Map<String, AttachmentService> attachmentServiceMap = context.getBeansOfType(AttachmentService.class);
			String key=saveType+"AttachmentServiceImpl";
			currentServices=attachmentServiceMap.get(key);
			if(BeanUtils.isEmpty(currentServices)){
				throw new RuntimeException("未找到对应的附件处理器，请检查系统属性中的file.saveType属性");
			}
		}
		return currentServices;
	}
}