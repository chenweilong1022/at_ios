<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="totalAmount">
      <el-input v-model="dataForm.totalAmount" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="numberThreads">
      <el-input v-model="dataForm.numberThreads" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="numberRegistered">
      <el-input v-model="dataForm.numberRegistered" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="numberSuccesses">
      <el-input v-model="dataForm.numberSuccesses" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="numberFailures">
      <el-input v-model="dataForm.numberFailures" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="registrationStatus">
      <el-input v-model="dataForm.registrationStatus" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder=""></el-input>
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
          id: 0,
          totalAmount: '',
          numberThreads: '',
          numberRegistered: '',
          numberSuccesses: '',
          numberFailures: '',
          registrationStatus: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          totalAmount: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          numberThreads: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          numberRegistered: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          numberSuccesses: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          numberFailures: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          registrationStatus: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/cdregistertask/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.totalAmount = data.cdregistertask.totalAmount
                this.dataForm.numberThreads = data.cdregistertask.numberThreads
                this.dataForm.numberRegistered = data.cdregistertask.numberRegistered
                this.dataForm.numberSuccesses = data.cdregistertask.numberSuccesses
                this.dataForm.numberFailures = data.cdregistertask.numberFailures
                this.dataForm.registrationStatus = data.cdregistertask.registrationStatus
                this.dataForm.deleteFlag = data.cdregistertask.deleteFlag
                this.dataForm.createTime = data.cdregistertask.createTime
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
              url: this.$http.adornUrl(`/ltt/cdregistertask/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'totalAmount': this.dataForm.totalAmount,
                'numberThreads': this.dataForm.numberThreads,
                'numberRegistered': this.dataForm.numberRegistered,
                'numberSuccesses': this.dataForm.numberSuccesses,
                'numberFailures': this.dataForm.numberFailures,
                'registrationStatus': this.dataForm.registrationStatus,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime
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
