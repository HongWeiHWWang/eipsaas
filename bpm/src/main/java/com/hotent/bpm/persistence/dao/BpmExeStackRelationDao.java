package com.hotent.bpm.persistence.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.bpm.persistence.model.BpmExeStackRelation;

/**
 * 
 * <pre> 
 * 描述：堆栈关系表 DAO接口
 * 构建组：x5-bpmx-platform
 * 作者:hugh
 * 邮箱:zxh@jee-soft.cn
 * 日期:2015-06-17 13:56:55
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmExeStackRelationDao extends BaseMapper<BpmExeStackRelation> {
	/**
	 * 根据堆栈ID获取关系记录
	 * @param stackId
	 *  是在ToStackId位置还是以FromStackId字段：to,from
	 * @return
	 */
     BpmExeStackRelation getByToStackId(@Param("stackId") String stackId);
     /**
 	 * 根据堆栈ID获取关系记录
 	 * @param isToOrFrom
 	 *  是在ToStackId位置还是以FromStackId字段：to,from
 	 * @return
 	 */
     BpmExeStackRelation getByFromStackId(@Param("stackId") String stackId);
     
	 List<BpmExeStackRelation> getListByProcInstId(String procInstId);
	 
	 /**
	 * 删除bpm_exe_stack_his 历史数据
	 * @param instId
	 */
	void removeHisByInstId(@Param("procInstId") String procInstId);

    BpmExeStackRelation getById(@Param("instId") String instId,@Param("fromId") String fromId,@Param("toId") String toId);
}
