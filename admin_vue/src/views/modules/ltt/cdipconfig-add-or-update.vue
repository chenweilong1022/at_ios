<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="国家">
        <el-select v-model="dataForm.countryCode" placeholder="国家" clearable>
          <el-option
            v-for="item in countryCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="静态ip" prop="staticIps">
        <el-input   :autosize="{ minRows: 20, maxRows: 20}" type="textarea" v-model="dataForm.staticIps" placeholder="静态ip"></el-input>
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
        countryCodes: [],
        dataForm: {
          id: 0,
          staticIps: '',
          countryCode: null,
          usedCount: '',
          httpPort: '',
          sock5Port: '',
          account: '',
          password: '',
          deleteFlag: '',
          createTime: '',
          updateTime: ''
        },
        dataRule: {
          ip: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          countryCode: [
            { required: true, message: '国家不能为空', trigger: 'blur' }
          ],
          usedCount: [
            { required: true, message: '使用次数不能为空', trigger: 'blur' }
          ],
          httpPort: [
            { required: true, message: '端口不能为空', trigger: 'blur' }
          ],
          sock5Port: [
            { required: true, message: 'sock5的端口不能为空', trigger: 'blur' }
          ],
          account: [
            { required: true, message: '帐号不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '更新时间不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      // 表单提交
      getCountryCodeEnums () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/countryCodes`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.countryCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.getCountryCodeEnums()
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/cdipconfig/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.ip = data.cdipconfig.ip
                this.dataForm.countryCode = data.cdipconfig.countryCode
                this.dataForm.usedCount = data.cdipconfig.usedCount
                this.dataForm.httpPort = data.cdipconfig.httpPort
                this.dataForm.sock5Port = data.cdipconfig.sock5Port
                this.dataForm.account = data.cdipconfig.account
                this.dataForm.password = data.cdipconfig.password
                this.dataForm.deleteFlag = data.cdipconfig.deleteFlag
                this.dataForm.createTime = data.cdipconfig.createTime
                this.dataForm.updateTime = data.cdipconfig.updateTime
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
              url: this.$http.adornUrl(`/ltt/cdipconfig/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'staticIps': this.dataForm.staticIps,
                'countryCode': this.dataForm.countryCode
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
