webpackJsonp([88],{"1GVP":function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("el-dialog",{directives:[{name:"loading",rawName:"v-loading",value:e.isLoading,expression:"isLoading"}],attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[r("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[r("el-form-item",{attrs:{label:"拉群号国家"}},[r("el-select",{attrs:{placeholder:"拉群号国家",clearable:""},model:{value:e.dataForm.countryCode,callback:function(t){e.$set(e.dataForm,"countryCode",t)},expression:"dataForm.countryCode"}},e._l(e.countryCodes,function(e){return r("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),r("el-form-item",{attrs:{label:"拉群号分组",prop:"userGroupId"}},[r("el-select",{attrs:{placeholder:"账户分组"},model:{value:e.dataForm.userGroupId,callback:function(t){e.$set(e.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}},e._l(e.dataUserGroupList,function(e){return r("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})}))],1),e._v(" "),r("el-form-item",{attrs:{label:"拉群号数量",prop:"pullGroupNumber"}},[r("el-input",{attrs:{placeholder:"拉群号数量"},model:{value:e.dataForm.pullGroupNumber,callback:function(t){e.$set(e.dataForm,"pullGroupNumber",t)},expression:"dataForm.pullGroupNumber"}})],1)],1),e._v(" "),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},s=r("46Yf")({data:function(){return{visible:!1,countryCodes:[],dataUserGroupList:[],isLoading:!1,dataForm:{id:0,countryCode:null,userGroupId:null,pullGroupNumber:1,ids:[],taskName:"",groupType:"",addTotalQuantity:"",successfulQuantity:"",failuresQuantity:"",taskStatus:"",schedule:"",updateTime:"",deleteFlag:"",createTime:"",sysUserId:""},dataRule:{taskName:[{required:!0,message:"任务名称不能为空",trigger:"blur"}],groupType:[{required:!0,message:"类型不能为空",trigger:"blur"}],addTotalQuantity:[{required:!0,message:"加粉总数不能为空",trigger:"blur"}],successfulQuantity:[{required:!0,message:"成功数不能为空",trigger:"blur"}],failuresQuantity:[{required:!0,message:"失败数不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"状态不能为空",trigger:"blur"}],schedule:[{required:!0,message:"进度不能为空",trigger:"blur"}],updateTime:[{required:!0,message:"更新时间不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],sysUserId:[{required:!0,message:"管理账户id不能为空",trigger:"blur"}]}}},methods:{getUserGroupDataList:function(){var e=this;this.$http({url:this.$http.adornUrl("/ltt/atusergroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(t){var r=t.data;r&&0===r.code?e.dataUserGroupList=r.page.list:e.dataUserGroupList=[]})},init:function(e){this.dataForm.ids=e,this.visible=!0,this.getCountryCodeEnums(),this.getUserGroupDataList()},getCountryCodeEnums:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(t){var r=t.data;r&&0===r.code?e.countryCodes=r.data:e.$message.error(r.msg)})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&(e.isLoading=!0,e.$http({url:e.$http.adornUrl("/ltt/atgroup/reallocateToken"),method:"post",data:e.$http.adornData({ids:e.dataForm.ids,userGroupId:e.dataForm.userGroupId,pullGroupNumber:e.dataForm.pullGroupNumber,countryCode:e.dataForm.countryCode})}).then(function(t){var r=t.data;r&&0===r.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(r.msg)}).finally(function(){e.isLoading=!1}))})}}},a,!1,null,null,null);t.default=s.exports}});