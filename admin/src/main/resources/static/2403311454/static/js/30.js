webpackJsonp([30,52],{"/caQ":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o={data:function(){return{proxy:null,type:2,platform:1,visible:!1,projectDataList:[],proxyOptions:[{value:1,label:"lunaproxy"},{value:2,label:"ip2world"},{value:3,label:"静态代理"}],options:[{value:1,label:"云存储"},{value:2,label:"项目配置"}],dataForm:{id:0,paramKey:"",paramValue:"",firefoxBaseUrl:"",firefoxToken:"",firefoxIid:"",firefoxCountry:"",firefoxCountry1:"",proxyUseCount:"",lineBaseHttp:"",lineAb:"",lineAppVersion:"",lineTxtToken:""},dataRule:{paramKey:[{required:!0,message:"参数名不能为空",trigger:"blur"}],paramValue:[{required:!0,message:"参数值不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/sys/config/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataForm.paramKey=a.config.paramKey,t.dataForm.paramValue=a.config.paramValue,t.dataForm.remark=a.config.remark,t.dataForm.lineBaseHttp=a.config.lineBaseHttp,t.dataForm.lineAb=a.config.lineAb,t.dataForm.lineAppVersion=a.config.lineAppVersion,t.dataForm.lineTxtToken=a.config.lineTxtToken,t.dataForm.firefoxBaseUrl=a.config.firefoxBaseUrl,t.dataForm.firefoxToken=a.config.firefoxToken,t.dataForm.firefoxIid=a.config.firefoxIid,t.dataForm.firefoxCountry=a.config.firefoxCountry,t.dataForm.firefoxCountry1=a.config.firefoxCountry1,t.dataForm.proxyUseCount=a.config.proxyUseCount,t.proxy=a.config.proxy)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/sys/config/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,paramKey:e.dataForm.paramKey,paramValue:e.dataForm.paramValue,lineBaseHttp:e.dataForm.lineBaseHttp,lineAb:e.dataForm.lineAb,lineAppVersion:e.dataForm.lineAppVersion,lineTxtToken:e.dataForm.lineTxtToken,firefoxBaseUrl:e.dataForm.firefoxBaseUrl,firefoxToken:e.dataForm.firefoxToken,firefoxIid:e.dataForm.firefoxIid,firefoxCountry:e.dataForm.firefoxCountry,firefoxCountry1:e.dataForm.firefoxCountry1,proxyUseCount:e.dataForm.proxyUseCount,proxy:e.proxy,type:e.type,remark:e.dataForm.remark})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},r={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"参数名"}},[a("el-select",{attrs:{placeholder:"类型",clearable:""},model:{value:e.type,callback:function(t){e.type=t},expression:"type"}},e._l(e.options,function(e){return a("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"代理"}},[a("el-select",{attrs:{placeholder:"代理",clearable:""},model:{value:e.proxy,callback:function(t){e.proxy=t},expression:"proxy"}},e._l(e.proxyOptions,function(e){return a("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"linehttp",prop:"lineBaseHttp"}},[a("el-input",{attrs:{placeholder:"linehttp"},model:{value:e.dataForm.lineBaseHttp,callback:function(t){e.$set(e.dataForm,"lineBaseHttp",t)},expression:"dataForm.lineBaseHttp"}})],1):e._e(),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"lineAb",prop:"lineAb"}},[a("el-input",{attrs:{placeholder:"lineAb"},model:{value:e.dataForm.lineAb,callback:function(t){e.$set(e.dataForm,"lineAb",t)},expression:"dataForm.lineAb"}})],1):e._e(),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"lineAppVersion",prop:"lineAppVersion"}},[a("el-input",{attrs:{placeholder:"lineAppVersion"},model:{value:e.dataForm.lineAppVersion,callback:function(t){e.$set(e.dataForm,"lineAppVersion",t)},expression:"dataForm.lineAppVersion"}})],1):e._e(),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"lineTxtToken",prop:"lineTxtToken"}},[a("el-input",{attrs:{placeholder:"lineTxtToken"},model:{value:e.dataForm.lineTxtToken,callback:function(t){e.$set(e.dataForm,"lineTxtToken",t)},expression:"dataForm.lineTxtToken"}})],1):e._e(),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"火狐狸接口",prop:"firefoxBaseUrl"}},[a("el-input",{attrs:{placeholder:"火狐狸接口"},model:{value:e.dataForm.firefoxBaseUrl,callback:function(t){e.$set(e.dataForm,"firefoxBaseUrl",t)},expression:"dataForm.firefoxBaseUrl"}})],1):e._e(),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"火狐狸token",prop:"firefoxToken"}},[a("el-input",{attrs:{placeholder:"火狐狸token"},model:{value:e.dataForm.firefoxToken,callback:function(t){e.$set(e.dataForm,"firefoxToken",t)},expression:"dataForm.firefoxToken"}})],1):e._e(),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"火狐狸项目id配置",prop:"firefoxIid"}},[a("el-input",{attrs:{placeholder:"火狐狸项目id配置"},model:{value:e.dataForm.firefoxIid,callback:function(t){e.$set(e.dataForm,"firefoxIid",t)},expression:"dataForm.firefoxIid"}})],1):e._e(),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"火狐狸国家配置",prop:"firefoxCountry"}},[a("el-input",{attrs:{placeholder:"火狐狸国家配置"},model:{value:e.dataForm.firefoxCountry,callback:function(t){e.$set(e.dataForm,"firefoxCountry",t)},expression:"dataForm.firefoxCountry"}})],1):e._e(),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"LINE国家配置",prop:"firefoxCountry1"}},[a("el-input",{attrs:{placeholder:"LINE国家配置"},model:{value:e.dataForm.firefoxCountry1,callback:function(t){e.$set(e.dataForm,"firefoxCountry1",t)},expression:"dataForm.firefoxCountry1"}})],1):e._e(),e._v(" "),2===e.type?a("el-form-item",{attrs:{label:"静态代理使用次数",prop:"proxyUseCount"}},[a("el-input",{attrs:{placeholder:"静态代理使用次数"},model:{value:e.dataForm.proxyUseCount,callback:function(t){e.$set(e.dataForm,"proxyUseCount",t)},expression:"dataForm.proxyUseCount"}})],1):e._e()],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},n=a("46Yf")(o,r,!1,null,null,null);t.default=n.exports},MKmw:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o={data:function(){return{dataForm:{paramKey:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:a("/caQ").default},activated:function(){this.getDataList()},methods:{getDataList:function(){var e=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/sys/config/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,paramKey:this.dataForm.paramKey})}).then(function(t){var a=t.data;a&&0===a.code?(e.dataList=a.page.list,e.totalPage=a.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},addOrUpdateHandle:function(e){var t=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){t.$refs.addOrUpdate.init(e)})},deleteHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(e?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/sys/config/delete"),method:"post",data:t.$http.adornData(a,!1)}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})}).catch(function(){})}}},r={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.getDataList()}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:e.dataForm.paramKey,callback:function(t){e.$set(e.dataForm,"paramKey",t)},expression:"dataForm.paramKey"}})],1),e._v(" "),a("el-form-item",[a("el-button",{on:{click:function(t){e.getDataList()}}},[e._v("查询")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.addOrUpdateHandle()}}},[e._v("新增")]),e._v(" "),a("el-button",{attrs:{type:"danger",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.deleteHandle()}}},[e._v("批量删除")])],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),e._v(" "),a("el-table-column",{attrs:{prop:"id","header-align":"center",align:"center",width:"80",label:"ID"}}),e._v(" "),a("el-table-column",{attrs:{prop:"paramKey","header-align":"center",align:"center",label:"参数名"}}),e._v(" "),a("el-table-column",{attrs:{prop:"paramValue","header-align":"center",align:"center",label:"参数值"}}),e._v(" "),a("el-table-column",{attrs:{prop:"remark","header-align":"center",align:"center",label:"备注"}}),e._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.addOrUpdateHandle(t.row.id)}}},[e._v("修改")]),e._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.deleteHandle(t.row.id)}}},[e._v("删除")])]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]},n=a("46Yf")(o,r,!1,null,null,null);t.default=n.exports}});