<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="token" prop="token">
      <el-input v-model="dataForm.token" placeholder="token"></el-input>
    </el-form-item>
    <el-form-item label="使用状态" prop="useFlag">
      <el-input v-model="dataForm.useFlag" placeholder="使用状态"></el-input>
    </el-form-item>
    <el-form-item label="删除标志" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder="删除标志"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="平台" prop="platform">
      <el-input v-model="dataForm.platform" placeholder="平台"></el-input>
    </el-form-item>
    <el-form-item label="分组id" prop="userGroupId">
      <el-input v-model="dataForm.userGroupId" placeholder="分组id"></el-input>
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
          token: '',
          useFlag: '',
          deleteFlag: '',
          createTime: '',
          platform: '',
          userGroupId: ''
        },
        dataRule: {
          token: [
            { required: true, message: 'token不能为空', trigger: 'blur' }
          ],
          useFlag: [
            { required: true, message: '使用状态不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          platform: [
            { required: true, message: '平台不能为空', trigger: 'blur' }
          ],
          userGroupId: [
            { required: true, message: '分组id不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atusertoken/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.token = data.atusertoken.token
                this.dataForm.useFlag = data.atusertoken.useFlag
                this.dataForm.deleteFlag = data.atusertoken.deleteFlag
                this.dataForm.createTime = data.atusertoken.createTime
                this.dataForm.platform = data.atusertoken.platform
                this.dataForm.userGroupId = data.atusertoken.userGroupId
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
              url: this.$http.adornUrl(`/ltt/atusertoken/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'token': this.dataForm.token,
                'useFlag': this.dataForm.useFlag,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'platform': this.dataForm.platform,
                'userGroupId': this.dataForm.userGroupId
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
