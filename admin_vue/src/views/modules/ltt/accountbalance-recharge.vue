<template>
  <el-dialog
    :title="'充值'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="所属账号" prop="sysUserId">
      <el-select
        v-model="dataForm.sysUserId"
        filterable
        remote :disabled="userDisabled"
        placeholder="请选择账号"
        :remote-method="queryBySearchWord"
        :loading="loading">
        <el-option
          v-for="item in sysUserAccountOptions"
          :key="item.userId"
          :label="item.username"
          :value="item.userId">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="充值金额" prop="amount">
      <el-input v-model="dataForm.amount" placeholder="充值金额"></el-input>
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
        userDisabled: false,
        sysUserAccountOptions: [],
        dataForm: {
          sysUserId: '',
          amount: ''
        },
        dataRule: {
          sysUserId: [
            { required: true, message: '所属账户不能为空', trigger: 'blur' }
          ],
          amount: [
            { required: true, message: '充值金额', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (sysUserId, sysUserName) {
        this.dataForm.sysUserId = null
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (sysUserId) {
            this.dataForm.sysUserId = sysUserId
            this.userDisabled = true
            this.sysUserAccountOptions = [{
              userId: sysUserId,
              username: sysUserName
            }]
          } else {
            this.queryBySearchWord();
          }
        })
      },
      /*
      根据搜索词，查询系统用户
    */
      queryBySearchWord (serchKey) {
        serchKey = serchKey == null ? '' : serchKey + ''
        this.$http({
          url: this.$http.adornUrl(`/sys/user/queryBySearchWord?searchWord=${serchKey}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.sysUserAccountOptions = data.userList
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/accountbalance/changeAccount`),
              method: 'post',
              data: this.$http.adornData({
                'sysUserId': this.dataForm.sysUserId,
                'amount': this.dataForm.amount,
                'transactionType': 1, /** 充值：1 **/
                'description': '账户充值'
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
      }
    }
  }
</script>
