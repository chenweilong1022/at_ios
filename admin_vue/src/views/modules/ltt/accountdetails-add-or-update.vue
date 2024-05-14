<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="账户ID，关联账户余额表" prop="accountId">
      <el-input v-model="dataForm.accountId" placeholder="账户ID，关联账户余额表"></el-input>
    </el-form-item>
    <el-form-item label="" prop="sysUserId">
      <el-input v-model="dataForm.sysUserId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="交易类型" prop="transactionType">
      <el-input v-model="dataForm.transactionType" placeholder="交易类型"></el-input>
    </el-form-item>
    <el-form-item label="交易金额，正数表示收入，负数表示支出" prop="amount">
      <el-input v-model="dataForm.amount" placeholder="交易金额，正数表示收入，负数表示支出"></el-input>
    </el-form-item>
    <el-form-item label="交易状态，如1成功、0失败等" prop="status">
      <el-input v-model="dataForm.status" placeholder="交易状态，如1成功、0失败等"></el-input>
    </el-form-item>
    <el-form-item label="交易描述，记录交易的详细信息" prop="description">
      <el-input v-model="dataForm.description" placeholder="交易描述，记录交易的详细信息"></el-input>
    </el-form-item>
    <el-form-item label="交易日期和时间" prop="transactionDate">
      <el-input v-model="dataForm.transactionDate" placeholder="交易日期和时间"></el-input>
    </el-form-item>
    <el-form-item label="操作人" prop="operationUserId">
      <el-input v-model="dataForm.operationUserId" placeholder="操作人"></el-input>
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
        dataForm: {
          transactionId: 0,
          accountId: '',
          sysUserId: '',
          transactionType: '',
          amount: '',
          status: '',
          description: '',
          transactionDate: '',
          operationUserId: ''
        },
        dataRule: {
          accountId: [
            { required: true, message: '账户ID，关联账户余额表不能为空', trigger: 'blur' }
          ],
          sysUserId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          transactionType: [
            { required: true, message: '交易类型不能为空', trigger: 'blur' }
          ],
          amount: [
            { required: true, message: '交易金额，正数表示收入，负数表示支出不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '交易状态，如1成功、0失败等不能为空', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '交易描述，记录交易的详细信息不能为空', trigger: 'blur' }
          ],
          transactionDate: [
            { required: true, message: '交易日期和时间不能为空', trigger: 'blur' }
          ],
          operationUserId: [
            { required: true, message: '操作人不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.transactionId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.transactionId) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/accountdetails/info/${this.dataForm.transactionId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.accountId = data.accountdetails.accountId
                this.dataForm.sysUserId = data.accountdetails.sysUserId
                this.dataForm.transactionType = data.accountdetails.transactionType
                this.dataForm.amount = data.accountdetails.amount
                this.dataForm.status = data.accountdetails.status
                this.dataForm.description = data.accountdetails.description
                this.dataForm.transactionDate = data.accountdetails.transactionDate
                this.dataForm.operationUserId = data.accountdetails.operationUserId
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/accountdetails/${!this.dataForm.transactionId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'transactionId': this.dataForm.transactionId || undefined,
                'accountId': this.dataForm.accountId,
                'sysUserId': this.dataForm.sysUserId,
                'transactionType': this.dataForm.transactionType,
                'amount': this.dataForm.amount,
                'status': this.dataForm.status,
                'description': this.dataForm.description,
                'transactionDate': this.dataForm.transactionDate,
                'operationUserId': this.dataForm.operationUserId
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
