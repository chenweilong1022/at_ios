webpackJsonp([94],{"/zYu":function(a,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,avatarTaskId:"",userGroupId:"",avatarGroupId:"",userId:"",avatarId:"",taskStatus:"",deleteFlag:"",createTime:""},dataRule:{avatarTaskId:[{required:!0,message:"头像任务id不能为空",trigger:"blur"}],userGroupId:[{required:!0,message:"账户分组不能为空",trigger:"blur"}],avatarGroupId:[{required:!0,message:"头像分组不能为空",trigger:"blur"}],userId:[{required:!0,message:"账户id不能为空",trigger:"blur"}],avatarId:[{required:!0,message:"头像id不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"任务状态不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{init:function(a){var t=this;this.dataForm.id=a||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/ltt/atavatarsubtask/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(a){var e=a.data;e&&0===e.code&&(t.dataForm.avatarTaskId=e.atavatarsubtask.avatarTaskId,t.dataForm.userGroupId=e.atavatarsubtask.userGroupId,t.dataForm.avatarGroupId=e.atavatarsubtask.avatarGroupId,t.dataForm.userId=e.atavatarsubtask.userId,t.dataForm.avatarId=e.atavatarsubtask.avatarId,t.dataForm.taskStatus=e.atavatarsubtask.taskStatus,t.dataForm.deleteFlag=e.atavatarsubtask.deleteFlag,t.dataForm.createTime=e.atavatarsubtask.createTime)})})},dataFormSubmit:function(){var a=this;this.$refs.dataForm.validate(function(t){t&&a.$http({url:a.$http.adornUrl("/ltt/atavatarsubtask/"+(a.dataForm.id?"update":"save")),method:"post",data:a.$http.adornData({id:a.dataForm.id||void 0,avatarTaskId:a.dataForm.avatarTaskId,userGroupId:a.dataForm.userGroupId,avatarGroupId:a.dataForm.avatarGroupId,userId:a.dataForm.userId,avatarId:a.dataForm.avatarId,taskStatus:a.dataForm.taskStatus,deleteFlag:a.dataForm.deleteFlag,createTime:a.dataForm.createTime})}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.visible=!1,a.$emit("refreshDataList")}}):a.$message.error(e.msg)})})}}},d={render:function(){var a=this,t=a.$createElement,e=a._self._c||t;return e("el-dialog",{attrs:{title:a.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:a.visible},on:{"update:visible":function(t){a.visible=t}}},[e("el-form",{ref:"dataForm",attrs:{model:a.dataForm,rules:a.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&a._k(t.keyCode,"enter",13,t.key))return null;a.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"头像任务id",prop:"avatarTaskId"}},[e("el-input",{attrs:{placeholder:"头像任务id"},model:{value:a.dataForm.avatarTaskId,callback:function(t){a.$set(a.dataForm,"avatarTaskId",t)},expression:"dataForm.avatarTaskId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"账户分组",prop:"userGroupId"}},[e("el-input",{attrs:{placeholder:"账户分组"},model:{value:a.dataForm.userGroupId,callback:function(t){a.$set(a.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"头像分组",prop:"avatarGroupId"}},[e("el-input",{attrs:{placeholder:"头像分组"},model:{value:a.dataForm.avatarGroupId,callback:function(t){a.$set(a.dataForm,"avatarGroupId",t)},expression:"dataForm.avatarGroupId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"账户id",prop:"userId"}},[e("el-input",{attrs:{placeholder:"账户id"},model:{value:a.dataForm.userId,callback:function(t){a.$set(a.dataForm,"userId",t)},expression:"dataForm.userId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"头像id",prop:"avatarId"}},[e("el-input",{attrs:{placeholder:"头像id"},model:{value:a.dataForm.avatarId,callback:function(t){a.$set(a.dataForm,"avatarId",t)},expression:"dataForm.avatarId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"任务状态",prop:"taskStatus"}},[e("el-input",{attrs:{placeholder:"任务状态"},model:{value:a.dataForm.taskStatus,callback:function(t){a.$set(a.dataForm,"taskStatus",t)},expression:"dataForm.taskStatus"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"删除标志",prop:"deleteFlag"}},[e("el-input",{attrs:{placeholder:"删除标志"},model:{value:a.dataForm.deleteFlag,callback:function(t){a.$set(a.dataForm,"deleteFlag",t)},expression:"dataForm.deleteFlag"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[e("el-input",{attrs:{placeholder:"创建时间"},model:{value:a.dataForm.createTime,callback:function(t){a.$set(a.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1)],1),a._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(t){a.visible=!1}}},[a._v("取消")]),a._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(t){a.dataFormSubmit()}}},[a._v("确定")])],1)],1)},staticRenderFns:[]},s=e("46Yf")(r,d,!1,null,null,null);t.default=s.exports}});