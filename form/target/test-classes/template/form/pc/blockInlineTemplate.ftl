<#setting number_format="0">
<br>	
<table class="table-form-inline" tableName="${table.name}" type="subGroup" path="${table.path}" <#if table.relation=='onetoone'>initdata="true"</#if> ng-if="!permission.table.${table.name}.hidden">
	<tr>
		<#if  table.relation!='onetoone'>
			<td colspan="${fieldList?size+1}">
				<span class="form-title">${table.desc} </span>
				<a style="color:#fff;" href="javascript:void(0);" class="btn-add" ng-click="add('${table.path}')" ng-if="permission.table.${table.name}.add">添加</a>
			</td>
		<#else>
			<td colspan="${fieldList?size}">
				<span class="form-title">${table.desc} </span>
			</td>
		</#if>
	</tr>
	<tr >
		<#list fieldList as field>
			<th ng-if="permission.fields.${table.name}.${field.name}!='n'">${field.desc}</th>
		</#list>
		
		<#if  table.relation!='onetoone'>
			<th >删除</th>
		</#if>	
	</tr>
	<tr   type="subGroupTr" ng-repeat="item in data.${table.path} track by $index">
		<#list fieldList as field>
			<td ng-if="permission.fields.${table.name}.${field.name}!='n'">
				<@input field=field isSub=true/>
			</td>
		</#list>
		
		<#if  table.relation!='onetoone'>
			<td >
				<a class="subFieldList floatTools block-delete" ng-click="remove('${table.path}',$index)" ng-if="permission.table.${table.name}.del" ><span class="fa fa-delete actionBtn" title="移除"></span></a>
			</td>
		</#if>
	</tr>
	<tr class="nodata" ng-if="!data.${table.path}.length &&permission.table.${table.name}.add && !permission.table.${table.name}.required">
		<th colspan="${fieldList?size}"  class="nodata">
			<span style="color:#dd5a43"?>请添加 ${table.desc}数据</span>
		</th>
	</tr>
	<tr class="nodata" ng-if="permission.table.${table.name}.required && !data.${table.path}.length">	
		<th colspan="${fieldList?size}"  ng-model="data.${table.path}" ht-validate="{required:true}" class="nodata">
			<span style="color:#dd5a43"?>至少添加一行${table.desc}数据！</span>
		</th>
	</tr>
	
</table>
		

