<#import "function.ftl" as func>
<#assign comment=model.tabComment>
<#assign class=model.variables.class>
<#assign package=model.variables.package>
<#assign classVar=model.variables.classVar>
<#assign system=vars.system>
<#assign companyEn=vars.companyEn>
<#assign commonList=model.commonList>
<#assign pkModel=model.pkModel>
<#assign pk=func.getPk(model) >
<#assign pkVar=func.convertUnderLine(pk) >
<#assign searchProps1=func.convertUnderLine(commonList[0].columnName)>
<#assign searchProps2=func.convertUnderLine(commonList[1].columnName)>
<template>
  <div class="fullheight">
    <ht-table
      @load="loadData"
      :data="data"
      :pageResult="pageResult"
      :selection="true"
      quick-search-props="${searchProps1},${searchProps2}"
      :show-export="false"
      :show-custom-column="false"
      ref="htTable"
    >
      <template v-slot:toolbar>
        <el-button-group>
          <el-button size="small" @click="showDialog()" icon="el-icon-plus">添加</el-button>
          <ht-delete-button url="<#noparse>${</#noparse>${system}<#noparse>}</#noparse>/${system}/${classVar}/v1/remove" :htTable="$refs.htTable">删除</ht-delete-button>
        </el-button-group>
      </template>
      <template>
        <ht-table-column type="index" width="50" align="center" label="序号" />
       	<#list commonList as col>
                	<#assign colName=func.convertUnderLine(col.columnName)/>
        <ht-table-column prop="${colName}" label="${col.getComment()}" :sortable="true" :show-overflow-tooltip="true">
          <#if col_index == 0  >
          <template v-slot="{row}">
            <el-link type="primary" @click="showDialog(row.id)" title="查看详情">{{row.${colName}}}</el-link>
          </template>
          </#if>
        </ht-table-column>
        </#list>
      </template>
    </ht-table>
    <ht-sidebar-dialog
      width="28%"
      title="${comment}"
      class="sp-manager__dialog"
      :visible="dialogVisible"
      :before-close="beforeCloseDialog"
    >
      <el-form v-form data-vv-scope="${classVar}Form">
      <#list commonList as col>
                	<#assign colName=func.convertUnderLine(col.columnName)/>
        <ht-form-item label="${col.getComment()}">
          <ht-input v-model="${classVar}.${colName}" validate="required" />
        </ht-form-item>
       </#list>
      </el-form>
      <div slot="footer" style="text-align: center">
        <ht-submit-button
          url="<#noparse>${</#noparse>${system}<#noparse>}</#noparse>/${system}/${classVar}/v1/save"
          :model="${classVar}"
          scope-name="${classVar}Form"
          @after-save-data="afterSaveData"
        >{{$t("eip.common.save")}}</ht-submit-button>
        <el-button @click="beforeCloseDialog">{{$t("eip.common.cancel")}}</el-button>
      </div>
    </ht-sidebar-dialog>
  </div>
</template>
<script>

export default {
  data() {
    return {
      dialogVisible: false,
      data: [],
      pageResult: {
        page: 1,
        pageSize: 50,
        total: 0
      },
      ${classVar}: {}
    };
  },
  mounted() {
    this.$validator = this.$root.$validator;
  },
  methods: {
    showDialog(id) {
      if (id) {
    	  this.$http.get("<#noparse>${</#noparse>${system}<#noparse>}</#noparse>/${system}/${classVar}/v1/getJson?id="+`<#noparse>${id}</#noparse>`).then(resp => {
              this.${classVar} = resp.data;
              this.dialogVisible = true;
          }, error => {
              reject(error);
          })
      } else {
        this.dialogVisible = true;
      }
    },
    beforeCloseDialog() {
      this.${classVar} = {  };
      this.dialogVisible = false;
    },
    loadData(param, cb) {
    	this.$http.post("<#noparse>${</#noparse>${system}<#noparse>}</#noparse>/${system}/${classVar}/v1/listJson", param).then(resp => {
            let response = resp.data;
            this.data = response.rows;
            this.pageResult = {
              page: response.page,
              pageSize: response.pageSize,
              total: response.total
            };
        }, error => {
            reject(error);
        }).finally(() => cb());
    },
    afterSaveData() {
      setTimeout(() => {
        this.beforeCloseDialog();
        this.$refs.htTable.load();
      }, 500);
    }
  }
};
</script>

<style lang="scss" scoped>
.sp-manager__dialog /deep/ > .el-dialog > .el-dialog__body {
  height: calc(100% - 170px);
}
</style>
