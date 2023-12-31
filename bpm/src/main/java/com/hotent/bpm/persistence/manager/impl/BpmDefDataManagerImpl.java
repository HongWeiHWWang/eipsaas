package com.hotent.bpm.persistence.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.BpmDefDataDao;
import com.hotent.bpm.persistence.manager.BpmDefDataManager;
import com.hotent.bpm.persistence.model.BpmDefData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：流程自动发起配置表 处理实现类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-04-07 10:51:28
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service
public class BpmDefDataManagerImpl extends BaseManagerImpl<BpmDefDataDao, BpmDefData> implements BpmDefDataManager{

	@Override
    @Transactional
	public void delByDefKey(String defKey) {
		baseMapper.delByDefKey(defKey);
	}

}
