package com.hotent.file.util;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.hotent.file.extend.ControlDocumentFormatRegistry;

/**
 * 创建文件转换器
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月15日
 */
@Component
public class ConverterUtils {

	@Value("${file.office.home}")
	String officeHome;
	OfficeManager officeManager;

	@PostConstruct
	public void initOfficeManager() {
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		File file=new File(officeHome);
		if(file.exists()){
			System.out.println(officeHome);
			configuration.setOfficeHome(officeHome);
			officeManager = configuration.buildOfficeManager();
			officeManager.start();
		}
	}

	public OfficeDocumentConverter getDocumentConverter() {
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager, new ControlDocumentFormatRegistry());

		converter.setDefaultLoadProperties(getLoadProperties());
		return converter;
	}

	private Map<String,?> getLoadProperties() {
		Map<String,Object> loadProperties = new HashMap<>(10);
		loadProperties.put("Hidden", true);
		loadProperties.put("ReadOnly", true);

		loadProperties.put("CharacterSet", Charset.forName("UTF-8").name());
		return loadProperties;
	}

	@PreDestroy
	public void destroyOfficeManager(){
		if (null != officeManager && officeManager.isRunning()) {
			officeManager.stop();
		}
	}
}
