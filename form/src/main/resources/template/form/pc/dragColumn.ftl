<#setting number_format="0">
<#if !isTabs>
${util.getIncludeFiles(includeFiles)}
</#if>
        <table class="form-table" cellspacing="0" cellpadding="0" border="0">
          <tbody>
          <#if !isTabs>
		   <tr @click="transitionIndex = -1">
              <th class="group-th" colspan="4">${formDesc}</th>
            </tr>
          </#if>
        <#list fieldList as field>
            <#if field.ctrlType!='sub' && field.ctrlType!='text'>
			<tr @click="transitionIndex = -1">
              <th width="10%" v-if="${getPermission(field,isSub)}!='n'">
                <span v-if="${getPermission(field,isSub)}=='b'">*</span>
                ${field.desc}：
              </th>
              <td width="40%" v-if="${getPermission(field,isSub)}!='n'">
			  <@input field=field isSub=false ganged=ganged/>
                
              </td>
			 </tr> 
			</#if>
            <#if field.ctrlType=='sub'>
            <tr @click="transitionIndex = -1" v-if="!permission.table.${field.tableName}.hidden">
              <th class="group-th" colspan="4" @click="transitionIndex = -1" sub-relation="${field.relation}">
              	${field.desc}
              	<#if field.isSubCustDialog>
					<@subDialog field=field />
				</#if>
              </th>
            </tr>
            <tr v-if="!permission.table.${field.tableName}.hidden">
              <td colspan="4">
            		<@subTable field=field/>
              </td>
              </tr>
            </#if>
		</#list>
		</tbody>
		</table>