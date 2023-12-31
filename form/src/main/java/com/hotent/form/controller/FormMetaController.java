package com.hotent.form.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.hotent.form.model.Form;
import com.hotent.form.model.FormMeta;
import com.hotent.form.persistence.manager.FormManager;
import com.hotent.form.persistence.manager.FormMetaManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 表单元数据管理
 * 
 * <pre>
 *  
 * 描述：流程任务表单管理
 * 构建组：x5-bpmx-platform
 * 作者:何一帆
 * 邮箱:heyf@jee-soft.cn
 * 日期:2014-1-10-下午3:29:34
 * 版权：广州宏天软件有限公司版权所有
 * </pre>
 */
@RestController
@RequestMapping("/form/formDef/v1")
@Api(tags = "表单元数据")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FormMetaController extends BaseController<FormMetaManager, FormMeta> {
	@Resource
	FormManager bpmFormManager;

	/**
	 * 流程任务表单列表(分页条件查询)数据
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception PageJson
	 * @exception @since 1.0.0
	 */
	@RequestMapping(value = "list", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "流程任务表单列表(分页条件查询)数据", httpMethod = "POST", notes = "流程任务表单列表(分页条件查询)数据")
	public PageList listJson(@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter)
			throws Exception {
		return baseService.listJson(queryFilter);
	}

	/**
	 * 获取与BO关联的所有表单
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "listJsonByBODef", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "流程任务表单列表(分页条件查询)数据", httpMethod = "POST", notes = "流程任务表单列表(分页条件查询)数据")
	public PageList listJsonByBODef(
			@ApiParam(name = "queryFilter", value = "通用查询对象") @RequestBody QueryFilter queryFilter,
			@ApiParam(name = "defId", value = "表单元数据定义ID") @RequestBody String defId,
			@ApiParam(name = "formType", value = "表单类型 ") @RequestBody String formType,
			@ApiParam(name = "topDefKey", value = "") @RequestBody String topDefKey) throws Exception {
		return baseService.listJsonByBODef(queryFilter, defId, formType, topDefKey);
	}

	@RequestMapping(value = "get", method = RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "获取表单元数据详情", httpMethod = "GET", notes = "编辑表单元数据页面")
	public CommonResult get(@ApiParam(name = "formDefId", value = "表单元数据ID") @RequestParam String formDefId)
			throws Exception {
		FormMeta bpmForm = null;
		if (StringUtil.isNotEmpty(formDefId)) {
			bpmForm = baseService.get(formDefId);
		}
		if (BeanUtils.isEmpty(bpmForm)) {
			return new CommonResult(false, "未获取到表单定义", null);
		} else {
			return new CommonResult(true, null, bpmForm);
		}
	}

	@RequestMapping(value = "getFormFieldTree", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "根据表单元数据ID获取表单的字段信息", httpMethod = "POST", notes = "编辑表单页面")
	public CommonResult getBoTreeByFormId(@ApiParam(name = "id", value = "表单ID") @RequestBody String formId)
			throws Exception {
		FormMeta bpmForm = baseService.get(formId);
		JsonNode fields = bpmForm.getFieldList();
		return new CommonResult(true, null, fields);
	}

	@RequestMapping(value = "getObject", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "根据根据表单key或id获取表单元数据定义。", httpMethod = "POST", notes = "编辑表单页面")
	public FormMeta getObject(@ApiParam(name = "id", value = "表单ID") @RequestBody String id,
			@ApiParam(name = "key", value = "表单key") @RequestBody String key) throws Exception {
		if (StringUtil.isNotEmpty(id)) {
			return baseService.get(id);
		}
		if (StringUtil.isNotEmpty(key)) {
			return baseService.getByKey(key);
		}
		return null;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "保存表单信息。", httpMethod = "POST", notes = "保存表单信息")
	public CommonResult save(@ApiParam(name = "form", value = "表单元数据对象") @RequestBody FormMeta bpmFormdef)
			throws Exception {
		if (BeanUtils.isNotEmpty(bpmFormdef.getId())) {
			baseService.update(bpmFormdef);
		} else {
			String formKey = bpmFormdef.getKey();
			if (baseService.getByKey(formKey) != null) {
				throw new RuntimeException("表单已经存在！key:" + formKey);
			}
			bpmFormdef.setId(UniqueIdUtil.getSuid());
			baseService.create(bpmFormdef);
		}
		return new CommonResult(true, "操作成功", null);
	}

	@RequestMapping(value = "remove", method = RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "批量删除表单记录。", httpMethod = "POST", notes = "保存表单信息")
	public CommonResult remove(@ApiParam(name = "id", value = "表单元数据ID!多个ID用,分割") @RequestBody String id)
			throws Exception {
		String[] aryIds = null;
		if (!StringUtil.isEmpty(id)) {
			aryIds = id.split(",");
		}
		String bpmNames = checkBpmForm(aryIds);// 检查是否绑定了表单
		if (StringUtil.isEmpty(bpmNames)) {
			baseService.removeByIds(aryIds);
			return new CommonResult(true, "删除流程任务表单成功", null);
		} else {
			String msg = "已被用于生成业务表单：" + bpmNames + "不能被删除";
			return new CommonResult(false, msg, null);
		}
	}

	/**
	 * 删除表单时检查是否绑定了流程 目前是先找表单和业务对象的关系，然后再找业务对象和流程的关系
	 * 
	 * @param aryIds
	 * @return
	 */
	private String checkBpmForm(String[] aryIds) {
		String formKey = "";
		for (String defId : aryIds) {// 多个表单同时删除
			List<Form> form = bpmFormManager.getByDefId(defId);
			if (BeanUtils.isNotEmpty(form)) {
				for (Form f : form)
					formKey += f.getName() + "（" + f.getFormKey() + "）,";
			}

		}

		return formKey;
	}

	@RequestMapping(value = "chooseDesignTemplate", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "加载编辑器设计模式的模板列表", httpMethod = "POST", notes = "加载编辑器设计模式的模板列表")
	public CommonResult chooseDesignTemplate(@ApiParam(name = "subject", value = "标题") @RequestBody String subject,
			@ApiParam(name = "categoryId", value = "") @RequestBody String categoryId,
			@ApiParam(name = "formDesc", value = "表单描述") @RequestBody String formDesc,
			@ApiParam(name = "isSimple", value = "true将只允许选择一行") @RequestBody Boolean isSimple) throws Exception {
		Map mv = baseService.getChooseDesignTemplate(subject, categoryId, formDesc, isSimple);
		return new CommonResult(true, null, mv);
	}

	@RequestMapping(value = "boFormDefDialog", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "表单定义对话框。 ", httpMethod = "POST", notes = "表单定义对话框。 ")
	public CommonResult dialog(@ApiParam(name = "isSingle", value = "true将只允许选择一行") @RequestBody Boolean isSingle,
			@ApiParam(name = "defId", value = "表单元数据定义ID") @RequestBody String defId,
			@ApiParam(name = "formType", value = "表单类型 ") @RequestBody String formType,
			@ApiParam(name = "topDefKey", value = "") @RequestBody String topDefKey) throws Exception {
		Map map = new HashMap();
		map.put("isSingle", isSingle);
		map.put("formType", formType);
		map.put("defId", defId);
		map.put("topDefKey", topDefKey);
		return new CommonResult(true, null, map);
	}

	@RequestMapping(value = "checkkeyIsExist", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "判断key是否存在。 ", httpMethod = "POST", notes = "表单定义对话框。 ")
	public boolean checkAliasIsExist(@ApiParam(name = "key", value = "表单key") @RequestBody String key)
			throws Exception {
		if (StringUtil.isEmpty(key))
			return false;
		FormMeta bpmForm = baseService.getByKey(key);
		if (BeanUtils.isEmpty(bpmForm))
			return false;
		return true;
	}

	@RequestMapping(value = "getOpinionConf", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "获得表单意见配置 ", httpMethod = "POST", notes = "获得表单意见配置 ")
	public String opinionConf(@ApiParam(name = "id", value = "表单元数据ID") @RequestBody String id) throws Exception {
		FormMeta def = baseService.get(id);
		String opinionConf = def.getOpinionConf();
		if (StringUtil.isEmpty(opinionConf))
			opinionConf = "[]";
		return opinionConf;
	}

	// 更新表单意见配置。
	@RequestMapping(value = "opinionConfSave", method = RequestMethod.POST, produces = {
			"application/json; charset=utf-8" })
	@ApiOperation(value = "更新表单意见配置。 ", httpMethod = "POST", notes = "更新表单意见配置。 ")
	public CommonResult opinionConfSave(@ApiParam(name = "id", value = "表单元数据id") @RequestBody String id,
			@ApiParam(name = "opinionConf", value = "表单元数据意见配置") @RequestBody String opinionConf) throws Exception {
		baseService.updateOpinionConf(id, opinionConf);
		return new CommonResult(true, "更新表单意见配置成功！", null);
	}

}
