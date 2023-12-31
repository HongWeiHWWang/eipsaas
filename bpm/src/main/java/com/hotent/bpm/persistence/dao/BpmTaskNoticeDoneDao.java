package com.hotent.bpm.persistence.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.base.constants.SQLConst;
import com.hotent.bpm.persistence.model.BpmTaskNoticeDone;

/**
 * 
 * <pre> 
 * 描述：知会任务已办表 DAO接口
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-04-09 09:24:30
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface BpmTaskNoticeDoneDao extends BaseMapper<BpmTaskNoticeDone> {

    /**
     * 根据流程定义ID删除
     * @param defId
     */
    void delBpmTaskNoticeDoneByDefId(@Param("defId") String defId);

    /**
     * 根据知会待办主键ID删除
     * @param id
     */
    void delBpmTaskNoticeDoneById(@Param("id") String id);

    /**
     * 根据流程实例ID删除
     * @param instId
     */
    void delBpmTaskNoticeDoneByInstId(@Param("instId") String instId);

    /**
     * 获取已阅事项在各分类下的数量
     * @param wrapper
     * @return
     */
    List<Map<String,Object>> getNoticeDoneReadCount(@Param(Constants.WRAPPER) Wrapper<BpmTaskNoticeDone> wrapper);
}
