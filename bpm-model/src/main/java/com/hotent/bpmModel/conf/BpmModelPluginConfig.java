package com.hotent.bpmModel.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hotent.activemq.model.JmsTableTypeConf;
import com.hotent.activemq.model.JmsTableTypeFiledDetail;
import com.hotent.base.util.JsonUtil;
import com.hotent.bpm.api.model.process.nodedef.ext.extmodel.FormExt;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.process.nodedef.ext.extmodel.DefaultFormExt;

@Configuration
public class BpmModelPluginConfig {

	@Bean("formExtregister")
	public FormExt formExt() {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(FormExt.class, new FormExtEventDeserializer());
		module.addDeserializer(Form.class, new FormEventDeserializer());
		JsonUtil.getMapper().registerModule(module);
		return new DefaultFormExt();
	}

	
	@Bean("modelTableTypeConf")
	public JmsTableTypeConf TableTypeConf() {
		JmsTableTypeConf.AddTypeConf("FLOW_TYPE",new JmsTableTypeFiledDetail("bpm_definition","DEF_ID_", "TYPE_ID_", "TYPE_NAME_"));
		return null;
	}

}
