<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="任务名称" prop="taskName">
      <el-input v-model="dataForm.taskName" placeholder="任务名称"></el-input>
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
          groupType: '',
          addTotalQuantity: '',
          successfulQuantity: '',
          failuresQuantity: '',
          taskStatus: '',
          schedule: '',
          updateTime: '',
          deleteFlag: '',
          createTime: '',
          sysUserId: ''
        },
        dataRule: {
          taskName: [
            { required: true, message: '任务名称不能为空', trigger: 'blur' }
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
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          sysUserId: [
            { required: true, message: '管理账户id不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atgrouptask/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.taskName = data.atgrouptask.taskName
                this.dataForm.groupType = data.atgrouptask.groupType
                this.dataForm.addTotalQuantity = data.atgrouptask.addTotalQuantity
                this.dataForm.successfulQuantity = data.atgrouptask.successfulQuantity
                this.dataForm.failuresQuantity = data.atgrouptask.failuresQuantity
                this.dataForm.taskStatus = data.atgrouptask.taskStatus
                this.dataForm.schedule = data.atgrouptask.schedule
                this.dataForm.updateTime = data.atgrouptask.updateTime
                this.dataForm.deleteFlag = data.atgrouptask.deleteFlag
                this.dataForm.createTime = data.atgrouptask.createTime
                this.dataForm.sysUserId = data.atgrouptask.sysUserId
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
              url: this.$http.adornUrl(`/ltt/atgrouptask/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'taskName': this.dataForm.taskName,
                'groupType': this.dataForm.groupType,
                'addTotalQuantity': this.dataForm.addTotalQuantity,
                'successfulQuantity': this.dataForm.successfulQuantity,
                'failuresQuantity': this.dataForm.failuresQuantity,
                'taskStatus': this.dataForm.taskStatus,
                'schedule': this.dataForm.schedule,
                'updateTime': this.dataForm.updateTime,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'sysUserId': this.dataForm.sysUserId
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
