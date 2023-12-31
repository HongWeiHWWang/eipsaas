<#macro subDiv layout>
<#assign subTablePath=layout.options.subTablePath>
<#assign subDivTablePath=layout.options.subDivTablePath>
<#assign sunBos=util.getSunBos(layout.list)>
<el-container sunBos="${sunBos}" v-if="!permission.table.${layout.options.boSubEntity}.hidden">
  <el-header style="height:30px;line-height:30px;background: #fafafa; font-weight: bold;font-size: 14px;">
  	${layout.desc}
  	 <#if layout.subtableBackfill>
  	 	${getSubDialog(layout)}
  	 </#if>
      <#if layout.options.relation!='onetoone'>
          <el-button
                  v-if="permission.table.${layout.options.boSubEntity}.add"
                  size="small"
                  type="text"
                  icon="el-icon-plus"
                  @click="addSubTab('data.${subTablePath}')"
          >{{$t('common.add')}}</el-button>
      </#if>
      <#if layout.options.relation=='onetoone'>
                  <el-button
                          v-show="'${layout.options.relation}'=='onetoone' && data.${subTablePath}.length<1"
                          v-if="permission.table.${layout.options.boSubEntity}.add"
                          size="small"
                          type="text"
                          icon="el-icon-plus"
                          @click="addSubTab('data.${subTablePath}')"
                  >{{$t('common.add')}}</el-button>
      </#if>
  </el-header>
  <el-main>
          <table class="form-table" cellspacing="0" cellpadding="0" border="0" v-for="(item, index) in data.${subTablePath}" :key="index" :sub-index="index" :data-index="index" data-subname="data.${subTablePath}">
              <tbody>
              <tr>
                  <td colspan="15">
              <#list layout.list as field>
              		<#if (field.ctrlType != 'suntable' && field.ctrlType != 'sunDiv')>
                          <#if field.ctrlType=='grid'>
                              <el-row type="flex" :gutter="${field.options.gutter}" justify="${field.options.justify}" align="${field.options.align}">
                                  <#list field.columns as gridGroup>
                                      <el-col :span="${gridGroup.span}" style="${gridGroup.style}">
                                          <#list gridGroup.list as gridField>
                                              ${getFormItem(gridField,2)}
                                          </#list>
                                      </el-col>
                                  </#list>
                              </el-row>
                          <#else>
                              <el-row <#if !field.options.noBindModel> v-if="${getPermission(field,true)}!='n' && !(permission.sub_${field.tableName} && permission.sub_${field.tableName}.${field.name}index !='n')" </#if>>
                                  ${getFormItem(field,2)}
                              </el-row>
                          </#if>
                     <#else>
                     	<el-row  v-if="!permission.table.${field.name}.hidden">
							<#if field.ctrlType == 'suntable'>
	                			<@sunTable layout=field />
	                		<#else>
	                			<@sunDiv layout=field />
	                		</#if>
                		</el-row>
					</#if>
              </#list>
                  </td>
              </tr>
              </tbody>
                  <tfoot v-if="isView && !(!permission.table.${layout.options.boSubEntity}.del && !permission.table.${layout.options.boSubEntity}.add)" @click="transitionIndex = -1" >
                  <tr >
                      <td colspan="15">
                        <#if layout.options.relation!='onetoone' && subDivTablePath != ''>
                          <el-button
                                  v-if="permission.table.${layout.options.boSubEntity}.add"
                                  size="small"
                                  type="text"
                                  icon="el-icon-plus"
                                  @click="addSubTab('data.${subDivTablePath}')"
                          >{{$t('common.add')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.del" @click="deleteRow('${subDivTablePath}',item)">{{$t('common.delete')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add" @click="copy(data.${subDivTablePath},item)">{{$t('common.copy')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="up(index,data.${subDivTablePath}),transitionIndex = index-1">{{$t('common.moveUp')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="down(index,data.${subDivTablePath}),transitionIndex = index+1">{{$t('common.moveDown')}}</el-button>
                        <#else>
                            <el-button
                                    v-if="permission.table.${layout.options.boSubEntity}.add"
                                    size="small"
                                    type="text"
                                    icon="el-icon-plus"
                                    @click="addSubTab('data.${subTablePath}')"
                            >{{$t('common.add')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.del" @click="deleteRow('${subTablePath}',item)">{{$t('common.delete')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add" @click="copy(data.${subTablePath},item)">{{$t('common.copy')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="up(index,data.${subTablePath}),transitionIndex = index-1">{{$t('common.moveUp')}}</el-button>
                            <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="down(index,data.${subTablePath}),transitionIndex = index+1">{{$t('common.moveDown')}}</el-button>
                        </#if>

                      </td>
                  </tr>
                  </tfoot>
          </table>
  </el-main>
</el-container>
</#macro>

