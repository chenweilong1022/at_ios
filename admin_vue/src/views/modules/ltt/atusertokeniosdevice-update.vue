<template>
  <el-dialog
    :title="'设置设备名称'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="设备名称" prop="deviceName">
        <el-input v-model="dataForm.deviceName" placeholder="设备名称"></el-input>
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
        deviceId: null,
        deviceName: null
      },
      dataRule: {
        deviceName: [
          {required: true, message: '设备名称不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.visible = true
      this.dataForm.deviceId = id
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/atusertokenios/updateDeviceName`),
            method: 'post',
            data: this.$http.adornData({
              'deviceId': this.dataForm.deviceId,
              'deviceName': this.dataForm.deviceName
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
    }
  }
}
</script>
