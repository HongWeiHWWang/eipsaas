<#-- 
Name: 数据列表模板
Desc:数据列表模板

本模板需要通过2次解析才能得到最终的Html
第一次解析：
*************************************************************
*************************************************************
*数据模型:****************************************************
*************************************************************
*************************************************************

tbarTitle：Tool Bar 的标题

********************************************
conditionFields:条件字段
--joinType：	条件联合类型
--name：	列名
--name：完全指定名
--operate：条件类型: =|>=|<=|….
--comment：注释
--type：	类型
--value：值
--valueFrom：值来源

************************************************************
displayFields：显示字段
--name：列名
--name：完全指定名
--label：别名
--index：索引
--comment：注释
--type：类型

******************************************************
tableIdCode:Table ID Code

**************************************************
displayId: 自定义显示的ID

**************************************************
pageHtml：分页的Html 详见pageAjax.xml

*************************************************
pageURL：当前页面的URL

searchFormURL：搜索表单的Action


sortField：当前排序字段

orderSeq：当前的排序类型

***********************************************
pkcols:主键列

deleteBaseURL：删除一行数据的BaseURL
editBaseURL：编辑一行数据的BaseURL
 -->


<#setting number_format="#">
<#assign displayFields=bpmDataTemplate.displayField?eval>
<#assign conditionFields=bpmDataTemplate.conditionField?eval>
<#assign filterFields=bpmDataTemplate.filterField?eval>
<#assign manageFields=bpmDataTemplate.manageField?eval>
<#assign sortFields=bpmDataTemplate.sortField?eval>

<#noparse>
<#setting number_format="#">
<#assign displayFields=bpmDataTemplate.displayField?eval>
<#assign conditionFields=bpmDataTemplate.conditionField?eval>
<#assign filterFields=bpmDataTemplate.filterField?eval>
<#assign manageFields=bpmDataTemplate.manageField?eval>
<#assign sortFields=bpmDataTemplate.sortField?eval>
<#assign boAlias=bpmDataTemplate.boDefAlias>
<#assign templateId=bpmDataTemplate.id>
<#assign pageSize=bpmDataTemplate.pageSize>
<#assign urlPre="$">
</#noparse>

<#--日期选择器 -->
<#macro genQueryDate field>
		<#switch field.qt>
			<#case "between">
				<input date-range-picker clearable="true" readonly style="background-color: #fff;" class="form-control input-sm date-picker" type="text"
	                    		   ht-query="${colPrefix}${field.na}" ng-model="${colPrefix}${field.na}" operation="${field.qt}"/>
			<#break>
			<#default>
				<input type="text" ht-query="${colPrefix}${field.na}" operation="${field.qt}" class="wdateTime inputText" />
			<#break>
		</#switch>
</#macro>

<#--单选按钮或复选框 -->
<#function getCheckboxOrRadio field> 
	<#assign rtn>
			<#if field.controlContent?if_exists>
				<#if field.controlContent?if_exists>
					<div style="display:inline">
						<#if field.ct=='radio'>
				            <#list field.controlContent as opt>
			                	<input type="${field.ct}" ht-query="${colPrefix}${field.na}" operation="${field.qt}" ng-model="${colPrefix}${field.na}" name="${colPrefix}${field.na}" value='${opt.optionValue}'/><label>${opt.optionKey}</label>
							</#list>
				        <#else>
				            <#list field.controlContent as opt>
			                	<input type="${field.ct}" ht-checkbox ht-query="${colPrefix}${field.na}" operation="${field.qt}" ng-model="${colPrefix}${field.na}" value='${opt.optionValue}'/><label>${opt.optionKey}</label>
							</#list>
				        </#if>
		                
		            </div>
				</#if>
			</#if>
	</#assign>
	<#return rtn>
</#function>

<#--获取条件-->
<#function getCondition condition field>
    <#assign rtn="">
    <#list condition as con>
        <#--处理运算符-->
        <#assign operate=con.op >
        <#--处理值-->
        <#assign val=con.val >
        <#if (field.dataType=="varchar") >
            <#assign val="'"+val+"'" >
        </#if>
        
        <#if con_index==0>
            <#assign rtn="value" + operate + val > 
        <#else>
            <#assign rtn=rtn + " && value" + operate + val >
        </#if>
    </#list>
    <#return rtn>
</#function>

<#--生成格式化函数-->
<#macro genFormaterFunction>
	<#if displayFields?if_exists>
	    <#list displayFields as field>
	        <#assign alarmSetting=field.alarmSetting >
	        <#assign formater=field.formater>
	        <#if formater?if_exists>
	            function ${field.name}_Formater(value, row, index){
	                 ${formater};
	            }
	        <#elseif alarmSetting?if_exists>
	            <#assign alarm=alarmSetting >
	            function ${field.name}_AlarmFormater(value, row, index){
	                <#list alarm as item>
	                    if(${getCondition(item.condition,field)}){
	                        return "<span style='color:${item.color};font-weight:bold;'>" + value +"</span>";
	                    }
	                </#list>
	                return value;
	            }
	        
	        </#if>      
	    </#list>
	 </#if>  
</#macro>

<#--生成查询条件宏 -->
<#macro genCondition field>
	<#assign content=field.controlContent>
	<#if field.vf=="static" >
			<label class="col-md-1 control-label">${field.cm}</label>
			<div class="col-md-2">
				<#switch field.ct>
					<#case "onetext">
						<#if field.ty == 'number'>
						<input type="text" operation="${field.qt}" ht-query="${colPrefix}${field.na}" class="form-control input-sm"  value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse>  validate="{number:true}" />
						<#else>
						<input type="text" operation="${field.qt}" ht-query="${colPrefix}${field.na}" class="form-control input-sm"  value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse>  />
						</#if>
					<#break>
					<#-- 自定义对话框 -->
					<#case "customDialog">
			            <#assign dg=content>
			            <input operation="${field.qt}" ht-query="${colPrefix}${field.na}" style="float: left; width: 60%;" desc="对话框" ng-model="${colPrefix}${field.na}"  class="form-control input-sm" type="text"/>
			           <span style="float: right; line-height: 18px;" class="btn btn-primary btn-sm fa fa-search-plus" ht-custdialog=${util.getCustDialogAttr(colPrefix,field)}>
						  选择 
					   </span>
			        <#break>
					<#case "date"><#--日期选择器 -->
						<@genQueryDate field=field/>
					<#break>
					<#case "select"><#--下拉选项-->
						<#assign options=content>
						<div style="display:inline">
							<#if util.getJsonByPath(field.option,'choiceType')=="dynamic">
								<select operation="${field.qt}" class="form-control input-sm" ht-select-query="${util.getSelectQuery(field.option,false)}" ht-query="${colPrefix}${field.na}" name="Q^${colPrefix}${field.na}^${field.qt}" ng-model="${colPrefix}${field.na}">
								</select>
							<#else>
								<select class="form-control input-sm" ht-query="${colPrefix}${field.na}"  operation="${field.qt}" ng-model="${colPrefix}${field.na}" ht-selects="${colPrefix}${field.na}">
					                <option value="">全部</option>
					                <#list field.controlContent as opt>
					                    <option value="${opt.optionValue}">${opt.optionKey}</option>
									</#list>
					            </select>
							</#if>
			            </div>
					<#break>
					<#case "dic"><#--数据字典-->
						<#assign dickey=util.getJsonByPath(field.controlContent,'alias')>
						<#assign resultField=util.getJsonByPath(field.controlContent,'resultField')>
						<div style="display:inline-block;" class="col-md-12">
							<div ht-dic='${colPrefix}${field.na}' dickey="${dickey}" resultfield="${resultField}" ht-query="${colPrefix}${field.na}" bind="${colPrefix}${field.na}" desc="数据字典" type="text"  ng-model="${colPrefix}${field.na}" class="form-control"  ></div>
						</div>
					<#break>
					<#case "radio"><#--单选按钮 -->
					<#case "checkbox"><#--复选框 -->
						${getCheckboxOrRadio(field)}  
					<#break>
					<#default>
						<input type="text" operation="${field.qt}" ht-query="${colPrefix}${field.na}" class="form-control input-sm" value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse> />
					<#break>
				</#switch>
			</div>
	</#if>
</#macro>

<#noparse>
<#--管理列-->
<#macro genManage manage managePermission actionUrl data>
	<#--编辑-->
	<#if manage.name == 'edit'>
		<#if managePermission.edit><button class="btn btn-info btn-sm" title="编辑" ng-click='operating("${templateId}",row.${pkField},"edit");' ><i class="fa fa-edit"></i> ${manage.desc}</button><#else><span/></#if>
	<#--删除-->
	<#elseif manage.name == 'del' >
		<#if managePermission.del>
		<button class="btn btn-info btn-sm" title="删除" ng-click='operating("${templateId}",row.${pkField},"edit");' ht-action='{"url":"${urlPre}{form}/form/dataTemplate/v1/boDel/${bpmDataTemplate.boDefAlias}?ids={{row.${pkField}}}","actType":"del"}' ><i class="fa fa-trash"></i> ${manage.desc}</button>
		<#else><span/></#if>
		
	<#--子表-->
	<#elseif manage.name == 'sub'>
		<#if managePermission.sub && hasSub>
			<button class="btn btn-info btn-sm" title="查看子表" ng-click='showSubList("${bpmDataTemplate.alias}",row.${pkField});' ><i class="fa fa-file-text"></i> ${manage.desc}</button>
		<#else><span/></#if>	
	
	<#--明细-->
	<#elseif manage.name == 'detail' >
		<#if managePermission.detail><button class="btn btn-info btn-sm" title="明细" ng-click='operating("${templateId}",row.${pkField},"get");' ><i class="fa fa-file-text"></i> ${manage.desc}</button><#else><span/></#if>
	<#else>
		
	</#if>
</#macro>


<#--顶部按钮-->
<#macro genToolBar manage managePermission actionUrl>
	<#-- 新增 -->
	<#if manage.name == 'add'>
		<#if managePermission.add>
			<button type="button" class="btn btn-success btn-sm" ng-click="operating('${templateId}','', 'add')"> <i class="fa fa-plus"></i>${manage.desc}</button>
		</#if>
	</#if>
	
	<#-- 启动流程 -->
	<#if manage.name == 'startFlow'>
		<#if managePermission.startFlow  >
			<button type="button" class="btn btn-primary btn-sm" ng-click="toStartFlow('${bpmDataTemplate.defId}')"> <i class="fa fa-send"></i>${manage.desc}</button>
		</#if>
	</#if>
	
	<#-- 删除 -->
	<#if manage.name == 'del'>
		<#if managePermission.del>
			<button class="btn btn-danger btn-sm remove"  ht-remove-array removekey="${pkField}" url="${urlPre}{form}/form/dataTemplate/v1/boDel/${bpmDataTemplate.boDefAlias}" > <i class="fa fa-trash"></i> ${manage.desc}</button>
		</#if>
	</#if>
	
	<#-- 导出 -->
	<#if manage.name == 'export'>
		<#if managePermission.export>
			<button type="button" class="btn btn-warning btn-sm" ng-click="exports('${actionUrl.export}')"> <i class="fa fa-sign-out"></i>${manage.desc}</button>
		</#if>
	</#if>
</#macro>


</#noparse>
<#--过滤条件
<#noparse><#if filterFields?if_exists>
<div class="panel" ajax="ajax"  displayId="${bpmDataTemplate.id}" filterKey="${filterKey}" >
<#if filterFields?size gt 1>
<div class='panel-nav'>
	<div class="l-tab-links">
		<ul style="left: 0px; ">
			<#list filterFields as field>
			<li tabid="${field.key}" <#if field.key ==filterKey> class="l-selected"</#if>>
				<a href="${field.url}" title="${field.name}">${field.desc}</a>
			</li>
			</#list>
		</ul>
	</div>
</div>
</#if>
</#noparse> -->
	<#-- 主体内容 start  -->
	<div class="white-bg border-left animated fadeInRight">
		<div class="ibox" ht-data-table="dataTable" url="<#noparse>${urlPre}</#noparse>{form}/form/dataTemplate/v1/listJson/<#noparse>${bpmDataTemplate.id}</#noparse>">
			<div class="ibox-title no-borders">
				<div class="col-md-12 ">
					<div class="col-md-10 btn-group tools-panel">
						<#noparse><#list manageFields as manage>
							<@genToolBar manage=manage managePermission=managePermission actionUrl=actionUrl />
						</#list></#noparse>
					</div>
				</div>
			</div>
			<div class="ibox-content">
				<form class="form-horizontal">
	                <div class="form-group">
	                	<#--查询条件-->
						<#if conditionFields?if_exists>
							<#list conditionFields as field>
								 <@genCondition field=field/>
							</#list>
						</#if>
	                    <div class="col-md-2">
	                        <button class="btn btn-primary btn-sm" type="button" 
	                        		ht-search><i class="fa fa-search"></i> 查询</button>
	                        <button class="btn btn-default btn-sm" type="button" 
	                        		ht-reset><i class="fa fa-reply-all"></i> 重置</button>
	                    </div>
	                </div>
	            </form>
			</div>
			<div class="ibox-footer">
				<table class="table table-bordered table-striped table-hover" ht-table>
					<tbody>
						<tr ng-repeat="row in dataTable.rows track by $index">
							<td width="30" class="center" selectable="true">
								<div class="checkbox"><input type="checkbox" ht-select="row"><label></label></div>
							</td>
							<td ng-bind="$index+1" title="序号"></td>
							<#list displayFields as field>
								<#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
								<td ht-field="row.${field.name}" title="${field.desc}" sortable="true"></td>
								<#noparse></#if></#noparse>
							</#list>  
							<td title="操作">
								<#noparse>
									<#if (manageFields?size > 0) >
										<#list manageFields as manage>
											<@genManage manage=manage managePermission=managePermission actionUrl=actionUrl data=data/>
										</#list>
									</#if>
								</#noparse>
								<#noparse>
									<button ng-if='!row.isStartFlow&&"${bpmDataTemplate.defId}"' class="btn btn-info btn-sm" title="启动流程" ng-click='startFlow("${bpmDataTemplate.defId}","${bpmDataTemplate.boDefAlias}",row.${pkField});' ><i class="fa fa-send"></i> 启动流程</button>
								</#noparse>
							</td>
						</tr>
						<tr class="no-row">
							<td colspan="7"><i class="fa fa-info-circle"></i> 没有查询到符合条件的记录</td>
						</tr>
					</tbody>
				</table>
				<div ht-pagination ht-page-size=${bpmDataTemplate.pageSize}></div>
			</div>
		</div>
	</div>
	
	
	<#-- 主体内容 end  -->	
<#-- 
<#noparse>
<#else>
   <div style="padding:6px 0px 12px 0px;">当前用户没有满足的过滤条件,请设置过滤条件。<div>
</#if>
</#noparse> 
-->