<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="头像任务id" prop="usernameTaskId">
      <el-input v-model="dataForm.usernameTaskId" placeholder="头像任务id"></el-input>
    </el-form-item>
    <el-form-item label="账户分组" prop="userGroupId">
      <el-input v-model="dataForm.userGroupId" placeholder="账户分组"></el-input>
    </el-form-item>
    <el-form-item label="昵称分组" prop="usernameGroupId">
      <el-input v-model="dataForm.usernameGroupId" placeholder="昵称分组"></el-input>
    </el-form-item>
    <el-form-item label="账户id" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="账户id"></el-input>
    </el-form-item>
    <el-form-item label="昵称id" prop="usernameId">
      <el-input v-model="dataForm.usernameId" placeholder="昵称id"></el-input>
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
    <el-form-item label="line协议的任务id" prop="lineTaskId">
      <el-input v-model="dataForm.lineTaskId" placeholder="line协议的任务id"></el-input>
    </el-form-item>
    <el-form-item label="line的协议返回信息" prop="msg">
      <el-input v-model="dataForm.msg" placeholder="line的协议返回信息"></el-input>
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
          usernameTaskId: '',
          userGroupId: '',
          usernameGroupId: '',
          userId: '',
          usernameId: '',
          taskStatus: '',
          deleteFlag: '',
          createTime: '',
          lineTaskId: '',
          msg: ''
        },
        dataRule: {
          usernameTaskId: [
            { required: true, message: '头像任务id不能为空', trigger: 'blur' }
          ],
          userGroupId: [
            { required: true, message: '账户分组不能为空', trigger: 'blur' }
          ],
          usernameGroupId: [
            { required: true, message: '昵称分组不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '账户id不能为空', trigger: 'blur' }
          ],
          usernameId: [
            { required: true, message: '昵称id不能为空', trigger: 'blur' }
          ],
          taskStatus: [
            { required: true, message: '任务状态不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          lineTaskId: [
            { required: true, message: 'line协议的任务id不能为空', trigger: 'blur' }
          ],
          msg: [
            { required: true, message: 'line的协议返回信息不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atusernamesubtask/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.usernameTaskId = data.atusernamesubtask.usernameTaskId
                this.dataForm.userGroupId = data.atusernamesubtask.userGroupId
                this.dataForm.usernameGroupId = data.atusernamesubtask.usernameGroupId
                this.dataForm.userId = data.atusernamesubtask.userId
                this.dataForm.usernameId = data.atusernamesubtask.usernameId
                this.dataForm.taskStatus = data.atusernamesubtask.taskStatus
                this.dataForm.deleteFlag = data.atusernamesubtask.deleteFlag
                this.dataForm.createTime = data.atusernamesubtask.createTime
                this.dataForm.lineTaskId = data.atusernamesubtask.lineTaskId
                this.dataForm.msg = data.atusernamesubtask.msg
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
              url: this.$http.adornUrl(`/ltt/atusernamesubtask/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'usernameTaskId': this.dataForm.usernameTaskId,
                'userGroupId': this.dataForm.userGroupId,
                'usernameGroupId': this.dataForm.usernameGroupId,
                'userId': this.dataForm.userId,
                'usernameId': this.dataForm.usernameId,
                'taskStatus': this.dataForm.taskStatus,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'lineTaskId': this.dataForm.lineTaskId,
                'msg': this.dataForm.msg
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
