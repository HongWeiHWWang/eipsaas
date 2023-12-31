package com.hotent.bpmModel.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hotent.base.exception.BaseException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpmModel.dao.BpmApprovalItemDao;
import com.hotent.bpmModel.manager.BpmApprovalItemManager;
import com.hotent.bpmModel.model.BpmApprovalItem;
import com.hotent.uc.api.impl.util.ContextUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * 常用语管理
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月15日
 */
@Service("bpmApprovalItemManager")
public class BpmApprovalItemManagerImpl extends BaseManagerImpl<BpmApprovalItemDao, BpmApprovalItem> implements BpmApprovalItemManager{
	@Resource
	PortalFeignService PortalFeignService;

	@Override
    @Transactional
	public void addTaskApproval(BpmApprovalItem bpmApprovalItem) throws ClientProtocolException, IOException {
		short type = bpmApprovalItem.getType();
		String curUserId = ContextUtil.getCurrentUserId();
		String[] expressions={StringUtil.join(bpmApprovalItem.getExpression().split("\n"), "")};
		if(type == 1 || type ==4){
			for (String expression : expressions) {
				if (StringUtil.isNotEmpty(expression)) {
					if (getCount(expression,type)>0)
						throw new BaseException("常用语【"+expression+"】已存在");
					BpmApprovalItem approvalItem = new BpmApprovalItem();
					approvalItem.setId(UniqueIdUtil.getSuid());
					approvalItem.setType(type);
					approvalItem.setExpression(expression);
					approvalItem.setUserId(curUserId);
					baseMapper.insert(approvalItem);
				}
				
			}
		}else if(type == 2) {
			if(StringUtil.isEmpty(bpmApprovalItem.getTypeId()))return;
			String[] typeIds = bpmApprovalItem.getTypeId().split(",");
			for (String typeId : typeIds) {
				for (String expression : expressions) {
					if (StringUtil.isNotEmpty(expression)) {
						BpmApprovalItem approvalItem = new BpmApprovalItem();
						approvalItem.setId(UniqueIdUtil.getSuid());
						approvalItem.setType(type);
						approvalItem.setTypeId(typeId);
						approvalItem.setExpression(expression);
						approvalItem.setUserId(curUserId);
						approvalItem.setDefName(PortalFeignService.getSysTypeById(typeId).get("name").asText());
						baseMapper.insert(approvalItem);
					}
				}
			}
			
		}else if(type == 3) {
			if(StringUtil.isEmpty(bpmApprovalItem.getDefKey()))return;
			String[] defKeys = bpmApprovalItem.getDefKey().split(",");
			String names = bpmApprovalItem.getDefName();
			String[] defNames = names.split(",");
			for (int i=0;i<defKeys.length;i++) {
				for (String expression : expressions) {
					if (StringUtil.isNotEmpty(expression)) {
						BpmApprovalItem approvalItem = new BpmApprovalItem();
						approvalItem.setId(UniqueIdUtil.getSuid());
						approvalItem.setType(type);
						approvalItem.setDefKey(defKeys[i]);
						approvalItem.setExpression(expression);
						approvalItem.setDefName(defNames[i]);
						approvalItem.setUserId(curUserId);
						baseMapper.insert(approvalItem);
					}
				}
			}
		}
		if(StringUtil.isNotEmpty(bpmApprovalItem.getId())){
			baseMapper.deleteById(bpmApprovalItem.getId());
		}
			
	}

	private Integer getCount(String expression,int type){
		QueryWrapper wrapper = new QueryWrapper();
		wrapper.eq("expression_",expression);
		wrapper.eq("type_",type);
		return baseMapper.selectCount(wrapper);
	}
	
	
	/**
	 * 取流程常用语。
	 * @param defKey	流程定义Key。
	 * @param typeIdPath	分类的父路径。
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Override
	public List<String> getApprovalByDefKeyAndTypeId(String defKey,String typeId,String userId) throws ClientProtocolException, IOException{
		List<String> taskAppItemsList = new ArrayList<String>();
		String curUserId = StringUtil.isNotEmpty(userId)?userId:ContextUtil.getCurrentUserId();
		//先获取本人的，系统全局的，和指定流程下的常用语
		List<BpmApprovalItem> taskAppItem1=baseMapper.getByDefKeyAndUserAndSys(defKey,curUserId);
		//获取分类为2的所有的常用语,指定流程分类的
		List<BpmApprovalItem> taskAppItem2=baseMapper.getItemByType(BpmApprovalItem.TYPE_FLOWTYPE);
		
		if (BeanUtils.isNotEmpty(taskAppItem1)) {
			for(BpmApprovalItem taskAppItem:taskAppItem1){
				taskAppItemsList.add(taskAppItem.getExpression());
			}
		}
		if (BeanUtils.isNotEmpty(taskAppItem2)&&StringUtil.isNotEmpty(typeId)) {
			//获取分类的父路径
			String typeIdPath = PortalFeignService.getSysTypeById(typeId).get("path").asText();
			String[] typeIds=typeIdPath.split("\\.");
			for (int i=1; i< typeIds.length ;i++) {
				for (BpmApprovalItem taskAppItem:taskAppItem2) {
					if ((taskAppItem.getTypeId().toString()).equals(typeIds[i])) {
						taskAppItemsList.add(taskAppItem.getExpression());
					}
				}
			}
		} 
		//去除重复的元素
		this.removeDuplicate(taskAppItemsList);
		return taskAppItemsList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void removeDuplicate (List<?> list){
		  HashSet h = new HashSet(list);
	      list.clear();
	      list.addAll(h);
	}
	
}
