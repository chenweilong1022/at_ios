<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">

      <el-form-item label="参数名">
        <el-select v-model="type" placeholder="类型" clearable>
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="ip服务">
        <el-select v-model="ip" @change="changeIpHandler" placeholder="ip服务" clearable>
          <el-option
            v-for="item in ipOptions"
            :key="item.label"
            :label="item.label"
            :value="item.label">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="代理">
        <el-select v-model="proxy" placeholder="代理" clearable>
          <el-option
            v-for="item in proxyOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="linehttp" prop="lineBaseHttp"  v-if="type === 2">
        <el-input v-model="dataForm.lineBaseHttp" placeholder="linehttp"></el-input>
      </el-form-item>
      <el-form-item label="lineAb" prop="lineAb"  v-if="type === 2">
        <el-input v-model="dataForm.lineAb" placeholder="lineAb"></el-input>
      </el-form-item>
      <el-form-item label="lineAppVersion" prop="lineAppVersion"  v-if="type === 2">
        <el-input v-model="dataForm.lineAppVersion" placeholder="lineAppVersion"></el-input>
      </el-form-item>
      <el-form-item label="lineTxtToken" prop="lineTxtToken"  v-if="type === 2">
        <el-input v-model="dataForm.lineTxtToken" placeholder="lineTxtToken"></el-input>
      </el-form-item>

      <el-form-item label="火狐狸接口" prop="firefoxBaseUrl"  v-if="type === 2">
        <el-input v-model="dataForm.firefoxBaseUrl" placeholder="火狐狸接口"></el-input>
      </el-form-item>
      <el-form-item label="火狐狸token" prop="firefoxToken"  v-if="type === 2">
        <el-input v-model="dataForm.firefoxToken" placeholder="火狐狸token"></el-input>
      </el-form-item>
      <el-form-item label="火狐狸项目id配置" prop="firefoxIid"  v-if="type === 2">
        <el-input v-model="dataForm.firefoxIid" placeholder="火狐狸项目id配置"></el-input>
      </el-form-item>
      <el-form-item label="火狐狸国家配置" prop="firefoxCountry"  v-if="type === 2">
        <el-input v-model="dataForm.firefoxCountry" placeholder="火狐狸国家配置"></el-input>
      </el-form-item>
      <el-form-item label="LINE国家配置" prop="firefoxCountry1"  v-if="type === 2">
        <el-input v-model="dataForm.firefoxCountry1" placeholder="LINE国家配置"></el-input>
      </el-form-item>
      <el-form-item label="静态代理使用次数" prop="proxyUseCount" v-if="type === 2">
        <el-input v-model="dataForm.proxyUseCount" placeholder="静态代理使用次数"></el-input>
      </el-form-item>
      <el-form-item label="四方验证码URL" prop="sfGetPhoneCodeUrl">
        <el-input v-model="dataForm.sfGetPhoneCodeUrl" placeholder="四方验证码URL"></el-input>
      </el-form-item>
      <el-form-item label="四方时区" prop="sfTimeZone" >
<!--        <el-input v-model="dataForm.sfTimeZone" placeholder="四方时区"></el-input>-->
        <el-select v-model="dataForm.sfTimeZone" placeholder="四方时区" clearable>
          <el-option
            v-for="item in options1"
            :key="item.value"
            :label="item.label"
            :value="item.value">
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
      proxy: null,
      type: 2,
      ip: 'web服务器',
      platform: 1,
      visible: false,
      projectDataList: [],
      ipOptions: [
        {
          value: 1,
          label: 'web服务器'
        },
        {
          value: 2,
          label: 'http://202.79.171.146:8880/'
        },
        {
          value: 3,
          label: 'http://216.83.53.90:8880/'
        }
      ],
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
      options1: [
        {
          value: 1,
          label: '中国时间'
        },
        {
          value: 2,
          label: '日本时间'
        }
      ],
      options: [
        {
          value: 1,
          label: '云存储'
        },
        {
          value: 2,
          label: '项目配置'
        }
      ],
      dataForm: {
        id: 0,
        sfTimeZone: null,
        paramKey: '',
        paramValue: '',
        firefoxBaseUrl: '',
        firefoxToken: '',
        firefoxIid: '',
        firefoxCountry: '',
        firefoxCountry1: '',
        proxyUseCount: '',
        sfGetPhoneCodeUrl: '',
        lineBaseHttp: '',

        lineAb: '',
        lineAppVersion: '',
        lineTxtToken: ''
      },
      dataRule: {
        paramKey: [
          { required: true, message: '参数名不能为空', trigger: 'blur' }
        ],
        paramValue: [
          { required: true, message: '参数值不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    changeIpHandler (){
      this.init(this.dataForm.id)
    },
    init (id) {
      this.dataForm.id = id || 0
      this.visible = true
      var ipPre = this.ip === 'web服务器' ? null : this.ip
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/sys/config/info/${this.dataForm.id}`, ipPre),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.paramKey = data.config.paramKey
              this.dataForm.paramValue = data.config.paramValue
              this.dataForm.remark = data.config.remark
              this.dataForm.lineBaseHttp = data.config.lineBaseHttp
              this.dataForm.lineAb = data.config.lineAb
              this.dataForm.lineAppVersion = data.config.lineAppVersion
              this.dataForm.lineTxtToken = data.config.lineTxtToken
              this.dataForm.firefoxBaseUrl = data.config.firefoxBaseUrl
              this.dataForm.firefoxToken = data.config.firefoxToken
              this.dataForm.firefoxIid = data.config.firefoxIid
              this.dataForm.firefoxCountry = data.config.firefoxCountry
              this.dataForm.firefoxCountry1 = data.config.firefoxCountry1
              this.dataForm.proxyUseCount = data.config.proxyUseCount
              this.dataForm.sfGetPhoneCodeUrl = data.config.sfGetPhoneCodeUrl
              this.dataForm.sfTimeZone = data.config.sfTimeZone
              this.proxy = data.config.proxy
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          var ipPre = this.ip === 'web服务器' ? null : this.ip
          this.$http({
            url: this.$http.adornUrl(`/sys/config/${!this.dataForm.id ? 'save' : 'update'}`,ipPre),
            method: 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'paramKey': this.dataForm.paramKey,
              'paramValue': this.dataForm.paramValue,
              'lineBaseHttp': this.dataForm.lineBaseHttp,
              'lineAb': this.dataForm.lineAb,
              'lineAppVersion': this.dataForm.lineAppVersion,
              'lineTxtToken': this.dataForm.lineTxtToken,
              'firefoxBaseUrl': this.dataForm.firefoxBaseUrl,
              'firefoxToken': this.dataForm.firefoxToken,
              'firefoxIid': this.dataForm.firefoxIid,
              'firefoxCountry': this.dataForm.firefoxCountry,
              'firefoxCountry1': this.dataForm.firefoxCountry1,
              'proxyUseCount': this.dataForm.proxyUseCount,
              'sfGetPhoneCodeUrl': this.dataForm.sfGetPhoneCodeUrl,
              'sfTimeZone': this.dataForm.sfTimeZone,
              'proxy': this.proxy,
              'type': this.type,
              'remark': this.dataForm.remark
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
