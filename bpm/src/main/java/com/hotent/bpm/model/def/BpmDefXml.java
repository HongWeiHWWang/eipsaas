package com.hotent.bpm.model.def;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.hotent.bpm.persistence.model.DefaultBpmDefinition;

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
@XmlRootElement(name = "bpmDef")
@XmlAccessorType(XmlAccessType.FIELD)
public class BpmDefXml {

	@XmlElement(name = "bpmDefinition", type = DefaultBpmDefinition.class)
	private DefaultBpmDefinition bpmDefinition;

	public DefaultBpmDefinition getBpmDefinition() {
		return bpmDefinition;
	}

	public void setBpmDefinition(DefaultBpmDefinition bpmDefinition) {
		this.bpmDefinition = bpmDefinition;
	}

}
