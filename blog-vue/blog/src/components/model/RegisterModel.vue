<template>
  <v-dialog v-model="registerDialog" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius: 4px">
      <v-icon class="float-right" @click="registerDialog = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <!-- 用户名 -->
        <v-text-field
          v-model="username"
          label="邮箱"
          placeholder="请输入您的邮箱"
          clearable
          @keyup.enter="register"
        ></v-text-field>
        <!-- 验证码 -->
        <div class="mt-7 send-wrapper">
          <v-text-field
            maxlength="6"
            v-model="code"
            label="验证码"
            placeholder="请输入6位验证码"
            @keyup.enter="register"
          ></v-text-field>
          <v-btn text small :disabled="flag" @click="sendCode">
            {{ codeMsg }}
          </v-btn>
        </div>
        <!-- 密码 -->
        <v-text-field
          v-model="password"
          class="mt-7"
          label="密码"
          placeholder="请输入您的密码"
          @keyup.enter="register"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show ? 'text' : 'password'"
          @click:append="show = !show"
        ></v-text-field>
        <!-- 注册按钮 -->
        <v-btn
          class="mt-7"
          block
          color="#673AB7"
          style="color: #fff"
          @click="register"
        >
          注册
        </v-btn>
        <!-- 登录 -->
        <div class="mt-10 login-tip">
          已有账号?<span @click="openLogin">登录</span>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import { sendCode, register } from '@/api/login'

export default {
  data () {
    return {
      username: '',
      code: '',
      password: '',
      flag: true,
      codeMsg: '发送',
      time: 60,
      show: false
    }
  },

  computed: {
    registerDialog: {
      set (value) {
        this.$store.state.registerDialog = value
      },
      get () {
        return this.$store.state.registerDialog
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

  watch: {
    username (value) {
      const reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)+$/
      if (reg.test(value)) {
        this.flag = false
      } else {
        this.flag = true
      }
    }
  },

  methods: {
    openLogin () {
      this.$store.state.registerDialog = false
      this.$store.state.loginDialog = true
    },
    sendCode () {
      const that = this
      // eslint-disable-next-line no-undef
      var captcha = new TencentCaptcha(this.config.TENCENT_CAPTCHA, function (
        res
      ) {
        if (res.ret === 0) {
          // 发送邮件
          that.countDown()
          sendCode(that.username).then(({ data }) => {
            if (data.status) {
              that.$store.dispatch('snackbar/openSnackbar', {
                type: 'success',
                message: '验证码发送成功'
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
    },
    countDown () {
      this.flag = true
      this.timer = setInterval(() => {
        this.time--
        this.codeMsg = this.time + 's'
        if (this.time <= 0) {
          clearInterval(this.timer)
          this.codeMsg = '发送'
          this.time = 60
          this.flag = false
        }
      }, 1000)
    },
    register () {
      const reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)+$/
      if (!reg.test(this.username)) {
        this.$store.dispatch('snackbar/openSnackbar', {
          type: 'error',
          message: '邮箱格式不正确'
        })
        return false
      }
      if (this.code.trim().length !== 6) {
        this.$store.dispatch('snackbar/openSnackbar', {
          type: 'error',
          message: '请输入6位验证码'
        })
        return false
      }
      if (this.password.trim().length < 6) {
        this.$store.dispatch('snackbar/openSnackbar', {
          type: 'error',
          message: '密码不能少于6位'
        })
        return false
      }
      const user = {
        username: this.username,
        password: this.password,
        code: this.code
      }
      register(user).then(({ data }) => {
        if (data.status) {
          this.username = ''
          this.password = ''
          this.openLogin()
          this.$store.dispatch('snackbar/openSnackbar', {
            type: 'success',
            message: '注册成功'
          })
        } else {
          this.$store.dispatch('snackbar/openSnackbar', {
            type: 'error',
            message: data.message
          })
        }
      })
    }
  }
}
</script>
