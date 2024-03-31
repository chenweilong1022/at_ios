<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="deviceId" prop="deviceId">
      <el-input v-model="dataForm.deviceId" placeholder="deviceId"></el-input>
    </el-form-item>
    <el-form-item label="phoneNumber" prop="phoneNumber">
      <el-input v-model="dataForm.phoneNumber" placeholder="phoneNumber"></el-input>
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
          country: '',
          bundleId: '',
          appUserId: '',
          userName: '',
          phoneNumber: '',
          mid: '',
          token: '',
          iosToken: '',
          useFlag: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          country: [
            { required: true, message: 'country不能为空', trigger: 'blur' }
          ],
          bundleId: [
            { required: true, message: 'bundleId不能为空', trigger: 'blur' }
          ],
          appUserId: [
            { required: true, message: 'appUserId不能为空', trigger: 'blur' }
          ],
          userName: [
            { required: true, message: 'userName不能为空', trigger: 'blur' }
          ],
          phoneNumber: [
            { required: true, message: 'phoneNumber不能为空', trigger: 'blur' }
          ],
          mid: [
            { required: true, message: 'mid不能为空', trigger: 'blur' }
          ],
          token: [
            { required: true, message: 'token不能为空', trigger: 'blur' }
          ],
          iosToken: [
            { required: true, message: 'ios_token不能为空', trigger: 'blur' }
          ],
          useFlag: [
            { required: true, message: '使用状态不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atusertokenios/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.country = data.atusertokenios.country
                this.dataForm.bundleId = data.atusertokenios.bundleId
                this.dataForm.appUserId = data.atusertokenios.appUserId
                this.dataForm.userName = data.atusertokenios.userName
                this.dataForm.phoneNumber = data.atusertokenios.phoneNumber
                this.dataForm.mid = data.atusertokenios.mid
                this.dataForm.token = data.atusertokenios.token
                this.dataForm.iosToken = data.atusertokenios.iosToken
                this.dataForm.useFlag = data.atusertokenios.useFlag
                this.dataForm.deleteFlag = data.atusertokenios.deleteFlag
                this.dataForm.createTime = data.atusertokenios.createTime
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
              url: this.$http.adornUrl(`/ltt/atusertokenios/backUp`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'country': this.dataForm.country,
                'bundleId': this.dataForm.bundleId,
                'appUserId': this.dataForm.appUserId,
                'userName': this.dataForm.userName,
                'phoneNumber': this.dataForm.phoneNumber,
                'mid': this.dataForm.mid,
                'token': this.dataForm.token,
                'iosToken': this.dataForm.iosToken,
                'useFlag': this.dataForm.useFlag,
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
