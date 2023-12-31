package com.hotent.base.conf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.hotent.base.filter.JwtAuthorizationTokenFilter;
import com.hotent.base.jwt.JwtAuthenticationEntryPoint;
import com.hotent.base.jwt.JwtTokenHandler;
import com.hotent.base.security.CustomPwdEncoder;
import com.hotent.base.security.HtDecisionManager;
import com.hotent.base.security.HtFilterSecurityInterceptor;
import com.hotent.base.util.StringUtil;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Resource
	UserDetailsService userDetailsService;
	@Resource
	JwtTokenHandler jwtTokenHandler;
	@Value("${jwt.header:'Authorization'}")
	String tokenHeader;
	@Value("${jwt.route.path:'/auth'}")
	String authenticationPath;
	@Value("${feign.encry.key:feignCallEncry}")
    private String encryKey;
	@Value("${hotent.security.ignore.httpUrls:''}")
	String permitAll;
	@Value("${hotent.security.deny.httpUrls:''}")
	String denyAll;
	@Resource
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Resource
	HtFilterSecurityInterceptor htFilterSecurityInterceptor;

	@Resource
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoderBean());
	}

	@Bean
	public PasswordEncoder passwordEncoderBean() {
		return new CustomPwdEncoder();
	}

	// 注册后台权限控制器
	@Bean
	public AccessDecisionManager accessDecisionManager() {
		return new HtDecisionManager();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		String[] permitAlls = new String[]{};
		String[] denyAlls = new String[]{};
		if(StringUtil.isNotEmpty(permitAll)){
			permitAlls = permitAll.split(",");
		}
		if(StringUtil.isNotEmpty(denyAll)){
			denyAlls = denyAll.split(",");
		}
		httpSecurity
		// we don't need CSRF because our token is invulnerable
		.csrf().disable()
		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
		// don't create session
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests()
		.antMatchers(permitAlls).permitAll()
		.antMatchers(denyAlls).denyAll()
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		.antMatchers("/auth/**").permitAll()
		.antMatchers("/ueditor/**").permitAll()
		.anyRequest().authenticated()
		.accessDecisionManager(accessDecisionManager());

		// Custom JWT based security filter
		JwtAuthorizationTokenFilter authenticationTokenFilter = new JwtAuthorizationTokenFilter(userDetailsService(), jwtTokenHandler, tokenHeader);
		authenticationTokenFilter.setEncryKey(encryKey);
		httpSecurity
		.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity
		.addFilterBefore(htFilterSecurityInterceptor, FilterSecurityInterceptor.class);
		httpSecurity
		.addFilterBefore(corsFilter(), ChannelProcessingFilter.class);

		// disable page caching
		httpSecurity
		.headers()
		.frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
		.cacheControl();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// AuthenticationTokenFilter will ignore the below paths
		web
		.ignoring()
		.antMatchers(
				HttpMethod.POST,
				authenticationPath,
				"/sys/sysLogs/v1/loginLogs",
				"/sys/sysLogs/v1/saveLogs",
				"/api/user/v1/user/loadUserByUsername",
				"/actuator/cert",
				"/form/formServiceController/v1/getFormAndBoExportXml"
				)
		.antMatchers(
				HttpMethod.GET,
				"/sso/**",
				"/sys/sysLogsSettings/v1/getSysLogsSettingStatusMap",
				"/sys/sysRoleAuth/v1/getMethodRoleAuth",
				"/system/file/v1/downloadFile",
				/*"/flow/bpmTaskReminder/v1/executeTaskReminderJob",*/
				"/flow/def/v1/bpmnXml",
				"/file/onlinePreviewController/v1/getFileByPathAndId**",
				"/file/onlinePreviewController/v1/getFileById**",
				"/portal/main/v1/appProperties",
				"/sys/sysProperties/v1/getByAlias",
				"/uc/tenantManage/v1/getTenantByCode",
                "/sys/sysProperties/v1/getDecryptByAlias"
				)
		// allow anonymous resource requests
		.and()
		.ignoring()
		.antMatchers(
				HttpMethod.GET,
				"/",
				"/error",
				"/*.html",
				"/favicon.ico",
				"/**/*.html",
				"/**/*.css",
				"/**/*.js",
				"/**/image"
				)
		.and()
		.ignoring()
		.antMatchers("/v2/api-docs",
				"/swagger-resources/configuration/ui",
				"/swagger-resources",
				"/swagger-resources/configuration/security",
				"/swagger-ui.html",
				"/proxy.stream",
				"/hystrix.stream",
				"/druid/**",
				"/hystrix/**",
				"/actuator/**",
				"/service/**");
	}

	/**
	 * 允许跨域访问的源
	 * @return
	 */
	@Bean  
	public CorsFilter corsFilter() {  
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();  
		CorsConfiguration corsConfiguration = new CorsConfiguration();  
		corsConfiguration.addAllowedOrigin("*");  
		corsConfiguration.addAllowedHeader("*");  
		corsConfiguration.addAllowedMethod("*");  
		source.registerCorsConfiguration("/**", corsConfiguration);  
		return new CorsFilter(source);  
	}

	@Bean
	public HtFilterSecurityInterceptor htFilterSecurityInterceptor(AccessDecisionManager accessDecisionManager) throws Exception{
		HtFilterSecurityInterceptor htFilterSecurityInterceptor = new HtFilterSecurityInterceptor();
		htFilterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager);
		return htFilterSecurityInterceptor;
	}
}
