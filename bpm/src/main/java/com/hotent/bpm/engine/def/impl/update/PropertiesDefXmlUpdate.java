package com.hotent.bpm.engine.def.impl.update;

import org.dom4j.Element;

import com.hotent.bpm.engine.def.DefXmlUpdate;

/**
 * 扩展属性更新。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-4-上午8:59:14
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class PropertiesDefXmlUpdate extends DefXmlUpdate {

	@Override
	public void update(Element oldEl, Element newEl) {
		String xParentPath="//ext:extProcess";
		String xPath="//ext:extProcess/ext:extProperties";

		handCommon(oldEl, newEl, xParentPath, xPath);
	}

	

}
