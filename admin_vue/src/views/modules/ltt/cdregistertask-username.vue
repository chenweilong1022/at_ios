<template>
  <el-dialog
    :title="'增加注册昵称'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <div v-if="this.dataListFlag == false">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm"
               label-width="80px">
        <el-form-item label="国家">
          <el-select v-model="dataForm.countryCode" placeholder="国家" clearable>
            <el-option
              v-for="item in countryCodes"
              :key="item.key"
              :label="item.value"
              :value="item.key">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称（换行传入）" prop="nickname">
          <el-input :autosize="{ minRows: 20, maxRows: 20}" type="textarea" v-model="dataForm.nickname"
                    placeholder="静态ip"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="cancelHandel()">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
    </div>
    <div v-if="this.dataListFlag == true">

      <el-form :inline="true" :model="dataForm">
        <el-form-item>
          <el-select v-model="dataForm.countryCode" placeholder="国家" clearable>
            <el-option
              v-for="item in countryCodes"
              :key="item.key"
              :label="item.value"
              :value="item.key">
            </el-option>
          </el-select>
        </el-form-item>
        <el-button @click="listRegisterNickname()" type="primary">查询</el-button>
        <el-button @click="addNickname()" type="success">新增</el-button>
        <el-button @click="deleteRegisterNickname()" type="danger">删除此国家</el-button>
      </el-form>

      <div>
        <div style="font-size: 25px; font-weight: bold; margin-bottom: 20px; margin-top: 10px">
          总条数:<div style="color: #17B3A3;display: inline;margin-right: 10px;">{{this.totalCount}}</div>
        </div>
        <div class="container">
          <!-- 使用 v-for 指令循环 dataList 并展示每个字符串 -->
          <div
            v-for="(item, index) in dataList"
            :key="index"
            class="tag-style"
          >
            {{ item }}
          </div>
        </div>
      </div>

    </div>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      countryCodes: [],
      dataListFlag: true,
      dataList: [],
      totalCount: 0,
      dataForm: {
        id: 0,
        nickname: '',
        countryCode: null
      },
      dataRule: {
        ip: [
          {required: true, message: '不能为空', trigger: 'blur'}
        ],
        countryCode: [
          {required: true, message: '国家不能为空', trigger: 'blur'}
        ],
        nickname: [
          {required: true, message: '昵称不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    // 表单提交
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
    },
    init(id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.getCountryCodeEnums()
      this.listRegisterNickname()
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/ltt/cdlineregister/saveRegisterNickname`),
            method: 'post',
            data: this.$http.adornData({
              'nickname': this.dataForm.nickname,
              'countryCode': this.dataForm.countryCode
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.listRegisterNickname()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    },
    // 查询列表
    listRegisterNickname() {
      if (this.dataForm.countryCode == null) {
        this.dataForm.countryCode = 81
      }
      this.$http({
        url: this.$http.adornUrl(`/ltt/cdlineregister/listRegisterNickname`),
        method: 'get',
        params: this.$http.adornParams({
          'countryCode': this.dataForm.countryCode
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          console.log(data.page)
          this.dataList = data.page.list
          this.totalCount = data.page.totalCount
        } else {
          this.dataList = []
          this.totalCount = 0
        }
        if (this.dataList.length > 0) {
          this.dataListFlag = true
        }
      })
    },
    // 删除昵称集合
    deleteRegisterNickname() {
      this.$http({
        url: this.$http.adornUrl(`/ltt/cdlineregister/deleteRegisterNickname`),
        method: 'get',
        params: this.$http.adornParams({
          'countryCode': this.dataForm.countryCode
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.dataListFlag = true
              this.listRegisterNickname()
            }
          })
        } else {
          this.$message.error(data.msg)
        }
      })
    },
    // 新增窗口打开
    addNickname() {
      this.dataListFlag = false
      this.dataForm.nickname = ''
    },
    cancelHandel () {
      this.dataListFlag = true
    }
  }
}
</script>
<style scoped>
.container {
  display: flex;          /* 使用Flexbox布局 */
  flex-wrap: wrap;        /* 允许换行 */
  align-items: center;    /* 垂直居中对齐 */
  justify-content: start;/* 水平开始对齐 */
}

.tag-style {
  margin: 5px;            /* 外边距 */
  padding: 8px 15px;      /* 内边距 */
  border-radius: 15px;    /* 圆角边框 */
  background-color: #ebeef5; /* 背景色 */
  color: #606266;        /* 文字颜色 */
  font-size: 14px;       /* 字体大小 */
  white-space: nowrap;   /* 防止文本换行 */
  cursor: pointer;        /* 鼠标悬停时显示指针手势 */
  transition: all 0.3s;   /* 平滑过渡效果 */
}

.tag-style:hover {
  background-color: #409eff; /* 鼠标悬浮时的背景色 */
  color: white;            /* 鼠标悬浮时的文字颜色 */
}
</style>
