package com.hotent.runtime.manager.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bpm.persistence.manager.BpmDefUserManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.runtime.dao.MeetingRoomAppointmentDao;
import com.hotent.runtime.manager.MeetingRoomAppointmentManager;
import com.hotent.runtime.manager.MeetingRoomManager;
import com.hotent.runtime.model.MeetingRoom;
import com.hotent.runtime.model.MeetingRoomAppointment;

/**
 * 
 * <pre> 
 * 描述：会议室预约 处理实现类
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-10 15:11:20
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("meetingRoomAppointmentManager")
public class MeetingRoomAppointmentManagerImpl extends BaseManagerImpl<MeetingRoomAppointmentDao, MeetingRoomAppointment> implements MeetingRoomAppointmentManager{
	@Resource
	BpmDefUserManager bpmDefUserManager;
	@Resource
	MeetingRoomManager meetingRoomManager;
	@Resource
	BpmDefinitionManager bpmDefinitionManager;
	@Override
	public PageList<ObjectNode> getAppointList(QueryFilter queryFilter) throws IOException {
		
		List<DefaultBpmDefinition> defs = bpmDefinitionManager.queryByDefKey("hylc");
		String  defId = ""; 
	       if(defs.size()>0 && BeanUtils.isNotEmpty(defs.get(defs.size()-1))){
	    	   defId= defs.get(defs.size()-1).getDefId();
	       }
		
		List<QueryField> querys = queryFilter.getQuerys();
		String weekStr ="";
		List<QueryField> newQuerys=new ArrayList<>();
		for (QueryField queryField : querys) {
			if("weekStr".equals(queryField.getProperty())){
				weekStr=queryField.getValue().toString();
			}else{
				newQuerys.add(queryField);
			}
		}
		queryFilter.setQuerys(newQuerys);
		QueryFilter qfForMeetingAppoint=QueryFilter.build();
		//获取用户申请批权限的会议室id
		/*List<String> idsList = bpmDefUserManager.getAuthorizeIdsByUserMap("meetingRoom");
		String ids= StringUtil.join(idsList.toArray(new String[]{}), ",");
		queryFilter.addFilter("ID_", ids, QueryOP.IN);*/
		PageList<MeetingRoom> meetingroomList=meetingRoomManager.query(queryFilter);
		String[] arr = weekStr.split(",");
		LocalDateTime begTime = TimeUtil.convertString(arr[0].split("\\|")[0]+" 00:00:00");
		LocalDateTime endTime = TimeUtil.convertString(arr[6].split("\\|")[0]+" 00:00:00");
		qfForMeetingAppoint.addFilter("APPOINTMENT_BEG_TIME_", begTime, QueryOP.GREAT_EQUAL);
		qfForMeetingAppoint.addFilter("APPOINTMENT_END_TIME_", endTime, QueryOP.LESS_EQUAL);
		qfForMeetingAppoint.addFilter("APPOINTMENT_STATUS_", "1", QueryOP.EQUAL);
		/*qfForMeetingAppoint.addFilter("MEETINGROOM_ID_", ids, QueryOP.IN);*/
		PageList<MeetingRoomAppointment> meetingAppointList=this.query(qfForMeetingAppoint);
		PageList<ObjectNode> resultList= new  PageList<ObjectNode> ();
		List<ObjectNode> objList=new ArrayList<>();
        for(int i=0;i<meetingroomList.getRows().size();i++){
        	ObjectNode data=JsonUtil.getMapper().createObjectNode();
        	MeetingRoom meetingroom=meetingroomList.getRows().get(i);
        	data.put("mtRoomId",meetingroom.getId());
        	data.put("defId",defId);
        	data.put("mtName",meetingroom.getName());
     		for(int z=0;z<arr.length;z++){
    			int curDate=Integer.valueOf(arr[z].split("\\|")[0].replace("-", ""));
        	    String elNmae=arr[z].split("\\|")[1];
        	    List<ObjectNode> appList=new ArrayList<>();
        		for(int j=0;j<meetingAppointList.getRows().size();j++){
        			MeetingRoomAppointment meetingAppoint=meetingAppointList.getRows().get(j);
            		int endDate=Integer.valueOf(TimeUtil.getDateString(meetingAppoint.getAppointmentEndTime(),"yyyyMMdd"));
            		int begDate=Integer.valueOf(TimeUtil.getDateString(meetingAppoint.getAppointmentBegTime(),"yyyyMMdd"));
            		//如果该会议室在预约表中有记录，并且该天在会议持续时间所在的天内 。则向该会议室该天加入该条记录
            		if(meetingAppoint.getMeetingroomId().equals(meetingroom.getId()) &&	curDate<=endDate && curDate>=begDate){
            			ObjectNode obj=JsonUtil.getMapper().createObjectNode();
            			obj.put("meetingId", meetingAppoint.getMeetingId());
            			obj.put("meetingName", meetingAppoint.getMeetingName());
            			obj.put("hostName", meetingAppoint.getHostessName());
            			obj.put("dateStr", TimeUtil.getDateString(meetingAppoint.getAppointmentBegTime(),"HH:mm:ss")+"-"+TimeUtil.getDateString(meetingAppoint.getAppointmentEndTime(),"HH:mm:ss"));
            			appList.add(obj);
            		}
            		
        	 }
        	 if(appList.size()>0){
        		   data.put(elNmae, JsonUtil.toJson(appList));
        	   }else{
        		   data.put(elNmae,""); 
        	   }
        	}
    		 objList.add(data);
        }
        resultList.setRows(objList);
        resultList.setPage(meetingroomList.getPage());
        resultList.setTotal(meetingroomList.getTotal());
        resultList.setPageSize(meetingroomList.getPageSize());
		return resultList;
	}
	
}
