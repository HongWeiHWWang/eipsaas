<#setting number_format="0">
<table class="table-form">
	<tr align="middle" class="form-title">
		<td colspan="4" class="form-title">${formDesc} </td>
	</tr>
	<#assign index=1>
	<#list fieldList as listObj>
			<#if listObj.isShow>
				<tr ht-toggle="${listObj.isOpen}"  ${util.getSeparator(listObj.key)}>
					<td colspan="4"  class="grid-groupTitle">${listObj.name}</td>
				</tr>
			</#if>
		<#list listObj.fields as field>
			<#if field.ctrlType=="multitext">
				<#if index==2>
					<th width="15%"></th>
					<td width="35%"></td>
				</tr>
				<#assign index=1>
				</#if>
				<tr ${util.getSeparator(listObj.key)}>
					<th width="15%" ng-if="permission.fields.${table.name}.${field.name}!='n'">${field.desc}</th>
					<td colspan="3"  ng-if="permission.fields.${table.name}.${field.name}!='n'" ${getColspan(index,field_has_next)}><@input field=field isSub=false ganged=ganged/></td>
				</tr>
			</#if> 
			
			<#if field.ctrlType!="multitext">
				<#if index==1>
				<tr ${util.getSeparator(listObj.key)}>
				</#if>
					<th width="15%" ng-if="permission.fields.${table.name}.${field.name}!='n'">${field.desc}</th>
					<td width="35%" ng-if="permission.fields.${table.name}.${field.name}!='n'" ${getColspan(index,field_has_next)}><@input field=field isSub=false ganged=ganged/></td>
				<#if !field_has_next || index==2>
				</tr>
				<#assign index=0>
				</#if> 
				<#assign index=index+1>
			</#if>
		
			
			
		</#list>
	</#list>
</table>

<#function getColspan index,hasNext>
	<#assign rtn="">
		 <#if !hasNext && index !=2>
			<#assign rtn="colspan='"+((2-index)*2+1)+"'"> 
		</#if>
<#return rtn>
</#function>