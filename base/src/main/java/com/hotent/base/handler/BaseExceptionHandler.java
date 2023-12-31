package com.hotent.base.handler;

import java.io.Serializable;
import java.net.ConnectException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ibatis.binding.BindingException;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;
import com.hotent.base.model.CommonResult;
import com.hotent.base.util.StringUtil;
import com.hotent.base.util.ThreadMsgUtil;

/**
 * 全局的异常处理类
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2020年4月4日
 */
@RestControllerAdvice(annotations = { RestController.class, Controller.class })
public class BaseExceptionHandler {
	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 请求参数类型错误异常的捕获
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = { BindException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public CommonResult<String> badRequest(BindException e) {
		log.error("occurs error when execute method ,message {}", ExceptionUtils.getFullStackTrace(e));
		return new CommonResult<>(ResponseErrorEnums.BAD_REQUEST);
	}

	/**
	 * 404错误异常的捕获
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = { NoHandlerFoundException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public CommonResult<String> badRequestNotFound(BindException e) {
		log.error("occurs error when execute method ,message {}", ExceptionUtils.getFullStackTrace(e));
		return new CommonResult<>(ResponseErrorEnums.NOT_FOUND);
	}

	/**
	 * mybatis未绑定异常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BindingException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResult<String> mybatis(Exception e) {
		log.error("occurs error when execute method ,message {}", ExceptionUtils.getFullStackTrace(e));
		return new CommonResult<>(ResponseErrorEnums.BOUND_STATEMENT_NOT_FOUNT);
	}

	/**
	 * 自定义异常的捕获 自定义抛出异常。统一的在这里捕获返回JSON格式的友好提示。
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = { BaseException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public <T extends Serializable> CommonResult<T> sendError(BaseException exception, HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		log.error("occurs error when execute url ={} ,message {}", requestURI, ExceptionUtils.getFullStackTrace(exception));
		return new CommonResult<>(exception.getCode(),
				StringUtil.isNotEmpty(exception.getMessage()) ? exception.getMessage() : exception.getDetailMessage());
	}

	/**
	 * 数据库操作出现异常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = { SQLException.class, DataAccessException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResult<String> systemError(Exception e) {
		log.error("occurs error when execute method ,message {}", ExceptionUtils.getFullStackTrace(e));
		if(e instanceof MyBatisSystemException){
		    return new CommonResult<>(((MyBatisSystemException) e).getRootCause().getMessage());
        }else{
        	CommonResult<String> commonResult = new CommonResult<>(ResponseErrorEnums.DATABASE_ERROR);
        	commonResult.setMessage(String.format("%s：%s", commonResult.getMessage(), ExceptionUtils.getRootCauseMessage(e)));
            return commonResult;
		}
    }

	/**
	 * 网络连接失败！
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = { ConnectException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResult<String> connect(Exception e) {
		log.error("occurs error when execute method ,message {}", ExceptionUtils.getFullStackTrace(e));
		return new CommonResult<>(ResponseErrorEnums.CONNECTION_ERROR);
	}

	@ExceptionHandler(value = { RuntimeException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResult<String> runTimeError(Exception e) {
		log.error("occurs error when execute method ,message {}", ExceptionUtils.getFullStackTrace(e));
		String errorMsg = ExceptionUtils.getRootCauseMessage(e);
		String flowErrorMsg = ThreadMsgUtil.getMapMsg(ThreadMsgUtil.MSG_FLOW_ERROR, true);
		if (StringUtil.isNotEmpty(errorMsg) && errorMsg.indexOf("流程异常") > -1 && StringUtil.isNotEmpty(flowErrorMsg)) {
			errorMsg = flowErrorMsg;
		}else if (StringUtil.isNotEmpty(errorMsg)) {
			errorMsg = errorMsg.replace("RuntimeException:", "");
		}
		return new CommonResult<>(false, errorMsg);
	}

	@ExceptionHandler(value = { Exception.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResult<String> notAllowed(Exception e) {
		log.error("occurs error when execute method ,message {}", ExceptionUtils.getFullStackTrace(e));
		return new CommonResult<>(ResponseErrorEnums.SYSTEM_ERROR);
	}
	
	/**
	 * 校验错误信息收集
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResult<String> MethodArgumentNotValidExceptiona(MethodArgumentNotValidException e) {
		StringBuffer stringBuffer = new StringBuffer();
		BindingResult bindingResult = e.getBindingResult();
		bindingResult.getAllErrors().forEach(error-> stringBuffer.append(error.getDefaultMessage()+" "));
		log.error("occurs error when execute method ,message {}", stringBuffer.toString());
		return new CommonResult<String>(ResponseErrorEnums.ILLEGAL_ARGUMENT.getCode(),stringBuffer.toString());
	}
}
