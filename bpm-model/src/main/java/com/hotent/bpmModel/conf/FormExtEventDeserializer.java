package com.hotent.bpmModel.conf;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.process.nodedef.ext.extmodel.DefaultFormExt;

public class FormExtEventDeserializer extends StdDeserializer<FormExt>
{
	private static final long serialVersionUID = 1L;
	
	protected FormExtEventDeserializer() {
		super(Form.class);
	}

	@Override
	public FormExt deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		TreeNode readValueAsTree = jp.readValueAsTree();
		String json = readValueAsTree.toString();
		DefaultFormExt form = JsonUtil.toBean(json, DefaultFormExt.class);
	    return form;
	}
}
