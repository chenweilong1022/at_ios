webpackJsonp([31,56],{G4Y8:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{dataForm:{productName:""},dataList:[],countryCodes:[],productTypeCodes:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1}},components:{AddOrUpdate:a("Lb6B").default},activated:function(){this.getDataList(),this.getCountryCodes(),this.getProductTypeCodes()},methods:{getDataList:function(){var t=this;this.dataListLoading=!0,this.$http({url:this.$http.adornUrl("/ltt/productinfo/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,productName:this.dataForm.productName})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},getCountryCodes:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.countryCodes=a.data:t.$message.error(a.msg)})},getProductTypeCodes:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getProductTypeCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.productTypeCodes=a.data:t.$message.error(a.msg)})},sizeChangeHandle:function(t){this.pageSize=t,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(t){this.pageIndex=t,this.getDataList()},selectionChangeHandle:function(t){this.dataListSelections=t},addOrUpdateHandle:function(t){var e=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){e.$refs.addOrUpdate.init(t)})},deleteHandle:function(t){var e=this,a=t?[t]:this.dataListSelections.map(function(t){return t.productId});this.$confirm("确定对[id="+a.join(",")+"]进行["+(t?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/productinfo/delete"),method:"post",data:e.$http.adornData(a,!1)}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},updateStatusHandle:function(t,e,a){var r=this;this.$confirm("确定进行["+a+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){r.$http({url:r.$http.adornUrl("/ltt/productinfo/update"),method:"post",data:r.$http.adornData({productId:t,status:e})}).then(function(t){var e=t.data;e&&0===e.code?r.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){r.getDataList()}}):r.$message.error(e.msg)})})}}},o={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:t.dataForm},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.getDataList()}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"商品名称检索",clearable:""},model:{value:t.dataForm.productName,callback:function(e){t.$set(t.dataForm,"productName",e)},expression:"dataForm.productName"}})],1),t._v(" "),a("el-form-item",[a("el-button",{on:{click:function(e){t.getDataList()}}},[t._v("查询")]),t._v(" "),t.isAuth("ltt:productinfo:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.addOrUpdateHandle()}}},[t._v("新增")]):t._e()],1)],1),t._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:t.dataList,border:""},on:{"selection-change":t.selectionChangeHandle}},[a("el-table-column",{attrs:{prop:"productName","header-align":"center",align:"center",label:"商品名称"}}),t._v(" "),a("el-table-column",{attrs:{prop:"productType","header-align":"center",align:"center",label:"商品类型"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(t.productTypeCodes,function(r){return e.row.productType===r.key?a("el-tag",{key:r.key},[t._v("\n            "+t._s(r.value)+"\n          ")]):t._e()})}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"countryCode","header-align":"center",align:"center",label:"国号(区号)"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(t.countryCodes,function(r){return e.row.countryCode===r.value?a("el-tag",{key:r.key},[t._v("\n            "+t._s(r.value)+"\n          ")]):t._e()})}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"price","header-align":"center",align:"center",label:"商品价格"}}),t._v(" "),a("el-table-column",{attrs:{prop:"status","header-align":"center",align:"center",label:"商品状态"},scopedSlots:t._u([{key:"default",fn:function(e){return[1===e.row.status?a("el-tag",{attrs:{size:"small",type:"success"}},[t._v("上架")]):a("el-tag",{attrs:{size:"small",type:"danger"}},[t._v("下架")])]}}])}),t._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),t._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.addOrUpdateHandle(e.row.productId)}}},[t._v("修改")]),t._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.deleteHandle(e.row.productId)}}},[t._v("删除")]),t._v(" "),1===e.row.status?a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.updateStatusHandle(e.row.productId,0,"下架")}}},[t._v("下架\n          ")]):a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){t.updateStatusHandle(e.row.productId,1,"上架")}}},[t._v("\n            上架\n          ")])]}}])})],1),t._v(" "),a("el-pagination",{attrs:{"current-page":t.pageIndex,"page-sizes":[10,20,50,100],"page-size":t.pageSize,total:t.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":t.sizeChangeHandle,"current-change":t.currentChangeHandle}}),t._v(" "),t.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:t.getDataList}}):t._e()],1)},staticRenderFns:[]},n=a("46Yf")(r,o,!1,null,null,null);e.default=n.exports},Lb6B:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{productId:0,productName:"",description:"",price:"",stockQuantity:"",countryCode:"",productType:"",status:"",createTime:"",updateTime:""},countryCodes:[],productTypeCodes:[],dataRule:{productName:[{required:!0,message:"商品名称不能为空",trigger:"blur"}],description:[{required:!0,message:"商品描述不能为空",trigger:"blur"}],price:[{required:!0,message:"商品价格不能为空",trigger:"blur"}],countryCode:[{required:!0,message:"国号(区号)不能为空",trigger:"blur"}],productType:[{required:!0,message:"商品图片URL地址不能为空",trigger:"blur"}],status:[{required:!0,message:"商品状态（1上架 0下架）不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],updateTime:[{required:!0,message:"最后更新时间不能为空",trigger:"blur"}]}}},methods:{init:function(t){var e=this;this.dataForm.productId=t||0,this.visible=!0,this.$nextTick(function(){e.$refs.dataForm.resetFields(),e.dataForm.productId&&e.$http({url:e.$http.adornUrl("/ltt/productinfo/info/"+e.dataForm.productId),method:"get",params:e.$http.adornParams()}).then(function(t){var a=t.data;a&&0===a.code&&(e.dataForm.productName=a.productInfo.productName,e.dataForm.description=a.productInfo.description,e.dataForm.price=a.productInfo.price,e.dataForm.stockQuantity=a.productInfo.stockQuantity,e.dataForm.countryCode=a.productInfo.countryCode,e.dataForm.productType=a.productInfo.productType,e.dataForm.status=a.productInfo.status,e.dataForm.createTime=a.productInfo.createTime,e.dataForm.updateTime=a.productInfo.updateTime)})}),this.getCountryCodes(),this.getProductTypeCodes()},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$http({url:t.$http.adornUrl("/ltt/productinfo/"+(t.dataForm.productId?"update":"save")),method:"post",data:t.$http.adornData({productId:t.dataForm.productId||void 0,productName:t.dataForm.productName,description:t.dataForm.description,price:t.dataForm.price,stockQuantity:t.dataForm.stockQuantity,countryCode:t.dataForm.countryCode,productType:t.dataForm.productType,status:t.dataForm.status,createTime:t.dataForm.createTime,updateTime:t.dataForm.updateTime})}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(a.msg)})})},getProductTypeCodes:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/getProductTypeCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.productTypeCodes=a.data:t.$message.error(a.msg)})},getCountryCodes:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(e){var a=e.data;a&&0===a.code?t.countryCodes=a.data:t.$message.error(a.msg)})}}},o={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("el-dialog",{attrs:{title:t.dataForm.productId?"修改":"新增","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[a("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"商品名称",prop:"productName"}},[a("el-input",{attrs:{placeholder:"商品名称"},model:{value:t.dataForm.productName,callback:function(e){t.$set(t.dataForm,"productName",e)},expression:"dataForm.productName"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"商品类型",prop:"productType"}},[a("el-select",{attrs:{filterable:"",placeholder:"请选择商品类型"},model:{value:t.dataForm.productType,callback:function(e){t.$set(t.dataForm,"productType",e)},expression:"dataForm.productType"}},t._l(t.productTypeCodes,function(t){return a("el-option",{key:t.key,attrs:{label:t.value,value:t.key}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"商品描述",prop:"description"}},[a("el-input",{attrs:{placeholder:"商品描述"},model:{value:t.dataForm.description,callback:function(e){t.$set(t.dataForm,"description",e)},expression:"dataForm.description"}})],1),t._v(" "),a("el-form-item",{attrs:{label:"国号(区号)",prop:"countryCode"}},[a("el-select",{attrs:{filterable:"",placeholder:"请选择国家"},model:{value:t.dataForm.countryCode,callback:function(e){t.$set(t.dataForm,"countryCode",e)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return a("el-option",{key:t.value,attrs:{label:t.value,value:t.value}})}))],1),t._v(" "),a("el-form-item",{attrs:{label:"商品价格",prop:"price"}},[a("el-input",{attrs:{placeholder:"商品价格"},model:{value:t.dataForm.price,callback:function(e){t.$set(t.dataForm,"price",e)},expression:"dataForm.price"}})],1)],1),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},n=a("46Yf")(r,o,!1,null,null,null);e.default=n.exports}});