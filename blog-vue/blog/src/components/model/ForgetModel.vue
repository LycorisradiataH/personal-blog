<template>
  <v-dialog v-model="forgetDialog" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius: 4px">
      <v-icon class="float-right" @click="forgetDialog = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <!-- 用户名 -->
        <v-text-field
          v-model="username"
          label="邮箱"
          placeholder="请输入您的邮箱"
          clearable
          @keyup.enter="forget"
        ></v-text-field>
        <!-- 验证码 -->
        <div class="mt-7 send-wrapper">
          <v-text-field
            maxlength="6"
            v-model="code"
            label="验证码"
            placeholder="请输入6位验证码"
            @keyup.enter="forget"
          ></v-text-field>
          <v-btn :disabled="flag" text small @click="sendCode">
            {{ codeMsg }}
          </v-btn>
        </div>
        <!-- 密码 -->
        <v-text-field
          v-model="password"
          class="mt-7"
          label="密码"
          placeholder="请输入您的密码"
          @keyup.enter="forget"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show ? 'text' : 'password'"
          @click:append="show = !show"
        ></v-text-field>
        <!-- 按钮 -->
        <v-btn
          class="mt-7"
          block
          color="green"
          style="color: #fff"
          @click="forget"
        >
          确定
        </v-btn>
        <!-- 登录 -->
        <div class="mt-10 login-tip">
          已有帐号?<span @click="openLogin">登录</span>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import { sendCode, forget } from '@/api/login'

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
    forgetDialog: {
      set (value) {
        this.$store.state.forgetDialog = value
      },
      get () {
        return this.$store.state.forgetDialog
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
      this.$store.state.forgetDialog = false
      this.$store.state.loginDialog = true
    },
    sendCode () {
      const that = this
      // eslint-disable-next-line no-undef
      var captcha = new TencentCaptcha(this.config.TENCENT_CAPTCHA, function (
        res
      ) {
        if (res.ret === 0) {
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
                message: '验证码发送失败'
              })
            }
          })
        }
      })
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
    forget () {
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
          message: '请输入6位的验证码'
        })
        return false
      }
      if (this.password.trim() < 6) {
        this.$store.dispatch('snackbar/openSnackbar', {
          type: 'error',
          message: '密码不能小于6位数'
        })
        return false
      }
      const user = {
        username: this.username,
        password: this.password,
        code: this.code
      }
      forget(user).then(({ data }) => {
        if (data.status) {
          this.$store.dispatch('snackbar/openSnackbar', {
            type: 'success',
            message: '修改密码成功'
          })
          this.openLogin()
        } else {
          this.$store.dispatch('snackbar/openSnackbar', {
            type: 'error',
            message: '修改密码失败'
          })
        }
      })
    }
  }
}
</script>
