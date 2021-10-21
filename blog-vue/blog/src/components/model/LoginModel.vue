<template>
  <v-dialog v-model="loginDialog" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius: 4px">
      <v-icon class="float-right" @click="loginDialog = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <!-- 用户名 -->
        <v-text-field
          v-model="username"
          label="邮箱"
          placeholder="请输入您的邮箱"
          clearable
          @keyup.enter="login"
        ></v-text-field>
        <!-- 密码 -->
        <v-text-field
          v-model="password"
          class="mt-7"
          label="密码"
          placeholder="请输入您的密码"
          @keyup.enter="login"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show ? 'text' : 'password'"
          @click:append="show = !show"
        ></v-text-field>
        <!-- 登录按钮 -->
        <v-btn
          class="mt-7"
          block
          color="blue"
          style="color: #fff"
          @click="login"
        >
          登录
        </v-btn>
        <!-- 注册和找回密码按钮 -->
        <div class="mt-10 login-tip">
          <span @click="openRegister">立即注册</span>
          <span @click="openForget" class="float-right">忘记密码?</span>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import { login } from '@/api/login'

export default {
  data () {
    return {
      username: '',
      password: '',
      show: false
    }
  },

  computed: {
    loginDialog: {
      set (value) {
        this.$store.state.loginDialog = value
      },
      get () {
        return this.$store.state.loginDialog
      }
    },
    isMobile () {
      const clientWidth = document.documentElement.clientWidth
      if (clientWidth > 960) {
        return false
      }
      return true
    }
  },

  methods: {
    openRegister () {
      this.$store.state.loginDialog = false
      this.$store.state.registerDialog = true
    },
    openForget () {
      this.$store.state.loginDialog = false
      this.$store.state.forgetDialog = true
    },
    login () {
      const reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)+$/
      if (!reg.test(this.username)) {
        this.$store.dispatch('snackbar/openSnackbar', {
          type: 'error',
          message: '邮箱格式不正确'
        })
        return false
      }
      if (this.password.trim().length === 0) {
        this.$store.dispatch('snackbar/openSnackbar', {
          type: 'warning',
          message: '密码不能为空'
        })
        return false
      }
      const that = this
      const user = {
        username: this.username,
        password: this.password
      }
      // eslint-disable-next-line no-undef
      var captcha = new TencentCaptcha(this.config.TENCENT_CAPTCHA, function (
        res
      ) {
        if (res.ret === 0) {
          // 发送登录请求
          login(user).then(({ data }) => {
            if (data.status) {
              that.username = ''
              that.password = ''
              that.$store.commit('login', data.data)
              that.$store.commit('closeModel')
              that.$store.dispatch('snackbar/openSnackbar', {
                type: 'success',
                message: '登录成功'
              })
            } else {
              that.$store.dispatch('snackbar/openSnackbar', {
                type: 'error',
                message: data.message
              })
            }
          })
        }
      })
      // 显示验证码
      captcha.show()
    }
  }
}
</script>

<style scoped>
.social-login-title {
  margin-top: 1.5rem;
  color: #b5b5b5;
  font-size: 0.75rem;
  text-align: center;
}
.social-login-title::before {
  content: '';
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}
.social-login-title::after {
  content: '';
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}
.social-login-wrapper {
  margin-top: 1rem;
  font-size: 2rem;
  text-align: center;
}
.social-login-wrapper a {
  text-decoration: none;
}
</style>
