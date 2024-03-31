webpackJsonp([89],{amsJ:function(a,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{dataUserGroupList:[],dataAvatarGroupList:[],visible:!1,dataForm:{id:0,taskName:"",taskStatus:"",schedule:"",userGroupId:"",avatarGroupId:"",executionQuantity:"",successfulQuantity:"",failuresQuantity:"",deleteFlag:"",createTime:""},dataRule:{taskName:[{required:!0,message:"任务名称不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"任务状态不能为空",trigger:"blur"}],schedule:[{required:!0,message:"进度不能为空",trigger:"blur"}],userGroupId:[{required:!0,message:"账户分组不能为空",trigger:"blur"}],avatarGroupId:[{required:!0,message:"头像分组不能为空",trigger:"blur"}],executionQuantity:[{required:!0,message:"执行数量不能为空",trigger:"blur"}],successfulQuantity:[{required:!0,message:"成功数量不能为空",trigger:"blur"}],failuresQuantity:[{required:!0,message:"失败数量不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{getAvatarGroupDataList:function(){var a=this;this.$http({url:this.$http.adornUrl("/ltt/atavatargroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(t){var e=t.data;e&&0===e.code?a.dataAvatarGroupList=e.page.list:a.dataAvatarGroupList=[]})},getUserGroupDataList:function(){var a=this;this.$http({url:this.$http.adornUrl("/ltt/atusergroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(t){var e=t.data;e&&0===e.code?a.dataUserGroupList=e.page.list:a.dataUserGroupList=[]})},init:function(a){var t=this;this.dataForm.id=a||0,this.visible=!0,this.getUserGroupDataList(),this.getAvatarGroupDataList(),this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/ltt/atavatartask/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(a){var e=a.data;e&&0===e.code&&(t.dataForm.taskName=e.atavatartask.taskName,t.dataForm.taskStatus=e.atavatartask.taskStatus,t.dataForm.schedule=e.atavatartask.schedule,t.dataForm.userGroupId=e.atavatartask.userGroupId,t.dataForm.avatarGroupId=e.atavatartask.avatarGroupId,t.dataForm.executionQuantity=e.atavatartask.executionQuantity,t.dataForm.successfulQuantity=e.atavatartask.successfulQuantity,t.dataForm.failuresQuantity=e.atavatartask.failuresQuantity,t.dataForm.deleteFlag=e.atavatartask.deleteFlag,t.dataForm.createTime=e.atavatartask.createTime)})})},dataFormSubmit:function(){var a=this;this.$refs.dataForm.validate(function(t){t&&a.$http({url:a.$http.adornUrl("/ltt/atavatartask/"+(a.dataForm.id?"update":"save")),method:"post",data:a.$http.adornData({id:a.dataForm.id||void 0,taskName:a.dataForm.taskName,taskStatus:a.dataForm.taskStatus,schedule:a.dataForm.schedule,userGroupId:a.dataForm.userGroupId,avatarGroupId:a.dataForm.avatarGroupId,executionQuantity:a.dataForm.executionQuantity,successfulQuantity:a.dataForm.successfulQuantity,failuresQuantity:a.dataForm.failuresQuantity,deleteFlag:a.dataForm.deleteFlag,createTime:a.dataForm.createTime})}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.visible=!1,a.$emit("refreshDataList")}}):a.$message.error(e.msg)})})}}},s={render:function(){var a=this,t=a.$createElement,e=a._self._c||t;return e("el-dialog",{attrs:{title:a.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:a.visible},on:{"update:visible":function(t){a.visible=t}}},[e("el-form",{ref:"dataForm",attrs:{model:a.dataForm,rules:a.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&a._k(t.keyCode,"enter",13,t.key))return null;a.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"任务名称",prop:"taskName"}},[e("el-input",{attrs:{placeholder:"任务名称"},model:{value:a.dataForm.taskName,callback:function(t){a.$set(a.dataForm,"taskName",t)},expression:"dataForm.taskName"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"账户分组",prop:"userGroupId"}},[e("el-select",{attrs:{placeholder:"账户分组"},model:{value:a.dataForm.userGroupId,callback:function(t){a.$set(a.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}},a._l(a.dataUserGroupList,function(a){return e("el-option",{key:a.id,attrs:{label:a.name,value:a.id}})}))],1),a._v(" "),e("el-form-item",{attrs:{label:"头像分组",prop:"avatarGroupId"}},[e("el-select",{attrs:{placeholder:"头像分组"},model:{value:a.dataForm.avatarGroupId,callback:function(t){a.$set(a.dataForm,"avatarGroupId",t)},expression:"dataForm.avatarGroupId"}},a._l(a.dataAvatarGroupList,function(a){return e("el-option",{key:a.id,attrs:{label:a.name,value:a.id}})}))],1)],1),a._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(t){a.visible=!1}}},[a._v("取消")]),a._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(t){a.dataFormSubmit()}}},[a._v("确定")])],1)],1)},staticRenderFns:[]},i=e("46Yf")(r,s,!1,null,null,null);t.default=i.exports}});