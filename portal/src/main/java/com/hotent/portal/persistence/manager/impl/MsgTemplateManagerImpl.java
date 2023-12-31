package com.hotent.portal.persistence.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.portal.model.MsgTemplate;
import com.hotent.portal.persistence.dao.MsgTemplateDao;
import com.hotent.portal.persistence.manager.MsgTemplateManager;

/**
 * 对象功能:消息模版 Manager
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 */
@Service("msgTemplateManager")
public class MsgTemplateManagerImpl extends BaseManagerImpl<MsgTemplateDao, MsgTemplate> implements MsgTemplateManager {

	@Override
	public MsgTemplate getByKey(String templateKey){
		return baseMapper.getByKey(templateKey);
	}

	@Override
	public MsgTemplate getDefault(String typeKey){
		return baseMapper.getDefault(typeKey);
	}

	@Override
	public void setDefault(String id) {
		MsgTemplate MsgTemplate = this.get(id);
		String typeKey = MsgTemplate.getTypeKey();
		baseMapper.setNotDefaultByType(typeKey);
		baseMapper.setDefault(id);
	}

	@Override
	public boolean isExistByKeyAndTypeKey(String key, String typeKey) {
		return baseMapper.isExistByKeyAndTypeKey(key,typeKey);
	}

	@Override
	public void setNotDefault(String id) {
		baseMapper.setNotDefaultById(id);
	}
}
