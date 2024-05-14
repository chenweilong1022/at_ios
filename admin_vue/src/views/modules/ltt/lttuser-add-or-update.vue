<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="昵称" prop="nickname">
      <el-input v-model="dataForm.nickname" placeholder="昵称"></el-input>
    </el-form-item>
    <el-form-item label="性别" prop="sex">
      <el-input v-model="dataForm.sex" placeholder="性别"></el-input>
    </el-form-item>
    <el-form-item label="性别描述" prop="sexDesc">
      <el-input v-model="dataForm.sexDesc" placeholder="性别描述"></el-input>
    </el-form-item>
    <el-form-item label="区号" prop="countryCode">
      <el-input v-model="dataForm.countryCode" placeholder="区号"></el-input>
    </el-form-item>
    <el-form-item label="语言" prop="language">
      <el-input v-model="dataForm.language" placeholder="语言"></el-input>
    </el-form-item>
    <el-form-item label="国家" prop="country">
      <el-input v-model="dataForm.country" placeholder="国家"></el-input>
    </el-form-item>
    <el-form-item label="省" prop="province">
      <el-input v-model="dataForm.province" placeholder="省"></el-input>
    </el-form-item>
    <el-form-item label="市" prop="city">
      <el-input v-model="dataForm.city" placeholder="市"></el-input>
    </el-form-item>
    <el-form-item label="微信头像" prop="headimgUrl">
      <el-input v-model="dataForm.headimgUrl" placeholder="微信头像"></el-input>
    </el-form-item>
    <el-form-item label="openId" prop="openid">
      <el-input v-model="dataForm.openid" placeholder="openId"></el-input>
    </el-form-item>
    <el-form-item label="token" prop="token">
      <el-input v-model="dataForm.token" placeholder="token"></el-input>
    </el-form-item>
    <el-form-item label="累计消费金额" prop="money">
      <el-input v-model="dataForm.money" placeholder="累计消费金额"></el-input>
    </el-form-item>
    <el-form-item label="累计使用次数" prop="number">
      <el-input v-model="dataForm.number" placeholder="累计使用次数"></el-input>
    </el-form-item>
    <el-form-item label="用户绑定的手机号（国外手机号会有区号）" prop="phoneNumber">
      <el-input v-model="dataForm.phoneNumber" placeholder="用户绑定的手机号（国外手机号会有区号）"></el-input>
    </el-form-item>
    <el-form-item label="没有区号的手机号" prop="mobile">
      <el-input v-model="dataForm.mobile" placeholder="没有区号的手机号"></el-input>
    </el-form-item>
    <el-form-item label="手机品牌" prop="brand">
      <el-input v-model="dataForm.brand" placeholder="手机品牌"></el-input>
    </el-form-item>
    <el-form-item label="手机型号" prop="model">
      <el-input v-model="dataForm.model" placeholder="手机型号"></el-input>
    </el-form-item>
    <el-form-item label="微信版本号" prop="version">
      <el-input v-model="dataForm.version" placeholder="微信版本号"></el-input>
    </el-form-item>
    <el-form-item label="操作系统版本" prop="system">
      <el-input v-model="dataForm.system" placeholder="操作系统版本"></el-input>
    </el-form-item>
    <el-form-item label="客户端平台" prop="platform">
      <el-input v-model="dataForm.platform" placeholder="客户端平台"></el-input>
    </el-form-item>
    <el-form-item label="客户端基础库版本" prop="sdkVersion">
      <el-input v-model="dataForm.sdkVersion" placeholder="客户端基础库版本"></el-input>
    </el-form-item>
    <el-form-item label="ip地址" prop="ip">
      <el-input v-model="dataForm.ip" placeholder="ip地址"></el-input>
    </el-form-item>
    <el-form-item label="纬度" prop="latitude">
      <el-input v-model="dataForm.latitude" placeholder="纬度"></el-input>
    </el-form-item>
    <el-form-item label="经度" prop="longitude">
      <el-input v-model="dataForm.longitude" placeholder="经度"></el-input>
    </el-form-item>
    <el-form-item label="1:公众号2:小程序" prop="source">
      <el-input v-model="dataForm.source" placeholder="1:公众号2:小程序"></el-input>
    </el-form-item>
    <el-form-item label="1:未授权2:已授权" prop="isAuth">
      <el-input v-model="dataForm.isAuth" placeholder="1:未授权2:已授权"></el-input>
    </el-form-item>
    <el-form-item label="1:未授权2:已授权" prop="isPhone">
      <el-input v-model="dataForm.isPhone" placeholder="1:未授权2:已授权"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="修改时间" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="修改时间"></el-input>
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
          nickname: '',
          sex: '',
          sexDesc: '',
          countryCode: '',
          language: '',
          country: '',
          province: '',
          city: '',
          headimgUrl: '',
          openid: '',
          token: '',
          money: '',
          number: '',
          phoneNumber: '',
          mobile: '',
          brand: '',
          model: '',
          version: '',
          system: '',
          platform: '',
          sdkVersion: '',
          ip: '',
          latitude: '',
          longitude: '',
          source: '',
          isAuth: '',
          isPhone: '',
          createTime: '',
          updateTime: ''
        },
        dataRule: {
          nickname: [
            { required: true, message: '昵称不能为空', trigger: 'blur' }
          ],
          sex: [
            { required: true, message: '性别不能为空', trigger: 'blur' }
          ],
          sexDesc: [
            { required: true, message: '性别描述不能为空', trigger: 'blur' }
          ],
          countryCode: [
            { required: true, message: '区号不能为空', trigger: 'blur' }
          ],
          language: [
            { required: true, message: '语言不能为空', trigger: 'blur' }
          ],
          country: [
            { required: true, message: '国家不能为空', trigger: 'blur' }
          ],
          province: [
            { required: true, message: '省不能为空', trigger: 'blur' }
          ],
          city: [
            { required: true, message: '市不能为空', trigger: 'blur' }
          ],
          headimgUrl: [
            { required: true, message: '微信头像不能为空', trigger: 'blur' }
          ],
          openid: [
            { required: true, message: 'openId不能为空', trigger: 'blur' }
          ],
          token: [
            { required: true, message: 'token不能为空', trigger: 'blur' }
          ],
          money: [
            { required: true, message: '累计消费金额不能为空', trigger: 'blur' }
          ],
          number: [
            { required: true, message: '累计使用次数不能为空', trigger: 'blur' }
          ],
          phoneNumber: [
            { required: true, message: '用户绑定的手机号（国外手机号会有区号）不能为空', trigger: 'blur' }
          ],
          mobile: [
            { required: true, message: '没有区号的手机号不能为空', trigger: 'blur' }
          ],
          brand: [
            { required: true, message: '手机品牌不能为空', trigger: 'blur' }
          ],
          model: [
            { required: true, message: '手机型号不能为空', trigger: 'blur' }
          ],
          version: [
            { required: true, message: '微信版本号不能为空', trigger: 'blur' }
          ],
          system: [
            { required: true, message: '操作系统版本不能为空', trigger: 'blur' }
          ],
          platform: [
            { required: true, message: '客户端平台不能为空', trigger: 'blur' }
          ],
          sdkVersion: [
            { required: true, message: '客户端基础库版本不能为空', trigger: 'blur' }
          ],
          ip: [
            { required: true, message: 'ip地址不能为空', trigger: 'blur' }
          ],
          latitude: [
            { required: true, message: '纬度不能为空', trigger: 'blur' }
          ],
          longitude: [
            { required: true, message: '经度不能为空', trigger: 'blur' }
          ],
          source: [
            { required: true, message: '1:公众号2:小程序不能为空', trigger: 'blur' }
          ],
          isAuth: [
            { required: true, message: '1:未授权2:已授权不能为空', trigger: 'blur' }
          ],
          isPhone: [
            { required: true, message: '1:未授权2:已授权不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '修改时间不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/lttuser/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.nickname = data.lttuser.nickname
                this.dataForm.sex = data.lttuser.sex
                this.dataForm.sexDesc = data.lttuser.sexDesc
                this.dataForm.countryCode = data.lttuser.countryCode
                this.dataForm.language = data.lttuser.language
                this.dataForm.country = data.lttuser.country
                this.dataForm.province = data.lttuser.province
                this.dataForm.city = data.lttuser.city
                this.dataForm.headimgUrl = data.lttuser.headimgUrl
                this.dataForm.openid = data.lttuser.openid
                this.dataForm.token = data.lttuser.token
                this.dataForm.money = data.lttuser.money
                this.dataForm.number = data.lttuser.number
                this.dataForm.phoneNumber = data.lttuser.phoneNumber
                this.dataForm.mobile = data.lttuser.mobile
                this.dataForm.brand = data.lttuser.brand
                this.dataForm.model = data.lttuser.model
                this.dataForm.version = data.lttuser.version
                this.dataForm.system = data.lttuser.system
                this.dataForm.platform = data.lttuser.platform
                this.dataForm.sdkVersion = data.lttuser.sdkVersion
                this.dataForm.ip = data.lttuser.ip
                this.dataForm.latitude = data.lttuser.latitude
                this.dataForm.longitude = data.lttuser.longitude
                this.dataForm.source = data.lttuser.source
                this.dataForm.isAuth = data.lttuser.isAuth
                this.dataForm.isPhone = data.lttuser.isPhone
                this.dataForm.createTime = data.lttuser.createTime
                this.dataForm.updateTime = data.lttuser.updateTime
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
              url: this.$http.adornUrl(`/ltt/lttuser/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'nickname': this.dataForm.nickname,
                'sex': this.dataForm.sex,
                'sexDesc': this.dataForm.sexDesc,
                'countryCode': this.dataForm.countryCode,
                'language': this.dataForm.language,
                'country': this.dataForm.country,
                'province': this.dataForm.province,
                'city': this.dataForm.city,
                'headimgUrl': this.dataForm.headimgUrl,
                'openid': this.dataForm.openid,
                'token': this.dataForm.token,
                'money': this.dataForm.money,
                'number': this.dataForm.number,
                'phoneNumber': this.dataForm.phoneNumber,
                'mobile': this.dataForm.mobile,
                'brand': this.dataForm.brand,
                'model': this.dataForm.model,
                'version': this.dataForm.version,
                'system': this.dataForm.system,
                'platform': this.dataForm.platform,
                'sdkVersion': this.dataForm.sdkVersion,
                'ip': this.dataForm.ip,
                'latitude': this.dataForm.latitude,
                'longitude': this.dataForm.longitude,
                'source': this.dataForm.source,
                'isAuth': this.dataForm.isAuth,
                'isPhone': this.dataForm.isPhone,
                'createTime': this.dataForm.createTime,
                'updateTime': this.dataForm.updateTime
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
