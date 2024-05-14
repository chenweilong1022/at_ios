<template>
  <el-dialog
    :title="!dataForm.id ? '数据上传' : '数据上传'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="分组名称" prop="name">
      <el-input v-model="dataForm.name" placeholder="分组名称" disabled=""></el-input>
    </el-form-item>

      <el-form-item label="分组名称" prop="name">
        <el-upload ref="upload"
          v-model:file-list="fileList"
          class="upload-demo"
          :multiple="multiple"
          :action="uploadUrl"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :on-success="handleSuccess"
          list-type="picture"
        >
          <el-button type="primary">Click to upload</el-button>
          <template #tip>
            <div class="el-upload__tip">
              jpg/png files with a size less than 500kb
            </div>
          </template>
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
        multiple: true,
        fileList: [],
        visible: false,
        uploadUrl: '',
        dataForm: {
          avatarList: [],
          id: 0,
          name: '',
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
      handleSuccess (uploadFile, response, uploadFiles) {
        this.fileList = uploadFiles
      },
      handleRemove (uploadFile, uploadFiles) {
        this.fileList = uploadFiles
      },
      handlePreview (uploadFile, uploadFiles) {
        this.fileList = uploadFiles
      },
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.uploadUrl = this.$http.adornUrl(`/app/file/upload`)
        if (this.$refs.upload != null) {
          this.$refs.upload.clearFiles()
        }
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atavatargroup/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.atAvatarGroup.name
                this.dataForm.deleteFlag = data.atAvatarGroup.deleteFlag
                this.dataForm.createTime = data.atAvatarGroup.createTime
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.dataForm.avatarList = []
        for (let i = 0; i < this.fileList.length; i++) {
          let data = this.fileList[i]
          this.dataForm.avatarList.push(data.response.data)
        }
        this.fileList = []
        console.log(this.dataForm.avatarList)
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atavatargroup/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'avatarList': this.dataForm.avatarList,
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
