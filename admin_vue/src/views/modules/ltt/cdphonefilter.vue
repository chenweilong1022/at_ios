<template>
  <el-dialog
    :title="'手机号筛选详情列表'"
    :close-on-click-modal="false"
    :visible.sync="visibleFlag">
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select
          v-model="dataForm.taskStatus"
          filterable clearable
          placeholder="请选择任务状态">
          <el-option
            v-for="item in taskStatusCodes"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button @click="reallocateToken()">子任务失败重试</el-button>
<!--        <el-button v-if="isAuth('ltt:cdphonefilter:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
<!--        <el-button v-if="isAuth('ltt:cdphonefilter:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        prop="taskStatus"
        header-align="center"
        align="center"
        label="任务状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.taskStatus === 3">
            {{scope.row.taskStatusStr}}
          </el-tag>
          <el-tag v-else>
            {{scope.row.taskStatus2Str}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="contactKey"
        header-align="center"
        align="center"
        label="手机号">
      </el-table-column>
      <el-table-column
        prop="mid"
        header-align="center"
        align="center"
        label="mid">
      </el-table-column>
      <el-table-column
        prop="displayName"
        header-align="center"
        align="center"
        label="名称">
      </el-table-column>
      <el-table-column
        prop="msg"
        header-align="center"
        align="center"
        label="失败备注">
      </el-table-column>
      <el-table-column
        prop="createdTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
<!--      <el-table-column-->
<!--        fixed="right"-->
<!--        header-align="center"-->
<!--        align="center"-->
<!--        width="150"-->
<!--        label="操作">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>-->
<!--          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>-->
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
  </el-dialog>
</template>

<script>
  import AddOrUpdate from './cdphonefilter-add-or-update'
  export default {
    data () {
      return {
        visibleFlag: false,
        dataForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        taskStatusCodes: [{ key: 2, value: "查询中"}, { key: 3, value: "查询完成"}, { key: 4, value: "查询失败"}],
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
      init (recordId) {
        this.pageIndex =1
        this.visibleFlag = true
        this.dataForm.recordId = recordId
        this.getDataList(recordId)
      },
      // 获取数据列表
      getDataList (recordId) {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/ltt/cdphonefilter/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'recordId': this.dataForm.recordId,
            'taskStatus': this.dataForm.taskStatus
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
      // 获取数据列表
      reallocateToken () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl(`/ltt/cdphonefilter/reallocateToken/${this.dataForm.recordId}`),
          method: 'get'
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
          }
        }).finally(() => {
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
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/cdphonefilter/delete'),
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
