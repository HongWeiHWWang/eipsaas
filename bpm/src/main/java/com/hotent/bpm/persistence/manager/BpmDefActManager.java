package com.hotent.bpm.persistence.manager;


import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.bpm.persistence.model.BpmDefAct;






/**
 * 对象功能:流程定义权限明细 Manager类
 * 开发公司:广州宏天软件有限公司
 * 开发人员:xucx
 * 创建时间:2014-03-05 14:10:50
 */

public interface BpmDefActManager extends BaseManager<BpmDefAct>{
	
	public  List<BpmDefAct> getByAuthorizeId(String authorizeId);

	public void delByAuthorizeId(String authorizeId);

	public List<BpmDefAct> getActRightByUserMap(Map<String, String> userRightMapStr, String authorizeType,
			String defKey);

	public List<BpmDefAct> getAll(Map<String, Object> params);
}
