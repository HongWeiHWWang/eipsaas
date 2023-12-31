package com.hotent.oa.persistence.manager.impl;


import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.oa.persistence.dao.MeetingroomDao;
import com.hotent.oa.model.Meetingroom;
import com.hotent.oa.persistence.manager.MeetingroomManager;

/**
 * 
 * <pre> 
 * 描述：会议室 处理实现类
 * 构建组：x7
 * 作者:David
 * 邮箱:376514860@qq.com
 * 日期:2023-12-24 01:13:25
 * 版权：wijo
 * </pre>
 */
@Service("meetingroomManager")
public class MeetingroomManagerImpl extends BaseManagerImpl<MeetingroomDao, Meetingroom> implements MeetingroomManager{
	
}
