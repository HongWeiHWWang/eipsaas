package com.hotent.activemq.model;

import java.util.HashMap;
import java.util.Map;

import com.hotent.base.util.StringUtil;

/**
 * JMS消息
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年10月9日
 */
public class JmsTableTypeConf {

	private static  Map<String, JmsTableTypeFiledDetail> typeConf = new  HashMap<>();
	

	private JmsTableTypeConf() {
	}


	public static Map<String, JmsTableTypeFiledDetail> getTypeConf() {
		return typeConf;
	}

	public static void AddTypeConf(String typeKey,JmsTableTypeFiledDetail detail) {
		if (StringUtil.isNotEmpty(typeKey) && StringUtil.isNotEmpty(detail.getTableName())) {
			typeConf.put(typeKey.toUpperCase(), detail);
		}
	}
 
	
}
