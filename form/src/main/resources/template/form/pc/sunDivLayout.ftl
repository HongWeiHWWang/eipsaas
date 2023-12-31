<#macro sunDiv layout>
<#assign subTablePath=layout.options.subTablePath>
<#assign sunTablePath=util.getSunTablePath(layout.options.subTablePath,"index")>
<#assign subName=util.getSubName(layout.options.subTablePath,"index")>
<el-container v-if="!permission.table.${layout.options.boSubEntity}.hidden">
    <el-header style="line-height:30px;background: #fafafa; font-weight: bold;font-size: 14px;">
        ${layout.desc}
  	 <#if layout.subtableBackfill>
         ${getSunDialog(layout)}
     </#if>
      <#if layout.options.relation!='onetoone'>
          <el-button
                  v-if="permission.table.${layout.options.boSubEntity}.add"
                  size="small"
                  type="text"
                  icon="el-icon-plus"
                  @click="addSunTab('data.${subTablePath}',index)"
          >{{$t('common.add')}}</el-button>
      </#if>
      <#if layout.options.relation=='onetoone'>
                  <el-button
                          v-if="permission.table.${layout.options.boSubEntity}.add"
                          size="small"
                          type="text"
                          icon="el-icon-plus"
                          @click="addSunTab('data.${subTablePath}',index)"
                  >{{$t('common.add')}}</el-button>
      </#if>
    </el-header>
    <el-main>
    	<#assign orgConfigStr = util.getJsonByPath(layout,'customDialogjson.orgConfig')>
      	<#assign orgConfigJson = util.getJsonStr(orgConfigStr)>
        <table class="form-table" :onload='initFill("${subListPath}",${orgConfigJson})' cellspacing="0" cellpadding="0" border="0" v-for="(item, sunIndex) in data.${sunTablePath}" :key="sunIndex" :data-index="sunIndex" :data-subname="'data.${subName}'">
            <tbody>
            <tr>
                <td colspan="15">
              <#list layout.list as field>
                          <#if field.ctrlType=='grid'>
                              <el-row type="flex" :gutter="${field.options.gutter}" justify="${field.options.justify}" align="${field.options.align}">
                                  <#list field.columns as gridGroup>
                                      <el-col :span="${gridGroup.span}" style="${gridGroup.style}">
                                          <#list gridGroup.list as gridField>
                                              ${getFormItem(gridField,3)}
                                          </#list>
                                      </el-col>
                                  </#list>
                              </el-row>
                          <#else>
                              <el-row <#if !field.options.noBindModel> v-if="${getPermission(field,true)}!='n' && !(permission.sub_${field.tableName} && permission.sub_${field.tableName}.${field.name}sunIndex !='n')" </#if>>
                                  ${getFormItem(field,3)}
                              </el-row>
                          </#if>
              </#list>
                </td>
            </tr>
            </tbody>
            <tfoot v-if="isView && !(!permission.table.${layout.options.boSubEntity}.del && !permission.table.${layout.options.boSubEntity}.add)" @click="transitionIndex = -1" >
            <tr >
                <td colspan="15">
                        <#if layout.options.relation!='onetoone'>
                            <el-button
                                    v-if="permission.table.${layout.options.boSubEntity}.add"
                                    size="small"
                                    type="text"
                                    icon="el-icon-plus"
                                    @click="addSunTab('data.${subTablePath}',index)"
                            >{{$t('common.add')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.del" @click="deleteSunRow('${subTablePath}',item,index)">{{$t('common.delete')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add" @click="copy(data.${sunTablePath},item)">{{$t('common.copy')}}111</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="up(sunIndex,data.${sunTablePath}),transitionIndex = sunIndex-1">{{$t('common.moveUp')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="down(sunIndex,data.${sunTablePath}),transitionIndex = sunIndex+1">{{$t('common.moveDown')}}</el-button>
                        </#if>

                </td>
            </tr>
            </tfoot>
        </table>
    </el-main>
</el-container>
</#macro>

<#function getSunDialog layout >
    <#assign custdialogConf  = util.getStringConf(layout.customDialogjson)>
    <#assign rtn>
    	 <eip-sunDialog
             :custdialog='${custdialogConf}'
             initFillData="${layout.initTemplateData}"
	  		 initFillDataType="${layout.initTemplateDataType}"
         />
    </#assign>
    <#return rtn>
</#function>