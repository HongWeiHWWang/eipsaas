package com.hotent.runtime.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.runtime.dao.BpmTaskTransRecordDao;
import com.hotent.runtime.manager.BpmTaskTransRecordManager;
import com.hotent.runtime.model.BpmTaskTransRecord;

/**
 * 
 * <pre> 
 * 描述：bpm_task_trans_record 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-04 16:12:29
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmTaskTransRecordManager")
public class BpmTaskTransRecordManagerImpl extends BaseManagerImpl<BpmTaskTransRecordDao, BpmTaskTransRecord> implements BpmTaskTransRecordManager{
	
	@Override
	public BpmTaskTransRecord getByTaskId(String taskId) {
		return baseMapper.getByTaskId(taskId);
	}
	@Override
	public PageList<BpmTaskTransRecord> getMyTransRecord(String userId,QueryFilter queryFilter) {
		queryFilter.withParam("userId", userId);
		PageBean pageBean = queryFilter.getPageBean();
    	IPage<BpmTaskTransRecord> page = new Page<BpmTaskTransRecord>(0, Integer.MAX_VALUE);
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
    	Class<BpmTaskTransRecord> currentModelClass = currentModelClass();
    	Wrapper<BpmTaskTransRecord> convert2Wrapper = convert2Wrapper(queryFilter, currentModelClass);
    	List<BpmTaskTransRecord> transRecords = baseMapper.getTransRecord(page,convert2Wrapper);
		return new PageList<BpmTaskTransRecord>(transRecords);
	}
	@Override
	public List<BpmTaskTransRecord> getTransRecordList(QueryFilter queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
    	IPage<BpmTaskTransRecord> page = new Page<BpmTaskTransRecord>(0, Integer.MAX_VALUE);
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
    	Class<BpmTaskTransRecord> currentModelClass = currentModelClass();
    	Wrapper<BpmTaskTransRecord> convert2Wrapper = convert2Wrapper(queryFilter, currentModelClass);
    	return baseMapper.getTransRecord(page,convert2Wrapper);
	}
}
