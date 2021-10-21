<template>
  <v-dialog v-model="emailDialog" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius: 4px">
      <v-icon class="float-right" @click="emailDialog = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <!-- 邮箱 -->
        <v-text-field
          v-model="email"
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
        <!-- 绑定按钮 -->
        <v-btn
          class="mt-7"
          block
          color="blue"
          style="color: #fff"
          @click="updateEmail"
        >
          绑定
        </v-btn>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import { sendCode, logout } from '@/api/login'
import { updateEmail } from '@/api/user'

export default {
  data () {
    return {
      email: this.$store.state.email,
      code: '',
      flag: true,
      codeMsg: '发送',
      time: 60,
      show: false
    }
  },

  computed: {
    emailDialog: {
      set (value) {
        this.$store.state.emailDialog = value
      },
      get () {
        return this.$store.state.emailDialog
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
    email (value) {
      const reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[A-A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)+$/
      if (reg.test(value)) {
        this.flag = false
      } else {
        this.flag = true
      }
    }
  },

  methods: {
    sendCode () {
      const that = this
      // eslint-disable-next-line no-undef
      const captcha = new TencentCaptcha(this.config.TENCENT_CAPTCHA, function (
        res
      ) {
        if (res.ret === 0) {
          // 发送邮件
          that.countDown()
          sendCode(that.email).then(({ data }) => {
            if (data.status) {
              that.$toast({ type: 'success', message: data.message })
            } else {
              that.$toast({ type: 'error', message: data.message })
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
    updateEmail () {
      const reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)+$/
      if (!reg.test(this.email)) {
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
      const user = {
        email: this.email,
        code: this.code
      }
      updateEmail(user).then(({ data }) => {
        if (data.status) {
          this.$store.commit('saveEmail', this.email)
          this.email = ''
          this.code = ''
          if (this.$route.path === '/user') {
            this.$router.go(-1)
          }
          this.$store.dispatch('snackbar/openSnackbar', {
            type: 'success',
            message: data.message
          })
          this.$store.commit('closeModel')
          logout().then(result => {
            if (result.data.status) {
              this.email = ''
              this.code = ''
              this.$store.commit('logout')
              this.$store.state.loginDialog = true
            }
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

<style scoped>
@media (min-width: 760px) {
  .login-container {
    padding: 1rem;
    border-radius: 4px;
    height: 400px;
  }
}
</style>
