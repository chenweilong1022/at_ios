<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="昵称分组id" prop="usernameGroupId">
      <el-input v-model="dataForm.usernameGroupId" placeholder="昵称分组id"></el-input>
    </el-form-item>
    <el-form-item label="昵称" prop="username">
      <el-input v-model="dataForm.username" placeholder="昵称"></el-input>
    </el-form-item>
    <el-form-item label="使用标识" prop="useFlag">
      <el-input v-model="dataForm.useFlag" placeholder="使用标识"></el-input>
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
          usernameGroupId: '',
          username: '',
          useFlag: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          usernameGroupId: [
            { required: true, message: '昵称分组id不能为空', trigger: 'blur' }
          ],
          username: [
            { required: true, message: '昵称不能为空', trigger: 'blur' }
          ],
          useFlag: [
            { required: true, message: '使用标识不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atusername/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.usernameGroupId = data.atUsername.usernameGroupId
                this.dataForm.username = data.atUsername.username
                this.dataForm.useFlag = data.atUsername.useFlag
                this.dataForm.deleteFlag = data.atUsername.deleteFlag
                this.dataForm.createTime = data.atUsername.createTime
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
              url: this.$http.adornUrl(`/ltt/atusername/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'usernameGroupId': this.dataForm.usernameGroupId,
                'username': this.dataForm.username,
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
