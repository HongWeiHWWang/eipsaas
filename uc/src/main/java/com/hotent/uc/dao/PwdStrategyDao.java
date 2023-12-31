package com.hotent.uc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotent.uc.model.PwdStrategy;

public interface PwdStrategyDao extends BaseMapper<PwdStrategy>{
	
	PwdStrategy getDefault();
}
