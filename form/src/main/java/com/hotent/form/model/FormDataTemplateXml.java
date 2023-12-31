package com.hotent.form.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 一个流程定义相关的数据。
 * 
 * <pre>
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-21-下午3:58:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@XmlRootElement(name = "formDataTemplate")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormDataTemplateXml {

	@XmlElement(name = "formDataTemplate", type = FormDataTemplate.class)
	private FormDataTemplate formDataTemplate;

    public FormDataTemplate getFormDataTemplate() {
        return formDataTemplate;
    }

    public void setFormDataTemplate(FormDataTemplate formDataTemplate) {
        this.formDataTemplate = formDataTemplate;
    }

}
