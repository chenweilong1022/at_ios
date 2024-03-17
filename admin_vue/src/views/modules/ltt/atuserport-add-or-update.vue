<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="端口数量" prop="portNum">
        <el-input v-model="dataForm.portNum" placeholder="端口数量"></el-input>
      </el-form-item>
      <!--    <el-form-item label="管理账户id" prop="sysUserId">-->
      <!--      <el-input v-model="dataForm.sysUserId" placeholder="管理账户id"></el-input>-->
      <!--    </el-form-item>-->
      <el-form-item class="block" label="过期时间" prop="expireTime">
        <el-date-picker type="datetime"
                        v-model="dataForm.expireTime"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        format="yyyy-MM-dd HH:mm:ss"
                        placeholder="过期时间"/>
      </el-form-item>
      <!--    <el-form-item label="删除标志" prop="deleteFlag">-->
      <!--      <el-input v-model="dataForm.deleteFlag" placeholder="删除标志"></el-input>-->
      <!--    </el-form-item>-->
      <!--    <el-form-item label="创建时间" prop="createTime">-->
      <!--      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>-->
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
  data() {
    return {
      visible: false,
      dataForm: {
        id: 0,
        portNum: '',
        sysUserId: '',
        expireTime: '',
        deleteFlag: '',
        createTime: ''
      },
      dataRule: {
        portNum: [
          {required: true, message: '端口数量不能为空', trigger: 'blur'}
        ],
        sysUserId: [
          {required: true, message: '管理账户id不能为空', trigger: 'blur'}
        ],
        expireTime: [
          {required: true, message: '过期时间不能为空', trigger: 'blur'}
        ],
        deleteFlag: [
          {required: true, message: '删除标志不能为空', trigger: 'blur'}
        ],
        createTime: [
          {required: true, message: '创建时间不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/atuserport/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.portNum = data.atUserPort.portNum
              this.dataForm.sysUserId = data.atUserPort.sysUserId
              this.dataForm.expireTime = data.atUserPort.expireTime
              this.dataForm.deleteFlag = data.atUserPort.deleteFlag
              this.dataForm.createTime = data.atUserPort.createTime
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/atuserport/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'portNum': this.dataForm.portNum,
              'sysUserId': this.dataForm.sysUserId,
              'expireTime': this.dataForm.expireTime,
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
<style>
</style>
