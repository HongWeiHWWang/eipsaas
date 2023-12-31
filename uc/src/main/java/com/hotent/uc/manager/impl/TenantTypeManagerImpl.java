package com.hotent.uc.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import com.hotent.uc.dao.TenantTypeDao;
import com.hotent.uc.manager.TenantManageManager;
import com.hotent.uc.manager.TenantTypeManager;
import com.hotent.uc.model.TenantType;

/**
 * 
 * <pre> 
 * 描述：租户类型管理 处理实现类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:52:37
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("tenantTypeManager")
public class TenantTypeManagerImpl extends BaseManagerImpl<TenantTypeDao, TenantType> implements TenantTypeManager{

	@Resource
	TenantManageManager tenantManageManager;
	
	@Override
	public List<TenantType> getByStatus(String status,List<String> authIds) {
		return baseMapper.getByStatus(status,BeanUtils.isNotEmpty(authIds)?authIds:null);
	}
	
	@Override
	public TenantType getByCode(String code) {
		return baseMapper.getByCode(code);
	}

	@Override
	public TenantType getDefault() {
		return baseMapper.getDefault();
	}

	@Override
	@Transactional
	public CommonResult<String> setDefault(String code) {
		TenantType type = baseMapper.getByCode(code);
		if(BeanUtils.isNotEmpty(type)){
			baseMapper.setNotDefault();
			type.setIsDefault("1");
			super.update(type);
			return new CommonResult<String>(true,"设置成功！");
		}else{
			return new CommonResult<String>(false,"租户类型编码【"+code+"】不存在！");
		}
	}
}
