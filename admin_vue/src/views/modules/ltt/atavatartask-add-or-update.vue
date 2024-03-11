<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="任务名称" prop="taskName">
      <el-input v-model="dataForm.taskName" placeholder="任务名称"></el-input>
    </el-form-item>
    <el-form-item label="账户分组" prop="userGroupId">
      <el-select v-model="dataForm.userGroupId" placeholder="账户分组">
        <el-option
          v-for="item in dataUserGroupList"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="头像分组" prop="avatarGroupId">
      <el-select v-model="dataForm.avatarGroupId" placeholder="头像分组">
        <el-option
          v-for="item in dataAvatarGroupList"
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
        dataUserGroupList: [],
        dataAvatarGroupList: [],
        visible: false,
        dataForm: {
          id: 0,
          taskName: '',
          taskStatus: '',
          schedule: '',
          userGroupId: '',
          avatarGroupId: '',
          executionQuantity: '',
          successfulQuantity: '',
          failuresQuantity: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          taskName: [
            { required: true, message: '任务名称不能为空', trigger: 'blur' }
          ],
          taskStatus: [
            { required: true, message: '任务状态不能为空', trigger: 'blur' }
          ],
          schedule: [
            { required: true, message: '进度不能为空', trigger: 'blur' }
          ],
          userGroupId: [
            { required: true, message: '账户分组不能为空', trigger: 'blur' }
          ],
          avatarGroupId: [
            { required: true, message: '头像分组不能为空', trigger: 'blur' }
          ],
          executionQuantity: [
            { required: true, message: '执行数量不能为空', trigger: 'blur' }
          ],
          successfulQuantity: [
            { required: true, message: '成功数量不能为空', trigger: 'blur' }
          ],
          failuresQuantity: [
            { required: true, message: '失败数量不能为空', trigger: 'blur' }
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
      // 获取数据列表
      getAvatarGroupDataList () {
        this.$http({
          url: this.$http.adornUrl('/ltt/atavatargroup/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 100
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataAvatarGroupList = data.page.list
          } else {
            this.dataAvatarGroupList = []
          }
        })
      },
      // 获取数据列表
      getUserGroupDataList () {
        this.$http({
          url: this.$http.adornUrl('/ltt/atusergroup/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 100
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataUserGroupList = data.page.list
          } else {
            this.dataUserGroupList = []
          }
        })
      },
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.getUserGroupDataList()
        this.getAvatarGroupDataList()
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atavatartask/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.taskName = data.atavatartask.taskName
                this.dataForm.taskStatus = data.atavatartask.taskStatus
                this.dataForm.schedule = data.atavatartask.schedule
                this.dataForm.userGroupId = data.atavatartask.userGroupId
                this.dataForm.avatarGroupId = data.atavatartask.avatarGroupId
                this.dataForm.executionQuantity = data.atavatartask.executionQuantity
                this.dataForm.successfulQuantity = data.atavatartask.successfulQuantity
                this.dataForm.failuresQuantity = data.atavatartask.failuresQuantity
                this.dataForm.deleteFlag = data.atavatartask.deleteFlag
                this.dataForm.createTime = data.atavatartask.createTime
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
              url: this.$http.adornUrl(`/ltt/atavatartask/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'taskName': this.dataForm.taskName,
                'taskStatus': this.dataForm.taskStatus,
                'schedule': this.dataForm.schedule,
                'userGroupId': this.dataForm.userGroupId,
                'avatarGroupId': this.dataForm.avatarGroupId,
                'executionQuantity': this.dataForm.executionQuantity,
                'successfulQuantity': this.dataForm.successfulQuantity,
                'failuresQuantity': this.dataForm.failuresQuantity,
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
