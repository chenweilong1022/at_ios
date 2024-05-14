<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="dataForm.nickname" placeholder="昵称" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input v-model="dataForm.username" placeholder="登录帐号" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password" :class="{ 'is-required': !dataForm.id }">
      <el-input v-model="dataForm.password" type="password" placeholder="密码" autocomplete="off"></el-input>
    </el-form-item>
      <el-form-item label="确认密码" prop="comfirmPassword" :class="{ 'is-required': !dataForm.id }">
        <el-input v-model="dataForm.comfirmPassword" type="password" placeholder="确认密码" autocomplete="off"></el-input>
      </el-form-item>
<!--    <el-form-item label="是否开启发送翻译  0：否   1：是" prop="sendTranslate">-->
<!--      <el-input v-model="dataForm.sendTranslate" placeholder="是否开启发送翻译  0：否   1：是"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="发送翻译语言" prop="sendLanguage">-->
<!--      <el-input v-model="dataForm.sendLanguage" placeholder="发送翻译语言"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="是否开启接收翻译  0：否   1：是" prop="receiveTranslate">-->
<!--      <el-input v-model="dataForm.receiveTranslate" placeholder="是否开启接收翻译  0：否   1：是"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="发送接收语言" prop="receiveLanguage">-->
<!--      <el-input v-model="dataForm.receiveLanguage" placeholder="发送接收语言"></el-input>-->
<!--    </el-form-item>-->
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
      var validatePassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          callback(new Error('密码不能为空'))
        } else {
          callback()
        }
      }
      var validateComfirmPassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          callback(new Error('确认密码不能为空'))
        } else if (this.dataForm.password !== value) {
          callback(new Error('确认密码与密码输入不一致'))
        } else {
          callback()
        }
      }
      return {
        visible: false,
        dataForm: {
          userId: 0,
          username: '',
          nickname: '',
          password: '',
          comfirmPassword: '',
          salt: '',
          email: '',
          mobile: '',
          status: '',
          onlineStatus: '',
          createUserId: '',
          createTime: '',
          updateTime: '',
          sendTranslate: '',
          sendLanguage: '',
          receiveTranslate: '',
          receiveLanguage: ''
        },
        dataRule: {
          username: [
            { required: true, message: '用户名不能为空', trigger: 'blur' }
          ],
          nickname: [
            { required: true, message: '昵称不能为空', trigger: 'blur' }
          ],
          password: [
            { validator: validatePassword, trigger: 'blur' }
          ],
          comfirmPassword: [
            { validator: validateComfirmPassword, trigger: 'blur' }
          ],
          // sendTranslate: [
          //   { required: true, message: '是否开启发送翻译  0：否   1：是不能为空', trigger: 'blur' }
          // ],
          // sendLanguage: [
          //   { required: true, message: '发送翻译语言不能为空', trigger: 'blur' }
          // ],
          // receiveTranslate: [
          //   { required: true, message: '是否开启接收翻译  0：否   1：是不能为空', trigger: 'blur' }
          // ],
          // receiveLanguage: [
          //   { required: true, message: '发送接收语言不能为空', trigger: 'blur' }
          // ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.userId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.userId) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/customeruser/info/${this.dataForm.userId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.username = data.customeruser.username
                this.dataForm.nickname = data.customeruser.nickname
                // this.dataForm.sendTranslate = data.customeruser.sendTranslate
                // this.dataForm.sendLanguage = data.customeruser.sendLanguage
                // this.dataForm.receiveTranslate = data.customeruser.receiveTranslate
                // this.dataForm.receiveLanguage = data.customeruser.receiveLanguage
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
              url: this.$http.adornUrl(`/ltt/customeruser/${!this.dataForm.userId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'userId': this.dataForm.userId || undefined,
                'username': this.dataForm.username,
                'nickname': this.dataForm.nickname,
                'password': this.dataForm.password,
                // 'email': this.dataForm.email,
                // 'mobile': this.dataForm.mobile,
                // 'status': this.dataForm.status,
                // 'onlineStatus': this.dataForm.onlineStatus,
                // 'createUserId': this.dataForm.createUserId,
                // 'createTime': this.dataForm.createTime,
                // 'updateTime': this.dataForm.updateTime,
                // 'sendTranslate': this.dataForm.sendTranslate,
                // 'sendLanguage': this.dataForm.sendLanguage,
                // 'receiveTranslate': this.dataForm.receiveTranslate,
                // 'receiveLanguage': this.dataForm.receiveLanguage
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
