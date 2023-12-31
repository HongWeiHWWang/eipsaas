package com.hotent.form.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 多个数据报表列表。
 * <pre> 
 * 作者：zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2020-7-7-16:07:08
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@XmlRootElement(name = "formDataTemplateXmlList")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormDataTemplateXmlList {
	
	@XmlElements({ @XmlElement(name = "formDataTemplateXml", type = FormDataTemplateXml.class) })
	private List<FormDataTemplateXml> formDataTemplateXmlList=new ArrayList<FormDataTemplateXml>();

    public List<FormDataTemplateXml> getFormDataTemplateXmlList() {
        return formDataTemplateXmlList;
    }

    public void setFormDataTemplateXmlList(List<FormDataTemplateXml> formDataTemplateXmlList) {
        this.formDataTemplateXmlList = formDataTemplateXmlList;
    }

    public void addFormDataTemplateXml(FormDataTemplateXml formDataTemplateXml){
		this.formDataTemplateXmlList.add(formDataTemplateXml);
	}
	

}
