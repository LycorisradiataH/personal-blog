<template>
  <v-app id="app">
    <!-- 消息提示 -->
    <snackbar></snackbar>
    <!-- 导航栏 -->
    <top-nav-bar></top-nav-bar>
    <!-- 侧边栏 -->
    <side-nav-bar></side-nav-bar>
    <!-- 内容 -->
    <v-main>
      <router-view :key="$route.fullPath"></router-view>
    </v-main>
    <!-- 页脚 -->
    <my-footer></my-footer>
    <!-- 返回顶部 -->
    <back-top></back-top>
    <!-- 搜索框 -->
    <search-model></search-model>
    <!-- 登录框 -->
    <login-model></login-model>
    <!-- 注册框 -->
    <register-model></register-model>
    <!-- 忘记密码框 -->
    <forget-model></forget-model>
    <!-- 修改邮箱框 -->
    <email-model></email-model>
  </v-app>
</template>

<script>
import Snackbar from '@/components/snackbar/Snackbar.vue'
import TopNavBar from '@/components/layout/TopNavBar'
import SideNavBar from '@/components/layout/SideNavBar'
import MyFooter from '@/components/layout/Footer'
import BackTop from '@/components/backtop/BackTop.vue'
import SearchModel from '@/components/model/SearchModel.vue'
import LoginModel from '@/components/model/LoginModel.vue'
import RegisterModel from '@/components/model/RegisterModel.vue'
import ForgetModel from '@/components/model/ForgetModel.vue'
import EmailModel from '@/components/model/EmailModel.vue'
import { getBlogInfo } from '@/api/blog'

export default {
  created () {
    // 上传访客信息
    this.axios.post('/api/report')
    // 获取博客信息
    this.getBlogInfo()
  },
  components: {
    Snackbar,
    TopNavBar,
    SideNavBar,
    MyFooter,
    BackTop,
    SearchModel,
    LoginModel,
    RegisterModel,
    ForgetModel,
    EmailModel
  },
  methods: {
    getBlogInfo () {
      getBlogInfo().then(({ data }) => {
        if (data.status) {
          this.$store.commit('checkBlogInfo', data.data)
        }
      })
    }
  },
  computed: {
    blogInfo () {
      return this.$store.state.blogInfo
    },
    isMobile () {
      const flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      )
      return flag
    }
  }
}
</script>
