package com.hotent.bpm.persistence.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.TaskFollowDao;
import com.hotent.bpm.persistence.manager.TaskFollowManager;
import com.hotent.bpm.persistence.model.TaskFollow;




/**
 * 
 * <pre> 
 * 描述：任务跟踪表 处理实现类
 * 构建组：x7
 * 作者:maoww
 * 邮箱:maoww@jee-soft.cn
 * 日期:2018-11-13 10:04:44	
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("taskFollowManager")
public class TaskFollowManagerImpl extends BaseManagerImpl<TaskFollowDao, TaskFollow> implements TaskFollowManager{

}
