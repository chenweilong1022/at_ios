<template>
  <div class="mod-home">

    <div class="summary">
      <div class="summary-item">
        <el-form :inline="true" :model="jpDataForm.dataForm" @keyup.enter.native="queryJpDataSummary()">
          <div>line信息 JP</div>
          <el-form-item>
            <el-date-picker
              v-model="jpDataForm.dataForm.timeKey"
              type="daterange" format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button @click="queryJpDataSummary()">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table
          :data="jpDataForm.dataList"
          row-class-name="custom-row"
          v-loading="jpDataForm.dataListLoading"
          style="width: 100%;">
          <el-table-column
            prop="summaryDate"
            label="日期"
            width="130">
          </el-table-column>
          <el-table-column
            prop="lineStock"
            label="line库存"
            width="130">
          </el-table-column>
          <el-table-column
            prop="userOnlineCount"
            label="在线账户"
            width="130">
          </el-table-column>
          <el-table-column
            prop="lineRegisterCount"
            label="今日注册量"
            width="130">
          </el-table-column>
          <el-table-column
            prop="userUseCount"
            label="今日使用量"
            width="120">
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="sizeJpChangeHandle"
          @current-change="currentJpChangeHandle"
          :current-page="jpDataForm.pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="jpDataForm.pageSize"
          :total="jpDataForm.totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </div>


      <div class="summary-item">
        <el-form :inline="true" :model="thDataForm.dataForm" @keyup.enter.native="queryThDataSummary()">
          <div>line信息 TH</div>
          <el-form-item>
            <el-date-picker
              v-model="thDataForm.dataForm.timeKey"
              type="daterange" format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button @click="queryThDataSummary()">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table
          :data="thDataForm.dataList"
          row-class-name="custom-row"
          v-loading="thDataForm.dataListLoading"
          style="width: 100%;">
          <el-table-column
            prop="summaryDate"
            label="日期"
            width="130">
          </el-table-column>
          <el-table-column
            prop="lineStock"
            label="line库存"
            width="130">
          </el-table-column>
          <el-table-column
            prop="userOnlineCount"
            label="在线账户"
            width="130">
          </el-table-column>
          <el-table-column
            prop="lineRegisterCount"
            label="今日注册量"
            width="130">
          </el-table-column>
          <el-table-column
            prop="userUseCount"
            label="今日使用量"
            width="120">
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="sizeThChangeHandle"
          @current-change="currentThChangeHandle"
          :current-page="thDataForm.pageIndex"
          :page-sizes="[1, 20, 50, 100]"
          :page-size="thDataForm.pageSize"
          :total="thDataForm.totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </div>

    </div>
  </div>
</template>

<script>
import {color} from "../../../static/plugins/echarts-3.8.5/echarts.common.min";

export default {
  computed: {
    color() {
      return color
    }
  },
  data() {
    return {
      jpDataForm: {
        dataForm: {
          timeKey: null
        },
        searchStartTime: null,
        searchEndTime: null,
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false
      },
      thDataForm: {
        dataForm: {
          timeKey: null
        },
        searchStartTime: null,
        searchEndTime: null,
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false
      },
      userSummary: []
    }
  },
  activated() {
    this.queryJpDataSummary()
    this.queryThDataSummary()
  },
  methods: {
    queryJpDataSummary () {
      var searchStartTime = null
      var searchEndTime = null
      if (this.jpDataForm.dataForm.timeKey != null && this.jpDataForm.dataForm.timeKey.length >= 2) {
        searchStartTime = this.jpDataForm.dataForm.timeKey[0]
        searchEndTime = this.jpDataForm.dataForm.timeKey[1]
      }
      this.$http({
        url: this.$http.adornUrl('/ltt/atuserdatasummary/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.jpDataForm.pageIndex,
          'limit': this.jpDataForm.pageSize,
          'searchStartTime': searchStartTime,
          'searchEndTime': searchEndTime,
          'countryCode': 'jp'
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.jpDataForm.dataList = data.page.list
          this.jpDataForm.totalPage = data.page.totalCount
        } else {
          this.jpDataForm.dataList = []
          this.jpDataForm.totalPage = 0
        }
        this.jpDataForm.dataListLoading = false
      })
    },
    queryThDataSummary () {
      var searchStartTime = null
      var searchEndTime = null
      if (this.thDataForm.dataForm.timeKey != null && this.thDataForm.dataForm.timeKey.length >= 2) {
        searchStartTime = this.thDataForm.dataForm.timeKey[0]
        searchEndTime = this.thDataForm.dataForm.timeKey[1]
      }
      this.$http({
        url: this.$http.adornUrl('/ltt/atuserdatasummary/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.thDataForm.pageIndex,
          'limit': this.thDataForm.pageSize,
          'searchStartTime': searchStartTime,
          'searchEndTime': searchEndTime,
          'countryCode': 'th'
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.thDataForm.dataList = data.page.list
          this.thDataForm.totalPage = data.page.totalCount
        } else {
          this.thDataForm.dataList = []
          this.thDataForm.totalPage = 0
        }
        this.thDataForm.dataListLoading = false
      })
    },
    // 每页数-th
    sizeThChangeHandle (val) {
      this.thDataForm.pageSize = val
      this.thDataForm.pageIndex = 1
      this.queryThDataSummary()
    },
    // 当前页-th
    currentThChangeHandle (val) {
      this.thDataForm.pageIndex = val
      this.queryThDataSummary()
    },
    // 每页数-jp
    sizeJpChangeHandle (val) {
      this.jpDataForm.pageSize = val
      this.jpDataForm.pageIndex = 1
      this.queryJpDataSummary()
    },
    // 当前页-jp
    currentJpChangeHandle (val) {
      this.jpDataForm.pageIndex = val
      this.queryJpDataSummary()
    },
  }
}
</script>

<style scoped>
.mod-home {
  line-height: 1.5;
}

.summary {
  display: flex;
  justify-content: space-between;
  width: 1400px;
  overflow-x: auto; /* 如果内容太多，出现水平滚动条 */
  background: #f8f9fa;
  text-align: center;
  //在容器的内部边界添加填充
  padding: 20px;
  margin: 10px; /* 添加外边距避免模块紧贴在一起 */
  min-height: 100px;
}

.summary-item {
  //给容器添加圆角。
  border-radius: 8px;
  //添加轻微的阴影效果，以提高层次感
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow-x: auto; /* 如果内容太多，出现水平滚动条 */
  border: 2px solid #dcdcdc; /* 明显的边框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15); /* 更深的阴影效果 */
  width: 80%; /* 全宽度使得居中更为明显，也可根据实际需要调整宽度 */
  margin: 10px 10px; /* Optional: Add margins to each item for better spacing */
  padding: 20px 20px; /* 增大内边距，使文字与边框之间的空间更大 */
  font-size: 18px; /* 特别大的字体大小 */
  text-align: center;
  justify-content: center; /* 水平居中 */
  flex-direction: row;
  min-height: 80px;
  align-items: center; /* 垂直居中所有内容 */
  display: inline;


  table {
    color: black; /* 设置字体颜色 */
  }

  .cell {
    color: black; /* 设置字体颜色 */
    font-size: 18px; /* 特别大的字体大小 */

    //font-weight: bold; /* 加粗字体 */
  }
}

</style>

