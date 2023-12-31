package com.hotent.base.model;

import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;

public class HtObjectNode extends ObjectNode {
	private static final long serialVersionUID = 1L;
	
	public HtObjectNode(HtJsonNodeFactory nc) {
		super(nc);
	}

	public HtObjectNode(HtJsonNodeFactory nc, ObjectNode objectNode) {
		this(nc);
		this.setAll(objectNode);
	}

	public String getString(String key) {
		return this.getString(key, "");
	}

	public String getString(String key, String defaultVal) {
		Assert.isTrue(StringUtil.isNotEmpty(key), "key不能为空");
		JsonNode jsonNode = super.get(key);
		if (jsonNode == null) {
			return defaultVal;
		}
		return jsonNode.asText();
	}

	public Integer getInt(String key, Integer defaultVal) {
		Assert.isTrue(StringUtil.isNotEmpty(key), "key不能为空");
		JsonNode jsonNode = super.get(key);
		if (jsonNode == null) {
			return defaultVal;
		}
		return jsonNode.asInt();
	}

	public Integer getInt(String fieldName) {
		return getInt(fieldName, null);
	}

	public Boolean getBoolean(String fieldName, Boolean defaultValue) {
		Assert.isTrue(StringUtil.isNotEmpty(fieldName), "key不能为空");
		JsonNode jsonNode = super.get(fieldName);
		if (jsonNode == null || !jsonNode.isBoolean()) {
			return defaultValue;
		}
		return jsonNode.asBoolean();
	}

	public Boolean getBoolean(String fieldName) {
		return getBoolean(fieldName, false);
	}

	public Long getLong(String fieldName, Long defaultValue) {
		Assert.isTrue(StringUtil.isNotEmpty(fieldName), "key不能为空");
		JsonNode jsonNode = super.get(fieldName);
		if (jsonNode == null || !jsonNode.isLong()) {
			return defaultValue;
		}
		return jsonNode.asLong();
	}

	public Long getLong(String fieldName) {
		return getLong(fieldName, null);
	}

	public ArrayNode getSubTableData(String subTableName) {
		ArrayNode arrayNode = JsonUtil.getMapper().createArrayNode();
		if (super.hasNonNull("subMap")) {
			ObjectNode subMap = (ObjectNode) super.get("subMap");
			if (subMap.hasNonNull(subTableName)) {
				arrayNode = (ArrayNode) subMap.get(subTableName);
			}
		}
		return arrayNode;
	}

	/**
	 * 设置bodata子表的值。
	 * @param subTableName
	 * @param filedName
	 * @param value
	 */
	public void setSubFiledVal(String subTableName, String filedName, Object value) {
		ArrayNode subDatas =getSubTableData(subTableName);
		for (JsonNode sub : subDatas) {
			ObjectNode sNode = (ObjectNode) sub;
			JsonUtil.putObjectToJson(sNode, filedName, value);
		}
	}
	/**
	 * 设置bodata子表的值。
	 * @param subTableName
	 * @param filedName
	 * @param value
	 * @param index
	 */
	public void setSubFiledVal(String subTableName, String filedName, Object value, int index) {
		ArrayNode subDatas = getSubTableData(subTableName);
		if (subDatas.size() <= index) {
			return;
		}
		for (int i = 0; i < subDatas.size(); i++) {
			if (i == index) {
				ObjectNode sNode = (ObjectNode) subDatas.get(index);
				JsonUtil.putObjectToJson(sNode, filedName, value);
			}
		}
	}
}
