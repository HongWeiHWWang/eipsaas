package com.hotent.Calendar.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.Calendar.dao.CalendarShiftDao;
import com.hotent.Calendar.dao.CalendarShiftPeroidDao;
import com.hotent.Calendar.manager.CalendarSettingManager;
import com.hotent.Calendar.manager.CalendarShiftManager;
import com.hotent.Calendar.manager.CalendarShiftPeroidManager;
import com.hotent.Calendar.model.CalendarShift;
import com.hotent.Calendar.model.CalendarShiftPeroid;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;


@Service("calendarShiftManager")
public class CalendarShiftManagerImpl extends BaseManagerImpl<CalendarShiftDao, CalendarShift> implements CalendarShiftManager{
	@Resource
	CalendarShiftPeroidDao calendarShiftPeroidDao;
	@Resource
	CalendarShiftPeroidManager calendarShiftPeroidManager;
	@Resource
	CalendarSettingManager  calendarSettingManager;
	
	/**
	 * 创建实体包含子表实体
	 */
	public void create(CalendarShift calendarShift){
    	super.create(calendarShift);
    	String shiftId = calendarShift.getId();
    	calendarShiftPeroidDao.delByMainId(shiftId);
    	List<CalendarShiftPeroid> workTimeList=calendarShift.getCalendarShiftPeroidList();
    	for(CalendarShiftPeroid CalendarShiftPeroid:workTimeList){
    		CalendarShiftPeroid.setShiftId(shiftId);
			calendarShiftPeroidManager.create(CalendarShiftPeroid);
    	}
    }
	
	/**
	 * 删除记录包含子表记录
	 */
	public void remove(String entityId){
		//PageBean pageBean = new PageBean(10000);
		//QueryFilter queryFilter = QueryFilter.build()
		//									 .withPage(pageBean)
		//									 .withQuery(new QueryField("SHIFT_ID_", entityId));
        //
		//List<CalendarSetting> list =  (List<CalendarSetting>) calendarSettingManager.query(queryFilter);
		//for (CalendarSetting calendarSetting : list) {
		//	calendarSettingManager.remove(calendarSetting.getId());
		//}
		super.remove(entityId);
    	calendarShiftPeroidDao.delByMainId(entityId);
		
	}
	
	/**
	 * 获取实体
	 */
    public CalendarShift get(String entityId){
    	CalendarShift CalendarShift=super.get(entityId);
    	List<CalendarShiftPeroid> calendarShiftPeroidList=calendarShiftPeroidDao.getCalendarShiftPeroidList(entityId);
    	CalendarShift.setCalendarShiftPeroidList(calendarShiftPeroidList);
    	return CalendarShift;
    }
    
    /**
     * 更新实体同时更新子表记录
     */
    public void update(CalendarShift  calendarShift){
    	super.update(calendarShift);
    	String shiftId = calendarShift.getId();
    	calendarShiftPeroidDao.delByMainId(shiftId);
    	List<CalendarShiftPeroid> calendarShiftPeroidList=calendarShift.getCalendarShiftPeroidList();
    	for(CalendarShiftPeroid calendarShiftPeroid:calendarShiftPeroidList){
			if(StringUtil.isEmpty(calendarShiftPeroid.getId())){
				calendarShiftPeroid.setId(UniqueIdUtil.getSuid());
			}
    		calendarShiftPeroid.setShiftId(shiftId);
    		calendarShiftPeroidDao.insert(calendarShiftPeroid);
    	}
    }
    
	
	public void workTimeAdd(Long shiftId, String[] startTime, String[] endTime, String[] memo){
		
		if(startTime!=null && endTime!=null){
			
			calendarShiftPeroidDao.delByMainId(shiftId.toString());
			
			for(int idx=0;idx<startTime.length;idx++){
				CalendarShiftPeroid CalendarShiftPeroid = new CalendarShiftPeroid();
				try {
					//CalendarShiftPeroid.setId(UniqueIdUtil.genId());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				CalendarShiftPeroid.setShiftId(shiftId.toString());
				CalendarShiftPeroid.setStartTime(startTime[idx]);
				CalendarShiftPeroid.setEndTime(endTime[idx]);
				CalendarShiftPeroid.setMemo(memo[idx]);
				calendarShiftPeroidDao.insert(CalendarShiftPeroid);
			}
		}
	}

	@Override
	public void setDefaultShift(String id) {
		//设置非默认
		baseMapper.setNotDefaultShift();
		CalendarShift calendarShift =this.get(id);
		calendarShift.setIsDefault("1");
		update(calendarShift);
		
	}
}
