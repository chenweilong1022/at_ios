webpackJsonp([103],{Cvvx:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("el-dialog",{attrs:{title:"充值","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[s("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[s("el-form-item",{attrs:{label:"所属账号",prop:"sysUserId"}},[s("el-select",{attrs:{filterable:"",remote:"",disabled:t.userDisabled,placeholder:"请选择账号","remote-method":t.queryBySearchWord,loading:t.loading},model:{value:t.dataForm.sysUserId,callback:function(e){t.$set(t.dataForm,"sysUserId",e)},expression:"dataForm.sysUserId"}},t._l(t.sysUserAccountOptions,function(t){return s("el-option",{key:t.userId,attrs:{label:t.username,value:t.userId}})}))],1),t._v(" "),s("el-form-item",{attrs:{label:"充值金额",prop:"amount"}},[s("el-input",{attrs:{placeholder:"充值金额"},model:{value:t.dataForm.amount,callback:function(e){t.$set(t.dataForm,"amount",e)},expression:"dataForm.amount"}})],1)],1),t._v(" "),s("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[s("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),s("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},r=s("46Yf")({data:function(){return{visible:!1,userDisabled:!1,sysUserAccountOptions:[],dataForm:{sysUserId:"",amount:""},dataRule:{sysUserId:[{required:!0,message:"所属账户不能为空",trigger:"blur"}],amount:[{required:!0,message:"充值金额",trigger:"blur"}]}}},methods:{init:function(t,e){var s=this;this.dataForm.sysUserId=null,this.visible=!0,this.$nextTick(function(){s.$refs.dataForm.resetFields(),t?(s.dataForm.sysUserId=t,s.userDisabled=!0,s.sysUserAccountOptions=[{userId:t,username:e}]):s.queryBySearchWord()})},queryBySearchWord:function(t){var e=this;t=null==t?"":t+"",this.$http({url:this.$http.adornUrl("/sys/user/queryBySearchWord?searchWord="+t),method:"get",params:this.$http.adornParams()}).then(function(t){var s=t.data;s&&0===s.code&&(e.sysUserAccountOptions=s.userList)})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/accountbalance/changeAccount"),method:"post",data:t.$http.adornData({sysUserId:t.dataForm.sysUserId,amount:t.dataForm.amount,transactionType:1,description:"账户充值"})}).then(function(e){var s=e.data;s&&0===s.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(s.msg)})})}}},a,!1,null,null,null);e.default=r.exports}});