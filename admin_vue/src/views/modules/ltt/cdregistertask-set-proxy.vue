<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="140px">
      <el-form-item label="注册代理ip">
        <el-select v-model="dataForm.proxyIp" placeholder="注册代理ip" clearable>
          <el-option
            v-for="item in proxyOptions"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
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
      proxyOptions: [
        {
          value: 1,
          label: 'lunaproxy'
        },
        {
          value: 2,
          label: 'ip2world'
        },
        {
          value: 3,
          label: '静态代理'
        }
      ],
      type: 2,
      proxy: '',
      visible: false,
      dataFormConfig: {
        id: 0,
        paramKey: '',
        paramValue: '',
        firefoxBaseUrl: '',
        firefoxToken: '',
        firefoxIid: '',
        firefoxCountry: '',
        firefoxCountry1: '',
        proxyUseCount: '',
        lineBaseHttp: '',
        lineAb: '',
        lineAppVersion: '',
        lineTxtToken: ''
      },
      countryCodes: [],
      dataForm: {
        id: 0,
        totalAmount: '',
        numberThreads: '50',
        numberRegistered: '',
        numberSuccesses: '',
        numberFailures: '',
        registrationStatus: '',
        realMachine: 1,
        deleteFlag: '',
        sfData: '',
        countryCode: 66,
        proxyIp: null,
        fillUp: 1,
        createTime: ''
      },
      dataRule: {
        totalAmount: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        numberThreads: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        numberRegistered: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        numberSuccesses: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        numberFailures: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        registrationStatus: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        deleteFlag: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 表单提交
    getProxyEnums () {
      this.$http({
        url: this.$http.adornUrl(`/app/enums/proxyStatus`),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.proxyOptions = data.data
        } else {
          this.$message.error(data.msg)
        }
      })
    },
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
      this.getProxyEnums()
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/cdregistertask/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.totalAmount = data.cdRegisterTask.totalAmount
              this.dataForm.numberThreads = data.cdRegisterTask.numberThreads
              this.dataForm.numberRegistered = data.cdRegisterTask.numberRegistered
              this.dataForm.numberSuccesses = data.cdRegisterTask.numberSuccesses
              this.dataForm.numberFailures = data.cdRegisterTask.numberFailures
              this.dataForm.registrationStatus = data.cdRegisterTask.registrationStatus
              this.dataForm.deleteFlag = data.cdRegisterTask.deleteFlag
              this.dataForm.proxyIp = data.cdRegisterTask.proxyIp
              this.dataForm.createTime = data.cdRegisterTask.createTime
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
            url: this.$http.adornUrl(`/ltt/cdregistertask/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'totalAmount': this.dataForm.totalAmount,
              'numberThreads': this.dataForm.numberThreads,
              'numberRegistered': this.dataForm.numberRegistered,
              'numberSuccesses': this.dataForm.numberSuccesses,
              'numberFailures': this.dataForm.numberFailures,
              'registrationStatus': this.dataForm.registrationStatus,
              'fillUp': this.dataForm.fillUp,
              'realMachine': this.dataForm.realMachine,
              'countryCode': this.dataForm.countryCode,
              'proxyIp': this.dataForm.proxyIp,
              'sfData': this.dataForm.sfData,
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
