<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="number">
      <el-input v-model="dataForm.number" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="pkey">
      <el-input v-model="dataForm.pkey" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="time">
      <el-input v-model="dataForm.time" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="country">
      <el-input v-model="dataForm.country" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="countrycode">
      <el-input v-model="dataForm.countrycode" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="other">
      <el-input v-model="dataForm.other" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="com">
      <el-input v-model="dataForm.com" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="phone">
      <el-input v-model="dataForm.phone" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="code">
      <el-input v-model="dataForm.code" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="phoneStatus">
      <el-input v-model="dataForm.phoneStatus" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="subtasksId">
      <el-input v-model="dataForm.subtasksId" placeholder=""></el-input>
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
          number: '',
          pkey: '',
          time: '',
          country: '',
          countrycode: '',
          other: '',
          com: '',
          phone: '',
          code: '',
          phoneStatus: '',
          deleteFlag: '',
          createTime: '',
          subtasksId: ''
        },
        dataRule: {
          number: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          pkey: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          time: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          country: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          countrycode: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          other: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          com: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          phone: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          code: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          phoneStatus: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          subtasksId: [
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
              url: this.$http.adornUrl(`/ltt/cdgetphone/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.number = data.cdgetphone.number
                this.dataForm.pkey = data.cdgetphone.pkey
                this.dataForm.time = data.cdgetphone.time
                this.dataForm.country = data.cdgetphone.country
                this.dataForm.countrycode = data.cdgetphone.countrycode
                this.dataForm.other = data.cdgetphone.other
                this.dataForm.com = data.cdgetphone.com
                this.dataForm.phone = data.cdgetphone.phone
                this.dataForm.code = data.cdgetphone.code
                this.dataForm.phoneStatus = data.cdgetphone.phoneStatus
                this.dataForm.deleteFlag = data.cdgetphone.deleteFlag
                this.dataForm.createTime = data.cdgetphone.createTime
                this.dataForm.subtasksId = data.cdgetphone.subtasksId
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
              url: this.$http.adornUrl(`/ltt/cdgetphone/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'number': this.dataForm.number,
                'pkey': this.dataForm.pkey,
                'time': this.dataForm.time,
                'country': this.dataForm.country,
                'countrycode': this.dataForm.countrycode,
                'other': this.dataForm.other,
                'com': this.dataForm.com,
                'phone': this.dataForm.phone,
                'code': this.dataForm.code,
                'phoneStatus': this.dataForm.phoneStatus,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'subtasksId': this.dataForm.subtasksId
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
