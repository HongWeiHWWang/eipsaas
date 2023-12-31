package com.hotent.bo.persistence.manager.impl;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import com.hotent.base.feign.BpmModelFeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.exception.BaseException;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JAXBUtil;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.bo.constant.BoConstants;
import com.hotent.bo.context.FormContextThreadUtil;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoDefXml;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoEntRel;
import com.hotent.bo.persistence.dao.BoDefDao;
import com.hotent.bo.persistence.manager.BoAttributeManager;
import com.hotent.bo.persistence.manager.BoDefManager;
import com.hotent.bo.persistence.manager.BoEntManager;
import com.hotent.bo.persistence.manager.BoEntRelManager;
import com.hotent.table.operator.ITableOperator;

/**
 * form_bo_def 处理实现类
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
@Service("boDefManager")
public class BoDefManagerImpl extends BaseManagerImpl<BoDefDao, BoDef> implements BoDefManager {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	// BoDef线程变量前缀
	private static final String BODEF_THREAD_PREFIX = "bodef_alias_id_";
	@Resource
	BoAttributeManager boAttributeManager;
	@Resource
	BoEntManager boEntManager;
	@Resource
	BoEntRelManager bOEntRelManager;
	@Resource
	ITableOperator tableOperator;
	@Resource
	BpmModelFeignService bpmModelFeignService;

	@Override
	@Transactional
	public void removeByIds(String... ids) {
		List<String> idList = Arrays.asList(ids);
		// 1.批量删除BoDef记录
		baseMapper.deleteBatchIds(idList);
		
		for (String defId : ids) {
		    boEntManager.remove(defId);
			// 2.根据defId删除关联的BoEnt对象
			boEntManager.deleteByDefId(defId);
			// 3.根据defId删除BoDef和BoEnt的关联关系
			bOEntRelManager.removeByDefId(defId);
		}
	}

	@Override
	@Transactional
	public void update(BoDef entity) {
		BoEnt mainEnt = entity.getBoEnt();
		if(BeanUtils.isNotEmpty(mainEnt) && !mainEnt.isCreatedTable() && entity.isSupportDb()){
			Set<String>  createTables = new HashSet<>();
			try {
				if(!mainEnt.isExternal()){
					createTables.add(mainEnt.getTableName());
					boEntManager.createTable(mainEnt);
				}
				List<BoEnt> childEntList = mainEnt.getChildEntList();
				for (BoEnt boEnt : childEntList) {
					if(!boEnt.isExternal()&&!boEnt.isCreatedTable()){
						createTables.add(boEnt.getTableName());
						boEntManager.createTable(boEnt);
						List<BoEnt> sunEntList = boEnt.getChildEntList();
						if(BeanUtils.isNotEmpty(sunEntList)){
							for (BoEnt sunEnt : sunEntList) {
								if(!sunEnt.isExternal()&&!sunEnt.isCreatedTable()){
									createTables.add(sunEnt.getTableName());
									boEntManager.createTable(sunEnt);
								}
							}
						}
					}
				}

			} catch (SQLException e) {
				for (String tableName : createTables) {
					try {
						tableOperator.dropTable(tableName);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				e.printStackTrace();
				throw new BaseException("创建表失败");
			}
		}else if(BeanUtils.isNotEmpty(mainEnt) && entity.isSupportDb()){
			try {
				List<BoEnt> childEntList = mainEnt.getChildEntList();
				Set<String>  createTables = new HashSet<>();
				for(BoEnt boEnt:childEntList){
					if(!boEnt.isExternal()&&!boEnt.isCreatedTable()){
							boEntManager.createTable(boEnt);
					}
					List<BoEnt> sunEntList = boEnt.getChildEntList();
					if(BeanUtils.isNotEmpty(sunEntList)){
						for (BoEnt sunEnt : sunEntList) {
							if(!sunEnt.isExternal()&&!sunEnt.isCreatedTable()){
								createTables.add(sunEnt.getTableName());
								boEntManager.createTable(sunEnt);
							}
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new BaseException("创建表失败");
			}
		}
		super.updateById(entity);
	}

	@Override
	public BoDef getByDefId(String defId) {
		BoDef boDef = null;
		boDef = getFromThreadVar(null, defId);
		if(BeanUtils.isNotEmpty(boDef)) {
			return boDef;
		}
		boDef = baseMapper.selectById(defId);
		buildBoDef(boDef);
		if(BeanUtils.isNotEmpty(boDef)){
			setThreadVar(boDef);
		}
		return boDef;
	}

	@Override
	public BoDef getByAlias(String alias) {
		BoDef boDef = null;
		boDef = getFromThreadVar(alias, null);
		if(BeanUtils.isNotEmpty(boDef)) {
			return boDef;
		}
		boDef = baseMapper.getByAlias(alias);
		buildBoDef(boDef);
		if(BeanUtils.isNotEmpty(boDef)){
			setThreadVar(boDef);
		}
		return boDef;
	}
	
	/**
	 *  从线程变量中获取BoDef
	 *  <p>因为构建BoDef耗时很久，所以将构建好的BoDef放入线程变量。</p>
	 * @param alias
	 * @param defId
	 * @return
	 */
	private BoDef getFromThreadVar(String alias, String defId) {
		BoDef bodef = null;
		if(StringUtil.isNotEmpty(alias)) {
			Object obj = FormContextThreadUtil.getCommuVar(BODEF_THREAD_PREFIX + alias, null);
			if(obj!=null) {
				bodef = (BoDef)obj;
			}
		}
		else if(StringUtil.isNotEmpty(defId)) {
			Object obj = FormContextThreadUtil.getCommuVar(BODEF_THREAD_PREFIX + defId, null);
			if(obj!=null) {
				bodef = (BoDef)obj;
			}
		}
		return bodef;
	}
	
	/**
	 * 构建好的BoDef放入线程变量
	 * @param boDef
	 */
	private void setThreadVar(BoDef boDef) {
		FormContextThreadUtil.putCommonVars(BODEF_THREAD_PREFIX + boDef.getAlias(), boDef);
		FormContextThreadUtil.putCommonVars(BODEF_THREAD_PREFIX + boDef.getId(), boDef);
	}

	@Override
	public BoDef getPureByAlias(String alias) {
		BoDef boDef = baseMapper.getByAlias(alias);
		return boDef;
	}
	
	/**
	 * 设置bo定义的属性列表和层次关系。
	 * 
	 * @param boDef
	 */
	private void buildBoDef(BoDef boDef) {
		if(BeanUtils.isEmpty(boDef)) {
			return;
		}
		Map<String, List<BoEnt>> refMap = new HashMap<String, List<BoEnt>>();
		Map<String, BoEntRel> entMap = new HashMap<String, BoEntRel>();
		List<BoEntRel> list = bOEntRelManager.getByDefId(boDef.getId());
		for (BoEntRel ref : list) {
			entMap.put(ref.getId(), ref);
		}
		String mainEntRefId = getMainEntRefId(list);
		for (BoEntRel ref : list) {
			BoEnt ent = ref.getRefEnt();
			if(BeanUtils.isEmpty(ent)){
				String refEntId = ref.getRefEntId();
				if(StringUtil.isEmpty(refEntId)) continue;
				ent = boEntManager.getById(refEntId);
				if(BeanUtils.isEmpty(ent)){
					throw new BaseException(String.format("id为：%s的bo实体不存在.", refEntId));
				}
			}
			//设置实体的关系类型，一对一 ，一对多，多对多
			ent.setType(ref.getType());
			if(BeanUtils.isEmpty(ent.getBoAttrList())) {
				// 设置属性。
				List<BoAttribute> attrList = boAttributeManager.getByBoEnt(ent);
				ent.setBoAttrList(attrList);
			}
			ent.setRelation(ent.getType());
			// 如果为主实体的情况。
			if (BoConstants.RELATION_MAIN.equals(ref.getType())) {
				ent.setShow("主实体");
				boDef.setBoEnt(ent);
			} else {
				BoEntRel boEntRel = entMap.get(ref.getParentId());
				if(mainEntRefId.equals(ref.getParentId())){
					ent.setShow("子实体");
				}else{
					ent.setShow("孙实体");
				}
				String parentId = boEntRel.getRefEntId();
				if (refMap.containsKey(parentId)) {
					List<BoEnt> tempList = refMap.get(parentId);
					tempList.add(ent);
				} else {
					List<BoEnt> tempList = new ArrayList<BoEnt>();
					tempList.add(ent);
					refMap.put(parentId, tempList);
				}
			}
		}
		// 构建实体结构。
		buildRefEnt(boDef.getBoEnt(), refMap);
	}
	
	/**
	 * 获取主实体关联id
	 * @param list
	 * @return
	 */
	private String getMainEntRefId(List<BoEntRel> list){
		for (BoEntRel boEntRel : list) {
			if(BoConstants.RELATION_MAIN.equals(boEntRel.getType())){
				return boEntRel.getId();
			}
		}
		return "";
	}

	/**
	 * 构建实体树形结构。
	 * 
	 * @param boEnt
	 * @param refMap
	 */
	private void buildRefEnt(BoEnt boEnt, Map<String, List<BoEnt>> refMap) {
		List<BoEnt> tempList = refMap.get(boEnt.getId());
		if (BeanUtils.isEmpty(tempList)) return;
		boEnt.setChildEntList(tempList);
		for (BoEnt childObj : tempList) {
			buildRefEnt(childObj, refMap);
		}
	}

	@Override
	public List<BoDef> parseXml(String xml) {
		try {
			BoDefXml def = (BoDefXml) JAXBUtil.unmarshall(xml, BoDefXml.class);
			List<BoDef> list= def.getDefList();
			return list;
		} catch (Exception e) {
			throw new BaseException("解析xml成业务对象定义时出错:" + e.getLocalizedMessage());
		}
	}

	@Override
	public String serialToXml(List<BoDef> boDef) {
		String xml = "";
		try {
			BoDefXml defXml = new BoDefXml();
			defXml.setDefList(boDef);
			xml = JAXBUtil.marshall(defXml, BoDefXml.class);
		} catch (JAXBException e) {
			throw new BaseException("序列换成XML时出错:" + e.getLocalizedMessage());
		}
		return xml;
	}

	@Override
	@Transactional
	public List<BoDef> importBoDef(List<BoDef> boDefs) {
		List<BoDef> rtnList=new ArrayList<BoDef>();
		for (BoDef boDef : boDefs) {
			BoDef boBoDef=importDef(boDef);
			rtnList.add(boBoDef);
		}
		return rtnList;
	}

	/**
	 * 导入一个bodef。
	 * 
	 * @param boDef
	 */
	private BoDef importDef(BoDef boDef) {
		String defId=boDef.getId();
		String alias = boDef.getAlias();
		BoDef dbDef = getPureByAlias(alias);// 数据库已存在的
		if (dbDef == null) {
			// 添加实例定义。
			boDef.setDeployed(false);
			this.create(boDef);
			ThreadMsgUtil.addMsg("定义："+boDef.getDescription()+"，成功导入");
		} else {
			defId = dbDef.getId();
			boDef.setId(defId);
			ThreadMsgUtil.addMsg("定义："+boDef.getDescription()+"，已存在故跳过");
		}
		// 添加实体
		BoEnt boEnt = boDef.getBoEnt();
		this.importEnt(defId, boEnt, "0");
		return boDef;
	}

	/**
	 * 递归导入实体。
	 * 
	 * @param defId
	 * @param ent
	 * @param parentId
	 * @return 
	 */
	private void importEnt(String defId, BoEnt ent, String parentId) {
		// 添加ENT
		String entId = ent.getId();

		if (ent.isExternal()) {
			// 外部表就初始化为已生成
			ent.setIsCreatedTable(true);
		}

		BoEnt dbEnt = boEntManager.getByName(ent.getName());// 在数据库
		if (dbEnt == null) {
			ent.setIsCreatedTable(false);
			ent.setStatus("enabled");
			boEntManager.create(ent);
			// 添加属性
			List<BoAttribute> attrList = ent.getBoAttrList();
			saveAttr(entId, attrList,ent);
			ThreadMsgUtil.addMsg("实例："+ent.getComment()+"，成功导入");
		} else {
			entId = dbEnt.getId();
			ent.setId(entId);
			ThreadMsgUtil.addMsg("实例："+ent.getComment()+"，已存在故跳过");
		}

		//开始处理def和ent的关系
		String relId = UniqueIdUtil.getSuid();
		BoEntRel dbRel = bOEntRelManager.getByDefIdAndEntId(defId, entId);
		if(dbRel==null){
			// 添加关系
			saveBoRel(relId, defId, parentId, entId, ent.getType());
		}else{
			relId=dbRel.getId();
		}

		// 处理子实体
		List<BoEnt> childEnts = ent.getChildEntList();
		if (BeanUtils.isEmpty(childEnts))
			return;

		for (BoEnt childBaseEnt : childEnts) {
			// 处理子实例
			importEnt(defId, childBaseEnt, relId);
		}
	}

	/**
	 * 保存属性。
	 * 
	 * @param entId
	 * @param attrList
	 */
	private void saveAttr(String entId, List<BoAttribute> attrList,BoEnt dbEnt) {
		for (BoAttribute attribute : attrList) {
			attribute.setBoEnt(dbEnt);
			attribute.setEntId(entId);
			boAttributeManager.create(attribute);
		}
	}

	/**
	 * 添加实体关联关系。
	 * 
	 * @param defId
	 * @param parentId
	 * @param entId
	 * @param type
	 */
	private void saveBoRel(String id, String defId, String parentId, String entId, String type) {

		BoEntRel rel = new BoEntRel();
		rel.setId(id);
		rel.setBoDefid(defId);
		rel.setParentId(parentId);
		rel.setRefEntId(entId);
		rel.setType(type);

		bOEntRelManager.create(rel);
	}

	@Override
	@Transactional
	public void save(String json) throws Exception {
		JsonNode jsonNode = JsonUtil.toJsonNode(json);
		BoDef boDef = JsonUtil.toBean(json, BoDef.class);
		if (StringUtil.isEmpty(boDef.getId())) {
			BoDef isAlias = this.getByAlias(boDef.getAlias());
			if(isAlias!=null){
				throw new BaseException("别名已存在");
			}
			boDef.setId(UniqueIdUtil.getSuid());
			this.create(boDef);
		} else {
			Map<String,Object> map = new HashMap<>();
			map.put("id", boDef.getId());
			map.put("rev", jsonNode.get("rev").asText());
			BoDef boDef1 = baseMapper.getBoDefByRev(map);
			if(BeanUtils.isNotEmpty(boDef1)) {
				this.update(boDef);
				bOEntRelManager.removeByDefId(boDef.getId());
			}else{
				throw new BaseException("此建模不是最新版本，请重新获取再修改");
			}
		}

		// 处理关系
		ArrayNode ents = (ArrayNode)jsonNode.get("ents");
		if(BeanUtils.isEmpty(ents)) {
			throw new BaseException("bo定义中的实体元素(ents)不能为空.");
		}
		String mainId = UniqueIdUtil.getSuid();
		//处理bo实体数据
		dealEnts(boDef, ents, mainId);
	}

	/**
	 * bo的json
	 * @throws IOException 
	 * **/
	@Override
	public ObjectNode getBOJson(String id) throws IOException {
		BoDef boDef = this.getByDefId(id);
		BoEnt mainBo = boDef.getBoEnt();
		Assert.notNull(mainBo, String.format("ID为%s的BoDef所对应的BoEnt为空", id));
		ObjectNode jsonNode = (ObjectNode)JsonUtil.toJsonNode(mainBo);

		jsonNode.put("boDefId", boDef.getId());
		jsonNode.put("boDefAlias", boDef.getAlias());
		jsonNode.put("path", boDef.getAlias());// 主表字段path = boDefAlias
		if(BeanUtils.isNotEmpty(mainBo.getChildEntList())){
			List<BoEnt> childEntList = mainBo.getChildEntList();
			ArrayNode arrayEnts = JsonUtil.getMapper().createArrayNode();
			for (BoEnt boEnt : childEntList) {
				ObjectNode objNode = (ObjectNode) JsonUtil.toJsonNode(boEnt);
				if(BeanUtils.isNotEmpty(boEnt.getChildEntList())){
					objNode.set("childEnts", JsonUtil.toJsonNode(boEnt.getChildEntList()));
				}
				arrayEnts.add(objNode);
			}
			jsonNode.set("childEnts", arrayEnts);
		}


		handelBOJSON(jsonNode);
		jsonNode.put("nodeType", "main");
		jsonNode.put("icon", "fa fa-th-large dark");
		return jsonNode;
	}


	/**
	 * bo的json
	 * @throws IOException 
	 * **/
	@Override
	public ObjectNode getBOJsonByBoDefCode(String code) throws IOException {
		BoDef boDef = this.getByAlias(code);
		BoEnt mainBo = boDef.getBoEnt();
		Assert.notNull(mainBo, String.format("code为%s的BoDef所对应的BoEnt为空", code));
		ObjectNode jsonNode = (ObjectNode)JsonUtil.toJsonNode(mainBo);

		jsonNode.put("boDefId", boDef.getId());
		jsonNode.put("boDefAlias", boDef.getAlias());
		jsonNode.put("path", boDef.getAlias());// 主表字段path = boDefAlias
		if(BeanUtils.isNotEmpty(mainBo.getChildEntList())){
			jsonNode.set("childEnts", JsonUtil.toJsonNode(mainBo.getChildEntList()));
		}


		handelBOJSON(jsonNode);
		jsonNode.put("nodeType", "main");
		jsonNode.put("icon", "fa fa-th-large dark");
		return jsonNode;
	}

	@Override
	@Transactional
	public void updateCategory(BoDef boDef) {
		super.update(boDef);
	}

	@Override
	@Transactional
	public String saveFormData(String json) throws Exception {
		JsonNode jsonNode = JsonUtil.toJsonNode(json);
		BoDef boDef = JsonUtil.toBean(json, BoDef.class);
		Boolean isDeployed = jsonNode.get("deployed").asBoolean();
		String defId = null;
		if (StringUtil.isEmpty(boDef.getId())) {
			BoDef isAlias = this.getByAlias(boDef.getAlias());
			if(isAlias!=null){
				throw new BaseException("别名已存在");
			}
			boDef.setId(UniqueIdUtil.getSuid());
			defId=boDef.getId();
			this.create(boDef);
		} else {
			Map<String,Object> map = new HashMap<>();
			map.put("id", boDef.getId());
			map.put("rev", jsonNode.get("rev").asInt());
			BoDef boDef1 = baseMapper.getBoDefByRev(map);
			if(BeanUtils.isNotEmpty(boDef1)) {
				super.update(boDef);
				bOEntRelManager.removeByDefId(boDef.getId());
			}else{
				throw new BaseException("此建模不是最新版本，请重新获取再修改");
			}
		}
		// 处理关系
		ArrayNode ents = (ArrayNode)jsonNode.get("ents");
		if(BeanUtils.isEmpty(ents)) {
			throw new BaseException("bo定义中的实体元素(ents)不能为空.");
		}
		String mainId = UniqueIdUtil.getSuid();
		//处理bo实体数据
		dealEnts(boDef, ents, mainId);
		if(isDeployed){
			BoDef byDefId = this.getByDefId(defId);
			if(BeanUtils.isEmpty(byDefId)){
                String id = jsonNode.get("id").asText();
                BoDef updateBo = this.getByDefId(id);
                updateBo.setDeployed(true);
                this.update(updateBo);
            }else {
                byDefId.setDeployed(true);
                this.update(byDefId);
            }
		}
		return defId;
	}
	
	/**
	 * 处理bo实体数据
	 * @param boDef
	 * @param ents
	 * @param mainId
	 * @throws Exception
	 */
	private void dealEnts(BoDef boDef,ArrayNode ents,String mainId) throws Exception{
		for (int i = 0; i < ents.size(); i++) {
			JsonNode subJsonNode = ents.get(i);
			BoEnt ent = JsonUtil.toBean(subJsonNode, BoEnt.class);
			if(BeanUtils.isNotEmpty(subJsonNode.get("createdTable")) && subJsonNode.get("createdTable").asBoolean()){
				ent.setIsCreatedTable(true);
			}
			boEntManager.saveEnt(ent);
			String entId = ent.getId();

			BoEntRel boEntRel = new BoEntRel();
			boEntRel.setBoDefid(boDef.getId());

			JsonNode relationNode = subJsonNode.get("relation");
			String relation = relationNode.asText();
			String sunMainId = UniqueIdUtil.getSuid();
			if (BoConstants.RELATION_MAIN.equals(relation)) {
				boEntRel.setParentId("0");
				boEntRel.setId(mainId);
			} else {
				boEntRel.setId(sunMainId);
				boEntRel.setParentId(mainId);
			}
			boEntRel.setRefEntId(entId);
			boEntRel.setType(relation);

			bOEntRelManager.create(boEntRel);
			if(BeanUtils.isNotEmpty(subJsonNode.get("children"))){
				ArrayNode sunents = (ArrayNode)subJsonNode.get("children");
				this.dealEnts(boDef, sunents, sunMainId);
			}
		}
	}

	@Override
	public Map<String, Object> getBindData(String id, String alias) throws IOException {
		//业务表单数据
		List<Map<String, String>> formDifinitionData = baseMapper.getFormDifinitionData(id);
		//流程数据
//		List<Map<String, String>> bpmDefinitionData = baseMapper.getBpmDefinitionData(alias);
		List<Map<String, String>> bpmDefinitionData = bpmModelFeignService.bpmDefinitionData(alias);
		//实体表
		List<Map<String, String>> entData = baseMapper.getEntData(id);
		Map<String, Object> map = new HashMap<>();
		map.put("formData",formDifinitionData);
		map.put("bpmData",bpmDefinitionData);
		map.put("entData",entData);
		return map;
	}

	@Override
	public List<Map<String, String>> getBpmDefinitionData(String formKey) throws Exception {
		return bpmModelFeignService.bpmDefinitionData(formKey);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getHideAttr(String tableName) throws Exception {
		List<BoEnt> boEnts = boEntManager.getByTableName(tableName);
		if(boEnts.size() == 0){
			return null;
		}
		List<BoAttribute> attrList = boAttributeManager.getByBoEnt(boEnts.get(0));
		List list = new ArrayList();
		for (int i = 0; i < attrList.size(); i++) {
			if("hide".equals(attrList.get(i).getStatus())){
				list.add(attrList.get(i));
			}
		}
		return list.size()>0 ? list : null;
	}

	@Override
	public ObjectNode getBoDefDetails(String id) throws Exception {
		BoDef boDef=getByDefId(id);
		BoEnt ent=boDef.getBoEnt();
		ObjectNode boDefNode=(ObjectNode) JsonUtil.toJsonNode(boDef);
		ObjectNode entNode=(ObjectNode) JsonUtil.toJsonNode(ent);
		ArrayNode node=JsonUtil.getMapper().createArrayNode();
		node.add(entNode);
		if(ent.getChildEntList()!=null && ent.getChildEntList().size()>0){
			for (BoEnt boent : ent.getChildEntList()) {
				ObjectNode no=(ObjectNode) JsonUtil.toJsonNode(boent);
				if(BeanUtils.isNotEmpty(boent.getChildEntList())){
					no.set("children", (ArrayNode)JsonUtil.toJsonNode(boent.getChildEntList()));
				}
				node.add(no);
			}
		}
		boDefNode.set("ents", node);
		return boDefNode;
	}

	@Override
	public ObjectNode getBoTreeData(String ids) throws Exception {
		if(StringUtil.isNotEmpty(ids)){
			ids = ids.replace("=", "");
		}
		String[] idsArr = ids.split(",");
		ArrayNode arrayNode = JsonUtil.getMapper().createArrayNode();
		for (String id : idsArr) {
			ObjectNode boJson = getBOJson(id);
			if (boJson.hasNonNull("children")) {
				ArrayNode main =(ArrayNode) boJson.get("children");
				for (Iterator<JsonNode> iterator = main.iterator(); iterator.hasNext();) {
					JsonNode jsonNode = iterator.next();
					if (jsonNode.hasNonNull("children")) {
						ArrayNode subTab = (ArrayNode) jsonNode.get("children");
						for (Iterator<JsonNode> subIterator = subTab.iterator(); subIterator.hasNext();) {
							JsonNode subNode = subIterator.next();
							if (subNode.hasNonNull("name") && "form_data_rev_".equals(subNode.get("name").asText())) {
								subIterator.remove();
							}
						}
					}else if (jsonNode.hasNonNull("name")&&"form_data_rev_".equals(jsonNode.get("name").asText()) ||
							(jsonNode.hasNonNull("status")&&"hide".equals(jsonNode.get("status").asText()))) {
						iterator.remove();
					}
				}
			}
			arrayNode.add(boJson);
		}
		ObjectNode bos = (ObjectNode) JsonUtil.toJsonNode("{\"id\":\"0\",\"parentId\":\"-1\",\"desc\":\"BO对象属性\"}");
		bos.set("children", arrayNode);
		return bos;
	}


	// 将表的 字段 和子表作为 children
	private void handelBOJSON(ObjectNode boJson) {
		ArrayNode children = JsonUtil.getMapper().createArrayNode();

		ArrayNode attrList = (ArrayNode)boJson.get("attributeList");

		for (int i = 0; i < attrList.size(); i++) {
			ObjectNode attr = (ObjectNode)attrList.get(i);
			attr.put("nodeType", "field");
			attr.set("entId", boJson.get("id"));
			attr.set("boDefId", boJson.get("boDefId"));
			attr.set("path", boJson.get("path"));

			String dataType = attr.get("dataType").asText();
			if ("number".equals(dataType)) {
				attr.put("icon", "ico_int green");
			} else if ("datetime".equals(dataType)) {
				attr.put("icon", "ico_date green");
			} else {
				attr.put("icon", "ico_string dark");
			}
		}

		children.addAll(attrList);
		boJson.remove("attributeList");
		// 处理子表
		ArrayNode childEntList = (ArrayNode)boJson.get("childEnts");
		if (BeanUtils.isNotEmpty(childEntList)){
			for (int i = 0; i < childEntList.size(); i++) {
				ObjectNode subTable = (ObjectNode)childEntList.get(i);
				subTable.put("nodeType", "sub");
				subTable.put("icon", "ico_complex blue");
				// 子表path：boDefAlias.sub_tableName;
				subTable.put("path", boJson.get("path").asText() + ".sub_" + subTable.get("name").asText()); 
				handelBOJSON(subTable);
			}
			attrList.addAll(childEntList);
		}
		boJson.remove("childEnts");
		boJson.set("children", attrList);
	}

	/**
	 * 先删除boentrel 再删除自身
	 */
	@Override
	@Transactional
	public boolean removeById(Serializable id) {
		Assert.notNull(id, "删除数据时传入的id不能为空");
		// 1.先删除对应的BoEnt
		boEntManager.deleteByDefId(id.toString());
		// 2.再删除关系表
		bOEntRelManager.removeByDefId(id.toString());
		// 3.删除BoDef本身
		return super.removeById(id);
	}

	@Override
	@Transactional
	public int removeByAlias(String alias) {
		return baseMapper.removeByAlias(alias);
	}

	@Override
	public List<BoDef> getByFormKey(String formKey) {
		return baseMapper.getByFormKey(formKey);
	}
}
