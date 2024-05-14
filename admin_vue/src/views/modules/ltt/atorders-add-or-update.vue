<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="用户ID" prop="sysUserId">
      <el-input v-model="dataForm.sysUserId" placeholder="用户ID"></el-input>
    </el-form-item>
    <el-form-item label="订单状态（待处理，处理中，已完成）" prop="orderStatus">
      <el-input v-model="dataForm.orderStatus" placeholder="订单状态（待处理，处理中，已完成）"></el-input>
    </el-form-item>
    <el-form-item label="订单总金额" prop="totalAmount">
      <el-input v-model="dataForm.totalAmount" placeholder="订单总金额"></el-input>
    </el-form-item>
    <el-form-item label="订单创建时间" prop="orderTime">
      <el-input v-model="dataForm.orderTime" placeholder="订单创建时间"></el-input>
    </el-form-item>
    <el-form-item label="订单最后更新时间" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="订单最后更新时间"></el-input>
    </el-form-item>
    <el-form-item label="订单备注" prop="notes">
      <el-input v-model="dataForm.notes" placeholder="订单备注"></el-input>
    </el-form-item>
    <el-form-item label="商品id" prop="productId">
      <el-input v-model="dataForm.productId" placeholder="商品id"></el-input>
    </el-form-item>
    <el-form-item label="商品类型" prop="productType">
      <el-input v-model="dataForm.productType" placeholder="商品类型"></el-input>
    </el-form-item>
    <el-form-item label="国家code" prop="countryCode">
      <el-input v-model="dataForm.countryCode" placeholder="国家code"></el-input>
    </el-form-item>
    <el-form-item label="购买数量" prop="orderNumber">
      <el-input v-model="dataForm.orderNumber" placeholder="购买数量"></el-input>
    </el-form-item>
    <el-form-item label="成功" prop="successNumber">
      <el-input v-model="dataForm.successNumber" placeholder="购买数量"></el-input>
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
    data () {
      return {
        visible: false,
        dataForm: {
          orderId: 0,
          sysUserId: '',
          orderStatus: '',
          totalAmount: '',
          orderTime: '',
          updateTime: '',
          notes: '',
          productId: '',
          productType: '',
          countryCode: '',
          orderNumber: ''
        },
        dataRule: {
          sysUserId: [
            { required: true, message: '用户ID不能为空', trigger: 'blur' }
          ],
          orderStatus: [
            { required: true, message: '订单状态（待处理，处理中，已完成）不能为空', trigger: 'blur' }
          ],
          totalAmount: [
            { required: true, message: '订单总金额不能为空', trigger: 'blur' }
          ],
          orderTime: [
            { required: true, message: '订单创建时间不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '订单最后更新时间不能为空', trigger: 'blur' }
          ],
          notes: [
            { required: true, message: '订单备注不能为空', trigger: 'blur' }
          ],
          productId: [
            { required: true, message: '商品id不能为空', trigger: 'blur' }
          ],
          productType: [
            { required: true, message: '商品类型不能为空', trigger: 'blur' }
          ],
          countryCode: [
            { required: true, message: '国家code不能为空', trigger: 'blur' }
          ],
          orderNumber: [
            { required: true, message: '购买数量不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.orderId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.orderId) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atorders/info/${this.dataForm.orderId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.sysUserId = data.atorders.sysUserId
                this.dataForm.orderStatus = data.atorders.orderStatus
                this.dataForm.totalAmount = data.atorders.totalAmount
                this.dataForm.orderTime = data.atorders.orderTime
                this.dataForm.updateTime = data.atorders.updateTime
                this.dataForm.notes = data.atorders.notes
                this.dataForm.productId = data.atorders.productId
                this.dataForm.productType = data.atorders.productType
                this.dataForm.countryCode = data.atorders.countryCode
                this.dataForm.orderNumber = data.atorders.orderNumber
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/atorders/${!this.dataForm.orderId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'orderId': this.dataForm.orderId || undefined,
                'sysUserId': this.dataForm.sysUserId,
                'orderStatus': this.dataForm.orderStatus,
                'totalAmount': this.dataForm.totalAmount,
                'orderTime': this.dataForm.orderTime,
                'updateTime': this.dataForm.updateTime,
                'notes': this.dataForm.notes,
                'productId': this.dataForm.productId,
                'productType': this.dataForm.productType,
                'countryCode': this.dataForm.countryCode,
                'orderNumber': this.dataForm.orderNumber
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
          }
        })
      }
    }
  }
</script>
