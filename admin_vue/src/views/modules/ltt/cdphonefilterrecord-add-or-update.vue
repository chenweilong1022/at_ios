<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="任务状态" prop="taskStatus">
      <el-input v-model="dataForm.taskStatus" placeholder="任务状态"></el-input>
    </el-form-item>
    <el-form-item label="删除标志" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder="删除标志"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="修改时间" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="修改时间"></el-input>
    </el-form-item>
    <el-form-item label="总数" prop="totalCount">
      <el-input v-model="dataForm.totalCount" placeholder="总数"></el-input>
    </el-form-item>
    <el-form-item label="成功数量" prop="successCount">
      <el-input v-model="dataForm.successCount" placeholder="成功数量"></el-input>
    </el-form-item>
    <el-form-item label="失败数量" prop="failCount">
      <el-input v-model="dataForm.failCount" placeholder="失败数量"></el-input>
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
          recordId: 0,
          taskStatus: '',
          deleteFlag: '',
          createTime: '',
          updateTime: '',
          totalCount: '',
          successCount: '',
          failCount: ''
        },
        dataRule: {
          taskStatus: [
            { required: true, message: '任务状态不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '修改时间不能为空', trigger: 'blur' }
          ],
          totalCount: [
            { required: true, message: '总数不能为空', trigger: 'blur' }
          ],
          successCount: [
            { required: true, message: '成功数量不能为空', trigger: 'blur' }
          ],
          failCount: [
            { required: true, message: '失败数量不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.recordId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.recordId) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/cdphonefilterrecord/info/${this.dataForm.recordId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.taskStatus = data.cdphonefilterrecord.taskStatus
                this.dataForm.deleteFlag = data.cdphonefilterrecord.deleteFlag
                this.dataForm.createTime = data.cdphonefilterrecord.createTime
                this.dataForm.updateTime = data.cdphonefilterrecord.updateTime
                this.dataForm.totalCount = data.cdphonefilterrecord.totalCount
                this.dataForm.successCount = data.cdphonefilterrecord.successCount
                this.dataForm.failCount = data.cdphonefilterrecord.failCount
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
              url: this.$http.adornUrl(`/ltt/cdphonefilterrecord/${!this.dataForm.recordId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'recordId': this.dataForm.recordId || undefined,
                'taskStatus': this.dataForm.taskStatus,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'updateTime': this.dataForm.updateTime,
                'totalCount': this.dataForm.totalCount,
                'successCount': this.dataForm.successCount,
                'failCount': this.dataForm.failCount
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
