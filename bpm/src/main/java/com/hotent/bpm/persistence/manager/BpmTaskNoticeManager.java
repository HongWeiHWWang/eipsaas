package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.persistence.model.BpmTaskNotice;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * <pre> 
 * 描述：知会任务表 处理接口
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-03-15 11:35:17
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmTaskNoticeManager extends BaseManager<BpmTaskNotice>{

    /**
     * 根据流程定义ID删除
     * @param defId
     */
    void delBpmTaskNoticeByDefId(String defId);

    /**
     * 根据流程实例ID删除
     * @param instId
     */
    void delBpmTaskNoticeByInstId(String instId);

    /**
     * 根据流程任务ID查询传阅任务
     * @param taskId
     */
    List<BpmTaskNotice> getBpmTaskNoticeByTaskId(String taskId);

    /**
     * 根据流程实例ID查询传阅任务
     * @param instId
     */
    List<BpmTaskNotice> getBpmTaskNoticeByInstId(@Param("instId") String instId);

    /**
     * 获取待阅在各分类下的数量
     */
    List<Map<String,Object>> getNoticeTodoReadCount(QueryFilter filter);

	void updateOwner(Map<String, Object> ownerMap);

	void updateAssignee(Map<String, Object> assigneeMap);
}
