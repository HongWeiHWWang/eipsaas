package com.hotent.bpm.plugin.core.runtime;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.bpm.api.constant.ExtractType;
import com.hotent.bpm.api.helper.identity.BpmIdentityBuilder;
import com.hotent.bpm.api.helper.identity.BpmIdentityConverter;
import com.hotent.bpm.api.helper.identity.BpmIdentityExtractService;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.plugin.core.def.BpmPluginDef;
import com.hotent.bpm.api.plugin.core.runtime.BpmUserCalcPlugin;
import com.hotent.bpm.api.plugin.core.session.BpmUserCalcPluginSession;
import com.hotent.bpm.plugin.core.plugindef.AbstractUserCalcPluginDef;

/**
 * 抽象用户计算插件，其他的人员计算插件策略实现这个接口。
 * <pre> 
 * 构建组：x5-bpmx-plugin-core
 * 作者：ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2014-7-8-下午2:29:44
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public abstract class AbstractUserCalcPlugin  implements BpmUserCalcPlugin{
	//是否是预览模式
	private static ThreadLocal<Boolean> isPreVrewModel = new ThreadLocal<Boolean>() ;
	
	/**
	 * 是否预览模式。
	 * @return
	 */
	public static boolean isPreviewMode(){
		Boolean  preVrewModel = isPreVrewModel.get();
		if(preVrewModel != null && preVrewModel){
			return true;
		}
		return false;
	}
	
	/**
	 * 清除预览模式。
	 */
	public static void cleanPreviewMode(){
		isPreVrewModel.remove();
	}
	
	/**
	 * 设置预览模式。
	 * @param isPreview
	 */
	public static void setPreviewMode(Boolean isPreview){
		isPreVrewModel.set(isPreview);
	}

	
	private BpmIdentityConverter bpmIdentityConverter;
	
	private BpmIdentityBuilder bpmIdentityBuilder;
	
	private BpmIdentityExtractService extractService = AppUtil.getBean(BpmIdentityExtractService.class);
	
	protected BpmIdentityConverter getBpmIdentityConverter() {
		if(bpmIdentityConverter==null)
			bpmIdentityConverter = (BpmIdentityConverter) AppUtil.getBean("bpmIdentityConverter");
		return bpmIdentityConverter;		
	}

	protected BpmIdentityBuilder getBpmIdentityBuilder() {
		if(bpmIdentityBuilder==null)
			bpmIdentityBuilder = AppUtil.getBean(BpmIdentityBuilder.class);
		return bpmIdentityBuilder;		
	}
	
	/**
	 * 插件查询数据，由具体类进行实现。
	 * @param pluginSession
	 * @param pluginDef
	 * @return 
	 * List&lt;BpmIdentity>
	 * @throws Exception 
	 */
	protected abstract List<BpmIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) throws Exception;
	
	/**
	 * 查询人员。
	 * @throws Exception 
	 */
	public List<BpmIdentity> execute(BpmUserCalcPluginSession pluginSession, BpmPluginDef pluginDef) throws Exception{
		// 如果是预览模式，而且当前插件不支持预览。则返回空集合。
		 if(isPreviewMode()){
			if(!this.supportPreView()) return Collections.emptyList();
		 }
		
		List<BpmIdentity>  list=queryByPluginDef(pluginSession,pluginDef);
		if(BeanUtils.isEmpty(list)) return list;
		
		ExtractType extractType = ((AbstractUserCalcPluginDef)pluginDef).getExtract();
		//排除重复选择，使用LinkedHashSet进行排除重复。
		Set<BpmIdentity> set=new LinkedHashSet<BpmIdentity>();
		List<BpmIdentity> rtnList=new ArrayList<BpmIdentity>();
		
		//根据抽取类型查询人员。
		list = extract(list, extractType);
		 
		set.addAll(list);
		 
		rtnList.addAll(set);
		
		
		 
		return rtnList;
	}
	
	
	
	
	/**
	 * 抽取处理
	 * @param bpmIdentities 待处理的实体集合 
	 * @param extractType 抽取类型（通过getExtractType方法获得）
	 * @param userRelGroupType 用户和用户组的关系类型键（key）
	 * void
	 */
	@SuppressWarnings("unchecked")
	protected List<BpmIdentity> extract(List<BpmIdentity> bpmIdentities,ExtractType extractType) {
		//数据有效性判断
		if(BeanUtils.isEmpty(bpmIdentities)) return Collections.EMPTY_LIST;		

		//临时存储抽取结果
		List<BpmIdentity> results = new ArrayList<BpmIdentity>();
		
		//根据类型做不同处理
		switch(extractType){
			//不抽取
			case EXACT_NOEXACT:				
				results = bpmIdentities;
				break;
			//抽取人员
			case EXACT_EXACT_USER:			
				results = extractService.extractBpmIdentity(bpmIdentities);
				break;
			//组合组用户
			case EXACT_GROUP_USER:
				results = extractService.extractUserGroup(bpmIdentities);
				break;
			//延迟抽取,适用会签节点.
			case EXACT_EXACT_DELAY:
				for(BpmIdentity identity:bpmIdentities){
					if(BpmIdentity.TYPE_GROUP.equals(identity.getType())){
						identity.setExtractType(ExtractType.EXACT_EXACT_DELAY);
						results.add(identity);
					}
				}
				break;
		}
		
		return results;
	}
	/**
	 * 是否支持预览。如果不支持子类重写改方法。
	 * @return
	 */
	public  boolean supportPreView(){
		return true;
	}
}
