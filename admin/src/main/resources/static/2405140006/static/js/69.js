webpackJsonp([69],{k06q:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,ip:"",deleteFlag:"",createTime:"",tokenPhone:"",lzCountry:""},dataRule:{ip:[{required:!0,message:"不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"不能为空",trigger:"blur"}],createTime:[{required:!0,message:"不能为空",trigger:"blur"}],tokenPhone:[{required:!0,message:"不能为空",trigger:"blur"}],lzCountry:[{required:!0,message:"不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/ltt/cdlineip/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataForm.ip=a.cdlineip.ip,t.dataForm.deleteFlag=a.cdlineip.deleteFlag,t.dataForm.createTime=a.cdlineip.createTime,t.dataForm.tokenPhone=a.cdlineip.tokenPhone,t.dataForm.lzCountry=a.cdlineip.lzCountry)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/cdlineip/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,ip:e.dataForm.ip,deleteFlag:e.dataForm.deleteFlag,createTime:e.dataForm.createTime,tokenPhone:e.dataForm.tokenPhone,lzCountry:e.dataForm.lzCountry})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},o={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"",prop:"ip"}},[a("el-input",{attrs:{placeholder:""},model:{value:e.dataForm.ip,callback:function(t){e.$set(e.dataForm,"ip",t)},expression:"dataForm.ip"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"",prop:"deleteFlag"}},[a("el-input",{attrs:{placeholder:""},model:{value:e.dataForm.deleteFlag,callback:function(t){e.$set(e.dataForm,"deleteFlag",t)},expression:"dataForm.deleteFlag"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"",prop:"createTime"}},[a("el-input",{attrs:{placeholder:""},model:{value:e.dataForm.createTime,callback:function(t){e.$set(e.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"",prop:"tokenPhone"}},[a("el-input",{attrs:{placeholder:""},model:{value:e.dataForm.tokenPhone,callback:function(t){e.$set(e.dataForm,"tokenPhone",t)},expression:"dataForm.tokenPhone"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"",prop:"lzCountry"}},[a("el-input",{attrs:{placeholder:""},model:{value:e.dataForm.lzCountry,callback:function(t){e.$set(e.dataForm,"lzCountry",t)},expression:"dataForm.lzCountry"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},l=a("46Yf")(r,o,!1,null,null,null);t.default=l.exports}});