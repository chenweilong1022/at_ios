webpackJsonp([52],{"/caQ":function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var o={data:function(){return{proxy:null,type:2,platform:1,visible:!1,projectDataList:[],proxyOptions:[{value:1,label:"lunaproxy"},{value:2,label:"ip2world"},{value:3,label:"静态代理"}],options:[{value:1,label:"云存储"},{value:2,label:"项目配置"}],dataForm:{id:0,paramKey:"",paramValue:"",firefoxBaseUrl:"",firefoxToken:"",firefoxIid:"",firefoxCountry:"",firefoxCountry1:"",proxyUseCount:"",lineBaseHttp:"",lineAb:"",lineAppVersion:"",lineTxtToken:""},dataRule:{paramKey:[{required:!0,message:"参数名不能为空",trigger:"blur"}],paramValue:[{required:!0,message:"参数值不能为空",trigger:"blur"}]}}},methods:{init:function(e){var a=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){a.$refs.dataForm.resetFields(),a.dataForm.id&&a.$http({url:a.$http.adornUrl("/sys/config/info/"+a.dataForm.id),method:"get",params:a.$http.adornParams()}).then(function(e){var t=e.data;t&&0===t.code&&(a.dataForm.paramKey=t.config.paramKey,a.dataForm.paramValue=t.config.paramValue,a.dataForm.remark=t.config.remark,a.dataForm.lineBaseHttp=t.config.lineBaseHttp,a.dataForm.lineAb=t.config.lineAb,a.dataForm.lineAppVersion=t.config.lineAppVersion,a.dataForm.lineTxtToken=t.config.lineTxtToken,a.dataForm.firefoxBaseUrl=t.config.firefoxBaseUrl,a.dataForm.firefoxToken=t.config.firefoxToken,a.dataForm.firefoxIid=t.config.firefoxIid,a.dataForm.firefoxCountry=t.config.firefoxCountry,a.dataForm.firefoxCountry1=t.config.firefoxCountry1,a.dataForm.proxyUseCount=t.config.proxyUseCount,a.proxy=t.config.proxy)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(a){a&&e.$http({url:e.$http.adornUrl("/sys/config/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,paramKey:e.dataForm.paramKey,paramValue:e.dataForm.paramValue,lineBaseHttp:e.dataForm.lineBaseHttp,lineAb:e.dataForm.lineAb,lineAppVersion:e.dataForm.lineAppVersion,lineTxtToken:e.dataForm.lineTxtToken,firefoxBaseUrl:e.dataForm.firefoxBaseUrl,firefoxToken:e.dataForm.firefoxToken,firefoxIid:e.dataForm.firefoxIid,firefoxCountry:e.dataForm.firefoxCountry,firefoxCountry1:e.dataForm.firefoxCountry1,proxyUseCount:e.dataForm.proxyUseCount,proxy:e.proxy,type:e.type,remark:e.dataForm.remark})}).then(function(a){var t=a.data;t&&0===t.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(t.msg)})})}}},r={render:function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(a){e.visible=a}}},[t("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(a){if(!("button"in a)&&e._k(a.keyCode,"enter",13,a.key))return null;e.dataFormSubmit()}}},[t("el-form-item",{attrs:{label:"参数名"}},[t("el-select",{attrs:{placeholder:"类型",clearable:""},model:{value:e.type,callback:function(a){e.type=a},expression:"type"}},e._l(e.options,function(e){return t("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),t("el-form-item",{attrs:{label:"代理"}},[t("el-select",{attrs:{placeholder:"代理",clearable:""},model:{value:e.proxy,callback:function(a){e.proxy=a},expression:"proxy"}},e._l(e.proxyOptions,function(e){return t("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"linehttp",prop:"lineBaseHttp"}},[t("el-input",{attrs:{placeholder:"linehttp"},model:{value:e.dataForm.lineBaseHttp,callback:function(a){e.$set(e.dataForm,"lineBaseHttp",a)},expression:"dataForm.lineBaseHttp"}})],1):e._e(),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"lineAb",prop:"lineAb"}},[t("el-input",{attrs:{placeholder:"lineAb"},model:{value:e.dataForm.lineAb,callback:function(a){e.$set(e.dataForm,"lineAb",a)},expression:"dataForm.lineAb"}})],1):e._e(),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"lineAppVersion",prop:"lineAppVersion"}},[t("el-input",{attrs:{placeholder:"lineAppVersion"},model:{value:e.dataForm.lineAppVersion,callback:function(a){e.$set(e.dataForm,"lineAppVersion",a)},expression:"dataForm.lineAppVersion"}})],1):e._e(),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"lineTxtToken",prop:"lineTxtToken"}},[t("el-input",{attrs:{placeholder:"lineTxtToken"},model:{value:e.dataForm.lineTxtToken,callback:function(a){e.$set(e.dataForm,"lineTxtToken",a)},expression:"dataForm.lineTxtToken"}})],1):e._e(),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"火狐狸接口",prop:"firefoxBaseUrl"}},[t("el-input",{attrs:{placeholder:"火狐狸接口"},model:{value:e.dataForm.firefoxBaseUrl,callback:function(a){e.$set(e.dataForm,"firefoxBaseUrl",a)},expression:"dataForm.firefoxBaseUrl"}})],1):e._e(),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"火狐狸token",prop:"firefoxToken"}},[t("el-input",{attrs:{placeholder:"火狐狸token"},model:{value:e.dataForm.firefoxToken,callback:function(a){e.$set(e.dataForm,"firefoxToken",a)},expression:"dataForm.firefoxToken"}})],1):e._e(),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"火狐狸项目id配置",prop:"firefoxIid"}},[t("el-input",{attrs:{placeholder:"火狐狸项目id配置"},model:{value:e.dataForm.firefoxIid,callback:function(a){e.$set(e.dataForm,"firefoxIid",a)},expression:"dataForm.firefoxIid"}})],1):e._e(),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"火狐狸国家配置",prop:"firefoxCountry"}},[t("el-input",{attrs:{placeholder:"火狐狸国家配置"},model:{value:e.dataForm.firefoxCountry,callback:function(a){e.$set(e.dataForm,"firefoxCountry",a)},expression:"dataForm.firefoxCountry"}})],1):e._e(),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"LINE国家配置",prop:"firefoxCountry1"}},[t("el-input",{attrs:{placeholder:"LINE国家配置"},model:{value:e.dataForm.firefoxCountry1,callback:function(a){e.$set(e.dataForm,"firefoxCountry1",a)},expression:"dataForm.firefoxCountry1"}})],1):e._e(),e._v(" "),2===e.type?t("el-form-item",{attrs:{label:"静态代理使用次数",prop:"proxyUseCount"}},[t("el-input",{attrs:{placeholder:"静态代理使用次数"},model:{value:e.dataForm.proxyUseCount,callback:function(a){e.$set(e.dataForm,"proxyUseCount",a)},expression:"dataForm.proxyUseCount"}})],1):e._e()],1),e._v(" "),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(a){e.visible=!1}}},[e._v("取消")]),e._v(" "),t("el-button",{attrs:{type:"primary"},on:{click:function(a){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},l=t("46Yf")(o,r,!1,null,null,null);a.default=l.exports}});