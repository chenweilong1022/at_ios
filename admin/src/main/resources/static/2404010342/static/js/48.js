webpackJsonp([48,93],{"6alu":function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:e("fU8o").default},activated:function(){this.getDataList()},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atavatar/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(a){var e=a.data;e&&0===e.code?(t.dataList=e.page.list,t.totalPage=e.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var a=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){a.$refs.addOrUpdate.init(t)})},deleteHandle:function(t){var a=this,e=t?[t]:this.dataListSelections.map(function(t){return t.id});this.$confirm("确定对[id="+e.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){a.$http({url:a.$http.adornUrl("/ltt/atavatar/delete"),method:"post",data:a.$http.adornData(e,!1)}).then(function(t){var e=t.data;e&&0===e.code?a.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){a.getDataList()}}):a.$message.error(e.msg)})})}}},i={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"mod-config"},[e("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(a){if(!("button"in a)&&t._k(a.keyCode,"enter",13,a.key))return null;t.getDataList()}}},[e("el-form-item",[e("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:t.dataForm.key,callback:function(a){t.$set(t.dataForm,"key",a)},expression:"dataForm.key"}})],1),t._v(" "),e("el-form-item",[e("el-button",{on:{click:function(a){t.getDataList()}}},[t._v("查询")]),t._v(" "),t.isAuth("ltt:atavatar:save")?e("el-button",{attrs:{type:"primary"},on:{click:function(a){t.addOrUpdateHandle()}}},[t._v("新增")]):t._e(),t._v(" "),t.isAuth("ltt:atavatar:delete")?e("el-button",{attrs:{type:"danger",disabled:t.dataListSelections.length<=0},on:{click:function(a){t.deleteHandle()}}},[t._v("批量删除")]):t._e()],1)],1),t._v(" "),e("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[e("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),t._v(" "),e("el-table-column",{attrs:{prop:"avatar","header-align":"center",align:"center",label:"头像"},scopedSlots:t._u([{key:"default",fn:function(t){return[e("img",{staticStyle:{width:"40px",height:"40px"},attrs:{src:t.row.avatar}})]}}])}),t._v(" "),e("el-table-column",{attrs:{prop:"useFlag","header-align":"center",align:"center",label:"使用标识"},scopedSlots:t._u([{key:"default",fn:function(a){return[0===a.row.useFlag?e("el-tag",{attrs:{size:"small"}},[t._v("已使用")]):e("el-tag",{attrs:{size:"small",type:"danger"}},[t._v("未使用")])]}}])}),t._v(" "),e("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),t._v(" "),e("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(a){return[e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.addOrUpdateHandle(a.row.id)}}},[t._v("修改")]),t._v(" "),e("el-button",{attrs:{type:"text",size:"small"},on:{click:function(e){t.deleteHandle(a.row.id)}}},[t._v("删除")])]}}])})],1),t._v(" "),e("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?e("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},n=e("46Yf")(r,i,!1,null,null,null);a.default=n.exports},fU8o:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,avatarGroupId:"",avatar:"",useFlag:"",deleteFlag:"",createTime:""},dataRule:{avatarGroupId:[{required:!0,message:"头像分组id不能为空",trigger:"blur"}],avatar:[{required:!0,message:"头像不能为空",trigger:"blur"}],useFlag:[{required:!0,message:"使用标识不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}]}}},methods:{init:function(t){var a=this;this.dataForm.id=t||0,this.visible=!0,this.$nextTick(function(){a.$refs.dataForm.resetFields(),a.dataForm.id&&a.$http({url:a.$http.adornUrl("/ltt/atavatar/info/"+a.dataForm.id),method:"get",params:a.$http.adornParams()}).then(function(t){var e=t.data;e&&0===e.code&&(a.dataForm.avatarGroupId=e.atAvatar.avatarGroupId,a.dataForm.avatar=e.atAvatar.avatar,a.dataForm.useFlag=e.atAvatar.useFlag,a.dataForm.deleteFlag=e.atAvatar.deleteFlag,a.dataForm.createTime=e.atAvatar.createTime)})})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(a){a&&t.$http({url:t.$http.adornUrl("/ltt/atavatar/"+(t.dataForm.id?"update":"save")),method:"post",data:t.$http.adornData({id:t.dataForm.id||void 0,avatarGroupId:t.dataForm.avatarGroupId,avatar:t.dataForm.avatar,useFlag:t.dataForm.useFlag,deleteFlag:t.dataForm.deleteFlag,createTime:t.dataForm.createTime})}).then(function(a){var e=a.data;e&&0===e.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(e.msg)})})}}},i={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("el-dialog",{attrs:{title:t.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(a){t.visible=a}}},[e("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(a){if(!("button"in a)&&t._k(a.keyCode,"enter",13,a.key))return null;t.dataFormSubmit()}}},[e("el-form-item",{attrs:{label:"头像分组id",prop:"avatarGroupId"}},[e("el-input",{attrs:{placeholder:"头像分组id"},model:{value:t.dataForm.avatarGroupId,callback:function(a){t.$set(t.dataForm,"avatarGroupId",a)},expression:"dataForm.avatarGroupId"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"头像",prop:"avatar"}},[e("el-input",{attrs:{placeholder:"头像"},model:{value:t.dataForm.avatar,callback:function(a){t.$set(t.dataForm,"avatar",a)},expression:"dataForm.avatar"}})],1),t._v(" "),e("el-form-item",{attrs:{label:"使用标识",prop:"useFlag"}},[e("el-input",{attrs:{placeholder:"使用标识"},model:{value:t.dataForm.useFlag,callback:function(a){t.$set(t.dataForm,"useFlag",a)},expression:"dataForm.useFlag"}})],1)],1),t._v(" "),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(a){t.visible=!1}}},[t._v("取消")]),t._v(" "),e("el-button",{attrs:{type:"primary"},on:{click:function(a){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},n=e("46Yf")(r,i,!1,null,null,null);a.default=n.exports}});