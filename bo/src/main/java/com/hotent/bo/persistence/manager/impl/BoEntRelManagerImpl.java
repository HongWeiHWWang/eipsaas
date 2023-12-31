package com.hotent.bo.persistence.manager.impl;

import java.util.List;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.bo.model.BoEntRel;
import com.hotent.bo.persistence.dao.BoEntRelDao;
import com.hotent.bo.persistence.manager.BoEntRelManager;

/**
 * bo应用定义处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月13日
 */
@Service("boEntRelManager")
public class BoEntRelManagerImpl extends BaseManagerImpl<BoEntRelDao, BoEntRel> implements BoEntRelManager{
	@Override
	public List<BoEntRel> getByDefId(String defId) {
		return baseMapper.getByDefId(defId);
	}
	@Override
	public void removeByDefId(String defId) {
		baseMapper.removeByDefId(defId);
	}
	@Override
	public List<BoEntRel> getByEntId(String entId) {
		return baseMapper.getByEntId(entId);
	}
	@Override
	public BoEntRel getByDefIdAndEntId(String defId,String entId){
		List<BoEntRel> list = baseMapper.getByDefIdAndEntId(defId,entId);
		if(BeanUtils.isEmpty(list)){
			return null;
		}
		else{
			int size = list.size();
			if(size==1){
				return list.get(0);
			}
			else{
				throw new TooManyResultsException(String.format("通过defId：%s和entId：%s查询BoDef与BoEnt的关系时有%s条记录", defId, entId, size));
			}
		}
	}
}
