<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="分组名称" prop="name">
      <el-input v-model="dataForm.name" placeholder="分组名称"></el-input>
    </el-form-item>
    <el-form-item label="分组类型">
      <el-select
        v-model="groupType"
        class="m-2"
        placeholder="Select"
        size="large"
        style="width: 240px"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
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
        groupType: null,
        options: [
          {
            value: 1,
            label: '手机号'
          },
          {
            value: 2,
            label: '不封控地区普通uid模式'
          },
          {
            value: 3,
            label: '自定义id'
          },
          {
            value: 4,
            label: '日本，台湾专用uid模式'
          }
        ],
        visible: false,
        dataForm: {
          id: 0,
          name: '',
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
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
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
              url: this.$http.adornUrl(`/ltt/atdatagroup/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
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
