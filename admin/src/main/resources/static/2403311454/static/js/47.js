webpackJsonp([47,90],{"/zYu":function(a,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,avatarTaskId:"",userGroupId:"",avatarGroupId:"",userId:"",avatarId:"",taskStatus:"",deleteFlag:"",createTime:""},dataRule:{avatarTaskId:[{required:!0,message:"头像任务id不能为空",trigger:"blur"}],userGroupId:[{required:!0,message:"账户分组不能为空",trigger:"blur"}],avatarGroupId:[{required:!0,message:"头像分组不能为空",trigger:"blur"}],userId:[{required:!0,message:"账户id不能为空",trigger:"blur"}],avatarId:[{required:!0,message:"头像id不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"任务状态不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{init:function(a){var t=this;this.dataForm.id=a||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/ltt/atavatarsubtask/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(a){var e=a.data;e&&0===e.code&&(t.dataForm.avatarTaskId=e.atavatarsubtask.avatarTaskId,t.dataForm.userGroupId=e.atavatarsubtask.userGroupId,t.dataForm.avatarGroupId=e.atavatarsubtask.avatarGroupId,t.dataForm.userId=e.atavatarsubtask.userId,t.dataForm.avatarId=e.atavatarsubtask.avatarId,t.dataForm.taskStatus=e.atavatarsubtask.taskStatus,t.dataForm.deleteFlag=e.atavatarsubtask.deleteFlag,t.dataForm.createTime=e.atavatarsubtask.createTime)})})},dataFormSubmit:function(){var a=this;this.$refs.dataForm.validate(function(t){t&&a.$http({url:a.$http.adornUrl("/ltt/atavatarsubtask/"+(a.dataForm.id?"update":"save")),method:"post",data:a.$http.adornData({id:a.dataForm.id||void 0,avatarTaskId:a.dataForm.avatarTaskId,userGroupId:a.dataForm.userGroupId,avatarGroupId:a.dataForm.avatarGroupId,userId:a.dataForm.userId,avatarId:a.dataForm.avatarId,taskStatus:a.dataForm.taskStatus,deleteFlag:a.dataForm.deleteFlag,createTime:a.dataForm.createTime})}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.visible=!1,a.$emit("refreshDataList")}}):a.$message.error(e.msg)})})}}},l={render:function(){var a=this,t=a.$createElement,e=a._self._c||t;return e("el-dialog",{attrs:{title:a.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:a.visible},on:{"update:visible":function(t){a.visible=t}}},[e("el-form",{ref:"dataForm",attrs:{model:a.dataForm,rules:a.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&a._k(t.keyCode,"enter",13,t.key))return null;a.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"头像任务id",prop:"avatarTaskId"}},[e("el-input",{attrs:{placeholder:"头像任务id"},model:{value:a.dataForm.avatarTaskId,callback:function(t){a.$set(a.dataForm,"avatarTaskId",t)},expression:"dataForm.avatarTaskId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"账户分组",prop:"userGroupId"}},[e("el-input",{attrs:{placeholder:"账户分组"},model:{value:a.dataForm.userGroupId,callback:function(t){a.$set(a.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"头像分组",prop:"avatarGroupId"}},[e("el-input",{attrs:{placeholder:"头像分组"},model:{value:a.dataForm.avatarGroupId,callback:function(t){a.$set(a.dataForm,"avatarGroupId",t)},expression:"dataForm.avatarGroupId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"账户id",prop:"userId"}},[e("el-input",{attrs:{placeholder:"账户id"},model:{value:a.dataForm.userId,callback:function(t){a.$set(a.dataForm,"userId",t)},expression:"dataForm.userId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"头像id",prop:"avatarId"}},[e("el-input",{attrs:{placeholder:"头像id"},model:{value:a.dataForm.avatarId,callback:function(t){a.$set(a.dataForm,"avatarId",t)},expression:"dataForm.avatarId"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"任务状态",prop:"taskStatus"}},[e("el-input",{attrs:{placeholder:"任务状态"},model:{value:a.dataForm.taskStatus,callback:function(t){a.$set(a.dataForm,"taskStatus",t)},expression:"dataForm.taskStatus"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"删除标志",prop:"deleteFlag"}},[e("el-input",{attrs:{placeholder:"删除标志"},model:{value:a.dataForm.deleteFlag,callback:function(t){a.$set(a.dataForm,"deleteFlag",t)},expression:"dataForm.deleteFlag"}})],1),a._v(" "),e("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[e("el-input",{attrs:{placeholder:"创建时间"},model:{value:a.dataForm.createTime,callback:function(t){a.$set(a.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1)],1),a._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(t){a.visible=!1}}},[a._v("取消")]),a._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(t){a.dataFormSubmit()}}},[a._v("确定")])],1)],1)},staticRenderFns:[]},d=e("46Yf")(r,l,!1,null,null,null);t.default=d.exports},"7QM2":function(a,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:e("/zYu").default},activated:function(){this.getDataList()},methods:{getDataList:function(){var a=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atavatarsubtask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(t){var e=t.data;e&&0===e.code?(a.dataList=e.page.list,a.totalPage=e.page.totalCount):(a.dataList=[],a.totalPage=0),a.dataListLoading=!1})},sizeChangeHandle:function(a){this.pageSize=a,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(a){this.pageIndex=a,this.getDataList()},selectionChangeHandle:function(a){this.dataListSelections=a},addOrUpdateHandle:function(a){var t=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){t.$refs.addOrUpdate.init(a)})},deleteHandle:function(a){var t=this,e=a?[a]:this.dataListSelections.map(function(a){return a.id});this.$confirm("确定对[id="+e.join(",")+"]进行["+(a?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/atavatarsubtask/delete"),method:"post",data:t.$http.adornData(e,!1)}).then(function(a){var e=a.data;e&&0===e.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(e.msg)})})}}},l={render:function(){var a=this,t=a.$createElement,e=a._self._c||t;return e("div",{staticClass:"mod-config"},[e("el-form",{attrs:{inline:!0,model:a.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&a._k(t.keyCode,"enter",13,t.key))return null;a.getDataList()}}},[e("el-form-item",[e("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:a.dataForm.key,callback:function(t){a.$set(a.dataForm,"key",t)},expression:"dataForm.key"}})],1),a._v(" "),e("el-form-item",[e("el-button",{on:{click:function(t){a.getDataList()}}},[a._v("查询")]),a._v(" "),a.isAuth("ltt:atavatarsubtask:save")?e("el-button",{attrs:{type:"primary"},on:{click:function(t){a.addOrUpdateHandle()}}},[a._v("新增")]):a._e(),a._v(" "),a.isAuth("ltt:atavatarsubtask:delete")?e("el-button",{attrs:{type:"danger",disabled:a.dataListSelections.length<=0},on:{click:function(t){a.deleteHandle()}}},[a._v("批量删除")]):a._e()],1)],1),a._v(" "),e("el-table",{directives:[{name:"loading",rawName:"v-loading",value:a.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:a.dataList,border:""},on:{"selection-change":a.selectionChangeHandle}},[e("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),a._v(" "),e("el-table-column",{attrs:{prop:"avatarTaskId","header-align":"center",align:"center",label:"头像任务id"}}),a._v(" "),e("el-table-column",{attrs:{prop:"userGroupId","header-align":"center",align:"center",label:"账户分组"}}),a._v(" "),e("el-table-column",{attrs:{prop:"avatarGroupId","header-align":"center",align:"center",label:"头像分组"}}),a._v(" "),e("el-table-column",{attrs:{prop:"userId","header-align":"center",align:"center",label:"账户id"}}),a._v(" "),e("el-table-column",{attrs:{prop:"avatarId","header-align":"center",align:"center",label:"头像id"}}),a._v(" "),e("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",label:"任务状态"}}),a._v(" "),e("el-table-column",{attrs:{prop:"deleteFlag","header-align":"center",align:"center",label:"删除标志"}}),a._v(" "),e("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),a._v(" "),e("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:a._u([{key:"default",fn:function(t){return[e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){a.addOrUpdateHandle(t.row.id)}}},[a._v("修改")]),a._v(" "),e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){a.deleteHandle(t.row.id)}}},[a._v("删除")])]}}])})],1),a._v(" "),e("el-pagination",{attrs:{"current-page":a.pageIndex,"page-sizes":[10,20,50,100],"page-size":a.pageSize,total:a.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":a.sizeChangeHandle,"current-change":a.currentChangeHandle}}),a._v(" "),a.addOrUpdateVisible?e("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:a.getDataList}}):a._e()],1)},staticRenderFns:[]},d=e("46Yf")(r,l,!1,null,null,null);t.default=d.exports}});