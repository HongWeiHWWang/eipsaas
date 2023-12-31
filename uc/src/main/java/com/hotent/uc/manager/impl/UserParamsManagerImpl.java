package com.hotent.uc.manager.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.uc.dao.UserParamsDao;
import com.hotent.uc.manager.UserParamsManager;
import com.hotent.uc.model.UserParams;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <pre> 
 * 描述：用户参数 处理实现类
 * 构建组：x5-bpmx-platform
 * 作者:liyg
 * 邮箱:liyg@jee-soft.cn
 * 日期:2016-11-01 17:11:33
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service
public class UserParamsManagerImpl extends BaseManagerImpl <UserParamsDao, UserParams> implements UserParamsManager{
	
	@Override
	public List<UserParams> getByUserId(String id) {
		return baseMapper.getByUserId(id);
	}
	@Override
    @Transactional
	public void saveParams(String userId, List<ObjectNode> lists) throws SQLException {
		
		for (ObjectNode ObjectNode : lists) {
			 UserParams params = this.getByUserIdAndAlias(userId, ObjectNode.get("alias").asText());
			 if(BeanUtils.isNotEmpty(params)){
				 if(ObjectNode.hasNonNull("value") && !"null".equals(ObjectNode.get("value").asText())){
					 params.setValue(ObjectNode.get("value").asText());
				 }else{
					 params.setValue("");
				 }
				 this.update(params);
			}else{
				UserParams userParams = new UserParams();
				userParams.setAlias(ObjectNode.get("alias").asText());
//				userParams.setValue(ObjectNode.get("value"));
				if(!"null".equals(ObjectNode.get("value"))){
					userParams.setValue(ObjectNode.get("value").asText());
				}
				userParams.setUserId(userId);
				userParams.setId(UniqueIdUtil.getSuid());
				this.create(userParams);
			}
		}
	}
	@Override
	public UserParams getByUserIdAndAlias(String userId, String key) {
		return baseMapper.getByUserIdAndCode(userId, key);
	}
	@Override
    @Transactional
	public Integer removePhysical() {
		return baseMapper.removePhysical();
	}
}
