<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="任务名称" prop="taskName">
      <el-input v-model="dataForm.taskName" placeholder="任务名称"></el-input>
    </el-form-item>
    <el-form-item label="账户分组" prop="userGroupId">
      <el-input v-model="dataForm.userGroupId" placeholder="账户分组"></el-input>
    </el-form-item>
    <el-form-item label="数据分组" prop="dataGroupId">
      <el-input v-model="dataForm.dataGroupId" placeholder="数据分组"></el-input>
    </el-form-item>
    <el-form-item label="类型" prop="groupType">
      <el-input v-model="dataForm.groupType" placeholder="类型"></el-input>
    </el-form-item>
    <el-form-item label="加粉总数" prop="addTotalQuantity">
      <el-input v-model="dataForm.addTotalQuantity" placeholder="加粉总数"></el-input>
    </el-form-item>
    <el-form-item label="成功数" prop="successfulQuantity">
      <el-input v-model="dataForm.successfulQuantity" placeholder="成功数"></el-input>
    </el-form-item>
    <el-form-item label="失败数" prop="failuresQuantity">
      <el-input v-model="dataForm.failuresQuantity" placeholder="失败数"></el-input>
    </el-form-item>
    <el-form-item label="状态" prop="taskStatus">
      <el-input v-model="dataForm.taskStatus" placeholder="状态"></el-input>
    </el-form-item>
    <el-form-item label="进度" prop="schedule">
      <el-input v-model="dataForm.schedule" placeholder="进度"></el-input>
    </el-form-item>
    <el-form-item label="更新时间" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="更新时间"></el-input>
    </el-form-item>
    <el-form-item label="加粉数量" prop="addQuantityLimit">
      <el-input v-model="dataForm.addQuantityLimit" placeholder="加粉数量"></el-input>
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
          userGroupId: '',
          dataGroupId: '',
          groupType: '',
          addTotalQuantity: '',
          successfulQuantity: '',
          failuresQuantity: '',
          taskStatus: '',
          schedule: '',
          updateTime: '',
          addQuantityLimit: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          taskName: [
            { required: true, message: '任务名称不能为空', trigger: 'blur' }
          ],
          userGroupId: [
            { required: true, message: '账户分组不能为空', trigger: 'blur' }
          ],
          dataGroupId: [
            { required: true, message: '数据分组不能为空', trigger: 'blur' }
          ],
          groupType: [
            { required: true, message: '类型不能为空', trigger: 'blur' }
          ],
          addTotalQuantity: [
            { required: true, message: '加粉总数不能为空', trigger: 'blur' }
          ],
          successfulQuantity: [
            { required: true, message: '成功数不能为空', trigger: 'blur' }
          ],
          failuresQuantity: [
            { required: true, message: '失败数不能为空', trigger: 'blur' }
          ],
          taskStatus: [
            { required: true, message: '状态不能为空', trigger: 'blur' }
          ],
          schedule: [
            { required: true, message: '进度不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '更新时间不能为空', trigger: 'blur' }
          ],
          addQuantityLimit: [
            { required: true, message: '加粉数量不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atdatatask/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.taskName = data.atdatatask.taskName
                this.dataForm.userGroupId = data.atdatatask.userGroupId
                this.dataForm.dataGroupId = data.atdatatask.dataGroupId
                this.dataForm.groupType = data.atdatatask.groupType
                this.dataForm.addTotalQuantity = data.atdatatask.addTotalQuantity
                this.dataForm.successfulQuantity = data.atdatatask.successfulQuantity
                this.dataForm.failuresQuantity = data.atdatatask.failuresQuantity
                this.dataForm.taskStatus = data.atdatatask.taskStatus
                this.dataForm.schedule = data.atdatatask.schedule
                this.dataForm.updateTime = data.atdatatask.updateTime
                this.dataForm.addQuantityLimit = data.atdatatask.addQuantityLimit
                this.dataForm.deleteFlag = data.atdatatask.deleteFlag
                this.dataForm.createTime = data.atdatatask.createTime
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
              url: this.$http.adornUrl(`/ltt/atdatatask/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'taskName': this.dataForm.taskName,
                'userGroupId': this.dataForm.userGroupId,
                'dataGroupId': this.dataForm.dataGroupId,
                'groupType': this.dataForm.groupType,
                'addTotalQuantity': this.dataForm.addTotalQuantity,
                'successfulQuantity': this.dataForm.successfulQuantity,
                'failuresQuantity': this.dataForm.failuresQuantity,
                'taskStatus': this.dataForm.taskStatus,
                'schedule': this.dataForm.schedule,
                'updateTime': this.dataForm.updateTime,
                'addQuantityLimit': this.dataForm.addQuantityLimit,
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
