package com.hotent.bo.bodef;

import java.util.List;

import com.hotent.bo.model.BoEnt;

/**
 * bo实体处理类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
public interface IBoEntHandler {

	/**
	 * 处理bo实体的修改
	 * @param boEnt			bo实体
	 * @param removeList	要移除的数据列表
	 * @param addList		要添加的数据列表
	 */
	void handlerEntChange(BoEnt boEnt,List<?> removeList,List<?> addList);
}
