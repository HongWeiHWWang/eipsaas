package com.hotent.bo.persistence.manager.impl;

import org.springframework.stereotype.Service;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bo.model.BoDataRel;
import com.hotent.bo.persistence.dao.BoDataRelDao;
import com.hotent.bo.persistence.manager.BoDataRelManager;

/**
 * bo数据关系manager默认实现
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date Apr 26, 2018
 */
@Service("boDataRelManager")
public class BoDataRelManagerImpl extends BaseManagerImpl<BoDataRelDao, BoDataRel> implements BoDataRelManager{
}
