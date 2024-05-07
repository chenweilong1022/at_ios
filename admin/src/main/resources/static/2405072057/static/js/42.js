webpackJsonp([42,82],{Bq9D:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={data:function(){return{visible:!1,dataForm:{orderId:0,sysUserId:"",orderStatus:"",totalAmount:"",orderTime:"",updateTime:"",notes:"",productId:"",productType:"",countryCode:"",orderNumber:""},dataRule:{sysUserId:[{required:!0,message:"用户ID不能为空",trigger:"blur"}],orderStatus:[{required:!0,message:"订单状态（待处理，处理中，已完成）不能为空",trigger:"blur"}],totalAmount:[{required:!0,message:"订单总金额不能为空",trigger:"blur"}],orderTime:[{required:!0,message:"订单创建时间不能为空",trigger:"blur"}],updateTime:[{required:!0,message:"订单最后更新时间不能为空",trigger:"blur"}],notes:[{required:!0,message:"订单备注不能为空",trigger:"blur"}],productId:[{required:!0,message:"商品id不能为空",trigger:"blur"}],productType:[{required:!0,message:"商品类型不能为空",trigger:"blur"}],countryCode:[{required:!0,message:"国家code不能为空",trigger:"blur"}],orderNumber:[{required:!0,message:"购买数量不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.orderId=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.orderId&&t.$http({url:t.$http.adornUrl("/ltt/atorders/info/"+t.dataForm.orderId),method:"get",params:t.$http.adornParams()}).then(function(e){var r=e.data;r&&0===r.code&&(t.dataForm.sysUserId=r.atorders.sysUserId,t.dataForm.orderStatus=r.atorders.orderStatus,t.dataForm.totalAmount=r.atorders.totalAmount,t.dataForm.orderTime=r.atorders.orderTime,t.dataForm.updateTime=r.atorders.updateTime,t.dataForm.notes=r.atorders.notes,t.dataForm.productId=r.atorders.productId,t.dataForm.productType=r.atorders.productType,t.dataForm.countryCode=r.atorders.countryCode,t.dataForm.orderNumber=r.atorders.orderNumber)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atorders/"+(e.dataForm.orderId?"update":"save")),method:"post",data:e.$http.adornData({orderId:e.dataForm.orderId||void 0,sysUserId:e.dataForm.sysUserId,orderStatus:e.dataForm.orderStatus,totalAmount:e.dataForm.totalAmount,orderTime:e.dataForm.orderTime,updateTime:e.dataForm.updateTime,notes:e.dataForm.notes,productId:e.dataForm.productId,productType:e.dataForm.productType,countryCode:e.dataForm.countryCode,orderNumber:e.dataForm.orderNumber})}).then(function(t){var r=t.data;r&&0===r.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(r.msg)})})}}},o={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[r("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[r("el-form-item",{attrs:{label:"用户ID",prop:"sysUserId"}},[r("el-input",{attrs:{placeholder:"用户ID"},model:{value:e.dataForm.sysUserId,callback:function(t){e.$set(e.dataForm,"sysUserId",t)},expression:"dataForm.sysUserId"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"订单状态（待处理，处理中，已完成）",prop:"orderStatus"}},[r("el-input",{attrs:{placeholder:"订单状态（待处理，处理中，已完成）"},model:{value:e.dataForm.orderStatus,callback:function(t){e.$set(e.dataForm,"orderStatus",t)},expression:"dataForm.orderStatus"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"订单总金额",prop:"totalAmount"}},[r("el-input",{attrs:{placeholder:"订单总金额"},model:{value:e.dataForm.totalAmount,callback:function(t){e.$set(e.dataForm,"totalAmount",t)},expression:"dataForm.totalAmount"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"订单创建时间",prop:"orderTime"}},[r("el-input",{attrs:{placeholder:"订单创建时间"},model:{value:e.dataForm.orderTime,callback:function(t){e.$set(e.dataForm,"orderTime",t)},expression:"dataForm.orderTime"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"订单最后更新时间",prop:"updateTime"}},[r("el-input",{attrs:{placeholder:"订单最后更新时间"},model:{value:e.dataForm.updateTime,callback:function(t){e.$set(e.dataForm,"updateTime",t)},expression:"dataForm.updateTime"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"订单备注",prop:"notes"}},[r("el-input",{attrs:{placeholder:"订单备注"},model:{value:e.dataForm.notes,callback:function(t){e.$set(e.dataForm,"notes",t)},expression:"dataForm.notes"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"商品id",prop:"productId"}},[r("el-input",{attrs:{placeholder:"商品id"},model:{value:e.dataForm.productId,callback:function(t){e.$set(e.dataForm,"productId",t)},expression:"dataForm.productId"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"商品类型",prop:"productType"}},[r("el-input",{attrs:{placeholder:"商品类型"},model:{value:e.dataForm.productType,callback:function(t){e.$set(e.dataForm,"productType",t)},expression:"dataForm.productType"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"国家code",prop:"countryCode"}},[r("el-input",{attrs:{placeholder:"国家code"},model:{value:e.dataForm.countryCode,callback:function(t){e.$set(e.dataForm,"countryCode",t)},expression:"dataForm.countryCode"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"购买数量",prop:"orderNumber"}},[r("el-input",{attrs:{placeholder:"购买数量"},model:{value:e.dataForm.orderNumber,callback:function(t){e.$set(e.dataForm,"orderNumber",t)},expression:"dataForm.orderNumber"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"成功",prop:"successNumber"}},[r("el-input",{attrs:{placeholder:"购买数量"},model:{value:e.dataForm.successNumber,callback:function(t){e.$set(e.dataForm,"successNumber",t)},expression:"dataForm.successNumber"}})],1)],1),e._v(" "),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},d=r("46Yf")(a,o,!1,null,null,null);t.default=d.exports},KbV3:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={data:function(){return{dataForm:{sysUserId:null,countryCode:null,productType:null,orderStatus:null},sysUserAccountOptions:[],countryCodes:[],orderStatusCodes:[],dataList:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:r("Bq9D").default},activated:function(){this.getDataList(),this.getCountryCodes(),this.getProductTypeCodes(),this.getOrderStatus(),this.queryBySearchWord()},methods:{getDataList:function(){var e=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/atorders/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,sysUserId:this.dataForm.sysUserId,countryCode:this.dataForm.countryCode,productType:this.dataForm.productType,orderStatus:this.dataForm.orderStatus})}).then(function(t){var r=t.data;r&&0===r.code?(e.dataList=r.page.list,e.totalPage=r.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},addOrUpdateHandle:function(e){var t=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){t.$refs.addOrUpdate.init(e)})},deleteHandle:function(e){var t=this,r=e?[e]:this.dataListSelections.map(function(e){return e.orderId});this.$confirm("确定对[id="+r.join(",")+"]进行["+(e?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/atorders/delete"),method:"post",data:t.$http.adornData(r,!1)}).then(function(e){var r=e.data;r&&0===r.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(r.msg)})})},queryBySearchWord:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/sys/user/queryBySearchWord?searchWord="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var r=e.data;r&&0===r.code&&(t.sysUserAccountOptions=r.userList)})},getCountryCodes:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(t){var r=t.data;r&&0===r.code?e.countryCodes=r.data:e.$message.error(r.msg)})},getProductTypeCodes:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/getProductTypeCodes"),method:"get"}).then(function(t){var r=t.data;r&&0===r.code?e.productTypeCodes=r.data:e.$message.error(r.msg)})},getOrderStatus:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/getOrderStatus"),method:"get"}).then(function(t){var r=t.data;r&&0===r.code?e.orderStatusCodes=r.data:e.$message.error(r.msg)})}}},o={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"mod-config"},[r("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.getDataList()}}},[r("el-form-item",{attrs:{label:"所属账号",prop:"sysUserId"}},[r("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"请选择账号","remote-method":e.queryBySearchWord,loading:e.loading},model:{value:e.dataForm.sysUserId,callback:function(t){e.$set(e.dataForm,"sysUserId",t)},expression:"dataForm.sysUserId"}},e._l(e.sysUserAccountOptions,function(e){return r("el-option",{key:e.userId,attrs:{label:e.username,value:e.userId}})}))],1),e._v(" "),r("el-form-item",{attrs:{label:"国号(区号)",prop:"countryCode"}},[r("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择国家"},model:{value:e.dataForm.countryCode,callback:function(t){e.$set(e.dataForm,"countryCode",t)},expression:"dataForm.countryCode"}},e._l(e.countryCodes,function(e){return r("el-option",{key:e.value,attrs:{label:e.value,value:e.value}})}))],1),e._v(" "),r("el-form-item",{attrs:{label:"商品类型",prop:"productType"}},[r("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择商品类型"},model:{value:e.dataForm.productType,callback:function(t){e.$set(e.dataForm,"productType",t)},expression:"dataForm.productType"}},e._l(e.productTypeCodes,function(e){return r("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),r("el-form-item",{attrs:{label:"订单状态",prop:"orderStatus"}},[r("el-select",{attrs:{filterable:"",clearable:"",placeholder:"请选择订单状态"},model:{value:e.dataForm.orderStatus,callback:function(t){e.$set(e.dataForm,"orderStatus",t)},expression:"dataForm.orderStatus"}},e._l(e.orderStatusCodes,function(e){return r("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),r("el-form-item",[r("el-button",{on:{click:function(t){e.getDataList()}}},[e._v("查询")])],1)],1),e._v(" "),r("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[r("el-table-column",{attrs:{prop:"sysUsername","header-align":"center",align:"center",label:"账户名称"}}),e._v(" "),r("el-table-column",{attrs:{prop:"orderStatus","header-align":"center",align:"center",label:"订单状态"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.orderStatusCodes,function(a){return t.row.orderStatus===a.key?r("el-tag",{key:a.key},[e._v("\n            "+e._s(a.value)+"\n          ")]):e._e()})}}])}),e._v(" "),r("el-table-column",{attrs:{prop:"countryCode","header-align":"center",align:"center",label:"国家code"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.countryCodes,function(a){return t.row.countryCode===a.value?r("el-tag",{key:a.key},[e._v("\n            "+e._s(a.value)+"\n          ")]):e._e()})}}])}),e._v(" "),r("el-table-column",{attrs:{prop:"productType","header-align":"center",align:"center",label:"商品类型"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.productTypeCodes,function(a){return t.row.productType===a.key?r("el-tag",{key:a.key},[e._v("\n            "+e._s(a.value)+"\n          ")]):e._e()})}}])}),e._v(" "),r("el-table-column",{attrs:{prop:"orderNumber","header-align":"center",align:"center",label:"购买数量"}}),e._v(" "),r("el-table-column",{attrs:{prop:"totalAmount","header-align":"center",align:"center",label:"订单总金额"}}),e._v(" "),r("el-table-column",{attrs:{prop:"orderTime","header-align":"center",align:"center",label:"订单创建时间"}}),e._v(" "),r("el-table-column",{attrs:{prop:"updateTime","header-align":"center",align:"center",label:"订单最后更新时间"}})],1),e._v(" "),r("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?r("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]},d=r("46Yf")(a,o,!1,null,null,null);t.default=d.exports}});