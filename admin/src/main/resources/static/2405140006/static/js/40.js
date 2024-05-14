webpackJsonp([40,78],{"/TpL":function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,usernameTaskId:"",userGroupId:"",usernameGroupId:"",userId:"",usernameId:"",taskStatus:"",deleteFlag:"",createTime:"",lineTaskId:"",msg:""},dataRule:{usernameTaskId:[{required:!0,message:"头像任务id不能为空",trigger:"blur"}],userGroupId:[{required:!0,message:"账户分组不能为空",trigger:"blur"}],usernameGroupId:[{required:!0,message:"昵称分组不能为空",trigger:"blur"}],userId:[{required:!0,message:"账户id不能为空",trigger:"blur"}],usernameId:[{required:!0,message:"昵称id不能为空",trigger:"blur"}],taskStatus:[{required:!0,message:"任务状态不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],lineTaskId:[{required:!0,message:"line协议的任务id不能为空",trigger:"blur"}],msg:[{required:!0,message:"line的协议返回信息不能为空",trigger:"blur"}]}}},methods:{init:function(e){var a=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){a.$refs.dataForm.resetFields(),a.dataForm.id&&a.$http({url:a.$http.adornUrl("/ltt/atusernamesubtask/info/"+a.dataForm.id),method:"get",params:a.$http.adornParams()}).then(function(e){var t=e.data;t&&0===t.code&&(a.dataForm.usernameTaskId=t.atusernamesubtask.usernameTaskId,a.dataForm.userGroupId=t.atusernamesubtask.userGroupId,a.dataForm.usernameGroupId=t.atusernamesubtask.usernameGroupId,a.dataForm.userId=t.atusernamesubtask.userId,a.dataForm.usernameId=t.atusernamesubtask.usernameId,a.dataForm.taskStatus=t.atusernamesubtask.taskStatus,a.dataForm.deleteFlag=t.atusernamesubtask.deleteFlag,a.dataForm.createTime=t.atusernamesubtask.createTime,a.dataForm.lineTaskId=t.atusernamesubtask.lineTaskId,a.dataForm.msg=t.atusernamesubtask.msg)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(a){a&&e.$http({url:e.$http.adornUrl("/ltt/atusernamesubtask/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,usernameTaskId:e.dataForm.usernameTaskId,userGroupId:e.dataForm.userGroupId,usernameGroupId:e.dataForm.usernameGroupId,userId:e.dataForm.userId,usernameId:e.dataForm.usernameId,taskStatus:e.dataForm.taskStatus,deleteFlag:e.dataForm.deleteFlag,createTime:e.dataForm.createTime,lineTaskId:e.dataForm.lineTaskId,msg:e.dataForm.msg})}).then(function(a){var t=a.data;t&&0===t.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(t.msg)})})}}},s={render:function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(a){e.visible=a}}},[t("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(a){if(!("button"in a)&&e._k(a.keyCode,"enter",13,a.key))return null;e.dataFormSubmit()}}},[t("el-form-item",{attrs:{label:"头像任务id",prop:"usernameTaskId"}},[t("el-input",{attrs:{placeholder:"头像任务id"},model:{value:e.dataForm.usernameTaskId,callback:function(a){e.$set(e.dataForm,"usernameTaskId",a)},expression:"dataForm.usernameTaskId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"账户分组",prop:"userGroupId"}},[t("el-input",{attrs:{placeholder:"账户分组"},model:{value:e.dataForm.userGroupId,callback:function(a){e.$set(e.dataForm,"userGroupId",a)},expression:"dataForm.userGroupId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"昵称分组",prop:"usernameGroupId"}},[t("el-input",{attrs:{placeholder:"昵称分组"},model:{value:e.dataForm.usernameGroupId,callback:function(a){e.$set(e.dataForm,"usernameGroupId",a)},expression:"dataForm.usernameGroupId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"账户id",prop:"userId"}},[t("el-input",{attrs:{placeholder:"账户id"},model:{value:e.dataForm.userId,callback:function(a){e.$set(e.dataForm,"userId",a)},expression:"dataForm.userId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"昵称id",prop:"usernameId"}},[t("el-input",{attrs:{placeholder:"昵称id"},model:{value:e.dataForm.usernameId,callback:function(a){e.$set(e.dataForm,"usernameId",a)},expression:"dataForm.usernameId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"任务状态",prop:"taskStatus"}},[t("el-input",{attrs:{placeholder:"任务状态"},model:{value:e.dataForm.taskStatus,callback:function(a){e.$set(e.dataForm,"taskStatus",a)},expression:"dataForm.taskStatus"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"删除标志",prop:"deleteFlag"}},[t("el-input",{attrs:{placeholder:"删除标志"},model:{value:e.dataForm.deleteFlag,callback:function(a){e.$set(e.dataForm,"deleteFlag",a)},expression:"dataForm.deleteFlag"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[t("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(a){e.$set(e.dataForm,"createTime",a)},expression:"dataForm.createTime"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"line协议的任务id",prop:"lineTaskId"}},[t("el-input",{attrs:{placeholder:"line协议的任务id"},model:{value:e.dataForm.lineTaskId,callback:function(a){e.$set(e.dataForm,"lineTaskId",a)},expression:"dataForm.lineTaskId"}})],1),e._v(" "),t("el-form-item",{attrs:{label:"line的协议返回信息",prop:"msg"}},[t("el-input",{attrs:{placeholder:"line的协议返回信息"},model:{value:e.dataForm.msg,callback:function(a){e.$set(e.dataForm,"msg",a)},expression:"dataForm.msg"}})],1)],1),e._v(" "),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(a){e.visible=!1}}},[e._v("取消")]),e._v(" "),t("el-button",{attrs:{type:"primary"},on:{click:function(a){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},n=t("46Yf")(r,s,!1,null,null,null);a.default=n.exports},JmDB:function(e,a,t){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:t("/TpL").default},activated:function(){this.getDataList()},methods:{getDataList:function(){var e=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atusernamesubtask/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(a){var t=a.data;t&&0===t.code?(e.dataList=t.page.list,e.totalPage=t.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},addOrUpdateHandle:function(e){var a=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){a.$refs.addOrUpdate.init(e)})},deleteHandle:function(e){var a=this,t=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对[id="+t.join(",")+"]进行["+(e?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){a.$http({url:a.$http.adornUrl("/ltt/atusernamesubtask/delete"),method:"post",data:a.$http.adornData(t,!1)}).then(function(e){var t=e.data;t&&0===t.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.getDataList()}}):a.$message.error(t.msg)})})}}},s={render:function(){var e=this,a=e.$createElement,t=e._self._c||a;return t("div",{staticClass:"mod-config"},[t("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(a){if(!("button"in a)&&e._k(a.keyCode,"enter",13,a.key))return null;e.getDataList()}}},[t("el-form-item",[t("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:e.dataForm.key,callback:function(a){e.$set(e.dataForm,"key",a)},expression:"dataForm.key"}})],1),e._v(" "),t("el-form-item",[t("el-button",{on:{click:function(a){e.getDataList()}}},[e._v("查询")]),e._v(" "),e.isAuth("ltt:atusernamesubtask:save")?t("el-button",{attrs:{type:"primary"},on:{click:function(a){e.addOrUpdateHandle()}}},[e._v("新增")]):e._e(),e._v(" "),e.isAuth("ltt:atusernamesubtask:delete")?t("el-button",{attrs:{type:"danger",disabled:e.dataListSelections.length<=0},on:{click:function(a){e.deleteHandle()}}},[e._v("批量删除")]):e._e()],1)],1),e._v(" "),t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[t("el-table-column",{attrs:{prop:"usernameTaskId","header-align":"center",align:"center",label:"头像任务id"}}),e._v(" "),t("el-table-column",{attrs:{prop:"userGroupId","header-align":"center",align:"center",label:"账户分组"}}),e._v(" "),t("el-table-column",{attrs:{prop:"usernameGroupId","header-align":"center",align:"center",label:"昵称分组"}}),e._v(" "),t("el-table-column",{attrs:{prop:"userId","header-align":"center",align:"center",label:"账户id"}}),e._v(" "),t("el-table-column",{attrs:{prop:"usernameId","header-align":"center",align:"center",label:"昵称id"}}),e._v(" "),t("el-table-column",{attrs:{prop:"taskStatus","header-align":"center",align:"center",label:"任务状态"}}),e._v(" "),t("el-table-column",{attrs:{prop:"deleteFlag","header-align":"center",align:"center",label:"删除标志"}}),e._v(" "),t("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),e._v(" "),t("el-table-column",{attrs:{prop:"lineTaskId","header-align":"center",align:"center",label:"line协议的任务id"}}),e._v(" "),t("el-table-column",{attrs:{prop:"msg","header-align":"center",align:"center",label:"line的协议返回信息"}}),e._v(" "),t("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-button",{attrs:{type:"text",size:"small"},on:{click:function(t){e.addOrUpdateHandle(a.row.id)}}},[e._v("修改")]),e._v(" "),t("el-button",{attrs:{type:"text",size:"small"},on:{click:function(t){e.deleteHandle(a.row.id)}}},[e._v("删除")])]}}])})],1),e._v(" "),t("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?t("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]},n=t("46Yf")(r,s,!1,null,null,null);a.default=n.exports}});