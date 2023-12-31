package com.hotent.tools.sqlgen.util;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * 解析XMl的帮助类
 * <pre>
 * 构建组：x6-tools
 * 作者：hugh
 * 邮箱:zhuangxh@jee-soft.cn
 * 日期:2014-9-26-上午10：09:54
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 * 
 */
public class XmlUtil {
	/**
	 * 解析XML字符串
	 * 
	 * @param cls
	 * @param xmlIs
	 * @return
	 * @throws JAXBException
	 */
	public static Object unmarshall(Class<?> cls, InputStream xmlIs)
			throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(cls);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		return unmarshaller.unmarshal(xmlIs);
	}
}
