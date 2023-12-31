package com.hotent.runtime.manager;

import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.runtime.model.MeetingRoomAppointment;

/**
 * 
 * <pre> 
 * 描述：会议室预约 处理接口
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-10 15:11:20
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface MeetingRoomAppointmentManager extends BaseManager<MeetingRoomAppointment>{
	 
	PageList<ObjectNode> getAppointList(QueryFilter queryFilter) throws IOException;
	
}
