<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="分组名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="分组名称"></el-input>
      </el-form-item>
      <el-form-item label="分组类型" prop="name">
        <el-select
          v-model="dataForm.userGroupType"
          class="m-2" clearable
          placeholder="账户状态"
          style="width: 240px">
          <el-option
            v-for="item in atUserGroupTypeCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key"
          />
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
        visible: false,
        atUserGroupTypeCodes: [],
        dataForm: {
          id: 0,
          name: '',
          userGroupType: null,
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          name: [
            { required: true, message: '分组名称不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.dataForm.userGroupType = 1
        this.getAtUserGroupType()
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atusergroup/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.atUserGroup.name
                this.dataForm.userGroupType = data.atUserGroup.userGroupType
                this.dataForm.deleteFlag = data.atUserGroup.deleteFlag
                this.dataForm.createTime = data.atUserGroup.createTime
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
              url: this.$http.adornUrl(`/ltt/atusergroup/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'userGroupType': this.dataForm.userGroupType,
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
      },
      getAtUserGroupType () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getAtUserGroupType`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.atUserGroupTypeCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      }
    }
  }
</script>
