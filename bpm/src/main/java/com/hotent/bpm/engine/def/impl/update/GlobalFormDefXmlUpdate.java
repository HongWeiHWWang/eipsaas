package com.hotent.bpm.engine.def.impl.update;

import org.dom4j.Element;

import com.hotent.bpm.engine.def.DefXmlUpdate;

public class GlobalFormDefXmlUpdate extends DefXmlUpdate {

	@Override
	public void update(Element oldEl, Element newEl) {
		String xParentPath="//ext:extProcess";
		String xPath="//ext:extProcess/ext:globalForm";
		
		handCommon(oldEl, newEl, xParentPath, xPath);
		

	}

}
