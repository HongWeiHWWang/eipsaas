<#macro divContainer divContainerField>
<el-container :style='${util.getMapString(divContainerField,"controlstyle")}'>
  <el-main>
  	<el-form   <#if divContainerField.inline==true> :inline="true"<#else> label-position="left"  label-width="80px" </#if> class="demo-form-inline">
  	  <#list divContainerField.children as field>
		    <#assign tips = util.getJsonByPath(field,"tips")>
            <#if field.ctrlType!='divContainer' && field.ctrlType!='sub' && field.ctrlType!='iframe' && field.ctrlType!='img' && field.ctrlType!='textFixed' && field.ctrlType!='onetextBtn' && field.ctrlType!='realtimeInputCtrl' && field.ctrlType!='realtimeMultitext' && field.ctrlType!='dataTable' && !field.hideLabel>
				<ht-form-item v-if="${getPermission(field,isSub)}!='n'" ${util.getAttrs(':validate',field)} :permission="${getPermission(field,isSub)}" :style='${util.getMapString(field,"labelstyle")}'>
               		<span slot="labeldesc">${field.desc}<#if tips?length gt 0><i class="el-input__icon icon-query" :style="{fontSize:'13px'}"  ></i></#if>: </span>
                	<span slot="tipcontent">${field.tips}</span>
                	<span slot="fieldControl">
                	 	<@input field=field isSub=false ganged=ganged/>
                	</span>
                </ht-form-item>
            <#elseif  field.ctrlType=='onetextBtn'>
                    <ht-form-item ${util.getAttrs(':validate',field)} <#if field.bindEventjson.isShowInput> :permission="${getPermission(field,isSub)}" </#if> :style='${util.getMapString(field,"labelstyle")}'>
                        <span slot="labeldesc">${field.desc}<#if tips?length gt 0><i class="el-input__icon icon-query" :style="{fontSize:'13px'}"  ></i></#if>: </span>
                    	<span slot="tipcontent">${field.tips}</span>
                    	<span slot="fieldControl">
	                	 	<@input field=field isSub=false ganged=ganged/>
	                	</span>
                    </ht-form-item>
            <#elseif  field.hideLabel || field.ctrlType=='divContainer' >
                <ht-form-item ${util.getAttrs(':validate',field)} :permission="${getPermission(field,isSub)}" :style='${util.getMapString(field,"labelstyle")}'>
                    <span slot="labeldesc">${field.desc}<#if tips?length gt 0><i class="el-input__icon icon-query" :style="{fontSize:'13px'}"  ></i></#if>: </span>
                	<span slot="tipcontent">${field.tips}</span>
                	<span slot="fieldControl">
                	 	<@input field=field isSub=false ganged=ganged/>
                	</span>
                </ht-form-item>
            <#else>
                <ht-form-item ${util.getAttrs(':validate',field)} :permission="${getPermission(field,isSub)}" :style='${util.getMapString(field,"labelstyle")}'>
               		<span slot="labeldesc">${field.desc}<#if tips?length gt 0><i class="el-input__icon icon-query" :style="{fontSize:'13px'}"  ></i></#if>: </span>
                	<span slot="tipcontent">${field.tips}</span>
                	<span slot="fieldControl">
                	 	<@input field=field isSub=false ganged=ganged/>
                	</span>
                </ht-form-item>
            </#if>
	   </#list>
	 </el-form>
  </el-main>
</el-container>
	
</#macro>