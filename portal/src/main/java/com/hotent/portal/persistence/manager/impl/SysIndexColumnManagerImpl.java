package com.hotent.portal.persistence.manager.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.constants.SystemConstants;
import com.hotent.base.constants.TenantConstant;
import com.hotent.base.feign.FormFeignService;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.groovy.GroovyScriptEngine;
import com.hotent.base.handler.MultiTenantHandler;
import com.hotent.base.handler.MultiTenantIgnoreResult;
import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.query.PageBean;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.query.QueryOP;
import com.hotent.base.service.InvokeResult;
import com.hotent.base.service.ServiceClient;
import com.hotent.base.util.AppUtil;
import com.hotent.base.util.Base64;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.portal.index.IndexTabList;
import com.hotent.portal.model.SysIndexColumn;
import com.hotent.portal.model.SysObjRights;
import com.hotent.portal.persistence.dao.SysIndexColumnDao;
import com.hotent.portal.persistence.manager.AuthorityManager;
import com.hotent.portal.persistence.manager.SysIndexColumnManager;
import com.hotent.sys.constants.CategoryConstants;
import com.hotent.sys.persistence.manager.SysAuthUserManager;
import com.hotent.sys.persistence.manager.SysCategoryManager;
import com.hotent.sys.persistence.manager.SysTypeManager;
import com.hotent.sys.persistence.model.SysAuthUser.BPMDEFUSER_OBJ_TYPE;
import com.hotent.sys.persistence.model.SysCategory;
import com.hotent.sys.persistence.model.SysType;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * 首页栏目 Service类
 * 
 * @company 广州宏天软件股份有限公司
 * @author maoww
 * @email maoww@jee-soft.cn
 * @date 2018年6月11日
 */
@Service("sysIndexColumnManager")
public class SysIndexColumnManagerImpl extends BaseManagerImpl<SysIndexColumnDao, SysIndexColumn> implements SysIndexColumnManager{
	@Resource
	ServiceClient serviceClient;
	@Resource
	AuthorityManager authorityManager;
	@Resource
	SysCategoryManager sysCategoryManager;
	@Resource
	SysTypeManager sysTypeManager;
	@Resource
	FormFeignService formFeignService;
	@Resource
	UCFeignService ucFeignService;
	@Resource
	MultiTenantHandler multiTenantHandler;
	@Resource
	SysAuthUserManager sysAuthUserManager;
	
	private GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine();

	public SysIndexColumnManagerImpl() {
	}

	/**
	 * 获取有权限的栏目
	 * @param filter
	 * @param params
	 * @param isParse
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SysIndexColumn> getHashRightColumnList(QueryFilter<SysIndexColumn> filter, Map<String, Object> params, Boolean isParse, short type, IUser user) throws Exception {
		List<SysIndexColumn> list;
		try(MultiTenantIgnoreResult setThreadLocalIgnore = MultiTenantHandler.setThreadLocalIgnore()){
			filter.addFilter(multiTenantHandler.getTenantIdColumn(), Arrays.asList(TenantConstant.PLATFORM_TENANT_ID, user.getTenantId()), QueryOP.IN);
			list = getByUserIdFilter(type,TenantConstant.PLATFORM_TENANT_ID, user.getTenantId());
		}
		if (isParse)
			this.parseList(list, params);
		return list;
	}

	private void parseList(List<SysIndexColumn> list, Map<String, Object> params)
			throws Exception {
		if (BeanUtils.isEmpty(list))
			return;
		for (SysIndexColumn sysIndexColumn : list) {
			String templateHtml = parseTemplateHtml(sysIndexColumn, params);
			sysIndexColumn.setTemplateHtml(templateHtml);
		}
	}

	public List<SysIndexColumn> getByUserIdFilter(short type,String ...tenantIds) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		params.put("relationMap", map);
		//获取当前用户布局权限
		try {
			Map<String, Set<String>> authMap = authorityManager.getUserRightMap();
			ArrayNode array = ucFeignService.getCurrentUserAuthOrgLayout(ContextUtil.getCurrentUserId());
			if(BeanUtils.isNotEmpty(array)){
				Set<String> authOrgIdList = new HashSet<String>();
				for (JsonNode jsonNode : array) {
					ObjectNode node = (ObjectNode) jsonNode;
					authOrgIdList.add(node.get("orgId").asText());
				}
				authMap.put("auth_org", authOrgIdList);
			}
			params.put("relationMap", authMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.put("objType", BPMDEFUSER_OBJ_TYPE.INDEX_COLUMN);
		params.put("isPublic", type);
		params.put("tenantIds", Arrays.asList(tenantIds));
		return baseMapper.getByUserIdFilter(params);
	}
	
	/**
	 * 获取栏目的模版的HTML
	 * 
	 * @param sysIndexColumn
	 * @param ctxPath
	 * @return
	 * @throws Exception
	 */
	public String parseTemplateHtml(SysIndexColumn sysIndexColumn, Map<String, Object> params) throws Exception {
		ObjectNode json = parseTemplateJSON(sysIndexColumn, params);
		return json.get("html").asText();
	}


	private Class<?> getParameterTypes(String type) {
		Class<?> claz = null;
		try {
			if (type.equalsIgnoreCase("string")) {
				claz = String.class;
			} else if (type.equalsIgnoreCase("int")) {
				claz = Integer.class;
			} else if (type.equalsIgnoreCase("float")) {
				claz = Float.class;
			} else if (type.equalsIgnoreCase("double")) {
				claz = Double.class;
			} else if (type.equalsIgnoreCase("byte")) {
				claz = Byte.class;
			} else if (type.equalsIgnoreCase("short")) {
				claz = Short.class;
			} else if (type.equalsIgnoreCase("long")) {
				claz = Long.class;
			} else if (type.equalsIgnoreCase("boolean")) {
				claz = Boolean.class;
			} else if (type.equalsIgnoreCase("date")) {
				claz = Date.class;
			} else {
				claz = String.class;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return claz;
	}

	/**
	 * 给栏目添加个别名
	 * 
	 * @param html
	 * @param sysIndexColumn
	 * @param pageBean
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	private String parserHtml(String html, SysIndexColumn sysIndexColumn,
			PageBean pageBean, Map<String, Object> params) throws Exception {

		if (StringUtil.isEmpty(html))
			return "";
		Document doc = Jsoup.parseBodyFragment(html);
		Elements els = doc.body().children();
		if (BeanUtils.isEmpty(els))
			return doc.body().html();
		Element el = els.get(0);
		el.attr("template-alias", sysIndexColumn.getAlias());
		ObjectNode json = JsonUtil.getMapper().createObjectNode();
		// params.remove("__ctx");
		for (String key : params.keySet()) {
			//			json.accumulate(key, params.get(key));
			json.set(key, JsonUtil.toJsonNode(params.get(key)));
		}
		if (BeanUtils.isNotEmpty(pageBean)) {
			json.put("currentPage", pageBean.getPage());
			json.put("pageSize", pageBean.getPageSize());
		}
		el.attr("template-params", json.toString());
		html = doc.body().html();
		return html;

	}


	/**
	 * 获取栏目的模版的HTML
	 * 
	 * @param sysIndexColumn
	 * @param ctxPath
	 * @return
	 * @throws Exception
	 */
	public String parseTemplateHtmlJSON(SysIndexColumn sysIndexColumn, Map<String, Object> params) throws Exception {
		ObjectNode json = parseTemplateJSON(sysIndexColumn, params);
		return json.toString();
	}

	/**
	 * 获取栏目的模版的HTML
	 * 
	 * @param sysIndexColumn
	 * @param ctxPath
	 * @return
	 * @throws Exception
	 */
	public ObjectNode parseTemplateJSON(SysIndexColumn sysIndexColumn, Map<String, Object> params) throws Exception {
		String dataFrom = sysIndexColumn.getDataFrom();
		String html = sysIndexColumn.getTemplateHtml();

		html = Base64.getFromBase64(html);

		Short colType = sysIndexColumn.getColType();
		short dataMode = sysIndexColumn.getDataMode();

		String dataParam = sysIndexColumn.getDataParam();
		Object data = null;
		// 获取具体栏目的数据。
		try {
			Class<?>[] parameterTypes = getParameterTypes(dataParam, params);
			Object[] param = getDataParam(dataParam, params);
			if (SysIndexColumn.DATA_MODE_SERVICE == dataMode) {// service方式
				data = getModelByHandler(dataFrom, param, parameterTypes);
			} else if (SysIndexColumn.DATA_MODE_QUERY == dataMode) { // 自定义查询方式
				String alias = sysIndexColumn.getDataFrom();
				data = formFeignService.getQueryPage(alias);
			}else if(SysIndexColumn.DATA_MODE_WEBSERVICE == dataMode){//WebServices 方法
				data = callWebService(dataFrom);
			}else if(SysIndexColumn.DATA_MODE_RESTFUL == dataMode){//restful 接口
				//restful接口前台取数据
//				JsonNode jsonObj = HttpClientUtil.postHttp(dataFrom, getArrayParams(dataParam,param,parameterTypes));
//				if(BeanUtils.isNotEmpty(jsonObj)){
//					data = jsonObj;
//				}
			}
		} catch (Exception e) {
			// 出异常不影响其它数据
			e.printStackTrace();
		}
		Long height = BeanUtils.isEmpty(sysIndexColumn.getColHeight()) ? 320
				: sysIndexColumn.getColHeight();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("alias", sysIndexColumn.getAlias());
		model.put("title", sysIndexColumn.getName());
		model.put("url", sysIndexColumn.getColUrl());
		model.put("height", height);
//		PageBean pageBean = null;
//		if (sysIndexColumn.getNeedPage() == 1) // 进行分页
//			pageBean = handerPageBean(data);
//		map.put("pageBean", pageBean); // 获取的数据

//		html = "<#setting number_format=\"#\">" + html;

//		try {
//			html = parseByStringTemplate(map, html);
//		} catch (Exception e) {
//			System.out.println("解析模板出错："+e.getMessage());
//		}
//		html = parserHtml(html, sysIndexColumn, pageBean, params);

		ObjectNode json = JsonUtil.getMapper().createObjectNode();
		// 如果是图表则返回数据
		if (BeanUtils.isNotEmpty(colType) && (SysIndexColumn.COLUMN_TYPE_CHART == colType
				|| SysIndexColumn.COLUMN_TYPE_CALENDAR == colType)&&BeanUtils.isNotEmpty(data)) // 加载图表的数据
			json.set("option", JsonUtil.toJsonNode(data));
		// 这些数据前台解析
		json.put("isRefresh", sysIndexColumn.getSupportRefesh());
		json.put("refreshTime", sysIndexColumn.getRefeshTime());
		json.put("show", sysIndexColumn.getShowEffect());
		json.put("type", colType);
		json.put("height", height);
		json.put("html", html);
		if(BeanUtils.isNotEmpty(data)){
			json.set("data", JsonUtil.toJsonNode(data));
		}
		json.set("model", JsonUtil.toJsonNode(model));
		json.put("requestType", sysIndexColumn.getRequestType());
		json.put("dataParam", sysIndexColumn.getDataParam());
		json.put("dataFrom", sysIndexColumn.getDataFrom());
		return json;
	}


	private ArrayNode getArrayParams(String dataParam, Object[] param, Class<?>[] parameterTypes){
		ObjectMapper mapper = JsonUtil.getMapper();
		ArrayNode array = mapper.createArrayNode();
		if(StringUtil.isNotEmpty(dataParam)){
			try {
				JsonNode paramArray = JsonUtil.toJsonNode(dataParam);
				for(int i=0;i<paramArray.size();i++){
					ObjectNode obj = (ObjectNode)paramArray.get(i);
					obj.set("value", JsonUtil.toJsonNode(param[i]));
					obj.put("type", parameterTypes[i].getCanonicalName());
					array.add(obj);
				}
			} catch (Exception e) {}
		}
		return array;
	}


	/**
	 * 根据字符串模版解析出内容
	 * @param obj 需要解析的对象。
	 * @param templateSource	字符串模版。
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private  String parseByStringTemplate(Object obj, String templateSource) throws TemplateException, IOException
	{
		Configuration cfg = new Configuration();
		cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		StringTemplateLoader loader = new StringTemplateLoader();
		cfg.setTemplateLoader(loader);
		cfg.setClassicCompatible(true);
		loader.putTemplate("freemaker", templateSource);
		Template template = cfg.getTemplate("freemaker");   
		StringWriter writer = new StringWriter();   
		template.process(obj, writer); 
		return writer.toString();

	}

	/**
	 * 处理分页数据
	 * 
	 * @param data
	 * @return
	 */
	private PageBean handerPageBean(Object data) {
		PageBean pageBean = null;
		try {
			if (data instanceof PageList<?>) {
				pageBean = new PageBean();
				PageList<?> pageList = (PageList<?>) data;
				int page = (int)pageList.getPage(); // 获取分页的数据
				int pageSize = (int)pageList.getPageSize();
				pageBean.setPage(page);
				pageBean.setPageSize(pageSize);
			} else if (data instanceof IndexTabList) {
				IndexTabList indexTablist = (IndexTabList) data;
				pageBean = getIndexTabPageBean(indexTablist); // 获取分页的数据
			}
		} catch (Exception e) {

		}
		return pageBean;
	}

	private PageBean getIndexTabPageBean(IndexTabList indexTablist) {
		if (BeanUtils.isEmpty(indexTablist))
			return null;
		return indexTablist.getPageBean();
	}

	private Class<?>[] getParameterTypes(String dataParam,
			Map<String, Object> params) throws Exception {
		if (JsonUtil.isEmpty(dataParam) || StringUtil.isEmpty(dataParam))
			return new Class<?>[0];
		JsonNode jary = JsonUtil.toJsonNode(dataParam);
		Class<?>[] parameterTypes = new Class<?>[jary.size()];
		for (int i = 0; i < jary.size(); i++) {
			JsonNode json = jary.get(i);
			String type = "string";
			if(BeanUtils.isNotEmpty(json.get("type"))){
				type = json.get("type").toString();
			}
			parameterTypes[i] = getParameterTypes(type);
		}
		return parameterTypes;
	}

	private Object[] getDataParam(String dataParam, Map<String, Object> params) {
		if (JsonUtil.isEmpty(dataParam) || StringUtil.isEmpty(dataParam))
			return null;
		ArrayNode jary = JsonUtil.getMapper().createArrayNode();
		Object[] args = new Object[jary.size()];
		for (int i = 0; i < jary.size(); i++) {
			ObjectNode json = (ObjectNode) jary.get(i);
			String name = json.get("name").asText();
			String type = json.get("type").asText();
			String mode = json.get("mode").asText();
			String value = json.get("value").asText();

			Object o = value;
			if (mode.equalsIgnoreCase("1")) {// 页面传入的
				o = params.get(name);
				if (JsonUtil.isEmpty(o) && BeanUtils.isNotEmpty(value))// 如果空值则取默认值
					o = value;
			} else if (mode.equalsIgnoreCase("2")) {// 脚本传入
				o = groovyScriptEngine.executeString(value, params);
			}
			Object val = StringUtil.parserObject(o, type);

			args[i] = val;
		}
		return args;
	}

	/**
	 * 根据handler取得数据。
	 * 
	 * <pre>
	 * handler 为 service +"." + 方法名称。
	 * </pre>
	 * 
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	private Object getModelByHandler(String handler, Object[] args,
			Class<?>[] parameterTypes) throws Exception {
		Object model = null;
		if (StringUtil.isEmpty(handler))
			return model;
		int rtn = isHandlerValidNoCmd(handler, parameterTypes);
		if (rtn != 0)
			return model;
		String[] aryHandler = handler.split("[.]");
		if (aryHandler == null)
			return model;
		String beanId = aryHandler[0];
		String method = aryHandler[1];
		// 触发该Bean下的业务方法
		Object serviceBean = AppUtil.getBean(beanId);
		// 如果配置数据来源的方法带有参数的时候

		if (serviceBean == null)
			return model;
		if (args == null || args.length <= 0) {
			parameterTypes = new Class<?>[0];
		}
		Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
				parameterTypes);

		model = invokeMethod.invoke(serviceBean, args);
		if (BeanUtils.isEmpty(model))
			model = null;
		return model;
	}

	/**
	 * 验证handler输入是否是否有效。
	 * 
	 * <pre>
	 * 	handler 输入规则。
	 *  spring的 serviceId +“." + 方法名称。
	 * </pre>
	 * 
	 * @param handler
	 *            spring 的serviceId + "." + 方法名
	 * @param parameterTypes 
	 * @param args 
	 * @return 0 有效，-1，格式不对，-2 没有找到service类，-3没有找到对应的方法，-4，未知的错误。
	 */
	public static int isHandlerValidNoCmd(String handler, Class<?>[] parameterTypes) {

		if (handler.indexOf(".") == -1)
			return -1;
		String[] aryHandler = handler.split("[.]");
		String beanId = aryHandler[0];
		String method = aryHandler[1];
		Object serviceBean = null;
		try {
			serviceBean = AppUtil.getBean(beanId);
		} catch (Exception ex) {
			return -2;
		}
		if (serviceBean == null)
			return -2;

		try {
			Method invokeMethod = serviceBean.getClass().getMethod(method, parameterTypes);
			if (invokeMethod != null) {
				return 0;
			} else {
				return -3;
			}
		} catch (NoSuchMethodException e) {
			return -3;
		} catch (Exception e) {
			return -4;
		}
	}


	/**
	 * 通过别名获取栏目
	 * 
	 * @param columnAlias
	 * @return
	 */
	public SysIndexColumn getByColumnAlias(String alias) {
		SysIndexColumn column= baseMapper.getByColumnAlias(alias);
		if(BeanUtils.isNotEmpty(column)){
			//判断当前用户是否有改栏目的展示权限
			boolean hasRight = sysAuthUserManager.hasRights(column.getId());
			column.setDisplayRights(hasRight);
		}
		return column;
	}

	public String getHtmlByColumnAlias(String alias, Map<String, Object> params)
			throws Exception {
		SysIndexColumn sysIndexColumn = this.getByColumnAlias(alias);
		if (BeanUtils.isEmpty(sysIndexColumn))
			return "";
		return parseTemplateHtmlJSON(sysIndexColumn, params);
	}

	/**
	 * 解析设计模版的html
	 * 
	 * @param designHtml
	 *            设计的html
	 * @param columnList
	 *            有权限栏目列表
	 * @return
	 */
	public String parserDesignHtml(String designHtml, List<SysIndexColumn> columnList) {
		if (StringUtil.isEmpty(designHtml))
			return null;
		Document doc = Jsoup.parseBodyFragment(designHtml);
		Elements els = doc.select("[template-alias]");
		for (Iterator<Element> it = els.iterator(); it.hasNext();) {
			Element el = it.next();
			String value = el.attr("template-alias");
			String h = getSysIndexColumn(value, columnList);
			Element parent = el.parent();
			el.remove();
			parent.append(h);
		}
		designHtml = doc.body().html();
		return designHtml;
	}

	/**
	 * 通过别名获取模版
	 * 
	 * @param alias
	 * @param columnList
	 * @return
	 */
	private String getSysIndexColumn(String alias, List<SysIndexColumn> columnList) {
		for (SysIndexColumn sysIndexColumn : columnList) {
			if (alias.equals(sysIndexColumn.getAlias()))
				return sysIndexColumn.getTemplateHtml();
		}
		return "";
	}

	/**
	 * 取得类型下的列表Map
	 * 
	 * @param columnList
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Map<String, List<SysIndexColumn>> getColumnMap(List<SysIndexColumn> columnList) throws Exception {

		SysCategory sysCategory = sysCategoryManager.getByTypeKey(CategoryConstants.CAT_INDEX_COLUMN.key());
		// 根节点parentId = -1； 标记
		SysType type = new SysType();
		type.setId(sysCategory.getId());
		type.setName(sysCategory.getName());
		type.setParentId("-1");
		type.setTypeKey(sysCategory.getGroupKey());
		
		List<SysType> sysTypeList = sysTypeManager.getByGroupKey(CategoryConstants.CAT_INDEX_COLUMN.key());
		sysTypeList.add(type);
		
		Map<String,String> typeIdName = new HashMap<String,String>();
		for(SysType t : sysTypeList) {
			typeIdName.put(t.getId(), t.getName());
		}
		
		Map<String, List<SysIndexColumn>> map1 = new LinkedHashMap<String, List<SysIndexColumn>>();
		for (SysIndexColumn sysIndexColumn : columnList) {
			String catalog = sysIndexColumn.getCatalog();
			
			if(typeIdName.keySet().contains(catalog)) {
				String name = typeIdName.get(catalog);
				List<SysIndexColumn> list = map1.get(name);
				if (BeanUtils.isEmpty(list)) {
					list = new ArrayList<SysIndexColumn>();
				}
				list.add(sysIndexColumn);
				map1.put(name, list);
			}else {
				String name = "默认栏目";
				List<SysIndexColumn> list = map1.get(name);
				if (BeanUtils.isEmpty(list)) {
					list = new ArrayList<SysIndexColumn>();
				}
				list.add(sysIndexColumn);
				map1.put(name, list);
			}
		}
		return map1;
	}

	@Override
	public Map<String, List<Map<String, Object>>> getColumnMap2(List<SysIndexColumn> columnList) {
		Map<String, List<Map<String, Object>>> result = new LinkedHashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> publicList = new ArrayList<>();
		List<Map<String, Object>> privateList = new ArrayList<>();
		if (BeanUtils.isNotEmpty(columnList)) {
			for(SysIndexColumn item : columnList) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name",item.getName());
				map.put("alias",item.getAlias());
				if(TenantConstant.PLATFORM_TENANT_ID.equals(item.getTenantId())){
					publicList.add(map);
				}else{
					privateList.add(map);
				}
			}
		}
		result.put("公有栏目", publicList);
		result.put("私有栏目", privateList);
		return result;
	}

	@Override
	public Boolean isExistAlias(String alias, String id) {
		if (id == null || "0".equals(id))
			id = null;
		Map<String, Object> params = new HashMap<>();
		params.put("alias", alias);
		params.put("id", id);
		Integer count = baseMapper.isExistAlias(params);
		return count > 0;
	}


	public Object callWebService(String alias) throws Exception {
		try {
			Map<String, Object> params = new  HashMap<String, Object>();
			InvokeResult result = serviceClient.invoke(alias,params);
			ObjectNode jsonObject = JsonUtil.getMapper().createObjectNode();
			if(StringUtil.isNotEmpty(result.getJson())){
				jsonObject = (ObjectNode) JsonUtil.toJsonNode(result.getJson());
				try {
					return JsonUtil.toJsonNode(jsonObject.get("list"));
				} catch (Exception e) {
					return jsonObject.get("list").asText();
				}

			}
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtil.getMapper().createObjectNode();
		}
	}

	@Override
	public List<SysIndexColumn> batchGetColumnAliases(String aliases) {
		if(StringUtil.isEmpty(aliases)) {
			return new ArrayList<>();
		}
		String[] ary = aliases.split(",");
		//该用户有权限的栏目ids
		List<String> hasRightsColIds = sysAuthUserManager.getAuthorizeIdsByUserMap(BPMDEFUSER_OBJ_TYPE.INDEX_COLUMN);
		
		return baseMapper.batchGetByColumnAliases(Arrays.asList(ary),String.join(",", hasRightsColIds));
	}

	@Override
	@Transactional
	public void createAndAuth(SysIndexColumn sysIndexColumn) throws IOException {
		this.create(sysIndexColumn);
		//新建栏目，默认授权给所有人 
		String ownerNameJson = " [{\"type\":\"everyone\",\"title\":\"所有人\",\"checked\":true}]";
		sysAuthUserManager.saveRights(sysIndexColumn.getId(), BPMDEFUSER_OBJ_TYPE.INDEX_COLUMN, ownerNameJson);
	}

	@Override
	public List<SysIndexColumn> getAllByLayoutType(QueryFilter queryFilter) {
		return baseMapper.selectList(convert2Wrapper(queryFilter, currentModelClass()));
	}

}
