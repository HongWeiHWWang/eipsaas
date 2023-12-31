package com.hotent.bo.instance.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Set;
import java.util.function.UnaryOperator;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.BpmRuntimeFeignService;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.base.util.time.DateFormatUtil;
import com.hotent.base.util.time.TimeUtil;
import com.hotent.bo.constant.BoConstants;
import com.hotent.bo.context.FormContextThreadUtil;
import com.hotent.bo.exception.BoBaseException;
import com.hotent.bo.instance.BoSubDataHandler;
import com.hotent.bo.model.BoAttribute;
import com.hotent.bo.model.BoData;
import com.hotent.bo.model.BoDataRel;
import com.hotent.bo.model.BoDef;
import com.hotent.bo.model.BoEnt;
import com.hotent.bo.model.BoResult;
import com.hotent.bo.model.SqlModel;
import com.hotent.bo.persistence.dao.BoDataRelDao;
import com.hotent.bo.persistence.manager.BoEntManager;
import com.hotent.table.model.Column;
import com.hotent.uc.api.model.IUser;

import io.jsonwebtoken.lang.Assert;

/**
 * 保存到数据库中的处理器
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月12日
 */
@Service("boDataHandler")
public class BoDbHandlerImpl extends AbstractBoDataHandler {
	@Resource
	DatabaseContext databaseContext;
	@Resource
	BoDataRelDao boDataRelDao;
	@Resource
	BoSubDataHandler boSubDataHandler;
	@Resource
	CommonManager commonManager;
	@Resource
	BoEntManager boEntManager;

	@Override
	public List<BoResult> save(String id, String defId, BoData curData) {
		BoEnt boEnt = curData.getBoEnt();
		Map<String, Object> row = curData.getData();
		String pk="";
		if(boEnt!=null){
			pk = boEnt.getPkKey().toLowerCase();
			if(StringUtil.isNotEmpty(id)){
				row.put(pk, id);
			}
		}
		// 添加结果。
		List<BoResult> resultList = new ArrayList<BoResult>();
		try {
			// 数据中包含主表表示更新
			if (row.containsKey(pk)) {
				update(curData, resultList);
			} else {
				add(curData, resultList, "0");
			}
		} catch (NumberFormatException ex){
			ex.printStackTrace();
			String[] str = ex.getMessage().split(":");
			throw new NumberFormatException(str[1] + "非数字，请输入正确数值！");
		} catch (BaseException e){
			throw new BaseException(e.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BoBaseException(ex.getMessage());
		}

		// 设置bodefAlias.
		if(curData.getBoDef()!=null){
			setBoDefAlias(resultList, curData.getBoDef().getAlias());
		}

		return resultList;
	}
	
	private BoDef getBoDefByAlias(String alias) {
		BoDef boDef = boDefManager.getByAlias(alias);
		return boDef;
	}

	@Override
	public BoData getById(Object pk, String bodefCode) {
		BoDef boDef = getBoDefByAlias(bodefCode);
		BoData boData = new BoData();
		boData.setBoDef(boDef);
		BoEnt boEnt = boDef.getBoEnt();
		boData.setBoEnt(boEnt);

		Map<String, Object> row = getById(boEnt, pk);
		boData.setData(row);
		// 子表处理
		List<BoEnt> childEntList = boEnt.getChildEntList();

		for (BoEnt childEnt : childEntList) {
			List<Map<String, Object>> list = getByFk(childEnt, pk);
			String key = childEnt.getName();
			List<BoData> listData = new ArrayList<BoData>();
			//孙表实体列表
			List<BoEnt> sunEntList = childEnt.getChildEntList();
			Map<String,Object> initData = childEnt.getInitData();
			for (Map<String, Object> rowMap : list) {
				BoData childData = new BoData();
				Map<String, Object> rtnMap = convertDbToData(childEnt, rowMap);
				childData.setData(rtnMap);
				//处理孙表
				if(BeanUtils.isNotEmpty(sunEntList)){
					String subPkStr = StringUtil.isNotEmpty(childEnt.getPk())?childEnt.getPk():BoEnt.PK_NAME;
					String subPk = (String) rowMap.get(subPkStr);
					Map<String,Object> sunInitData = new HashMap<String,Object>();
					for (BoEnt sunEnt : sunEntList) {
						List<Map<String, Object>> sunList = getByFk(sunEnt, subPk);
						String sunKey = sunEnt.getName();
						List<BoData> sunListData = new ArrayList<BoData>();
						for (Map<String, Object> sunRowMap : sunList) {
							BoData sunData = new BoData();
							Map<String, Object> sunRtnMap = convertDbToData(sunEnt, sunRowMap);
							sunData.setData(sunRtnMap);
							sunListData.add(sunData);
						}
						childData.addInitDataMap(sunKey, sunEnt.getInitData());
						childData.setSubList(sunKey, sunListData);
						initData.put("sub_"+sunEnt.getName(), new ArrayList<>());
						sunInitData.put(sunEnt.getName(), sunEnt.getInitData());
					}
					initData.put("initData",sunInitData);
				}
				listData.add(childData);
			}
			boData.addInitDataMap(key, initData);
			boData.setSubList(key, listData);
		}
		return boData;
	}

	/**
	 * 将从数据库读取的数据到实例数据。
	 * 
	 * @param boEnt
	 * @param map
	 * @return
	 */
	private Map<String, Object> convertDbToData(BoEnt boEnt, Map<String, Object> map) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		for (Entry<String, Object> ent : map.entrySet()) {
			String field = ent.getKey().toLowerCase();
			BoAttribute attribute = boEnt.getAttrByField(field);
			if (BeanUtils.isNotEmpty(attribute)) {
				// 处理日期。
				Object val = handValue(attribute, ent.getValue());
				rtnMap.put(attribute.getName(), val);
			}
		}
		rtnMap.put("form_data_rev_", map.get("F_form_data_rev_"));
		return rtnMap;
	}

	/**
	 * 数据根据bo属性处理。
	 * 
	 * @param attr
	 * @param val
	 * @return
	 */
	private Object handValue(BoAttribute attr, Object val) {
		if (BeanUtils.isEmpty(val))
			return val;
		if (Column.COLUMN_TYPE_DATE.equals(attr.getDataType())) {
			String format = attr.getFormat();
			if(val instanceof Timestamp){
				Timestamp times = (Timestamp) val;
				return TimeUtil.getDateTimeString(times.toLocalDateTime(), format);
			}else if(val instanceof java.util.Date){
				return TimeUtil.getDateTimeString(DateFormatUtil.parse((Date)val), format);
			}else if(val instanceof LocalDateTime ){
				return TimeUtil.getDateTimeString((LocalDateTime) val, format);
			}
		}
		return val;
	}

	/**
	 * 获取一行数据。
	 * 
	 * @param boEnt
	 * @return
	 */
	private Map<String, Object> getById(BoEnt boEnt, Object pk) {
		String sql = String.format("select * from %s where %s=#{pk}", boEnt.getTableName(), boEnt.getPkKey());

		Map<String, Object> map = null;
		if (boEnt.isExternal()) {
			// 切换外部表查询数据
			try(DatabaseSwitchResult setDataSource = databaseContext.setDataSource(boEnt.getDsName())){
				map = getOneById(sql, pk);
			} catch (Exception e) {
				throw new BoBaseException("操作外部表：" + boEnt.getDsName() + " 中的 " + boEnt.getDesc() + " 出错：" + ExceptionUtils.getRootCauseMessage(e));
			}
		}else{
			map = getOneById(sql, pk);
		}

		Map<String, Object> rtnMap = convertDbToData(boEnt, map);

		return rtnMap;
	}

	/**
	 * 通过主键查询一行数据
	 * @param sql
	 * @param pk
	 * @return
	 */
	private Map<String, Object> getOneById(String sql, Object pk){
		Map<String, Object> buildMap = new HashMap<>();
		if(BeanUtils.isNotEmpty(pk)) {
			buildMap.put("pk", pk);
		}
		List<Map<String, Object>> result = commonManager.query(sql, buildMap);
		Assert.isTrue(BeanUtils.isNotEmpty(result) && result.size() == 1, "通过主键查询数据时结果为空或查询到超过一条记录");
		return result.get(0);
	}

	/**
	 * 根据外键获取列表数据。
	 * 
	 * @param boEnt
	 * @return
	 */
	private List<Map<String, Object>> getByFk(BoEnt boEnt, Object pk){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			BpmRuntimeFeignService bpmRuntimeFeignService= AppUtil.getBean(BpmRuntimeFeignService.class);
			ObjectNode objectNode = (ObjectNode) JsonUtil.toJsonNode(boEnt);

			String defId = (String) FormContextThreadUtil.getCommuVar("defId","");
			String nodeId = (String) FormContextThreadUtil.getCommuVar("nodeId","");
			String parentDefKey = (String) FormContextThreadUtil.getCommuVar("parentDefKey", "local_");
			if(StringUtil.isEmpty(parentDefKey)) {
				parentDefKey = "";
			}

			CommonResult<String> result = bpmRuntimeFeignService.getSubDataSqlByFk(objectNode, pk, defId, nodeId, parentDefKey);
			if(result.getState()) {
				if(boEnt.isExternal()){
					//外部表数据
					try(DatabaseSwitchResult setDataSource = databaseContext.setDataSource(boEnt.getDsName())){
						list = commonManager.query(result.getValue(), pk);
					} catch (Exception e) {
						throw new RuntimeException("操作外部表：" + boEnt.getDsName() + " 中的 " + boEnt.getDesc() + " 出错："+e.getMessage(), e);
					}

				}else{
					list = commonManager.query(result.getValue(), pk);
				}
			}else {
				throw new RuntimeException("获取子表数据sql失败："+result.getMessage());
			}

		} catch (IOException e) {
			throw new RuntimeException("获取子表数据sql失败："+e.getMessage());
		}
		return list;
	}

	/**
	 * 处理添加数据的情况。
	 * 
	 * @param curData
	 * @param resultList
	 * @param parentId
	 * @throws ParseException
	 */
	private void add(BoData curData, List<BoResult> resultList, String parentId) throws ParseException {
		BoEnt boEnt = curData.getBoEnt();

		Map<String, Object> map = convertDbMap(curData);
		// 添加主表
		BoResult boResult = insert(boEnt, map, parentId);
		boResult.setBoAlias(curData.getBoDefAlias());
		resultList.add(boResult);
		if(BeanUtils.isEmpty(boEnt))return;
		Map<String, BoEnt> entMap = boEnt.getChildMap();
		if (BeanUtils.isEmpty(entMap) && StringUtil.isNotEmpty(parentId) && BeanUtils.isNotEmpty(curData.getData()) && !curData.getData().isEmpty()){
			boolean hasSun = false;
			for (String key : curData.getData().keySet()) {
				if(key.startsWith("sub_") && BeanUtils.isNotEmpty(curData.getData().get(key))){
					hasSun = true;
					break;
				}
			}
			if(hasSun){
				List<BoEnt> sunEnts = boEntManager.getBySubEntId(boEnt.getId());
				if(BeanUtils.isNotEmpty(sunEnts)){
					boEnt.setChildEntList(sunEnts);
					entMap = boEnt.getChildMap();
				}else{
					return ;
				}
			}else{
				return;
			}
		}
		// 子表添加
		for (Entry<String, List<BoData>> ent : curData.getSubMap().entrySet()) {
			String key = ent.getKey();
			String tableName = key.replaceFirst("sub_", "");
			BoEnt childEnt = entMap.get(tableName.toLowerCase());
			List<BoData> subDataList = ent.getValue();
			for (BoData chidData : subDataList) {
				// 设置子数据的实体元数据。
				chidData.setBoEnt(childEnt);
				add(chidData, resultList, boResult.getPk());
			}
		}
	}

	/**
	 * 更新数据只管两层结构。
	 * 
	 * <pre>
	 * 	1.更新主表。
	 *  2.更新子表。
	 *  	1.添加
	 *  	2.删除
	 *  	3.更新
	 * </pre>
	 * 
	 * @param curData
	 * @param resultList
	 * @throws ParseException
	 */
	private void update(BoData curData, List<BoResult> resultList) throws Exception {
		BoEnt boEnt = curData.getBoEnt();

		Map<String, Object> map = convertDbMap(curData);
		String modifyResult  = "";
		// 更新主表
		BoResult boResult = update(boEnt, map);
		if (StringUtil.isNotEmpty(boResult.getModifyDetail())) {
			modifyResult=String.format("主表【%s】修改明细：\n%s", boEnt.getDesc(),boResult.getModifyDetail());
		}
		boResult.setBoAlias(curData.getBoDefAlias());
		resultList.add(boResult);

		String pk = (String) map.get(boEnt.getPkKey());

		Map<String, BoEnt> entMap = boEnt.getChildMap();

		if (BeanUtils.isEmpty(entMap)) return;
		//删除子表数据中不存在的数据
		if(BeanUtils.isEmpty(curData.getSubMap())){
			for (Entry<String, BoEnt> entry : entMap.entrySet()) {  
				BoEnt childEnt = (BoEnt) entMap.get(entry.getKey());
				if(BeanUtils.isNotEmpty(childEnt)){
					// 获取原来的数据。
					Map<String, Map<String, Object>> oldDatas = getOldSubDatas(childEnt, pk);
					// 删除孙表数据
					deleteSunBoData(childEnt, oldDatas, resultList);
					for(String subPk : oldDatas.keySet()){
						BoResult result = delete(childEnt, subPk);
						resultList.add(result);
					}
					modifyResult+=String.format("\n子表【%s】删除明细：%s", childEnt.getDesc(),BeanUtils.ObjectToString(oldDatas));
				}
			}  
		}else{
			// 子表数据更新。
			for (Entry<String, List<BoData>> ent : curData.getSubMap().entrySet()) {
				String key = ent.getKey();
				String tableName = key.replaceFirst("sub_", "");
				BoEnt childEnt = entMap.get(tableName.toLowerCase());
				if (BeanUtils.isEmpty(childEnt)) {
					continue;
				}
				// 获取原来的数据。
				Map<String, Map<String, Object>> oldDatas = getOldSubDatas(childEnt, pk);
				//存在的记录。
				Set<String> updSet=new HashSet<String>();
				//获取原来的数据
				List<Map<String, Object>> oldList = this.getByFk(childEnt, pk);
				List<BoData> subDataList = ent.getValue();
				String curSubModifyRes = "";
				//获取孙表
				List<BoEnt> sunEnts = boEntManager.getBySubEntId(childEnt.getId());
				for (int i = 0;i<subDataList.size();i++) {
					BoData chidData = subDataList.get(i);
					chidData.setBoEnt(childEnt);
					String childPkField = childEnt.getPkKey();
					Map<String, Object> childRow = convertDbMap(chidData);
					// 表示数据存在。
					if (chidData.containKey(childPkField)) {
						String childPk = chidData.getString(childPkField);
						updSet.add(childPk);
						// 包含
						if (oldDatas.keySet().contains(childPk)) {
							BoResult result = update(childEnt, childRow);
							//处理孙表数据
							if(BeanUtils.isNotEmpty(sunEnts)){
								updateSunBoData(sunEnts, chidData, childPk, resultList);
							}
							if (StringUtil.isNotEmpty(result.getModifyDetail())) {
								curSubModifyRes+=String.format("\n第【%s】行修改明细：%s", i+1,result.getModifyDetail());
							}
							if (result != null) {
								if("0".equals(result.getParentId())){
									result.setParentId(pk);
								}
								resultList.add(result);
							}
						}
					}
					// 数据不存在则添加
					else {
						BoResult result = insert(childEnt, childRow, pk);
						//处理孙表数据
						addSunBoData(resultList, sunEnts, chidData, result.getPk());
						//if(BeanUtils.isNotEmpty(childEnt.getChildEntList()))
						curSubModifyRes+=String.format("\n第【%s】行新增明细：%s", i+1,BeanUtils.ObjectToString(childRow));
						resultList.add(result);
					}
				}
				//查询通过子表数据授权SQL过滤后的数据
				List<Map<String, Object>> list = this.getByFk(childEnt, pk);
				if(list.size()>0){
					//把没过滤的其他数据抽取出来
					Set<String> stringSet = new HashSet<>();
					for(String string:oldDatas.keySet()){
						stringSet.add(string);
					}
					for (int i = 0; i < list.size(); i++) {
						if(stringSet.contains(list.get(i).get("ID_"))){
							stringSet.remove(list.get(i).get("ID_").toString());
						}
					}
					//抽取出来的数据赋值给要更新的数据集合
					for(String s:stringSet){
						updSet.add(s);
						if (oldDatas.keySet().contains(s)) {
							for (int i = 0; i <oldList.size() ; i++) {
								if(oldList.get(i).get("ID_").toString().equals(s)){
									BoResult result = update(childEnt, oldList.get(i));
									if (result != null) {
										resultList.add(result);
									}
									break;
								}
							}

						}
					}
				}
				//原来的集合不包含提交的记录就要删除。
				List<String> delDatas  =new ArrayList<>();
				for(String subPk : oldDatas.keySet()){
					if(!updSet.contains(subPk)){
						delDatas.add(BeanUtils.ObjectToString(oldDatas.get(subPk)));
						//处理孙表数据删除
						deleteSunBoData(resultList, sunEnts, subPk);
						BoResult result = delete(childEnt, subPk);
						resultList.add(result);
					}
				}
				if (delDatas.size()>0) {
					curSubModifyRes+=String.format("\n删除明细【%s】", StringUtil.join(delDatas, ","));
				}
				if (StringUtil.isNotEmpty(curSubModifyRes)) {
					modifyResult+=String.format("\n子表【%s】变更明细：%s", childEnt.getDesc(),curSubModifyRes);
				}
			}
		}
		// 设置bodefAlias
		setBoDefAlias(resultList, curData.getBoDefAlias());
		if (resultList.size()>0) {
			resultList.get(0).setModifyDetail(modifyResult);
		}
	}

	//子表数据更新后处理孙表数据
	private void updateSunBoData(List<BoEnt> sunEnts,BoData chidData,String childPk,List<BoResult> resultList) throws IOException, ParseException{
		Map<String, Object> subData = chidData.getData();
		if(BeanUtils.isNotEmpty(subData)){
			for (BoEnt sunEnt : sunEnts) {
				//获取原来的数据
				Map<String, Map<String, Object>> oldSunDatas = getOldSubDatas(sunEnt, childPk);
				if(BeanUtils.isNotEmpty(subData.get("sub_"+sunEnt.getName()))){
					JsonNode sunDatas = JsonUtil.toJsonNode(subData.get("sub_"+sunEnt.getName()));
					String sunPkField = sunEnt.getPkKey();
					for (JsonNode sunNode : sunDatas) {
						BoData sunData = new BoData();
						sunData.setBoEnt(sunEnt);
						sunData.setData(JsonUtil.toMap(JsonUtil.toJson(sunNode)));
						Map<String, Object> sunRow = convertDbMap(sunData);
						if (sunData.containKey(sunPkField)) {
							String sunPk = sunData.getString(sunPkField);
							if(oldSunDatas.keySet().contains(sunPk)){//更新
								BoResult result = update(sunEnt, sunRow);
								if (result != null) {
									if("0".equals(result.getParentId())){
										result.setParentId(childPk);
									}
									resultList.add(result);
								}
								oldSunDatas.remove(sunPk);
							}
						}else{//新增
							BoResult result = insert(sunEnt, sunRow, childPk);
							if (result != null) {
								if("0".equals(result.getParentId())){
									result.setParentId(childPk);
								}
								resultList.add(result);
							}
						}
					}
					if(BeanUtils.isNotEmpty(oldSunDatas)){//删除
						for(String sunPk : oldSunDatas.keySet()){
							BoResult result = delete(sunEnt, sunPk);
							resultList.add(result);
						}
					}
				}else if(BeanUtils.isNotEmpty(oldSunDatas)){
					for(String sunPk:oldSunDatas.keySet()){
						BoResult result = delete(sunEnt, sunPk);
						resultList.add(result);
					}
				}
			}
		}
	}

	/**
	 * 删除孙表数据
	 * @param childEnt
	 * @param oldDatas
	 * @param resultList
	 */
	private void deleteSunBoData(BoEnt childEnt,Map<String, Map<String, Object>> oldDatas,List<BoResult> resultList){
		if(BeanUtils.isNotEmpty(childEnt) && BeanUtils.isNotEmpty(childEnt.getChildEntList()) && BeanUtils.isNotEmpty(oldDatas)){
			List<BoEnt> sunEnts = childEnt.getChildEntList();
			for (BoEnt sunEnt : sunEnts) {
				for(String subPk : oldDatas.keySet()){
					Map<String, Map<String, Object>> oldSunDatas = getOldSubDatas(sunEnt, subPk);
					if(BeanUtils.isNotEmpty(oldSunDatas)){
						for(String sunPk : oldSunDatas.keySet()){
							BoResult result = delete(sunEnt, sunPk);
							resultList.add(result);
						}
					}
				}
			}
		}
	}

	/**
	 * 处理孙数据的新增
	 * @param resultList
	 * @param sunEnts
	 * @param chidData
	 * @param parentId
	 */
	private void addSunBoData(List<BoResult> resultList,List<BoEnt> sunEnts,BoData chidData,String parentId){
		//处理孙表数据
		try {
			if(BeanUtils.isNotEmpty(sunEnts)){
				boolean hasSun = false;
				Map<String,Object> sunDataMap = new HashMap<String, Object>();
				for (String subKey : chidData.getData().keySet()) {
					if(subKey.startsWith("sub_") && BeanUtils.isNotEmpty(chidData.getData().get(subKey))){
						sunDataMap.put(subKey, chidData.getData().get(subKey));
						hasSun = true;
					}
				}
				if(hasSun){
					for (BoEnt sunBoEnt : sunEnts) {
						if(sunDataMap.containsKey("sub_"+sunBoEnt.getName())){
							ArrayNode sunArray = (ArrayNode) JsonUtil.toJsonNode(sunDataMap.get("sub_"+sunBoEnt.getName()));
							for (JsonNode jsonNode : sunArray) {
								Map<String, Object> sunRow = JsonUtil.toMap(JsonUtil.toJson(jsonNode));
								BoData sunData = new BoData();
								sunData.setBoDef(chidData.getBoDef());
								sunData.setBoEnt(sunBoEnt);
								sunData.setBoDefAlias(chidData.getBoDefAlias());
								sunData.setData(sunRow);
								Map<String, Object> row = convertDbMap(sunData);
								BoResult sunresult = insert(sunBoEnt, row, parentId);
								resultList.add(sunresult);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("处理孙表数据失败："+e.getMessage());
		}
	}

	private void deleteSunBoData(List<BoResult> resultList,List<BoEnt> sunEnts,String subPk){
		//处理孙表数据删除
		if(BeanUtils.isNotEmpty(sunEnts)){
			for (BoEnt sunEnt : sunEnts) {
				// 获取原来的数据。
				Map<String, Map<String, Object>> oldSunDatas = getOldSubDatas(sunEnt, subPk);
				if(BeanUtils.isNotEmpty(oldSunDatas)){
					for(String sunPk : oldSunDatas.keySet()){
						BoResult sunResult = delete(sunEnt, sunPk);
						resultList.add(sunResult);
					}
				}
			}
		}
	}

	/**
	 * 将数据进行转换。
	 * 
	 * <pre>
	 * 1.将map数据转成DB字段。
	 * 2.转换数据的类型。
	 * </pre>
	 *
	 * @param curData
	 * @return
	 * @throws ParseException
	 */
	private Map<String, Object> convertDbMap(BoData curData) throws ParseException {

		Map<String, Object> rtnMap = new HashMap<String, Object>();
		BoEnt boEnt =  curData.getBoEnt();
		if(boEnt==null){
			return rtnMap;
		}
		List<BoAttribute> list = boEnt.getBoAttrList();
		for (BoAttribute attr : list) {
			BoAttribute boAttr = (BoAttribute) attr;
			String fieldName = boAttr.getFieldName();
			String name = boAttr.getName();
			if (!curData.containKey(name)) continue;
			Object obj = curData.getValByKey(name);
			if(BeanUtils.isNotEmpty(obj) && (!name.equals("form_data_rev_") && !name.equals("F_form_data_rev_"))) {
				String dataType = "varchar";
				if(obj.toString().length() > attr.getIntLen() && dataType.equals(attr.getDataType())){
					throw new BaseException(attr.getDesc() + "字段超过设置的字符长度！");
				}
			}
			/*if(attr.getIsRequired() == 1 && BeanUtils.isEmpty(obj.toString()) && !"hide".equals(attr.getStatus())){
				throw new BaseException(attr.getDesc() + "：请输入内容！");
			}*/
			rtnMap.put(fieldName, obj);
		}

		String pk=boEnt.getPkKey();
		//		String fk=boEnt.getFk();	//去掉添加外键的逻辑

		if (curData.containKey(pk)) {
			rtnMap.put(pk, curData.getString(pk));
		}
		//去掉添加外键sql
		//		if (StringUtil.isNotEmpty(fk) && curData.containKey(fk)) {
		//			rtnMap.put(fk, curData.getString(fk));
		//		}
		if (!boEnt.isExternal()) {
			rtnMap.put("F_form_data_rev_", BeanUtils.isNotEmpty(curData.getByKey("form_data_rev_"))?curData.getByKey("form_data_rev_"):0);
		}
		return rtnMap;
	}

	/**
	 * 插入数据。
	 *
	 * @param row
	 * @param parentId
	 * @return
	 */
	private BoResult insert(BoEnt boEnt, Map<String, Object> row, String parentId) {
		String tableName = "";
		String pkField = "";
		if(boEnt!=null){
			tableName = boEnt.getTableName();
			pkField = boEnt.getPkKey();
		}
		String id = UniqueIdUtil.getSuid();
		StringBuffer fieldNames = new StringBuffer(",");
		StringBuffer params = new StringBuffer();
		final List<Object> values = new ArrayList<Object>();

		fieldNames.append(pkField).append(",");
		params.append("?,");
		values.add(id);

		// 多对多时关联放到第三张表中。
		if(boEnt!=null){
			if (!boEnt.getType().equals(BoConstants.RELATION_MANY_TO_MANY) && StringUtil.isNotEmpty(boEnt.getFk())) {
				fieldNames.append(boEnt.getFk()).append(",");
				params.append("?,");
				values.add(parentId);
			}
		}
		for (Map.Entry<String, Object> entry : row.entrySet()) {
			if(fieldNames.toString().toUpperCase().indexOf(","+entry.getKey().toUpperCase()+",") < 0) {
				fieldNames.append(entry.getKey()).append(",");
				params.append("?,");
				values.add(entry.getValue());
			}
		}
		fieldNames = new StringBuffer(fieldNames.substring(1));
		StringBuffer sql = new StringBuffer();
		if(!"".equals(tableName)&&tableName!=null){
			sql.append(" INSERT INTO ");
			sql.append(tableName);
			sql.append("(");
			sql.append(fieldNames.substring(0, fieldNames.length() - 1));
			sql.append(")");
			sql.append(" VALUES (");
			sql.append(params.substring(0, params.length() - 1));
			sql.append(")");

			SqlModel sqlModel = new SqlModel(sql.toString(), values.toArray());

			// 执行插入动作。
			executeSql(sqlModel, boEnt);

			// 多对多的情况处理中间表。
			if (boEnt.getType().equals(BoConstants.RELATION_MANY_TO_MANY)) {
				String relPk = UniqueIdUtil.getSuid();
				BoDataRel entRel = new BoDataRel(relPk, parentId, id, boEnt.getName());
				boDataRelDao.insert(entRel);
			}
		}


		/**
		 * 结果返回
		 */
		BoResult result = new BoResult();
		result.setParentId(parentId);
		result.setAction(BoConstants.HANDLE_ADD);
		result.setBoEnt(boEnt);
		result.setPk(id);
		return result;
	}

	/**
	 * 
	 * 执行sql语句。
	 * 
	 * @param model
	 * @param boEnt
	 *            :为null时默认是在本地数据与执行该sql void
	 * @return 
	 * @exception
	 * @since 1.0.0
	 */
	@SuppressWarnings("all")
	private int executeSql(SqlModel model, BoEnt boEnt) {
		String sql = model.getSql();
		int update = -1;
		if (StringUtil.isEmpty(sql)) {
			return update;
		}
		Object[] obs = model.getValues();
		if (boEnt == null || !boEnt.isExternal()) {
			update = commonManager.execute(sql, obs);
		} else {
			try {
				ExecutorService executorService = Executors.newCachedThreadPool();
				Future future = executorService.submit(new Callable(){
				    public Object call() throws Exception {
				    	try(DatabaseSwitchResult setDataSource = databaseContext.setDataSource(boEnt.getDsName())){
				    		return commonManager.execute(sql, obs);
						} catch (Exception e) {
							throw new RuntimeException("操作外部表：" + boEnt.getDsName() + " 中的 " + boEnt.getDesc() + " 出错："+e.getMessage(),e);
						}
				        
				    }
				});
				update=(int) future.get();
			} catch (Exception e2) {
				throw new RuntimeException("操作外部表：" + boEnt.getDsName() + " 中的 " + boEnt.getDesc() + " 出错："+e2.getMessage(),e2);
			}
		}
		return update;
	}

	/**
	 * 更新一行数据。
	 * 
	 * @param boEnt
	 * @param row
	 * @return
	 */
	private BoResult update(BoEnt boEnt, Map<String, Object> row) {
		BoResult result = new BoResult();

		String tableName = boEnt.getTableName();
		String pkField = boEnt.getPkKey();
		String pkValue = row.get(pkField).toString();
		String pkType = boEnt.getPkType();

		boolean isDataChange = false;
		if (!boEnt.isExternal()) {
			Map<String, Object> oldEntData = null;
			if(!(Column.COLUMN_TYPE_NUMBER.equals(pkType) || Column.COLUMN_TYPE_INT.equals(pkType))){
				oldEntData = this.getOneById("select * from "+tableName +" where "+pkField+" ='" +pkValue + "'", null);
			}else{
				oldEntData = this.getOneById("select * from "+tableName +" where "+pkField+" ="+pkValue, null);
			}

			if (BeanUtils.isNotEmpty(oldEntData)) {
				String modifyDetail = compareData(boEnt, row, oldEntData);
				if (StringUtil.isNotEmpty(modifyDetail)) {
					result.setModifyDetail(modifyDetail);
					isDataChange =true;
				}
			}
		}else{//外部表更新时不进行数据版本校验
			ThreadMsgUtil.addMapMsg("hasCheckFormDataRev", "true");
		}

		int version = 0;
		if(BeanUtils.isNotEmpty(row.get("F_form_data_rev_"))){
			version = Integer.parseInt(row.get("F_form_data_rev_").toString());
		}
		if (isDataChange) {
			row.put("F_form_data_rev_", version+1);
		}
		final List<Object> values = new ArrayList<Object>();

		StringBuffer set = new StringBuffer();

		for (Map.Entry<String, Object> entry : row.entrySet()) {
			// 主键忽略
			if (pkField.equals(entry.getKey()))
				continue;

			set.append(entry.getKey()).append("=?,");
			values.add(entry.getValue());
		}

		result.setAction(BoConstants.HANDLE_UPDATE);
		result.setBoEnt(boEnt);
		result.setPk(pkValue);

		if (values.size() == 0)
			return result;
		// sql
		StringBuffer sql = new StringBuffer();

		sql.append(" update ");
		sql.append(tableName);
		sql.append(" set ");
		sql.append(set.substring(0, set.length() - 1));
		sql.append(" where ");
		sql.append(pkField);
		sql.append("=?");
		values.add(pkValue);
		//同一个线程只做一次根据表单版本进行的校验
		if (!"true".equals(ThreadMsgUtil.getMapMsg("hasCheckFormDataRev"))) {
			sql.append(" and F_form_data_rev_=?");
			values.add(version);
		}
		SqlModel sqlModel = new SqlModel(sql.toString(), values.toArray());

		int numger = executeSql(sqlModel, boEnt);
		if (numger==0) {
			throw new RuntimeException("表单数据已被其他用户修改，请重新加载数据。");
		}
		ThreadMsgUtil.addMapMsg("hasCheckFormDataRev", "true");
		String fkField = boEnt.getFk();
		if(row.containsKey(fkField)){
			String fkValue = row.get(fkField).toString();
			if(StringUtils.isNotEmpty(fkValue)&&!fkValue.equals("0")){
				result.setParentId(fkValue);
			}
		}

		return result;
	}

	/**
	 * 删除数据。
	 * 
	 * @param boEnt
	 * @return
	 */
	private BoResult delete(BoEnt boEnt, String pk) {
		String sql = "delete  from " + boEnt.getTableName() + "  where " + boEnt.getPkKey() + " =? ";

		SqlModel sqlModel = new SqlModel(sql, new Object[] { pk });

		executeSql(sqlModel, boEnt);
		// 多对多的情况删除关联数据。
		if (boEnt.getType().equals(BoConstants.RELATION_MANY_TO_MANY)) {
			sql = "delete from form_bo_data_relation where SUB_BO_NAME='" + boEnt.getName() + "' and FK_=?";
			sqlModel = new SqlModel(sql, new Object[] { pk });
			executeSql(sqlModel, null);
		}

		// 删除表数据
		BoResult result = new BoResult();
		result.setAction(BoConstants.HANDLE_DELETE); //实体表中的数据被删除，则删除对应的bpm_bus_link里的数据
		result.setBoEnt(boEnt);
		result.setPk(pk);
		return result;
	}

	/**
	 * 根据主键递归获取数据。
	 */
	@Override
	public BoData getResById(Object id, String bodefCode) {
		BoDef boDef = getBoDefByAlias(bodefCode);

		BoData boData = new BoData();
		BoEnt boEnt = boDef.getBoEnt();

		Map<String, Object> row = getById(boEnt, id);
		boData.setData(row);

		getCascadeById(id, boEnt, boData);

		return boData;
	}

	/**
	 * 递归调用。
	 * 
	 * @param id
	 * @param boEnt
	 * @param boData
	 */
	private void getCascadeById(Object id, BoEnt boEnt, BoData boData) {
		// 子表处理
		List<BoEnt> childEntList = boEnt.getChildEntList();

		if (BeanUtils.isEmpty(childEntList))
			return;

		/**
		 * 子表处理。
		 */
		for (BoEnt childEnt : childEntList) {
			List<Map<String, Object>> list = getByFk(childEnt, id);
			String key = childEnt.getName();

			List<BoData> listData = new ArrayList<BoData>();

			for (Map<String, Object> rowMap : list) {
				BoData childData = new BoData();
				Map<String, Object> rtnMap = convertDbToData(childEnt, rowMap);
				childData.setData(rtnMap);
				listData.add(childData);

				String childId = (String) rowMap.get(childEnt.getPkKey());
				// 递归
				getCascadeById(childId, childEnt, childData);
			}
			boData.setSubList(key, listData);
		}

	}

	@Override
	public String saveType() {
		return BoConstants.SAVE_MODE_DB;
	}

	@Override
	public void removeBoData(String boCode, String[] aryIds) {
		BoDef boDef = getBoDefByAlias(boCode);
		BoEnt boEnt = boDef.getBoEnt();
		List<BoEnt> childEntList = boEnt.getChildEntList();

		for (String id : aryIds) {
			delete(boEnt, id);

			for (BoEnt child : childEntList) {
				String sql = "delete  from " + child.getTableName() + "  where " + child.getFk() + " =? ";
				SqlModel sqlModel = new SqlModel(sql, new Object[] { id });

				executeSql(sqlModel, boEnt);
			}
		}
	}

	@Override
	public List<Map<String, Object>> getList(String boCode, Map<String, Object> param) {
		BoDef boDef = getBoDefByAlias(boCode);
		BoEnt boEnt = boDef.getBoEnt();
		StringBuffer sql = new StringBuffer("select * from "+boEnt.getTableName());

		List<Object> p = new ArrayList<Object>();
		if(BeanUtils.isNotEmpty(param)){
			sql.append(" where 1=1 ");
			for (String fieldName : param.keySet()) {
				String filedName = fieldName.toLowerCase();
				// 如果param Name为某个字段。则拼接where 条件
				if(boEnt.getAttrFieldMap().containsKey(filedName)){
					sql.append(" and "+filedName+"=? ");
					p.add(param.get(fieldName));
				}
			}
		}
		List<Map<String, Object>> list = null; 

		if(boEnt.isExternal()) {
			try(DatabaseSwitchResult setDataSource = databaseContext.setDataSource(boEnt.getDsName())){
				list = commonManager.query(sql.toString(), p.toArray());
			} catch (Exception e) {
				throw new RuntimeException("操作外部表：" + boEnt.getDsName() + " 中的 " + boEnt.getDesc() + " 出错：" + ExceptionUtils.getRootCauseMessage(e));
			}
		}
		else {
			list = commonManager.query(sql.toString(), p.toArray());
		}

		// 数据转换
		List<Map<String, Object>> returnData = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> rowMap : list) {
			Map<String, Object> rtnMap = convertDbToData(boEnt, rowMap);
			returnData.add(rtnMap);
		}

		return returnData;
	}

	/**
	 * 查询数据  
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public PageList<Map<String, Object>> getList(String boCode, QueryFilter queryFilter) {
		BoDef boDef = getBoDefByAlias(boCode);
		BoEnt boEnt = boDef.getBoEnt();
		StringBuffer sb = new StringBuffer(" select * from " + boEnt.getTableName());
		// 通过通用查询器查询分页数据
		PageList<Map<String, Object>> queryForPageList = commonManager.query(sb.toString(), queryFilter);

		List<Map<String, Object>> rows = queryForPageList.getRows();

		rows.replaceAll(new UnaryOperator<Map<String,Object>>() {
			@Override
			public Map<String, Object> apply(Map<String, Object> rowMap) {
				return convertDbToData(boEnt, rowMap);
			}
		});
		return queryForPageList;
	}


	private String compareData(BoEnt boEnt, Map<String, Object> newMap,Map<String, Object> oldMap){
		//因为前端和后台返回的字段大小写不一致，所以此处统一的字段别名全部转小写来判断
		Map<String, Object> newData = new HashMap<String, Object>();
		for (Iterator<Entry<String, Object>> iterator = newMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> next = iterator.next();
			newData.put(next.getKey().toLowerCase(), next.getValue());
		}
		Map<String, Object> oldData = new HashMap<String, Object>();
		for (Iterator<Entry<String, Object>> iterator = oldMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> next = iterator.next();
			oldData.put(next.getKey().toLowerCase(), next.getValue());
		}

		Map<String, BoAttribute> attrFieldMap = boEnt.getAttrFieldMap();
		List<String> changeList = new ArrayList<>();
		//首先遍历新的,和老数据中不一致的，则视为修改了。老数据中不存在的，则为新增的。
		for (Iterator<Entry<String, Object>> iterator = newData.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> next = iterator.next();
			String filed = next.getKey();
			if(!filed.equals("f_form_data_rev_")){
				Object newVal = next.getValue();
				Object oldVal = oldData.get(filed);
				if (!BeanUtils.isEquals(newVal, oldVal)) {
					changeList.add(String.format("【%s】由【%s】修改为【%s】", attrFieldMap.get(filed).getDesc(),BeanUtils.ObjectToString(oldVal),BeanUtils.ObjectToString(newVal)));
				}
			}
		}
		//遍历旧的，如果新的不存在则认为已删除
		for (Iterator<Entry<String, Object>> iterator = oldData.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> next = iterator.next();
			if (BeanUtils.isEmpty(newData.get(next.getKey())) && BeanUtils.isNotEmpty(oldData.get(next.getKey()))  && !BoEnt.FK_NAME.equalsIgnoreCase(next.getKey())) {
				BoAttribute boAttribute = attrFieldMap.get(next.getKey());
				if(BeanUtils.isEmpty(boAttribute)) {
					continue;
				}
				String desc = boAttribute.getDesc();
				String objectToString = BeanUtils.ObjectToString(oldData.get(next.getKey()));
				changeList.add(String.format("【%s】由【%s】修改为【】", desc,objectToString));
			}
		}
		return StringUtil.join(changeList, ",");
	}

	/**
	 * 获取子表数据map集合
	 * @param childEnt
	 * @param pk
	 * @return
	 */
	private Map<String, Map<String, Object>> getOldSubDatas(BoEnt childEnt, String pk) {
		Map<String, Map<String, Object>> map = new HashMap<>();
		String pkField = childEnt.getPkKey().toLowerCase();
		List<Map<String, Object>> oldList = this.getByFk(childEnt, pk);

		for (Map<String, Object> row : oldList) {
			for (Map.Entry<String, Object> entry : row.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(pkField)) {
					map.put(entry.getValue().toString(),row);
					break;
				}
			}
		}
		return map;
	}


}
