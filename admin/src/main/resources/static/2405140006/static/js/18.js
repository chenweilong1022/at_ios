webpackJsonp([18,76,77],{g0GN:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{dataUserGroupList:[],dataUsernameGroupList:[],visible:!1,dataForm:{id:0,taskName:"",taskStatus:"",schedule:"",userGroupId:"",usernameGroupId:"",executionQuantity:"",successfulQuantity:"",failuresQuantity:"",deleteFlag:"",createTime:""},dataRule:{taskName:[{required:!0,message:"任务名称不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"任务状态不能为空",trigger:"blur"}],schedule:[{required:!0,message:"进度不能为空",trigger:"blur"}],userGroupId:[{required:!0,message:"账户分组不能为空",trigger:"blur"}],usernameGroupId:[{required:!0,message:"昵称分组不能为空",trigger:"blur"}],executionQuantity:[{required:!0,message:"执行数量不能为空",trigger:"blur"}],successfulQuantity:[{required:!0,message:"成功数量不能为空",trigger:"blur"}],failuresQuantity:[{required:!0,message:"失败数量不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{getUsernameGroupDataList:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atusernamegroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(e){var a=e.data;a&&0===a.code?t.dataUsernameGroupList=a.page.list:t.dataUsernameGroupList=[]})},getUserGroupDataList:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atusergroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(e){var a=e.data;a&&0===a.code?t.dataUserGroupList=a.page.list:t.dataUserGroupList=[]})},init:function(t){var e=this;this.dataForm.id=t||0,this.visible=!0,this.getUserGroupDataList(),this.getUsernameGroupDataList(),this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.id&&e.$http({url:e.$http.adornUrl("/ltt/atusernametask/info/"+e.dataForm.id),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.taskName=a.atusernametask.taskName,e.dataForm.taskStatus=a.atusernametask.taskStatus,e.dataForm.schedule=a.atusernametask.schedule,e.dataForm.userGroupId=a.atusernametask.userGroupId,e.dataForm.usernameGroupId=a.atusernametask.usernameGroupId,e.dataForm.executionQuantity=a.atusernametask.executionQuantity,e.dataForm.successfulQuantity=a.atusernametask.successfulQuantity,e.dataForm.failuresQuantity=a.atusernametask.failuresQuantity,e.dataForm.deleteFlag=a.atusernametask.deleteFlag,e.dataForm.createTime=a.atusernametask.createTime)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/atusernametask/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,taskName:t.dataForm.taskName,taskStatus:t.dataForm.taskStatus,schedule:t.dataForm.schedule,userGroupId:t.dataForm.userGroupId,usernameGroupId:t.dataForm.usernameGroupId,executionQuantity:t.dataForm.executionQuantity,successfulQuantity:t.dataForm.successfulQuantity,failuresQuantity:t.dataForm.failuresQuantity,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},s={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"任务名称",prop:"taskName"}},[a("el-input",{attrs:{placeholder:"任务名称"},model:{value:t.dataForm.taskName,callback:function(e){t.$set(t.dataForm,"taskName",e)},expression:"dataForm.taskName"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"账户分组",prop:"userGroupId"}},[a("el-select",{attrs:{placeholder:"账户分组"},model:{value:t.dataForm.userGroupId,callback:function(e){t.$set(t.dataForm,"userGroupId",e)},expression:"dataForm.userGroupId"}},t._l(t.dataUserGroupList,function(t){return a("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"昵称分组",prop:"usernameGroupId"}},[a("el-select",{attrs:{placeholder:"昵称分组"},model:{value:t.dataForm.usernameGroupId,callback:function(e){t.$set(t.dataForm,"usernameGroupId",e)},expression:"dataForm.usernameGroupId"}},t._l(t.dataUsernameGroupList,function(t){return a("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},i=a("46Yf")(r,s,!1,null,null,null);e.default=i.exports},pmwI:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=a("g0GN"),s={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,errLogsVisible:!1}},components:{ErrLogs:a("qDH8").default,AddOrUpdate:r.default},activated:function(){this.getDataList()},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atusernametask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var e=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(t)})},errLogsHandle:function(t){var e=this;this.errLogsVisible=!0,this.$nextTick(function(){e.$refs.errLogs.init(t)})},errRetryHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"错误重试":"批量错误重试")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atusernametask/errRetry"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},deleteHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atusernametask/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})}}},i={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:t.dataForm.key,callback:function(e){t.$set(t.dataForm,"key",e)},expression:"dataForm.key"}})],1),t._v(" "),a("el-form-item",[a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")]),t._v(" "),t.isAuth("ltt:atusernametask:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.addOrUpdateHandle()}}},[t._v("新增")]):t._e(),t._v(" "),t.isAuth("ltt:atusernametask:delete")?a("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.deleteHandle()}}},[t._v("批量删除")]):t._e()],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{prop:"taskName","header-align":"center",align:"center",label:"任务名称"}}),t._v(" "),a("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",label:"任务状态"},scopedSlots:t._u([{key:"default",fn:function(e){return[3===e.row.taskStatus?a("el-button",{attrs:{type:"success",plain:""}},[t._v(t._s(e.row.taskStatusStr))]):t._e(),t._v(" "),2===e.row.taskStatus?a("el-button",{attrs:{type:"warning",plain:""}},[t._v(t._s(e.row.taskStatusStr))]):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"schedule","header-align":"center",align:"center",label:"进度"},scopedSlots:t._u([{key:"default",fn:function(t){return[a("el-progress",{attrs:{"stroke-width":10,type:"circle",percentage:t.row.scheduleFloat}})]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"userGroupId","header-align":"center",align:"center",label:"账户分组"}}),t._v(" "),a("el-table-column",{attrs:{prop:"usernameGroupId","header-align":"center",align:"center",label:"昵称分组"}}),t._v(" "),a("el-table-column",{attrs:{prop:"executionQuantity","header-align":"center",align:"center",label:"执行数量"}}),t._v(" "),a("el-table-column",{attrs:{prop:"successfulQuantity","header-align":"center",align:"center",label:"成功数量"}}),t._v(" "),a("el-table-column",{attrs:{prop:"failuresQuantity","header-align":"center",align:"center",label:"失败数量"}}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),t._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.errRetryHandle(e.row.id)}}},[t._v("错误重试")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.deleteHandle(e.row.id)}}},[t._v("删除")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.errLogsHandle(e.row.id)}}},[t._v("错误日志")])]}}])})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.errLogsVisible?a("err-logs",{ref:"errLogs",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},n=a("46Yf")(s,i,!1,null,null,null);e.default=n.exports},qDH8:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{dataForm:{key:"",id:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,dataUserGroupList:[],dataAvatarGroupList:[],visible:!1,dataRule:{}}},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atusernamesubtask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,taskStatus:5,usernameTaskId:this.dataForm.id})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var e=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(t)})},init:function(t){this.dataForm.id=t||0,this.visible=!0,this.getDataList()},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/atavatartask/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,taskName:t.dataForm.taskName,taskStatus:t.dataForm.taskStatus,schedule:t.dataForm.schedule,userGroupId:t.dataForm.userGroupId,avatarGroupId:t.dataForm.avatarGroupId,executionQuantity:t.dataForm.executionQuantity,successfulQuantity:t.dataForm.successfulQuantity,failuresQuantity:t.dataForm.failuresQuantity,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},s={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:"错误日志","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("div",{staticClass:"mod-config"},[a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""}},[a("el-table-column",{attrs:{prop:"userTelephone","header-align":"center",align:"center",label:"联系人手机号"}}),t._v(" "),a("el-table-column",{attrs:{prop:"msg","header-align":"center",align:"center",label:"内容"}}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e()],1)])},staticRenderFns:[]},i=a("46Yf")(r,s,!1,null,null,null);e.default=i.exports}});