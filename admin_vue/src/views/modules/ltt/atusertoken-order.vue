<template>
  <el-dialog
    :title="'账户购买'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <!--    <el-form-item label="平台" prop="platform">-->
      <!--      <el-select-->
      <!--        v-model="dataForm.platform"-->
      <!--        class="m-2"-->
      <!--        placeholder="Select"-->
      <!--        size="large"-->
      <!--        style="width: 240px">-->
      <!--        <el-option-->
      <!--          v-for="item in options"-->
      <!--          :key="item.value"-->
      <!--          :label="item.label"-->
      <!--          :value="item.value"/>-->
      <!--      </el-select>-->
      <!--    </el-form-item>-->
      <!--      <el-form-item label="账号分组" prop="userGroupId">-->
      <!--        <el-select-->
      <!--          v-model="dataForm.userGroupId"-->
      <!--          class="m-2"-->
      <!--          placeholder="Select"-->
      <!--          size="large"-->
      <!--          style="width: 240px">-->
      <!--          <el-option-->
      <!--            v-for="item in userGroupData"-->
      <!--            :key="item.id"-->
      <!--            :label="item.name"-->
      <!--            :value="item.id"-->
      <!--          />-->
      <!--        </el-select>-->
      <!--      </el-form-item>-->
      <el-form-item label="注册国家" prop="countryCode">
        <el-select v-model="dataForm.countryCode" placeholder="请选择注册国家" clearable @change="productChangeHandler">
          <el-option
            v-for="item in countryCodes"
            :key="item.value"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="dataForm.productName" placeholder="商品名称" disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="商品价格" prop="price">
        <el-input v-model="dataForm.price" placeholder="商品价格" disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="购买数量" prop="orderNumber">
        <el-input v-model="dataForm.orderNumber" placeholder="购买数量"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      options: [
        {
          value: 1,
          label: '安卓'
        },
        {
          value: 2,
          label: 'IOS'
        }
      ],
      countryValue: null,
      dataForm: {
        countryCode: null,
        productId: null,
        productName: null,
        price: null,
        orderNumber: null
      },
      userGroupData: [],
      productInfoOptions: [],
      countryCodes: [],
      dataRule: {
        countryCode: [
          {required: true, message: '注册国家不能为空', trigger: 'blur'}
        ],
        orderNumber: [
          {required: true, message: '产品信息不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.dataForm.txtUrl = res.data
    },
    init() {
      this.visible = true
      this.getCountryCodeEnums()
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$confirm(`确定购买[${this.countryValue}] 数量:[${this.dataForm.orderNumber}]?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atorders/createOrderToken`),
              method: 'post',
              data: this.$http.adornData({
                'countryCode': this.dataForm.countryCode,
                'orderNumber': this.dataForm.orderNumber,
                'productId': this.dataForm.productId
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          })
        }
      })
    },
    productChangeHandler() {
      this.countryCodes.find((item) => {
        this.countryValue = item.value
      })
      this.queryProductBySearchWord()
    },
    queryProductBySearchWord() {
      this.$http({
        url: this.$http.adornUrl(`/ltt/productinfo/queryOnlyProduct`),
        method: 'get',
        params: this.$http.adornParams({
          'countryCode': this.dataForm.countryCode,
          'productType': 1, // 1代表token
          'status': 1
        })
      }).then(({data}) => {
        if (data && data.code === 0 && data.productInfo != null) {
          this.dataForm.productName = data.productInfo.productName
          this.dataForm.price = data.productInfo.price
          this.dataForm.productId = data.productInfo.productId
        } else {
          this.dataForm.productName = ''
          this.dataForm.price = ''
          this.dataForm.productId = ''
        }
      })
    },
    getCountryCodeEnums() {
      this.$http({
        url: this.$http.adornUrl(`/app/enums/countryCodes`),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.countryCodes = data.data
        } else {
          this.$message.error(data.msg)
        }
      })
    }
  }
}
</script>
