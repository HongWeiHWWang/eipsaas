<#setting number_format="0">
<#assign indexShow = "" >
${util.getIncludeFiles(includeFiles)}
<#list tabs as tab>
	<#if tab.isShow>
		<#assign indexShow = indexShow+tab_index+",">
	</#if>
</#list>
<el-collapse   value="${indexShow}" ref="collapse">
	<#list tabs as tab>

		<#if tab.isCollaps>
			<el-collapse-item title="${tab.tabField.desc}" name="${tab_index}" style="background: chartreuse">${tab.tabHtml}</el-collapse-item>
		<#else>
			${tab.tabHtml}
		</#if>
	</#list>
</el-collapse>

