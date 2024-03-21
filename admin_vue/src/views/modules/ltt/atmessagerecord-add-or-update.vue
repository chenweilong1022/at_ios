<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="客服id" prop="customerServiceId">
      <el-input v-model="dataForm.customerServiceId" placeholder="客服id"></el-input>
    </el-form-item>
    <el-form-item label="所属客服" prop="customerService">
      <el-input v-model="dataForm.customerService" placeholder="所属客服"></el-input>
    </el-form-item>
    <el-form-item label="消息人" prop="messageUserId">
      <el-input v-model="dataForm.messageUserId" placeholder="消息人"></el-input>
    </el-form-item>
    <el-form-item label="消息uid" prop="messageUid">
      <el-input v-model="dataForm.messageUid" placeholder="消息uid"></el-input>
    </el-form-item>
    <el-form-item label="消息类型" prop="messageType">
      <el-input v-model="dataForm.messageType" placeholder="消息类型"></el-input>
    </el-form-item>
    <el-form-item label="消息内容" prop="messageContent">
      <el-input v-model="dataForm.messageContent" placeholder="消息内容"></el-input>
    </el-form-item>
    <el-form-item label="收发" prop="sendReceive">
      <el-input v-model="dataForm.sendReceive" placeholder="收发"></el-input>
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
          customerServiceId: '',
          customerService: '',
          messageUserId: '',
          messageUid: '',
          messageType: '',
          messageContent: '',
          sendReceive: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          customerServiceId: [
            { required: true, message: '客服id不能为空', trigger: 'blur' }
          ],
          customerService: [
            { required: true, message: '所属客服不能为空', trigger: 'blur' }
          ],
          messageUserId: [
            { required: true, message: '消息人不能为空', trigger: 'blur' }
          ],
          messageUid: [
            { required: true, message: '消息uid不能为空', trigger: 'blur' }
          ],
          messageType: [
            { required: true, message: '消息类型不能为空', trigger: 'blur' }
          ],
          messageContent: [
            { required: true, message: '消息内容不能为空', trigger: 'blur' }
          ],
          sendReceive: [
            { required: true, message: '收发不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atmessagerecord/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.customerServiceId = data.atmessagerecord.customerServiceId
                this.dataForm.customerService = data.atmessagerecord.customerService
                this.dataForm.messageUserId = data.atmessagerecord.messageUserId
                this.dataForm.messageUid = data.atmessagerecord.messageUid
                this.dataForm.messageType = data.atmessagerecord.messageType
                this.dataForm.messageContent = data.atmessagerecord.messageContent
                this.dataForm.sendReceive = data.atmessagerecord.sendReceive
                this.dataForm.deleteFlag = data.atmessagerecord.deleteFlag
                this.dataForm.createTime = data.atmessagerecord.createTime
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
              url: this.$http.adornUrl(`/ltt/atmessagerecord/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'customerServiceId': this.dataForm.customerServiceId,
                'customerService': this.dataForm.customerService,
                'messageUserId': this.dataForm.messageUserId,
                'messageUid': this.dataForm.messageUid,
                'messageType': this.dataForm.messageType,
                'messageContent': this.dataForm.messageContent,
                'sendReceive': this.dataForm.sendReceive,
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
