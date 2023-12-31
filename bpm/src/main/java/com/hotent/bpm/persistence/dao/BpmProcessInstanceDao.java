package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.base.constants.SQLConst;
import com.hotent.bpm.api.model.process.inst.BpmProcessInstance;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.uc.api.model.IGroup;


public interface BpmProcessInstanceDao extends BaseMapper<DefaultBpmProcessInstance> {
	
	/**
	 * 添加流程实例。
	 * @param processInstance 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void createHistory(DefaultBpmProcessInstance processInstance);
	
	
	/**
	 * 更新流程实例。
	 * @param processInstance 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	void updateHistory(DefaultBpmProcessInstance processInstance);

    /**
     * 根据流程定义获取流程实例列表。 （测试实例数据）
     * @param bpmnDefKey		流程定义ID
     * @return
     * List&lt;DefaultBpmProcessInstance>
     */
    List<DefaultBpmProcessInstance> getTestListByBpmnDefKey(@Param("defKey") String bpmnDefKey);

	DefaultBpmProcessInstance getBpmProcessInstanceHistory(@Param("procInstId") String procInstId);
	DefaultBpmProcessInstance getBpmProcessInstanceHistoryByBpmnInstId(@Param("bpmnInstId") String bpmnInstId);

	/**
	 * 根据activiti实例查询流程运行实例。
	 * @param bpmnInstId
	 * @return 
	 * DefaultBpmProcessInstance
	 * @exception 
	 * @since  1.0.0
	 */
	DefaultBpmProcessInstance getBpmnInstId(@Param("bpmnInstId") String bpmnInstId);
	
	IPage<DefaultBpmProcessInstance> getByUserId(IPage<DefaultBpmProcessInstance> page,@Param(SQLConst.QUERY_FILTER) Map<String,Object> params);
	
	void updateStatusByInstanceId(@Param("processInstanceId") String processInstanceId,@Param("status") String status);
	
	void updateStatusByBpmnInstanceId(@Param("processInstanceId") String processInstanceId,@Param("status") String status);
	/**
	 * 按条件查找实例
	 * @param userId
	 * @param groupList
	 * @return
	 */
	IPage<DefaultBpmProcessInstance> getByUserIdGroupList(IPage<DefaultBpmProcessInstance> page,@Param("userId") String userId,@Param("groupList") List<IGroup> groupList);
	/**
	 * 按人员查找其参与的流程实例并分页返回结果
	 * @param usreId
	 * @return
	 * List<DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getByAttendUserId(IPage<DefaultBpmProcessInstance> page,@Param("userId") String usreId);
	
	
	/**
	 * 根据流程实例获取流程实例ID列表。
	 * @param instList
	 * @return 
	 * List&lt;String>
	 */
	List<String> getBpmnByInstList(@Param("list") List<String> instList);
	
	/**
	 * 根据流程定义获取流程实例列表。 
	 * @param bpmnDefKey		流程定义key
	 * @return 
	 * List&lt;DefaultBpmProcessInstance>
	 */
	List<DefaultBpmProcessInstance> getListByBpmnDefKey(@Param("defKey") String bpmnDefKey);

	/**
	 * 获取我的请求在各分类下的数量
	 * @return
	 */
	List<Map<String,Object>> getMyRequestCount(Map<String,Object> param);
	/**
	 * 获取我的请求的数量
	 * @return
	 */
	Long getMyRequestCountByUserId(@Param("userId") String userId);
	
	/**
	 * 获取我的办结。
	 * @param params
	 * @return  List&lt;DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getMyCompletedByUserId(IPage<DefaultBpmProcessInstance> page,@Param(SQLConst.QUERY_FILTER) Map<String,Object> params);
	/**
	 *  获取我发起的草稿。
	 * @param params
	 * @return  List&lt;DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getDraftsByUserId(IPage<DefaultBpmProcessInstance> page,@Param(SQLConst.QUERY_FILTER) Map<String,Object> params);
	
	/**
	 *  获取已办事宜。
	 * @param params
	 * @return  List&lt;DefaultBpmProcessInstance>
	 */
	IPage<Map<String,Object>> getHandledByUserId(IPage<DefaultBpmProcessInstance> page,@Param(Constants.WRAPPER) Wrapper<DefaultBpmProcessInstance> wrapper);

	/**
	 *  获取办结事宜。
	 * @param wrapper
	 * @return  List&lt;DefaultBpmProcessInstance>
	 */
	IPage<DefaultBpmProcessInstance> getCompletedByUserId(IPage<DefaultBpmProcessInstance> page,@Param(Constants.WRAPPER) Wrapper<DefaultBpmProcessInstance> wrapper);
	
	/**
	 * 更新流程实例是否禁止。
	 * @param defKey
	 * @param isForbidden 
	 * void
	 */
	void updForbiddenByDefKey(@Param("defKey")String defKey,@Param("isForbidden") Integer isForbidden);
	
	/**
	 * 根据流程实例ID更新流程实例是否禁止。
	 * @param instId
	 * @param isForbidden 
	 * void
	 */
	void updForbiddenByInstId(@Param("id")String instId,@Param("isForbidden") Integer isForbidden);
	
	/**
	 * 根据流程键和是否正式获取流程实例。
	 * @param defKey
	 * @param formal
	 * @return 
	 * List&lt;DefaultBpmProcessInstance>
	 */
	List<DefaultBpmProcessInstance> getByDefKeyFormal(@Param("defKey") String defKey,@Param("formal") String formal);
	
	
	/**
	 * 根据父实例ID获取流程实例列表。
	 * @param parentInstId
	 * @return 
	 * List&lt;DefaultBpmProcessInstance>
	 */
	List<DefaultBpmProcessInstance> getByParentId(@Param("parentInstId") String parentInstId);
	
	
	/**
	 * 根据流程定义ID
	 * @param procDefId
	 * @return 
	 * List&lt;DefaultBpmProcessInstance>
	 */
	List<DefaultBpmProcessInstance> getListByDefId(@Param("procDefId") String procDefId);
 
	/**
	 * 根据父流程实例ID和节点定义ID查子流程实例ＩＤ
	 * @Title: getBpmnByParentIdAndSuperNodeId 
	 * @Description: TODO
	 * @param parentInstId
	 * @param superNodeId
	 * @return
	 * @return: BpmProcessInstance
	 */
	List<BpmProcessInstance> getBpmnByParentIdAndSuperNodeId(@Param("parentInstId") String parentInstId,@Param("superNodeId") String superNodeId);
	
	List<BpmProcessInstance> getHiBpmnByParentIdAndSuperNodeId(@Param("parentInstId") String parentInstId,@Param("superNodeId") String superNodeId);
	/**
	 * 根据业务主键获取流程实例。
	 * @param businessKey
	 * @return
	 */
	DefaultBpmProcessInstance getByBusinessKey(@Param("businessKey") String businessKey);


	IPage<DefaultBpmProcessInstance> getMyHandledMeeting(IPage<DefaultBpmProcessInstance> page,@Param(SQLConst.QUERY_FILTER) Map<String, Object> params);

    /**
     * 根据用户ID查询用户流程
     * @param map
     * @return
     */
	IPage<DefaultBpmProcessInstance> queryByuserId(IPage<DefaultBpmProcessInstance> page,@Param(Constants.WRAPPER) Wrapper<DefaultBpmProcessInstance> wrapper);

    /**
     * 根据流程实例ID获取流程实例信息
     * @param params
     * @return
     */
    IPage<DefaultBpmProcessInstance> getById(IPage<DefaultBpmProcessInstance> page,@Param(Constants.WRAPPER) Wrapper<DefaultBpmProcessInstance> wrapper);

    /**
     * 根据流程实例ID物理删除流程实例记录数据
     * @param instId
     * @param isDele
     */
    void isDeleInst(@Param("instId") String instId,@Param("isDele") Integer isDele);

    /**
     * 根据流程实例ID物理删除审批记录数据
     * @param instId,isDele
     */
    void isDeleOpinion(@Param("instId") String instId,@Param("isDele") Integer isDele);

    /**
     * 根据流程实例ID物理删除知会待办记录数据
     * @param instId,isDele
     */
    void isDeleNotice(@Param("instId") String instId,@Param("isDele") Integer isDele);

    /**
     * 根据流程实例ID物理删除待办记录数据
     * @param instId,isDele
     */
    void isDeleTask(@Param("instId") String instId,@Param("isDele") Integer isDele);

    /**
     * 根据流程实例ID获取任务ID
     * @param instId
     * @return
     */
    List<String> getBpmTaskIdByInstId(@Param("instId") String instId);

    /**
     * 根据流程实例ID删除知会待办记录数据
     * @param instId
     */
    void deleteNotice(@Param("instId") String instId);

    /**
     * 根据流程实例ID查询最新的一条审批记录任务ID
     * @param instId
     */
    List<String> getNodeIdByInstId(@Param("instId") String instId);
    
    List<Map<String,Object>> getFlowFieldList(IPage<DefaultBpmProcessInstance> page,@Param(Constants.WRAPPER) Wrapper<DefaultBpmProcessInstance> wrapper);


    IPage<Map<String, Object>> getDoneInstList(IPage<DefaultBpmProcessInstance> page,@Param(Constants.WRAPPER) Wrapper<DefaultBpmProcessInstance> wrapper);

	/**
	 * 获取完成待办各分类下数量
	 * @param params
	 * @return
	 */
    List<Map<String,Object>> getDoneInstCount(@Param(Constants.WRAPPER) Wrapper<DefaultBpmProcessInstance> wrapper);
    /**
     * 获取用户完成待办的数量
     * @param userId
     * @return
     */
    Long getDoneInstCountByUserId(@Param("userId") String userId);
    
    List<DefaultBpmProcessInstance> getListByRightMap(@Param("userRightMap") Map<String, String> userRightMap);

}
