<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="" prop="ip">
      <el-input v-model="dataForm.ip" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="tokenPhone">
      <el-input v-model="dataForm.tokenPhone" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="lzCountry">
      <el-input v-model="dataForm.lzCountry" placeholder=""></el-input>
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
          ip: '',
          deleteFlag: '',
          createTime: '',
          tokenPhone: '',
          lzCountry: ''
        },
        dataRule: {
          ip: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          tokenPhone: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          lzCountry: [
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
              url: this.$http.adornUrl(`/ltt/cdlineipproxy/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.ip = data.cdlineipproxy.ip
                this.dataForm.deleteFlag = data.cdlineipproxy.deleteFlag
                this.dataForm.createTime = data.cdlineipproxy.createTime
                this.dataForm.tokenPhone = data.cdlineipproxy.tokenPhone
                this.dataForm.lzCountry = data.cdlineipproxy.lzCountry
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
              url: this.$http.adornUrl(`/ltt/cdlineipproxy/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'ip': this.dataForm.ip,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'tokenPhone': this.dataForm.tokenPhone,
                'lzCountry': this.dataForm.lzCountry
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
