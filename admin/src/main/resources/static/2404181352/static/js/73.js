webpackJsonp([73],{qDH8:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var i={data:function(){return{dataForm:{key:"",id:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,dataUserGroupList:[],dataAvatarGroupList:[],visible:!1,dataRule:{}}},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atusernamesubtask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,taskStatus:5,usernameTaskId:this.dataForm.id})}).then(function(a){var e=a.data;e&&0===e.code?(t.dataList=e.page.list,t.totalPage=e.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var a=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){a.$refs.addOrUpdate.init(t)})},init:function(t){this.dataForm.id=t||0,this.visible=!0,this.getDataList()},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(a){a&&t.$http({url:t.$http.adornUrl("/ltt/atavatartask/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,taskName:t.dataForm.taskName,taskStatus:t.dataForm.taskStatus,schedule:t.dataForm.schedule,userGroupId:t.dataForm.userGroupId,avatarGroupId:t.dataForm.avatarGroupId,executionQuantity:t.dataForm.executionQuantity,successfulQuantity:t.dataForm.successfulQuantity,failuresQuantity:t.dataForm.failuresQuantity,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(a){var e=a.data;e&&0===e.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(e.msg)})})}}},s={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("el-dialog",{attrs:{title:"错误日志","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(a){t.visible=a}}},[e("div",{staticClass:"mod-config"},[e("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""}},[e("el-table-column",{attrs:{prop:"userTelephone","header-align":"center",align:"center",label:"联系人手机号"}}),t._v(" "),e("el-table-column",{attrs:{prop:"msg","header-align":"center",align:"center",label:"内容"}}),t._v(" "),e("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}})],1),t._v(" "),e("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?e("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e()],1)])},staticRenderFns:[]},n=e("46Yf")(i,s,!1,null,null,null);a.default=n.exports}});