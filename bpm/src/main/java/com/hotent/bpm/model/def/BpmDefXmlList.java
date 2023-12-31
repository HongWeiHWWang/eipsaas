package com.hotent.bpm.model.def;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 多个流程定义列表。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-21-下午3:59:08
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@XmlRootElement(name = "bpmlist")
@XmlAccessorType(XmlAccessType.FIELD)
public class BpmDefXmlList {
	
	@XmlElements({ @XmlElement(name = "bpmDef", type = BpmDefXml.class) })
	private List<BpmDefXml> bpmList=new ArrayList<BpmDefXml>();

	public List<BpmDefXml> getBpmList() {
		return bpmList;
	}

	public void setBpmList(List<BpmDefXml> bpmList) {
		this.bpmList = bpmList;
	}
	
	
	public void addBpmDefXml(BpmDefXml defXml){
		this.bpmList.add(defXml);
	}
	

}
