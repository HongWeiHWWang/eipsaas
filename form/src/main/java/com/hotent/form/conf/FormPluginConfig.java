package com.hotent.form.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hotent.activemq.model.JmsTableTypeConf;
import com.hotent.activemq.model.JmsTableTypeFiledDetail;

@Configuration
public class FormPluginConfig {
	
	
//	@Bean(name="defaultObjectRightType")
//	public ArrayList<IPermission> getDefaultObjectRightType(
//			@Qualifier("everyOnePermission") EveryOnePermission everyOnePermission,
//			@Qualifier("usersPermission") UsersPermission usersPermission,
//			@Qualifier("orgPermission") OrgPermission orgPermission,
//			@Qualifier("posPermission") PosPermission posPermission,
//			@Qualifier("rolePermission") RolePermission rolePermission){
//		ArrayList<IPermission > list = new ArrayList<IPermission >();
//		list.add(everyOnePermission);
//		list.add(usersPermission);
//		list.add(orgPermission);
//		list.add(posPermission);
//		list.add(rolePermission);
//		
//		return list;
//	}
//	
	
	
//	@Bean("everyOnePermission") 
//	public EveryOnePermission everyOnePermission(){
//		return new EveryOnePermission();
//	}
//	
//	@Bean("usersPermission") 
//	public UsersPermission usersPermission(){
//		return new UsersPermission();
//	}
//	
//	@Bean("orgPermission") 
//	public OrgPermission orgPermission(){
//		return new OrgPermission();
//	}
//	
//	@Bean("posPermission") 
//	public PosPermission posPermission(){
//		return new PosPermission();
//	}
//	
//	@Bean("rolePermission") 
//	public RolePermission rolePermission(){
//		return new RolePermission();
//	}
//	
//	@Bean("nonePermission") 
//	public NonePermission nonePermission(){
//		return new NonePermission();
//	}
//	
//	@Bean("scriptPermission") 
//	public ScriptPermission scriptPermission(){
//		return new ScriptPermission();
//	}
	@Bean("formTableTypeConf")
	public JmsTableTypeConf TableTypeConf() {
		JmsTableTypeConf.AddTypeConf("DEF_TYPE",new JmsTableTypeFiledDetail("form_bo_def","ID_", "", "CATEGORY_NAME_"));
		JmsTableTypeConf.AddTypeConf("FORM_TYPE",new JmsTableTypeFiledDetail("form_definition","ID_", "TYPE_ID_", "TYPE_NAME_"));
		return null;
	}
}
