package com.hotent.bo.persistence.manager.impl;

import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bo.model.BoInst;
import com.hotent.bo.persistence.dao.BoInstDao;
import com.hotent.bo.persistence.manager.BoInstManager;

/**
 * 业务对象实例 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月13日
 */
@Service("boInstManager")
public class BoInstManagerImpl extends BaseManagerImpl<BoInstDao, BoInst> implements BoInstManager{
}
