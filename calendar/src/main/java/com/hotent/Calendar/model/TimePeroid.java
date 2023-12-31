package com.hotent.Calendar.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.time.TimeUtil;


/**
 * 时间段 
 * @author lenovo
 *
 */
public class TimePeroid implements Comparable<TimePeroid>{
	
	//如何表示startDate 均不能为空
	protected LocalDateTime startDate;   //开始时间
	protected LocalDateTime endDate;     //结束时间
	protected long time;        //时长（ 毫秒）
    protected String PeroidType;//时间段类型，上班、请假、加班
    /**
     * 上班
     */
    public static final String PEROID_TYPE_SHIFT="shift";
    /**
     * 请假
     */
    public static final String PEROID_TYPE_ABSENCE="absence";
    /**
     * 加班
     */
    public static final String PEROID_TYPE_OVER="over";
	/**
	 * 构造函数
	 */
	public TimePeroid(){
		
	}
	/**
	 * 构造函数
	 * overTime类型转TimePart
	 * @param overTime
	 */
	public TimePeroid(LocalDateTime strat_date,LocalDateTime end_date,String peroid_type) {
	this.startDate=strat_date;
	this.endDate=end_date;
	this.time=getTime(end_date)-getTime(strat_date);
	this.PeroidType=peroid_type;
	}
	
	public LocalDateTime getStartDate() {
		return startDate;
	}
	/**
	 * 每次设置开始时间，都要计算时长
	 * @author hjx
	 * @version 创建时间：2014-2-24  下午2:26:57
	 * @param beginDate
	 */
	public void setStartDate(LocalDateTime beginDate) {
		this.startDate = beginDate;
		this.time=getTime(this.endDate)-getTime(beginDate);
	}
	public LocalDateTime getEndDate() {
		return endDate;
	}
	/**
	 * 次设置结束时间，都要计算时长
	 * @author hjx
	 * @version 创建时间：2014-2-24  下午2:27:03
	 * @param endDate
	 */
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
		this.time=getTime(endDate)-getTime(this.getStartDate());
	}
	/**
	 * 计算时长 毫秒
	 * @author hjx
	 * @version 创建时间：2014-2-20  下午4:47:12
	 * @return
	 */
	public long getTime() {
		return getTime(endDate)-getTime(startDate);
	}
	public void setTime(Long time) {
		this.time = time;
	}

	
	/**
	 * 比较大小
	 * 这样就能使用Collections.sort(lists)排序
	 */
	@Override
	public int compareTo(TimePeroid timePeroid) {
		  if (null == timePeroid) return 1;  
	        else {  
	            return this.startDate.compareTo(timePeroid.startDate);  
	        }  
	}
	
	@Override
	public String toString(){
		return TimeUtil.getDateTimeString(this.getStartDate())
				+"~"
				+TimeUtil.getDateTimeString(this.getEndDate())
				+"["+this.PeroidType+"]"
				+"["+(long)(time/60000)+"]"
				;
		
	}
	
	private long getTime(LocalDateTime date){
		if(BeanUtils.isEmpty(date)){
			return 0;
		}
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = date.atZone(zone).toInstant();
		return instant.toEpochMilli();
	}
	

}
