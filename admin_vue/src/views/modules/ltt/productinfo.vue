<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.productName" placeholder="商品名称检索" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('ltt:productinfo:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <!--        <el-button v-if="isAuth('ltt:productinfo:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <!--      <el-table-column-->
      <!--        prop="productId"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="">-->
      <!--      </el-table-column>-->
      <el-table-column
        prop="productName"
        header-align="center"
        align="center"
        label="商品名称">
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
        prop="countryCode"
        header-align="center"
        align="center"
        label="国号(区号)">
        <template slot-scope="scope">
          <el-tag v-for="item in countryCodes" :key="item.key" v-if="scope.row.countryCode === item.value">
            {{ item.value }}
          </el-tag>
        </template>
      </el-table-column>
      <!--      <el-table-column-->
      <!--      <el-table-column-->
      <!--        prop="description"-->
      <!--        header-align="center"-->
      <!--        align="center"-->
      <!--        label="商品描述">-->
      <!--      </el-table-column>-->
      <el-table-column
        prop="price"
        header-align="center"
        align="center"
        label="商品价格">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="stockQuantity"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="库存数量">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="商品状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" size="small" type="success">上架</el-tag>
          <el-tag v-else size="small" type="danger">下架</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.productId)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.productId)">删除</el-button>
          <el-button v-if="scope.row.status === 1" type="text" size="small"
                     @click="updateStatusHandle(scope.row.productId, 0, '下架')">下架
          </el-button>
          <el-button v-else type="text" size="small" @click="updateStatusHandle(scope.row.productId, 1, '上架')">
            上架
          </el-button>
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
</template>

<script>
import AddOrUpdate from './productinfo-add-or-update'

export default {
  data() {
    return {
      dataForm: {
        productName: ''
      },
      dataList: [],
      countryCodes: [],
      productTypeCodes: [],
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
  activated() {
    this.getDataList()
    this.getCountryCodes()
    this.getProductTypeCodes()
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/ltt/productinfo/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'productName': this.dataForm.productName
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
    getCountryCodes() {
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
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(productId) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(productId)
      })
    },
    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.productId
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/ltt/productinfo/delete'),
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
    // 上下级
    updateStatusHandle(id, status, statusName) {
      this.$confirm(`确定进行[${statusName}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`/ltt/productinfo/update`),
          method: 'post',
          data: this.$http.adornData({
            'productId': id,
            'status': status
          })
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
