import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: resolve => require(['../views/home/Home.vue'], resolve),
    meta: {
      title: 'Morri的个人博客'
    }
  },
  {
    path: '/article/:articleId',
    component: resolve => require(['../views/article/Article.vue'], resolve)
  },
  {
    path: '/archive',
    component: resolve => require(['../views/archive/Archive.vue'], resolve),
    meta: {
      title: '归档'
    }
  },
  {
    path: '/category',
    component: resolve => require(['../views/category/Category.vue'], resolve),
    meta: {
      title: '分类'
    }
  },
  {
    path: '/category/:categoryId',
    component: resolve => require(['../views/article/ArticleList.vue'], resolve)
  },
  {
    path: '/tag',
    component: resolve => require(['../views/tag/Tag.vue'], resolve),
    meta: {
      title: '标签'
    }
  },
  {
    path: '/tag/:tagId',
    component: resolve => require(['../views/article/ArticleList.vue'], resolve)
  },
  {
    path: '/about',
    component: resolve => require(['../views/about/About.vue'], resolve),
    meta: {
      title: '关于我'
    }
  },
  {
    path: '/message',
    component: resolve => require(['../views/message/Message.vue'], resolve),
    meta: {
      title: '留言'
    }
  },
  {
    path: '/user',
    component: resolve => require(['../views/user/UserInfo.vue'], resolve),
    meta: {
      title: '个人中心'
    }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
