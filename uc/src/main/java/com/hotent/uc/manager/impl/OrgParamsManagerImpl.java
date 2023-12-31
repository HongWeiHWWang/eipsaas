package com.hotent.uc.manager.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.hotent.uc.dao.OrgParamsDao;
import com.hotent.uc.manager.OrgParamsManager;
import com.hotent.uc.model.OrgParams;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：组织参数 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2016-11-04 11:39:44
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class OrgParamsManagerImpl extends BaseManagerImpl <OrgParamsDao, OrgParams> implements OrgParamsManager{
	
	@Override
	public List<OrgParams> getByOrgId(String id) {
		return baseMapper.getByOrgId(id);
	}

	@Override
    @Transactional
	public void saveParams(String orgId, List<ObjectNode> lists) {
		for (ObjectNode ObjectNode : lists) {
			String alias = ObjectNode.get("alias").asText();
			OrgParams params = this.getByOrgIdAndAlias(orgId,alias);
			if(BeanUtils.isNotEmpty(params)){
				 this.remove(params.getId());
			}
			OrgParams orgParams = new OrgParams();
			orgParams.setAlias(alias);
			orgParams.setValue(ObjectNode.get("value").asText());
			orgParams.setOrgId(orgId);
			orgParams.setId(UniqueIdUtil.getSuid());
			this.create(orgParams);
		}
	}
	@Override
	public OrgParams getByOrgIdAndAlias(String groupId, String key) {
		return baseMapper.getByOrgIdAndAlias(groupId, key);
	}

	@Override
    @Transactional
	public void removeByOrgId(String orgId) {
		baseMapper.removeByOrgId(orgId,LocalDateTime.now());
	}

	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
}
