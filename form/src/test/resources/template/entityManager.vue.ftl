<#assign baseUrl><#noparse>${</#noparse>${cfg.system}<#noparse>}</#noparse><#if package.ModuleName??>/${package.ModuleName}</#if>/${table.entityPath}/v1</#assign>
<template>
  <div class="fullheight">
    <ht-table
      @load="loadData"
      :data="data"
      :pageResult="pageResult"
      :selection="true"
      quick-search-props="${table.fields[0].propertyName}"
      :show-export="false"
      :show-custom-column="false"
      ref="htTable"
    >
      <template v-slot:toolbar>
        <el-button-group>
          <el-button size="small" @click="showDialog()" icon="el-icon-plus">添加</el-button>
          <ht-delete-button url="${baseUrl}/" :htTable="$refs.htTable">删除</ht-delete-button>
        </el-button-group>
      </template>
      <template>
        <ht-table-column type="index" width="50" align="center" label="序号" />
        <#list table.fields as field>
        <ht-table-column
          prop="${field.propertyName}"
          label="${field.comment}"
          :sortable="true"
          :show-overflow-tooltip="true"
        >
          <#if field_index == 0  >
          <template v-slot="{row}">
            <el-link
              type="primary"
              @click="showDialog(row.id)"
              title="查看详情"
            >{{row.${field.propertyName}}}</el-link>
          </template>
          </#if>
        </ht-table-column>
        </#list>
      </template>
    </ht-table>
    <ht-sidebar-dialog
      width="28%"
      title="<#if table.comment??>${table.comment}<#else>实体信息</#if>"
      class="sp-manager__dialog"
      :visible="dialogVisible"
      :before-close="beforeCloseDialog"
    >
      <el-form v-form data-vv-scope="${table.entityName}Form">
        <#list table.fields as field>
        <ht-form-item label="${field.comment}">
          <ht-input v-model="${table.entityName}.${field.propertyName}" validate="required" />
        </ht-form-item>
        </#list>
      </el-form>
      <div slot="footer" style="text-align: center">
        <ht-submit-button
          url="${baseUrl}/"
          :model="${table.entityName}"
          :request-method="saveMethod"
          scope-name="${table.entityName}Form"
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
      ${table.entityName}: {},
      saveMethod: "POST"
    };
  },
  mounted() {
    this.$validator = this.$root.$validator;
  },
  methods: {
    showDialog(id) {
      if (id) {
        this.saveMethod = "PUT";
        this.$http.get("${baseUrl}/" + id).then(
          resp => {
            this.${table.entityName} = resp.data;
            this.dialogVisible = true;
          },
          error => {
            reject(error);
          }
        );
      } else {
        this.saveMethod = "POST";
        this.dialogVisible = true;
      }
    },
    beforeCloseDialog() {
      this.${table.entityName} = {};
      this.dialogVisible = false;
    },
    loadData(param, cb) {
      this.$http
        .post("${baseUrl}/query", param)
        .then(
          resp => {
            let response = resp.data;
            this.data = response.rows;
            this.pageResult = {
              page: response.page,
              pageSize: response.pageSize,
              total: response.total
            };
          },
          error => {
            reject(error);
          }
        )
        .finally(() => cb());
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
