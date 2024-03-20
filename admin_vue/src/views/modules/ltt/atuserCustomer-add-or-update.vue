<template>
  <el-dialog
    :title="'分配客服'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="选择客服" prop="customerServiceId">
        <el-select
          @change="userCustomerChangeHandler"
          v-model="dataForm.customerServiceId"
          filterable clearable
          remote
          placeholder="选择客服"
          :remote-method="queryCustomerByFuzzyName"
          :loading="loading">
          <el-option
            v-for="item in customerUserOptions"
            :key="item.userId"
            :label="item.nickname"
            :value="item.userId">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      visible: false,
      customerUserOptions: [],
      userCustomerName: '',
      dataForm: {
        ids: [],
        customerServiceId: ''
      },
      dataRule: {
        customerServiceId: [
          {required: true, message: '选择客服不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    userCustomerChangeHandler (id) {
      this.customerUserOptions.find((item) => {
        this.userCustomerName = item.nickname
      })
    },
    init (ids) {
      this.visible = true
      this.ids = ids
      console.log(ids)
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/atuser/updateUserCustomer`),
            method: 'post',
            data: this.$http.adornData({
              'ids': this.ids,
              'customerServiceId': this.dataForm.customerServiceId,
              'customerService': this.userCustomerName
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    },
    /*
        根据搜索词，查询客服
     */
    queryCustomerByFuzzyName (serchKey) {
      serchKey = serchKey == null ? '' : serchKey + ''
      this.$http({
        url: this.$http.adornUrl(`/ltt/customeruser/queryCustomerByFuzzyName?key=${serchKey}`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.customerUserOptions = [{userId: 0, nickname: '未分配'},...data.customerList]
        }
      })
    }
  }
}
</script>
