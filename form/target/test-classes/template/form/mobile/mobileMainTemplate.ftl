<#setting number_format="0">
<div class="mainForm">
	<div class="formTitle">${formDesc}</div>
	<#list fieldList as listObj>
		<#if listObj.isShow>
			<div class="group-title">${listObj.name}</div>
		</#if>
		<#list listObj.fields as field>
		
	        <div class="formItem" ng-if="permission.fields.${table.name}.${field.name}!='n'">
	            <div class="itemTitle">${field.desc}</div>
	            <div class="iteminput">
	              <@input field=field isSub=false/>
	            </div>
	        </div>
		</#list>
		
	</#list>
</div>

