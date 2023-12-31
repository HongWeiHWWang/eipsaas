<#macro subTable field>
    <#assign subTablePath=field.children[0].path>
<table class="form-table" cellspacing="0" cellpadding="0" border="0">
    <tbody v-for="(item, index) in data.${subTablePath}" :key="index" :class="{'transition':index==transitionIndex}" :data-index="index" data-subname="data.${subTablePath}">
    <#list field.trGroupSup as group>
    <tr v-permit="${maxCol*2}" @click="transitionIndex = -1">
          <#list group.childrens as field>
              <th style="border-left: 1px solid #f9f9f9;" width="${(maxCol==1)?string('20%','10%')}" v-if="${getPermission(field,2)}!='n'">
                  <span v-if="${getPermission(field,2)}=='b'">*</span>
                  ${field.desc}：
              </th>
              <td width="${util.getWidth(maxCol, group.count)}" ${util.getColspan(maxCol, group.count, field_index + 1, false)} v-if="${getPermission(field,2)}!='n'">
                  <@input field=field type=2 ganged=ganged/>
              </td>
          </#list>
    </tr>
    </#list>
    <tr v-if="isView">
        <th colspan="15" class="group-th" v-if="permission.table.${field.tableName}.del" style="padding: 0 10px;">
            <el-button size="small" type="text" icon="el-icon-delete" @click="deleteRow('${subTablePath}',item)">{{$t('common.delete')}}</el-button>
            <el-button size="small" type="text" icon="el-icon-tickets" v-if="'${field.relation}'!='onetoone'" @click="copy(data.${subTablePath},item)">{{$t('common.copy')}}</el-button>
            <el-button size="small" type="text" icon="el-icon-arrow-up" v-if="'${field.relation}'!='onetoone'" @click="up(index,data.${subTablePath}),transitionIndex = index-1">{{$t('common.moveUp')}}</el-button>
            <el-button size="small" type="text" icon="el-icon-arrow-down" v-if="'${field.relation}'!='onetoone'" @click="down(index,data.${subTablePath}),transitionIndex = index+1">{{$t('common.moveDown')}}</el-button>
        </th>
    </tr>
    </tbody>

    <#if field.relation!='onetoone'>
      <tfoot v-if="isView" @click="transitionIndex = -1">
      <tr v-if="permission.table.${field.tableName}.add">
          <td colspan="15">
              <el-button size="small" type="text" icon="el-icon-plus" @click="addSubTab('data.${subTablePath}')">{{$t('common.add')}}</el-button>
          </td>
      </tr>
      </tfoot>
    </#if>
    <#if field.relation=='onetoone'>
      <tfoot v-if="isView" @click="transitionIndex = -1" v-show="'${field.relation}'=='onetoone' && data.${subTablePath}.length<1">
      <tr v-if="permission.table.${field.tableName}.add">
          <td colspan="15">
              <el-button size="small" type="text" icon="el-icon-plus" @click="addSubTab('data.${subTablePath}')">{{$t('common.add')}}</el-button>
          </td>
      </tr>
      </tfoot>
    </#if>

</table>
</#macro>