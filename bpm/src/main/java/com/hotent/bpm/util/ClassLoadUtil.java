package com.hotent.bpm.util;

import javax.xml.transform.TransformerFactoryConfigurationError;

/**
 * 从动态链接库中获取相应的类
 * @author heyifan
 *
 */
public class ClassLoadUtil {
	/**
	 * 根据流程的key，流程名称和流程设计器的xml通过转换，转换成流程activiti的XML。
	 * 
	 * @param id
	 *            流程key。
	 * @param name
	 *            流程名称。
	 * @param xml
	 *            设计器输出的流程定义xml。
	 * @return 返回转换后的xml。
	 * @throws TransformerFactoryConfigurationError
	 * @throws Exception
	 */
	public static String transform(String id, String name, String xml)
			throws Exception {
		return BpmTransUtil.transform(id, name, xml);
	}
}