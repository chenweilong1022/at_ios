<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="头像" prop="avatar">
      <el-input v-model="dataForm.avatar" placeholder="头像"></el-input>
    </el-form-item>
    <el-form-item label="国家" prop="nation">
      <el-input v-model="dataForm.nation" placeholder="国家"></el-input>
    </el-form-item>
    <el-form-item label="电话" prop="telephone">
      <el-input v-model="dataForm.telephone" placeholder="电话"></el-input>
    </el-form-item>
    <el-form-item label="昵称" prop="nickName">
      <el-input v-model="dataForm.nickName" placeholder="昵称"></el-input>
    </el-form-item>
    <el-form-item label="好友数量" prop="numberFriends">
      <el-input v-model="dataForm.numberFriends" placeholder="好友数量"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="password">
      <el-input v-model="dataForm.password" placeholder="密码"></el-input>
    </el-form-item>
    <el-form-item label="分组id" prop="userGroupId">
      <el-input v-model="dataForm.userGroupId" placeholder="分组id"></el-input>
    </el-form-item>
    <el-form-item label="分组名称" prop="userGroupName">
      <el-input v-model="dataForm.userGroupName" placeholder="分组名称"></el-input>
    </el-form-item>
    <el-form-item label="所属客服id" prop="customerServiceId">
      <el-input v-model="dataForm.customerServiceId" placeholder="所属客服id"></el-input>
    </el-form-item>
    <el-form-item label="所属客服" prop="customerService">
      <el-input v-model="dataForm.customerService" placeholder="所属客服"></el-input>
    </el-form-item>
    <el-form-item label="删除标志" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder="删除标志"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="用户tokenid" prop="userTokenId">
      <el-input v-model="dataForm.userTokenId" placeholder="用户tokenid"></el-input>
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
          avatar: '',
          nation: '',
          telephone: '',
          nickName: '',
          numberFriends: '',
          password: '',
          userGroupId: '',
          userGroupName: '',
          customerServiceId: '',
          customerService: '',
          deleteFlag: '',
          createTime: '',
          userTokenId: ''
        },
        dataRule: {
          avatar: [
            { required: true, message: '头像不能为空', trigger: 'blur' }
          ],
          nation: [
            { required: true, message: '国家不能为空', trigger: 'blur' }
          ],
          telephone: [
            { required: true, message: '电话不能为空', trigger: 'blur' }
          ],
          nickName: [
            { required: true, message: '昵称不能为空', trigger: 'blur' }
          ],
          numberFriends: [
            { required: true, message: '好友数量不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          userGroupId: [
            { required: true, message: '分组id不能为空', trigger: 'blur' }
          ],
          userGroupName: [
            { required: true, message: '分组名称不能为空', trigger: 'blur' }
          ],
          customerServiceId: [
            { required: true, message: '所属客服id不能为空', trigger: 'blur' }
          ],
          customerService: [
            { required: true, message: '所属客服不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          userTokenId: [
            { required: true, message: '用户tokenid不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/ltt/atuser/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.avatar = data.atuser.avatar
                this.dataForm.nation = data.atuser.nation
                this.dataForm.telephone = data.atuser.telephone
                this.dataForm.nickName = data.atuser.nickName
                this.dataForm.numberFriends = data.atuser.numberFriends
                this.dataForm.password = data.atuser.password
                this.dataForm.userGroupId = data.atuser.userGroupId
                this.dataForm.userGroupName = data.atuser.userGroupName
                this.dataForm.customerServiceId = data.atuser.customerServiceId
                this.dataForm.customerService = data.atuser.customerService
                this.dataForm.deleteFlag = data.atuser.deleteFlag
                this.dataForm.createTime = data.atuser.createTime
                this.dataForm.userTokenId = data.atuser.userTokenId
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
              url: this.$http.adornUrl(`/ltt/atuser/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'avatar': this.dataForm.avatar,
                'nation': this.dataForm.nation,
                'telephone': this.dataForm.telephone,
                'nickName': this.dataForm.nickName,
                'numberFriends': this.dataForm.numberFriends,
                'password': this.dataForm.password,
                'userGroupId': this.dataForm.userGroupId,
                'userGroupName': this.dataForm.userGroupName,
                'customerServiceId': this.dataForm.customerServiceId,
                'customerService': this.dataForm.customerService,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'userTokenId': this.dataForm.userTokenId
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
