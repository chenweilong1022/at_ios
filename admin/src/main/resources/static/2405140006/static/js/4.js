webpackJsonp([4,75,83,84,85],{"+N1c":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=a("IHPB"),o=a.n(r),n={data:function(){return{visible:!1,customerUserOptions:[],userCustomerName:"",dataForm:{ids:[],customerServiceId:""},dataRule:{customerServiceId:[{required:!0,message:"选择客服不能为空",trigger:"blur"}]}}},methods:{userCustomerChangeHandler:function(e){var t=this;this.customerUserOptions.find(function(a){a.id===e&&(t.userCustomerName=a.nickname)})},init:function(e){this.visible=!0,this.ids=e,console.log(e),this.queryCustomerByFuzzyName()},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atuser/updateUserCustomer"),method:"post",data:e.$http.adornData({ids:e.ids,customerServiceId:e.dataForm.customerServiceId,customerService:e.userCustomerName})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})},queryCustomerByFuzzyName:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/customeruser/queryCustomerByFuzzyName?key="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.customerUserOptions=[{userId:0,nickname:"未分配"}].concat(o()(a.customerList)))})}}},s={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:"分配客服","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"选择客服",prop:"customerServiceId"}},[a("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择客服","remote-method":e.queryCustomerByFuzzyName,loading:e.loading},on:{change:e.userCustomerChangeHandler},model:{value:e.dataForm.customerServiceId,callback:function(t){e.$set(e.dataForm,"customerServiceId",t)},expression:"dataForm.customerServiceId"}},e._l(e.customerUserOptions,function(e){return a("el-option",{key:e.userId,attrs:{label:e.nickname,value:e.userId}})}))],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},i=a("46Yf")(n,s,!1,null,null,null);t.default=i.exports},Q1dw:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:"转移分组","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"120px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"红灯数量",prop:"redPhoneCount"}},[a("el-tag",{staticStyle:{"font-size":"18px"},attrs:{type:"danger"}},[e._v(e._s(e.redPhoneCount)+"个")])],1),e._v(" "),a("el-form-item",{attrs:{label:"绿灯数量",prop:"greenPhoneCount"}},[a("el-tag",{staticStyle:{"font-size":"18px"},attrs:{type:"danger"}},[e._v(e._s(e.greenPhoneCount)+"个")])],1),e._v(" "),a("el-form-item",{attrs:{label:"是否过滤红灯",prop:"filterRed"}},[a("el-radio-group",{model:{value:e.filterRed,callback:function(t){e.filterRed=t},expression:"filterRed"}},[a("el-radio-button",{attrs:{label:!0}},[e._v("是")]),e._v(" "),a("el-radio-button",{attrs:{label:!1}},[e._v("否")])],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"账号分组",prop:"userGroupId"}},[a("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择分组","remote-method":e.queryUserGroupBySearchWord,loading:e.loading},on:{change:e.userGroupChangeHandler},model:{value:e.dataForm.userGroupId,callback:function(t){e.$set(e.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}},e._l(e.userGroupOptions,function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})}))],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},o=a("46Yf")({data:function(){return{visible:!1,userGroupOptions:[],userGroupName:"",dataForm:{ids:[],userGroupId:""},redPhoneCount:0,greenPhoneCount:0,filterRed:!0,dataRule:{userGroupId:[{required:!0,message:"分组id不能为空",trigger:"blur"}]}}},methods:{userGroupChangeHandler:function(e){var t=this;this.userGroupOptions.find(function(a){a.id===e&&(t.userGroupName=a.name)})},init:function(e,t,a){this.visible=!0,this.ids=e,this.redPhoneCount=t,this.greenPhoneCount=a,this.queryUserGroupBySearchWord()},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atuser/updateUserGroup"),method:"post",data:e.$http.adornData({ids:e.ids,userGroupId:e.dataForm.userGroupId,userGroupName:e.userGroupName,filterRed:e.filterRed})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})},queryUserGroupBySearchWord:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/atusergroup/queryByFuzzyName?searchWord="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.userGroupOptions=a.groupList)})}}},r,!1,null,null,null);t.default=o.exports},Sirn:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={data:function(){return{visible:!1,dataForm:{id:0,avatar:"",nation:"",telephone:"",nickName:"",numberFriends:"",password:"",userGroupId:"",userGroupName:"",customerServiceId:"",customerService:"",deleteFlag:"",createTime:"",userTokenId:""},dataRule:{avatar:[{required:!0,message:"头像不能为空",trigger:"blur"}],nation:[{required:!0,message:"国家不能为空",trigger:"blur"}],telephone:[{required:!0,message:"电话不能为空",trigger:"blur"}],nickName:[{required:!0,message:"昵称不能为空",trigger:"blur"}],numberFriends:[{required:!0,message:"好友数量不能为空",trigger:"blur"}],password:[{required:!0,message:"密码不能为空",trigger:"blur"}],userGroupId:[{required:!0,message:"分组id不能为空",trigger:"blur"}],userGroupName:[{required:!0,message:"分组名称不能为空",trigger:"blur"}],customerServiceId:[{required:!0,message:"所属客服id不能为空",trigger:"blur"}],customerService:[{required:!0,message:"所属客服不能为空",trigger:"blur"}],deleteFlag:[{required:!0,message:"删除标志不能为空",trigger:"blur"}],createTime:[{required:!0,message:"创建时间不能为空",trigger:"blur"}],userTokenId:[{required:!0,message:"用户tokenid不能为空",trigger:"blur"}]}}},methods:{init:function(e){var t=this;this.dataForm.id=e||0,this.visible=!0,this.$nextTick(function(){t.$refs.dataForm.resetFields(),t.dataForm.id&&t.$http({url:t.$http.adornUrl("/ltt/atuser/info/"+t.dataForm.id),method:"get",params:t.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.dataForm.avatar=a.atUser.avatar,t.dataForm.nation=a.atUser.nation,t.dataForm.telephone=a.atUser.telephone,t.dataForm.nickName=a.atUser.nickName,t.dataForm.numberFriends=a.atUser.numberFriends,t.dataForm.password=a.atUser.password,t.dataForm.userGroupId=a.atUser.userGroupId,t.dataForm.userGroupName=a.atUser.userGroupName,t.dataForm.customerServiceId=a.atUser.customerServiceId,t.dataForm.customerService=a.atUser.customerService,t.dataForm.deleteFlag=a.atUser.deleteFlag,t.dataForm.createTime=a.atUser.createTime,t.dataForm.userTokenId=a.atUser.userTokenId)})})},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$http({url:e.$http.adornUrl("/ltt/atuser/"+(e.dataForm.id?"update":"save")),method:"post",data:e.$http.adornData({id:e.dataForm.id||void 0,avatar:e.dataForm.avatar,nation:e.dataForm.nation,telephone:e.dataForm.telephone,nickName:e.dataForm.nickName,numberFriends:e.dataForm.numberFriends,password:e.dataForm.password,userGroupId:e.dataForm.userGroupId,userGroupName:e.dataForm.userGroupName,customerServiceId:e.dataForm.customerServiceId,customerService:e.dataForm.customerService,deleteFlag:e.dataForm.deleteFlag,createTime:e.dataForm.createTime,userTokenId:e.dataForm.userTokenId})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})}}},o={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:e.dataForm.id?"修改":"新增","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"头像",prop:"avatar"}},[a("el-input",{attrs:{placeholder:"头像"},model:{value:e.dataForm.avatar,callback:function(t){e.$set(e.dataForm,"avatar",t)},expression:"dataForm.avatar"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"国家",prop:"nation"}},[a("el-input",{attrs:{placeholder:"国家"},model:{value:e.dataForm.nation,callback:function(t){e.$set(e.dataForm,"nation",t)},expression:"dataForm.nation"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"电话",prop:"telephone"}},[a("el-input",{attrs:{placeholder:"电话"},model:{value:e.dataForm.telephone,callback:function(t){e.$set(e.dataForm,"telephone",t)},expression:"dataForm.telephone"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"昵称",prop:"nickName"}},[a("el-input",{attrs:{placeholder:"昵称"},model:{value:e.dataForm.nickName,callback:function(t){e.$set(e.dataForm,"nickName",t)},expression:"dataForm.nickName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"好友数量",prop:"numberFriends"}},[a("el-input",{attrs:{placeholder:"好友数量"},model:{value:e.dataForm.numberFriends,callback:function(t){e.$set(e.dataForm,"numberFriends",t)},expression:"dataForm.numberFriends"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"密码",prop:"password"}},[a("el-input",{attrs:{placeholder:"密码"},model:{value:e.dataForm.password,callback:function(t){e.$set(e.dataForm,"password",t)},expression:"dataForm.password"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"分组id",prop:"userGroupId"}},[a("el-input",{attrs:{placeholder:"分组id"},model:{value:e.dataForm.userGroupId,callback:function(t){e.$set(e.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"分组名称",prop:"userGroupName"}},[a("el-input",{attrs:{placeholder:"分组名称"},model:{value:e.dataForm.userGroupName,callback:function(t){e.$set(e.dataForm,"userGroupName",t)},expression:"dataForm.userGroupName"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"所属客服id",prop:"customerServiceId"}},[a("el-input",{attrs:{placeholder:"所属客服id"},model:{value:e.dataForm.customerServiceId,callback:function(t){e.$set(e.dataForm,"customerServiceId",t)},expression:"dataForm.customerServiceId"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"所属客服",prop:"customerService"}},[a("el-input",{attrs:{placeholder:"所属客服"},model:{value:e.dataForm.customerService,callback:function(t){e.$set(e.dataForm,"customerService",t)},expression:"dataForm.customerService"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"删除标志",prop:"deleteFlag"}},[a("el-input",{attrs:{placeholder:"删除标志"},model:{value:e.dataForm.deleteFlag,callback:function(t){e.$set(e.dataForm,"deleteFlag",t)},expression:"dataForm.deleteFlag"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"创建时间",prop:"createTime"}},[a("el-input",{attrs:{placeholder:"创建时间"},model:{value:e.dataForm.createTime,callback:function(t){e.$set(e.dataForm,"createTime",t)},expression:"dataForm.createTime"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"用户tokenid",prop:"userTokenId"}},[a("el-input",{attrs:{placeholder:"用户tokenid"},model:{value:e.dataForm.userTokenId,callback:function(t){e.$set(e.dataForm,"userTokenId",t)},expression:"dataForm.userTokenId"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},n=a("46Yf")(r,o,!1,null,null,null);t.default=n.exports},Ti2L:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-dialog",{attrs:{title:"账户购买","close-on-click-modal":!1,visible:e.visible},on:{"update:visible":function(t){e.visible=t}}},[a("el-form",{ref:"dataForm",attrs:{model:e.dataForm,rules:e.dataRule,"label-width":"80px"},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.dataFormSubmit()}}},[a("el-form-item",{attrs:{label:"注册国家",prop:"countryCode"}},[a("el-select",{attrs:{placeholder:"请选择注册国家",clearable:""},on:{change:e.productChangeHandler},model:{value:e.dataForm.countryCode,callback:function(t){e.$set(e.dataForm,"countryCode",t)},expression:"dataForm.countryCode"}},e._l(e.countryCodes,function(e){return a("el-option",{key:e.value,attrs:{label:e.value,value:e.value}})}))],1),e._v(" "),a("el-form-item",{attrs:{label:"价格",prop:"price"}},[a("el-row",[a("el-col",{attrs:{span:12}},[a("el-input",{attrs:{placeholder:"价格",disabled:"true"},model:{value:e.dataForm.price,callback:function(t){e.$set(e.dataForm,"price",t)},expression:"dataForm.price"}},[a("template",{attrs:{slot:"append"},slot:"append"},[e._v("U")])],2)],1)],1)],1),e._v(" "),a("el-form-item",{attrs:{label:"购买数量",prop:"orderNumber"}},[a("el-input",{attrs:{placeholder:"购买数量"},model:{value:e.dataForm.orderNumber,callback:function(t){e.$set(e.dataForm,"orderNumber",t)},expression:"dataForm.orderNumber"}})],1),e._v(" "),a("el-form-item",{attrs:{label:"当前库存",prop:"lineRegisterCount"}},[a("el-input",{attrs:{placeholder:"当前库存",disabled:"true"},model:{value:e.dataForm.lineRegisterCount,callback:function(t){e.$set(e.dataForm,"lineRegisterCount",t)},expression:"dataForm.lineRegisterCount"}})],1)],1),e._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(t){e.visible=!1}}},[e._v("取消")]),e._v(" "),a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dataFormSubmit()}}},[e._v("确定")])],1)],1)},staticRenderFns:[]},o=a("46Yf")({data:function(){return{visible:!1,options:[{value:1,label:"安卓"},{value:2,label:"IOS"}],countryValue:null,dataForm:{countryCode:null,productId:null,productName:null,lineRegisterCount:null,price:null,orderNumber:null},userGroupData:[],productInfoOptions:[],countryCodes:[],dataRule:{countryCode:[{required:!0,message:"注册国家不能为空",trigger:"blur"}],orderNumber:[{required:!0,message:"产品信息不能为空",trigger:"blur"}]}}},methods:{handleAvatarSuccess:function(e,t){this.dataForm.txtUrl=e.data},init:function(){this.visible=!0,this.getCountryCodeEnums(),this.productChangeHandler()},dataFormSubmit:function(){var e=this;this.$refs.dataForm.validate(function(t){t&&e.$confirm("确定购买数量:["+e.dataForm.orderNumber+"]?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atorders/createOrderToken"),method:"post",data:e.$http.adornData({countryCode:e.dataForm.countryCode,orderNumber:e.dataForm.orderNumber,productId:e.dataForm.productId})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){e.visible=!1,e.$emit("refreshDataList")}}):e.$message.error(a.msg)})})})},productChangeHandler:function(){this.queryProductBySearchWord(),this.queryLineRegisterCount()},queryProductBySearchWord:function(){var e=this;this.$http({url:this.$http.adornUrl("/ltt/productinfo/queryOnlyProduct"),method:"get",params:this.$http.adornParams({countryCode:this.dataForm.countryCode,productType:1,status:1})}).then(function(t){var a=t.data;a&&0===a.code&&null!=a.productInfo?(e.dataForm.productName=a.productInfo.productName,e.dataForm.price=a.productInfo.price,e.dataForm.productId=a.productInfo.productId):(e.dataForm.productName="",e.dataForm.price="",e.dataForm.productId="")})},queryLineRegisterCount:function(){var e=this;this.$http({url:this.$http.adornUrl("/ltt/cdlineregister/queryLineRegisterCount"),method:"get",params:this.$http.adornParams({countryCode:this.dataForm.countryCode})}).then(function(t){var a=t.data;a&&0===a.code&&null!=a.lineRegisterCount?e.dataForm.lineRegisterCount=a.lineRegisterCount:e.dataForm.lineRegisterCount=0})},getCountryCodeEnums:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(t){var a=t.data;a&&0===a.code?e.countryCodes=a.data:e.$message.error(a.msg)})}}},r,!1,null,null,null);t.default=o.exports},jGEJ:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var r=a("IHPB"),o=a.n(r),n=a("Sirn"),s=a("dwaA"),i=a("Ti2L"),l=a("Q1dw"),u=a("+N1c"),d={data:function(){return{userGroupOptions:[],customerUserOptions:[],countryCodes:[],statusOptions:[{value:1,label:"未验证"},{value:2,label:"封号"},{value:3,label:"下线"},{value:4,label:"在线"},{value:5,label:"数据错误"},{value:6,label:"已使用"},{value:7,label:"需要刷新token"}],validateOptions:[{value:0,label:"否"},{value:1,label:"是"}],dataForm:{id:null,nickName:"",nation:"",telephone:"",userGroupId:null,status:null,customerServiceId:null,validateFlag:null,userSource:null,selectLimit:null,tokenOpenStatus:null,tokenOpenTimeSort:null,registerCount:null,tokenOpenTime:null,tokenErrMsg:null},customerServiceField:"",dataList:[],atUserSourceCodes:[],openStatusCodes:[],pageIndex:1,pageSize:10,totalPage:0,dataListLoading:!1,dataListSelections:[],addOrUpdateVisible:!1,userImportVisible:!1,userTokenOrderVisible:!1,userTransferGroupVisible:!1,userCustomerVisible:!1,redPhoneCount:0}},components:{UserImport:s.default,UserTokenOrder:i.default,UserTransferGroup:l.default,UserCustomerGroup:u.default,AddOrUpdate:n.default},activated:function(){this.init(),this.getDataList(),this.badgeClass(),this.getAtUserSource(),this.getOpenStatus(),this.getCountryCodeEnums(),this.queryCustomerByFuzzyName(""),this.queryUserGroupBySearchWord("")},methods:{init:function(){this.dataForm.id=this.$route.query.userId},onSortChange:function(e){e.column;var t=e.prop,a=e.order;console.log("Sort by",t,"with order",a),this.dataForm.tokenOpenTimeSort=null==a?null:"ascending"===a?0:1,this.getDataList()},getCountryCodeEnums:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/countryCodes"),method:"get"}).then(function(t){var a=t.data;a&&0===a.code?e.countryCodes=a.data:e.$message.error(a.msg)})},getDataList:function(e){var t=this;this.dataListLoading=!0,null!=this.dataForm.selectLimit&&this.dataForm.selectLimit>0&&(this.pageIndex=1,this.pageSize=this.dataForm.selectLimit),null!=e&&e>0&&(this.pageIndex=1),this.$http({url:this.$http.adornUrl("/ltt/atuser/list"),method:"get",params:this.$http.adornParams({page:this.pageIndex,limit:this.pageSize,nickName:this.dataForm.nickName,nation:this.dataForm.nation,telephone:this.dataForm.telephone,userGroupId:this.dataForm.userGroupId,status:this.dataForm.status,customerServiceId:this.dataForm.customerServiceId,customerService:this.dataForm.customerService,validateFlag:this.dataForm.validateFlag,userSource:this.dataForm.userSource,id:this.dataForm.id,selectLimit:this.dataForm.selectLimit,tokenOpenStatus:this.dataForm.tokenOpenStatus,tokenOpenTimeSort:this.dataForm.tokenOpenTimeSort,registerCount:this.dataForm.registerCount})}).then(function(e){var a=e.data;a&&0===a.code?(t.dataList=a.page.list,t.totalPage=a.page.totalCount):(t.dataList=[],t.totalPage=0),t.dataListLoading=!1})},badgeClass:function(e){return null===e?"green-badge":!0===e?"green-badge":!1===e?"red-badge":void 0},copyPhoneHandle:function(e){var t=this,a=e?[e.trim()]:this.dataListSelections.map(function(e){return e.telephone.trim()});navigator.clipboard.writeText(a).then(function(){t.$message.success("手机号复制成功！")})},getBadgeType:function(e){return null===e?"":1===e?"success":2===e?"warning":e>=3?"danger":""},sizeChangeHandle:function(e){this.pageSize=e,this.pageIndex=1,this.getDataList()},currentChangeHandle:function(e){this.pageIndex=e,this.getDataList()},selectionChangeHandle:function(e){this.dataListSelections=e},addOrUpdateHandle:function(e){var t=this;this.addOrUpdateVisible=!0,this.$nextTick(function(){t.$refs.addOrUpdate.init(e)})},userImportHandle:function(e){var t=this;this.userImportVisible=!0,this.$nextTick(function(){t.$refs.userImport.init(e)})},userTokenOrderHandle:function(e){var t=this;this.userTokenOrderVisible=!0,this.$nextTick(function(){t.$refs.userTokenOrder.init(e)})},userTransferGroupHandle:function(e){var t=this;this.userTransferGroupVisible=!0;var a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.phoneRed();var r=a.length-this.redPhoneCount;this.$nextTick(function(){t.$refs.userTransferGroup.init(a,t.redPhoneCount,r)})},phoneRed:function(){var e=this.dataListSelections.filter(function(e){return!1===e.phoneState}).map(function(e){return e.phoneState});this.redPhoneCount=e.length},userCustomerHandle:function(e){var t=this;this.userCustomerVisible=!0;var a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对所选项进行客服分配操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$nextTick(function(){t.$refs.userCustomerGroup.init(a)})})},userValidateHandle:function(){var e=this,t=this.dataListSelections.map(function(e){return e.id}),a=t.length>0?0:1;this.$confirm("确定要对["+(a?"所有":"勾选")+"]账户进行验活操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atuser/validateUserStatus"),method:"post",data:e.$http.adornData({ids:t,validateFlag:a})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功，正在进行验活操作，请稍后",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},maintainUserHandle:function(){var e=this,t=this.dataListSelections.map(function(e){return e.id});this.$confirm("确定要对勾选账户进行 养号 操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){e.$http({url:e.$http.adornUrl("/ltt/atuser/maintainUser"),method:"post",data:e.$http.adornData({ids:t,validateFlag:!1})}).then(function(t){var a=t.data;a&&0===a.code?e.$message({message:"操作成功，正在进行养号操作，请稍后",type:"success",duration:1500,onClose:function(){e.getDataList()}}):e.$message.error(a.msg)})})},importTokenHandle:function(){var e=this,t=this.dataListSelections.map(function(e){return e.id});this.$confirm("确定要对勾选账户token导出?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){window.open(e.$http.adornUrl("/ltt/atuser/importToken?token="+e.$cookie.get("token")+"&ids="+t))})},getFilename:function(e){return e.substring(e.lastIndexOf("/")+1)},deleteHandle:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对[id="+a.join(",")+"]进行["+(e?"删除":"批量删除")+"]操作?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/atuser/delete"),method:"post",data:t.$http.adornData(a,!1)}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})})},cleanBlockData:function(e){var t=this,a=e?[e]:this.dataListSelections.map(function(e){return e.id});this.$confirm("确定对封号数据进行清理?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(function(){t.$http({url:t.$http.adornUrl("/ltt/atuser/cleanBlockData"),method:"post",data:t.$http.adornData(a,!1)}).then(function(e){var a=e.data;a&&0===a.code?t.$message({message:"操作成功",type:"success",duration:1500,onClose:function(){t.getDataList()}}):t.$message.error(a.msg)})})},queryUserGroupBySearchWord:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/atusergroup/queryByFuzzyName?searchWord="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.userGroupOptions=[{id:0,name:"未分组"}].concat(o()(a.groupList)))})},queryCustomerByFuzzyName:function(e){var t=this;e=null==e?"":e+"",this.$http({url:this.$http.adornUrl("/ltt/customeruser/queryCustomerByFuzzyName?key="+e),method:"get",params:this.$http.adornParams()}).then(function(e){var a=e.data;a&&0===a.code&&(t.customerUserOptions=[{userId:0,nickname:"未分配"}].concat(o()(a.customerList)))})},getAtUserSource:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/getAtUserSource"),method:"get"}).then(function(t){var a=t.data;a&&0===a.code?e.atUserSourceCodes=a.data:e.$message.error(a.msg)})},getOpenStatus:function(){var e=this;this.$http({url:this.$http.adornUrl("/app/enums/getOpenStatus"),method:"get"}).then(function(t){var a=t.data;a&&0===a.code?e.openStatusCodes=a.data:e.$message.error(a.msg)})},clearSearch:function(){this.dataForm.id=null,this.dataForm.nickName=null,this.dataForm.nation=null,this.dataForm.telephone=null,this.dataForm.userGroupId=null,this.dataForm.status=null,this.dataForm.customerServiceId=null,this.dataForm.customerService=null,this.dataForm.validateFlag=null,this.dataForm.userSource=null,this.dataForm.selectLimit=null,this.dataForm.tokenOpenStatus=null,this.dataForm.tokenOpenTimeSort=null,this.dataForm.registerCount=null,this.pageSize=10,this.getDataList(1)}}},c={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"mod-config"},[a("el-form",{attrs:{inline:!0,model:e.dataForm},nativeOn:{keyup:function(t){if(!("button"in t)&&e._k(t.keyCode,"enter",13,t.key))return null;e.getDataList(1)}}},[a("el-form-item",[a("el-input",{attrs:{placeholder:"id",clearable:""},model:{value:e.dataForm.id,callback:function(t){e.$set(e.dataForm,"id",t)},expression:"dataForm.id"}})],1),e._v(" "),a("el-form-item",[a("el-select",{attrs:{placeholder:"拉群号国家",clearable:""},model:{value:e.dataForm.nation,callback:function(t){e.$set(e.dataForm,"nation",t)},expression:"dataForm.nation"}},e._l(e.countryCodes,function(e){return a("el-option",{key:e.value,attrs:{label:e.value,value:e.value}})}))],1),e._v(" "),a("el-form-item",[a("el-input",{attrs:{placeholder:"手机号",clearable:""},model:{value:e.dataForm.telephone,callback:function(t){e.$set(e.dataForm,"telephone",t)},expression:"dataForm.telephone"}})],1),e._v(" "),a("el-form-item",[a("el-input",{attrs:{placeholder:"卡注册次数",clearable:""},model:{value:e.dataForm.registerCount,callback:function(t){e.$set(e.dataForm,"registerCount",t)},expression:"dataForm.registerCount"}})],1),e._v(" "),a("el-form-item",[a("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择分组","remote-method":e.queryUserGroupBySearchWord},model:{value:e.dataForm.userGroupId,callback:function(t){e.$set(e.dataForm,"userGroupId",t)},expression:"dataForm.userGroupId"}},e._l(e.userGroupOptions,function(e){return a("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})}))],1),e._v(" "),a("el-form-item",[a("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{clearable:"",placeholder:"账户状态"},model:{value:e.dataForm.status,callback:function(t){e.$set(e.dataForm,"status",t)},expression:"dataForm.status"}},e._l(e.statusOptions,function(e){return a("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),a("el-form-item",[a("el-select",{attrs:{filterable:"",clearable:"",remote:"",placeholder:"选择客服","remote-method":e.queryCustomerByFuzzyName},model:{value:e.dataForm.customerServiceId,callback:function(t){e.$set(e.dataForm,"customerServiceId",t)},expression:"dataForm.customerServiceId"}},e._l(e.customerUserOptions,function(e){return a("el-option",{key:e.userId,attrs:{label:e.nickname,value:e.userId}})})),e._v(" "),a("el-form-item",[a("el-select",{staticClass:"m-2",staticStyle:{width:"240px"},attrs:{clearable:"",placeholder:"是否有验活记录"},model:{value:e.dataForm.validateFlag,callback:function(t){e.$set(e.dataForm,"validateFlag",t)},expression:"dataForm.validateFlag"}},e._l(e.validateOptions,function(e){return a("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})}))],1),e._v(" "),a("el-form-item",[a("el-select",{attrs:{placeholder:"账户来源",clearable:""},model:{value:e.dataForm.userSource,callback:function(t){e.$set(e.dataForm,"userSource",t)},expression:"dataForm.userSource"}},e._l(e.atUserSourceCodes,function(e){return a("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),a("el-form-item",[a("el-select",{attrs:{placeholder:"打开状态",clearable:""},model:{value:e.dataForm.tokenOpenStatus,callback:function(t){e.$set(e.dataForm,"tokenOpenStatus",t)},expression:"dataForm.tokenOpenStatus"}},e._l(e.openStatusCodes,function(e){return a("el-option",{key:e.key,attrs:{label:e.value,value:e.key}})}))],1),e._v(" "),a("el-form-item",[a("el-input",{attrs:{placeholder:"查询条数",clearable:""},model:{value:e.dataForm.selectLimit,callback:function(t){e.$set(e.dataForm,"selectLimit",t)},expression:"dataForm.selectLimit"}})],1),e._v(" "),a("el-button",{on:{click:function(t){e.getDataList(1)}}},[e._v("查询")]),e._v(" "),a("el-button",{attrs:{type:"info"},on:{click:function(t){e.clearSearch()}}},[e._v("清除搜索条件")]),e._v(" "),a("div",[e.isAuth("ltt:atuser:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.userImportHandle()}}},[e._v("账户导入\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?a("el-button",{attrs:{type:"primary"},on:{click:function(t){e.userTokenOrderHandle()}}},[e._v("账户购买\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?a("el-button",{attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.userTransferGroupHandle()}}},[e._v("转移分组\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?a("el-button",{attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.importTokenHandle()}}},[e._v("token导出\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?a("el-button",{attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.userCustomerHandle()}}},[e._v("分配客服\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?a("el-button",{staticStyle:{"margin-top":"15px"},attrs:{type:"primary"},on:{click:function(t){e.userValidateHandle()}}},[e._v("验活账号\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?a("el-button",{staticStyle:{"margin-top":"15px"},attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.maintainUserHandle()}}},[e._v("养号\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:delete")?a("el-button",{staticStyle:{"margin-top":"15px"},attrs:{type:"danger",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.deleteHandle()}}},[e._v("批量删除\n      ")]):e._e(),e._v(" "),e.isAuth("ltt:atuser:save")?a("el-button",{staticStyle:{"margin-top":"15px"},attrs:{type:"danger"},on:{click:function(t){e.cleanBlockData()}}},[e._v("清理封号\n      ")]):e._e(),e._v(" "),a("el-button",{attrs:{type:"primary",disabled:e.dataListSelections.length<=0},on:{click:function(t){e.copyPhoneHandle()}}},[e._v("复制手机号\n      ")])],1)],1)],1),e._v(" "),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.dataListLoading,expression:"dataListLoading"}],staticStyle:{width:"100%"},attrs:{data:e.dataList,border:""},on:{"sort-change":e.onSortChange,"selection-change":e.selectionChangeHandle}},[a("el-table-column",{attrs:{type:"selection","header-align":"center",align:"center",width:"50"}}),e._v(" "),a("el-table-column",{attrs:{prop:"id","header-align":"center",align:"center",label:"id"}}),e._v(" "),a("el-table-column",{attrs:{prop:"nation","header-align":"center",align:"center",label:"国家"}}),e._v(" "),a("el-table-column",{attrs:{prop:"telephone","header-align":"center",align:"center",width:"150px",label:"电话"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-badge",{staticClass:"item",class:e.badgeClass(t.row.phoneState),attrs:{value:t.row.registerCount}},[a("el-button",{attrs:{size:"text"},on:{click:function(a){e.copyPhoneHandle(t.row.telephone)}}},[e._v(e._s(t.row.telephone))])],1)]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"nickName","header-align":"center",align:"center",label:"昵称"}}),e._v(" "),a("el-table-column",{attrs:{prop:"userGroupName","header-align":"center",align:"center",label:"分组名称"}}),e._v(" "),a("el-table-column",{attrs:{prop:"userSource","header-align":"center",align:"center",label:"账号来源"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.atUserSourceCodes,function(r){return t.row.userSource===r.key?a("el-tag",{key:r.key},[e._v("\n          "+e._s(r.value)+"\n        ")]):e._e()})}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"status","header-align":"center",align:"center",label:"状态"},scopedSlots:e._u([{key:"default",fn:function(t){return[1===t.row.status?a("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("未验证")]):2===t.row.status?a("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("封号")]):3===t.row.status?a("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("下线")]):4===t.row.status?a("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("在线")]):6===t.row.status?a("el-tag",{attrs:{size:"small",type:"danger"}},[e._v("已使用")]):a("el-tag",{attrs:{size:"small"}},[e._v("数据错误")])]}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"createTime","header-align":"center",align:"center",label:"创建时间"}}),e._v(" "),a("el-table-column",{attrs:{prop:"tokenOpenStatus","header-align":"center",align:"center",label:"打开状态"},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.openStatusCodes,function(r){return t.row.tokenOpenStatus===r.key?a("el-tag",{key:r.key},[e._v("\n          "+e._s(r.value)+"\n        ")]):e._e()})}}])}),e._v(" "),a("el-table-column",{attrs:{prop:"tokenOpenTime","header-align":"center",align:"center",sortable:"",label:"打开时间"}}),e._v(" "),a("el-table-column",{attrs:{prop:"status","header-align":"center",align:"center",label:"日志信息"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v("\n        "+e._s(t.row.msg)+"/"+e._s(t.row.tokenErrMsg)+"\n      ")]}}])}),e._v(" "),a("el-table-column",{attrs:{fixed:"right","header-align":"center",align:"center",width:"150",label:"操作"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.addOrUpdateHandle(t.row.id)}}},[e._v("修改")]),e._v(" "),a("el-button",{attrs:{type:"text",size:"small"},on:{click:function(a){e.deleteHandle(t.row.id)}}},[e._v("删除")])]}}])})],1),e._v(" "),a("el-pagination",{attrs:{"current-page":e.pageIndex,"page-sizes":[10,20,30,50,100,500,1e3],"page-size":e.pageSize,total:e.totalPage,layout:"total, sizes, prev, pager, next, jumper"},on:{"size-change":e.sizeChangeHandle,"current-change":e.currentChangeHandle}}),e._v(" "),e.addOrUpdateVisible?a("add-or-update",{ref:"addOrUpdate",on:{refreshDataList:e.getDataList}}):e._e(),e._v(" "),e.userImportVisible?a("user-import",{ref:"userImport",on:{refreshDataList:e.getDataList}}):e._e(),e._v(" "),e.userTokenOrderVisible?a("user-token-order",{ref:"userTokenOrder",on:{refreshDataList:e.getDataList}}):e._e(),e._v(" "),e.userTransferGroupVisible?a("user-transfer-group",{ref:"userTransferGroup",on:{refreshDataList:e.getDataList}}):e._e(),e._v(" "),e.userCustomerVisible?a("user-customer-group",{ref:"userCustomerGroup",on:{refreshDataList:e.getDataList}}):e._e()],1)},staticRenderFns:[]};var m=a("46Yf")(d,c,!1,function(e){a("lJ4/")},"data-v-53bdef7c",null);t.default=m.exports},"lJ4/":function(e,t,a){var r=a("uchH");"string"==typeof r&&(r=[[e.i,r,""]]),r.locals&&(e.exports=r.locals);a("XkoO")("d090c9a4",r,!0)},uchH:function(e,t,a){(e.exports=a("acE3")(!1)).push([e.i,"\n.item[data-v-53bdef7c] {\n  margin-top: 10px;\n}\n\n/* 定义绿色背景样式 */\n[data-v-53bdef7c].green-badge .el-badge__content {\n  background-color: green;\n  color: white;\n}\n\n/* 定义红色背景样式 */\n[data-v-53bdef7c].red-badge .el-badge__content {\n  background-color: red;\n  color: white;\n}\n\n",""])}});