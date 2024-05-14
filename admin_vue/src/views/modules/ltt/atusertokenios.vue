<template>
  <el-dialog
    :title="'设置设备名称'"
    :close-on-click-modal="false"
    :visible.sync="visibleFlag">
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
      <el-form-item label="国号(区号)" prop="country">
        <el-select
          v-model="dataForm.country"
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
      <el-form-item label="是否还原" prop="reductionFlag">
        <el-select
          v-model="dataForm.reductionFlag"
          filterable clearable
          placeholder="请选择是否已还原">
          <el-option
            v-for="item in reductionFlagCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('ltt:atusertokenios:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        prop="country"
        header-align="center"
        align="center"
        label="country">
      </el-table-column>
      <el-table-column
        prop="bundleId"
        header-align="center"
        align="center"
        label="bundleId">
      </el-table-column>
      <el-table-column
        prop="userName"
        header-align="center"
        align="center"
        label="userName">
      </el-table-column>
      <el-table-column
        prop="phoneNumber"
        header-align="center"
        align="center"
        label="phoneNumber">
      </el-table-column>
      <el-table-column
        prop="mid"
        header-align="center"
        align="center"
        label="mid">
      </el-table-column>
<!--      <el-table-column-->
<!--        prop="useFlag"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        label="使用状态">-->
<!--      </el-table-column>-->
      <el-table-column
        prop="reductionFlag"
        header-align="center"
        align="center"
        label="还原状态">
        <template slot-scope="scope">
          <el-tag v-for="item in reductionFlagCodes" :key="item.key" v-if="scope.row.reductionFlag === item.key">
            {{ item.value }}
          </el-tag>
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
          <el-button type="text" size="small" @click="restoreDataHandle(scope.row.id)">还原</el-button>
          <el-button type="text" size="small" @click="backupHandle(scope.row.id)">备份</el-button>
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
  </el-dialog>
</template>

<script>
  import AddOrUpdate from './atusertokenios-add-or-update'
  export default {
    data () {
      return {
        visibleFlag: false,
        dataForm: {
          reductionFlag: null,
          sysUserId: null,
          country: null
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        reductionFlagCodes: [{ key: 1, value: "未还原"}, { key: 0, value: "已还原"}],
        sysUserAccountOptions: [],
        countryCodes: []
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
      this.getCountryCodes()
    },
    methods: {
      init (deviceId, deviceName) {
        this.visibleFlag = true
        this.dataForm.deviceId = deviceId
        this.dataForm.deviceName = deviceName
        this.getDataList(deviceId)
        this.getCountryCodes()
        this.queryBySearchWord()
      },
      // 获取数据列表
      getDataList (deviceId) {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/ltt/atusertokenios/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'deviceId': this.dataForm.deviceId,
            'reductionFlag': this.dataForm.reductionFlag,
            'sysUserId': this.dataForm.sysUserId,
            'country': this.dataForm.country
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
      backupHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '备份' : '批量备份'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atusertokenios/backUp'),
            method: 'post',
            data: this.$http.adornData({
              ids: ids
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
      restoreDataHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '还原' : '批量还原'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atusertokenios/taskIosFind'),
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
            url: this.$http.adornUrl('/ltt/atusertokenios/delete'),
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
