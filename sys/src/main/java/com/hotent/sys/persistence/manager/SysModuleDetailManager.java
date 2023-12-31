package com.hotent.sys.persistence.manager;

import java.io.Serializable;
import java.util.List;

import com.hotent.base.manager.BaseManager;
import com.hotent.sys.persistence.model.SysModuleDetail;

public interface SysModuleDetailManager extends BaseManager<SysModuleDetail> {
    /**
     * 根据模块主表id获取明细
     * @param moduleId
     * @param type
     * @return
     */
    List<SysModuleDetail> getModuleDetail(String moduleId, String type);

    /**
     * 根据模块id删除明细
     * @param moduleId
     */
    void removeByModuleId(Serializable moduleId);
}
