<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item label="所属账号" prop="sysUserId">
        <el-select
          v-model="dataForm.sysUserId"
          filterable clearable
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
      <el-form-item label="国号(区号)" prop="countryCode">
        <el-select
          v-model="dataForm.countryCode"
          filterable clearable
          placeholder="请选择国家">
          <el-option
            v-for="item in countryCodes"
            :key="item.value"
            :label="item.value"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品类型" prop="productType">
        <el-select
          v-model="dataForm.productType"
          filterable clearable
          placeholder="请选择商品类型">
          <el-option
            v-for="item in productTypeCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select
          v-model="dataForm.orderStatus"
          filterable clearable
          placeholder="请选择订单状态">
          <el-option
            v-for="item in orderStatusCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
<!--        <el-button v-if="isAuth('ltt:atorders:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
<!--        <el-button v-if="isAuth('ltt:atorders:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
<!--      <el-table-column-->
<!--        type="selection"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        width="50">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="orderId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="订单ID">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="sysUsername"
        header-align="center"
        align="center"
        label="账户名称">
      </el-table-column>
      <el-table-column
        prop="orderStatus"
        header-align="center"
        align="center"
        label="订单状态">
        <template slot-scope="scope">
          <el-tag v-for="item in orderStatusCodes" :key="item.key" v-if="scope.row.orderStatus === item.key">
            {{ item.value }}
          </el-tag>
        </template>
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="notes"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="订单备注">-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--        prop="productId"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="商品id">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="countryCode"
        header-align="center"
        align="center"
        label="国家code">
        <template slot-scope="scope">
          <el-tag v-for="item in countryCodes" :key="item.key" v-if="scope.row.countryCode === item.value">
            {{ item.value }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="productType"
        header-align="center"
        align="center"
        label="商品类型">
        <template slot-scope="scope">
          <el-tag v-for="item in productTypeCodes" :key="item.key" v-if="scope.row.productType === item.key">
            {{ item.value }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="orderNumber"
        header-align="center"
        align="center"
        label="购买数量">
      </el-table-column>
      <el-table-column
        prop="totalAmount"
        header-align="center"
        align="center"
        label="订单总金额">
      </el-table-column>
      <el-table-column
        prop="orderTime"
        header-align="center"
        align="center"
        label="订单创建时间">
      </el-table-column>
      <el-table-column
        prop="updateTime"
        header-align="center"
        align="center"
        label="订单最后更新时间">
      </el-table-column>
<!--      <el-table-column-->
<!--        fixed="right"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        width="150"-->
<!--        label="操作">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.orderId)">修改</el-button>-->
<!--          <el-button type="text" size="small" @click="deleteHandle(scope.row.orderId)">删除</el-button>-->
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
  import AddOrUpdate from './atorders-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          sysUserId: null,
          countryCode: null,
          productType: null,
          orderStatus: null
        },
        sysUserAccountOptions: [],
        countryCodes: [],
        orderStatusCodes: [],
        dataList: [],
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
      this.getDataList()
      this.getCountryCodes()
      this.getProductTypeCodes()
      this.getOrderStatus()
      this.queryBySearchWord()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/ltt/atorders/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'sysUserId': this.dataForm.sysUserId,
            'countryCode': this.dataForm.countryCode,
            'productType': this.dataForm.productType,
            'orderStatus': this.dataForm.orderStatus
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
          return item.orderId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atorders/delete'),
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
      getCountryCodes () {
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
      getProductTypeCodes() {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getProductTypeCodes`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.productTypeCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      getOrderStatus() {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getOrderStatus`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.orderStatusCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      }

    }
  }
</script>
