package com.hotent.bpm.persistence.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.Direction;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.persistence.dao.BpmTaskNoticeDao;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.model.BpmTaskNotice;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：知会任务表 处理实现类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-03-15 11:35:17
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmTaskNoticeManager")
public class BpmTaskNoticeManagerImpl extends BaseManagerImpl<BpmTaskNoticeDao, BpmTaskNotice> implements BpmTaskNoticeManager{

    @Override
    @Transactional
    public void delBpmTaskNoticeByDefId(String defId) {
        baseMapper.delBpmTaskNoticeByDefId(defId);
    }

    @Override
    @Transactional
    public void delBpmTaskNoticeByInstId(String instId) {
    	baseMapper.delBpmTaskNoticeByInstId(instId);
    }

    @Override
    public List<BpmTaskNotice> getBpmTaskNoticeByTaskId(String taskId) {
        return  baseMapper.getBpmTaskNoticeByTaskId(taskId);
    }

    @Override
    public List<BpmTaskNotice> getBpmTaskNoticeByInstId(String instId) {
        return baseMapper.getBpmTaskNoticeByInstId(instId);	
    }

    @Override
    public List<Map<String, Object>> getNoticeTodoReadCount(QueryFilter filter) {
        return baseMapper.getNoticeTodoReadCount(convert2Wrapper(filter, currentModelClass()));
    }

	@Override
    @Transactional
	public void updateOwner(Map<String, Object> ownerMap) {
		baseMapper.updateOwner(ownerMap);
	}

	@Override
    @Transactional
	public void updateAssignee(Map<String, Object> assigneeMap) {
		baseMapper.updateAssignee(assigneeMap);
	}
	
	@Override
	public PageList<BpmTaskNotice> query(QueryFilter<BpmTaskNotice> queryFilter) {
		queryFilter.setDefaultSort("ID_", Direction.DESC);
		return new PageList<>(baseMapper.customQuery(convert2IPage(queryFilter.getPageBean()),convert2Wrapper(queryFilter, currentModelClass())));
	}
}
