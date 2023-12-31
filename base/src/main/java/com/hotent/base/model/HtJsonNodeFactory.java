package com.hotent.base.model;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class HtJsonNodeFactory extends JsonNodeFactory{
	private static final long serialVersionUID = -2528062905576193172L;

	public static HtJsonNodeFactory build(boolean bigDecimalExact) {
		return new HtJsonNodeFactory(bigDecimalExact);
	}
	
	public static HtJsonNodeFactory build() {
		return new HtJsonNodeFactory();
	}

	public HtJsonNodeFactory() {
		this(false);
	}
	
	public HtJsonNodeFactory(boolean bigDecimalExact) {
		super(bigDecimalExact);
	}

	public HtObjectNode htObjectNode(ObjectNode objectNode) {
		return new HtObjectNode(this, objectNode);
	}
}
