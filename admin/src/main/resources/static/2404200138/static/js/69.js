webpackJsonp([69],{"6XLs":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:"设置设备名称","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"设备名称",prop:"deviceName"}},[a("el-input",{attrs:{placeholder:"设备名称"},model:{value:e.dataForm.deviceName,callback:function(t){e.$set(e.dataForm,"deviceName",t)},expression:"dataForm.deviceName"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},i=a("46Yf")({data:function(){return{visible:!1,dataForm:{deviceId:null,deviceName:null},dataRule:{deviceName:[{required:!0,message:"设备名称不能为空",trigger:"blur"}]}}},methods:{init:function(e){this.visible=!0,this.dataForm.deviceId=e},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atusertokenios/updateDeviceName"),method:"post",data:e.$http.adornData({deviceId:e.dataForm.deviceId,deviceName:e.dataForm.deviceName})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})},queryUserGroupBySearchWord:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/atusergroup/queryByFuzzyName?searchWord="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.userGroupOptions=a.groupList)})}}},r,!1,null,null,null);t.default=i.exports}});