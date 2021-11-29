import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import snackbar from '@/store/modules/snackbar'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    searchDialog: false,
    loginDialog: false,
    registerDialog: false,
    forgetDialog: false,
    emailDialog: false,
    drawer: false,
    userId: null,
    avatar: null,
    email: null,
    nickname: null,
    intro: null,
    webSite: null,
    articleLikeSet: [],
    commentLikeSet: [],
    blogInfo: {
      pageList: [],
      websiteConfig: {
        socialUrlList: []
      }
    }
  },
  mutations: {
    login (state, user) {
      state.userId = user.id
      state.avatar = user.avatar
      state.email = user.email
      state.nickname = user.nickname
      state.intro = user.intro
      state.webSite = user.webSite
      state.articleLikeSet = user.articleLikeSet ? user.articleLikeSet : []
      state.commentLikeSet = user.commentLikeSet ? user.commentLikeSet : []
    },
    logout (state) {
      state.userId = null
      state.avatar = null
      state.email = null
      state.nickname = null
      state.intro = null
      state.webSite = null
      state.articleLikeSet = []
      state.commentLikeSet = []
    },
    closeModel (state) {
      state.searchDialog = false
      state.loginDialog = false
      state.registerDialog = false
      state.forgetDialog = false
      state.emailDialog = false
    },
    saveEmail (state, email) {
      state.email = email
    },
    updateUserInfo (state, user) {
      state.nickname = user.nickname
      state.webSite = user.webSite
      state.intro = user.intro
    },
    savePageInfo (state, pageList) {
      state.pageList = pageList
    },
    updateAvatar (state, avatar) {
      state.avatar = avatar
    },
    checkBlogInfo (state, blogInfo) {
      state.blogInfo = blogInfo
      state.blogInfo.pageList = blogInfo.pageList
      state.blogInfo.websiteConfig = blogInfo.websiteConfig
      state.blogInfo.websiteConfig.socialUrlList =
        blogInfo.websiteConfig.socialUrlList
    },
    articleLikeSet (state, articleId) {
      const articleLikeSet = state.articleLikeSet
      if (articleLikeSet.indexOf(articleId) !== -1) {
        articleLikeSet.splice(articleLikeSet.indexOf(articleId), 1)
      } else {
        articleLikeSet.push(articleId)
      }
    },
    commentLikeSet (state, commentId) {
      const commentLikeSet = state.commentLikeSet
      if (commentLikeSet.indexOf(commentId) !== -1) {
        commentLikeSet.splice(commentLikeSet.indexOf(commentId), 1)
      } else {
        commentLikeSet.push(commentId)
      }
    }
  },
  actions: {},
  modules: {
    snackbar
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
})
