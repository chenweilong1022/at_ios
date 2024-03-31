<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
<!--      <el-form-item>-->
<!--        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="所属账号:" prop="sysUserId">
        <el-select
          v-model="dataForm.sysUserId"
          filterable
          remote
          placeholder="请选择账号"
          :remote-method="queryBySearchWord"
          :loading="loading">
          <el-option
            v-for="item in sysUserAccountOptions"
            :key="item.userId"
            :label="item.username"
            :value="item.userId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="交易类型:" prop="sysUserId">
        <el-select
          v-model="dataForm.transactionType"
          filterable
          placeholder="请选择交易类型">
          <el-option
            v-for="item in accountTransactionTypeCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="交易状态:" prop="sysUserId">
        <el-select
          v-model="dataForm.status"
          filterable
          placeholder="请选择交易状态">
          <el-option
            v-for="item in accountTransactionStatusCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
<!--        <el-button v-if="isAuth('ltt:accountdetails:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
<!--        <el-button v-if="isAuth('ltt:accountdetails:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        prop="transactionId"
        header-align="center"
        align="center"
        label="交易ID">
      </el-table-column>
      <el-table-column
        prop="sysUsername"
        header-align="center"
        align="center"
        label="账户名">
      </el-table-column>
      <el-table-column
        prop="transactionType"
        header-align="center"
        align="center"
        label="交易类型">
        <template slot-scope="scope">
          <el-tag v-for="item in accountTransactionTypeCodes" :key="item.key" v-if="scope.row.transactionType === item.key">
            {{ item.value }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="amount"
        header-align="center"
        align="center"
        label="交易金额">
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="交易状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" size="small" type="success">成功</el-tag>
          <el-tag v-else size="small" type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="description"
        header-align="center"
        align="center"
        label="交易描述">
      </el-table-column>
      <el-table-column
        prop="transactionDate"
        header-align="center"
        align="center"
        label="交易时间">
      </el-table-column>
<!--      <el-table-column-->
<!--        fixed="right"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        width="150"-->
<!--        label="操作">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.transactionId)">修改</el-button>-->
<!--          <el-button type="text" size="small" @click="deleteHandle(scope.row.transactionId)">删除</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
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
</template>

<script>
  import AddOrUpdate from './accountdetails-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          key: '',
          sysUserId: null,
          transactionType: null,
          status: null
        },
        dataList: [],
        accountTransactionTypeCodes: [],
        accountTransactionStatusCodes: [],
        sysUserAccountOptions: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.init()
      this.getDataList()
      this.getAccountTransactionTypeCodes()
      this.getAccountTransactionStatusCodes()
    },
    methods: {
      init() {
        this.dataForm.sysUserId = this.$route.query.sysUserId
        this.dataForm.sysUsername = this.$route.query.sysUsername
        if (this.dataForm.sysUserId) {
          this.sysUserAccountOptions = [{
            userId: this.dataForm.sysUserId,
            username: this.dataForm.sysUsername
          }]
        }
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/ltt/accountdetails/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'sysUserId': this.dataForm.sysUserId,
            'transactionType': this.dataForm.transactionType,
            'status': this.dataForm.status
          })
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
      getAccountTransactionTypeCodes () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/accountTransactionTypeCodes`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.accountTransactionTypeCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      getAccountTransactionStatusCodes () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/accountTransactionStatusCodes`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.accountTransactionStatusCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      /*
     根据搜索词，查询系统用户
   */
      queryBySearchWord (serchKey) {
        serchKey = serchKey == null ? '' : serchKey + ''
        this.$http({
          url: this.$http.adornUrl(`/sys/user/queryBySearchWord?searchWord=${serchKey}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.sysUserAccountOptions = data.userList
          }
        })
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
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.transactionId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/accountdetails/delete'),
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
      }
    }
  }
</script>
