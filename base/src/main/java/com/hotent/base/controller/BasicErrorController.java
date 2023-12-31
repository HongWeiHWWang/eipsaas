package com.hotent.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.model.CommonResult;

/**
 * 基础的请求错误的处理器
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月4日
 */
@RestController
public class BasicErrorController implements ErrorController{
	private static final String ERROR_PATH="/error";

	@RequestMapping("/error")
	public CommonResult<String> handleError(HttpServletRequest request){
		//获取statusCode:401,404,500
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if(statusCode==404){
			return new CommonResult<>(ResponseErrorEnums.NOT_FOUND);
		}
		else if(statusCode==401) {
			return new CommonResult<>(ResponseErrorEnums.NOLOGIN);
		}
		else {
			return new CommonResult<>(ResponseErrorEnums.SYSTEM_ERROR);
		}
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
