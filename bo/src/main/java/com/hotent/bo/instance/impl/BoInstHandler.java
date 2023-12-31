package com.hotent.bo.instance.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.constant.BoConstants;
import com.hotent.bo.exception.BoBaseException;
import com.hotent.bo.instance.DataTransform;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoInst;
import com.hotent.bo.model.BoResult;
import com.hotent.bo.persistence.dao.BoInstDao;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.bo.util.BoUtil;
import org.springframework.stereotype.Service;

@Service
public class BoInstHandler extends AbstractBoDataHandler {
	@Resource
	BoInstDao boInstDao;
	@Resource
	DataTransform dataTransform;
	@Resource
	BoDefManager boDefManager;
	
	private void setId( BoData curData,boolean isAdd){
		if(isAdd){
			curData.set("uuid_", UUID.randomUUID().toString());
		}
		else{
			if(!curData.containKey("uuid_")){
				curData.set("uuid_", UUID.randomUUID().toString());
			}
		}
	}

	@Override
	public List<BoResult> save(String id, String defId, BoData curData) throws IOException {
		curData.removeByKey("initData");
		boolean isAdd=StringUtil.isEmpty(id);
		//设置唯一ID
		setId(curData,isAdd);
		Map<String, List<BoData>> map=curData.getSubMap();
		for (Map.Entry<String, List<BoData>> ent : map.entrySet()){
			List<BoData> list=ent.getValue();
			for(BoData bo:list){
				setId(bo,isAdd);
			}
		}
		
		String json = dataTransform.getByData(curData,false);
		
		BoInst inst=null;
		
		if(isAdd){
			String pk=UniqueIdUtil.getSuid();
			inst=new BoInst();
			inst.setId(pk);
			inst.setDefId(defId);
			inst.setInstData(json);
			inst.setCreateTime(LocalDateTime.now());
			boInstDao.insert(inst);
		}
		else{
			inst=boInstDao.selectById(id);
			inst.setInstData(json);
			boInstDao.updateById(inst);
		}
		
		List<BoResult> list=new ArrayList<BoResult>();
		BoResult result=new BoResult();
		
		String action=isAdd ? BoConstants.HANDLE_ADD:BoConstants.HANDLE_UPDATE;
		result.setAction(action);
		result.setPk(inst.getId());
		result.setBoEnt(curData.getBoEnt());
		
		
		list.add(result);
		if(curData.getBoDef()==null){
			BoDef boDef=boDefManager.getByDefId(defId);
			curData.setBoDef(boDef);
		}
		setBoDefAlias(list,curData.getBoDef().getAlias());
		
		return list;
	}

	@Override
	public BoData getById(Object id, String bodefCode) throws IOException {
		BoDef boDef= boDefManager.getByAlias(bodefCode);
		BoEnt boEnt= boDef.getBoEnt();
		BoInst inst=boInstDao.selectById((String)id);
		String json=inst.getInstData();
		JsonNode jsonObj = JsonUtil.toJsonNode(json);
		BoData data=BoUtil.transJSON(jsonObj);
		data.setBoDef(boDef);
		data.setBoEnt(boEnt);
		//设置初始化值。
		setInitData(data,boEnt);
		
		return data;
	}
	
	private void setInitData(BoData data, BoEnt boEnt){
		List<BoEnt> list=boEnt.getChildEntList();
		if(BeanUtils.isEmpty(list)) return;
		for(BoEnt ent : list){
			data.addInitDataMap(ent.getName(), ent.getInitData());
			setInitData(data, ent);
		}
	}
	

	@Override
	public BoData getResById(Object id, String bodefCode) throws IOException {
		return getById(id, bodefCode);
	}

	@Override
	public String saveType() {
		return BoConstants.SAVE_MODE_BOOBJECT;
	}

	@Override
	public void removeBoData(String boCode, String[] aryIds) {
		throw new BoBaseException("保存在通用实例表中的bo数据不支持通过ID删除数据.");
	}

	@Override
	public List<Map<String,Object>> getList(String boCode, Map<String, Object> param) {
		throw new BoBaseException("保存在通用实例表中的bo数据不支持参数查询.");
	}

	@Override
	public PageList<Map<String, Object>> getList(String boCode, QueryFilter queryFilter) {
		throw new BoBaseException("保存在通用实例表中的bo数据不支持参数及分页查询.");
	}
}
