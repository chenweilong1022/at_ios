<template>
  <el-dialog
    width="90%"
    :title="dataForm.taskName"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <div class="mod-config">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList(), listSummary()">
        <el-form-item>
          <el-select v-model="phoneStatus" placeholder="注册状态" clearable>
            <el-option
              v-for="item in phoneStatusCodes"
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
          <el-button @click="getDataList(), listSummary()">查询</el-button>
          <el-button v-if="isAuth('ltt:atuser:delete')" type="primary" @click="registerRetryHandle()"
                     :disabled="dataListSelections.length <= 0">错误重试
          </el-button>
          <el-button v-if="isAuth('ltt:atuser:delete')" type="primary" @click="registerRetryHandle2()"
                     :disabled="dataListSelections.length <= 0">拉黑重试
          </el-button>
          <el-button type="primary" @click="copyPhoneHandle()"
                     :disabled="dataListSelections.length <= 0">复制拉群手机号
          </el-button>
          <el-button type="primary" @click="filterErrorCode()">过滤封号
          </el-button>
          <el-button type="danger" size="small"
                     @click="invalidatePhoneHandle()">作废</el-button>
        </div>
      </el-form>
      <div style="font-size: 25px; font-weight: bold; margin-bottom: 20px; margin-top: 20px">
        待处理:<div style="color: #17B3A3;display: inline;margin-right: 10px;">{{summary.waitCount}}</div>
        发起注册:<div style="color: #17B3A3;display: inline">{{summary.startRegisterCount}}</div>
        提交验证码:<div style="color: #17B3A3;display: inline">{{summary.submitRegisterCount}}</div>
        注册成功:<div style="color: #17B3A3;display: inline">{{summary.successRegisterCount}}</div>
<!--        注册异常:<div style="color: #17B3A3;display: inline">{{summary.errorRegisterCount}}</div>-->
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
          align="center" width="100px"
          label="app版本号">
        </el-table-column>
        <el-table-column
          prop="countryCode"
          header-align="center"
          align="center" width="100px"
          label="国家代码">
        </el-table-column>
        <el-table-column
          prop="phone"
          header-align="center"
          align="center" width="130px"
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
          align="center" width="250px"
          label="注册代理">
          <template slot-scope="scope">
            {{scope.row.cdLineRegisterEntity.proxy}}
          </template>
        </el-table-column>
        <el-table-column
          prop="smsCode"
          header-align="center"
          align="center" width="100px"
          label="验证码">
        </el-table-column>
        <el-table-column
          prop="phoneStatusStr"
          header-align="center" width="110px"
          align="center"
          label="注册状态">
          <template slot-scope="scope">
            <el-tag>
              {{scope.row.phoneStatusStr}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="errMsg"
          header-align="center"
          align="center" width="150px"
          label="失败原因">
          <template slot-scope="scope">
            {{scope.row.cdLineRegisterEntity.errMsg}}
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          header-align="center"
          align="center" width="210px"
          label="时间">
          <template slot-scope="scope">
            <div>注册：{{ scope.row.createTime }}</div>
            <div>取号：{{ scope.row.firstEnterTime }}</div>
          </template>
        </el-table-column>
        <el-table-column
          prop="retryNum"
          header-align="center"
          align="center" width="100px"
          label="重试次数">
          <template slot-scope="scope">
          <span :style="{ color: scope.row.retryNum !== null && scope.row.retryNum >= 3 ? 'red' : 'black',
          fontWeight: 'bold', fontSize: '16px'}">
          {{ scope.row.retryNum }}
        </span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          header-align="center"
          align="center"
          label="操作">
          <template slot-scope="scope">
            <el-button type="primary" size="small" v-if="scope.row.phoneStatus !== 7"
                       @click="registerRetryHandle(scope.row.id)">错误重试</el-button>
            <el-button type="danger" size="small" v-if="scope.row.phoneStatus !== 7"
                       @click="invalidatePhoneHandle(scope.row.id)">作废</el-button>
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
        phoneStatus: null,
        phoneStatusCodes: [],
        dataList: [],
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
        summary: {
          successRegisterCount: null,
          waitCount: null,
          startRegisterCount: null,
          submitRegisterCount: null,
          errorRegisterCount: null
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
          'phoneStatus': this.phoneStatus,
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
      },
      listSummary () {
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
          'tasksId': this.dataForm.id,
          'countryCode': this.countryCode,
          'createStartTime': createStartTime,
          'createEndTime': createEndTime
        })
        // 查询列表数据汇总
        this.$http({
          url: this.$http.adornUrl('/ltt/cdlineregister/listSummary'),
          method: 'get',
          params: param
        }).then(({data}) => {
          if (data && data.code === 0 && data.summary != null) {
            this.summary.successRegisterCount = data.summary.successRegisterCount
            this.summary.waitCount = data.summary.waitCount
            this.summary.startRegisterCount = data.summary.startRegisterCount
            this.summary.submitRegisterCount = data.summary.submitRegisterCount
            this.summary.errorRegisterCount = data.summary.errorRegisterCount
          } else {
            this.summary.successRegisterCount = 0
            this.summary.waitCount = 0
            this.summary.startRegisterCount = 0
            this.summary.submitRegisterCount = 0
            this.summary.errorRegisterCount = 0
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
        this.getPhoneStatus()
        this.getDataList()
        this.listSummary()
      },
      getDefaultData () {
        // 获取当前日期
        const currentDate = new Date()
        // 获取三天前的日期
        const threeDaysAgo = new Date(currentDate)
        threeDaysAgo.setDate(currentDate.getDate() - 1)

        const tomorrowData = new Date(currentDate)
        tomorrowData.setDate(currentDate.getDate() + 1)

        // 格式化日期为 yyyy-MM-dd
        this.dataForm.timeKey = [this.formatDateKey(threeDaysAgo), this.formatDateKey(tomorrowData)] // 设置默认时间为最近三天
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
      // 账号作废
      invalidatePhoneHandle(id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定作废?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`/ltt/cdlineregister/invalidatePhone`),
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
      // 删除
      registerRetryHandle (id, ipClearFlag) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        let url
        if (ipClearFlag === true) {
          url = '/ltt/cdlineregister/registerRetr2'
        } else {
          url = '/ltt/cdlineregister/registerRetry'
        }
        this.$confirm(`确定重新注册操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(url),
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
      // 删除
      registerRetryHandle2 (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定重新注册操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`/ltt/cdlineregister/registerRetry2`),
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
          return !(item.errMsg != null && item.errMsg !== '' && item.errMsg.includes('Code:100') && item.errMsg !== 'セッションがタイムアウトしました。 もう一度お試しください' && item.phoneStatus !== 7)
        })
      },
      getPhoneStatus () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getRegisterStatus`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.phoneStatusCodes = data.data
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
.button-style {
  height: 24px; /* 指定按钮高度 */
  line-height: 24px; /* 使文本垂直居中 */
  margin-right: 5px; /* 按钮之间的间距 */
}
</style>
