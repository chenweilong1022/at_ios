<template>
  <nav class="site-navbar" :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand" @click="$router.push({ name: 'home' })">
        <a class="site-navbar__brand-lg" href="javascript:;">ATIOS</a>
        <a class="site-navbar__brand-mini" href="javascript:;">ATIOS</a>
      </h1>
    </div>
    <div class="site-navbar__body clearfix">
      <el-menu
        class="site-navbar__menu"
        mode="horizontal">
        <el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
          <icon-svg name="zhedie"></icon-svg>
        </el-menu-item>
      </el-menu>
      <div class="port">
        账户余额:<div style="color: #17B3A3;display: inline;">{{ accountBalance }}</div>
        端口总数:<div style="color: #17B3A3;display: inline">{{ portNumTotal }}</div>
        端口剩余:<div style="color: #17B3A3;display: inline">{{ portNumSurplus }}</div>
        过期时间:<div style="color: #17B3A3;display: inline">{{ expireTime }}</div>
      </div>
      <el-menu
        class="site-navbar__menu site-navbar__menu--right leftC"
        mode="horizontal">
        <el-menu-item index="1" @click="$router.push({ name: 'theme' })">
          <template slot="title">
            <el-badge value="new">
              <icon-svg name="shezhi" class="el-icon-setting"></icon-svg>
            </el-badge>
          </template>
        </el-menu-item>
        <!--<el-menu-item index="2">-->
          <!--<el-badge value="hot">-->
            <!--<a href="//www.renren.io/" target="_blank">官方社区</a>-->
          <!--</el-badge>-->
        <!--</el-menu-item>-->
        <!--<el-submenu index="3">-->
          <!--<template slot="title">Git源码</template>-->
          <!--<el-menu-item index="2-1"><a href="//github.com/daxiongYang/renren-fast-vue" target="_blank">前端</a></el-menu-item>-->
          <!--<el-menu-item index="2-2"><a href="//git.oschina.net/renrenio/renren-fast" target="_blank">后台</a></el-menu-item>-->
          <!--<el-menu-item index="2-3"><a href="//git.oschina.net/renrenio/renren-generator" target="_blank">代码生成器</a></el-menu-item>-->
        <!--</el-submenu>-->
        <el-menu-item class="site-navbar__avatar" index="3">
          <el-dropdown :show-timeout="0" placement="bottom">
            <span class="el-dropdown-link">
              <img src="~@/assets/img/avatar.png" :alt="userName">{{ userName }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="updatePasswordHandle()">修改密码</el-dropdown-item>
              <el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </div>
    <!-- 弹窗, 修改密码 -->
    <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
  </nav>
</template>

<script>
  import UpdatePassword from './main-navbar-update-password'
  import { clearLoginInfo } from '@/utils'
  export default {
    data () {
      return {
        updatePassowrdVisible: false,
        portNumTotal: 0,
        expireTime: '无',
        portNumSurplus: 0,
        accountBalance: 0
      }
    },
    components: {
      UpdatePassword
    },
    computed: {
      navbarLayoutType: {
        get () { return this.$store.state.common.navbarLayoutType }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold },
        set (val) { this.$store.commit('common/updateSidebarFold', val) }
      },
      mainTabs: {
        get () { return this.$store.state.common.mainTabs },
        set (val) { this.$store.commit('common/updateMainTabs', val) }
      },
      userName: {
        get () { return this.$store.state.user.name }
      }
    },
    created () {
      this.portDataSummary()
      this.getAccountBalance()
    },
    methods: {
      // 修改密码
      updatePasswordHandle () {
        this.updatePassowrdVisible = true
        this.$nextTick(() => {
          this.$refs.updatePassowrd.init()
        })
      },
      // 退出
      logoutHandle () {
        this.$confirm(`确定进行[退出]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/logout'),
            method: 'post',
            data: this.$http.adornData()
          }).then(({data}) => {
            if (data && data.code === 0) {
              clearLoginInfo()
              this.$router.push({ name: 'login' })
            }
          })
        }).catch(() => {})
      },
      /**
       * 账户信息
       */
      getAccountBalance () {
        this.$http({
          url: this.$http.adornUrl(`/ltt/accountbalance/getBySysUserId`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.accountBalance = data.balance.balance
          }
        })
      },
      /**
       * 端口数据汇总
       */
      portDataSummary () {
        this.$http({
          url: this.$http.adornUrl(`/ltt/atuserport/portDataSummary`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.portNumTotal = data.portData.portNumTotal
            this.expireTime = data.portData.expireTime
            this.portNumSurplus = data.portData.portNumSurplus
          }
        })
      }
    }
  }
</script>

<style scoped>
.port {
  flex: 1;
  text-align: right;
  margin: auto;
  font-size: large;
}
.site-navbar__body {
  display: flex;
  flex-direction: row;
  align-content: center;
}
</style>
