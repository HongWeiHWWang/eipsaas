package com.hotent.portal.persistence.manager.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.portal.model.PressRelease;
import com.hotent.portal.persistence.dao.PressReleaseDao;
import com.hotent.portal.persistence.manager.PressReleaseManager;

/**
 * 
 * <pre> 
 * 描述：新闻公告 处理实现类
 * 构建组：x7
 * 作者:heyf
 * 邮箱:heyf@jee-soft.cn
 * 日期:2020-04-02 18:17:27
 * 版权：广州宏天软件股份有限公司
 * </pre>
 */
@Service("pressReleaseManager")
public class PressReleaseManagerImpl extends BaseManagerImpl<PressReleaseDao, PressRelease> implements PressReleaseManager{
	
	@Override
	public List<PressRelease> getByType(String fLbtssfl,String FFbfs) {
		QueryWrapper<PressRelease> wrapper=new QueryWrapper<PressRelease>();
		wrapper.eq("F_fbfs", FFbfs);
		wrapper.ge("F_yxsj", LocalDateTime.now());
		if(! "all".equals(fLbtssfl)){
			wrapper.eq("F_lbtssfl", fLbtssfl);
		}
		
		List<PressRelease> query=baseMapper.getAll(wrapper);
		return query;
	}
	
	@Override
	public List<Object> getNews() {
		List<String> news=baseMapper.getNews(LocalDateTime.now());
		List<Object> objs=new ArrayList<>();
		for (String string : news) {
			java.util.Map<String, Object> map=new HashMap<>();
			map.put("typeName", string);
	
			QueryWrapper<PressRelease> wrapper=new QueryWrapper<PressRelease>();
			wrapper.eq("F_fbfs", "2");
			wrapper.eq("F_gsbssfl", string);
			wrapper.ge("F_yxsj", LocalDateTime.now());
			
			List<PressRelease> query = baseMapper.getAll(wrapper);
			map.put("item", query);
			if(query.size()>0){
				objs.add(map);
			}
		}
		return objs;
	}
}
