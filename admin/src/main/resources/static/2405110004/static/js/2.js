webpackJsonp([2,12,84],{"/zQB":function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=a("FgeY"),o=a("1GVP"),n={data:function(){return{dataList:[],pageIndex:1,pageSize:10,totalPage:0,errLogsVisible:!1,dataListLoading:!1,addOrUpdateVisible:!1,dataListSelections:[],isLoading:!1,groupType:null,groupStatusList:[],uploadUrl:"",options:[],groupStatusCodes:[],tableData:[],navyUrlFileList:[],dataUserGroupList:[],materialUrlFileList:[],isModalVisible:!1,tableDataFlag:!1,fileContentList:[],remaining:"",countryCodes:[],openAppOptions:[],judgeOptions:[],dataRule:{},dataFormGroupTask:{id:0,taskName:"",groupType:"",addTotalQuantity:"",successfulQuantity:"",failuresQuantity:"",taskStatus:"",schedule:"",updateTime:"",deleteFlag:"",createTime:"",sysUserId:""},dataForm:{id:null,userGroupId:null,userGroupIdH:null,changeGroupCountryCode:null,changeGroupId:null,groupType:null,groupName:"",countryCode:66,countryCodeH:81,groupCountTotal:94,pullGroupNumber:1,groupCount:null,groupCountStart:0,intervalSecond:7,searchIntervalSecond:null,ipCountryCode:null,autoPullGroup:2,randomGroupName:1,accountGroupDistributed:1,navyUrlList:[],materialUrlList:[]}}},components:{ErrLogs:a("+reW").default,ModalBox:r.default,ReallocateToken:o.default},activated:function(){var t=this.$route.query.id;this.dataForm.id=t,this.uploadUrl=this.$http.adornUrl("/app/file/upload"),this.getCountryCodeEnums(),this.getOpenApps(),this.getUserGroupDataList(),this.getGroupType(),this.infoById(),this.getDataList(),this.getGroupStatusCodes()},methods:{sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},nextGroup:function(){this.dataFormGroupTask.taskStatus=2},getDataList:function(){var t=this;this.isLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atgroup/list"),method:"post",data:this.$http.adornData({page:this.pageIndex,limit:this.pageSize,groupTaskId:this.dataForm.id,groupStatusList:this.groupStatusList})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0)}).finally(function(){t.isLoading=!1})},errLogsHandle:function(t){var e=this;this.errLogsVisible=!0,this.$nextTick(function(){e.$refs.errLogs.init(t)})},atUserHandle:function(t){var e=this;this.$nextTick(function(){e.$router.push({name:"ltt-atuser",query:{userId:t}})})},copyToClipboard:function(t){var e=t?[t]:this.dataListSelections.map(function(t){return t.userTelephone}),a=document.createElement("textarea");a.value=e,a.setAttribute("readonly",""),a.style.position="absolute",a.style.left="-9999px",document.body.appendChild(a);var r=document.createRange();r.selectNode(a);var o=window.getSelection();o.removeAllRanges(),o.addRange(r),document.execCommand("copy"),document.body.removeChild(a),this.$message.success("手机号复制成功！")},infoById:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atgrouptask/info/"+this.dataForm.id),method:"get",params:this.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataFormGroupTask.taskName=a.atGroupTask.taskName,t.dataFormGroupTask.groupType=a.atGroupTask.groupType,t.dataFormGroupTask.addTotalQuantity=a.atGroupTask.addTotalQuantity,t.dataFormGroupTask.successfulQuantity=a.atGroupTask.successfulQuantity,t.dataFormGroupTask.failuresQuantity=a.atGroupTask.failuresQuantity,t.dataFormGroupTask.taskStatus=a.atGroupTask.taskStatus,t.dataFormGroupTask.schedule=a.atGroupTask.schedule,t.dataFormGroupTask.updateTime=a.atGroupTask.updateTime,t.dataFormGroupTask.deleteFlag=a.atGroupTask.deleteFlag,t.dataFormGroupTask.createTime=a.atGroupTask.createTime,t.dataFormGroupTask.sysUserId=a.atGroupTask.sysUserId)})},getGroupType:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getGroupType"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.options=a.data:t.$message.error(a.msg)})},errRetryHandle:function(t){var e=this;this.$confirm("确定进行拉群重试操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atgroup/errRetryGroup/"+t),method:"post"}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},groupTypeChangeHandler:function(){this.dataForm.groupType=this.groupType},getUserGroupDataList:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atusergroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(e){var a=e.data;a&&0===a.code?t.dataUserGroupList=a.page.list:t.dataUserGroupList=[]})},getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.countryCodes=a.data:t.$message.error(a.msg)})},getOpenApps:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getOpenApps"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.openAppOptions=a.data:t.$message.error(a.msg)})},hide:function(){this.tableDataFlag=!1},show:function(){this.tableDataFlag=!0},reallocateTokenHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"重新分配账号":"批量重新分配账号")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.addOrUpdateVisible=!0,e.$nextTick(function(){e.$refs.reallocateToken.init(a)})})},updateGroupHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"重新修改群名称":"批量重新修改群名称")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.isLoading=!0,e.$nextTick(function(){e.$http({url:e.$http.adornUrl("/ltt/atgroup/updateGroupName"),method:"post",data:e.$http.adornData({ids:a})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功，成功修改"+a.successCount+"条",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)}).finally(function(){e.isLoading=!1})})})},getRealGroupNameHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"获取真实群名称":"批量获取真实群名称")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.isLoading=!0,e.$nextTick(function(){e.$http({url:e.$http.adornUrl("/ltt/atgroup/getRealGroupName"),method:"post",data:e.$http.adornData({ids:a})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)}).finally(function(){e.isLoading=!1})})})},startTaskHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"启动任务":"批量启动任务")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.isLoading=!0,e.$nextTick(function(){e.$http({url:e.$http.adornUrl("/ltt/atgroup/startTask"),method:"post",data:e.$http.adornData({ids:a})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)}).finally(function(){e.isLoading=!1})})})},exportHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"导出报表":"批量导出报表")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){window.open(e.$http.adornUrl("/ltt/atgroup/importZip?token="+e.$cookie.get("token")+"&ids="+a.join(",")))})},startGroup14Handler:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"开始拉群":"批量开始拉群")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atgroup/startGroup"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},startGroupHandler:function(){var t=this;this.isLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atgrouptask/onGroupStart"),method:"POST",data:this.$http.adornData({id:this.dataForm.id,groupName:this.dataForm.groupName,countryCode:this.dataForm.countryCode,countryCodeH:this.dataForm.countryCodeH,intervalSecond:this.dataForm.intervalSecond,searchIntervalSecond:this.dataForm.searchIntervalSecond,ipCountryCode:this.dataForm.ipCountryCode,autoPullGroup:this.dataForm.autoPullGroup,randomGroupName:this.dataForm.randomGroupName,userGroupId:this.dataForm.userGroupId,changeGroupCountryCode:this.dataForm.changeGroupCountryCode,changeGroupId:this.dataForm.changeGroupId,userGroupIdH:this.dataForm.userGroupIdH,navyUrlList:this.dataForm.navyUrlList,groupCountStart:this.dataForm.groupCountStart,openApp:this.dataForm.openApp,accountGroupDistributed:this.dataForm.accountGroupDistributed,pullGroupNumber:this.dataForm.pullGroupNumber,groupCountTotal:this.dataForm.groupCountTotal,materialUrlList:this.dataForm.materialUrlList,groupType:this.groupType,groupCount:this.dataForm.groupCount})}).then(function(e){var a=e.data;a&&0===a.code?t.infoById():t.$message.error(a.msg)}).finally(function(){t.isLoading=!1})},onGroupPreExportHandler:function(){this.isLoading=!0,this.dataForm.navyUrlList=[];for(var t=0;t<this.navyUrlFileList.length;t++){var e=this.navyUrlFileList[t];this.dataForm.navyUrlList.push(e.response.data)}console.log(this.dataForm.navyUrlList),this.dataForm.materialUrlList=[];for(var a=0;a<this.materialUrlFileList.length;a++){var r=this.materialUrlFileList[a];this.dataForm.materialUrlList.push(r.response.data)}window.open(this.$http.adornUrl("/ltt/atgrouptask/onGroupPreExport?groupName="+this.dataForm.groupName+"&groupCountStart="+this.dataForm.groupCountStart+"&groupCountTotal="+this.dataForm.groupCountTotal+"&groupCount="+this.dataForm.groupCount+"&navyUrlList="+this.dataForm.navyUrlList.join(",")+"&materialUrlList="+this.dataForm.materialUrlList.join(",")+"&token="+this.$cookie.get("token"))),this.isLoading=!1},onGroupPreHandler:function(){var t=this;this.show(),this.isLoading=!0,this.dataForm.navyUrlList=[];for(var e=0;e<this.navyUrlFileList.length;e++){var a=this.navyUrlFileList[e];this.dataForm.navyUrlList.push(a.response.data)}console.log(this.dataForm.navyUrlList),this.dataForm.materialUrlList=[];for(var r=0;r<this.materialUrlFileList.length;r++){var o=this.materialUrlFileList[r];this.dataForm.materialUrlList.push(o.response.data)}console.log(this.dataForm.materialUrlList),this.$http({url:this.$http.adornUrl("/ltt/atgrouptask/onGroupPre"),method:"POST",data:this.$http.adornData({groupName:this.dataForm.groupName,navyUrlList:this.dataForm.navyUrlList,groupCountStart:this.dataForm.groupCountStart,groupCountTotal:this.dataForm.groupCountTotal,pullGroupNumber:this.dataForm.pullGroupNumber,materialUrlList:this.dataForm.materialUrlList,groupCount:this.dataForm.groupCount})}).then(function(e){var a=e.data;a&&0===a.code?(t.tableData=a.onGroupPreVOS,t.remaining=a.remaining):t.$message.error(a.msg)}).finally(function(){t.isLoading=!1})},getGroupStatusCodes:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getGroupStatus"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.groupStatusCodes=a.data:t.$message.error(a.msg)})},handleMaterialUrlListHanlder:function(t,e,a){this.materialUrlFileList=a},handleMaterialUrlListHanlderRemove:function(t,e){this.materialUrlFileList=e},handleMaterialUrlListHanlderPreview:function(t,e){this.materialUrlFileList=e},handleNavyUrlListHandler:function(t,e,a){this.navyUrlFileList=a},handleNavyUrlListHandlerRemove:function(t,e){this.navyUrlFileList=e},handleNavyUrlListHandlerPreview:function(t,e){this.navyUrlFileList=e},onMouseOver:function(){var t=this;this.isLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atgrouptask/getGroupNameList"),method:"get",params:this.$http.adornParams({groupName:this.dataForm.groupName,groupCountStart:this.dataForm.groupCountStart,groupCount:this.dataForm.groupCount,pullGroupNumber:this.dataForm.pullGroupNumber,groupCountTotal:this.dataForm.groupCountTotal})}).then(function(e){var a=e.data;a&&0===a.code?t.fileContentList=a.data:t.$message.error(a.msg)}).finally(function(){t.isLoading=!1})}}},l={render:function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("div",{directives:[{name:"loading",rawName:"v-loading",value:t.isLoading,expression:"isLoading"}],staticClass:"mod-config"},[r("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule}},[1===t.dataFormGroupTask.taskStatus||2===t.dataFormGroupTask.taskStatus?r("div",{staticClass:"group-container"},[r("div",{staticClass:"img-logo"},[r("img",{attrs:{src:a("rG05")}})]),t._v(" "),r("div",{staticClass:"group-content"},[r("div",{staticClass:"title"},[t._v("自定义群名")]),t._v(" "),r("el-row",{attrs:{gutter:20}},[r("el-col",{attrs:{span:12}},[r("el-form-item",{attrs:{label:"",prop:"groupName"}},[r("el-input",{attrs:{placeholder:"自定义群名"},model:{value:t.dataForm.groupName,callback:function(e){t.$set(t.dataForm,"groupName",e)},expression:"dataForm.groupName"}})],1)],1),t._v(" "),r("el-col",{attrs:{span:6}},[r("el-form-item",{attrs:{label:"",prop:"groupCountStart"}},[r("el-input",{attrs:{placeholder:"从哪里开始"},model:{value:t.dataForm.groupCountStart,callback:function(e){t.$set(t.dataForm,"groupCountStart",e)},expression:"dataForm.groupCountStart"}})],1)],1),t._v(" "),r("el-col",{attrs:{span:6}},[r("el-form-item",{attrs:{label:"",prop:"groupCount"}},[r("el-input",{attrs:{placeholder:"数量"},model:{value:t.dataForm.groupCount,callback:function(e){t.$set(t.dataForm,"groupCount",e)},expression:"dataForm.groupCount"}})],1)],1),t._v(" "),r("el-col",{attrs:{span:24}},[r("el-tooltip",{staticClass:"item",attrs:{content:null,effect:"dark",placement:"top-start"}},[r("template",{attrs:{slot:"content"},slot:"content"},[r("div",{staticClass:"tooltip-content"},t._l(t.fileContentList,function(e){return r("pre",[t._v(t._s(e))])}))]),t._v(" "),r("el-button",{on:{click:t.onMouseOver}},[t._v("查看群名")])],2)],1)],1),t._v(" "),r("el-row",{attrs:{gutter:20}},[r("el-col",{attrs:{span:12}},[r("div",{staticClass:"title"},[t._v("水军")]),t._v(" "),r("el-upload",{staticStyle:{"text-align":"center"},attrs:{drag:"",action:t.uploadUrl,"on-success":t.handleNavyUrlListHandler,"on-preview":t.handleNavyUrlListHandlerPreview,"on-remove":t.handleNavyUrlListHandlerRemove,multiple:""},model:{value:t.navyUrlFileList,callback:function(e){t.navyUrlFileList=e},expression:"navyUrlFileList"}},[r("i",{staticClass:"el-icon-upload"}),t._v(" "),r("div",{staticClass:"el-upload__text"},[t._v("将文件拖到此处，或"),r("em",[t._v("点击上传")])])])],1),t._v(" "),r("el-col",{attrs:{span:12}},[r("div",{staticClass:"title"},[t._v("料子")]),t._v(" "),r("el-upload",{staticStyle:{"text-align":"center"},attrs:{drag:"",action:t.uploadUrl,"on-success":t.handleMaterialUrlListHanlder,"on-preview":t.handleMaterialUrlListHanlderPreview,"on-remove":t.handleMaterialUrlListHanlderRemove,multiple:""},model:{value:t.materialUrlFileList,callback:function(e){t.materialUrlFileList=e},expression:"materialUrlFileList"}},[r("i",{staticClass:"el-icon-upload"}),t._v(" "),r("div",{staticClass:"el-upload__text"},[t._v("将文件拖到此处，或"),r("em",[t._v("点击上传")])])])],1)],1),t._v(" "),r("el-row",[r("el-col",{attrs:{span:24}},[r("el-button",{on:{click:t.startGroupHandler}},[t._v("开始拉群")]),t._v(" "),r("el-button",{on:{click:t.onGroupPreHandler}},[t._v("预览")]),t._v(" "),r("el-button",{on:{click:t.hide}},[t._v("隐藏表格")]),t._v(" "),r("el-button",{on:{click:t.show}},[t._v("显示表格")]),t._v(" "),r("el-button",{on:{click:t.onGroupPreExportHandler}},[t._v("导出剩余粉")])],1)],1),t._v(" "),r("el-row",[r("el-col",{attrs:{span:24}},[t.tableDataFlag?r("el-table",{staticStyle:{width:"100%"},attrs:{data:t.tableData}},[r("el-table-column",{attrs:{label:t.remaining,align:"center"}},[r("el-table-column",{attrs:{prop:"groupName",align:"center",label:"群名称"}}),t._v(" "),r("el-table-column",{attrs:{align:"center",prop:"navyTextListsStr",label:"水军"}}),t._v(" "),r("el-table-column",{attrs:{align:"center",prop:"materialUrlsStr",label:"料子"}}),t._v(" "),r("el-table-column",{attrs:{align:"center",prop:"total",label:"总和"}})],1)],1):t._e()],1)],1)],1),t._v(" "),r("div",{staticClass:"money-container"},[r("div",{staticClass:"title"},[t._v("拉群配置")]),t._v(" "),r("el-form-item",{attrs:{label:"拉群号国家"}},[r("el-select",{attrs:{placeholder:"拉群号国家",clearable:""},model:{value:t.dataForm.countryCode,callback:function(e){t.$set(t.dataForm,"countryCode",e)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),r("el-form-item",{attrs:{label:"拉群号分组",prop:"userGroupId"}},[r("el-select",{attrs:{placeholder:"账户分组"},model:{value:t.dataForm.userGroupId,callback:function(e){t.$set(t.dataForm,"userGroupId",e)},expression:"dataForm.userGroupId"}},t._l(t.dataUserGroupList,function(t){return r("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),6===t.groupType?r("el-form-item",{attrs:{label:"合群号国家"}},[r("el-select",{attrs:{placeholder:"合群号国家",clearable:""},model:{value:t.dataForm.countryCodeH,callback:function(e){t.$set(t.dataForm,"countryCodeH",e)},expression:"dataForm.countryCodeH"}},t._l(t.countryCodes,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1):t._e(),t._v(" "),6===t.groupType?r("el-form-item",{attrs:{label:"合群号分组",prop:"userGroupIdH"}},[r("el-select",{attrs:{placeholder:"合群号分组"},model:{value:t.dataForm.userGroupIdH,callback:function(e){t.$set(t.dataForm,"userGroupIdH",e)},expression:"dataForm.userGroupIdH"}},t._l(t.dataUserGroupList,function(t){return r("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1):t._e(),t._v(" "),r("el-form-item",{attrs:{label:"拉群类型",prop:"groupType"}},[r("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{placeholder:"选择类型",size:"large"},on:{change:t.groupTypeChangeHandler},model:{value:t.groupType,callback:function(e){t.groupType=e},expression:"groupType"}},t._l(t.options,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),r("el-form-item",{attrs:{label:"群人数",prop:"groupCountTotal"}},[r("el-input",{staticStyle:{width:"70%"},attrs:{placeholder:"群人数"},model:{value:t.dataForm.groupCountTotal,callback:function(e){t.$set(t.dataForm,"groupCountTotal",e)},expression:"dataForm.groupCountTotal"}})],1),t._v(" "),r("el-form-item",{attrs:{label:"拉群号数量",prop:"pullGroupNumber"}},[r("el-input",{staticStyle:{width:"70%"},attrs:{placeholder:"拉群号数量"},model:{value:t.dataForm.pullGroupNumber,callback:function(e){t.$set(t.dataForm,"pullGroupNumber",e)},expression:"dataForm.pullGroupNumber"}})],1),t._v(" "),r("el-form-item",{attrs:{label:"加粉间隔秒数",prop:"intervalSecond"}},[r("el-input",{staticStyle:{width:"70%"},attrs:{placeholder:"加粉间隔秒数"},model:{value:t.dataForm.intervalSecond,callback:function(e){t.$set(t.dataForm,"intervalSecond",e)},expression:"dataForm.intervalSecond"}})],1),t._v(" "),1===t.groupType?r("el-form-item",{attrs:{label:"搜索间隔秒数",prop:"searchIntervalSecond"}},[r("el-input",{staticStyle:{width:"70%"},attrs:{placeholder:"搜索间隔秒数"},model:{value:t.dataForm.searchIntervalSecond,callback:function(e){t.$set(t.dataForm,"searchIntervalSecond",e)},expression:"dataForm.searchIntervalSecond"}})],1):t._e(),t._v(" "),r("el-form-item",{attrs:{label:"代理ip"}},[r("el-select",{attrs:{placeholder:"代理ip",clearable:""},model:{value:t.dataForm.ipCountryCode,callback:function(e){t.$set(t.dataForm,"ipCountryCode",e)},expression:"dataForm.ipCountryCode"}},t._l(t.countryCodes,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),r("el-form-item",{attrs:{label:"加粉打开app"}},[r("el-select",{attrs:{placeholder:"加粉打开app",clearable:""},model:{value:t.dataForm.openApp,callback:function(e){t.$set(t.dataForm,"openApp",e)},expression:"dataForm.openApp"}},t._l(t.openAppOptions,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),r("el-form-item",{attrs:{label:"加粉结束自动拉群"}},[r("el-select",{attrs:{placeholder:"加粉结束自动拉群",clearable:""},model:{value:t.dataForm.autoPullGroup,callback:function(e){t.$set(t.dataForm,"autoPullGroup",e)},expression:"dataForm.autoPullGroup"}},t._l(t.openAppOptions,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),r("el-form-item",{attrs:{label:"群名是否随机"}},[r("el-select",{attrs:{placeholder:"群名是否随机",clearable:""},model:{value:t.dataForm.randomGroupName,callback:function(e){t.$set(t.dataForm,"randomGroupName",e)},expression:"dataForm.randomGroupName"}},t._l(t.openAppOptions,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),2===t.dataForm.randomGroupName?r("el-form-item",{attrs:{label:"改群名号国家",prop:"changeGroupCountryCode"}},[r("el-select",{attrs:{placeholder:"改群名号国家",clearable:""},model:{value:t.dataForm.changeGroupCountryCode,callback:function(e){t.$set(t.dataForm,"changeGroupCountryCode",e)},expression:"dataForm.changeGroupCountryCode"}},t._l(t.countryCodes,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1):t._e(),t._v(" "),2===t.dataForm.randomGroupName?r("el-form-item",{attrs:{label:"改群名号分组",prop:"changeGroupId"}},[r("el-select",{attrs:{placeholder:"改群名号分组"},model:{value:t.dataForm.changeGroupId,callback:function(e){t.$set(t.dataForm,"changeGroupId",e)},expression:"dataForm.changeGroupId"}},t._l(t.dataUserGroupList,function(t){return r("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1):t._e(),t._v(" "),2===t.dataForm.randomGroupName?r("el-form-item",{attrs:{label:"一号几群",prop:"accountGroupDistributed"}},[r("el-input",{staticStyle:{width:"70%"},attrs:{placeholder:"一号几群"},model:{value:t.dataForm.accountGroupDistributed,callback:function(e){t.$set(t.dataForm,"accountGroupDistributed",e)},expression:"dataForm.accountGroupDistributed"}})],1):t._e()],1)]):r("div",[r("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[r("el-form-item",{attrs:{label:"拉群状态",prop:"groupStatusList"}},[r("el-select",{attrs:{multiple:"",placeholder:"选择类型",size:"large"},model:{value:t.groupStatusList,callback:function(e){t.groupStatusList=e},expression:"groupStatusList"}},t._l(t.groupStatusCodes,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),r("el-form-item",[r("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")]),t._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(e){t.nextGroup()}}},[t._v("继续拉群")]),t._v(" "),r("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.exportHandle()}}},[t._v("导出报表")]),t._v(" "),r("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.startGroup14Handler()}}},[t._v("开始拉群")]),t._v(" "),r("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.reallocateTokenHandle()}}},[t._v("重新分配账号拉群")]),t._v(" "),r("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.updateGroupHandle()}}},[t._v("修改群名")]),t._v(" "),r("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.getRealGroupNameHandle()}}},[t._v("获取真实群名称")]),t._v(" "),r("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.startTaskHandle()}}},[t._v("启动任务")]),t._v(" "),r("el-button",{attrs:{type:"primary",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.copyToClipboard()}}},[t._v("复制手机号\n          ")])],1)],1),t._v(" "),r("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[r("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),r("el-table-column",{attrs:{prop:"groupName","header-align":"center",align:"center",label:"群名称"}}),t._v(" "),r("el-table-column",{attrs:{prop:"realGroupName","header-align":"center",align:"center",label:"真实群名称"}}),t._v(" "),r("el-table-column",{attrs:{prop:"roomId","header-align":"center",align:"center",label:"群号"}}),t._v(" "),r("el-table-column",{attrs:{prop:"successfullyAttractGroupsNumber","header-align":"center",align:"center",label:"群人数"}}),t._v(" "),r("el-table-column",{attrs:{prop:"userTelephone","header-align":"center",align:"center",width:"180",label:"拉群手机号"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("div",[r("el-button",{attrs:{type:"text"},on:{click:function(a){t.atUserHandle(e.row.userId)}}},[t._v(t._s(e.row.userTelephone))])],1),t._v(" "),r("div",[r("el-button",{attrs:{type:"text"},on:{click:function(a){t.copyToClipboard(e.row.userTelephone)}}},[t._v("复制")])],1)]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"groupStatusStr","header-align":"center",align:"center",width:"100",label:"拉群状态"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("el-tag",{attrs:{size:"small",type:"danger"}},[t._v(t._s(e.row.groupStatusStr))])]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",width:"100",label:"时间"}}),t._v(" "),r("el-table-column",{attrs:{prop:"addTotalQuantity","header-align":"center",align:"center",width:"120",label:"加粉数据"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("div",[t._v("加粉总数："+t._s(e.row.addTotalQuantity))]),t._v(" "),r("div",[t._v("成功数："+t._s(e.row.successfulQuantity))]),t._v(" "),r("div",[t._v("失败数："+t._s(e.row.failuresQuantity))])]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"randomGroupName","header-align":"center",align:"center",label:"群名是否随机"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(t.openAppOptions,function(a){return e.row.randomGroupName===a.key?r("el-tag",{key:a.key},[t._v("\n                "+t._s(a.value)+"\n              ")]):t._e()})}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",width:"100",label:"加粉状态"},scopedSlots:t._u([{key:"default",fn:function(e){return[5===e.row.taskStatus?r("el-button",{attrs:{type:"success",plain:""}},[t._v(t._s(e.row.taskStatusStr))]):t._e(),t._v(" "),3===e.row.taskStatus?r("el-button",{attrs:{type:"success",plain:""}},[t._v(t._s(e.row.taskStatusStr))]):t._e(),t._v(" "),2===e.row.taskStatus?r("el-button",{attrs:{type:"warning",plain:""}},[t._v(t._s(e.row.taskStatusStr))]):t._e(),t._v(" "),1===e.row.taskStatus?r("el-button",{attrs:{type:"primary",plain:""}},[t._v(t._s(e.row.taskStatusStr))]):t._e(),t._v(" "),0===e.row.taskStatus?r("el-button",{attrs:{type:"primary",plain:""}},[t._v(t._s(e.row.taskStatusStr))]):t._e()]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"schedule","header-align":"center",align:"center",width:"95",label:"加粉进度"},scopedSlots:t._u([{key:"default",fn:function(t){return[r("el-progress",{attrs:{"stroke-width":7,type:"circle",width:70,percentage:t.row.scheduleFloat}})]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"changeUserPhone","header-align":"center",align:"center",width:"180",label:"修改群水军手机号"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("div",[r("el-button",{attrs:{type:"text"},on:{click:function(a){t.atUserHandle(e.row.changeUserId)}}},[t._v(t._s(e.row.changeUserPhone))])],1),t._v(" "),r("div",[r("el-button",{attrs:{type:"text"},on:{click:function(a){t.copyToClipboard(e.row.changeUserPhone)}}},[t._v("复制")])],1)]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"msg","header-align":"center",align:"center",width:"100",label:"日志"}}),t._v(" "),r("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[r("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.errLogsHandle(e.row.id)}}},[t._v("错误日志")]),t._v(" "),null!=e.row.msg&&-1!==e.row.msg.indexOf("网络异常")||null!=e.row.roomId&&0==e.row.successfullyAttractGroupsNumber?r("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.errRetryHandle(e.row.id)}}},[t._v("错误重试")]):t._e()]}}])})],1),t._v(" "),r("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,30,50,100,2e3],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}})],1)]),t._v(" "),t.addOrUpdateVisible?r("reallocate-token",{ref:"reallocateToken",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.errLogsVisible?r("err-logs",{ref:"errLogs",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]};var i=a("46Yf")(n,l,!1,function(t){a("iEyj")},"data-v-30ace396",null);e.default=i.exports},"1GVP":function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"拉群号国家"}},[a("el-select",{attrs:{placeholder:"拉群号国家",clearable:""},model:{value:t.dataForm.countryCode,callback:function(e){t.$set(t.dataForm,"countryCode",e)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"拉群号分组",prop:"userGroupId"}},[a("el-select",{attrs:{placeholder:"账户分组"},model:{value:t.dataForm.userGroupId,callback:function(e){t.$set(t.dataForm,"userGroupId",e)},expression:"dataForm.userGroupId"}},t._l(t.dataUserGroupList,function(t){return a("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"拉群号数量",prop:"pullGroupNumber"}},[a("el-input",{attrs:{placeholder:"拉群号数量"},model:{value:t.dataForm.pullGroupNumber,callback:function(e){t.$set(t.dataForm,"pullGroupNumber",e)},expression:"dataForm.pullGroupNumber"}})],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},o=a("46Yf")({data:function(){return{visible:!1,countryCodes:[],dataUserGroupList:[],dataForm:{id:0,countryCode:null,userGroupId:null,pullGroupNumber:1,ids:[],taskName:"",groupType:"",addTotalQuantity:"",successfulQuantity:"",failuresQuantity:"",taskStatus:"",schedule:"",updateTime:"",deleteFlag:"",createTime:"",sysUserId:""},dataRule:{taskName:[{required:!0,message:"任务名称不能为空",trigger:"blur"}],groupType:[{required:!0,message:"类型不能为空",trigger:"blur"}],addTotalQuantity:[{required:!0,message:"加粉总数不能为空",trigger:"blur"}],successfulQuantity:[{required:!0,message:"成功数不能为空",trigger:"blur"}],failuresQuantity:[{required:!0,message:"失败数不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"状态不能为空",trigger:"blur"}],schedule:[{required:!0,message:"进度不能为空",trigger:"blur"}],updateTime:[{required:!0,message:"更新时间不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],sysUserId:[{required:!0,message:"管理账户id不能为空",trigger:"blur"}]}}},methods:{getUserGroupDataList:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atusergroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(e){var a=e.data;a&&0===a.code?t.dataUserGroupList=a.page.list:t.dataUserGroupList=[]})},init:function(t){this.dataForm.ids=t,this.visible=!0,this.getCountryCodeEnums(),this.getUserGroupDataList()},getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.countryCodes=a.data:t.$message.error(a.msg)})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/atgroup/reallocateToken"),method:"post",data:t.$http.adornData({ids:t.dataForm.ids,userGroupId:t.dataForm.userGroupId,pullGroupNumber:t.dataForm.pullGroupNumber,countryCode:t.dataForm.countryCode})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},r,!1,null,null,null);e.default=o.exports},FgeY:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={props:{isVisible:Boolean,textContent:String},methods:{closeModal:function(){this.$emit("update:isVisible",!1)}}},o={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return t.isVisible?a("div",{staticClass:"modal-overlay",on:{click:function(e){if(e.target!==e.currentTarget)return null;t.closeModal(e)}}},[a("div",{staticClass:"modal"},[a("div",{staticClass:"modal-header"},[a("h3",[t._v("Text File Content")]),t._v(" "),a("button",{on:{click:t.closeModal}},[t._v("Close")])]),t._v(" "),a("div",{staticClass:"modal-body"},[a("pre",[t._v(t._s(t.textContent))])])])]):t._e()},staticRenderFns:[]};var n=a("46Yf")(r,o,!1,function(t){a("Wxgv")},"data-v-21d7af7c",null);e.default=n.exports},PlrG:function(t,e,a){(t.exports=a("acE3")(!1)).push([t.i,"\n.modal-overlay[data-v-21d7af7c] {\n  position: fixed;\n  top: 0;\n  left: 0;\n  right: 0;\n  bottom: 0;\n  background-color: rgba(0, 0, 0, 0.5);\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  -webkit-box-pack: center;\n      -ms-flex-pack: center;\n          justify-content: center;\n}\n.modal[data-v-21d7af7c] {\n  background: white;\n  padding: 20px;\n  border-radius: 5px;\n  width: 50%;\n  max-width: 500px;\n}\n.modal .modal-header[data-v-21d7af7c] {\n    display: -webkit-box;\n    display: -ms-flexbox;\n    display: flex;\n    -webkit-box-pack: justify;\n        -ms-flex-pack: justify;\n            justify-content: space-between;\n    -webkit-box-align: center;\n        -ms-flex-align: center;\n            align-items: center;\n}\n.modal .modal-header h3[data-v-21d7af7c] {\n      margin: 0;\n}\n.modal .modal-header button[data-v-21d7af7c] {\n      border: none;\n      background-color: #f44336;\n      color: white;\n      padding: 5px 10px;\n      border-radius: 3px;\n      cursor: pointer;\n}\n.modal .modal-header button[data-v-21d7af7c]:hover {\n        background-color: #ea1c0d;\n}\n.modal .modal-body[data-v-21d7af7c] {\n    margin-top: 20px;\n}\n.modal .modal-body pre[data-v-21d7af7c] {\n      white-space: pre-wrap;\n      /* CSS 3 */\n      white-space: -moz-pre-wrap;\n      /* Mozilla, since 1999 */\n      white-space: -pre-wrap;\n      /* Opera 4-6 */\n      white-space: -o-pre-wrap;\n      /* Opera 7 */\n      word-wrap: break-word;\n      /* Internet Explorer 5.5+ */\n}\n",""])},Wxgv:function(t,e,a){var r=a("PlrG");"string"==typeof r&&(r=[[t.i,r,""]]),r.locals&&(t.exports=r.locals);a("XkoO")("6e8d033e",r,!0)},hYlD:function(t,e,a){(t.exports=a("acE3")(!1)).push([t.i,"\n.tooltip-content[data-v-30ace396] {\n  max-height: 100px;\n  overflow-y: auto;\n  white-space: normal;\n}\n.mod-config[data-v-30ace396] {\n  width: 100px;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n}\n.mod-config .el-form[data-v-30ace396] {\n    -webkit-box-flex: 1;\n        -ms-flex: 1;\n            flex: 1;\n    display: -webkit-box;\n    display: -ms-flexbox;\n    display: flex;\n}\n.mod-config .el-form .group-container[data-v-30ace396] {\n      -webkit-box-flex: 1;\n          -ms-flex: 1;\n              flex: 1;\n      display: -webkit-box;\n      display: -ms-flexbox;\n      display: flex;\n      -webkit-box-orient: horizontal;\n      -webkit-box-direction: normal;\n          -ms-flex-direction: row;\n              flex-direction: row;\n}\n.mod-config .el-form .group-container .img-logo[data-v-30ace396] {\n        -webkit-box-flex: 1;\n            -ms-flex: 1;\n                flex: 1;\n}\n.mod-config .el-form .group-container .img-logo .title[data-v-30ace396] {\n          margin: 10px 0px;\n          text-align: center;\n          font-size: 30px;\n          font-weight: bold;\n}\n.mod-config .el-form .group-container .group-content[data-v-30ace396] {\n        -webkit-box-flex: 2;\n            -ms-flex: 2;\n                flex: 2;\n        margin: 0 auto;\n        text-align: center;\n        font-size: 30px;\n        font-weight: bold;\n}\n.mod-config .el-form .group-container .group-content .title[data-v-30ace396] {\n          margin: 20px 0;\n}\n.mod-config .el-form .group-container .money-container[data-v-30ace396] {\n        -webkit-box-flex: 1;\n            -ms-flex: 1;\n                flex: 1;\n        margin-left: 66px;\n}\n.mod-config .el-form .group-container .money-container .title[data-v-30ace396] {\n          margin: 10px 0px;\n          text-align: center;\n          font-size: 30px;\n          font-weight: bold;\n}\n",""])},iEyj:function(t,e,a){var r=a("hYlD");"string"==typeof r&&(r=[[t.i,r,""]]),r.locals&&(t.exports=r.locals);a("XkoO")("7ac70ce4",r,!0)},rG05:function(t,e,a){t.exports=a.p+"static/img/250px-Bagua-name-earlier.svg.722a8d1.png"}});