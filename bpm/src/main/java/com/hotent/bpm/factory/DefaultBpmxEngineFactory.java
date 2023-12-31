package com.hotent.bpm.factory;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.engine.BpmxEngine;
import com.hotent.bpm.api.engine.BpmxEngineFactory;


//@Service
public class DefaultBpmxEngineFactory implements BpmxEngineFactory {

	@Resource
	private List<BpmxEngine> bpmxEngines;
		
	private String defaultEngineName="x5";

	private static final Log logger = LogFactory.getLog(DefaultBpmxEngineFactory.class);

	@Override
	public BpmxEngine getEngine() {
		return this.getEngine(defaultEngineName);
	}

	@Override
	public BpmxEngine getEngine(String engineName) {
		logger.debug("获取BPMX引擎，名称为：" + engineName);

		if (BeanUtils.isNotEmpty(bpmxEngines)) {
			for (BpmxEngine bpmxEngine : this.bpmxEngines) {
				logger.debug("找到引擎：" + bpmxEngine.getName());
				if (bpmxEngine.getName().equals(engineName)) {
					return bpmxEngine;
				}
			}
		}

		logger.warn("无法找到名称为：" + engineName + "的引擎。");

		return null;
	}
	
	public void setDefaultEngineName(String defaultEngineName) {
		this.defaultEngineName = defaultEngineName;
	}

	public void setBpmxEngines(List<BpmxEngine> bpmxEngines) {
		this.bpmxEngines = bpmxEngines;
	}
	
	
	
}
