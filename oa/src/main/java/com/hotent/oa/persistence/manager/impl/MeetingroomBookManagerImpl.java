package com.hotent.oa.persistence.manager.impl;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.oa.model.Meetingroom;
import com.hotent.oa.model.MeetingroomBook;
import com.hotent.oa.persistence.dao.MeetingroomBookDao;
import com.hotent.oa.persistence.manager.MeetingroomBookManager;
import com.hotent.oa.persistence.manager.MeetingroomManager;
import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <pre> 
 * 描述：会议室预约 处理实现类
 * 构建组：x7
 * 作者:David
 * 邮箱:376514860@qq.com
 * 日期:2023-12-24 01:13:25
 * 版权：wijo
 * </pre>
 */
@Service("meetingroomBookManager")
public class MeetingroomBookManagerImpl extends BaseManagerImpl<MeetingroomBookDao, MeetingroomBook> implements MeetingroomBookManager {
    @Resource
    MeetingroomManager meetingroomManager;
    public PageList<ObjectNode> getBookList(QueryFilter queryFilter) throws IOException {


        List<QueryField> querys = queryFilter.getQuerys();
        String dayStr ="";
        List<QueryField> newQuerys=new ArrayList<>();
        for (QueryField queryField : querys) {
            if("dayStr".equals(queryField.getProperty())){
                dayStr=queryField.getValue().toString();
            }else{
                newQuerys.add(queryField);
            }
        }
        queryFilter.setQuerys(newQuerys);
        QueryFilter qfForMeetingroomBook=QueryFilter.build();

        //获取用户申请批权限的会议室id
		/*List<String> idsList = bpmDefUserManager.getAuthorizeIdsByUserMap("meetingRoom");
		String ids= StringUtil.join(idsList.toArray(new String[]{}), ",");
		queryFilter.addFilter("ID_", ids, QueryOP.IN);*/
        PageList<Meetingroom> meetingroomList=meetingroomManager.query(queryFilter);

        LocalDateTime begTime = TimeUtil.convertString(dayStr+" 00:00:00");
        LocalDateTime endTime = TimeUtil.convertString(dayStr+" 23:59:59");
        qfForMeetingroomBook.addFilter("F_APPOINTMENT_BEG_TIME_", begTime, QueryOP.GREAT_EQUAL);
        qfForMeetingroomBook.addFilter("F_APPOINTMENT_END_TIME_", endTime, QueryOP.LESS_EQUAL);
        //qfForMeetingroomBook.addFilter("F_APPOINTMENT_STATUS_", "1", QueryOP.EQUAL);
        /*qfForMeetingAppoint.addFilter("MEETINGROOM_ID_", ids, QueryOP.IN);*/
        PageList<MeetingroomBook> meetingroomBookList=this.query(qfForMeetingroomBook);
        PageList<ObjectNode> resultList= new  PageList<ObjectNode> ();
        List<ObjectNode> objList=new ArrayList<>();
        for(int i=0;i<meetingroomList.getRows().size();i++){
            ObjectNode data= JsonUtil.getMapper().createObjectNode();
            Meetingroom meetingroom=meetingroomList.getRows().get(i);
            data.put("id",meetingroom.getId());
            data.put("refId",meetingroom.getRefId());
            data.put("FName",meetingroom.getFName());
            data.put("FCapacity",meetingroom.getFCapacity());
            data.put("FArea",meetingroom.getFArea());
            data.put("FStatus",meetingroom.getFStatus());

            List<ObjectNode> bookList=new ArrayList<>();
            for(int j=0;j<meetingroomBookList.getRows().size();j++) {
                MeetingroomBook meetingroomBook = meetingroomBookList.getRows().get(j);
                //int endDate=Integer.valueOf(TimeUtil.getDateString(meetingroomBook.getFAppointmentEndTime(),"yyyyMMdd"));
                //int begDate=Integer.valueOf(TimeUtil.getDateString(meetingroomBook.getFAppointmentBegTime(),"yyyyMMdd"));
                //如果该会议室在预约表中有记录，则向该会议室该天加入该条记录
                if(meetingroomBook.getFMeetingroomId().equals(meetingroom.getId())){
                    ObjectNode obj=JsonUtil.getMapper().createObjectNode();
                    obj.put("FMeetingId", meetingroomBook.getFMeetingId());
                    obj.put("FMeetingName", meetingroomBook.getFMeetingName());
                    obj.put("FHosstessName", meetingroomBook.getFHosstessName());
                    obj.put("FHosstessPhoto", meetingroomBook.getFHosstessPhoto());
                    obj.put("FAppointmentBegTime", TimeUtil.getDateString(meetingroomBook.getFAppointmentBegTime(), "yyyy-MM-dd HH:mm:ss"));
                    obj.put("FAppointmentEndTime", TimeUtil.getDateString(meetingroomBook.getFAppointmentEndTime(), "yyyy-MM-dd HH:mm:ss"));
                    bookList.add(obj);
                }

            }
            if(bookList.size()>0){
                data.put("MeetingroomBookList", JsonUtil.toJson(bookList));
            }else{
                data.put("MeetingroomBookList","");
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
