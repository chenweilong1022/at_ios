<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="商品名称" prop="productName">
      <el-input v-model="dataForm.productName" placeholder="商品名称"></el-input>
    </el-form-item>
    <el-form-item label="商品描述" prop="description">
      <el-input v-model="dataForm.description" placeholder="商品描述"></el-input>
    </el-form-item>
    <el-form-item label="商品价格" prop="price">
      <el-input v-model="dataForm.price" placeholder="商品价格"></el-input>
    </el-form-item>
    <el-form-item label="库存数量" prop="stockQuantity">
      <el-input v-model="dataForm.stockQuantity" placeholder="库存数量"></el-input>
    </el-form-item>
<!--    <el-form-item label="所属分类" prop="categoryType">-->
<!--      <el-input v-model="dataForm.categoryType" placeholder="所属分类"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="商品图片URL地址" prop="imageUrl">-->
<!--      <el-input v-model="dataForm.imageUrl" placeholder="商品图片URL地址"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="商品状态（1上架 0下架）" prop="status">-->
<!--      <el-input v-model="dataForm.status" placeholder="商品状态（1上架 0下架）"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="创建时间" prop="createTime">-->
<!--      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item label="最后更新时间" prop="updateTime">-->
<!--      <el-input v-model="dataForm.updateTime" placeholder="最后更新时间"></el-input>-->
<!--    </el-form-item>-->
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
          productId: 0,
          productName: '',
          description: '',
          price: '',
          stockQuantity: '',
          categoryType: '',
          imageUrl: '',
          status: '',
          createTime: '',
          updateTime: ''
        },
        dataRule: {
          productName: [
            { required: true, message: '商品名称不能为空', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '商品描述不能为空', trigger: 'blur' }
          ],
          price: [
            { required: true, message: '商品价格不能为空', trigger: 'blur' }
          ],
          stockQuantity: [
            { required: true, message: '库存数量不能为空', trigger: 'blur' }
          ],
          categoryType: [
            { required: true, message: '所属分类不能为空', trigger: 'blur' }
          ],
          imageUrl: [
            { required: true, message: '商品图片URL地址不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '商品状态（1上架 0下架）不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ],
          updateTime: [
            { required: true, message: '最后更新时间不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.productId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.productId) {
            this.$http({
              url: this.$http.adornUrl(`/ltt/productinfo/info/${this.dataForm.productId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.productName = data.productinfo.productName
                this.dataForm.description = data.productinfo.description
                this.dataForm.price = data.productinfo.price
                this.dataForm.stockQuantity = data.productinfo.stockQuantity
                this.dataForm.categoryType = data.productinfo.categoryType
                this.dataForm.imageUrl = data.productinfo.imageUrl
                this.dataForm.status = data.productinfo.status
                this.dataForm.createTime = data.productinfo.createTime
                this.dataForm.updateTime = data.productinfo.updateTime
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
              url: this.$http.adornUrl(`/ltt/productinfo/${!this.dataForm.productId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'productId': this.dataForm.productId || undefined,
                'productName': this.dataForm.productName,
                'description': this.dataForm.description,
                'price': this.dataForm.price,
                'stockQuantity': this.dataForm.stockQuantity,
                'categoryType': this.dataForm.categoryType,
                'imageUrl': this.dataForm.imageUrl,
                'status': this.dataForm.status,
                'createTime': this.dataForm.createTime,
                'updateTime': this.dataForm.updateTime
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
