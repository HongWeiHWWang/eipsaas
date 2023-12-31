package com.hotent.bpm.persistence.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.hotent.base.manager.BaseManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.persistence.model.BpmDefData;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.query.BpmDefFieldSorts;
import com.hotent.bpm.persistence.model.query.BpmDefQueryFields;
import com.hotent.uc.api.model.IUser;

public interface BpmDefinitionManager extends BaseManager<DefaultBpmDefinition> {
	/**
	 * 
	 * 查询流程定义实体，并查询它的一对一子表的流程定义数据
	 * 
	 * @param entityId
	 * @return DefaultBpmDefinition
	 */
	DefaultBpmDefinition getById(String entityId);

	/**
	 * 
	 * 根据流程业务主键查询和该主键一致的流程定义（该定义记录为主版本）（默认不需要设置的数据）
	 * 
	 * @param defKey
	 * @return DefaultBpmDefinition
	 */
	DefaultBpmDefinition getMainByDefKey(String defKey);
	/**
	 * 
	 * 根据流程业务主键查询和该主键一致的流程定义（该定义记录为主版本）
	 * 
	 * @param defKey
	 * @param needData 是否需要设置的数据
	 * @return DefaultBpmDefinition
	 */
	DefaultBpmDefinition getMainByDefKey(String defKey, boolean needData);

	/**
	 * 根据流程主键查询所有流程定义
	 * 
	 * @param defKey
	 * @return List<DefaultBpmDefinition>
	 */
	List<DefaultBpmDefinition> queryByDefKey(String defKey);

	/**
	 * 根据流程创建人查询所有自己创建的流程定义key
	 * 
	 * @param userId
	 * @return List<DefaultBpmDefinition>
	 */
	List<String> queryByCreateBy(String userId);

	/**
	 * 根据流程主键查询所有历史流程定义
	 * 
	 * @param defKey
	 * @return List<DefaultBpmDefinition>
	 */
	List<DefaultBpmDefinition> queryHistorys(String defKey);

	/**
	 * 对传入的流程定义克隆一份，将新产生的流程定义设置为主版本，并更新其它版本的流程定义为历史版本 （传入的流程定义可以是主版本，也可以是历史版本）
	 * 
	 * @param bpmDefinition
	 * @return DefaultBpmDefinition
	 */
	DefaultBpmDefinition cloneToMain(DefaultBpmDefinition bpmDefinition);

	/**
	 * 根据流程主键获得最大的版本号
	 * 
	 * @param defKey
	 * @return
	 */
	Integer getMaxVersion(String defKey);

	/**
	 * 根据查询条件、条件关系、分页对象（其中包含排序）进行流程定义的查询。（这个方法支持分页）
	 * 单元测试类见BpmDefinitionManagerTest
	 * 
	 * @param bpmDefQueryFields
	 *            查询条件
	 * @param fieldRelation
	 *            条件关系
	 * @param page
	 *            分页对象
	 * @return
	 */
	List<DefaultBpmDefinition> query(BpmDefQueryFields bpmDefQueryFields,
			FieldRelation fieldRelation, PageBean page);

	/**
	 * 根据查询条件、条件关系、排序条件进行流程定义的查询。（这个方法不支持分页）
	 * 
	 * @param bpmDefQueryFields
	 *            查询条件
	 * @param fieldRelation
	 *            条件关系
	 * @param bpmDefFieldSorts
	 *            分页对象
	 * @return
	 */
	List<DefaultBpmDefinition> query(BpmDefQueryFields bpmDefQueryFields,
			FieldRelation fieldRelation, BpmDefFieldSorts bpmDefFieldSorts);

	/**
	 * 根据查询条件进行流程定义的查询（包含分管授权）。
	 * 
	 * @param queryFilter
	 *            查询条件
	 * @return 流程定义列表
	 * @throws IOException 
	 */
	PageList<DefaultBpmDefinition> queryList(QueryFilter queryFilter) throws IOException;

	PageList<DefaultBpmDefinition> queryList(QueryFilter queryFilter,IUser user) throws IOException;
	
	/**
	 * 级联删除流程，包括历史数据、流程实例、任务实例、流程节点配置、流程本身
	 * 
	 * @param entityId
	 *            流程定义主键ID
	 * @throws Exception 
	 */
	void removeCascade(String entityId) throws Exception;

	/**
	 * 按bpmn定义ID获取流程定义
	 * 
	 * @param bpmnDefId
	 * @return defId
	 */
	String getDefIdByBpmnDefId(String bpmnDefId);

	/**
	 * 将流程更新到主版本。
	 * 
	 * @param entityId
	 *            void
	 */
	void updMainVersion(String entityId);

	/**
	 * 通过BPMN的流程定义ID获取流程定义实体
	 * 
	 * @param bpmnDefId
	 * @return
	 */
	DefaultBpmDefinition getByBpmnDefId(String bpmnDefId);

	/**
	 * 通过BPMN的流程定义发布ID获取流程定义实体
	 * 
	 * @param bpmnDeployId
	 * @return
	 */
	DefaultBpmDefinition getByBpmnDeployId(String bpmnDeployId);

	/**
	 * 更新流程定义的XML
	 * 
	 * @param defId
	 * @param defXml
	 * @return String 返回转化后后的BPMN XML
	 * @throws Exception 
	 */
	String updateBpmDefXml(String defId, String defXml) throws Exception;

	/**
	 * 更新流程状态
	 * 
	 * @param defId
	 * @param status
	 *            void
	 */
	void updateStatus(String defId, String status);

	/**
	 * 根据流程定义ID更新流程的定义XML。
	 * 
	 * @param defId
	 * @param bpmnXml
	 *            void
	 */
	void updBpmData(String defId, BpmDefData bpmDefData);

	/**
	 * 发布流程定义。
	 * 
	 * @param bpmDefinition
	 *            void
	 * @throws Exception 
	 */
	boolean deploy(BpmDefinition bpmDefinition) throws Exception;

	/**
	 * 更新流程定义。
	 * @param bpmDefinition
	 * @return boolean
	 * @throws Exception 
	 */
	boolean updateBpmDefinition(BpmDefinition bpmDefinition) throws Exception;
	
	
	/**
	 * 更新流程分类。
	 * @param typeId		流程分类
	 * @param defList 		流程定义列表
	 * void
	 */
	void updDefType(String typeId,String typeName,List<String> defList);

	/**
	 * 保存草稿。
	 * 
	 * @param bpmDefinition
	 * @return boolean
	 * @throws Exception 
	 */
	boolean saveDraft(BpmDefinition bpmDefinition) throws Exception;
	
	
	/**
	 * 根据map查询查询流程定义列表。
	 * @param map
	 * @return  List&lt;DefaultBpmDefinition>
	 */
	List<DefaultBpmDefinition> queryListByMap(Map<String,Object> map);
	
	
	/**
	 * 根据流程定义ID删除。
	 * @param defId 
	 * void
	 */
	void removeDefId(String defId);

	
	/**
	 * 根据流程定义ID删除相关的记录
	 * @param cascade	是否级联删除该流程的其他版本
	 * @param defIds
	 * @throws Exception
	 */
	void removeDefIds(boolean cascade, Boolean isVersion,String ...defIds) throws Exception;

	void copyDef(String defId, String name, String defKey) throws Exception;
	
	/**
	 * 修改流程定义状态。
	 * 
	 * @param bpmDefinition
	 * @param oldStatus
	 *            void
	 * @throws Exception 
	 */
	void updBpmDefinitionStatus(DefaultBpmDefinition bpmDefinition, String oldStatus) throws Exception;

    /**
     * 更加定义ID、关联锁版本查询表单定义信息
     * @param map
     * @return
     */
    public DefaultBpmDefinition getBpmDefinitionByRev(Map<String,Object> map);
    
    /**
     * 更新流程key更新历史版本的流程分类id
     * @param defKey
     * @param typeId
     */
    void updateTypeIdByDefKey(String defKey, String typeId);

	CommonResult<String> setDefType(String typeName, String typeId, List<String> defIds);

	List<Map<String,Object>> getDefCount(QueryFilter queryFilter) throws Exception;
	
	/**
	 * 根据分类ID集合查询流程defKey
	 * @param typeIds
	 * @return
	 */
	List<String> queryByTypeId(List<String> typeIds);


	/**
	 * 获取流程绑定关系
	 * @param defId
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getBindRelation(String defId) throws Exception;

	List<Map<String, String>> bpmDefinitionData(String alias);
	
	/**
	 * 将导入文件暂存在缓存中
	 * @param fileId
	 * @param fileJson
	 * @return
	 */
	String putImportFileInCache(String fileId, String fileJson);
	
	/**
	 * 从缓存中取出暂存文件
	 * @param fileId
	 * @return
	 */
	String getImportFileFromCache(String fileId);
	
	/**
	 * 删除缓存中的暂存文件
	 * @param fileId
	 * @return
	 */
	void delImportFileFromCache(String fileId);
}
