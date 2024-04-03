<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="料子">
        <el-upload
          class="upload-demo"
          action="http://localhost:8880/app/file/upload"
          :on-success="handleAvatarSuccess">
          <el-button size="small" type="primary">点击上传</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
      <el-button type="primary" @click="exportTxt()">导出token</el-button>
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
        textUrl: '',
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
        updateTime: '',
        lineTaskId: '',
        msg: '',
        groupType: '',
        refreshContactStatus: ''
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
        updateTime: [
          { required: true, message: '修改时间不能为空', trigger: 'blur' }
        ],
        lineTaskId: [
          { required: true, message: 'line协议的任务id不能为空', trigger: 'blur' }
        ],
        msg: [
          { required: true, message: 'line的协议返回信息不能为空', trigger: 'blur' }
        ],
        groupType: [
          { required: true, message: '类型不能为空', trigger: 'blur' }
        ],
        refreshContactStatus: [
          { required: true, message: '刷新联系人状态不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleAvatarSuccess (res, file) {
      this.dataForm.textUrl = res.data
    },
    init (id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/cdphonefilter/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.dataTaskId = data.cdphonefilter.dataTaskId
              this.dataForm.userId = data.cdphonefilter.userId
              this.dataForm.taskStatus = data.cdphonefilter.taskStatus
              this.dataForm.luid = data.cdphonefilter.luid
              this.dataForm.contactType = data.cdphonefilter.contactType
              this.dataForm.contactKey = data.cdphonefilter.contactKey
              this.dataForm.mid = data.cdphonefilter.mid
              this.dataForm.createdTime = data.cdphonefilter.createdTime
              this.dataForm.type = data.cdphonefilter.type
              this.dataForm.status = data.cdphonefilter.status
              this.dataForm.relation = data.cdphonefilter.relation
              this.dataForm.displayName = data.cdphonefilter.displayName
              this.dataForm.phoneticName = data.cdphonefilter.phoneticName
              this.dataForm.pictureStatus = data.cdphonefilter.pictureStatus
              this.dataForm.thumbnailUrl = data.cdphonefilter.thumbnailUrl
              this.dataForm.statusMessage = data.cdphonefilter.statusMessage
              this.dataForm.displayNameOverridden = data.cdphonefilter.displayNameOverridden
              this.dataForm.favoriteTime = data.cdphonefilter.favoriteTime
              this.dataForm.capableVoiceCall = data.cdphonefilter.capableVoiceCall
              this.dataForm.capableVideoCall = data.cdphonefilter.capableVideoCall
              this.dataForm.capableMyhome = data.cdphonefilter.capableMyhome
              this.dataForm.capableBuddy = data.cdphonefilter.capableBuddy
              this.dataForm.attributes = data.cdphonefilter.attributes
              this.dataForm.settings = data.cdphonefilter.settings
              this.dataForm.picturePath = data.cdphonefilter.picturePath
              this.dataForm.recommendpArams = data.cdphonefilter.recommendpArams
              this.dataForm.friendRequestStatus = data.cdphonefilter.friendRequestStatus
              this.dataForm.musicProfile = data.cdphonefilter.musicProfile
              this.dataForm.videoProfile = data.cdphonefilter.videoProfile
              this.dataForm.deleteFlag = data.cdphonefilter.deleteFlag
              this.dataForm.createTime = data.cdphonefilter.createTime
              this.dataForm.updateTime = data.cdphonefilter.updateTime
              this.dataForm.lineTaskId = data.cdphonefilter.lineTaskId
              this.dataForm.msg = data.cdphonefilter.msg
              this.dataForm.groupType = data.cdphonefilter.groupType
              this.dataForm.refreshContactStatus = data.cdphonefilter.refreshContactStatus
            }
          })
        }
      })
    },
    exportTxt () {
      window.open(this.$http.adornUrl(`/ltt/cdphonefilter/exportSJ?textUrl=${this.dataForm.textUrl}&token=${this.$cookie.get('token')}`));
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/cdphonefilter/${!this.dataForm.id ? 'save' : 'update'}`),
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
              'updateTime': this.dataForm.updateTime,
              'lineTaskId': this.dataForm.lineTaskId,
              'msg': this.dataForm.msg,
              'textUrl': this.dataForm.textUrl,
              'groupType': this.dataForm.groupType,
              'refreshContactStatus': this.dataForm.refreshContactStatus
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
