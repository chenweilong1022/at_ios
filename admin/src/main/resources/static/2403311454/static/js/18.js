webpackJsonp([18,82,83],{"+reW":function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{dataForm:{key:"",id:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,dataUserGroupList:[],dataAvatarGroupList:[],visible:!1,dataRule:{}}},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atdatasubtask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,taskStatus:5,dataTaskId:this.dataForm.id})}).then(function(a){var e=a.data;e&&0===e.code?(t.dataList=e.page.list,t.totalPage=e.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var a=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){a.$refs.addOrUpdate.init(t)})},init:function(t){this.dataForm.id=t||0,this.visible=!0,this.getDataList()},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(a){a&&t.$http({url:t.$http.adornUrl("/ltt/atavatartask/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,taskName:t.dataForm.taskName,taskStatus:t.dataForm.taskStatus,schedule:t.dataForm.schedule,userGroupId:t.dataForm.userGroupId,avatarGroupId:t.dataForm.avatarGroupId,executionQuantity:t.dataForm.executionQuantity,successfulQuantity:t.dataForm.successfulQuantity,failuresQuantity:t.dataForm.failuresQuantity,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(a){var e=a.data;e&&0===e.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(e.msg)})})}}},i={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("el-dialog",{attrs:{title:"错误日志","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(a){t.visible=a}}},[e("div",{staticClass:"mod-config"},[e("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""}},[e("el-table-column",{attrs:{prop:"telephone","header-align":"center",align:"center",label:"联系人手机号"}}),t._v(" "),e("el-table-column",{attrs:{prop:"msg","header-align":"center",align:"center",label:"内容"}}),t._v(" "),e("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}})],1),t._v(" "),e("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?e("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e()],1)])},staticRenderFns:[]},s=e("46Yf")(r,i,!1,null,null,null);a.default=s.exports},eFR7:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{groupType:null,visible:!1,dataUserGroupList:[],datagroupList:[],options:[{value:1,label:"手机号"},{value:2,label:"不封控地区普通uid模式"},{value:3,label:"自定义id"},{value:4,label:"日本，台湾专用uid模式"},{value:5,label:"同步通讯录模式"}],dataForm:{id:0,taskName:"",userGroupId:"",dataGroupId:"",groupType:"",addTotalQuantity:"",successfulQuantity:"",failuresQuantity:"",taskStatus:"",schedule:"",updateTime:"",addQuantityLimit:"",deleteFlag:"",createTime:""},dataRule:{taskName:[{required:!0,message:"任务名称不能为空",trigger:"blur"}],userGroupId:[{required:!0,message:"账户分组不能为空",trigger:"blur"}],dataGroupId:[{required:!0,message:"数据分组不能为空",trigger:"blur"}],groupType:[{required:!0,message:"类型不能为空",trigger:"blur"}],addTotalQuantity:[{required:!0,message:"加粉总数不能为空",trigger:"blur"}],successfulQuantity:[{required:!0,message:"成功数不能为空",trigger:"blur"}],failuresQuantity:[{required:!0,message:"失败数不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"状态不能为空",trigger:"blur"}],schedule:[{required:!0,message:"进度不能为空",trigger:"blur"}],updateTime:[{required:!0,message:"更新时间不能为空",trigger:"blur"}],addQuantityLimit:[{required:!0,message:"加粉数量不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{groupTypeChangeHandler:function(){this.dataForm.groupType=this.groupType,5===this.groupType?this.getDataGroupDataList(1):this.getDataGroupDataList(this.groupType)},getUserGroupDataList:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atusergroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(a){var e=a.data;e&&0===e.code?t.dataUserGroupList=e.page.list:t.dataUserGroupList=[]})},getDataGroupDataList:function(t){var a=this;this.$http({url:this.$http.adornUrl("/ltt/atdatagroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100,groupType:t})}).then(function(t){var e=t.data;e&&0===e.code?a.datagroupList=e.page.list:a.datagroupList=[]})},init:function(t){var a=this;this.dataForm.id=t||0,this.groupType=null,this.visible=!0,this.getUserGroupDataList(),this.getDataGroupDataList(null),this.$nextTick(function(){a.$refs.dataForm.resetFields(),a.dataForm.id&&a.$http({url:a.$http.adornUrl("/ltt/atdatatask/info/"+a.dataForm.id),method:"get",params:a.$http.adornParams()}).then(function(t){var e=t.data;e&&0===e.code&&(a.dataForm.taskName=e.atdatatask.taskName,a.dataForm.userGroupId=e.atdatatask.userGroupId,a.dataForm.dataGroupId=e.atdatatask.dataGroupId,a.dataForm.groupType=e.atdatatask.groupType,a.dataForm.addTotalQuantity=e.atdatatask.addTotalQuantity,a.dataForm.successfulQuantity=e.atdatatask.successfulQuantity,a.dataForm.failuresQuantity=e.atdatatask.failuresQuantity,a.dataForm.taskStatus=e.atdatatask.taskStatus,a.dataForm.schedule=e.atdatatask.schedule,a.dataForm.updateTime=e.atdatatask.updateTime,a.dataForm.addQuantityLimit=e.atdatatask.addQuantityLimit,a.dataForm.deleteFlag=e.atdatatask.deleteFlag,a.dataForm.createTime=e.atdatatask.createTime)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(a){a&&(t.dataForm.groupType=t.groupType,t.$http({url:t.$http.adornUrl("/ltt/atdatatask/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,taskName:t.dataForm.taskName,userGroupId:t.dataForm.userGroupId,dataGroupId:t.dataForm.dataGroupId,groupType:t.dataForm.groupType,addTotalQuantity:t.dataForm.addTotalQuantity,successfulQuantity:t.dataForm.successfulQuantity,failuresQuantity:t.dataForm.failuresQuantity,taskStatus:t.dataForm.taskStatus,schedule:t.dataForm.schedule,updateTime:t.dataForm.updateTime,addQuantityLimit:t.dataForm.addQuantityLimit,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(a){var e=a.data;e&&0===e.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(e.msg)}))})}}},i={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(a){t.visible=a}}},[e("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(a){if(!("button"in a)&&t._k(a.keyCode,"enter",13,a.key))return null;t.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"任务名称",prop:"taskName"}},[e("el-input",{attrs:{placeholder:"任务名称"},model:{value:t.dataForm.taskName,callback:function(a){t.$set(t.dataForm,"taskName",a)},expression:"dataForm.taskName"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"类型",prop:"groupType"}},[e("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{placeholder:"选择类型",size:"large"},on:{change:t.groupTypeChangeHandler},model:{value:t.groupType,callback:function(a){t.groupType=a},expression:"groupType"}},t._l(t.options,function(t){return e("el-option",{key:t.value,attrs:{label:t.label,value:t.value}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"账户分组",prop:"userGroupId"}},[e("el-select",{attrs:{placeholder:"账户分组"},model:{value:t.dataForm.userGroupId,callback:function(a){t.$set(t.dataForm,"userGroupId",a)},expression:"dataForm.userGroupId"}},t._l(t.dataUserGroupList,function(t){return e("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"数据分组",prop:"dataGroupId"}},[e("el-select",{attrs:{placeholder:"数据分组"},model:{value:t.dataForm.dataGroupId,callback:function(a){t.$set(t.dataForm,"dataGroupId",a)},expression:"dataForm.dataGroupId"}},t._l(t.datagroupList,function(t){return e("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"每个号加粉数量",prop:"addQuantityLimit"}},[e("el-input-number",{attrs:{min:1,max:100,label:"每个号加粉数量"},model:{value:t.dataForm.addQuantityLimit,callback:function(a){t.$set(t.dataForm,"addQuantityLimit",a)},expression:"dataForm.addQuantityLimit"}})],1)],1),t._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(a){t.visible=!1}}},[t._v("取消")]),t._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(a){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},s=e("46Yf")(r,i,!1,null,null,null);a.default=s.exports},uZMK:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r=e("eFR7"),i={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,errLogsVisible:!1}},components:{ErrLogs:e("+reW").default,AddOrUpdate:r.default},activated:function(){this.getDataList()},methods:{startUpHandle:function(t){var a=this,e=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+e.join(",")+"]进行["+(t?"启动任务":"批量启动任务")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){a.$http({url:a.$http.adornUrl("/ltt/atdatatask/startUp"),method:"post",data:a.$http.adornData(e,!1)}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.getDataList()}}):a.$message.error(e.msg)})})},getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atdatatask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(a){var e=a.data;e&&0===e.code?(t.dataList=e.page.list,t.totalPage=e.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var a=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){a.$refs.addOrUpdate.init(t)})},errLogsHandle:function(t){var a=this;this.errLogsVisible=!0,this.$nextTick(function(){a.$refs.errLogs.init(t)})},errRetryHandle:function(t){var a=this,e=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+e.join(",")+"]进行["+(t?"错误重试":"批量错误重试")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){a.$http({url:a.$http.adornUrl("/ltt/atdatatask/errRetry"),method:"post",data:a.$http.adornData(e,!1)}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.getDataList()}}):a.$message.error(e.msg)})})},deleteHandle:function(t){var a=this,e=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+e.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){a.$http({url:a.$http.adornUrl("/ltt/atdatatask/delete"),method:"post",data:a.$http.adornData(e,!1)}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.getDataList()}}):a.$message.error(e.msg)})})}}},s={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"mod-config"},[e("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(a){if(!("button"in a)&&t._k(a.keyCode,"enter",13,a.key))return null;t.getDataList()}}},[e("el-form-item",[e("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:t.dataForm.key,callback:function(a){t.$set(t.dataForm,"key",a)},expression:"dataForm.key"}})],1),t._v(" "),e("el-form-item",[e("el-button",{on:{click:function(a){t.getDataList()}}},[t._v("查询")]),t._v(" "),t.isAuth("ltt:atdatatask:save")?e("el-button",{attrs:{type:"primary"},on:{click:function(a){t.addOrUpdateHandle()}}},[t._v("新增")]):t._e(),t._v(" "),t.isAuth("ltt:atdatatask:delete")?e("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(a){t.deleteHandle()}}},[t._v("批量删除")]):t._e()],1)],1),t._v(" "),e("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[e("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),e("el-table-column",{attrs:{prop:"taskName","header-align":"center",align:"center",width:"120",label:"任务名称"}}),t._v(" "),e("el-table-column",{attrs:{prop:"userGroupId","header-align":"center",align:"center",label:"账户分组"}}),t._v(" "),e("el-table-column",{attrs:{prop:"dataGroupId","header-align":"center",align:"center",label:"数据分组"}}),t._v(" "),e("el-table-column",{attrs:{prop:"groupTypeStr","header-align":"center",align:"center",label:"类型"}}),t._v(" "),e("el-table-column",{attrs:{prop:"addTotalQuantity","header-align":"center",align:"center",label:"加粉总数"}}),t._v(" "),e("el-table-column",{attrs:{prop:"successfulQuantity","header-align":"center",align:"center",label:"成功数"}}),t._v(" "),e("el-table-column",{attrs:{prop:"failuresQuantity","header-align":"center",align:"center",label:"失败数"}}),t._v(" "),e("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",label:"状态"},scopedSlots:t._u([{key:"default",fn:function(a){return[3===a.row.taskStatus?e("el-button",{attrs:{type:"success",plain:""}},[t._v(t._s(a.row.taskStatusStr))]):t._e(),t._v(" "),2===a.row.taskStatus?e("el-button",{attrs:{type:"warning",plain:""}},[t._v(t._s(a.row.taskStatusStr))]):t._e(),t._v(" "),1===a.row.taskStatus?e("el-button",{attrs:{type:"primary",plain:""}},[t._v(t._s(a.row.taskStatusStr))]):t._e(),t._v(" "),0===a.row.taskStatus?e("el-button",{attrs:{type:"primary",plain:""}},[t._v(t._s(a.row.taskStatusStr))]):t._e()]}}])}),t._v(" "),e("el-table-column",{attrs:{prop:"schedule","header-align":"center",align:"center",label:"进度"},scopedSlots:t._u([{key:"default",fn:function(t){return[e("el-progress",{attrs:{"stroke-width":10,type:"circle",percentage:t.row.scheduleFloat}})]}}])}),t._v(" "),e("el-table-column",{attrs:{prop:"updateTime","header-align":"center",align:"center",label:"更新时间"}}),t._v(" "),e("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(a){return[0===a.row.taskStatus?e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.startUpHandle(a.row.id)}}},[t._v("启动")]):t._e(),t._v(" "),e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.errRetryHandle(a.row.id)}}},[t._v("错误重试")]),t._v(" "),e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.deleteHandle(a.row.id)}}},[t._v("删除")]),t._v(" "),e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.errLogsHandle(a.row.id)}}},[t._v("错误日志")])]}}])})],1),t._v(" "),e("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?e("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.errLogsVisible?e("err-logs",{ref:"errLogs",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},n=e("46Yf")(i,s,!1,null,null,null);a.default=n.exports}});