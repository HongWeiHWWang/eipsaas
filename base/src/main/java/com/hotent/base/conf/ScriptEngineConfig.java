package com.hotent.base.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotent.base.groovy.GroovyScriptEngine;

@Configuration
public class ScriptEngineConfig {
	@Bean
	public GroovyScriptEngine getGroovyScriptEngine(){
		GroovyScriptEngine engine = new GroovyScriptEngine();
		List<String> bindingInterface = new ArrayList<String>();
		bindingInterface.add("com.hotent.base.groovy.IScript");
		bindingInterface.add("com.hotent.base.groovy.IUserScript");
		engine.setBindingInterface(bindingInterface);
		return engine;
	}
}
