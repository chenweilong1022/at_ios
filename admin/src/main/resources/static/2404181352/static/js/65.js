webpackJsonp([65],{MpWc:function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,username:"",pass:"",deleteFlag:"",createTime:""},dataRule:{username:[{required:!0,message:"不能为空",trigger:"blur"}],pass:[{required:!0,message:"不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"不能为空",trigger:"blur"}],createTime:[{required:!0,message:"不能为空",trigger:"blur"}]}}},methods:{init:function(e){var a=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){a.$refs.dataForm.resetFields(),a.dataForm.id&&a.$http({url:a.$http.adornUrl("/ltt/cdlineusernamepass/info/"+a.dataForm.id),method:"get",params:a.$http.adornParams()}).then(function(e){var t=e.data;t&&0===t.code&&(a.dataForm.username=t.cdlineusernamepass.username,a.dataForm.pass=t.cdlineusernamepass.pass,a.dataForm.deleteFlag=t.cdlineusernamepass.deleteFlag,a.dataForm.createTime=t.cdlineusernamepass.createTime)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(a){a&&e.$http({url:e.$http.adornUrl("/ltt/cdlineusernamepass/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,username:e.dataForm.username,pass:e.dataForm.pass,deleteFlag:e.dataForm.deleteFlag,createTime:e.dataForm.createTime})}).then(function(a){var t=a.data;t&&0===t.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(t.msg)})})}}},s={render:function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(a){e.visible=a}}},[t("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(a){if(!("button"in a)&&e._k(a.keyCode,"enter",13,a.key))return null;e.dataFormSubmit()}}},[t("el-form-item",{attrs:{label:"",prop:"username"}},[t("el-input",{attrs:{placeholder:""},model:{value:e.dataForm.username,callback:function(a){e.$set(e.dataForm,"username",a)},expression:"dataForm.username"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"",prop:"pass"}},[t("el-input",{attrs:{placeholder:""},model:{value:e.dataForm.pass,callback:function(a){e.$set(e.dataForm,"pass",a)},expression:"dataForm.pass"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"",prop:"deleteFlag"}},[t("el-input",{attrs:{placeholder:""},model:{value:e.dataForm.deleteFlag,callback:function(a){e.$set(e.dataForm,"deleteFlag",a)},expression:"dataForm.deleteFlag"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"",prop:"createTime"}},[t("el-input",{attrs:{placeholder:""},model:{value:e.dataForm.createTime,callback:function(a){e.$set(e.dataForm,"createTime",a)},expression:"dataForm.createTime"}})],1)],1),e._v(" "),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(a){e.visible=!1}}},[e._v("取消")]),e._v(" "),t("el-button",{attrs:{type:"primary"},on:{click:function(a){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},l=t("46Yf")(r,s,!1,null,null,null);a.default=l.exports}});