<template>
  <div class="mod-config" v-loading="isLoading">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
      <div class="group-container" v-if="dataFormGroupTask.taskStatus === 1 || dataFormGroupTask.taskStatus === 2">

        <div class="img-logo">
          <img src="~@/assets/250px-Bagua-name-earlier.svg.png">

          <div class="title">拉群配置</div>
          <el-form-item label="拉群号国家">
            <el-select v-model="dataForm.countryCode" placeholder="拉群号国家" clearable>
              <el-option
                v-for="item in countryCodes"
                :key="item.key"
                :label="item.value"
                :value="item.key">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="拉群号分组" prop="userGroupId">
            <el-select v-model="dataForm.userGroupId" placeholder="账户分组">
              <el-option
                v-for="item in dataUserGroupList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="拉群类型" prop="groupType">
            <el-select
              v-model="groupType"
              @change="groupTypeChangeHandler"
              class="m-2"
              placeholder="选择类型"
              size="large"
              style="width: 240px">
              <el-option
                v-for="item in options"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="群人数" prop="groupCountTotal">
            <el-input v-model="dataForm.groupCountTotal" placeholder="群人数"></el-input>
          </el-form-item>
          <el-form-item label="拉群号数量" prop="pullGroupNumber">
            <el-input v-model="dataForm.pullGroupNumber" placeholder="拉群号数量"></el-input>
          </el-form-item>

        </div>

        <div class="group-content">
          <div class="title">自定义群名</div>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="" prop="groupName">
                <el-input v-model="dataForm.groupName" placeholder="自定义群名"></el-input>
              </el-form-item>
            </el-col>

            <el-col :span="6">
              <el-form-item label="" prop="groupCountStart">
                <el-input v-model="dataForm.groupCountStart" placeholder="从哪里开始"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="" prop="groupCount">
                <el-input v-model="dataForm.groupCount" placeholder="数量"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-tooltip class="item" :content="null" effect="dark" placement="top-start">
                <template slot="content">
                  <div class="tooltip-content">
                    <pre v-for="item in fileContentList">{{item}}</pre>
                  </div>
                </template>
                <el-button @click="onMouseOver">查看群名</el-button>
              </el-tooltip>
            </el-col>
          </el-row>

          <el-row  :gutter="20">

            <el-col :span="12">
              <div class="title">水军</div>
              <el-upload
                drag
                v-model:file-list="navyUrlFileList"
                :action="uploadUrl"
                :on-success="handleNavyUrlListHandler"
                :on-preview="handleNavyUrlListHandlerPreview"
                :on-remove="handleNavyUrlListHandlerRemove"
                multiple
                style="text-align: center;">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              </el-upload>
            </el-col>



            <el-col :span="12">
              <div class="title">料子</div>
              <el-upload
                drag
                v-model:file-list="materialUrlFileList"
                :action="uploadUrl"
                :on-success="handleMaterialUrlListHanlder"
                :on-preview="handleMaterialUrlListHanlderPreview"
                :on-remove="handleMaterialUrlListHanlderRemove"
                multiple
                style="text-align: center;">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              </el-upload>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="24">
              <el-button @click="startGroupHandler">开始拉群</el-button>
              <el-button @click="onGroupPreHandler">预览</el-button>
              <el-button @click="hide">隐藏表格</el-button>
              <el-button @click="show">显示表格</el-button>
              <el-button @click="onGroupPreExportHandler">导出剩余粉</el-button>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-table
                v-if="tableDataFlag"
                :data="tableData"
                style="width: 100%">
                <el-table-column :label="remaining" align="center">
                  <el-table-column
                    prop="groupName"
                    align="center"
                    label="群名称">
                  </el-table-column>
                  <el-table-column
                    align="center"
                    prop="navyTextListsStr"
                    label="水军">
                  </el-table-column>
                  <el-table-column
                    align="center"
                    prop="materialUrlsStr"
                    label="料子">
                  </el-table-column>
                  <el-table-column
                    align="center"
                    prop="total"
                    label="总和">
                  </el-table-column>
                </el-table-column>
              </el-table>
            </el-col>
          </el-row>

        </div>

        <div class="money-container">

        </div>
      </div>
      <div v-else>
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item label="拉群状态" prop="groupStatusList">
          <el-select
            v-model:group-type-list="groupStatusList"
            multiple
            placeholder="选择类型"
            size="large">
            <el-option
              v-for="item in groupStatusCodes"
              :key="item.key"
              :label="item.value"
              :value="item.key"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">查询</el-button>
          <el-button type="primary" @click="nextGroup()">继续拉群</el-button>
          <el-button type="danger" @click="exportHandle()" :disabled="dataListSelections.length <= 0">导出报表</el-button>
          <el-button type="danger" @click="reallocateTokenHandle()" :disabled="dataListSelections.length <= 0">重新分配账号拉群</el-button>
          <el-button type="danger" @click="startTaskHandle()" :disabled="dataListSelections.length <= 0">启动任务</el-button>
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
            prop="groupName"
            header-align="center"
            align="center"
            label="群名称">
          </el-table-column>
          <el-table-column
            prop="roomId"
            header-align="center"
            align="center"
            label="群号">
          </el-table-column>
          <el-table-column
            prop="chatRoomUrl"
            header-align="center"
            align="center"
            label="群链接">
          </el-table-column>
          <el-table-column
            prop="roomTicketId"
            header-align="center"
            align="center"
            label="群二维码">
          </el-table-column>
          <el-table-column
            prop="successfullyAttractGroupsNumber"
            header-align="center"
            align="center"
            label="群人数">
          </el-table-column>
          <el-table-column
            prop="groupStatusStr"
            header-align="center"
            align="center"
            width="120"
            label="拉群状态">
            <template slot-scope="scope">
              <el-tag  size="small" type="danger">{{ scope.row.groupStatusStr }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="createTime"
            header-align="center"
            align="center"
            width="100"
            label="时间">
          </el-table-column>
          <el-table-column
            prop="addTotalQuantity"
            header-align="center"
            align="center"
            label="加粉总数">
          </el-table-column>
          <el-table-column
            prop="successfulQuantity"
            header-align="center"
            align="center"
            label="成功数">
          </el-table-column>
          <el-table-column
            prop="failuresQuantity"
            header-align="center"
            align="center"
            label="失败数">
          </el-table-column>
          <el-table-column
            prop="taskStatus"
            header-align="center"
            align="center"
            width="120"
            label="加粉状态">
            <template slot-scope="scope">
              <el-button v-if="scope.row.taskStatus === 5" type="success" plain>{{scope.row.taskStatusStr}}</el-button>
              <el-button v-if="scope.row.taskStatus === 3" type="success" plain>{{scope.row.taskStatusStr}}</el-button>
              <el-button v-if="scope.row.taskStatus === 2" type="warning" plain>{{scope.row.taskStatusStr}}</el-button>
              <el-button v-if="scope.row.taskStatus === 1" type="primary" plain>{{scope.row.taskStatusStr}}</el-button>
              <el-button v-if="scope.row.taskStatus === 0" type="primary" plain>{{scope.row.taskStatusStr}}</el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="schedule"
            header-align="center"
            align="center"
            width="220"
            label="加粉进度">
            <template slot-scope="scope">
              <el-progress :stroke-width="10" type="circle" :percentage="scope.row.scheduleFloat"></el-progress>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            header-align="center"
            align="center"
            width="150"
            label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="errLogsHandle(scope.row.dataGroupId)">错误日志</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100,2000]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>
      </div>
    </el-form>
    <reallocate-token v-if="addOrUpdateVisible" ref="reallocateToken" @refreshDataList="getDataList"></reallocate-token>
    <err-logs  v-if="errLogsVisible" ref="errLogs" @refreshDataList="getDataList"></err-logs>
  </div>
</template>

<script>
import ModalBox from './modalbox.vue'
import ReallocateToken from './atgrouptask-add-reallocate_token'
import ErrLogs from "./atdatatask-err-logs.vue";
  export default {
    data () {
      return {
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        errLogsVisible: false,
        dataListLoading: false,
        addOrUpdateVisible: false,
        dataListSelections: [],
        isLoading: false,
        groupType: null,
        groupStatusList: [],
        uploadUrl: '',
        options: [],
        groupStatusCodes: [],
        tableData: [],
        navyUrlFileList: [],
        dataUserGroupList: [],
        materialUrlFileList: [],
        isModalVisible: false,
        tableDataFlag: false,
        fileContentList: [],
        remaining: '',
        countryCodes: [],
        dataRule: {
        },
        dataFormGroupTask: {
          id: 0,
          taskName: '',
          groupType: '',
          addTotalQuantity: '',
          successfulQuantity: '',
          failuresQuantity: '',
          taskStatus: '',
          schedule: '',
          updateTime: '',
          deleteFlag: '',
          createTime: '',
          sysUserId: ''
        },
        dataForm: {
          id: null,
          userGroupId: null,
          groupType: null,
          groupName: '',
          countryCode: 66,
          groupCountTotal: 99,
          pullGroupNumber: 1,
          groupCount: null,
          groupCountStart: 0,
          navyUrlList: [],
          materialUrlList: []
        }
      }
    },
    components: {
      ErrLogs,
      ModalBox,
      ReallocateToken
    },
    activated () {
      const groupTaskId = this.$route.query.id
      this.dataForm.id = groupTaskId
      this.uploadUrl = this.$http.adornUrl(`/app/file/upload`)
      this.getCountryCodeEnums()
      this.getUserGroupDataList()
      this.getGroupType()
      this.infoById()
      this.getDataList()
      this.getGroupStatusCodes()
    },
    methods: {
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
      nextGroup () {
        this.dataFormGroupTask.taskStatus = 2
      },
      getDataList () {
        this.isLoading = true
        this.$http({
          url: this.$http.adornUrl('/ltt/atgroup/list'),
          method: 'post',
          data: this.$http.adornData({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'groupTaskId': this.dataForm.id,
            'groupStatusList': this.groupStatusList
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
        }).finally(() => {
          this.isLoading = false
        })
      },
      // 错误日志
      errLogsHandle (id) {
        this.errLogsVisible = true
        this.$nextTick(() => {
          this.$refs.errLogs.init(id)
        })
      },
      infoById () {
        this.$http({
          url: this.$http.adornUrl(`/ltt/atgrouptask/info/${this.dataForm.id}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataFormGroupTask.taskName = data.atGroupTask.taskName
            this.dataFormGroupTask.groupType = data.atGroupTask.groupType
            this.dataFormGroupTask.addTotalQuantity = data.atGroupTask.addTotalQuantity
            this.dataFormGroupTask.successfulQuantity = data.atGroupTask.successfulQuantity
            this.dataFormGroupTask.failuresQuantity = data.atGroupTask.failuresQuantity
            this.dataFormGroupTask.taskStatus = data.atGroupTask.taskStatus
            this.dataFormGroupTask.schedule = data.atGroupTask.schedule
            this.dataFormGroupTask.updateTime = data.atGroupTask.updateTime
            this.dataFormGroupTask.deleteFlag = data.atGroupTask.deleteFlag
            this.dataFormGroupTask.createTime = data.atGroupTask.createTime
            this.dataFormGroupTask.sysUserId = data.atGroupTask.sysUserId
          }
        })
      },
      getGroupType () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getGroupType`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.options = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      groupTypeChangeHandler () {
        this.dataForm.groupType = this.groupType
      },
      // 获取数据列表
      getUserGroupDataList () {
        this.$http({
          url: this.$http.adornUrl('/ltt/atusergroup/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 100
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataUserGroupList = data.page.list
          } else {
            this.dataUserGroupList = []
          }
        })
      },
      // 表单提交
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
      hide () {
        this.tableDataFlag = false;
      },
      show () {
        this.tableDataFlag = true;
      },
      reallocateTokenHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '重新分配账号' : '批量重新分配账号'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.addOrUpdateVisible = true
          this.$nextTick(() => {
            this.$refs.reallocateToken.init(ids)
          })
        })
      },
      startTaskHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '启动任务' : '批量启动任务'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.isLoading = true
          this.$nextTick(() => {
            this.$http({
              url: this.$http.adornUrl('/ltt/atgroup/startTask'),
              method: 'post',
              data: this.$http.adornData({
                'ids':ids
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
            }).finally(() => {
              this.isLoading = false
            })
          })
        })
      },
      exportHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '导出报表' : '批量导出报表'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          window.open(this.$http.adornUrl(`/ltt/atgroup/importZip?token=${this.$cookie.get('token')}&ids=${ids.join(',')}`))
        })
      },
      startGroupHandler () {
        this.isLoading = true
        this.$http({
          url: this.$http.adornUrl('/ltt/atgrouptask/onGroupStart'),
          method: 'POST',
          data: this.$http.adornData({
            'id': this.dataForm.id,
            'groupName': this.dataForm.groupName,
            'countryCode': this.dataForm.countryCode,
            'userGroupId': this.dataForm.userGroupId,
            'navyUrlList': this.dataForm.navyUrlList,
            'groupCountStart': this.dataForm.groupCountStart,
            'pullGroupNumber': this.dataForm.pullGroupNumber,
            'groupCountTotal': this.dataForm.groupCountTotal,
            'materialUrlList': this.dataForm.materialUrlList,
            'groupType': this.groupType,
            'groupCount': this.dataForm.groupCount
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.infoById()
          } else {
            this.$message.error(data.msg)
          }
        }).finally(() => {
          this.isLoading = false
        })
      },
      onGroupPreExportHandler () {
        this.isLoading = true
        this.dataForm.navyUrlList = []
        for (let i = 0; i < this.navyUrlFileList.length; i++) {
          let data = this.navyUrlFileList[i]
          this.dataForm.navyUrlList.push(data.response.data)
        }
        console.log(this.dataForm.navyUrlList)
        this.dataForm.materialUrlList = []
        for (let i = 0; i < this.materialUrlFileList.length; i++) {
          let data = this.materialUrlFileList[i]
          this.dataForm.materialUrlList.push(data.response.data)
        }
        window.open(this.$http.adornUrl(`/ltt/atgrouptask/onGroupPreExport?groupName=${this.dataForm.groupName}&groupCountStart=${this.dataForm.groupCountStart}&groupCountTotal=${this.dataForm.groupCountTotal}&groupCount=${this.dataForm.groupCount}&navyUrlList=${this.dataForm.navyUrlList.join(',')}&materialUrlList=${this.dataForm.materialUrlList.join(',')}&token=${this.$cookie.get('token')}`))
        this.isLoading = false
      },
      onGroupPreHandler () {
        this.show()
        this.isLoading = true
        this.dataForm.navyUrlList = []
        for (let i = 0; i < this.navyUrlFileList.length; i++) {
          let data = this.navyUrlFileList[i]
          this.dataForm.navyUrlList.push(data.response.data)
        }
        console.log(this.dataForm.navyUrlList)
        this.dataForm.materialUrlList = []
        for (let i = 0; i < this.materialUrlFileList.length; i++) {
          let data = this.materialUrlFileList[i]
          this.dataForm.materialUrlList.push(data.response.data)
        }
        console.log(this.dataForm.materialUrlList)
        this.$http({
          url: this.$http.adornUrl('/ltt/atgrouptask/onGroupPre'),
          method: 'POST',
          data: this.$http.adornData({
            'groupName': this.dataForm.groupName,
            'navyUrlList': this.dataForm.navyUrlList,
            'groupCountStart': this.dataForm.groupCountStart,
            'groupCountTotal': this.dataForm.groupCountTotal,
            'pullGroupNumber': this.dataForm.pullGroupNumber,
            'materialUrlList': this.dataForm.materialUrlList,
            'groupCount': this.dataForm.groupCount
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.tableData = data.onGroupPreVOS
            this.remaining = data.remaining
          } else {
            this.$message.error(data.msg)
          }
        }).finally(() => {
          this.isLoading = false
        })
      },
      getGroupStatusCodes () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getGroupStatus`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.groupStatusCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      handleMaterialUrlListHanlder (uploadFile, response, uploadFiles) {
        this.materialUrlFileList = uploadFiles
      },
      handleMaterialUrlListHanlderRemove (uploadFile, uploadFiles) {
        this.materialUrlFileList = uploadFiles
      },
      handleMaterialUrlListHanlderPreview (uploadFile, uploadFiles) {
        this.materialUrlFileList = uploadFiles
      },
      handleNavyUrlListHandler (uploadFile, response, uploadFiles) {
        this.navyUrlFileList = uploadFiles
      },
      handleNavyUrlListHandlerRemove (uploadFile, uploadFiles) {
        this.navyUrlFileList = uploadFiles
      },
      handleNavyUrlListHandlerPreview (uploadFile, uploadFiles) {
        this.navyUrlFileList = uploadFiles
      },
      onMouseOver () {
        this.isLoading = true
        this.$http({
          url: this.$http.adornUrl('/ltt/atgrouptask/getGroupNameList'),
          method: 'get',
          params: this.$http.adornParams({
            'groupName': this.dataForm.groupName,
            'groupCountStart': this.dataForm.groupCountStart,
            'groupCount': this.dataForm.groupCount,
            'pullGroupNumber': this.dataForm.pullGroupNumber,
            'groupCountTotal': this.dataForm.groupCountTotal
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.fileContentList = data.data
          } else {
            this.$message.error(data.msg)
          }
        }).finally(() => {
          this.isLoading = false
        })
      }
    }
  }
</script>
<style scoped lang="scss">
.tooltip-content {
  max-height: 100px;
  overflow-y: auto;
  white-space: normal;
}
.mod-config {
  width: 100px;
  display: flex;
  .el-form {
    flex: 1;
    display: flex;
    .group-container {
      flex: 1;
      display: flex;
      flex-direction: row;
      .img-logo {
        flex: 1;
        .title {
          margin: 10px 0px;
          text-align: center;
          font-size: 30px;
          font-weight: bold;
        }
      }
      .group-content {
        flex: 2;
        margin: 0 auto;
        text-align: center;
        font-size: 30px;
        font-weight: bold;
        .title {
          margin: 20px 0;
        }
      }
      .money-container {
        flex: 1;
      }
    }
  }
}
</style>
