webpackJsonp([58],{YnqI:function(t,a,r){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var o={data:function(){var t=this;return{visible:!1,dataForm:{userId:0,password:"",comfirmPassword:""},dataRule:{password:[{validator:function(a,r,o){t.dataForm.id||/\S/.test(r)?o():o(new Error("密码不能为空"))},trigger:"blur"}],comfirmPassword:[{validator:function(a,r,o){t.dataForm.id||/\S/.test(r)?t.dataForm.password!==r?o(new Error("确认密码与密码输入不一致")):o():o(new Error("确认密码不能为空"))},trigger:"blur"}]}}},methods:{init:function(t){this.dataForm.userId=t||0,this.visible=!0},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(a){a&&t.$http({url:t.$http.adornUrl("/ltt/customeruser/updatePassword"),method:"post",data:t.$http.adornData({userId:t.dataForm.userId,password:t.dataForm.password})}).then(function(a){var r=a.data;r&&0===r.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(r.msg)})})}}},e={render:function(){var t=this,a=t.$createElement,r=t._self._c||a;return r("el-dialog",{attrs:{title:"修改密码","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(a){t.visible=a}}},[r("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(a){if(!("button"in a)&&t._k(a.keyCode,"enter",13,a.key))return null;t.dataFormSubmit()}}},[r("el-form-item",{attrs:{label:"密码",prop:"password"}},[r("el-input",{attrs:{type:"password",placeholder:"密码",autocomplete:"off"},model:{value:t.dataForm.password,callback:function(a){t.$set(t.dataForm,"password",a)},expression:"dataForm.password"}})],1),t._v(" "),r("el-form-item",{attrs:{label:"确认密码",prop:"comfirmPassword"}},[r("el-input",{attrs:{type:"password",placeholder:"确认密码",autocomplete:"off"},model:{value:t.dataForm.comfirmPassword,callback:function(a){t.$set(t.dataForm,"comfirmPassword",a)},expression:"dataForm.comfirmPassword"}})],1)],1),t._v(" "),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(a){t.visible=!1}}},[t._v("取消")]),t._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(a){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},s=r("46Yf")(o,e,!1,null,null,null);a.default=s.exports}});