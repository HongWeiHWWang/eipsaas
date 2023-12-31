package com.hotent.Calendar.manager.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.Calendar.dao.CalendarAssignDao;
import com.hotent.Calendar.dao.CalendarDao;
import com.hotent.Calendar.dao.CalendarSettingDao;
import com.hotent.Calendar.manager.CalendarAssignManager;
import com.hotent.Calendar.manager.CalendarSettingManager;
import com.hotent.Calendar.model.Calendar;
import com.hotent.Calendar.model.CalendarAssign;
import com.hotent.Calendar.model.CalendarSetting;
import com.hotent.Calendar.model.CalendarShiftPeroid;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;

@Service("calendarAssignManager")
public class CalendarAssignManagerImpl extends BaseManagerImpl<CalendarAssignDao, CalendarAssign> implements CalendarAssignManager{
	@Resource
	CalendarDao calendarDao;
	@Resource
	CalendarSettingManager calendarSettingManager;
	@Resource
	CalendarSettingDao calendarSettingDao;
	 
	
	/**
	 * 取日历设置。
	 * <pre>
	 * 	1.根据个人获取日历。
	 *  2.没有获取到则获取部门的日历。
	 *  3.部门也没有设置的情况，获取系统默认的日历。
	 *  4.没有则返回0。
	 * </pre>
	 * @param userId
	 * @return
	 */
	public String getCalendarIdByUserId(String userId){ 
		//根据用户ID取得日历ID，一个用户应该只有一个日历
		CalendarAssign calendarAssign =baseMapper.getByAssignId(CalendarAssign.TYPE_USER, userId);
		if(calendarAssign==null){
			//SysOrg sysOrg = sysOrgDao.getPrimaryOrgByUserId(userId);
			//XogGroup xogGroup=XogGroupGao;
//			if(sysOrg!=null){
//				long orgId=sysOrg.getOrgId();
//				calendarAssign =calendarAssignDao.getByAssignId(CalendarAssign.TYPE_ORGANIZATION, orgId);
//			}
		}
		if(calendarAssign!=null){
			return calendarAssign.getCanlendarId();
		}
		//获取默认的日历。
		Calendar calendar=calendarDao.getDefaultCalendar();
		if(calendar!=null){
			return calendar.getId();
		}
		return "0";
	}
	
	/**
	 * 重写 
	 * 使用自定义的get方法
	 */
	@Override
	public CalendarAssign get(Serializable id) {
		return baseMapper.get(id);
	}
	
	/**
	 * 重写 
	 * 使用自定义的query方法
	 */
	@Override
	public PageList<CalendarAssign> query(QueryFilter<CalendarAssign> queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
    	IPage<CalendarAssign> page = new Page<CalendarAssign>(0, Integer.MAX_VALUE);
    	if(BeanUtils.isNotEmpty(pageBean)){
    		page = convert2IPage(pageBean);
    	}
    	Class<CalendarAssign> currentModelClass = currentModelClass();
    	Wrapper<CalendarAssign> convert2Wrapper = convert2Wrapper(queryFilter, currentModelClass);
		return baseMapper.query(page, convert2Wrapper);
	}
	/**
	 * 根据日历列表获取相应的工作时间分段列表
	 * @param list
	 * @return
	 */
	@Deprecated
	public List<CalendarShiftPeroid> getBycalList(List<CalendarSetting> list){
		List<CalendarShiftPeroid>tmplist=new ArrayList<CalendarShiftPeroid>();
		List<CalendarShiftPeroid>worklist=new ArrayList<CalendarShiftPeroid>();
		for(CalendarSetting setting:list){
			String calDay=setting.getCalDay();   
			
            List<CalendarShiftPeroid>workTimeList=setting.getCalendarShiftPeroidList();
			for(CalendarShiftPeroid work:workTimeList){
				work.setCalDay(calDay);
				tmplist .add((CalendarShiftPeroid)work.clone());
			}
		}
		int len=tmplist.size();
		for(int i=0;i<len;i++){
			CalendarShiftPeroid workTime=tmplist.get(i);  
			
			if(i<len-1){
				int j=i+1;
				CalendarShiftPeroid nextTime=tmplist.get(j);
				if(workTime.getEndDateTime().compareTo(nextTime.getStartDateTime())>0){
					workTime.setEndDateTime(nextTime.getEndDateTime());
					worklist.add(workTime);
					i++;
				}
				else{
					worklist.add(workTime);
				}
			}else{
				worklist.add(workTime);
			}
		}
		return worklist;
	}
	
	
	/**
	 * 返回系统默认工作日历的工作时段
	 * @param startDate 开始时间
	 * @return
	 */
	public List<CalendarShiftPeroid> getTaskTimeByDefault(LocalDateTime startDate){
		Calendar CalCalendar=calendarDao.getDefaultCalendar();
		if(CalCalendar!=null){
			String calendarId = CalCalendar.getId();
			List<CalendarShiftPeroid> worklist= calendarSettingManager.getByCalendarId(calendarId,startDate);
			return worklist;
		}	
		return null;	
	}
	
	
	/**
	 * 取被分配的类型,用户或组织
	 * @return
	 */
	public List<Map<String,String>> getAssignUserType(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", "1");
		map.put("name", "用户");
		list.add(map);
		
		map = new HashMap<String,String>();
		map.put("id", "2");
		map.put("name", "组织");
		list.add(map);
		
		return list;
	}
	
	/**
	 * 根据日历id删除记录
	 * @param calIds
	 */
	public void delByCalId(String[] calIds){
		for (String calId : calIds) {
			baseMapper.delByCalId(calId);
		}
	}
	
	/**
	 * 根据用户ID得到唯一条分配信息
	 * @param assignId
	 * @return
	 */
	public CalendarAssign getbyAssignId(String assignId){
		return baseMapper.getbyAssign(assignId);
	}

	@Override
	public List<String> saveAssign(String assign) throws Exception{
		List<String> duplicateNames = new ArrayList<String>();
		ObjectNode jobject = (ObjectNode) JsonUtil.toJsonNode(assign);
		String assignType = jobject.get("assignType").asText();
		String calendarId = jobject.get("calendarId").asText();
		ArrayNode jsonArray = (ArrayNode) jobject.get("assign");
		String id = null;
		try {
			id = jobject.get("id").asText();
		} catch (Exception e) {}
		for (Object object : jsonArray) {
			ObjectNode jObj = (ObjectNode) JsonUtil.toJsonNode(object);
			String assignId = jObj.get("id").asText();
			String assignUserName = "";
			if(assignType.equals("1")){
				assignUserName = jObj.get("fullname").asText();
			}else if(assignType.equals("2")){
				assignUserName = jObj.get("name").asText();
			}
			CalendarAssign calendarAssign = new CalendarAssign();
			calendarAssign.setAssignId(assignId);
			calendarAssign.setAssignType(assignType);
			calendarAssign.setCanlendarId(calendarId);
			calendarAssign.setAssignUserName(assignUserName);
			if(StringUtil.isNotEmpty(id)){
				CalendarAssign oldAssign = getbyAssignId(assignId);
				if(oldAssign!=null && !oldAssign.getId().equals(id)){
					duplicateNames.add(jObj.get("name").asText());
					continue;
				}else{
					calendarAssign.setId(id);
					this.update(calendarAssign);
				}
			}else{
				CalendarAssign oldAssign = getbyAssignId(assignId);
				if(oldAssign!=null){
					duplicateNames.add(jObj.get("name").asText());
					continue;
				}
				calendarAssign.setId(UniqueIdUtil.getSuid());
				this.create(calendarAssign);
			}
		}
		return duplicateNames;
	}
}
