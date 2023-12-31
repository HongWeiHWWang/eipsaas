package com.hotent.bpm;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hotent.bpm.api.engine.BpmxEngineFactory;

/**
 * <pre> 
 * 描述：BPMX
 * 构建组：x5-bpmx-core
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-27-下午6:06:07
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class BpmxCoreConfig {
	private final static Log logger = LogFactory.getLog(BpmxCoreConfig.class);

	private static BpmxCoreConfig instance;

	private ApplicationContext cxt;

	private static String[] springContextXml = { "classpath:conf/x5-bpmx-core.xml" };

	private BpmxCoreConfig() {
		logger.info("Welcome to bpmx core !");
	}

	public static BpmxCoreConfig getInstance() {
		return BpmxCoreConfig.getInstance(springContextXml);
	}

	public static BpmxCoreConfig getInstance(String[] springContext) {

		if (instance == null) {
			logger.debug("BpmxCoreConfig对象还未创建，执行初始化操作。");

			instance = new BpmxCoreConfig();

			// 载入Spring
			ApplicationContext cxt = new ClassPathXmlApplicationContext(springContext);

			instance.setCxt(cxt);
		}

		return instance;
	}

	public ApplicationContext getCxt() {
		return cxt;
	}

	public void setCxt(ApplicationContext cxt) {
		this.cxt = cxt;
	}

	public <T> T getBean(Class<T> beanClass) {
		return this.cxt.getBean(beanClass);
	}

	public BpmxEngineFactory getBpmxEngineFactory() {
		return this.getBean(BpmxEngineFactory.class);
	}

}
