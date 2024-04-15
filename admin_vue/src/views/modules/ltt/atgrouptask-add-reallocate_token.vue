<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="拉群号国家">
        <el-select v-model="dataForm.countryCode" placeholder="拉群号国家" clearable>
          <el-option
            v-for="item in countryCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="拉群号分组" prop="userGroupId">
        <el-select v-model="dataForm.userGroupId" placeholder="账户分组">
          <el-option
            v-for="item in dataUserGroupList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="拉群号数量" prop="pullGroupNumber">
        <el-input v-model="dataForm.pullGroupNumber" placeholder="拉群号数量"></el-input>
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
        countryCodes: [],
        dataUserGroupList: [],
        dataForm: {
          id: 0,
          countryCode: null,
          userGroupId: null,
          pullGroupNumber: 2,
          ids: [],
          taskName: '',
          groupType: '',
          addTotalQuantity: '',
          successfulQuantity: '',
          failuresQuantity: '',
          taskStatus: '',
          schedule: '',
          updateTime: '',
          deleteFlag: '',
          createTime: '',
          sysUserId: ''
        },
        dataRule: {
          taskName: [
            { required: true, message: '任务名称不能为空', trigger: 'blur' }
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
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          sysUserId: [
            { required: true, message: '管理账户id不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
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
      init (ids) {
        this.dataForm.ids = ids
        this.visible = true
        this.getCountryCodeEnums()
        this.getUserGroupDataList()
      },
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
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atgroup/reallocateToken`),
              method: 'post',
              data: this.$http.adornData({
                'ids': this.dataForm.ids,
                'userGroupId': this.dataForm.userGroupId,
                'pullGroupNumber': this.dataForm.pullGroupNumber,
                'countryCode': this.dataForm.countryCode
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
