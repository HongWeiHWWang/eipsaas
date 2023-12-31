package com.hotent.bo.instance;

/**
 * 获取BoDataHandler工厂类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoInstanceFactory {
	
	/**
	 * 获取bo数据处理器
	 * <pre>
	 * 根据存储类型获取对应的BoDataHandler
	 * </pre>
	 * @param saveType	数据处理方式
	 * @return			bo实例数据处理器
	 */
	BoDataHandler getBySaveType(String saveType);

}
