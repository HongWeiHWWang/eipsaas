package com.hotent.form.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 用于bo的导入导出的包装类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月13日
 */
@XmlRootElement(name = "bpmFormRights")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormRightXml {
	@XmlElement(name = "BpmFormRight", type = FormRight.class)
	private List<FormRight> rightList=new ArrayList<FormRight>();
	
	public List<FormRight> getRightList() {
		return rightList;
	}

	public void setRightList(List<FormRight> rightList) {
		this.rightList = rightList;
	}

	/**
	 * @param formRights
	 */
	public void addBpmFormRight(List<FormRight> formRights){
		this.rightList.addAll(formRights);
	}
}
