webpackJsonp([3,15,61,62,64],{JpE8:function(t,e,a){(t.exports=a("acE3")(!1)).push([t.i,"\n.item[data-v-6b4b7c18] {\n  margin-top: 10px;\n}\n\n/* 定义绿色背景样式 */\n[data-v-6b4b7c18].green-badge .el-badge__content {\n  background-color: green;\n  color: white;\n}\n\n/* 定义红色背景样式 */\n[data-v-6b4b7c18].red-badge .el-badge__content {\n  background-color: red;\n  color: white;\n}\n",""])},JzVk:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{visible:!1,countryCodes:[],dataForm:{countryCode:null,expireHours:null},dataRule:{}}},methods:{getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.countryCodes=a.data:t.$message.error(a.msg)})},init:function(){this.visible=!0,this.getCountryCodeEnums()},cleanIpByCountryCode:function(){var t=this;console.log(this.dataForm.countryCode),null!==this.dataForm.countryCode?this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/cdlineregister/cleanIpByCountryCode"),method:"get",params:t.$http.adornParams({countryCode:t.dataForm.countryCode})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功，正在清理中，请等待",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})}):this.$message.error("请输入需要清理的ip国家")},cleanInvalidIp:function(){var t=this;null!==this.dataForm.expireHours?this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/cdlineregister/cleanInvalidIp"),method:"get",params:t.$http.adornParams({expireHours:t.dataForm.expireHours})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功，正在清理中，请等待",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})}):this.$message.error("请输入剩余小时")}}},n={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:"清理ip","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"140px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"清理ip对应的国家",prop:"countryCode"}},[a("el-select",{attrs:{placeholder:"清理ip对应的国家",clearable:""},model:{value:t.dataForm.countryCode,callback:function(e){t.$set(t.dataForm,"countryCode",e)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value2,value:t.key}})}))],1),t._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"danger"},on:{click:function(e){t.cleanIpByCountryCode()}}},[t._v("清理ip")])],1),t._v(" "),a("el-form-item",{attrs:{label:"清理黑名单ip剩余小时",prop:"expireHours"}},[a("el-input",{attrs:{placeholder:"清理黑名单ip剩余小时"},model:{value:t.dataForm.expireHours,callback:function(e){t.$set(t.dataForm,"expireHours",e)},expression:"dataForm.expireHours"}})],1),t._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"danger"},on:{click:function(e){t.cleanInvalidIp()}}},[t._v("清理黑名单ip")])],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")])],1)],1)},staticRenderFns:[]},s=a("46Yf")(r,n,!1,null,null,null);e.default=s.exports},KxwK:function(t,e,a){var r=a("JpE8");"string"==typeof r&&(r=[[t.i,r,""]]),r.locals&&(t.exports=r.locals);a("XkoO")("c3be0cb4",r,!0)},a0AD:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{proxyOptions:[{value:1,label:"lunaproxy"},{value:2,label:"ip2world"},{value:3,label:"静态代理"}],type:2,proxy:"",visible:!1,dataFormConfig:{id:0,paramKey:"",paramValue:"",firefoxBaseUrl:"",firefoxToken:"",firefoxIid:"",firefoxCountry:"",firefoxCountry1:"",proxyUseCount:"",lineBaseHttp:"",lineAb:"",lineAppVersion:"",lineTxtToken:""},countryCodes:[],dataForm:{id:0,totalAmount:"",numberThreads:"50",numberRegistered:"",numberSuccesses:"",numberFailures:"",registrationStatus:"",realMachine:1,deleteFlag:"",sfData:"",countryCode:66,proxyIp:null,fillUp:1,createTime:""},dataRule:{totalAmount:[{required:!0,message:"不能为空",trigger:"blur"}],numberThreads:[{required:!0,message:"不能为空",trigger:"blur"}],numberRegistered:[{required:!0,message:"不能为空",trigger:"blur"}],numberSuccesses:[{required:!0,message:"不能为空",trigger:"blur"}],numberFailures:[{required:!0,message:"不能为空",trigger:"blur"}],registrationStatus:[{required:!0,message:"不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"不能为空",trigger:"blur"}],createTime:[{required:!0,message:"不能为空",trigger:"blur"}]}}},methods:{getProxyEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/proxyStatus"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.proxyOptions=a.data:t.$message.error(a.msg)})},getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.countryCodes=a.data:t.$message.error(a.msg)})},init:function(t){var e=this;this.dataForm.id=t||0,this.visible=!0,this.getProxyEnums(),this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.id&&e.$http({url:e.$http.adornUrl("/ltt/cdregistertask/info/"+e.dataForm.id),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.totalAmount=a.cdRegisterTask.totalAmount,e.dataForm.numberThreads=a.cdRegisterTask.numberThreads,e.dataForm.numberRegistered=a.cdRegisterTask.numberRegistered,e.dataForm.numberSuccesses=a.cdRegisterTask.numberSuccesses,e.dataForm.numberFailures=a.cdRegisterTask.numberFailures,e.dataForm.registrationStatus=a.cdRegisterTask.registrationStatus,e.dataForm.deleteFlag=a.cdRegisterTask.deleteFlag,e.dataForm.proxyIp=a.cdRegisterTask.proxyIp,e.dataForm.createTime=a.cdRegisterTask.createTime)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/cdregistertask/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,proxyIp:t.dataForm.proxyIp})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},n={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"140px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"注册代理ip"}},[a("el-select",{attrs:{placeholder:"注册代理ip",clearable:""},model:{value:t.dataForm.proxyIp,callback:function(e){t.$set(t.dataForm,"proxyIp",e)},expression:"dataForm.proxyIp"}},t._l(t.proxyOptions,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},s=a("46Yf")(r,n,!1,null,null,null);e.default=s.exports},s65w:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{proxyOptions:[{value:1,label:"lunaproxy"},{value:2,label:"ip2world"},{value:3,label:"静态代理"}],type:2,proxy:"",visible:!1,dataFormConfig:{id:0,paramKey:"",paramValue:"",firefoxBaseUrl:"",firefoxToken:"",firefoxIid:"",firefoxCountry:"",firefoxCountry1:"",proxyUseCount:"",lineBaseHttp:"",lineAb:"",lineAppVersion:"",lineTxtToken:""},countryCodes:[],dataForm:{id:0,taskName:null,totalAmount:"",numberThreads:"50",numberRegistered:"",numberSuccesses:"",numberFailures:"",registrationStatus:"",realMachine:1,deleteFlag:"",sfData:"",countryCode:66,proxyIp:null,fillUp:1,createTime:""},dataRule:{totalAmount:[{required:!0,message:"不能为空",trigger:"blur"}],numberThreads:[{required:!0,message:"不能为空",trigger:"blur"}],numberRegistered:[{required:!0,message:"不能为空",trigger:"blur"}],numberSuccesses:[{required:!0,message:"不能为空",trigger:"blur"}],numberFailures:[{required:!0,message:"不能为空",trigger:"blur"}],registrationStatus:[{required:!0,message:"不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"不能为空",trigger:"blur"}],createTime:[{required:!0,message:"不能为空",trigger:"blur"}]}}},methods:{getProxyEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/proxyStatus"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.proxyOptions=a.data:t.$message.error(a.msg)})},getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.countryCodes=a.data:t.$message.error(a.msg)})},init:function(t){var e=this;this.dataForm.id=t||0,this.visible=!0,this.getCountryCodeEnums(),this.getProxyEnums(),this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.id&&e.$http({url:e.$http.adornUrl("/ltt/cdregistertask/info/"+e.dataForm.id),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.taskName=a.cdregistertask.taskName,e.dataForm.totalAmount=a.cdregistertask.totalAmount,e.dataForm.numberThreads=a.cdregistertask.numberThreads,e.dataForm.numberRegistered=a.cdregistertask.numberRegistered,e.dataForm.numberSuccesses=a.cdregistertask.numberSuccesses,e.dataForm.numberFailures=a.cdregistertask.numberFailures,e.dataForm.registrationStatus=a.cdregistertask.registrationStatus,e.dataForm.deleteFlag=a.cdregistertask.deleteFlag,e.dataForm.createTime=a.cdregistertask.createTime)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/cdregistertask/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,taskName:t.dataForm.taskName,totalAmount:t.dataForm.totalAmount,numberThreads:t.dataForm.numberThreads,numberRegistered:t.dataForm.numberRegistered,numberSuccesses:t.dataForm.numberSuccesses,numberFailures:t.dataForm.numberFailures,registrationStatus:t.dataForm.registrationStatus,fillUp:t.dataForm.fillUp,realMachine:t.dataForm.realMachine,countryCode:t.dataForm.countryCode,proxyIp:t.dataForm.proxyIp,sfData:t.dataForm.sfData,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},n={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"140px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"任务名称",prop:"taskName"}},[a("el-input",{attrs:{placeholder:"任务名称"},model:{value:t.dataForm.taskName,callback:function(e){t.$set(t.dataForm,"taskName",e)},expression:"dataForm.taskName"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"注册数量（成功）",prop:"totalAmount"}},[a("el-input",{attrs:{placeholder:"注册数量（成功）"},model:{value:t.dataForm.totalAmount,callback:function(e){t.$set(t.dataForm,"totalAmount",e)},expression:"dataForm.totalAmount"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"线程数",prop:"numberThreads"}},[a("el-input",{attrs:{placeholder:"线程数"},model:{value:t.dataForm.numberThreads,callback:function(e){t.$set(t.dataForm,"numberThreads",e)},expression:"dataForm.numberThreads"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"是否真机"}},[a("el-radio-group",{model:{value:t.dataForm.realMachine,callback:function(e){t.$set(t.dataForm,"realMachine",e)},expression:"dataForm.realMachine"}},[a("el-radio",{attrs:{label:1}},[t._v("否")]),t._v(" "),a("el-radio",{attrs:{label:2}},[t._v("是")])],1)],1),t._v(" "),a("el-form-item",{attrs:{label:"是否自动补充"}},[a("el-radio-group",{model:{value:t.dataForm.fillUp,callback:function(e){t.$set(t.dataForm,"fillUp",e)},expression:"dataForm.fillUp"}},[a("el-radio",{attrs:{label:0}},[t._v("补充")]),t._v(" "),a("el-radio",{attrs:{label:1}},[t._v("不补充")])],1)],1),t._v(" "),a("el-form-item",{attrs:{label:"注册国家"}},[a("el-select",{attrs:{placeholder:"注册国家",clearable:""},model:{value:t.dataForm.countryCode,callback:function(e){t.$set(t.dataForm,"countryCode",e)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value2,value:t.key}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"注册代理ip"}},[a("el-select",{attrs:{placeholder:"注册代理ip",clearable:""},model:{value:t.dataForm.proxyIp,callback:function(e){t.$set(t.dataForm,"proxyIp",e)},expression:"dataForm.proxyIp"}},t._l(t.proxyOptions,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),8101===t.dataForm.countryCode?a("el-form-item",{attrs:{label:"卡数据",prop:"sfData"}},[a("el-input",{attrs:{autosize:{minRows:20,maxRows:20},type:"textarea",placeholder:"卡数据"},model:{value:t.dataForm.sfData,callback:function(e){t.$set(t.dataForm,"sfData",e)},expression:"dataForm.sfData"}})],1):t._e()],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},s=a("46Yf")(r,n,!1,null,null,null);e.default=s.exports},vABA:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=a("s65w"),n=a("a0AD"),s=a("JzVk"),i=a("y49+"),o={data:function(){return{dataForm:{countryCode:null,registrationStatus:null,taskName:null},countryCodes:[],dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],registrationStatusCodes:[{key:1,value:"新注册"},{key:2,value:"注册中"},{key:3,value:"暂停注册"},{key:7,value:"注册完成"},{key:9,value:"真机注册任务"}],addOrUpdateVisible:!1,cleanIpVisible:!1,setProxyVisible:!1,cdregistertaskAccountListVisible:!1}},components:{AddOrUpdate:r.default,CleanIp:s.default,SetProxy:n.default,CdregistertaskAccountList:i.default},activated:function(){this.getDataList(),this.getCountryCodeEnums()},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/cdregistertask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,countryCode:this.dataForm.countryCode,registrationStatus:this.dataForm.registrationStatus,taskName:this.dataForm.taskName})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.countryCodes=a.data:t.$message.error(a.msg)})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var e=this;t?(this.setProxyVisible=!0,this.$nextTick(function(){e.$refs.setProxy.init(t)})):(this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(t)}))},cleanIpHandle:function(){var t=this;this.cleanIpVisible=!0,this.$nextTick(function(){t.$refs.CleanIp.init()})},cdregistertaskAccountListHandle:function(t,e,a){var r=this;this.cdregistertaskAccountListVisible=!0,this.$nextTick(function(){r.$refs.cdregistertaskAccountList.init(t,e,a)})},deleteHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/cdregistertask/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},deleteRegisterTaskHandle:function(t){var e=this;this.$confirm("确定删除操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/cdregistertask/deleteRegisterTask"),method:"get",params:e.$http.adornParams({taskId:t})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},stopRegisterTask:function(t){var e=this;this.$confirm("确定暂停注册任务?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/cdregistertask/stopRegisterTask"),method:"post",params:e.$http.adornParams({taskId:t})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})}}},l={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",{attrs:{label:"任务名称"}},[a("el-input",{attrs:{placeholder:"任务名称",clearable:""},model:{value:t.dataForm.taskName,callback:function(e){t.$set(t.dataForm,"taskName",e)},expression:"dataForm.taskName"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"注册国家"}},[a("el-select",{attrs:{placeholder:"注册国家",clearable:""},model:{value:t.dataForm.countryCode,callback:function(e){t.$set(t.dataForm,"countryCode",e)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"注册状态",prop:"registrationStatus"}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择注册状态"},model:{value:t.dataForm.registrationStatus,callback:function(e){t.$set(t.dataForm,"registrationStatus",e)},expression:"dataForm.registrationStatus"}},t._l(t.registrationStatusCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",[a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")]),t._v(" "),t.isAuth("ltt:cdregistertask:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.addOrUpdateHandle()}}},[t._v("新增注册")]):t._e(),t._v(" "),t.isAuth("ltt:cdregistertask:save")?a("el-button",{attrs:{type:"danger"},on:{click:function(e){t.cleanIpHandle()}}},[t._v("清理ip")]):t._e(),t._v(" "),t.isAuth("ltt:cdregistertask:save")?a("el-button",{attrs:{type:"success"},on:{click:function(e){t.cdregistertaskAccountListHandle(null,"山谷",81)}}},[t._v("山谷")]):t._e(),t._v(" "),t.isAuth("ltt:cdregistertask:save")?a("el-button",{attrs:{type:"success"},on:{click:function(e){t.cdregistertaskAccountListHandle(null,"子弹",8101)}}},[t._v("子弹")]):t._e()],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),a("el-table-column",{attrs:{prop:"id","header-align":"center",align:"center",label:"任务id"}}),t._v(" "),a("el-table-column",{attrs:{prop:"taskName","header-align":"center",align:"center",label:"任务名称"}}),t._v(" "),a("el-table-column",{attrs:{prop:"countryCode","header-align":"center",align:"center",label:"国家code"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(t.countryCodes,function(r){return e.row.countryCode===r.key?a("el-tag",{key:r.key},[t._v("\n            "+t._s(r.value)+"\n          ")]):t._e()})}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"totalAmount","header-align":"center",align:"center",label:"注册数据"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("div",[t._v("总数量："+t._s(e.row.totalAmount))]),t._v(" "),a("div",[t._v("成功数量："+t._s(e.row.numberSuccesses))])]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"schedule","header-align":"center",align:"center",label:"进度"},scopedSlots:t._u([{key:"default",fn:function(t){return[a("el-progress",{attrs:{"stroke-width":10,type:"circle",percentage:t.row.scheduleFloat}})]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"registrationStatusStr","header-align":"center",align:"center",label:"注册状态"},scopedSlots:t._u([{key:"default",fn:function(e){return[7===e.row.registrationStatus?a("el-button",{attrs:{type:"success",plain:""}},[t._v(t._s(e.row.registrationStatusStr))]):t._e(),t._v(" "),3===e.row.registrationStatus?a("el-button",{attrs:{type:"info",plain:""}},[t._v(t._s(e.row.registrationStatusStr))]):t._e(),t._v(" "),1===e.row.registrationStatus?a("el-button",{attrs:{type:"warning",plain:""}},[t._v(t._s(e.row.registrationStatusStr))]):t._e(),t._v(" "),2===e.row.registrationStatus?a("el-button",{attrs:{type:"warning",plain:""}},[t._v(t._s(e.row.registrationStatusStr))]):t._e(),t._v(" "),6===e.row.registrationStatus?a("el-button",{attrs:{type:"warning",plain:""}},[t._v(t._s(e.row.registrationStatusStr))]):t._e(),t._v(" "),9===e.row.registrationStatus?a("el-button",{attrs:{type:"warning",plain:""}},[t._v(t._s(e.row.registrationStatusStr))]):t._e()]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"numberThreads","header-align":"center",align:"center",label:"线程数"}}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),t._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.addOrUpdateHandle(e.row.id)}}},[t._v("代理设置")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.cdregistertaskAccountListHandle(e.row.id,e.row.taskName,e.row.countryCode)}}},[t._v("注册详情")]),t._v(" "),9===e.row.registrationStatus?a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.deleteHandle(e.row.id)}}},[t._v("停止真机任务")]):t._e(),t._v(" "),3!=e.row.registrationStatus&&7!=e.row.registrationStatus&&9!=e.row.registrationStatus?a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.stopRegisterTask(e.row.id)}}},[t._v("暂停任务")]):t._e(),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.deleteRegisterTaskHandle(e.row.id)}}},[t._v("删除注册任务")])]}}])})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.cleanIpVisible?a("clean-ip",{ref:"CleanIp",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.setProxyVisible?a("set-proxy",{ref:"setProxy",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.cdregistertaskAccountListVisible?a("cdregistertask-account-list",{ref:"cdregistertaskAccountList",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},d=a("46Yf")(o,l,!1,null,null,null);e.default=d.exports},"y49+":function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=a("IHPB"),n=a.n(r),s=a("QF3l"),i={data:function(){return{registerStatus:null,registerStatusCodes:[],dataList:[],summary:null,pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,visible:!1,dataForm:{id:0,taskName:null,countryCode:null,timeKey:null},dataRule:{totalAmount:[{required:!0,message:"不能为空",trigger:"blur"}],numberThreads:[{required:!0,message:"不能为空",trigger:"blur"}],numberRegistered:[{required:!0,message:"不能为空",trigger:"blur"}],numberSuccesses:[{required:!0,message:"不能为空",trigger:"blur"}],numberFailures:[{required:!0,message:"不能为空",trigger:"blur"}],registrationStatus:[{required:!0,message:"不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"不能为空",trigger:"blur"}],createTime:[{required:!0,message:"不能为空",trigger:"blur"}]}}},components:{AddOrUpdate:a("5XyS").default,CdlineRegisterList:s.default},methods:{getDataList:function(){var t=this;this.dataListLoading=!0;var e=null,a=null;null!=this.dataForm.timeKey&&this.dataForm.timeKey.length>=2&&(e=this.dataForm.timeKey[0],a=this.dataForm.timeKey[1]);var r=this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,registerStatus:this.registerStatus,tasksId:this.dataForm.id,phone:this.dataForm.phone,countryCode:this.countryCode,createStartTime:e,createEndTime:a});this.$http({url:this.$http.adornUrl("/ltt/cdlineregister/listByTaskId"),method:"get",params:r}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1}),this.$http({url:this.$http.adornUrl("/ltt/cdlineregister/listSummary"),method:"get",params:r}).then(function(e){var a=e.data;a&&0===a.code?t.summary=a.summary:t.summary=null})},init:function(t,e,a){this.dataForm.id=t||null,e=e||"详情",null!=t&&(e=t+"-"+e),this.dataForm.taskName=e,this.countryCode=a,this.visible=!0,this.getDefaultData(),this.getRegisterStatus(),this.getDataList()},getDefaultData:function(){var t=new Date,e=new Date(t);e.setDate(t.getDate()-3),this.dataForm.timeKey=[this.formatDateKey(e),this.formatDateKey(t)]},formatDateKey:function(t){return t.getFullYear()+"-"+("0"+(t.getMonth()+1)).slice(-2)+"-"+("0"+t.getDate()).slice(-2)},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},registerRetryHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定重新注册操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/cdlineregister/registerRetry"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},badgeClass:function(t){return null===t?"green-badge":!0===t?"green-badge":!1===t?"red-badge":void 0},copyPhoneHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.phone});navigator.clipboard.writeText(a).then(function(){e.$message.success("手机号复制成功！")})},filterErrorCode:function(){this.dataList=this.dataList.filter(function(t){return!(null!=t.errMsg&&""!==t.errMsg&&t.errMsg.includes("Code:100"))})},getRegisterStatus:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getRegisterStatus"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.registerStatusCodes=[{key:0,value:"待处理"}].concat(n()(a.data)):t.$message.error(a.msg)})}}},o={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{width:"70%",title:t.dataForm.taskName,"close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",[a("el-select",{attrs:{placeholder:"注册状态",clearable:""},model:{value:t.registerStatus,callback:function(e){t.registerStatus=e},expression:"registerStatus"}},t._l(t.registerStatusCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",[a("el-input",{attrs:{placeholder:"手机号",clearable:""},model:{value:t.dataForm.phone,callback:function(e){t.$set(t.dataForm,"phone",e)},expression:"dataForm.phone"}})],1),t._v(" "),a("el-form-item",[a("el-date-picker",{attrs:{type:"daterange",format:"yyyy-MM-dd","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:t.dataForm.timeKey,callback:function(e){t.$set(t.dataForm,"timeKey",e)},expression:"dataForm.timeKey"}})],1),t._v(" "),a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")]),t._v(" "),a("div",[t.isAuth("ltt:atuser:delete")?a("el-button",{attrs:{type:"primary",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.registerRetryHandle()}}},[t._v("错误重试\n          ")]):t._e(),t._v(" "),a("el-button",{attrs:{type:"primary",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.copyPhoneHandle()}}},[t._v("复制拉群手机号\n          ")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.filterErrorCode()}}},[t._v("过滤封号\n          ")])],1)],1),t._v(" "),a("div",{staticStyle:{"font-size":"25px","font-weight":"bold","margin-bottom":"20px","margin-top":"20px"}},[t._v("\n        注册中:"),a("div",{staticStyle:{color:"#17B3A3",display:"inline","margin-right":"10px"}},[t._v(t._s(t.summary.waitRegisterCount))]),t._v("\n        注册成功:"),a("div",{staticStyle:{color:"#17B3A3",display:"inline"}},[t._v(t._s(t.summary.successRegisterCount))])]),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),a("el-table-column",{attrs:{prop:"appVersion","header-align":"center",align:"center",label:"app版本号"}}),t._v(" "),a("el-table-column",{attrs:{prop:"countryCode","header-align":"center",align:"center",label:"国家代码"}}),t._v(" "),a("el-table-column",{attrs:{prop:"phone","header-align":"center",align:"center",width:"150px",label:"手机号"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-badge",{staticClass:"item",class:t.badgeClass(e.row.phoneState),attrs:{value:e.row.registerCount}},[a("el-button",{attrs:{type:"text"},on:{click:function(a){t.copyPhoneHandle(e.row.phone)}}},[t._v(t._s(e.row.phone))])],1)]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"proxy","header-align":"center",align:"center",label:"注册代理"}}),t._v(" "),a("el-table-column",{attrs:{prop:"smsCode","header-align":"center",align:"center",label:"验证码"}}),t._v(" "),a("el-table-column",{attrs:{prop:"registerStatus","header-align":"center",align:"center",label:"注册状态"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(t.registerStatusCodes,function(r){return e.row.registerStatus===r.key?a("el-tag",{key:r.value},[t._v("\n              "+t._s(r.value)+"\n            ")]):t._e()})}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"errMsg","header-align":"center",align:"center",label:"失败原因"}}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"时间"}}),t._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.registerRetryHandle(e.row.id)}}},[t._v("错误重试")])]}}])})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e()],1)])},staticRenderFns:[]};var l=a("46Yf")(i,o,!1,function(t){a("KxwK")},"data-v-6b4b7c18",null);e.default=l.exports}});