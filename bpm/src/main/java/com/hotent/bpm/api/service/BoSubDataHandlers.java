package com.hotent.bpm.api.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.model.CommonResult;

import java.util.List;
import java.util.Map;

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
public interface BoSubDataHandlers {

    /**
     * 根据外键获取子表数据sql
     *
     * @param boEnt		bo实体
     * @param fkValue	外键值
     * @return			子表数据列表
     */
    CommonResult<String> getSubDataSqlByFk(ObjectNode boEnt, Object fkValue,String defId,String nodeId,String parentDefKey)throws Exception;
}
