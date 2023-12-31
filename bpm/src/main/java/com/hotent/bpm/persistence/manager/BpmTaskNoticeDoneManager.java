package com.hotent.bpm.persistence.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.persistence.model.BpmTaskNoticeDone;

import java.util.List;
import java.util.Map;

/**
 * 
 * <pre> 
 * 描述：知会任务已办表 处理接口
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-04-09 09:24:30
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmTaskNoticeDoneManager extends BaseManager<BpmTaskNoticeDone>{

    /**
     * 根据流程定义ID删除
     * @param defId
     */
    void delBpmTaskNoticeDoneByDefId(String defId);

    /**
     * 根据知会待办主键ID删除
     * @param id
     */
    void delBpmTaskNoticeDoneById(String id);

    /**
     * 根据流程实例ID删除
     * @param instId
     */
    void delBpmTaskNoticeDoneByInstId(String instId);

    List<Map<String,Object>> getNoticeDoneReadCount(QueryFilter filter);
}
