webpackJsonp([34,64],{LMOi:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={data:function(){return{visibleFlag:!1,dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],taskStatusCodes:[{key:2,value:"查询中"},{key:3,value:"查询完成"},{key:4,value:"查询失败"}],addOrUpdateVisible:!1}},components:{AddOrUpdate:a("O36k").default},activated:function(){this.getDataList()},methods:{init:function(t){this.pageIndex=1,this.visibleFlag=!0,this.dataForm.recordId=t,this.getDataList(t)},getDataList:function(t){var e=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/cdphonefilter/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,recordId:this.dataForm.recordId,taskStatus:this.dataForm.taskStatus})}).then(function(t){var a=t.data;a&&0===a.code?(e.dataList=a.page.list,e.totalPage=a.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var e=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(t)})},deleteHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/cdphonefilter/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})}}},i={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:"手机号筛选详情列表","close-on-click-modal":!1,visible:t.visibleFlag},on:{"update:visible":function(e){t.visibleFlag=e}}},[a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",{attrs:{label:"任务状态",prop:"taskStatus"}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择任务状态"},model:{value:t.dataForm.taskStatus,callback:function(e){t.$set(t.dataForm,"taskStatus",e)},expression:"dataForm.taskStatus"}},t._l(t.taskStatusCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",[a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")])],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",label:"任务状态"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(t.taskStatusCodes,function(n){return e.row.taskStatus===n.key?a("el-tag",{key:n.key},[t._v("\n            "+t._s(n.value)+"\n          ")]):t._e()})}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"contactKey","header-align":"center",align:"center",label:"手机号"}}),t._v(" "),a("el-table-column",{attrs:{prop:"mid","header-align":"center",align:"center",label:"mid"}}),t._v(" "),a("el-table-column",{attrs:{prop:"displayName","header-align":"center",align:"center",label:"名称"}}),t._v(" "),a("el-table-column",{attrs:{prop:"msg","header-align":"center",align:"center",label:"失败备注"}}),t._v(" "),a("el-table-column",{attrs:{prop:"createdTime","header-align":"center",align:"center",label:"创建时间"}})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e()],1)])},staticRenderFns:[]},l=a("46Yf")(n,i,!1,null,null,null);e.default=l.exports},rsU1:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("O36k"),i={data:function(){return{dataForm:{taskStatus:null},dataList:[],pageIndex:1,pageSize:10,totalPage:0,detailListVisible:!1,dataListLoading:!1,dataListSelections:[],taskStatusCodes:[{key:2,value:"查询中"},{key:3,value:"查询完成"},{key:4,value:"查询失败"}],addOrUpdateVisible:!1}},components:{DetailList:a("LMOi").default,AddOrUpdate:n.default},activated:function(){this.getDataList()},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/cdphonefilter/recordList"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,taskStatus:this.dataForm.taskStatus})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var e=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(t)})},deleteHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.recordId});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/cdphonefilterrecord/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},exportTxt:function(t){window.open(this.$http.adornUrl("/ltt/cdphonefilter/exportSJ?recordId="+t+"&token="+this.$cookie.get("token")))},detailList:function(t){var e=this;this.detailListVisible=!0,this.$nextTick(function(){e.$refs.detailList.init(t)})}}},l={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",{attrs:{label:"任务状态",prop:"taskStatus"}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择任务状态"},model:{value:t.dataForm.taskStatus,callback:function(e){t.$set(t.dataForm,"taskStatus",e)},expression:"dataForm.taskStatus"}},t._l(t.taskStatusCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",[a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")]),t._v(" "),t.isAuth("ltt:cdphonefilter:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.addOrUpdateHandle()}}},[t._v("新增")]):t._e()],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{prop:"recordId","header-align":"center",align:"center",label:"记录编号"}}),t._v(" "),a("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",label:"任务状态"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(t.taskStatusCodes,function(n){return e.row.taskStatus===n.key?a("el-tag",{key:n.key},[t._v("\n            "+t._s(n.value)+"\n          ")]):t._e()})}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"totalCount","header-align":"center",align:"center",label:"总数量"}}),t._v(" "),a("el-table-column",{attrs:{prop:"successCount","header-align":"center",align:"center",label:"成功数量"}}),t._v(" "),a("el-table-column",{attrs:{prop:"failCount","header-align":"center",align:"center",label:"失败数量"}}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),t._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.exportTxt(e.row.recordId)}}},[t._v("导出token")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.detailList(e.row.recordId)}}},[t._v("详情")])]}}])})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.detailListVisible?a("detail-list",{ref:"detailList",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},s=a("46Yf")(i,l,!1,null,null,null);e.default=s.exports}});