package com.hotent.i18n.persistence.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotent.base.query.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hotent.base.manager.impl.BaseManagerImpl;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.SQLUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.i18n.persistence.dao.I18nMessageDao;
import com.hotent.i18n.persistence.manager.I18nMessageManager;
import com.hotent.i18n.persistence.manager.I18nMessageTypeManager;
import com.hotent.i18n.persistence.model.I18nMessage;
import com.hotent.i18n.persistence.model.I18nMessageType;
import com.hotent.i18n.support.service.MessageService;

import poi.util.ExcelUtil;

/**
 * 
 * <pre> 
 * 描述：国际化资源 处理实现类
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@Service("i18nMessageManager")
public class I18nMessageManagerImpl extends BaseManagerImpl<I18nMessageDao, I18nMessage> implements I18nMessageManager {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	I18nMessageDao i18nMessageDao;
	@Resource
	I18nMessageTypeManager i18nMessageTypeManager;
	@Resource
	MessageService messageService;

	@Override
	public PageList<Map<String,String>> getList(QueryFilter filter) {
		List<I18nMessageType> typeList = i18nMessageTypeManager.list();
		List<String> types = new ArrayList<String>();
		for(I18nMessageType type : typeList){
			types.add(type.getType());
		}
		filter.withParam("types", types);
		List<Map<String,String>> map;
		//添加查询构造条件
		if(filter.getQuerys().size()>0) {
			QueryField field=(QueryField)filter.getQuerys().get(0);
			if(FieldRelation.AND.equals(field.getRelation())) {
				filter.withParam("relation", FieldRelation.AND);
			}
		}
		//分页
        PageBean pageBean = filter.getPageBean();
        IPage<Map<String, String>> page = new Page<Map<String, String>>(0, Integer.MAX_VALUE);
        if(BeanUtils.isNotEmpty(pageBean)){
            page = pageBean==null ? new Page<>() : new Page<Map<String, String>>(pageBean.getPage(), pageBean.getPageSize(), pageBean.showTotal());
        }
		String dbType = SQLUtil.getDbType();
        copyQuerysInParams(filter);
		if("oracle".equals(dbType)){
			map = i18nMessageDao.getList_oracle(page,filter.getParams());
		}else{
			map = i18nMessageDao.getList_mysql(page,filter.getParams());
		}
        PageList<Map<String,String>> pageList = new PageList<Map<String,String>>(map);
        pageList.setTotal(page.getTotal());
        pageList.setRows(map);
        pageList.setPageSize(page.getSize());
        pageList.setPage(page.getCurrent());
		return pageList;
	}
	@Override
	public Map<String,Object> getByMesKey(String key,String dbType) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("key", key);
		//QueryFilter.build().withDefaultPage().getParams();
		List<I18nMessageType> typeList = i18nMessageTypeManager.list();
		List<String> types = new ArrayList<String>();
		for(I18nMessageType type : typeList){
			types.add(type.getType());
		}
		params.put("types", types);
		if("oracle".equals(dbType)){
			return i18nMessageDao.getByMesKey_oracle(params);
		}
		return i18nMessageDao.getByMesKey_mysql(params);
	}
	@Override
	public void delByKey(String key) {
		i18nMessageDao.delByKey(key);
	}
	@Override
	public void delByKeyAndType(String key, String type) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("key",key);
		params.put("type",type);
		i18nMessageDao.delByKeyAndType(params);
	}
	@Override
	public void delByKeys(String... keys) {
		for(String key : keys){
			delByKey(key);
			//同时清除缓存
			messageService.delByKey(key);
		}
	}
	@Override
	public Map<String,Object> saveI18nMessage(String key, List<Map<String, String>> mesTypeInfo,String oldKey) {
		Map<String,Object> rtn = new HashMap<String,Object>();
		for(Map<String, String> m : mesTypeInfo){
			String type = m.get("type").toString();
			String val = m.get("val");
			I18nMessage i18n = null;
			if(key.equals(oldKey)){
				i18n = i18nMessageDao.getByKeyAndType(key, type);
			}else{
				i18n = i18nMessageDao.getByKeyAndType(oldKey, type);
			}
			if(BeanUtils.isEmpty(i18n) && StringUtil.isNotEmpty(val)){//新增
				i18n = new I18nMessage();
				i18n.setId(UniqueIdUtil.getSuid());
				i18n.setType(type);
				i18n.setKey(key);
				i18n.setValue(val);
				i18nMessageDao.insert(i18n);
			}else if (BeanUtils.isNotEmpty(i18n) && StringUtil.isNotEmpty(val)) {// 修改
				i18n.setType(type);
				i18n.setKey(key);
				i18n.setValue(val);
				i18nMessageDao.updateById(i18n);
			}else if(BeanUtils.isNotEmpty(i18n) && StringUtil.isEmpty(val)){// 删除
				messageService.hdel(i18n.getKey(), i18n.getType());
				i18nMessageDao.deleteById(i18n.getId());
			}
		}
		rtn.put("result", true);
		rtn.put("msg", "国际化资源操作成功");
		return rtn;

	}

	@Override
	public Map<String, Object> importMessage(MultipartFile file) throws Exception {
		Map<String,Object> rtnMap = new HashMap<String, Object>();
		Boolean result = true;
		String console = "";
		if(file == null || file.isEmpty()){
			result = false;
			console = "文件为空！";
			throw new RuntimeException(console);
		}

		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
		if(!fileExt.toUpperCase().equals(".XLS") && !fileExt.toUpperCase().equals(".XLSX")){
			result = false;
			console = "上传文件不是excel类型！";
			throw new RuntimeException(console);
		}

		Boolean isExcel2003 = true;
		if(fileExt.toUpperCase().equals(".XLSX")){
			isExcel2003 = false;
		}
		Workbook wb = null;
		if(isExcel2003){
			wb = new HSSFWorkbook(file.getInputStream());
		}else{
			wb = new XSSFWorkbook(file.getInputStream());
		}
		List<I18nMessageType> typeList = new ArrayList<I18nMessageType>();
		List<I18nMessage> messageAddList = new ArrayList<I18nMessage>();
		List<I18nMessage> messageUpdateList = new ArrayList<I18nMessage>();

		String reg = "^[A-Za-z0-9_.]+$";
		String reg1 ="^[A-Za-z0-9-]+$";
		boolean isEmptyExcel=true;
		//处理excel表中的数据
		for(int j=0;j<wb.getNumberOfSheets();j++){
			Sheet sheet = wb.getSheetAt(j);
			if (sheet.getPhysicalNumberOfRows() >1 ) {
				isEmptyExcel=false;
				Map<String,Integer> headMap = new HashMap<String,Integer>();
				for(int i=0;i<sheet.getRow(0).getLastCellNum();i++){
					Row row = sheet.getRow(0);
					headMap.put(row.getCell(i).getStringCellValue().trim(), i);
				}
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {// 从第二行开始读取数据
					Row row = sheet.getRow(i);
					String messageKey =getCellValue(row,headMap,"国际化资源key",j,i,255);//国际化资源key
					String messageValue =getCellValue(row,headMap,"国际化资源值",j,i,512);//国际化资源值
					String messageType = getCellValue(row,headMap,"国际化语言类型",j,i,30);//国际化语言类型
					String messageDesc = getCellValue(row,headMap,"国际化语言类型说明",j,i,64);//国际化语言类型说明
					if (!messageKey.matches(reg)) throw new RuntimeException("Excel表格第"+(j+1)+"个工作簿第"+(i+1)+"行【国际化资源key】不符合规定，只能是数字,字母,点,下划线组成！");
					if (!messageType.matches(reg1)) throw new RuntimeException("Excel表格第"+(j+1)+"个工作簿第"+(i+1)+"行【国际化语言类型】不符合规定，只能是数字,字母或-组成！");
					I18nMessageType t = i18nMessageTypeManager.getByType(messageType);
					if(BeanUtils.isEmpty(t)){
						t = new I18nMessageType();
						t.setId(UniqueIdUtil.getSuid());
						t.setType(messageType);
						t.setDesc(messageDesc);
						dealTypeList(typeList,t);
					}
					I18nMessage m = i18nMessageDao.getByKeyAndType(messageKey, messageType);
					if(BeanUtils.isEmpty(m)){
						m = new I18nMessage();
						m.setId(UniqueIdUtil.getSuid());
						m.setKey(messageKey);
						m.setType(messageType);
						m.setValue(messageValue);
						dealMessageList(messageAddList,m);
					}else{
						m.setValue(messageValue);
						messageUpdateList.add(m);
					}
				}
			}
		}
		if(isEmptyExcel){
			result = false;
			console = "文件为空，导入失败";
			throw new RuntimeException(console);
		}

		for(I18nMessageType type : typeList){
			i18nMessageTypeManager.create(type);
		}
		for(I18nMessage message : messageAddList){
			i18nMessageDao.insert(message);
		}
		for(I18nMessage message : messageUpdateList){
			i18nMessageDao.updateById(message);
		}

		rtnMap.put("result", result);
		rtnMap.put("console", console);

		return rtnMap;
	}

	public String getCellValue(Row row,Map<String,Integer> headMap,String key,int j,int i,int maxLength){
		String val="";
		j++;
		boolean required=!"国际化语言类型说明".equals(key);
		try {
			if(headMap.get(key)==null ) {
				if (required) throw new RuntimeException("Excel表格第"+j+"个工作簿第缺少必填的列【"+key+"】");
			}else{
				if (row.getCell(headMap.get(key))==null ) {
					if (required ) throw new RuntimeException("Excel表格第"+j+"个工作簿第"+(i+1)+"行【"+key+"】必填！");
				}else{
					val=row.getCell(headMap.get(key)).getStringCellValue().trim();
					if(BeanUtils.isEmpty(val)) val="";
					if (required && "".equals(val)) throw new RuntimeException("Excel表格第"+j+"个工作簿第"+(i+1)+"行【"+key+"】必填！"); 
				}
			}
			if (val.length()>maxLength) throw new RuntimeException("Excel表格第"+j+"个工作簿第"+(i+1)+"行【"+key+"】长度过长,最大长度"+maxLength+"！");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return val;
	}

	/**
	 * 新增国际化支持的语言类型
	 * @param list
	 * @param t
	 */
	private void dealTypeList(List<I18nMessageType> list,I18nMessageType t){
		if(list.size() == 0){
			list.add(t);
		}
		for(int j = 0;j<list.size();j++){
			I18nMessageType type = list.get(j);
			if(type.getType().equals(t.getType())){
				break;
			}
			if(j == (list.size()-1) && !type.getId().equals(t.getId())  && !type.getType().equals(t.getType())){
				list.add(t);
			}
		}
	}

	/**
	 * 添加国际化资源
	 * @param list
	 * @param m
	 */
	private void dealMessageList(List<I18nMessage> list,I18nMessage m){
		if(list.size() == 0){
			list.add(m);
		}
		for(int j = 0;j<list.size();j++){
			I18nMessage msg = list.get(j);
			if(msg.getType().equals(m.getType()) && msg.getKey().equals(m.getKey()) ){
				break;
			}
			if(j == (list.size()-1) && !(msg.getKey().equals(m.getKey()) && msg.getType().equals(m.getType()))){
				list.add(m);
			}
		}
	}

	@Override
	public HSSFWorkbook exportExcel() throws Exception {
		List<Map<String, String>> pageList = i18nMessageDao.getI18nInfo();
		if(BeanUtils.isEmpty(pageList)){
			throw new RuntimeException("没有国际化资源，无法进行导出操作！");
		}else{
			Map<String, String> fieldMap = new HashMap<String,String>();
			for(String key :pageList.get(0).keySet()){
				if("ID_".equals(key)) continue;
				if("KEY_".equals(key)){
					fieldMap.put("KEY_", "国际化资源key");
				}
				if("VALUE_".equals(key)){
					fieldMap.put("VALUE_", "国际化资源值");
				}
				if("TYPE_".equals(key)){
					fieldMap.put("TYPE_", "国际化语言类型");
				}
				if("DESC_".equals(key)){
					fieldMap.put("DESC_", "国际化语言类型说明");
				}
			}
			String title = "国际化资源列表";
			return ExcelUtil.exportExcel(title, 24, fieldMap, pageList);
		}
	}
	@Override
	public List<Map<String, String>> getSearchList(String val) {
		List<I18nMessageType> typeList = i18nMessageTypeManager.list();
		List<String> types = new ArrayList<String>();
		for(I18nMessageType type : typeList){
			types.add(type.getType());
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("types", types);
		if(StringUtil.isNotEmpty(val)){
			params.put("val", "%"+val+"%");
		}
		String dbType = SQLUtil.getDbType();
		if("oracle".equals(dbType)){
			return i18nMessageDao.searchList_oracle(params);
		}
		return i18nMessageDao.searchList_mysql(params);
	}
}
