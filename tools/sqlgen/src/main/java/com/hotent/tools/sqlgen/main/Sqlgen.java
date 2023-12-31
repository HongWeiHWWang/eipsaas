package com.hotent.tools.sqlgen.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.hotent.tools.sqlgen.entity.ObjectFactory;
import com.hotent.tools.sqlgen.entity.Resources;
import com.hotent.tools.sqlgen.util.FileUtil;
import com.hotent.tools.sqlgen.util.FreemarkUtil;
import com.hotent.tools.sqlgen.util.StringUtil;
import com.hotent.tools.sqlgen.util.XmlUtil;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * SQL生成
 * 
 * <pre>
 * 构建组：x6-tools
 * 作者：hugh zhuang
 * 邮箱:zhuangxh@jee-soft.cn
 * 日期:2014-9-26-上午10：09:54
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
public class Sqlgen {
	/**
	 * Spring的配置路径（相对于classpath）
	 */
	private static String[] springContextXml = { "classpath*:conf/x6-tools-sqlgen.xml" };
	
	/**
	 * 生成SQL的XML路径（相对于classpath）
	 */
	private String resourcePath = "sqlgen.xml";

	/**
	 * Spring 连接数据库
	 */
	private JdbcTemplate jdbcTemplate;

	private ApplicationContext applicationContext;

	/**
	 * 初始化Spring的配置
	 * 
	 * @throws IOException
	 * @throws BeansException
	 */
	private void init() throws Exception {
		applicationContext = new ClassPathXmlApplicationContext(springContextXml);
		jdbcTemplate = (JdbcTemplate) applicationContext
				.getBean("jdbcTemplate");

	
	}

	private Resources getResources() throws Exception {
		
		Resource resource = new ClassPathResource(resourcePath);
		InputStream is=null;
		try{
			is=resource.getInputStream();
		}
		//在ant下执行会找不到路径，使用catch 。
		catch(Exception ex){
			File file=new File("");
			String path=file.getAbsolutePath() + File.separator + resourcePath;
			is=new FileInputStream(path);
		}
		
		
		return ((Resources) XmlUtil.unmarshall(ObjectFactory.class, is));
	}

	/**
	 * 主要生成SQL
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String genSql() throws Exception {

		Resources resources = this.getResources();
		// <<表,<原来的ID，现在的ID>>
		Map<String, Map<String, String>> tableMap = new HashMap<String, Map<String, String>>();
		StringBuffer sb = new StringBuffer();
		for (com.hotent.tools.sqlgen.entity.Resource r : resources
				.getResource()) {
			// 生成SQL模版
			String template = r.getTemplate();
			// 根据查询的SQl
			String sql = r.getSql();
			// 父类ID
			String parentId = r.getParentId();
			// 父类ID的值
			String parentIdVal = StringUtil.isNotEmpty(parentId) ? r
					.getParentIdVal() : "";
			// 表
			String table = r.getTable();
			// 主键
			String pk = r.getPk();
			// 主表
			String mainTable = r.getMainTable();
			// 外键
			String fk = r.getFk();
			// 变量
			String var = r.getVariables();
			JSONObject json = JSONObject.fromObject(var);
			Map<String, Object> variables = new HashMap<String, Object>();
			if (!JSONUtils.isNull(json))
				variables = json;
			if (StringUtil.isNotEmpty(parentId))
				variables.put(parentId, parentIdVal);

			List<Map<String, Object>> dataList = getNewData(sql, pk, parentId,
					parentIdVal, parentIdVal, table, mainTable, fk, variables, tableMap,
					new HashMap<String, String>(),
					new ArrayList<Map<String, Object>>());
			for (Map<String, Object> dataMap : dataList) {
				sb.append(FreemarkUtil.parseByStringTemplate(dataMap, template));
			}
		}
		if (resources.isIsEndSql())
			sb.append("\ncommit;");
		String filePath = resources.getFilePath();
		FileUtil.writeFile(filePath, sb.toString());

		System.out.println("============生成SQL成功：" + filePath);
		return filePath;
	}

	/**
	 * 新的生成SQL数据
	 * 
	 * @param sql
	 * @param pk
	 * @param parentId
	 * @param parentIdVal
	 * @param newPkValue
	 * @param table
	 * @param mainTable
	 * @param fk
	 * @param variables
	 * @param tableMap
	 * @param pkMap
	 * @param dataList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getNewData(String sql, String pk,
			String parentId, String parentIdVal, String newPkValue,
			String table, String mainTable, String fk,
			Map<String, Object> variables,
			Map<String, Map<String, String>> tableMap,
			Map<String, String> pkMap, List<Map<String, Object>> dataList)
			throws Exception {
		String sqlStr = FreemarkUtil.parseByStringTemplate(variables, sql)
				.trim();
		List<Map<String, Object>> dataMaps = jdbcTemplate.queryForList(sqlStr);
		if (StringUtil.isEmpty(dataMaps))
			return dataList;
		Map<String, String> mainPkMap = tableMap.get(mainTable);
		// 行遍历数据
		for (Map<String, Object> map : dataMaps) {
			Iterator<?> it = map.entrySet().iterator();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			boolean flag = true;
			// 遍历一行数据
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = ((Entry<String, Object>) it
						.next());
				String key = entry.getKey().toUpperCase();
				Object val = entry.getValue();
				// 特殊字符的处理
				if (val instanceof String && StringUtil.isNotEmpty(val))
					entry.setValue(val.toString().replaceAll("&", "'||'&'||'"));
				// 主键
				if (key.equals(pk.toUpperCase())) {
					String pkVal = val.toString();
					String newPkVal = String.valueOf(dataList.size() + 1);
					pkMap.put(pkVal, newPkVal);
					entry.setValue(newPkVal);
					variables.put(parentId, val.toString());
				}
				// 父类ID
				else if (StringUtil.isNotEmpty(parentId)
						&& key.equals(parentId.toUpperCase()))
					entry.setValue(newPkValue);
				// 外键
				else if (StringUtil.isNotEmpty(fk)
						&& key.equals(fk.toUpperCase())
						&& StringUtil.isNotEmpty(mainPkMap)) {
					Long fkVal = Long.parseLong(val.toString());
					if (StringUtil.isEmpty(mainPkMap.get(fkVal))) {
						flag = false;
						break;
					}
					entry.setValue(mainPkMap.get(fkVal).toString());
				}

				dataMap.put(key, entry.getValue());
			}
			if (flag)
				dataList.add(dataMap);

			// 有父类ID的递归
			if (StringUtil.isNotEmpty(parentId))
				this.getNewData(sql, pk, parentId, parentIdVal, dataMap.get(pk)
						.toString(), table, mainTable, fk, variables, tableMap,
						pkMap, dataList);
		}
		tableMap.put(table, pkMap);
		return dataList;
	}

	/**
	 * 执行语句
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		try {
			this.init();
			this.genSql();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Sqlgen sqlgen = new Sqlgen();
		sqlgen.execute();
	}

}
