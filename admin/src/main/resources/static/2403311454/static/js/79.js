webpackJsonp([79],{az7b:function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,customerServiceId:"",customerService:"",messageUserId:"",messageUid:"",messageType:"",messageContent:"",sendReceive:"",deleteFlag:"",createTime:""},dataRule:{customerServiceId:[{required:!0,message:"客服id不能为空",trigger:"blur"}],customerService:[{required:!0,message:"所属客服不能为空",trigger:"blur"}],messageUserId:[{required:!0,message:"消息人不能为空",trigger:"blur"}],messageUid:[{required:!0,message:"消息uid不能为空",trigger:"blur"}],messageType:[{required:!0,message:"消息类型不能为空",trigger:"blur"}],messageContent:[{required:!0,message:"消息内容不能为空",trigger:"blur"}],sendReceive:[{required:!0,message:"收发不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{init:function(e){var a=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){a.$refs.dataForm.resetFields(),a.dataForm.id&&a.$http({url:a.$http.adornUrl("/ltt/atmessagerecord/info/"+a.dataForm.id),method:"get",params:a.$http.adornParams()}).then(function(e){var t=e.data;t&&0===t.code&&(a.dataForm.customerServiceId=t.atmessagerecord.customerServiceId,a.dataForm.customerService=t.atmessagerecord.customerService,a.dataForm.messageUserId=t.atmessagerecord.messageUserId,a.dataForm.messageUid=t.atmessagerecord.messageUid,a.dataForm.messageType=t.atmessagerecord.messageType,a.dataForm.messageContent=t.atmessagerecord.messageContent,a.dataForm.sendReceive=t.atmessagerecord.sendReceive,a.dataForm.deleteFlag=t.atmessagerecord.deleteFlag,a.dataForm.createTime=t.atmessagerecord.createTime)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(a){a&&e.$http({url:e.$http.adornUrl("/ltt/atmessagerecord/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,customerServiceId:e.dataForm.customerServiceId,customerService:e.dataForm.customerService,messageUserId:e.dataForm.messageUserId,messageUid:e.dataForm.messageUid,messageType:e.dataForm.messageType,messageContent:e.dataForm.messageContent,sendReceive:e.dataForm.sendReceive,deleteFlag:e.dataForm.deleteFlag,createTime:e.dataForm.createTime})}).then(function(a){var t=a.data;t&&0===t.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(t.msg)})})}}},s={render:function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(a){e.visible=a}}},[t("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(a){if(!("button"in a)&&e._k(a.keyCode,"enter",13,a.key))return null;e.dataFormSubmit()}}},[t("el-form-item",{attrs:{label:"客服id",prop:"customerServiceId"}},[t("el-input",{attrs:{placeholder:"客服id"},model:{value:e.dataForm.customerServiceId,callback:function(a){e.$set(e.dataForm,"customerServiceId",a)},expression:"dataForm.customerServiceId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"所属客服",prop:"customerService"}},[t("el-input",{attrs:{placeholder:"所属客服"},model:{value:e.dataForm.customerService,callback:function(a){e.$set(e.dataForm,"customerService",a)},expression:"dataForm.customerService"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"消息人",prop:"messageUserId"}},[t("el-input",{attrs:{placeholder:"消息人"},model:{value:e.dataForm.messageUserId,callback:function(a){e.$set(e.dataForm,"messageUserId",a)},expression:"dataForm.messageUserId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"消息uid",prop:"messageUid"}},[t("el-input",{attrs:{placeholder:"消息uid"},model:{value:e.dataForm.messageUid,callback:function(a){e.$set(e.dataForm,"messageUid",a)},expression:"dataForm.messageUid"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"消息类型",prop:"messageType"}},[t("el-input",{attrs:{placeholder:"消息类型"},model:{value:e.dataForm.messageType,callback:function(a){e.$set(e.dataForm,"messageType",a)},expression:"dataForm.messageType"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"消息内容",prop:"messageContent"}},[t("el-input",{attrs:{placeholder:"消息内容"},model:{value:e.dataForm.messageContent,callback:function(a){e.$set(e.dataForm,"messageContent",a)},expression:"dataForm.messageContent"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"收发",prop:"sendReceive"}},[t("el-input",{attrs:{placeholder:"收发"},model:{value:e.dataForm.sendReceive,callback:function(a){e.$set(e.dataForm,"sendReceive",a)},expression:"dataForm.sendReceive"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"删除标志",prop:"deleteFlag"}},[t("el-input",{attrs:{placeholder:"删除标志"},model:{value:e.dataForm.deleteFlag,callback:function(a){e.$set(e.dataForm,"deleteFlag",a)},expression:"dataForm.deleteFlag"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[t("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(a){e.$set(e.dataForm,"createTime",a)},expression:"dataForm.createTime"}})],1)],1),e._v(" "),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(a){e.visible=!1}}},[e._v("取消")]),e._v(" "),t("el-button",{attrs:{type:"primary"},on:{click:function(a){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},o=t("46Yf")(r,s,!1,null,null,null);a.default=o.exports}});