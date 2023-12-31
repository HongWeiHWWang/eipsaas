package com.hotent.bpmModel.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.bpmModel.dao.BpmOftenFlowDao;
import com.hotent.bpmModel.manager.BpmOftenFlowManager;
import com.hotent.bpmModel.model.BpmOftenFlow;

/**
 * 
 * <pre>
 *  
 * 描述：通用流程 处理实现类
 * 构建组：x7
 * 作者:liyg
 * 邮箱:liygui@jee-soft.cn
 * 日期:2019-03-04 15:23:03
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("bpmCommonDefManager")
public class BpmOftenFlowManagerImpl extends BaseManagerImpl<BpmOftenFlowDao, BpmOftenFlow>
		implements BpmOftenFlowManager {
	@Override
	@Transactional
	public void removeByUserIdAndDefKeys(String userId, List<String> defkeys) {
		baseMapper.removeByUserIdAndDefKeys(userId, defkeys);
	}

	@Override
	@Transactional
	public void saveOrUpdateCommonFlow(String defkeys) throws Exception{
		if (StringUtil.isNotEmpty(defkeys)) {
			String[] defkeysAry = defkeys.split(",");
			ArrayList<String> list = new ArrayList<>(Arrays.asList(defkeysAry));
			//this.removeByUserIdAndDefKeys("-1", list);
			List<BpmOftenFlow> bpmOftenFlows = new ArrayList<>();
			list.forEach(i -> {
				List<BpmOftenFlow> flows = baseMapper.getBpmOftenFlows("-1", i);
				if(BeanUtils.isNotEmpty(flows)) {
					throw new BaseException("流程key：" + i + "已存在，请重新选择");
				}
				bpmOftenFlows.add(new BpmOftenFlow("-1", "所有人", i));
			});
			this.saveBatch(bpmOftenFlows);
		}
	}

	@Override
	@Transactional
	public void saveMyFlow(String userId, String userName, ArrayNode list) {
		this.removeByUserIdAndDefKeys(userId, null);
		List<BpmOftenFlow> bpmOftenFlows = new ArrayList<>();
		list.forEach(i -> {
			bpmOftenFlows.add(new BpmOftenFlow(userId, userName, i.get("defKey").asText()));
		});
		this.saveBatch(bpmOftenFlows);
	}

	public PageList<BpmOftenFlow> query(QueryFilter<BpmOftenFlow> queryFilter) {
		return new PageList<>(baseMapper.customQuery(convert2IPage(queryFilter.getPageBean()),convert2Wrapper(queryFilter, currentModelClass())));
	}

	@Override
	public List<BpmOftenFlow> getBpmOftenFlows(String userId, String defKey) {
		return baseMapper.getBpmOftenFlows(userId, defKey);
	}
}
