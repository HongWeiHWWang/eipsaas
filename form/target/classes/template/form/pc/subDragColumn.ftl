<#macro subTable field>
<#assign subTablePath=field.children[0].path>
<table class="form-table" cellspacing="0" cellpadding="0" border="0">
                  <thead>
           		  <#if field.customHeader gt 1 >
           		  ${util.decodeBase64(field.customHeader)}
                  <#else>
                    <tr class="sub-table-header" @click="transitionIndex = -1">
			          <#if field.relation!='onetoone'><th width="48">{{$t('common.seq')}}</th></#if>
			          <#list field.children as field>
			          	<#assign tips = util.getJsonByPath(field,"tips")>
			            <th v-if="${getPermission(field,isSub)}!='n'" style="font-weight:${util.getStyleBold(field,'boldLable')};${util.getStyles(field,'color','lableColor')};${util.getStyles(field,'width','width')};"><span  v-if="${getPermission(field,isSub)}=='b'">*</span>
			            	<#if tips?length gt 0>
			            		<el-tooltip class="item" effect="dark" content="${field.tips}" placement="top-end"><el-span>${field.desc}<i class="el-input__icon icon-query" :style="{fontSize:'13px'}"></i></el-span></el-tooltip>
							<#else>
								${field.desc}
							</#if>
			            </th>
			          </#list>
			           <#if field.relation!='onetoone'> <th width="170" v-if="permission.table.${field.tableName}.del">{{$t('common.operation')}}</th></#if>
                    </tr>
                    
				  </#if>
                  </thead>
                  <tbody>
          <tr v-for="(item, index) in data.${subTablePath}" :key="index" :data-index="index" :class="{'transition':index==transitionIndex}" data-subname="data.${subTablePath}">
                                <#if field.relation!='onetoone'><td @click="transitionIndex = -1" align="center" style="border-left: 1px solid #f9f9f9;">{{index + 1}}</td></#if>
                                <#list field.children as field>
                                    <td @click="transitionIndex = -1" v-if="${getPermission(field,isSub)}!='n'" style="font-weight:${util.getStyleBold(field,'boldValue')};${util.getStyles(field,'color','valueColor')};${util.getStyles(field,'width','width')};"><@input field=field isSub=true ganged=ganged/></td>
                                </#list>
                                <#if field.relation!='onetoone'>
                                  <td class="trash" v-if="permission.table.${field.tableName}.del">
                                      <el-button size="small" type="text" @click="deleteRow('${subTablePath}',item)">{{$t('common.delete')}}</el-button>
                                      <el-button size="small" type="text" @click="copy(data.${subTablePath},item)">{{$t('common.copy')}}</el-button>
                                      <el-button size="small" type="text" v-if="'${field.relation}'!='onetoone'" @click="up(index,data.${subTablePath}),transitionIndex = index-1">{{$t('common.moveUp')}}</el-button>
                                      <el-button size="small" type="text" v-if="'${field.relation}'!='onetoone'" @click="down(index,data.${subTablePath}),transitionIndex = index+1">{{$t('common.moveDown')}}</el-button>
                                  </td>
                                </#if>
                            </tr>
                  </tbody>
          <#if field.relation!='onetoone'>
                  <tfoot v-if="isView" @click="transitionIndex = -1">
                    <tr v-if="permission.table.${field.tableName}.add">
                      <td colspan="15">
                        <el-button
                          size="small"
                          type="text"
                          icon="el-icon-plus"
                          @click="addSubTab('data.${subTablePath}')"
                        >{{$t('common.add')}}</el-button>
                      </td>
                    </tr>
                  </tfoot>
           </#if>
                   <#if field.relation=='onetoone'>
                  <tfoot v-if="isView" @click="transitionIndex = -1" v-show="'${field.relation}'=='onetoone' && data.${subTablePath}.length<1">
                    <tr v-if="permission.table.${field.tableName}.add">
                      <td colspan="15">
                        <el-button
                          size="small"
                          type="text"
                          icon="el-icon-plus"
                          @click="addSubTab('data.${subTablePath}')"
                        >{{$t('common.add')}}</el-button>
                      </td>
                    </tr>
                  </tfoot>
           </#if>
                </table>
</#macro>