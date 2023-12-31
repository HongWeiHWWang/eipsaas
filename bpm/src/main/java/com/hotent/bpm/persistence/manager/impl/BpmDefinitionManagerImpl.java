package com.hotent.bpm.persistence.manager.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.cache.annotation.CacheEvict;
import com.hotent.base.cache.annotation.CachePut;
import com.hotent.base.cache.annotation.Cacheable;
import com.hotent.base.cache.annotation.FirstCache;
import com.hotent.base.exception.WorkFlowException;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.Direction;
import com.hotent.base.query.FieldRelation;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.Dom4jUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bpm.api.constant.DesignerType;
import com.hotent.bpm.api.constant.NodeType;
import com.hotent.bpm.api.event.BpmDefinitionDelEvent;
import com.hotent.bpm.api.model.identity.BpmIdentity;
import com.hotent.bpm.api.model.process.def.BpmDefinition;
import com.hotent.bpm.api.model.process.def.BpmProcessDef;
import com.hotent.bpm.api.model.process.def.BpmProcessDefExt;
import com.hotent.bpm.api.model.process.nodedef.BpmNodeDef;
import com.hotent.bpm.api.model.process.nodedef.ext.SubProcessNodeDef;
import com.hotent.bpm.api.service.BpmDefinitionAccessor;
import com.hotent.bpm.api.service.BpmDefinitionService;
import com.hotent.bpm.engine.def.DefXmlTransForm;
import com.hotent.bpm.engine.def.impl.DefaultBpmDefConditionService;
import com.hotent.bpm.listener.BpmDefinitionListener;
import com.hotent.bpm.model.form.Form;
import com.hotent.bpm.model.identity.DefaultBpmIdentity;
import com.hotent.bpm.natapi.def.DefTransform;
import com.hotent.bpm.natapi.def.NatProDefinitionService;
import com.hotent.bpm.persistence.dao.ActTaskDao;
import com.hotent.bpm.persistence.dao.BpmDefinitionDao;
import com.hotent.bpm.persistence.dao.BpmProcessInstanceDao;
import com.hotent.bpm.persistence.manager.BpmDefAuthorizeManager;
import com.hotent.bpm.persistence.manager.BpmDefDataManager;
import com.hotent.bpm.persistence.manager.BpmDefinitionManager;
import com.hotent.bpm.persistence.manager.BpmInstFormManager;
import com.hotent.bpm.persistence.manager.BpmProBoManager;
import com.hotent.bpm.persistence.manager.BpmProcessInstanceManager;
import com.hotent.bpm.persistence.manager.BpmSecretaryManageManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeDoneManager;
import com.hotent.bpm.persistence.manager.BpmTaskNoticeManager;
import com.hotent.bpm.persistence.manager.BpmTaskTurnManager;
import com.hotent.bpm.persistence.model.AuthorizeRight;
import com.hotent.bpm.persistence.model.BpmDefAuthorizeType.BPMDEFAUTHORIZE_RIGHT_TYPE;
import com.hotent.bpm.persistence.model.BpmDefData;
import com.hotent.bpm.persistence.model.BpmProBo;
import com.hotent.bpm.persistence.model.BpmSecretaryManage;
import com.hotent.bpm.persistence.model.DefaultBpmDefinition;
import com.hotent.bpm.persistence.model.DefaultBpmProcessDefExt;
import com.hotent.bpm.persistence.model.DefaultBpmProcessInstance;
import com.hotent.bpm.persistence.model.query.BpmDefFieldSorts;
import com.hotent.bpm.persistence.model.query.BpmDefQueryFields;
import com.hotent.bpm.persistence.util.BpmnXmlValidateUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;
import com.hotent.uc.api.service.IUserService;



@Service
public class BpmDefinitionManagerImpl extends BaseManagerImpl<BpmDefinitionDao, DefaultBpmDefinition> implements BpmDefinitionManager {
	private final Log logger = LogFactory.getLog(getClass());

	@Resource
	BpmDefinitionDao bpmDefinitionDao;
	@Resource
	BpmDefDataManager bpmDefDataManager;
	@Resource
	BpmProcessInstanceManager bpmProcessInstanceManager;
	@Resource
	NatProDefinitionService natProDefinitionService;
	@Resource
	DefXmlTransForm defXmlTransForm;
	@Resource
	BpmDefAuthorizeManager bpmDefAuthorizeManager;
	@Resource 
	BpmProBoManager bpmProBoManager;
	@Resource 
	FormFeignService formRestfulService;
	@Resource
	IUserService userServiceImpl;
	@Resource
	ActTaskDao actTaskDao;
	@Resource
	BpmDefinitionService bpmDefinitionService;
	@Resource
	DefaultBpmDefConditionService bpmDefHandler;
    @Resource
    BpmTaskNoticeManager bpmTaskNoticeManager;
    @Resource
    BpmTaskNoticeDoneManager bpmTaskNoticeDoneManager;
    @Resource
    BpmTaskTurnManager bpmTaskTurnManager;
    @Resource
    BpmDefinitionManager bpmDefinitionManager;
    @Resource
    BpmProcessInstanceDao bpmProcessInstanceDao;
    @Resource
    BpmInstFormManager bpmInstFromManager;
	@Resource
	BpmDefinitionAccessor bpmDefinitionAccessor;

	/**
	 * 添加缓存
	 * @param def
	 */
	private void addCache(DefaultBpmDefinition def) {
		Assert.notNull(def, "流程定义为空");
		String defId = def.getDefId();
		BpmDefinitionManagerImpl bean = AppUtil.getBean(getClass());
		// 缓存流程定义
		bean.putDefInCache(def);
		// 缓存bpmnId与defId
		bean.putBpmnDefIdInCache(def.getBpmnDefId(), defId);
		
		// 如果是主版本，把当前defKey添加到缓存中
		if(def.isMain()) {
			bean.putFlowKeyInCache(def.getDefKey(), defId);
		}
	}
	
	/**
	 * 将流程定义放入缓存
	 * <p>以defId为key，以流程定义为值</p>
	 * @param def
	 * @return
	 */
	@CachePut(value = BpmDefinition.BPM_DEF_CACHENAME, key = "#def.defId")
	protected DefaultBpmDefinition putDefInCache(DefaultBpmDefinition def) {
		return def;
	}
	
	/**
	 * 将bpmnId与defId作为键值对存放在缓存中
	 * @param bpmnDefId
	 * @param defId
	 * @return
	 */
	@CachePut(value = BpmDefinition.BPMN_ID_CACHENAME, key = "#bpmnDefId")
	protected String putBpmnDefIdInCache(String bpmnDefId, String defId) {
		return defId;
	}
	
	/**
	 * 将flowKey与defId作为键值对存放在缓存中
	 * @param flowKey
	 * @param defId
	 * @return
	 */
	@CachePut(value = BpmDefinition.FLOW_KEY_CACHENAME, key = "#flowKey")
	protected String putFlowKeyInCache(String flowKey, String defId) {
		return defId;
	}
	
	/**
	 * 读取缓存。
	 * 
	 * @param defId
	 * @return DefaultBpmDefinition
	 */
	@Cacheable(value = BpmDefinition.BPM_DEF_CACHENAME, key="#defId")
	protected DefaultBpmDefinition getFromCache(String defId) {
		return null;
	}

	/**
	 * 根据流程定义ID获取DEFID
	 * 
	 * @param bpmnDefId
	 * @return String
	 */
	@Cacheable(value = BpmDefinition.BPMN_ID_CACHENAME, key="#bpmnDefId")
	protected String getDefIdByBpmnIdFromCache(String bpmnDefId) {
		return null;
	}

	private void publishEvent(BpmDefinition def) {
		// 添加license4eip7 限制包会导致无法更新缓存,需要添加下面这行代码
		AppUtil.getBean(BpmDefinitionListener.class);
		// 清除相关流程定义缓存。
		List<DefaultBpmDefinition> defList = bpmDefinitionDao.getByDefKey(def.getDefKey());
		for (DefaultBpmDefinition defEntity : defList) {
			AppUtil.publishEvent(new BpmDefinitionDelEvent(defEntity));
		}
		
		// AppUtil.publishEvent(new BpmDefinitionDelEvent(def));
	}

	@Override
    @Transactional
	public DefaultBpmDefinition getById(String entityId) {
		if (StringUtil.isEmpty(entityId)) {
			return null;
		}
		
		BpmDefinitionManagerImpl bean = AppUtil.getBean(getClass());

		DefaultBpmDefinition bpmDef = bean.getFromCache(entityId);

		if (bpmDef != null) {
			return bpmDef;
		}
		DefaultBpmDefinition defaultBpmDefinition = get(entityId);
		if(defaultBpmDefinition==null) {
			return null;
		}
		BpmDefData bpmDefData = bpmDefDataManager.get(entityId);
		defaultBpmDefinition.setBpmDefData(bpmDefData);
		// 添加缓存。
		addCache(defaultBpmDefinition);

		return defaultBpmDefinition;
	}

	@Override
	public DefaultBpmDefinition getMainByDefKey(String defKey) {
		return getMainByDefKey(defKey, false);
	}

	@Override
	public DefaultBpmDefinition getMainByDefKey(String defKey, boolean needData) {
		DefaultBpmDefinition bpmDef = bpmDefinitionDao.getMainByDefKey(defKey);;
		if (bpmDef == null || !needData) {
			return bpmDef;
		}
		// 流程定义XML数据。
		BpmDefData bpmDefData = bpmDefDataManager.get(bpmDef.getDefId());
		bpmDef.setBpmDefData(bpmDefData);
		return bpmDef;
	}

	@Override
    @Transactional
	public void create(DefaultBpmDefinition entity) {
		super.create(entity);

		if (StringUtil.isEmpty(entity.getBpmDefData().getId())) {
			entity.getBpmDefData().setId(entity.getDefId());
		}
		bpmDefDataManager.create(entity.getBpmDefData());
	}

	@Override
    @Transactional
	public void update(DefaultBpmDefinition entity) {
		super.update(entity);

		if (StringUtil.isNotEmpty(entity.getBpmDefData().getId())) {
			bpmDefDataManager.update(entity.getBpmDefData());
		}
		// 清理缓存
		publishEvent(entity);
	}

	@Override
    @Transactional
	public void removeCascade(String defId) throws Exception {
		BpmDefinition bpmDef = this.get(defId);
		String defKey = bpmDef.getDefKey();
		// 删除时清除缓存。
		List<DefaultBpmDefinition> defList = bpmDefinitionDao.getByDefKey(defKey);
		for (DefaultBpmDefinition def : defList) {
			publishEvent(def);
		}

		List<DefaultBpmProcessInstance> instList = bpmProcessInstanceManager.getListByBpmnDefKey(defKey);

		for (DefaultBpmProcessInstance inst : instList) {
			bpmProcessInstanceManager.physicsRemove(inst.getId());
		}
		
		//删除Actviti流程任务
		if(StringUtil.isNotEmpty(bpmDef.getBpmnDefId())){
			bpmDefinitionDao.delActTask(bpmDef.getBpmnDefId());
		}
		
		// 删除流程数据表。
		removeActviti(defKey);
		// 删除平台流程数据。
		removeDef(defKey);
	}

	/**
	 * 删除流程平台数据。
	 * 
	 * @param defKey
	 *            void
	 * @throws Exception 
	 */
    @Transactional
	private void removeDef(String defKey) throws Exception {
		bpmDefDataManager.delByDefKey(defKey);
		bpmDefinitionDao.delByKey(defKey);
		// 删除表单权限
		ObjectNode params=JsonUtil.getMapper().createObjectNode();
		params.put("flowKey", defKey);
		params.put("parentFlowKey", "");
		params.put("permissionType", 1);
		formRestfulService.removeFormRightByFlowKey(params);
		// 删除表单权限
		ObjectNode params1=JsonUtil.getMapper().createObjectNode();
		params1.put("flowKey", defKey);
		params1.put("parentFlowKey", "");
		params1.put("permissionType", 2);
	    formRestfulService.removeFormRightByFlowKey(params1);
	}

	/**
	 * 删除流程引擎的流程数据。
	 * 
	 * @param defKey
	 *            void
	 */
    @Transactional
	private void removeActviti(String defKey) {
		bpmDefinitionDao.delActByteArray(defKey);
		bpmDefinitionDao.delActDeploy(defKey);
		bpmDefinitionDao.delActRunIdentitylink(defKey);
		bpmDefinitionDao.delActRunVariable(defKey);
		bpmDefinitionDao.delActRunExecution(defKey);
		bpmDefinitionDao.delActDef(defKey);
	}

	@Override
    @Transactional
	public DefaultBpmDefinition cloneToMain(DefaultBpmDefinition bpmDefinition) {
		// 克隆流程定义并新建
		DefaultBpmDefinition newBpmDefinition = (DefaultBpmDefinition) bpmDefinition.clone();
		newBpmDefinition.setDefId(UniqueIdUtil.getSuid());
		// 将新的流程定义设置为主版本
		newBpmDefinition.setIsMain("Y");
		// 设置版本号
		Integer maxVersion = getMaxVersion(bpmDefinition.getDefKey());
		newBpmDefinition.setVersion(maxVersion);
		newBpmDefinition.setCreateTime(LocalDateTime.now());
		super.create(newBpmDefinition);

		// 更新其他版本为非主版本，更新当前的为主版本。
		updMainVersion(newBpmDefinition.getDefId());

		// 创建流程定义XML数据记录
		newBpmDefinition.getBpmDefData().setId(newBpmDefinition.getDefId());
		bpmDefDataManager.create(newBpmDefinition.getBpmDefData());

		return newBpmDefinition;
	}

	@Override
	public List<DefaultBpmDefinition> queryByDefKey(String defKey) {
		return bpmDefinitionDao.queryByDefKey(defKey);
	}

	@Override
	public List<DefaultBpmDefinition> queryHistorys(String defKey) {
		return bpmDefinitionDao.queryHistorys(defKey);
	}

	@Override
	public Integer getMaxVersion(String defKey) {
		Integer version = bpmDefinitionDao.getMaxVersion(defKey);
		if (version != null) {
			return version + 1;
		}
		return 1;
	}

	@SuppressWarnings("unchecked")
	public List<DefaultBpmDefinition> query(BpmDefQueryFields bpmDefQueryFields, FieldRelation fieldRelation, PageBean page) {
		QueryFilter queryFilter = QueryFilter.build();
		// 查询条件
		// 排序和分页
		queryFilter.setQuerys(bpmDefQueryFields.getQueryFields());
		queryFilter.setPageBean( page);
		return (List<DefaultBpmDefinition>) this.query(queryFilter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DefaultBpmDefinition> query(BpmDefQueryFields bpmDefQueryFields, FieldRelation fieldRelation, BpmDefFieldSorts bpmDefFieldSorts) {
		QueryFilter queryFilter = QueryFilter.build();
		// 查询条件
		// 设置
		queryFilter.setQuerys(bpmDefQueryFields.getQueryFields());
		queryFilter.setSorter(bpmDefFieldSorts.getFieldSorts());
		queryFilter.setPageBean(null);
		return (List<DefaultBpmDefinition>) query(queryFilter);
	}

	public String getDefIdByBpmnDefId(String bpmnDefId) {
		BpmDefinitionManagerImpl bean = AppUtil.getBean(getClass());
		String defId = bean.getDefIdByBpmnIdFromCache(bpmnDefId);
		if (StringUtil.isNotEmpty(defId)) {
			return defId;
		}
		return bpmDefinitionDao.getDefIdByBpmnDefId(bpmnDefId);
	}

	@Override
    @Transactional
	public void updMainVersion(String entityId) {
		BpmDefinition def = this.get(entityId);
		bpmDefinitionDao.updateNotMainVersion(entityId);
		bpmDefinitionDao.updateToMainVersion(entityId);
		
		publishEvent(def);

	}

	@Override
	public DefaultBpmDefinition getByBpmnDefId(String bpmnDefId) {
		DefaultBpmDefinition bpmDef = null;
		BpmDefinitionManagerImpl bean = AppUtil.getBean(getClass());
		String defId = bean.getDefIdByBpmnIdFromCache(bpmnDefId);
		if(StringUtil.isNotEmpty(defId)) {
			bpmDef = bean.getFromCache(defId);
		}
		if(bpmDef==null) {
			bpmDef = bpmDefinitionDao.getByBpmnDefId(bpmnDefId);
		}
		return bpmDef;
	}

	@Override
	public DefaultBpmDefinition getByBpmnDeployId(String bpmnDeployId) {
		return bpmDefinitionDao.getByBpmnDeployId(bpmnDeployId);
	}

	@Override
    @Transactional
	public String updateBpmDefXml(String defId, String defXml) throws Exception {
		DefaultBpmDefinition bpmDefinition = this.get(defId);
		BpmDefData bpmDefData = bpmDefDataManager.get(defId);
		if (bpmDefData == null)
			return null;
		bpmDefData.setDefXml(defXml);
		DesignerType designType = DesignerType.valueOf(bpmDefinition.getDesigner());
		DefTransform trans = natProDefinitionService.getDefTransform(designType);
		String bpmnDefXml = trans.convert(bpmDefinition.getDefKey(), bpmDefinition.getName(), defXml);
		bpmDefData.setBpmnXml(bpmnDefXml);
		bpmDefDataManager.update(bpmDefData);
		// 清理缓存
		publishEvent(bpmDefinition);
		return bpmnDefXml;
	}

	@Override
    @Transactional
	public void updateStatus(String defId, String status) {
		bpmDefinitionDao.updateStatus(defId,status);
	}

	@Override
    @Transactional
	public void updBpmData(String defId, BpmDefData bpmDefData) {
		DefaultBpmDefinition bpmDefinition = super.get(defId);

		bpmDefDataManager.update(bpmDefData);
		// 更新activiti的XML。
		String deployId = bpmDefinition.getBpmnDeployId();
		natProDefinitionService.writeDefXml(deployId, bpmDefData.getBpmnXml());
		// 清理缓存
		publishEvent(bpmDefinition);

	}

	@Override
	public List<String> queryByCreateBy(String userId) {
		return bpmDefinitionDao.queryDefKeyByCreateBy(userId);
	}

	@Override
    @Transactional
	public void removeByIds(String... ids) {
		for (String id : ids) {
			try {
				removeCascade(id);
			} catch (Exception e) {
				throw new WorkFlowException(ExceptionUtils.getRootCauseMessage(e));
			}
		}
	}

	@Override
    @Transactional
    public boolean deploy(BpmDefinition bpmDefinition) throws Exception {
        // 数据有效性判断
        if (!isAvailable(bpmDefinition)) {
            return false;
        }
        // 如果是新的流程定义标识
        boolean isNewDef = StringUtil.isEmpty(bpmDefinition.getDefId()) ? true : false;
        // 是否为草稿
        boolean isDraft = false;
        // 旧的bpmnXml数据
        String oldBpmnXml = "";
        if (isNewDef) {
            // 判断defKey是否存在，存在则不允许发布
            DefaultBpmDefinition mainBpmDefinition = getMainByDefKey(bpmDefinition.getDefKey(), false);
            if (mainBpmDefinition != null) {
                logger.error("defKey '" + bpmDefinition.getDefKey() + "' is exists ");
                return false;
            }
        } else {
            DefaultBpmDefinition tempDef = this.getById(bpmDefinition.getDefId());
            oldBpmnXml = tempDef.getBpmnXml();
            if (tempDef != null && StringUtil.isEmpty(tempDef.getBpmnDefId())) {
                isDraft = true;
            }
        }

        // 调用native的xml转换接口，转换defXml为bpmnXml
        String bpmnXml = getBpmnXmlByDesignFile(bpmDefinition);
        if (StringUtil.isEmpty(bpmnXml)) {
            throw new RuntimeException("流程发布失败！流程图不能为空");
        }
        // 更新插件
        bpmnXml = updateBpmnXmlPlugins(bpmnXml, oldBpmnXml);

        //校验流程完整性
        List<BpmNodeDef> nodeDefs = new  ArrayList<BpmNodeDef>();
        if(isNewDef){
            try {
                nodeDefs = BpmnXmlValidateUtil.getNodeDefs(bpmnXml);
            } catch (Exception e) {
                throw new RuntimeException("流程定义解析失败（可能是未执行更新流程引擎操作）："+e.getMessage());
            }
        }else{
            try {
                nodeDefs = bpmDefinitionService.getAllBpmNodeDefs(bpmDefinition.getDefId());
            } catch (Exception e) {
                throw new RuntimeException("流程定义校验失败："+e.getMessage());
            }
        }
        ObjectNode msg = BpmnXmlValidateUtil.vilateBpmXml(nodeDefs);
        if(!msg.get("isTrue").asBoolean()){
            throw new RuntimeException("流程定义校验失败："+msg.get("errorMsgs").asText());
        }


        // 调用native接口发布流程
        String deployId = null;
        String bpmnDefId = null;
        try {
            deployId = natProDefinitionService.deploy("", bpmDefinition.getName(), bpmnXml);
            bpmnDefId = natProDefinitionService.getProcessDefinitionIdByDeployId(deployId);
        } catch (UnsupportedEncodingException e) {
            logger.error("Invoke natProDefinitionService.deploy method error = " + e.getMessage());
            return false;
        }
        DefaultBpmDefinition def = (DefaultBpmDefinition) bpmDefinition;
        // 根据该流程是否已持久化做分支处理
        if (isNewDef) { // 新流程定义

            def.setDefId(UniqueIdUtil.getSuid());
            def.setVersion(1);
            def.setMainDefId(bpmDefinition.getDefId());
            def.setIsMain("Y");
            def.setStatus(BpmDefinition.STATUS.DEPLOY);
            def.setTestStatus(BpmDefinition.TEST_STATUS.TEST);

            //def.setDesc("");
            def.setBpmnDefId(bpmnDefId);
            def.setBpmnDeployId(deployId);
            def.setBpmnXml(bpmnXml);
            def.setCreateTime(LocalDateTime.now());
            // 保存
            this.create(def);

        } else if (isDraft) {// 若从草稿进行发布，即需要更新该定义
            def.setStatus(BpmDefinition.STATUS.DEPLOY);
            def.setTestStatus(BpmDefinition.TEST_STATUS.TEST);
            def.setVersion(1);
            def.setMainDefId(bpmDefinition.getDefId());
            def.setIsMain("Y");
            def.setBpmnDefId(bpmnDefId);
            def.setBpmnDeployId(deployId);
            def.setBpmnXml(bpmnXml);
            this.update(def);
        } else {// 发布新版本

            DefaultBpmDefinition oldBpmDefinition = (DefaultBpmDefinition) bpmDefinition;

            oldBpmDefinition.setStatus(BpmDefinition.STATUS.DEPLOY);
            oldBpmDefinition.setTestStatus(BpmDefinition.TEST_STATUS.TEST);

            oldBpmDefinition.setBpmnDefId(bpmnDefId);
            oldBpmDefinition.setBpmnDeployId(deployId);
            oldBpmDefinition.setBpmnXml(bpmnXml);
            // 根据旧的流程定义克隆一份，并将新的流程定义作为主版本
            this.cloneToMain(oldBpmDefinition);

        }
        // 清除缓存
        publishEvent(def);
        return true;

    }

	@Override
    @Transactional
	public boolean updateBpmDefinition(BpmDefinition bpmDefinition) throws Exception {
		// 进行数据有效性判断
		if (!isAvailable(bpmDefinition))
			return false;

		// 调用native的xml转换接口，转换defXml为bpmnXml
		String bpmnXml = getBpmnXmlByDesignFile(bpmDefinition);
		if (StringUtil.isEmpty(bpmnXml)) {
			return false;
		}
		// 更新插件
		DefaultBpmDefinition oldBpmDefinition = this.getById(bpmDefinition.getDefId());
		String oldBpmnXml = oldBpmDefinition.getBpmnXml();
		bpmnXml = updateBpmnXmlPlugins(bpmnXml, oldBpmnXml);

		// 判断bpmnXml是否改变
		boolean isBpmnXmlChange = bpmnXml.equals(bpmDefinition.getBpmnXml()) ? false : true;

		DefaultBpmDefinition def = (DefaultBpmDefinition) bpmDefinition;
		if (isBpmnXmlChange) {
			if(StringUtil.isEmpty(def.getStatus())) def.setStatus(oldBpmDefinition.getStatus());
			// 保存流程
			def.setBpmnXml(bpmnXml);
			// 确定是否已经发布，为空则为未发布。
			if (StringUtil.isNotEmpty(bpmDefinition.getBpmnDeployId())) {
				// 发布流程引擎中的定义
				natProDefinitionService.writeDefXml(bpmDefinition.getBpmnDeployId(), bpmnXml);
			}
		}
		this.update(def);

		// 清除缓存
		publishEvent(def);

		return true;
	}

	@Override
    @Transactional
	public boolean saveDraft(BpmDefinition bpmDefinition) throws Exception {
		// 先判断是否可以处理
		if (bpmDefinition == null || StringUtil.isEmpty(bpmDefinition.getDefKey())) {
			//return false;
		}
		DefaultBpmDefinition def = (DefaultBpmDefinition) bpmDefinition;

		String bpmnXml = getBpmnXmlByDesignFile(bpmDefinition);
		if(StringUtil.isEmpty(bpmnXml)){
			throw new RuntimeException("流程图不能为空！");
		}
		def.setBpmnXml(bpmnXml);
		// 设置状态
		def.setIsMain("Y");
		def.setStatus(BpmDefinition.STATUS.DRAFT);
		def.setTestStatus(BpmDefinition.TEST_STATUS.TEST);
		// 草稿默认为版本0
		def.setVersion(0);
		String defId = UniqueIdUtil.getSuid();
		def.setDefId(defId);
		def.setMainDefId(defId);
		def.setCreateTime(LocalDateTime.now());
		this.create(def);
		return true;
	}

	/**
	 * 
	 * 根据根据设计文件转换成标准的bpmn20定义文件。
	 * 
	 * @param bpmDefinition
	 * @return String
	 * @throws Exception 
	 */
	private String getBpmnXmlByDesignFile(BpmDefinition bpmDefinition) throws Exception {
		String bpmnXml = "";
		DesignerType designType = DesignerType.valueOf(bpmDefinition.getDesigner());
		DefTransform trans = natProDefinitionService.getDefTransform(designType);
		if(DesignerType.WEB.name().equals(bpmDefinition.getDesigner())){
			DefaultBpmDefinition def = (DefaultBpmDefinition)bpmDefinition;
			bpmnXml = trans.convert(bpmDefinition.getDefKey(), bpmDefinition.getName(), def.getDefJson());
		}else{
			bpmnXml = trans.convert(bpmDefinition.getDefKey(), bpmDefinition.getName(), bpmDefinition.getDefXml());
		}
		
		//web流程设计器需将分支条件等信息拷贝过来
		if(StringUtil.isNotEmpty(bpmDefinition.getDefId())&&DesignerType.WEB.name().equals(bpmDefinition.getDesigner())){
			List<BpmNodeDef> nodeDefs = bpmDefinitionService.getAllBpmNodeDefs(bpmDefinition.getDefId());
			Map<String, String>  map = new HashMap<>();
			map.put("xml", bpmnXml);
			copyBranchConfig(nodeDefs,map);
			bpmnXml = map.get("xml");
		}
		return bpmnXml;
	}
    
	@SuppressWarnings("static-access")
	private void copyBranchConfig(List<BpmNodeDef> nodeDefs ,Map<String, String> map){
		if (BeanUtils.isEmpty(nodeDefs)) {
			return;
		}
		for (BpmNodeDef bpmNodeDef : nodeDefs) {
			if(NodeType.EXCLUSIVEGATEWAY.equals(bpmNodeDef.getType()) || NodeType.INCLUSIVEGATEWAY.equals(bpmNodeDef.getType())){
				Map<String, String> conditionsMap = bpmNodeDef.getConditions();
				if(BeanUtils.isNotEmpty(conditionsMap)){
					try {
						map.put("xml",bpmDefHandler.converConditionXml(bpmNodeDef.getNodeId(), conditionsMap, map.get("xml")));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else if (NodeType.SUBPROCESS.equals(bpmNodeDef.getType())) {
				SubProcessNodeDef subProcessNodeDef = (SubProcessNodeDef) bpmNodeDef;
				copyBranchConfig(subProcessNodeDef.getChildBpmProcessDef().getBpmnNodeDefs(),map);
			}
		}
	}
	
	private String updateBpmnXmlPlugins(String newBpmnXml, String oldBpmnXml) {
		if (StringUtil.isEmpty(oldBpmnXml)) {
			return newBpmnXml;
		}
		String bpmnXml = defXmlTransForm.transform(oldBpmnXml, newBpmnXml);
		return bpmnXml;
	}

	/**
	 *
	 * 判断流程定义的数据是否有效（即包含必须的一些数据）
	 * 
	 * @param bpmDefinition
	 * @return boolean
	 */
	private boolean isAvailable(BpmDefinition bpmDefinition) {
		if (bpmDefinition == null || StringUtil.isEmpty(bpmDefinition.getName()) || StringUtil.isEmpty(bpmDefinition.getDefKey())) {
			logger.error("DefaultBpmDefinitionService.deploy data error, bpmDefinition=" + bpmDefinition);
			return false;
		}
		return true;
	}

	@Override
	public  PageList<DefaultBpmDefinition> queryList(QueryFilter queryFilter) throws IOException {
		IUser user = ContextUtil.getCurrentUser();
		return queryList(queryFilter, user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageList<DefaultBpmDefinition> queryList(QueryFilter queryFilter, IUser user) throws IOException {
		String userId = user.getUserId();
		Map<String, ObjectNode> authorizeRightMap = null;
		//当前人是否超管
		boolean isAdmin=user.isAdmin();
		
		queryFilter.addParams("isAdmin", isAdmin?1:0);
		//普通用户先获取授权流程及权限。
		if (!isAdmin) {
			// 获得流程分管授权与用户相关的信息集合的流程权限内容
			authorizeRightMap = getCommUserCondition(userId,queryFilter);
		}
		//查询当前用户所有领导共享的流程。
		BpmSecretaryManageManager secretaryManageManager = AppUtil.getBean(BpmSecretaryManageManager.class);
		Map<String, Object> leaderHasRightShareDefs = secretaryManageManager.getLeadersRigthMapBySecretaryId(userId,BpmSecretaryManage.RIGHT_START,true);
		Map<String, Set<String>> defShareMap = new HashMap<>();
		Set<String> allShareDefs = new HashSet<>();
		for (Iterator<Entry<String, Object>> iterator = leaderHasRightShareDefs.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> next = iterator.next();
			String leaderId = next.getKey();
			Map<String, Object> value = (Map<String, Object>) next.getValue();
			Map<String, String> laederRightMap =(Map<String, String>) value.get("groupMap");
			String shaerDefKeys= (String) value.get("defKeys");
			if (StringUtil.isEmpty(shaerDefKeys)) {
				continue;
			}
			Map<String, Object> leaderActRightMap = bpmDefAuthorizeManager.getActRightByRightMapAndUserId(leaderId, BPMDEFAUTHORIZE_RIGHT_TYPE.START, true, true,laederRightMap);
			String defKeys = (String) leaderActRightMap.get("defKeys");
			if (StringUtil.isEmpty(defKeys)) {
				continue;
			}
			defKeys = defKeys.replaceAll("'", "");
			Set<String> hasRightDefs = new HashSet<>(Arrays.asList(defKeys.split(",")));
			Set<String> set = new HashSet<String>();
			set.addAll(hasRightDefs);
			
			if (StringUtil.isNotEmpty(shaerDefKeys)) {
				shaerDefKeys = shaerDefKeys.replaceAll("'", "");
				set.retainAll(new HashSet<>(Arrays.asList(shaerDefKeys.split(","))));
				for (String defKey : set) {
					Set<String> leaderIdSet = defShareMap.containsKey(defKey)?defShareMap.get(defKey):new HashSet<>();
					leaderIdSet.add(leaderId);
					defShareMap.put(defKey, leaderIdSet);
				}
				allShareDefs.addAll(set);
			}
			
		}
		String oldDefKeys = BeanUtils.isEmpty(queryFilter.getParams().get("defKeys"))?"''":(String) queryFilter.getParams().get("defKeys");

		if (!allShareDefs.isEmpty()) {
			oldDefKeys +=",'"+StringUtil.join(new ArrayList<>(allShareDefs), "','")+"'";
		}
		queryFilter.addParams("defKeys", oldDefKeys);
		// 查询列表
		PageList<DefaultBpmDefinition> list =  this.query(queryFilter);
		if (list == null) return list;
		
		UCFeignService uCFeignService = AppUtil.getBean(UCFeignService.class);
		ArrayNode users = uCFeignService.getUserByIdsOrAccounts(StringUtil.join(new ArrayList<>(leaderHasRightShareDefs.keySet()), ","));
		Map<String, String> userNameMap = new HashMap<>();
		for (JsonNode jsonNode : users) {
			ObjectNode objectNode = (ObjectNode) jsonNode;
			userNameMap.put(objectNode.get("id").asText(), objectNode.get("fullname").asText());
		}
		
		// 把前面获得的流程分管授权的权限内容设置到流程管理列表
		for (DefaultBpmDefinition bpmDefinition : list.getRows()) {
			ObjectNode authorizeRight =null;
			if(isAdmin){
				//超管的权限默认。
				authorizeRight=AuthorizeRight.getAdminRight();
			}
			else{
				authorizeRight = authorizeRightMap.get(bpmDefinition.getDefKey());
			}
			bpmDefinition.setAuthorizeRight(authorizeRight);
			if (defShareMap.containsKey(bpmDefinition.getDefKey())) {
				List<BpmIdentity> lIdentities =new ArrayList<>();
				lIdentities.add(new DefaultBpmIdentity("0", "自己", "user"));
				Set<String> leaderIdSet=defShareMap.get(bpmDefinition.getDefKey());
				for (String leaderId : leaderIdSet) {
					lIdentities.add(new DefaultBpmIdentity(leaderId, userNameMap.get(leaderId)+"(领导)", "user"));
				}
				bpmDefinition.setLeaders(lIdentities);
			}
		}
		return list;
	}
	
	public PageList<DefaultBpmDefinition> query(QueryFilter<DefaultBpmDefinition> queryFilter) {
		Map<String, Object> params = queryFilter.getParams();
		if (BeanUtils.isNotEmpty(params) &&  (int)params.get("isAdmin") == 0) {
			String defKeys = (String) params.get("defKeys");
			if (BeanUtils.isEmpty(defKeys)) {
				return new PageList<>();
			}else {
				queryFilter.addFilter("def_key_", defKeys, QueryOP.IN,FieldRelation.AND,"a");
			}
		}
		queryFilter.setDefaultSort("createTime", Direction.DESC);
		queryFilter.setParams(new HashMap<>());
		return super.query(queryFilter);
	}
	
	
	/**
	 * 普通用户获取有权限的流程定义及权限。
	 * @param userId
	 * @param queryFilter
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, ObjectNode> getCommUserCondition(String userId,QueryFilter queryFilter) throws IOException{
			String bpmDefAuthorizeRightType = BPMDEFAUTHORIZE_RIGHT_TYPE.MANAGEMENT;
			// 有传入权限类型的话
			Map<String, Object> params = queryFilter.getParams();
			String right = (String) params.get("bpmDefAuthorizeRightType");
			if (StringUtil.isNotEmpty(right)) {
				bpmDefAuthorizeRightType = right;
			}
			// 获得流程分管授权与用户相关的信息
			Map<String, Object> actRightMap = bpmDefAuthorizeManager.getActRightByUserId(userId, bpmDefAuthorizeRightType, true, true);
			// 获得流程分管授权与用户相关的信息集合的流程KEY
			String defKeys = (String) actRightMap.get("defKeys");
			if (StringUtil.isNotEmpty(defKeys)){
				queryFilter.addParams("defKeys", defKeys);
			}
			// 获得流程分管授权与用户相关的信息集合的流程权限内容
			Map<String, ObjectNode> authorizeRightMap = (Map<String, ObjectNode>) actRightMap.get("authorizeRightMap");
			return authorizeRightMap;
	}

	@Override
	public List<DefaultBpmDefinition> queryListByMap(Map<String, Object> map) {
		return this.bpmDefinitionDao.queryListByMap(map);
	}

	@Override
    @Transactional
	public void updDefType(String typeId,String typeName, List<String> defList) {
		this.bpmDefinitionDao.updDefType(typeId,typeName,defList);
	}

	@Override
    @Transactional
	public void removeDefId(String defId) {
		BpmDefinition bpmDef = super.get(defId);
		
		if(bpmDef.isMain()) {
			throw new RuntimeException("非级联删除时，不能删除当前主版本的流程定义");
		}
		publishEvent(bpmDef);

		// 根据流程定义ID获取实例列表。
		List<DefaultBpmProcessInstance> instList = bpmProcessInstanceManager.getListByDefId(defId);

		for (DefaultBpmProcessInstance inst : instList) {
			bpmProcessInstanceManager.physicsRemove(inst.getId());
		}
		// 删除流程引擎表
		removeActvitiByDefId(defId);
		// 删除流程定义表。
		removeDefByDefId(defId);
	}

	/**
	 * 删除流程平台数据。
	 * 
	 * @param defId
	 *            void
	 */
    @Transactional
	private void removeDefByDefId(String defId) {
		bpmDefDataManager.remove(defId);
		super.remove(defId);
	}

	/**
	 * 删除流程引擎的流程数据。
	 * 
	 * @param defId
	 *            void
	 */
    @Transactional
	private void removeActvitiByDefId(String defId) {
		bpmDefinitionDao.delActByteArrayByDefId(defId);
		bpmDefinitionDao.delActDeployByDefId(defId);
		bpmDefinitionDao.delActDefByDefId(defId);
	}

	@Override
    @Transactional
	public void removeDefIds(boolean cascade, Boolean isVersion,String... defIds) throws Exception {
		for (String defId : defIds) {
            DefaultBpmDefinition defaultBpmDefinition = bpmDefinitionManager.getById(defId);

            //todo 子流程删除
            List<DefaultBpmProcessInstance> instances = null;
            //是否是从版本管理删除
            if(isVersion){
                instances = bpmProcessInstanceManager.getListByDefId(defId);//根据流程定义ID获取流程实例列表
            }else{
                //根据流程定义key获取流程实例列表
                instances = bpmProcessInstanceDao.getListByBpmnDefKey(defaultBpmDefinition.getDefKey());
            }
            if(BeanUtils.isNotEmpty(instances)){
                for (DefaultBpmProcessInstance entity : instances){
                    //根据父实例ID获取子流程实例列表
                    List<DefaultBpmProcessInstance> instancesSub = bpmProcessInstanceDao.getByParentId(entity.getId());
                    if(BeanUtils.isNotEmpty(instancesSub)){
                        List<String> listStr = new ArrayList<>();
                        for (DefaultBpmProcessInstance instance : instancesSub){
                            String instId=instance.getId();
                            //根据子流程实例ID 清除流程实例

                            bpmProcessInstanceManager.remove(instId);
                            //根据子流程实例ID 删除当前流程所使用的表单关系
                            bpmInstFromManager.removeDataByInstId(instId);
                            //根据子流程实例ID 删除知会待办任务
                            bpmTaskNoticeManager.delBpmTaskNoticeByInstId(instId);
                            //根据子流程实例ID 删除知会已办任务
                            bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneByInstId(instId);
                            //根据子流程实例ID 删除我转办的任务
                            listStr.add(instId);
                        }
                        if (BeanUtils.isNotEmpty(listStr)) {
                            bpmTaskTurnManager.delByInstList(listStr);//根据流程实例列表删除任务
                        }
                    }
                }
            }
            //todo 主流程删除
            List<DefaultBpmDefinition> listDefaultBpmDefinition = new ArrayList<>();
            //是否是从版本管理删除
            if(isVersion){
                listDefaultBpmDefinition.add(defaultBpmDefinition);//根据传入的流程定义ID查询流程定义信息
            }else{
                //根据流程KEY查询所有流程定义ID
                listDefaultBpmDefinition = bpmDefinitionManager.queryByDefKey(defaultBpmDefinition.getDefKey());
            }
            for(DefaultBpmDefinition entity : listDefaultBpmDefinition) {
                defId = entity.getDefId();
                BpmDefinition bpmDef = entity;
                bpmTaskNoticeManager.delBpmTaskNoticeByDefId(defId);//删除知会任务待办
                bpmTaskNoticeDoneManager.delBpmTaskNoticeDoneByDefId(defId);//删除知会任务已办
                //根据流程定义ID 删除我转办的任务
                List<DefaultBpmProcessInstance> list = bpmProcessInstanceManager.getListByDefId(defId);//根据流程定义ID获取实例列表
                List<String> listStr = new ArrayList<>();
                if (BeanUtils.isNotEmpty(list)) {
                    for (DefaultBpmProcessInstance defaultBpmProcessInstance : list) {
                        listStr.add(defaultBpmProcessInstance.getId());
                    }
                }
                if (BeanUtils.isNotEmpty(listStr)) {
                    bpmTaskTurnManager.delByInstList(listStr);//根据流程实例列表删除任务
                }
                if (cascade) {
                    //removeCascade(defId);//todo 这么写会造成定义ID有多个时只能查到第一个的信息
                    removeCascadeByDef(bpmDef);
                } else {
                    //removeDefId(defId);//同上
                    removeDefByDef(bpmDef);
                }
            }
		}
	}

    @Transactional
    public void removeCascadeByDef(BpmDefinition bpmDef) throws Exception {
        String defKey = bpmDef.getDefKey();
        // 删除时清除缓存。
        List<DefaultBpmDefinition> defList = bpmDefinitionDao.getByDefKey(defKey);
        for (DefaultBpmDefinition def : defList) {
            publishEvent(def);
        }

        List<DefaultBpmProcessInstance> instList = bpmProcessInstanceManager.getListByBpmnDefKey(defKey);

        for (DefaultBpmProcessInstance inst : instList) {
            bpmProcessInstanceManager.physicsRemove(inst.getId());
        }

        //删除Actviti流程任务
        if(StringUtil.isNotEmpty(bpmDef.getBpmnDefId())){
            bpmDefinitionDao.delActTask(bpmDef.getBpmnDefId());
        }

        // 删除流程数据表。
        removeActviti(defKey);
        // 删除平台流程数据。
        removeDef(defKey);
    }

    @Transactional
    public void removeDefByDef(BpmDefinition bpmDef) {
        if(bpmDef.isMain()) {
            throw new RuntimeException("非级联删除时，不能删除当前主版本的流程定义");
        }
        publishEvent(bpmDef);

        // 根据流程定义ID获取实例列表。
        List<DefaultBpmProcessInstance> instList = bpmProcessInstanceManager.getListByDefId(bpmDef.getDefId());

        for (DefaultBpmProcessInstance inst : instList) {
            bpmProcessInstanceManager.physicsRemove(inst.getId());
        }
        // 删除流程引擎表
        removeActvitiByDefId(bpmDef.getDefId());
        // 删除流程定义表。
        removeDefByDefId(bpmDef.getDefId());
    }

	@Override
    @Transactional
	public void copyDef(String defId, String name, String defKey) throws Exception {
		
		DefaultBpmDefinition bpmDefinition = super.get(defId);
		BpmDefData bpmDefData =  bpmDefDataManager.get(defId);
		String bpmnXml = bpmDefData.getBpmnXml();
		String oldDefKey = bpmDefinition.getDefKey();

		bpmnXml =  updateBpmXml(bpmnXml, defKey, name);
		bpmDefinition.setDefKey(defKey);
		bpmDefinition.setName(name);
		bpmDefData.setBpmnXml(bpmnXml);
		
		// 发布
		// 调用native接口发布流程
		String deployId = null;
		String bpmnDefId = null;
		try {
			deployId = natProDefinitionService.deploy("", bpmDefinition.getName(), bpmnXml);
			bpmnDefId = natProDefinitionService.getProcessDefinitionIdByDeployId(deployId);
		} catch (UnsupportedEncodingException e) {
			logger.error("Invoke natProDefinitionService.deploy method error = " + e.getMessage());
		}
		bpmDefinition.setDefId(UniqueIdUtil.getSuid());
		bpmDefinition.setVersion(1);
		bpmDefinition.setMainDefId(bpmDefinition.getDefId());
		bpmDefinition.setIsMain("Y");
		bpmDefinition.setStatus(BpmDefinition.STATUS.DEPLOY);
		bpmDefinition.setTestStatus(BpmDefinition.TEST_STATUS.TEST);
		bpmDefinition.setDesc("");
		bpmDefinition.setBpmnDefId(bpmnDefId);
		bpmDefinition.setBpmnDeployId(deployId);
		bpmDefinition.setBpmnXml(bpmnXml);
		bpmDefData.setId(bpmDefinition.getDefId());
		bpmDefinition.setCreateTime(LocalDateTime.now());
		// 保存
		super.create(bpmDefinition);
		bpmDefDataManager.create(bpmDefData);
		copyBpmProByDefId(defId,bpmDefinition.getDefId());
		copyBpmFormRightByFlowKey(oldDefKey,defKey);
		
	}
	
	// 复制流程表单权限
    @Transactional
	private void copyBpmFormRightByFlowKey(String oldDefKey, String defKey) throws Exception {
		QueryFilter queryFilter = QueryFilter.build();
		queryFilter.addFilter("flow_key_", oldDefKey,QueryOP.EQUAL);
		queryFilter.addFilter("permission_type_", 1,QueryOP.EQUAL);
		queryFilter.setPageBean(null);
		List<ObjectNode> lists = formRestfulService.queryFormRight(queryFilter);
		for (ObjectNode bpmFormRight : lists) {
			bpmFormRight.put("id",UniqueIdUtil.getSuid());
			bpmFormRight.put("flowKey",defKey);
			formRestfulService.createFormRight(bpmFormRight);
		}
	}

	// 复制流程业务对象设置表
    @Transactional
	private void copyBpmProByDefId(String oldDefId, String newDefId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("processId", oldDefId);
		List<BpmProBo> lists = bpmProBoManager.getByProcess(params);
		for (BpmProBo bpmProBo : lists) {
			bpmProBo.setId(UniqueIdUtil.getSuid());
			bpmProBo.setProcessId(newDefId);
			bpmProBoManager.create(bpmProBo);
		}
	}

	@SuppressWarnings("rawtypes")
    @Transactional
	private String updateBpmXml(String bpmnXml,String defKey, String name){
		Document loadXml = Dom4jUtil.loadXml(bpmnXml);
	   // 根节点  
       Element root = loadXml.getRootElement();
       // 取得某节点下名为"process"的所有字节点  
       List nodes = root.elements("process");  
       // xml元素  
       if(nodes!=null&&nodes.size()==1){
	       	Element element = (Element) nodes.get(0);
	       	element.attribute("id").setText(defKey);
	       	element.attribute("name").setText(name);
       }
       return Dom4jUtil.docToString(loadXml);
	}

	@Override
    @Transactional
	public void updBpmDefinitionStatus(DefaultBpmDefinition bpmDefinition, String oldStatus) throws Exception {
		/*String status = bpmDefinition.getStatus();mark
		// 原来为草稿改成发布
		if (BpmDefinition.STATUS.DRAFT.equalsIgnoreCase(oldStatus) && BpmDefinition.STATUS.DEPLOY.equalsIgnoreCase(status)) {
			bpmDefinitionService.deploy(bpmDefinition);
		}
		// 状态修改成发布。
		if (!status.equalsIgnoreCase(oldStatus) && BpmDefinition.STATUS.DEPLOY.equalsIgnoreCase(status)) {
			bpmProcessInstanceManager.updForbiddenByDefKey(bpmDefinition.getDefKey(), BpmProcessInstance.FORBIDDEN_NO);
		}
		// 将流程实例修改成禁用实例。
		if (!status.equalsIgnoreCase(oldStatus) && BpmDefinition.STATUS.FORBIDDEN_INSTANCE.equalsIgnoreCase(status)) {
			bpmProcessInstanceManager.updForbiddenByDefKey(bpmDefinition.getDefKey(), BpmProcessInstance.FORBIDDEN_YES);
		}*/
	}

    @Override
    public DefaultBpmDefinition getBpmDefinitionByRev(Map<String,Object> map){
        return bpmDefinitionDao.getBpmDefinitionByRev(map);
    }

	@Override
    @Transactional
	public void updateTypeIdByDefKey(String defKey, String typeId) {
		/*//更新流程定义分类id
		 bpmDefinitionDao.updateTypeIdByDefKey(defKey,typeId);*/
		 //更新流程实例分类id
		 bpmDefinitionDao.updateInstTypeIdByDefKey(defKey,typeId);
		 //更新流程实例历史数据分类id
		 bpmDefinitionDao.updateInstHiTypeIdByDefKey(defKey,typeId);
		 //更新待办分类id
		 bpmDefinitionDao.updateTaskTypeIdByDefKey(defKey,typeId);
		 //更新知会任务分类id
		 bpmDefinitionDao.updateTaskNoticeTypeIdByDefKey(defKey,typeId);
	}

	@Override
    @Transactional
	public CommonResult<String> setDefType(String typeName, String typeId, List<String> defIds) {
		bpmDefinitionDao.updDefType(typeId, typeName, defIds);
		return new CommonResult<>("操作成功");
	}

	@Override
	public List<Map<String, Object>> getDefCount(QueryFilter queryFilter) throws Exception{
		IUser user = ContextUtil.getCurrentUser();
		String userId = user.getUserId();
		Map<String, ObjectNode> authorizeRightMap = null;
		//当前人是否超管
		boolean isAdmin=user.isAdmin();

		queryFilter.addParams("isAdmin", isAdmin?1:0);
		//普通用户先获取授权流程及权限。
		if (!isAdmin) {
			// 获得流程分管授权与用户相关的信息集合的流程权限内容
			authorizeRightMap = getCommUserCondition(userId,queryFilter);
		}
		//查询当前用户所有领导共享的流程。
		BpmSecretaryManageManager secretaryManageManager = AppUtil.getBean(BpmSecretaryManageManager.class);
		Map<String, Object> leaderHasRightShareDefs = secretaryManageManager.getLeadersRigthMapBySecretaryId(userId,BpmSecretaryManage.RIGHT_START,true);
		Map<String, Set<String>> defShareMap = new HashMap<>();
		Set<String> allShareDefs = new HashSet<>();
		for (Iterator<Entry<String, Object>> iterator = leaderHasRightShareDefs.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> next = iterator.next();
			String leaderId = next.getKey();
			Map<String, Object> value = (Map<String, Object>) next.getValue();
			Map<String, String> laederRightMap =(Map<String, String>) value.get("groupMap");
			String shaerDefKeys= (String) value.get("defKeys");
			if (StringUtil.isEmpty(shaerDefKeys)) {
				continue;
			}
			Map<String, Object> leaderActRightMap = bpmDefAuthorizeManager.getActRightByRightMapAndUserId(leaderId, BPMDEFAUTHORIZE_RIGHT_TYPE.START, true, true,laederRightMap);
			String defKeys = (String) leaderActRightMap.get("defKeys");
			if (StringUtil.isEmpty(defKeys)) {
				continue;
			}
			defKeys = defKeys.replaceAll("'", "");
			Set<String> hasRightDefs = new HashSet<>(Arrays.asList(defKeys.split(",")));
			Set<String> set = new HashSet<String>();
			set.addAll(hasRightDefs);

			if (StringUtil.isNotEmpty(shaerDefKeys)) {
				shaerDefKeys = shaerDefKeys.replaceAll("'", "");
				set.retainAll(new HashSet<>(Arrays.asList(shaerDefKeys.split(","))));
				for (String defKey : set) {
					Set<String> leaderIdSet = defShareMap.containsKey(defKey)?defShareMap.get(defKey):new HashSet<>();
					leaderIdSet.add(leaderId);
					defShareMap.put(defKey, leaderIdSet);
				}
				allShareDefs.addAll(set);
			}

		}
		String oldDefKeys = BeanUtils.isEmpty(queryFilter.getParams().get("defKeys"))?"''":(String) queryFilter.getParams().get("defKeys");

		if (!allShareDefs.isEmpty()) {
			oldDefKeys +=",'"+StringUtil.join(new ArrayList<>(allShareDefs), "','")+"'";
		}
		queryFilter.addParams("defKeys", oldDefKeys);
		// 查询列表
		copyQuerysInParams(queryFilter);
		return bpmDefinitionDao.getDefCount(convert2Wrapper(queryFilter,currentModelClass()));
	}

	@Override
	public List<String> queryByTypeId(List<String> typeIds) {
		return baseMapper.queryDefKeyByTypeId(typeIds);
	}

	@Override
	public Map<String, Object> getBindRelation(String defId) throws Exception {
		BpmProcessDef<BpmProcessDefExt> bpmProcessDefExt = bpmDefinitionAccessor.getBpmProcessDef(defId);
		DefaultBpmProcessDefExt defExt = (DefaultBpmProcessDefExt) bpmProcessDefExt.getProcessDefExt();
		Form frm = defExt.getGlobalForm();//获取全局表单
		Map<String,Object> pcForm = new HashMap<>();
		Map<String,Object> mobileForm = new HashMap<>();
		Map<String,Object> result = new HashMap<>();
		if(frm != null) {
			pcForm.put("pcName", frm.getName());
			pcForm.put("pcAlias", frm.getFormValue());
			Map<String, Object> pcEnt = formRestfulService.getFormData(frm.getFormValue(), "");
			result.put("pcForm",pcForm);
			result.put("pcEnt",pcEnt.get("pcEnt"));
		}
		Form mobileFrm = defExt.getGlobalMobileForm();//全局手机表单
		if(mobileFrm != null) {
			mobileForm.put("mobileName", mobileFrm.getName());
			mobileForm.put("mobileAlias", mobileFrm.getFormValue());
			Map<String, Object> mobileEnt = formRestfulService.getFormData("", mobileFrm.getFormValue());
			result.put("mobileForm",mobileForm);
			result.put("mobileEnt",mobileEnt.get("mobileEnt"));
		}
		return result;
	}

	@Override
	public List<Map<String, String>> bpmDefinitionData(String alias) {
		return bpmDefinitionDao.getBpmDefinitionData(alias);
	}

	@Override
	@CachePut(value = "bpm:importFile", key="#fileId",
			  firstCache = @FirstCache(expireTime = 1, timeUnit = TimeUnit.HOURS))
	public String putImportFileInCache(String fileId, String fileJson) {
		return fileJson;
	}

	@Override
	@Cacheable(value = "bpm:importFile", key="#fileId",
			   firstCache = @FirstCache(expireTime = 1, timeUnit = TimeUnit.HOURS))
	public String getImportFileFromCache(String fileId) {
		return null;
	}

	@Override
	@CacheEvict(value = "bpm:importFile", key="#fileId")
	public void delImportFileFromCache(String fileId) {}
}
