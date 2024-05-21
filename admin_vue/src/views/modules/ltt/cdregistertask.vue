<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item label="任务名称">
        <el-input v-model="dataForm.taskName" placeholder="任务名称" clearable></el-input>
      </el-form-item>
      <el-form-item label="注册国家">
        <el-select v-model="dataForm.countryCode" placeholder="注册国家" clearable>
          <el-option
            v-for="item in countryCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="注册状态" prop="registrationStatus">
        <el-select
          v-model="dataForm.registrationStatus"
          filterable clearable
          placeholder="请选择注册状态">
          <el-option
            v-for="item in registrationStatusCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('ltt:cdregistertask:save')" type="primary" @click="addOrUpdateHandle()">新增注册</el-button>
        <el-button v-if="isAuth('ltt:cdregistertask:save')" type="danger" @click="cleanIpHandle()">清理ip</el-button>
        <el-button v-if="isAuth('ltt:cdregistertask:save')" type="success" @click="cdregistertaskAccountListHandle(null, '山谷', 81)">山谷</el-button>
        <el-button v-if="isAuth('ltt:cdregistertask:save')" type="success" @click="cdregistertaskAccountListHandle(null, '子弹', 8101)">子弹</el-button>
        <el-button v-if="isAuth('ltt:cdregistertask:save')" type="primary" @click="addRegisterNameHandle()">增加注册名称</el-button>
      </el-form-item>
    </el-form>
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
        prop="id"
        header-align="center"
        align="center"
        label="任务id">
      </el-table-column>
      <el-table-column
        prop="taskName"
        header-align="center"
        align="center"
        label="任务名称">
      </el-table-column>
      <el-table-column
        prop="countryCode"
        header-align="center"
        align="center"
        label="国家code">
        <template slot-scope="scope">
          <el-tag v-for="item in countryCodes" :key="item.key" v-if="scope.row.countryCode === item.key">
            {{ item.value }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="totalAmount"
        header-align="center"
        align="center"
        label="注册数据">
        <template slot-scope="scope">
          <div>总数量：{{ scope.row.totalAmount }}</div>
          <div>成功数量：{{ scope.row.numberSuccesses }}</div>
        </template>
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="numberRegistered"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="注册数量">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="schedule"
        header-align="center"
        align="center"
        label="进度">
        <template slot-scope="scope">
          <el-progress :stroke-width="10" type="circle" :percentage="scope.row.scheduleFloat"></el-progress>
        </template>
      </el-table-column>
      <el-table-column
        prop="registrationStatusStr"
        header-align="center"
        align="center"
        label="注册状态">
        <template slot-scope="scope">
          <el-button v-if="scope.row.registrationStatus === 7" type="success" plain>{{scope.row.registrationStatusStr}}</el-button>
          <el-button v-if="scope.row.registrationStatus === 3" type="info" plain>{{scope.row.registrationStatusStr}}</el-button>
          <el-button v-if="scope.row.registrationStatus === 1" type="warning" plain>{{scope.row.registrationStatusStr}}</el-button>
          <el-button v-if="scope.row.registrationStatus === 2" type="warning" plain>{{scope.row.registrationStatusStr}}</el-button>
          <el-button v-if="scope.row.registrationStatus === 6" type="warning" plain>{{scope.row.registrationStatusStr}}</el-button>
          <el-button v-if="scope.row.registrationStatus === 9" type="warning" plain>{{scope.row.registrationStatusStr}}</el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="numberThreads"
        header-align="center"
        align="center"
        label="线程数">
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
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">代理设置</el-button>
          <el-button type="text" size="small" @click="cdregistertaskAccountListHandle(scope.row.id, scope.row.taskName, scope.row.countryCode)">注册详情</el-button>
          <el-button v-if="scope.row.registrationStatus === 9" type="text" size="small" @click="deleteHandle(scope.row.id)">停止真机任务</el-button>
          <el-button v-if="scope.row.registrationStatus != 3 && scope.row.registrationStatus != 7&& scope.row.registrationStatus != 9" type="text" size="small" @click="stopRegisterTask(scope.row.id)">暂停任务</el-button>
          <el-button type="text" size="small" @click="deleteRegisterTaskHandle(scope.row.id)">删除注册任务</el-button>
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
    <clean-ip v-if="cleanIpVisible" ref="CleanIp" @refreshDataList="getDataList"></clean-ip>
    <add-username v-if="addRegisterNameVisible" ref="AddUsername" @refreshDataList="getDataList"></add-username>
    <set-proxy v-if="setProxyVisible" ref="setProxy" @refreshDataList="getDataList"></set-proxy>
    <cdregistertask-account-list v-if="cdregistertaskAccountListVisible" ref="cdregistertaskAccountList" @refreshDataList="getDataList"></cdregistertask-account-list>
  </div>
</template>

<script>
import AddOrUpdate from './cdregistertask-add-or-update'
import SetProxy from './cdregistertask-set-proxy'
import CleanIp from './cdregister-ip'
import AddUsername from './cdregistertask-username'
import CdregistertaskAccountList from './cdregistertask-account-list'
export default {
  data () {
    return {
      dataForm: {
        countryCode: null,
        registrationStatus: null,
        taskName: null
      },
      countryCodes: [],
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      registrationStatusCodes: [{key: 1, value: '新注册'}, {key: 2, value: '注册中'}, {key: 3, value: '暂停注册'}, {key: 7, value: '注册完成'}, {key: 9, value: '真机注册任务'}],
      addOrUpdateVisible: false,
      cleanIpVisible: false,
      addRegisterNameVisible: false,
      setProxyVisible: false,
      cdregistertaskAccountListVisible: false
    }
  },
  components: {
    AddOrUpdate,
    CleanIp,
    AddUsername,
    SetProxy,
    CdregistertaskAccountList
  },
  activated () {
    this.getDataList()
    this.getCountryCodeEnums()
  },
  methods: {
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/ltt/cdregistertask/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'countryCode': this.dataForm.countryCode,
          'registrationStatus': this.dataForm.registrationStatus,
          'taskName': this.dataForm.taskName
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
      if (id) {
        this.setProxyVisible = true
        this.$nextTick(() => {
          this.$refs.setProxy.init(id)
        })
      } else {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      }
    },
    cleanIpHandle () {
      this.cleanIpVisible = true
      this.$nextTick(() => {
        this.$refs.CleanIp.init()
      })
    },
    addRegisterNameHandle () {
      this.addRegisterNameVisible = true
      this.$nextTick(() => {
        this.$refs.AddUsername.init()
      })
    },
    // 新增 / 修改
    cdregistertaskAccountListHandle (id, taskName, countryCode) {
      this.cdregistertaskAccountListVisible = true
      this.$nextTick(() => {
        this.$refs.cdregistertaskAccountList.init(id, taskName, countryCode)
      })
    },
    // 删除
    deleteHandle (id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/ltt/cdregistertask/delete'),
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
    // 删除注册任务
    deleteRegisterTaskHandle (taskId) {
      this.$confirm(`确定删除操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/ltt/cdregistertask/deleteRegisterTask'),
          method: 'get',
          params: this.$http.adornParams({
            'taskId': taskId
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
    },
    // 暂停注册任务
    stopRegisterTask (id) {
      this.$confirm(`确定暂停注册任务?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/ltt/cdregistertask/stopRegisterTask'),
          method: 'post',
          params: this.$http.adornParams({
            'taskId': id
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
