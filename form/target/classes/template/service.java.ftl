package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
 * ${table.comment!} 服务类
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
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

}
</#if>
