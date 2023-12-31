package com.hotent.uc.manager;

import java.util.List;
import java.util.Map;

import com.hotent.uc.model.Properties;
import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.uc.params.properties.PropertiesVo;


/**
 * 
 * <pre> 
 * 描述：portal_sys_properties 处理接口
 * 构建组：x5-bpmx-platform
 * 作者:ray
 * 邮箱:zhangyg@jee-soft.cn
 * 日期:2016-07-28 09:19:53
 * 版权：广州宏天软件有限公司
 * </pre>
 */
public interface PropertiesService extends   BaseManager<Properties>{
	
	/**
	 * 删除所有已逻辑删除的实体（物理删除）
	 * @param entityId 实体对象ID
	 */
	Integer removePhysical();
	
	
	
	
	/**
	 * 分组列表。
	 * @return
	 */
	List<String> getGroups();
	
	/**
	 * 判断别名是否存在。
	 * @param sysProperties
	 * @return 
	 */
	boolean isExist(Properties properties);
	
	
	
	/**
	 * 重新读取属性配置。
	 * @return
	 */
	Map<String,String>  reloadProperty();
	
	/**
     * 通过别名获取系统属性对象
     */
    Properties getPropertiesByCode(String code);
        
    void removeByCode(String ...codes);  
    /**
	 * 根据别名返回参数值。
	 * @param code
	 * @return
	 */
	String getByCode(String code);
	/**
	 * 根据别名返回参数值。
	 * @param code	别名	
	 * @param defaultValue	默认值
	 * @return
	 */
	String getByCode(String code,String defaultValue);
	/**
	 * 根据别名获取int参数值。
	 * @param code
	 * @return
	 */
	Integer getIntByCode(String code);
	/**
	 * 根据别名获取参数值。
	 * @param code
	 * @param defaulValue
	 * @return
	 */
	Integer getIntByCode(String code,Integer defaulValue);
	
	/**
	 * 根据别名获取长整型参数值。
	 * @param code
	 * @return
	 */
	Long getLongByCode(String code);
	/**
	 * 根据别名获取布尔型参数值。
	 * @param code
	 * @return
	 */
	boolean getBooleanByCode(String code);
	
	/**
	 * 根据别名获取布尔型参数值。
	 * @param code
	 * @param defaulValue
	 * @return
	 */
	boolean getBooleanByCode(String code,boolean defaulValue);
	
	/**
	 * 更新系统参数
	 * @param relTypeVo
	 * @return
	 * @throws Exception
	 */
	CommonResult<String> updateProperties(PropertiesVo propertiesVo) throws Exception;
}
