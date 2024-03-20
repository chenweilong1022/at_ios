<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="任务名称" prop="taskName">
      <el-input v-model="dataForm.taskName" placeholder="任务名称"></el-input>
    </el-form-item>
    <el-form-item label="任务状态" prop="taskStatus">
      <el-input v-model="dataForm.taskStatus" placeholder="任务状态"></el-input>
    </el-form-item>
    <el-form-item label="进度" prop="schedule">
      <el-input v-model="dataForm.schedule" placeholder="进度"></el-input>
    </el-form-item>
    <el-form-item label="账户分组" prop="userGroupId">
      <el-input v-model="dataForm.userGroupId" placeholder="账户分组"></el-input>
    </el-form-item>
    <el-form-item label="昵称分组" prop="usernameGroupId">
      <el-input v-model="dataForm.usernameGroupId" placeholder="昵称分组"></el-input>
    </el-form-item>
    <el-form-item label="执行数量" prop="executionQuantity">
      <el-input v-model="dataForm.executionQuantity" placeholder="执行数量"></el-input>
    </el-form-item>
    <el-form-item label="成功数量" prop="successfulQuantity">
      <el-input v-model="dataForm.successfulQuantity" placeholder="成功数量"></el-input>
    </el-form-item>
    <el-form-item label="失败数量" prop="failuresQuantity">
      <el-input v-model="dataForm.failuresQuantity" placeholder="失败数量"></el-input>
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
          taskName: '',
          taskStatus: '',
          schedule: '',
          userGroupId: '',
          usernameGroupId: '',
          executionQuantity: '',
          successfulQuantity: '',
          failuresQuantity: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          taskName: [
            { required: true, message: '任务名称不能为空', trigger: 'blur' }
          ],
          taskStatus: [
            { required: true, message: '任务状态不能为空', trigger: 'blur' }
          ],
          schedule: [
            { required: true, message: '进度不能为空', trigger: 'blur' }
          ],
          userGroupId: [
            { required: true, message: '账户分组不能为空', trigger: 'blur' }
          ],
          usernameGroupId: [
            { required: true, message: '昵称分组不能为空', trigger: 'blur' }
          ],
          executionQuantity: [
            { required: true, message: '执行数量不能为空', trigger: 'blur' }
          ],
          successfulQuantity: [
            { required: true, message: '成功数量不能为空', trigger: 'blur' }
          ],
          failuresQuantity: [
            { required: true, message: '失败数量不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atusernametask/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.taskName = data.atusernametask.taskName
                this.dataForm.taskStatus = data.atusernametask.taskStatus
                this.dataForm.schedule = data.atusernametask.schedule
                this.dataForm.userGroupId = data.atusernametask.userGroupId
                this.dataForm.usernameGroupId = data.atusernametask.usernameGroupId
                this.dataForm.executionQuantity = data.atusernametask.executionQuantity
                this.dataForm.successfulQuantity = data.atusernametask.successfulQuantity
                this.dataForm.failuresQuantity = data.atusernametask.failuresQuantity
                this.dataForm.deleteFlag = data.atusernametask.deleteFlag
                this.dataForm.createTime = data.atusernametask.createTime
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
              url: this.$http.adornUrl(`/ltt/atusernametask/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'taskName': this.dataForm.taskName,
                'taskStatus': this.dataForm.taskStatus,
                'schedule': this.dataForm.schedule,
                'userGroupId': this.dataForm.userGroupId,
                'usernameGroupId': this.dataForm.usernameGroupId,
                'executionQuantity': this.dataForm.executionQuantity,
                'successfulQuantity': this.dataForm.successfulQuantity,
                'failuresQuantity': this.dataForm.failuresQuantity,
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
