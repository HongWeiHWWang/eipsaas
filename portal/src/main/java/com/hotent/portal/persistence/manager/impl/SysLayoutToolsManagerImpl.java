package com.hotent.portal.persistence.manager.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.portal.model.SysLayoutTools;
import com.hotent.portal.persistence.dao.SysIndexToolsDao;
import com.hotent.portal.persistence.dao.SysLayoutToolsDao;
import com.hotent.portal.persistence.manager.SysLayoutToolsManager;


/**
 * 布局工具设置 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@Service("sysLayoutToolsManager")
public class SysLayoutToolsManagerImpl extends BaseManagerImpl<SysLayoutToolsDao, SysLayoutTools> implements SysLayoutToolsManager{

	@Resource
	SysIndexToolsDao sysIndexToolsDao;
	
	@Override
	public SysLayoutTools getByLayoutID(String layoutId, String toolsType) {
		Map<String, Object> params = new HashMap<>();
		params.put("layoutId", layoutId);
		params.put("toolsType", toolsType);
		return baseMapper.getByLayoutID(params);
	}

//	@Override
//	public List<SysIndexTools> queryTools(String layoutId, String tools) {
//		List<SysIndexTools> sysIndexToolsList = new ArrayList<SysIndexTools>();
//		List<String> authorizeIdsByUserMap = bpmdefUserManager.getAuthorizeIdsByUserMap(SysIndexTools.INDEX_TOOLS);
//		SysLayoutTools sysLayoutTools = sysLayoutToolsDao.getByLayoutID(layoutId, tools);
//		if(sysLayoutTools != null){
//		String[] toolsArray = sysLayoutTools.getToolsIds().split(",");
//			for(String toolId : toolsArray){
//				if(authorizeIdsByUserMap.contains(toolId)){
//					SysIndexTools sysIndexTools = sysIndexToolsDao.get(toolId);
//					sysIndexToolsList.add(sysIndexTools);
//				}
//			}
//		}
//		return sysIndexToolsList;
//	}
}
