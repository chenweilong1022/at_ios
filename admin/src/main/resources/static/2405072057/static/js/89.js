webpackJsonp([89],{sbT4:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{groupType:null,uploadUrl:"",options:[],visible:!1,dataForm:{id:0,name:"",txtUrl:"",groupType:"",deleteFlag:"",createTime:""},dataRule:{name:[{required:!0,message:"分组名称不能为空",trigger:"blur"}],groupType:[{required:!0,message:"分组类型不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{handleAvatarSuccess:function(t,e){this.dataForm.txtUrl=t.data},init:function(t){var e=this;this.getGroupType(),this.dataForm.id=t||0,this.uploadUrl=this.$http.adornUrl("/app/file/upload"),this.visible=!0,null!=this.$refs.upload&&this.$refs.upload.clearFiles(),this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.id&&e.$http({url:e.$http.adornUrl("/ltt/atdatagroup/info/"+e.dataForm.id),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.name=a.atDataGroup.name,e.dataForm.groupType=a.atDataGroup.groupType,e.dataForm.deleteFlag=a.atDataGroup.deleteFlag,e.dataForm.createTime=a.atDataGroup.createTime,e.groupType=e.dataForm.groupType)})})},getGroupType:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getGroupType"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.options=a.data:t.$message.error(a.msg)})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/atdatagroup/updateBatchGroup"),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,name:t.dataForm.name,txtUrl:t.dataForm.txtUrl,groupType:t.groupType,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},o={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"分组名称",prop:"name"}},[a("el-input",{attrs:{placeholder:"分组名称",disabled:""},model:{value:t.dataForm.name,callback:function(e){t.$set(t.dataForm,"name",e)},expression:"dataForm.name"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"分组类型"}},[a("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{disabled:"",placeholder:"Select",size:"large"},model:{value:t.groupType,callback:function(e){t.groupType=e},expression:"groupType"}},t._l(t.options,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"数据上传",prop:"groupName"}},[a("el-upload",{ref:"upload",staticClass:"upload-demo",attrs:{action:t.uploadUrl,"on-success":t.handleAvatarSuccess}},[a("el-button",{attrs:{size:"small",type:"primary"}},[t._v("点击上传")])],1)],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},l=a("46Yf")(r,o,!1,null,null,null);e.default=l.exports}});