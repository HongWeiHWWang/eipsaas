package com.hotent.bpm.persistence.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;

/**
 * 
 * <pre> 
 * 描述：流程定义
 * 构建组：x5-bpmx-core
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2013-12-23-上午8:41:25
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public interface BpmDefinitionDao extends BaseMapper<DefaultBpmDefinition> {
	
	/**
	 * 根据流程主键将流程更新为主版本。
	 * @param mainDefId
	 * @param defKey 
	 * void
	 */
	void updateNotMainVersion(@Param("defId") String defId);
	void updateToMainVersion(@Param("defId") String defId);
	/**
	 * 根据流程业务主键查询和该主键一致的流程定义（该定义记录为主版本）
	 * @param defKey
	 * @return 
	 * DefaultBpmDefinition
	 */
	DefaultBpmDefinition getMainByDefKey(@Param("defKey")String defKey);
	
	/**
	 * 获得流程业务主键相同的最大版本号记录
	 * @param defKey
	 * @return 
	 * DefaultBpmDefinition
	 */
	Integer getMaxVersion(@Param("defKey")String defKey);
	
	/**
	 * 根据流程业务主键查询流程定义记录
	 * @param defKey
	 * @return 
	 * List<DefaultBpmDefinition>
	 */
	List<DefaultBpmDefinition> queryByDefKey(@Param("defKey")String defKey);
	
	/**
	 * 根据流程创建人查询所有自己创建的流程定义defKey
	 * @param userId
	 * @return 
	 * List<DefaultBpmDefinition>
	 */
	public List<String> queryDefKeyByCreateBy(String userId);
	
	/**
	 * 根据分类ID获取所有的流程定义defKey
	 * @param typeId
	 * @return
	 */
	public List<String> queryDefKeyByTypeId(@Param("typeIds")List<String> typeIds);
	
	/**
	 * 根据流程业务主键查询流程定义历史记录
	 * @param defKey
	 * @return 
	 * List<DefaultBpmDefinition>
	 */
	List<DefaultBpmDefinition> queryHistorys(@Param("defKey")String defKey);
	
	/**
	 * 按BpmnDefID获取流程定义
	 * @param bpmnDefId
	 * @return 
	 * DefaultBpmDefinition
	 */
	String getDefIdByBpmnDefId(@Param("bpmnDefId")String bpmnDefId);
	/**
	 * 通过BPMN的流程定义ID获取流程定义实体
	 * @param bpmnDefId
	 * @return
	 */
	DefaultBpmDefinition getByBpmnDefId(@Param("bpmnDefId") String bpmnDefId);
	/**
	 * 通过BPMN的流程定义发布获取流程定义实体
	 * @param bpmnDeployId
	 * @return
	 */
	DefaultBpmDefinition getByBpmnDeployId(@Param("bpmnDeployId") String bpmnDeployId);
	
	/**
	 * 更新状态
	 * @param defId
	 * @param status 
	 * void
	 */
	void updateStatus(@Param("defId") String defId,@Param("status") String status);
	
	/**
	 * 根据KEY获取流程定义列表。 
	 * @param defKey
	 * @return 
	 * List&lt;DefaultBpmDefinition>
	 */
	List<DefaultBpmDefinition> getByDefKey(@Param("defKey") String defKey);
	
	/**
	 * 根据流程key删除流程定义。
	 */
	void delByKey(@Param("defKey") String defKey);
	
	/**
	 * 根据流程定义key删除。
	 * act_re_deployment
	 * @param defKey 
	 * void
	 */
	void delActDeploy(@Param("defKey") String defKey);
	
	/**
	 * 根据流程定义删除。
	 * act_ge_bytearray
	 * @param defKey 
	 * void
	 */
	void delActByteArray(@Param("defKey") String defKey);
	
	/**
	 * 根据流程定义删除。
	 * act_re_procdef。
	 * @param defKey 
	 * void
	 */
	void delActDef(@Param("defKey") String defKey);
	
	/**
	 * 根据流程定义删除activty流程运行数据。
	 * act_re_procdef。
	 * @param defKey 
	 * void
	 */
	void delActRunExecution(@Param("defKey")String defKey);
	
	void delActRunIdentitylink(@Param("defKey")String defKey);
	
	void delActRunVariable(@Param("defKey")String defKey);
	
	/**
	 * 根据map获取流程定义。
	 * @param map
	 * @return  List&lt;DefaultBpmDefinition>
	 */
	List<DefaultBpmDefinition> queryListByMap(Map<String,Object> map);
	
	
	/**
	 * 更新流程定义分类。
	 * @param typeId	分类ID
	 * @param defList 	流程定义列表
	 * void
	 */
	void updDefType(@Param("typeId") String typeId,@Param("typeName") String typeName,@Param("defList") List<String> defList);
	
	
	/**
	 * 根据流程定义id删除 act_re_deployment表记录。
	 * @param defId 
	 * void
	 */
	void delActDeployByDefId(@Param("defId")String defId);
	
	
	/**
	 * 根据流程定义id删除 act_re_procdef表记录。
	 * @param defId 
	 * void
	 */
	void delActDefByDefId(@Param("defId")String defId);
	
	/**
	 * 根据流程定义id删除 act_ge_bytearray表记录。
	 * @param defId 
	 * void
	 */
	void delActByteArrayByDefId(@Param("defId")String defId);
	
	
	/**
	 * 根据流程bpmnDefId删除。
	 * act_ru_task。
	 * @param defId 
	 * void
	 */
	void delActTask(@Param("defId")String defId);

    /**
     * 更加定义ID、关联锁版本查询表单定义信息
     * @param map
     * @return
     */
    public DefaultBpmDefinition getBpmDefinitionByRev(Map<String,Object> map);

	/**
	 * 获取各分类下的流程
	 * @param param
	 * @return
	 */
	List<Map<String,Object>> getDefCount(@Param(Constants.WRAPPER) Wrapper<DefaultBpmDefinition> param);

	/**
	 * 获取流程
	 * @param alias
	 * @return
	 */
	List<Map<String,String>> getBpmDefinitionData(String alias);

	void updateTypeIdByDefKey(@Param("defKey")String defKey,@Param("typeId") String typeId);
	void updateInstTypeIdByDefKey(@Param("defKey")String defKey,@Param("typeId") String typeId);
	void updateInstHiTypeIdByDefKey(@Param("defKey")String defKey,@Param("typeId") String typeId);
	void updateTaskTypeIdByDefKey(@Param("defKey")String defKey,@Param("typeId") String typeId);
	void updateTaskNoticeTypeIdByDefKey(@Param("defKey")String defKey,@Param("typeId") String typeId);
	
}
