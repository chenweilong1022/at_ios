webpackJsonp([92],{p64g:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i={data:function(){return{multiple:!0,fileList:[],visible:!1,uploadUrl:"",dataForm:{avatarList:[],id:0,name:"",deleteFlag:"",createTime:""},dataRule:{name:[{required:!0,message:"分组名称不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{handleSuccess:function(t,e,a){console.log(t),console.log(a),this.fileList=a},handleRemove:function(t,e){this.fileList=e,console.log(t),console.log(e)},handlePreview:function(t,e){console.log(t),console.log(e),this.fileList=e},init:function(t){var e=this;this.dataForm.id=t||0,this.visible=!0,this.uploadUrl=this.$http.adornUrl("/app/file/upload"),this.$refs.upload.clearFiles(),this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.id&&e.$http({url:e.$http.adornUrl("/ltt/atavatargroup/info/"+e.dataForm.id),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.name=a.atAvatarGroup.name,e.dataForm.deleteFlag=a.atAvatarGroup.deleteFlag,e.dataForm.createTime=a.atAvatarGroup.createTime)})})},dataFormSubmit:function(){var t=this;this.dataForm.avatarList=[];for(var e=0;e<this.fileList.length;e++){var a=this.fileList[e];this.dataForm.avatarList.push(a.response.data)}this.fileList=[],console.log(this.dataForm.avatarList),this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/atavatargroup/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,name:t.dataForm.name,avatarList:t.dataForm.avatarList,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},l={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:(t.dataForm.id,"数据上传"),"close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"分组名称",prop:"name"}},[a("el-input",{attrs:{placeholder:"分组名称",disabled:""},model:{value:t.dataForm.name,callback:function(e){t.$set(t.dataForm,"name",e)},expression:"dataForm.name"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"分组名称",prop:"name"}},[a("el-upload",{ref:"upload",staticClass:"upload-demo",attrs:{multiple:t.multiple,action:t.uploadUrl,"on-preview":t.handlePreview,"on-remove":t.handleRemove,"on-success":t.handleSuccess,"list-type":"picture"},model:{value:t.fileList,callback:function(e){t.fileList=e},expression:"fileList"}},[a("el-button",{attrs:{type:"primary"}},[t._v("Click to upload")]),t._v(" "),[a("div",{staticClass:"el-upload__tip"},[t._v("\n            jpg/png files with a size less than 500kb\n          ")])]],2)],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},r=a("46Yf")(i,l,!1,null,null,null);e.default=r.exports}});