package com.hotent.bpm.persistence.manager.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.persistence.dao.BpmReportListDao;
import com.hotent.bpm.persistence.manager.BpmReportActManager;
import com.hotent.bpm.persistence.manager.BpmReportListManager;
import com.hotent.bpm.persistence.model.BpmReportList;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author tangxin
 *
 */
@Service("bpmReportListManager")
public class BpmReportListManagerImpl extends BaseManagerImpl<BpmReportListDao, BpmReportList> implements BpmReportListManager{
	

	@Resource
	BpmReportActManager bpmReportActManager;
	

	
	@Override
    @Transactional
	public void removeByIds(String... ids){
		for (String id : ids) {
			this.remove(id);
		}
	}

    @Transactional
	public void remove(String id){
		BpmReportList reportList =  super.get(id);
		if(BeanUtils.isNotEmpty(reportList)){
			bpmReportActManager.removeByReportId(id);
		}
		super.remove(id);
	}
	
	
}	
