<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="头像任务id" prop="avatarTaskId">
      <el-input v-model="dataForm.avatarTaskId" placeholder="头像任务id"></el-input>
    </el-form-item>
    <el-form-item label="账户分组" prop="userGroupId">
      <el-input v-model="dataForm.userGroupId" placeholder="账户分组"></el-input>
    </el-form-item>
    <el-form-item label="头像分组" prop="avatarGroupId">
      <el-input v-model="dataForm.avatarGroupId" placeholder="头像分组"></el-input>
    </el-form-item>
    <el-form-item label="账户id" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="账户id"></el-input>
    </el-form-item>
    <el-form-item label="头像id" prop="avatarId">
      <el-input v-model="dataForm.avatarId" placeholder="头像id"></el-input>
    </el-form-item>
    <el-form-item label="任务状态" prop="taskStatus">
      <el-input v-model="dataForm.taskStatus" placeholder="任务状态"></el-input>
    </el-form-item>
    <el-form-item label="删除标志" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder="删除标志"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
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
          avatarTaskId: '',
          userGroupId: '',
          avatarGroupId: '',
          userId: '',
          avatarId: '',
          taskStatus: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          avatarTaskId: [
            { required: true, message: '头像任务id不能为空', trigger: 'blur' }
          ],
          userGroupId: [
            { required: true, message: '账户分组不能为空', trigger: 'blur' }
          ],
          avatarGroupId: [
            { required: true, message: '头像分组不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '账户id不能为空', trigger: 'blur' }
          ],
          avatarId: [
            { required: true, message: '头像id不能为空', trigger: 'blur' }
          ],
          taskStatus: [
            { required: true, message: '任务状态不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atavatarsubtask/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.avatarTaskId = data.atavatarsubtask.avatarTaskId
                this.dataForm.userGroupId = data.atavatarsubtask.userGroupId
                this.dataForm.avatarGroupId = data.atavatarsubtask.avatarGroupId
                this.dataForm.userId = data.atavatarsubtask.userId
                this.dataForm.avatarId = data.atavatarsubtask.avatarId
                this.dataForm.taskStatus = data.atavatarsubtask.taskStatus
                this.dataForm.deleteFlag = data.atavatarsubtask.deleteFlag
                this.dataForm.createTime = data.atavatarsubtask.createTime
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
              url: this.$http.adornUrl(`/ltt/atavatarsubtask/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'avatarTaskId': this.dataForm.avatarTaskId,
                'userGroupId': this.dataForm.userGroupId,
                'avatarGroupId': this.dataForm.avatarGroupId,
                'userId': this.dataForm.userId,
                'avatarId': this.dataForm.avatarId,
                'taskStatus': this.dataForm.taskStatus,
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
