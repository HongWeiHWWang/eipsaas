package com.hotent.bo.instance;

import java.io.IOException;

import com.hotent.bo.model.BoData;

/**
 * 数据转换接口
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface DataTransform {
	
	/**
	 * 将字符串转换成BoData对象。
	 * @param	data	字符串格式数据
	 * @return			bo数据
	 */
	BoData parse(String data) throws IOException;
	
	/**
	 * 根据boData获取字符串数据
	 * <pre>
	 * 字符串数据的格式可以是json或者 xml
	 * </pre>
	 * @param boData	bo数据
	 * @param needInit	是否需要初始化数据
	 * @return			字符串格式数据
	 */
	String getByData(BoData boData,boolean needInit) throws IOException;
}
