package com.hotent.bo.instance.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.util.JsonUtil;
import com.hotent.bo.instance.DataTransform;
import com.hotent.bo.model.BoData;
import com.hotent.bo.util.BoUtil;

@Service
public class JsonTransformImpl implements DataTransform {
	@Override
	public BoData parse(String data) throws IOException {
		JsonNode jsonNode = JsonUtil.toJsonNode(data);
		return BoUtil.transJSON(jsonNode);
	}

	@Override
	public String getByData(BoData boData,boolean needInitData) throws IOException{
		ObjectNode json = BoUtil.toJSON(boData,needInitData);
		return json.toString();
	}
}
