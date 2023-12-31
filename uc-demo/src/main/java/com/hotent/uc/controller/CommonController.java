package com.hotent.uc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hotent.base.controller.BaseController;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.PinyinUtil;
import com.hotent.base.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 角色组织模块接口
 * @author zhangxw
 *
 */
@RestController
@RequestMapping("/api/common/v1/")
@Api(tags="CommonController")
public class CommonController extends BaseController {
	/**
	 * 汉字转拼音
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="getPinyin",method=RequestMethod.GET, produces = {
	"application/json; charset=utf-8" })
	@ApiOperation(value = "汉字转拼音", httpMethod = "GET", notes = "汉字转拼音")
	public CommonResult<String> getPinyin(@ApiParam(name="chinese",value="需要获取拼音字母的中文", required = true) @RequestParam String chinese,@ApiParam(name="type",value="类型:1 为全拼，否则为首字母", required = true) @RequestParam Integer type) throws Exception{
		String pinyin = "";
		if(StringUtil.isNotEmpty(chinese)){
			if(type == 1){
				pinyin = PinyinUtil.getPinyin(chinese);
			}else{
				pinyin = PinyinUtil.getPinYinHeadChar(chinese);
			}
		}
		return new CommonResult<String>(true,"获取拼音成功！",pinyin);
	}
}
