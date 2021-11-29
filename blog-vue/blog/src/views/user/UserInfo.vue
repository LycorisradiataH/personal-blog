<template>
  <div class="user-info">
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">个人中心</h1>
    </div>
    <!-- 个人信息 -->
    <v-card class="blog-container">
      <div>
        <span class="info-title">基本信息</span>
      </div>
      <v-row class="info-wrapper">
        <v-col md="3" cols="12">
          <!-- 头像 -->
          <button id="pick-avatar">
            <v-avatar size="140">
              <img :src="this.$store.state.avatar" />
            </v-avatar>
          </button>
          <avatar-cropper
            @uploaded="uploadAvatar"
            trigger="#pick-avatar"
            upload-url="/api/user/avatar"
          />
        </v-col>
        <v-col md="7" cols="12">
          <!-- 昵称 -->
          <v-text-field
            v-model="userInfo.nickname"
            label="昵称"
            placeholder="请输入您的昵称"
          ></v-text-field>
          <!-- 个人网站 -->
          <v-text-field
            v-model="userInfo.webSite"
            label="个人网站"
            class="mt-7"
            placeholder="请输入你的网站"
          ></v-text-field>
          <!-- 个人简介 -->
          <v-text-field
            v-model="userInfo.intro"
            class="mt-7"
            label="个人简介"
            placeholder="介绍下自己吧"
          ></v-text-field>
          <!-- 邮箱 -->
          <div class="mt-7 binding">
            <v-text-field
              disabled
              v-model="userInfo.email"
              label="邮箱"
              placeholder="请绑定邮箱"
            ></v-text-field>
            <v-btn text small @click="openEmailModel">
              修改绑定
            </v-btn>
          </div>
          <v-btn @click="updateUserInfo" outlined class="mt-5">修改</v-btn>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
import AvatarCropper from 'vue-avatar-cropper'
import { updateUserInfo } from '@/api/user'

export default {
  components: {
    AvatarCropper
  },

  data () {
    return {
      userInfo: {
        nickname: this.$store.state.nickname,
        intro: this.$store.state.intro,
        webSite: this.$store.state.webSite,
        email: this.$store.state.email
      }
    }
  },

  computed: {
    email () {
      return this.$store.state.email
    },
    cover () {
      var cover = ''
      this.$store.state.blogInfo.pageList.forEach(item => {
        if (item.pageLabel === 'user') {
          cover = item.pageCover
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    }
  },

  methods: {
    updateUserInfo () {
      updateUserInfo(this.userInfo).then(({ data }) => {
        if (data.status) {
          this.$store.commit('updateUserInfo', this.userInfo)
          this.$store.dispatch('snackbar/openSnackbar', {
            type: 'success',
            message: '修改信息成功'
          })
        } else {
          this.$store.dispatch('snackbar/openSnackbar', {
            type: 'error',
            message: data.message
          })
        }
      })
    },
    uploadAvatar (res) {
      if (res.status) {
        this.$store.commit('updateAvatar', res.data)
        this.$store.dispatch('snackbar/openSnackbar', {
          type: 'success',
          message: '修改头像成功'
        })
      } else {
        this.$store.dispatch('snackbar/openSnackbar', {
          type: 'error',
          message: res.message
        })
      }
    },
    openEmailModel () {
      this.$store.state.emailDialog = true
    }
  }
}
</script>

<style scoped>
.info-title {
  font-size: 1.25rem;
  font-weight: bold;
}
.info-wrapper {
  margin-top: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
#pick-avatar {
  outline: none;
}
.binding {
  display: flex;
  align-items: center;
}
</style>
