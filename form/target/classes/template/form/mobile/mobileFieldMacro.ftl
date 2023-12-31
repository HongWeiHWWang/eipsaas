<#setting number_format="0">
<#function getNgModel field isSub>
    <#assign rtn><#if isSub>item.${field.name}<#else>data.${field.path}.${field.name}</#if></#assign>
    <#return rtn>
</#function>
<#function getPermission field isSub>
	<#assign rtn>permission.fields.${field.tableName}.${field.name}</#assign>
	<#if rtn?matches("^permission\\.fields\\.\\w+\\.\\w+$")>
		<#return rtn>
	<#else>
		<#stop "Incorrect field format: ${field.desc}">	
	</#if>
</#function>

<#function getInput field isSub ganged>
    <#assign rtn>
		<input ht-input="${getNgModel(field,isSub)}" placeholder="${field.desc}" class="text_ipt" type="text" ng-model="${getNgModel(field,isSub)}"  permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-funcexp,ht-validate,ht-datecalc,ht-number',field)}/>
    </#assign>
    <#return rtn>
</#function>

<#function getSelect field isMutl isSub ganged>
    <#assign rtn>
        <#if util.getJsonByPath(field.option,'choiceType')=="dynamic">
		<select ht-select-query="${util.getSelectQuery(field.option,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" <#if isMutl>multiple</#if> class="form-control" ${util.getAttrs('selectquery,ht-validate',field)})}>
        </select>
        <#else>
            <#assign list=util.getJsonByPath(field.option,'choice')?eval>
            <#if isMutl> <input type="text" placeholder="请选择" id="${field.tableName}_${field.name}"/> <div style="display:none;" > </#if>
			<select ht-select="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" <#if isMutl>multiple</#if> filedname="${field.tableName}_${field.name}" ng-model="${getNgModel(field,isSub)}"  class="form-control" ${util.getAttrs('ht-validate',field)} >
			<#if !isMutl> <option value="">请选择</option> </#if>
				<#list list as choice>
                    <#if choice.value!="">
						<option value="${choice.key}">${choice.value}</option>
                    </#if>
                </#list>
            </select>
            <#if isMutl> </div> </#if>
        </#if>
    </#assign>
    <#return rtn>
</#function>

<#function getCheckbox field isSub ganged>
    <#assign rtn>
        <#if util.getJsonByPath(field.option,'choiceType')=="dynamic">
	  	 <input fieldtype="checkbox" ht-select-query="${util.getSelectQuery(field.option,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" <#if isMutl>multiple</#if> class="form-control" ${util.getAttrs('selectquery,ht-validate',field)})} />
        <#else>
            <#assign list=util.getJsonByPath(field.option,'choice')?eval>
			<div ht-checkboxs="${getNgModel(field,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-validate',field)}>
			<#list list?sort_by("value") as choice>
                <div class="check_cell">
                    <input type="checkbox" class="text_check" value="${choice.key}" />
                    <label class="check_label" for="kkk01">${choice.value}</label>
                </div>
            </#list>
            </div>
        </#if>
    </#assign>
    <#return rtn>
</#function>

<#function getDialog field isSub>
    <#assign name  = util.getJsonByPath(field.customDialogjson,'name')>
    <#assign icon  = util.getJsonByPath(field.customDialogjson,'icon')>
    <#assign custdialogConf  = util.getStringConf(field.customDialogjson)>
    <#assign rtn>
		<input ht-input="${getNgModel(field,isSub)}" style="float: left;" desc="${field.desc}" class="form-control nowrap" type="text" ng-model="${getNgModel(field,isSub)}"  permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-funcexp,ht-validate,ht-datecalc,ht-number',field)} ${util.getFieldGanged(field.path+"."+field.name,ganged)}/>
		<span permission="${getPermission(field,isSub)}" style="float: right;"  class="btn  btn-primary fa ${icon}" ht-custdialog=${custdialogConf}>
            ${name}
        </span>
    </#assign>
    <#return rtn>
</#function>

<#function getGangedSelect field isSub ganged>
    <#assign rtn>
	<select desc="${field.desc}" ht-select-query="${util.getSelectQuery(field.option,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" class="form-control" ${util.getAttrs('selectquery,ht-validate',field)})} ${util.getFieldGanged(field.path+"."+field.name,ganged)}>
    </select>
    </#assign>
    <#return rtn>
</#function>

<#function getRelFlows field isSub ganged>
    <#assign rtn>
	<div desc="${field.desc}" ht-rel-flow="${getNgModel(field,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-validate',field)} ${util.getFieldGanged(field.path+"."+field.name,ganged)} ></div>
    </#assign>
    <#return rtn>
</#function>

<#function getRadio field isSub ganged>
    <#assign rtn>
        <#assign list=util.getJsonByPath(field.option,'choice')?eval>
			<div ht-radios="${getNgModel(field,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" ${util.getPermissionNgif(field)} ${util.getAttrs('ht-validate',field)} >
			<#list list?sort_by("value") as choice>
                <div class="radio_cell">
                    <input type="radio" class="text_radio" ng-model="${getNgModel(field,isSub)}" value="${choice.key}" />
                    <label class="radio_label">${choice.value}</label>
                </div>
            </#list>
            </div>
    </#assign>
    <#return rtn>
</#function>
<#-- 注意不能加空格  -->
<#macro input field isSub ganged>
    <#switch field.ctrlType>
        <#case 'onetext'><#--单行文本框-->${getInput(field,isSub)}
            <#break>
        <#case 'multitext'><#--多行文本框-->
            <textarea ht-input="${getNgModel(field,isSub)}" placeholder="${field.desc}" class="form-control text_area" type="text" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-funcexp,ht-validate,ht-datecalc',field)}> </textarea>
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
            <div class="time_chioce">
                <input placeholder="${field.desc}" ${util.getCtrlDate(field)} class="form-control" type="text" ng-model="${getNgModel(field,isSub)}"  ht-date  permission="${getPermission(field,isSub)}" ${util.getAttrs('ht-funcexp,ht-validate',field)} show-current-date="${field.option.showCurrentDate}" />
            </div>
            <#break>
        <#case 'selector'><#--选择器(包括组织，岗位，角色，用户选择器等控件组合)-->
            <div class="form-control" ng-model="${getNgModel(field,isSub)}" ht-selector="${getNgModel(field,isSub)}" selectorconfig="${util.getHtSelector(field.option,isSub)}" permission="${getPermission(field,isSub)}" placeholder="${field.desc}" ${util.getAttrs('ht-funcexp,ht-validate',field)} type="text" />
            <#break>
        <#case 'officeplugin'><#--office控件-->
            <p class="kongj_ti">请在电脑上操作！</p>
            <#break>
        <#case 'fileupload'><#--文件上传-->
            <div ht-upload="${getNgModel(field,isSub)}" isSingle="${util.getJsonByPath(field.option,"file.isSingle")}"  ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" class="form-control"   placeholder="${field.desc}" type="text" ${util.getAttrs('ht-validate',field)}></div>
            <#break>
        <#case 'dic'> <#--数据字典-->
            <div ht-dic='${getNgModel(field,isSub)}' dickey="${util.getJsonByPath(field.option,"dic")}" <#if field.bindDicName?exists> bind="${getNgModel(field.bindDicName,isSub)}" </#if> permission="${getPermission(field,isSub)}" placeholder="${field.desc}" type="text"  ng-model="${getNgModel(field,isSub)}" class="form-control" ${util.getAttrs('ht-validate',field)}></div>
            <#break>
        <#case 'dialog'><#--自定义对话框-->
            ${getDialog(field,isSub)}
            <#break>
        <#case 'customQuery'><#--关联数据下拉选项-->
            ${getGangedSelect(field,isSub,ganged)}
            <#break>
        <#case 'relFlow'><#--相关流程-->
            ${getRelFlows(field,isSub,ganged)}
            <#break>
        <#case 'websign'><#--web签章-->
	        【暂时不支持web签章】
            <#break>
        <#case 'identity'><#--流水号-->
	        【暂时不支持流水号】
            <#break>
    </#switch>
</#macro>