<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item label="设备id" prop="deviceId">
        <el-input v-model="dataForm.deviceId" placeholder="设备id"></el-input>
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceName">
        <el-input v-model="dataForm.deviceName" placeholder="设备名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        prop="deviceId"
        header-align="center"
        align="center"
        label="设备id">
      </el-table-column>
      <el-table-column
        prop="deviceName"
        header-align="center"
        align="center"
        label="设备名称">
      </el-table-column>
      <el-table-column
        prop="deviceCount"
        header-align="center"
        align="center"
        label="数据总数">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="updateDeviceName(scope.row.deviceId)">设置设备名称</el-button>
          <el-button type="text" size="small" @click="tokenListHandle(scope.row.deviceId, scope.row.deviceName)">token列表</el-button>
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
    <update-device-name v-if="updateDeviceNameVisible" ref="updateDeviceName" @refreshDataList="getDataList"></update-device-name>
    <token-list v-if="tokenListVisible" ref="tokenList" @refreshDataList="getDataList"></token-list>
  </div>
</template>

<script>
import UpdateDeviceName from './atusertokeniosdevice-update'
import TokenList from './atusertokenios'

export default {
  data() {
    return {
      dataForm: {
        deviceId: null,
        deviceName: null
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      updateDeviceNameVisible: false,
      tokenListVisible: false,
      sysUserAccountOptions: [],
      countryCodes: []
    }
  },
  components: {
    UpdateDeviceName,
    TokenList
  },
  activated() {
    this.getDataList()
    this.getCountryCodes()
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/ltt/atusertokenios/queryDevicePage'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'deviceId': this.dataForm.deviceId,
          'deviceName': this.dataForm.deviceName
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
    tokenListHandle (deviceId, deviceName) {
      this.tokenListVisible = true
      this.$nextTick(() => {
        this.$refs.tokenList.init(deviceId, deviceName)
      })
      // this.$nextTick(() => {
      //   this.$router.push({name: 'ltt-accountdetails', query: { sysUserId: sysUserId, sysUsername: sysUsername}})
      // })
    },
    updateDeviceName (id) {
      this.updateDeviceNameVisible = true
      this.$nextTick(() => {
        this.$refs.updateDeviceName.init(id)
      })
    }
  }
}
</script>
