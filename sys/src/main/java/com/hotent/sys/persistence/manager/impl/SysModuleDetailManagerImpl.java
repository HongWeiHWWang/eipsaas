package com.hotent.sys.persistence.manager.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.sys.persistence.dao.SysModuleDetailDao;
import com.hotent.sys.persistence.manager.SysModuleDetailManager;
import com.hotent.sys.persistence.model.SysModuleDetail;

/**
 *
 * <pre>
 * 描述：SYS_MODULE_DETAIL 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2019-12-04 10:58:10
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("sysModuleDetailManager")
public class SysModuleDetailManagerImpl extends BaseManagerImpl<SysModuleDetailDao, SysModuleDetail> implements SysModuleDetailManager {
    @Override
    public List<SysModuleDetail> getModuleDetail(String moduleId, String type) {
        return baseMapper.getModuleDetail(moduleId, type);
    }
    @Override
    public void removeByModuleId(Serializable moduleId) {
    	baseMapper.removeByModuleId(moduleId);
    }
}
