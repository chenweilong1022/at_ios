webpackJsonp([72],{Ti2L:function(t,e,r){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o={render:function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("el-dialog",{attrs:{title:"账户购买","close-on-click-modal":!1,visible:t.visible},on:{"update:visible":function(e){t.visible=e}}},[r("el-form",{ref:"dataForm",attrs:{model:t.dataForm,rules:t.dataRule,"label-width":"80px"},nativeOn:{keyup:function(e){if(!("button"in e)&&t._k(e.keyCode,"enter",13,e.key))return null;t.dataFormSubmit()}}},[r("el-form-item",{attrs:{label:"注册国家",prop:"countryCode"}},[r("el-select",{attrs:{placeholder:"请选择注册国家",clearable:""},on:{change:t.productChangeHandler},model:{value:t.dataForm.countryCode,callback:function(e){t.$set(t.dataForm,"countryCode",e)},expression:"dataForm.countryCode"}},t._l(t.countryCodes,function(t){return r("el-option",{key:t.value,attrs:{label:t.value,value:t.value}})}))],1),t._v(" "),r("el-form-item",{attrs:{label:"价格",prop:"price"}},[r("el-row",[r("el-col",{attrs:{span:12}},[r("el-input",{attrs:{placeholder:"价格",disabled:"true"},model:{value:t.dataForm.price,callback:function(e){t.$set(t.dataForm,"price",e)},expression:"dataForm.price"}},[r("template",{attrs:{slot:"append"},slot:"append"},[t._v("U")])],2)],1)],1)],1),t._v(" "),r("el-form-item",{attrs:{label:"购买数量",prop:"orderNumber"}},[r("el-input",{attrs:{placeholder:"购买数量"},model:{value:t.dataForm.orderNumber,callback:function(e){t.$set(t.dataForm,"orderNumber",e)},expression:"dataForm.orderNumber"}})],1)],1),t._v(" "),r("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[r("el-button",{on:{click:function(e){t.visible=!1}}},[t._v("取消")]),t._v(" "),r("el-button",{attrs:{type:"primary"},on:{click:function(e){t.dataFormSubmit()}}},[t._v("确定")])],1)],1)},staticRenderFns:[]},a=r("46Yf")({data:function(){return{visible:!1,options:[{value:1,label:"安卓"},{value:2,label:"IOS"}],countryValue:null,dataForm:{countryCode:null,productId:null,productName:null,price:null,orderNumber:null},userGroupData:[],productInfoOptions:[],countryCodes:[],dataRule:{countryCode:[{required:!0,message:"注册国家不能为空",trigger:"blur"}],orderNumber:[{required:!0,message:"产品信息不能为空",trigger:"blur"}]}}},methods:{handleAvatarSuccess:function(t,e){this.dataForm.txtUrl=t.data},init:function(){this.visible=!0,this.getCountryCodeEnums(),this.productChangeHandler()},dataFormSubmit:function(){var t=this;this.$refs.dataForm.validate(function(e){e&&t.$confirm("确定购买数量:["+t.dataForm.orderNumber+"]?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/atorders/createOrderToken"),method:"post",data:t.$http.adornData({countryCode:t.dataForm.countryCode,orderNumber:t.dataForm.orderNumber,productId:t.dataForm.productId})}).then(function(e){var r=e.data;r&&0===r.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.visible=!1,t.$emit("refreshDataList")}}):t.$message.error(r.msg)})})})},productChangeHandler:function(){this.queryProductBySearchWord()},queryProductBySearchWord:function(){var t=this;this.$http({url:this.$http.adornUrl("/ltt/productinfo/queryOnlyProduct"),method:"get",params:this.$http.adornParams({countryCode:this.dataForm.countryCode,productType:1,status:1})}).then(function(e){var r=e.data;r&&0===r.code&&null!=r.productInfo?(t.dataForm.productName=r.productInfo.productName,t.dataForm.price=r.productInfo.price,t.dataForm.productId=r.productInfo.productId):(t.dataForm.productName="",t.dataForm.price="",t.dataForm.productId="")})},getCountryCodeEnums:function(){var t=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(e){var r=e.data;r&&0===r.code?t.countryCodes=r.data:t.$message.error(r.msg)})}}},o,!1,null,null,null);e.default=a.exports}});