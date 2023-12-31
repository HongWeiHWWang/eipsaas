package com.hotent.base.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.hotent.base.annotation.ApiGroup;
import com.hotent.base.constants.ApiGroupConsts;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 * 
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月19日
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Value("${spring.profiles.title}")
	private String title;
	@Value("${spring.profiles.description}")
	private String description;
	@Value("${spring.profiles.version}")
	private String version;

	@Bean
	public Docket formApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("表单接口")
				.apiInfo(productApiInfo())
				.useDefaultResponseMessages(false)
				.forCodeGeneration(false)
				.select()
				.apis(getPredicateWithGroup(ApiGroupConsts.GROUP_FORM))
				.paths(PathSelectors.any())
				.build().securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
	}
	
	@Bean
	public Docket bpmApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("流程接口")
				.apiInfo(productApiInfo())
				.useDefaultResponseMessages(false)
				.forCodeGeneration(false)
				.select()
				.apis(getPredicateWithGroup(ApiGroupConsts.GROUP_BPM))
				.paths(PathSelectors.any())
				.build().securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
	}
	
	@Bean
	public Docket ucApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("用户中心接口")
				.apiInfo(productApiInfo())
				.useDefaultResponseMessages(false)
				.forCodeGeneration(false)
				.select()
				.apis(getPredicateWithGroup(ApiGroupConsts.GROUP_UC))
				.paths(PathSelectors.any())
				.build().securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
	}
	
	@Bean
	public Docket portalApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("门户接口")
				.apiInfo(productApiInfo())
				.useDefaultResponseMessages(false)
				.forCodeGeneration(false)
				.select()
				.apis(getPredicateWithGroup(ApiGroupConsts.GROUP_PORTAL))
				.paths(PathSelectors.any())
				.build().securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
	}

	@Bean
	public Docket oaApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("OA接口")
				.apiInfo(productApiInfo())
				.useDefaultResponseMessages(false)
				.forCodeGeneration(false)
				.select()
				.apis(getPredicateWithGroup(ApiGroupConsts.GROUP_OA))
				.paths(PathSelectors.any())
				.build().securityContexts(Lists.newArrayList(securityContext()))
				.securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
	}
	private ApiKey apiKey() {
		return new ApiKey("BearerToken", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex("/.*"))
				.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
	}

	/**
	 * 通过接口分组过滤
	 * @param group
	 * @return
	 */
	private Predicate<RequestHandler> getPredicateWithGroup(String group) {
		return new Predicate<RequestHandler>() {
			@Override
			public boolean apply(RequestHandler input) {
				// 找到controller类上的ApiGroup注解
				Optional<ApiGroup> ApiGroup = input.findControllerAnnotation(ApiGroup.class);
				if(ApiGroup.isPresent() && Arrays.asList(ApiGroup.get().group()).contains(group)) {
					return true;
				}
				return false;
			}
		};
	}

	@SuppressWarnings("rawtypes")
	private ApiInfo productApiInfo() {
		Contact contact = new Contact("", "www.wijo.com", "");
		ApiInfo apiInfo = new ApiInfo(title,
				description,
				version,
				"http://127.0.0.1:8080",
				contact,
				"license",
				"license url",
				new ArrayList<VendorExtension>());
		return apiInfo;
	}
}
