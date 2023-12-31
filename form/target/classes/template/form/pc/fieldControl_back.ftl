<#setting number_format="0">
<#function getNgModel field isSub>
 	<#assign rtn><#if isSub>item.${field.name}<#else>data.${field.path}.${field.name}</#if></#assign>
	<#return rtn>
</#function>
<#function getAtter field isSub>
 	<#assign rtn><#if isSub>item.${field.name}<#else>data.${field.tableName}.${field.name}</#if></#assign>
	<#return rtn>
</#function>
<#function getId field isSub>
 	<#assign rtn><#if isSub>item${field.name}<#else>data${field.path}${field.name}</#if></#assign>
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
	<#assign isBindIdentity = util.getJsonByPath(field,"isBindIdentity")>
	<#assign json = util.getStringConf(field.bindIdentityjson)>
	<#assign isEdit = util.getJsonByPath(field.bindIdentityjson,"isEdit")>
   <#assign isInputEdit = util.getJsonByPath(field.bindIdentityjson,"isInputEdit")>
	<#assign rtn>
		<ht-input v-model="${getNgModel(field,isSub)}"  :permission="${getPermission(field,isSub)}"  atter="${getAtter(field,isSub)}" attr="${getAtter(field,isSub)}" ${util.getAttrs(':validate,htfuncexp,inputType,bindIdentityjson,placeholder,:htDatecalc,tooltipplacement',field)} style="font-weight:${util.getStyleBold(field,'boldValue')};${util.getStyles(field,'color','valueColor')}"  :style='${util.getMapString(field,"controlstyle")}'>
			<span slot="labeldesc">${field.desc}</span>
		</ht-input>
	</#assign>
	<#return rtn>
</#function>

<#function getSelect field isMutl isSub ganged> 
<#assign rtn>
	<#if util.getJsonByPath(field.option,'choiceType')=="dynamic">
	<ht-select :ganged="${util.getSelectQuery(field.option,isSub)}" v-model="${getNgModel(field,isSub)}" ${util.getAttrs('multiple,:filterable,:allowCreate,tooltipplacement',field)} :permission="${getPermission(field,isSub)}" ${util.getAttrs(':validate,:linkage,linkage',field)}     :style='${util.getMapString(field,"controlstyle")}'>
	  <span slot="labeldesc">${field.desc}</span>
	</ht-select>
	<#else>
		<ht-select v-model="${getNgModel(field,isSub)}" :permission="${getPermission(field,isSub)}" :selectlist='${util.getJsonByPath(field.option,'choice')}' ${util.getAttrs('multiple,:filterable,:allowCreate,tooltipplacement',field)} ${util.getAttrs(':validate,:linkage,linkage',field)}    :style='${util.getMapString(field,"controlstyle")}'>
		  <span slot="labeldesc">${field.desc}</span>
		</ht-select>
	</#if>
</#assign>
<#return rtn>
</#function>

<#function getTreeselect field isMutl isSub ganged> 
<#assign rtn>
	<#if util.getJsonByPath(field.option,'choiceType')=="dynamic">
	<ht-treeselect :ganged="${util.getSelectQuery(field.option,isSub)}" v-model="${getNgModel(field,isSub)}" ${util.getAttrs('multiple,:filterable,:allowCreate,tooltipplacement,placeholder',field)} :permission="${getPermission(field,isSub)}" ${util.getAttrs(':validate,:linkage,linkage',field)}     :style='${util.getMapString(field,"controlstyle")}'>
	  <span slot="labeldesc">${field.desc}</span>
	</ht-treeselect>
	<#else>
		<ht-treeselect v-model="${getNgModel(field,isSub)}" :permission="${getPermission(field,isSub)}" :selectlist='${util.getJsonByPath(field.option,'choice')}' ${util.getAttrs('multiple,:filterable,:allowCreate,tooltipplacement',field)} ${util.getAttrs(':validate,:linkage,linkage',field)}    :style='${util.getMapString(field,"controlstyle")}'>
		  <span slot="labeldesc">${field.desc}</span>
		</ht-treeselect>
	</#if>
</#assign>
<#return rtn>
</#function>

<#function getDialog field isSub> 
	<#assign name  = util.getJsonByPath(field.customDialogjson,'name')>
	<#assign icon  = util.getJsonByPath(field.customDialogjson,'icon')>
	<#assign custdialogConf  = util.getStringConf(field.customDialogjson)>
	<#assign rtn>
		<ht-dialog v-model="${getNgModel(field,isSub)}" :custdialog='${custdialogConf}':permission="${getPermission(field,isSub)}" atter="${getAtter(field,isSub)}"  ${util.getAttrs('tooltipplacement',field)}   :style='${util.getMapString(field,"controlstyle")}' >
			<span slot="labeldesc">${field.desc}</span>
		</ht-dialog>
	</#assign>
<#return rtn>
</#function>

<#function getDialogBtn field isSub> 
	<#assign name  = util.getJsonByPath(field.bindEventjson,'name')>
	<#assign icon  = util.getJsonByPath(field.bindEventjson,'icon')>
	<#assign isShowInput  = util.getJsonByPath(field.bindEventjson,'isShowInput')>
	<#assign alias  = util.getJsonByPath(field.bindEventjson,'alias')>
	<#assign script  = util.getJsonByPath(field.bindEventjson,'script')>
	<#assign rtn>
		<#if isShowInput=='true'>
			<ht-dialog-btn :isShowInput="${isShowInput?default(true)}" v-model="${getNgModel(field,isSub)}" :permission="${getPermission(field,isSub)}" attr="${getAtter(field,isSub)}" icon="${icon}" btnName="${name}" htCustomScript="${script}" ${util.getAttrs(':validate,placeholder,tooltipplacement',field)} :style='${util.getMapString(field,"controlstyle")}'>
				<span slot="labeldesc">${field.desc}</span>
			</ht-dialog-btn>
		<#else>
			<ht-dialog-btn :isShowInput="${isShowInput?default(true)}"  icon="${icon}" btnName="${name}" htCustomScript="${script}" ${util.getAttrs(':validate,placeholder,tooltipplacement',field)} :style='${util.getMapString(field,"controlstyle")}'>
				<span slot="labeldesc">${field.desc}</span>
			</ht-dialog-btn>
		</#if>
	</#assign>
<#return rtn>
</#function>

<#function getSubDialog field> 
	<#assign name  = util.getJsonByPath(field.customDialogjson,'name')>
	<#assign icon  = util.getJsonByPath(field.customDialogjson,'icon')>
	<#assign custdialogConf  = util.getStringConf(field.customDialogjson)>
	<#assign rtn>
		<ht-subDialog :custdialog='${custdialogConf}' icon="${icon}" btnName="${name}" />
	</#assign>
<#return rtn>
</#function>

<#function getGangedSelect field isSub ganged> 
<#assign rtn>
	<ht-gangedSelect v-model="${getNgModel(field,isSub)}" :permission="${getPermission(field,isSub)}" :ganged="${util.getSelectQuery(field.option,isSub)}"  :style='${util.getMapString(field,"controlstyle")}'>
		<span slot="labeldesc">${field.desc}</span>
	</ht-gangedSelect>
</#assign>
<#return rtn>
</#function>
<#function getCheckbox field isSub ganged> 
	<#assign rtn>
			<#assign list=util.getJsonByPath(field.option,'choice')?eval>
			<div desc="${field.desc}" ht-checkboxs="${getNgModel(field,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}" ${util.getFieldGanged(field.path+"."+field.name,ganged)}>
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
			<div desc="${field.desc}" ht-radios="${getNgModel(field,isSub)}" ng-model="${getNgModel(field,isSub)}" permission="${getPermission(field,isSub)}"  ${util.getFieldGanged(field.path+"."+field.name,ganged)} >
			<#list list as choice>
				<label class="radio-inline"> <input type="radio" htradios="input" value="${choice.key}" ng-model="${getNgModel(field,isSub)}"> ${choice.value}</label>
			</#list>
			</div>
	</#assign>
	<#return rtn>
</#function>
<#function getRelFlows field isSub ganged>
<#assign rtn>
<ht-rel-flow v-model="${getNgModel(field,isSub)}" atter="${getAtter(field,isSub)}" :permission="${getPermission(field,isSub)}" ${util.getAttrs(':validate',field)} />
</#assign>
<#return rtn>
</#function>
<#-- 注意不能加空格  -->
<#macro input field isSub ganged>
<#switch field.ctrlType>
<#case 'onetext'><#--单行文本框-->${getInput(field,isSub,ganged)}
<#break>
<#case 'multitext'><#--多行文本框-->
<ht-textarea type="text" v-model="${getNgModel(field,isSub)}" id="${getId(field,isSub)}" :permission="${getPermission(field,isSub)}" ${util.getAttrs(':vEditor,placeholder,tooltipplacement,:validate',field)}    :style='${util.getMapString(field,"controlstyle")}'>
	<span slot="labeldesc">${field.desc}</span>
</ht-textarea>
<#break>
<#case 'text'><#--文本-->
<ht-textarea type="text" textValue='${field.textValue}' :vText="true" v-model="${getNgModel(field,isSub)}"
    ${util.getAttrs('tooltipplacement',field)}   :style='${util.getMapString(field,"controlstyle")}'
             styles="font-weight:${util.getStyleBold(field,'boldText')};${util.getStyles(field,'color','textColor')};${util.getStyles(field,'font-size','textSize')}"   :permission="${getPermission(field,isSub)}">
	<span slot="labeldesc">${field.desc}</span>
</ht-textarea>
<#break>
<#case 'select'><#--下拉选项-->
${getSelect(field,false,isSub,ganged)}
<#break>
<#case 'treeselect'><#--下拉树选项-->
${getTreeselect(field,false,isSub,ganged)}
<#break>
<#case 'multiselect'><#--下拉选项多选-->
${getSelect(field,true,isSub,ganged)}
<#break>
<#case 'checkbox'><#--复选框-->
<ht-checkbox v-model="${getNgModel(field,isSub)}" <#if field.option.isVertical?? && field.option.isVertical!= "" >:is-vertical="${field.option.isVertical}"<#else>:is-vertical="false"</#if> :ganged="${util.getSelectQuery(field.option,isSub)}" :permission="${getPermission(field,isSub)}" cklist='${util.getJsonByPath(field.option,'choice')}' ${util.getAttrs(':validate,:linkage,linkage,tooltipplacement',field)}   :style='${util.getMapString(field,"controlstyle")}'>
	<span slot="labeldesc">${field.desc}</span>
</ht-checkbox>
<#break>
<#case 'radio'><#--单选框-->
<ht-radio v-model="${getNgModel(field,isSub)}" <#if field.option.isVertical?? && field.option.isVertical!= "">:is-vertical="${field.option.isVertical}"<#else>:is-vertical="false"</#if> :ganged="${util.getSelectQuery(field.option,isSub)}" :permission="${getPermission(field,isSub)}" rdlist='${util.getJsonByPath(field.option,'choice')}' ${util.getAttrs(':validate,:linkage,linkage,tooltipplacement,',field)}    :style='${util.getMapString(field,"controlstyle")}'>
	<span slot="labeldesc">${field.desc}</span>
</ht-radio>
<#break>
<#case 'date'><#--日期控件-->
<ht-date v-model="${getNgModel(field,isSub)}" ${util.getAttrs('ht-funcexp,:validate,ht-date,tooltipplacement',field)} :permission="${getPermission(field,isSub)}" :showDate="${util.getJsonByPath(field.option,'showCurrentDate','false')}"  :day="${util.getJsonByPath(field.option,'day','0')}" format="${field.option.dataFormat}"  inputFormat="${field.option.inputFormat}"   :style='${util.getMapString(field,"controlstyle")}'>
	<span slot="labeldesc">${field.desc}</span>
</ht-date>
<#break>
<#case 'selector'><#--选择器(包括组织，岗位，角色，用户选择器等控件组合)-->
<${util.getHtSelectorType(field.option,isSub)} ${util.getAttrs(':validate,tooltipplacement',field)} v-model="${getNgModel(field,isSub)}" :selectorconfig="${util.getHtSelector(field.option,isSub)}" :permission="${getPermission(field,isSub)}" />
<#break>
<#case 'officeplugin'><#--office控件-->
<div ng-model="${getNgModel(field,isSub)}"  desc="${field.desc}" ht-input="${getNgModel(field,isSub)}"  permission="${getPermission(field,isSub)}"  ${util.getAttrs('ht-validate,ht-office-plugin',field)} />
<#break>
<#case 'fileupload'><#--文件上传-->
<ht-file v-model="${getNgModel(field,isSub)}" :multiple="${util.getJsonByPath(field.option.file,'multiple','false')}"  accept="${util.getJsonByPath(field.option.file,'accept','false')}" limit="${util.getJsonByPath(field.option.file,'limit','false')}" :permission="${getPermission(field,isSub)}"   ${util.getAttrs(':validate,tooltipplacement,propConf',field)}  :style='${util.getMapString(field,"controlstyle")}'   >
  <span slot="labeldesc">${field.desc}</span>
</ht-file>
<#break>
<#case 'dic'> <#--数据字典-->
<ht-dic dickey="${util.getJsonByPath(field.option,"dic")}" <#if field.bindDicName?exists> bind="${getNgModel(field.bindDicName,isSub)}" </#if> :permission="${getPermission(field,isSub)}" v-model="${getNgModel(field,isSub)}"  ${util.getAttrs(':validate,:filterable,tooltipplacement',field)} :style='${util.getMapString(field,"controlstyle")}' >
  <span slot="labeldesc">${field.desc}</span>
</ht-dic>
<#break>
<#case 'websign'><#--web签章-->
<input style="${getOfficeStyle(field.option)}"  ng-model="${getNgModel(field,isSub)}"  desc="${field.desc}"  permission="${getPermission(field,isSub)}" type="hidden" controltype="webSign" value="" ${util.getAttrs(':validate,htfuncexp',field)} />
<#break>
<#case 'identity'><#--流水号-->
<input ht-identity='{alias:"${util.getJsonByPath(field.option,"identity.alias")}"}' ng-model="${getNgModel(field,isSub)}"  permission="${getPermission(field,isSub)}" desc="${field.desc}" class="form-control" type="text" ${util.getAttrs('ht-funcexp,:ht-validate,ht-datecalc,tooltipplacement',field)}  />
<#break>
<#case 'opinion'><#--表单意见-->
<div class="form-control ${field.name}" ht-bpm-opinion="data.__form_opinion.${field.name}" opinion-history="opinionList.${field.name}" permission="permission.opinion.${field.name}"  style="width:${field.option.width}px!important;height:${field.option.height}px"></div>
<#break>
<#case 'htbpmflowimage'><#--流程示意图-->
<fieldset style="border:1px dotted;"><legend htbpmflowimage="" style="font-size: 15px;">流程示意图：</legend><div ht-bpm-flow-image="inHtml" class="flowchart" style="width:${field.option.width}px!important;height:${field.option.height}px"></div></fieldset>
<#break>
<#case 'history'><#--审批历史-->
<div ht-bpm-approval-history="inHtml" class="fa fa-history flowchart" style="width:${field.option.width}px!important;height:${field.option.height}px"></div>
<#break>
<#case 'dialog'><#--自定义对话框-->
${getDialog(field,isSub)}
<#break>
<#case 'onetextBtn'><#--按钮控件-->
${getDialogBtn(field,isSub)}
<#break>
<#case 'digitalControl'><#--数字控件-->
<ht-digital v-model="${getNgModel(field,isSub)}" :permission="${getPermission(field,isSub)}" atter="${getAtter(field,isSub)}" attr="${getNgModel(field,isSub)}" ${util.getAttrs(':validate,inputType,htfuncexp,placeholder,tooltipplacement',field)} style="font-weight:${util.getStyleBold(field,'boldValue')};${util.getStyles(field,'color','valueColor')}" :style='${util.getMapString(field,"controlstyle")}' :option='${util.getMapString(field,"option")}'>
 <span slot="labeldesc">${field.desc}</span>
</ht-digital>
<#break>
<#case 'currencyControl'><#--货币控件-->
<ht-currency  v-model="${getNgModel(field,isSub)}" :permission="${getPermission(field,isSub)}" atter="${getAtter(field,isSub)}" attr="${getNgModel(field,isSub)}" ${util.getAttrs(':validate,inputType,htfuncexp,placeholder,tooltipplacement',field)} style="font-weight:${util.getStyleBold(field,'boldValue')};${util.getStyles(field,'color','valueColor')}" :style='${util.getMapString(field,"controlstyle")}' :option='${util.getMapString(field,"option")}'>
  <span slot="labeldesc">${field.desc}</span>
</ht-currency>
<#break>
<#case 'iframe'><#--iframe控件-->
	<#if field.option.frameborder>
		<iframe src="${field.iframeSrc}" style="height:${field.option.iframeSrcHeight}px;width:${field.option.iframeSrcWidth}%;border:1px solid  ${field.option.lableColor}"></iframe>
	<#else>
		<iframe src="${field.iframeSrc}" style="height:${field.option.iframeSrcHeight}px;width:${field.option.iframeSrcWidth}%;border:0px"></iframe>
	</#if>
<#break>
<#case 'img'><#--img控件-->
	<img src="${field.imgSrc}" style="height:${field.option.imgSrcHeight}px;width:${field.option.imgSrcWidth}px"></img>
<#break>

<#case 'textFixed'><#--固定文本-->
	<font class="text-fixed" style="font-weight:${util.getStyleBold(field,'textFixedBoldText')};${util.getTextFixedStyles(field,'color','textFixedColor')};${util.getTextFixedStyles(field,'font-size','textFixedSize')}">${field.textFixed}</font>
<#break>

<#case 'customQuery'><#--关联数据下拉选项-->
${getGangedSelect(field,isSub,ganged)}
<#break>
<#case 'relFlow'><#--相关流程-->
${getRelFlows(field,isSub,ganged)}
<#break>
<#case 'url'><#--URl-->
<a src="${field.desc}" target="_blank" />
<#break>
<#case 'realtimeInputCtrl'><#-- 表单实时控件  单行 -->
<ht-realtime-input ${util.getAttrs('inputType,placeholder,tooltipplacement',field)} style="font-weight:${util.getStyleBold(field,'boldValue')};${util.getStyles(field,'color','valueColor')}" :style='${util.getMapString(field,"controlstyle")}' :option='${util.getMapString(field,"option")}'>
  <span slot="labeldesc">${field.desc}</span>
</ht-realtime-input>
<#break>
<#case 'realtimeMultitext'><#-- 表单实时控件  多行  -->
<ht-realtime-textarea ${util.getAttrs('inputType,placeholder,tooltipplacement',field)} style="font-weight:${util.getStyleBold(field,'boldValue')};${util.getStyles(field,'color','valueColor')}" :style='${util.getMapString(field,"controlstyle")}' :option='${util.getMapString(field,"option")}'>
  <span slot="labeldesc">${field.desc}</span>
</ht-realtime-textarea>
<#break>
<#case 'onetextautocomplete'><#-- 带输入建议 -->
<ht-input-autocomplete v-model="${getNgModel(field,isSub)}" :ganged="${util.getSelectQuery(field.option,isSub)}" :permission="${getPermission(field,isSub)}" autoTiplist='${util.getJsonByPath(field.option,'choice')}' ${util.getAttrs(':validate,:linkage,placeholder,linkage,tooltipplacement,',field)}    :style='${util.getMapString(field,"controlstyle")}'>
	<span slot="labeldesc">${field.desc}</span>
</ht-input-autocomplete>
<#break>
<#case 'cascader'><#-- 级联选择 -->
<ht-cascader :ganged="${util.getSelectQuery(field.option,isSub)}" v-model="${getNgModel(field,isSub)}" atter="${getAtter(field,isSub)}" attr="${getAtter(field,isSub)}"  :permission="${getPermission(field,isSub)}" ${util.getAttrs(':validate,placeholder,tooltipplacement,',field)} :style='${util.getMapString(field,"controlstyle")}'>
	<span slot="labeldesc">${field.desc}</span>
</ht-cascader>
<#break>
<#case 'treeCtrl'><#-- 树控件 -->
<ht-tree :ganged="${util.getSelectQuery(field.option,isSub)}" script="${field.nodeClickScript}">
</ht-tree>
<#break>
<#case 'dataTable'><#-- 数据列表 -->
    <table class="form-table" cellspacing="0" cellpadding="0" border="0">
        <tbody>
            <#list field.dataTable.rowList as row>
                <tr @click="transitionIndex = -1">
                    <#list row.colList as item>
                        <#if item.tableField?? && (item.tableField?size > 0) >
                            <td style="text-align: center;">
                                <#list item.tableField as field>
                                    <@input field=field isSub=false ganged=ganged/>
                                </#list>
                            </td>
                        <#elseif item.tableField?? && (item.tableField?size == 0) >
                            <td style="text-align: center;">
                            </td>
                        <#elseif item.rowHeadName??>
                            <td style="text-align: center;background-color: #f9f9f9">
                                <span>${item.rowHeadName}</span>
                            </td>
                        <#elseif item.colHeadName??>
                            <td style="text-align: center;background-color: #f9f9f9">
                                <span>${item.colHeadName}</span>
                            </td>
                        </#if>
                    </#list>
                </tr>
            </#list>
        </tbody>
    </table>
<#break>
<#case 'divContainer'><#-- div容器模板 -->
<@divContainer divContainerField=field/>
<#break>
</#switch>

</#macro>

<#macro subDialog field>
${getSubDialog(field)}
</#macro>