<template>
  <el-dialog
    :title="'清理ip'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="140px">

      <el-form-item label="清理ip对应的国家" prop="countryCode">
        <el-select v-model="dataForm.countryCode" placeholder="清理ip对应的国家" clearable>
          <el-option
            v-for="item in countryCodes"
            :key="item.key"
            :label="item.value2"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="cleanIpByCountryCode()">清理ip</el-button>
      </el-form-item>

      <el-form-item label="清理黑名单ip剩余小时" prop="expireHours">
        <el-input v-model="dataForm.expireHours" placeholder="清理黑名单ip剩余小时"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="cleanInvalidIp()">清理黑名单ip</el-button>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      countryCodes: [],
      dataForm: {
        countryCode: null,
        expireHours: null
      },
      dataRule: {
      }
    }
  },
  methods: {
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
    init () {
      this.visible = true
      this.getCountryCodeEnums()
    },
    // 清理ip
    cleanIpByCountryCode () {
      console.log(this.dataForm.countryCode)
      if (this.dataForm.countryCode === null) {
        this.$message.error('请输入需要清理的ip国家')
        return
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/cdlineregister/cleanIpByCountryCode`),
            method: 'get',
            params: this.$http.adornParams({
              'countryCode': this.dataForm.countryCode
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功，正在清理中，请等待',
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
    // 清理ip
    cleanInvalidIp () {
      if (this.dataForm.expireHours === null) {
        this.$message.error('请输入剩余小时')
        return
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/cdlineregister/cleanInvalidIp`),
            method: 'get',
            params: this.$http.adornParams({
              'expireHours': this.dataForm.expireHours
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功，正在清理中，请等待',
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
