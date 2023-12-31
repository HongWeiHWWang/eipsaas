package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.base.constants.SQLConst;
import com.hotent.bpm.persistence.model.BpmTaskNotice;

/**
 * 
 * <pre> 
 * 描述：知会任务表 DAO接口
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-03-15 11:35:17
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmTaskNoticeDao extends BaseMapper<BpmTaskNotice> {

    /**
     * 根据流程定义ID删除
     * @param defId
     */
    void delBpmTaskNoticeByDefId(@Param("defId") String defId);

    /**
     * 根据流程实例ID删除
     * @param instId
     */
    void delBpmTaskNoticeByInstId(@Param("instId") String instId);

    /**
     * 根据流程任务ID查询传阅任务
     * @param taskId
     */
    List<BpmTaskNotice> getBpmTaskNoticeByTaskId(@Param("taskId") String taskId);

    /**
     * 根据流程实例ID查询传阅任务
     * @param instId
     */
    List<BpmTaskNotice> getBpmTaskNoticeByInstId(@Param("instId") String instId);

	void updateOwner(Map<String, Object> ownerMap);

	void updateAssignee(Map<String, Object> assigneeMap);

    /**
     * 获取待阅在各分类下的数量
     * @return
     */
	List<Map<String,Object>> getNoticeTodoReadCount(@Param(Constants.WRAPPER) Wrapper<BpmTaskNotice> wrapper);

	IPage<BpmTaskNotice> customQuery(IPage<BpmTaskNotice> convert2iPage, @Param(Constants.WRAPPER) Wrapper<BpmTaskNotice> wrapper);
}
