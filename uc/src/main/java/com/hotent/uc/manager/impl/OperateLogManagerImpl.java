package com.hotent.uc.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotent.uc.dao.DemensionDao;
import com.hotent.uc.dao.OperateLogDao;
import com.hotent.uc.manager.OperateLogManager;
import com.hotent.uc.model.Demension;
import com.hotent.uc.model.OperateLog;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * 城市业务逻辑实现类
 *
 */
@Service
public class OperateLogManagerImpl extends BaseManagerImpl<OperateLogDao,OperateLog> implements OperateLogManager {

  

	@Override
    @Transactional
	public CommonResult<String> removeByIdStr(String ids) {
		if (BeanUtils.isNotEmpty(ids)) {
			String[]  idArray=ids.split(",");
			for (String id : idArray) {
				baseMapper.removePhysicalById(id);
			}
		}
		 return new CommonResult<String>(true, "删除日志成功！", "");
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}

}