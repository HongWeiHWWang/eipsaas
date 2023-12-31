package com.hotent.form.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.model.CommonResult;
import com.hotent.form.generator.GeneratorModel;
import com.hotent.form.generator.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 代码生成器的控制器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年5月7日
 */
@RestController
@RequestMapping("/form/generator/v1/")
@Api(tags="在线代码生成")
@ApiGroup(group= {ApiGroupConsts.GROUP_FORM})
public class GeneratorController {
	@Resource
	GeneratorService generatorService;

	@RequestMapping(value="start", method=RequestMethod.POST, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "开始生成", httpMethod = "POST", notes = "开始进行代码生成")
	public CommonResult<String> start(@ApiParam(name="json",value="代码生成的模型", required = true) @RequestBody GeneratorModel generatorModel) throws Exception{
		String codeFolder = generatorService.generator(generatorModel);
		return new CommonResult<String>(true, "完成代码生成", codeFolder);
	}

	@RequestMapping(value="download" ,method=RequestMethod.GET, produces = { "application/json; charset=utf-8" })
	@ApiOperation(value = "下载生成的代码", httpMethod = "GET", notes = "下载生成的代码")
	public void download(HttpServletResponse response, @ApiParam(name="codeFolder",value="生成的代码目录", required = true) @RequestParam String codeFolder) throws Exception {
		response.reset();
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("APPLICATION/OCTET-STREAM");
		generatorService.download(response, codeFolder);
	}
}
