package com.hotent.form.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.UniqueIdUtil;
import com.hotent.form.enums.FormType;
import com.hotent.form.model.FormMeta;
import com.hotent.form.model.FormTemplate;
import com.hotent.form.persistence.manager.FormMetaManager;
import com.hotent.form.persistence.manager.FormTemplateManager;
import com.hotent.uc.api.impl.util.ContextUtil;
import com.hotent.uc.api.model.IUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 表单模版管理
 * @company 广州宏天软件股份有限公司
 * @author:lj
 * @date:2018年6月7日
 */
@RestController
@RequestMapping("/form/template/v1")
@Api(tags="表单模板")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemplateController extends BaseController<FormTemplateManager, FormTemplate>{
	@Resource
	FormTemplateManager bpmFormTemplateManager;
	@Resource
	FormMetaManager bpmFormDefManager;

	@RequestMapping(value="list", method=RequestMethod.POST, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单模版列表（带分页信息）", httpMethod = "POST", notes = "获取表单模版列表")
	public PageList<FormTemplate> listJson(@ApiParam(name="queryFilter",value="通用查询对象")@RequestBody QueryFilter queryFilter) throws Exception{
		return bpmFormTemplateManager.query(queryFilter);

	}

	@RequestMapping(value="save",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "添加表单模版", httpMethod = "POST", notes = "添加表单模版")
	public CommonResult save(@ApiParam(name="bpmFormTemplate",value="表单模版对象", required = true) @RequestBody FormTemplate bpmFormTemplate) throws Exception{
		String templateId=bpmFormTemplate.getTemplateId();
        bpmFormTemplate.setIsDefault(0);
		if(StringUtil.isEmpty(templateId)){
			bpmFormTemplate.setId(UniqueIdUtil.getSuid());
			bpmFormTemplate.setCanedit(1);
		}
		FormTemplate template=bpmFormTemplateManager.get(templateId);
		if(BeanUtils.isEmpty(template)) {
			bpmFormTemplateManager.create(bpmFormTemplate);
			return new CommonResult(true, "添加成功", null);
		}else {
		    Map<String,Object> map = new HashMap<>();
            map.put("id", templateId);
            map.put("rev", bpmFormTemplate.getRev());
            FormTemplate bpmFormTemplate1=bpmFormTemplateManager.getTemplateByRev(map);
            if(BeanUtils.isNotEmpty(bpmFormTemplate1)) {
                bpmFormTemplateManager.update(bpmFormTemplate);
                return new CommonResult(true, "更新成功", null);
            }else {
                return new CommonResult(false, "此模板不是最新版本，请重新获取再修改", null);
            }
		}
	}

	/**
	 *  复制模板信息
	 * @param request
	 * @param response
	 * @param template
	 * @throws Exception
	 */
	@RequestMapping(value="copyTemplate",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "复制模板信息", httpMethod = "POST", notes = "复制模板信息")
	public CommonResult copyTemplate(@ApiParam(name="templateId",value="表单模版ID", required = true) @RequestParam String templateId,
			@ApiParam(name="newTemplateName",value="新表单模板名字", required = true) @RequestParam String newTemplateName,
			@ApiParam(name="newAlias",value="新表单模板别名", required = true) @RequestParam String newAlias) throws Exception
	{   
		FormTemplate bpmFormTemplate=bpmFormTemplateManager.get(templateId);
		boolean isExist=bpmFormTemplateManager.isExistAlias(newAlias);

		if(isExist){
			return new CommonResult(false,"该别名已被使用",null);
		}else{
			FormTemplate template=new FormTemplate();
			String newId=UniqueIdUtil.getSuid();
			template.setTemplateId(newId);
			template.setTemplateName(newTemplateName);
			template.setAlias(newAlias);
			template.setCanedit(1);
			template.setHtml(bpmFormTemplate.getHtml());
			template.setMacrotemplateAlias(bpmFormTemplate.getMacrotemplateAlias());
			template.setTemplateDesc(bpmFormTemplate.getTemplateDesc());
			template.setTemplateType(bpmFormTemplate.getTemplateType());
			template.setSource("custom");
			bpmFormTemplateManager.create(template);
			return new CommonResult(true,"复制模板成功",null);
		}
	}

	@RequestMapping(value="backUp",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "将用户自定义模板备份", httpMethod = "POST", notes = "将用户自定义模板备份")
	public CommonResult backUp(@ApiParam(name="templateId",value="表单模版ID", required = true)@RequestBody String templateId)throws Exception
	{
		bpmFormTemplateManager.backUpTemplate(templateId);
		return new CommonResult(true,"模板备份成功!",null);
	}

	@RequestMapping(value="templateEdit",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "编辑表单模版信息页面", httpMethod = "POST", notes = "编辑表单模版信息页面")
	public CommonResult edit(@ApiParam(name="templateId",value="表单模版ID", required = true)@RequestBody String templateId) throws Exception{
		FormTemplate bpmFormTemplate=null;
		if(StringUtil.isNotEmpty(templateId)){
			bpmFormTemplate=bpmFormTemplateManager.get(templateId);
		}
		List<FormTemplate> macroTemplates = bpmFormTemplateManager.getAllMacroTemplate();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("bpmFormTemplate", bpmFormTemplate);
		map.put("macroTemplates", macroTemplates);
		return new CommonResult(true,"",map);
	}

	@RequestMapping(value="templateGet",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "表单模版明细页面", httpMethod = "POST", notes = "表单模版明细页面")
	public CommonResult get(@ApiParam(name="templateId",value="表单模版ID", required = true)@RequestBody String templateId) throws Exception{
		FormTemplate bpmFormTemplate=null;
		if(StringUtil.isNotEmpty(templateId)){
			bpmFormTemplate=bpmFormTemplateManager.get(templateId);
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("bpmFormTemplate", bpmFormTemplate);
		return new CommonResult(true,"",map);
	}

	@RequestMapping(value="checkAliasIsExist",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "检查模板别名是否唯一", httpMethod = "POST", notes = "检查模板别名是否唯一")
	public CommonResult<Boolean> checkAliasIsExist(@ApiParam(name="alias",value="表单模版别名", required = true)@RequestBody String alias) throws Exception{
		if(StringUtil.isEmpty(alias))
			return new CommonResult<Boolean>(true,"",false);
		FormTemplate bpmFormTemplate=bpmFormTemplateManager.getByTemplateAlias(alias);
		if(BeanUtils.isEmpty(bpmFormTemplate))
			return new CommonResult<Boolean>(true,"",false);
		return new CommonResult<Boolean>(true,"",true);
	}
	
	@RequestMapping(value="remove",method=RequestMethod.DELETE, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除表单模板", httpMethod = "DELETE", notes = "批量删除表单模板")
	public CommonResult remove(@ApiParam(name="templateId",value="表单模板ID!多个ID用,分割", required = true)@RequestParam String ids) throws Exception{
		String[] aryIds={};
		if(!StringUtil.isEmpty(ids)){
			aryIds=ids.split(",");
		}
		bpmFormTemplateManager.removeByIds(aryIds);
		return new CommonResult(true,"删除表单模版成功",null);
	}
	
	@RequestMapping(value="init",method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "取得初始化模板信息", httpMethod = "POST", notes = "取得初始化模板信息")
	public CommonResult init()throws Exception
	{	
		bpmFormTemplateManager.initAllTemplate();
		return new CommonResult(true,"初始化表单模板成功!");
	}
	
	@RequestMapping(value="selectTemplate",method=RequestMethod.POST,produces={"application/json; charset=utf-8"})
	@ApiOperation(value = "选择模板", httpMethod = "POST", notes = "选择模板")
	public CommonResult selectTemplate(@ApiParam(name="defId",value="表单元数据Id", required = true)@RequestBody String defId,
			@ApiParam(name="isSimple",value="0或者1", required = true)@RequestBody int isSimple,
			@ApiParam(name="templatesId",value="模板id", required = true)@RequestBody String templatesId,
			@ApiParam(name="formType",value="模板类型pc 或者 mobile", required = false)@RequestBody String formType) throws Exception {
		Map map = bpmFormTemplateManager.selectTemplate(defId,isSimple,templatesId,formType);
		return new CommonResult(true,"获取模板成功",map);
	}
	
	@RequestMapping(value="isTemplateByAlias", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "检测请求方法别名是否已经存在", httpMethod = "GET", notes = "检测请求方法别名是否已经存在")
	public @ResponseBody CommonResult<Boolean> isMethodExistByAlias(@ApiParam(name="alias", value="模板别名", required = true)@RequestParam String alias) throws Exception {
		FormTemplate template = bpmFormTemplateManager.getByTemplateAlias(alias);
		CommonResult<Boolean> commonResult = new CommonResult<Boolean>();
		if(BeanUtils.isNotEmpty(template)) {
			commonResult.setValue(true);
		}
		return commonResult;
	}
	
	@RequestMapping(value="getTemplateType", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据模版类型取得模版列表。", httpMethod = "GET", notes = "根据模版类型取得模版列表。")
	public  Map<String,Object> getTemplateType() throws Exception {
		return bpmFormTemplateManager.getTemplateTypeMap("main","macro","subTable");
	}

	@RequestMapping(value="setDefault", method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "设置默认模板", httpMethod = "GET", notes = "设置默认模板")
	public @ResponseBody CommonResult setDefault(@ApiParam(name="id", value="模板id", required = true)@RequestParam String id,@ApiParam(name="type", value="模板类型", required = true)@RequestParam String type) throws Exception {
		bpmFormTemplateManager.setDefault(id,type);
		return new CommonResult(true,"更新成功");
	}
}
