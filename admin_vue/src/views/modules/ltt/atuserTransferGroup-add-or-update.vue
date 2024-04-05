<template>
  <el-dialog
    :title="'转移分组'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="账号分组" prop="userGroupId">
        <el-select
          @change="userGroupChangeHandler"
          v-model="dataForm.userGroupId"
          filterable clearable
          remote
          placeholder="选择分组"
          :remote-method="queryUserGroupBySearchWord"
          :loading="loading">
          <el-option
            v-for="item in userGroupOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id">
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
      visible: false,
      userGroupOptions: [],
      userGroupName: '',
      dataForm: {
        ids: [],
        userGroupId: ''
      },
      dataRule: {
        userGroupId: [
          {required: true, message: '分组id不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    userGroupChangeHandler (id) {
      this.userGroupOptions.find((item) => {
       if (item.id === id) {
        this.userGroupName = item.name
       }
      })
    },
    init (ids) {
      this.visible = true
      this.ids = ids
      console.log(ids)
      this.queryUserGroupBySearchWord()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/atuser/updateUserGroup`),
            method: 'post',
            data: this.$http.adornData({
              'ids': this.ids,
              'userGroupId': this.dataForm.userGroupId,
              'userGroupName': this.userGroupName
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
    /*
     根据搜索词，查询用户分组
  */
    queryUserGroupBySearchWord (serchKey) {
      serchKey = serchKey == null ? '' : serchKey + ''
      this.$http({
        url: this.$http.adornUrl(`/ltt/atusergroup/queryByFuzzyName?searchWord=${serchKey}`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.userGroupOptions = data.groupList
        }
      })
    },
  }
}
</script>
