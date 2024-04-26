<template>
  <div class="mod-home">
    <div class="summary" style="display: flex;flex-direction: row;align-items: center;width: 113%">
      <div class="summary-item">line库存:
        <div style="color: #007BFF;display: inline;"> {{ dataForm.registerStock }}</div>
      </div>
      <div class="summary-item">今日注册line:
        <div style="color: #28a745;display: inline;"> {{ dataForm.todayRegisterNum }}</div>
      </div>
      <div class="summary-item">今日已使用账户:
        <div style="color: #dc3545;display: inline;">{{ dataForm.usedUserStock }}</div>
      </div>
      <div class="summary-item">在线账户:
        <div style="color: #ffc107;display: inline;"> {{ dataForm.onlineUserNum }}</div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  data() {
    return {
      dataForm: {
        registerStock: 0,
        todayRegisterNum: 0,
        usedUserStock: 0,
        onlineUserNum: 0
      }
    }
  },
  activated() {
    this.queryLineRegisterCount()
    this.queryUserSummary()
  },
  methods: {
    queryLineRegisterCount() {
      this.$http({
        url: this.$http.adornUrl(`/ltt/cdlineregister/queryLineRegisterSummary`),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0 && data.lineRegisterSummary != null) {
          this.dataForm.registerStock = data.lineRegisterSummary.registerStock
          this.dataForm.todayRegisterNum = data.lineRegisterSummary.todayRegisterNum
        } else {
          this.dataForm.registerStock = 0
          this.dataForm.todayRegisterNum = 0
        }
      })
    },
    queryUserSummary() {
      this.$http({
        url: this.$http.adornUrl(`/ltt/atuser/queryUserSummary`),
        method: 'get'
      }).then(({data}) => {
        if (data && data.code === 0 && data.userSummary != null) {
          this.dataForm.usedUserStock = data.userSummary.usedUserStock
          this.dataForm.onlineUserNum = data.userSummary.onlineUserNum
        } else {
          this.dataForm.usedUserStock = 0
          this.dataForm.onlineUserNum = 0
        }
      })
    }
  }
}
</script>

<style scoped>
.mod-home {
  line-height: 1.5;
}

.summary {
  //设置容器为 Flexbox 布局
  display: flex;
  background: #f8f9fa;
  text-align: center;
  //在容器的内部边界添加填充
  padding: 20px;
  //给容器添加圆角。
  border-radius: 8px;
  //添加轻微的阴影效果，以提高层次感
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow-x: auto; /* 如果内容太多，出现水平滚动条 */
  border: 2px solid #dcdcdc; /* 明显的边框 */
  box-shadow: 0 4px 8px rgba(0,0,0,0.15); /* 更深的阴影效果 */
  margin: 10px;         /* 添加外边距避免模块紧贴在一起 */
  min-height: 100px;
  width: 100%; /* 全宽度使得居中更为明显，也可根据实际需要调整宽度 */
}

.summary-item {
  width: 80%; /* 全宽度使得居中更为明显，也可根据实际需要调整宽度 */
  margin: 10px 0; /* 添加上下间隔以清晰区分各项 */
  margin: 10px 10px; /* Optional: Add margins to each item for better spacing */
  padding: 20px 20px;  /* 增大内边距，使文字与边框之间的空间更大 */
  font-size: 18px;     /* 特别大的字体大小 */
  font-weight: bold;
  text-align: center;
  justify-content: center; /* 水平居中 */
  flex-direction: row;
  min-height: 80px;
  align-items: center; /* 垂直居中所有内容 */
  display: inline;
}

.summary-item > div {
  display: inline-block; /* 改用 inline-block 以适应 flex 对齐 */
  color: inherit; /* 从父元素继承颜色，这一行可有可无，具体看你的设计需求 */
}
</style>

