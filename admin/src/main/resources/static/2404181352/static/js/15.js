webpackJsonp([15,69,70],{"6XLs":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:"设置设备名称","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"设备名称",prop:"deviceName"}},[a("el-input",{attrs:{placeholder:"设备名称"},model:{value:e.dataForm.deviceName,callback:function(t){e.$set(e.dataForm,"deviceName",t)},expression:"dataForm.deviceName"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},i=a("46Yf")({data:function(){return{visible:!1,dataForm:{deviceId:null,deviceName:null},dataRule:{deviceName:[{required:!0,message:"设备名称不能为空",trigger:"blur"}]}}},methods:{init:function(e){this.visible=!0,this.dataForm.deviceId=e},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atusertokenios/updateDeviceName"),method:"post",data:e.$http.adornData({deviceId:e.dataForm.deviceId,deviceName:e.dataForm.deviceName})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})},queryUserGroupBySearchWord:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/atusergroup/queryByFuzzyName?searchWord="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.userGroupOptions=a.groupList)})}}},n,!1,null,null,null);t.default=i.exports},NVTx:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n={data:function(){return{visibleFlag:!1,dataForm:{reductionFlag:null,sysUserId:null,country:null},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,reductionFlagCodes:[{key:1,value:"未还原"},{key:0,value:"已还原"}],sysUserAccountOptions:[],countryCodes:[]}},components:{AddOrUpdate:a("rHDt").default},activated:function(){this.getDataList(),this.getCountryCodes()},methods:{init:function(e,t){this.visibleFlag=!0,this.dataForm.deviceId=e,this.dataForm.deviceName=t,this.getDataList(e),this.getCountryCodes(),this.queryBySearchWord()},getDataList:function(e){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atusertokenios/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,deviceId:this.dataForm.deviceId,reductionFlag:this.dataForm.reductionFlag,sysUserId:this.dataForm.sysUserId,country:this.dataForm.country})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},addOrUpdateHandle:function(e){var t=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){t.$refs.addOrUpdate.init(e)})},backupHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(e?"备份":"批量备份")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/atusertokenios/backUp"),method:"post",data:t.$http.adornData({ids:a})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})})},restoreDataHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(e?"还原":"批量还原")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/atusertokenios/taskIosFind"),method:"post",data:t.$http.adornData(a,!1)}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})})},queryBySearchWord:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/sys/user/queryBySearchWord?searchWord="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.sysUserAccountOptions=a.userList)})},getCountryCodes:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(t){var a=t.data;a&&0===a.code?e.countryCodes=a.data:e.$message.error(a.msg)})},deleteHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(e?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/atusertokenios/delete"),method:"post",data:t.$http.adornData(a,!1)}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})})}}},i={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:"设置设备名称","close-on-click-modal":!1,visible:e.visibleFlag},on:{"update:visible":function(t){e.visibleFlag=t}}},[a("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.getDataList()}}},[a("el-form-item",{attrs:{label:"所属账号",prop:"sysUserId"}},[a("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"请选择账号","remote-method":e.queryBySearchWord,loading:e.loading},model:{value:e.dataForm.sysUserId,callback:function(t){e.$set(e.dataForm,"sysUserId",t)},expression:"dataForm.sysUserId"}},e._l(e.sysUserAccountOptions,function(e){return a("el-option",{key:e.userId,attrs:{label:e.username,value:e.userId}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"国号(区号)",prop:"country"}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择国家"},model:{value:e.dataForm.country,callback:function(t){e.$set(e.dataForm,"country",t)},expression:"dataForm.country"}},e._l(e.countryCodes,function(e){return a("el-option",{key:e.value,attrs:{label:e.value,value:e.value}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"是否还原",prop:"reductionFlag"}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择是否已还原"},model:{value:e.dataForm.reductionFlag,callback:function(t){e.$set(e.dataForm,"reductionFlag",t)},expression:"dataForm.reductionFlag"}},e._l(e.reductionFlagCodes,function(e){return a("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),a("el-form-item",[a("el-button",{on:{click:function(t){e.getDataList()}}},[e._v("查询")]),e._v(" "),e.isAuth("ltt:atusertokenios:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.addOrUpdateHandle()}}},[e._v("新增")]):e._e()],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[a("el-table-column",{attrs:{prop:"country","header-align":"center",align:"center",label:"country"}}),e._v(" "),a("el-table-column",{attrs:{prop:"bundleId","header-align":"center",align:"center",label:"bundleId"}}),e._v(" "),a("el-table-column",{attrs:{prop:"userName","header-align":"center",align:"center",label:"userName"}}),e._v(" "),a("el-table-column",{attrs:{prop:"phoneNumber","header-align":"center",align:"center",label:"phoneNumber"}}),e._v(" "),a("el-table-column",{attrs:{prop:"mid","header-align":"center",align:"center",label:"mid"}}),e._v(" "),a("el-table-column",{attrs:{prop:"reductionFlag","header-align":"center",align:"center",label:"还原状态"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.reductionFlagCodes,function(n){return t.row.reductionFlag===n.key?a("el-tag",{key:n.key},[e._v("\n            "+e._s(n.value)+"\n          ")]):e._e()})}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),e._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.restoreDataHandle(t.row.id)}}},[e._v("还原")]),e._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.backupHandle(t.row.id)}}},[e._v("备份")])]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]},r=a("46Yf")(n,i,!1,null,null,null);t.default=r.exports},k1yD:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=a("6XLs"),i=a("NVTx"),r={data:function(){return{dataForm:{deviceId:null,deviceName:null},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],updateDeviceNameVisible:!1,tokenListVisible:!1,sysUserAccountOptions:[],countryCodes:[]}},components:{UpdateDeviceName:n.default,TokenList:i.default},activated:function(){this.getDataList(),this.getCountryCodes()},methods:{getDataList:function(){var e=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atusertokenios/queryDevicePage"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,deviceId:this.dataForm.deviceId,deviceName:this.dataForm.deviceName})}).then(function(t){var a=t.data;a&&0===a.code?(e.dataList=a.page.list,e.totalPage=a.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},tokenListHandle:function(e,t){var a=this;this.tokenListVisible=!0,this.$nextTick(function(){a.$refs.tokenList.init(e,t)})},updateDeviceName:function(e){var t=this;this.updateDeviceNameVisible=!0,this.$nextTick(function(){t.$refs.updateDeviceName.init(e)})}}},o={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.getDataList()}}},[a("el-form-item",{attrs:{label:"设备id",prop:"deviceId"}},[a("el-input",{attrs:{placeholder:"设备id"},model:{value:e.dataForm.deviceId,callback:function(t){e.$set(e.dataForm,"deviceId",t)},expression:"dataForm.deviceId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"设备名称",prop:"deviceName"}},[a("el-input",{attrs:{placeholder:"设备名称"},model:{value:e.dataForm.deviceName,callback:function(t){e.$set(e.dataForm,"deviceName",t)},expression:"dataForm.deviceName"}})],1),e._v(" "),a("el-form-item",[a("el-button",{on:{click:function(t){e.getDataList()}}},[e._v("查询")])],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[a("el-table-column",{attrs:{prop:"deviceId","header-align":"center",align:"center",label:"设备id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"deviceName","header-align":"center",align:"center",label:"设备名称"}}),e._v(" "),a("el-table-column",{attrs:{prop:"deviceCount","header-align":"center",align:"center",label:"数据总数"}}),e._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.updateDeviceName(t.row.deviceId)}}},[e._v("设置设备名称")]),e._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.tokenListHandle(t.row.deviceId,t.row.deviceName)}}},[e._v("token列表")])]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.updateDeviceNameVisible?a("update-device-name",{ref:"updateDeviceName",on:{refreshDataList:e.getDataList}}):e._e(),e._v(" "),e.tokenListVisible?a("token-list",{ref:"tokenList",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]},s=a("46Yf")(r,o,!1,null,null,null);t.default=s.exports}});