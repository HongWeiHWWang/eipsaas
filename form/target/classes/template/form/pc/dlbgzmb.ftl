<#if expandMap.treeCtrl.showTree>
<el-row>
  <el-col :span="4">
  	  <#assign field = expandMap.treeCtrl>
  	  <@input field=field type=1 ganged=ganged/>
  </el-col>
  <el-col :span="20">
</#if>

<#setting number_format="0">
<#if !isTabs>
${util.getIncludeFiles(includeFiles)}
</#if>

<#if hasStepControl>
	<#assign stepPosition = util.getAttrText(stepAttr,"stepPosition")>
	<#if (stepPosition=='left'|| stepPosition=='right')>
		<div style="display:flex;flex-direction:row;">
	<#else>
		<div style="display:flex;flex-direction:column;">
	</#if>
	<#if stepPosition=='left'>
		<ht-step direction='vertical' v-model="${getNgModel(stepAttr,type)}" content='${util.getJsonByPath(stepAttr,'steps')}' :style='${util.getMapString(stepAttr,"controlstyle")}'></ht-step>
	</#if>
	<#if stepPosition=='top'>
		<ht-step direction='horizontal' v-model="${getNgModel(stepAttr,type)}" content='${util.getJsonByPath(stepAttr,'steps')}' :style='${util.getMapString(stepAttr,"controlstyle")}'></ht-step>
	</#if>
</#if>

<table class="form-table" cellspacing="0" cellpadding="0" border="0" >
  <tbody>
  	<#if !isTabs>
    	<tr  @click="transitionIndex = -1">
     	 <th class="group-th" colspan="${maxCol*2}">${formDesc}</th>
   	    </tr>
    </#if>
    <#list trGroup as group>
      <#if group.isSub>
        <#list group.fields as field>
          <tr @click="transitionIndex = -1" v-if="!permission.table.${field.tableName}.hidden">
            <th class="group-th" colspan="${maxCol*2}" sub-relation="${field.relation}">
            	${field.desc}
            	<#if field.isSubCustDialog>
					<@subDialog field=field />
				</#if>
            </th>
          </tr>
          <tr v-if="!permission.table.${field.tableName}.hidden">
            <td colspan="${maxCol*2}">
            <@subTable field=field/>
            </td>
          </tr>
        </#list>
      <#else>
        <tr v-permit="${maxCol*2}" @click="transitionIndex = -1">
          <#list group.fields as field>
          	<#assign tips = util.getJsonByPath(field,"tips")>
            <#if field.ctrlType!='divContainer' && field.ctrlType!='sub' && field.ctrlType!='iframe' && field.ctrlType!='img' && field.ctrlType!='textFixed' && field.ctrlType!='onetextBtn' && field.ctrlType!='realtimeInputCtrl' && field.ctrlType!='realtimeMultitext' && field.ctrlType!='dataTable' && !field.hideLabel>
	            <th width="${(maxCol==1)?string('20%','10%')}" v-if="${getPermission(field,type)}!='n'" style="font-weight:${util.getStyleBold(field,'boldLable')};${util.getStyles(field,'color','lableColor')}">
	                <ht-label ${util.getAttrs(':validate',field)} :permission="${getPermission(field,type)}" :style='${util.getMapString(field,"labelstyle")}'>
	               		<span slot="labeldesc">${field.desc}<#if tips?length gt 0><i class="el-input__icon icon-query" :style="{fontSize:'13px'}"  ></i></#if>: </span>
	                	<span slot="tipcontent">${field.tips}</span>
	                </ht-label>
	            </th>
	            <td width="${util.getWidth(maxCol, group.count)}" ${util.getColspan(maxCol, group.count, field_index + 1, false)} v-if="${getPermission(field,type)}!='n'">
	            <@input field=field type=1 ganged=ganged/>
	            </td>
            <#elseif  field.ctrlType=='onetextBtn'>
                <th width="${(maxCol==1)?string('20%','10%')}" style="font-weight:${util.getStyleBold(field,'boldLable')};${util.getStyles(field,'color','lableColor')}">
                    <ht-label ${util.getAttrs(':validate',field)} <#if field.bindEventjson.isShowInput> :permission="${getPermission(field,type)}" </#if> :style='${util.getMapString(field,"labelstyle")}'>
                        <span slot="labeldesc">${field.desc}<#if tips?length gt 0><i class="el-input__icon icon-query" :style="{fontSize:'13px'}"  ></i></#if>: </span>
                    	<span slot="tipcontent">${field.tips}</span>
                    </ht-label>
                </th>
                <td ${util.getColspan(maxCol, group.count, field_index + 1, true)}>
                    <@input field=field type=1 ganged=ganged/>
                </td>
            <#elseif  field.hideLabel || field.ctrlType=='divContainer' >
                <td colspan="2">
                    <@input field=field type=1 ganged=ganged/>
                </td>
            <#else>
                <th width="${(maxCol==1)?string('20%','10%')}" style="font-weight:${util.getStyleBold(field,'boldLable')};${util.getStyles(field,'color','lableColor')}">
                    <ht-label :style='${util.getMapString(field,"labelstyle")}'>
                        <span slot="labeldesc">${field.desc}<#if tips?length gt 0><i class="el-input__icon icon-query" :style="{fontSize:'13px'}"  ></i></#if>: </span>
                    	<span slot="tipcontent">${field.tips}</span>
                    </ht-label>
                </th>
                <td ${util.getColspan(maxCol, group.count, field_index + 1, true)}>
                    <@input field=field type=1 ganged=ganged/>
                </td>
            </#if>
          </#list>
        </tr>
      </#if>
    </#list>
  </tbody>
</table>

<#if hasStepControl>
	<#assign stepPosition = util.getAttrText(stepAttr,"stepPosition")>
	<#if stepPosition=='right'>
		<ht-step direction='vertical' v-model="${getNgModel(stepAttr,type)}" content='${util.getJsonByPath(stepAttr,'steps')}' :style='${util.getMapString(stepAttr,"controlstyle")}'></ht-step>
	</#if>
	<#if stepPosition=='buttom'>
		<ht-step direction='horizontal' v-model="${getNgModel(stepAttr,type)}" content='${util.getJsonByPath(stepAttr,'steps')}' :style='${util.getMapString(stepAttr,"controlstyle")}'></ht-step>
	</#if>
	</div>
</#if>

<#if expandMap.treeCtrl.showTree>
  </el-col>
</el-row>
</#if>