<#setting number_format="0">
${util.getIncludeFiles(includeFiles)}
<el-tabs  type="border-card" ref="el_table_check">
 	<#list tabs as tab>
  		<el-tab-pane <#if tab.tabField.ctrlType == 'tabCheck'>disabled ref="next_step_check_${tab_index}" </#if>   label="${tab.tabField.desc}" icon="el-icon-edit">${tab.tabHtml}
  		<#if tab.tabField.ctrlType == 'tabCheck'>
		    <div class="tabcheck_btn">
		    	 <#if tab_index!=0>
			          <el-button type="success" class="step_change_btn" @click="nextStepClick('${tab.tabField.nextCheck}',${tab_index},'back')" size="small">上一步</el-button>
				 </#if>  
				 
		    	 <#if tab_has_next>
			          <el-button type="primary" class="step_change_btn" @click="nextStepClick('${tab.tabField.nextCheck}',${tab_index},'next')" size="small">下一步</el-button>
				 </#if>  
	        </div>
		 </#if>  
	    </el-tab-pane>
	</#list>
</el-tabs>