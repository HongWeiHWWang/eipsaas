package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};

/**
 * ${table.comment!} 前端控制器
 *
 <#if cfg.companyName??>
 * @company ${cfg.companyName}
 </#if>
 * @author ${author}
 <#if cfg.authorEmail??>
 * @email ${cfg.authorEmail}
 </#if>
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>/v1/")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass}<${table.serviceName}, ${entity}> {
<#else>
public class ${table.controllerName} {
</#if>

}
</#if>
