<template>
  <div class="app-conntainer">

    <div class="group-container">
      <el-form :inline="true">
        <el-form-item>
          <el-input placeholder="参数名" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button>查询</el-button>
        </el-form-item>
      </el-form>
      <el-row class="t-header-row">
        <el-col :span="12" class="title">
          分组名称
        </el-col>
        <el-col :span="12" class="title">
          人数
        </el-col>
      </el-row>

      <el-row class="t-data-row">
        <el-col :span="12">
          <div class="data-icon-text">
            <i class="el-icon-folder"></i>
            <div>
              无聊天好友
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <el-tag type="success" effect="dark">0</el-tag>
        </el-col>
      </el-row>
      <el-row class="t-data-row">
        <el-col :span="12">
          <div class="data-icon-text">
            <i class="el-icon-folder"></i>
            <div>
              无聊天好友
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <el-tag type="info" effect="dark">0</el-tag>
        </el-col>
      </el-row>
      <el-row class="t-data-row">
        <el-col :span="12">
          <div class="data-icon-text">
            <i class="el-icon-folder"></i>
            <div>
              无聊天好友
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <el-tag type="danger" effect="dark">0</el-tag>
        </el-col>
      </el-row>
      <el-row class="t-data-row">
        <el-col :span="12">
          <div class="data-icon-text">
            <i class="el-icon-folder"></i>
            <div>
              无聊天好友
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <el-tag type="warning" effect="dark">0</el-tag>
        </el-col>
      </el-row>
    </div>


      <div class="sidebar1">
        <div class="profile-section">
          <img src="~@/assets/img/WeChat19a93943eddee99b88a5f5409c51889b.jpg" alt="Profile Image" class="profile-image">
        </div>
        <!-- Menu Icons -->
        <div class="menu">
          <i :class="icon" v-for="icon in icons"></i>
        </div>
      </div>

    <div class="sidebar">
      <!-- Contact List -->
      <ul class="contact-list">
        <li :style="currentIndex === index ? 'background-color: lightblue;' : ''" v-for="(data,index) in dataList" :key="index" class="contact-item" @click="contactHandler(index)">
          <img :src="`https://profile.line-scdn.net` + data.picturePath" :alt="data.displayName" class="avatar">
          <div class="contact-details">
            <h4 class="contact-name">{{ data.displayName }}</h4>
            <p class="contact-message">{{ data.lastMessage }}</p>
          </div>
          <span class="contact-time">{{ data.createTime }}</span>
          <span class="contact-notification" v-if="data.unread">{{ data.unread }}</span>
        </li>
      </ul>
    </div>
    <div class="chat-window">
      <!-- Chat Header -->
      <div class="chat-header">
        <span class="back-arrow">←</span>
        <h2 class="chat-title">对话标题</h2>
        <span class="menu-dots">⋮</span>
      </div>

      <!-- Message Content -->
      <div class="message-content">
        <!-- Mock messages -->
        <div class="message-box received" v-for="message in messages" :key="message.id">
          <p class="message-text">{{ message.content }}</p>
          <span class="message-time">{{ message.time }}</span>
        </div>
        <!-- More messages here -->
      </div>

      <!-- Input Area -->
      <div class="input-area">
        <input type="text" v-model="newMessage" placeholder="输入消息内容" />
        <button @click="sendMessage">发送</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      dataList: [],
      totalPage: null,
      currentIndex: 1,
      icons: ['el-icon-chat-dot-round', 'el-icon-user-solid'],
      newMessage: '',
      messages: [
        // Mock data for messages
        { id: 1, content: '这是一条接收的消息', time: '16:55' },
        // Add more message objects here
      ],
      contacts: [
        // Sample data structure for contacts
        { id: 1, name: "BOSS", avatar: "boss.jpg", lastMessage: "明日报告", time: "17:17", unread: 1 },
        { id: 1, name: "BOSS", avatar: "boss.jpg", lastMessage: "明日报告", time: "17:17", unread: 1 },
        // More contacts...
      ]
    }
  },
  activated () {
    this.getDataList()
  },
  computed: {
    currentColor () {
      return
    }
  },
  methods: {
    contactHandler (i) {
      this.currentIndex = i
      this.$forceUpdate()
      console.log(this.currentIndex)
    },
    getDataList () {
      this.$http({
        url: this.$http.adornUrl('/ltt/atdatasubtask/listFriend'),
        method: 'get',
        params: this.$http.adornParams({
          'page': 1,
          'limit': 100
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
          console.log(this.dataList)
        } else {
          this.dataList = []
          this.totalPage = 0
        }
      })
    }
  }
}
</script>



<style scoped>

.group-container {
  width: 300px;
}

.t-data-row {
  line-height: 40px;
  font-size: 20px;
  color: black;

  padding: 10px;
  border-bottom: 1px solid #eaeaea;
}

.data-icon-text {
  display: flex;
  align-items: center;
}


.t-header-row {
  font-size: 20px;
  font-weight: bold;
  color: black;
  padding: 10px;
  border-bottom: 1px solid #eaeaea;
}

.app-conntainer {
  display: flex;
  align-items: stretch;
  flex: 1;
}
.sidebar {
  width: 300px;
  background: #ffffff;
  //overflow-y: scroll;
  display: flex;
  flex-direction: column;
  height: 80vh;
}

.search-add-container {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  background: #f2f2f2;
}

.search-add-container input[type="search"] {
  flex-grow: 1;
  border: none;
  padding: 8px;
  margin-right: 10px;
}

.add-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.contact-list {
  //list-style: none;
  padding: 0;
  margin: 0;
  //position: fixed;
  overflow-y: scroll;
  //flex: 1;
  //display: flex;
  //height: 100vh;
  //flex-direction: column;
}

.contact-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #eaeaea;
  overflow-y: scroll;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 10px;
}

.contact-details {
  flex-grow: 1;
}

.contact-name {
  font-size: 16px;
  margin-bottom: 5px;
}

.contact-message {
  font-size: 12px;
  color: #666;
}

.contact-time {
  font-size: 12px;
  color: #999;
  margin-left: auto;
  padding-left: 10px;
}

.contact-notification {
  background: #ff3b30;
  color: white;
  border-radius: 50%;
  padding: 0 6px;
  font-size: 12px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 10px;
}
</style>
<style scoped>
.chat-window {
  display: flex;
  flex: 1;
  flex-direction: column;
  //height: 100vh;
  padding-left: 10rpx;
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  background-color: #f2f2f2;
  border-bottom: 1px solid #e2e2e2;
}

.chat-title {
  font-size: 16px;
}

.message-content {
  flex-grow: 1;
  padding: 16px;
  overflow-y: auto;
}

.message-box {
  max-width: 60%;
  margin-bottom: 12px;
  padding: 8px;
  border-radius: 8px;
  line-height: 1.4;
}

.received {
  background-color: #ececec;
  align-self: flex-start;
}

.input-area {
  display: flex;
  padding: 16px;
  background-color: #f2f2f2;
  border-top: 1px solid #e2e2e2;
}

.input-area input {
  flex-grow: 1;
  border: none;
  padding: 8px;
  margin-right: 8px;
  border-radius: 18px;
  background-color: #fff;
}

.input-area button {
  border: none;
  background-color: #0084ff;
  color: #fff;
  padding: 8px 16px;
  border-radius: 18px;
  cursor: pointer;
}
</style>

<style scoped>
.sidebar1 {
  width: 80px;
  background-color: #ececec;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.status-indicators .status-dot {
  height: 15px;
  width: 15px;
  border-radius: 50%;
  margin-bottom: 10px;
}

.profile-section .profile-image {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin: 20px 0;
}

.menu {
  height: 400px;
  display: flex;
  flex-direction: column;
}
.menu i {
  font-size: 40px;
  margin: 20px 0 ;
}

.t-data-row {
  /* Existing styles... */
  transition: background-color 0.2s;
}

.t-data-row:after {
  /* Existing styles... */
  transition: transform 0.4s, background-color 0.2s;
}

/* Hover effect */
.t-data-row:hover {
  background-color: #a2a2a2; /* Color on hover for the switch background */
}

.t-data-row:hover:after {
  background-color: #f0f0f0; /* Color on hover for the switch circle */
}


.contact-item {
  /* Existing styles... */
  transition: background-color 0.2s;
}

.contact-item:after {
  /* Existing styles... */
  transition: transform 0.4s, background-color 0.2s;
}

/* Hover effect */
.contact-item:hover {
  background-color: #a2a2a2; /* Color on hover for the switch background */
}

.contact-item:hover:after {
  background-color: #f0f0f0; /* Color on hover for the switch circle */
}

</style>
