webpackJsonp([43,81],{"1IDX":function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:a("poID").default},activated:function(){this.getDataList()},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atgrouptask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var e=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(t)})},openCreate:function(t){this.$router.push({name:"atgrouptask-create",query:{id:t}})},deleteHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atgrouptask/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})}}},i={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:t.dataForm.key,callback:function(e){t.$set(t.dataForm,"key",e)},expression:"dataForm.key"}})],1),t._v(" "),a("el-form-item",[a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")]),t._v(" "),t.isAuth("ltt:atgrouptask:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.addOrUpdateHandle()}}},[t._v("新增")]):t._e(),t._v(" "),t.isAuth("ltt:atgrouptask:delete")?a("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.deleteHandle()}}},[t._v("批量删除")]):t._e()],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),a("el-table-column",{attrs:{prop:"id","header-align":"center",align:"center",label:"主键"}}),t._v(" "),a("el-table-column",{attrs:{prop:"taskName","header-align":"center",align:"center",label:"任务名称"}}),t._v(" "),a("el-table-column",{attrs:{prop:"groupType","header-align":"center",align:"center",label:"类型"}}),t._v(" "),a("el-table-column",{attrs:{prop:"addTotalQuantity","header-align":"center",align:"center",label:"加粉总数"}}),t._v(" "),a("el-table-column",{attrs:{prop:"successfulQuantity","header-align":"center",align:"center",label:"成功数"}}),t._v(" "),a("el-table-column",{attrs:{prop:"failuresQuantity","header-align":"center",align:"center",label:"失败数"}}),t._v(" "),a("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",label:"状态"}}),t._v(" "),a("el-table-column",{attrs:{prop:"schedule","header-align":"center",align:"center",label:"进度"}}),t._v(" "),a("el-table-column",{attrs:{prop:"updateTime","header-align":"center",align:"center",label:"更新时间"}}),t._v(" "),a("el-table-column",{attrs:{prop:"deleteFlag","header-align":"center",align:"center",label:"删除标志"}}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),t._v(" "),a("el-table-column",{attrs:{prop:"sysUserId","header-align":"center",align:"center",label:"管理账户id"}}),t._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.addOrUpdateHandle(e.row.id)}}},[t._v("修改")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.deleteHandle(e.row.id)}}},[t._v("删除")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.openCreate(e.row.id)}}},[t._v("整合")])]}}])})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},n=a("46Yf")(r,i,!1,null,null,null);e.default=n.exports},poID:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,taskName:"",groupType:"",addTotalQuantity:"",successfulQuantity:"",failuresQuantity:"",taskStatus:"",schedule:"",updateTime:"",deleteFlag:"",createTime:"",sysUserId:""},dataRule:{taskName:[{required:!0,message:"任务名称不能为空",trigger:"blur"}],groupType:[{required:!0,message:"类型不能为空",trigger:"blur"}],addTotalQuantity:[{required:!0,message:"加粉总数不能为空",trigger:"blur"}],successfulQuantity:[{required:!0,message:"成功数不能为空",trigger:"blur"}],failuresQuantity:[{required:!0,message:"失败数不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"状态不能为空",trigger:"blur"}],schedule:[{required:!0,message:"进度不能为空",trigger:"blur"}],updateTime:[{required:!0,message:"更新时间不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],sysUserId:[{required:!0,message:"管理账户id不能为空",trigger:"blur"}]}}},methods:{init:function(t){var e=this;this.dataForm.id=t||0,this.visible=!0,this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.id&&e.$http({url:e.$http.adornUrl("/ltt/atgrouptask/info/"+e.dataForm.id),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.taskName=a.atgrouptask.taskName,e.dataForm.groupType=a.atgrouptask.groupType,e.dataForm.addTotalQuantity=a.atgrouptask.addTotalQuantity,e.dataForm.successfulQuantity=a.atgrouptask.successfulQuantity,e.dataForm.failuresQuantity=a.atgrouptask.failuresQuantity,e.dataForm.taskStatus=a.atgrouptask.taskStatus,e.dataForm.schedule=a.atgrouptask.schedule,e.dataForm.updateTime=a.atgrouptask.updateTime,e.dataForm.deleteFlag=a.atgrouptask.deleteFlag,e.dataForm.createTime=a.atgrouptask.createTime,e.dataForm.sysUserId=a.atgrouptask.sysUserId)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/atgrouptask/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,taskName:t.dataForm.taskName,groupType:t.dataForm.groupType,addTotalQuantity:t.dataForm.addTotalQuantity,successfulQuantity:t.dataForm.successfulQuantity,failuresQuantity:t.dataForm.failuresQuantity,taskStatus:t.dataForm.taskStatus,schedule:t.dataForm.schedule,updateTime:t.dataForm.updateTime,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime,sysUserId:t.dataForm.sysUserId})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},i={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"任务名称",prop:"taskName"}},[a("el-input",{attrs:{placeholder:"任务名称"},model:{value:t.dataForm.taskName,callback:function(e){t.$set(t.dataForm,"taskName",e)},expression:"dataForm.taskName"}})],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},n=a("46Yf")(r,i,!1,null,null,null);e.default=n.exports}});