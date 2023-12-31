package com.hotent.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.template.TemplateEngine;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.FileUtil;
import com.hotent.base.util.SQLUtil;
import com.hotent.base.util.StringUtil;
import com.hotent.i18n.persistence.manager.I18nMessageManager;
import com.hotent.i18n.persistence.manager.I18nMessageTypeManager;
import com.hotent.i18n.persistence.model.I18nMessage;
import com.hotent.i18n.persistence.model.I18nMessageType;
import com.hotent.i18n.support.service.MessageService;
import com.hotent.i18n.util.I18nUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import poi.util.ExcelUtil;


/**
 *
 * <pre>
 * 描述：国际化资源 控制器类
 * @author zhaoxy
 * @company 广州宏天软件股份有限公司
 * @email zhxy@jee-soft.cn
 * @date 2018-06-06 14:20
 * </pre>
 */
@RestController
@RequestMapping("/i18n/custom/i18nMessage/v1/")
@Api(tags="国际化资源")
@ApiGroup(group= {ApiGroupConsts.GROUP_PORTAL})
public class I18nMessageController extends BaseController<I18nMessageManager, I18nMessage>{
	@Resource
	I18nMessageManager i18nMessageManager;
	@Resource
	MessageService messageService;
	@Resource
	I18nMessageTypeManager i18nMessageTypeManager;
	@Resource
	TemplateEngine templateEngine;


	@RequestMapping(value="list", method= RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "国际化资源列表(分页条件查询)数据", httpMethod = "POST", notes = "国际化资源列表(分页条件查询)数据")
	public PageList<Map<String, String>> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter<I18nMessage> queryFilter) throws Exception {
		return i18nMessageManager.getList(queryFilter);
	}

	@RequestMapping(value="init",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "初始化国际化资源到Cache中", httpMethod = "POST", notes = "初始化国际化资源到Cache中")
	public CommonResult<String> init() throws Exception{
		messageService.initMessage();
		return new CommonResult<>("初始化资源成功");
	}

    public static void main(String[] args) throws  Exception{
        String userIds ="11,12,123,132";
        System.out.println(userIds.split(","));
        //ObjectNode users= JsonUtil.getMapper().createObjectNode();
        //users.put("useridlist", userIds.split(","));
        //System.out.println(JsonUtil.toJson(users));
    }
	@RequestMapping(value="clearCache",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "清空Cache中的所有国际化资源", httpMethod = "POST", notes = "清空Cache中的所有国际化资源")
	public void clearCache() throws Exception{
		messageService.clearAllMessage();
	}

	@RequestMapping(value="getJson",method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "国际化资源明细页面", httpMethod = "GET", notes = "国际化资源明细页面")
	public Object getJson(@ApiParam(name="id",value="i18n定义id", required = true) @RequestParam String id) throws Exception{
		if(StringUtil.isEmpty(id)){
			return new I18nMessage();
		}
		I18nMessage i18nMessage=i18nMessageManager.get(id);
		return i18nMessage;
	}

	@RequestMapping(value="getI18nMessageJson",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据key获取国际化资源", httpMethod = "POST", notes = "根据key获取国际化资源")
	public Object getI18nMessageJson(@ApiParam(name="key",value="i18n定义的key", required = true) @RequestParam String key) throws Exception{
		Map<String,Object> map = null;
		String dbType = SQLUtil.getDbType();
		map = i18nMessageManager.getByMesKey(key,dbType);
		List<Map<String,String>> mesTypeInfo = new ArrayList<Map<String,String>>();
		List<I18nMessageType> typeList = i18nMessageTypeManager.list();
		for(I18nMessageType type:typeList ){
			Map<String,String> m = new HashMap<String,String>();
			m.put("type", type.getType());
			m.put("desc", type.getDesc());
			if(BeanUtils.isNotEmpty(map)){
				if(BeanUtils.isNotEmpty(map.get(type.getType()))){
					m.put("val", map.get(type.getType()).toString());
				}else{
					m.put("val", "");
				}
			}
			mesTypeInfo.add(m);
		}
		if(BeanUtils.isEmpty(map)){
			map = new HashMap<String,Object>();
		}
		map.put("mesTypeInfo", mesTypeInfo);
		return map;
	}


	@RequestMapping(value="getByMessKey",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据key获取国际化资源", httpMethod = "POST", notes = "根据key获取国际化资源")
	public Object getByMessKey(@ApiParam(name="key",value="i18n定义的key", required = true) @RequestParam String key) throws Exception{
		Map<String,Object> map = null;
		String dbType = SQLUtil.getDbType();
		map = i18nMessageManager.getByMesKey(key,dbType);
		return map;
	}


	@RequestMapping(value="delByKey",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据资源key删除国际化资源", httpMethod = "DELETE", notes = "根据资源key删除国际化资源")
	public Object delByKey(@ApiParam(name="key",value="i18n定义的key", required = true) @RequestParam String key) throws Exception{
		try {
			String[] keys=StringUtil.getStringAryByStr(key);
			i18nMessageManager.delByKeys(keys);
			return new CommonResult<String>(true,I18nUtil.getMessage("i18nMessage.deleteSuccess", LocaleContextHolder.getLocale()),null);
		} catch (Exception e) {
			return new CommonResult<String>(false,I18nUtil.getMessage("i18nMessage.deleteFail", LocaleContextHolder.getLocale()),null);
		}
	}

	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存国际化资源信息", httpMethod = "POST", notes = "保存国际化资源信息")
	public Object save(@ApiParam(name="i18nMessage",value="i18n定义的实体", required = true) @RequestBody I18nMessage i18nMessage,
					    @ApiParam(name="oldKey",value="i18n定义的oldKey", required = true) @RequestParam String oldKey) throws Exception{
		try {
			i18nMessageManager.saveI18nMessage(i18nMessage.getKey(), i18nMessage.getMesTypeInfo(),oldKey);
			return new CommonResult<String>(true,I18nUtil.getMessage("i18nMessage.operationSuccess",LocaleContextHolder.getLocale()),null);
		} catch (Exception e) {
			return new CommonResult<String>(false,I18nUtil.getMessage("i18nMessage.operationFail",LocaleContextHolder.getLocale()),null);
		}
	}

	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除国际化资源记录", httpMethod = "DELETE", notes = "批量删除国际化资源记录")
	public Object remove(@ApiParam(name="ids",value="i18n定义ids", required = true) @RequestParam String ids) throws Exception{
		try {
			String[] aryIds=StringUtil.getStringAryByStr(ids);
			i18nMessageManager.removeByIds(aryIds);
			return new CommonResult<String>(true,I18nUtil.getMessage("i18nMessage.deleteSuccess",LocaleContextHolder.getLocale()),null);
		} catch (Exception e) {
			return new CommonResult<String>(false,I18nUtil.getMessage("i18nMessage.deleteFail",LocaleContextHolder.getLocale()),null);
		}
	}

	@RequestMapping("jsResource/{jsAlias}")
	public void getJsResource(@PathVariable("jsAlias") String jsAlias, HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/javascript;charset=utf-8");
		response.setHeader("Cache-Control", "max-age=300");
        response.setHeader("Pragma", "max-age=300");
		response.getWriter().print(getJsResource(jsAlias));
	}

	private List<String> getCodes(String temp){
		List<String> result = new ArrayList<String>();
		Pattern regex = Pattern.compile("\"\\$\\{(.*?)\\}\"");
		Matcher regexMatcher = regex.matcher(temp);
		while (regexMatcher.find()) {
			result.add(regexMatcher.group(1));
		}
		return result;
	}

	// 通过JS资源模板名称获取JS资源文件
	private String getJsResource(String resourceName) throws Exception {
		if (StringUtil.isEmpty(resourceName)) {
			throw new RuntimeException("资源名称不能为空");
		}
		String templatePath = String.format("template/message/%s.ftl", resourceName);
		String jsTemplate = FileUtil.readByClassPath(templatePath);
		if (StringUtil.isEmpty(jsTemplate)) {
			throw new RuntimeException(String.format("资源名称对应的模板文件不存在:%s", templatePath));
		}
		List<String> codes = getCodes(jsTemplate);
		Map<String, String> messages = I18nUtil.getMessages(codes, LocaleContextHolder.getLocale());
		String jsonResource = templateEngine.parseByTemplate(jsTemplate, messages);
		return jsonResource;
	}

	@RequestMapping(value="importMessage",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "从excel导入国际化资源", httpMethod = "POST", notes = "从excel导入国际化资源")
	public Object importMessage(@ApiParam(name="file",value="i18n定义file", required = true) @RequestParam  MultipartFile file) throws Exception{
		String resultMsg = "";
		try {
			i18nMessageManager.importMessage(file);
			resultMsg = "导入国际化资源成功！";
			return new CommonResult<String>(true,resultMsg,null);
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResult<String>(false,e.getMessage(),null);
		}
	}

	@RequestMapping(value="exportMessage",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "导出国际化资源", httpMethod = "POST", notes = "导出国际化资源")
	public void exportMessage(HttpServletResponse response) throws Exception{
		try {

			HSSFWorkbook book = i18nMessageManager.exportExcel();
			ExcelUtil.downloadExcel(book, "国际化资源列表", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@RequestMapping(value="getListJson",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据val获取所有数据", httpMethod = "POST", notes = "根据val获取所有数据")
	public Object getListJson(@ApiParam(name="val",value="i18n定义val", required = true) @RequestParam  String val) throws Exception{
		List<Map<String,Object>> rtn = new ArrayList<Map<String,Object>>();
		List<Map<String,String>> datas = i18nMessageManager.getSearchList(val);
		for(Map<String,String> m : datas){
			Map<String,Object> data = new HashMap<String,Object>();
			List<String> vals = new ArrayList<String>();
			for(String key : m.keySet()){
				if("key_".equals(key.toLowerCase())){
					data.put("key", m.get(key));
				}else{
					vals.add(m.get(key));
				}
			}
			data.put("vals", vals);
			rtn.add(data);
		}
		return rtn ;
	}
}
