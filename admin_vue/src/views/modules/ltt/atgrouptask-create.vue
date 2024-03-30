<template>
  <div class="mod-config">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm">
      <div class="group-container">

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
              <el-button @click="onGroupPreHandler">预览</el-button>
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
        navyUrlFileList: [],
        materialUrlFileList: [],
        isModalVisible: false,
        fileContentList: [],
        dataRule: {
        },
        dataForm: {
          groupName: '',
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
      const userId = this.$route.query.id
      console.log(userId)
    },
    methods: {
      onGroupPreHandler () {
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
            this.fileContentList = data.data
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
