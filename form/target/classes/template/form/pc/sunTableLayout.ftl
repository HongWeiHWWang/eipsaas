<#macro sunTable layout>
<#assign subTablePath=layout.options.subTablePath>
<#assign sunTablePath=util.getSunTablePath(layout.options.subTablePath,"index")>
<#assign subName=util.getSubName(layout.options.subTablePath,"index")>
<el-container v-if="!permission.table.${layout.options.boSubEntity}.hidden">
  <el-header style="height:30px;line-height:30px;background: #fafafa; font-weight: bold;font-size: 14px;">
  	${layout.desc}
  	 <#if layout.subtableBackfill> 
  	 	${getSunDialog(layout)}
  	 </#if>
  </el-header>
  <el-main>
      <el-scrollbar>
        <table class="form-table" cellspacing="0" cellpadding="0" border="0">
          <thead>
              <#if layout.customHeader gt 1 >
                ${util.decodeBase64(layout.customHeader)}
              <#else>
                <tr class="sub-table-header" @click="transitionIndex = -1">
                  <#if layout.options.relation!='onetoone'><th style="min-width: 48px;">{{$t('common.seq')}}</th></#if>
                  <#list layout.list as field>
                    <#assign tip = util.getJsonByPath(field.options,"tip")>
                    <th <#if !field.options.noBindModel> v-if="${getPermission(field,true)}!='n'"</#if> style="min-width: 200px;font-weight:${util.getStyleBold(field.options,'boldLable')};${util.getStyles(field.options,'color','lableColor')};${util.getStyles(field.options,'width','labelstyleWidth')};"><#if !field.options.noBindModel><span  v-if="${getPermission(field,true)}=='b'">*</span></#if>
                        <#if tip?length gt 0>
                            <el-tooltip placement="top">
                                <div slot="content" >${field.options.tip}</div>
                                <i class="el-icon-question" />
                            </el-tooltip>
                        </#if>
                        <span style="${util.getFieldStyle(field.options)}">${field.desc}</span>
                    </th>
                  </#list>
                   <#if layout.options.relation!='onetoone'> <th style="min-width: 170px;" v-if="permission.table.${layout.options.boSubEntity}.del">{{$t('common.operation')}}</th></#if>
                </tr>
              </#if>
          </thead>
          <tbody>
              <tr v-for="(item, sunIndex) in data.${sunTablePath}" :key="sunIndex" :data-index="sunIndex" :class="{'transition':sunIndex==transitionIndex}" :data-subname="'data.${subName}'">
                <#if layout.options.relation!='onetoone'><td @click="transitionIndex = -1" align="center" style="border-left: 1px solid #f9f9f9;">{{sunIndex + 1}}</td></#if>
                <#list layout.list as field>
                    <td @click="transitionIndex = -1" <#if !field.options.noBindModel> v-if="${getPermission(field,3)}!='n' " </#if> style="font-weight:${util.getStyleBold(field,'boldValue')};${util.getStyles(field,'color','valueColor')};${util.getStyles(field,'width','width')};">
                       <@input field=field type=3/>
                    </td>
                </#list>
                <#if layout.options.relation!='onetoone'>
                  <td class="trash" v-if="permission.table.${layout.options.boSubEntity}.del">
                      <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.del" @click="deleteSunRow('${subTablePath}',item,index)">{{$t('common.delete')}}</el-button>
                      <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add" @click="copy(data.${sunTablePath},item)">{{$t('common.copy')}}</el-button>
                      <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="up(sunIndex,data.${sunTablePath}),transitionIndex = sunIndex-1">{{$t('common.moveUp')}}</el-button>
                      <el-button size="small" type="text" v-if="permission.table.${layout.options.boSubEntity}.add && '${layout.options.relation}'!='onetoone'" @click="down(sunIndex,data.${sunTablePath}),transitionIndex = sunIndex+1">{{$t('common.moveDown')}}</el-button>
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
                      @click="addSunTab('data.${subTablePath}',index)"
                    >{{$t('common.add')}}</el-button>
                  </td>
                </tr>
              </tfoot>
           </#if>
           <#if layout.options.relation=='onetoone'>
              <tfoot @click="transitionIndex = -1" v-show="'${layout.options.relation}'=='onetoone' && data.${sunTablePath}.length<1">
                <tr v-if="permission.table.${layout.options.boSubEntity}.add">
                  <td colspan="15">
                    <el-button
                      size="small"
                      type="text"
                      icon="el-icon-plus"
                      @click="addSunTab('data.${subTablePath}',index)"
                    >{{$t('common.add')}}</el-button>
                  </td>
                </tr>
              </tfoot>
           </#if>
        </table>
      </el-scrollbar>
  </el-main>
</el-container>
</#macro>

<#function getSunDialog layout >
    <#assign custdialogConf  = util.getStringConf(layout.customDialogjson)>
    <#assign rtn>
    	 <eip-sunDialog
    	  :custdialog='${custdialogConf}'
        />
    </#assign>
    <#return rtn>
</#function>