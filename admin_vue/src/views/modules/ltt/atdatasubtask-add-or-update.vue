<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="数据任务id" prop="dataTaskId">
      <el-input v-model="dataForm.dataTaskId" placeholder="数据任务id"></el-input>
    </el-form-item>
    <el-form-item label="账户id" prop="userId">
      <el-input v-model="dataForm.userId" placeholder="账户id"></el-input>
    </el-form-item>
    <el-form-item label="任务状态" prop="taskStatus">
      <el-input v-model="dataForm.taskStatus" placeholder="任务状态"></el-input>
    </el-form-item>
    <el-form-item label="" prop="luid">
      <el-input v-model="dataForm.luid" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="contactType">
      <el-input v-model="dataForm.contactType" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="contactKey">
      <el-input v-model="dataForm.contactKey" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="mid">
      <el-input v-model="dataForm.mid" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="createdTime">
      <el-input v-model="dataForm.createdTime" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="type">
      <el-input v-model="dataForm.type" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="status">
      <el-input v-model="dataForm.status" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="relation">
      <el-input v-model="dataForm.relation" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="displayName">
      <el-input v-model="dataForm.displayName" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="phoneticName">
      <el-input v-model="dataForm.phoneticName" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="pictureStatus">
      <el-input v-model="dataForm.pictureStatus" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="thumbnailUrl">
      <el-input v-model="dataForm.thumbnailUrl" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="statusMessage">
      <el-input v-model="dataForm.statusMessage" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="displayNameOverridden">
      <el-input v-model="dataForm.displayNameOverridden" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="favoriteTime">
      <el-input v-model="dataForm.favoriteTime" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="capableVoiceCall">
      <el-input v-model="dataForm.capableVoiceCall" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="capableVideoCall">
      <el-input v-model="dataForm.capableVideoCall" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="capableMyhome">
      <el-input v-model="dataForm.capableMyhome" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="capableBuddy">
      <el-input v-model="dataForm.capableBuddy" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="attributes">
      <el-input v-model="dataForm.attributes" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="settings">
      <el-input v-model="dataForm.settings" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="picturePath">
      <el-input v-model="dataForm.picturePath" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="recommendpArams">
      <el-input v-model="dataForm.recommendpArams" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="friendRequestStatus">
      <el-input v-model="dataForm.friendRequestStatus" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="musicProfile">
      <el-input v-model="dataForm.musicProfile" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="" prop="videoProfile">
      <el-input v-model="dataForm.videoProfile" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="删除标志" prop="deleteFlag">
      <el-input v-model="dataForm.deleteFlag" placeholder="删除标志"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
    </el-form-item>
    <el-form-item label="line协议的任务id" prop="lineTaskId">
      <el-input v-model="dataForm.lineTaskId" placeholder="line协议的任务id"></el-input>
    </el-form-item>
    <el-form-item label="line的协议返回信息" prop="msg">
      <el-input v-model="dataForm.msg" placeholder="line的协议返回信息"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          id: 0,
          dataTaskId: '',
          userId: '',
          taskStatus: '',
          luid: '',
          contactType: '',
          contactKey: '',
          mid: '',
          createdTime: '',
          type: '',
          status: '',
          relation: '',
          displayName: '',
          phoneticName: '',
          pictureStatus: '',
          thumbnailUrl: '',
          statusMessage: '',
          displayNameOverridden: '',
          favoriteTime: '',
          capableVoiceCall: '',
          capableVideoCall: '',
          capableMyhome: '',
          capableBuddy: '',
          attributes: '',
          settings: '',
          picturePath: '',
          recommendpArams: '',
          friendRequestStatus: '',
          musicProfile: '',
          videoProfile: '',
          deleteFlag: '',
          createTime: '',
          lineTaskId: '',
          msg: ''
        },
        dataRule: {
          dataTaskId: [
            { required: true, message: '数据任务id不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '账户id不能为空', trigger: 'blur' }
          ],
          taskStatus: [
            { required: true, message: '任务状态不能为空', trigger: 'blur' }
          ],
          luid: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          contactType: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          contactKey: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          mid: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          createdTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          relation: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          displayName: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          phoneticName: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          pictureStatus: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          thumbnailUrl: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          statusMessage: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          displayNameOverridden: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          favoriteTime: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          capableVoiceCall: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          capableVideoCall: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          capableMyhome: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          capableBuddy: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          attributes: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          settings: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          picturePath: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          recommendpArams: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          friendRequestStatus: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          musicProfile: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          videoProfile: [
            { required: true, message: '不能为空', trigger: 'blur' }
          ],
          deleteFlag: [
            { required: true, message: '删除标志不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          lineTaskId: [
            { required: true, message: 'line协议的任务id不能为空', trigger: 'blur' }
          ],
          msg: [
            { required: true, message: 'line的协议返回信息不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atdatasubtask/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.dataTaskId = data.atdatasubtask.dataTaskId
                this.dataForm.userId = data.atdatasubtask.userId
                this.dataForm.taskStatus = data.atdatasubtask.taskStatus
                this.dataForm.luid = data.atdatasubtask.luid
                this.dataForm.contactType = data.atdatasubtask.contactType
                this.dataForm.contactKey = data.atdatasubtask.contactKey
                this.dataForm.mid = data.atdatasubtask.mid
                this.dataForm.createdTime = data.atdatasubtask.createdTime
                this.dataForm.type = data.atdatasubtask.type
                this.dataForm.status = data.atdatasubtask.status
                this.dataForm.relation = data.atdatasubtask.relation
                this.dataForm.displayName = data.atdatasubtask.displayName
                this.dataForm.phoneticName = data.atdatasubtask.phoneticName
                this.dataForm.pictureStatus = data.atdatasubtask.pictureStatus
                this.dataForm.thumbnailUrl = data.atdatasubtask.thumbnailUrl
                this.dataForm.statusMessage = data.atdatasubtask.statusMessage
                this.dataForm.displayNameOverridden = data.atdatasubtask.displayNameOverridden
                this.dataForm.favoriteTime = data.atdatasubtask.favoriteTime
                this.dataForm.capableVoiceCall = data.atdatasubtask.capableVoiceCall
                this.dataForm.capableVideoCall = data.atdatasubtask.capableVideoCall
                this.dataForm.capableMyhome = data.atdatasubtask.capableMyhome
                this.dataForm.capableBuddy = data.atdatasubtask.capableBuddy
                this.dataForm.attributes = data.atdatasubtask.attributes
                this.dataForm.settings = data.atdatasubtask.settings
                this.dataForm.picturePath = data.atdatasubtask.picturePath
                this.dataForm.recommendpArams = data.atdatasubtask.recommendpArams
                this.dataForm.friendRequestStatus = data.atdatasubtask.friendRequestStatus
                this.dataForm.musicProfile = data.atdatasubtask.musicProfile
                this.dataForm.videoProfile = data.atdatasubtask.videoProfile
                this.dataForm.deleteFlag = data.atdatasubtask.deleteFlag
                this.dataForm.createTime = data.atdatasubtask.createTime
                this.dataForm.lineTaskId = data.atdatasubtask.lineTaskId
                this.dataForm.msg = data.atdatasubtask.msg
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atdatasubtask/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'dataTaskId': this.dataForm.dataTaskId,
                'userId': this.dataForm.userId,
                'taskStatus': this.dataForm.taskStatus,
                'luid': this.dataForm.luid,
                'contactType': this.dataForm.contactType,
                'contactKey': this.dataForm.contactKey,
                'mid': this.dataForm.mid,
                'createdTime': this.dataForm.createdTime,
                'type': this.dataForm.type,
                'status': this.dataForm.status,
                'relation': this.dataForm.relation,
                'displayName': this.dataForm.displayName,
                'phoneticName': this.dataForm.phoneticName,
                'pictureStatus': this.dataForm.pictureStatus,
                'thumbnailUrl': this.dataForm.thumbnailUrl,
                'statusMessage': this.dataForm.statusMessage,
                'displayNameOverridden': this.dataForm.displayNameOverridden,
                'favoriteTime': this.dataForm.favoriteTime,
                'capableVoiceCall': this.dataForm.capableVoiceCall,
                'capableVideoCall': this.dataForm.capableVideoCall,
                'capableMyhome': this.dataForm.capableMyhome,
                'capableBuddy': this.dataForm.capableBuddy,
                'attributes': this.dataForm.attributes,
                'settings': this.dataForm.settings,
                'picturePath': this.dataForm.picturePath,
                'recommendpArams': this.dataForm.recommendpArams,
                'friendRequestStatus': this.dataForm.friendRequestStatus,
                'musicProfile': this.dataForm.musicProfile,
                'videoProfile': this.dataForm.videoProfile,
                'deleteFlag': this.dataForm.deleteFlag,
                'createTime': this.dataForm.createTime,
                'lineTaskId': this.dataForm.lineTaskId,
                'msg': this.dataForm.msg
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
