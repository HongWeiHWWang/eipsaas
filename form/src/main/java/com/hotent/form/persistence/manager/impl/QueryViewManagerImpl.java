package com.hotent.form.persistence.manager.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.datasource.DatabaseContext;
import com.hotent.base.datasource.DatabaseSwitchResult;
import com.hotent.base.exception.BaseException;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.manager.CommonManager;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.*;
import com.hotent.base.template.impl.FreeMarkerEngine;
import com.hotent.base.util.*;
import com.hotent.form.model.FormTemplate;
import com.hotent.form.model.QuerySqldef;
import com.hotent.form.model.QueryView;
import com.hotent.form.persistence.dao.QueryViewDao;
import com.hotent.form.persistence.manager.FormTemplateManager;
import com.hotent.form.persistence.manager.QuerySqldefManager;
import com.hotent.form.persistence.manager.QueryViewManager;
import com.hotent.form.util.FreeMakerUtil;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.impl.util.PermissionCalc;
import com.hotent.uc.api.impl.var.IContextVar;
import com.hotent.uc.api.model.IUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service("queryViewManager")
public class QueryViewManagerImpl extends BaseManagerImpl<QueryViewDao, QueryView> implements QueryViewManager {
	private static final String LOGIN_USER = "loginUser";
	private static final String LOGIN_USER_ORGS = "loginUserOrgs";
	private static final String LOGIN_USER_SUB_ORGS = "loginUserSubOrgs";
	private static final String CUSTOM_ORGS = "customOrgs";
	
	@Resource
	QuerySqldefManager querySqldefManager;
	@Resource
	GroovyScriptEngine groovyScriptEngine;
	@Resource
	JdbcTemplate jdbcTemplate;
	@Resource(name="formPermissionCalc")
	PermissionCalc permssionCalc;
	@Resource
	DatabaseContext databaseContext;
	@Resource
	FreeMarkerEngine freemarkEngine;
	@Resource
	FormTemplateManager bpmFormTemplateManager;
	@Resource
	CommonManager commomManager;

	@Override
	public List<QueryView> getBySqlAlias(String sqlAlias) {
		return baseMapper.getBySqlAlias(sqlAlias);
	}

	@Override
	@Transactional
	public void removeBySqlAlias(String sqlAlias) {
		baseMapper.removeBySqlAlias(sqlAlias);
	}

	@Override
	public QueryView getBySqlAliasAndAlias(String sqlAlias, String alias) {
		return baseMapper.getBySqlAliasAndAlias(sqlAlias, alias);
	}

	@Override
	public QueryView getByAlias(String alias) {
		return baseMapper.getByAlias(alias);
	}

	@Override
	public boolean listByAlias(String alias) {
		List<QueryView> queryViews = baseMapper.listByAlias(alias);
		if(queryViews.size()>1){
			return false;
		}
		return true;
	}

	public String getShowSql(QueryView queryView, Map<String, Object> queryParams) throws Exception {
		// 过滤条件是SQL替代，直接返回
		if (queryView.getFilterType() == (short) 2) {
			return executeScript(queryView.getFilter(), queryParams);
		}
		QuerySqldef querySqldef = querySqldefManager.getByAlias(queryView.getSqlAlias());
		return querySqldef.getSql();
	}
	
	@Override
	public String getFilterSql(short filterType,String filterField,String dsName,Map<String, Object> param) throws IOException {
		StringBuffer sb = new StringBuffer();
		String sql = "";
		Map<String, Set<String>> curProfiles =  permssionCalc.getCurrentProfiles();
		List<Map<String,String>> filters = getFilterPermission(filterField,curProfiles); 
		ArrayNode jsonArray = (ArrayNode) JsonUtil.toJsonNode(filterField);
		ObjectNode json = JsonUtil.arrayToObject(jsonArray, "key");
		if(BeanUtils.isEmpty(filters)) return sb.toString();
		for (Map<String,String> map : filters) {
			ObjectNode jsonObject = (ObjectNode) json.get(map.get("filterKey"));
			int type = JsonUtil.getInt(jsonObject, "type", 0);
			if (2==type) {// 过滤条件是SQL替代，直接返回
				return executeScript(jsonObject.get("condition").asText(), param);
			}else if(1==type){// 条件脚本
				String dbType = databaseContext.getDbTypeByAlias(dsName);
				sql = FilterJsonStructUtil.getSql(JsonUtil.getString(jsonObject, "condition"), dbType);
			}else if(3==type){//追加SQL
				sql = executeScript(jsonObject.get("condition").asText(), param);
			}else if(4==type){//数据权限
				sql = getDataPermissionSql(jsonObject.get("condition").asText());
			}
			if(StringUtil.isNotEmpty(sql)){
				if(4!=type){
					sb.append(" AND ");
				}
				sb.append(sql);
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 获取当前用户有权限的过滤条件
	 * @param filterField
	 * @param curProfiles
	 * @return
	 * @throws IOException 
	 */
	private List<Map<String, String>> getFilterPermission(String filterField,
			Map<String, Set<String>> curProfiles) throws IOException {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		if(StringUtil.isEmpty(filterField)) 
			return list;
		ArrayNode jsonAry = (ArrayNode) JsonUtil.toJsonNode(filterField);
		
		for (Object obj : jsonAry) {
			ObjectNode json = (ObjectNode)obj;
			ArrayNode rights = json.get("right").isArray()?(ArrayNode)json.get("right"):(ArrayNode)JsonUtil.toJsonNode(json.get("right").asText());
			boolean hasRight = false;
			for (JsonNode permission : rights) {
				hasRight = permssionCalc.hasRight(permission.toString(), curProfiles);
				if(hasRight){
					break;
				}
			}
			if(hasRight){
				Map<String,String> map = new HashMap<String, String>();
				map.put("name", JsonUtil.getString(json, "name", ""));
				map.put("filterKey", JsonUtil.getString(json, "key", ""));
				list.add(map);
			}
		}
		
		return list;
	}
	
	@Override
	public String getDataPermissionSql(String dataPermission) throws IOException {
		StringBuffer sb = new StringBuffer();
		if(StringUtil.isNotEmpty(dataPermission)){
			ArrayNode permissionArrayJson = (ArrayNode) JsonUtil.toJsonNode(dataPermission);
			Set<String> orgIds = new HashSet<String>(); 
			IUser currentUser = ContextUtil.getCurrentUser();
			//  获取数据权限配置 从缓存中获取
			for (JsonNode node : permissionArrayJson) {
				if(BeanUtils.isNotEmpty(node.get("field"))){
					if(LOGIN_USER.equals(node.get("type").asText())){
						sb.append(" AND "+node.get("field").asText()+"='"+currentUser.getUserId()+"'");
					}else if(LOGIN_USER_ORGS.equals(node.get("type").asText())){
						String currentUserOrgIds = currentUser.getAttrbuite("CURRENT_USER_ORGIDS");
						if(StringUtil.isNotEmpty(currentUserOrgIds)){
							String[] oids = currentUserOrgIds.split(",");
							Set<String> oidSet = new HashSet<String>(Arrays.asList(oids));
							String inSql = StringUtil.convertListToSingleQuotesString(oidSet);
							sb.append(" AND "+node.get("field").asText()+" in ("+inSql+")");
							orgIds.addAll(oidSet);
						}
					}else if(LOGIN_USER_SUB_ORGS.equals(node.get("type").asText())){
						String currentUserSubOrgIds = AuthenticationUtil.getCurrentUserSubOrgIds();
						if(StringUtil.isNotEmpty(currentUserSubOrgIds)){
							String[] oids = currentUserSubOrgIds.split(",");
							Set<String> oidSet = new HashSet<String>(Arrays.asList(oids));
							String inSql = StringUtil.convertListToSingleQuotesString(oidSet);
							sb.append(" AND "+node.get("field").asText()+" in ("+inSql+")");
							orgIds.addAll(oidSet);
						}
					}else if(CUSTOM_ORGS.equals(node.get("type").asText())){
						ArrayNode tmpArray = (ArrayNode)node.get("orgs");
						for (JsonNode tmpJsonNode : tmpArray) {
							orgIds.add(tmpJsonNode.get("id").asText());
						}
						String inSql = StringUtil.convertListToSingleQuotesString(orgIds);
						sb.append(" AND "+node.get("field").asText()+" in ("+inSql+")");
					}
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void handleTemplate(QueryView queryView) throws Exception {
		FormTemplate template = bpmFormTemplateManager.getByTemplateAlias(queryView.getTemplateAlias());

		Map<String, Object> paramMap = new HashMap<String, Object>();
		ArrayNode showsJA = (ArrayNode) JsonUtil.toJsonNode(queryView.getShows());

		// 分离出页头和行内按钮
		ArrayNode buttonsJA = (ArrayNode) JsonUtil.toJsonNode(queryView.getButtons());
		List<ObjectNode> navButtons = new ArrayList<ObjectNode>();// 页头
		List<ObjectNode> rowButtons = new ArrayList<ObjectNode>();// 行内
		for (int i = 0; i < buttonsJA.size(); i++) {
			ObjectNode temp = (ObjectNode) buttonsJA.get(i);
			if (JsonUtil.getString(temp, "hidden", "0").equals("1")) {
				continue;
			}
			if (JsonUtil.getString(temp, "inRow", "0").equals("0")) {
				navButtons.add(temp);
			} else {
				rowButtons.add(temp);
			}
		}
		//是否显示合计
		boolean showSummary = false;
		// 获取默认排序
		String defSortField = "";// 默认排序字段
		String defSortSeq = "";// 默认排序方式
		for (int i = 0; i < showsJA.size(); i++) {
			ObjectNode temp = (ObjectNode) showsJA.get(i);
			if (temp.get("defaultSort").asText().equals("1")) {
				defSortField = JsonUtil.getString(temp, "fieldName");
				defSortSeq = JsonUtil.getString(temp, "sortSeq");
				break;
			}
			if(!showSummary && BeanUtils.isNotEmpty(temp.get("summaryType")) && "sum".equals(temp.get("summaryType").asText())){
				showSummary = true;
			}
		}
		FreeMakerUtil freeMakerUtil = new FreeMakerUtil();
		paramMap.put("util", freeMakerUtil);
		paramMap.put("queryView", queryView);
		paramMap.put("showMap", JsonUtil.arrayToObject(showsJA, "fieldName"));
		paramMap.put("navButtons", navButtons);
		paramMap.put("rowButtons", rowButtons);
		paramMap.put("sortField", defSortField);
		paramMap.put("sortSeq", defSortSeq);
		paramMap.put("showSummary", showSummary);
		paramMap.put("isIndistinct", queryView.getIsIndistinct());
		paramMap.put("conditionAllName", queryView.getConditionAllName());
		paramMap.put("conditionAllDesc", "请输入关键字  "+queryView.getConditionAllDesc());

		String html = freemarkEngine.parseByStringTemplate(template.getHtml(), paramMap);
		queryView.setTemplate(html);
	}

	@Override
	public PageList getShowData(String sqlAlias, String alias, QueryFilter queryFilter, boolean getAll, boolean initSearch) throws Exception {
		QueryView queryView = getBySqlAliasAndAlias(sqlAlias, alias);
		QuerySqldef querySqldef = querySqldefManager.getByAlias(sqlAlias);
		Map<String, Object> queryParams = queryFilter.getParams();
		if (queryFilter.getQuerys().size()>0) {
			JsonNode cdt = JsonUtil.toJsonNode(queryView.getConditions());
			if (BeanUtils.isNotEmpty(cdt)) {
				String tableName = cdt.get(0).get("name").asText().split("\\.")[0];
				List list = queryFilter.getQuerys();
                for (int i = 0; i < list.size(); i++) {
                    QueryField qd = (QueryField) list.get(i);
                    String queryCriteria = qd.getProperty();
                    String table = queryCriteria.substring(0,tableName.length());
                    String fieldName = queryCriteria.substring(tableName.length() + 1, queryCriteria.length());
                    qd.setProperty(table+"."+fieldName);
                }
			}
		}
		PageList pageList = new PageList<>();
		if (initSearch) {
			//更加显示字段组装SQL
			String showSql = "";
			if (StringUtil.isNotEmpty(queryView.getShows())){
				for(int i=0;i<JsonUtil.toJsonNode(queryView.getShows()).size();i++){
					if(JsonUtil.toJsonNode(queryView.getShows()).get(i).get("name").toString().indexOf(".")!= -1) {
						showSql = showSql + JsonUtil.toJsonNode(queryView.getShows()).get(i).get("name")+
								" as "+JsonUtil.toJsonNode(queryView.getShows()).get(i).get("fieldName")+",";
					}else {
						showSql = showSql + JsonUtil.toJsonNode(queryView.getShows()).get(i).get("name") + ",";
					}
				}
				showSql = showSql.substring(0,showSql.length()-1);
			}

			String sql = getShowSql(queryView,queryParams);
			if (StringUtil.isNotEmpty(showSql)){
				if(sql.split("from").length>1){
					String[] strD = sql.split("from");
					sql = "select "+showSql.replaceAll("\"", "")+" from"+strD[1];
				}else{
					String[] strD = sql.split("FROM");
					sql = "SELECT "+showSql.replaceAll("\"", "")+" FROM"+strD[1];
				}

			}

			if (StringUtil.isNotEmpty(queryView.getShows())) {
				List<FieldSort> sorter = queryFilter.getSorter();
				Set<String>  frontSortAttr = new HashSet<>();
				for (FieldSort fieldSort : sorter) {
					frontSortAttr.add(fieldSort.getProperty());
				}
				ArrayNode jsonNode = (ArrayNode) JsonUtil.toJsonNode(queryView.getShows());
				for (JsonNode obj : jsonNode) {
					ObjectNode objectNode = (ObjectNode) obj;
					if (objectNode.hasNonNull("defaultSort") && 1 == objectNode.get("defaultSort").asInt() && !frontSortAttr.contains(objectNode.get("name").asText()) && objectNode.hasNonNull("sortSeq") && StringUtil.isNotEmpty(objectNode.get("sortSeq").asText())) {
						sorter.add(new FieldSort(objectNode.get("name").asText(), Direction.fromString(objectNode.get("sortSeq").asText().toUpperCase())));
					}
				}
			}
			String filterSql = getFilterSql(queryView.getFilterType(),queryView.getFilter(),querySqldef.getDsName(),queryParams);
			queryFilter.withParam("filterSql",filterSql);
			try (DatabaseSwitchResult dResult = databaseContext.setDataSource(querySqldef.getDsName())){
				if (queryView.getNeedPage() == (short) 0 || getAll) {// 不分页
					queryFilter.setPageBean(new PageBean(1, Integer.MAX_VALUE));
				}
				pageList = commomManager.queryByCustomSql(sql, queryFilter);
				handleShowData(queryView, pageList.getRows());
			} catch(Exception e){
				throw new BaseException(e.getMessage());
			}
		}

		return pageList;
	}

	/**
	 * 字符串的常量
	 * 
	 * @param script
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	private String executeScript(String script, Map<String, Object> param) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("param", param);
		vars.putAll(param);
		String str = groovyScriptEngine.executeString(replaceVar(script), vars);
		return str;
	}

	@SuppressWarnings("unchecked")
	private String replaceVar(String str) {
		List<IContextVar> comVarList = (List<IContextVar>) AppUtil.getBean("queryViewComVarList");
		for (IContextVar c : comVarList) {
			str = str.replace("[" + c.getAlias() + "]", c.getValue());
		}
		return "return \"" + str + "\" ;";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void handleShowData(QueryView queryView, List list) throws IOException {
		ArrayNode showJA = (ArrayNode) JsonUtil.toJsonNode(queryView.getShows());
		Map<String, Map<String, Object>> cacheMap = new HashMap<String, Map<String, Object>>();// 在sql中，做缓存
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) list.get(i);
			Map<String, Object> tmp = new HashMap<String, Object>();// 最终的字段
			for (int j = 0; j < showJA.size(); j++) {
				ObjectNode jo = (ObjectNode) showJA.get(j);
				String fieldName = jo.get("fieldName").asText();
				if (JsonUtil.getString(jo, "hidden", "0").equals("0")) {// 不隐藏就添加
				    if(BeanUtils.isNotEmpty(MapUtil.getIgnoreCase(map, fieldName, ""))){
                        tmp.put(fieldName, MapUtil.getIgnoreCase(map, fieldName, "").toString());
                    }else{
                        tmp.put(fieldName, "");
                    }
				} else {
					continue;
				}

				if (JsonUtil.getString(jo, "isVirtual", "0").equals("0")) {// 是虚拟列
					continue;
				}

				String con = MapUtil.getIgnoreCase(map, jo.get("virtualFrom").asText()) + "";// 来源的值
				String str = jo.get("resultFrom").asText().replace("#CON#", con);
				Object val = getValFromCache(cacheMap, fieldName, con);// 先从缓存取
				if (BeanUtils.isEmpty(val)) {// 取不到值才开始计算
					if (jo.get("resultFromType").asText().equals("script")) {
						val = groovyScriptEngine.executeString(str, new HashMap<String, Object>());
					} else if (jo.get("resultFromType").asText().equals("sql")) {
						try {
							Map<String, Object> m = jdbcTemplate.queryForMap(str);
							for (String k : m.keySet()) {// 肯定只有一个值的
								val = m.get(k);
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
							val = "";
						}
					}
					putValToCache(cacheMap, fieldName, con, val);// 增加缓存
				}
				tmp.put(fieldName, val.toString());
			}
			list.set(i, tmp);
		}
	}

	/**
	 * <pre>
	 * 在虚拟列处理工程中，从缓存里取值
	 * 缓存map的格式如下：eg:
	 * sex:{0:男,1:女},isChinese:{0:否,1:是}
	 * </pre>
	 * 
	 * @param cacheMap
	 * @param fieldName
	 *            :需要缓存的虚拟列 列名
	 * @param key
	 *            :来源列的值
	 * @return Object 对应的值
	 * @exception
	 * @since 1.0.0
	 */
	private Object getValFromCache(Map<String, Map<String, Object>> cacheMap, String fieldName, String key) {
		Map<String, Object> map = cacheMap.get(fieldName);
		if (BeanUtils.isEmpty(map)) {
			return null;
		}
		return map.get(key);
	}

	/**
	 * <pre>
	 * 在虚拟列处理工程中，从缓存里取值
	 * 缓存map的格式如下：eg:
	 * sex:{0:男,1:女},isChinese:{0:否,1:是}
	 * </pre>
	 * 
	 * @param cacheMap
	 * @param fieldName
	 *            :需要缓存的虚拟列 列名
	 * @param key
	 *            :来源列的值
	 * @param val
	 *            ：对应的值 void
	 * @exception
	 * @since 1.0.0
	 */
	private void putValToCache(Map<String, Map<String, Object>> cacheMap, String fieldName, String key, Object val) {
		Map<String, Object> map = cacheMap.get(fieldName);
		if (BeanUtils.isEmpty(map)) {
			map = new HashMap<String, Object>();
			cacheMap.put(fieldName, map);
		}
		map.put(key, val);
	}
}
