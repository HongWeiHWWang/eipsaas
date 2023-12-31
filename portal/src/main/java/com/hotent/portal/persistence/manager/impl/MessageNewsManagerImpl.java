package com.hotent.portal.persistence.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryField;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.model.MessageNews;
import com.hotent.portal.persistence.dao.MessageNewsDao;
import com.hotent.portal.persistence.manager.AuthorityManager;
import com.hotent.portal.persistence.manager.MessageNewsManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.impl.util.PermissionCalc;
import com.hotent.uc.api.model.IUser;

/**
 * 
 * <pre> 
 * 描述：新闻公告 处理实现类
 * 构建组：x7
 * 作者:dengyg
 * 邮箱:dengyg@jee-soft.cn
 * 日期:2018-08-20 16:04:35
 * 版权：广州宏天软件有限公司
 * </pre>
 */
@Service("messageNewsManager")
public class MessageNewsManagerImpl extends BaseManagerImpl<MessageNewsDao, MessageNews> implements MessageNewsManager{
	@Resource
	UCFeignService ucFeignService;
	@Resource
	AuthorityManager authorityManager;
	@Resource
	PermissionCalc permssionCalc;
	
	@Override
	public PageList<MessageNews> query(QueryFilter queryFilter){
		List<QueryField> querys = queryFilter.getQuerys();
		boolean isp = false;
		for (QueryField queryField : querys) {
			if("2".equals(queryField.getValue()) && ("FStatus".equals(queryField.getProperty())||"F_status".equals(queryField.getProperty()))){
				isp = true;
				break;
			}
		}
		IUser user = ContextUtil.getCurrentUser();
		if(isp && !user.isAdmin()){
			QueryFilter filter = QueryFilter.build().withPage(new PageBean(1, Integer.MAX_VALUE));
			PageList<MessageNews> pageList = super.query(filter);
			if(pageList.getTotal()>0){
				List<MessageNews> all = pageList.getRows();
				Map<String, Set<String>> authMap = authorityManager.getUserRightMap();
				List<String> authIds = new ArrayList<String>();
				for (MessageNews messageNews : all) {
					try {
						if(StringUtil.isNotEmpty(messageNews.getFCkqxsz())){
							ArrayNode authArray = (ArrayNode) JsonUtil.toJsonNode(messageNews.getFCkqxsz());
							for (JsonNode jsonNode : authArray) {
								if(permssionCalc.hasRight(jsonNode.toString(), authMap)){
									authIds.add(messageNews.getId());
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(authIds.size()>1000){
					int sourceSize = authIds.size();
				    int size = (authIds.size() / 1000) + 1;
				    for (int i = 0; i < size; i++) {
				        List<String> subset = new ArrayList<String>();
				        for (int j = i * 1000; j < (i + 1) * 1000; j++) {
				            if (j < sourceSize) {
				                subset.add(authIds.get(j));
				            }
				        }
				        if(subset.size()>0){
				        	queryFilter.addFilter("id", subset, QueryOP.IN, FieldRelation.OR, "group01");
				        }
				    }
				}else if(authIds.size()>0){
					queryFilter.addFilter("id", authIds, QueryOP.IN, FieldRelation.AND, "group01");
				}
			}
		}
		return super.query(queryFilter);
	}
}
