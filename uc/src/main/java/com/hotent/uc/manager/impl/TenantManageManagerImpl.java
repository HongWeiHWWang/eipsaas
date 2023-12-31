package com.hotent.uc.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import com.hotent.base.util.time.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.TenantUtil;
import com.hotent.uc.dao.TenantManageDao;
import com.hotent.uc.manager.TenantManageManager;
import com.hotent.uc.model.TenantManage;

import io.seata.spring.annotation.GlobalTransactional;

/**
 * 
 * <pre> 
 * 描述：UC_TENANT_MANAGE 处理实现类
 * 构建组：x7
 * 作者:zhangxw
 * 邮箱:zhangxw@jee-soft.cn
 * 日期:2020-04-17 10:56:07
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("tenantManageManager")
public class TenantManageManagerImpl extends BaseManagerImpl<TenantManageDao, TenantManage> implements TenantManageManager{
	
	@Resource
	PortalFeignService portalFeignService;
	
	/**
	 * <pre>
	 * 重写通用的创建数据的方法
	 * 创建租户的同时初始化一份平台的数据给租户使用(维度，默认分类)
	 * 需要处理分布式事务
	 * 
	 * </pre>
	 */
	@Override
    @GlobalTransactional
	public void create(TenantManage t) {
		t.setUpdateTime(DateUtil.getCurrentDate());
		super.create(t);
		TenantUtil.initData(t.getId(), TenantConstant.INIT_UC_DATA_TABLE_NAMES);
		try {			
			CommonResult<String> initData = portalFeignService.initData(t.getId());
			if(!initData.getState()) {
				throw new BaseException("创建租户初始化数据失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("创建租户初始化数据失败");
		}
	}

	@Override
	public TenantManage getByCode(String code) {
		return baseMapper.getByCode(code);
	}

	@Override
	public List<TenantManage> getByTypeId(String typeId) {
		return baseMapper.getByTypeId(typeId);
	}

	@Override
	@Transactional
	public void deleteByTypeId(String typeId) {
		baseMapper.deleteByTypeId(typeId);
	}

	@Override
	public TenantManage getByDomain(String domain) {
		return baseMapper.getByDomain(domain);
	}

	@Override
	public PageList queryWithType(QueryFilter queryFilter) {
		IPage page = convert2IPage(queryFilter.getPageBean());
		return new PageList(baseMapper.queryWithType(page,convert2Wrapper(queryFilter,currentModelClass())));
	}

	@Override
	public List<TenantManage> getByStatus(String typeId, String status) {
		return baseMapper.getByStatus(typeId, status);
	}
	
}
