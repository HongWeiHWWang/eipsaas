package com.hotent.bo.instance;

import java.util.List;
import java.util.Map;

import com.hotent.bo.model.BoEnt;

/**
 * 获取子表数据接口
 * <pre>
 * 获取子表数据有可能会根据当前登录用户信息获取相应的数据
 * </pre>
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface BoSubDataHandler {
	
	/**
	 * 根据外键获取子表数据
	 * 
	 * @param boEnt		bo实体
	 * @param fkValue	外键值
	 * @return			子表数据列表
	 */
	List<Map<String,Object>> getSubDataByFk(BoEnt boEnt, Object fkValue);
}
