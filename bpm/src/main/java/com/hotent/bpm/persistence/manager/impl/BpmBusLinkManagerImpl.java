package com.hotent.bpm.persistence.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.hotent.base.feign.FormFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.bpm.persistence.dao.BpmBusLinkDao;
import com.hotent.bpm.persistence.manager.BpmBusLinkManager;
import com.hotent.bpm.persistence.model.BpmBusLink;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmBusLinkManager")
public class BpmBusLinkManagerImpl extends BaseManagerImpl<BpmBusLinkDao, BpmBusLink> implements BpmBusLinkManager {
	@Resource
	BpmBusLinkDao bpmBusLinkDao;
	@Resource
	JdbcTemplate jdbcTemplate;


	@Override
	public BpmBusLink getByBusinesKey(String businessKey, String formIdentity, boolean isNumber) {
		return bpmBusLinkDao.getByBusinesKey(getParams(businessKey, formIdentity, isNumber));

	}

    @Override
    @Transactional
    public void removeDataByInstId(String instId) throws Exception {
        // 删除bpm_bus_link和对应的表数据
        List<BpmBusLink> links = baseMapper.getAllByInstId(instId);
        if(BeanUtils.isNotEmpty(links)){
            FormFeignService boDefService = AppUtil.getBean(FormFeignService.class);
            boDefService.removeDataByBusLink(JsonUtil.toJsonNode(links));
        }
        for (BpmBusLink link : links) {
            // 删除Bpm_bus_link数据
            remove(link.getId());
        }
    }

	@Override
    @Transactional
	public void delByBusinesKey(String businessKey, String formIdentity, boolean isNumber) {
		bpmBusLinkDao.delByBusinesKey(getParams(businessKey, formIdentity, isNumber));
	}

	@Override
	public List<BpmBusLink> getByInstId(String instId) {
		return bpmBusLinkDao.getByInstId(instId);
	}

	@Override
	public BpmBusLink getByBusinesKey(String businessKey, boolean isNumber) {
		return bpmBusLinkDao.getByBusinesKey(getParams(businessKey, null, isNumber));
	}

	@Override
    @Transactional
	public void create(BpmBusLink entity) {
		super.create(entity);
	}

	@Override
    @Transactional
	public void removeDataByDefId(String defId) throws Exception {
		// 删除bpm_bus_link和对应的表数据
		List<BpmBusLink> links = getByDefId(defId);
		if(BeanUtils.isNotEmpty(links)){
			FormFeignService boDefService = AppUtil.getBean(FormFeignService.class);
			boDefService.removeDataByBusLink(JsonUtil.toJsonNode(links));
		}
		for (BpmBusLink link : links) {
			// 删除Bpm_bus_link数据
			remove(link.getId());
		}
	}

	@Override
	public List<BpmBusLink> getByDefId(String defId) {
		return bpmBusLinkDao.getByDefId(defId);
	}

	@Override
	public Map<String, BpmBusLink> getMapByInstId(String instId) {
		Map<String, BpmBusLink> map = new HashMap<String, BpmBusLink>();
		List<BpmBusLink> list = this.getByInstId(instId);
		for (BpmBusLink busLink : list) {
			map.put(busLink.getBoDefCode(), busLink);
		}
		return map;
	}
	
	private Map<String, Object> getParams(String businessKey, String formIdentity,
			boolean isNumber){
		Map<String, Object> params=new HashMap<String, Object>();
		
		if(isNumber){
			params.put("businessKey", Long.parseLong(businessKey));
		}
		else{
			params.put("businessKey", businessKey);
		}
		
		if(StringUtil.isNotEmpty(formIdentity)){
			params.put("formIdentity", formIdentity);
		}
		
		params.put("isNumber", isNumber);
		
		return params;
	}

}
