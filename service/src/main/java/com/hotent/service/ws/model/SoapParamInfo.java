package com.hotent.service.ws.model;

import java.util.Map;

public class SoapParamInfo extends AbstractSoapModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int index;

	private Map<String, Object> structureInfos;

	private Class<?> typeClass;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Map<String, Object> getStructureInfos() {
		return structureInfos;
	}

	public void setStructureInfos(Map<String, Object> structureInfos) {
		this.structureInfos = structureInfos;
	}

	public Class<?> getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(Class<?> typeClass) {
		this.typeClass = typeClass;
	}
}
