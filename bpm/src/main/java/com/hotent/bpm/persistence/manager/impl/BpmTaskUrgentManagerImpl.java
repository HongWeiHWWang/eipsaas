package com.hotent.bpm.persistence.manager.impl;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.BpmTaskUrgentDao;
import com.hotent.bpm.persistence.manager.BpmTaskUrgentManager;
import com.hotent.bpm.persistence.model.BpmTaskUrgent;

/**
 * 
 * <pre> 
 * 描述：人工催办 处理实现类
 * 构建组：x7
 * 作者:zhaoxy
 * 邮箱:zhaoxy@jee-soft.cn
 * 日期:2019-09-02 15:26:38
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("bpmTaskUrgentManager")
public class BpmTaskUrgentManagerImpl extends BaseManagerImpl<BpmTaskUrgentDao, BpmTaskUrgent> implements BpmTaskUrgentManager{

}
