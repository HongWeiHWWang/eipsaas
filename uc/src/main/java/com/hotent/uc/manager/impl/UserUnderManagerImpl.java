package com.hotent.uc.manager.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.uc.dao.UserUnderDao;
import com.hotent.uc.manager.UserUnderManager;
import com.hotent.uc.model.UserUnder;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：下属管理 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2017-07-25 09:24:29
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class UserUnderManagerImpl extends BaseManagerImpl <UserUnderDao, UserUnder> implements UserUnderManager{
	
	
	@Override
	public IPage<UserUnder> getUserUnder(QueryFilter queryFilter) {
		PageBean pageBean = queryFilter.getPageBean();
		// 设置分页
		//PageHelper.startPage(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
		queryFilter.addFilter("IS_DELE_", "1", QueryOP.NOT_EQUAL);
		return baseMapper.getUserUnder(convert2IPage(pageBean),convert2Wrapper(queryFilter, currentModelClass()));
	}
	@Override
    @Transactional
	public void delByUpIdAndUderId(String orgId,String underUserId) {
		baseMapper.delByUpIdAndUderId(orgId,underUserId,LocalDateTime.now());
	}
	
	@Override
    @Transactional
	public void delByUserIdAndOrgId(String userId, String orgId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgId);
		map.put("userId", userId);
		map.put("updateTime", LocalDateTime.now());
		baseMapper.delByUserIdAndOrgId(map);
	}
	@Override
    @Transactional
	public void delByOrgId(String orgId) {
		baseMapper.delByOrgId(orgId,LocalDateTime.now());
	}
	@Override
	public List<UserUnder> getUserUnder(Map<String, Object> params) {
		return baseMapper.getUserUnderNOPage(params);
	}
	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
}
