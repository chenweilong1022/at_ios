webpackJsonp([65],{s65w:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{proxyOptions:[{value:1,label:"lunaproxy"},{value:2,label:"ip2world"},{value:3,label:"静态代理"}],type:2,proxy:"",visible:!1,dataFormConfig:{id:0,paramKey:"",paramValue:"",firefoxBaseUrl:"",firefoxToken:"",firefoxIid:"",firefoxCountry:"",firefoxCountry1:"",proxyUseCount:"",lineBaseHttp:"",lineAb:"",lineAppVersion:"",lineTxtToken:""},countryCodes:[],dataForm:{id:0,taskName:null,totalAmount:"",numberThreads:"50",numberRegistered:"",numberSuccesses:"",numberFailures:"",registrationStatus:"",realMachine:1,deleteFlag:"",sfData:"",countryCode:66,proxyIp:1,fillUp:1,createTime:""},dataRule:{totalAmount:[{required:!0,message:"不能为空",trigger:"blur"}],numberThreads:[{required:!0,message:"不能为空",trigger:"blur"}],numberRegistered:[{required:!0,message:"不能为空",trigger:"blur"}],numberSuccesses:[{required:!0,message:"不能为空",trigger:"blur"}],numberFailures:[{required:!0,message:"不能为空",trigger:"blur"}],registrationStatus:[{required:!0,message:"不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"不能为空",trigger:"blur"}],createTime:[{required:!0,message:"不能为空",trigger:"blur"}]}}},methods:{getProxyEnums:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/proxyStatus"),method:"get"}).then(function(t){var a=t.data;a&&0===a.code?e.proxyOptions=a.data:e.$message.error(a.msg)})},getCountryCodeEnums:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(t){var a=t.data;a&&0===a.code?e.countryCodes=a.data:e.$message.error(a.msg)})},init:function(e){var t=this;this.dataForm.id=e||0,this.visible=!0,this.getCountryCodeEnums(),this.getProxyEnums(),this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/ltt/cdregistertask/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataForm.taskName=a.cdregistertask.taskName,t.dataForm.totalAmount=a.cdregistertask.totalAmount,t.dataForm.numberThreads=a.cdregistertask.numberThreads,t.dataForm.numberRegistered=a.cdregistertask.numberRegistered,t.dataForm.numberSuccesses=a.cdregistertask.numberSuccesses,t.dataForm.numberFailures=a.cdregistertask.numberFailures,t.dataForm.registrationStatus=a.cdregistertask.registrationStatus,t.dataForm.deleteFlag=a.cdregistertask.deleteFlag,t.dataForm.createTime=a.cdregistertask.createTime)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/cdregistertask/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,taskName:e.dataForm.taskName,totalAmount:e.dataForm.totalAmount,numberThreads:e.dataForm.numberThreads,numberRegistered:e.dataForm.numberRegistered,numberSuccesses:e.dataForm.numberSuccesses,numberFailures:e.dataForm.numberFailures,registrationStatus:e.dataForm.registrationStatus,fillUp:e.dataForm.fillUp,realMachine:e.dataForm.realMachine,countryCode:e.dataForm.countryCode,proxyIp:e.dataForm.proxyIp,sfData:e.dataForm.sfData,deleteFlag:e.dataForm.deleteFlag,createTime:e.dataForm.createTime})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},o={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"140px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"任务名称",prop:"taskName"}},[a("el-input",{attrs:{placeholder:"任务名称"},model:{value:e.dataForm.taskName,callback:function(t){e.$set(e.dataForm,"taskName",t)},expression:"dataForm.taskName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"注册数量（成功）",prop:"totalAmount"}},[a("el-input",{attrs:{placeholder:"注册数量（成功）"},model:{value:e.dataForm.totalAmount,callback:function(t){e.$set(e.dataForm,"totalAmount",t)},expression:"dataForm.totalAmount"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"线程数",prop:"numberThreads"}},[a("el-input",{attrs:{placeholder:"线程数"},model:{value:e.dataForm.numberThreads,callback:function(t){e.$set(e.dataForm,"numberThreads",t)},expression:"dataForm.numberThreads"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"是否真机"}},[a("el-radio-group",{model:{value:e.dataForm.realMachine,callback:function(t){e.$set(e.dataForm,"realMachine",t)},expression:"dataForm.realMachine"}},[a("el-radio",{attrs:{label:1}},[e._v("否")]),e._v(" "),a("el-radio",{attrs:{label:2}},[e._v("是")])],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"是否自动补充"}},[a("el-radio-group",{model:{value:e.dataForm.fillUp,callback:function(t){e.$set(e.dataForm,"fillUp",t)},expression:"dataForm.fillUp"}},[a("el-radio",{attrs:{label:0}},[e._v("补充")]),e._v(" "),a("el-radio",{attrs:{label:1}},[e._v("不补充")])],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"注册国家"}},[a("el-select",{attrs:{placeholder:"注册国家",clearable:""},model:{value:e.dataForm.countryCode,callback:function(t){e.$set(e.dataForm,"countryCode",t)},expression:"dataForm.countryCode"}},e._l(e.countryCodes,function(e){return a("el-option",{key:e.key,attrs:{label:e.value2,value:e.key}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"注册代理ip"}},[a("el-select",{attrs:{placeholder:"注册代理ip",clearable:""},model:{value:e.dataForm.proxyIp,callback:function(t){e.$set(e.dataForm,"proxyIp",t)},expression:"dataForm.proxyIp"}},e._l(e.proxyOptions,function(e){return a("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),8101===e.dataForm.countryCode?a("el-form-item",{attrs:{label:"卡数据",prop:"sfData"}},[a("el-input",{attrs:{autosize:{minRows:20,maxRows:20},type:"textarea",placeholder:"卡数据"},model:{value:e.dataForm.sfData,callback:function(t){e.$set(e.dataForm,"sfData",t)},expression:"dataForm.sfData"}})],1):e._e()],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},s=a("46Yf")(r,o,!1,null,null,null);t.default=s.exports}});