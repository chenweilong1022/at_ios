webpackJsonp([61],{a0AD:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={data:function(){return{proxyOptions:[{value:1,label:"lunaproxy"},{value:2,label:"ip2world"},{value:3,label:"静态代理"}],type:2,proxy:"",visible:!1,dataFormConfig:{id:0,paramKey:"",paramValue:"",firefoxBaseUrl:"",firefoxToken:"",firefoxIid:"",firefoxCountry:"",firefoxCountry1:"",proxyUseCount:"",lineBaseHttp:"",lineAb:"",lineAppVersion:"",lineTxtToken:""},countryCodes:[],dataForm:{id:0,totalAmount:"",numberThreads:"50",numberRegistered:"",numberSuccesses:"",numberFailures:"",registrationStatus:"",realMachine:1,deleteFlag:"",sfData:"",countryCode:66,proxyIp:null,fillUp:1,createTime:""},dataRule:{totalAmount:[{required:!0,message:"不能为空",trigger:"blur"}],numberThreads:[{required:!0,message:"不能为空",trigger:"blur"}],numberRegistered:[{required:!0,message:"不能为空",trigger:"blur"}],numberSuccesses:[{required:!0,message:"不能为空",trigger:"blur"}],numberFailures:[{required:!0,message:"不能为空",trigger:"blur"}],registrationStatus:[{required:!0,message:"不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"不能为空",trigger:"blur"}],createTime:[{required:!0,message:"不能为空",trigger:"blur"}]}}},methods:{getProxyEnums:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/proxyStatus"),method:"get"}).then(function(t){var r=t.data;r&&0===r.code?e.proxyOptions=r.data:e.$message.error(r.msg)})},getCountryCodeEnums:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(t){var r=t.data;r&&0===r.code?e.countryCodes=r.data:e.$message.error(r.msg)})},init:function(e){var t=this;this.dataForm.id=e||0,this.visible=!0,this.getProxyEnums(),this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/ltt/cdregistertask/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(e){var r=e.data;r&&0===r.code&&(t.dataForm.totalAmount=r.cdRegisterTask.totalAmount,t.dataForm.numberThreads=r.cdRegisterTask.numberThreads,t.dataForm.numberRegistered=r.cdRegisterTask.numberRegistered,t.dataForm.numberSuccesses=r.cdRegisterTask.numberSuccesses,t.dataForm.numberFailures=r.cdRegisterTask.numberFailures,t.dataForm.registrationStatus=r.cdRegisterTask.registrationStatus,t.dataForm.deleteFlag=r.cdRegisterTask.deleteFlag,t.dataForm.proxyIp=r.cdRegisterTask.proxyIp,t.dataForm.createTime=r.cdRegisterTask.createTime)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/cdregistertask/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,totalAmount:e.dataForm.totalAmount,numberThreads:e.dataForm.numberThreads,numberRegistered:e.dataForm.numberRegistered,numberSuccesses:e.dataForm.numberSuccesses,numberFailures:e.dataForm.numberFailures,registrationStatus:e.dataForm.registrationStatus,fillUp:e.dataForm.fillUp,realMachine:e.dataForm.realMachine,countryCode:e.dataForm.countryCode,proxyIp:e.dataForm.proxyIp,sfData:e.dataForm.sfData,deleteFlag:e.dataForm.deleteFlag,createTime:e.dataForm.createTime})}).then(function(t){var r=t.data;r&&0===r.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(r.msg)})})}}},s={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[r("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"140px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[r("el-form-item",{attrs:{label:"注册代理ip"}},[r("el-select",{attrs:{placeholder:"注册代理ip",clearable:""},model:{value:e.dataForm.proxyIp,callback:function(t){e.$set(e.dataForm,"proxyIp",t)},expression:"dataForm.proxyIp"}},e._l(e.proxyOptions,function(e){return r("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1)],1),e._v(" "),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},o=r("46Yf")(a,s,!1,null,null,null);t.default=o.exports}});