<#setting number_format="0">

<#function getColspan index,hasNext>
	<#assign rtn="">
		 <#if !hasNext && index !=4>
			<#assign rtn="colspan='"+((4-index)*2+1)+"'"> 
		</#if>
<#return rtn>
</#function>

<table class="table-form">
	<tr align="middle" class="form-title">
		<td colspan="8" class="form-title">${formDesc} </td>
	</tr>
	<#assign index=1>
	<#list fieldList as listObj>
		<#if listObj.isShow>
			<tr ht-toggle="${listObj.isOpen}"  ${util.getSeparator(listObj.key)}>
				<td colspan="8"  class="grid-groupTitle">${listObj.name}</td>
			</tr>
		</#if>
		<#list listObj.fields as field>
			<#if index==1>
			<tr ${util.getSeparator(listObj.key)}>
			</#if>
				<th width="8%" ng-if="permission.fields.${table.name}.${field.name}!='n'">${field.desc}</th>
				<td width="17%" ng-if="permission.fields.${table.name}.${field.name}!='n'" <#if field.ctrlType==7>class="ub ub-ac"</#if> ${getColspan(index,field_has_next)}><@input field=field isSub=false ganged=ganged/></td>
			<#if !field_has_next || index==4>
			</tr>
			<#assign index=0>
			</#if> 
			<#assign index=index+1>
			
			
		</#list>
	</#list>
</table>