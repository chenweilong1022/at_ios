<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="140px">
      <el-form-item label="任务名称" prop="taskName">
        <el-input v-model="dataForm.taskName" placeholder="任务名称"></el-input>
      </el-form-item>
      <el-form-item label="注册数量（成功）" prop="totalAmount">
        <el-input v-model="dataForm.totalAmount" placeholder="注册数量（成功）"></el-input>
      </el-form-item>
      <el-form-item label="线程数" prop="numberThreads">
        <el-input v-model="dataForm.numberThreads" placeholder="线程数"></el-input>
      </el-form-item>
      <el-form-item label="是否真机">
        <el-radio-group v-model="dataForm.realMachine">
          <el-radio :label="1">否</el-radio>
          <el-radio :label="2">是</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否自动补充">
        <el-radio-group v-model="dataForm.fillUp">
          <el-radio :label="0">补充</el-radio>
          <el-radio :label="1">不补充</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="注册国家">
        <el-select v-model="dataForm.countryCode" placeholder="注册国家" clearable>
          <el-option
            v-for="item in countryCodes"
            :key="item.key"
            :label="item.value2"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>

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

      <el-form-item v-if="8101 === dataForm.countryCode" label="卡数据" prop="sfData">
        <el-input   :autosize="{ minRows: 20, maxRows: 20}" type="textarea" v-model="dataForm.sfData" placeholder="卡数据"></el-input>
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
        taskName: null,
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
        proxyIp: 1,
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
      this.getCountryCodeEnums()
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
              this.dataForm.taskName = data.cdregistertask.taskName
              this.dataForm.totalAmount = data.cdregistertask.totalAmount
              this.dataForm.numberThreads = data.cdregistertask.numberThreads
              this.dataForm.numberRegistered = data.cdregistertask.numberRegistered
              this.dataForm.numberSuccesses = data.cdregistertask.numberSuccesses
              this.dataForm.numberFailures = data.cdregistertask.numberFailures
              this.dataForm.registrationStatus = data.cdregistertask.registrationStatus
              this.dataForm.deleteFlag = data.cdregistertask.deleteFlag
              this.dataForm.createTime = data.cdregistertask.createTime
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
              'taskName': this.dataForm.taskName,
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
