webpackJsonp([23,98,99],{Cvvx:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:"充值","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"所属账号",prop:"sysUserId"}},[a("el-select",{attrs:{filterable:"",remote:"",disabled:t.userDisabled,placeholder:"请选择账号","remote-method":t.queryBySearchWord,loading:t.loading},model:{value:t.dataForm.sysUserId,callback:function(e){t.$set(t.dataForm,"sysUserId",e)},expression:"dataForm.sysUserId"}},t._l(t.sysUserAccountOptions,function(t){return a("el-option",{key:t.userId,attrs:{label:t.username,value:t.userId}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"充值金额",prop:"amount"}},[a("el-input",{attrs:{placeholder:"充值金额"},model:{value:t.dataForm.amount,callback:function(e){t.$set(t.dataForm,"amount",e)},expression:"dataForm.amount"}})],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},s=a("46Yf")({data:function(){return{visible:!1,userDisabled:!1,sysUserAccountOptions:[],dataForm:{sysUserId:"",amount:""},dataRule:{sysUserId:[{required:!0,message:"所属账户不能为空",trigger:"blur"}],amount:[{required:!0,message:"充值金额",trigger:"blur"}]}}},methods:{init:function(t,e){var a=this;this.dataForm.sysUserId=null,this.visible=!0,this.$nextTick(function(){a.$refs.dataForm.resetFields(),t?(a.dataForm.sysUserId=t,a.userDisabled=!0,a.sysUserAccountOptions=[{userId:t,username:e}]):a.queryBySearchWord()})},queryBySearchWord:function(t){var e=this;t=null==t?"":t+"",this.$http({url:this.$http.adornUrl("/sys/user/queryBySearchWord?searchWord="+t),method:"get",params:this.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.sysUserAccountOptions=a.userList)})},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/accountbalance/changeAccount"),method:"post",data:t.$http.adornData({sysUserId:t.dataForm.sysUserId,amount:t.dataForm.amount,transactionType:1,description:"账户充值"})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})}}},n,!1,null,null,null);e.default=s.exports},P0bW:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={data:function(){return{dataForm:{key:"",sysUserId:null,transactionType:null,status:null},dataList:[],accountTransactionTypeCodes:[],accountTransactionStatusCodes:[],sysUserAccountOptions:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:a("eaqw").default},activated:function(){this.init(),this.getDataList(),this.getAccountTransactionTypeCodes(),this.getAccountTransactionStatusCodes(),this.queryBySearchWord()},methods:{init:function(){this.dataForm.sysUserId=this.$route.query.sysUserId,this.dataForm.sysUsername=this.$route.query.sysUsername,this.dataForm.sysUserId&&(this.sysUserAccountOptions=[{userId:this.dataForm.sysUserId,username:this.dataForm.sysUsername}])},getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/accountdetails/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,sysUserId:this.dataForm.sysUserId,transactionType:this.dataForm.transactionType,status:this.dataForm.status})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},getAccountTransactionTypeCodes:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/accountTransactionTypeCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.accountTransactionTypeCodes=a.data:t.$message.error(a.msg)})},getAccountTransactionStatusCodes:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/accountTransactionStatusCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.accountTransactionStatusCodes=a.data:t.$message.error(a.msg)})},queryBySearchWord:function(t){var e=this;t=null==t?"":t+"",this.$http({url:this.$http.adornUrl("/sys/user/queryBySearchWord?searchWord="+t),method:"get",params:this.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.sysUserAccountOptions=a.userList)})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var e=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(t)})},deleteHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.transactionId});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/accountdetails/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})}}},s={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",{attrs:{label:"所属账号:",prop:"sysUserId"}},[a("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"请选择账号","remote-method":t.queryBySearchWord,loading:t.loading},model:{value:t.dataForm.sysUserId,callback:function(e){t.$set(t.dataForm,"sysUserId",e)},expression:"dataForm.sysUserId"}},t._l(t.sysUserAccountOptions,function(t){return a("el-option",{key:t.userId,attrs:{label:t.username,value:t.userId}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"交易类型:",prop:"sysUserId"}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择交易类型"},model:{value:t.dataForm.transactionType,callback:function(e){t.$set(t.dataForm,"transactionType",e)},expression:"dataForm.transactionType"}},t._l(t.accountTransactionTypeCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"交易状态:",prop:"sysUserId"}},[a("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择交易状态"},model:{value:t.dataForm.status,callback:function(e){t.$set(t.dataForm,"status",e)},expression:"dataForm.status"}},t._l(t.accountTransactionStatusCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",[a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")])],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{prop:"sysUsername","header-align":"center",align:"center",label:"账户名"}}),t._v(" "),a("el-table-column",{attrs:{prop:"transactionType","header-align":"center",align:"center",label:"交易类型"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(t.accountTransactionTypeCodes,function(n){return e.row.transactionType===n.key?a("el-tag",{key:n.key},[t._v("\n            "+t._s(n.value)+"\n          ")]):t._e()})}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"amount","header-align":"center",align:"center",label:"交易金额"}}),t._v(" "),a("el-table-column",{attrs:{prop:"beforeAmount","header-align":"center",align:"center",label:"变更前余额"}}),t._v(" "),a("el-table-column",{attrs:{prop:"afterAmount","header-align":"center",align:"center",label:"变动后余额"}}),t._v(" "),a("el-table-column",{attrs:{prop:"status","header-align":"center",align:"center",label:"交易状态"},scopedSlots:t._u([{key:"default",fn:function(e){return[1===e.row.status?a("el-tag",{attrs:{size:"small",type:"success"}},[t._v("成功")]):a("el-tag",{attrs:{size:"small",type:"danger"}},[t._v("失败")])]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"description","header-align":"center",align:"center",label:"交易描述"}}),t._v(" "),a("el-table-column",{attrs:{prop:"transactionDate","header-align":"center",align:"center",label:"交易时间"}})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},r=a("46Yf")(n,s,!1,null,null,null);e.default=r.exports},mRqz:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("Cvvx"),s=a("P0bW"),r={data:function(){return{dataForm:{key:""},dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],rechargeVisible:!1,accountDetailVisible:!1}},components:{Recharge:n.default,AccountDetail:s.default},activated:function(){this.getDataList()},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/accountbalance/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,key:this.dataForm.key})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},rechargeHandle:function(t,e){var a=this;this.rechargeVisible=!0,this.$nextTick(function(){a.$refs.recharge.init(t,e)})},accountDetailHandle:function(t,e){var a=this;this.accountDetailVisible=!0,this.$nextTick(function(){a.$router.push({name:"ltt-accountdetails",query:{sysUserId:t,sysUsername:e}})})},deleteHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.accountId});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/accountbalance/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})}}},i={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"参数名",clearable:""},model:{value:t.dataForm.key,callback:function(e){t.$set(t.dataForm,"key",e)},expression:"dataForm.key"}})],1),t._v(" "),a("el-form-item",[a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")]),t._v(" "),t.isAuth("ltt:accountbalance:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.rechargeHandle()}}},[t._v("充值")]):t._e()],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{prop:"sysUserId","header-align":"center",align:"center",label:"用户ID"}}),t._v(" "),a("el-table-column",{attrs:{prop:"sysUsername","header-align":"center",align:"center",label:"用户名"}}),t._v(" "),a("el-table-column",{attrs:{prop:"balance","header-align":"center",align:"center",label:"可用余额"}}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),t._v(" "),a("el-table-column",{attrs:{prop:"updateTime","header-align":"center",align:"center",label:"最后更新时间"}}),t._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.rechargeHandle(e.row.sysUserId,e.row.sysUsername)}}},[t._v("充值")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.accountDetailHandle(e.row.sysUserId,e.row.sysUsername)}}},[t._v("流水明细")])]}}])})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.rechargeVisible?a("recharge",{ref:"recharge",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},o=a("46Yf")(r,i,!1,null,null,null);e.default=o.exports}});