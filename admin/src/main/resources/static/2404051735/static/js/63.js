webpackJsonp([63],{kxYp:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{recordId:0,taskStatus:"",deleteFlag:"",createTime:"",updateTime:"",totalCount:"",successCount:"",failCount:""},dataRule:{taskStatus:[{required:!0,message:"任务状态不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],updateTime:[{required:!0,message:"修改时间不能为空",trigger:"blur"}],totalCount:[{required:!0,message:"总数不能为空",trigger:"blur"}],successCount:[{required:!0,message:"成功数量不能为空",trigger:"blur"}],failCount:[{required:!0,message:"失败数量不能为空",trigger:"blur"}]}}},methods:{init:function(t){var e=this;this.dataForm.recordId=t||0,this.visible=!0,this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.recordId&&e.$http({url:e.$http.adornUrl("/ltt/cdphonefilterrecord/info/"+e.dataForm.recordId),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.taskStatus=a.cdphonefilterrecord.taskStatus,e.dataForm.deleteFlag=a.cdphonefilterrecord.deleteFlag,e.dataForm.createTime=a.cdphonefilterrecord.createTime,e.dataForm.updateTime=a.cdphonefilterrecord.updateTime,e.dataForm.totalCount=a.cdphonefilterrecord.totalCount,e.dataForm.successCount=a.cdphonefilterrecord.successCount,e.dataForm.failCount=a.cdphonefilterrecord.failCount)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/cdphonefilterrecord/"+(t.dataForm.recordId?"update":"save")),method:"post",data:t.$http.adornData({recordId:t.dataForm.recordId||void 0,taskStatus:t.dataForm.taskStatus,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime,updateTime:t.dataForm.updateTime,totalCount:t.dataForm.totalCount,successCount:t.dataForm.successCount,failCount:t.dataForm.failCount})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},o={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"任务状态",prop:"taskStatus"}},[a("el-input",{attrs:{placeholder:"任务状态"},model:{value:t.dataForm.taskStatus,callback:function(e){t.$set(t.dataForm,"taskStatus",e)},expression:"dataForm.taskStatus"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"删除标志",prop:"deleteFlag"}},[a("el-input",{attrs:{placeholder:"删除标志"},model:{value:t.dataForm.deleteFlag,callback:function(e){t.$set(t.dataForm,"deleteFlag",e)},expression:"dataForm.deleteFlag"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[a("el-input",{attrs:{placeholder:"创建时间"},model:{value:t.dataForm.createTime,callback:function(e){t.$set(t.dataForm,"createTime",e)},expression:"dataForm.createTime"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"修改时间",prop:"updateTime"}},[a("el-input",{attrs:{placeholder:"修改时间"},model:{value:t.dataForm.updateTime,callback:function(e){t.$set(t.dataForm,"updateTime",e)},expression:"dataForm.updateTime"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"总数",prop:"totalCount"}},[a("el-input",{attrs:{placeholder:"总数"},model:{value:t.dataForm.totalCount,callback:function(e){t.$set(t.dataForm,"totalCount",e)},expression:"dataForm.totalCount"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"成功数量",prop:"successCount"}},[a("el-input",{attrs:{placeholder:"成功数量"},model:{value:t.dataForm.successCount,callback:function(e){t.$set(t.dataForm,"successCount",e)},expression:"dataForm.successCount"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"失败数量",prop:"failCount"}},[a("el-input",{attrs:{placeholder:"失败数量"},model:{value:t.dataForm.failCount,callback:function(e){t.$set(t.dataForm,"failCount",e)},expression:"dataForm.failCount"}})],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},l=a("46Yf")(r,o,!1,null,null,null);e.default=l.exports}});