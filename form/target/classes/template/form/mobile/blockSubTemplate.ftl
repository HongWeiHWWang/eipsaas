<#macro subTable field>
    <#assign subTablePath=field.children[0].path>
    <#assign subCustdialogConf  = util.getStringConf(field.customDialogjson)>
<div class="childF_box" type="subGroup" tableName="${field.tableName}" path="${subTablePath}" <#if field.relation=='onetoone'>initdata="true"</#if> ng-if="!permission.table.${field.tableName}.hidden">
    <div ng-if="!data.${subTablePath}.length" class="Dtitle_05">${formDesc}</div>

    <div ng-repeat="item in data.${subTablePath} track by $index">
        <div class="clearfix" style="height:24px">
            <p class="childF_num">${formDesc}<#noparse>{{$index+1}}</#noparse></p>
			<#if field.relation!='onetoone'>
				<em ng-click="remove('${subTablePath}',$index)" ng-if="permission.table.${field.tableName}.del" class="childF_delete"></em>
            </#if>
            <span style="float: right; line-height: 22px; width: 8%;height: 24px; font-size: 14px;color: #d3d3d3;" ng-if="permission.table.${field.tableName}.add&& ${field.isSubCustDialog}" class="btn btn-primary btn-sm fa fa-search-plus" ht-sub-custdialog=${subCustdialogConf}>
             </span>
        </div>
        <div class="formT_box">
			<#list field.children as field>
                <div class="detail_list clearfix" ng-class="{'must':permission.fields.${field.tableName}.${field.name}=='b'}" ng-if="permission.fields.${field.tableName}.${field.name}!='n'">
                    <div class="detail_list_tit">${field.desc}</div>
                    <div class="detail_list_cont">
						<@input field=field isSub=true ganged=ganged/>
                    </div>
                </div>
            </#list>
        </div>
    </div>
	
	<#if field.relation!='onetoone'>
		<div ng-if="permission.table.${field.tableName}.add && !permission.table.${field.tableName}.required">
            <div class="add_childF" ng-click="add('${subTablePath}')"><span class="add_childF_icon" >添加${table.desc}子表</span></div>
        </div>
    </#if>
    <div ng-if="permission.table.${field.tableName}.required && !data.${subTablePath}.length">
        <input style="display:none" class="isRequired" ng-if="permission.table.${field.tableName}.required && !data.${subTablePath}.length"/>
        <div class="add_childF" ng-click="add('${subTablePath}')"><span class="add_childF_icon" >至少添加一行${table.desc}数据</span></div>
    </div>
</div>
</#macro>