package com.hotent.form.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.controller.BaseController;
import com.hotent.base.feign.UCFeignService;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.BeanUtils;
import com.hotent.base.util.JsonUtil;
import com.hotent.form.manager.FormCodegenLogManager;
import com.hotent.form.model.FormCodegenLog;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 代码生成日志 前端控制器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/formCodegenLog/v1/")
public class FormCodegenLogController extends BaseController<FormCodegenLogManager, FormCodegenLog> {
	@Resource
	UCFeignService ucFeignService;
	
	@GetMapping("/{id}")
	@ApiOperation("根据id查询实体")
	@Override
	public FormCodegenLog getById(@ApiParam(name="id", value="实体id") @PathVariable String id) {
		FormCodegenLog formCodegenLog = baseService.getById(id);
		if(BeanUtils.isNotEmpty(formCodegenLog)) {
			CommonResult<JsonNode> user = ucFeignService.getUserById(formCodegenLog.getCreateBy());
			if(user != null && user.getState()) {
				JsonNode value = user.getValue();
				if(value != null && value.isObject()) {
					String fullname = JsonUtil.getString((ObjectNode)value,"fullname");
					formCodegenLog.setCreateBy(fullname);
				}
			}
		}
		return formCodegenLog;
	}
}
