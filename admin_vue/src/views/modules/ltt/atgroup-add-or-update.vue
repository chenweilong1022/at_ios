<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="groupName">
      <el-input v-model="dataForm.groupName" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="proxy">
      <el-input v-model="dataForm.proxy" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="intervals">
      <el-input v-model="dataForm.intervals" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="roomId">
      <el-input v-model="dataForm.roomId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="chatRoomUrl">
      <el-input v-model="dataForm.chatRoomUrl" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="roomTicketId">
      <el-input v-model="dataForm.roomTicketId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="uploadGroupNumber">
      <el-input v-model="dataForm.uploadGroupNumber" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="currentExecutionsNumber">
      <el-input v-model="dataForm.currentExecutionsNumber" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="successfullyAttractGroupsNumber">
      <el-input v-model="dataForm.successfullyAttractGroupsNumber" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="groupStatus">
      <el-input v-model="dataForm.groupStatus" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="taskId">
      <el-input v-model="dataForm.taskId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="lineRegisterId">
      <el-input v-model="dataForm.lineRegisterId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="addType">
      <el-input v-model="dataForm.addType" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="materialId">
      <el-input v-model="dataForm.materialId" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="materialPhoneType">
      <el-input v-model="dataForm.materialPhoneType" placeholder=""></el-input>
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
          groupName: '',
          proxy: '',
          intervals: '',
          roomId: '',
          chatRoomUrl: '',
          roomTicketId: '',
          uploadGroupNumber: '',
          currentExecutionsNumber: '',
          successfullyAttractGroupsNumber: '',
          groupStatus: '',
          deleteFlag: '',
          createTime: '',
          taskId: '',
          lineRegisterId: '',
          addType: '',
          materialId: '',
          materialPhoneType: ''
        },
        dataRule: {
          groupName: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          proxy: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          intervals: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          roomId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          chatRoomUrl: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          roomTicketId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          uploadGroupNumber: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          currentExecutionsNumber: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          successfullyAttractGroupsNumber: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          groupStatus: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          taskId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          lineRegisterId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          addType: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          materialId: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          materialPhoneType: [
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
              url: this.$http.adornUrl(`/ltt/atgroup/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.groupName = data.atgroup.groupName
                this.dataForm.proxy = data.atgroup.proxy
                this.dataForm.intervals = data.atgroup.intervals
                this.dataForm.roomId = data.atgroup.roomId
                this.dataForm.chatRoomUrl = data.atgroup.chatRoomUrl
                this.dataForm.roomTicketId = data.atgroup.roomTicketId
                this.dataForm.uploadGroupNumber = data.atgroup.uploadGroupNumber
                this.dataForm.currentExecutionsNumber = data.atgroup.currentExecutionsNumber
                this.dataForm.successfullyAttractGroupsNumber = data.atgroup.successfullyAttractGroupsNumber
                this.dataForm.groupStatus = data.atgroup.groupStatus
                this.dataForm.deleteFlag = data.atgroup.deleteFlag
                this.dataForm.createTime = data.atgroup.createTime
                this.dataForm.taskId = data.atgroup.taskId
                this.dataForm.lineRegisterId = data.atgroup.lineRegisterId
                this.dataForm.addType = data.atgroup.addType
                this.dataForm.materialId = data.atgroup.materialId
                this.dataForm.materialPhoneType = data.atgroup.materialPhoneType
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
              url: this.$http.adornUrl(`/ltt/atgroup/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'groupName': this.dataForm.groupName,
                'proxy': this.dataForm.proxy,
                'intervals': this.dataForm.intervals,
                'roomId': this.dataForm.roomId,
                'chatRoomUrl': this.dataForm.chatRoomUrl,
                'roomTicketId': this.dataForm.roomTicketId,
                'uploadGroupNumber': this.dataForm.uploadGroupNumber,
                'currentExecutionsNumber': this.dataForm.currentExecutionsNumber,
                'successfullyAttractGroupsNumber': this.dataForm.successfullyAttractGroupsNumber,
                'groupStatus': this.dataForm.groupStatus,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'taskId': this.dataForm.taskId,
                'lineRegisterId': this.dataForm.lineRegisterId,
                'addType': this.dataForm.addType,
                'materialId': this.dataForm.materialId,
                'materialPhoneType': this.dataForm.materialPhoneType
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
