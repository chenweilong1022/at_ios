webpackJsonp([87],{"17Zv":function(a,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,dataGroupId:"",data:"",useFlag:"",deleteFlag:"",createTime:""},dataRule:{dataGroupId:[{required:!0,message:"数据分组id不能为空",trigger:"blur"}],data:[{required:!0,message:"数据不能为空",trigger:"blur"}],useFlag:[{required:!0,message:"使用标识不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{init:function(a){var t=this;this.dataForm.id=a||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/ltt/atdata/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(a){var e=a.data;e&&0===e.code&&(t.dataForm.dataGroupId=e.atdata.dataGroupId,t.dataForm.data=e.atdata.data,t.dataForm.useFlag=e.atdata.useFlag,t.dataForm.deleteFlag=e.atdata.deleteFlag,t.dataForm.createTime=e.atdata.createTime)})})},dataFormSubmit:function(){var a=this;this.$refs.dataForm.validate(function(t){t&&a.$http({url:a.$http.adornUrl("/ltt/atdata/"+(a.dataForm.id?"update":"save")),method:"post",data:a.$http.adornData({id:a.dataForm.id||void 0,dataGroupId:a.dataForm.dataGroupId,data:a.dataForm.data,useFlag:a.dataForm.useFlag,deleteFlag:a.dataForm.deleteFlag,createTime:a.dataForm.createTime})}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.visible=!1,a.$emit("refreshDataList")}}):a.$message.error(e.msg)})})}}},d={render:function(){var a=this,t=a.$createElement,e=a._self._c||t;return e("el-dialog",{attrs:{title:a.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:a.visible},on:{"update:visible":function(t){a.visible=t}}},[e("el-form",{ref:"dataForm",attrs:{model:a.dataForm,rules:a.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&a._k(t.keyCode,"enter",13,t.key))return null;a.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"数据分组id",prop:"dataGroupId"}},[e("el-input",{attrs:{placeholder:"数据分组id"},model:{value:a.dataForm.dataGroupId,callback:function(t){a.$set(a.dataForm,"dataGroupId",t)},expression:"dataForm.dataGroupId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"数据",prop:"data"}},[e("el-input",{attrs:{placeholder:"数据"},model:{value:a.dataForm.data,callback:function(t){a.$set(a.dataForm,"data",t)},expression:"dataForm.data"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"使用标识",prop:"useFlag"}},[e("el-input",{attrs:{placeholder:"使用标识"},model:{value:a.dataForm.useFlag,callback:function(t){a.$set(a.dataForm,"useFlag",t)},expression:"dataForm.useFlag"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"删除标志",prop:"deleteFlag"}},[e("el-input",{attrs:{placeholder:"删除标志"},model:{value:a.dataForm.deleteFlag,callback:function(t){a.$set(a.dataForm,"deleteFlag",t)},expression:"dataForm.deleteFlag"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[e("el-input",{attrs:{placeholder:"创建时间"},model:{value:a.dataForm.createTime,callback:function(t){a.$set(a.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1)],1),a._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(t){a.visible=!1}}},[a._v("取消")]),a._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(t){a.dataFormSubmit()}}},[a._v("确定")])],1)],1)},staticRenderFns:[]},l=e("46Yf")(r,d,!1,null,null,null);t.default=l.exports}});