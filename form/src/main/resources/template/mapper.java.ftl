package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};

/**
 * ${table.comment!} Mapper 接口
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
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
