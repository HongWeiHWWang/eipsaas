package com.hotent.bpm.persistence.manager;

import java.util.List;
import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmExeStackRelation;

/**
 * 堆栈关系表 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author zhangxianwen
 * @email zhangxw@jee-soft.cn
 * @date 2018年6月28日
 */
public interface BpmExeStackRelationManager extends BaseManager<BpmExeStackRelation>{
	/**
	 * 根据堆栈ID获取关系记录
	 * @param stackId
	 *  是在ToStackId位置还是以FromStackId字段：to,from
	 * @return
	 */
     BpmExeStackRelation getByToStackId(String stackId);
     /**
 	 * 根据堆栈ID获取关系记录
 	 * @param isToOrFrom
 	 *  是在ToStackId位置还是以FromStackId字段：to,from
 	 * @return
 	 */
     BpmExeStackRelation getByFromStackId(String stackId);
     
	 List<BpmExeStackRelation> getListByProcInstId(String procInstId);
	 
	 /**
	 * 删除bpm_exe_stack_his 历史数据
	 * @param instId
	 */
	void removeHisByInstId(String procInstId);

    BpmExeStackRelation getById(String instId,String fromId, String toId);
}
