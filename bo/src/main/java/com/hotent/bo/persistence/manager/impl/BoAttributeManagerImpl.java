package com.hotent.bo.persistence.manager.impl;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.persistence.dao.BoAttributeDao;
import com.hotent.bo.persistence.dao.BoEntDao;
import com.hotent.bo.persistence.manager.BoAttributeManager;

/**
 * 业务实体定义属性 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
@Service("boAttributeManager")
public class BoAttributeManagerImpl extends BaseManagerImpl<BoAttributeDao, BoAttribute> implements BoAttributeManager {
	@Resource
	BoEntDao boEntDao;

	@Override
	public List<BoAttribute> getByEntId(String entId) {
		BoEnt ent=boEntDao.selectById(entId);
		List<BoAttribute> list= baseMapper.getByEntId(entId);
		for(BoAttribute attribute:list){
			attribute.setBoEnt(ent);
		}
		return list; 
	}
	

	@Override
	public List<BoAttribute> getByBoEnt(BoEnt boEnt) {
		List<BoAttribute> list = baseMapper.getByEntId(boEnt.getId());
		for(BoAttribute attribute:list){
			attribute.setBoEnt(boEnt);
		}
		return list;
	}

	@Override
	public void removeByEntId(String entId) {
		baseMapper.removeByEntId(entId);
	}

	@Override
	public void updateAttrStatus(String json) throws IOException {
		BoAttribute boAttribute = JsonUtil.toBean(json, BoAttribute.class);
		if(StringUtil.isNotEmpty(boAttribute.getId())){
			boAttribute.setStatus("hide");
			baseMapper.updateById(boAttribute);
		}
	}

	@Override
	public void recovery(String json) throws Exception {
		BoAttribute boAttribute = JsonUtil.toBean(json, BoAttribute.class);
		if(StringUtil.isNotEmpty(boAttribute.getId())){
			boAttribute.setStatus("show");
			baseMapper.updateById(boAttribute);
		}
	}
}
