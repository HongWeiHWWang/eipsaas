<br>
	<div class="div-block" type="subGroup" tableName="${table.name}" path="${table.path}" <#if table.relation=='onetoone'>initdata="true"</#if> ng-if="!permission.table.${table.name}.hidden">
		<div class="block-title">
			<span class="form-title">${table.desc} </span>
			<#if table.relation!='onetoone'>
				<a style="color:#fff;" href="javascript:void(0);" class="btn-add" ng-click="add('${table.path}')" ng-if="permission.table.${table.name}.add">
				<span class="icon-plus"></span>
				添加
				</a>
			</#if>
		</div>
		<div class="div-form" ng-repeat="item in data.${table.path} track by $index">
			<#if table.relation!='onetoone'>
				<a ng-click="remove('${table.path}',$index)" ng-if="permission.table.${table.name}.del" >
					<span class="fa fa-delete actionBtn block-delete" title="移除"></span>
				</a>
			</#if>
			<table class="table-form">
				<#list fieldList as field>
					<#if field.isSeparator && field.separatorTitle!="">
						<tr>
							<td colspan="2" class="grid-groupTitle">${field.separatorTitle}</td>
						</tr>
					</#if> 
					
					<tr ng-if="permission.fields.${table.name}.${field.name}!='n'">
						<th width="30%">${field.desc}</th>
						<td width="70%"><@input field=field isSub=isSub/></td>
					</tr>
					
					
				</#list>
			</table>
		</div>
		<div ng-if="!data.${table.path}.length &&permission.table.${table.name}.add && !permission.table.${table.name}.required" class="nodata">
			请添加 ${table.desc}数据
		</div>
		<div ng-if="permission.table.${table.name}.required && !data.${table.path}.length" class="nodata">
			至少添加一行${table.desc}数据！
			<input type="hidden" ng-model="data.${table.path}" ht-validate="{required:true}" />
		</div>
	</div>
<br>