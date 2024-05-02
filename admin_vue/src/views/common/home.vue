<template>
  <div class="mod-home">

      <div class="summary">
      <div class="summary-item">
        <el-table
          :data="lineRegisterSummary"
          row-class-name="custom-row"
          style="width: 100%;">
          <el-table-column
            prop="countryCode"
            label="line信息"
            width="130">
          </el-table-column>
          <el-table-column :label="'库存总数: ' + dataForm.registerTotalStock">
            <el-table-column :label="'今日注册总数: ' + dataForm.todayRegisterTotalNum">
              <el-table-column
                prop="registerStock"
                label="库存"
                width="130">
              </el-table-column>
              <el-table-column
                prop="todayRegisterNum"
                label="今日注册量"
                width="130">
              </el-table-column>
              <el-table-column
                prop="yesterdayRegisterNum"
                label="昨日注册量"
                width="130">
              </el-table-column>
              <el-table-column
                prop="beforeRegisterNum"
                label="前天注册量"
                width="120">
              </el-table-column>
            </el-table-column>
          </el-table-column>
        </el-table>
      </div>


      <div class="summary-item">
        <el-table
          :data="userSummary"
          row-class-name="custom-row"
          style="width: 100%">
          <el-table-column
            prop="countryCode"
            label="账号信息"
            width="220">
          </el-table-column>
          <el-table-column :label="'在线总数: ' + dataForm.onlineUserTotalNum">
            <el-table-column :label="'今日已使用总数: ' + dataForm.usedUserTotalStock">
              <el-table-column
                prop="onlineUserNum"
                label="在线数量"
                width="220">
              </el-table-column>
              <el-table-column
                prop="usedUserStock"
                label="今日已使用数量"
                width="220">
              </el-table-column>
            </el-table-column>
          </el-table-column>
        </el-table>
      </div>
      <!--        <div class="container-item">line库存:-->
      <!--          <div style="color: #007BFF;display: inline;"> {{ dataForm.registerTotalStock }}</div>-->
      <!--        </div>-->
      <!--        <div class="container-item">今日注册line数量:-->
      <!--          <div style="color: #28a745;display: inline;"> {{ dataForm.todayRegisterTotalNum }}</div>-->
      <!--        </div>-->
      <!--        <div v-for="(scope, index) in lineRegisterSummary"-->
      <!--             :key="index"-->
      <!--             class="type"-->
      <!--             :data="scope">-->
      <!--          <h3>{{ scope.countryCode }}</h3>-->
      <!--          <p>line库存: {{ scope.registerStock }}</p>-->
      <!--          <p>今日注册line数量: {{ scope.todayRegisterNum }}</p>-->
      <!--          <p>昨日注册line数量: {{ scope.yesterdayRegisterNum }}</p>-->
      <!--          <p>前天注册line数量: {{ scope.beforeRegisterNum }}</p>-->
      <!--        </div>-->
      <!--      </div>-->
      <!--      <div class="box">-->
      <!--        <div class="container-item">在线账户:-->
      <!--          <div style="color: #28a745;display: inline;"> {{ dataForm.onlineUserTotalNum }}</div>-->
      <!--        </div>-->
      <!--        <div class="container-item">今日已使用账户:-->
      <!--          <div style="color: #007BFF;display: inline;"> {{ dataForm.usedUserTotalStock }}</div>-->
      <!--        </div>-->
      <!--        <div v-for="(scope, index) in userSummary"-->
      <!--             :key="index"-->
      <!--             class="type"-->
      <!--             :data="scope">-->
      <!--          <h3>{{ scope.countryCode }}</h3>-->
      <!--          <p>在线账户: {{ scope.onlineUserNum}}</p>-->
      <!--          <p>今日已使用账户: {{ scope.usedUserStock}}</p>-->
      <!--        </div>-->

    </div>

    <!--    <div class="summary" style="display: flex;flex-direction: row;align-items: center;width: 113%">-->
    <!--      <div class="summary-item">line库存:-->
    <!--        <div style="color: #007BFF;display: inline;"> {{ dataForm.registerTotalStock }}</div>-->
    <!--      </div>-->
    <!--      <div class="summary-item">今日注册line:-->
    <!--        <div style="color: #28a745;display: inline;"> {{ dataForm.todayRegisterTotalNum }}</div>-->
    <!--      </div>-->
    <!--      <div class="summary-item">今日已使用账户:-->
    <!--        <div style="color: #dc3545;display: inline;">{{ dataForm.usedUserTotalStock }}</div>-->
    <!--      </div>-->
    <!--      <div class="summary-item">在线账户:-->
    <!--        <div style="color: #ffc107;display: inline;"> {{ dataForm.onlineUserTotalNum }}</div>-->
    <!--      </div>-->
    <!--    </div>-->
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
      dataForm: {
        registerTotalStock: 0,
        todayRegisterTotalNum: 0,
        usedUserTotalStock: 0,
        onlineUserTotalNum: 0
      },
      lineRegisterSummary: [],
      userSummary: []
    }
  },
  activated() {
    this.queryLineRegisterCount()
    this.queryUserSummary()
  },
  methods: {
    queryLineRegisterCount() {
      this.$http({
        url: this.$http.adornUrl(`/ltt/cdlineregister/queryLineRegisterSummary`),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0 && data.lineRegisterSummary != null) {
          this.lineRegisterSummary = data.lineRegisterSummary
          let registerStockTemp = 0
          let todayRegisterNumTemp = 0
          for (let i = 0; i < data.lineRegisterSummary.length; i++) {
            registerStockTemp += data.lineRegisterSummary[i].registerStock
            todayRegisterNumTemp += data.lineRegisterSummary[i].todayRegisterNum
          }
          this.dataForm.registerTotalStock = registerStockTemp
          this.dataForm.todayRegisterTotalNum = todayRegisterNumTemp
        } else {
          this.dataForm.registerTotalStock = 0
          this.dataForm.todayRegisterTotalNum = 0
          this.lineRegisterSummary = []
        }
      })
    },
    queryUserSummary() {
      this.$http({
        url: this.$http.adornUrl(`/ltt/atuser/queryUserSummary`),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0 && data.userSummary != null) {
          this.userSummary = data.userSummary
          let usedUserStockTemp = 0
          let onlineUserNumTemp = 0
          for (let i = 0; i < data.userSummary.length; i++) {
            usedUserStockTemp += data.userSummary[i].usedUserStock
            onlineUserNumTemp += data.userSummary[i].onlineUserNum
          }
          this.dataForm.usedUserTotalStock = usedUserStockTemp
          this.dataForm.onlineUserTotalNum = onlineUserNumTemp
        } else {
          this.dataForm.usedUserTotalStock = 0
          this.dataForm.onlineUserTotalNum = 0
          this.userSummary = []
        }
      })
    }
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
  margin: 10px;         /* 添加外边距避免模块紧贴在一起 */
  min-height: 100px;
}

.summary-item {
  //给容器添加圆角。
  border-radius: 8px;
  //添加轻微的阴影效果，以提高层次感
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow-x: auto; /* 如果内容太多，出现水平滚动条 */
  border: 2px solid #dcdcdc; /* 明显的边框 */
  box-shadow: 0 4px 8px rgba(0,0,0,0.15); /* 更深的阴影效果 */
  width: 80%; /* 全宽度使得居中更为明显，也可根据实际需要调整宽度 */
  margin: 10px 10px; /* Optional: Add margins to each item for better spacing */
  padding: 20px 20px;  /* 增大内边距，使文字与边框之间的空间更大 */
  font-size: 18px;     /* 特别大的字体大小 */
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
    font-size: 18px;     /* 特别大的字体大小 */

    //font-weight: bold; /* 加粗字体 */
  }
}

</style>

