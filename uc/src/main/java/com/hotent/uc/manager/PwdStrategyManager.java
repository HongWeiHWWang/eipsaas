package com.hotent.uc.manager;

import com.hotent.base.manager.BaseManager;
import com.hotent.uc.model.PwdStrategy;

public interface PwdStrategyManager extends BaseManager<PwdStrategy> {
	
	PwdStrategy getDefault();
}
