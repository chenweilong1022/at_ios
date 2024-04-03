webpackJsonp([84],{eFR7:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{groupType:null,visible:!1,dataUserGroupList:[],datagroupList:[],countryCodes:[],options:[],dataForm:{id:0,taskName:"",userGroupId:"",dataGroupId:"",groupType:"",addTotalQuantity:"",countryCode:null,successfulQuantity:"",failuresQuantity:"",taskStatus:"",schedule:"",updateTime:"",addQuantityLimit:"",deleteFlag:"",createTime:""},dataRule:{taskName:[{required:!0,message:"任务名称不能为空",trigger:"blur"}],userGroupId:[{required:!0,message:"账户分组不能为空",trigger:"blur"}],dataGroupId:[{required:!0,message:"数据分组不能为空",trigger:"blur"}],groupType:[{required:!0,message:"类型不能为空",trigger:"blur"}],addTotalQuantity:[{required:!0,message:"加粉总数不能为空",trigger:"blur"}],successfulQuantity:[{required:!0,message:"成功数不能为空",trigger:"blur"}],failuresQuantity:[{required:!0,message:"失败数不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"状态不能为空",trigger:"blur"}],schedule:[{required:!0,message:"进度不能为空",trigger:"blur"}],updateTime:[{required:!0,message:"更新时间不能为空",trigger:"blur"}],addQuantityLimit:[{required:!0,message:"加粉数量不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(a){var e=a.data;e&&0===e.code?t.countryCodes=e.data:t.$message.error(e.msg)})},getGroupType:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getGroupType"),method:"get"}).then(function(a){var e=a.data;e&&0===e.code?t.options=e.data:t.$message.error(e.msg)})},groupTypeChangeHandler:function(){this.dataForm.groupType=this.groupType,this.getDataGroupDataList(this.groupType)},getUserGroupDataList:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atusergroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(a){var e=a.data;e&&0===e.code?t.dataUserGroupList=e.page.list:t.dataUserGroupList=[]})},getDataGroupDataList:function(t){var a=this;this.$http({url:this.$http.adornUrl("/ltt/atdatagroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100,groupType:t})}).then(function(t){var e=t.data;e&&0===e.code?a.datagroupList=e.page.list:a.datagroupList=[]})},init:function(t){var a=this;this.dataForm.id=t||0,this.groupType=null,this.visible=!0,this.getGroupType(),this.getCountryCodeEnums(),this.getUserGroupDataList(),this.getDataGroupDataList(null),this.$nextTick(function(){a.$refs.dataForm.resetFields(),a.dataForm.id&&a.$http({url:a.$http.adornUrl("/ltt/atdatatask/info/"+a.dataForm.id),method:"get",params:a.$http.adornParams()}).then(function(t){var e=t.data;e&&0===e.code&&(a.dataForm.taskName=e.atdatatask.taskName,a.dataForm.userGroupId=e.atdatatask.userGroupId,a.dataForm.dataGroupId=e.atdatatask.dataGroupId,a.dataForm.groupType=e.atdatatask.groupType,a.dataForm.addTotalQuantity=e.atdatatask.addTotalQuantity,a.dataForm.successfulQuantity=e.atdatatask.successfulQuantity,a.dataForm.failuresQuantity=e.atdatatask.failuresQuantity,a.dataForm.taskStatus=e.atdatatask.taskStatus,a.dataForm.schedule=e.atdatatask.schedule,a.dataForm.updateTime=e.atdatatask.updateTime,a.dataForm.addQuantityLimit=e.atdatatask.addQuantityLimit,a.dataForm.deleteFlag=e.atdatatask.deleteFlag,a.dataForm.createTime=e.atdatatask.createTime)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(a){a&&(t.dataForm.groupType=t.groupType,t.$http({url:t.$http.adornUrl("/ltt/atdatatask/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,taskName:t.dataForm.taskName,userGroupId:t.dataForm.userGroupId,dataGroupId:t.dataForm.dataGroupId,groupType:t.dataForm.groupType,countryCode:t.dataForm.countryCode,addTotalQuantity:t.dataForm.addTotalQuantity,successfulQuantity:t.dataForm.successfulQuantity,failuresQuantity:t.dataForm.failuresQuantity,taskStatus:t.dataForm.taskStatus,schedule:t.dataForm.schedule,updateTime:t.dataForm.updateTime,addQuantityLimit:t.dataForm.addQuantityLimit,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(a){var e=a.data;e&&0===e.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(e.msg)}))})}}},o={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(a){t.visible=a}}},[e("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(a){if(!("button"in a)&&t._k(a.keyCode,"enter",13,a.key))return null;t.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"任务名称",prop:"taskName"}},[e("el-input",{attrs:{placeholder:"任务名称"},model:{value:t.dataForm.taskName,callback:function(a){t.$set(t.dataForm,"taskName",a)},expression:"dataForm.taskName"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"类型",prop:"groupType"}},[e("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{placeholder:"选择类型",size:"large"},on:{change:t.groupTypeChangeHandler},model:{value:t.groupType,callback:function(a){t.groupType=a},expression:"groupType"}},t._l(t.options,function(t){return e("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"账户分组",prop:"userGroupId"}},[e("el-select",{attrs:{placeholder:"账户分组"},model:{value:t.dataForm.userGroupId,callback:function(a){t.$set(t.dataForm,"userGroupId",a)},expression:"dataForm.userGroupId"}},t._l(t.dataUserGroupList,function(t){return e("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"加粉号国家"}},[e("el-select",{attrs:{placeholder:"拉群号国家",clearable:""},model:{value:t.dataForm.countryCode,callback:function(a){t.$set(t.dataForm,"countryCode",a)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return e("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"数据分组",prop:"dataGroupId"}},[e("el-select",{attrs:{placeholder:"数据分组"},model:{value:t.dataForm.dataGroupId,callback:function(a){t.$set(t.dataForm,"dataGroupId",a)},expression:"dataForm.dataGroupId"}},t._l(t.datagroupList,function(t){return e("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"每个号加粉数量",prop:"addQuantityLimit"}},[e("el-input-number",{attrs:{min:1,max:99,label:"每个号加粉数量"},model:{value:t.dataForm.addQuantityLimit,callback:function(a){t.$set(t.dataForm,"addQuantityLimit",a)},expression:"dataForm.addQuantityLimit"}})],1)],1),t._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(a){t.visible=!1}}},[t._v("取消")]),t._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(a){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},s=e("46Yf")(r,o,!1,null,null,null);a.default=s.exports}});