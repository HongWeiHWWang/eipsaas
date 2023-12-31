package com.hotent.uc.manager.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.service.PwdStrategyService;
import com.hotent.base.util.JsonUtil;
import com.hotent.uc.dao.PwdStrategyDao;
import com.hotent.uc.manager.PwdStrategyManager;
import com.hotent.uc.model.PwdStrategy;

@Service("pwdStrategyManager")
public class PwdStrategyManagerImpl extends BaseManagerImpl<PwdStrategyDao, PwdStrategy> implements PwdStrategyManager,PwdStrategyService{

	@Override
	public PwdStrategy getDefault() {
		return baseMapper.getDefault();
	}

	@Override
	public JsonNode getJsonDefault() {
		try {
			PwdStrategy pwdStrategy = baseMapper.getDefault();
			return JsonUtil.toJsonNode(pwdStrategy);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("获取默认密码策略失败");
		}
	}

}
