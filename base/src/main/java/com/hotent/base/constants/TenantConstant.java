package com.hotent.base.constants;

import java.util.Arrays;
import java.util.List;

/**
 * 租户常量
 * @author jason
 * @data 2020-04-19
 */
public interface TenantConstant {

	/**
	 * 平台的租户id
	 */
	String PLATFORM_TENANT_ID = "-1";

	/**
	 * 租户用户系统忽略的菜单
	 */
	List<String> IGNORE_MENU = Arrays.asList("sysPropertiesManager","schedulerManager","tenant","tenantTypeManager",
			"tenantManager","tenantType","tenantParamsManager","sysModuleList","i18n","i18nMessageTypeManager","i18nMessageManager","pwdStrategy");

	/**
	 * 创建租户用户时哪些PORTAL模块的表的数据需要创建一份给租户
	 */
	List<String> INIT_PORTAL_DATA_TABLE_NAMES = Arrays.asList("portal_sys_type","portal_sys_logs_settings");
	
	/**
	 * 创建租户用户时哪些UC模块的表的数据需要创建一份给租户
	 */
	List<String> INIT_UC_DATA_TABLE_NAMES = Arrays.asList("uc_demension");

	/**
	 * 多租户模式下不按照租户隔离的物理表清单
	 */
	final List<String> IGNORE_TABLES = Arrays.asList("act_hi_procinst", "act_hi_actinst", "act_hi_taskinst",
			"act_hi_varinst", "act_hi_detail", "act_hi_comment", "act_hi_attachment", "act_hi_identitylink",
			"act_ge_property", "act_ge_bytearray", "act_re_deployment", "act_re_model", "act_ru_execution",
			"act_ru_job", "act_re_procdef", "act_ru_task", "act_ru_identitylink", "act_ru_variable",
			"act_ru_event_subscr", "act_evt_log", "act_procdef_info","qrtz_job_details","portal_sys_joblog",
			"uc_tenant_manage","uc_tenant_type","uc_tenant_type","uc_tenant_auth","uc_tenant_manage","uc_tenant_params",
			"uc_tenant_mail_server","uc_tenant_logs","portal_sys_datasource_def","portal_i18n_message_type","uc_tenant_ignore_menu",
			"portal_sys_type_group","form_bo_ent","form_bo_ent_relation","form_bo_attr","form_template",
			"bpm_exe_stack_relation_his","bpm_exe_stack_his","uc_pwd_strategy","bpm_exe_stack_relation","bpm_exe_stack","bpm_exe_stack_executor");
}
