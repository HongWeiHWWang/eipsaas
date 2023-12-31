package com.hotent.runtime.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.runtime.dao.MeetingRoomDao;
import com.hotent.runtime.manager.MeetingRoomManager;
import com.hotent.runtime.model.MeetingRoom;

/**
 * 
 * <pre> 
 * 描述：会议室 处理实现类
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-10 15:01:37
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("meetingRoomManager")
public class MeetingRoomManagerImpl extends BaseManagerImpl<MeetingRoomDao, MeetingRoom> implements MeetingRoomManager{
	
}
