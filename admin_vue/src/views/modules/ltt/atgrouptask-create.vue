<template>
  <div class="mod-config">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
      <div class="group-container">

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

        </div>

        <div class="group-content">
          <div class="title">自定义群名</div>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="" prop="groupName">
                <el-input v-model="dataForm.groupName" placeholder="自定义群名"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
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
                action="http://localhost:8880/app/file/upload"
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
                action="http://localhost:8880/app/file/upload"
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
    </el-form>
    <modal-box
      :isVisible="isModalVisible"
      :textContent="fileContent"
      @update:isVisible="isModalVisible = $event"
    />
  </div>
</template>

<script>
import ModalBox from './modalbox.vue'
  export default {
    data () {
      return {
        groupType: null,
        options: [],
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
        dataForm: {
          id: null,
          userGroupId: null,
          groupType: null,
          groupName: '',
          countryCode: 66,
          groupCount: null,
          navyUrlList: [],
          materialUrlList: []
        }
      }
    },
    components: {
      ModalBox
    },
    activated () {
      const groupTaskId = this.$route.query.id
      this.dataForm.id = groupTaskId
      this.getCountryCodeEnums()
      this.getUserGroupDataList()
      this.getGroupType()
    },
    methods: {
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
      startGroupHandler () {
        this.$http({
          url: this.$http.adornUrl('/ltt/atgrouptask/onGroupStart'),
          method: 'POST',
          data: this.$http.adornData({
            'id': this.dataForm.id,
            'groupName': this.dataForm.groupName,
            'countryCode': this.dataForm.countryCode,
            'userGroupId': this.dataForm.userGroupId,
            'navyUrlList': this.dataForm.navyUrlList,
            'materialUrlList': this.dataForm.materialUrlList,
            'groupType': this.groupType,
            'groupCount': this.dataForm.groupCount
          })
        }).then(({data}) => {
          if (data && data.code === 0) {

          } else {
            this.$message.error(data.msg)
          }
        })
      },
      onGroupPreHandler () {
        this.show()
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
        this.$http({
          url: this.$http.adornUrl('/ltt/atgrouptask/getGroupNameList'),
          method: 'get',
          params: this.$http.adornParams({
            'groupName': this.dataForm.groupName,
            'groupCount': this.dataForm.groupCount
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.fileContentList = data.data
          } else {
            this.$message.error(data.msg)
          }
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
