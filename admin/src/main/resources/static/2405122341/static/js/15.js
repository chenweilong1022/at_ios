webpackJsonp([15],{u9yT:function(e,t,a){(e.exports=a("acE3")(!1)).push([e.i,"\n.item {\n  margin-top: 10px;\n  margin-right: 20px;\n}\n",""])},uYTX:function(e,t,a){var r=a("u9yT");"string"==typeof r&&(r=[[e.i,r,""]]),r.locals&&(e.exports=r.locals);a("XkoO")("50f4563f",r,!0)},"y49+":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=a("IHPB"),n=a.n(r),i=a("QF3l"),s={data:function(){return{registerStatus:null,registerStatusCodes:[],dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,visible:!1,dataForm:{id:0,taskName:null},dataRule:{totalAmount:[{required:!0,message:"不能为空",trigger:"blur"}],numberThreads:[{required:!0,message:"不能为空",trigger:"blur"}],numberRegistered:[{required:!0,message:"不能为空",trigger:"blur"}],numberSuccesses:[{required:!0,message:"不能为空",trigger:"blur"}],numberFailures:[{required:!0,message:"不能为空",trigger:"blur"}],registrationStatus:[{required:!0,message:"不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"不能为空",trigger:"blur"}],createTime:[{required:!0,message:"不能为空",trigger:"blur"}]}}},components:{AddOrUpdate:a("5XyS").default,CdlineRegisterList:i.default},methods:{getDataList:function(){var e=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/cdlineregister/listByTaskId"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,registerStatus:this.registerStatus,tasksId:this.dataForm.id,phone:this.dataForm.phone})}).then(function(t){var a=t.data;a&&0===a.code?(e.dataList=a.page.list,e.totalPage=a.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},init:function(e,t){this.dataForm.id=e||0,this.dataForm.taskName=t||"详情",this.visible=!0,this.getRegisterStatus(),this.getDataList()},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},registerRetryHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定重新注册操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/cdlineregister/registerRetry"),method:"post",data:t.$http.adornData(a,!1)}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})})},copyPhoneHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.phone});navigator.clipboard.writeText(a).then(function(){t.$message.success("手机号复制成功！")})},filterErrorCode:function(){this.dataList=this.dataList.filter(function(e){return!(null!=e.errMsg&&""!==e.errMsg&&e.errMsg.includes("Code:100"))})},getRegisterStatus:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/getRegisterStatus"),method:"get"}).then(function(t){var a=t.data;a&&0===a.code?e.registerStatusCodes=[{key:0,value:"待处理"}].concat(n()(a.data)):e.$message.error(a.msg)})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/cdregistertask/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,totalAmount:e.dataForm.totalAmount,numberThreads:e.dataForm.numberThreads,numberRegistered:e.dataForm.numberRegistered,numberSuccesses:e.dataForm.numberSuccesses,numberFailures:e.dataForm.numberFailures,registrationStatus:e.dataForm.registrationStatus,deleteFlag:e.dataForm.deleteFlag,createTime:e.dataForm.createTime})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},l={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{width:"70%",title:e.dataForm.taskName,"close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.getDataList()}}},[a("el-form-item",[a("el-select",{attrs:{placeholder:"注册状态",clearable:""},model:{value:e.registerStatus,callback:function(t){e.registerStatus=t},expression:"registerStatus"}},e._l(e.registerStatusCodes,function(e){return a("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),a("el-form-item",[a("el-input",{attrs:{placeholder:"手机号",clearable:""},model:{value:e.dataForm.phone,callback:function(t){e.$set(e.dataForm,"phone",t)},expression:"dataForm.phone"}})],1),e._v(" "),a("el-form-item",[a("el-button",{on:{click:function(t){e.getDataList()}}},[e._v("查询")]),e._v(" "),e.isAuth("ltt:atuser:delete")?a("el-button",{attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.registerRetryHandle()}}},[e._v("错误重试\n          ")]):e._e(),e._v(" "),a("el-button",{attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.copyPhoneHandle()}}},[e._v("复制拉群手机号\n          ")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.filterErrorCode()}}},[e._v("过滤封号\n          ")])],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),e._v(" "),a("el-table-column",{attrs:{prop:"appVersion","header-align":"center",align:"center",label:"app版本号"}}),e._v(" "),a("el-table-column",{attrs:{prop:"countryCode","header-align":"center",align:"center",label:"国家代码"}}),e._v(" "),a("el-table-column",{attrs:{prop:"phone","header-align":"center",align:"center",width:"150px",label:"手机号"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-badge",{staticClass:"item",attrs:{value:t.row.registerCount}},[a("el-button",{attrs:{type:"text"},on:{click:function(a){e.copyPhoneHandle(t.row.phone)}}},[e._v(e._s(t.row.phone))])],1)]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"proxy","header-align":"center",align:"center",label:"注册代理"}}),e._v(" "),a("el-table-column",{attrs:{prop:"smsCode","header-align":"center",align:"center",label:"验证码"}}),e._v(" "),a("el-table-column",{attrs:{prop:"registerStatus","header-align":"center",align:"center",label:"注册状态"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.registerStatusCodes,function(r){return t.row.registerStatus===r.key?a("el-tag",{key:r.value},[e._v("\n              "+e._s(r.value)+"\n            ")]):e._e()})}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"errMsg","header-align":"center",align:"center",label:"失败原因"}}),e._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"时间"}}),e._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.registerRetryHandle(t.row.id)}}},[e._v("错误重试")])]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e()],1)])},staticRenderFns:[]};var o=a("46Yf")(s,l,!1,function(e){a("uYTX")},null,null);t.default=o.exports}});