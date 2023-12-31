package com.hotent.uc.api.impl.conf;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.hotent.uc.api.impl.model.permission.EveryOnePermission;
import com.hotent.uc.api.impl.model.permission.NonePermission;
import com.hotent.uc.api.impl.model.permission.OrgPermission;
import com.hotent.uc.api.impl.model.permission.PosPermission;
import com.hotent.uc.api.impl.model.permission.RolePermission;
import com.hotent.uc.api.impl.model.permission.ScriptPermission;
import com.hotent.uc.api.impl.model.permission.UsersPermission;
import com.hotent.uc.api.impl.util.PermissionCalc;
import com.hotent.uc.api.impl.var.CurrentUserAccountVar;
import com.hotent.uc.api.impl.var.CurrentUserIdVar;
import com.hotent.uc.api.impl.var.IContextVar;
import com.hotent.uc.api.model.IPermission;

/**
 * 类 {@code PermissionAndCalcConfig} 权限
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
@Configuration
public class PermissionAndCalcConfig {

    /**
     * 获取默认权限类型
     * @param everyOnePermission 所有人权限
     * @param usersPermission 用户权限
     * @param orgPermission 组织权限
     * @param posPermission 岗位权限
     * @param rolePermission 角色权限
     * @return 默认权限类型
     */
	@Bean(name="defaultObjectRightType")
	public ArrayList<IPermission> getDefaultObjectRightType(
			@Qualifier("everyOnePermission") EveryOnePermission everyOnePermission,
			@Qualifier("usersPermission") UsersPermission usersPermission,
			@Qualifier("orgPermission") OrgPermission orgPermission,
			@Qualifier("posPermission") PosPermission posPermission,
			@Qualifier("rolePermission") RolePermission rolePermission){
		ArrayList<IPermission > list = new ArrayList<IPermission >();
		list.add(everyOnePermission);
		list.add(usersPermission);
		list.add(orgPermission);
		list.add(posPermission);
		list.add(rolePermission);
		
		return list;
	}

    /**
     * 获取所有人权限
     * @return 所有人权限
     */
	@Bean("everyOnePermission") 
	public EveryOnePermission everyOnePermission(){
		return new EveryOnePermission();
	}

    /**
     * 获取用户权限
     * @return 用户权限
     */
	@Bean("usersPermission") 
	public UsersPermission usersPermission(){
		return new UsersPermission();
	}

    /**
     * 获取组织权限
     * @return 组织权限
     */
	@Bean("orgPermission") 
	public OrgPermission orgPermission(){
		return new OrgPermission();
	}

    /**
     * 获取岗位权限
     * @return 岗位权限
     */
	@Bean("posPermission") 
	public PosPermission posPermission(){
		return new PosPermission();
	}

    /**
     * 获取角色权限
     * @return 角色权限
     */
	@Bean("rolePermission") 
	public RolePermission rolePermission(){
		return new RolePermission();
	}

    /**
     * 获取无权限
     * @return 无权限
     */
	@Bean("nonePermission") 
	public NonePermission nonePermission(){
		return new NonePermission();
	}

    /**
     * 获取脚本权限
     * @return 脚本权限
     */
	@Bean("scriptPermission") 
	public ScriptPermission scriptPermission(){
		return new ScriptPermission();
	}


    /**
     * 获取表单权限配置
     * @param formPermissionCalcList 表单权限类型
     * @return 表单权限配置
     */
	@Bean(name="formPermissionCalc")
	@Primary
	public PermissionCalc formPermissionCalc(@Qualifier("formPermissionCalcList") ArrayList<IPermission> formPermissionCalcList){
		PermissionCalc permissionCalc = new PermissionCalc();
		permissionCalc.setPermissionList(formPermissionCalcList);
		return permissionCalc;
	}

    /**
     * 获取表单权限类型
     * @param nonePermission 无权限
     * @param everyOnePermission 所有人权限
     * @param scriptPermission 脚本计算权限
     * @param usersPermission 用户权限
     * @param orgPermission 组织权限
     * @param posPermission 岗位权限
     * @param rolePermission 角色权限
     * @return 表单权限类型
     */
	@Bean(name="formPermissionCalcList")
	public ArrayList<IPermission> formPermissionCalcList(
			@Qualifier("nonePermission") NonePermission nonePermission,
			@Qualifier("everyOnePermission") EveryOnePermission everyOnePermission,
			@Qualifier("scriptPermission") ScriptPermission scriptPermission,
			@Qualifier("usersPermission") UsersPermission usersPermission,
			@Qualifier("orgPermission") OrgPermission orgPermission,
			@Qualifier("posPermission") PosPermission posPermission,
			@Qualifier("rolePermission") RolePermission rolePermission){
		ArrayList<IPermission > list = new ArrayList<IPermission >();
		list.add(everyOnePermission);
		list.add(usersPermission);
		list.add(orgPermission);
		list.add(posPermission);
		list.add(rolePermission);
		list.add(nonePermission);
		list.add(scriptPermission);
		return list;
	}

    /**
     * 获取当前用户账号
     * @return 当前用户账号
     */
	@Bean("currentUserAccountVar") 
	public CurrentUserAccountVar currentUserAccountVar(){
		return new CurrentUserAccountVar();
	}

    /**
     * 获取当前用户ID
     * @return 当前用户ID
     */
	@Bean("currentUserIdVar") 
	public CurrentUserIdVar currentUserIdVar(){
		return new CurrentUserIdVar();
	}

    /**
     * 根据当前用户账号和用户ID获取用户信息
     * @param currentUserAccountVar 当前用户账号
     * @param currentUserIdVar 当前用户ID
     * @return 用户信息
     */
	@SuppressWarnings("rawtypes")
	@Bean(name="queryViewComVarList")
	public ArrayList queryViewComVarList(
			@Qualifier("currentUserAccountVar") CurrentUserAccountVar currentUserAccountVar,
			@Qualifier("currentUserIdVar") CurrentUserIdVar currentUserIdVar){
		ArrayList<IContextVar> list = new ArrayList<IContextVar >();
		list.add(currentUserAccountVar);
		list.add(currentUserIdVar);
		return list;
	}
}
