webpackJsonp([3,12,84],{"/zQB":function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r=e("FgeY"),o=e("1GVP"),n={data:function(){return{dataList:[],pageIndex:1,pageSize:10,totalPage:0,errLogsVisible:!1,dataListLoading:!1,addOrUpdateVisible:!1,dataListSelections:[],isLoading:!1,groupType:null,groupStatusList:[],uploadUrl:"",options:[],groupStatusCodes:[],tableData:[],navyUrlFileList:[],dataUserGroupList:[],materialUrlFileList:[],isModalVisible:!1,tableDataFlag:!1,fileContentList:[],remaining:"",countryCodes:[],dataRule:{},dataFormGroupTask:{id:0,taskName:"",groupType:"",addTotalQuantity:"",successfulQuantity:"",failuresQuantity:"",taskStatus:"",schedule:"",updateTime:"",deleteFlag:"",createTime:"",sysUserId:""},dataForm:{id:null,userGroupId:null,groupType:null,groupName:"",countryCode:66,groupCountTotal:99,pullGroupNumber:1,groupCount:null,groupCountStart:0,navyUrlList:[],materialUrlList:[]}}},components:{ErrLogs:e("+reW").default,ModalBox:r.default,ReallocateToken:o.default},activated:function(){var t=this.$route.query.id;this.dataForm.id=t,this.uploadUrl=this.$http.adornUrl("/app/file/upload"),this.getCountryCodeEnums(),this.getUserGroupDataList(),this.getGroupType(),this.infoById(),this.getDataList(),this.getGroupStatusCodes()},methods:{sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},nextGroup:function(){this.dataFormGroupTask.taskStatus=2},getDataList:function(){var t=this;this.isLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atgroup/list"),method:"post",data:this.$http.adornData({page:this.pageIndex,limit:this.pageSize,groupTaskId:this.dataForm.id,groupStatusList:this.groupStatusList})}).then(function(a){var e=a.data;e&&0===e.code?(t.dataList=e.page.list,t.totalPage=e.page.totalCount):(t.dataList=[],t.totalPage=0)}).finally(function(){t.isLoading=!1})},errLogsHandle:function(t){var a=this;this.errLogsVisible=!0,this.$nextTick(function(){a.$refs.errLogs.init(t)})},infoById:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atgrouptask/info/"+this.dataForm.id),method:"get",params:this.$http.adornParams()}).then(function(a){var e=a.data;e&&0===e.code&&(t.dataFormGroupTask.taskName=e.atGroupTask.taskName,t.dataFormGroupTask.groupType=e.atGroupTask.groupType,t.dataFormGroupTask.addTotalQuantity=e.atGroupTask.addTotalQuantity,t.dataFormGroupTask.successfulQuantity=e.atGroupTask.successfulQuantity,t.dataFormGroupTask.failuresQuantity=e.atGroupTask.failuresQuantity,t.dataFormGroupTask.taskStatus=e.atGroupTask.taskStatus,t.dataFormGroupTask.schedule=e.atGroupTask.schedule,t.dataFormGroupTask.updateTime=e.atGroupTask.updateTime,t.dataFormGroupTask.deleteFlag=e.atGroupTask.deleteFlag,t.dataFormGroupTask.createTime=e.atGroupTask.createTime,t.dataFormGroupTask.sysUserId=e.atGroupTask.sysUserId)})},getGroupType:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getGroupType"),method:"get"}).then(function(a){var e=a.data;e&&0===e.code?t.options=e.data:t.$message.error(e.msg)})},groupTypeChangeHandler:function(){this.dataForm.groupType=this.groupType},getUserGroupDataList:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atusergroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(a){var e=a.data;e&&0===e.code?t.dataUserGroupList=e.page.list:t.dataUserGroupList=[]})},getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(a){var e=a.data;e&&0===e.code?t.countryCodes=e.data:t.$message.error(e.msg)})},hide:function(){this.tableDataFlag=!1},show:function(){this.tableDataFlag=!0},reallocateTokenHandle:function(t){var a=this,e=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+e.join(",")+"]进行["+(t?"重新分配账号":"批量重新分配账号")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){a.addOrUpdateVisible=!0,a.$nextTick(function(){a.$refs.reallocateToken.init(e)})})},startTaskHandle:function(t){var a=this,e=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+e.join(",")+"]进行["+(t?"启动任务":"批量启动任务")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){a.isLoading=!0,a.$nextTick(function(){a.$http({url:a.$http.adornUrl("/ltt/atgroup/startTask"),method:"post",data:a.$http.adornData({ids:e})}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.getDataList()}}):a.$message.error(e.msg)}).finally(function(){a.isLoading=!1})})})},exportHandle:function(t){var a=this,e=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+e.join(",")+"]进行["+(t?"导出报表":"批量导出报表")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){window.open(a.$http.adornUrl("/ltt/atgroup/importZip?token="+a.$cookie.get("token")+"&ids="+e.join(",")))})},startGroupHandler:function(){var t=this;this.isLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atgrouptask/onGroupStart"),method:"POST",data:this.$http.adornData({id:this.dataForm.id,groupName:this.dataForm.groupName,countryCode:this.dataForm.countryCode,userGroupId:this.dataForm.userGroupId,navyUrlList:this.dataForm.navyUrlList,groupCountStart:this.dataForm.groupCountStart,pullGroupNumber:this.dataForm.pullGroupNumber,groupCountTotal:this.dataForm.groupCountTotal,materialUrlList:this.dataForm.materialUrlList,groupType:this.groupType,groupCount:this.dataForm.groupCount})}).then(function(a){var e=a.data;e&&0===e.code?t.infoById():t.$message.error(e.msg)}).finally(function(){t.isLoading=!1})},onGroupPreExportHandler:function(){this.isLoading=!0,this.dataForm.navyUrlList=[];for(var t=0;t<this.navyUrlFileList.length;t++){var a=this.navyUrlFileList[t];this.dataForm.navyUrlList.push(a.response.data)}console.log(this.dataForm.navyUrlList),this.dataForm.materialUrlList=[];for(var e=0;e<this.materialUrlFileList.length;e++){var r=this.materialUrlFileList[e];this.dataForm.materialUrlList.push(r.response.data)}window.open(this.$http.adornUrl("/ltt/atgrouptask/onGroupPreExport?groupName="+this.dataForm.groupName+"&groupCountStart="+this.dataForm.groupCountStart+"&groupCountTotal="+this.dataForm.groupCountTotal+"&groupCount="+this.dataForm.groupCount+"&navyUrlList="+this.dataForm.navyUrlList.join(",")+"&materialUrlList="+this.dataForm.materialUrlList.join(",")+"&token="+this.$cookie.get("token"))),this.isLoading=!1},onGroupPreHandler:function(){var t=this;this.show(),this.isLoading=!0,this.dataForm.navyUrlList=[];for(var a=0;a<this.navyUrlFileList.length;a++){var e=this.navyUrlFileList[a];this.dataForm.navyUrlList.push(e.response.data)}console.log(this.dataForm.navyUrlList),this.dataForm.materialUrlList=[];for(var r=0;r<this.materialUrlFileList.length;r++){var o=this.materialUrlFileList[r];this.dataForm.materialUrlList.push(o.response.data)}console.log(this.dataForm.materialUrlList),this.$http({url:this.$http.adornUrl("/ltt/atgrouptask/onGroupPre"),method:"POST",data:this.$http.adornData({groupName:this.dataForm.groupName,navyUrlList:this.dataForm.navyUrlList,groupCountStart:this.dataForm.groupCountStart,groupCountTotal:this.dataForm.groupCountTotal,pullGroupNumber:this.dataForm.pullGroupNumber,materialUrlList:this.dataForm.materialUrlList,groupCount:this.dataForm.groupCount})}).then(function(a){var e=a.data;e&&0===e.code?(t.tableData=e.onGroupPreVOS,t.remaining=e.remaining):t.$message.error(e.msg)}).finally(function(){t.isLoading=!1})},getGroupStatusCodes:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getGroupStatus"),method:"get"}).then(function(a){var e=a.data;e&&0===e.code?t.groupStatusCodes=e.data:t.$message.error(e.msg)})},handleMaterialUrlListHanlder:function(t,a,e){this.materialUrlFileList=e},handleMaterialUrlListHanlderRemove:function(t,a){this.materialUrlFileList=a},handleMaterialUrlListHanlderPreview:function(t,a){this.materialUrlFileList=a},handleNavyUrlListHandler:function(t,a,e){this.navyUrlFileList=e},handleNavyUrlListHandlerRemove:function(t,a){this.navyUrlFileList=a},handleNavyUrlListHandlerPreview:function(t,a){this.navyUrlFileList=a},onMouseOver:function(){var t=this;this.isLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atgrouptask/getGroupNameList"),method:"get",params:this.$http.adornParams({groupName:this.dataForm.groupName,groupCountStart:this.dataForm.groupCountStart,groupCount:this.dataForm.groupCount,pullGroupNumber:this.dataForm.pullGroupNumber,groupCountTotal:this.dataForm.groupCountTotal})}).then(function(a){var e=a.data;e&&0===e.code?t.fileContentList=e.data:t.$message.error(e.msg)}).finally(function(){t.isLoading=!1})}}},l={render:function(){var t=this,a=t.$createElement,r=t._self._c||a;return r("div",{directives:[{name:"loading",rawName:"v-loading",value:t.isLoading,expression:"isLoading"}],staticClass:"mod-config"},[r("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule}},[1===t.dataFormGroupTask.taskStatus||2===t.dataFormGroupTask.taskStatus?r("div",{staticClass:"group-container"},[r("div",{staticClass:"img-logo"},[r("img",{attrs:{src:e("rG05")}}),t._v(" "),r("div",{staticClass:"title"},[t._v("拉群配置")]),t._v(" "),r("el-form-item",{attrs:{label:"拉群号国家"}},[r("el-select",{attrs:{placeholder:"拉群号国家",clearable:""},model:{value:t.dataForm.countryCode,callback:function(a){t.$set(t.dataForm,"countryCode",a)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),r("el-form-item",{attrs:{label:"拉群号分组",prop:"userGroupId"}},[r("el-select",{attrs:{placeholder:"账户分组"},model:{value:t.dataForm.userGroupId,callback:function(a){t.$set(t.dataForm,"userGroupId",a)},expression:"dataForm.userGroupId"}},t._l(t.dataUserGroupList,function(t){return r("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),r("el-form-item",{attrs:{label:"拉群类型",prop:"groupType"}},[r("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{placeholder:"选择类型",size:"large"},on:{change:t.groupTypeChangeHandler},model:{value:t.groupType,callback:function(a){t.groupType=a},expression:"groupType"}},t._l(t.options,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),r("el-form-item",{attrs:{label:"群人数",prop:"groupCountTotal"}},[r("el-input",{attrs:{placeholder:"群人数"},model:{value:t.dataForm.groupCountTotal,callback:function(a){t.$set(t.dataForm,"groupCountTotal",a)},expression:"dataForm.groupCountTotal"}})],1),t._v(" "),r("el-form-item",{attrs:{label:"拉群号数量",prop:"pullGroupNumber"}},[r("el-input",{attrs:{placeholder:"拉群号数量"},model:{value:t.dataForm.pullGroupNumber,callback:function(a){t.$set(t.dataForm,"pullGroupNumber",a)},expression:"dataForm.pullGroupNumber"}})],1)],1),t._v(" "),r("div",{staticClass:"group-content"},[r("div",{staticClass:"title"},[t._v("自定义群名")]),t._v(" "),r("el-row",{attrs:{gutter:20}},[r("el-col",{attrs:{span:12}},[r("el-form-item",{attrs:{label:"",prop:"groupName"}},[r("el-input",{attrs:{placeholder:"自定义群名"},model:{value:t.dataForm.groupName,callback:function(a){t.$set(t.dataForm,"groupName",a)},expression:"dataForm.groupName"}})],1)],1),t._v(" "),r("el-col",{attrs:{span:6}},[r("el-form-item",{attrs:{label:"",prop:"groupCountStart"}},[r("el-input",{attrs:{placeholder:"从哪里开始"},model:{value:t.dataForm.groupCountStart,callback:function(a){t.$set(t.dataForm,"groupCountStart",a)},expression:"dataForm.groupCountStart"}})],1)],1),t._v(" "),r("el-col",{attrs:{span:6}},[r("el-form-item",{attrs:{label:"",prop:"groupCount"}},[r("el-input",{attrs:{placeholder:"数量"},model:{value:t.dataForm.groupCount,callback:function(a){t.$set(t.dataForm,"groupCount",a)},expression:"dataForm.groupCount"}})],1)],1),t._v(" "),r("el-col",{attrs:{span:24}},[r("el-tooltip",{staticClass:"item",attrs:{content:null,effect:"dark",placement:"top-start"}},[r("template",{attrs:{slot:"content"},slot:"content"},[r("div",{staticClass:"tooltip-content"},t._l(t.fileContentList,function(a){return r("pre",[t._v(t._s(a))])}))]),t._v(" "),r("el-button",{on:{click:t.onMouseOver}},[t._v("查看群名")])],2)],1)],1),t._v(" "),r("el-row",{attrs:{gutter:20}},[r("el-col",{attrs:{span:12}},[r("div",{staticClass:"title"},[t._v("水军")]),t._v(" "),r("el-upload",{staticStyle:{"text-align":"center"},attrs:{drag:"",action:t.uploadUrl,"on-success":t.handleNavyUrlListHandler,"on-preview":t.handleNavyUrlListHandlerPreview,"on-remove":t.handleNavyUrlListHandlerRemove,multiple:""},model:{value:t.navyUrlFileList,callback:function(a){t.navyUrlFileList=a},expression:"navyUrlFileList"}},[r("i",{staticClass:"el-icon-upload"}),t._v(" "),r("div",{staticClass:"el-upload__text"},[t._v("将文件拖到此处，或"),r("em",[t._v("点击上传")])])])],1),t._v(" "),r("el-col",{attrs:{span:12}},[r("div",{staticClass:"title"},[t._v("料子")]),t._v(" "),r("el-upload",{staticStyle:{"text-align":"center"},attrs:{drag:"",action:t.uploadUrl,"on-success":t.handleMaterialUrlListHanlder,"on-preview":t.handleMaterialUrlListHanlderPreview,"on-remove":t.handleMaterialUrlListHanlderRemove,multiple:""},model:{value:t.materialUrlFileList,callback:function(a){t.materialUrlFileList=a},expression:"materialUrlFileList"}},[r("i",{staticClass:"el-icon-upload"}),t._v(" "),r("div",{staticClass:"el-upload__text"},[t._v("将文件拖到此处，或"),r("em",[t._v("点击上传")])])])],1)],1),t._v(" "),r("el-row",[r("el-col",{attrs:{span:24}},[r("el-button",{on:{click:t.startGroupHandler}},[t._v("开始拉群")]),t._v(" "),r("el-button",{on:{click:t.onGroupPreHandler}},[t._v("预览")]),t._v(" "),r("el-button",{on:{click:t.hide}},[t._v("隐藏表格")]),t._v(" "),r("el-button",{on:{click:t.show}},[t._v("显示表格")]),t._v(" "),r("el-button",{on:{click:t.onGroupPreExportHandler}},[t._v("导出剩余粉")])],1)],1),t._v(" "),r("el-row",[r("el-col",{attrs:{span:24}},[t.tableDataFlag?r("el-table",{staticStyle:{width:"100%"},attrs:{data:t.tableData}},[r("el-table-column",{attrs:{label:t.remaining,align:"center"}},[r("el-table-column",{attrs:{prop:"groupName",align:"center",label:"群名称"}}),t._v(" "),r("el-table-column",{attrs:{align:"center",prop:"navyTextListsStr",label:"水军"}}),t._v(" "),r("el-table-column",{attrs:{align:"center",prop:"materialUrlsStr",label:"料子"}}),t._v(" "),r("el-table-column",{attrs:{align:"center",prop:"total",label:"总和"}})],1)],1):t._e()],1)],1)],1),t._v(" "),r("div",{staticClass:"money-container"})]):r("div",[r("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(a){if(!("button"in a)&&t._k(a.keyCode,"enter",13,a.key))return null;t.getDataList()}}},[r("el-form-item",{attrs:{label:"拉群状态",prop:"groupStatusList"}},[r("el-select",{attrs:{multiple:"",placeholder:"选择类型",size:"large"},model:{value:t.groupStatusList,callback:function(a){t.groupStatusList=a},expression:"groupStatusList"}},t._l(t.groupStatusCodes,function(t){return r("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),r("el-form-item",[r("el-button",{on:{click:function(a){t.getDataList()}}},[t._v("查询")]),t._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(a){t.nextGroup()}}},[t._v("继续拉群")]),t._v(" "),r("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(a){t.exportHandle()}}},[t._v("导出报表")]),t._v(" "),r("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(a){t.reallocateTokenHandle()}}},[t._v("重新分配账号拉群")]),t._v(" "),r("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(a){t.startTaskHandle()}}},[t._v("启动任务")])],1)],1),t._v(" "),r("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[r("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),r("el-table-column",{attrs:{prop:"groupName","header-align":"center",align:"center",label:"群名称"}}),t._v(" "),r("el-table-column",{attrs:{prop:"roomId","header-align":"center",align:"center",label:"群号"}}),t._v(" "),r("el-table-column",{attrs:{prop:"chatRoomUrl","header-align":"center",align:"center",label:"群链接"}}),t._v(" "),r("el-table-column",{attrs:{prop:"roomTicketId","header-align":"center",align:"center",label:"群二维码"}}),t._v(" "),r("el-table-column",{attrs:{prop:"successfullyAttractGroupsNumber","header-align":"center",align:"center",label:"群人数"}}),t._v(" "),r("el-table-column",{attrs:{prop:"groupStatusStr","header-align":"center",align:"center",width:"120",label:"拉群状态"},scopedSlots:t._u([{key:"default",fn:function(a){return[r("el-tag",{attrs:{size:"small",type:"danger"}},[t._v(t._s(a.row.groupStatusStr))])]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",width:"100",label:"时间"}}),t._v(" "),r("el-table-column",{attrs:{prop:"addTotalQuantity","header-align":"center",align:"center",label:"加粉总数"}}),t._v(" "),r("el-table-column",{attrs:{prop:"successfulQuantity","header-align":"center",align:"center",label:"成功数"}}),t._v(" "),r("el-table-column",{attrs:{prop:"failuresQuantity","header-align":"center",align:"center",label:"失败数"}}),t._v(" "),r("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",width:"120",label:"加粉状态"},scopedSlots:t._u([{key:"default",fn:function(a){return[5===a.row.taskStatus?r("el-button",{attrs:{type:"success",plain:""}},[t._v(t._s(a.row.taskStatusStr))]):t._e(),t._v(" "),3===a.row.taskStatus?r("el-button",{attrs:{type:"success",plain:""}},[t._v(t._s(a.row.taskStatusStr))]):t._e(),t._v(" "),2===a.row.taskStatus?r("el-button",{attrs:{type:"warning",plain:""}},[t._v(t._s(a.row.taskStatusStr))]):t._e(),t._v(" "),1===a.row.taskStatus?r("el-button",{attrs:{type:"primary",plain:""}},[t._v(t._s(a.row.taskStatusStr))]):t._e(),t._v(" "),0===a.row.taskStatus?r("el-button",{attrs:{type:"primary",plain:""}},[t._v(t._s(a.row.taskStatusStr))]):t._e()]}}])}),t._v(" "),r("el-table-column",{attrs:{prop:"schedule","header-align":"center",align:"center",width:"220",label:"加粉进度"},scopedSlots:t._u([{key:"default",fn:function(t){return[r("el-progress",{attrs:{"stroke-width":10,type:"circle",percentage:t.row.scheduleFloat}})]}}])}),t._v(" "),r("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(a){return[r("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.errLogsHandle(a.row.id)}}},[t._v("错误日志")])]}}])})],1),t._v(" "),r("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100,2e3],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}})],1)]),t._v(" "),t.addOrUpdateVisible?r("reallocate-token",{ref:"reallocateToken",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.errLogsVisible?r("err-logs",{ref:"errLogs",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]};var i=e("46Yf")(n,l,!1,function(t){e("iEyj")},"data-v-30ace396",null);a.default=i.exports},"1GVP":function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(a){t.visible=a}}},[e("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(a){if(!("button"in a)&&t._k(a.keyCode,"enter",13,a.key))return null;t.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"拉群号国家"}},[e("el-select",{attrs:{placeholder:"拉群号国家",clearable:""},model:{value:t.dataForm.countryCode,callback:function(a){t.$set(t.dataForm,"countryCode",a)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return e("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"拉群号分组",prop:"userGroupId"}},[e("el-select",{attrs:{placeholder:"账户分组"},model:{value:t.dataForm.userGroupId,callback:function(a){t.$set(t.dataForm,"userGroupId",a)},expression:"dataForm.userGroupId"}},t._l(t.dataUserGroupList,function(t){return e("el-option",{key:t.id,attrs:{label:t.name,value:t.id}})}))],1),t._v(" "),e("el-form-item",{attrs:{label:"拉群号数量",prop:"pullGroupNumber"}},[e("el-input",{attrs:{placeholder:"拉群号数量"},model:{value:t.dataForm.pullGroupNumber,callback:function(a){t.$set(t.dataForm,"pullGroupNumber",a)},expression:"dataForm.pullGroupNumber"}})],1)],1),t._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(a){t.visible=!1}}},[t._v("取消")]),t._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(a){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},o=e("46Yf")({data:function(){return{visible:!1,countryCodes:[],dataUserGroupList:[],dataForm:{id:0,countryCode:null,userGroupId:null,pullGroupNumber:2,ids:[],taskName:"",groupType:"",addTotalQuantity:"",successfulQuantity:"",failuresQuantity:"",taskStatus:"",schedule:"",updateTime:"",deleteFlag:"",createTime:"",sysUserId:""},dataRule:{taskName:[{required:!0,message:"任务名称不能为空",trigger:"blur"}],groupType:[{required:!0,message:"类型不能为空",trigger:"blur"}],addTotalQuantity:[{required:!0,message:"加粉总数不能为空",trigger:"blur"}],successfulQuantity:[{required:!0,message:"成功数不能为空",trigger:"blur"}],failuresQuantity:[{required:!0,message:"失败数不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"状态不能为空",trigger:"blur"}],schedule:[{required:!0,message:"进度不能为空",trigger:"blur"}],updateTime:[{required:!0,message:"更新时间不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],sysUserId:[{required:!0,message:"管理账户id不能为空",trigger:"blur"}]}}},methods:{getUserGroupDataList:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/atusergroup/list"),method:"get",params:this.$http.adornParams({page:1,limit:100})}).then(function(a){var e=a.data;e&&0===e.code?t.dataUserGroupList=e.page.list:t.dataUserGroupList=[]})},init:function(t){this.dataForm.ids=t,this.visible=!0,this.getCountryCodeEnums(),this.getUserGroupDataList()},getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(a){var e=a.data;e&&0===e.code?t.countryCodes=e.data:t.$message.error(e.msg)})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(a){a&&t.$http({url:t.$http.adornUrl("/ltt/atgroup/reallocateToken"),method:"post",data:t.$http.adornData({ids:t.dataForm.ids,userGroupId:t.dataForm.userGroupId,pullGroupNumber:t.dataForm.pullGroupNumber,countryCode:t.dataForm.countryCode})}).then(function(a){var e=a.data;e&&0===e.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(e.msg)})})}}},r,!1,null,null,null);a.default=o.exports},FgeY:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={props:{isVisible:Boolean,textContent:String},methods:{closeModal:function(){this.$emit("update:isVisible",!1)}}},o={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return t.isVisible?e("div",{staticClass:"modal-overlay",on:{click:function(a){if(a.target!==a.currentTarget)return null;t.closeModal(a)}}},[e("div",{staticClass:"modal"},[e("div",{staticClass:"modal-header"},[e("h3",[t._v("Text File Content")]),t._v(" "),e("button",{on:{click:t.closeModal}},[t._v("Close")])]),t._v(" "),e("div",{staticClass:"modal-body"},[e("pre",[t._v(t._s(t.textContent))])])])]):t._e()},staticRenderFns:[]};var n=e("46Yf")(r,o,!1,function(t){e("Wxgv")},"data-v-21d7af7c",null);a.default=n.exports},PlrG:function(t,a,e){(t.exports=e("acE3")(!1)).push([t.i,"\n.modal-overlay[data-v-21d7af7c] {\n  position: fixed;\n  top: 0;\n  left: 0;\n  right: 0;\n  bottom: 0;\n  background-color: rgba(0, 0, 0, 0.5);\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n  -webkit-box-align: center;\n      -ms-flex-align: center;\n          align-items: center;\n  -webkit-box-pack: center;\n      -ms-flex-pack: center;\n          justify-content: center;\n}\n.modal[data-v-21d7af7c] {\n  background: white;\n  padding: 20px;\n  border-radius: 5px;\n  width: 50%;\n  max-width: 500px;\n}\n.modal .modal-header[data-v-21d7af7c] {\n    display: -webkit-box;\n    display: -ms-flexbox;\n    display: flex;\n    -webkit-box-pack: justify;\n        -ms-flex-pack: justify;\n            justify-content: space-between;\n    -webkit-box-align: center;\n        -ms-flex-align: center;\n            align-items: center;\n}\n.modal .modal-header h3[data-v-21d7af7c] {\n      margin: 0;\n}\n.modal .modal-header button[data-v-21d7af7c] {\n      border: none;\n      background-color: #f44336;\n      color: white;\n      padding: 5px 10px;\n      border-radius: 3px;\n      cursor: pointer;\n}\n.modal .modal-header button[data-v-21d7af7c]:hover {\n        background-color: #ea1c0d;\n}\n.modal .modal-body[data-v-21d7af7c] {\n    margin-top: 20px;\n}\n.modal .modal-body pre[data-v-21d7af7c] {\n      white-space: pre-wrap;\n      /* CSS 3 */\n      white-space: -moz-pre-wrap;\n      /* Mozilla, since 1999 */\n      white-space: -pre-wrap;\n      /* Opera 4-6 */\n      white-space: -o-pre-wrap;\n      /* Opera 7 */\n      word-wrap: break-word;\n      /* Internet Explorer 5.5+ */\n}\n",""])},Wxgv:function(t,a,e){var r=e("PlrG");"string"==typeof r&&(r=[[t.i,r,""]]),r.locals&&(t.exports=r.locals);e("XkoO")("6e8d033e",r,!0)},hYlD:function(t,a,e){(t.exports=e("acE3")(!1)).push([t.i,"\n.tooltip-content[data-v-30ace396] {\n  max-height: 100px;\n  overflow-y: auto;\n  white-space: normal;\n}\n.mod-config[data-v-30ace396] {\n  width: 100px;\n  display: -webkit-box;\n  display: -ms-flexbox;\n  display: flex;\n}\n.mod-config .el-form[data-v-30ace396] {\n    -webkit-box-flex: 1;\n        -ms-flex: 1;\n            flex: 1;\n    display: -webkit-box;\n    display: -ms-flexbox;\n    display: flex;\n}\n.mod-config .el-form .group-container[data-v-30ace396] {\n      -webkit-box-flex: 1;\n          -ms-flex: 1;\n              flex: 1;\n      display: -webkit-box;\n      display: -ms-flexbox;\n      display: flex;\n      -webkit-box-orient: horizontal;\n      -webkit-box-direction: normal;\n          -ms-flex-direction: row;\n              flex-direction: row;\n}\n.mod-config .el-form .group-container .img-logo[data-v-30ace396] {\n        -webkit-box-flex: 1;\n            -ms-flex: 1;\n                flex: 1;\n}\n.mod-config .el-form .group-container .img-logo .title[data-v-30ace396] {\n          margin: 10px 0px;\n          text-align: center;\n          font-size: 30px;\n          font-weight: bold;\n}\n.mod-config .el-form .group-container .group-content[data-v-30ace396] {\n        -webkit-box-flex: 2;\n            -ms-flex: 2;\n                flex: 2;\n        margin: 0 auto;\n        text-align: center;\n        font-size: 30px;\n        font-weight: bold;\n}\n.mod-config .el-form .group-container .group-content .title[data-v-30ace396] {\n          margin: 20px 0;\n}\n.mod-config .el-form .group-container .money-container[data-v-30ace396] {\n        -webkit-box-flex: 1;\n            -ms-flex: 1;\n                flex: 1;\n}\n",""])},iEyj:function(t,a,e){var r=e("hYlD");"string"==typeof r&&(r=[[t.i,r,""]]),r.locals&&(t.exports=r.locals);e("XkoO")("7ac70ce4",r,!0)},rG05:function(t,a,e){t.exports=e.p+"static/img/250px-Bagua-name-earlier.svg.722a8d1.png"}});