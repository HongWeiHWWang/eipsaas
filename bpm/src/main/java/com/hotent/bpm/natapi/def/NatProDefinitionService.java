package com.hotent.bpm.natapi.def;

import java.io.UnsupportedEncodingException;

import com.hotent.bpm.api.constant.DesignerType;

/**
 * 
 * <pre> 
 * 描述：原生的流程定义服务接口
 * 构建组：x5-bpmx-native-api
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-11-22-上午9:21:15
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface NatProDefinitionService {
	
	/**
	 * 发布流程定义
	 * @param name 流程的名称
	 * @param defXml 发布XML
	 * @return 
	 * String 发布ID
	 */
	String deploy(String tenantId, String name,String defXml) throws UnsupportedEncodingException;
	

	/**
	 * 获取流程定义XML
	 * @param 流程定义发布ID
	 * @return 
	 * String
	 */
	String getDefXmlByDeployId(String deployId);
	/**
	 * 修改流程定义XML
	 * @param deployId 流程定义发布ID
	 * @param defXml 新XML
	 * @return void
	 */
	void writeDefXml(String deployId,String defXml);
	
	
	
	/**
	 * 通过发布ID取得流程定义ID
	 * @param deployId
	 * @return 
	 * String
	 */
	String getProcessDefinitionIdByDeployId(String deployId);
	
	
	/**
	 * 根据流程定义ID删除缓存。
	 * @param bpmnDefId
	 * @return 
	 * String
	 */
	void clearCacheByBpmnDefId(String bpmnDefId);
	
	
	
	
	/**
	 * 根据类型获取转换。
	 * @param designerType
	 * @return 
	 * DefTransform
	 */
	DefTransform getDefTransform(DesignerType designerType);
	
	
		
}
