package com.hotent.bpm.persistence.manager.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.bpm.persistence.dao.ActHiTaskInstDao;
import com.hotent.bpm.persistence.manager.ActHiTaskInstManager;
import com.hotent.bpm.persistence.model.ActHiTaskInst;

@Service("actHiTaskInstManager")
public class ActHiTaskInstManagerImpl extends BaseManagerImpl<ActHiTaskInstDao, ActHiTaskInst> implements ActHiTaskInstManager{
	@Resource
	ActHiTaskInstDao actHiTaskInstDao;

}
