webpackJsonp([1,5,69,77,78],{"+N1c":function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n("IHPB"),r=n.n(a),o={data:function(){return{visible:!1,customerUserOptions:[],userCustomerName:"",dataForm:{ids:[],customerServiceId:""},dataRule:{customerServiceId:[{required:!0,message:"选择客服不能为空",trigger:"blur"}]}}},methods:{userCustomerChangeHandler:function(e){var t=this;this.customerUserOptions.find(function(e){t.userCustomerName=e.nickname})},init:function(e){this.visible=!0,this.ids=e,console.log(e)},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atuser/updateUserCustomer"),method:"post",data:e.$http.adornData({ids:e.ids,customerServiceId:e.dataForm.customerServiceId,customerService:e.userCustomerName})}).then(function(t){var n=t.data;n&&0===n.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(n.msg)})})},queryCustomerByFuzzyName:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/customeruser/queryCustomerByFuzzyName?key="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var n=e.data;n&&0===n.code&&(t.customerUserOptions=[{userId:0,nickname:"未分配"}].concat(r()(n.customerList)))})}}},s={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("el-dialog",{attrs:{title:"分配客服","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[n("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[n("el-form-item",{attrs:{label:"选择客服",prop:"customerServiceId"}},[n("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择客服","remote-method":e.queryCustomerByFuzzyName,loading:e.loading},on:{change:e.userCustomerChangeHandler},model:{value:e.dataForm.customerServiceId,callback:function(t){e.$set(e.dataForm,"customerServiceId",t)},expression:"dataForm.customerServiceId"}},e._l(e.customerUserOptions,function(e){return n("el-option",{key:e.userId,attrs:{label:e.nickname,value:e.userId}})}))],1)],1),e._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},l=n("46Yf")(o,s,!1,null,null,null);t.default=l.exports},"0Rdk":function(e,t,n){var a=n("e/tU");"string"==typeof a&&(a=[[e.i,a,""]]),a.locals&&(e.exports=a.locals);n("XkoO")("3cec4af5",a,!0)},"2ZiF":function(e,t,n){"use strict";var a=n("1tfL"),r=n("uSC2"),o=n("PGvq"),s=n("V88E"),l=n("pBi0"),i=n("qaXV"),u=n("bJ/5"),d=n("/tnA");r(r.S+r.F*!n("3Bg5")(function(e){Array.from(e)}),"Array",{from:function(e){var t,n,r,c,m=o(e),p="function"==typeof this?this:Array,f=arguments.length,h=f>1?arguments[1]:void 0,v=void 0!==h,g=0,b=d(m);if(v&&(h=a(h,f>2?arguments[2]:void 0,2)),void 0==b||p==Array&&l(b))for(n=new p(t=i(m.length));t>g;g++)u(n,g,v?h(m[g],g):m[g]);else for(c=b.call(m),n=new p;!(r=c.next()).done;g++)u(n,g,v?s(c,h,[r.value,g],!0):r.value);return n.length=g,n}})},IHPB:function(e,t,n){"use strict";t.__esModule=!0;var a,r=n("kfHR"),o=(a=r)&&a.__esModule?a:{default:a};t.default=function(e){if(Array.isArray(e)){for(var t=0,n=Array(e.length);t<e.length;t++)n[t]=e[t];return n}return(0,o.default)(e)}},KYMQ:function(e,t,n){n("g5OY"),n("2ZiF"),e.exports=n("DH3n").Array.from},Q1dw:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={data:function(){return{visible:!1,userGroupOptions:[],userGroupName:"",dataForm:{ids:[],userGroupId:""},dataRule:{userGroupId:[{required:!0,message:"分组id不能为空",trigger:"blur"}]}}},methods:{userGroupChangeHandler:function(e){var t=this;this.userGroupOptions.find(function(e){t.userGroupName=e.name})},init:function(e){this.visible=!0,this.ids=e,console.log(e)},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atuser/updateUserGroup"),method:"post",data:e.$http.adornData({ids:e.ids,userGroupId:e.dataForm.userGroupId,userGroupName:e.userGroupName})}).then(function(t){var n=t.data;n&&0===n.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(n.msg)})})},queryUserGroupBySearchWord:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/atusergroup/queryByFuzzyName?searchWord="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var n=e.data;n&&0===n.code&&(t.userGroupOptions=n.groupList)})}}},r={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("el-dialog",{attrs:{title:"转移分组","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[n("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[n("el-form-item",{attrs:{label:"账号分组",prop:"userGroupId"}},[n("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择分组","remote-method":e.queryUserGroupBySearchWord,loading:e.loading},on:{change:e.userGroupChangeHandler},model:{value:e.dataForm.userGroupId,callback:function(t){e.$set(e.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}},e._l(e.userGroupOptions,function(e){return n("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})}))],1)],1),e._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},o=n("46Yf")(a,r,!1,null,null,null);t.default=o.exports},Sirn:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={data:function(){return{visible:!1,dataForm:{id:0,avatar:"",nation:"",telephone:"",nickName:"",numberFriends:"",password:"",userGroupId:"",userGroupName:"",customerServiceId:"",customerService:"",deleteFlag:"",createTime:"",userTokenId:""},dataRule:{avatar:[{required:!0,message:"头像不能为空",trigger:"blur"}],nation:[{required:!0,message:"国家不能为空",trigger:"blur"}],telephone:[{required:!0,message:"电话不能为空",trigger:"blur"}],nickName:[{required:!0,message:"昵称不能为空",trigger:"blur"}],numberFriends:[{required:!0,message:"好友数量不能为空",trigger:"blur"}],password:[{required:!0,message:"密码不能为空",trigger:"blur"}],userGroupId:[{required:!0,message:"分组id不能为空",trigger:"blur"}],userGroupName:[{required:!0,message:"分组名称不能为空",trigger:"blur"}],customerServiceId:[{required:!0,message:"所属客服id不能为空",trigger:"blur"}],customerService:[{required:!0,message:"所属客服不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],userTokenId:[{required:!0,message:"用户tokenid不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/ltt/atuser/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(e){var n=e.data;n&&0===n.code&&(t.dataForm.avatar=n.atUser.avatar,t.dataForm.nation=n.atUser.nation,t.dataForm.telephone=n.atUser.telephone,t.dataForm.nickName=n.atUser.nickName,t.dataForm.numberFriends=n.atUser.numberFriends,t.dataForm.password=n.atUser.password,t.dataForm.userGroupId=n.atUser.userGroupId,t.dataForm.userGroupName=n.atUser.userGroupName,t.dataForm.customerServiceId=n.atUser.customerServiceId,t.dataForm.customerService=n.atUser.customerService,t.dataForm.deleteFlag=n.atUser.deleteFlag,t.dataForm.createTime=n.atUser.createTime,t.dataForm.userTokenId=n.atUser.userTokenId)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atuser/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,avatar:e.dataForm.avatar,nation:e.dataForm.nation,telephone:e.dataForm.telephone,nickName:e.dataForm.nickName,numberFriends:e.dataForm.numberFriends,password:e.dataForm.password,userGroupId:e.dataForm.userGroupId,userGroupName:e.dataForm.userGroupName,customerServiceId:e.dataForm.customerServiceId,customerService:e.dataForm.customerService,deleteFlag:e.dataForm.deleteFlag,createTime:e.dataForm.createTime,userTokenId:e.dataForm.userTokenId})}).then(function(t){var n=t.data;n&&0===n.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(n.msg)})})}}},r={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[n("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[n("el-form-item",{attrs:{label:"头像",prop:"avatar"}},[n("el-input",{attrs:{placeholder:"头像"},model:{value:e.dataForm.avatar,callback:function(t){e.$set(e.dataForm,"avatar",t)},expression:"dataForm.avatar"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"国家",prop:"nation"}},[n("el-input",{attrs:{placeholder:"国家"},model:{value:e.dataForm.nation,callback:function(t){e.$set(e.dataForm,"nation",t)},expression:"dataForm.nation"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"电话",prop:"telephone"}},[n("el-input",{attrs:{placeholder:"电话"},model:{value:e.dataForm.telephone,callback:function(t){e.$set(e.dataForm,"telephone",t)},expression:"dataForm.telephone"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"昵称",prop:"nickName"}},[n("el-input",{attrs:{placeholder:"昵称"},model:{value:e.dataForm.nickName,callback:function(t){e.$set(e.dataForm,"nickName",t)},expression:"dataForm.nickName"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"好友数量",prop:"numberFriends"}},[n("el-input",{attrs:{placeholder:"好友数量"},model:{value:e.dataForm.numberFriends,callback:function(t){e.$set(e.dataForm,"numberFriends",t)},expression:"dataForm.numberFriends"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"密码",prop:"password"}},[n("el-input",{attrs:{placeholder:"密码"},model:{value:e.dataForm.password,callback:function(t){e.$set(e.dataForm,"password",t)},expression:"dataForm.password"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"分组id",prop:"userGroupId"}},[n("el-input",{attrs:{placeholder:"分组id"},model:{value:e.dataForm.userGroupId,callback:function(t){e.$set(e.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"分组名称",prop:"userGroupName"}},[n("el-input",{attrs:{placeholder:"分组名称"},model:{value:e.dataForm.userGroupName,callback:function(t){e.$set(e.dataForm,"userGroupName",t)},expression:"dataForm.userGroupName"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"所属客服id",prop:"customerServiceId"}},[n("el-input",{attrs:{placeholder:"所属客服id"},model:{value:e.dataForm.customerServiceId,callback:function(t){e.$set(e.dataForm,"customerServiceId",t)},expression:"dataForm.customerServiceId"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"所属客服",prop:"customerService"}},[n("el-input",{attrs:{placeholder:"所属客服"},model:{value:e.dataForm.customerService,callback:function(t){e.$set(e.dataForm,"customerService",t)},expression:"dataForm.customerService"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"删除标志",prop:"deleteFlag"}},[n("el-input",{attrs:{placeholder:"删除标志"},model:{value:e.dataForm.deleteFlag,callback:function(t){e.$set(e.dataForm,"deleteFlag",t)},expression:"dataForm.deleteFlag"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[n("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(t){e.$set(e.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"用户tokenid",prop:"userTokenId"}},[n("el-input",{attrs:{placeholder:"用户tokenid"},model:{value:e.dataForm.userTokenId,callback:function(t){e.$set(e.dataForm,"userTokenId",t)},expression:"dataForm.userTokenId"}})],1)],1),e._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},o=n("46Yf")(a,r,!1,null,null,null);t.default=o.exports},Ti2L:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("el-dialog",{attrs:{title:"账户购买","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[n("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[n("el-form-item",{attrs:{label:"注册国家",prop:"countryCode"}},[n("el-select",{attrs:{placeholder:"请选择注册国家",clearable:""},on:{change:e.productChangeHandler},model:{value:e.dataForm.countryCode,callback:function(t){e.$set(e.dataForm,"countryCode",t)},expression:"dataForm.countryCode"}},e._l(e.countryCodes,function(e){return n("el-option",{key:e.value,attrs:{label:e.value,value:e.value}})}))],1),e._v(" "),n("el-form-item",{attrs:{label:"商品名称",prop:"productName"}},[n("el-input",{attrs:{placeholder:"商品名称",disabled:"true"},model:{value:e.dataForm.productName,callback:function(t){e.$set(e.dataForm,"productName",t)},expression:"dataForm.productName"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"商品价格",prop:"price"}},[n("el-input",{attrs:{placeholder:"商品价格",disabled:"true"},model:{value:e.dataForm.price,callback:function(t){e.$set(e.dataForm,"price",t)},expression:"dataForm.price"}})],1),e._v(" "),n("el-form-item",{attrs:{label:"购买数量",prop:"orderNumber"}},[n("el-input",{attrs:{placeholder:"购买数量"},model:{value:e.dataForm.orderNumber,callback:function(t){e.$set(e.dataForm,"orderNumber",t)},expression:"dataForm.orderNumber"}})],1)],1),e._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},r=n("46Yf")({data:function(){return{visible:!1,options:[{value:1,label:"安卓"},{value:2,label:"IOS"}],countryValue:null,dataForm:{countryCode:null,productId:null,productName:null,price:null,orderNumber:null},userGroupData:[],productInfoOptions:[],countryCodes:[],dataRule:{countryCode:[{required:!0,message:"注册国家不能为空",trigger:"blur"}],orderNumber:[{required:!0,message:"产品信息不能为空",trigger:"blur"}]}}},methods:{handleAvatarSuccess:function(e,t){this.dataForm.txtUrl=e.data},init:function(){this.visible=!0,this.getCountryCodeEnums()},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$confirm("确定购买["+e.countryValue+"] 数量:["+e.dataForm.orderNumber+"]?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atorders/createOrderToken"),method:"post",data:e.$http.adornData({countryCode:e.dataForm.countryCode,orderNumber:e.dataForm.orderNumber,productId:e.dataForm.productId})}).then(function(t){var n=t.data;n&&0===n.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(n.msg)})})})},productChangeHandler:function(){var e=this;this.countryCodes.find(function(t){e.countryValue=t.value}),this.queryProductBySearchWord()},queryProductBySearchWord:function(){var e=this;this.$http({url:this.$http.adornUrl("/ltt/productinfo/queryOnlyProduct"),method:"get",params:this.$http.adornParams({countryCode:this.dataForm.countryCode,productType:1,status:1})}).then(function(t){var n=t.data;n&&0===n.code&&null!=n.productInfo?(e.dataForm.productName=n.productInfo.productName,e.dataForm.price=n.productInfo.price,e.dataForm.productId=n.productInfo.productId):(e.dataForm.productName="",e.dataForm.price="",e.dataForm.productId="")})},getCountryCodeEnums:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(t){var n=t.data;n&&0===n.code?e.countryCodes=n.data:e.$message.error(n.msg)})}}},a,!1,null,null,null);t.default=r.exports},"bJ/5":function(e,t,n){"use strict";var a=n("FIRb"),r=n("ZwTi");e.exports=function(e,t,n){t in e?a.f(e,t,r(0,n)):e[t]=n}},"e/tU":function(e,t,n){(e.exports=n("acE3")(!1)).push([e.i,"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",""])},jGEJ:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=n("IHPB"),r=n.n(a),o=n("Sirn"),s=n("dwaA"),l=n("Ti2L"),i=n("Q1dw"),u=n("+N1c"),d={data:function(){return{userGroupOptions:[],customerUserOptions:[],statusOptions:[{value:1,label:"未验证"},{value:2,label:"封号"},{value:3,label:"下线"},{value:4,label:"在线"},{value:5,label:"数据错误"}],validateOptions:[{value:0,label:"否"},{value:1,label:"是"}],dataForm:{nickName:"",nation:"",telephone:"",userGroupId:null,status:null,customerServiceId:null,validateFlag:null,userSource:null,selectLimit:null},customerServiceField:"",dataList:[],atUserSourceCodes:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,userImportVisible:!1,userTokenOrderVisible:!1,userTransferGroupVisible:!1,userCustomerVisible:!1}},components:{UserImport:s.default,UserTokenOrder:l.default,UserTransferGroup:i.default,UserCustomerGroup:u.default,AddOrUpdate:o.default},activated:function(){this.getDataList(),this.getAtUserSource()},methods:{getDataList:function(){var e=this;this.dataListLoading=!0,null!=this.dataForm.selectLimit&&this.dataForm.selectLimit>0&&(this.pageIndex=1,this.pageSize=this.dataForm.selectLimit),this.$http({url:this.$http.adornUrl("/ltt/atuser/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,nickName:this.dataForm.nickName,nation:this.dataForm.nation,telephone:this.dataForm.telephone,userGroupId:this.dataForm.userGroupId,status:this.dataForm.status,customerServiceId:this.dataForm.customerServiceId,customerService:this.dataForm.customerService,validateFlag:this.dataForm.validateFlag,userSource:this.dataForm.userSource,selectLimit:this.dataForm.selectLimit})}).then(function(t){var n=t.data;n&&0===n.code?(e.dataList=n.page.list,e.totalPage=n.page.totalCount):(e.dataList=[],e.totalPage=0),e.dataListLoading=!1})},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},addOrUpdateHandle:function(e){var t=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){t.$refs.addOrUpdate.init(e)})},userImportHandle:function(e){var t=this;this.userImportVisible=!0,this.$nextTick(function(){t.$refs.userImport.init(e)})},userTokenOrderHandle:function(e){var t=this;this.userTokenOrderVisible=!0,this.$nextTick(function(){t.$refs.userTokenOrder.init(e)})},userTransferGroupHandle:function(e){var t=this;this.userTransferGroupVisible=!0;var n=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对所选项进行分组转移操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$nextTick(function(){t.$refs.userTransferGroup.init(n)})})},userCustomerHandle:function(e){var t=this;this.userCustomerVisible=!0;var n=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对所选项进行客服分配操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$nextTick(function(){t.$refs.userCustomerGroup.init(n)})})},userValidateHandle:function(){var e=this,t=this.dataListSelections.map(function(e){return e.id}),n=t.length>0?0:1;this.$confirm("确定要对["+(n?"所有":"勾选")+"]账户进行验活操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atuser/validateUserStatus"),method:"post",data:e.$http.adornData({ids:t,validateFlag:n})}).then(function(t){var n=t.data;n&&0===n.code?e.$message({message:"操作成功，正在进行验活操作，请稍后",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(n.msg)})})},downloadUserTokenTxtHandle:function(){var e=this,t=this.dataListSelections.map(function(e){return e.id});this.$confirm("确定要对勾选账户进行账户下载操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atuser/downloadUserTokenTxt"),method:"post",data:e.$http.adornData(t,!1)}).then(function(t){var n=t.data;if(n&&0===n.code){var a=e.getFilename(n.fileUrl);console.log(n.fileUrl);var r=document.createElement("a");fetch(n.fileUrl).then(function(e){return e.blob()}).then(function(e){r.href=URL.createObjectURL(e),console.log(r.href),r.download=a,document.body.appendChild(r),r.click()})}else e.$message.error(n.msg)})})},getFilename:function(e){return e.substring(e.lastIndexOf("/")+1)},deleteHandle:function(e){var t=this,n=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对[id="+n.join(",")+"]进行["+(e?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/atuser/delete"),method:"post",data:t.$http.adornData(n,!1)}).then(function(e){var n=e.data;n&&0===n.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(n.msg)})})},queryUserGroupBySearchWord:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/atusergroup/queryByFuzzyName?searchWord="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var n=e.data;n&&0===n.code&&(t.userGroupOptions=n.groupList)})},queryCustomerByFuzzyName:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/customeruser/queryCustomerByFuzzyName?key="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var n=e.data;n&&0===n.code&&(t.customerUserOptions=[{userId:0,nickname:"未分配"}].concat(r()(n.customerList)))})},getAtUserSource:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/getAtUserSource"),method:"get"}).then(function(t){var n=t.data;n&&0===n.code?e.atUserSourceCodes=n.data:e.$message.error(n.msg)})}}},c={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"mod-config"},[n("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.getDataList()}}},[n("el-form-item",[n("el-input",{attrs:{placeholder:"昵称",clearable:""},model:{value:e.dataForm.nickName,callback:function(t){e.$set(e.dataForm,"nickName",t)},expression:"dataForm.nickName"}})],1),e._v(" "),n("el-form-item",[n("el-input",{attrs:{placeholder:"国家",clearable:""},model:{value:e.dataForm.nation,callback:function(t){e.$set(e.dataForm,"nation",t)},expression:"dataForm.nation"}})],1),e._v(" "),n("el-form-item",[n("el-input",{attrs:{placeholder:"手机号",clearable:""},model:{value:e.dataForm.telephone,callback:function(t){e.$set(e.dataForm,"telephone",t)},expression:"dataForm.telephone"}})],1),e._v(" "),n("el-form-item",[n("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择分组","remote-method":e.queryUserGroupBySearchWord,loading:e.loading},model:{value:e.dataForm.userGroupId,callback:function(t){e.$set(e.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}},e._l(e.userGroupOptions,function(e){return n("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})}))],1),e._v(" "),n("el-form-item",[n("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{clearable:"",placeholder:"账户状态"},model:{value:e.dataForm.status,callback:function(t){e.$set(e.dataForm,"status",t)},expression:"dataForm.status"}},e._l(e.statusOptions,function(e){return n("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),n("el-form-item",[n("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择客服","remote-method":e.queryCustomerByFuzzyName,loading:e.loading},model:{value:e.dataForm.customerServiceId,callback:function(t){e.$set(e.dataForm,"customerServiceId",t)},expression:"dataForm.customerServiceId"}},e._l(e.customerUserOptions,function(e){return n("el-option",{key:e.userId,attrs:{label:e.nickname,value:e.userId}})})),e._v(" "),n("el-form-item",[n("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{clearable:"",placeholder:"是否有验活记录"},model:{value:e.dataForm.validateFlag,callback:function(t){e.$set(e.dataForm,"validateFlag",t)},expression:"dataForm.validateFlag"}},e._l(e.validateOptions,function(e){return n("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),n("el-form-item",[n("el-select",{attrs:{placeholder:"账户来源",clearable:""},model:{value:e.dataForm.userSource,callback:function(t){e.$set(e.dataForm,"userSource",t)},expression:"dataForm.userSource"}},e._l(e.atUserSourceCodes,function(e){return n("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),n("el-form-item",[n("el-input",{attrs:{placeholder:"查询条数",clearable:""},model:{value:e.dataForm.selectLimit,callback:function(t){e.$set(e.dataForm,"selectLimit",t)},expression:"dataForm.selectLimit"}})],1),e._v(" "),n("el-button",{on:{click:function(t){e.getDataList()}}},[e._v("查询")]),e._v(" "),e.isAuth("ltt:atuser:save")?n("el-button",{attrs:{type:"primary"},on:{click:function(t){e.userImportHandle()}}},[e._v("账户导入\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?n("el-button",{attrs:{type:"primary"},on:{click:function(t){e.userTokenOrderHandle()}}},[e._v("账户购买\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?n("el-button",{attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.userTransferGroupHandle()}}},[e._v("转移分组\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?n("el-button",{attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.downloadUserTokenTxtHandle()}}},[e._v("下载账户txt\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?n("el-button",{attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.userCustomerHandle()}}},[e._v("分配客服\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?n("el-button",{staticStyle:{"margin-top":"15px"},attrs:{type:"primary"},on:{click:function(t){e.userValidateHandle()}}},[e._v("验活账号\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:delete")?n("el-button",{staticStyle:{"margin-top":"15px"},attrs:{type:"danger",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.deleteHandle()}}},[e._v("批量删除\n      ")]):e._e()],1)],1),e._v(" "),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"selection-change":e.selectionChangeHandle}},[n("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),e._v(" "),n("el-table-column",{attrs:{prop:"avatar","header-align":"center",align:"center",label:"头像"}}),e._v(" "),n("el-table-column",{attrs:{prop:"nation","header-align":"center",align:"center",label:"国家"}}),e._v(" "),n("el-table-column",{attrs:{prop:"telephone","header-align":"center",align:"center",label:"电话"}}),e._v(" "),n("el-table-column",{attrs:{prop:"nickName","header-align":"center",align:"center",label:"昵称"}}),e._v(" "),n("el-table-column",{attrs:{prop:"numberFriends","header-align":"center",align:"center",label:"好友数量"}}),e._v(" "),n("el-table-column",{attrs:{prop:"password","header-align":"center",align:"center",label:"密码"}}),e._v(" "),n("el-table-column",{attrs:{prop:"userGroupName","header-align":"center",align:"center",label:"分组名称"}}),e._v(" "),n("el-table-column",{attrs:{prop:"userSource","header-align":"center",align:"center",label:"账号来源"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.atUserSourceCodes,function(a){return t.row.userSource===a.key?n("el-tag",{key:a.key},[e._v("\n          "+e._s(a.value)+"\n        ")]):e._e()})}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"status","header-align":"center",align:"center",label:"状态"},scopedSlots:e._u([{key:"default",fn:function(t){return[1===t.row.status?n("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("未验证")]):2===t.row.status?n("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("封号")]):3===t.row.status?n("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("下线")]):4===t.row.status?n("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("在线")]):6===t.row.status?n("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("已使用")]):n("el-tag",{attrs:{size:"small"}},[e._v("数据错误")])]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"customerService","header-align":"center",align:"center",label:"所属客服"},scopedSlots:e._u([{key:"default",fn:function(t){return[null==t.row.customerServiceId?n("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("未分配")]):n("el-tag",{attrs:{size:"small"}},[e._v(e._s(t.row.customerService))])]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),e._v(" "),n("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{attrs:{type:"text",size:"small"},on:{click:function(n){e.addOrUpdateHandle(t.row.id)}}},[e._v("修改")]),e._v(" "),n("el-button",{attrs:{type:"text",size:"small"},on:{click:function(n){e.deleteHandle(t.row.id)}}},[e._v("删除")])]}}])})],1),e._v(" "),n("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,50,100],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?n("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e(),e._v(" "),e.userImportVisible?n("user-import",{ref:"userImport",on:{refreshDataList:e.getDataList}}):e._e(),e._v(" "),e.userTokenOrderVisible?n("user-token-order",{ref:"userTokenOrder",on:{refreshDataList:e.getDataList}}):e._e(),e._v(" "),e.userTransferGroupVisible?n("user-transfer-group",{ref:"userTransferGroup",on:{refreshDataList:e.getDataList}}):e._e(),e._v(" "),e.userCustomerVisible?n("user-customer-group",{ref:"userCustomerGroup",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]};var m=n("46Yf")(d,c,!1,function(e){n("0Rdk")},null,null);t.default=m.exports},kfHR:function(e,t,n){e.exports={default:n("KYMQ"),__esModule:!0}}});