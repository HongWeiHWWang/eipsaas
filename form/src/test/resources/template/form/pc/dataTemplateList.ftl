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
</#noparse>
<#--日期选择器 -->
<#macro genQueryDate field>
		<#switch field.qt>
			<#case "D">
				<input type="text" name="Q^${colPrefix}${field.na}^${field.qt}" readonly="readonly"  class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse>  />
			<#break>
			<#case "DL">
				<input type="text" name="Q^${colPrefix}${field.na}^${field.qt}" readonly="readonly"  class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse> />
			<#break>
			<#case "DG">
				<input type="text" name="Q^${colPrefix}${field.na}^${field.qt}"  readonly="readonly" class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse>  />
			<#break>
			<#case "DR">
				从:
				<input type="text" name="Q^${colPrefix}${field.na}^DL" readonly="readonly" class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q^begin${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse>  />
				</li><li>到:
				<input type="text" name="Q^${colPrefix}${field.na}^DG" readonly="readonly" class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q^end${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse> />
			<#break>
			<#default>
				<input type="text" name="Q^${colPrefix}${field.na}^${field.qt}" class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse> />
			<#break>
		</#switch>
</#macro>

<#--单选按钮或复选框 -->
<#function getCheckboxOrRadio field> 
	<#assign rtn>
			<#if field.controlContent?if_exists>
				<#if field.controlContent?if_exists>
					<div style="display:inline">
					<input type="text" name="Q^${colPrefix}${field.na}^${field.qt}"  ng-model="${colPrefix}${field.na}" class="inputText" style="display:none"/>
					<select class="inputText" ht-select="${colPrefix}${field.na}" ng-model="${colPrefix}${field.na}" <#if field.ct=="checkbox">multiple="multiple"</#if>>
		                <option value="">全部</option>
						<#list field.controlContent as opt>
		                    <option value="${opt.key}">${opt.value}</option>
						</#list>
		            </select>
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
			<span>${field.cm}:</span>		
		<#switch field.ct>
			<#case "onetext">
				<#if field.ty == 'number'>
				<input type="text" name="Q^${colPrefix}${field.na}^${field.qt}" class="inputText"  value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse>  validate="{number:true}" />
				<#else>
				<input type="text" name="Q^${colPrefix}${field.na}^${field.qt}" class="inputText"  value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse>  />
				</#if>
			<#break>
			<#-- 自定对话框 -->
			<#case "userSelector">
			<#case "postSelector">
			<#case "orgSelector">
			<#case "roleSelector">
	            <#assign dg=content>
	            <input type="text" id="${fieldName}" name="Q^${colPrefix}${field.na}^${field.qt}" class="inputText"  readonly="readonly" />
	            <a class="btn btn-primary btn-xs" onclick="showCustomDialog(this,'${field.ct}','${dg}')">选择</a>
	        <#break>
			<#case "date"><#--日期选择器 -->
				<@genQueryDate field=field/>
			<#break>
			<#case "select"><#--下拉选项-->
				<#assign options=content>
				<div style="display:inline">
					<#if util.getJsonByPath(field.option,'choiceType')=="dynamic">
						<select class="inputText" ht-select-query="${util.getSelectQuery(field.option,false)}" name="Q^${colPrefix}${field.na}^${field.qt}" ng-model="${colPrefix}${field.na}">
						</select>
					<#else>
						<select class="inputText" name="Q^${colPrefix}${field.na}^${field.qt}" ng-model="${colPrefix}${field.na}" ht-select="${colPrefix}${field.na}">
			                <option value="">全部</option>
			                <#list options as opt>
			                    <option value="${opt.key}">${opt.value}</option>
			                </#list>
			            </select>
					</#if>
	            </div>
			<#break>
			<#case "dic"><#--数据字典-->
				<div style="display:inline-block;">
					<div ht-dic='${colPrefix}${field.na}' dickey="${content}"  desc="${field.cm}" type="text"  class="form-control" ></div>
					<input type="text" id="${fieldName}" name="Q^${colPrefix}${field.na}^${field.qt}"  ng-model="${colPrefix}${field.na}" class="inputText" style="display:none;"/>
				</div>
			<#break>
			<#case "radio"><#--单选按钮 -->
			<#case "checkbox"><#--复选框 -->
				${getCheckboxOrRadio(field)}  
			<#break>
			<#default>
				<input type="text" name="Q^${colPrefix}${field.na}^${field.qt}" class="inputText" value="<#noparse>${param[</#noparse>'Q^${colPrefix}${field.na}^${field.qt}'<#noparse>]}"</#noparse> />
			<#break>
		</#switch>
	</#if>
</#macro>

<#noparse>
<#--管理列-->
<#macro genManage manage managePermission actionUrl data>
	<#--编辑-->
	<#if manage.name == 'edit'>
		<#if managePermission.edit>"<a class='btn btn-default fa fa-edit' action='${actionUrl.edit}' onClick='openDetail(this,"+row.${pkField}+",\"edit\");' herf='javascript:void(0)'>${manage.desc}</a>"<#else>"<span/>"</#if>
	<#--删除-->
	<#elseif manage.name == 'del' >
		<#if managePermission.del>"<a class='rowInLine btn btn-default fa fa-remove' action='${actionUrl.del}?${pkField}="+row.${pkField}+"' herf='javascript:void(0)'>${manage.desc}</a>"<#else>"<span/>"</#if>
	<#--明细-->
	<#elseif manage.name == 'detail' >
		<#if managePermission.detail>"<a class='btn btn-default fa fa-detail' action='${actionUrl.detail}' onClick='openDetail(this,"+row.${pkField}+",\"get\");' herf='javascript:void(0)'>${manage.desc}</a>"<#else>"<span/>"</#if>
	<#else>
		"<span/>"
	</#if>
</#macro>


<#--顶部按钮-->
<#macro genToolBar manage managePermission actionUrl>
	<#-- 新增 -->
	<#if manage.name == 'add'>
		<#if managePermission.add>
			<a class="btn btn-sm btn-primary fa-add" action="${actionUrl.add}" href="javascript:void(0)" onclick="openDetail(this,'','add')"><span>${manage.desc}</span></a>
		</#if>
	</#if>
	
	<#-- 新增 -->
	<#if manage.name == 'startFlow'>
		<#if managePermission.startFlow  >
			<a class="btn btn-sm btn-primary fa-send" action="${actionUrl.startFlow}" href="javascript:void(0)" onclick="$.openFullWindow('${actionUrl.startFlow}')"><span>${manage.desc}</span></a>
		</#if>
	</#if>
	
	<#-- 删除 -->
	<#if manage.name == 'del'>
		<#if managePermission.del>
			<a class="btn btn-sm btn-primary fa-remove" action="${actionUrl.del}" href="javascript:void(0)" ><span>${manage.desc}</span></a>
		</#if>
	</#if>
	
	<#-- 导出 -->
	<#if manage.name == 'export'>
		<#if managePermission.export>
			<a class="btn btn-sm btn-primary fa-sign-out" onclick="exports('${actionUrl.export}')" href="javascript:void(0)" ><span>${manage.desc}</span></a>
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
	<div class="easyui-layout"  fit="true">
		<div data-options="region:'center',border:false">
		
			<div id="gridSearch" class="toolbar-search ">
				<div class="toolbar-head">
					<!-- 顶部按钮 -->
					<div class="buttons">
						<#if hasCondition>
						<a class="btn btn-sm btn-primary fa-search" href="javascript:void(0)">
							<span>搜索</span>
						</a>
						</#if>
						<#noparse><#list filters as filter>
							<#if filter_index!=0>
								<button class="btn btn-sm btn-primary fa-search" ng-click="changeFilterKey('${filter.filterKey}')" filterkey="${filter.filterKey}">
									<span>${filter.name}</span>
								</button>
							</#if>
						</#list></#noparse>
						<#if hasCondition>
						<a href="#" class="btn btn-sm btn-primary fa-rotate-left">
							<span>重置</span>
						</a>
						</#if>
						<#noparse><#list manageFields as manage>
							<@genToolBar manage=manage managePermission=managePermission actionUrl=actionUrl />
						</#list></#noparse>
					</div>
					<#if conditionFields?if_exists>
					<div class="tools">
						<a href="javascript:;" class="collapse">
							<i class=" fa  fa-angle-double-up"></i>
						</a>
					</div>
					</#if>
				</div>
				<div class="toolbar-body">
					<form id="searchForm" class="search-form" style="display:none;">
						<#--查询条件-->
						<#if conditionFields?if_exists>
							<ul class="row">
							<#list conditionFields as field>
								<li>
								 	<@genCondition field=field/>
								</li>
							</#list>
							</ul>
						</#if>
						<#noparse><#list filters as filter>
							<!-- 过滤条件 -->
							<#if filter_index==0>
								<input type="hidden" name="filterKey" value="${filter.filterKey}" />
							</#if>
						</#list></#noparse>
					</form>
				</div>
			</div>
			
			<div id="grid" class="my-easyui-datagrid"></div>
		</div>
	</div>
	
	<script>
	<#--调用生成格式化函数体-->
	<@genFormaterFunction/>
		
		var url ="listJson/<#noparse>${templateId}</#noparse>";  
		var filterKey = "<#noparse><#if (filters?size>0) >${filters[0].filterKey}</#if></#noparse>";
		<#-- 根据显示字段生成列表 -->
		function loadGrid() {
		$('#grid').datagrid($.extend($defaultOptions,{
			url : url,
			idField : "${pkField}",
			<#if sortFields?if_exists>
			<#else>
				sortName : '${pkField}',
				sortOrder : 'desc',
			</#if>
			fit:true,
			<#noparse>
			pagination: ${bpmDataTemplate.needPage},
			pageSize: ${pageSize},
			</#noparse>
			queryParams: {filterKey: filterKey},
			columns : [ [
			{field : '${pkField}',sortName : "${pkField}",checkbox : true},   
			<#list displayFields as field>
				<#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
				{field : '${field.name}',sortName : '${colPrefix}${field.name}',title : '${field.desc}',width : 130,align : 'center',sortable : 'true'
					<#if field.formater>
			            ,formatter:${field.name}_Formater
			        <#elseif field.alarmSetting>
			            ,formatter:${field.name}_AlarmFormater
			        </#if>}, 
				<#noparse></#if></#noparse>
			</#list>  
			{
				field : 'colManage',
				title : '操作',
				align : 'center',
				formatter : function(value, row, index) {
					var colHtml = <#noparse><#if (manageFields?size <= 0) >"";</#if><#list manageFields as manage>
						<@genManage manage=manage managePermission=managePermission actionUrl=actionUrl data=data/>
						<#if manage_has_next>+</#if>
						</#list></#noparse>;
						
						<#noparse>
						if(!row.isStartFlow&&'${bpmDataTemplate.defId}'){
						 colHtml = colHtml + "<a class='rowInLine btn btn-default fa fa-send' onclick=\"startFlow(\'${bpmDataTemplate.defId}\',\'${bpmDataTemplate.boDefAlias}\',\'"+row.${pkField}+"\')\">启动流程</a>";
						}</#noparse>;
					return colHtml;
				}
			} ] ]
		}));
	}
	
	$(function(){
		loadGrid();
	});
	</script>
	<#-- 主体内容 end  -->	
<#-- 
<#noparse>
<#else>
   <div style="padding:6px 0px 12px 0px;">当前用户没有满足的过滤条件,请设置过滤条件。<div>
</#if>
</#noparse> 
-->