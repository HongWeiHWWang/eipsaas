package com.hotent.bpm.persistence.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.persistence.dao.BpmTaskNoticeDoneDao;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeDoneManager;
import com.hotent.bpm.persistence.model.BpmTaskNoticeDone;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：知会任务已办表 处理实现类
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-04-09 09:24:30
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmTaskNoticeDoneManager")
public class BpmTaskNoticeDoneManagerImpl extends BaseManagerImpl<BpmTaskNoticeDoneDao, BpmTaskNoticeDone> implements BpmTaskNoticeDoneManager {

    @Override
    @Transactional
    public void delBpmTaskNoticeDoneByDefId(String defId) {
        baseMapper.delBpmTaskNoticeDoneByDefId(defId);
    }

    @Override
    @Transactional
    public void delBpmTaskNoticeDoneById(String id) {
    	baseMapper.delBpmTaskNoticeDoneById(id);
    }

    @Override
    @Transactional
    public void delBpmTaskNoticeDoneByInstId(String instId) {	
    	baseMapper.delBpmTaskNoticeDoneByInstId(instId);
    }

    @Override
    public List<Map<String, Object>> getNoticeDoneReadCount(QueryFilter filter) {
        return baseMapper.getNoticeDoneReadCount(convert2Wrapper(filter, currentModelClass()));
    }
}
