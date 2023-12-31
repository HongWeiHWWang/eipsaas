<#macro subTable layout>
<#assign subListPath=layout.options.subTablePath>
<#assign sunBos=util.getSunBos(layout.list)>
<el-container v-if="!permission.table.${layout.options.boSubEntity}.hidden">
  <el-header sunBos="${sunBos}" style="height:30px;line-height:30px;background: #fafafa; font-weight: bold;font-size: 14px;">
  	${layout.desc}
  	 <#if layout.subtableBackfill> 
  	 	${getSubDialog(layout)}
  	 </#if>
  </el-header>
  <el-main>
  	<#assign orgConfigStr = util.getJsonByPath(layout,'customDialogjson.orgConfig')>
    <#assign orgConfigJson = util.getJsonStr(orgConfigStr)>
    <div class="formT_box">
        <div class="xh_table" id="xh_tablegd_${subListPath}">
            <div class="xh_hdleft" :style="left_image" id="lefthk_${subListPath}" @click="xhleft('${subListPath}')"></div>
            <table class="form-table" :onload='initFill("${subListPath}",${orgConfigJson})' cellspacing="0" cellpadding="0" border="0">
              <thead>
                  <#if layout.customHeader gt 1 >
                    ${util.decodeBase64(layout.customHeader)}
                  <#else>
                    <tr class="sub-table-header" @click="transitionIndex = -1">
                      <#if layout.options.relation!='onetoone'><th style="min-width: 48px;">{{$t('common.seq')}}</th></#if>
                      <#list layout.list as field>
                        <#assign tip = util.getJsonByPath(field.options,"tip")>
                            <#if (field.ctrlType != 'suntable' && field.ctrlType != 'sunDiv')>
                                <th <#if !field.options.noBindModel> v-if="${getPermission(field,true)}!='n'"</#if> style="min-width: 200px;font-weight:${util.getStyleBold(field.options,'boldLable')};${util.getStyles(field.options,'color','lableColor')};${util.getStyles(field.options,'width','labelstyleWidth')};"><#if !field.options.noBindModel><span  v-if="${getPermission(field,true)}=='b'">*</span></#if>
                                    <#if tip?length gt 0>
                                        <el-tooltip placement="top">
                                            <div slot="content" >${field.options.tip}</div>
                                            <i class="el-icon-question" />
                                        </el-tooltip>
                                    </#if>
                                    <span style="${util.getFieldStyle(field.options)}">${field.desc}</span>
                                </th>
                             <#else>
                                <th v-if="!permission.table.${field.name}.hidden">
                                    <span>${field.desc}</span>
                                </th>
                            </#if>
                      </#list>
                       <#if layout.options.relation!='onetoone'> <th style="min-width: 170px;" v-if="permission.table.${layout.options.boSubEntity}.del">{{$t('common.operation')}}</th></#if>
                    </tr>
                  </#if>
              </thead>
              <tbody>
                  <tr v-for="(item, index) in data.${subListPath}" :key="index" :data-index="index" :sub-index="index" :class="{'transition':index==transitionIndex}" data-subname="data.${subListPath}">
                    <#if layout.options.relation!='onetoone'><td @click="transitionIndex = -1" align="center" style="border-left: 1px solid #f9f9f9;">{{index + 1}}</td></#if>
                    <#list layout.list as field>
                        <#if (field.ctrlType != 'suntable' && field.ctrlType != 'sunDiv')>
                            <td @click="transitionIndex = -1" <#if !field.options.noBindModel> v-if="${getPermission(field,2)}!='n' && !(permission.sub_${field.tableName} && permission.sub_${field.tableName}.${field.name}index !='n')" </#if> style="font-weight:${util.getStyleBold(field,'boldValue')};${util.getStyles(field,'color','valueColor')};${util.getStyles(field,'width','width')};">
                               <@input field=field type=2/>
                            </td>
                        <#else>
                            <td v-if="!permission.table.${field.name}.hidden">
                                <#if field.ctrlType == 'suntable'>
                                    <@sunTable layout=field />
                                <#else>
                                    <@sunDiv layout=field />
                                </#if>
                            </td>
                        </#if>

                    </#list>
                    <#if layout.options.relation!='onetoone'>
                      <td class="trash" v-if="permission.table.${layout.options.boSubEntity}.del">
                          <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.del" @click="deleteRow('${subListPath}',item)">{{$t('common.delete')}}</el-button>
                          <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add" @click="copy(data.${subListPath},item)">{{$t('common.copy')}}</el-button>
                          <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="up(index,data.${subListPath}),transitionIndex = index-1">{{$t('common.moveUp')}}</el-button>
                          <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="down(index,data.${subListPath}),transitionIndex = index+1">{{$t('common.moveDown')}}</el-button>
                      </td>
                    </#if>
                  </tr>
              </tbody>
              <#if layout.options.relation!='onetoone'>
                  <tfoot v-if="isView" @click="transitionIndex = -1">
                    <tr v-if="permission.table.${layout.options.boSubEntity}.add">
                      <td colspan="15">
                        <el-button
                          size="small"
                          type="text"
                          icon="el-icon-plus"
                          @click="addSubTab('data.${subListPath}')"
                        >{{$t('common.add')}}</el-button>
                      </td>
                    </tr>
                  </tfoot>
               </#if>
               <#if layout.options.relation=='onetoone'>
                  <tfoot @click="transitionIndex = -1" v-show="'${layout.options.relation}'=='onetoone' && data.${subListPath}.length<1">
                    <tr v-if="permission.table.${layout.options.boSubEntity}.add">
                      <td colspan="15">
                        <el-button
                          size="small"
                          type="text"
                          icon="el-icon-plus"
                          @click="addSubTab('data.${subListPath}')"
                        >{{$t('common.add')}}</el-button>
                      </td>
                    </tr>
                  </tfoot>
               </#if>
            </table>
            <div class="xh_hd" :style="right_image" id="righthk_${subListPath}" @click="xhright('${subListPath}')"></div>
        </div>
    </div>
  </el-main>
</el-container>
</#macro>

<#function getSubDialog layout >
    <#assign custdialogConf  = util.getStringConf(layout.customDialogjson)>
    <#assign rtn>
    	 <eip-subDialog
    	  :custdialog='${custdialogConf}'
    	  initFillData="${layout.initTemplateData}"
    	  initFillDataType="${layout.initTemplateDataType}"
        />
    </#assign>
    <#return rtn>
</#function>