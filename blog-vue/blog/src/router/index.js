import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/home/Home'
import Article from '@/views/article/Article'
import Archive from '@/views/archive/Archive'
import Category from '@/views/category/Category'
import Tag from '@/views/tag/Tag'
import About from '@/views/about/About'
import Message from '@/views/message/Message'
import ArticleList from '@/views/article/ArticleList'
import UserInfo from '@/views/user/UserInfo'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: 'Hua的个人博客'
    }
  },
  {
    path: '/article/:articleId',
    component: Article
  },
  {
    path: '/archive',
    name: 'Archive',
    component: Archive,
    meta: {
      title: '归档'
    }
  },
  {
    path: '/category',
    name: 'Category',
    component: Category,
    meta: {
      title: '分类'
    }
  },
  {
    path: '/category/:categoryId',
    component: ArticleList
  },
  {
    path: '/tag',
    name: 'Tag',
    component: Tag,
    meta: {
      title: '标签'
    }
  },
  {
    path: '/tag/:tagId',
    component: ArticleList
  },
  {
    path: '/about',
    name: 'About',
    component: About,
    meta: {
      title: '关于我'
    }
  },
  {
    path: '/message',
    name: 'Message',
    component: Message,
    meta: {
      title: '留言'
    }
  },
  {
    path: '/user',
    name: 'UserInfo',
    component: UserInfo,
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
