<#setting number_format="0">
<table class="table-form">
	<tr align="middle" class="form-title">
		<td colspan="2" class="form-title">${formDesc} </td>
	</tr>
	<#list fieldList as listObj>
		<#if listObj.isShow>
			<tr ht-toggle="${listObj.isOpen}"  ${util.getSeparator(listObj.key)}>
				<td colspan="2"  class="grid-groupTitle">${listObj.name}</td>
			</tr>
		</#if>
		<#list listObj.fields as field>
			<tr ${util.getSeparator(listObj.key)} ng-if="permission.fields.${table.name}.${field.name}!='n'"> 
				<th width="30%">${field.desc}</th>
				<td width="70%"><@input field=field isSub=false ganged=ganged/></td>
			</tr>
			<#--分组-->
			
		</#list>
	</#list>
</table>
