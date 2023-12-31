package com.hotent.bpm.persistence.manager.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AuthenticationUtil;
import com.hotent.bpm.persistence.dao.BpmInterposeRecoredDao;
import com.hotent.bpm.persistence.manager.BpmInterposeRecoredManager;
import com.hotent.bpm.persistence.model.BpmInterposeRecored;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：流程干预记录表 处理实现类
 * 构建组：x7
 * 作者:heyifan
 * 邮箱:heyf@jee-soft.cn
 * 日期:2019-01-04 00:40:18
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("bpmInterposeRecoredManager")
public class BpmInterposeRecoredManagerImpl extends BaseManagerImpl<BpmInterposeRecoredDao, BpmInterposeRecored> implements BpmInterposeRecoredManager{

	
	@Override
    @Transactional
	public void create(BpmInterposeRecored recored){
		recored.setAuditor(AuthenticationUtil.getCurrentUserId());
		recored.setAuditorName(AuthenticationUtil.getCurrentUserFullname());
		recored.setCreateTime(LocalDateTime.now());
		recored.setCompleteTime(LocalDateTime.now());
		super.create(recored);
	}
}
