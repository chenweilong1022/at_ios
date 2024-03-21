<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.contactKey" placeholder="好友手机号" clearable></el-input>
      </el-form-item>
      <el-form-item>
      <el-input v-model="dataForm.telephone" placeholder="主号手机号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="dataForm.userGroupId"
          filterable clearable
          remote
          placeholder="主账号分组"
          :remote-method="queryUserGroupBySearchWord"
          :loading="loading">
          <el-option
            v-for="item in userGroupOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
          <el-select
            v-model="dataForm.userStatus"
            class="m-2" clearable
            placeholder="主账号状态"
            style="width: 240px">
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
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
      <el-form-item>
        <el-select
          v-model="dataForm.customerServiceId"
          filterable clearable
          remote
          placeholder="选择客服"
          :remote-method="queryCustomerByFuzzyName"
          :loading="loading">
          <el-option
            v-for="item in customerUserOptions"
            :key="item.userId"
            :label="item.nickname"
            :value="item.userId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('ltt:atdatasubtask:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('ltt:atdatasubtask:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
        prop="picturePath"
        header-align="center"
        align="center"
        label="头像">
        <template slot-scope="scope">
          <img style="width: 40px;height: 40px" :src="'https://profile.line-scdn.net' + scope.row.picturePath">
        </template>
      </el-table-column>
      <el-table-column
        prop="displayName"
        header-align="center"
        align="center"
        label="昵称">
      </el-table-column>
      <el-table-column
        prop="mid"
        header-align="center"
        align="center"
        label="好友uid">
      </el-table-column>
      <el-table-column
        prop="contactKey"
        header-align="center"
        align="center"
        label="好友手机号">
      </el-table-column>
      <el-table-column
        prop="telephone"
        header-align="center"
        align="center"
        label="主号手机号">
      </el-table-column>
      <el-table-column
        prop="userStatus"
        header-align="center"
        align="center"
        label="主号状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" size="small" type="danger">主号未验证</el-tag>
          <el-tag v-else-if="scope.row.status === 2" size="small" type="danger">主号封号</el-tag>
          <el-tag v-else-if="scope.row.status === 3" size="small" type="danger">主号下线</el-tag>
          <el-tag v-else-if="scope.row.status === 4" size="small" type="success">主号在线</el-tag>
          <el-tag v-else size="small" type="danger">主号数据错误</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="taskStatus"
        header-align="center"
        align="center"
        label="加友方式">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.taskStatus == null" size="small">主动添加</el-tag>
          <el-tag v-else size="small">被动添加</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        header-align="center"
        align="center"
        label="更新时间">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
<!--          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>-->
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
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
  import AddOrUpdate from './atdatasubtask-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          contactKey: null,
          telephone: null,
          userGroupId: null,
          userStatus: null,
          customerServiceId: null,
          updateStartTime: null,
          updateEndTime: null,
          timeKey: null
        },
        dataList: [],
        userGroupOptions: [],
        customerUserOptions: [],
        statusOptions: [
          {
            value: 1,
            label: '未验证'
          },
          {
            value: 2,
            label: '封号'
          },
          {
            value: 3,
            label: '下线'
          },
          {
            value: 4,
            label: '在线'
          },
          {
            value: 5,
            label: '数据错误'
          }
        ],
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
    },
    methods: {
      // 获取数据列表
      getDataList() {
        this.dataListLoading = true
        var updateStartTime = null
        var updateEndTime = null
        if (this.dataForm.timeKey != null && this.dataForm.timeKey.length >= 2) {
          updateStartTime = this.dataForm.timeKey[0]
          updateEndTime = this.dataForm.timeKey[1]
        }
        this.$http({
          url: this.$http.adornUrl('/ltt/atdatasubtask/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'contactKey': this.dataForm.contactKey,
            'telephone': this.dataForm.telephone,
            'userGroupId': this.dataForm.userGroupId,
            'userStatus': this.dataForm.userStatus,
            'customerServiceId': this.dataForm.customerServiceId,
            'updateStartTime': updateStartTime,
            'updateEndTime': updateEndTime
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
      /*
       根据搜索词，查询用户分组
    */
      queryUserGroupBySearchWord (serchKey) {
        serchKey = serchKey == null ? '' : serchKey + ''
        this.$http({
          url: this.$http.adornUrl(`/ltt/atusergroup/queryByFuzzyName?searchWord=${serchKey}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.userGroupOptions = data.groupList
          }
        })
      },
      /*
       根据搜索词，查询客服
    */
      queryCustomerByFuzzyName (serchKey) {
        serchKey = serchKey == null ? '' : serchKey + ''
        this.$http({
          url: this.$http.adornUrl(`/ltt/customeruser/queryCustomerByFuzzyName?key=${serchKey}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.customerUserOptions = data.customerList
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
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atdatasubtask/delete'),
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
