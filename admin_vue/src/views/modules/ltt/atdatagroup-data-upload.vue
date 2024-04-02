<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="分组名称" prop="name">
      <el-input v-model="dataForm.name" placeholder="分组名称" disabled></el-input>
    </el-form-item>
    <el-form-item label="分组类型">
      <el-select
        disabled
        v-model="groupType"
        class="m-2"
        placeholder="Select"
        size="large"
        style="width: 240px"
      >
        <el-option
          v-for="item in options"
          :key="item.key"
          :label="item.value"
          :value="item.key"
        />
      </el-select>
    </el-form-item>
      <el-form-item label="数据上传" prop="groupName">
        <el-upload ref="upload"
          class="upload-demo" :action="uploadUrl"
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
        groupType: null,
        uploadUrl: '',
        options: [],
        visible: false,
        dataForm: {
          id: 0,
          name: '',
          txtUrl: '',
          groupType: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          name: [
            { required: true, message: '分组名称不能为空', trigger: 'blur' }
          ],
          groupType: [
            { required: true, message: '分组类型不能为空', trigger: 'blur' }
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
      handleAvatarSuccess (res, file) {
        this.dataForm.txtUrl = res.data
      },
      init (id) {
        this.getGroupType()
        this.dataForm.id = id || 0
        this.uploadUrl = this.$http.adornUrl(`/app/file/upload`)
        this.visible = true
        this.$refs.upload.clearFiles()
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atdatagroup/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.atDataGroup.name
                this.dataForm.groupType = data.atDataGroup.groupType
                this.dataForm.deleteFlag = data.atDataGroup.deleteFlag
                this.dataForm.createTime = data.atDataGroup.createTime
                this.groupType = this.dataForm.groupType
              }
            })
          }
        })
      },
      getGroupType () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getGroupType`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.options = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atdatagroup/updateBatchGroup`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'txtUrl': this.dataForm.txtUrl,
                'groupType': this.groupType,
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
