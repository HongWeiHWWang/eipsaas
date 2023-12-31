<#setting number_format="0">
<#function getNgModel field isSub>
 	<#assign rtn><#if isSub>item.${field.name}<#else>data.${field.path}.${field.name}</#if></#assign>
	<#return rtn>
</#function>
<#function getPermission field isSub>
	<#assign rtn>permission.fields.${field.tableName}.${field.name}</#assign>
	<#return rtn>
</#function>

<#function getOfficeStyle json> 
	<#assign x  = util.getJsonByPath(json,"office.x.value")>
	<#assign xu = util.getJsonByPath(json,"office.x.unit")>
	<#assign y  = util.getJsonByPath(json,"office.y.value")>
	<#assign yu = util.getJsonByPath(json,"office.y.unit")>
	<#assign rtn>
		width:${x+xu};height:${y+yu};
	</#assign>
	<#return rtn>
</#function>

<#function getInput field isSub ganged> 
	<#assign rtn>
		<input ht-input="${getNgModel(field,isSub)}" desc="${field.desc}" class="form-control" type="text" ng-model="${getNgModel(field,isSub)}"  permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-funcexp,ht-validate,ht-datecalc,ht-number',field)} ${util.getFieldGanged(field.path+"."+field.name,ganged)}/>
	</#assign>
	<#return rtn>
</#function>

<#function getSelect field isMutl isSub ganged> 
<#assign rtn>
		<#if util.getJsonByPath(field.option,'choiceType')=="dynamic">
		<select desc="${field.desc}" ht-select-query="${util.getSelectQuery(field.option,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" <#if isMutl>multiple</#if> class="form-control" ${util.getAttrs('selectquery,ht-validate',field)})} ${util.getFieldGanged(field.path+"."+field.name,ganged)}>
		</select>
		<#else>
			<#assign list=util.getJsonByPath(field.option,'choice')?eval>
			<select desc="${field.desc}" ht-select="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" <#if isMutl>multiple</#if> ng-model="${getNgModel(field,isSub)}"  class="form-control" ${util.getAttrs('ht-validate',field)}  ${util.getFieldGanged(field.path+"."+field.name,ganged)}>
				<option value="">请选择</option>
				<#list list as choice>
					<#if choice.value!="">
						<option value="${choice.key}">${choice.value}</option>
					</#if>
				</#list>
			</select>
		</#if>
</#assign>
<#return rtn>
</#function>
<#function getCheckbox field isSub ganged> 
	<#assign rtn>
			<#assign list=util.getJsonByPath(field.option,'choice')?eval>
			<div desc="${field.desc}" ht-checkboxs="${getNgModel(field,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-validate',field)} ${util.getFieldGanged(field.path+"."+field.name,ganged)}>
			<#list list as choice>
				<label class="checkbox-inline"> <input type="checkbox"   value="${choice.key}">${choice.value}</label>
			</#list>
			</div>
	</#assign>
	<#return rtn>
</#function>
<#function getRadio field isSub ganged> 
	<#assign rtn>
			<#assign list=util.getJsonByPath(field.option,'choice')?eval>
			<div desc="${field.desc}" ht-radios="${getNgModel(field,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-validate',field)} ${util.getFieldGanged(field.path+"."+field.name,ganged)} >
			<#list list as choice>
				<label class="radio-inline"> <input type="radio" htradios="input" value="${choice.key}" ng-model="${getNgModel(field,isSub)}"> ${choice.value}</label>
			</#list>
			</div>
	</#assign>
	<#return rtn>
</#function>
<#-- 注意不能加空格  -->
<#macro input field isSub ganged>
<#switch field.ctrlType>
<#case 'hidtext'><#--隐藏域-->
<input desc="${field.desc}" class="form-control" ng-show="false" type="text" ng-model="${getNgModel(field,isSub)}" />
<#break>
<#case 'onetext'><#--单行文本框-->${getInput(field,isSub,ganged)}
<#break>
<#case 'multitext'><#--多行文本框-->
<textarea ht-input="${getNgModel(field,isSub)}" desc="${field.desc}" class="form-control" type="text" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-funcexp,ht-validate,ht-datecalc,ht-editor',field)} ${util.getFieldGanged(field.path+"."+field.name,ganged)}> </textarea>
<#break>
<#case 'select'><#--下拉选项-->
${getSelect(field,false,isSub,ganged)}
<#break>
<#case 'multiselect'><#--下拉选项多选-->
${getSelect(field,true,isSub,ganged)}
<#break>
<#case 'checkbox'><#--复选框-->
${getCheckbox(field,isSub,ganged)}
<#break>
<#case 'radio'><#--单选框-->
${getRadio(field,isSub,ganged)}
<#break>
<#case 'date'><#--日期控件-->
<input desc="${field.desc}" class="form-control" type="text" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-funcexp,ht-validate,ht-date',field)} ${util.getFieldGanged(field.path+"."+field.name,ganged)} show-current-date="${field.option.showCurrentDate}" />
<#break>
<#case 'selector'><#--选择器(包括组织，岗位，角色，用户选择器等控件组合)-->
<div class="form-control" ng-model="${getNgModel(field,isSub)}" ht-selector="${getNgModel(field,isSub)}" selectorconfig="${util.getHtSelector(field.option,isSub)}" permission="${getPermission(field,isSub)}" desc="${field.desc}" ${util.getAttrs('ht-funcexp,ht-validate',field)} ${util.getFieldGanged(field.path+"."+field.name,ganged)} type="text" />
<#break>
<#case 'officeplugin'><#--office控件-->
<div ng-model="${getNgModel(field,isSub)}"  desc="${field.desc}"  permission="${getPermission(field,isSub)}"  ${util.getAttrs('ht-validate,ht-office-plugin',field)} />
<#break>
<#case 'fileupload'><#--文件上传-->
<div ht-upload="${getNgModel(field,isSub)}" isSingle="${util.getJsonByPath(field.option,"file.isSingle")}" isSizeLimit="${util.getJsonByPath(field.option,"file.isSizeLimit")}" size="${util.getJsonByPath(field.option,"file.size")}" isFormatLimit="${util.getJsonByPath(field.option,"file.isFormatLimit")}" formatLimit="${util.getJsonByPath(field.option,"file.formatLimit")}"  ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" class="form-control"   desc="${field.desc}" type="text" ${util.getAttrs('ht-validate',field)}></div>
<#break>
<#case 'dic'> <#--数据字典-->
<div ht-dic='${getNgModel(field,isSub)}' dickey="${util.getJsonByPath(field.option,"dic")}" <#if field.bindDicName?exists> bind="${getNgModel(field.bindDicName,isSub)}" </#if> permission="${getPermission(field,isSub)}" desc="${field.desc}" type="text"  ng-model="${getNgModel(field,isSub)}" class="form-control" ${util.getAttrs('ht-validate',field)}></div>
<#break>
<#case 'websign'><#--web签章-->
<input style="${getOfficeStyle(field.option)}"  ng-model="${getNgModel(field,isSub)}"  desc="${field.desc}"  permission="${getPermission(field,isSub)}" type="hidden" controltype="webSign" value="" ${util.getAttrs('ht-validate',field)} />
<#break>
<#case 'identity'><#--流水号-->
<input ht-identity='{alias:"${util.getJsonByPath(field.option,"identity.alias")}"}' ng-model="${getNgModel(field,isSub)}"  permission="${getPermission(field,isSub)}" desc="${field.desc}" class="form-control" type="text" ${util.getAttrs('ht-funcexp,ht-validate,ht-datecalc',field)} />
<#break>
</#switch>
</#macro>