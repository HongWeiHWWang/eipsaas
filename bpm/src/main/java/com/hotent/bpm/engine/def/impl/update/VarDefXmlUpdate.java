package com.hotent.bpm.engine.def.impl.update;

import org.dom4j.Element;

import com.hotent.bpm.engine.def.DefXmlUpdate;

/**
 * 流程变量更新。
 * <pre> 
 * 构建组：x5-bpmx-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-4-上午8:59:42
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class VarDefXmlUpdate extends DefXmlUpdate {

	@Override
	public void update(Element oldEl, Element newEl) {
		String xParentPath="//ext:extProcess";
		String xPath="//ext:extProcess/ext:varDefs";
		
		handCommon(oldEl, newEl, xParentPath, xPath);
		
		

	}

}
