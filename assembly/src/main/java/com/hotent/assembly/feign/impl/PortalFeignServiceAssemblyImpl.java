package com.hotent.assembly.feign.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hotent.base.enums.ResponseErrorEnums;
import com.hotent.base.exception.BaseException;
import com.hotent.base.feign.PortalFeignService;
import com.hotent.base.jms.Notice;
import com.hotent.base.model.CommonResult;
import com.hotent.base.query.PageList;
import com.hotent.base.query.QueryFilter;
import com.hotent.base.util.JsonUtil;
import com.hotent.file.controller.FileController;
import com.hotent.portal.controller.CalendarController;
import com.hotent.portal.controller.JmsProducerController;
import com.hotent.portal.controller.MessageNewsController;
import com.hotent.portal.controller.MessageReceiverController;
import com.hotent.portal.controller.SysAuthUserController;
import com.hotent.portal.controller.SysDataSourceController;
import com.hotent.portal.controller.SysIdentityController;
import com.hotent.portal.controller.SysLogsSettingsController;
import com.hotent.portal.controller.SysPropertiesController;
import com.hotent.portal.controller.SysRoleAuthController;
import com.hotent.portal.controller.SysTypeController;
import com.hotent.portal.controller.TenantController;
import com.hotent.portal.model.MessageNews;
import com.hotent.portal.params.CalendarVo;
import com.hotent.portal.params.MessaboxVo;
import com.hotent.sys.persistence.model.SysDataSource;
import com.hotent.sys.persistence.model.SysType;
import com.hotent.system.controller.SysExternalUniteController;

@Service
@Primary
public class PortalFeignServiceAssemblyImpl implements PortalFeignService{
	@Resource
	SysRoleAuthController sysRoleAuthController;
	@Resource
	SysDataSourceController sysDataSourceController;
	@Resource
	SysPropertiesController sysPropertiesController;
	@Resource
	SysTypeController sysTypeController;
	@Resource
	JmsProducerController jmsProducerController;
	@Resource
	CalendarController calendarController;
	@Resource
	MessageReceiverController messageReceiverController;
	@Resource
	SysIdentityController sysIdentityController;
	@Resource
	MessageNewsController messageNewsController;
	@Resource
	SysLogsSettingsController sysLogsSettingsController;
	@Resource
	SysAuthUserController sysAuthUserController;
	@Resource
	SysExternalUniteController sysExternalUniteController;
	@Resource
	TenantController tenantController;
	@Resource
	FileController fileController;

	@Override
	public List<HashMap<String, String>> getMethodRoleAuth() {
		try {
			return sysRoleAuthController.getMethodRoleAuth();
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getPropertyByAlias(String alias) {
		try {
			return sysPropertiesController.getByAlias(alias, Optional.empty());
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getByAlias(String alias, String defaultValue) {
		try {
			return sysPropertiesController.getByAlias(alias, Optional.ofNullable(defaultValue));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getSysTypeById(String id) {
		try {
			SysType json = sysTypeController.getJson(id);
			return (ObjectNode)JsonUtil.toJsonNode(json);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getAllSysType(QueryFilter queryFilter) {
		try {
			PageList<SysType> listJson = sysTypeController.listJson(queryFilter);
			return (ObjectNode)JsonUtil.toJsonNode(listJson);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<String> sendNoticeToQueue(Notice notice) {
		try {
			return jmsProducerController.sendNoticeToQueue(notice);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<String> sendToQueue(ObjectNode model) {
		try {
			return jmsProducerController.sendToQueue(model);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getEndTimeByUser(ObjectNode param) {
		try {
			return calendarController.getEndTimeByUser(JsonUtil.toBean(param, CalendarVo.class));
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Long getWorkTimeByUser(ObjectNode param) {
		try {
			String workTimeByUser = calendarController.getWorkTimeByUser(JsonUtil.toBean(param, CalendarVo.class));
			return Long.parseLong(workTimeByUser);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getMessBoxInfo(String account) {
		try {
			MessaboxVo messBoxInfo = messageReceiverController.getMessBoxInfo(account);
			return (ObjectNode)JsonUtil.toJsonNode(messBoxInfo);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public JsonNode getBeanByAlias(String alias) {
		try {
			return sysDataSourceController.getBeanByAlias(alias);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getNextIdByAlias(String alias) {
		try {
			CommonResult<String> nextIdByAlias = sysIdentityController.getNextIdByAlias(alias);
			Assert.isTrue(nextIdByAlias.getState(), nextIdByAlias.getMessage());
			return nextIdByAlias.getValue();
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode getMessageNews(JsonNode queryFilter) {
		try {
			PageList<MessageNews> list = messageNewsController.list(JsonUtil.toBean(queryFilter, QueryFilter.class), Optional.empty());
			return (ObjectNode)JsonUtil.toJsonNode(list);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode publicMsgNews(String ids) {
		try {
			CommonResult<String> publicMsgNews = messageNewsController.publicMsgNews(ids);
			return (ObjectNode)JsonUtil.toJsonNode(publicMsgNews);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public boolean calcPermssion(String permssionJson) {
		try {
			return sysAuthUserController.calcPermssion(permssionJson);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public ObjectNode calcAllPermssion(String permssionJson) {
		try {
			return sysAuthUserController.calcAllPermssion(permssionJson);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public List<String> getAuthorizeIdsByUserMap(String objType) {
		try {
			return sysAuthUserController.getAuthorizeIdsByUserMap(objType);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getToken(String type) {
		try {
			return sysExternalUniteController.getToken(type);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public CommonResult<String> initData(String tenantId) {
		try {
			return tenantController.initData(tenantId);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String getUserInfoUrl(String type, String code) {
		try {
			return sysExternalUniteController.getUserInfoUrl(type,code);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public String wordPrint(ObjectNode objectNode) {
		try {
			return fileController.wordPrint(objectNode);
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Map<String, String> getSysLogsSettingStatusMap() {
		try {
			return sysLogsSettingsController.getSysLogsSettingStatusMap();
		} catch (Exception e) {
			throw new BaseException(ResponseErrorEnums.SERVICE_INVOKE_ERROR, ExceptionUtils.getRootCauseMessage(e));
		}
	}
}
