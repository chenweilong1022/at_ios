<template>
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
        <el-button v-if="isAuth('ltt:cdphonefilter:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        prop="recordId"
        header-align="center"
        align="center"
        label="记录编号">
      </el-table-column>
      <el-table-column
        prop="taskStatus"
        header-align="center"
        align="center"
        label="任务状态">
        <template slot-scope="scope">
          <el-tag v-for="item in taskStatusCodes" :key="item.key" v-if="scope.row.taskStatus === item.key">
            {{ item.value }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="totalCount"
        header-align="center"
        align="center"
        label="总数量">
      </el-table-column>
      <el-table-column
        prop="successCount"
        header-align="center"
        align="center"
        label="成功数量">
      </el-table-column>
      <el-table-column
        prop="failCount"
        header-align="center"
        align="center"
        label="失败数量">
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
<!--          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.recordId)">修改</el-button>-->
<!--          <el-button type="text" size="small" @click="deleteHandle(scope.row.recordId)">删除</el-button>-->
          <el-button type="text" size="small" @click="exportTxt(scope.row.recordId)">导出token</el-button>
          <el-button type="text" size="small" @click="detailList(scope.row.recordId)">详情</el-button>
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
    <detail-list v-if="detailListVisible" ref="detailList" @refreshDataList="getDataList"></detail-list>

  </div>
</template>

<script>
import AddOrUpdate from './cdphonefilter-add-or-update'
import DetailList from "./cdphonefilter";
  export default {
    data () {
      return {
        dataForm: {
          taskStatus: null
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        detailListVisible: false,
        dataListLoading: false,
        dataListSelections: [],
        taskStatusCodes: [{ key: 2, value: "查询中"}, { key: 3, value: "查询完成"}, { key: 4, value: "查询失败"}],
        addOrUpdateVisible: false
      }
    },
    components: {
      DetailList,
      AddOrUpdate
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/ltt/cdphonefilter/recordList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
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
          return item.recordId
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/cdphonefilterrecord/delete'),
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
      exportTxt (recordId) {
        // window.open(this.$http.adornUrl(`/ltt/cdphonefilter/exportSJ?recordId=${recordId}&token=${this.$cookie.get('token')}`));
        this.$http({
          url: this.$http.adornUrl(`/ltt/cdphonefilter/exportSJ?recordId=${recordId}&token=${this.$cookie.get('token')}`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            data.fileUrl.map(item => {
              console.log(item)
              var fileName = this.getFilename(item)
              var link = document.createElement('a')
              // 这里是将url转成blob地址，
              fetch(item).then((res) => res.blob())
                .then((blob) => {
                  // 将链接地址字符内容转变成blob地址
                  link.href = URL.createObjectURL(blob)
                  console.log(link.href)
                  /**
                   * 下载的文件的名字
                   */
                  link.download = fileName
                  document.body.appendChild(link)
                  link.click()
                })
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      getFilename(url) {
        // 从图片链接中提取文件名
        return url.substring(url.lastIndexOf('/')+1)
      },
      detailList (recordId) {
        this.detailListVisible = true
        this.$nextTick(() => {
          this.$refs.detailList.init(recordId)
        })
        // this.$nextTick(() => {
        //   this.$router.push({name: 'ltt-accountdetails', query: { sysUserId: sysUserId, sysUsername: sysUsername}})
        // })
      }
    }
  }
</script>
