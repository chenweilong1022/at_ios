webpackJsonp([79],{Q1dw:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o={data:function(){return{visible:!1,userGroupOptions:[],userGroupName:"",dataForm:{ids:[],userGroupId:""},dataRule:{userGroupId:[{required:!0,message:"分组id不能为空",trigger:"blur"}]}}},methods:{userGroupChangeHandler:function(e){var t=this;this.userGroupOptions.find(function(e){t.userGroupName=e.name})},init:function(e){this.visible=!0,this.ids=e,console.log(e),this.queryUserGroupBySearchWord()},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atuser/updateUserGroup"),method:"post",data:e.$http.adornData({ids:e.ids,userGroupId:e.dataForm.userGroupId,userGroupName:e.userGroupName})}).then(function(t){var r=t.data;r&&0===r.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(r.msg)})})},queryUserGroupBySearchWord:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/atusergroup/queryByFuzzyName?searchWord="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var r=e.data;r&&0===r.code&&(t.userGroupOptions=r.groupList)})}}},a={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("el-dialog",{attrs:{title:"转移分组","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[r("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[r("el-form-item",{attrs:{label:"账号分组",prop:"userGroupId"}},[r("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择分组","remote-method":e.queryUserGroupBySearchWord,loading:e.loading},on:{change:e.userGroupChangeHandler},model:{value:e.dataForm.userGroupId,callback:function(t){e.$set(e.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}},e._l(e.userGroupOptions,function(e){return r("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})}))],1)],1),e._v(" "),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},s=r("46Yf")(o,a,!1,null,null,null);t.default=s.exports}});