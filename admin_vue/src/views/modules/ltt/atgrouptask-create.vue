<template>
  <div class="mod-config" v-loading="isLoading">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
      <div class="group-container" v-if="dataFormGroupTask.taskStatus === 1 || dataFormGroupTask.taskStatus === 2">

        <div class="img-logo">
          <img src="~@/assets/250px-Bagua-name-earlier.svg.png">


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
              <el-button @click="startGroupHandler" type="success">开始拉群</el-button>
              <el-button @click="onGroupPreHandler" type="primary">预览</el-button>
              <el-button @click="hide" type="info">隐藏表格</el-button>
              <el-button @click="show" type="primary">显示表格</el-button>
              <el-button @click="onGroupPreExportHandler" type="danger">导出剩余粉</el-button>
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
          <el-form-item>
            <el-checkbox v-model="dataForm.autoFill">账号填充</el-checkbox>
          </el-form-item>

          <el-form-item v-if="groupType === 6" label="合群号国家">
            <el-select v-model="dataForm.countryCodeH" placeholder="合群号国家" clearable>
              <el-option
                v-for="item in countryCodes"
                :key="item.key"
                :label="item.value"
                :value="item.key">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item v-if="groupType === 6" label="合群号分组" prop="userGroupIdH">
            <el-select v-model="dataForm.userGroupIdH" placeholder="合群号分组">
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
            <el-input v-model="dataForm.groupCountTotal" placeholder="群人数" style="width: 70%;"></el-input>
          </el-form-item>
          <el-form-item label="拉群号数量" prop="pullGroupNumber">
            <el-input v-model="dataForm.pullGroupNumber" placeholder="拉群号数量" style="width: 70%;"></el-input>
          </el-form-item>
          <el-form-item label="加粉间隔秒数" prop="intervalSecond">
            <el-input v-model="dataForm.intervalSecond" placeholder="加粉间隔秒数" style="width: 70%;"></el-input>
          </el-form-item>
          <el-form-item v-if="groupType === 1" label="搜索间隔秒数" prop="searchIntervalSecond">
            <el-input v-model="dataForm.searchIntervalSecond" placeholder="搜索间隔秒数" style="width: 70%;"></el-input>
          </el-form-item>
          <el-form-item label="代理ip" class="red-form-item" >
            <el-select v-model="dataForm.ipCountryCode" placeholder="代理ip" clearable class="red-select">
              <el-option
                v-for="item in countryCodes"
                :key="item.key"
                :label="item.value"
                :value="item.key">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="加粉打开app">
            <el-select v-model="dataForm.openApp" placeholder="加粉打开app" clearable>
              <el-option
                v-for="item in openAppOptions"
                :key="item.key"
                :label="item.value"
                :value="item.key">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="加粉结束自动拉群">
            <el-select v-model="dataForm.autoPullGroup" placeholder="加粉结束自动拉群" clearable>
              <el-option
                v-for="item in openAppOptions"
                :key="item.key"
                :label="item.value"
                :value="item.key">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="群名是否随机">
            <el-select v-model="dataForm.randomGroupName" placeholder="群名是否随机" clearable>
              <el-option
                v-for="item in openAppOptions"
                :key="item.key"
                :label="item.value"
                :value="item.key">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="改群名号国家" prop="changeGroupCountryCode" v-if="dataForm.randomGroupName === 2">
            <el-select v-model="dataForm.changeGroupCountryCode"   placeholder="改群名号国家" clearable>
              <el-option
                v-for="item in countryCodes"
                :key="item.key"
                :label="item.value"
                :value="item.key">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="改群名号分组" prop="changeGroupId" v-if="dataForm.randomGroupName === 2" >
            <el-select v-model="dataForm.changeGroupId"   placeholder="改群名号分组">
              <el-option
                v-for="item in dataUserGroupList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="一号几群" prop="accountGroupDistributed" v-if="dataForm.randomGroupName === 2">
            <el-input v-model="dataForm.accountGroupDistributed" placeholder="一号几群" style="width: 70%;"></el-input>
          </el-form-item>

        </div>
      </div>
      <div v-else>
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item prop="groupStatusList">
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
            <el-select
              v-model="dataForm.taskStatus"
              clearable
              placeholder="加粉"
              size="large">
              <el-option
                v-for="item in taskStatuss"
                :key="item.key"
                :label="item.value"
                :value="item.key"
              />
            </el-select>
          <el-button @click="getDataList()">查询</el-button>
          <el-button type="primary" @click="nextGroup()">继续拉群</el-button>
          <el-button type="success" @click="startTaskHandle()" :disabled="dataListSelections.length <= 0">启动任务</el-button>
          <el-button type="info" @click="exportHandle()">导出报表</el-button>
          <el-button type="success" @click="startGroup14Handler()" :disabled="dataListSelections.length <= 0">开始拉群</el-button>
          <el-button type="danger" @click="reallocateTokenHandle()" :disabled="dataListSelections.length <= 0">重新分配账号拉群</el-button>
          <el-button type="primary" @click="updateGroupHandle()" :disabled="dataListSelections.length <= 0">修改群名</el-button>
          <el-button type="info" @click="getRealGroupNameHandle()" :disabled="dataListSelections.length <= 0">获取真实群名称</el-button>
            <div style="margin-top: 20px;">
          <el-button type="primary" @click="copyPhoneHandle()" :disabled="dataListSelections.length <= 0">复制手机号</el-button>
          <el-button type="primary" @click="pushGroupSubtaskHandle()" :disabled="dataListSelections.length <= 0">推动拉群</el-button>
          <el-button type="primary" @click="syncNumberPeopleHandle()" :disabled="dataListSelections.length <= 0">更新群人数</el-button>
          <el-button type="danger" @click="userRegisterRetryHandle()" :disabled="dataListSelections.length <= 0">拉群号重注册</el-button>
            </div>
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
            align="center" width="50"
            label="id">
          </el-table-column>
          <el-table-column
            prop="groupName"
            header-align="center"
            align="center"  width="80"
            label="群名称">
          </el-table-column>
          <el-table-column
            prop="realGroupName"
            header-align="center"
            align="center"  width="80"
            label="真实群名称">
          </el-table-column>
          <el-table-column
            prop="roomId"
            header-align="center"
            align="center"  width="110"
            label="群号">
          </el-table-column>
          <el-table-column
            prop="successfullyAttractGroupsNumber"
            header-align="center" width="50"
            align="center"
            label="群人数">
          </el-table-column>
          <el-table-column
            prop="userId"
            header-align="center"
            align="center" width="50"
            label="拉群userId">
          </el-table-column>
          <el-table-column
            prop="userTelephone"
            header-align="center"
            align="center" width="130"
            label="拉群手机号">
            <template slot-scope="scope">
              <div>
                <el-badge :value="scope.row.phoneRegisterCount" :class="badgeClass(scope.row.phoneState)" class="item">
                  <el-button type="text" @click="atUserHandle(scope.row.userId)">{{scope.row.userTelephone }}</el-button>
                </el-badge>
              </div>
              <!-- 复制按钮 -->
              <div><el-button type="text" v-if="scope.row.userTelephone != null" @click="copyPhoneHandle(scope.row.userTelephone)">复制</el-button></div>
            </template>
          </el-table-column>
          <el-table-column
            prop="groupStatusStr"
            header-align="center"
            align="center"
            width="100"
            label="拉群状态">
            <template slot-scope="scope">
              <el-tag size="small" type="danger">{{ scope.row.groupStatusStr }}</el-tag>
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
            width="110"
            label="加粉数据">
            <template slot-scope="scope">
              <div>加粉总数：{{ scope.row.addTotalQuantity }}</div>
              <div>成功数：{{ scope.row.successfulQuantity }}</div>
              <div>失败数：{{ scope.row.failuresQuantity }}</div>
            </template>
          </el-table-column>
          <el-table-column
            prop="randomGroupName"
            header-align="center"
            align="center" width="50"
            label="群名是否随机">
            <template slot-scope="scope">
              <el-tag v-for="item in openAppOptions" :key="item.key" v-if="scope.row.randomGroupName === item.key">
                {{ item.value }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="taskStatus"
            header-align="center"
            align="center"
            width="90"
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
            width="90"
            label="加粉进度">
            <template slot-scope="scope">
              <el-progress :stroke-width="7" type="circle"  :width="70" :percentage="scope.row.scheduleFloat"></el-progress>
            </template>
          </el-table-column>
          <el-table-column
            prop="changeUserPhone"
            header-align="center"
            align="center" width="130"
            label="修改群水军手机号">
            <template slot-scope="scope">
              <div><el-button type="text" @click="atUserHandle(scope.row.changeUserId)">{{scope.row.changeUserPhone}}</el-button></div>
              <!-- 复制按钮 -->
              <div><el-button type="text" @click="copyPhoneHandle(scope.row.changeUserPhone)">复制</el-button></div>
            </template>
          </el-table-column>
          <el-table-column
            prop="msg"
            header-align="center"
            align="center"
            width="100"
            label="日志">
          </el-table-column>
          <el-table-column
            prop="tIdBy"
            header-align="center"
            align="center"
            width="60"
            label="服务器">
          </el-table-column>
          <el-table-column
            fixed="right"
            header-align="center"
            align="center"
            width="150"
            label="操作">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="errLogsHandle(scope.row.id)">错误日志</el-button>
              <el-button type="danger" size="small"
                         v-if="(scope.row.msg != null && scope.row.msg.indexOf('网络异常') !== -1) ||
                         (scope.row.roomId != null && scope.row.successfullyAttractGroupsNumber == 0)"
                         @click="errRetryHandle(scope.row.id)">错误重试</el-button>
              <el-button type="primary" @click="pushGroupSubtaskHandle(scope.row.id)">推动拉群</el-button>
              <el-button type="danger" v-if="scope.row.showUserRegisterFlag === true"
                         @click="userRegisterRetryHandle(scope.row.id)">拉群号重新注册</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20,30, 50, 100,2000]"
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
        taskStatuss: [],
        openAppOptions: [],
        judgeOptions: [],
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
          autoFill: false,
          userGroupIdH: null,
          changeGroupCountryCode: null,
          changeGroupId: null,
          groupType: null,
          groupName: '',
          countryCode: 66,
          countryCodeH: 81,
          groupCountTotal: 94,
          pullGroupNumber: 1,
          groupCount: null,
          taskStatus: null,
          groupCountStart: 0,
          intervalSecond: 7,
          searchIntervalSecond: null,
          ipCountryCode: 81,
          autoPullGroup: 2,
          randomGroupName: 1,
          accountGroupDistributed: 1,
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
      this.getTaskStatusEnums()
      this.getOpenApps()
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
            'taskStatus': this.dataForm.taskStatus,
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
      badgeClass (state) {
        if (state === true) {
          return 'green-badge'
        }
        if (state === false) {
          return 'red-badge'
        }
      },
      // 拉群账号
      atUserHandle (userId) {
        this.$nextTick(() => {
          this.$router.push({name: 'ltt-atuser', query:{ userId: userId}})
        })
      },
      copyPhoneHandle (telephone) {
        var telephones = telephone ? [telephone] : this.dataListSelections.map(item => {
          return item.userTelephone
        })
        navigator.clipboard.writeText(telephones).then(() => {
          this.$message.success('手机号复制成功！')
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
      errRetryHandle (id) {
        this.$confirm(`确定进行拉群重试操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atgroup/errRetryGroup/' + id),
            method: 'post'
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
      getTaskStatusEnums () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/taskStatus`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.taskStatuss = data.data
          } else {
            this.$message.error(data.msg)
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
      getOpenApps () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getOpenApps`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.openAppOptions = data.data
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
      updateGroupHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '重新修改群名称' : '批量重新修改群名称'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.isLoading = true
          this.$nextTick(() => {
            this.$http({
              url: this.$http.adornUrl('/ltt/atgroup/updateGroupName'),
              method: 'post',
              data: this.$http.adornData({
                'ids': ids
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功，成功修改' + data.successCount + '条',
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
      getRealGroupNameHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '获取真实群名称' : '批量获取真实群名称'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.isLoading = true
          this.$nextTick(() => {
            this.$http({
              url: this.$http.adornUrl('/ltt/atgroup/getRealGroupName'),
              method: 'post',
              data: this.$http.adornData({
                'ids': ids
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
      syncNumberPeopleHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '同步群人数' : '批量同步群人数'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.isLoading = true
          this.$nextTick(() => {
            this.$http({
              url: this.$http.adornUrl('/ltt/atgroup/syncNumberPeople'),
              method: 'post',
              data: this.$http.adornData({
                'ids': ids
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
                'ids': ids
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
          window.open(this.$http.adornUrl(`/ltt/atgroup/importZip?token=${this.$cookie.get('token')}&id=${this.dataForm.id}`))
        })
      },
      startGroup14Handler (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '开始拉群' : '批量开始拉群'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atgroup/startGroup'),
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
      pushGroupSubtaskHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定推动操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atgroup/pushGroupSubtask'),
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
      userRegisterRetryHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定拉群号重新注册操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atgroup/groupFailRegisterAgains'),
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
      startGroupHandler () {
        this.isLoading = true
        console.log(this.dataForm.autoFill)
        this.$http({
          url: this.$http.adornUrl('/ltt/atgrouptask/onGroupStart'),
          method: 'POST',
          data: this.$http.adornData({
            'id': this.dataForm.id,
            'groupName': this.dataForm.groupName,
            'countryCode': this.dataForm.countryCode,
            'autoFill': this.dataForm.autoFill,
            'countryCodeH': this.dataForm.countryCodeH,
            'intervalSecond': this.dataForm.intervalSecond,
            'searchIntervalSecond': this.dataForm.searchIntervalSecond,
            'ipCountryCode': this.dataForm.ipCountryCode,
            'autoPullGroup': this.dataForm.autoPullGroup,
            'randomGroupName': this.dataForm.randomGroupName,
            'userGroupId': this.dataForm.userGroupId,
            'changeGroupCountryCode': this.dataForm.changeGroupCountryCode,
            'changeGroupId': this.dataForm.changeGroupId,
            'userGroupIdH': this.dataForm.userGroupIdH,
            'navyUrlList': this.dataForm.navyUrlList,
            'groupCountStart': this.dataForm.groupCountStart,
            'openApp': this.dataForm.openApp,
            'accountGroupDistributed': this.dataForm.accountGroupDistributed,
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
<style lang="scss" scoped>
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
        margin-left: 66px;
        .title {
          margin: 10px 0px;
          text-align: center;
          font-size: 30px;
          font-weight: bold;
        }
      }
    }
  }
}
.item {
  margin-top: 10px;
}
/* 定义绿色背景样式 */
/deep/ .green-badge .el-badge__content {
  background-color: green;
  color: white;
}

/* 定义红色背景样式 */
/deep/ .red-badge .el-badge__content {
  background-color: red;
  color: white;
}
.red-form-item /deep/ .el-form-item__label {
  color: red !important;
}

.red-select /deep/ .el-input__inner {
  border-color: red !important;
  color: red !important;
}
</style>
