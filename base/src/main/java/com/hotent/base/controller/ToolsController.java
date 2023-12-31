package com.hotent.base.controller;

import java.util.Optional;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.PinyinUtil;
import com.hotent.base.vo.DruidEncryptDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/base/tools/v1/")
@Api(tags="工具接口")
@ApiGroup(group= {ApiGroupConsts.GROUP_BPM,ApiGroupConsts.GROUP_FORM,ApiGroupConsts.GROUP_PORTAL,ApiGroupConsts.GROUP_UC})
public class ToolsController{

	@RequestMapping(value = "getPinyin", method = RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取拼音", httpMethod = "GET", notes = "获取拼音")
	public CommonResult<String> getPinyin(
			@ApiParam(required = true, name = "chinese", value = "中文内容") @RequestParam String chinese,
			@ApiParam(name = "type", value = "类型是1 则为全拼，否则为首字母") @RequestParam Optional<Integer> type
			) throws AuthenticationException {
		Integer ptype = type.orElse(1);	
		String pinying = "";
		//如果是类型是1 则为全拼，否则为首字母
		if(ptype == 1){
			pinying = PinyinUtil.getPinyin(chinese);
		}else{
			pinying = PinyinUtil.getPinYinHeadChar(chinese);
		}
		return new CommonResult<String>(true,"获取拼音成功！",pinying);
	}
	
	@RequestMapping(value = "encryptDbPassword", method = RequestMethod.GET, produces={"application/json; charset=utf-8" })
	@ApiOperation(value = "获取数据库密码加密字符串", httpMethod = "GET", notes = "获取数据库密码加密信息")
	public CommonResult<DruidEncryptDTO> encryptDbPassword(@ApiParam(required = true, name = "password", value = "待加密密码") @RequestParam String password) throws Exception {
        String[] arr = CryptoUtils.genKeyPair(512);
        DruidEncryptDTO druidEncryptDTO = new DruidEncryptDTO();
        druidEncryptDTO.setPassword(CryptoUtils.encrypt(arr[0], password));
        druidEncryptDTO.setPublicKey(arr[1]);
		return new CommonResult<DruidEncryptDTO>(true,"获取拼音成功！",druidEncryptDTO);
	}
}