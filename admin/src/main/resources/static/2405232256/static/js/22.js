webpackJsonp([22,95,96],{gBTk:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{groupType:null,options:[],visible:!1,dataForm:{id:0,name:"",groupType:"",deleteFlag:"",createTime:""},dataRule:{name:[{required:!0,message:"分组名称不能为空",trigger:"blur"}],groupType:[{required:!0,message:"分组类型不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{getGroupType:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getGroupType"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.options=a.data:t.$message.error(a.msg)})},init:function(t){var e=this;this.dataForm.id=t||0,this.visible=!0,this.getGroupType(),this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.id&&e.$http({url:e.$http.adornUrl("/ltt/atdatagroup/info/"+e.dataForm.id),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.name=a.atDataGroup.name,e.dataForm.groupType=a.atDataGroup.groupType,e.dataForm.deleteFlag=a.atDataGroup.deleteFlag,e.dataForm.createTime=a.atDataGroup.createTime)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/atdatagroup/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,name:t.dataForm.name,groupType:t.groupType,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},i={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"分组名称",prop:"name"}},[a("el-input",{attrs:{placeholder:"分组名称"},model:{value:t.dataForm.name,callback:function(e){t.$set(t.dataForm,"name",e)},expression:"dataForm.name"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"分组类型"}},[a("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{placeholder:"选择类型",size:"large"},model:{value:t.groupType,callback:function(e){t.groupType=e},expression:"groupType"}},t._l(t.options,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},o=a("46Yf")(r,i,!1,null,null,null);e.default=o.exports},sbT4:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{groupType:null,uploadUrl:"",options:[],visible:!1,dataForm:{id:0,name:"",txtUrl:"",groupType:"",deleteFlag:"",createTime:""},dataRule:{name:[{required:!0,message:"分组名称不能为空",trigger:"blur"}],groupType:[{required:!0,message:"分组类型不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{handleAvatarSuccess:function(t,e){this.dataForm.txtUrl=t.data},init:function(t){var e=this;this.getGroupType(),this.dataForm.id=t||0,this.uploadUrl=this.$http.adornUrl("/app/file/upload"),this.visible=!0,null!=this.$refs.upload&&this.$refs.upload.clearFiles(),this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.id&&e.$http({url:e.$http.adornUrl("/ltt/atdatagroup/info/"+e.dataForm.id),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.name=a.atDataGroup.name,e.dataForm.groupType=a.atDataGroup.groupType,e.dataForm.deleteFlag=a.atDataGroup.deleteFlag,e.dataForm.createTime=a.atDataGroup.createTime,e.groupType=e.dataForm.groupType)})})},getGroupType:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getGroupType"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.options=a.data:t.$message.error(a.msg)})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/atdatagroup/updateBatchGroup"),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,name:t.dataForm.name,txtUrl:t.dataForm.txtUrl,groupType:t.groupType,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},i={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"分组名称",prop:"name"}},[a("el-input",{attrs:{placeholder:"分组名称",disabled:""},model:{value:t.dataForm.name,callback:function(e){t.$set(t.dataForm,"name",e)},expression:"dataForm.name"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"分组类型"}},[a("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{disabled:"",placeholder:"Select",size:"large"},model:{value:t.groupType,callback:function(e){t.groupType=e},expression:"groupType"}},t._l(t.options,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"数据上传",prop:"groupName"}},[a("el-upload",{ref:"upload",staticClass:"upload-demo",attrs:{action:t.uploadUrl,"on-success":t.handleAvatarSuccess}},[a("el-button",{attrs:{size:"small",type:"primary"}},[t._v("点击上传")])],1)],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},o=a("46Yf")(r,i,!1,null,null,null);e.default=o.exports},zyYb:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r=a("gBTk"),i={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,dataUploadVisible:!1}},components:{DataUpload:a("sbT4").default,AddOrUpdate:r.default},activated:function(){this.getDataList()},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atdatagroup/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var e=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(t)})},dataUploadHandle:function(t){var e=this;this.dataUploadVisible=!0,this.$nextTick(function(){e.$refs.dataUpload.init(t)})},deleteHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"删除，分组下数据也会被相应删除":"批量删除，分组下数据也会被相应删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atdatagroup/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})}}},o={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:t.dataForm.key,callback:function(e){t.$set(t.dataForm,"key",e)},expression:"dataForm.key"}})],1),t._v(" "),a("el-form-item",[a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")]),t._v(" "),t.isAuth("ltt:atdatagroup:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.addOrUpdateHandle()}}},[t._v("新增")]):t._e(),t._v(" "),t.isAuth("ltt:atdatagroup:delete")?a("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(e){t.deleteHandle()}}},[t._v("批量删除")]):t._e()],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),a("el-table-column",{attrs:{prop:"name","header-align":"center",align:"center",label:"分组名称"}}),t._v(" "),a("el-table-column",{attrs:{prop:"groupTypeStr","header-align":"center",align:"center",label:"分组类型"}}),t._v(" "),a("el-table-column",{attrs:{prop:"dataGroupIdCount","header-align":"center",align:"center",label:"数据剩余数量"}}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),t._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.dataUploadHandle(e.row.id)}}},[t._v("数据上传")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.addOrUpdateHandle(e.row.id)}}},[t._v("修改")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.deleteHandle(e.row.id)}}},[t._v("删除")])]}}])})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e(),t._v(" "),t.dataUploadVisible?a("data-upload",{ref:"dataUpload",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},n=a("46Yf")(i,o,!1,null,null,null);e.default=n.exports}});