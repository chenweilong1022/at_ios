<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="平台" prop="platform">
      <el-select
        v-model="dataForm.platform"
        class="m-2"
        placeholder="Select"
        size="large"
        style="width: 240px">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"/>
      </el-select>
    </el-form-item>
      <el-form-item label="账号分组" prop="userGroupId">
        <el-select
          v-model="dataForm.userGroupId"
          class="m-2"
          placeholder="Select"
          size="large"
          style="width: 240px">
          <el-option
            v-for="item in userGroupData"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="数据上传">
        <el-upload
          class="upload-demo"
          action="http://localhost:8880/app/file/upload"
          :on-success="handleAvatarSuccess">
          <el-button size="small" type="primary">点击上传</el-button>
        </el-upload>
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
        options: [
          {
            value: 1,
            label: '安卓'
          },
          {
            value: 2,
            label: 'IOS'
          }
        ],
        dataForm: {
          id: 0,
          token: '',
          useFlag: '',
          txtUrl: '',
          deleteFlag: '',
          createTime: '',
          platform: '',
          userGroupId: ''
        },
        userGroupData: [],
        dataRule: {
          token: [
            { required: true, message: 'token不能为空', trigger: 'blur' }
          ],
          useFlag: [
            { required: true, message: '使用状态不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          platform: [
            { required: true, message: '平台不能为空', trigger: 'blur' }
          ],
          userGroupId: [
            { required: true, message: '分组id不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      handleAvatarSuccess (res, file) {
        this.dataForm.txtUrl = res.data
      },
      init () {
        this.visible = true
        this.getDataList()
      },
      // 获取数据列表
      getDataList () {
        this.$http({
          url: this.$http.adornUrl('/ltt/atusergroup/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 111
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.userGroupData = data.page.list
          } else {
            this.userGroupData = []
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atusertoken/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'token': this.dataForm.token,
                'useFlag': this.dataForm.useFlag,
                'txtUrl': this.dataForm.txtUrl,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'platform': this.dataForm.platform,
                'userGroupId': this.dataForm.userGroupId
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
