webpackJsonp([45,84],{adHc:function(a,e,t){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,dataTaskId:"",userId:"",taskStatus:"",luid:"",contactType:"",contactKey:"",mid:"",createdTime:"",type:"",status:"",relation:"",displayName:"",phoneticName:"",pictureStatus:"",thumbnailUrl:"",statusMessage:"",displayNameOverridden:"",favoriteTime:"",capableVoiceCall:"",capableVideoCall:"",capableMyhome:"",capableBuddy:"",attributes:"",settings:"",picturePath:"",recommendpArams:"",friendRequestStatus:"",musicProfile:"",videoProfile:"",deleteFlag:"",createTime:"",lineTaskId:"",msg:""},dataRule:{dataTaskId:[{required:!0,message:"数据任务id不能为空",trigger:"blur"}],userId:[{required:!0,message:"账户id不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"任务状态不能为空",trigger:"blur"}],luid:[{required:!0,message:"不能为空",trigger:"blur"}],contactType:[{required:!0,message:"不能为空",trigger:"blur"}],contactKey:[{required:!0,message:"不能为空",trigger:"blur"}],mid:[{required:!0,message:"不能为空",trigger:"blur"}],createdTime:[{required:!0,message:"不能为空",trigger:"blur"}],type:[{required:!0,message:"不能为空",trigger:"blur"}],status:[{required:!0,message:"不能为空",trigger:"blur"}],relation:[{required:!0,message:"不能为空",trigger:"blur"}],displayName:[{required:!0,message:"不能为空",trigger:"blur"}],phoneticName:[{required:!0,message:"不能为空",trigger:"blur"}],pictureStatus:[{required:!0,message:"不能为空",trigger:"blur"}],thumbnailUrl:[{required:!0,message:"不能为空",trigger:"blur"}],statusMessage:[{required:!0,message:"不能为空",trigger:"blur"}],displayNameOverridden:[{required:!0,message:"不能为空",trigger:"blur"}],favoriteTime:[{required:!0,message:"不能为空",trigger:"blur"}],capableVoiceCall:[{required:!0,message:"不能为空",trigger:"blur"}],capableVideoCall:[{required:!0,message:"不能为空",trigger:"blur"}],capableMyhome:[{required:!0,message:"不能为空",trigger:"blur"}],capableBuddy:[{required:!0,message:"不能为空",trigger:"blur"}],attributes:[{required:!0,message:"不能为空",trigger:"blur"}],settings:[{required:!0,message:"不能为空",trigger:"blur"}],picturePath:[{required:!0,message:"不能为空",trigger:"blur"}],recommendpArams:[{required:!0,message:"不能为空",trigger:"blur"}],friendRequestStatus:[{required:!0,message:"不能为空",trigger:"blur"}],musicProfile:[{required:!0,message:"不能为空",trigger:"blur"}],videoProfile:[{required:!0,message:"不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],lineTaskId:[{required:!0,message:"line协议的任务id不能为空",trigger:"blur"}],msg:[{required:!0,message:"line的协议返回信息不能为空",trigger:"blur"}]}}},methods:{init:function(a){var e=this;this.dataForm.id=a||0,this.visible=!0,this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.id&&e.$http({url:e.$http.adornUrl("/ltt/atdatasubtask/info/"+e.dataForm.id),method:"get",params:e.$http.adornParams()}).then(function(a){var t=a.data;t&&0===t.code&&(e.dataForm.dataTaskId=t.atdatasubtask.dataTaskId,e.dataForm.userId=t.atdatasubtask.userId,e.dataForm.taskStatus=t.atdatasubtask.taskStatus,e.dataForm.luid=t.atdatasubtask.luid,e.dataForm.contactType=t.atdatasubtask.contactType,e.dataForm.contactKey=t.atdatasubtask.contactKey,e.dataForm.mid=t.atdatasubtask.mid,e.dataForm.createdTime=t.atdatasubtask.createdTime,e.dataForm.type=t.atdatasubtask.type,e.dataForm.status=t.atdatasubtask.status,e.dataForm.relation=t.atdatasubtask.relation,e.dataForm.displayName=t.atdatasubtask.displayName,e.dataForm.phoneticName=t.atdatasubtask.phoneticName,e.dataForm.pictureStatus=t.atdatasubtask.pictureStatus,e.dataForm.thumbnailUrl=t.atdatasubtask.thumbnailUrl,e.dataForm.statusMessage=t.atdatasubtask.statusMessage,e.dataForm.displayNameOverridden=t.atdatasubtask.displayNameOverridden,e.dataForm.favoriteTime=t.atdatasubtask.favoriteTime,e.dataForm.capableVoiceCall=t.atdatasubtask.capableVoiceCall,e.dataForm.capableVideoCall=t.atdatasubtask.capableVideoCall,e.dataForm.capableMyhome=t.atdatasubtask.capableMyhome,e.dataForm.capableBuddy=t.atdatasubtask.capableBuddy,e.dataForm.attributes=t.atdatasubtask.attributes,e.dataForm.settings=t.atdatasubtask.settings,e.dataForm.picturePath=t.atdatasubtask.picturePath,e.dataForm.recommendpArams=t.atdatasubtask.recommendpArams,e.dataForm.friendRequestStatus=t.atdatasubtask.friendRequestStatus,e.dataForm.musicProfile=t.atdatasubtask.musicProfile,e.dataForm.videoProfile=t.atdatasubtask.videoProfile,e.dataForm.deleteFlag=t.atdatasubtask.deleteFlag,e.dataForm.createTime=t.atdatasubtask.createTime,e.dataForm.lineTaskId=t.atdatasubtask.lineTaskId,e.dataForm.msg=t.atdatasubtask.msg)})})},dataFormSubmit:function(){var a=this;this.$refs.dataForm.validate(function(e){e&&a.$http({url:a.$http.adornUrl("/ltt/atdatasubtask/"+(a.dataForm.id?"update":"save")),method:"post",data:a.$http.adornData({id:a.dataForm.id||void 0,dataTaskId:a.dataForm.dataTaskId,userId:a.dataForm.userId,taskStatus:a.dataForm.taskStatus,luid:a.dataForm.luid,contactType:a.dataForm.contactType,contactKey:a.dataForm.contactKey,mid:a.dataForm.mid,createdTime:a.dataForm.createdTime,type:a.dataForm.type,status:a.dataForm.status,relation:a.dataForm.relation,displayName:a.dataForm.displayName,phoneticName:a.dataForm.phoneticName,pictureStatus:a.dataForm.pictureStatus,thumbnailUrl:a.dataForm.thumbnailUrl,statusMessage:a.dataForm.statusMessage,displayNameOverridden:a.dataForm.displayNameOverridden,favoriteTime:a.dataForm.favoriteTime,capableVoiceCall:a.dataForm.capableVoiceCall,capableVideoCall:a.dataForm.capableVideoCall,capableMyhome:a.dataForm.capableMyhome,capableBuddy:a.dataForm.capableBuddy,attributes:a.dataForm.attributes,settings:a.dataForm.settings,picturePath:a.dataForm.picturePath,recommendpArams:a.dataForm.recommendpArams,friendRequestStatus:a.dataForm.friendRequestStatus,musicProfile:a.dataForm.musicProfile,videoProfile:a.dataForm.videoProfile,deleteFlag:a.dataForm.deleteFlag,createTime:a.dataForm.createTime,lineTaskId:a.dataForm.lineTaskId,msg:a.dataForm.msg})}).then(function(e){var t=e.data;t&&0===t.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.visible=!1,a.$emit("refreshDataList")}}):a.$message.error(t.msg)})})}}},l={render:function(){var a=this,e=a.$createElement,t=a._self._c||e;return t("el-dialog",{attrs:{title:a.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:a.visible},on:{"update:visible":function(e){a.visible=e}}},[t("el-form",{ref:"dataForm",attrs:{model:a.dataForm,rules:a.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&a._k(e.keyCode,"enter",13,e.key))return null;a.dataFormSubmit()}}},[t("el-form-item",{attrs:{label:"数据任务id",prop:"dataTaskId"}},[t("el-input",{attrs:{placeholder:"数据任务id"},model:{value:a.dataForm.dataTaskId,callback:function(e){a.$set(a.dataForm,"dataTaskId",e)},expression:"dataForm.dataTaskId"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"账户id",prop:"userId"}},[t("el-input",{attrs:{placeholder:"账户id"},model:{value:a.dataForm.userId,callback:function(e){a.$set(a.dataForm,"userId",e)},expression:"dataForm.userId"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"任务状态",prop:"taskStatus"}},[t("el-input",{attrs:{placeholder:"任务状态"},model:{value:a.dataForm.taskStatus,callback:function(e){a.$set(a.dataForm,"taskStatus",e)},expression:"dataForm.taskStatus"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"luid"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.luid,callback:function(e){a.$set(a.dataForm,"luid",e)},expression:"dataForm.luid"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"contactType"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.contactType,callback:function(e){a.$set(a.dataForm,"contactType",e)},expression:"dataForm.contactType"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"contactKey"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.contactKey,callback:function(e){a.$set(a.dataForm,"contactKey",e)},expression:"dataForm.contactKey"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"mid"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.mid,callback:function(e){a.$set(a.dataForm,"mid",e)},expression:"dataForm.mid"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"createdTime"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.createdTime,callback:function(e){a.$set(a.dataForm,"createdTime",e)},expression:"dataForm.createdTime"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"type"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.type,callback:function(e){a.$set(a.dataForm,"type",e)},expression:"dataForm.type"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"status"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.status,callback:function(e){a.$set(a.dataForm,"status",e)},expression:"dataForm.status"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"relation"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.relation,callback:function(e){a.$set(a.dataForm,"relation",e)},expression:"dataForm.relation"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"displayName"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.displayName,callback:function(e){a.$set(a.dataForm,"displayName",e)},expression:"dataForm.displayName"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"phoneticName"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.phoneticName,callback:function(e){a.$set(a.dataForm,"phoneticName",e)},expression:"dataForm.phoneticName"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"pictureStatus"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.pictureStatus,callback:function(e){a.$set(a.dataForm,"pictureStatus",e)},expression:"dataForm.pictureStatus"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"thumbnailUrl"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.thumbnailUrl,callback:function(e){a.$set(a.dataForm,"thumbnailUrl",e)},expression:"dataForm.thumbnailUrl"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"statusMessage"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.statusMessage,callback:function(e){a.$set(a.dataForm,"statusMessage",e)},expression:"dataForm.statusMessage"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"displayNameOverridden"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.displayNameOverridden,callback:function(e){a.$set(a.dataForm,"displayNameOverridden",e)},expression:"dataForm.displayNameOverridden"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"favoriteTime"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.favoriteTime,callback:function(e){a.$set(a.dataForm,"favoriteTime",e)},expression:"dataForm.favoriteTime"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"capableVoiceCall"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.capableVoiceCall,callback:function(e){a.$set(a.dataForm,"capableVoiceCall",e)},expression:"dataForm.capableVoiceCall"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"capableVideoCall"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.capableVideoCall,callback:function(e){a.$set(a.dataForm,"capableVideoCall",e)},expression:"dataForm.capableVideoCall"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"capableMyhome"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.capableMyhome,callback:function(e){a.$set(a.dataForm,"capableMyhome",e)},expression:"dataForm.capableMyhome"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"capableBuddy"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.capableBuddy,callback:function(e){a.$set(a.dataForm,"capableBuddy",e)},expression:"dataForm.capableBuddy"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"attributes"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.attributes,callback:function(e){a.$set(a.dataForm,"attributes",e)},expression:"dataForm.attributes"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"settings"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.settings,callback:function(e){a.$set(a.dataForm,"settings",e)},expression:"dataForm.settings"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"picturePath"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.picturePath,callback:function(e){a.$set(a.dataForm,"picturePath",e)},expression:"dataForm.picturePath"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"recommendpArams"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.recommendpArams,callback:function(e){a.$set(a.dataForm,"recommendpArams",e)},expression:"dataForm.recommendpArams"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"friendRequestStatus"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.friendRequestStatus,callback:function(e){a.$set(a.dataForm,"friendRequestStatus",e)},expression:"dataForm.friendRequestStatus"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"musicProfile"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.musicProfile,callback:function(e){a.$set(a.dataForm,"musicProfile",e)},expression:"dataForm.musicProfile"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"",prop:"videoProfile"}},[t("el-input",{attrs:{placeholder:""},model:{value:a.dataForm.videoProfile,callback:function(e){a.$set(a.dataForm,"videoProfile",e)},expression:"dataForm.videoProfile"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"删除标志",prop:"deleteFlag"}},[t("el-input",{attrs:{placeholder:"删除标志"},model:{value:a.dataForm.deleteFlag,callback:function(e){a.$set(a.dataForm,"deleteFlag",e)},expression:"dataForm.deleteFlag"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[t("el-input",{attrs:{placeholder:"创建时间"},model:{value:a.dataForm.createTime,callback:function(e){a.$set(a.dataForm,"createTime",e)},expression:"dataForm.createTime"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"line协议的任务id",prop:"lineTaskId"}},[t("el-input",{attrs:{placeholder:"line协议的任务id"},model:{value:a.dataForm.lineTaskId,callback:function(e){a.$set(a.dataForm,"lineTaskId",e)},expression:"dataForm.lineTaskId"}})],1),a._v(" "),t("el-form-item",{attrs:{label:"line的协议返回信息",prop:"msg"}},[t("el-input",{attrs:{placeholder:"line的协议返回信息"},model:{value:a.dataForm.msg,callback:function(e){a.$set(a.dataForm,"msg",e)},expression:"dataForm.msg"}})],1)],1),a._v(" "),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(e){a.visible=!1}}},[a._v("取消")]),a._v(" "),t("el-button",{attrs:{type:"primary"},on:{click:function(e){a.dataFormSubmit()}}},[a._v("确定")])],1)],1)},staticRenderFns:[]},s=t("46Yf")(r,l,!1,null,null,null);e.default=s.exports},gJbJ:function(a,e,t){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{dataForm:{contactKey:null,telephone:null,userGroupId:null,userStatus:null,customerServiceId:null,updateStartTime:null,updateEndTime:null,timeKey:null},dataList:[],userGroupOptions:[],customerUserOptions:[],statusOptions:[{value:1,label:"未验证"},{value:2,label:"封号"},{value:3,label:"下线"},{value:4,label:"在线"},{value:5,label:"数据错误"}],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:t("adHc").default},activated:function(){this.getDataList()},methods:{getDataList:function(){var a=this;this.dataListLoading=!0;var e=null,t=null;null!=this.dataForm.timeKey&&this.dataForm.timeKey.length>=2&&(e=this.dataForm.timeKey[0],t=this.dataForm.timeKey[1]),this.$http({url:this.$http.adornUrl("/ltt/atdatasubtask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,contactKey:this.dataForm.contactKey,telephone:this.dataForm.telephone,userGroupId:this.dataForm.userGroupId,userStatus:this.dataForm.userStatus,customerServiceId:this.dataForm.customerServiceId,updateStartTime:e,updateEndTime:t})}).then(function(e){var t=e.data;t&&0===t.code?(a.dataList=t.page.list,a.totalPage=t.page.totalCount):(a.dataList=[],a.totalPage=0),a.dataListLoading=!1})},queryUserGroupBySearchWord:function(a){var e=this;a=null==a?"":a+"",this.$http({url:this.$http.adornUrl("/ltt/atusergroup/queryByFuzzyName?searchWord="+a),method:"get",params:this.$http.adornParams()}).then(function(a){var t=a.data;t&&0===t.code&&(e.userGroupOptions=t.groupList)})},queryCustomerByFuzzyName:function(a){var e=this;a=null==a?"":a+"",this.$http({url:this.$http.adornUrl("/ltt/customeruser/queryCustomerByFuzzyName?key="+a),method:"get",params:this.$http.adornParams()}).then(function(a){var t=a.data;t&&0===t.code&&(e.customerUserOptions=t.customerList)})},sizeChangeHandle:function(a){this.pageSize=a,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(a){this.pageIndex=a,this.getDataList()},selectionChangeHandle:function(a){this.dataListSelections=a},addOrUpdateHandle:function(a){var e=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(a)})},deleteHandle:function(a){var e=this,t=a?[a]:this.dataListSelections.map(function(a){return a.id});this.$confirm("确定对[id="+t.join(",")+"]进行["+(a?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atdatasubtask/delete"),method:"post",data:e.$http.adornData(t,!1)}).then(function(a){var t=a.data;t&&0===t.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(t.msg)})})}}},l={render:function(){var a=this,e=a.$createElement,t=a._self._c||e;return t("div",{staticClass:"mod-config"},[t("el-form",{attrs:{inline:!0,model:a.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&a._k(e.keyCode,"enter",13,e.key))return null;a.getDataList()}}},[t("el-form-item",[t("el-input",{attrs:{placeholder:"好友手机号",clearable:""},model:{value:a.dataForm.contactKey,callback:function(e){a.$set(a.dataForm,"contactKey",e)},expression:"dataForm.contactKey"}})],1),a._v(" "),t("el-form-item",[t("el-input",{attrs:{placeholder:"主号手机号",clearable:""},model:{value:a.dataForm.telephone,callback:function(e){a.$set(a.dataForm,"telephone",e)},expression:"dataForm.telephone"}})],1),a._v(" "),t("el-form-item",[t("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"主账号分组","remote-method":a.queryUserGroupBySearchWord,loading:a.loading},model:{value:a.dataForm.userGroupId,callback:function(e){a.$set(a.dataForm,"userGroupId",e)},expression:"dataForm.userGroupId"}},a._l(a.userGroupOptions,function(a){return t("el-option",{key:a.id,attrs:{label:a.name,value:a.id}})}))],1),a._v(" "),t("el-form-item",[t("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{clearable:"",placeholder:"主账号状态"},model:{value:a.dataForm.userStatus,callback:function(e){a.$set(a.dataForm,"userStatus",e)},expression:"dataForm.userStatus"}},a._l(a.statusOptions,function(a){return t("el-option",{key:a.value,attrs:{label:a.label,value:a.value}})}))],1),a._v(" "),t("el-form-item",[t("el-date-picker",{attrs:{type:"daterange",format:"yyyy-MM-dd","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:a.dataForm.timeKey,callback:function(e){a.$set(a.dataForm,"timeKey",e)},expression:"dataForm.timeKey"}})],1),a._v(" "),t("el-form-item",[t("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择客服","remote-method":a.queryCustomerByFuzzyName,loading:a.loading},model:{value:a.dataForm.customerServiceId,callback:function(e){a.$set(a.dataForm,"customerServiceId",e)},expression:"dataForm.customerServiceId"}},a._l(a.customerUserOptions,function(a){return t("el-option",{key:a.userId,attrs:{label:a.nickname,value:a.userId}})}))],1),a._v(" "),t("el-form-item",[t("el-button",{on:{click:function(e){a.getDataList()}}},[a._v("查询")]),a._v(" "),a.isAuth("ltt:atdatasubtask:save")?t("el-button",{attrs:{type:"primary"},on:{click:function(e){a.addOrUpdateHandle()}}},[a._v("新增")]):a._e(),a._v(" "),a.isAuth("ltt:atdatasubtask:delete")?t("el-button",{attrs:{type:"danger",disabled:a.dataListSelections.length<=0},on:{click:function(e){a.deleteHandle()}}},[a._v("批量删除")]):a._e()],1)],1),a._v(" "),t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:a.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:a.dataList,border:""},on:{"selection-change":a.selectionChangeHandle}},[t("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),a._v(" "),t("el-table-column",{attrs:{prop:"picturePath","header-align":"center",align:"center",label:"头像"},scopedSlots:a._u([{key:"default",fn:function(a){return[t("img",{staticStyle:{width:"40px",height:"40px"},attrs:{src:"https://profile.line-scdn.net"+a.row.picturePath}})]}}])}),a._v(" "),t("el-table-column",{attrs:{prop:"displayName","header-align":"center",align:"center",label:"昵称"}}),a._v(" "),t("el-table-column",{attrs:{prop:"mid","header-align":"center",align:"center",label:"好友uid"}}),a._v(" "),t("el-table-column",{attrs:{prop:"contactKey","header-align":"center",align:"center",label:"好友手机号"}}),a._v(" "),t("el-table-column",{attrs:{prop:"telephone","header-align":"center",align:"center",label:"主号手机号"}}),a._v(" "),t("el-table-column",{attrs:{prop:"userStatus","header-align":"center",align:"center",label:"主号状态"},scopedSlots:a._u([{key:"default",fn:function(e){return[1===e.row.status?t("el-tag",{attrs:{size:"small",type:"danger"}},[a._v("主号未验证")]):2===e.row.status?t("el-tag",{attrs:{size:"small",type:"danger"}},[a._v("主号封号")]):3===e.row.status?t("el-tag",{attrs:{size:"small",type:"danger"}},[a._v("主号下线")]):4===e.row.status?t("el-tag",{attrs:{size:"small",type:"success"}},[a._v("主号在线")]):t("el-tag",{attrs:{size:"small",type:"danger"}},[a._v("主号数据错误")])]}}])}),a._v(" "),t("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",label:"加友方式"},scopedSlots:a._u([{key:"default",fn:function(e){return[null==e.row.taskStatus?t("el-tag",{attrs:{size:"small"}},[a._v("主动添加")]):t("el-tag",{attrs:{size:"small"}},[a._v("被动添加")])]}}])}),a._v(" "),t("el-table-column",{attrs:{prop:"updateTime","header-align":"center",align:"center",label:"更新时间"}}),a._v(" "),t("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:a._u([{key:"default",fn:function(e){return[t("el-button",{attrs:{type:"text",size:"small"},on:{click:function(t){a.deleteHandle(e.row.id)}}},[a._v("删除")])]}}])})],1),a._v(" "),t("el-pagination",{attrs:{"current-page":a.pageIndex,"page-sizes":[10,20,50,100],"page-size":a.pageSize,total:a.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":a.sizeChangeHandle,"current-change":a.currentChangeHandle}}),a._v(" "),a.addOrUpdateVisible?t("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:a.getDataList}}):a._e()],1)},staticRenderFns:[]},s=t("46Yf")(r,l,!1,null,null,null);e.default=s.exports}});