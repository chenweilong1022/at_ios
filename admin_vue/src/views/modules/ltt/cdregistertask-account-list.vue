<template>
  <el-dialog
    width="70%"
    :title="dataForm.taskName"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <div class="mod-config">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-select v-model="registerStatus" placeholder="注册状态" clearable>
            <el-option
              v-for="item in registerStatusCodes"
              :key="item.key"
              :label="item.value"
              :value="item.key">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="dataForm.phone" placeholder="手机号" clearable></el-input>
        </el-form-item>

        <el-form-item>
          <el-date-picker
            v-model="dataForm.timeKey"
            type="daterange" format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>

        <div>
          <el-button @click="getDataList()">查询</el-button>
          <el-button v-if="isAuth('ltt:atuser:delete')" type="primary" @click="registerRetryHandle()"
                     :disabled="dataListSelections.length <= 0">错误重试
          </el-button>
          <el-button type="primary" @click="copyPhoneHandle()"
                     :disabled="dataListSelections.length <= 0">复制拉群手机号
          </el-button>
          <el-button type="primary" @click="filterErrorCode()">过滤封号
          </el-button>
        </div>
      </el-form>
      <div style="font-size: 25px; font-weight: bold; margin-bottom: 20px; margin-top: 20px">
        注册中:<div style="color: #17B3A3;display: inline;margin-right: 10px;">{{waitRegisterCount}}</div>
        注册成功:<div style="color: #17B3A3;display: inline">{{successRegisterCount}}</div>
      </div>
      <el-table
        :data="dataList"
        border
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandle"
        style="width: 100%;">
        <el-table-column
          type="selection"
          header-align="center"
          align="center"
          width="50">
        </el-table-column>
        <el-table-column
          prop="appVersion"
          header-align="center"
          align="center"
          label="app版本号">
        </el-table-column>
        <el-table-column
          prop="countryCode"
          header-align="center"
          align="center"
          label="国家代码">
        </el-table-column>
        <el-table-column
          prop="phone"
          header-align="center"
          align="center" width="150px"
          label="手机号">
          <template slot-scope="scope">
          <el-badge :value="scope.row.registerCount" :class="badgeClass(scope.row.phoneState)" class="item">
            <el-button type="text" @click="copyPhoneHandle(scope.row.phone)">{{ scope.row.phone }}</el-button>
          </el-badge>
          </template>
        </el-table-column>
        <el-table-column
          prop="proxy"
          header-align="center"
          align="center"
          label="注册代理">
        </el-table-column>
        <el-table-column
          prop="smsCode"
          header-align="center"
          align="center"
          label="验证码">
        </el-table-column>
        <el-table-column
          prop="registerStatus"
          header-align="center"
          align="center"
          label="注册状态">
          <template slot-scope="scope">
            <el-tag v-for="item in registerStatusCodes" :key="item.value" v-if="scope.row.registerStatus === item.key">
              {{ item.value }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="errMsg"
          header-align="center"
          align="center"
          label="失败原因">
        </el-table-column>
        <el-table-column
          prop="createTime"
          header-align="center"
          align="center"
          label="时间">
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          width="150"
          label="操作">
          <template slot-scope="scope">
<!--            <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>-->
<!--            <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>-->
<!--            v-if="scope.row.registerStatus === 5 && scope.row.countryCode === 'jp'"-->
            <el-button type="text" size="small"

                       @click="registerRetryHandle(scope.row.id)">错误重试</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="totalPage"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
  </el-dialog>
</template>

<script>
 import CdlineRegisterList from './cdlineregister'
 import AddOrUpdate from "./cdlineregister-add-or-update.vue";
  export default {
    data () {
      return {
        registerStatus: null,
        registerStatusCodes: [],
        dataList: [],
        waitRegisterCount: null,
        successRegisterCount: null,
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        visible: false,
        dataForm: {
          id: 0,
          taskName: null,
          countryCode: null,
          timeKey: null
        },
        dataRule: {
          totalAmount: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          numberThreads: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          numberRegistered: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          numberSuccesses: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          numberFailures: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          registrationStatus: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    components: {
      AddOrUpdate,
      CdlineRegisterList
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        var createStartTime = null
        var createEndTime = null
        if (this.dataForm.timeKey != null && this.dataForm.timeKey.length >= 2) {
          createStartTime = this.dataForm.timeKey[0]
          createEndTime = this.dataForm.timeKey[1]
        }

        var param = this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'registerStatus': this.registerStatus,
          'tasksId': this.dataForm.id,
          'phone': this.dataForm.phone,
          'countryCode': this.countryCode,
          'createStartTime': createStartTime,
          'createEndTime': createEndTime
        })

        // 查询列表
        this.$http({
          url: this.$http.adornUrl('/ltt/cdlineregister/listByTaskId'),
          method: 'get',
          params: param
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })

        // 查询列表数据汇总
        this.$http({
          url: this.$http.adornUrl('/ltt/cdlineregister/listSummary'),
          method: 'get',
          params: param
        }).then(({data}) => {
          if (data && data.code === 0 && data.summary != null) {
            this.waitRegisterCount = data.summary.waitRegisterCount
            this.successRegisterCount = data.summary.successRegisterCount
          } else {
            this.waitRegisterCount = 0
            this.successRegisterCount = 0
          }
        })
      },
      init (id, taskName, countryCode) {
        this.dataForm.id = id || null
        taskName = taskName || '详情'
        if (id != null) {
          taskName = id + '-' + taskName
        }
        this.dataForm.taskName = taskName
        this.countryCode = countryCode
        this.visible = true
        this.getDefaultData()
        this.getRegisterStatus()
        this.getDataList()
      },
      getDefaultData () {
        // 获取当前日期
        const currentDate = new Date()
        // 获取三天前的日期
        const threeDaysAgo = new Date(currentDate)
        threeDaysAgo.setDate(currentDate.getDate() - 3)

        // 格式化日期为 yyyy-MM-dd
        this.dataForm.timeKey = [this.formatDateKey(threeDaysAgo), this.formatDateKey(currentDate)] // 设置默认时间为最近三天
      },
      formatDateKey (date) {
        const year = date.getFullYear()
        const month = ('0' + (date.getMonth() + 1)).slice(-2) // 月份从0开始，需要+1
        const day = ('0' + date.getDate()).slice(-2)
        return `${year}-${month}-${day}`
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 删除
      registerRetryHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定重新注册操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`/ltt/cdlineregister/registerRetry`),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },
      badgeClass(state) {
        if (state === null) {
          return 'green-badge'
        }
        if (state === true) {
          return 'green-badge'
        }
        if (state === false) {
          return 'red-badge'
        }
      },
      copyPhoneHandle (phone) {
        var phones = phone ? [phone] : this.dataListSelections.map(item => {
          return item.phone
        })
        navigator.clipboard.writeText(phones).then(() => {
          this.$message.success('手机号复制成功！')
        })
      },
      filterErrorCode () {
        this.dataList = this.dataList.filter(item => {
          return !(item.errMsg != null && item.errMsg !== '' && item.errMsg.includes('Code:100'))
        })
      },
      getRegisterStatus () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getRegisterStatus`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.registerStatusCodes = [{key: 0, value: '待处理'},...data.data]
          } else {
            this.$message.error(data.msg)
          }
        })
      }
    }
  }
</script>
<style scoped>
.item {
  margin-top: 10px;
}

/* 定义绿色背景样式 */
/deep/ .green-badge .el-badge__content {
  background-color: green;
  color: white;
}

/* 定义红色背景样式 */
/deep/ .red-badge .el-badge__content {
  background-color: red;
  color: white;
}
</style>
