<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="任务名称" prop="taskName">
      <el-input v-model="dataForm.taskName" placeholder="任务名称"></el-input>
    </el-form-item>

      <el-form-item label="类型" prop="groupType">
          <el-select
            v-model="groupType"
            @change="groupTypeChangeHandler"
            class="m-2"
            placeholder="选择类型"
            size="large"
            style="width: 240px">
            <el-option
              v-for="item in options"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            />
          </el-select>
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
      <el-form-item label="加粉号国家">
        <el-select v-model="dataForm.countryCode" placeholder="拉群号国家" clearable>
          <el-option
            v-for="item in countryCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
    <el-form-item label="数据分组" prop="dataGroupId">
      <el-select v-model="dataForm.dataGroupId" placeholder="数据分组">
        <el-option
          v-for="item in datagroupList"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="每个号加粉数量" prop="addQuantityLimit">
      <el-input-number v-model="dataForm.addQuantityLimit" :min="1" :max="99" label="每个号加粉数量"></el-input-number>

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
        visible: false,
        dataUserGroupList: [],
        datagroupList: [],
        countryCodes: [],
        options: [],
        dataForm: {
          id: 0,
          taskName: '',
          userGroupId: '',
          dataGroupId: '',
          groupType: '',
          addTotalQuantity: '',
          countryCode: null,
          successfulQuantity: '',
          failuresQuantity: '',
          taskStatus: '',
          schedule: '',
          updateTime: '',
          addQuantityLimit: '',
          deleteFlag: '',
          createTime: ''
        },
        dataRule: {
          taskName: [
            { required: true, message: '任务名称不能为空', trigger: 'blur' }
          ],
          userGroupId: [
            { required: true, message: '账户分组不能为空', trigger: 'blur' }
          ],
          dataGroupId: [
            { required: true, message: '数据分组不能为空', trigger: 'blur' }
          ],
          groupType: [
            { required: true, message: '类型不能为空', trigger: 'blur' }
          ],
          addTotalQuantity: [
            { required: true, message: '加粉总数不能为空', trigger: 'blur' }
          ],
          successfulQuantity: [
            { required: true, message: '成功数不能为空', trigger: 'blur' }
          ],
          failuresQuantity: [
            { required: true, message: '失败数不能为空', trigger: 'blur' }
          ],
          taskStatus: [
            { required: true, message: '状态不能为空', trigger: 'blur' }
          ],
          schedule: [
            { required: true, message: '进度不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '更新时间不能为空', trigger: 'blur' }
          ],
          addQuantityLimit: [
            { required: true, message: '加粉数量不能为空', trigger: 'blur' }
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
      groupTypeChangeHandler () {
        this.dataForm.groupType = this.groupType
        this.getDataGroupDataList(this.groupType)
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
      // 获取数据列表
      getDataGroupDataList (type) {
        this.$http({
          url: this.$http.adornUrl('/ltt/atdatagroup/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 100,
            'groupType': type
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.datagroupList = data.page.list
          } else {
            this.datagroupList = []
          }
        })
      },
      init (id) {
        this.dataForm.id = id || 0
        this.groupType = null
        this.visible = true
        this.getGroupType()
        this.getCountryCodeEnums()
        this.getUserGroupDataList()
        this.getDataGroupDataList(null)
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atdatatask/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.taskName = data.atdatatask.taskName
                this.dataForm.userGroupId = data.atdatatask.userGroupId
                this.dataForm.dataGroupId = data.atdatatask.dataGroupId
                this.dataForm.groupType = data.atdatatask.groupType
                this.dataForm.addTotalQuantity = data.atdatatask.addTotalQuantity
                this.dataForm.successfulQuantity = data.atdatatask.successfulQuantity
                this.dataForm.failuresQuantity = data.atdatatask.failuresQuantity
                this.dataForm.taskStatus = data.atdatatask.taskStatus
                this.dataForm.schedule = data.atdatatask.schedule
                this.dataForm.updateTime = data.atdatatask.updateTime
                this.dataForm.addQuantityLimit = data.atdatatask.addQuantityLimit
                this.dataForm.deleteFlag = data.atdatatask.deleteFlag
                this.dataForm.createTime = data.atdatatask.createTime
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.dataForm.groupType = this.groupType
            this.$http({
              url: this.$http.adornUrl(`/ltt/atdatatask/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'taskName': this.dataForm.taskName,
                'userGroupId': this.dataForm.userGroupId,
                'dataGroupId': this.dataForm.dataGroupId,
                'groupType': this.dataForm.groupType,
                'countryCode': this.dataForm.countryCode,
                'addTotalQuantity': this.dataForm.addTotalQuantity,
                'successfulQuantity': this.dataForm.successfulQuantity,
                'failuresQuantity': this.dataForm.failuresQuantity,
                'taskStatus': this.dataForm.taskStatus,
                'schedule': this.dataForm.schedule,
                'updateTime': this.dataForm.updateTime,
                'addQuantityLimit': this.dataForm.addQuantityLimit,
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
